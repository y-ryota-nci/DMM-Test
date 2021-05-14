package jp.co.nci.iwf.endpoint.dc.dc0131;

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
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwvContainer;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleDoc;

/**
 * 業務文書公開設定画面サービス
 */
@BizLogic
public class Dc0131Service extends MmBaseService<WfmMenuRole> implements DcCodeBook {

	@Inject
	Dc0131Repository repository;
	@Inject
	private NumberingService numbering;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Dc0131Response init(Dc0131Request req) {
		final Dc0131Response res = this.createResponse(Dc0131Response.class, req);

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
		res.accessibleDocs = repository.getAccessibleDoc(req.corporationCode, req.menuRoleCode, localeCode);
		res.treeItems = repository.getScreenDocLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
				.stream()
				.filter(d -> !res.accessibleDocs.stream().anyMatch(c -> Type.FILE.equals(d.type) && c.screenDocId.equals(d.id)))
				.map(d -> new Dc0131TreeItem(d))
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
	public Dc0131Response create(Dc0131Request req) {
		final Dc0131Response res = createResponse(Dc0131Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		String error = validate(req);
		if (isEmpty(error)) {
			req.accessibleDocs.forEach(e -> {
				e.setAccessibleDocId(numbering.newPK(MwmAccessibleDoc.class));
				e.setCorporationCode(req.corporationCode);
				e.setMenuRoleCode(req.menuRoleCode);;
				e.setDeleteFlag(DeleteFlag.OFF);
				repository.insert(e);
			});

			res.accessibleDocs = repository.getAccessibleDoc(req.corporationCode, req.menuRoleCode, localeCode);
			res.treeItems = repository.getScreenDocLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
					.stream()
					.filter(d -> !res.accessibleDocs.stream().anyMatch(c -> Type.FILE.equals(d.type) && c.screenDocId.equals(d.id)))
					.map(d -> new Dc0131TreeItem(d))
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
	private String validate(Dc0131Request req) {
		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		for (MwmAccessibleDoc ad : req.accessibleDocs) {
			if (isEmpty(ad.getCorporationCode()) || isEmpty(ad.getMenuRoleCode())
					|| isEmpty(ad.getScreenDocId())
			) {
					throw new BadRequestException("更新キーが足りません");
			}
			final MwvScreenDocDef sdd = repository.getMwvScreenDoc(
					ad.getScreenDocId(), localeCode);

			// 自分自身を除く同一キーのレコードがすでに存在するか
			if (repository.isExists(ad)) {
				WfmMenuRole mr = getWfmMenuRole(ad.getCorporationCode(), ad.getMenuRoleCode());
				String key = String.format("%s=%s, %s=%s",
						i18n.getText(MessageCd.screenProcessName), sdd.screenDocName,
						i18n.getText(MessageCd.menuRoleName), mr.getMenuRoleName());
				return i18n.getText(MessageCd.MSG0192, key);
			}

			// 画面に含まれているコンテナが、すべてテーブル同期済みあること
			List<MwvContainer> containers = repository.getMwvContainers(sdd.screenDocId, localeCode);
			for (MwvContainer c : containers) {
				if (c.tableModifiedTimestamp == null
						|| compareTo(c.tableSyncTimestamp, c.tableModifiedTimestamp) < 0) {
					return i18n.getText(MessageCd.MSG0193, sdd.screenDocName, c.containerName);
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
	public Dc0131Response update(Dc0131Request req) {
		final Dc0131Response res = createResponse(Dc0131Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		String error = validate(req);
		if (isEmpty(error)) {
			req.accessibleDocs.forEach(e -> {
				final MwmAccessibleDoc entity = repository.getAccessibleDoc(e.getAccessibleDocId());
				if (entity == null) {
					throw new NotFoundException("アクセス可能画面情報が取得できませんでした -> accessibleDocId=" + e.getAccessibleDocId());
				}
				entity.setValidStartDate(e.getValidStartDate());
				entity.setValidEndDate(e.getValidEndDate());
				repository.update(entity);
			});

			res.accessibleDocs = repository.getAccessibleDoc(req.corporationCode, req.menuRoleCode, localeCode);
			res.treeItems = repository.getScreenDocLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
					.stream()
					.filter(d -> !res.accessibleDocs.stream().anyMatch(c -> Type.FILE.equals(d.type) && c.screenDocId.equals(d.id)))
					.map(d -> new Dc0131TreeItem(d))
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
	public Dc0131Response remove(Dc0131Request req) {
		final Dc0131Response res = createResponse(Dc0131Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		req.accessibleDocs.forEach(e -> {
			final MwmAccessibleDoc entity = repository.getAccessibleDoc(e.getAccessibleDocId());
			if (entity == null) {
				throw new NotFoundException("アクセス可能画面情報が取得できませんでした -> accessibleDocId=" + e.getAccessibleDocId());
			}
			repository.delete(entity);
		});

		res.accessibleDocs = repository.getAccessibleDoc(req.corporationCode, req.menuRoleCode, localeCode);
		res.treeItems = repository.getScreenDocLevelDef(req.corporationCode, req.menuRoleCode, localeCode)
				.stream()
				.filter(d -> !res.accessibleDocs.stream().anyMatch(c -> Type.FILE.equals(d.type) && c.screenDocId.equals(d.id)))
				.map(d -> new Dc0131TreeItem(d))
				.collect(Collectors.toList());

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.accessibleManagement));
		res.success = true;
		return res;
	}
}
