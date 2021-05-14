package jp.co.nci.iwf.endpoint.vd.vd0051;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascript;

/**
 * Javascript設定のサービス
 */
@BizLogic
public class Vd0051Service extends BaseService {
	@Inject
	private Vd0051Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0051InitResponse init(Vd0051InitRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final Vd0051InitResponse res = createResponse(Vd0051InitResponse.class, req);
		if (req.javascriptHistoryId == null) {
			res.entity = new Vd0051Entity();
			res.entity.corporationCode = login.getCorporationCode();
			res.entity.historyNo = 1;
		}
		else {
			res.entity = repository.getEntity(req.javascriptHistoryId, login.getLocaleCode());

			if (res.entity == null)
				throw new NotFoundException("JAVASCRIPT_HISTORY_ID=" + req.javascriptHistoryId + "のレコードが存在しません");

			// 排他判定
			if (!eq(res.entity.getVersion(), req.version))
				throw new AlreadyUpdatedException(
						"JAVASCRIPT_HISTORY_ID=" + req.javascriptHistoryId + " VERSION=" + req.version);
		}
		res.success = true;
		return res;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Vd0051SaveRequest req) {
		final Vd0051InitResponse res = createResponse(Vd0051InitResponse.class, req);

		// バリデーション
		final String error = validate(req);
		if (isEmpty(error)) {

			// JavaScriptファイル定義
			final LoginInfo login = sessionHolder.getLoginInfo();
			MwmJavascript js = repository.get(req.entity.javascriptId);
			MessageCd messageCd;
			if (js == null) {
				req.entity.javascriptId = repository.insert(req.entity, login);
				messageCd = MessageCd.MSG0066;
			}
			else {
				repository.update(js, req.entity);
				messageCd = MessageCd.MSG0067;
			}

			// 既存のJavascript履歴で生きているものを論理削除
			repository.deleteHistory(req.entity.javascriptId, login);

			// 新しいJavascript履歴を追加
			// （これで生きているJavascript履歴は１つしかないのが保障される）
			long javascriptHistoryId = repository.insertHistory(req.entity);

			// 多言語対応
			multi.save("MWM_JAVASCRIPT", req.entity.javascriptId, "REMARKS", req.entity.remarks);

			// 読み直し
			res.entity = repository.getEntity(javascriptHistoryId, login.getLocaleCode());

			res.addSuccesses(i18n.getText(messageCd, "Javascript"));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}

		return res;
	}

	/** バリデーション */
	private String validate(Vd0051SaveRequest req) {
		final Vd0051Entity e = req.entity;

		// スクリプト内容が必須
		if (isEmpty(e.script))
			return i18n.getText(MessageCd.MSG0001, MessageCd.scriptContents);

		// ファイル名が必須
		if (isEmpty(e.fileName))
			return i18n.getText(MessageCd.MSG0001, MessageCd.fileName);

		// すでにファイル名が別のJavascriptIdで使用済み
		final MwmJavascript js = repository.getByFileName(e.corporationCode, e.fileName);
		if (js != null && (e.javascriptId == null || e.javascriptId != js.getJavascriptId()))
			return i18n.getText(MessageCd.MSG0130, MessageCd.fileName);

		return null;
	}

}
