package jp.co.nci.iwf.designer.service.userData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.UserTable;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;

/**
 * パーツのユーザデータのサービス
 */
@BizLogic
public class UserDataService extends BaseRepository {

	/** 画面定義読込みサービス */
	@Inject private ScreenLoadService service;
	/** セッション情報 */
	@Inject private SessionHolder sessionHolder;
	/** パーツのユーザデータのリポジトリ */
	@Inject private UserDataRepository repository;

	/**
	 * 画面プロセスIDをキーにパーツ定義へユーザデータを読み込んで、実行時インスタンスを生成
	 * @param screenProcessId
	 * @param ctx
	 */
	@Transactional
	public void loadScreenAndUserData(RuntimeContext ctx) {
		// 画面定義およびパーツ定義の読み込み
		service.loadScreenParts(ctx.screenId, ctx);

		// 実行時インスタンスを生成し、ユーザデータを流し込む
		final PartsDesignRootContainer dc = ctx.root;
		final PartsRootContainer pc = dc.newParts(null, null, ctx);
		final List<Map<String, Object>> list = repository.getRootUserData(
				ctx.root.tableName, ctx.corporationCode, ctx.processId);
		ctx.runtimeMap.put(pc.htmlId, pc);

		// ユーザデータを再帰的に読み込む
		loadUserData(pc, dc, list, ctx);

		// ユーザデータを読み終わったので、画面カスタムクラスを呼び直す
		final IScreenCustom custom = IScreenCustom.get(ctx.screenCustomClass);
		final Map<String, PartsDesign> designCodeMap = ctx.designMap.values().stream()
				.collect(Collectors.toMap(e -> e.designCode, e -> e));
		custom.modifyDesignContext(ctx, designCodeMap);
	}

	/**
	 * ユーザデータを再帰的に読み込む
	 * @param pc コンテナパーツ
	 * @param dc コンテナパーツ定義
	 * @param userDataList ユーザデータのリスト
	 * @param ctx デザイナコンテキスト
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadUserData(
			PartsContainerBase<?> pc,
			PartsDesignContainer dc,
			List<Map<String, Object>> userDataList,
			RuntimeContext ctx
	) {
		for (int i = 0; i < userDataList.size(); i++) {
			final Map<String, Object> userData = userDataList.get(i);

			// 不足した行データがあれば、行インスタンスを追加
			final PartsContainerRow row;
			if (pc.rows.size() > i)
				row = pc.rows.get(i);
			else
				row = dc.addRows(pc, ctx);

			row.runtimeId = toLong(userData.get(UserTable.RUNTIME_ID));
			row.version = toLong(userData.get(UserTable.VERSION));

			// 行内のパーツでユーザデータを反映
			for (String htmlId : row.children) {
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);

				// 通常起票、またはコピー起票かつコピー対象パーツなら、ユーザデータを読み込む
				if (ctx.copyProcessId == null || d.copyTargetFlag) {
					p.fromUserData(d, userData);
				}

				// 子パーツがあるなら当パーツのランタイムIDを親ランタイムIDとして、ネストした子孫データもすべて反映
				if (p instanceof PartsContainerBase) {
					// 子パーツのユーザデータを抽出
					final PartsDesignContainer dcChild = (PartsDesignContainer)d;
					final PartsContainerBase<?> pcChild = (PartsContainerBase<?>)p;
					final List<Map<String, Object>> childUserDataList =
							repository.getChildUserData(dcChild.tableName , row.runtimeId);
					loadUserData(pcChild, dcChild, childUserDataList, ctx);
				}
			}
		}
	}

	/**
	 * ユーザデータを差分更新
	 * @param ctx デザイナーコンテキスト
	 * @param ecResults 有効条件判定結果Map
	 */
	public void save(RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		// ルートコンテナのパーツ
		final PartsRootContainer pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 当プロセスIDに紐付く既存データをすべて抽出
		final Map<String, Map<Long, UserDataEntity>> currentMap = new HashMap<>();
		getCurrentUserData(ctx, ctx.root, currentMap);

		// 画面の入力内容を吸い上げ
		final Map<String, List<UserDataEntity>> inputMap = new HashMap<>();
		final PartsContainerRow parentRow = null;
		fillInputedUserData(ctx, ctx.root, pc, parentRow, inputMap, ecResults);	// 論理削除もここで判断してる

		// 差分更新
		for (String tableName : inputMap.keySet()) {
			// 入力データのランタイムIDで既存データを消し込み。
			// これで消し込めたら更新、消し込めなければ新規とする
			final List<UserDataEntity> inputs = inputMap.get(tableName);
			final Map<Long, UserDataEntity> currents = currentMap.get(tableName);
			if (currents == null) {
				for (UserDataEntity input : inputs) {
					repository.insert(input, login);
				}
			}
			else {
				for (UserDataEntity input : inputs) {
					final UserDataEntity current = currents.remove(input.runtimeId);
					if (current == null)
						repository.insert(input, login);
					else
						repository.update(input, current, login);
				}
			}
		}

		// 既存データの残余は未使用データなので、削除
		for (String tableName : currentMap.keySet()) {
			Map<Long, UserDataEntity> oldTables = currentMap.get(tableName);
			if (oldTables != null && !oldTables.isEmpty()) {
				repository.deletePhysical(tableName, oldTables.keySet());
			}
		}

		// 更新完了後に、パーツ固有の処理があるかもしれない
		afterUpdateUserData(ctx, pc, ecResults);
	}

