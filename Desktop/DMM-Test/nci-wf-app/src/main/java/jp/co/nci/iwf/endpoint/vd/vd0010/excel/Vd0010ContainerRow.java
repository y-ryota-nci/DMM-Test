package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

/**
 * コンテナ一覧のEXCELダウンロード用コンテナ定義パーツBean
 */
public class Vd0010ContainerRow {
	/** コンストラクタ */
	public Vd0010ContainerRow() {
	}

	/** CSSクラス */
	public String cssClass;
	/** CSSスタイル */
	public String cssStyle;
	/** フォントサイズ */
	public String fontSizeName;
	/** レンダリング方法（0:Bootstrapのグリッドでレンダリング、1:インラインでレンダリング） */
	public String renderingMethodName;
	/** 幅(大画面時) */
	public Integer colLg;
	/** 幅(中画面時) */
	public Integer colMd;
	/** 幅(小画面時) */
	public Integer colSm;
	/** モバイル端末なら非表示フラグ */
	public boolean mobileInvisibleFlag;
	/** コピー起票対象フラグ */
	public boolean copyTargetFlag;
	/** 説明 */
	public String description;
	/** タブ順付与フラグ */
	public boolean grantTabIndexFlag;
	/** 表示ラベル */
	public String labelText;
	/** パーツコード */
	public String partsCode;
	/** パーツ種別 */
	public String partsTypeName;
	/** 必須フラグ */
	public boolean requiredFlag;
	/** 並び順（タブ順ではない） */
	public Integer sortOrder;
	/** 業務管理項目コード */
	public String businessInfo;
	/** デフォルト値 */
	public String defaultValue;
	/** 拡張情報 */
	public String extInfo;
}
