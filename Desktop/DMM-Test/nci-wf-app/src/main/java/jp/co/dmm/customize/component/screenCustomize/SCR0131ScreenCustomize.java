package jp.co.dmm.customize.component.screenCustomize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.system.SqlService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 変更取引先申請（DGHD用）画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0131ScreenCustomize extends DmmScreenCustomize {

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** SQLサービス */
	@Inject private SqlService sqlService;
	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;

	/**
	 * 初期表示前処理
	 */
	@Override
	public void beforeInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {

		// パラメータ
		String companyCd = req.param1;
		String splrCd = req.param2;

		if (StringUtils.isNotEmpty(splrCd)) {

			// 取引先情報をユーザデータとして読み込み
			final Map<String, List<UserDataEntity>> tables = new HashMap<>();
			{
				final Object[] params = {companyCd, splrCd};
				final String tableName = "MWT_SPLR_REQUEST";
				final String sql = sqlService.get("SP0020_05_2");
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
				tables.put(tableName, userDataList);
			}
			// 取引先口座明細情報をユーザデータとして読み込み
			{
				final Object[] params = {splrCd};
				final String tableName = "MWT_SPLR_REQUEST_DETAIL";
				final String sql = sqlService.get("SP0020_06_2");
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
				tables.put(tableName, userDataList);
			}

			// ユーザデータからランタイムMapを生成
			loader.fillUserData(ctx, tables, false);

			// 会社コードを取得


			// フィールド値を設定
			{
				// 起票者の状態で上書き
				// 変更申請を起票した人の組織コードを使用する
				LoginInfo login = sessionHolder.getLoginInfo();
				ctx.runtimeMap.get("TXT0301").setValue(login.getCorporationCode());
				ctx.runtimeMap.get("TXT0302").setValue(login.getUserCode());
				ctx.runtimeMap.get("TXT0025").setValue(login.getUserName());
				ctx.runtimeMap.get("TXT0303").setValue(login.getOrganizationCode()); // 申請者の所属組織名（本務）そのまま
				ctx.runtimeMap.get("TXT0026").setValue(login.getOrganizationName()); // 申請者の所属組織名（本務）そのまま
				ctx.runtimeMap.get("TXT0304").setValue(login.getExtendedInfo01()); // 所在地コード
				ctx.runtimeMap.get("TXT0028").setValue(login.getSbmtrAddr());

				// 口座明細の会社コード
//				PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD1000");
//
//				for (PartsContainerRow row : grid.rows) {
//					String prefix = grid.htmlId + "-" + row.rowId + "_";
//					ctx.runtimeMap.get(prefix + "TXT1301").setValue(login.getCorporationCode());
//				}
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

			String bnkaccCdSs = req.runtimeMap.get(prefix + "MST1013").getValue()
					+ "_" + req.runtimeMap.get(prefix + "TXT1013").getValue();
			if (req.runtimeMap.get(prefix + "TXT1013").getValue().trim().length() > 0) {
				if (bnkaccCdSsSet.contains(bnkaccCdSs)) {
					// エラーを設定
					throw new InvalidUserInputException("取引先口座管理情報において会社コード・振込先銀行口座コード（SuperStream）が重複しています。");
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