	/**
	 * ユーザデータを物理削除
	 * @param ctx デザイナーコンテキスト
	 */
	public void deletePhysical(RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		// 当プロセスIDに紐付く既存データをすべて抽出し、物理削除
		final Map<String, Map<Long, UserDataEntity>> currentMap = new HashMap<>();
		getCurrentUserData(ctx, ctx.root, currentMap);
		for (String tableName : currentMap.keySet()) {
			Map<Long, UserDataEntity> oldTables = currentMap.get(tableName);
			if (oldTables != null && !oldTables.isEmpty()) {
				repository.deletePhysical(tableName, oldTables.keySet());
			}
		}

		// 更新完了後に、パーツ固有の処理があるかもしれない
		final PartsRootContainer pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);
		afterUpdateUserData(ctx, pc, ecResults);
	}

	/**
	 * ユーザデータ更新後の、パーツ単位の後処理
	 * @param ctx
	 * @param pc
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void afterUpdateUserData(RuntimeContext ctx, PartsContainerBase<?> pc, Map<String, EvaluateCondition> ecResults) {
		for (PartsContainerRow row : pc.rows) {
			// パーツ固有の後処理を呼び出す
			for (String htmlId : row.children) {
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);
				p.afterUpdateUserData(d, ctx, pc, row, ecResults);

				// 子要素があれば再帰呼び出し
				if (p instanceof PartsContainerBase) {
					afterUpdateUserData(ctx, (PartsContainerBase<?>)p, ecResults);
				}
			}
		}
	}

	/**
	 * パーツから業務管理項目を抜き出してMap化
	 * @param ctx
	 * @return
	 */
	public Map<String, String> createBusinessInfoMap(DesignerContext ctx) {
		// ルートコンテナのパーツ
		final Map<String, String> bizMap = new HashMap<>();
		if (ctx.runtimeMap != null) {
			final PartsRootContainer pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

			collectBizInfoMap(pc, ctx, bizMap, "0");
		}
		return bizMap;
	}

	/**
	 * パーツから文書管理項目を抜き出してMap化
	 * @param ctx
	 * @return
	 */
	public Map<String, String> createDocBusinessInfoMap(DesignerContext ctx) {
		// ルートコンテナのパーツ
		final Map<String, String> bizMap = new HashMap<>();
		if (ctx.runtimeMap != null) {
			final PartsRootContainer pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

			collectBizInfoMap(pc, ctx, bizMap, "1");
		}
		return bizMap;
	}

	/**
	 * 業務管理項目を再帰的に抜き出し
	 * @param c パーツコンテナ
	 * @param ctx デザイナーコンテキスト
	 * @param bizMap 業務管理項目Map
	 * @param type 0:業務管理項目、1:文書管理項目
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void collectBizInfoMap(PartsContainerBase<?> c, DesignerContext ctx, Map<String, String> bizMap, String type) {
		for (PartsContainerRow row : c.rows) {
			for (String htmlId : row.children) {
				PartsBase p = ctx.runtimeMap.get(htmlId);
				PartsDesign d = ctx.designMap.get(p.partsId);

				// 業務管理項目の値を抜き出し
				String code = eq("1", type) ? d.docBusinessInfoCode : d.businessInfoCode;
				if (isNotEmpty(code) && !bizMap.containsKey(code)) {
					bizMap.put(code, p.toBusinessInfoValue(d, ctx));
				}
				// 子要素があれば再帰呼び出し
				if (p instanceof PartsContainerBase) {
					collectBizInfoMap((PartsContainerBase<?>)p, ctx, bizMap, type);
				}
			}
		}
	}

	/**
	 * 画面入力内容を吸い上げて、テーブル名をキーとしたMapに変換
	 * @param ctx デザイナーコンテキスト
	 * @param ecResults 有効条件Map
	 * @return テーブル名をキーとしたユーザデータMap
	 */
	public Map<String, List<UserDataEntity>> toInputedUserData(RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		// ルートコンテナのパーツ
		final PartsDesignContainer dc = ctx.root;
		final PartsRootContainer pc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 画面の入力内容を吸い上げ
		final Map<String, List<UserDataEntity>> inputMap = new HashMap<>();
		final PartsContainerRow parentRow = null;
		fillInputedUserData(ctx, dc, pc, parentRow, inputMap, ecResults);

		return inputMap;
	}

	/** 画面入力内容を吸い上げ */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fillInputedUserData(RuntimeContext ctx, PartsDesignContainer dc, PartsContainerBase<?> pc, PartsContainerRow parentRow, Map<String, List<UserDataEntity>> news, Map<String, EvaluateCondition> ecResults) {
		List<UserDataEntity> tables = news.get(dc.tableName);
		if (tables == null) {
			tables = new ArrayList<>();
			news.put(dc.tableName, tables);
		}

		int sortOrder = 0;
		for (PartsContainerRow row : pc.rows) {
			// 入力されたとみなせない行はDB更新対象外
			if (!row.isInputed(dc.inputedJudgePartsIds, ctx, ecResults)) {
				continue;
			}

			final UserDataEntity ud = new UserDataEntity();
			tables.add(ud);

			// キー
			if (row.runtimeId == null) {
				row.runtimeId = repository.nextRuntimeId();
			}
			ud.tableName = dc.tableName;
			ud.runtimeId = row.runtimeId;
			ud.parentRuntimeId = (parentRow == null ? -1L : parentRow.runtimeId);
			ud.corporationCode = ctx.corporationCode;
			ud.processId = ctx.processId;
			ud.sortOrder = ++sortOrder;
			ud.version = row.version;
			ud.deleteFlag = (ActionType.CANCEL.equals(ctx.actionType) ? DeleteFlag.ON : DeleteFlag.OFF);
			ud.values = new HashMap<>();

			// 入力値をユーザデータへ反映
			for (String htmlId : row.children) {
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);
				p.toUserData(d, ud.values, ctx, ecResults);

				// 子要素があれば再帰呼び出し
				if (d instanceof PartsDesignContainer) {
					fillInputedUserData(ctx, (PartsDesignContainer)d, (PartsContainerBase<?>)p, row, news, ecResults);
				}
			}
		}
	}

	/** 当プロセスIDに紐付く既存データをすべて抽出 */
	private void getCurrentUserData(RuntimeContext ctx, PartsDesignContainer c, Map<String, Map<Long, UserDataEntity>> olds) {
		Map<Long, UserDataEntity> tables = olds.get(c.tableName);
		if (tables == null) {
			tables = new HashMap<>();
			olds.put(c.tableName, tables);
		}

		// 既存ユーザデータを抽出し、ランタイムID単位でMap化
		final List<UserDataEntity> list = repository.get(c.tableName, ctx.corporationCode, ctx.processId);
		for (UserDataEntity utd : list) {
			tables.put(utd.runtimeId, utd);
		}

		// 子要素があれば再帰呼び出し
		for (Long partsId : c.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);
			if (d instanceof PartsDesignContainer) {
				getCurrentUserData(ctx, (PartsDesignContainer)d, olds);
			}
		}
	}

	/** ワークフローパーツ添付ファイル情報を抽出 */
	public List<MwtPartsAttachFileWf> getMwtPartsAttachFileWf(Long runtimeId, Long partsId) {
		return repository.getMwtPartsAttachFileWf(runtimeId, partsId);
	}

	/** ワークフローパーツ添付ファイルをインサート */
	public MwtPartsAttachFileWf insert(MwtPartsAttachFileWf file) {
		return repository.insert(file);
	}

	/** ワークフロー添付ファイルを抽出 */
	public MwtPartsAttachFileWf getMwtPartsAttachFileWf(Long partsAttachFileWfId) {
		if (partsAttachFileWfId == null)
			return null;
		return em.find(MwtPartsAttachFileWf.class, partsAttachFileWfId);
	}

	/**
	 * コピー元プロセスIDのユーザデータを抽出し、デザイン定義のコピー起票対象をもとにコピー処理を行う
	 * @param ctx デザイナーコンテキスト
	 * @param copyProcessId コピー元のプロセスID。
	 * @see UserDataService.loadUserData
	 */
	public void copyScreenAndUserData(RuntimeContext ctx, Long copyProcessId) {
		// コピー元プロセスIDを使って、ユーザデータをロード
		ctx.processId = copyProcessId;
		ctx.copyProcessId = copyProcessId;
		loadScreenAndUserData(ctx);

		// コピー元プロセスIDをクリアして、新規文書として扱う
		ctx.processId = null;

		// ランタイムパーツに設定されているのはコピー元プロセスのランタイムIDなので、不要なランタイムIDをクリア。
		// これでユーザデータを保存すれば、新たなランタイムIDを採番するようになる。
		// （個々のパーツのコピー要否は、UserDataService#loadUserData()の中でやってる）
		for (Iterator<PartsBase<?>> it = ctx.runtimeMap.values().iterator(); it.hasNext(); ) {
			PartsBase<?> p = it.next();
			if (p instanceof PartsContainerBase) {
				PartsContainerBase<?> pc = (PartsContainerBase<?>)p;
				for (PartsContainerRow row : pc.rows) {
					row.runtimeId = null;
					row.version = null;
				}
			}
		}

		// ユーザデータを読み終わったので、画面カスタムクラスを呼び直す
		final IScreenCustom custom = IScreenCustom.get(ctx.screenCustomClass);
		final Map<String, PartsDesign> designCodeMap = ctx.designMap.values().stream()
				.collect(Collectors.toMap(e -> e.designCode, e -> e));
		custom.modifyDesignContext(ctx, designCodeMap);
	}
}
