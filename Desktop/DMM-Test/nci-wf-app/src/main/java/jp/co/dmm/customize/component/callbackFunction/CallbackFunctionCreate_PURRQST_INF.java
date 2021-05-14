package jp.co.dmm.customize.component.callbackFunction;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 購入依頼回答部長承認、購入依頼回答本部長承認の承認後に呼び出されるコールバックファンクション。
 * WF購入依頼テーブル(MWT_CNTR000212, MWT_CNTR000213)からDMMの購入依頼情報テーブル(PURRQST_INF, PURRQSTDTL_INF)へデータを吸い出す。
 */
public class CallbackFunctionCreate_PURRQST_INF extends BaseCallbackFunction {

	private static final String REPLACE = quotePattern("${REPLACE}");

	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;

		// 遷移後のステータスが「PR501：調達・納品待」以外の場合は実施しない
		if (!"PR501".equals(out.getBusinessProcessStatus())) {
			return;
		}

		final String companyCd = ctx.corporationCode;
		final String purrqstNo = ctx.runtimeMap.get("NMB0005").getValue();

		// 書き込み先のテーブルのレコードを削除
		delete(companyCd, purrqstNo);

		// 購入依頼情報登録処理
		insertSelectPURRQSTINF(in, ctx.root.tableName, root.rows.get(0).runtimeId);

		// 購入依頼明細情報登録処理
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0030");
		final String childTableName = ((PartsDesignGrid)ctx.designMap.get(grid.partsId)).tableName;
		int purrqstDtlNo = 0;
		for (PartsContainerRow detail : grid.rows) {
			insertSelectPURRQSTDTLINF(in, childTableName, detail.runtimeId, purrqstNo, ++purrqstDtlNo);
		}

	}

	/**
	 * 書き込み先テーブルのレコードを削除
	 * @param companyCd 会社コード
	 * @param purrqstNo 購入依頼No
	 * @return
	 */
	private int delete(String companyCd, String purrqstNo) {
		int count = execSql(getSql("PR0000_01"), new Object[] {companyCd, purrqstNo});
		count += execSql(getSql("PR0000_02"), new Object[] {companyCd, purrqstNo});
		return count;
	}

	/** 購入依頼情報テーブルをINSERT */
	private int insertSelectPURRQSTINF(MoveActivityInstanceInParam in, String tableName, Long runtimeId) {
		final String sql = getSql("PR0000_03").replaceFirst(REPLACE, tableName);
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {corporationCode, userCode, ipAddr, corporationCode, userCode, ipAddr, runtimeId};
		return execSql(sql, args);
	}

	/** 購入依頼明細情報テーブルをINSERT */
	private int insertSelectPURRQSTDTLINF(MoveActivityInstanceInParam in, String tableName, Long runtimeId, String purOrdNo, int purOrdDtlNo) {
		final String sql = getSql("PR0000_04").replaceFirst(REPLACE, tableName);
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Object[] args = {purOrdNo, purOrdDtlNo, "JPY", corporationCode, userCode, ipAddr, corporationCode, userCode, ipAddr, runtimeId};
		return execSql(sql, args);
	}

	/** 正規表現のパターン文字列(検索文字列)をエスケープ  */
	private static String quotePattern(String s) {
		if (StringUtils.isEmpty(s)) return "";
		return Pattern.quote(s);
	}

}
