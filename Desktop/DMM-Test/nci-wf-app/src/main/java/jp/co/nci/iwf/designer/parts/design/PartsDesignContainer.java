package jp.co.nci.iwf.designer.parts.design;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【デザイン時】コンテナパーツの抽象基底クラス
 */
public abstract class PartsDesignContainer extends PartsDesign {
	/** コンテナコード */
	public String containerCode;
	/** コンテナ名 */
	public String containerName;
	/** テーブル名 */
	public String tableName;
	/** 背景HTML */
	public String bgHtml;
	/** コンテナ固有のカスタムCSSスタイル */
	public String customCssStyle;
	/** 企業コード */
	public String corporationCode;
	/** パーツコード採番用の連番 */
	public int partsCodeSeq;
	/** Submit時の関数名 */
	public String submitFuncName;
	/** Submit時のパラメータ */
	public String submitFuncParam;
	/** ロード時の関数名 */
	public String loadFuncName;
	/** ロード時のパラメータ */
	public String loadFuncParam;
	/** 起案担当者変更時の関数名 */
	public String changeStartUserFuncName;
	/** 起案担当者変更時の関数パラメータ */
	public String changeStartUserFuncParam;
	/** コンテナ定義のバージョン */
	public Long containerVersion;
	/** DB同期時にテーブルをDROPしない */
	public boolean notDropTableFlag;


	/** 初期行数 */
	public Integer initRowCount = 1;
	/** 最小行数 */
	public Integer minRowCount = 1;
	/** ページあたりの行数 */
	public Integer pageSize;

	/** コンテナが直接の子パーツID（孫パーツは含まない） */
	public final List<Long> childPartsIds = new ArrayList<>();

	/** コンテナが参照しているjavascriptファイル */
	public List<PartsJavascript> javascripts = new ArrayList<>();

	/** 入力済み判定パーツIDリスト（List<Long>だとJSONで変換するとIntegerと区別がつかずデシリアライズ時にエラーとなるので、精度の担保出来るBigDecimalで定義） */
	public List<BigDecimal> inputedJudgePartsIds = new ArrayList<>();

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 * @return
	 */
	public List<PartsColumn> newColumns() {
		return new ArrayList<>();
	};

	/**
	 * コンテナの子要素を初期行数分だけ生成
	 * @param container 対象コンテナ
	 * @param initSampleCount 初期行数
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public void fillRows(PartsContainerBase<?> container, DesignerContext ctx) {
		int len = (TrayType.NEW.equals(ctx.trayType) ? initRowCount : minRowCount);
		for (int i = 0; i < len; i++) {
			addRows(container, ctx);
		}
	}

	/**
	 * コンテナの子要素を1行分生成し、既存行へ追加
	 * @param container
	 * @param ctx
	 * @return
	 */
	public PartsContainerRow addRows(PartsContainerBase<?> container, DesignerContext ctx) {
		final PartsContainerRow row = new PartsContainerRow();
		container.rows.add(row);

		// 新しい行IDを求める
		int newRowId = 1;
		for (PartsContainerRow r : container.rows) {
			if (r.rowId >= newRowId)
				newRowId = r.rowId + 1;
		}
		row.rowId = newRowId;
		row.sortOrder = container.rows.size();

		for (Long partsId : childPartsIds) {
			final PartsDesign d = ctx.designMap.get(partsId);
			final PartsBase<?> p = d.newParts(container, row.rowId, ctx);

			// 親コンテナと自パーツの表示条件をマージしたうえで設定
			// また、あとで子要素を処理したときに親の結果が正しく伝播するよう、表示条件Map自身を書き換え
			p.dcType = getMergeDcType(d, ctx);
			ctx.dcMap.put(p.partsId, p.dcType);

			// コンテナと子パーツを紐付け
			if (!row.children.contains(p.htmlId)) {
				row.children.add(p.htmlId);
			}

			ctx.runtimeMap.put(p.htmlId, p);
		}
		return row;
	}

	/**
	 * 自パーツと親パーツの表示条件をマージ
	 * @param d パーツ定義
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	private int getMergeDcType(PartsDesign d, DesignerContext ctx) {
		int self = MiscUtils.defaults(ctx.dcMap.get(d.partsId), DcType.INPUTABLE);

		// 自分と親とを比較し、重みがある(＝表示条件が厳しい)ほうを採用
		if (d.parentPartsId != null && d.parentPartsId > 0) {
			int parent = MiscUtils.defaults(ctx.dcMap.get(d.parentPartsId), DcType.INPUTABLE);
			self = Math.max(self, parent);
		}

		return self;
	}
}
