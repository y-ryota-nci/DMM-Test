package jp.co.dmm.customize.component.screenCustomize;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.component.DmmCodeBook;
import jp.co.dmm.customize.endpoint.po.po0010.Po0010Service;
import jp.co.dmm.customize.endpoint.py.PayInfService;
import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomService;
import jp.co.dmm.customize.jpa.entity.mw.AccClndMst;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignDropdown;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRadio;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.designer.parts.runtime.PartsRadio;
import jp.co.nci.iwf.designer.service.PartsMasterService;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 新規_支払依頼申請の画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0050ScreenCustomize extends DmmScreenCustomize implements DmmCodeBook {

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;

	@Inject private Po0010Service poService;
	@Inject private PayInfService service;

	@Inject private SessionHolder sessionHolder;
	@Inject private MwmLookupService lookupService;

	/** マスタ選択パーツで、汎用マスタの検索結果を他パーツへばらまき処理を行うためのサービス */
	@Inject private PartsMasterService partsMasterService;

	@Inject private DmmCustomService dmmCustomService;

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
				// 支払業務コードと名称設定
				setPayAppl(ctx, res.contents.startUserInfo);

				// データ設定
				fillUserData(req, res, ctx);

				// 申請者情報
				setApplicationInfo(ctx, res.contents.startUserInfo);
			}
			// 申請日：申請するまではシステム日付、申請後は申請日
			setApplicationDate(res, ctx);

			final String purordTp = ctx.runtimeMap.get("TXT0147").getValue();
			final String advpayFg = ctx.runtimeMap.get("TXT0155").getValue();
			final PartsBase<?> $addRtoBaseDt = ctx.runtimeMap.get("TXT0186");

			if (eq($addRtoBaseDt.dcType, DcType.INPUTABLE)
					&& (PurordTp.EXPENSE.equals(purordTp) || CommonFlag.ON.equals(advpayFg) || isEmpty($addRtoBaseDt.getValue()))) {
				if (CommonFlag.ON.equals(advpayFg)) {
					// 社内レート基準日：支払予定日
					final PartsBase<?> $payPlnDt = ctx.runtimeMap.get("TXT0044");	// 支払予定日
					if (eq($payPlnDt.dcType, DcType.INPUTABLE)) {
						final Date payPlnDt = isEmpty($payPlnDt.getValue()) ? today() : toDate($payPlnDt.getValue(), "yyyy/MM/dd");
						$addRtoBaseDt.setValue(toStr(payPlnDt, "yyyy/MM/dd"));
					}
				} else {
					// 社内レート基準日：費用計上月の末日（費用計上月が未設定ならシステム日付の末日）
					final PartsBase<?> $cstAddYm = ctx.runtimeMap.get("TXT0175");	// 費用計上月
					if (eq($cstAddYm.dcType, DcType.INPUTABLE)) {
						final Date cstAddDt = isEmpty($cstAddYm.getValue()) ? today() : toDate($cstAddYm.getValue() + "/01", "yyyy/MM/dd");
						final Date endOfLastM = dmmCustomService.getRtoBaseDt(cstAddDt);
						$addRtoBaseDt.setValue(toStr(endOfLastM, "yyyy/MM/dd"));
					}
				}

				// 通貨コードに対応した社内レートと小数点桁数を最新のマスタから反映
				final PartsMaster currency = (PartsMaster)ctx.runtimeMap.get("MST0149");
				if (in(currency.dcType, DcType.INPUTABLE, DcType.UKNOWN)) {
					partsMasterService.distributeMasterValues(currency, ctx);
				}
			}
		}
	}

	/**
	 * 支払業務コードと名称設定処理
	 * @param ctx
	 */
	protected void setPayAppl(RuntimeContext ctx, UserInfo startUserInfo) {
		// 支払業務コードと名称には申請者の所属組織の支払業務コード（（組織マスタ）.[拡張情報01]）を設定
		String companyCd = startUserInfo.getCorporationCode();
		String payApplCd = startUserInfo.getPayApplCd();
		PayApplMst pam = poService.getPayApplMst(companyCd, payApplCd);
		if (pam != null) {
			ctx.runtimeMap.get("TXT0117").setValue(pam.getId().getPayApplCd());
			ctx.runtimeMap.get("TXT0118").setValue(pam.getPayApplNm());
		}
	}

	/**
	 * データ設定処理
	 * @param req
	 * @param res
	 * @param ctx
	 */
	protected void fillUserData(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		// パラメータ
		final String companyCd = req.param1;
		final String payNo = req.param2;
		if (isNotEmpty(companyCd) && isNotEmpty(payNo)) {
			final String advpayFg = ctx.runtimeMap.get("TXT0155").getValue();

			final Map<String, String> keys = new HashMap<>();
			keys.put("companyCd", companyCd);
			keys.put("payNo", payNo);
			keys.put("advpayFg", advpayFg);

			// ユーザデータからランタイムMapを生成
			loader.fillUserData(ctx, createUserDataMap(keys), false);
		}
	}

	/**
	 * 申請情報設定処理
	 * @param ctx
	 */
	protected void setApplicationInfo(RuntimeContext ctx, UserInfo startUserInfo) {
		ctx.runtimeMap.get("TXT0089").setValue(startUserInfo.getCorporationCode());
		ctx.runtimeMap.get("TXT0067").setValue(startUserInfo.getUserCode());
		ctx.runtimeMap.get("TXT0085").setValue(startUserInfo.getUserName());
		ctx.runtimeMap.get("TXT0068").setValue(startUserInfo.getOrganizationCode());	// 第五階層（チーム）の組織コード
		ctx.runtimeMap.get("TXT0086").setValue(startUserInfo.getOrganizationName());
		ctx.runtimeMap.get("TXT0070").setValue(startUserInfo.getExtendedInfo01()); // 所在地コード
		ctx.runtimeMap.get("TXT0087").setValue(startUserInfo.getSbmtrAddr());
		ctx.runtimeMap.get("TXT0099").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード

		// 明細の申請者情報
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0080");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			ctx.runtimeMap.get(prefix + "TXT0027").setValue(startUserInfo.getCorporationCode());
			ctx.runtimeMap.get(prefix + "TXT0023").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
		}
	}

	/**
	 * 申請日設定処理
	 * @param ctx
	 */
	protected void setApplicationDate(Vd0310InitResponse res, RuntimeContext ctx) {
		PartsBase<?> applyDate = ctx.runtimeMap.get("TXT0069");
		if (res.contents.applicationDate == null) {
			applyDate.setValue(toStr(today())); // 申請日
		} else {
			applyDate.setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
		}
		final String rcvinspYm = ctx.runtimeMap.get("TXT0173").getValue();
		if (isNotEmpty(rcvinspYm)) {
			setCstAddYm(ctx.runtimeMap);
		}
	}

	/**
	 * 費用計上月設定処理
	 * @param runtimeMap
	 * @param clndDt 対象日付
	 */
	protected void setCstAddYm(Map<String, PartsBase<?>> runtimeMap) {
		final String companyCd = runtimeMap.get("TXT0089").getValue();	// 会社CD
		final String rcvinspYm = runtimeMap.get("TXT0173").getValue();	// サービス利用月
		final PartsBase<?> cstAddYm = runtimeMap.get("TXT0175");		// 費用計上月
		cstAddYm.setValue(dmmCustomService.getCstAddYm(companyCd, rcvinspYm, false));
	}

	/**
	 * バリデーション後処理
	 */
	@Override
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// 一時保存の場合は各追加チェック処理を行わない
		if (!ActionType.NORMAL.equals(req.actionInfo.actionType)) {
			return;
		}

		// 所在地のバリデーション
		validSbmtAddr(req, "TXT0070");

		// 支払予定日のバリデーション
		final PartsBase<?> $payPlnDt = req.runtimeMap.get("TXT0044");
		final String payPlnDt = $payPlnDt.getValue();
		if (isNotEmpty(payPlnDt)) {
			final String companyCd = req.runtimeMap.get("TXT0089").getValue();
			final AccClndMst m = service.getAccClndMst(companyCd, payPlnDt);
			if (isEmpty(m)) {
				res.errors.add(new PartsValidationResult($payPlnDt, "支払予定日", "会計カレンダーが存在しないため、処理を続行できません。"));
			} else if ("1".equals(m.getBnkHldayTp())) {
				res.errors.add(new PartsValidationResult($payPlnDt, "支払予定日", "休日が設定されています。"));
			}
		}

