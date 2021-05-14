package jp.co.nci.iwf.component.route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * アクティビティ情報.
 * 並行承認(開始)であれば子供に自分自身の一覧を保持する
 */
public class ActivityEntity implements Serializable {

	/** ID(承認ルート内では一意となる) */
	public Long id;
	/** 会社コード */
	public String corporationCode;
	/** プロセスID */
	public Long processId;
	/** テンプレートID() */
	public Long templateId;
	/** アクティビティID */
	public Long activityId;
	/** 次アクティビティID */
	public Long activityIdNext;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義コード枝番 */
	public String processDefDetailCode;
	/** アクティビティ定義コード */
	public String activityDefCode;
	/** アクティビティ定義名 */
	public String activityDefName;
	/** アクティビティ状態(ACTIVITY_STATUS). */
	public String activityStatus;
	/** アクティビティ区分 */
	public String activityType;
//	/** アクティビティキー */
//	public String activityKey;
	/** 親アクティビティキー */
	public String parentKey;
//	/** 前アクティビティキー */
//	public String previousKey;
//	/** 前テンプレートID */
//	public Long templateIdPrev;
	/** 次テンプレートID */
	public Long templateIdNext;
//	/** 並行終了アクティビティキー(branchStartActivity==trueの場合、対応する分岐終了アクティビティのキーが入る) */
//	public String branchEndKey;
//	/** 並行終了アクティビティのテンプレートID(branchStartActivity==trueの場合、対応する分岐終了アクティビティのtemplateIdが入る) */
//	public Long branchEndTemplateId;
	/** アクティビティ処理日 */
	public Date activityDate;

	/** 承認者一覧 */
	public List<AssignedUserInfo> assignedUserList;
//	/** 新規承認者一覧 */
//	public List<AssignedUserInfo> newAssignedUserList;

	/** 現在のアクティビティかどうか */
	public boolean currentActivity;
	/** 終了済みアクティビティかどうか */
	public boolean closeActivity;
	/** 分岐スタートアクティビティかどうか */
	public boolean branchStartActivity;
	/** 削除可能アクティビティかどうか */
	public boolean deletableActivity;
	/** 承認者変更可能アクティビティかどうか */
	public boolean changeableActivity;
	/** 参加者変更定義が存在するかどうか */
	public boolean existChangeDefFlag;
	/** 新規に追加したアクティビティかどうか */
	public boolean addedActivity;
	/** 参加者変更されたアクティビティかどうか */
	public boolean changeActivity;

	/** 並行承認内のAssignedEntity一覧 */
	public List<ActivityEntity> children;

	/**
	 * コンストラクタ.
	 */
	public ActivityEntity() {
		children = new ArrayList<ActivityEntity>();
	}
}
