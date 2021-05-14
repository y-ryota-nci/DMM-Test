package jp.co.dmm.customize.endpoint.bd.bd0802;

import java.util.List;

import jp.co.dmm.customize.endpoint.bd.bd0801.Bd0801Entity;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 予算計画履歴作成画面の初期化レスポンス
 */
public class Bd0802InitResponse extends BaseResponse {
	/** 年度 */
	public String yrCd;
	public String yrNm;
	/** 検収基準・支払基準 */
	public String rcvCostPayTp;
	public String rcvCostPayTpNm;
	/** 本部 */
	public String organizationNameLv2;
	/** 部・室 */
	public String organizationCodeLv3;
	public String organizationNameLv3;

	/** B/S／P/L区分 */
	public String bsplTp;
	public String bsplTpNm;


	/** 検索結果 */
	public List<Bd0801Entity> results;
}
