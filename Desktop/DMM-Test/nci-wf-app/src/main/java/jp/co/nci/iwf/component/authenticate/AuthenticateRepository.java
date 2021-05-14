package jp.co.nci.iwf.component.authenticate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.BelongType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CorporationCode;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenuScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;
import jp.co.nci.iwf.jpa.entity.wm.WfmAssignRoleDetail;
import jp.co.nci.iwf.jpa.entity.wm.WfmMenuRoleDetail;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * ログイン認証のリポジトリ
 */
@ApplicationScoped
public class AuthenticateRepository extends BaseRepository {
	@Inject private Logger log;

	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * ユーザ所属で所有しているメニューロールCDを抽出
	 * @param belongs ユーザ所属リスト
	 * @return
	 */
	public Set<String> getMenuRoleCds(List<WfvUserBelongImpl> belongs) {
		if (belongs == null || belongs.isEmpty()) {
			return new HashSet<>();
		}

		List<String> replace = new ArrayList<>();
		List<String> params = new ArrayList<>();

		// 企業
		{
			final Set<String> corps = belongs.stream()
					.map(e -> e.getCorporationCode())
					.collect(Collectors.toSet());
			for (String corporationCode : corps) {
				replace.add("(MENU_ROLE_ASSIGNMENT_TYPE='A' and CORPORATION_CODE_ACCESS=?)");
				params.add(corporationCode);
			}
		}
		// 組織
		{
			final Map<String, Set<String>> orgs = belongs.stream()
				.collect(
					Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
					Collectors.mapping(WfvUserBelongImpl::getOrganizationCode, Collectors.toSet())));

			for (String corporationCode : orgs.keySet()) {
				Set<String> organizationCodes = orgs.get(corporationCode);
				for (String organizationCode : organizationCodes) {
					replace.add("(MENU_ROLE_ASSIGNMENT_TYPE='1' and CORPORATION_CODE_ACCESS=? and ORGANIZATION_CODE_ACCESS=?)");
					params.add(corporationCode);
					params.add(organizationCode);
				}
			}
		}
		// 組織＋役職
		final Map<String, Map<String, Set<String>>> orgPostCodes = belongs.stream()
				.collect(
						Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
								Collectors.groupingBy(WfvUserBelongImpl::getOrganizationCode,
									Collectors.mapping(
											WfvUserBelongImpl::getPostCode,
											Collectors.toSet()))));
		for (String corporationCode : orgPostCodes.keySet()) {
			Map<String, Set<String>> orgs = orgPostCodes.get(corporationCode);
			for (String organizationCode : orgs.keySet()) {
				Set<String> posts = orgs.get(organizationCode);
				for (String postCode : posts) {
					replace.add("(MENU_ROLE_ASSIGNMENT_TYPE='2' and CORPORATION_CODE_ACCESS=? and ORGANIZATION_CODE_ACCESS=? and POST_CODE_ACCESS=?)");
					params.add(corporationCode);
					params.add(organizationCode);
					params.add(postCode);
				}
			}
		}
		// 役職
		{
			final Map<String, Set<String>> posts = belongs.stream()
				.collect(
					Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
					Collectors.mapping(WfvUserBelongImpl::getPostCode, Collectors.toSet())));
			for (String corporationCode : posts.keySet()) {
				Set<String> postCodes = posts.get(corporationCode);
				for (String postCode : postCodes) {
					replace.add("(MENU_ROLE_ASSIGNMENT_TYPE='3' and CORPORATION_CODE_ACCESS=? and POST_CODE_ACCESS=?)");
					params.add(corporationCode);
					params.add(postCode);
				}
			}
		}
		// ユーザ
		{
			final Map<String, Set<String>> users = belongs.stream()
					.collect(Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
							Collectors.mapping(WfvUserBelongImpl::getUserCode, Collectors.toSet())));
			for (String corporationCode : users.keySet()) {
				Set<String> userCodes = users.get(corporationCode);
				for (String userCode : userCodes) {
					replace.add("(MENU_ROLE_ASSIGNMENT_TYPE='4' and CORPORATION_CODE_ACCESS=? and USER_CODE_ACCESS=?)");
					params.add(corporationCode);
					params.add(userCode);
				}
			}
		}
		String sql = getSql("AU0001").replaceFirst("###REPLACE###", String.join(" or ", replace));

		// メニューロールCDだけを集約
		final Set<String> roleCds = select(WfmMenuRoleDetail.class, sql, params.toArray())
				.stream()
				.map(d -> d.getPk().getMenuRoleCode())
				.collect(Collectors.toSet());
		return roleCds;
	}

	/**
	 * メニューロールCDがアクセス可能なメニューを抽出
	 * @param tableCorporationCode 企業コード
	 * @param menuRoleCds メニューロールコード
	 * @return
	 */
	public List<MwmMenu> getAccessibleMenus(List<WfvUserBelongImpl> belongs, Collection<String> menuRoleCds, String localeCode) {
		if (isEmpty(belongs) || isEmpty(menuRoleCds)) {
			return new ArrayList<>();
		}

		final List<String> params = new ArrayList<>();
		params.add(localeCode);
		params.add(belongs.get(0).getCorporationCode());

		StringBuilder replace = new StringBuilder();
		for (String menuRoleCd : menuRoleCds) {
			replace.append(replace.length() == 0 ? "?" : ", ?");
			params.add(menuRoleCd);
		}
		String sql = "";
		// ASP管理者権限をもっているときは会社コードASPの権限コードASPを参照する
		if (menuRoleCds.contains(CorporationCode.ASP)) {
			String addASP = "(CORPORATION_CODE = 'ASP' and MENU_ROLE_CODE = 'ASP') or ";
			sql = getSql("AU0002").replaceFirst("###REPLACE###", addASP).replaceFirst("###REPLACE###", replace.toString());
		} else {
		// 管理者権限をもっていないとき
			sql = getSql("AU0002").replaceFirst("###REPLACE###", "").replaceFirst("###REPLACE###", replace.toString());
		}

		List<MwmMenu> menus = select(MwmMenu.class, sql, params.toArray());

		// あとでメニューのURLを補正する場合があるので、JPAとの連携を切断する
		for (MwmMenu m : menus) {
			em.detach(m);
		}
		return menus;
	}

	/** メニューIDをキーにメニュー配下画面マスタから画面IDを返す */
	public Set<String> getAccessibleScreenIds(List<MwmMenu> accessibleMenus) {
		StringBuilder sql = new StringBuilder(getSql("AU0008"));
		sql.append(" ").append(toInListSql("MENU_ID", accessibleMenus.size()));
		Object[] params = accessibleMenus.stream().map(am -> am.getMenuId()).toArray();

		try {
			List<MwmMenuScreen> list = select(MwmMenuScreen.class, sql.toString(), params);
			Set<String> results = new HashSet<>();
			for (MwmMenuScreen ms : list) {
				if (isNotEmpty(ms.getScreenId())) {
					String[] screenIds = ms.getScreenId().split("[ ,]");
					for (String screenId : screenIds) {
						results.add(screenId);
					}
				}
			}
			return results;
		}
		catch (PersistenceException e) {
			log.warn("メニュー配下画面マスタが存在しないため、アクセス権の判定を行いません。");
			return null;
		}
	}

	/** 画面プロセス／メニュー連携マスタを抽出 */
	public List<MwmScreenProcessMenu> getMwmScreenProcessMenu(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmScreenProcessMenu.class, getSql("AU0000_01"), params);
	}

	/**
	 * ユーザ所属で所有している文書管理用の参加者ロールCDを抽出.
	 * ※文書管理用は所属区分が「A, 1, 2, 3, 4」に限定して抽出しています
	 * @param belongs ユーザ所属リスト
	 * @return
	 */
	public Set<String> getDocAssignRoleCds(List<WfvUserBelongImpl> belongs) {
		if (belongs == null || belongs.isEmpty()) {
			return new HashSet<>();
		}

		List<String> replace = new ArrayList<>();
		List<String> params = new ArrayList<>();

		// 企業
		{
			final Set<String> corps = belongs.stream()
					.map(e -> e.getCorporationCode())
					.collect(Collectors.toSet());
			for (String corporationCode : corps) {
				replace.add("(BELONG_TYPE='A' and CORPORATION_CODE_ASSIGNED=?)");
				params.add(corporationCode);
			}
		}
		// 組織
		{
			final Map<String, Set<String>> orgs = belongs.stream()
				.collect(
					Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
					Collectors.mapping(WfvUserBelongImpl::getOrganizationCode, Collectors.toSet())));

			for (String corporationCode : orgs.keySet()) {
				Set<String> organizationCodes = orgs.get(corporationCode);
				for (String organizationCode : organizationCodes) {
					replace.add("(BELONG_TYPE='1' and CORPORATION_CODE_ASSIGNED=? and ORGANIZATION_CODE_ASSIGNED=?)");
					params.add(corporationCode);
					params.add(organizationCode);
				}
			}
		}
		// 組織＋役職
		final Map<String, Map<String, Set<String>>> orgPostCodes = belongs.stream()
				.collect(
						Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
								Collectors.groupingBy(WfvUserBelongImpl::getOrganizationCode,
									Collectors.mapping(
											WfvUserBelongImpl::getPostCode,
											Collectors.toSet()))));
		for (String corporationCode : orgPostCodes.keySet()) {
			Map<String, Set<String>> orgs = orgPostCodes.get(corporationCode);
			for (String organizationCode : orgs.keySet()) {
				Set<String> posts = orgs.get(organizationCode);
				for (String postCode : posts) {
					replace.add("(BELONG_TYPE='2' and CORPORATION_CODE_ASSIGNED=? and ORGANIZATION_CODE_ASSIGNED=? and POST_CODE_ASSIGNED=?)");
					params.add(corporationCode);
					params.add(organizationCode);
					params.add(postCode);
				}
			}
		}
		// 役職
		{
			final Map<String, Set<String>> posts = belongs.stream()
				.collect(
					Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
					Collectors.mapping(WfvUserBelongImpl::getPostCode, Collectors.toSet())));
			for (String corporationCode : posts.keySet()) {
				Set<String> postCodes = posts.get(corporationCode);
				for (String postCode : postCodes) {
					replace.add("(BELONG_TYPE='3' and CORPORATION_CODE_ASSIGNED=? and POST_CODE_ASSIGNED=?)");
					params.add(corporationCode);
					params.add(postCode);
				}
			}
		}
		// ユーザ
		{
			final Map<String, Set<String>> users = belongs.stream()
					.collect(Collectors.groupingBy(WfvUserBelongImpl::getCorporationCode,
							Collectors.mapping(WfvUserBelongImpl::getUserCode, Collectors.toSet())));
			for (String corporationCode : users.keySet()) {
				Set<String> userCodes = users.get(corporationCode);
				for (String userCode : userCodes) {
					replace.add("(BELONG_TYPE='4' and CORPORATION_CODE_ASSIGNED=? and USER_CODE_ASSIGNED=?)");
					params.add(corporationCode);
					params.add(userCode);
				}
			}
		}
		String sql = getSql("AU0011").replaceFirst("###REPLACE###", String.join(" or ", replace));

		// 参加者ロールCDだけを集約
		final Set<String> roleCds = select(WfmAssignRoleDetail.class, sql, params.toArray())
				.stream()
				.map(d -> d.getPk().getAssignRoleCode())
				.collect(Collectors.toSet());
		return roleCds;
	}

	/** ユーザ所属から所有している参加者ロールCDを返す */
	public Set<String> getAssignRoleCds(String corporationCode, List<WfvUserBelongImpl> belongs) {
		final List<Object> params = asList(corporationCode);
		final Stream<String> belongTypes = Stream.of(BelongType.ORGANIZATION, BelongType.ORG_POST, BelongType.POST, BelongType.USER);
		final String sub = belongTypes
				.flatMap(belongType -> belongs.stream().map(ub -> {
					if (eq(BelongType.ORGANIZATION, belongType)) {
						params.add(ub.getCorporationCode());
						params.add(ub.getOrganizationCode());
						return "(X.BELONG_TYPE = '1' and X.CORPORATION_CODE_ASSIGNED = ? and X.ORGANIZATION_CODE_ASSIGNED = ?)";
					}
					if (eq(BelongType.ORG_POST, belongType)) {
						params.add(ub.getCorporationCode());
						params.add(ub.getOrganizationCode());
						params.add(ub.getPostCode());
						return "(X.BELONG_TYPE = '2' and X.CORPORATION_CODE_ASSIGNED = ? and X.ORGANIZATION_CODE_ASSIGNED = ? and X.POST_CODE_ASSIGNED = ?)";
					}
					if (eq(BelongType.POST, belongType)) {
						params.add(ub.getCorporationCode());
						params.add(ub.getPostCode());
						return "(X.BELONG_TYPE = '3' and X.CORPORATION_CODE_ASSIGNED = ? and X.POST_CODE_ASSIGNED = ?)";
					}
					if (eq(BelongType.USER, belongType)) {
						params.add(ub.getCorporationCode());
						params.add(ub.getUserCode());
						return "(X.BELONG_TYPE = '4' and X.CORPORATION_CODE_ASSIGNED = ? and X.USER_CODE_ASSIGNED = ?)";
					}
					return "";
				}))
				.collect(Collectors.joining(" or "));

		final String sql = getSql("AU0000_02").replaceFirst(REPLACE, sub);

		final Connection conn = em.unwrap(Connection.class);	// このConnectionはJPA管理だから勝手に閉じてはダメ
		try {
			return NativeSqlUtils.select(conn, WfmAssignRole.class, sql.toString(), params.toArray())
					.stream()
					.map(ar -> ar.getAssignRoleCode())
					.collect(Collectors.toSet());
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
