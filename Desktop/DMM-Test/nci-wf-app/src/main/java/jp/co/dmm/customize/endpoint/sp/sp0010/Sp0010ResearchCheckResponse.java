package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.ArrayList;
import java.util.List;

import jp.co.dmm.customize.endpoint.sp.SplrMstEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 取引先情報名寄せチェック結果レスポンス
 */
public class Sp0010ResearchCheckResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** チェック結果 */
	public String checkResults = "0";

	/** 対象リスト */
	public List<SplrMstEntity> results = new ArrayList<SplrMstEntity>();

	/** 既に登録済み会社コード */
	public String existsCompanyCds;
}
