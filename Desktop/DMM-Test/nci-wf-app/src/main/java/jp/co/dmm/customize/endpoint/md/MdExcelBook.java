package jp.co.dmm.customize.endpoint.md;

import java.io.Serializable;
import java.util.Set;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MdExcelBook implements Serializable {
	/** 区切り文字 */
	public final String SEPARATOR = "\t";

	/** シート「取引先マスタ」 */
	public MdExcelSheetSplr sheetSplr;
	/** シート「振込先マスタ」 */
	public MdExcelSheetPayeeBnkacc sheetAcc;
	/** シート「関係先マスタ」 */
	public MdExcelSheetRltPrt sheetRlt;
	/** シート「反社情報」 */
	public MdExcelSheetOrgCrm sheetOrg;


	/** 会社コード一覧 */
	public Set<String> existCompanyCodes;
	/** 取引先コード一覧 */
	public Set<String> existSplrCodes;
	/** 処理区分 */
	public Set<String> processTypes = MiscUtils.asSet("A","U","C","D");
	/** 取引先種別 */
	public Set<String> splrTypes = MiscUtils.asSet("1","2","3");
	/** 法人／個人区分 */
	public Set<String> crpPrsTypes = MiscUtils.asSet("1","2");
	/** 国内・海外区分 */
	public Set<String> dmsAbrTypes = MiscUtils.asSet("1","2");
	/** 関係会社区分 */
	public Set<String> affcmpTypes = MiscUtils.asSet("0","1");
	/** 取引状況区分 */
	public Set<String> trdStsTypes = MiscUtils.asSet("1","2","3");
	/** 都道府県）コード */
	public Set<String> ardPrfTypes = MiscUtils.asSet(
		"01","02","03","04","05","06","07","08","09","10",
		"11","12","13","14","15","16","17","18","19","20",
		"21","22","23","24","25","26","27","28","29","30",
		"31","32","33","34","35","36","37","38","39","40",
		"41","42","43","44","45","46","47");

	/** 仕入先社員区分*/
	public Set<String> buyeeStfTypes = MiscUtils.asSet("1","2");
	/** 口座種別*/
	public Set<String> bnkaccTypes = MiscUtils.asSet("01","02");
	/** 振込手数料負担区分 */
	public Set<String> payCmmOblTypes = MiscUtils.asSet("0","1");
	/** 休日処理区分 */
	public Set<String> hldTrtTypes = MiscUtils.asSet("0","1");
	/** 最終判定区分 */
	public Set<String> lastJdgTps = MiscUtils.asSet("0","1");
	/** 部門コード一覧 */
	public Set<String> existBumonCodes;
	/** 部門コード一覧 */
	public Set<String> existBnkaccSSCodes;
	/** 国コード一覧 */
	public Set<String> existLndCds;
	/** 判定区分*/
	public Set<String> jdgTps = MiscUtils.asSet("0","1","2","3","4");

}
