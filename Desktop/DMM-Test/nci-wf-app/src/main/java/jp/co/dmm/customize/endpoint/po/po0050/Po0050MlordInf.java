package jp.co.dmm.customize.endpoint.po.po0050;

/**
 * 通販取込データの一行分を表すBeanクラス.
 * ※取込時は全項目、文字列として取り込む
 */
public class Po0050MlordInf {

	/** ロットNo */
	public String lotNo;
	/** 連番 */
	public Integer sqno;
	/** 伝票No */
	public String slpNo;
	/** 行No */
	public Integer lnNo;
	/** 仕入日 */
	public String buyDt;
	/** 仕入先コード */
	public String buyCd;
	/** 仕入先名称 */
	public String buyNmKj;
	/** 摘要 */
	public String abst;
	/** 商品コード */
	public String cmmdtCd;
	/** 品番 */
	public String prtNo;
	/** 商品タイトル */
	public String cmmdtTtl;
	/** 数量 */
	public Long qnt;
	/** 単価 */
	public Double uc;
	/** 金額 */
	public Long amt;
	/** 備考 */
	public String rmk;
	/** 取引先コード */
	public String splrCd;
	/** 部門コード */
	public String bumonCd;
	/** 消費税処理単位 */
	public String taxUnt;
	/** 消費税フラグ */
	public String taxFg;
	/** 消費税コード */
	public String taxCd;
	/** 費目コード(1) */
	public String itmExpsCd1;
	/** 費目コード(2) */
	public String itmExpsCd2;
	/** 課税対象区分 */
	public String taxSbjTp;
	/** 消費税種類コード */
	public String taxKndCd;
	/** 消費税フラグ(税率変更) */
	public String taxFgChg;
	/** エラー内容 */
	public String errorText;
	/** 発注No */
	public String purordNo;
	/** 検収No */
	public String rcvinspNo;
	/** 仕入No */
	public String buyNo;
}
