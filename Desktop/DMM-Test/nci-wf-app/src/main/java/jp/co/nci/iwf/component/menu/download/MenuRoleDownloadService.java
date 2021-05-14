package jp.co.nci.iwf.component.menu.download;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.download.BaseDownloadService;
import jp.co.nci.iwf.component.route.download.ProcessDefNameLookupService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

@BizLogic
public class MenuRoleDownloadService extends BaseDownloadService {
	@Inject private WfInstanceWrapper wf;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;
	@Inject private ProcessDefNameLookupService nameLookup;

	/**
	 * 指定されたプロセス定義とその関連テーブルをすべて抽出
	 * @param corporationCode 企業コード
	 * @param corporationName 企業名
	 * @param menuRoleType メニューロール区分
	 * @return
	 */
	public MenuRoleDownloadDto createDto(String corporationCode, String corporationName, String menuRoleType, Set<String> menuRoleCodes) {
		if (isEmpty(corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final MenuRoleDownloadDto dto = new MenuRoleDownloadDto(corporationCode, corporationName, menuRoleType);

		//------------------------------------------
		// ダウンロード時のAPPバージョンとDB接続先情報
		//------------------------------------------
		dto.appVersion = manifest.getVersion();
		dto.dbDestination = destination.getUrl();
		dto.dbUser = destination.getUser();
		dto.timestampCreated = timestamp();
		dto.hostIpAddr = hsr.getLocalAddr();
		dto.hostName = hsr.getLocalName();

		// メニューロール(WFM_MENU_ROLE)
		dto.menuRoleList = getMenuRoleList(corporationCode, menuRoleType, menuRoleCodes);
		// メニューロール構成(WFM_MENU_ROLE_DETAIL)
		dto.menuRoleDetailList = getMenuRoleDetailList(corporationCode, menuRoleCodes);

		// 名称ルックアップリスト(WFM_NAME_LOOKUP)
		{
			final Map<Class<?>, List<?>> toAllEntities = toAllEntities(dto);
			dto.nameLookupList = nameLookup.getNameLookupList(toAllEntities);
		}

		// 抽出結果がユニークキーと矛盾してないか検証
		validateUniqueKeys(dto);

		return dto;
	}

	/** メニューロール構成(WFM_MENU_ROLE_DETAIL) */
	private List<WfmMenuRoleDetail> getMenuRoleDetailList(String corporationCode, Set<String> menuRoleCodes) {
		final SearchWfmMenuRoleDetailInParam in = new SearchWfmMenuRoleDetailInParam();
		in.setCorporationCode(corporationCode);
		return wf.searchWfmMenuRoleDetail(in).getMenuRoleDetails()
				.stream()
				.filter(e -> menuRoleCodes.contains(e.getMenuRoleCode()))
				.collect(Collectors.toList());
	}

	/** メニューロール(WFM_MENU_ROLE) */
	private List<WfmMenuRole> getMenuRoleList(String corporationCode, String menuRoleType, Set<String> menuRoleCodes) {
		final SearchWfmMenuRoleInParam in = new SearchWfmMenuRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setMenuRoleType(menuRoleType);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "MR." + WfmMenuRole.MENU_ROLE_TYPE),
				new OrderBy(true, "MR." + WfmMenuRole.CORPORATION_CODE),
				new OrderBy(true, "MR." + WfmMenuRole.MENU_ROLE_CODE),
		});
		// 抽出されたメニューロールの大半は選択されるはずなので、インメモリで絞り込みを行う。
		// メニューロールもせいぜい300件ぐらいしかないので、これで十分だ
		return wf.searchWfmMenuRole(in).getMenuRoles().stream()
				.filter(mr -> menuRoleCodes.contains(mr.getMenuRoleCode()))
				.collect(Collectors.toList());
	}

	/** ダウンロード対象のエンティティリストをリフレクションで扱いやすいようフラットに配列化 */
	public Map<Class<?>, List<?>> toAllEntities(MenuRoleDownloadDto dto) {
		// ダウンロード対象のエンティティをフラットに扱うためMap化
		Map<Class<?>, List<?>> map = new HashMap<>();
		map.put(WfmMenuRole.class, dto.menuRoleList);
		map.put(WfmMenuRoleDetail.class, dto.menuRoleDetailList);
		map.put(WfmNameLookup.class, dto.nameLookupList);
		return map;
	}
}
