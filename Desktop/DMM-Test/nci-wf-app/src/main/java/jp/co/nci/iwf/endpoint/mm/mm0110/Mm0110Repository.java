package jp.co.nci.iwf.endpoint.mm.mm0110;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmCorporationPropertyInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmCorporationPropertyInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmCorporationPropertyInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmCorporationPropertyOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmCorporationPropertyOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmCorporationPropertyOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.base.impl.WfmCorporationPropertyImpl;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationProperty;

/**
 * システムプロパティ編集画面のリポジトリ
 */
@ApplicationScoped
public class Mm0110Repository extends BaseRepository {
	@Inject private WfInstanceWrapper wf;
	@Inject private SessionHolder sessionHolder;

	/**
	 * システムプロパティ抽出
	 * @param corporationCode
	 * @return
	 */
	public List<Mm0110Entity> getProperties(String corporationCode, String localeCode) {
		final Object[] params = { corporationCode, localeCode };
		return select(Mm0110Entity.class, getSql("MM0110_01"), params);
	}

	/** WFM_CORP_PROP_MASTER抽出 */
	public List<WfmCorpPropMaster> getMwmCorpPropMaster() {
		return select(WfmCorpPropMaster.class, getSql("MM0110_02"));
	}

	/** WFM_CORP_PROP_MASTER更新 */
	public void update(WfmCorpPropMaster current, Mm0110Entity input) {
		if (!eq(current.getDefaultValue(), input.defaultValue)) {
			current.setDefaultValue(input.defaultValue);
			current.setTimestampUpdated(input.defaultTimestampUpdated);
		}
	}

	/** WFM_CORPORATION_PROPERTY抽出 */
	public List<WfmCorporationProperty> getWfmCorporationProperty(String corporationCode) {
		if (isEmpty(corporationCode))
			return null;

		final Object[] params = { corporationCode };
		return select(WfmCorporationProperty.class, getSql("MM0110_03"), params);
	}

	/** WFM_CORPORATION_PROPERTY更新 */
	public void update(WfmCorporationProperty current, Mm0110Entity input) {
		if (!eq(current.getPropertyValue(), input.propertyValue)) {
			final WfmCorporationPropertyImpl dest = new WfmCorporationPropertyImpl();
			copyFieldsAndProperties(current, dest);
			dest.setCorporationCode(current.getPk().getCorporationCode());
			dest.setPropertyCode(current.getPk().getPropertyCode());
			dest.setPropertyValue(input.propertyValue);
			dest.setTimestampUpdated(input.corporationTimestampUpdated);

			final UpdateWfmCorporationPropertyInParam in = new UpdateWfmCorporationPropertyInParam();
			in.setWfmCorporationProperty(dest);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final UpdateWfmCorporationPropertyOutParam out = wf.updateWfmCorporationProperty(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode())) {
				throw new WfException(out);
			}
		}
	}

	/** WFM_CORPORATION_PROPERTY挿入 */
	public void insert(Mm0110Entity input) {
		final WfmCorporationPropertyImpl dest = new WfmCorporationPropertyImpl();
		copyFieldsAndProperties(input, dest);

		final InsertWfmCorporationPropertyInParam in = new InsertWfmCorporationPropertyInParam();
		in.setWfmCorporationProperty(dest);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final InsertWfmCorporationPropertyOutParam out = wf.insertWfmCorporationProperty(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode())) {
			throw new WfException(out);
		}
	}

	/** WFM_CORPORATION_PROPERTY削除 */
	public void delete(WfmCorporationProperty current, Mm0110Entity input) {
		final WfmCorporationPropertyImpl dest = new WfmCorporationPropertyImpl();
		copyFieldsAndProperties(current, dest);
		dest.setCorporationCode(current.getPk().getCorporationCode());
		dest.setPropertyCode(current.getPk().getPropertyCode());
		dest.setTimestampUpdated(input.corporationTimestampUpdated);

		final DeleteWfmCorporationPropertyInParam in = new DeleteWfmCorporationPropertyInParam();
		in.setWfmCorporationProperty(dest);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final DeleteWfmCorporationPropertyOutParam out = wf.deleteWfmCorporationProperty(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode())) {
			throw new WfException(out);
		}
	}

}
