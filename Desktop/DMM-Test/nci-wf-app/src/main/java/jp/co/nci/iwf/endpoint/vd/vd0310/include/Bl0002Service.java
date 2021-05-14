package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import org.apache.commons.lang3.NotImplementedException;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.ApplicationStatus;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.tableSearch.TableSearchEntity;
import jp.co.nci.iwf.component.tableSearch.TableSearchService;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAttachFile;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.runtime.IPartsPaging;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFile;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFileRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.PartsValidationService;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;

/**
 * ブロック：文書内容のサービス
 */
@BizLogic
public class Bl0002Service extends BaseService {
	@Inject protected PartsRenderFactory factory;
	@Inject private ScreenLoadService screenLoadService;
	@Inject private UserDataService userDataService;
	@Inject private PartsValidationService validator;
	@Inject private TableSearchService tsService;

	/**
	 * コンテナへの空行追加
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse addEmptyLine(Bl0002PartsRequest req) {
		// デザイナコンテキストの生成
		final DesignerContext ctx = getDesignContext(req);

		// 対象パーツに行追加
		final PartsContainerBase<?> p = (PartsContainerBase<?>)ctx.runtimeMap.get(req.htmlId);
		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(p.partsId);
		d.addRows(p, ctx);

		// 並び順を振り直し
		int i = 0;
		for (PartsContainerRow row : p.rows) {
			row.sortOrder = ++i;
		}
		// 対象パーツの親コンテナの条件判定結果を格納
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();

		// 差分HTMLをレンダリング
		final String html = factory.renderDiff(p, d, ctx, ecCache);

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/** プレビュー/申請時/実行時を切り分けたデザイナーコンテキストを取得 */
	protected DesignerContext getDesignContext(Bl0002PartsRequest req) {
		// プレビュー時は変更中の内容がリクエストとして送信されてくる。
		// 実行時/申請時はデータベースから読み直す。
		DesignerContext ctx = null;
		if (req.previewContext != null) {
			// リクエストのを流用
			ctx = req.previewContext;
		} else {
			// パラメータをもとにContextを再生成して、パーツ定義をDBから読み直し
			ctx = RuntimeContext.newInstance(req.contents, req.viewWidth, req.runtimeMap);
			screenLoadService.loadScreenParts(ctx.screenId, ctx);
		}
		ctx.runtimeMap = req.runtimeMap;
		ctx.viewWidth = req.viewWidth;

		return ctx;
	}

