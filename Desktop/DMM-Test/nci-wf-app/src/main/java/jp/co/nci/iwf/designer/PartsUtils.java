package jp.co.nci.iwf.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.designer.DesignerCodeBook.FontSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAttachFile;
import jp.co.nci.iwf.designer.parts.design.PartsDesignCheckbox;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignDropdown;
import jp.co.nci.iwf.designer.parts.design.PartsDesignEventButton;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.design.PartsDesignHyperlink;
import jp.co.nci.iwf.designer.parts.design.PartsDesignImage;
import jp.co.nci.iwf.designer.parts.design.PartsDesignLabel;
import jp.co.nci.iwf.designer.parts.design.PartsDesignMaster;
import jp.co.nci.iwf.designer.parts.design.PartsDesignNumbering;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOrganizationSelect;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRadio;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRepeater;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignSearchButton;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStamp;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStandAlone;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.design.PartsDesignUserSelect;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFile;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsCheckbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsDropdown;
import jp.co.nci.iwf.designer.parts.runtime.PartsEventButton;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsHyperlink;
import jp.co.nci.iwf.designer.parts.runtime.PartsImage;
import jp.co.nci.iwf.designer.parts.runtime.PartsLabel;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.designer.parts.runtime.PartsNumbering;
import jp.co.nci.iwf.designer.parts.runtime.PartsOrganizationSelect;
import jp.co.nci.iwf.designer.parts.runtime.PartsRadio;
import jp.co.nci.iwf.designer.parts.runtime.PartsRepeater;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsSearchButton;
import jp.co.nci.iwf.designer.parts.runtime.PartsStamp;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsUserSelect;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツユーティリティ
 */
public class PartsUtils extends MiscUtils {
//	private static final ObjectMapper om = JacksonConfig.getObjectMapper();

	/**
	 * HtmlIdを生成
	 * @param parts HtmlIdを与えたいパーツ
	 * @param parent 親コンテナ
	 * @param rowId 行ID
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public static String toHtmlId(PartsBase<?> parts, PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		// ルートコンテナは直接レンダリングされることがないので、HtmlIdもない
		if (parts instanceof PartsRootContainer)
			return "";

		final PartsDesign design = ctx.designMap.get(parts.partsId);
		final String partsCode = design.partsCode;

		// ルートコンテナ配下のパーツはHtmlId＝パーツコード
		if (parent == null
				|| parent instanceof PartsRootContainer
				|| isEmpty(parent.htmlId)) {
			return partsCode;
		}

		// 親コンテナのHtmlIdを流用して生成。
		if (rowId == null) {
			return parent.htmlId + "_" + partsCode;
		}
		return parent.htmlId + "-" + rowId + "_" + partsCode;
	}

	/**
	 * HtmlIdから親コンテナ(パーツ)のHtmlIdを戻す.
	 * @param htmlId
	 * @return
	 */
	public static String getParentHtmlId(String htmlId) {
		// パーツのHtmlIdは「親コンテナのHtmlId-行ID(ｎ)_…_パーツコード」のような形式になっている
		// お尻のほうからパーツ間の区切り文字「_」および明細行の区切り文字「-」を除去し、親パーツのHtmlIdを取得する
		if (StringUtils.contains(htmlId, "_")) {
			return StringUtils.substringBeforeLast(StringUtils.substringBeforeLast(htmlId, "_"), "-");
		}
		return null;
	}

	/**
	 * HtmlIdから親コンテナ(パーツ)のHtmlIdを行ID付で戻す.
	 * （例： CTNR0001-1_BTN0001なら CTNR0001-1を返す）
	 * @param htmlId
	 * @return
	 */
	public static String getParentHtmlIdWithRowId(String htmlId) {
		String parentHtmlId = null;
		if (isNotEmpty(htmlId)) {
			int pos = htmlId.lastIndexOf('_');
			if (pos > -1) {
				parentHtmlId = htmlId.substring(0, pos);
			}
		}
		return parentHtmlId;
	}

