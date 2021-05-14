package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import jp.co.dmm.customize.component.catalog.CatalogService;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalog;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogUsedDepartment;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * カタログ登録画面サービス
 */
@BizLogic
public class Ct0011Service extends BaseService {
	@Inject private CatalogService catalogService;
	@Inject private Ct0011Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ct0011InitResponse init(Ct0011InitRequest req) {
		Ct0011InitResponse res = createResponse(Ct0011InitResponse.class, req);
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.editable = loginInfo.isAspAdmin() || loginInfo.isGroupAdmin() || loginInfo.isCorpAdmin() || loginInfo.isUserAdmin();

		if (isNotEmpty(req.catalogId)) {
			res.catalog = repository.getCatalog(req.catalogId);
			MwmCatalogImage catalogImage = catalogService.getCatalogImageByCatalogId(req.catalogId);
			if (isNotEmpty(catalogImage)) {
				res.catalogImage = new CatalogImage(catalogImage);
			}
			res.catalogUsedDepartments = repository.getCatalogUsedDepartments(req.catalogId, loginInfo.getLocaleCode());
		} else {
			res.catalog = new MwmCatalog();
			res.catalog.setCorporationCode(loginInfo.getCorporationCode());
			res.catalog.setDeleteFlag(DeleteFlag.OFF);
			res.catalogUsedDepartments = new ArrayList<>();
		}
		res.editable = res.editable || res.catalogUsedDepartments.stream().anyMatch(ud -> {
			return CommonFlag.ON.equals(ud.inChargeFlag)
					&& loginInfo.getCorporationCode().equals(ud.corporationCode)
					&& loginInfo.getOrganizationCodes().contains(ud.organizationCode);
		});
		res.catalogCategories = catalogService.getCatalogCategories(true);
		res.catalogUnits = catalogService.getCatalogUnits(true);
		res.salesTaxTypes = catalogService.getSalesTaxTypes(true);
		res.success = true;
		return res;
	}

	@Transactional
	public CatalogImage upload(List<FormDataBodyPart> bodyParts) {
		CatalogImage res = null;
		int count = 0;

		for (BodyPart bodyPart : bodyParts) {
			FormDataContentDisposition cd = (FormDataContentDisposition)bodyPart.getContentDisposition();
			if (eq("file", cd.getName())) {
				final UploadFile f = new UploadFile(bodyPart);
				final byte[] fileData = toBytes(f.stream);
				count++;

				// 最大ファイル数
				if (count > 1)
					throw new InvalidUserInputException(MessageCd.MSG0155, 1);

				final MwmCatalogImage file = new MwmCatalogImage();
				file.setDeleteFlag(DeleteFlag.ON);	// まだ仮登録なので論理削除として登録する
				file.setFileData(fileData);
				file.setFileName(f.fileName);
				res = new CatalogImage(repository.insert(file));
			}
		}
		return res;
	}

	@Transactional
	public Ct0011Response insert(Ct0011Request req) {
		Ct0011Response res = save(req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, "カタログ"));
		res.success = true;
		return res;
	}

	@Transactional
	public Ct0011Response update(Ct0011Request req) {
		Ct0011Response res = save(req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, "カタログ"));
		res.success = true;
		return res;
	}

	private Ct0011Response save(Ct0011Request req) {
		Ct0011Response res = createResponse(Ct0011Response.class, req);

		// カタログ差分更新
		{
			MwmCatalog input = req.catalog;
			MwmCatalog org = repository.getCatalog(input.getCatalogId());
			if (org == null) {
				res.catalog = repository.insert(input);
			} else {
				res.catalog = repository.update(input, org);
			}
		}

		// カタログ画像更新
		{
			MwmCatalogImage catalogImage = catalogService.getCatalogImageByCatalogId(res.catalog.getCatalogId());
			if (isNotEmpty(req.catalogImage) && req.catalogImage.getCatalogImageId() > 0) {
				if (isNotEmpty(catalogImage) && !eq(catalogImage.getCatalogImageId(), req.catalogImage.getCatalogImageId())) {
					repository.delete(catalogImage);
				}
				MwmCatalogImage image = catalogService.getCatalogImage(req.catalogImage.getCatalogImageId());
				image.setCatalogId(res.catalog.getCatalogId());
				image.setDeleteFlag(DeleteFlag.OFF);
				res.catalogImage = new CatalogImage(repository.update(image));
			}
		}

		// カタログ利用部門更新
		{
			// 削除されたレコードを物理削除
			repository.delete(req.removeCatalogUsedDepartments);
			// 登録 or 更新
			req.catalogUsedDepartments.forEach(input -> {
				MwmCatalogUsedDepartment org = repository.getCatalogUsedDepartment(input.getCatalogUsedDepartmentId());
				if (org == null) {
					input.setCatalogId(res.catalog.getCatalogId());
					input.setDeleteFlag(DeleteFlag.OFF);
					repository.insert(input);
				} else {
					repository.update(input, org);
				}
			});

			final LoginInfo loginInfo = sessionHolder.getLoginInfo();
			res.catalogUsedDepartments = repository.getCatalogUsedDepartments(res.catalog.getCatalogId(), loginInfo.getLocaleCode());

			// 権限を再取得
			res.editable = loginInfo.isAspAdmin() || loginInfo.isGroupAdmin() || loginInfo.isCorpAdmin() || loginInfo.isUserAdmin();
			res.editable = res.editable || res.catalogUsedDepartments.stream().anyMatch(ud -> {
				return CommonFlag.ON.equals(ud.inChargeFlag)
						&& loginInfo.getCorporationCode().equals(ud.corporationCode)
						&& loginInfo.getOrganizationCodes().contains(ud.organizationCode);
			});
		}

		return res;
	}

	@Transactional
	public Ct0011Response delete(Ct0011Request req) {
		Ct0011Response res = createResponse(Ct0011Response.class, req);
		MwmCatalog input = req.catalog;
		MwmCatalog org = repository.getCatalog(input.getCatalogId());
		repository.delete(input, org);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, "カタログ"));
		res.success = true;
		return res;
	}
}
