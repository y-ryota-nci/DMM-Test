package jp.co.dmm.customize.endpoint.mg.mg0211;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 源泉税区分マスタ登録Service
 */
@BizLogic
public class Mg0211Service extends BaseService {

	@Inject private Mg0211Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0211Response init(Mg0211Request req) {
		Mg0211Response res = get(req);

		//選択肢設定
		String targetCompanyCd = req.companyCd;
		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		//削除フラグ選択肢
		res.dltFg = repository.getSelectItems(targetCompanyCd, "dltFg", false);
		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());

		return res;
	}

	/**
	 * 源泉税区分マスタ取得
	 * @param req
	 * @return
	 */
	public Mg0211Response get(Mg0211Request req) {
		final Mg0211Response res = createResponse(Mg0211Response.class, req);
		res.success = true;

		//対象の社内レートマスタ取得
		res.entity = repository.get(req);

		return res;
	}

	/**
	 * 社内レートマスタ存在チェック
	 * @param req
	 * @return
	 */
	public boolean insertCheck(Mg0211Request req) {
		// 対象の社内レートマスタ取得
		final Mg0211Response res = get(req);
		return isNotEmpty(res.entity.companyCd);
	}

	/**
	 * 社内レートマスタ登録
	 * @param req
	 * @return
	 */
	public Mg0211Response insert(Mg0211Request req) {
		final Mg0211Response res = createResponse(Mg0211Response.class, req);
		String companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		String userCode = sessionHolder.getWfUserRole().getUserCode();
		String ipAddress = sessionHolder.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		/** 作成者／更新者情報を設定 */
		req.inputs.corporationCodeCreated = companyCd;
		req.inputs.userCodeCreated = userCode;
		req.inputs.ipCreated = ipAddress;
		req.inputs.timestampCreated = now;
		req.inputs.corporationCodeUpdated = companyCd;
		req.inputs.userCodeUpdated = userCode;
		req.inputs.ipUpdated = ipAddress;
		req.inputs.timestampUpdated = now;

		/** 社内レートマスタ登録 */
		repository.insert(req.inputs);

		req.companyCd = req.inputs.companyCd;
		req.hldtaxTp = req.inputs.hldtaxTp;
		res.entity = repository.get(req);
		res.success = true;
		return res;
	}

	/**
	 * 社内レートマスタ更新
	 * @param req
	 * @return
	 */
	public Mg0211Response update(Mg0211Request req) {
		final Mg0211Response res = createResponse(Mg0211Response.class, req);
		String companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		String userCode = sessionHolder.getWfUserRole().getUserCode();
		String ipAddress = sessionHolder.getWfUserRole().getIpAddress();

		/** 更新者情報を設定 */
		req.inputs.corporationCodeUpdated = companyCd;
		req.inputs.userCodeUpdated = userCode;
		req.inputs.ipUpdated = ipAddress;

		/** 社内レートマスタ更新 */
		repository.update(req.inputs);

		req.companyCd = req.inputs.companyCd;
		req.hldtaxTp = req.inputs.hldtaxTp;
		res.entity = repository.get(req);
		res.success = true;
		return res;
	}

}
