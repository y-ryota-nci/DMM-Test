package jp.co.dmm.customize.component.screenCustomize;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.GetRouteInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetRouteOutParam;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WftActivityEx;
import jp.co.nci.integrated_workflow.model.custom.WftAssignedEx;
import jp.co.nci.integrated_workflow.model.custom.WfvRoute;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.integrated_workflow.param.input.SearchWftActivityInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWftAssignedInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWftActivityOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWftAssignedOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.pdf.JRDataSourceConverter;
import jp.co.nci.iwf.component.pdf.PdfService;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.PartsPrintValueService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomBase;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import net.sf.jasperreports.renderers.SimpleDataRenderer;

/**
 * 画面カスタムクラス(JasperReportsによるPDF出力)
 */
@BizLogic
public class ScreenCustomPrint extends ScreenCustomBase implements ScreenCustomPrintCodeBook{

	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;

	/** WF API */
	@Inject
	protected ScreenCustomPrintRepository ScreenCustomPrintRepository;

	/** WF API */
	@Inject
	protected MwmLookupService MwmLookupService;

	@Inject
	private Logger log;

	/**
	 * ダウンロードコンテンツをoutputへ書き込む
	 *
	 * @param req
	 *            リクエスト(含むアクション）
	 * @param res（含むデザイナーコンテキスト）
	 * @param in
	 *            API呼出し時のINパラメータ
	 * @param out
	 *            API呼出し時のOUTパラメータ
	 * @param functionDef
	 *            アクション機能
	 * @param output
	 *            書き込み先ストリーム（close()不要）
	 * @return ファイルダウンロード用のファイル名
	 */
	@Override
	public String doDownload(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in,
			OutParamCallbackBase out, WfvFunctionDef functionDef, OutputStream output) throws IOException {
		//TODO DMMこれから帳票DLのキャンセル機能実装予定
		//if (true) throw new InvalidUserInputException("帳票DLをキャンセルします");

		// 会社CD
		final String companyCode = res.ctx.corporationCode;
		final PartsPrintValueService printValueService = get(PartsPrintValueService.class);
		final PartsRootContainer root = (PartsRootContainer) res.ctx.runtimeMap.get(res.ctx.root.containerCode);
		final String containerName = res.ctx.root.containerName;

		// 帳票のヘッダ部をMap化（キーはJasper定義のParameter名、値はそのParameterの内容）
		final Map<String, Object> header = toHeaderMap(printValueService, root, res.ctx, companyCode);

		// 帳票の明細部分をリスト化（明細リストは List<Map>でもいいし、List<Bean>でもいい）
		final List<Map<String, Object>> details = containerToList(printValueService, root, res.ctx, companyCode);

		BufferedImage image = getImage(printValueService, root, res, containerName, companyCode);
		if (image != null) {
			header.put(JasperParam.STAMP, image);
		}

		//検収のみ発注NOから契約期間を取得する
		if (ReportName.ACCEPT_REPORT.equals(containerName)) {
			Map<String, Date> contractDates = checkCntractDateForAccept(printValueService, root, res.ctx, companyCode);
			if (contractDates != null) {
				for (Map.Entry<String, Date> contractDate : contractDates.entrySet()) {
					if (contractDate.getValue() != null) {
						header.put(contractDate.getKey(), dateFormatForContract(contractDate.getValue()));
					} else {
						header.put(contractDate.getKey(), "");
					}
				}
			}
		}

		String jasperName = getJasperFileName(req, res);

		// 帳票単位か明細単位かどうか
		if (checkTaxClass(printValueService, root, res.ctx)) {
			StringBuilder sb = new StringBuilder(jasperName);
			sb.insert(sb.indexOf("情報"), "明細");
			jasperName = sb.toString();
		}

		// PDF生成し、ストリームへ書き出し
		final PdfService pdfService = get(PdfService.class);
		pdfService.writePdfStream(jasperName, header, details.toArray(), output);

		// ダウンロード時のファイル名
		return getOutputFileName(jasperName);
	}

