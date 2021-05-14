package jp.co.dmm.customize.component.screenCustomize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.component.dowjones.DowjonesCodeBook;
import jp.co.dmm.customize.component.dowjones.DowjonesEntity;
import jp.co.dmm.customize.component.dowjones.DowjonesRequest;
import jp.co.dmm.customize.component.dowjones.DowjonesResponse;
import jp.co.dmm.customize.component.dowjones.DowjonesService;
import jp.co.dmm.customize.component.parts.DmmEntityField;
import jp.co.dmm.customize.component.parts.DmmEntityRow;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.CodeBook.CorporationCodes;
import jp.co.nci.iwf.component.CodeBook.ProcessBbsMailType;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.AuthenticateRepository;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessBbsInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0010Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0010Service;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;

/**
 * 新規取引先申請（反社）画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0201ScreenCustomize extends DmmScreenCustomize implements DowjonesCodeBook {

	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;
	@Inject private MwmLookupService lookup;
	@Inject private DowjonesService dowjonesService;
	/** 要説明(掲示板)情報サービス */
	@Inject private Bl0010Service bl0010Service;
	@Inject protected WfInstanceWrapper wf;
	@Inject protected AuthenticateRepository repository;

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		if (in(req.trayType, TrayType.NEW, TrayType.WORKLIST)) {
			if (req.processId == null) {

				// 申請者情報
				LoginInfo login = sessionHolder.getLoginInfo();
				ctx.runtimeMap.get("TXT0301").setValue(login.getCorporationCode());
				ctx.runtimeMap.get("TXT0302").setValue(login.getUserCode());
				ctx.runtimeMap.get("TXT0025").setValue(login.getUserName());
				ctx.runtimeMap.get("TXT0303").setValue(login.getOrganizationCode()); // 申請者の所属組織名（本務）そのまま
				ctx.runtimeMap.get("TXT0026").setValue(login.getOrganizationName()); // 申請者の所属組織名（本務）そのまま
				ctx.runtimeMap.get("TXT0304").setValue(login.getExtendedInfo01()); // 所在地コード
				ctx.runtimeMap.get("TXT0028").setValue(login.getSbmtrAddr());

				// 口座明細の会社コード
				PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD1000");

				for (PartsContainerRow row : grid.rows) {
					String prefix = grid.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT1301").setValue(login.getCorporationCode());
				}
			}

			// 申請日：申請するまではシステム日付、申請後は申請日
			PartsBase<?> applyDate = ctx.runtimeMap.get("TXT0027");
			if (res.contents.applicationDate == null) {
				applyDate.setValue(toStr(today())); // 申請日
			} else {
				applyDate.setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
			}
			// BC部ロード時に関係先情報に設定を行う
			if (eq("0000000003", res.contents.activityDefCode)) {
				ctx.runtimeMap.get("TXT2022").setValue(ctx.runtimeMap.get("TXT0008").getValue());
				ctx.runtimeMap.get("RAD2023").setValue(ctx.runtimeMap.get("RAD0004").getValue());
				ctx.runtimeMap.get("TXT2024").setValue(ctx.runtimeMap.get("TXT2010").getValue());
				ctx.runtimeMap.get("TXT2025").setValue(ctx.runtimeMap.get("TXT0006").getValue());
				ctx.runtimeMap.get("TXT2026").setValue(ctx.runtimeMap.get("TXT2040").getValue());
				ctx.runtimeMap.get("TXT2027").setValue(ctx.runtimeMap.get("TXT2009").getValue());
			}
			ctx.runtimeMap.get("TXT2043").setValue(CommonFlag.OFF);
		}
	}

	/**
	 * バリデーション前処理
	 */
	@Override
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {

		// 所在地のバリデーション
		validSbmtAddr(req, "TXT0304");

		// 振込先銀行口座コード（SuperStream）のユニークチェック
		Set<String> bnkaccCdSsSet = new TreeSet<>();
		PartsGrid grid = (PartsGrid) req.runtimeMap.get("GRD1000");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";


			if ("0000000004".equals(req.contents.activityDefCode)
					&& req.runtimeMap.get(prefix + "TXT1013").getValue().trim().length() > 0) {
				String bnkaccCdSs = req.runtimeMap.get(prefix + "MST1013").getValue()
						+ "_" + req.runtimeMap.get(prefix + "TXT1013").getValue();

				if (bnkaccCdSsSet.contains(bnkaccCdSs)) {
					// エラーを設定
					throw new InvalidUserInputException("取引先口座管理情報において振込先銀行口座コード（SuperStream）が重複しています。");
				} else {
					bnkaccCdSsSet.add(bnkaccCdSs);
				}
			}
		}
	}

	/**
	 * バリデーション後処理
	 */
	@Override
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		if (res.errors == null || res.errors.isEmpty()) {
			// 申請日を設定
			setRequestDate(req, "TXT0027");
		}
	}

	@Override
	public void beforeUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// BC部、法務部承認以外処理しない
		if (!in(req.contents.activityDefCode, "0000000003", "0000000005")) {
			return;
		}
		// BC部でアクションタイプが状態遷移以外の場合処理をしない
		if (eq("0000000003", req.contents.activityDefCode) && !eq(ActionType.NORMAL, req.actionInfo.actionType)) {
			return;
		}

		// 法務部で反社データ取得フラグが"1"以外の場合処理をしない
		final Map<String, PartsBase<?>> runtimeMap = res.ctx.runtimeMap;
		if (eq("0000000005", req.contents.activityDefCode) && !eq(CommonFlag.ON, runtimeMap.get("TXT2043").getValue())) {
			return;
		}

		// ダウジョーンズ接続処理
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Map<String, MwmLookup> lookpus = lookup.getMap(localeCode, CorporationCodes.DMM_COM, LookupGroupId.DOWJONES_CONFIG);
		final boolean use = lookpus.containsKey(DowjonesConfig.USE) && toBool(lookpus.get(DowjonesConfig.USE).getLookupName());
		if (!use) {
			return;
		}

		int totalCount = 0;
		final StringBuilder contents = new StringBuilder();

		// 反社情報設定（親）
		{
			final DowjonesRequest djReq = new DowjonesRequest();
			djReq.rltPrtNm = runtimeMap.get("TXT2022").getValue();

			if (isNotEmpty(runtimeMap.get("RAD2023").getValue())) {
				djReq.crpPrsTp = eq("1", runtimeMap.get("RAD2023").getValue()) ? "E" : "P";
			}
			djReq.lndCdDjii = runtimeMap.get("TXT2026").getValue();

			if (isNotEmpty(runtimeMap.get("TXT2027").getValue())) {
				djReq.brthDt = runtimeMap.get("TXT2027").getValue().replaceAll("/", "-");
			}

			final DowjonesResponse djRes = dowjonesService.executeNameSearch(djReq);
			totalCount+= djRes.totalHits;
			if (djRes.totalHits > 100) {
				contents.append(contents.length() == 0 ? "" : "\n");
				contents.append(String.format("[%s][%d件]Dow Jones 取込データが、100件を超えました。", djReq.rltPrtNm, djRes.totalHits));
			}

			runtimeMap.get("TXT2028").setValue(String.valueOf(djRes.totalHits));
//			runtimeMap.get("TXT2030").setValue("");
			final List<DmmEntityRow> newRows = new ArrayList<>();
			if (isNotEmpty(djRes.matchs)) {
				for (DowjonesEntity e : djRes.matchs) {
					final DmmEntityRow row = new DmmEntityRow();
					row.fields = convertDmmEntityFields(e);
					newRows.add(row);
				}
			}
			rowsToContainer(res.ctx, "GRD2041", newRows);
		}

		// 反社情報設定（子）
		{
			final PartsGrid grid = (PartsGrid)runtimeMap.get("GRD2012");
			for (PartsContainerRow gridRow : grid.rows) {
				final String prefix = grid.htmlId + "-" + gridRow.rowId + "_";
				final DowjonesRequest djReq = new DowjonesRequest();
				djReq.rltPrtNm = runtimeMap.get(prefix + "TXT0003").getValue();

				if (isNotEmpty(runtimeMap.get(prefix + "RAD0004").getValue())) {
					djReq.crpPrsTp = eq("1", runtimeMap.get(prefix + "RAD0004").getValue()) ? "E" : "P";
				}
				djReq.lndCdDjii = runtimeMap.get(prefix + "TXT0007").getValue();

				if (isNotEmpty(runtimeMap.get(prefix + "TXT0009").getValue())) {
					djReq.brthDt = runtimeMap.get(prefix + "TXT0009").getValue().replaceAll("/", "-");
				}

				final DowjonesResponse djRes = dowjonesService.executeNameSearch(djReq);
				totalCount+= djRes.totalHits;
				if (djRes.totalHits > 100) {
					contents.append(contents.length() == 0 ? "" : "\n");
					contents.append(String.format("[%s][%d件]Dow Jones 取込データが、100件を超えました。", djReq.rltPrtNm, djRes.totalHits));
				}
				runtimeMap.get(prefix + "TXT0010").setValue(String.valueOf(djRes.totalHits));
//				runtimeMap.get(prefix + "TXT0012").setValue("");

				final List<DmmEntityRow> newRows = new ArrayList<>();
				if (isNotEmpty(djRes.matchs)) {
					for (DowjonesEntity e : djRes.matchs) {
						final DmmEntityRow row = new DmmEntityRow();
						row.fields = convertDmmEntityFields(e);
						newRows.add(row);
					}
				}
				rowsToContainer(res.ctx, prefix + "GRD0015", newRows);
			}
		}
		// 総件数
		runtimeMap.get("TXT2038").setValue(String.valueOf(totalCount));
		// 要説明追加処理
		if (contents.length() > 0) {
			final LoginInfo loginInfo = sessionHolder.getLoginInfo();
			final Bl0010Request bl0010Req = new Bl0010Request();
			bl0010Req.corporationCode = req.contents.corporationCode;
			bl0010Req.processId = req.contents.processId;
			bl0010Req.processBbsInfo = new ProcessBbsInfo();
			bl0010Req.processBbsInfo.belong = loginInfo.getOrganizationCode() + "@" + loginInfo.getPostCode();
			bl0010Req.processBbsInfo.contents = contents.toString();
			bl0010Req.processBbsInfo.processBbsIdUp = new Long(0);
			bl0010Req.processBbsInfo.processBbsMailType = ProcessBbsMailType.NONE;
			bl0010Req.processBbsInfo.attachFiles = new ArrayList<>();
			bl0010Service.submit(bl0010Req);
		}
	}

	private List<DmmEntityField> convertDmmEntityFields(DowjonesEntity entity) {
		final List<DmmEntityField> fields = new ArrayList<>();
		// プロファイルID
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0003";
			f.value = entity.peid;
			fields.add(f);
		}

		// 一致名称
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0004";
			f.value = entity.mtchNm;
			fields.add(f);
		}

		// 国コード
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0005";
			f.value = entity.lndCd;
			fields.add(f);
		}

		// 国名
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0006";
			f.value = entity.lndNm;
			fields.add(f);
		}

		// 国コード（DJII）
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0007";
			f.value = entity.lndCdDjii;
			fields.add(f);
		}
		// 性別
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0008";
			f.value = entity.gndTp;
			fields.add(f);
		}

		// 生年月日
		{
			final DmmEntityField f = new DmmEntityField();
			f.partsCode = "TXT0009";
			f.value = entity.brthDt;
			fields.add(f);
		}

		return fields;
	}

	private void rowsToContainer(DesignerContext ctx, String htmlId, List<DmmEntityRow> newRows) {
		final PartsContainerBase<?> c = (PartsContainerBase<?>)ctx.runtimeMap.get(htmlId);
		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(c.partsId);

		// 旧データは不要なので、子要素を削除
		c.rows.stream().flatMap(r -> r.children.stream()).forEach(h -> {
			ctx.runtimeMap.remove(h);
		});
		c.rows.clear();

		// 既存行から空き行を見つけて新しいレコードを反映
		for (DmmEntityRow newRow : newRows) {
			PartsContainerRow row = d.addRows(c, ctx);
			fillRow(row, newRow, ctx);
		}

		// 並び順をつけ直し
		for (int i = 0; i < c.rows.size(); i++) {
			c.rows.get(i).sortOrder = (i + 1);
		}
	}

	/** コンテナ行に新しい行データを反映 */
	private void fillRow(PartsContainerRow row, DmmEntityRow newRow, DesignerContext ctx) {
		// 新しい行データを反映
		for (DmmEntityField field : newRow.fields) {
			for (String htmlId : row.children) {
				if (htmlId.endsWith(field.partsCode)) {
					final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
					final PartsDesign d = ctx.designMap.get(p.partsId);
					if (eq(field.partsCode, d.partsCode)) {
						p.values.put(p.defaultRoleCode, field.value);
						break;
					}
				}
			}
		}
	}

	/**
	 * ワークフロー更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		if (isNotEmpty(res.bizInfos)) {
			final PartsTextbox parts = (PartsTextbox)req.runtimeMap.get("TXT0008");
			res.bizInfos.put("PROCESS_BUSINESS_INFO_011", parts.getValue());
		}
	}

	@Override
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {
		// ワークリストから遷移した場合は処理しない
		if (TrayType.WORKLIST == ctx.trayType || TrayType.NEW == ctx.trayType || TrayType.BATCH == ctx.trayType) {
			return;
		}

		// 処理者のロールに反社情報参照可能ロールが設定されていれば処理しない
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		final String localeCode = loginInfo.getLocaleCode();
		final String corporationCode = ((RuntimeContext)ctx).corporationCode;
		final Set<String> lookpus = lookup.getMap(localeCode, corporationCode, LookupGroupId.ORG_CRM_INF_REFER_ROLE).keySet();
		final List<WfvUserBelongImpl> belongs = getWfvUserBelongList(loginInfo.getCorporationCode(), loginInfo.getUserAddedInfo());
		final Set<String> assignRoleCodes  = repository.getAssignRoleCds(corporationCode, belongs);
		if (assignRoleCodes.stream().anyMatch(r -> lookpus.contains(r))) {
			return;
		}
		// 該当パーツを非表示に制御
		// 一致件数
		if (designCodeMap.containsKey("TXT2028")) {
			ctx.dcMap.put(designCodeMap.get("TXT2028").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("TXT2028")) {
			ctx.runtimeMap.get("TXT2028").dcType = DcType.HIDDEN;
		}

		// 一致件数参照
		if (designCodeMap.containsKey("EVT2029")) {
			ctx.dcMap.put(designCodeMap.get("EVT2029").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("EVT2029")) {
			ctx.runtimeMap.get("EVT2029").dcType = DcType.HIDDEN;
		}

		// 一致プロファイルID
		if (designCodeMap.containsKey("TXT2030")) {
			ctx.dcMap.put(designCodeMap.get("TXT2030").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("TXT2030")) {
			ctx.runtimeMap.get("TXT2030").dcType = DcType.HIDDEN;
		}

		// 判定
		if (designCodeMap.containsKey("DDL2031")) {
			ctx.dcMap.put(designCodeMap.get("DDL2031").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("DDL2031")) {
			ctx.runtimeMap.get("DDL2031").dcType = DcType.HIDDEN;
		}

		// コメント
		if (designCodeMap.containsKey("TXT2032")) {
			ctx.dcMap.put(designCodeMap.get("TXT2032").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("TXT2032")) {
			ctx.runtimeMap.get("TXT2032").dcType = DcType.HIDDEN;
		}

		// 関係先情報
		if (designCodeMap.containsKey("GRD2012")) {
			ctx.dcMap.put(designCodeMap.get("GRD2012").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("GRD2012")) {
			ctx.runtimeMap.get("GRD2012").dcType = DcType.HIDDEN;
		}

		// 反社データ取得
		if (designCodeMap.containsKey("EVT2013")) {
			ctx.dcMap.put(designCodeMap.get("EVT2013").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("EVT2013")) {
			ctx.runtimeMap.get("EVT2013").dcType = DcType.HIDDEN;
		}

		// 最終判定区分
		if (designCodeMap.containsKey("DDL2035")) {
			ctx.dcMap.put(designCodeMap.get("DDL2035").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("DDL2035")) {
			ctx.runtimeMap.get("DDL2035").dcType = DcType.HIDDEN;
		}

		// 最終判定備考
		if (designCodeMap.containsKey("TXT2037")) {
			ctx.dcMap.put(designCodeMap.get("TXT2037").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("TXT2037")) {
			ctx.runtimeMap.get("TXT2037").dcType = DcType.HIDDEN;
		}

		// 一致プロファイルID削除
		if (designCodeMap.containsKey("EVT2042")) {
			ctx.dcMap.put(designCodeMap.get("EVT2042").partsId, DcType.HIDDEN);
		}
		if (ctx.runtimeMap.containsKey("EVT2042")) {
			ctx.runtimeMap.get("EVT2042").dcType = DcType.HIDDEN;
		}

	}

	/** ユーザ所属ビュー抽出 */
	private List<WfvUserBelongImpl> getWfvUserBelongList(String corporationCode, String userAddedInfo) {
		final java.sql.Date today = today();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(corporationCode);
		in.setUserAddedInfo(userAddedInfo);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today);
		in.setValidEndDateOrganization(today);
		in.setValidStartDatePost(today);
		in.setValidEndDatePost(today);
		in.setValidStartDateUser(today);
		in.setValidEndDateUser(today);
		in.setValidStartDateUserBelong(today);
		in.setValidEndDateUserBelong(today);
		in.setOrderBy(new OrderBy[] {new OrderBy(true, WfcUserBelong.JOB_TYPE)});
		return wf.searchWfvUserBelong(in).getUserBelongList();
	}
}
