package jp.co.nci.iwf.endpoint.na.na0002;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmMenuRoleOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.endpoint.na.NaCodeBook;
import jp.co.nci.iwf.jpa.entity.ex.MwvContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleScreen;

/**
 * アクセス可能画面設定サービス
 */
@BizLogic
public class Na0002Service extends MmBaseService<WfmMenuRole> implements NaCodeBook {

	@Inject
	Na0002Repository repository;
	@Inject
	private NumberingService numbering;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Na0002Response init(Na0002Request req) {
		final Na0002Response res = this.createResponse(Na0002Response.class, req);

		if (isEmpty(req.corporationCode)) {
			throw new BadRequestException("企業コードが未指定です");
		}
		if (isEmpty(req.menuRoleCode)) {
			throw new BadRequestException("メニューロールコードが未指定です");
		}

		SearchWfmMenuRoleInParam inParam = new SearchWfmMenuRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setMenuRoleCode(req.menuRoleCode);
		inParam.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
		SearchWfmMenuRoleOutParam outParam = wf.searchWfmMenuRole(inParam);
		final List<WfmMenuRole> list = outParam.getMenuRoles();
		if (CommonUtil.isEmpty(list)) {
			throw new BadRequestException("存在しないメニューロールです");
		}
		res.menuRole = list.get(0);

		// メニュー取得
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		res.accessibleScreens = repository.getAccessibleScreen(req.corporationCode, req.menuRoleCode, localeCode);
		res.treeItems = repository.getScreenProcessLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
				.stream()
				.filter(s -> !res.accessibleScreens.stream().anyMatch(c -> Type.FILE.equals(s.type) && c.screenProcessId.equals(s.id)))
				.map(s -> new Na0002TreeItem(s))
				.collect(Collectors.toList());

		res.success = true;

		return res;
	}

	/**
	 * 登録処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Na0002Response create(Na0002Request req) {
		final Na0002Response res = createResponse(Na0002Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		String error = validate(req);
		if (isEmpty(error)) {
			req.accessibleScreens.forEach(e -> {
				e.setAccessibleScreenId(numbering.newPK(MwmAccessibleScreen.class));
				e.setCorporationCode(req.corporationCode);
				e.setMenuRoleCode(req.menuRoleCode);;
				e.setDeleteFlag(DeleteFlag.OFF);
				repository.insert(e);
			});

			res.accessibleScreens = repository.getAccessibleScreen(req.corporationCode, req.menuRoleCode, localeCode);
			res.treeItems = repository.getScreenProcessLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
					.stream()
					.filter(s -> !res.accessibleScreens.stream().anyMatch(c -> Type.FILE.equals(s.type) && c.screenProcessId.equals(s.id)))
					.map(s -> new Na0002TreeItem(s))
					.collect(Collectors.toList());

			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.accessibleManagement));
			res.success = true;
		} else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/** 登録用エラーチェック */
	private String validate(Na0002Request req) {
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		for (MwmAccessibleScreen as : req.accessibleScreens) {
			if (isEmpty(as.getCorporationCode()) || isEmpty(as.getMenuRoleCode())
					|| isEmpty(as.getScreenProcessId())
			) {
					throw new BadRequestException("更新キーが足りません");
			}
			final MwvScreenProcessDef spd = repository.getMwvScreenProcess(
					as.getScreenProcessId(), localeCode);

			// 自分自身を除く同一キーのレコードがすでに存在するか
			if (repository.isExists(as)) {
				WfmMenuRole mr = getWfmMenuRole(as.getCorporationCode(), as.getMenuRoleCode());
				String key = String.format("%s=%s, %s=%s",
						i18n.getText(MessageCd.screenProcessName), spd.screenProcessName,
						i18n.getText(MessageCd.menuRoleName), mr.getMenuRoleName());
				return i18n.getText(MessageCd.MSG0192, key);
			}

			// 画面に含まれているコンテナが、すべてテーブル同期済みあること
			List<MwvContainer> containers = repository.getMwvContainers(spd.screenProcessId, localeCode);
			for (MwvContainer c : containers) {
				if (c.tableModifiedTimestamp == null
						|| compareTo(c.tableSyncTimestamp, c.tableModifiedTimestamp) < 0) {
					return i18n.getText(MessageCd.MSG0193, spd.screenProcessName, c.containerName);
				}
			}
		}
		return null;
	}

	/** メニューロール抽出 */
	private WfmMenuRole getWfmMenuRole(String corporationCode, String menuRoleCode) {
		final SearchWfmMenuRoleInParam in = new SearchWfmMenuRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setMenuRoleCode(menuRoleCode);
		List<WfmMenuRole> list = wf.searchWfmMenuRole(in).getMenuRoles();
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * 更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Na0002Response update(Na0002Request req) {
		final Na0002Response res = createResponse(Na0002Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		String error = validate(req);
		if (isEmpty(error)) {
			req.accessibleScreens.forEach(e -> {
				final MwmAccessibleScreen entity = repository.getAccessibleScreen(e.getAccessibleScreenId());
				if (entity == null) {
					throw new NotFoundException("アクセス可能画面情報が取得できませんでした -> accessibleScreenId=" + e.getAccessibleScreenId());
				}
				entity.setValidStartDate(e.getValidStartDate());
				entity.setValidEndDate(e.getValidEndDate());
				entity.setNewApplyDisplayFlag(e.getNewApplyDisplayFlag());
				repository.update(entity);
			});

			res.accessibleScreens = repository.getAccessibleScreen(req.corporationCode, req.menuRoleCode, localeCode);
			res.treeItems = repository.getScreenProcessLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
					.stream()
					.filter(s -> !res.accessibleScreens.stream().anyMatch(c -> Type.FILE.equals(s.type) && c.screenProcessId.equals(s.id)))
					.map(s -> new Na0002TreeItem(s))
					.collect(Collectors.toList());

			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.accessibleManagement));
			res.success = true;
		} else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/**
	 * 削除処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Na0002Response remove(Na0002Request req) {
		final Na0002Response res = createResponse(Na0002Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		req.accessibleScreens.forEach(e -> {
			final MwmAccessibleScreen entity = repository.getAccessibleScreen(e.getAccessibleScreenId());
			if (entity == null) {
				throw new NotFoundException("アクセス可能画面情報が取得できませんでした -> accessibleScreenId=" + e.getAccessibleScreenId());
			}
			repository.delete(entity);
		});

		res.accessibleScreens = repository.getAccessibleScreen(req.corporationCode, req.menuRoleCode, localeCode);
		res.treeItems = repository.getScreenProcessLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
				.stream()
				.filter(s -> !res.accessibleScreens.stream().anyMatch(c -> Type.FILE.equals(s.type) && c.screenProcessId.equals(s.id)))
				.map(s -> new Na0002TreeItem(s))
				.collect(Collectors.toList());

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.accessibleManagement));
		res.success = true;
		return res;
	}
}
