package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WftInformationSharer;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.HistoryInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.PullbackInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.PullforwardInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.SendbackInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ApprovalRelationInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.BlockDisplayEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessBbsInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessMemoInfo;

/**
 * 申請・承認画面コンテンツ情報
 */
public class Vd0310Contents extends BaseContents {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 集約プロセスID */
	public Long processIdAggregation;
	/** 業務ステータス */
	public String businessProcessStatus;
	/** 業務ステータス名 */
	public String businessProcessStatusName;
	/** アクティビティID（未起票の時はNULL） */
	public Long activityId;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義詳細コード */
	public String processDefDetailCode;
	/** プロセス定義名 */
	public String processDefName;
	/** 現在のアクティビティ定義コード */
	public String activityDefCode;
	/** 現在のアクティビティ定義名 */
	public String activityDefName;
	/** 開始アクティビティ定義 */
	public String startActivityDefCode;
	/** 更新日時 */
	public Timestamp timestampUpdated;
	/** 承認者設定画面を使用するか */
	public boolean useApproversSettingScreen;
	/** 連携先文書ID */
	public Long relatedDocId;

	/** プロセス状態 */
	public String processStatus;
	/** 決裁状態 */
	public String approvalStatus;
	/** 決裁No */
	public String approvalNo;
	/** 件名 */
	public String subject;
	/** 申請No */
	public String applicationNo;
	/** 申請状態 */
	public String applicationStatus;
	/** 申請年月日 */
	public Timestamp applicationDate;

	/** アクション情報 */
	public List<ActionInfo> actionList;
	/** 差戻し遷移先情報 */
	public Map<Long, List<SendbackInfo>> sendbackList;
	/** 引き戻し先情報 */
	public List<PullbackInfo> pullbackList;
	/** 引き上げ先情報 */
	public PullforwardInfo pullforwardInfo;

	/** ブロック情報 */
	public List<BlockDisplayEntity> blockList = new ArrayList<>();
	/** 承認ルート情報 */
	public List<ActivityEntity> routeList;

	/** 履歴情報 */
	public List<HistoryInfo> historyList;
	/** 決裁関連文書情報 */
	public List<ApprovalRelationInfo> approvalRelationList;
	/** デフォルト参照者情報(情報共有者定義情報) */
	public List<WfmInformationSharerDef> informationSharerDefList;
	/** 参照者情報 */
	public List<WftInformationSharer> informationSharerList;

	/** 添付ファイル情報 */
	public List<AttachFileWfInfo> attachFileWfList = new ArrayList<>();
	/** 要説明(掲示板)情報 */
	public List<ProcessBbsInfo> processBbsList;
	/** メモ情報 */
	public List<ProcessMemoInfo> memoList;
	/** 代理元のユーザ情報（企業コード_ユーザコード） */
	public String proxyUser;
	/** プロセス定義.コメント表示フラグ */
	public String processCommentDisplayFlag;
	/** 文書ファイル情報 */
	public List<DocFileWfInfo> docFileWfList = new ArrayList<>();

}
