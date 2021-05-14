package jp.co.nci.iwf.designer.service.javascript;

import jp.co.nci.integrated_workflow.common.util.SecurityUtils;

/**
 * Javascriptコンテンツ
 */
public class JsContents {
	/** 空インスタンス */
	public static final JsContents EMPTY;

	static {
		// 空インスタンスを生成
		EMPTY = new JsContents("", 0L);
	}

	/** スクリプト内容 */
	public String contents;
	/** 最終更新日 */
	public long lastModified;
	/** ETag */
	public String etag;

	/** デフォルト・コンストラクタ */
	public JsContents() {
	}

	/**
	 * コンストラクタ。インスタンス化と同時にコンテンツからETAGを生成
	 * @param contents Javascriptコンテンツ
	 * @param lastModified 最終更新日
	 */
	public JsContents(CharSequence contents, long lastModified) {
		this.contents =  contents == null ? "" : contents.toString();
		this.lastModified = lastModified;
		// ETAGの計算。セキュアである必要はないので、パフォーマンスの良いMD5をつかっておく
		this.etag = SecurityUtils.hashMD5(this.contents);
	}
}
