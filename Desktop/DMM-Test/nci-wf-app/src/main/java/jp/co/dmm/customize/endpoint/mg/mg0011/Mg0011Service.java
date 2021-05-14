package jp.co.dmm.customize.endpoint.mg.mg0011;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import jp.co.dmm.customize.jpa.entity.mw.ItmImgMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 品目マスタ設定画面サービス
 */
@ApplicationScoped
public class Mg0011Service extends BasePagingService {

	@Inject private Mg0011Repository repository;

	/**
	 * 登録画面初期化
	 * @param req
	 * @return
	 */
	public Mg0011GetResponse init(Mg0011GetRequest req) {

		// レスポンス作成
		// 対象の取引先マスタ取得
		Mg0011GetResponse res = get(req);

		// 選択肢設定
		String targetCompanyCd = req.companyCd;

		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		if (StringUtils.isEmpty(res.entity.companyCd)) {
			res.entity.companyCd = targetCompanyCd;
		}

		// 在庫区分選択肢
		res.stckTps = repository.getSelectItems(targetCompanyCd, "KbnStock", false);

		// 調達部門選択肢
		res.prcFldTps = repository.getSelectItems(targetCompanyCd, "PrcFldTps", false);

		// 単位コード選択肢
		res.untCds = repository.getSelectItems(targetCompanyCd, "KbnUnit", true);

		// 削除フラグ選択肢
		res.dltFgNm = repository.getSelectItems(targetCompanyCd, "dltFg", false);

		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());

		return res;
	}

	/**
	 * 品目マスタ取得
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mg0011GetResponse get(Mg0011GetRequest req) {

		final Mg0011GetResponse res = createResponse(Mg0011GetResponse.class, req);
		res.success = true;

		// 対象の品目マスタ取得
		res.entity =  repository.get(req);
		return res;
	}

	/**
	 * 更新処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0011UpdateResponse update(Mg0011UpdateRequest req) {

		//更新
		repository.update(req, sessionHolder.getWfUserRole());

		// レスポンス作成
		Mg0011UpdateResponse res = createResponse(Mg0011UpdateResponse.class, req);
		res.success = true;

		return res;
	}

	/**
	 * 新規登録処理
	 * @param req 登録リクエスト
	 * @return 登録結果レスポンス
	 */
	public Mg0011UpdateResponse insert(Mg0011UpdateRequest req) {

		//登録
		String sqno = String.valueOf(repository.insert(req, sessionHolder.getWfUserRole()));

		// レスポンス作成
		Mg0011UpdateResponse res = createResponse(Mg0011UpdateResponse.class, req);

		// 採番された連番を取得
		Mg0011Entity entity = new Mg0011Entity();
		entity.companyCd = req.companyCd;
		entity.orgnzCd = req.orgnzCd;
		entity.itmCd = req.itmCd;
		entity.sqno = sqno;

		res.entity = entity;
		res.success = true;
		return res;
	}

	/**
	 * 品目マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean insertCheck(Mg0011UpdateRequest req) {
		return repository.getMaxSqno(req, false) > 0;
	}

	/**
	 * 品目マスタチェック
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public boolean updateCheck(Mg0011UpdateRequest req) {
		return repository.getMaxSqno(req, true) > 0;
	}

	@Transactional
	public Mg0011UploadResponse upload(List<FormDataBodyPart> bodyParts) {
		Mg0011UploadResponse res = null;
		int count = 0;

		for (BodyPart bodyPart : bodyParts) {
			FormDataContentDisposition cd = (FormDataContentDisposition)bodyPart.getContentDisposition();
			if (eq("file", cd.getName())) {
				final UploadFile f = new UploadFile(bodyPart);
				final byte[] fileData = toBytes(f.stream);
				count++;

				// 最大ファイル数
				if (count > 1)
					throw new InvalidUserInputException(MessageCd.MSG0155, 1);

				final ItmImgMst file = new ItmImgMst();
				file.setDltFg(DeleteFlag.ON);	// まだ仮登録なので論理削除として登録する
				file.setFileData(fileData);
				file.setFileNm(f.fileName);
				res = new Mg0011UploadResponse(repository.insertItmImg(file, sessionHolder.getWfUserRole()));
			}
		}
		return res;
	}

	/**
	 *
	 * @param itmImgId 生成された画像ID
	 * @return
	 */
	public ItmImgMst getItemImage(Long itmImgId) {
		return repository.getItmImgByPK(itmImgId, sessionHolder.getWfUserRole());
	}
}
