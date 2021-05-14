package jp.co.nci.iwf.component.document;

import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.UpdateVersionType;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100Contents;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.entity.BizDocInfoEntity;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwtBizDocInfo;

/**
 * 業務文書情報のサービス
 */
@BizLogic
public class BizDocService extends BaseService {

	@Inject private BizDocRepository repository;
	/** パーツレンダラーFactory */
	@Inject protected PartsRenderFactory render;
	/** (画面デザイナー)ユーザデータサービス */
	@Inject private UserDataService userDataService;
	/** パーツ条件に関するユーティリティクラス. */
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 業務文書情報Entity取得.
	 * @param docId 文書ID
	 * @param screenDocId 画面文書定義ID
	 * @return
	 */
	public BizDocInfoEntity getBizDocInfoEntity(Long docId, Long screenDocId) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		return repository.getBizDocInfoEntity(docId, screenDocId, login);
	}

	public BizDocInfo getBizDocInfo(Long docId, Long screenDocId) {
		return new BizDocInfo(this.getBizDocInfoEntity(docId, screenDocId));
	}

	public RuntimeContext createRuntimeContext(Dc0100Contents contents, ViewWidth viewWidth) {
		return RuntimeContext.newInstance(contents, viewWidth, null);
	}

	/**
	 * 文書内容の実行時インスタンス生成.
	 * @param ctx
	 * @param copyProcessId コピー対象のプロセスID
	 */
	public void loadScreenAndUserData(RuntimeContext ctx, Long copyProcessId) {
		// 画面プロセスIDをキーにパーツ定義へユーザデータを読み込んで、実行時インスタンスを生成
		if (copyProcessId == null) {
			userDataService.loadScreenAndUserData(ctx);
		} else {
			userDataService.copyScreenAndUserData(ctx, copyProcessId);
		}
	}

	/**
	 * デザイナのコンテンツを一括レンダリング
	 * @param ctx
	 * @return
	 */
	public String renderAll(DesignerContext ctx) {
		return render.renderAll(ctx);
	}

	/**
	 * デザイナのコンテンツを一括レンダリング
	 * @param ctx
	 * @param ecCache 有効条件の判定結果キャッシュ
	 * @return
	 */
	public String renderAll(DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		return render.renderAll(ctx, ecCache);
	}

	/**
	 * パーツに紐付くユーザデータの更新処理
	 * @param req
	 * @param res
	 * @return
	 */
	private Map<String, String> update(RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		// パーツに紐付くユーザデータの更新
		userDataService.save(ctx, ecResults);

		// ユーザデータから文書管理項目を抜き出し
		return userDataService.createDocBusinessInfoMap(ctx);
	}

	/**
	 * 業務文書情報の差分更新
	 * @param docId
	 * @param title
	 * @param bizDocInfo
	 * @param versionInfo
	 * @param ctx
	 */
	public Map<String, String> saveBizDocInfo(Long docId, BizDocInfo bizDocInfo, String updateVersionType, RuntimeContext ctx) {

		// バージョンを更新するか
		boolean isVersionUp = !eq(UpdateVersionType.DO_NOT_UPDATE, updateVersionType);

		// 業務文書情報の差分更新
		final Long tranId = saveMwtBizDocInfo(docId, bizDocInfo, isVersionUp);

		// 文書内容の登録／更新
		if (ctx != null) {
			// 新規登録時もしくはバージョンアップ時はトランザクションID(tranId)をセット
			// またあわせてRuntimeIdをクリア
			// こうすればバージョンがあがった場合に新たな文書内容データとして新規に登録してくれる
			if (ctx.processId == null || !eq(ctx.processId, tranId)) {
				// RuntimeIdのクリア
				clearRuntimeId(ctx, null);
				ctx.processId = tranId;
			}
			// 更新し、その結果として文書管理項目Mapを取得
			final Map<String, EvaluateCondition> ecResults = partsCondUtils.createEcResults(ctx);
			return this.update(ctx, ecResults);
		}
		return null;
	}

	/**
	 * 業務文書情報の差分更新.
	 * @param docVersionId 文書バージョンID
	 * @param bizDocInfo 業務文書情報
	 * @return
	 */
	private Long saveMwtBizDocInfo(long docId, BizDocInfo bizDocInfo, boolean isVersionUp) {
		final MwtBizDocInfo org = repository.getMwtBizDocInfo(docId);
		Long tranId = null;
		if (org == null) {
			final MwtBizDocInfo entity = new MwtBizDocInfo();
			entity.setDocId(docId);
			entity.setScreenDocId(bizDocInfo.screenDocId);
			entity.setTranId(bizDocInfo.processId);
			tranId = repository.insert(entity);
		} else {
			tranId = repository.update(org, bizDocInfo, isVersionUp);
		}
		return tranId;
	}

	/**
	 * 文書内容のRuntimeIdクリア処理.
	 * @param ctx
	 * @param pc
	 */
	@SuppressWarnings({ "rawtypes"})
	public void clearRuntimeId(RuntimeContext ctx, PartsContainerBase<?> pc) {
		if (pc == null) {
			pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);
		}
		for (PartsContainerRow row : pc.rows) {
			row.runtimeId = null;
			for (String htmlId : row.children) {
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);
				// 子要素があれば再帰呼び出し
				if (d instanceof PartsDesignContainer) {
					this.clearRuntimeId(ctx, (PartsContainerBase<?>)p);
				}
			}
		}
	}

	/** 画面文書定義マスタ取得. */
	public MwvScreenDocDef getMwvScreenDocDef(String corporationCode, String screenDocCode, String localeCode) {
		return repository.getMwvScreenDocDef(corporationCode, screenDocCode, localeCode);
	}
}
