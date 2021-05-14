package jp.co.dmm.customize.endpoint.mg.mg0180;

import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.InRtoMstPK;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0180Request extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 会社付加コード */
	public String companyAddedInfo;
	/** 会社名称 */
	public String companyNm;
	/** 通貨コード */
	public String mnyCd;
	/** 通貨名称 */
	public String mnyNm;
	/** 連番 */
	public long sqno;

	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;

	/** 選択リスト */
	public List<InRtoMstPK> checkeds;
}
