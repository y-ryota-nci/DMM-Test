package jp.co.dmm.customize.component.screenCustomize;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 新規取引先申請画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0051ScreenCustomize extends DmmScreenCustomize {

	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;

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

}
