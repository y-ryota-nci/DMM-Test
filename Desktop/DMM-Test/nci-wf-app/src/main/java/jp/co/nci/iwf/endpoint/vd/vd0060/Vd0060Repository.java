package jp.co.nci.iwf.endpoint.vd.vd0060;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 選択肢一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0060Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	public int count(Vd0060SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("VD0060_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Vd0060SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 企業コード
		sql.append(" where O.CORPORATION_CODE = ?");
		params.add(req.corporationCode);

		// パーツ選択肢コード(前方一致検索)
		if (isNotEmpty(req.optionCode)) {
			sql.append(" and O.OPTION_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.optionCode));
		}
		// パーツ選択肢名(部分一致検索)
		if (isNotEmpty(req.optionName)) {
			sql.append(" and O.OPTION_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.optionName));
		}

		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	/**
	 * ページ制御付で検索
	 * @param req
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<Vd0060Entity> select(Vd0060SearchRequest req, Vd0060SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("VD0060_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		final List<Vd0060Entity> entities = select(Vd0060Entity.class, sql.toString(), params.toArray());

		// この選択肢を参照しているコンテナを、選択肢IDをキーにMap化
		final Map<Long, List<Vd0060Container>> containerMap = getContainersUsing(entities);

		// 「この選択肢を参照しているコンテナ」を設定
		for (Vd0060Entity entity : entities) {
			em.detach(entity);
			List<Vd0060Container> containers = containerMap.get(entity.optionId);
			if (containers != null && !containers.isEmpty()) {
				entity.containerUsingOption = containers.stream()
						.map(c -> String.format("[%s] %s", c.containerCode, c.containerName))
						.distinct()
						.collect(Collectors.joining(", "));
			}
		}
		return entities;
	}

	/** 選択肢一覧を参照しているコンテナ一覧を抽出 */
	private Map<Long, List<Vd0060Container>> getContainersUsing(List<Vd0060Entity> entities) {
		final Set<Long> optionIds = entities.stream().map(e -> e.optionId).collect(Collectors.toSet());
		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());
		params.addAll(optionIds);

		final String KEY = quotePattern("${OPTION_IDS}");
		final String replacement = toInListSql("PO.OPTION_ID", entities.size());
		final String sql = getSql("VD0060_08").replaceFirst(KEY, replacement);
		return select(Vd0060Container.class, sql, params.toArray())
				.stream()
				.collect(Collectors.groupingBy(c -> c.optionId));
	}

	/**
	 * 削除
	 * @param optionId 選択肢ID
	 */
	public void delete(Long optionId) {
		// MWM_OPTION
		final Object[] params = { optionId };
		execSql(getSql("VD0060_03"), params);
		// 多言語マスタの削除
		this.deleteMultilingal("MWM_OPTION", Arrays.asList(optionId));

		// 多言語削除用に選択肢IDに紐付くパーツ選択肢項目の選択肢項目ID一覧を取得
		final List<Long> optionItemIds = this.getOptionItemIds(optionId);
		// MWM_OPTION_ITEM
		execSql(getSql("VD0060_04"), params);
		// 多言語マスタの削除
		if (!optionItemIds.isEmpty())
			this.deleteMultilingal("MWM_OPTION_ITEM", optionItemIds);

		// MWM_PARTS_OPTION
		execSql(getSql("VD0060_05"), params);
	}

	private void deleteMultilingal(String tableName, List<Long> ids) {
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("VD0060_06"));
		sql.append(toInListSql("ID", ids.size()));
		final List<Object> params = new ArrayList<>();
		params.add(tableName);
		params.addAll(ids);
		execSql(sql.toString(), params.toArray());
	}

	private List<Long> getOptionItemIds(Long optionId) {
		StringBuilder sql = new StringBuilder(getSql("VD0060_07"));
		final Object[] params = { optionId };
		return select(MwmOptionItem.class, sql.toString(), params).stream()
					.map(MwmOptionItem::getOptionItemId)
					.collect(Collectors.toList());
	}
}
