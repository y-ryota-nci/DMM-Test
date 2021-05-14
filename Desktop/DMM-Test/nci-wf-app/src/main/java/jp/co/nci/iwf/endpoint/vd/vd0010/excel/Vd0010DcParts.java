package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.util.HashMap;
import java.util.Map;

import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020Dc;
import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020Parts;
import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020PartsDc;

/**
 * コンテナ一覧のEXCELダウンロード用パーツ表示条件Bean
 */
public class Vd0010DcParts {
	/**
	 * コンストラクタ
	 * @param parts パーツ
	 * @param dcMap 表示条件マスタMap
	 * @param partsDcMap パーツ表示条件マスタMap
	 * @param dcTypes 表示区分Map
	 */
	public Vd0010DcParts(Mm0020Parts parts, Map<Long, Mm0020Dc> dcMap, Map<Long, Map<Long, Mm0020PartsDc>> partsDcMap,
			Map<String, String> dcTypes) {
		this.labelText = parts.labelText;
		this.partsCode = parts.partsCode;
		this.maps = new HashMap<>(dcMap.size());

		for (Long dcId : dcMap.keySet()) {
			final Mm0020Dc dc = dcMap.get(dcId);
			final String dcName = dc.getDcName();
			final Map<Long, Mm0020PartsDc> m = partsDcMap.get(parts.partsId);
				if (m != null) {
				final Mm0020PartsDc pdc = m.get(dcId);
				if (pdc != null) {
					final String dcType = String.valueOf(pdc.getDcType());
					final String dcTypeName = dcTypes.get(dcType);
					maps.put(dcName, dcTypeName);
				}
			}
		}
	}

	/** パーツコード */
	public String partsCode;

	/** 表示ラベル */
	public String labelText;

	/** 表示条件Map */
	public Map<String, String> maps;
}