	/**
	 * コンテナへの行の削除
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse deleteLine(Bl0002PartsRequest req) {
		// デザイナコンテキストの生成
		final DesignerContext ctx = getDesignContext(req);

		// 対象行を削除し、並び順を振り直し
		final PartsContainerBase<?> p = (PartsContainerBase<?>)ctx.runtimeMap.get(req.htmlId);
		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(p.partsId);
		int i = 0;
		final Set<String> deleteHtmlIds = new HashSet<>();
		for (Iterator<PartsContainerRow> it = p.rows.iterator(); it.hasNext(); ) {
			final PartsContainerRow row = it.next();
			final String prefix = p.htmlId + "-" + row.rowId;
			if (req.rowHtmlIds.contains(prefix)) {
				it.remove();
				deleteHtmlIds.add(prefix);
				continue;
			}
			row.sortOrder = ++i;
		}
		// ランタイムMapから対象パーツとその配下を削除
		PartsUtils.deleteRuntime(deleteHtmlIds, ctx.runtimeMap);

		// 最低行数に満たなければ空行を足す
		final int min = Math.min(d.initRowCount, d.minRowCount);
		while (p.rows.size() < min) {
			d.addRows(p, ctx);
		}

		// ページ番号の補正
		int pageCount = p.calcPageCount();
		if (p.pageNo > pageCount)
			p.pageNo = pageCount;

		// 対象パーツの親コンテナの条件判定結果を格納
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();

		// 差分HTMLをレンダリング
		final String html = factory.renderDiff(p, d, ctx, ecCache);

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/**
	 * コンテナへの行コピー
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse copyLine(Bl0002PartsRequest req) {
		// デザイナコンテキストの生成
		final DesignerContext ctx = getDesignContext(req);

		// 対象行をコピー
		final PartsContainerBase<?> p = (PartsContainerBase<?>)ctx.runtimeMap.get(req.htmlId);
		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(p.partsId);
		final PartsContainerRow[] rows = p.rows.toArray(new PartsContainerRow[p.rows.size()]);
		for (PartsContainerRow row : rows) {
			final String prefix = p.htmlId + "-" + row.rowId;
			if (req.rowHtmlIds.contains(prefix)) {
				// 空行を追加し、コピー元行の内容で上書き。ただし行IDだけは上書きされないようにする
				PartsContainerRow newRow = d.addRows(p, ctx);
				int newRowId = newRow.rowId;
				PartsUtils.copyRow(row, newRow, ctx, false);
				newRow.rowId = newRowId;
			}
		}

		// 並び順を振り直し
		int i = 0;
		for (PartsContainerRow row : p.rows) {
			row.sortOrder = ++i;
		}

		// 対象パーツの親コンテナの条件判定結果を格納
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();

		// 差分HTMLをレンダリング
		final String html = factory.renderDiff(p, d, ctx, ecCache);

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/**
	 * コンテナへの行数変更
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse changeLineCount(Bl0002PartsRequest req) {
		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		if (req.newLineCount == null)
			throw new BadRequestException("行数が未指定です");
		if (req.newLineCount > 99)
			throw new BadRequestException("行数が多すぎます：newLineCount=" + req.newLineCount);
		if (req.newLineCount < 0)
			throw new BadRequestException("行数が少なすぎます：newLineCount=" + req.newLineCount);

		// デザイナコンテキストの生成
		final DesignerContext ctx = getDesignContext(req);

		// コンテナの行数を指定値となるまで行追加／行削除
		final PartsContainerBase<?> container = (PartsContainerBase<?>)ctx.runtimeMap.get(req.htmlId);
		PartsUtils.adjustRowCount(container, ctx, req.newLineCount);

		// 対象パーツの親コンテナの条件判定結果を格納
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();

		// 差分HTMLをレンダリング
		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(container.partsId);
		final String html = factory.renderDiff(container, d, ctx, ecCache);

		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/**
	 * ページ番号の変更
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse changePageNo(Bl0002PartsRequest req) {
		// デザイナコンテキストの生成
		final DesignerContext ctx = getDesignContext(req);

		// 新しいページ番号を指定
		// changePageNoは添付ファイルパーツでも使用しているため
		// rcやrdは「PartsContainerBase」や「PartsDesignContainer」ではなく
		// より上位の基底クラスにてcastするように修正
//		final PartsContainerBase<?> rc = (PartsContainerBase<?>)ctx.runtimeMap.get(req.htmlId);
		final PartsBase<?> rc = (PartsBase<?>)ctx.runtimeMap.get(req.htmlId);
//		final PartsDesignContainer rd = (PartsDesignContainer)ctx.designMap.get(rc.partsId);
		final PartsDesign rd = (PartsDesign)ctx.designMap.get(rc.partsId);
		if (rc instanceof IPartsPaging) {
			final IPartsPaging p = (IPartsPaging)rc;
			p.adjustNewPageNo(req.newPageNo);
		}
		else {
			// 実装されていない
			throw new NotImplementedException("パーツが IPartsPaging を実装していません");
		}

		// 対象パーツの親コンテナの条件判定結果を格納
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();

		// 差分HTMLをレンダリング
		final String html = factory.renderDiff(rc, rd, ctx, ecCache);

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/** パーツ定義を読み込む */
	public RuntimeContext load(Vd0310ExecuteRequest req) {
		// DBからパーツ定義を読み込むが、ユーザデータはリクエストから取り込む
		final RuntimeContext ctx = RuntimeContext.newInstance(req);
		screenLoadService.loadScreenParts(ctx.screenId, ctx);
		return ctx;
	}