//		final String rcvinspYm = req.runtimeMap.get("TXT0173").getValue();
//		if (isNotEmpty(payPlnDt) && isNotEmpty(rcvinspYm)) {
//			final String companyCd = req.runtimeMap.get("TXT0089").getValue();
//			final String applicationDate = req.contents.applicationDate == null ? toStr(today()) : toStr(req.contents.applicationDate, "yyyy/MM/dd");
//			final AccClndMst m = service.getAccClndMst(companyCd, applicationDate);
//			final String targetDt = isEmpty(m) || "1".equals(m.getStlTpFncobl()) ? rcvinspYm + "/01" : toStr(toDate(applicationDate, FORMAT_DATE), "yyyy/MM") + "/01";
//			final String cstAddDt = toStr(addDay(addMonth(toDate(targetDt, "yyyy/MM/dd"), 1), -1));
//			if (payPlnDt.compareTo(cstAddDt) < 0) {
//				res.errors.add(new PartsValidationResult($payPlnDt, "支払予定日", String.format("支払予定日は費用計上日(%s)以降にしてください。", cstAddDt)));
//			}
//		}

		// 振込先口座のバリデーション(支払方法＝振込[10])
		final String payMth = req.runtimeMap.get("RAD0064").getValue();
		if (isNotEmpty(payPlnDt) && isNotEmpty(payMth) && eq("10", payMth)) {
			final String companyCd = req.runtimeMap.get("TXT0089").getValue();
			final String payeeBnkaccCd = req.runtimeMap.get("TXT0047").getValue();
			if (!service.validatePayeeBnkaccCdSs(companyCd, payeeBnkaccCd, payPlnDt)) {
				throw new InvalidUserInputException("選択された振込先銀行口座に振込先銀行口座コード（SuperStream）が設定されていません。");
			}

		}

		// 支払明細
		final String advpayFg = req.runtimeMap.get("TXT0155").getValue();
		PartsGrid grid = (PartsGrid)req.runtimeMap.get("GRD0080");
		Map<String, BigDecimal> matAmtMap = new HashMap<>();
		String slpGrpGl = null, cstTp = null;
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			// 費目１コードが入力済みならアクティブ行とみなす
			String itmexpsCd1 = req.runtimeMap.get(prefix + "TXT0001").getValue();
			if (isNotEmpty(itmexpsCd1)) {
				// 全明細行で伝票グループ（GL）が同一であること
				String group = req.runtimeMap.get(prefix + "TXT0046").getValue();
				if (isNotEmpty(slpGrpGl) && isNotEmpty(group) && !eq(slpGrpGl, group)) {
					throw new InvalidUserInputException("伝票グループが異なる費目が選択されています");
				}
				slpGrpGl = group;
				// 全明細行で経費区分が同一であること
				String type = req.runtimeMap.get(prefix + "TXT0047").getValue();
				if (isNotEmpty(cstTp) && isNotEmpty(type) && !eq(cstTp, type)) {
					throw new InvalidUserInputException("経費区分が異なる費目が選択されています");
				}
				cstTp = type;
			}

			if (CommonFlag.ON.equals(advpayFg)) {
				continue;
			}

			// 支払金額（税込）
			String rcvinspAmtIntax = req.runtimeMap.get(prefix + "TXT0038").getValue();
			// 前払No
			String advpayNo = req.runtimeMap.get(prefix + "TXT0049").getValue();
			// 前払充当額
			PartsBase<?> $matAmt = req.runtimeMap.get(prefix + "TXT0050");
			String matAmt = $matAmt.getValue();
			if (isNotEmpty(advpayNo)) {
				if (isNotEmpty(rcvinspAmtIntax) && toBD(rcvinspAmtIntax).compareTo(toBD(matAmt)) < 0) {
					res.errors.add(new PartsValidationResult($matAmt, "前払消込額", "前払消込額は支払金額（税込）以下にしてください。"));
				}
				// 今回の申請分の前払Noに対する前払充当額を合算
				BigDecimal value = matAmtMap.get(advpayNo);
				if (value == null)
					matAmtMap.put(advpayNo, new BigDecimal(matAmt));
				else
					matAmtMap.put(advpayNo, value.add(new BigDecimal(matAmt)));
			}
		}
		// 前払No単位で前払充当額≦前払金残高であること
		// 経費の場合のみ残額チェックを行う(検収から引き継いだ場合はすでに残額が更新されている為チェックを行わない)
		final String purordTp = req.runtimeMap.get("TXT0147").getValue();
		if (PurordTp.EXPENSE.equals(purordTp)) {
			final String companyCd = req.runtimeMap.get("TXT0089").getValue();
			final String payNo = req.runtimeMap.get("NMB0037").getValue();
			for (String advpayNo : matAmtMap.keySet()) {
				// （支払申請内での）前払Noに対する今回充当額
				final BigDecimal matAmt = matAmtMap.get(advpayNo);
				// （DBの）既存の消込金額（＝残高）
				final BigDecimal remain = service.getRmnPayAmt(companyCd, payNo, advpayNo);
				if (remain.compareTo(matAmt) < 0) {
					String rmnAmt = NumberFormat.getNumberInstance().format(remain);
					throw new InvalidUserInputException("前払No「" + advpayNo + "」に対する前払消込額が残高（" + rmnAmt + "）を超過しています");
				}
			}
		}

		if (res.errors == null || res.errors.isEmpty()) {
			// 申請日を設定
			setRequestDate(req, "TXT0069");
			// 支払依頼日を設定
			setRequestDate(req, "TXT0043");
			// 費用計上月（申請～決裁まで。決裁以降は書き換えない）
			if (in(req.contents.activityDefCode, "0000000001", "0000000002", "0000000003", "0000000004")) {
				setCstAddYm(req.runtimeMap);
			}
			// 人事用フラグ書き換え処理(起票のみ更新処理)
			if (eq("0000000001", req.contents.activityDefCode)) {
				final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
				final String corporationCode = req.runtimeMap.get("TXT0089").getValue();
				final Set<String> itmexpsCds = lookupService.get(localeCode, corporationCode, LookupGroupId.HR_DPT_FG_SBJ_ITMEXPS_CD)
						.stream().map(a -> a.getLookupId()).collect(Collectors.toSet());
				final PartsBase<?> hrDptFg = req.runtimeMap.get("TXT0215");
				if (isNotEmpty(hrDptFg)) {
					hrDptFg.setValue("");
					for (PartsContainerRow row : grid.rows) {
						String prefix = grid.htmlId + "-" + row.rowId + "_";
						// 費目１コードが入力済みならアクティブ行とみなす
						String itmexpsCd1 = req.runtimeMap.get(prefix + "TXT0001").getValue();
						if (itmexpsCds.contains(itmexpsCd1)) {
							hrDptFg.setValue(CommonFlag.ON);
							break;
						}
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
			final PartsRadio parts = (PartsRadio)req.runtimeMap.get("RAD0064");
			res.bizInfos.put("PROCESS_BUSINESS_INFO_013", parts.getLabel((PartsDesignRadio)res.ctx.designMap.get(parts.partsId)));
		}
	}

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	@Override
	public Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys) {
		final String companyCd = keys.get("companyCd");
		final String payNo = keys.get("payNo");
		final String advpayFg = keys.get("advpayFg");
		return service.getUserDataMap(companyCd, payNo, advpayFg);
	}

	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、RuntimeMapやDesignMapを書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	@Override
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {

//		// 源泉税区分の表示条件を書き換え
//		if (!(this instanceof SCR0079ScreenCustomize)
//				&& !(this instanceof SCR0075ScreenCustomize)
//				&& isNotEmpty(ctx.dcId) && ctx.dcId.intValue() == 1) {
//			final PartsDesignMaster MST0139 = (PartsDesignMaster)designCodeMap.get("MST0139");
//			MST0139.readonly = true;
//		}

		// 明細書き換え処理
		{
			PartsDesignGrid design = (PartsDesignGrid)designCodeMap.get("GRD0080");
			// 各ボタンを表示
			design.showAddEmptyButtonFlag = true;
			design.showDeleteButtonFlag = true;
		}

		// 通貨に対応した小数点桁数
		final PartsBase<?> rdxpntGdt = ctx.runtimeMap.get("TXT0171");
		if (rdxpntGdt != null) {
			final Integer decimalPlaces = toInt(rdxpntGdt.getValue());
			final String[] designCodes = {
					"TXT0135",	// [ヘッダ部]合計金額(税抜)
					"TXT0137",	// [ヘッダ部]合計金額(税込)
					"TXT0143",	// [ヘッダ部]源泉対象額
					"TXT0145",	// [ヘッダ部]源泉徴収額
					"TXT0177",	// [ヘッダ部]支払額
					"TXT0185",	// [ヘッダ部]合計税額
					"TXT0220",	// [ヘッダ部]税抜金額(10%対象)
					"TXT0222",	// [ヘッダ部]税額(10%対象)
					"TXT0224",	// [ヘッダ部]税込金額(10%対象)
					"TXT0226",	// [ヘッダ部]税抜金額(軽減8%対象)
					"TXT0228",	// [ヘッダ部]税額(軽減8%対象)
					"TXT0230",	// [ヘッダ部]税込金額(軽減8%対象)
					"TXT0232",	// [ヘッダ部]税抜金額(8%対象)
					"TXT0234",	// [ヘッダ部]税額(8%対象)
					"TXT0236",	// [ヘッダ部]税込金額(8%対象)
					"GRD0080_TXT0008",	// [明細部]検収金額
					"GRD0080_TXT0032",	// [明細部]支払金額(入力値)
					"GRD0080_TXT0037",	// [明細部]支払金額(税抜)
					"GRD0080_TXT0038",	// [明細部]支払金額(税込)
					"GRD0080_TXT0050",	// [明細部]前払充当額
			};
			// 「通貨に対応した小数点桁数」を「金額欄の小数点桁数」として設定
			for (String designCode : designCodes) {
				final PartsDesignTextbox d = (PartsDesignTextbox)designCodeMap.get(designCode);
				d.decimalPlaces = MiscUtils.defaults(decimalPlaces, 0);
			}
			// 前払区分による明細の表示列の書き換え
			final String advpayTp = ctx.runtimeMap.get("RAD0115").getValue();
			final PartsDesignGrid grid = (PartsDesignGrid)designCodeMap.get("GRD0080");
			for (PartsRelation pr : grid.relations) {
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if (in(partsCode, "TXT0049", "TXT0050")) {		// 前払No／前払金額
					pr.width = eq(advpayTp, CommonFlag.ON) ? 2 : 0;
				} else if (eq(partsCode, "TXT0022")) {		// 検収摘要
					pr.width = eq(advpayTp, CommonFlag.ON) ? 8 : 12;
				}
			}
		}

		// 明細の申請者情報
		// 新たな行を追加するたびに、操作者情報を自動設定できるよう、初期値を書き換え
		final String prefix = "GRD0080_";
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			designCodeMap.get(prefix + "TXT0027").defaultValue = ctx.runtimeMap.get("TXT0089").getValue();	// 企業コード
			designCodeMap.get(prefix + "TXT0023").defaultValue = ctx.runtimeMap.get("TXT0099").getValue();	// 第三階層（部・室）の組織コード
			designCodeMap.get(prefix + "TXT0053").defaultValue = ctx.runtimeMap.get("TXT0039").getValue();	// 取引先CD
			final String mnyCd = ctx.runtimeMap.get("MST0149").getValue();									// 通貨CD
			designCodeMap.get(prefix + "TXT0054").defaultValue = mnyCd;										// 通貨CD
			designCodeMap.get(prefix + "TXT0070").defaultValue = isEmpty(mnyCd) || eq("JPY", mnyCd) ? "1" : "2";// 通貨区分

			final PartsBase<?> advcstMrk = ctx.runtimeMap.get("RAD0214");
			final PartsDesignGrid design = (PartsDesignGrid)designCodeMap.get("GRD0080");
			// 表示セルを変更
			for (PartsRelation pr : design.relations) {
				String partsCode = ctx.designMap.get(pr.targetPartsId).partsCode;
				if (in(partsCode, "DDL0058", "TXT0060", "BTN0061", "DDL0062", "DDL0063", "TXT0064", "BTN0065")) {
					pr.width = advcstMrk == null || CommonFlag.OFF.equals(advcstMrk.getValue()) ? 0 : pr.width;
				}
			}
		}

		// 請求先会社から起案担当者の会社CDを除外
		final PartsDesignDropdown dd = (PartsDesignDropdown)designCodeMap.get(prefix + "DDL0044");
		if (isNotEmpty(dd) && isNotEmpty(dd.optionItems) && isNotEmpty(ctx.startUserInfo)) {
			for (Iterator<PartsOptionItem> it = dd.optionItems.iterator(); it.hasNext(); ) {
				PartsOptionItem item = it.next();
				if (eq(item.value, ctx.startUserInfo.getCorporationCode()))
					it.remove();
			}
		}
	}

	/**
	 * ダウンロードコンテンツをoutputへ書き込む
	 * @param req リクエスト(含むアクション）
	 * @param res（含むデザイナーコンテキスト）
	 * @param in API呼出し時のINパラメータ
	 * @param out API呼出し時のOUTパラメータ
	 * @param functionDef アクション機能
	 * @param output 書き込み先ストリーム（close()不要）
	 * @return ファイルダウンロード用のファイル名
	 */
	@Override
	public String doDownload(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res,
			InParamCallbackBase in, OutParamCallbackBase out,
			WfvFunctionDef functionDef, OutputStream output) throws IOException {
		final ScreenCustomPrint ScreenCustomPrint = get(ScreenCustomPrint.class);
		return ScreenCustomPrint.doDownload(req, res, in, out, functionDef, output);
	}
}
