package jp.co.nci.iwf.designer.parts;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.javascript.ChangeStartUserFunction;
import jp.co.nci.iwf.designer.service.javascript.LoadFunction;
import jp.co.nci.iwf.designer.service.javascript.SubmitFunction;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.StampInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;

/**
 * 画面デザイナのレンダリング用コンテキスト
 */
public class DesignerContext implements Serializable {

	/** レンダリングモード */
	public RenderMode renderMode;

	/** トレイタイプ */
	public TrayType trayType;

	/** ルートコンテナ定義 */
	public PartsDesignRootContainer root;

	/** 画面ID */
	public long screenId;
	/** 画面コード */
	public String screenCode;
	/** 画面名称 */
	public String screenName;
	/** 画面情報 */
	public MwvScreen screen;

	/** 表示条件ID */
	public Long dcId;

	/** パーツIDをキーとしたデザイン時パーツ定義Map */
	public Map<Long, PartsDesign> designMap = new LinkedHashMap<>();

	/** HtmlIdをキーとした実行時パーツMap */
	public Map<String, PartsBase<?>> runtimeMap = new LinkedHashMap<>();

	/** パーツIDをキーとしたパーツ表示条件.表示区分Map */
	public Map<Long, Integer> dcMap = new HashMap<>();

//	/** 有効条件の比較先パーツIDをキーとした比較元パーツIDMap */
//	public Map<Long, Set<Long>> targetEcMap = new HashMap<Long, Set<Long>>();

	/** パーツ条件の比較先パーツIDをキーとした比較元パーツIDMap */
	public Map<Long, Set<Long>> targetCondMap = new HashMap<Long, Set<Long>>();

	/** 計算式の計算元パーツIDをキーとした計算先パーツIDMap */
	public Map<Long, Set<Long>> targetCalcMap = new HashMap<>();

	/** 計算条件の判定元パーツIDをキーとした計算先パーツIDMap */
	public Map<Long, Set<Long>> targetCalcEcMap = new HashMap<>();

	/** 使用されている全コンテナIDリスト */
	public Set<Long> containerIds = new HashSet<>();
	/** 入力者情報 */
	public UserInfo processUserInfo;
	/** 起案担当者 */
	public UserInfo startUserInfo;
	/** ハンコ履歴情報 */
	public Map<String, StampInfo> stampMap = new HashMap<>();
	/** Ajaxの起動元パーツのパーツID一覧Map */
	public Map<Long, Set<Long>> triggerAjaxMap = new HashMap<>();
	/** Submit時の呼び出しJavascript関数 */
	public List<SubmitFunction> submitFunctions = new ArrayList<>();
	/** Load時の呼び出しJavascript関数 */
	public List<LoadFunction> loadFunctions = new ArrayList<>();
	/** 起案担当者変更時の呼び出しJavascript関数 */
	public List<ChangeStartUserFunction> changeStartUserFunctions = new ArrayList<>();
	/** 外部Javascriptとして参照するjavascriptIdリスト */
	public List<Long> javascriptIds = new ArrayList<>();
	/** 画面カスタムクラス名（FQCN） */
	public String screenCustomClass;
	/** クライアント側の表示幅 */
	public ViewWidth viewWidth;

	/** デザイン時のコンテキストをインスタンス化 */
	public static DesignerContext designInstance(ViewWidth viewWidth) {
		// デザイン時は決め打ちで生成してよい
		final DesignerContext ctx = new DesignerContext();
		ctx.viewWidth = viewWidth;
		ctx.renderMode = RenderMode.DESIGN;
		ctx.trayType = TrayType.NEW;
		final UserInfo dummy = createDummyUserInfo();
		ctx.startUserInfo = dummy;
		ctx.processUserInfo = dummy;
		return ctx;
	}

	/** プレビュー時のコンテキストをインスタンス化 */
	public static DesignerContext previewInstance(Long dcId, TrayType trayType, ViewWidth viewWidth) {
		// デザイン時は決め打ちで生成してよい
		final DesignerContext ctx = designInstance(viewWidth);
		ctx.renderMode = RenderMode.PREVIEW;
		ctx.dcId = dcId;
		ctx.trayType = trayType;
		final UserInfo dummy = createDummyUserInfo();
		ctx.startUserInfo = dummy;
		ctx.processUserInfo = dummy;
		return ctx;
	}

	private static UserInfo createDummyUserInfo() {
		return new UserInfo(CDI.current().select(SessionHolder.class).get().getLoginInfo());
	}
}
