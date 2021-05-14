package jp.co.nci.iwf.endpoint.mm.mm0300;

import java.sql.Date;
import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ルート一覧リクエスト
 */
public class Mm0300Request extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義名称 */
	public String processDefName;
	/** 有効期間開始年月日 */
	public Date validStartDate;
	/** 有効期間終了年月日 */
	public Date validEndDate;
	/** 削除フラグ */
	public String deleteFlag;

	/** ダウンロード対象のプロセス定義リスト */
	public List<WfmProcessDef> procList;
}
