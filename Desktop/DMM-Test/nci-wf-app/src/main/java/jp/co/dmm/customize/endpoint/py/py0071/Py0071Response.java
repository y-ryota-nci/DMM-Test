package jp.co.dmm.customize.endpoint.py.py0071;

import java.math.BigDecimal;
import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 買掛残高の検索結果レスポンス
 */
public class Py0071Response extends BasePagingResponse {

	/** 会社コード */
	public String companyCd;
	/** 勘定科目選択肢 */
	public List<OptionItem> accCds;
	/** 取引先選択肢 */
	public List<OptionItem> splrCds;

	/** 会社名 */
	public String companyNm;
	/** 勘定科目名 */
	public String accNm;
	/** 取引先名(漢字) */
	public String splrNmKj;

	/** 前月残高邦貨金額 */
	public BigDecimal prvTotalAmtJpy;
	/** 借方金額合計 */
	public BigDecimal dbtTotalAmtJpy;
	/** 貸方金額合計 */
	public BigDecimal cdtTotalAmtJpy;
	/** 当月残高邦貨金額 */
	public BigDecimal nxtTotalAmtJpy;
}
