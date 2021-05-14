package jp.co.nci.iwf.endpoint.dc.dc0072;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDetail;

/**
 * テンプレート設定サービス.
 */
@BizLogic
public class Dc0072Service extends BasePagingService {

	/** ルックアップサービス */
	@Inject private WfmLookupService wfmLookup;
	/** テンプレート設定リポジトリ */
	@Inject private Dc0072Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0072Response init(Dc0072Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		final Dc0072Response res = createResponse(Dc0072Response.class, req);
		if (req.metaTemplateId != null) {
			final MwmMetaTemplateDef entity = repository.getByPk(req.metaTemplateId);
			if (entity == null) {
				throw new NotFoundException("該当するテンプレートはありません。テンプレートID＝" + req.metaTemplateId);
			} else if (req.version != null && !eq(entity.getVersion(), req.version)) {
				throw new AlreadyUpdatedException();
			} else {
				res.entity = entity;
			}
		} else {
			final MwmMetaTemplateDef entity = new MwmMetaTemplateDef();
			entity.setCorporationCode(login.getCorporationCode());
			entity.setDeleteFlag(DeleteFlag.OFF);
			res.entity = entity;
		}
		// 削除区分の選択肢
		res.deleteFlags = wfmLookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

	/**
	 * テンプレート明細検索.
	 * @param req
	 * @return
	 */
	public Dc0072Response search(Dc0072Request req) {
		if (req.metaTemplateId == null) {
			throw new BadRequestException("メタテンプレートIDが未指定です");
		}
		// 件数を抽出
		final int allCount = repository.count(req);
		final Dc0072Response res = createResponse(Dc0072Response.class, req, allCount);
		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;
		res.results = repository.select(req);
		res.success = true;
		return res;
	}

	/**
	 * 登録・更新処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0072Response save(Dc0072SaveRequest req) {
		final Dc0072Response res = createResponse(Dc0072Response.class, req);
		final MwmMetaTemplateDef org = repository.get(req.entity.getCorporationCode(), req.entity.getMetaTemplateCode());
		if (!validate(req, res, org)) {
			return res;
		}
		MwmMetaTemplateDef updated = null;
		if (org == null) {
			updated = repository.insert(req.entity);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.extendedItem));
		} else {
			updated = repository.update(org, req.entity);
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.extendedItem));
		}
		res.entity = updated;
		res.success = true;
		return res;
	}

	private boolean validate(Dc0072SaveRequest req, Dc0072Response res, MwmMetaTemplateDef entity) {
		if (entity != null) {
			if (!eq(entity.getMetaTemplateId(), req.entity.getMetaTemplateId())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0049));
			} else if (!eq(entity.getVersion(), req.entity.getVersion())) {
				res.addAlerts(i18n.getText(MessageCd.MSG0050));
			}
		}
		return res.alerts.isEmpty();
	}

	/**
	 * テンプレート明細差k所処理.
	 * @param req
	 * @return
	 */
	@Transactional
	public Dc0072Response save(Dc0072DeleteRequest req) {
		final Dc0072Response res = createResponse(Dc0072Response.class, req);
		// テンプレート明細一覧取得
		final Map<Long, MwmMetaTemplateDetail> map = repository.getMwmMetaTemplateDetailList(req.metaTemplateId);
		for (MwmMetaTemplateDetail target : req.deleteList) {
			MwmMetaTemplateDetail org = map.remove(target.getMetaTemplateDetailId());
			if (org != null) {
				if (!eq(org.getVersion(), target.getVersion())) {
					res.addAlerts(i18n.getText(MessageCd.MSG0050));
					break;
				}
			}
		}
		// 削除対象のデータを一括削除
		final List<Long> deleteIds = req.deleteList.stream().map(e -> e.getMetaTemplateDetailId()).collect(Collectors.toList());
		repository.deleteMwmMetaTemplateDetails(deleteIds);

		// 残ったものは並び順を再設定して更新
		int sortOrder = 1;
		for (MwmMetaTemplateDetail entity : map.values()) {
			entity.setSortOrder(sortOrder);
			repository.update(entity);
			sortOrder++;
		}
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, "#{テンプレート明細}"));
		res.success = true;
		return res;
	}
}
