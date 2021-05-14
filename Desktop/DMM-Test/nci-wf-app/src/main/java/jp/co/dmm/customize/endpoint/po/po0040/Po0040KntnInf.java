package jp.co.dmm.customize.endpoint.po.po0040;

/**
 * kintone取込データの一行分を表すBeanクラス.
 * ※取込時は全項目、文字列として取り込む
 */
public class Po0040KntnInf {

	/** ロットNo */
	public String lotNo;
	/** 連番 */
	public Integer sqno;
	/** レコード番号 */
	public String recNo;
	/** イベントNo */
	public String evntNo;
	/** kintoneステータス */
	public String kntnSts;
	/** 開催日 */
	public String exhbDt;
	/** イベント管理No */
	public String evntMngNo;
	/** イベント内容 */
	public String evntCont;
	/** kintoneホールID */
	public String kntnHllId;
	/** ホール名称 */
	public String hllNm;
	/** プロダクションID */
	public String prdctId;
	/** プロダクション名称 */
	public String prdctNm;
	/** タレント名称 */
	public String tlntNm;
	/** 取引先コード */
	public String splrCd;
	/** 基本金額 */
	public Long baseAmt;
	/** 調整額（基本金額） */
	public Long adjBaseAmt;
	/** 交通費 */
	public Long trnspExpAmt;
	/** 調整額（交通費） */
	public Long adjTrnspExpAmt;
	/** 原稿作成費 */
	public Long mnscrExpAmt;
	/** 請求金額 */
	public Long invAmt;
	/** 部門コード */
	public String bumonCd;
	/** 分析コード */
	public String anlysCd;
	/** 概要 */
	public String smry;
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
