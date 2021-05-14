package jp.co.dmm.customize.endpoint.mg.mg0011;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.dmm.customize.jpa.entity.mw.ItmImgMst;
import jp.co.dmm.customize.jpa.entity.mw.ItmImgMstPK;
import jp.co.dmm.customize.jpa.entity.mw.ItmMst;
import jp.co.dmm.customize.jpa.entity.mw.ItmMstPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 品目マスタ設定画面のリポジトリ
 */
@ApplicationScoped
public class Mg0011Repository extends BaseRepository {

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode, boolean isEmpty) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	/**
	 * 自身が所属する会社取得処理
	 * @param userAddedInfo ユーザ付加コード
	 * @param localeCode ロケールコード
	 * @return 自身が所属する会社リスト
	 */
	@SuppressWarnings("unchecked")
	public List<OptionItem> getCompanyItems (String userAddedInfo, String localeCode) {

		List<OptionItem> companyList = new ArrayList<OptionItem>();

		String sql = "SELECT CV.CORPORATION_CODE, CV.CORPORATION_NAME FROM WFM_CORPORATION_V CV, WFM_USER WU " +
				"WHERE CV.CORPORATION_CODE = WU.CORPORATION_CODE " +
				"AND WU.USER_ADDED_INFO = ? " +
				"AND CV.LOCALE_CODE = ?";

		Object[] params = {userAddedInfo, localeCode};

		Query query = em.createNativeQuery(sql);
		putParams(query, params);

		List<Object[]> results = query.getResultList();

		for (Object[] record : results) {
			OptionItem item = new OptionItem();
			item.setValue((String)record[0]);
			item.setLabel((String)record[1]);
			companyList.add(item);
		}

		return companyList;
	}

	/**
	 * 対象の品目マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Mg0011Entity get(Mg0011GetRequest req) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0010_02"));

		// パラメータ
		final List<Object> params = new ArrayList<>();

		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and IM.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 組織コード
		if (isNotEmpty(req.orgnzCd)) {
			sql.append(" and IM.ORGNZ_CD = ? ");
			params.add(req.orgnzCd);
		}

		// 品目コード
		if (isNotEmpty(req.itmCd)) {
			sql.append(" and IM.ITM_CD = ? ");
			params.add(req.itmCd);
		}

		// 連番
		if (isNotEmpty(req.sqno)) {
			sql.append(" and IM.SQNO = ? ");
			params.add(req.sqno);
		}

		List<Object[]> results = new ArrayList<>();

		//検索条件が存在する場合のみ検索(存在しない場合は新規追加)
		if(params.size() != 0) {
			Query query = em.createNativeQuery(sql.toString());
			putParams(query, params.toArray());
			results = query.getResultList();
		}

		if (results.size() == 1) {
			Mg0011Entity entity = convertEntity(results.get(0));
			setItmImgId(entity);

			return entity;

		} else {
			return new Mg0011Entity();
		}
	}

	/**
	 * 対象の品目マスタ抽出
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void setItmImgId(Mg0011Entity entity) {

		// SQL
		StringBuilder sql = new StringBuilder(getSql("MG0011_02"));

		// パラメータ
		Object[] params = {entity.companyCd, entity.orgnzCd, entity.itmCd};

		Query query = em.createNativeQuery(sql.toString());
		putParams(query, params);

		List<Object> results = query.getResultList();

		if (results.size() != 0) {
			entity.itmImgId = ((BigDecimal) results.get(0)).toString();
		}
	}

	private Mg0011Entity convertEntity(Object[] result) {

		Mg0011Entity entity = new Mg0011Entity();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		entity.companyCd = (String)result[0];
		entity.orgnzCd = (String) result[1];
		entity.orgnzNm = (String) result[2];
		entity.itmCd = (String) result[3];
		entity.sqno = result[4].toString();
		entity.itmNm = (String) result[5];
		entity.ctgryCd = (String) result[6];

		entity.stckTp = (String) result[7];

		entity.untCd = (String) result[8];
		entity.amt = result[9] != null ? (BigDecimal) result[9] : null;
		entity.makerNm = (String) result[10];
		entity.makerMdlNo = (String) result[11];
		entity.itmRmk = (String) result[12];

		entity.prcFldTp = (String) result[13];

		entity.vdDtS = result[14] != null ? (Date) result[14] : null;
		entity.vdDtE = result[15] != null ? (Date) result[15] : null;
		entity.vdDtSStr = entity.vdDtS != null ? sd.format(entity.vdDtS) : null;
		entity.vdDtEStr = entity.vdDtE != null ? sd.format(entity.vdDtE) : null;

		entity.dltFg = (String)result[16];
		entity.dltFgNm = (String)result[17];

		entity.stckTpNm = (String) result[18];
		entity.prcFldTpNm = (String) result[19];

		entity.splrCd = (String) result[20];
		entity.splrNmKj = (String) result[21];
		entity.splrNmKn = (String) result[22];

		entity.taxCd = (String) result[23];
		entity.itmVrsn = result[24] != null ? (BigDecimal) result[24] : null;

		entity.taxNm = (String) result[25];

		return entity;
	}

	/**
	 * 品目情報更新処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return
	 */
	public void update(Mg0011UpdateRequest req, WfUserRole userRole) {
		// 更新ときの共通情報
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		//ITM_MSTのキーオブジェクトを生成
		ItmMstPK pk = new ItmMstPK();
		pk.setCompanyCd(corporationCode);
		pk.setOrgnzCd(req.orgnzCd);
		pk.setItmCd(req.itmCd);
		pk.setSqno(req.sqno.longValue());

		//ITM_MSTのインスタンスをロードする
		ItmMst itmMst = em.find(ItmMst.class, pk);

		itmMst.setItmNm(req.itmNm);
		itmMst.setCtgryCd(req.ctgryCd);
		itmMst.setStckTp(req.stckTp);
		itmMst.setSplrCd(req.splrCd);
		itmMst.setSplrNmKj(req.splrNmKj);
		itmMst.setSplrNmKn(req.splrNmKn);
		itmMst.setUntCd(req.untCd);
		itmMst.setAmt(req.amt != null ? new BigDecimal(req.amt) : null);
		itmMst.setTaxCd(req.taxCd);
		itmMst.setMakerNm(req.makerNm);
		itmMst.setMakerMdlNo(req.makerMdlNo);
		itmMst.setItmRmk(req.itmRmk);
		itmMst.setItmVrsn(req.itmVrsn != null ? new BigDecimal(req.itmVrsn) : null);
		itmMst.setPrcFldTp(req.prcFldTp);
		itmMst.setVdDtS(req.vdDtS);
		itmMst.setVdDtE(req.vdDtE);

		itmMst.setCorporationCodeUpdated(corporationCode);
		itmMst.setUserCodeUpdated(userCode);
		itmMst.setIpUpdated(ipAddr);
		itmMst.setTimestampUpdated(now);

		em.merge(itmMst);
	}

	/**
	 * 品目登録処理
	 * @param req リクエスト
	 * @param res レスポンス
	 * @return 登録時の連番されたSQNO
	 */
	public long insert(Mg0011UpdateRequest req, WfUserRole userRole) {

		//品目画像更新
		if(isNotEmpty(req.itmImgId)) {
			updateItmImg(req, userRole);
			deleteItmImg(req, userRole);
		}

		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		//ITM_MSTのインスタンスをロードする
		ItmMst itmMst = new ItmMst();

		//ITM_MSTのキーオブジェクトを生成
		ItmMstPK pk = new ItmMstPK();

		//現在最大SQNOを取得（未登録の場合:0返却）
		long sqno = getMaxSqno(req);
		sqno++;

		pk.setCompanyCd(corporationCode);
		pk.setOrgnzCd(req.orgnzCd);
		pk.setItmCd(req.itmCd);
		pk.setSqno(sqno);

		//insert
		itmMst.setId(pk);
		itmMst.setItmNm(req.itmNm);
		itmMst.setCtgryCd(req.ctgryCd);
		itmMst.setStckTp(req.stckTp);
		itmMst.setSplrCd(req.splrCd);
		itmMst.setSplrNmKj(req.splrNmKj);
		itmMst.setSplrNmKn(req.splrNmKn);
		itmMst.setUntCd(req.untCd);
		itmMst.setAmt(req.amt != null ? new BigDecimal(req.amt) : null);
		itmMst.setTaxCd(req.taxCd);
		itmMst.setMakerNm(req.makerNm);
		itmMst.setMakerMdlNo(req.makerMdlNo);
		itmMst.setItmRmk(req.itmRmk);
		itmMst.setItmVrsn(req.itmVrsn != null ? new BigDecimal(req.itmVrsn) : null);
		itmMst.setPrcFldTp(req.prcFldTp);
		itmMst.setVdDtS(req.vdDtS);
		itmMst.setVdDtE(req.vdDtE);
		itmMst.setDltFg(req.dltFg);

		itmMst.setCorporationCodeCreated(corporationCode);
		itmMst.setUserCodeCreated(userCode);
		itmMst.setIpCreated(ipAddr);
		itmMst.setTimestampCreated(now);

		itmMst.setCorporationCodeUpdated(corporationCode);
		itmMst.setUserCodeUpdated(userCode);
		itmMst.setIpUpdated(ipAddr);
		itmMst.setTimestampUpdated(now);

		em.persist(itmMst);

		return sqno;
	}

	/**
	 *
	 * @param input アップロード画像情報
	 * @param userRole 操作者
	 * @return
	 */
	public ItmImgMst insertItmImg(ItmImgMst input, WfUserRole userRole) {
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// UUIDにより、画像IDを生成（重複ならない仕組み）。DBは18桁数のため、1000000を割る
		long itmImgId = Long.divideUnsigned(UUID.randomUUID().getMostSignificantBits(), 1000000) ;

		ItmImgMstPK pk = new ItmImgMstPK();
		pk.setItmImgId(itmImgId);
		pk.setCompanyCd(corporationCode);

		input.setId(pk);

		//まだ仮登録なので論理削除として登録する
		input.setOrgnzCd("DUMMY");
		input.setItmCd("DUMMY");
		input.setVrsn(BigDecimal.valueOf(1));


		input.setCorporationCodeCreated(corporationCode);
		input.setUserCodeCreated(userCode);
		input.setIpCreated(ipAddr);
		input.setTimestampCreated(now);

		input.setCorporationCodeUpdated(corporationCode);
		input.setUserCodeUpdated(userCode);
		input.setIpUpdated(ipAddr);
		input.setTimestampUpdated(now);

		em.persist(input);
		em.flush();
		return em.find(ItmImgMst.class, pk);
	}

	/**
	 *
	 * @param itmImgId 画像ID
	 * @param userRole 操作者
	 * @return
	 */
	public ItmImgMst getItmImgByPK(Long itmImgId, WfUserRole userRole) {
		ItmImgMstPK pk = new ItmImgMstPK();
		pk.setItmImgId(itmImgId);
		pk.setCompanyCd(userRole.getCorporationCode());

		return em.find(ItmImgMst.class, pk);
	}

	/**
	 * 品目画像更新
	 * @param req
	 * @param userRole 操作者
	 * @return
	 */
	public int updateItmImg(Mg0011UpdateRequest req, WfUserRole userRole) {
		StringBuilder updateSql = new StringBuilder(getSql("MG0011_01"));

		// パラメータ
		final List<Object> params = new ArrayList<>();
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		params.add(req.orgnzCd);
		params.add(req.itmCd);
		params.add(DeleteFlag.OFF);
		params.add(corporationCode);
		params.add(userCode);
		params.add(ipAddr);
		params.add(now);
		params.add(req.companyCd);
		params.add(req.itmImgId);

		Query query = em.createNativeQuery(updateSql.toString());
		putParams(query, params.toArray());
		int updateCnt = query.executeUpdate();

		return updateCnt;
	}

	/**
	 * 品目画像削除
	 * @param req
	 * @param userRole 操作者
	 * @return
	 */
	public int deleteItmImg(Mg0011UpdateRequest req, WfUserRole userRole) {

		// パラメータ
		final String corporationCode = userRole.getCorporationCode();
		final String userCode = userRole.getUserCode();
		final String ipAddr = userRole.getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		//その他の品目画像は削除フラグを「1」に設定
		StringBuilder deleteSql = new StringBuilder(getSql("MG0011_03"));
		final List<Object> delParams = new ArrayList<>();
		delParams.add(corporationCode);
		delParams.add(userCode);
		delParams.add(ipAddr);
		delParams.add(now);
		delParams.add(req.companyCd);
		delParams.add(req.itmCd);
		delParams.add(req.itmImgId);

		Query delQuery = em.createNativeQuery(deleteSql.toString());
		putParams(delQuery, delParams.toArray());
		int updateCnt = delQuery.executeUpdate();

		return updateCnt;
	}

	/**
	 * 存在チェック
	 * @param req
	 * @return
	 */
	public int getMaxSqno(Mg0011UpdateRequest req, boolean updateCheck) {
		//連番取得
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0011_04"));
		sql.append(" and DLT_FG = '0' ");

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.orgnzCd);
		params.add(req.itmCd);

		if (req.vdDtE != null) {
			sql.append(" and (VD_DT_S is null or VD_DT_S <= ?) ");
			params.add(req.vdDtE);
		}

		if (req.vdDtS != null) {
			sql.append(" and (VD_DT_E is null or VD_DT_E >= ?) ");
			params.add(req.vdDtS);
		}

		if (updateCheck) {
			sql.append(" and SQNO != ? ");
			params.add(req.sqno);
		}

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}

	/**
	 * Insert用最大SQNOを取得（削除フラグ、有効期限など考慮しない）
	 * @param req
	 * @return
	 */
	public int getMaxSqno(Mg0011UpdateRequest req) {
		//連番取得
		//SQL
		StringBuilder sql = new StringBuilder(getSql("MG0011_04"));

		List<Object> params = new ArrayList<>();
		params.add(req.companyCd);
		params.add(req.orgnzCd);
		params.add(req.itmCd);

		Integer results = count(sql, params.toArray());

		return results != null ? results.intValue() : 0;
	}

	public ItmMst getByPk(String companyCd, String orgnzCd, String itmCd, long sqno) {
		ItmMstPK id = new ItmMstPK();
		id.setCompanyCd(companyCd);
		id.setOrgnzCd(orgnzCd);
		id.setItmCd(itmCd);
		id.setSqno(sqno);

		ItmMst itmMst = em.find(ItmMst.class, id);
		return itmMst;
	}


}
