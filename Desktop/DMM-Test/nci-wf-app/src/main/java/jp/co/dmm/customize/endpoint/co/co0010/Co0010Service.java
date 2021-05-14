package jp.co.dmm.customize.endpoint.co.co0010;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 契約一覧サービス
 */
@ApplicationScoped
public class Co0010Service extends BasePagingService {

	@Inject private Co0010Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Co0010SearchResponse init(Co0010SearchRequest req) {
		final Co0010SearchResponse res = createResponse(Co0010SearchResponse.class, req);
		res.companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		// 依頼種別
		res.cntrctshtFrmts = new ArrayList<OptionItem>();
		res.cntrctshtFrmts.add(OptionItem.EMPTY);
		res.cntrctshtFrmts.add(new OptionItem("1", "法務依頼 有（レビュー）"));
		res.cntrctshtFrmts.add(new OptionItem("2", "法務依頼 有（新規作成）"));
		res.cntrctshtFrmts.add(new OptionItem("3", "法務依頼 無（ひな型利用）"));

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Co0010SearchResponse search(Co0010SearchRequest req) {
		req.companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		final int allCount = repository.count(req);
		final Co0010SearchResponse res = createResponse(Co0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		res.success = true;
		return res;
	}

	/**
	 * 変更申請バリデーション
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public Co0010GetScreenProcessIdResponse validate(Co0010GetScreenProcessIdRequest req) {
		final Co0010GetScreenProcessIdResponse res = createResponse(Co0010GetScreenProcessIdResponse.class, req);

		int countCntrct = repository.countCntrct(req);
		if (countCntrct > 0) {
			res.success = false;
			throw new InvalidUserInputException("会社コード[" + req.companyCd + "]、契約No[" + req.cntrctNo + "]はまだ変更申請中です。");
		}

		res.screenProcessId = repository.getScreenProcessId(req.companyCd);
		res.success = true;
		return res;
	}
}