	/**
	 * HtmlIdから親コンテナの行IDを返す
	 * @param htmlId
	 * @return
	 */
	public static Integer getParentRowId(String htmlId) {
		if (StringUtils.contains(htmlId, "_")) {
			String rowId = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(htmlId, "_"), "-");
			return (rowId == null ? null :Integer.valueOf(rowId));
		}
		return null;
	}

	/**
	 * HtmlIdから親を含めた祖先コンテナ(パーツ)のHtmlId一覧を戻す.
	 * @param htmlId
	 * @return
	 */
	public static List<String> getParentHtmlIds(String htmlId) {
		List<String> list = new ArrayList<String>();
		String parentHtmlId = htmlId;
		while ((parentHtmlId = getParentHtmlId(parentHtmlId)) != null) {
			list.add(parentHtmlId);
		}
//		return list.isEmpty() ? null : list;
		return list;
	}

	/**
	 * パーツ種別からパーツクラスをかえす
	 * @param partsType パーツ種別
	 * @return
	 */
	public static Class<? extends PartsBase<?>> toPartsClass(int partsType) {
		switch (partsType) {
		case PartsType.TEXTBOX: return PartsTextbox.class;
		case PartsType.LABEL: return PartsLabel.class;
		case PartsType.HYPERLINK: return PartsHyperlink.class;
		case PartsType.ATTACHFILE: return PartsAttachFile.class;
		case PartsType.IMAGE: return PartsImage.class;
		case PartsType.CHECKBOX: return PartsCheckbox.class;
		case PartsType.RADIO: return PartsRadio.class;
		case PartsType.DROPDOWN: return PartsDropdown.class;
		case PartsType.USER: return PartsUserSelect.class;
		case PartsType.ORGANIZATION: return PartsOrganizationSelect.class;
		case PartsType.REPEATER: return PartsRepeater.class;
		case PartsType.STAND_ALONE: return PartsStandAlone.class;
		case PartsType.GRID: return PartsGrid.class;
		case PartsType.ROOT_CONTAINER: return PartsRootContainer.class;
		case PartsType.NUMBERING: return PartsNumbering.class;
		case PartsType.STAMP: return PartsStamp.class;
		case PartsType.SEARCH_BUTTON: return PartsSearchButton.class;
		case PartsType.EVENT_BUTTON: return PartsEventButton.class;
		case PartsType.MASTER: return PartsMaster.class;
		}
		throw new BadRequestException("パーツ種別に対応したパーツが未定義です。partsType=" + partsType);
	}

	/**
	 * パーツ種別からパーツ定義クラスを返す
	 * @param partsType パーツ種別
	 * @return
	 */
	public static Class<? extends PartsDesign> toPartsDesignClass(int partsType) {
		switch (partsType) {
		case PartsType.TEXTBOX: return PartsDesignTextbox.class;
		case PartsType.LABEL: return PartsDesignLabel.class;
		case PartsType.HYPERLINK: return PartsDesignHyperlink.class;
		case PartsType.ATTACHFILE: return PartsDesignAttachFile.class;
		case PartsType.IMAGE: return PartsDesignImage.class;
		case PartsType.CHECKBOX: return PartsDesignCheckbox.class;
		case PartsType.RADIO: return PartsDesignRadio.class;
		case PartsType.DROPDOWN: return PartsDesignDropdown.class;
		case PartsType.USER: return PartsDesignUserSelect.class;
		case PartsType.ORGANIZATION: return PartsDesignOrganizationSelect.class;
		case PartsType.REPEATER: return PartsDesignRepeater.class;
		case PartsType.STAND_ALONE: return PartsDesignStandAlone.class;
		case PartsType.GRID: return PartsDesignGrid.class;
		case PartsType.ROOT_CONTAINER: return PartsDesignRootContainer.class;
		case PartsType.NUMBERING: return PartsDesignNumbering.class;
		case PartsType.STAMP: return PartsDesignStamp.class;
		case PartsType.SEARCH_BUTTON: return PartsDesignSearchButton.class;
		case PartsType.EVENT_BUTTON: return PartsDesignEventButton.class;
		case PartsType.MASTER: return PartsDesignMaster.class;
		}
		throw new BadRequestException("パーツ種別に対応したパーツ定義がありません。partsType=" + partsType);
	}

	/**
	 * パーツコードを生成
	 * @param partsType パーツ種別
	 * @param partsCodeSeq パーツコードの元となる連番
	 * @return
	 */
	public static String toPartsCode(int partsType, int partsCodeSeq) {
		String prefix = null;
		switch (partsType) {
		case PartsType.TEXTBOX: prefix = "TXT"; break;
		case PartsType.LABEL: prefix = "LBL"; break;
		case PartsType.HYPERLINK: prefix = "LNK"; break;
		case PartsType.ATTACHFILE: prefix = "ATT"; break;
		case PartsType.IMAGE: prefix = "IMG"; break;
		case PartsType.CHECKBOX: prefix = "CHK"; break;
		case PartsType.RADIO: prefix = "RAD"; break;
		case PartsType.DROPDOWN: prefix = "DDL"; break;
		case PartsType.USER: prefix = "USR"; break;
		case PartsType.ORGANIZATION: prefix = "ORG"; break;
		case PartsType.REPEATER: prefix = "RPT"; break;
		case PartsType.STAND_ALONE: prefix = "SAS"; break;
		case PartsType.GRID: prefix = "GRD"; break;
		case PartsType.ROOT_CONTAINER: prefix = "CNTR"; break;
		case PartsType.NUMBERING: prefix = "NMB"; break;
		case PartsType.STAMP: prefix = "STP"; break;
		case PartsType.SEARCH_BUTTON: prefix = "BTN"; break;
		case PartsType.EVENT_BUTTON: prefix = "EVT"; break;
		case PartsType.MASTER: prefix = "MST"; break;
		default:
			throw new BadRequestException("パーツ種別に対応したパーツ定義がありません。partsType=" + partsType);
		}
		return String.format("%s%04d", prefix, partsCodeSeq);
	}

	/**
	 * 対象パーツのデザインコードを返す
	 * @param d
	 * @param designMap
	 * @return
	 */
	public static String toDesignCode(PartsDesign d, PartsDesignContainer c) {
		String partsCode = d.partsCode;
		String parentDesignCode = (c == null || c instanceof PartsDesignRootContainer) ?
				null : defaults(c.designCode, c.containerCode, c.partsCode);
		return toDesignCode(parentDesignCode, partsCode);
	}

	/**
	 * 対象パーツのデザインコードを返す
	 * @param d
	 * @param designMap
	 * @return
	 */
	public static String toDesignCode(String parentDesignCode, String partsCode) {
		if (isEmpty(parentDesignCode))
			return partsCode;
		return parentDesignCode + "_" + partsCode;
	}

	/**
	 * 新しいパーツ定義を生成
	 * @param partsType パーツ種別
	 * @param root ルートコンテナ
	 * @return
	 */
	public static PartsDesign newPartsDesign(final int partsType, final DesignerContext ctx) {
		final Class<? extends PartsDesign> c = PartsUtils.toPartsDesignClass(partsType);
		final PartsDesign design = newInstance(c);
		final PartsDesignRootContainer root = ctx.root;
		design.containerId = root == null ? null : root.containerId;
		design.partsType = partsType;
		design.partsId = nextPartsId();
		design.partsCode = nextPartsCode(partsType, ctx);
		design.designCode =  PartsUtils.toDesignCode(design, root);
		design.fontSize = FontSize.Inherit;

		design.copyTargetFlag = true;
		design.grantTabIndexFlag = true;
		design.labelText = design.partsCode;
		design.sortOrder = root == null ? 0 : root.partsCodeSeq;
		design.renderingMethod = RenderingMethod.BOOTSTRAP_GRID;

		// 初期値の付与
		design.setInitValue();
		design.columns = design.newColumns();

		// パーツ初期値設定後の最終調整処理
		design.afterLoad();

		return design;
	}

	/** パーツコードを採番 */
	private static String nextPartsCode(final int partsType, final DesignerContext ctx) {
		final PartsDesignRootContainer root = ctx.root;
		Set<String> partsCodes = ctx.designMap.values().stream()
				.map(d -> d.partsCode)
				.collect(Collectors.toSet());

		// 使用されていないパーツコードを探す
		int i = (root == null ? 1 : root.partsCodeSeq);
		while (i < 10000) {	// パーツコードの連番部分は4桁である
			final String partsCode = PartsUtils.toPartsCode(partsType, ++i);
			if (!partsCodes.contains(partsCode)) {
				root.partsCodeSeq = i;
				return partsCode;
			}
		}
		throw new InternalServerErrorException("空きパーツコードがありません。");
	}

	/**
	 * パーツ定義をコピー
	 * @param container
	 * @param src
	 * @param ctx
	 * @return
	 */
	public static PartsDesign copyPartsDesign(PartsDesignContainer container, PartsDesign src, DesignerContext ctx) {
		if (src == null) throw new BadRequestException("コピー元パーツ定義が存在しません");

		// ここで得られたパーツはサーバ側であり、このインスタンスを改変しても
		// クライアント側のパーツには何の影響もない。
		// よってパーツ定義ID等だけを書き換えれば簡単にコピーを作成できる
		final Class<? extends PartsDesign> c = PartsUtils.toPartsDesignClass(src.partsType);
		final PartsDesign design = newInstance(c);
		copyFields(src, design);

		design.partsId = nextPartsId();
		design.partsCode = PartsUtils.toPartsCode(design.partsType, container.partsCodeSeq);
		design.designCode = PartsUtils.toDesignCode(design, ctx.root);
		design.columns = design.newColumns();

		// ラベル文言の末尾に '~copy'を付与（長すぎるならトリム）
		design.labelText = trunc(design.labelText + "~copy", 100);

		return design;
	}

	/**
	 * デザイン時のパーツMapを再構築する。
	 * ルートコンテナから子コンテナを経由して辿りつけるパーツをMap化することで、
	 * 削除されたり、子コンテナIDを変更したことでゴミが混在するようになってしまったパーツMapを正常化するために使用される。
	 * @param ctx デザイナーコンテキスト
	 * @param partsIds 調査対象のパーツIDリスト
	 * @param excludePartsId 追加しないパーツのパーツID（例えば削除することが決まっているパーツとか）
	 * @return
	 */
	public static Map<Long, PartsDesign> rebuildDesignMap(DesignerContext ctx, List<Long> partsIds, Long excludePartsId) {
		final Map<Long, PartsDesign> map = new HashMap<>(64);
		for (Long partsId : partsIds) {
			final PartsDesign d = ctx.designMap.get(partsId);
			if (d != null) {
				// 除外パーツIDに指定されていなければ追加
				if (excludePartsId == null || !excludePartsId.equals(partsId)) {
					map.put(partsId, d);
				}
				// コンテナなら子要素分を再帰呼び出し
				if (d instanceof PartsDesignChildHolder) {
					PartsDesignChildHolder child = (PartsDesignChildHolder)d;
					map.putAll(rebuildDesignMap(ctx, child.childPartsIds, excludePartsId));
				}
			}
		}
		return map;
	}

	/** パーツIDの採番 */
	private static long nextPartsId() {
		NumberingService numbering = CDI.current().select(NumberingService.class).get();
		return numbering.newPK(MwmPart.class);
	}

	/**
	 *  パーツ定義のカラム定義を新たに生成し、既存とマージ
	 * @param design
	 */
	public static void mergeColumns(PartsDesign design) {
		final List<PartsColumn> news = design.newColumns();
		final List<PartsColumn> olds = design.columns;
		// カラム定義が複数ある場合、roleCodeが必須、かつカラム定義毎に一意であること
		// 上記条件に合致しない場合はエラーで落とす
		if (news.size() > 1) {
			if (news.stream().anyMatch(pc -> isEmpty(pc.roleCode)))
				throw new InternalServerErrorException("PartsColumn#roleCodeは必須です。");
			Set<String> roleCodes = news.stream().map(pc -> pc.roleCode).collect(Collectors.toSet());
			if (news.size() != roleCodes.size())
				throw new InternalServerErrorException("PartsColumn#roleCodeが重複しています。");
		}
		for (int i = 0; i < news.size(); i++) {
			final PartsColumn pc = news.get(i);
			// oldsから同一のroleCode（お互いnull同士でもOK）を持つカラム定義を取得
			final PartsColumn old = olds.stream()
					.filter(c -> MiscUtils.eq(c.roleCode, pc.roleCode))
					.findFirst()
					.orElse(null);
			if (old != null) {
				pc.columnName = old.columnName;
				pc.sortOrder = old.sortOrder;
				pc.columnTypeLabel = pc.toString();
			}
		}
		design.columns = news;
	}

	/**
	 * ラベルに関連付けられたパーツがあれば、それらの間でラベル文言を同期
	 * @param label
	 * @param designMap
	 */
	public static void syncLabelText(PartsDesignLabel label, Map<Long, ? extends PartsDesign> designMap) {
		if (label.partsIdFor != null) {
			final PartsDesign d = designMap.get(label.partsIdFor);
			if (d != null) {
				d.labelText = label.labelText;

				// カラム定義のラベルも同期させる
				mergeColumns(d);
			}
		}
	}

	/**
	 * コンテナ固有のカスタムCSSスタイルを、再帰的にすべてのコンテナから収集する
	 * @param styles 収集先の文字バッファ
	 * @param c コンテナ
	 * @param ctx デザイナコンテキスト
	 */
	public static String toCustomStyles(DesignerContext ctx) {
		HtmlStringBuilder html = new HtmlStringBuilder();
		collectCustomStyles(html, ctx.root, ctx);
		return html.toString();
	}

	/**
	 * コンテナ固有のカスタムCSSスタイルを、再帰的にすべてのコンテナから収集する
	 * @param styles 収集先の文字バッファ
	 * @param c コンテナ
	 * @param ctx デザイナコンテキスト
	 */
	public static void collectCustomStyles(HtmlStringBuilder styles, PartsDesignContainer c, DesignerContext ctx) {
		if (isNotEmpty(c.customCssStyle) )
			styles.append(c.customCssStyle).append(CodeBook.CRLF);

		for (Long partsId : c.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);
			if (d instanceof PartsDesignContainer) {
				collectCustomStyles(styles, (PartsDesignContainer)d, ctx);
			}
		}
	}

	/**
	 * コンテナの行コピー
	 * @param srcRow
	 * @param newRow
	 * @param ctx
	 * @param copyAll 無条件で全コピーするならtrue、コピー起票の対象＝trueのパーツだけコピーするならfalse
	 */
	public static void copyRow(PartsContainerRow srcRow, PartsContainerRow newRow, DesignerContext ctx, boolean copyAll) {
		assert(srcRow != null && srcRow.children != null);
		assert(newRow != null && newRow.children != null);
		assert(srcRow.children.size() == newRow.children.size());

		for (int i = 0; i < srcRow.children.size(); i++) {
			String srcHtmlId = srcRow.children.get(i);
			String newHtmlId = newRow.children.get(i);
			PartsBase<?> pNew = ctx.runtimeMap.get(newHtmlId);
			PartsBase<?> pSrc = ctx.runtimeMap.get(srcHtmlId);
			assert(pNew != null);
			assert(pSrc != null);
			PartsDesign d = ctx.designMap.get(pNew.partsId);
			assert(d != null);
			if (copyAll || d.copyTargetFlag)
				// 明示的に無条件コピーが指定されているか、パーツがコピー起票対象なら丸ごとコピー
				pNew.values = pSrc.values;
			else {
				// コピー起票対象でなければ、クリアしてデフォルト値をセット
				pNew.clearAndSetDefaultValue(d);
			}
			// 子要素があれば再帰呼び出し
			if (pNew instanceof PartsContainerBase) {
				PartsContainerBase<?> cNew = (PartsContainerBase<?>)pNew;
				PartsContainerBase<?> cSrc = (PartsContainerBase<?>)pSrc;

				// コピー元／コピー先でコンテナ内の行数が異なるとコピーに過不足が出てしまうので、
				// コンテンツ行数を合わせる
				PartsUtils.adjustRowCount(cNew, ctx, cSrc.rows.size());
				assert(cNew.rows.size() == cSrc.rows.size());

				for (int n = 0; n < cNew.rows.size(); n++) {
					copyRow(cSrc.rows.get(n), cNew.rows.get(n), ctx, copyAll);
				}
			}
		}
	}

	/**
	 * コンテナの行数を指定値となるまで行追加／行削除
	 * @param container コンテナ
	 * @param ctx デザイナコンテキスト
	 * @param newLineCount 新しい行数
	 */
	public static void adjustRowCount(PartsContainerBase<?> container, DesignerContext ctx, int newLineCount) {
		assert(container != null);

		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(container.partsId);
		assert(d != null);

		// 指定行に満たなければ空行を足す
		int count = Math.max(newLineCount, d.minRowCount);
		while (container.rows.size() < count) {
			d.addRows(container, ctx);
		}

		// 既存が指定行以上なら末尾から削除する
		final Set<String> deleteHtmlIds = new HashSet<>();
		while (container.rows.size() > count) {
			PartsContainerRow row = container.rows.remove(container.rows.size() - 1);
			if (row != null) {
				final String prefix = container.htmlId + "-" + row.rowId;
				deleteHtmlIds.add(prefix);
			}
		}

		// ランタイムMapから対象パーツとその配下を削除
		PartsUtils.deleteRuntime(deleteHtmlIds, ctx.runtimeMap);

		// ページ番号の補正
		int pageCount = container.calcPageCount();
		if (container.pageNo > pageCount)
			container.pageNo = pageCount;

		// 並び順を振り直し
		int i = 0;
		for (PartsContainerRow row : container.rows) {
			row.sortOrder = ++i;
		}
	}

	/**
	 * 行IDの位置するページ番号を返す
	 * @param rowId 行ID
	 * @param pageSize ページあたりの行数
	 * @return
	 */
	public static int getPageNo(Integer rowId, Integer pageSize) {
		if (rowId == null || pageSize == null || pageSize < 1)
			return -1;
		if (rowId < 1)
			return 0;
		Double pageNo = Math.ceil(rowId.doubleValue() / pageSize.doubleValue());
		return pageNo.intValue();
	}

	/**
	 * 「指定パーツIDをもつ」＋「基準パーツと同一or下位コンテナに存在する」パーツを求める
	 * @param partsId 対象パーツID
	 * @param baseHtmlId 基準パーツのhtmlId
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public static PartsBase<?> findParts(long partsId, String baseHtmlId, DesignerContext ctx) {
		// 親のhtmlId
		String parentHtmlId = getParentHtmlIdWithRowId(baseHtmlId);
		// 「基準パーツと同一or下位コンテナに存在する」＝同じ親のhtmlIdを含んでいる
		// ただしbaseHtmlIdが「CTNR0001-1_xxx」だと親のHtmlIdが「CTNR0001-1」となるため、
		//「CTNR0001-10_xxx」も同じと判断されてしまう
		// よって親のHtmlIdに"_"を付与した形で判定を行うようにする
		String prefix = null;
		if (StringUtils.isNotEmpty(parentHtmlId))
			prefix = parentHtmlId + "_";

		// 基準パーツと同じコンテナ上かその配下コンテナにある指定パーツIDを探す
		for (PartsBase<?> p  : ctx.runtimeMap.values()) {
			if (p.partsId == partsId && (isEmpty(prefix) || p.htmlId.startsWith(prefix))) {
				return p;
			}
		}
		return null;
	}

	/**
	 * 対象パーツHtmlIdとその配下パーツをランタイムMapから削除
	 * @param deleteHtmlIds 削除対象パーツのHtmlIdリスト
	 * @param runtimeMap ランタイムMap
	 */
	public static void deleteRuntime(Set<String> deleteHtmlIds, Map<String, PartsBase<?>> runtimeMap) {
		for (String deleteHtmlId : deleteHtmlIds) {
			// 「基準パーツと同一or下位コンテナに存在する」＝同じ親のhtmlIdを含んでいる
			// ただしbaseHtmlIdが「CTNR0001-1_xxx」だと親のHtmlIdが「CTNR0001-1」となるため、
			//「CTNR0001-10_xxx」も同じと判断されてしまう
			// よって親のHtmlIdに"_"を付与した形で判定を行うようにする
			final String prefix = deleteHtmlId + "_";
			for (Iterator<String> it = runtimeMap.keySet().iterator(); it.hasNext(); ) {
				String htmlId = it.next();
				if (eq(htmlId, deleteHtmlId) || htmlId.startsWith(prefix))
					it.remove();
			}
		}
	}

	/**
	 * 大きすぎる、または小さすぎるページNoの補正
	 * @param total 総行数
	 * @param pageSize ページあたりの行数
	 * @param pageNo ページNo
	 * @return
	 */
	public static int adjustPageNo(int total, int pageSize, int pageNo) {
		int pageCount = PartsUtils.getPageNo(total, pageSize);
		return adjustPageNo(pageCount, pageNo);
	}

	/**
	 * 大きすぎる、または小さすぎるページNoの補正
	 * @param total 総行数
	 * @param pageSize ページあたりの行数
	 * @param pageNo ページNo
	 * @return
	 */
	public static int adjustPageNo(int pageCount, int pageNo) {
		if (pageNo > pageCount)
			pageNo = pageCount;
		else if (pageNo < 1)
			pageNo = 1;
		return pageNo;
	}

	/** パーツ種別に対するパーツ種別名を返す */
	public static String getPartsTypeName(int partsType) {
		final I18nService i18n = CDI.current().select(I18nService.class).get();
		switch (partsType) {
		case PartsType.ATTACHFILE: return i18n.getText(MessageCd.attachFile);
		case PartsType.IMAGE: return i18n.getText(MessageCd.picture);
		case PartsType.SEARCH_BUTTON: return i18n.getText(MessageCd.searchButton);
		case PartsType.EVENT_BUTTON: return i18n.getText(MessageCd.eventButton);
		case PartsType.CHECKBOX: return i18n.getText(MessageCd.checkbox);
		case PartsType.DROPDOWN: return i18n.getText(MessageCd.dropdown);
		case PartsType.GRID : return i18n.getText(MessageCd.grid);
		case PartsType.HYPERLINK: return i18n.getText(MessageCd.hyperlink);
		case PartsType.LABEL: return i18n.getText(MessageCd.label);
		case PartsType.MASTER: return i18n.getText(MessageCd.masterSelector);
		case PartsType.NUMBERING: return i18n.getText(MessageCd.numbering);
		case PartsType.ORGANIZATION: return i18n.getText(MessageCd.organizationSelect);
		case PartsType.RADIO: return i18n.getText(MessageCd.radio);
		case PartsType.REPEATER: return i18n.getText(MessageCd.repeater);
		case PartsType.STAND_ALONE: return i18n.getText(MessageCd.partsStandAloneScreen);
		case PartsType.ROOT_CONTAINER: return i18n.getText(MessageCd.containerInfo);
		case PartsType.STAMP: return i18n.getText(MessageCd.stamp);
		case PartsType.TEXTBOX: return i18n.getText(MessageCd.textbox);
		case PartsType.USER: return i18n.getText(MessageCd.userSelect);
		default: return "(Unknown)";
		}
	}

	/** コンテナに対して配下のデザインコードを書き換える */
	public static void refreshDesignCode(PartsDesignContainer c, DesignerContext ctx) {
		// 配下のデザインコードを書き換え
		for (Long partsId : c.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);
			d.partsCode = toDesignCode(d, c);

			// 配下がコンテナがあれば孫も書き換え
			if (d instanceof PartsDesignContainer) {
				refreshDesignCode((PartsDesignContainer)d, ctx);
			}
		}
	}
}
