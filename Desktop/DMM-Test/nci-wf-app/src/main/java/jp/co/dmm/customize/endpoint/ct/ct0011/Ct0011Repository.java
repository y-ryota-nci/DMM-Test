package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.jpa.entity.mw.MwmCatalog;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogImage;
import jp.co.dmm.customize.jpa.entity.mw.MwmCatalogUsedDepartment;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;

@ApplicationScoped
public class Ct0011Repository extends BaseRepository {

	/** 採番サービス */
	@Inject private NumberingService number;

	public MwmCatalog getCatalog(long catalogId) {
		return em.find(MwmCatalog.class, catalogId);
	}

	public List<CatalogUsedDepartment> getCatalogUsedDepartments(long catalogId, String localeCode) {
		final Object[] params = { localeCode, catalogId };
		return select(CatalogUsedDepartment.class, getSql("CT0011_01"), params);
	}

	public MwmCatalogImage insert(MwmCatalogImage input) {
		long catalogImageId = number.newPK(MwmCatalogImage.class);
		input.setCatalogImageId(catalogImageId);
		input.setCatalogId(catalogImageId * -1);
		em.persist(input);
		em.flush();
		return em.find(MwmCatalogImage.class, catalogImageId);
	}

	public MwmCatalog insert(MwmCatalog input) {
		long catalogId = number.newPK(MwmCatalog.class);
		input.setCatalogId(catalogId);
		em.persist(input);
		em.flush();
		return getCatalog(catalogId);
	}

	public MwmCatalogImage update(MwmCatalogImage input) {
		em.merge(input);
		em.flush();
		return em.find(MwmCatalogImage.class, input.getCatalogImageId());
	}

	public void delete(MwmCatalogImage input) {
		em.remove(input);
		em.flush();
	}

	public MwmCatalog update(MwmCatalog input, MwmCatalog org) {
		org.setCatalogCode(input.getCatalogCode());
		org.setCatalogName(input.getCatalogName());
		org.setCatalogCategoryId(input.getCatalogCategoryId());
		org.setStockType(input.getStockType());
		org.setVendorName(input.getVendorName());
		org.setCatalogUnitId(input.getCatalogUnitId());
		org.setUnitPrice(input.getUnitPrice());
		org.setSalesTaxType(input.getSalesTaxType());
		org.setMakerName(input.getMakerName());
		org.setMakerModelNumber(input.getMakerModelNumber());
		org.setRemarks(input.getRemarks());
		org.setVersion(input.getVersion());
		em.merge(org);
		em.flush();
		return getCatalog(org.getCatalogId());
	}

	public void delete(MwmCatalog input, MwmCatalog org) {
		org.setDeleteFlag(DeleteFlag.ON);
		org.setVersion(input.getVersion());
		em.merge(org);
		em.flush();
	}

	public void delete(List<MwmCatalogUsedDepartment> inputs) {
		inputs.forEach(input -> {
			MwmCatalogUsedDepartment org = getCatalogUsedDepartment(input.getCatalogUsedDepartmentId());
			if (org != null) {
				org.setVersion(input.getVersion());
				em.remove(org);
			}
		});
		em.flush();
	}

	public MwmCatalogUsedDepartment getCatalogUsedDepartment(long catalogUsedDepartmentId) {
		return em.find(MwmCatalogUsedDepartment.class, catalogUsedDepartmentId);
	}

	public void insert(MwmCatalogUsedDepartment input) {
		long catalogUsedDepartmentId = number.newPK(MwmCatalogUsedDepartment.class);
		input.setCatalogUsedDepartmentId(catalogUsedDepartmentId);
		em.persist(input);
		em.flush();
	}

	public void update(MwmCatalogUsedDepartment input, MwmCatalogUsedDepartment org) {
		org.setCorporationCode(input.getCorporationCode());
		org.setOrganizationCode(input.getOrganizationCode());
		org.setInChargeFlag(input.getInChargeFlag());
		org.setVersion(input.getVersion());
		em.merge(org);
		em.flush();
	}
}
