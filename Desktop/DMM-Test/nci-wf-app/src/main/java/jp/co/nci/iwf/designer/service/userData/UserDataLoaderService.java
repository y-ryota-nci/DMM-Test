package jp.co.nci.iwf.designer.service.userData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ユーザデータをMap形式で読み込んで、ランタイムMapを生成するサービス
 */
@ApplicationScoped
public class UserDataLoaderService extends MiscUtils {
	@Inject private UserDataRepository repository;

	public List<UserDataEntity> getUserData(String tableName, String sql, Object[] params) {
		assert tableName != null : "テーブル名が未指定です";
		assert sql != null : "SQLが未指定です";
		return repository.selectUserData(tableName, sql, params);
	}

	/**
	 * ユーザデータリストをパーツに読み込む
	 * @param ctx デザイナーコンテキスト
	 * @param tables テーブル名をキー、「そのテーブルのユーザデータリスト」を値としたMap
	 * @param copyTargetOnly コピー対象パーツのみを読み込むならtrue、すべて読み込むならfalse
	 */
	public void fillUserData(RuntimeContext ctx, Map<String, List<UserDataEntity>> tables, boolean copyTargetOnly) {
		// 旧データを削除
		ctx.runtimeMap.clear();
		// ルートコンテナを生成
		final PartsDesignRootContainer dc = ctx.root;
		final PartsRootContainer pc = dc.newParts(null, null, ctx);
		ctx.runtimeMap.put(pc.htmlId, pc);
		fillUserData(ctx, tables, dc, pc, copyTargetOnly);

		// ユーザデータを読み終わったので、画面カスタムクラスを呼び直す
		final IScreenCustom custom = IScreenCustom.get(ctx.screenCustomClass);
		final Map<String, PartsDesign> designCodeMap = ctx.designMap.values().stream()
				.collect(Collectors.toMap(e -> e.designCode, e -> e));
		custom.modifyDesignContext(ctx, designCodeMap);
	}

	/**
	 * 【再帰呼び出し】ユーザデータリストをパーツに読み込む
	 * @param ctx デザイナーコンテキスト
	 * @param tables テーブル名をキー、「そのテーブルのユーザデータリスト」を値としたMap
	 * @param dc コンテナのパーツ定義
	 * @param pc 実行時パーツ情報
	 * @param copyTargetOnly コピー対象パーツのみを読み込むならtrue、すべて読み込むならfalse
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fillUserData(RuntimeContext ctx, Map<String, List<UserDataEntity>> tables, PartsDesignContainer dc, PartsContainerBase<?> pc, boolean copyTargetOnly) {
		final List<UserDataEntity> userDataList = tables.get(dc.tableName);
		if (userDataList == null) {
			return;
		}

		for (int i = 0; i < userDataList.size(); i++) {
			// コンテナの不足行を追加
			final PartsContainerRow row;
			if (pc.rows.size() > i)
				row = pc.rows.get(i);
			else
				row = dc.addRows(pc, ctx);

			final Map<String, Object> userData = userDataList.get(i).values;
			for (String htmlId : row.children) {
				// 「すべてコピー」 or 「コピー対象のみ＋パーツがコピー対象」なら、ユーザデータを反映
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);
				if (!copyTargetOnly || d.copyTargetFlag) {
					p.fromUserData(d, userData);
				}

				// 子パーツがあるならネストした子孫データもすべて反映
				if (p instanceof PartsContainerBase) {
					final PartsDesignContainer dcChild = (PartsDesignContainer)d;
					final PartsContainerBase<?> pcChild = (PartsContainerBase<?>)p;
					fillUserData(ctx, tables, dcChild, pcChild, copyTargetOnly);
				}
			}
		}
	}
}
