package jp.co.dmm.customize.endpoint.mg.mg0331;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.LndMst;
import jp.co.dmm.customize.jpa.entity.mw.LndMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 国マスタ編集画面のリポジトリ
 */
@ApplicationScoped
public class Mg0331Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 選択肢取得
	 * @return 選択肢リスト
	 * @param localeCode ロケールコード
	 */
	public List<OptionItem> getSelectItems(boolean isEmpty, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(CorporationCodes.DMM_COM);
		params.add("dltFg");
		params.add(localeCode);

		final String sql = "select * from MWV_OPTION_ITEM where CORPORATION_CODE = ? and OPTION_CODE = ? and DELETE_FLAG = '0' and LOCALE_CODE = ? order by SORT_ORDER";

		final List<OptionItem> items = new ArrayList<>();
		if (isEmpty) {
			items.add(OptionItem.EMPTY);
		}
		items.addAll(select(MwmOptionItem.class, sql, params.toArray()).stream().map(o -> new OptionItem(o.getCode(), o.getLabel())).collect(Collectors.toList()));
		return items;
	}

	/**
	 * 対象の国マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public Mg0331Entity get(Mg0331Request req, String localeCode) {

		final StringBuilder replace = new StringBuilder();
		replace.append("  L.LND_CD");
		replace.append(", L.LND_NM");
		replace.append(", L.LND_CD_DJII");
		replace.append(", L.SORT_ORDER");
		replace.append(", L.DLT_FG");

		final StringBuilder sql = new StringBuilder(getSql("MG0330_01").replaceFirst(REPLACE, replace.toString()));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(CorporationCodes.DMM_COM);

		sql.append(" and L.LND_CD = ? ");
		params.add(req.lndCd);

		return selectOne(Mg0331Entity.class, sql.toString(), params.toArray());

	}

	/**
	 * 国情報を更新
	 * @param req
	 * @param wfUserRole
	 */
	public void update(Mg0331SaveRequest req, WfUserRole wfUserRole) {
		//
		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// DBに存在する住所情報を取得
		final LndMstPK pk = new LndMstPK();
		pk.setCompanyCd(CorporationCodes.DMM_COM);
		pk.setLndCd(req.lndCd);

		final LndMst dbEntity = em.find(LndMst.class, pk);
		if (isNotEmpty(dbEntity)) {
			dbEntity.setLndNm(req.lndNm);
			dbEntity.setLndCdDjii(req.lndCdDjii);
			dbEntity.setSortOrder(Integer.valueOf(req.sortOrder));
			dbEntity.setDltFg(req.dltFg);
			dbEntity.setCorporationCodeUpdated(corporationCode);
			dbEntity.setUserCodeUpdated(userCode);
			dbEntity.setIpUpdated(ipAddr);
			dbEntity.setTimestampUpdated(now);
			em.merge(dbEntity);
		}
	}

	/**
	 * 国情報を新規登録
	 * @param req
	 * @param wfUserRole
	 */
	public void insert(Mg0331SaveRequest req, WfUserRole wfUserRole) {

		final String corporationCode = wfUserRole.getCorporationCode();
		final String userCode = wfUserRole.getUserCode();
		final String ipAddr = wfUserRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final LndMstPK pk = new LndMstPK();
		pk.setCompanyCd(CorporationCodes.DMM_COM);
		pk.setLndCd(req.lndCd);

		final LndMst entity = new LndMst();
		entity.setId(pk);

		entity.setLndNm(req.lndNm);
		entity.setLndCdDjii(req.lndCdDjii);
		entity.setSortOrder(Integer.valueOf(req.sortOrder));
		entity.setDltFg(DeleteFlag.OFF);
		entity.setCorporationCodeCreated(corporationCode);
		entity.setUserCodeCreated(userCode);
		entity.setIpCreated(ipAddr);
		entity.setTimestampCreated(now);
		entity.setCorporationCodeUpdated(corporationCode);
		entity.setUserCodeUpdated(userCode);
		entity.setIpUpdated(ipAddr);
		entity.setTimestampUpdated(now);
		em.persist(entity);
	}
}
