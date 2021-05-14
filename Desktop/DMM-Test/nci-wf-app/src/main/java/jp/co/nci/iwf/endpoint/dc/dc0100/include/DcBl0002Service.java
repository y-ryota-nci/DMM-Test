package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.AssignedStatus;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.BizDocService;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BizDocInfoEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.StampInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002Service;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書(業務文書)ブロック：文書内容のサービス.
 *
 */
@BizLogic
public class DcBl0002Service extends BaseService {

	@Inject private BizDocService bizDocService;
	@Inject private Bl0002Service bl0002Service;
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public RuntimeContext init(Dc0100InitRequest req, Dc0100InitResponse res) {
		final boolean isCopy = (req.docId == null && req.copyDocId != null);
		// 業務文書情報
		BizDocInfoEntity bizDocInfoEntity = null;
		Long copyProcessId = null;
		if (isCopy) {
			bizDocInfoEntity = bizDocService.getBizDocInfoEntity(req.copyDocId, req.screenDocId);
			copyProcessId = bizDocInfoEntity.tranId;
		} else {
			bizDocInfoEntity = bizDocService.getBizDocInfoEntity(req.docId, req.screenDocId);
			res.contents.processId = bizDocInfoEntity.tranId;
		}
		res.contents.corporationCode = bizDocInfoEntity.corporationCode;
		res.contents.screenDocId = bizDocInfoEntity.screenDocId;
		res.contents.screenDocName = bizDocInfoEntity.screenDocName;
		res.contents.screenId = bizDocInfoEntity.screenId;
		res.contents.screenName = bizDocInfoEntity.screenName;
		res.contents.contentsType = DcCodeBook.ContentsType.BIZ_DOC;
		res.contents.bizDocInfo = new BizDocInfo(bizDocInfoEntity);

		final RuntimeContext ctx = RuntimeContext.newInstance(res.contents, req.viewWidth, null);
		// 画面カスタマイズにはとりあえずNULLインスタンスを設定しておく
		IScreenCustom screenCustom = IScreenCustom.get(null);
		// 画面プロセスIDをキーにパーツ定義へユーザデータを読み込んで、実行時インスタンスを生成
		bizDocService.loadScreenAndUserData(ctx, copyProcessId);

		// 画面カスタムクラス：未定義ならNULLインスタンスとして初期化されます
		screenCustom = IScreenCustom.get(ctx.screenCustomClass);
		screenCustom.afterInitLoad(req, res, ctx);

		// スタンプパーツが存在する場合履歴情報を取得する
		if (isNotEmpty(res.contents.latestHistoryList) && existsStampParts(ctx.runtimeMap)) {
			ctx.stampMap.clear();
			res.contents.latestHistoryList
					.stream()
					.filter(l -> isNotEmpty(l.stampCode) && eq(AssignedStatus.END, l.assignedStatus))
					.collect(Collectors.groupingBy(l -> l.stampCode, Collectors.toList()))
					.forEach((k, v) -> {
						ctx.stampMap.put(k, new StampInfo(v.get(0)));
					});
			res.contents.stampMap = ctx.stampMap;
		}

		screenCustom.beforeInitRender(req, res, ctx);

		// パーツのHTML
		res.html = bizDocService.renderAll(ctx);

		// 画面デザイナのキー情報を転写
		res.contents.runtimeMap = ctx.runtimeMap;
		res.contents.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
		res.contents.submitFunctions = ctx.submitFunctions;
		res.contents.loadFunctions = ctx.loadFunctions;
		res.contents.changeStartUserFunctions = ctx.changeStartUserFunctions;
		res.contents.javascriptIds = ctx.javascriptIds;
		res.contents.screenCustomClass = ctx.screenCustomClass;

		screenCustom.afterInitRender(req, res, ctx);

		return ctx;
	}

	/**
	 * パーツのバリデーション
	 * @param req
	 * @param res
	 */
	public void validate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
		final IScreenCustom screenCustom = IScreenCustom.get(req.contents.screenCustomClass);
		// パーツのバリデーション
		screenCustom.beforeValidate(req, res);
		Map<String, EvaluateCondition> ecResults = null;
		if (req.runtimeMap != null && !req.runtimeMap.isEmpty()) {
			// パーツ定義の読み込み
			res.ctx = bl0002Service.load(req);

			// 現在のパーツ値による条件判定結果Map
			ecResults = partsCondUtils.createEcResults(res.ctx);

			// バリデーションとその結果の反映（バリデーション結果による親コンテナのページ番号の変更処理）
			res.errors = bl0002Service.validate(res.ctx, ecResults);
			if (!res.errors.isEmpty()) {
				res.html = bizDocService.renderAll(res.ctx, ecResults);
				res.runtimeMap = res.ctx.runtimeMap;
			}
		}
		screenCustom.afterValidate(req, res);
	}

	/**
	 * 業務文書情報の差分更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		res.docBizInfos = bizDocService.saveBizDocInfo(docId, defaults(req.bizDocInfo, req.contents.bizDocInfo), req.versionInfo.updateVersionType, res.ctx);
	}

	private boolean existsStampParts(Map<String, PartsBase<?>> runtimeMap) {
		for (PartsBase<?> p : runtimeMap.values()) {
			if (PartsType.STAMP == p.partsType) return true;
		}
		return false;
	}
}
