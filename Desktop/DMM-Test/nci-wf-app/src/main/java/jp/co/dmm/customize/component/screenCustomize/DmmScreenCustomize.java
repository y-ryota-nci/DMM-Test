package jp.co.dmm.customize.component.screenCustomize;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Named;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.common.CodeMaster.DateFormatStr;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomBase;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * DMM_画面カスタマイズの基底クラス
 */
@ScreenCustomizable
@Named
public class DmmScreenCustomize extends ScreenCustomBase {

	/**
	 * 所在地バリデーション
	 * @param req
	 * @param partsId
	 */
	public void validSbmtAddr(Vd0310ExecuteRequest req, String partsId) {

//		// 起案担当者
//		UserInfo startUserInfo = req.startUserInfo;
//		// ユーザ情報取得
//		SearchWfvUserBelongInParam inParam = new SearchWfvUserBelongInParam();
//		inParam.setCorporationCode(startUserInfo.getCorporationCode());
//		inParam.setUserCode(startUserInfo.getUserCode());
//
//		WfInstanceWrapper wf = CDI.current().select(WfInstanceWrapper.class).get();
//		SearchWfvUserBelongOutParam outParam = wf.searchWfvUserBelong(inParam);
//
//		String errorMsg = "所在地が現在登録されている所在地と異なります。起案者を選択し直すか申請を再作成して下さい。";
//
//		// 現在のユーザ所属情報にある所在地と申請時の所在地が異なればエラーとする。
//		// ユーザ所属情報が取得できなくてもエラーとする。これは一時的な措置で、仕様が決まったら変更する
//		if (outParam.getUserBelongList() != null && outParam.getUserBelongList().size() != 0) {
//			// DBの値
//			String sbmtAddrCdNow = outParam.getUserBelongList().get(0).getExtendedInfo01();
//			// パーツに設定された値
//			PartsTextbox sbmtAddrCdParts = (PartsTextbox)req.runtimeMap.get(partsId);
//			if (!StringUtils.isEmpty(sbmtAddrCdParts.getValue()) &&
//				!StringUtils.equals(sbmtAddrCdNow, sbmtAddrCdParts.getValue())) {
//				throw new InvalidUserInputException(errorMsg);
//			}
//		} else {
//			throw new InvalidUserInputException(errorMsg);
//		}
	}
	/**
	 * 申請日の設定
	 * @param req
	 * @param partsId
	 */
	public void setRequestDate(Vd0310ExecuteRequest req, String partsId) {

		// WFT_PROCESSの申請日が更新されるまで画面の申請日を更新する
		if (req.contents.applicationDate == null) {
			// 申請日を設定
			PartsTextbox requestDateParts = (PartsTextbox) req.runtimeMap.get(partsId);
			requestDateParts.values.put("text",
					new SimpleDateFormat(DateFormatStr.YYYYMMDD_SLASH).format(new Date()));
		}
	}

	/**
	 * パーツ値のセット（デフォルト役割コードに応じた値のみセット）
	 * @param ctx デザイナーコンテキスト
	 * @param htmlId パーツのHTML_ID
	 * @param value 値
	 */
	protected void setPartsValue(DesignerContext ctx, String htmlId, Object value) {
		final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
		if (p == null) {
			throw new InternalServerErrorException("htmlIdに対応するパーツがruntimeMapにありません。 htmlId=" + htmlId);
		}
		final String val = value == null ? "" : String.valueOf(value);
		p.setValue(p.defaultRoleCode, val);	// 必要ならroleCodeを引数に取るオーバーロード関数を作るべし
	}

	/**
	 * パーツ値のセット
	 * @param ctx デザイナーコンテキスト
	 * @param htmlId パーツのHTML_ID
	 * @param roleCode 役割コード
	 * @param value 値
	 */
	protected void setPartsValue(DesignerContext ctx, String htmlId, String roleCode, Object value) {
		assert(ctx != null) : "DesignerContextが未指定";
		assert(isNotEmpty(htmlId)) : "htmlIdが未指定";
		assert(isNotEmpty(roleCode)) : "役割コードが未指定";

		final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
		if (p == null) {
			throw new InternalServerErrorException("htmlIdに対応するパーツがruntimeMapにありません。 htmlId=" + htmlId);
		}
		final String val = value == null ? "" : String.valueOf(value);
		p.setValue(roleCode, val);
	}

//	/** 対象日の前月末日を返す */
//	protected Date getEndOfLastMonth(Date d) {
//		if (d == null)
//			return null;
//		final Calendar cal = Calendar.getInstance();
//		cal.setTime(addMonth(d, -1));
//		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
//		return new java.sql.Date(cal.getTimeInMillis());
//	}

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
		throw new InvalidUserInputException("帳票の定義がありません");
	}
}
