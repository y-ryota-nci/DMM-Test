package jp.co.dmm.customize.component.callbackFunction;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 決裁時に呼び出されるコールバックファンクション。
 * WF前払テーブル(MWT_ADVPAY, MWT_CNTR000213)からDMMの購入依頼情報テーブル(ADVPAY_INF, PAYDTL_INF, PAY_INF)へデータを吸い出す。
 */
public class CallbackFunctionCreate_ADVPAY_INF extends BaseCallbackFunction {

	private static final String REPLACE = quotePattern("${REPLACE}");

	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		final String companyCd = ctx.runtimeMap.get("TXT0054").getValue();
		final String advpayNo = ctx.runtimeMap.get("NMB0005").getValue();
		final String payNo = ctx.runtimeMap.get("NMB0066").getValue();
		final String payDtlNo = ctx.runtimeMap.get("TXT0068").getValue();

		// 書き込み先のテーブルのレコードを削除
		delete(companyCd, advpayNo, payNo, payDtlNo);

		// 登録処理
		String tableName = ctx.root.tableName;
		Long runtimeId = root.rows.get(0).runtimeId;

		// 前払金情報登録処理
		insertSelectADVPAYINF(in, tableName, runtimeId);

		// 支払情報登録処理
		insertSelectPAYINF(in, tableName, runtimeId);

		// 支払明細情報登録処理
		insertSelectPAYDTLINF(in, tableName, runtimeId);

	}

	/**
	 * 書き込み先テーブルのレコードを削除
	 * @param companyCd 会社コード
	 * @param purrqstNo 購入依頼No
	 * @return
	 */
	private int delete(String companyCd, String advpayNo, String payNo, String payDtlNo) {
		int count = execSql(getSql("AP0000_01"), new Object[] {companyCd, advpayNo});
		count += execSql(getSql("AP0000_02"), new Object[] {companyCd, payNo});
		count += execSql(getSql("AP0000_03"), new Object[] {companyCd, payNo, payDtlNo});
		return count;
	}

	/** 前払金情報テーブルをINSERT */
	private int insertSelectADVPAYINF(MoveActivityInstanceInParam in, String tableName, Long runtimeId) {
		final String sql = getSql("AP0000_04").replaceFirst(REPLACE, tableName);
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {corporationCode, userCode, ipAddr, corporationCode, userCode, ipAddr, runtimeId};
		return execSql(sql, args);
	}

	/** 支払情報テーブルをINSERT */
	private int insertSelectPAYINF(MoveActivityInstanceInParam in, String tableName, Long runtimeId) {
		final String sql = getSql("AP0000_05").replaceFirst(REPLACE, tableName);
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {corporationCode, userCode, ipAddr, corporationCode, userCode, ipAddr, runtimeId};
		return execSql(sql, args);
	}

	/** 支払明細情報テーブルをINSERT */
	private int insertSelectPAYDTLINF(MoveActivityInstanceInParam in, String tableName, Long runtimeId) {
		final String sql = getSql("AP0000_06").replaceFirst(REPLACE, tableName);
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {corporationCode, userCode, ipAddr, corporationCode, userCode, ipAddr, runtimeId};
		return execSql(sql, args);
	}

	/** 正規表現のパターン文字列(検索文字列)をエスケープ  */
	private static String quotePattern(String s) {
		if (StringUtils.isEmpty(s)) return "";
		return Pattern.quote(s);
	}

}