	//印鑑データのパスを取得
	public BufferedImage getImage(PartsPrintValueService printValueService, PartsRootContainer root, Vd0310ExecuteResponse res, String containerName, String companyCode) {
		final List<WfvRoute> list = this.getRoute(res);
		boolean stampFlag = false;
		for (WfvRoute route : list) {
			//	発注
			if (ReportName.ORDER_REPORT.equals(containerName) || ReportName.OREDER_DETAIL_REPORT.equals(containerName)) {
				//決裁ポイント
				if (PrintTriggerActivity.ACTIVITY_FOR_ORDER_STAMP.equals(route.getActivityDefCode()) && (route.getActivityDate() != null)) {
					stampFlag = true;
				}
			}
			//	検収
			if (ReportName.ACCEPT_REPORT.equals(containerName) || ReportName.ACCEPT_DETAIL_REPORT.equals(containerName)) {
				//決裁ポイント
				if (PrintTriggerActivity.ACTIVITY_FOR_ACEPT_STAMP.equals(route.getActivityDefCode()) && (route.getActivityDate() != null)) {
					stampFlag = true;
				}
			}
			//	支払
			if (ReportName.PAYMENT_REPORT.equals(containerName) || ReportName.PAYMENT_DETAIL_REPORT.equals(containerName)) {
				//	決裁ポイント
				if (PrintTriggerActivity.ACTIVITY_FOR_PAYMENT_STAMP.equals(route.getActivityDefCode()) && (route.getActivityDate() != null)) {
					stampFlag = true;
				}
			}
		}

		// 印鑑データ取得
		if (stampFlag) {
			String stampType = null;
			if (ReportName.PAYMENT_REPORT.equals(containerName) || ReportName.PAYMENT_DETAIL_REPORT.equals(containerName)) {
				//支払通知書では印鑑種別の項目がなく、角印しか押下しないため角印のルックアップIDに固定
				if (checkEvidenceType(printValueService, root, res.ctx)) stampType = "2";
			} else {
				stampType = checkStampType(printValueService, root, res.ctx);
			}
			if (stampType != null) {
				try {
					List<MwmLookup> lookupStampTypes = MwmLookupService.get("ja", companyCode, LookupGroupId.STAMP_TYPE);
					String stampPath = null;
					for (MwmLookup lookupStampType : lookupStampTypes) {
						if (stampType.equals(lookupStampType.getLookupId()) && !StampName.NOTHING.equals(lookupStampType.getLookupName())) {
							stampPath = lookupStampType.getLookupName();
						}
					}
					if (stampPath != null) {
						try {
							return ImageIO.read(new File(stampPath));
						} catch (IOException e) {
							//ルックアップに設定したパスに画像がない場合エラーが発生する
							log.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	/**
	 * WFT_ASSIGNED情報の取得
	 *
	 * @param processId
	 * @return
	 */
	public List<WftAssignedEx> getWftAssignedList(Long processId) {

		SearchWftAssignedInParam in = new SearchWftAssignedInParam();
		in.setProcessId(processId);

		SearchWftAssignedOutParam out = wf.searchWftAssigned(in);
		return out.getResultList();
	}

	/**
	 * WFT_ASSIGNED情報の取得
	 *
	 * @param processId
	 * @return
	 */
	public List<WftActivityEx> getWftActivityLists(String corporationCode, Long processId) {

		SearchWftActivityInParam in = new SearchWftActivityInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);

		SearchWftActivityOutParam out = wf.searchWftActivity(in);
		return out.getResultList();
	}

	/**
	 * 承認ルート(インスタンス)情報の取得.
	 * @param req
	 */
	protected List<WfvRoute> getRoute(Vd0310ExecuteResponse res) {
		// 承認ルート情報取得条件の生成
		GetRouteInParam in = new GetRouteInParam();
		in.setUnfoldRoleFlg(true);// 承認者を取得する
		in.setCorporationCode(res.ctx.corporationCode);
		in.setProcessId(res.ctx.processId);
		// ユーザー情報セット
		in.setWfUserRole(createWfUserRole(LoginInfo.get()));
		// 実行
		GetRouteOutParam out = wf.getRoute(in);
		final List<WfvRoute> list = out.getWfvRouteList();
		return list;
	}

	/**
	 * IWF API用ユーザロールを生成(セッションへ格納はしない)
	 * @param loginInfo ログイン情報
	 * @return
	 */
	private WfUserRole createWfUserRole(LoginInfo user) {
		final WfUserRole ur = new WfUserRoleImpl();
		ur.setCorporationCode(user.getCorporationCode());
		ur.setUserCode(user.getUserCode());
		return ur;
	}

	/**
	 * 【発注/検収】印鑑種別が丸印か角印、印鑑不要かのチェック
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected String checkStampType(PartsPrintValueService printValueService, PartsRootContainer root,
			DesignerContext ctx) {
		final PartsContainerRow row = (PartsContainerRow) root.rows.get(0);
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			String key = d.designCode;
			switch (p.partsType) {
			// チェックボックス・ラジオ・ドロップダウン
			case PartsType.DROPDOWN:
				if (isNotEmpty(p.getValue())) {
					if (key.equals(PartsId.ORDER_STAMP_TYPE)) {
						return p.getValue();
					}
					if (key.equals(PartsId.ACCEPT_STAMP_TYPE)) {
						return p.getValue();
					}
				}
				break;
			}
		}
		return null;
	}


	/**
	 * 発注NOから契約期間を取得
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @param companyCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Map<String, Date> checkCntractDateForAccept(PartsPrintValueService printValueService, PartsRootContainer root,
			DesignerContext ctx, String companyCode) {
		final PartsContainerRow row = (PartsContainerRow) root.rows.get(0);
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			switch (p.partsType) {
			case PartsType.GRID:
			case PartsType.REPEATER:
				List<Map<String, Object>> containerList = printValueService.containerToList((PartsContainerBase<?>) p,
						ctx);
				String[] acceptList;
				if (isNotEmpty(containerList)) {
						acceptList = containerList.get(0).get("GRD0059_TXT0028").toString().split("-");
						return checkCntractDateFromOrderNo(acceptList[0], companyCode);
				}
				break;
			}
		}
		return null;
	}

	/**
	 * 発注NOから契約期間を取得
	 *
	 * @param orderNo
	 * @param companyCode
	 * @return
	 */
	private Map<String, Date> checkCntractDateFromOrderNo(String orderNo, String companyCode) {
		Map<String, Date> contractDates = ScreenCustomPrintRepository.getContractInfoFromOrderInfo(orderNo, companyCode);
		return contractDates;
	}

	/**
	 * 消費税処理単位が伝票単位か明細単位かのチェック
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected boolean checkTaxClass(PartsPrintValueService printValueService, PartsRootContainer root,
			DesignerContext ctx) {
		Map<String, Object> headerMap = printValueService.toHeaderMap(root, ctx);
		final PartsContainerRow row = (PartsContainerRow) root.rows.get(0);
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			String key = d.designCode;
			Object value = headerMap.get(d.designCode);
			switch (p.partsType) {
			// チェックボックス・ラジオ・ドロップダウン
			case PartsType.RADIO:
				if (isNotEmpty(p.getValue())) {
					if (value.equals(TaxType.DETAIL)) {
						if (key.equals(PartsId.ORDER_TAX_TYPE)) {
							return true;
						}
						if (key.equals(PartsId.ACCEPT_TAX_TYPE)) {
							return true;
						}
						if (key.equals(PartsId.PAYMENT_TAX_TYPE)) {
							return true;
						}
					}
				}
				break;
			}
		}
		return false;
	}

	/**
	 * 【支払】証憑区分が請求書か支払通知書かチェック
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected boolean checkEvidenceType(PartsPrintValueService printValueService, PartsRootContainer root,
			DesignerContext ctx) {
		Map<String, Object> headerMap = printValueService.toHeaderMap(root, ctx);
		final PartsContainerRow row = (PartsContainerRow) root.rows.get(0);
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			String key = d.designCode;
			Object value = headerMap.get(d.designCode);
			switch (p.partsType) {
			// チェックボックス・ラジオ・ドロップダウン
			case PartsType.RADIO:
				if (isNotEmpty(p.getValue())) {
					if (value.equals(EvidenceType.PAYMENT)) {
						if (key.equals(PartsId.PAYMENT_EVIDENCE_TYPE)) {
							return true;
						}
					}
				}
				break;
			}
		}
		return false;
	}


	/**
	 * 帳票のヘッダ部のMapをJasperReports出力用に変換し返す
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @return
	 */
	protected Map<String, Object> toHeaderMap(PartsPrintValueService printValueService, PartsRootContainer root,
			DesignerContext ctx, String companyCode) {
		Map<String, Object> headerMap = printValueService.toHeaderMap(root, ctx);
		final PartsContainerRow row = (PartsContainerRow) root.rows.get(0);
		return convertJasperReportsMap(printValueService, row, ctx, headerMap, companyCode);
	}

	/**
	 * 帳票の明細部分をリスト化（明細リストは List<Map>でもいいし、List<Bean>でもいい）
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected List<Map<String, Object>> containerToList(PartsPrintValueService printValueService,
			PartsRootContainer root, RuntimeContext ctx, String companyCode) {
		final PartsContainerRow row = (PartsContainerRow) root.rows.get(0);
		List<Map<String, Object>> details = new LinkedList<>();
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			switch (p.partsType) {
			// グリッド・リピーター
			case PartsType.GRID:
			case PartsType.REPEATER:
				details = printValueService.containerToList((PartsContainerBase<?>) p, ctx);

				for (int i = 0; i < details.size(); i++) {
					details.set(i, convertJasperReportsMap(printValueService, ((PartsContainerBase<?>) p).rows.get(i),ctx, details.get(i), companyCode));
				}
				break;
			}
		}
		return details;
	}

	/**
	 * 取得した情報をJasperReports用に加工
	 *
	 * @param printValueService
	 * @param root
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Map<String, Object> convertJasperReportsMap(PartsPrintValueService printValueService,
			PartsContainerRow row, DesignerContext ctx, Map<String, Object> printValueMap, String companyCode) {
		Map<String, Object> map = new LinkedHashMap<>();
		String moneyType = null;
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			String key = d.designCode;
			Object value = printValueMap.get(d.designCode);
			switch (p.partsType) {
			// チェックボックス・ラジオ・ドロップダウン
			case PartsType.RADIO:
				if (isNotEmpty(p.getValue())) {
					// ラベルが返るので値も別途設定
					map.put(key, value);
				}
				break;
			case PartsType.CHECKBOX:
			case PartsType.DROPDOWN:
				if (isNotEmpty(p.getValue())) {
					// ラベルが返るので値も別途設定
					map.put(key, p.getValue());
					map.put(key + "_label", value);
				}
				break;
			// グリッド・リピーター
			case PartsType.GRID:
			case PartsType.REPEATER:
				List<Map<String, Object>> containerList = printValueService.containerToList((PartsContainerBase<?>) p,
						ctx);
				for (int i = 0; i < containerList.size(); i++) {
					containerList.set(i, convertJasperReportsMap(printValueService,
							((PartsContainerBase<?>) p).rows.get(i), ctx, containerList.get(i), companyCode));
				}
				if (isNotEmpty(containerList)) {
					map.put(key, JRDataSourceConverter.toDataSource(containerList.toArray()));
				}
				break;
			// 画像
			case PartsType.IMAGE:
				if (isNotEmpty(value)) {
					// byte配列をImageに変換
					byte[] image = (byte[]) value;
					try {
						map.put(key, ImageIO.read(new ByteArrayInputStream(image)));
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
				break;
			// スタンプパーツ
			case PartsType.STAMP:
				if (isNotEmpty(value)) {
					// svg文字列をRendererに変換
					String svg = (String) value;
					try {
						map.put(key, SimpleDataRenderer.getInstance(svg.getBytes("UTF-8")));
					} catch (UnsupportedEncodingException e) {
						log.error(e.getMessage(), e);
					}
				}
				break;

			//マスタ
			case PartsType.MASTER:
				if (isNotEmpty(value) ) {
					//通貨単位のチェック
					if ((key.equals(PartsId.ORDER_MONEY_TYPE)  && ReportName.ORDER_REPORT.equals(ctx.root.containerName))
							|| (key.equals(PartsId.ACCEPT_MONEY_TYPE)  && ReportName.ACCEPT_REPORT.equals(ctx.root.containerName))
							|| (key.equals(PartsId.PAYMENT_MONEY_TYPE) && ReportName.PAYMENT_REPORT.equals(ctx.root.containerName))) {
						moneyType = checkMoneyType(value, companyCode);
					}
					if (isNotEmpty(p.getValue())) {
						map.put(key, p.getValue());
						map.put(key + "_label", value);
					}
				}
				break;

			default:
				if (printValueMap.containsKey(key)) {
					if (isNotEmpty(value)) {
						//【発注】契約情報取得
						if (key.equals("TXT0075") && ReportName.ORDER_REPORT.equals(ctx.root.containerName)) {
							Map<String, Date> contractDates = getContractInfo(value, companyCode);
							for (Map.Entry<String, Date> contractDate : contractDates.entrySet()) {
								if (contractDate.getValue() != null) {
									map.put(contractDate.getKey(), dateFormatForContract(contractDate.getValue()));
								} else {
									map.put(contractDate.getKey(), "");
								}
							}
						}
						//日付のフォーマットを変更
						if ((key.equals(PartsId.ORDER_APPLICATION_DATE)  && ReportName.ORDER_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.ORDER_ACCEPTING_DATE)  && ReportName.ORDER_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.ACCEPT_APPLICATION_DATE) && ReportName.ACCEPT_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.ACCEPT_ACCEPTED_DATE) && ReportName.ACCEPT_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.ACCEPT_END_DATE) && ReportName.ACCEPT_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.PAYMENT_APPLICATION_DATE)  && ReportName.PAYMENT_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.PAYMENT_PAYING_DATE)  && ReportName.PAYMENT_REPORT.equals(ctx.root.containerName))) {

							value = dateFormat(value);
						}
						if ((key.equals(PartsId.ORDER_TOTAL)  && ReportName.ORDER_REPORT.equals(ctx.root.containerName))
								|| (key.equals(PartsId.PAYMENT_TOTAL) && ReportName.PAYMENT_REPORT.equals(ctx.root.containerName))) {
							if (moneyType != null) {
								String paymentSum = moneyType + value;
								map.put("PAYMENT_SUM", paymentSum);
							}
						//検収のみ支払額の項目がないため別に通貨記号を渡す
						} else if ((key.equals(PartsId.ACCEPT_TOTAL) && ReportName.ACCEPT_REPORT.equals(ctx.root.containerName))) {
							if (moneyType != null) {
								map.put("MONEY_TYPE", moneyType);
							}
						}
						map.put(key, value);
					}
				}
				break;
			}
		}
		return map;
	}

	/**
	 * 通貨名から通貨記号を返す
	 *
	 * @param value
	 * @return
	 */
	protected String checkMoneyType(Object moneyName, String companyCode) {
		String moneyType = ScreenCustomPrintRepository.getMnyType(moneyName, companyCode);
		return moneyType;
	}

	/**
	 * 契約NOから契約期間を返す
	 *
	 * @param value
	 * @return
	 */
	protected Map<String, Date> getContractInfo(Object contractNo, String companyCode) {
		Map<String, Date> contractDates = ScreenCustomPrintRepository.getContractInfo(contractNo, companyCode);
		return contractDates;
	}

	/**
	 * 日付のフォーマットを変更する
	 *
	 * @param dateString
	 * @return
	 */
	protected Object dateFormat (Object dateString) {
		if (dateString instanceof String) {
			try {
				DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
				LocalDate ld = LocalDate.parse((String)dateString, dtf1);
				return dtf2.format(ld);
			} catch (Exception e) {
	            //必須項目の該当日付欄に日付を入力する前に印刷するとエラーが発生する
	        }
		}
		return dateString;
	}

	/**
	 * 日付のフォーマットを変更する
	 *
	 * @param dateString
	 * @return
	 */
	protected Object dateFormatForContract (Date contractDate) {
		if (contractDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String dateString = sdf.format(contractDate);
			return dateString;
		}
		return null;
	}

	/**
	 * Jasperファイル名
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	protected String getJasperFileName(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		return res.ctx.root.containerName + ".jasper";
	}

	/**
	 * 出力ファイル名
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	protected String getOutputFileName(String jasperName) {
		return String.format("%s_%s.pdf", toStr(new Date(), "yyyyMMddHHmmss"),jasperName.replaceFirst("\\.jasper", ""));
	}
}
