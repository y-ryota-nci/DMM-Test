package jp.co.dmm.customize.endpoint.bd.bd0808;

import java.io.Serializable;
import java.math.BigDecimal;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * 特定組織指定バージョン予算／実績分析画面の検索結果
 */
public class Bd0808Result implements Serializable {
	public String yrCd;
	public String rcvCostPayTp;
	public String organizationCodeLv2;
	public String organizationNameLv2;
	public String organizationCodeLv3;
	public String organizationNameLv3;
	public String bgtItmCd;
	public String bgtItmNm;
	public String totalLineFlag;
	public String bsPlTp;
	public int rowspan;	/* セル結合用のrowspan（予算科目数 * 4） */
	// 予算
	public BigDecimal bgtAmt01;
	public BigDecimal bgtAmt02;
	public BigDecimal bgtAmt03;
	public BigDecimal bgtAmt04;
	public BigDecimal bgtAmt05;
	public BigDecimal bgtAmt06;
	public BigDecimal bgtAmt07;
	public BigDecimal bgtAmt08;
	public BigDecimal bgtAmt09;
	public BigDecimal bgtAmt10;
	public BigDecimal bgtAmt11;
	public BigDecimal bgtAmt12;
	public BigDecimal bgtAmtTtl;
	// 実績(COM)
	public BigDecimal pfmAmt01 = BigDecimal.ZERO;
	public BigDecimal pfmAmt02 = BigDecimal.ZERO;
	public BigDecimal pfmAmt03 = BigDecimal.ZERO;
	public BigDecimal pfmAmt04 = BigDecimal.ZERO;
	public BigDecimal pfmAmt05 = BigDecimal.ZERO;
	public BigDecimal pfmAmt06 = BigDecimal.ZERO;
	public BigDecimal pfmAmt07 = BigDecimal.ZERO;
	public BigDecimal pfmAmt08 = BigDecimal.ZERO;
	public BigDecimal pfmAmt09 = BigDecimal.ZERO;
	public BigDecimal pfmAmt10 = BigDecimal.ZERO;
	public BigDecimal pfmAmt11 = BigDecimal.ZERO;
	public BigDecimal pfmAmt12 = BigDecimal.ZERO;
	public BigDecimal pfmAmtTtl = BigDecimal.ZERO;
	// 差異
	public BigDecimal difAmt01 = BigDecimal.ZERO;
	public BigDecimal difAmt02 = BigDecimal.ZERO;
	public BigDecimal difAmt03 = BigDecimal.ZERO;
	public BigDecimal difAmt04 = BigDecimal.ZERO;
	public BigDecimal difAmt05 = BigDecimal.ZERO;
	public BigDecimal difAmt06 = BigDecimal.ZERO;
	public BigDecimal difAmt07 = BigDecimal.ZERO;
	public BigDecimal difAmt08 = BigDecimal.ZERO;
	public BigDecimal difAmt09 = BigDecimal.ZERO;
	public BigDecimal difAmt10 = BigDecimal.ZERO;
	public BigDecimal difAmt11 = BigDecimal.ZERO;
	public BigDecimal difAmt12 = BigDecimal.ZERO;
	public BigDecimal difAmtTtl = BigDecimal.ZERO;

	/** コンストラクタ */
	public Bd0808Result() {
	}

	/** コンストラクタ */
	public Bd0808Result(Bd0808Entity b, Bd0808Entity p) {
		this.yrCd = b.yrCd;
		this.organizationCodeLv2 = b.organizationCodeUp;
		this.organizationNameLv2 = b.organizationNameUp;
		this.organizationCodeLv3 = b.organizationCode;
		this.organizationNameLv3 = b.organizationName;
		this.rcvCostPayTp = b.rcvCostPayTp;
		this.bgtItmCd = b.bgtItmCd;
		this.bgtItmNm = b.bgtItmNm;

		// 予算
		this.bgtAmt01 = b.bgtAmt01;
		this.bgtAmt02 = b.bgtAmt02;
		this.bgtAmt03 = b.bgtAmt03;
		this.bgtAmt04 = b.bgtAmt04;
		this.bgtAmt05 = b.bgtAmt05;
		this.bgtAmt06 = b.bgtAmt06;
		this.bgtAmt07 = b.bgtAmt07;
		this.bgtAmt08 = b.bgtAmt08;
		this.bgtAmt09 = b.bgtAmt09;
		this.bgtAmt10 = b.bgtAmt10;
		this.bgtAmt11 = b.bgtAmt11;
		this.bgtAmt12 = b.bgtAmt12;

		this.bgtAmtTtl = new BigDecimal(0);
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt01) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt02) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt03) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt04) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt05) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt06) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt07) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt08) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt09) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt10) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt11) ;
		this.bgtAmtTtl = this.bgtAmtTtl.add(this.bgtAmt12) ;

		// 実績
		if (p != null) {
			this.pfmAmt01 = p.bgtAmt01;
			this.pfmAmt02 = p.bgtAmt02;
			this.pfmAmt03 = p.bgtAmt03;
			this.pfmAmt04 = p.bgtAmt04;
			this.pfmAmt05 = p.bgtAmt05;
			this.pfmAmt06 = p.bgtAmt06;
			this.pfmAmt07 = p.bgtAmt07;
			this.pfmAmt08 = p.bgtAmt08;
			this.pfmAmt09 = p.bgtAmt09;
			this.pfmAmt10 = p.bgtAmt10;
			this.pfmAmt11 = p.bgtAmt11;
			this.pfmAmt12 = p.bgtAmt12;
			this.totalLineFlag = p.totalLineFlag;
			this.bsPlTp = p.bsPlTp;

			if("1".equals(p.bsPlTp)) {
				this.pfmAmtTtl = this.pfmAmt12;
			} else {
				this.pfmAmtTtl = new BigDecimal(0);
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt01) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt02) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt03) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt04) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt05) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt06) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt07) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt08) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt09) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt10) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt11) ;
				this.pfmAmtTtl = this.pfmAmtTtl.add(this.pfmAmt12) ;
			}

		}
		// 差分
		this.difAmt01 = diff(this.bgtAmt01, this.pfmAmt01);
		this.difAmt02 = diff(this.bgtAmt02, this.pfmAmt02);
		this.difAmt03 = diff(this.bgtAmt03, this.pfmAmt03);
		this.difAmt04 = diff(this.bgtAmt04, this.pfmAmt04);
		this.difAmt05 = diff(this.bgtAmt05, this.pfmAmt05);
		this.difAmt06 = diff(this.bgtAmt06, this.pfmAmt06);
		this.difAmt07 = diff(this.bgtAmt07, this.pfmAmt07);
		this.difAmt08 = diff(this.bgtAmt08, this.pfmAmt08);
		this.difAmt09 = diff(this.bgtAmt09, this.pfmAmt09);
		this.difAmt10 = diff(this.bgtAmt10, this.pfmAmt10);
		this.difAmt11 = diff(this.bgtAmt11, this.pfmAmt11);
		this.difAmt12 = diff(this.bgtAmt12, this.pfmAmt12);
		this.difAmtTtl = diff(this.bgtAmtTtl, this.pfmAmtTtl);
	}

	/** 差異＝予算－実績(COM)－実績(DCM) */
	private BigDecimal diff(BigDecimal b, BigDecimal p) {
		BigDecimal r = MiscUtils.defaults(b, BigDecimal.ZERO);
		if (p != null)
			r = r.subtract(p);
		return r;
	}
}
