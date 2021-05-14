package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsEvents;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsFileLimitation;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFile;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFileRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;

/**
 * 【デザイン時】添付ファイルパーツ。
 * 申請／承認時に動的にファイルアップロード可能な添付ファイルコントロール。
 */
public class PartsDesignAttachFile extends PartsDesign implements PartsEvents {
	/** リンクを開く対象window */
	public String target;
	/** 複数ファイルのみを扱うか(trueなら複数添付ファイル、falseだと単一添付ファイルのみを扱う) */
	public boolean multiple;
	/** 初期行数（デザイン時のサンプル行） */
	public int initSampleCount;
	/** 必須ファイル数 */
	public Integer requiredFileCount;
	/** 最大ファイル数 */
	public Integer maxFileCount;
	/** ページあたりの行数 */
	public Integer pageSize;
	/** (複数ファイルを扱うときに)コメント欄を使用するか */
	public boolean notUseComment;

	/** どんなファイル形式でも許可 */
	public boolean enableAny = true;
	/** 画像を許可 */
	public boolean enableImage;
	/** PDFを許可 */
	public boolean enablePdf;
	/** WORD文書を許可 */
	public boolean enableWord;
	/** EXCEL文書を許可 */
	public boolean enableExcel;
	/** PowerPoint文書を許可 */
	public boolean enablePowerPoint;
	/** CSVファイルを許可 */
	public boolean enableCsv;
	/** テキストファイルを許可 */
	public boolean enableText;
	/** HTMLを許可 */
	public boolean enableHtml;
	/** 「その他」を許可 */
	public boolean enableOther;
	/** 「その他」のファイル拡張子の正規表現 */
	public String regExpOther;

	/** Checkbox固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"target",
			"multiple",
			"notUseComment",
			"initSampleCount",
			"requiredFileCount",
			"maxFileCount",
			"pageSize",
			"enableAny",
			"enableImage",
			"enablePdf",
			"enableWord",
			"enableExcel",
			"enablePowerPoint",
			"enableCsv",
			"enableText",
			"enableHtml",
			"enableOther",
			"regExpOther",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();

		target = "_blank";
		multiple = true;
		notUseComment = true;
		initSampleCount = 5;	// デザイン時のサンプル行
		requiredFileCount = 1;	// 必須ファイル数
		maxFileCount = 10;		// 最大ファイル数
		pageSize = 5;
		copyTargetFlag = false;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsAttachFile parts = new PartsAttachFile();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = null;
		parts.pageNo = 1;
		parts.pageSize = this.pageSize;
		parts.multiple = this.multiple;
		parts.maxFileCount = this.maxFileCount == null ? 0 : this.maxFileCount.intValue();
		parts.notUseComment = this.notUseComment;

		// デザイン用サンプル行数を生成
		parts.rows.clear();
		if (ctx.renderMode == RenderMode.DESIGN || ctx.renderMode == RenderMode.PREVIEW) {
			for (int i = 0; i < this.initSampleCount; i++) {
				PartsAttachFileRow row = new PartsAttachFileRow();
				row.fileName = "sample.txt";
				row.comments = "this is a sample.";
				row.sortOrder = i + 1;
				row.partsAttachFileWfId = (long)-row.sortOrder;
				parts.rows.add(row);
			}
		}
		// 許可されたファイル名の正規表現
		parts.fileRegExp = new PartsFileLimitation(this).toRegExp();

		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@Override
	public String[] extFieldNames() {
		return extFieldNames;
	}

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 * @return
	 */
	@Override
	public List<PartsColumn> newColumns() {
		return new ArrayList<>();	// 添付ファイルの実体は ワークフロー添付ファイル情報(MWT_ATTACH_FILE_WF)にある
	}


	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {
	}

	/** パーツ読込後の最終調整処理 */
	@Override
	public void afterLoad() {
		// イベント
		int i = 0;
		if (events.isEmpty()) {
			events.add(new PartsEvent(BEFORE_SELECT, ++i));
			events.add(new PartsEvent(AFTER_SELECT, ++i));
			events.add(new PartsEvent(BEFORE_CLEAR, ++i));
			events.add(new PartsEvent(AFTER_CLEAR, ++i));
		}
		events.sort((e1, e2) -> compareTo(e1.sortOrder, e2.sortOrder));
	}
}