	/** パーツ定義を読み込む */
	public RuntimeContext load(Dc0100ExecuteRequest req) {
		// DBからパーツ定義を読み込むが、ユーザデータはリクエストから取り込む
		final RuntimeContext ctx = RuntimeContext.newInstance(req);
		screenLoadService.loadScreenParts(ctx.screenId, ctx);
		return ctx;
	}

	/** パーツのバリデーション */
	public List<PartsValidationResult> validate(RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		// バリデーションとバリデーション結果による親コンテナのページ番号の変更処理
		final boolean required = ActionType.NORMAL.equals(ctx.actionType);
		return validator.validate(ctx, ecResults, required);
	}

	/**
	 * パーツに紐付くユーザデータの更新処理
	 * @param req
	 * @param res
	 * @return
	 */
	public Map<String, String> update(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, EvaluateCondition> ecResults) {
		final RuntimeContext ctx = res.ctx;
		final String actionType = req.actionInfo.actionType;
		final String status = req.contents.applicationStatus;	// nullになるのは新規起案か文書管理だけ

		// パーツに紐付くユーザデータの更新
		// 未申請の取消なら物理削除、申請済みの取消なら論理削除
		if (eq(ActionType.CANCEL, actionType) && eq(ApplicationStatus.NOT_APPLIED, status))
			userDataService.deletePhysical(ctx, ecResults);
		else
			userDataService.save(ctx, ecResults);

		// ユーザデータから業務管理項目を抜き出し
		return userDataService.createBusinessInfoMap(ctx);
	}

	/**
	 * デザイナのパーツデータ（＝ユーザデータ）から業務管理項目を抜き出してMap化
	 * @param res
	 * @return
	 */
	public Map<String, String> createBusinessInfoMap(Vd0310ExecuteResponse res) {
		return userDataService.createBusinessInfoMap(res.ctx);
	}

