package jp.co.nci.iwf.component.tray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * トレイ系画面でのCSVダウンロード項目定義
 */
public class TrayCsvDef extends MiscUtils {
	/** 「申請文書のヘッダ部」に相当するコンテナのテーブル名 */
	private String tblNameHeader;
	/** 「申請文書の明細部」に相当するコンテナのテーブル名 */
	private String tblNameDetail;
	/** トレイ設定の検索結果定義 */
	private List<TrayResultDef> defs;
	/** 「申請文書のヘッダ部」に相当するコンテナのカラム定義 */
	private List<PartsColumn> pcHeaders = new ArrayList<>(64);
	/** 「申請文書の明細部」に相当するコンテナのカラム定義 */
	private List<PartsColumn> pcDetails = new ArrayList<>(64);
	/** 「申請文書（ヘッダ部＋明細部）」のカラム定義 */
	public LinkedHashMap<String, String> columns = new LinkedHashMap<>(64);
	/** 申請文書のユーザデータを求めるためのSQL */
	public String sql;

	/** ヘッダ項目のカラム名に付与するプレフィックス */
	private static final String HED_COLUMN_PREFIX = "H_";
	/** 明細項目のカラム名に付与するプレフィックス */
	private static final String DTL_COLUMN_PREFIX = "D_";

	/** 共通項目のタイトルに付与するプレフィックス */
	private static final String CMN_TITLE_PREFIX = "[共通]";
	/** ヘッダ項目のタイトルに付与するプレフィックス */
	private static final String HED_TITLE_PREFIX = "[基本]";
	/** 明細項目のタイトルに付与するプレフィックス */
	private static final String DTL_TITLE_PREFIX = "[明細]";

	/**
	 * コンストラクタ
	 * @param defs トレイ設定の検索結果定義
	 * @param screenId 申請文書の画面ID
	 * @param ctx デザイナコンテキスト
	 */
	public TrayCsvDef(List<TrayResultDef> defs, Long screenId) {
		this.defs = defs;

		// パース
		parse(screenId);

		// DMM対応一覧NCI No.762対応
		// ヘッダと明細でカラム名が同一の場合、どちらかしか表示されなくなってしまう
		// それを回避するためヘッダには"H_"を、明細には"D_"をプレフィックスとして付与してやる
		// なおDMMではOracle12C R2を使用しているためカラム名の長さ30文字制限については考えない
		// 2019/07/25時点での調査では画面デザイナーで作成している画面のカラム名の最大長は25だった
		// ヘッダ
		for (PartsColumn pc : pcHeaders) {
			columns.put(HED_COLUMN_PREFIX + pc.columnName, HED_TITLE_PREFIX + pc.comments);
		}
		// 明細
		for (PartsColumn pc : pcDetails) {
			columns.put(DTL_COLUMN_PREFIX + pc.columnName, DTL_TITLE_PREFIX + pc.comments);
		}
		// SQL
		sql = toSql();
	}

	/** CSVヘッダ部のラベルリストを返す */
	public List<String> toTitles() {
		List<String> labels = new ArrayList<>();
		// 共通部
		for (TrayResultDef def : defs) {
			labels.add(CMN_TITLE_PREFIX + def.businessInfoName);
		}
		// 申請文書内容
		for (String columnName : columns.keySet()) {
			labels.add(columns.get(columnName));
		}
		return labels;
	}

	/** 画面定義からヘッダ部と明細部のカラム定義を解析 */
	private void parse(Long screenId) {
		if (screenId != null) {
			// 画面定義のロード
			final DesignerContext ctx = DesignerContext.designInstance(ViewWidth.LG);
			final ScreenLoadService screenLoaderService = CDI.current().select(ScreenLoadService.class).get();
			screenLoaderService.loadScreenParts(screenId, ctx);

			if (!ctx.designMap.isEmpty()) {
				// ヘッダ部のテーブル定義とカラム定義
				PartsDesignContainer container = null;
				this.tblNameHeader = ctx.root.tableName;
				for (Long partsId : ctx.root.childPartsIds) {
					final PartsDesign d = ctx.designMap.get(partsId);
					if (isOutputable(d)) {
						pcHeaders.addAll(d.columns);
					}

					// グリッド／リピーターは明細行として最後に出力するため、いったん退避
					if (container == null && in(d.partsType, PartsType.GRID, PartsType.REPEATER)) {
						container = (PartsDesignContainer)d;
					}
				}
				// 明細部のテーブル定義とカラム定義
				if (container != null) {
					this.tblNameDetail = container.tableName;
					for (Long childPartsId : container.childPartsIds) {
						final PartsDesign child = ctx.designMap.get(childPartsId);
						// 「明細行の中の明細」は取り込まない
						if (child instanceof PartsDesignChildHolder)
							continue;
						// 隠し項目は取り込まない
						if (isOutputable(child)) {
							pcDetails.addAll(child.columns);
						}
					}
				}
			}
		}
	}

	/** CSV出力対象パーツか */
	private boolean isOutputable(PartsDesign d) {
		return !d.columns.isEmpty()
				&& (d.partsType != PartsType.TEXTBOX || !((PartsDesignTextbox)d).renderAsHidden);
	}

	/** ユーザデータ抽出用のSQL生成 */
	private String toSql() {
		// SELECT句
		final StringBuilder s = new StringBuilder(512);
		s.append("select H.CORPORATION_CODE, H.PROCESS_ID, H.RUNTIME_ID");
		for (PartsColumn pc : pcHeaders) {
			s.append(", H.").append(pc.columnName).append(" as ").append(HED_COLUMN_PREFIX).append(pc.columnName);
		}
		for (PartsColumn pc : pcDetails) {
			s.append(", D.").append(pc.columnName).append(" as ").append(DTL_COLUMN_PREFIX).append(pc.columnName);
		}

		// FROM句
		if (pcDetails.isEmpty())
			s.append(" from ").append(this.tblNameHeader).append(" H");
		else
			s.append(String.format(" from %s H left join %s D on D.PARENT_RUNTIME_ID = H.RUNTIME_ID",
					this.tblNameHeader, this.tblNameDetail));

		// WHERE句
		s.append(" where H.CORPORATION_CODE = ? and H.PROCESS_ID = ? and H.DELETE_FLAG = '0'");

		// ORDER BY句
		if (pcDetails.isEmpty())
			s.append(" order by H.RUNTIME_ID");
		else
			s.append(" order by H.RUNTIME_ID, D.PARENT_RUNTIME_ID, D.SORT_ORDER, D.RUNTIME_ID");
		return s.toString();
	}
}
