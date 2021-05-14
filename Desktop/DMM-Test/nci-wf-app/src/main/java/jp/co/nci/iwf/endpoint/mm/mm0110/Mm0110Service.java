package jp.co.nci.iwf.endpoint.mm.mm0110;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.systemConfig.download.SystemConfigDownloader;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationProperty;

/**
 * システムプロパティ編集画面サービス
 */
@BizLogic
public class Mm0110Service extends BaseService {
	@Inject private Mm0110Repository repository;
	@Inject private CorporationService corp;
	@Inject private SystemConfigDownloader downloader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0110InitResponse init(Mm0110InitRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Mm0110InitResponse res = createResponse(Mm0110InitResponse.class, req);
		res.corporationCode = defaults(req.corporationCode, sessionHolder.getLoginInfo().getCorporationCode());
		res.corporations = corp.getMyCorporations(false);
		res.props = repository.getProperties(res.corporationCode, localeCode);
		res.success = true;
		return res;
	}

	/**
	 * 検索処理
	 * @param req
	 * @return
	 */
	public Mm0110SearchResponse search(Mm0110SearchRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Mm0110SearchResponse res = createResponse(Mm0110SearchResponse.class, req);
		res.props = repository.getProperties(req.corporationCode, localeCode);
		res.success = true;
		return res;
	}

	/**
	 * 保存処理
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0110SaveResponse save(Mm0110SaveRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		// キャッシュのクリア
		corpProp.clearCache();

		if (!eq(login.getCorporationCode(), req.corporationCode) && !login.isAspAdmin())
			throw new ForbiddenException("自社以外を編集できるのはASP管理者だけです。");

		// WFM_CORP_PROP_MASTER
		@SuppressWarnings("unused")
		int updated = 0;
		if (login.isAspAdmin()) {
			// ASPはデフォルト値を編集できる
			final Map<String, WfmCorpPropMaster> currents = repository.getMwmCorpPropMaster()
					.stream()
					.collect(Collectors.toMap(c -> c.getPropertyCode(), c -> c));
			for (Mm0110Entity input : req.inputs) {
				WfmCorpPropMaster current = currents.remove(input.propertyCode);
				if (current != null) {
					repository.update(current, input);
					updated++;
				}
			}
		}

		// WFM_CORPORATION_PROPERTY
		{
			Map<String, WfmCorporationProperty> currents =
					repository.getWfmCorporationProperty(req.corporationCode).stream()
					.filter(p -> p != null)
					.collect(Collectors.toMap(p -> p.getPk().getPropertyCode(), p -> p));
			for (Mm0110Entity input : req.inputs) {
				if (isEmpty(input.corporationCode)) {
					input.corporationCode = req.corporationCode;
				}
				WfmCorporationProperty current = currents.remove(input.propertyCode);
				if (current == null) {
					if (isEmpty(input.propertyValue)) {
						continue;	// 既存行なしで未入力なら何もしない
					} else {
						repository.insert(input);
						updated++;
					}
				} else if (isEmpty(input.propertyValue)) {
					// 既存行ありで未入力ならデフォルト値に従うということなので、企業固有値を削除
					repository.delete(current, input);
					updated++;
				} else {
					repository.update(current, input);
					updated++;
				}
			}
		}

		// 読み直し
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Mm0110SaveResponse res = createResponse(Mm0110SaveResponse.class, req);
		res.props = repository.getProperties(req.corporationCode, localeCode);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.systemProperty));
		res.success = true;
		return res;
	}

	/**
	 * 環境設定内容をダウンロード
	 * @return
	 */
	public StreamingOutput download(String corporationCode) {
		return downloader.setup(corporationCode);
	}

}
