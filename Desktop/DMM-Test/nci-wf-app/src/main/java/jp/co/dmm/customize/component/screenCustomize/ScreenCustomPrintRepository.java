package jp.co.dmm.customize.component.screenCustomize;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.jpa.entity.mw.CntrctInf;
import jp.co.dmm.customize.jpa.entity.mw.MnyMst;
import jp.co.dmm.customize.jpa.entity.mw.PurordInf;
import jp.co.nci.iwf.jersey.base.BaseRepository;

@ApplicationScoped
public class ScreenCustomPrintRepository extends BaseRepository{

	/**
	 * 通貨コード取得
	 * @param moneyType 通貨名
	 * @param companyCode 会社コード
	 * @return 通貨記号
	 */
	public String getMnyType(Object moneyName, String companyCode) {
		String query = "SELECT * FROM MNY_MST WHERE DLT_FG = ? AND MNY_NM = ? AND COMPANY_CD = ?";
		List<Object> params = new ArrayList<>();
		params.add("0");
		params.add(moneyName);
		params.add(companyCode);

		List<MnyMst> mnyMstList = select(MnyMst.class, query, params.toArray());
		for (MnyMst mst : mnyMstList) {
			return mst.getMnyMrk();
		}

		return null;
	}

	/**
	 * 契約情報取得
	 * @param moneyType 通貨名
	 * @param companyCode 会社コード
	 * @return 契約期間
	 */
	public Map<String, Date> getContractInfo(Object contractNo, String companyCode) {
		String query = "SELECT * FROM CNTRCT_INF WHERE DLT_FG = ? AND CNTRCT_NO = ? AND COMPANY_CD = ?";
		List<Object> params = new ArrayList<>();
		params.add("0");
		params.add(contractNo);
		params.add(companyCode);

		List<CntrctInf> cntrctInfList = select(CntrctInf.class, query, params.toArray());
		Map<String, Date> contractDates = new LinkedHashMap<>();
		for (CntrctInf mst : cntrctInfList) {
			contractDates.put("CONTRACT_FROM", mst.getCntrctPrdSDt());
			contractDates.put("CONTRACT_TO", mst.getCntrctPrdEDt());
		}
		return contractDates;
	}

	/**
	 * 契約情報取得
	 * @param orderNo 発注NO
	 * @param companyCode 会社コード
	 * @return 契約期間
	 */
	public Map<String, Date> getContractInfoFromOrderInfo(Object orderNo, String companyCode) {
		String query = "SELECT * FROM PURORD_INF WHERE DLT_FG = ? AND PURORD_NO = ? AND COMPANY_CD = ?";
		List<Object> params = new ArrayList<>();
		params.add("0");
		params.add(orderNo);
		params.add(companyCode);

		List<PurordInf> purordInfList = select(PurordInf.class, query, params.toArray());
		if (purordInfList.size() > 0) {
			return getContractInfo(purordInfList.get(0).getCntrctNo(), companyCode);
		}
		return null;
	}

}
