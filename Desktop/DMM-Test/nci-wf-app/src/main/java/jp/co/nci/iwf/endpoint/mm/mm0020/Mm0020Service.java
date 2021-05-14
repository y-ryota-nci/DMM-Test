package jp.co.nci.iwf.endpoint.mm.mm0020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsDc;

/**
 * 表示条件設定サービス
 */
@BizLogic
public class Mm0020Service extends BaseService {
	@Inject
	private Mm0020Repository repository;
	@Inject
	private MwmLookupService lookup;
	@Inject
	private MultilingalService multi;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0020InitResponse init(BaseRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Mm0020InitResponse res = createResponse(Mm0020InitResponse.class, req);

		// コンテナ一覧
		res.containers = repository.getContainers(corporationCode, localeCode)
				.stream()
				.map(c -> new OptionItem(c.getContainerId(), c.getContainerCode() + " " + c.getContainerName()))
				.collect(Collectors.toList());
		res.containers.add(0, OptionItem.EMPTY);

		// 表示条件区分の選択肢
		res.dcTypes = lookup.getOptionItems(LookupGroupId.DC_TYPE, true);

		res.success = true;
		return res;
	}

	/**
	 * コンテナIDに紐付くパーツ表示条件を抽出
	 * @param req
	 * @return
	 */
	public Mm0020Response search(Mm0020Request req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 表示条件マスタ
		final List<Mm0020Dc> dcList = repository.getDcList(localeCode);
		// パーツ表示条件リスト
		final List<Mm0020PartsDc> partsDcList = repository.getPartsDcList(req.containerId, localeCode);
		// パーツリスト
		final List<Mm0020Parts> partsList = repository.getPartsList(req.containerId, localeCode);

		final Mm0020Response res = createResponse(Mm0020Response.class, req);
		res.cols = dcList;
		res.rows = merge(dcList, partsList, partsDcList);
		res.success = true;
		return res;
	}

	/**
	 * パーツ表示条件の画面データを生成
	 * @param dcList 表示条件マスタ
	 * @param partsList パーツリスト
	 * @param partsDcList パーツ表示条件リスト
	 * @return
	 */
	private List<Map<String, Object>> merge(
			List<Mm0020Dc> dcList,
			List<Mm0020Parts> partsList,
			List<Mm0020PartsDc> partsDcList) {

		// パーツID/表示条件IDをキーとしたMapへ変換
		final Map<Long, Map<Long, Mm0020PartsDc>> maps = partsDcList.stream()
				.collect(Collectors.groupingBy(Mm0020PartsDc::getPartsId,
						Collectors.toMap(Mm0020PartsDc::getDcId, pdc -> pdc)));

		// 表示条件IDをキーとしたMap
		final Map<Long, Mm0020Dc> dcMap = dcList.stream()
				.collect(Collectors.toMap(Mm0020Dc::getDcId, dc -> dc));

		// 列に相当する表示条件IDは可変なので、特定の構造体にすることができない。
		// よって行に相当する部分を Mapで生成する。
		// これでクライアント側でJSONとしてみたら最初から固定のフィールドと変わらないはず
		final List<Map<String, Object>> results = new ArrayList<>();
		for (Mm0020Parts parts : partsList) {
			final long partsId = parts.partsId;
			final Map<String, Object> row = new HashMap<>();
			row.put("partsId", partsId);
			row.put("partsCode", parts.partsCode);
			row.put("labelText", parts.labelText);

			// 高速化のため、レコードがあるものだけを dcTypeXX としてフィールド化
			final Map<Long, Mm0020PartsDc> pdcs = maps.get(partsId);
			if (pdcs != null) {
				for (Mm0020PartsDc pdc : pdcs.values()) {
					row.put("dcType" + pdc.getDcId(), pdc.getDcType());
					dcMap.get(pdc.getDcId()).setSelected(true);
				}
			}
			results.add(row);
		}

		// 表示条件が何も選択されていなければ、先頭レコードにチェックをつけておく
		if (!dcList.isEmpty() && dcList.stream().filter(dc -> dc.isSelected()).count() == 0) {
			dcList.get(0).setSelected(true);
		}

		return results;
	}

	/**
	 * 更新処理
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Mm0020SaveRequest req) {
		// 表示条件マスタの差分更新
		saveDc(req);

		// パーツ表示条件の差分更新
		savePartsDc(req);

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.displayCondition));
		res.success = true;
		return res;
	}

	/** パーツ表示条件の差分更新 */
	private void savePartsDc(Mm0020SaveRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// DBの既存データを表示条件ID/パーツIDをキーにMap化
		final Map<Long, Map<Long, MwmPartsDc>> dcIds = repository
				.getMwmPartsDc(req.containerId, localeCode).stream()
				.collect(Collectors.groupingBy(MwmPartsDc::getDcId,
						Collectors.toMap(MwmPartsDc::getPartsId, pdc -> pdc)));

		// 入力内容を表示条件ID/パーツIDをキーにMap化
		final Map<Long, Map<Long, Mm0020PartsDc>> inputedByDcId = req.partsDcList.stream()
				.collect(Collectors.groupingBy(Mm0020PartsDc::getDcId,
						Collectors.toMap(Mm0020PartsDc::getPartsId, pdc -> pdc)));

		// 削除対象リスト
		final List<MwmPartsDc> removes = new ArrayList<>();

		for (Long dcId : inputedByDcId.keySet()) {
			final Map<Long, Mm0020PartsDc> inputsByPartsId = inputedByDcId.get(dcId);
			final Map<Long, MwmPartsDc> partsIds = dcIds.remove(dcId);
			for (Long partsId : inputsByPartsId.keySet()) {
				Mm0020PartsDc input = inputsByPartsId.get(partsId);
				MwmPartsDc pdc = (partsIds == null ? null : partsIds.remove(partsId));

				if (pdc == null)
					repository.insert(input);
				else
					repository.update(input, pdc);
			}
			// この表示条件IDで使わなくなったパーツ表示条件を削除対象に。
			// （たぶん、かつて存在し、今は削除されたパーツのものと思われる）
			if (partsIds != null && !partsIds.isEmpty())
				removes.addAll(partsIds.values());
		}

		// 不要になった表示条件IDに紐付くパーツ表示条件を削除対象に。
		for (Map<Long, MwmPartsDc> map : dcIds.values())
			removes.addAll(map.values());

		// 残余は不要なので物理削除
		repository.delete(removes);
	}

	/** 表示条件マスタの差分更新 */
	private void saveDc(Mm0020SaveRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Map<Long, MwmDc> currents = repository.getMwmDc(localeCode).stream()
				.collect(Collectors.toMap(MwmDc::getDcId, old -> old));

		for (Mm0020Dc input : req.dcList) {
			MwmDc current = currents.remove(input.getDcId());
			if (current == null)
				;	// 新規はありえない
			else {
				repository.update(input, current);

				// 多言語対応
				multi.save("MWM_DC", current.getDcId(), "DC_NAME", input.getDcName());
			}
		}
		// 残余もあり得ない
	}

}
