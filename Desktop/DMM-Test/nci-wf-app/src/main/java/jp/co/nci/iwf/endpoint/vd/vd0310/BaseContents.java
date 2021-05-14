package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.javascript.ChangeStartUserFunction;
import jp.co.nci.iwf.designer.service.javascript.LoadFunction;
import jp.co.nci.iwf.designer.service.javascript.SubmitFunction;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.StampInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.WfLatestHistoryInfo;

/**
 * ワークフロー／文書管理におけるContentsの基底クラス.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)	// クラスのFQCNをJSONフィールドに出力。クライアントから書き戻す際に必要
public abstract class BaseContents implements Serializable {

	/** 企業コード */
	public String corporationCode;
	/** プロセスID */
	public Long processId;
	/** 画面プロセス定義ID */
	public Long screenProcessId;
	/** 画面プロセス定義コード */
	public String screenProcessCode;
	/** 画面プロセス定義名 */
	public String screenProcessName;
	/** 画面ID */
	public Long screenId;
	/** 画面名 */
	public String screenName;
	/** 実行時データを格納したパーツのランタイムMap */
	public Map<String, PartsBase<?>> runtimeMap;
	/** 画面のカスタムCSSスタイル */
	public String customCssStyleTag;
	/** トレイタイプ */
	public TrayType trayType;
	/** 表示条件ID */
	public Long dcId;
	/** 入力者情報 */
	public UserInfo processUserInfo;
	/** 起案担当者 */
	public UserInfo startUserInfo;
	/** 現在のアクティビティ定義(未起票の場合は開始アクティビティ定義) */
	public WfmActivityDef activityDef;
	/** Submit時の呼び出しJavascript関数 */
	public List<SubmitFunction> submitFunctions;
	/** Load時の呼び出しJavascript関数 */
	public List<LoadFunction> loadFunctions;
	/** 起案担当者変更時の呼び出し関数 */
	public List<ChangeStartUserFunction> changeStartUserFunctions;
	/** 画面カスタムクラス */
	public String screenCustomClass;
	/** 最新の承認履歴 */
	public List<WfLatestHistoryInfo> latestHistoryList;
	/** スタンプ履歴情報 */
	public Map<String, StampInfo> stampMap;
	/** 外部JavascriptのjavascriptIdリスト */
	public List<Long> javascriptIds;
	/** 文書ID */
	public Long docId;
	/** 画面文書定義ID */
	public Long screenDocId;
	/** 画面文書定義名 */
	public String screenDocName;
	/** 旧消費税率(8%)を表示する基準日 */
	public String oldTaxRateDisplayReferenceDate;
}
