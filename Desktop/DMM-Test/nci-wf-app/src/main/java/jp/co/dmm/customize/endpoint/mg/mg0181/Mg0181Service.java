package jp.co.dmm.customize.endpoint.mg.mg0181;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 社内レートマスタ登録Service
 */
@BizLogic
public class Mg0181Service extends BaseService {

	@Inject private Mg0181Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0181Response init(Mg0181Request req) {
		Mg0181Response res = get(req);

		//選択肢設定
		String targetCompanyCd = req.companyCd;
		if (StringUtils.isEmpty(targetCompanyCd)) {
			targetCompanyCd = sessionHolder.getWfUserRole().getCorporationCode();
		}
		//削除フラグ選択肢
		res.dltFg = repository.getSelectItems(targetCompanyCd, "dltFg", false);
		// 会社選択
		res.companyItems = repository.getCompanyItems(sessionHolder.getLoginInfo().getUserAddedInfo(), sessionHolder.getLoginInfo().getLocaleCode());
		// 通貨コード
		res.mnyCds = repository.getMnyCdItems(targetCompanyCd);
		return res;
	}

	/**
	 * 社内レートマスタ取得
	 * @param req
	 * @return
	 */
	public Mg0181Response get(Mg0181Request req) {
		final Mg0181Response res = createResponse(Mg0181Response.class, req);
		res.success = true;

		//対象の社内レートマスタ取得
		res.entity = repository.get(req);

		return res;
	}

	/**
	 * 社内レートマスタ存在チェック（登録用）
	 * @param req
	 * @return
	 */
	public boolean insertCheck(Mg0181Request req) {
		return repository.getMaxSqno(req, false) > 0;
	}

	/**
	 * 社内レートマスタ存在チェック（更新用）
	 * @param req
	 * @return
	 */
	public boolean updateCheck(Mg0181Request req) {
		return repository.getMaxSqno(req, true) > 0;
	}

	/**
	 * 社内レートマスタ登録
	 * @param req
	 * @return
	 */
	public Mg0181Response insert(Mg0181Request req) {
		final Mg0181Response res = createResponse(Mg0181Response.class, req);
		String companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		String userCode = sessionHolder.getWfUserRole().getUserCode();
		String ipAddress = sessionHolder.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		/** 連番を初期化 */
		req.inputs.sqno = 1;
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
		req.mnyCd = req.inputs.mnyCd;
		req.sqno = req.inputs.sqno;
		res.entity = repository.get(req);
		res.success = true;
		return res;
	}

	/**
	 * 社内レートマスタ更新
	 * @param req
	 * @return
	 */
	public Mg0181Response update(Mg0181Request req) {
		final Mg0181Response res = createResponse(Mg0181Response.class, req);
		String companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		String userCode = sessionHolder.getWfUserRole().getUserCode();
		String ipAddress = sessionHolder.getWfUserRole().getIpAddress();

		/** 更新者情報を設定 */
		req.inputs.corporationCodeUpdated = companyCd;
		req.inputs.userCodeUpdated = userCode;
		req.inputs.ipUpdated = ipAddress;

		/** 社内レートマスタ更新 */
		long sqno = repository.update(req.inputs, sessionHolder.getWfUserRole());

		req.companyCd = req.inputs.companyCd;
		req.mnyCd = req.inputs.mnyCd;
		req.sqno = sqno;
		res.entity = repository.get(req);
		res.success = true;
		return res;
	}
}
