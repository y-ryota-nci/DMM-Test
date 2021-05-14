package jp.co.nci.iwf.designer.parts.renderer;

import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;

/**
 * マスタ選択パーツのトリガー／配布先パーツの htmlId と テーブルのカラム名を紐付ける構造体
 */
public class MasterPartsColumn {
	/** 対象パーツID */
	public Long targetPartsId;
	/** 対象パーツの htmlId */
	public String targetHtmlId;
	/** カラム名 */
	public String columnName;
	/** 変更非通知フラグ */
	public boolean noChangeEventFlag;

	/** コンストラクタ */
	public MasterPartsColumn() {}

	/**
	 * コンストラクタ
	 * @param pr パーツ関連情報
	 * @param htmlId マスタ選択パーツのhtmlId
	 * @param ctx デザイナーコンテキスト
	 */
	public MasterPartsColumn(PartsRelation pr, String htmlId, DesignerContext ctx) {
		this.targetPartsId = pr.targetPartsId;
		this.columnName = pr.columnName;
		this.noChangeEventFlag = pr.noChangeEventFlag;

		// パーツID＋マスタ選択パーツのhtmlIdから、対象パーツのHTML_IDを求める
		PartsBase<?> p = PartsUtils.findParts(pr.targetPartsId, htmlId, ctx);
		if (p == null) {
			throw new InternalServerErrorException("対象パーツが見つかりません partsId=" + pr.targetPartsId + " htmlId=" + htmlId);
		}
		this.targetHtmlId = p.htmlId;
	}
}
