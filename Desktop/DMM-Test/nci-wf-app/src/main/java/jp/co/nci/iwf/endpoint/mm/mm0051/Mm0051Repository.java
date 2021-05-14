package jp.co.nci.iwf.endpoint.mm.mm0051;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;

/**
 * 採番形式登録リポジトリ
 */
@ApplicationScoped
public class Mm0051Repository extends BaseRepository implements CodeBook {
	@Inject
	private NumberingService numbering;
	@Inject
	private MultilingalService multi;

	public long insert(final Mm0051Entity input) {
		// 採番形式マスタ
		final long partsNumberingFormatId = numbering.newPK(MwmPartsNumberingFormat.class);
		final MwmPartsNumberingFormat current = new MwmPartsNumberingFormat();
		current.setCorporationCode(input.corporationCode);
		current.setDeleteFlag(input.deleteFlag);
		current.setNumberingFormat(input.numberingFormat);
		current.setPartsNumberingFormatCode(input.partsNumberingFormatCode);
		current.setPartsNumberingFormatId(partsNumberingFormatId);
		current.setPartsNumberingFormatName(input.partsNumberingFormatName);
		current.setFormatType1(input.formatType1);
		current.setFormatType2(input.formatType2);
		current.setFormatType3(input.formatType3);
		current.setFormatType4(input.formatType4);
		current.setFormatType5(input.formatType5);
		current.setFormatType6(input.formatType6);
		current.setFormatType7(input.formatType7);
		current.setFormatType8(input.formatType8);
		current.setFormatType9(input.formatType9);
		current.setFormatValue1(input.formatValue1);
		current.setFormatValue2(input.formatValue2);
		current.setFormatValue3(input.formatValue3);
		current.setFormatValue4(input.formatValue4);
		current.setFormatValue5(input.formatValue5);
		current.setFormatValue6(input.formatValue6);
		current.setFormatValue7(input.formatValue7);
		current.setFormatValue8(input.formatValue8);
		current.setFormatValue9(input.formatValue9);
		current.setGroupingFlag1(input.groupingFlag1);
		current.setGroupingFlag2(input.groupingFlag2);
		current.setGroupingFlag3(input.groupingFlag3);
		current.setGroupingFlag4(input.groupingFlag4);
		current.setGroupingFlag5(input.groupingFlag5);
		current.setGroupingFlag6(input.groupingFlag6);
		current.setGroupingFlag7(input.groupingFlag7);
		current.setGroupingFlag8(input.groupingFlag8);
		current.setGroupingFlag9(input.groupingFlag9);
		em.persist(current);

		multi.save("MWM_PARTS_NUMBERING_FORMAT", input.partsNumberingFormatId, "PARTS_NUMBERING_FORMAT_NAME", input.partsNumberingFormatName);
		em.flush();

		return partsNumberingFormatId;
	}


	public void update(Mm0051Entity input) {
		// 採番形式マスタ
		{
			final MwmPartsNumberingFormat current = getMwmPartsNumberingFormat(input.partsNumberingFormatId);
			current.setNumberingFormat(input.numberingFormat);
			current.setPartsNumberingFormatName(input.partsNumberingFormatName);
			current.setFormatType1(input.formatType1);
			current.setFormatType2(input.formatType2);
			current.setFormatType3(input.formatType3);
			current.setFormatType4(input.formatType4);
			current.setFormatType5(input.formatType5);
			current.setFormatType6(input.formatType6);
			current.setFormatType7(input.formatType7);
			current.setFormatType8(input.formatType8);
			current.setFormatType9(input.formatType9);
			current.setFormatValue1(input.formatValue1);
			current.setFormatValue2(input.formatValue2);
			current.setFormatValue3(input.formatValue3);
			current.setFormatValue4(input.formatValue4);
			current.setFormatValue5(input.formatValue5);
			current.setFormatValue6(input.formatValue6);
			current.setFormatValue7(input.formatValue7);
			current.setFormatValue8(input.formatValue8);
			current.setFormatValue9(input.formatValue9);
			current.setGroupingFlag1(input.groupingFlag1);
			current.setGroupingFlag2(input.groupingFlag2);
			current.setGroupingFlag3(input.groupingFlag3);
			current.setGroupingFlag4(input.groupingFlag4);
			current.setGroupingFlag5(input.groupingFlag5);
			current.setGroupingFlag6(input.groupingFlag6);
			current.setGroupingFlag7(input.groupingFlag7);
			current.setGroupingFlag8(input.groupingFlag8);
			current.setGroupingFlag9(input.groupingFlag9);
			current.setDeleteFlag(input.deleteFlag);
			em.merge(current);

			multi.save("MWM_PARTS_NUMBERING_FORMAT", input.partsNumberingFormatId, "PARTS_NUMBERING_FORMAT_NAME", input.partsNumberingFormatName);
		}
	}

	public Mm0051Entity get(String corporationCode, String partsNumberingFormatCode, String localeCode) {
		Object[] params = { localeCode, corporationCode, partsNumberingFormatCode };
		return selectOne(Mm0051Entity.class, getSql("MM0051_01"), params);
	}

	public List<MwmPartsSequenceSpec> getSequenceList(String corporationCode) {
		List<String> params = new ArrayList<>();
		params.add(corporationCode);
		return select(MwmPartsSequenceSpec.class, getSql("MM0051_03"), params.toArray());
	}

	public MwmPartsNumberingFormat getMwmPartsNumberingFormat(long partsNumberingFormatId) {
		return em.find(MwmPartsNumberingFormat.class, partsNumberingFormatId);
	}
}