	/**
	 * マスタ選択パーツの選択肢を抽出
	 * @param req
	 * @return
	 */
	public List<OptionItem> getMasterOptionItem(Bl0002MasterRequest req) {
		if (req.tableSearchId == null)
			throw new BadRequestException("汎用テーブル検索条件IDが未指定です");
		if (req.initConditions == null)
			throw new BadRequestException("絞り込み条件Mapが未指定です");
		if (isEmpty(req.columnNameValue))
			throw new BadRequestException("「値」カラム名が未指定です");
		if (isEmpty(req.columnNameLabel))
			throw new BadRequestException("「ラベル」カラム名が未指定です");

		List<OptionItem> items = tsService.search(req.tableSearchId, req.initConditions)
				.stream()
				.map(e -> new OptionItem(e.get(req.columnNameValue), e.get(req.columnNameLabel)))
				.collect(Collectors.toList());
//		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * マスタ選択パーツを選択したことによる検索
	 * @param req
	 * @return
	 */
	public List<TableSearchEntity> getMasterResult(Bl0002MasterRequest req) {
		if (req.tableSearchId == null)
			throw new BadRequestException("汎用テーブル検索条件IDが未指定です");
		if (req.initConditions == null)
			throw new BadRequestException("絞り込み条件Mapが未指定です");

		return tsService.search(req.tableSearchId, req.initConditions);
	}

	/**
	 * ワークフローパーツ添付ファイルの削除
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse deletePartsAttachFile(Bl0002PartsRequest req) {
		final DesignerContext ctx = getDesignContext(req);
		final PartsAttachFile p = (PartsAttachFile)ctx.runtimeMap.get(req.htmlId);
		final PartsDesignAttachFile d = (PartsDesignAttachFile)ctx.designMap.get(p.partsId);

		// 選択行の添付ファイルを削除
		if (d.multiple) {
			// 複数ファイル
			int i = 0;
			for (Iterator<PartsAttachFileRow> it = p.rows.iterator(); it.hasNext(); ) {
				final PartsAttachFileRow row = it.next();
				if (req.partsAttachFileWfIds.contains(row.partsAttachFileWfId)) {
					it.remove();
				} else {
					row.sortOrder = ++i;
				}
			}
		} else {
			// 単一ファイル
			p.rows.clear();
		}

		// 添付ファイルを削除したことで、ページ数の変動があるかもしれぬ
		p.adjustNewPageNo(p.pageNo);

		// 差分HTMLをレンダリング
		final String html = factory.renderDiff(p, d, ctx, new HashMap<>());

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/**
	 * ワークフローパーツ添付ファイルの追加
	 * @param partsId アップロード内容を登録する対象パーツID
	 * @param multiple 複数添付ファイルを許可するか
	 * @param maxFileCount 最大添付ファイル数
	 * @param fileRegExp ファイル名の制限用の正規表現
	 * @param multiPart マルチパートデータ
	 * @return
	 */
	@Transactional
	public List<PartsAttachFileRow> addPartsAttachFileWf(Long partsId, boolean multiple, Integer maxFileCount, String fileRegExp, List<FormDataBodyPart> bodyParts) {
		final List<PartsAttachFileRow> rows = new ArrayList<>();
		int count = 0;

		for (BodyPart bodyPart : bodyParts) {
			FormDataContentDisposition cd = (FormDataContentDisposition)bodyPart.getContentDisposition();
			if (eq("file", cd.getName())) {

				final UploadFile f = new UploadFile(bodyPart);
				final byte[] fileData = toBytes(f.stream);
				count++;

				// 最大ファイル数
				if (multiple && count > maxFileCount)
					throw new InvalidUserInputException(MessageCd.MSG0155, maxFileCount);
				if (!multiple && count > 1)
					throw new InvalidUserInputException(MessageCd.MSG0155, 1);

				// ファイル名の制限用の正規表現
				if (isNotEmpty(fileRegExp)) {
					final Pattern p = Pattern.compile(fileRegExp, Pattern.CASE_INSENSITIVE);
					if (!p.matcher(f.fileName).find())
						throw new InvalidUserInputException(MessageCd.MSG0230, f.fileName);
				}

				// ワークフローパーツ添付ファイル情報をインサート
				final MwtPartsAttachFileWf file = new MwtPartsAttachFileWf();
				file.setDeleteFlag(DeleteFlag.ON);	// まだ仮登録なので論理削除として登録する
				file.setFileData(fileData);
				file.setFileName(f.fileName);
				file.setFileSize(fileData == null ? 0 : fileData.length);
				file.setPartsId(partsId);
				file.setSortOrder(count);
				userDataService.insert(file);

				// パーツ用エンティティ
				final PartsAttachFileRow row = new PartsAttachFileRow(file);
				rows.add(row);
			}
		};
		return rows;
	}

	/**
	 * パーツの再描画用HTML取得
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse refreshParts(Bl0002PartsRequest req) {
		final DesignerContext ctx = getDesignContext(req);

		// 差分HTMLをレンダリング
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();
		final PartsBase<?> p = ctx.runtimeMap.get(req.htmlId);
		final PartsDesign d = ctx.designMap.get(p.partsId);
		final String html = factory.renderDiff(p, d, ctx, ecCache);

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/**
	 * 全パーツを再描画する
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse redrawAllParts(Bl0002PartsRequest req) {
		final DesignerContext ctx = getDesignContext(req);

		// 指定されたパーツを起点に独立画面としてレンダリングするか？
		final String html;
		if (req.renderAsStandAlone && isNotEmpty(req.htmlId)) {
			html = factory.renderAsStandAlone(req.htmlId, ctx);
		} else {
			html = factory.renderAll(ctx);
		}

		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		res.html = html;
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}
}
