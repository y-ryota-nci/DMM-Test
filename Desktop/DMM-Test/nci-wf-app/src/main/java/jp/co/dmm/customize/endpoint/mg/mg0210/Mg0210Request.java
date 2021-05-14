package jp.co.dmm.customize.endpoint.mg.mg0210;

import java.util.List;

import jp.co.dmm.customize.jpa.entity.mw.HldtaxMstPK;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0210Request extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 会社付加コード */
	public String companyAddedInfo;
	/** 会社名称 */
	public String companyNm;
	/** 源泉税区分 */
	public String hldtaxTp;
	/** 源泉税名称 */
	public String hldtaxNm;

	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;

	/** 選択リスト */
	public List<HldtaxMstPK> checkeds;
}
