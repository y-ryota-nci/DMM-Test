package jp.co.nci.iwf.endpoint.mm.mm0060;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmDefaultBlockDisplay;

/**
 * ブロック表示順設定サービス
 */
@BizLogic
public class Mm0060Service extends BaseService implements CodeBook {
	@Inject
	private Mm0060Repository repository;
	@Inject
	private MultilingalService multi;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0060InitResponse init(Mm0060InitRequest req) {
		final Mm0060InitResponse res = createResponse(Mm0060InitResponse.class, req);

		// 画面プロセス定義一覧
		LoginInfo login = sessionHolder.getLoginInfo();
		res.screenProcesses = repository.getScreenProcesses(login.getCorporationCode(), login.getLocaleCode())
				.stream()
				.map(spd -> new OptionItem(
						spd.getScreenProcessId(),
						spd.getScreenProcessCode() + " " + spd.getScreenProcessName()))
				.collect(Collectors.toList());

		// 空行を追加
		res.screenProcesses.add(0, OptionItem.EMPTY);
		// 「デフォルトのブロック設定」行を追加
		res.screenProcesses.add(1, new OptionItem(
				DEFAULT_SCREEN_PROCESS_ID,
				"(" + i18n.getText(MessageCd.defaultBlockDisplaySetting) + ")"));

		res.success = true;
		return res;
	}

	/**
	 * 画面プロセス定義IDに紐付くブロック表示条件を抽出
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0060SearchResponse search(Mm0060SearchRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		// 表示条件マスタ
		final List<Mm0060Dc> dcs = repository.getDcs(localeCode);
		// 文書管理用の表示条件定義マスタは除外（文書管理用の表示条件IDは101から始まるが100未満をWF用とする）
		dcs.stream().filter(e -> (e.getDcId() < 100L)).collect(Collectors.toList());
		// ブロック表示条件リスト
		final List<Mm0060BlockDisplay> blockDisplays = repository.getBlockDisplays(
				corporationCode, req.screenProcessId);
		// ブロックIDリスト
		final List<Mm0060Block> blocks = repository.getBlocks(corporationCode, localeCode);

		final Mm0060SearchResponse res = createResponse(Mm0060SearchResponse.class, req);
		res.cols = dcs;
		res.rows = merge(dcs, blocks, blockDisplays, req.screenProcessId);
		res.success = true;
		return res;
	}

	/**
	 * ブロック表示順の画面データを生成
	 * @param dcs 表示条件マスタ
	 * @param blocks ブロックリスト
	 * @param blockDisplays ブロック表示条件マスタ
	 * @return
	 */
	private List<Map<String, Object>> merge(
			List<Mm0060Dc> dcs,
			List<Mm0060Block> blocks,
			List<Mm0060BlockDisplay> blockDisplays,
			long screenProcessId) {

		// ブロック表示順マスタをブロックID/表示条件IDをキーとしたMapへ変換
		final Map<Integer, Map<Long, Mm0060BlockDisplay>> maps = blockDisplays.stream()
				.collect(Collectors.groupingBy(Mm0060BlockDisplay::getBlockId,
						Collectors.toMap(Mm0060BlockDisplay::getDcId, bd -> bd)));
		if (!eq(DEFAULT_SCREEN_PROCESS_ID, screenProcessId) && maps.isEmpty()) {
			return new ArrayList<>();
		}

		// 表示条件IDをキーとしたMap
		final Map<Long, Mm0060Dc> dcMap = dcs.stream()
				.collect(Collectors.toMap(Mm0060Dc::getDcId, dc -> dc));

		// 列に相当する表示条件IDは可変なので、特定の構造体にすることができない。
		// よって行に相当する部分を Mapで生成する。
		// これでクライアント側でJSONとしてみたら最初から固定のフィールドと変わらないはず
		final List<Map<String, Object>> results = new ArrayList<>();
		for (Mm0060Block block : blocks) {
			final Integer blockId = block.getBlockId();
			final Map<String, Object> row = new HashMap<>();
			row.put("blockId", blockId);
			row.put("blockName", block.getBlockName());
			row.put("sortOrder", block.getSortOrder());

			// 高速化のため、レコードがあるものだけを dcTypeXX としてフィールド化
			final Map<Long, Mm0060BlockDisplay> bd = maps.get(blockId);
			if (bd != null) {
				for (Mm0060BlockDisplay v : bd.values()) {
					row.put("sortOrder", v.getSortOrder());
					row.put("displayFlag" + v.getDcId(), v.getDisplayFlag());
					row.put("expansionFlag" + v.getDcId(), v.getExpansionFlag());
					dcMap.get(v.getDcId()).setSelected(true);
				}
			}
			results.add(row);
		}

		// 表示条件が一つも選択されていなければ初回表示だと思われるので、全表示条件にチェックをつけておく
		if (!dcs.isEmpty() && dcs.stream().filter(dc -> dc.isSelected()).count() == 0) {
			dcs.forEach(dc -> dc.setSelected(true));
		}
		results.sort((m1, m2) -> ((Integer)m1.get("sortOrder")).compareTo((Integer)m2.get("sortOrder")));
		return results;
	}

	/**
	 * 登録処理
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0060SaveResponse save(Mm0060SaveRequest req) {
		// 表示条件マスタの差分更新
		saveDc(req);

		// ブロック表示条件の差分更新
		if (DEFAULT_SCREEN_PROCESS_ID == req.screenProcessId)
			saveDefaultBlockDisplay(req);
		else
			saveBlockDisplay(req);

		final Mm0060SaveResponse res = createResponse(Mm0060SaveResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.blockDisplaySettting));
		res.success = true;
		return res;
	}

	/** デフォルトブロック表示条件を差分更新  */
	private void saveDefaultBlockDisplay(Mm0060SaveRequest req) {
		// DBの既存データを表示条件ID/ブロックIDをキーにMap化
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Map<Long, Map<Integer, MwmDefaultBlockDisplay>> dcIds = repository
				.getDefaultBlockDisplay(corporationCode)
				.stream()
				.collect(Collectors.groupingBy(MwmDefaultBlockDisplay::getDcId,
						Collectors.toMap(MwmDefaultBlockDisplay::getBlockId, pdc -> pdc)));

		// 入力された内容を既存レコードから消し込んでいき、消し込めるかどうかで挿入／更新を切り替える
		for (Mm0060BlockDisplay input : req.blockDisplays) {
			Map<Integer, MwmDefaultBlockDisplay> blockIds = dcIds.get(input.getDcId());
			if (blockIds == null)
				repository.insertDefault(input, corporationCode);
			else {
				MwmDefaultBlockDisplay current = blockIds.remove(input.getBlockId());
				if (current == null)
					repository.insertDefault(input, corporationCode);
				else
					repository.updateDefault(current, input);
			}
		}
		// 残余は選択されていなかった表示条件なので、これを削除
		for (Map<Integer, MwmDefaultBlockDisplay> blockIds : dcIds.values()) {
			for (MwmDefaultBlockDisplay old : blockIds.values()) {
				repository.delete(old);
			}
		}
	}

	/** ブロック表示条件の差分更新 */
	private void saveBlockDisplay(Mm0060SaveRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		// DBの既存データを表示条件ID/ブロックIDをキーにMap化
		final Map<Long, Map<Integer, MwmBlockDisplay>> dcIds = repository
				.getMwmBlockDisplay(corporationCode, req.screenProcessId, localeCode)
				.stream()
				.collect(Collectors.groupingBy(MwmBlockDisplay::getDcId,
						Collectors.toMap(MwmBlockDisplay::getBlockId, pdc -> pdc)));

		// 入力内容を表示条件ID/ブロックIDをキーにMap化
		final Map<Long, Map<Integer, Mm0060BlockDisplay>> inputedByDcId = req.blockDisplays
				.stream()
				.collect(Collectors.groupingBy(Mm0060BlockDisplay::getDcId,
						Collectors.toMap(Mm0060BlockDisplay::getBlockId, pdc -> pdc)));

		// 削除対象リスト
		final List<MwmBlockDisplay> removes = new ArrayList<>();

		// 入力された内容を既存レコードから消し込んでいき、消し込めるかどうかで挿入／更新を切り替える
		for (Long dcId : inputedByDcId.keySet()) {
			final Map<Integer, Mm0060BlockDisplay> inputsByBlockId = inputedByDcId.get(dcId);
			final Map<Integer, MwmBlockDisplay> blockIds = dcIds.remove(dcId);
			for (Integer blockId : inputsByBlockId.keySet()) {
				Mm0060BlockDisplay input = inputsByBlockId.get(blockId);
				MwmBlockDisplay pdc = (blockIds == null ? null : blockIds.remove(blockId));

				if (pdc == null) {
					repository.insert(req.screenProcessId, input);
				} else {
					repository.update(input, pdc);
				}
			}
			// この表示条件IDで使わなくなったブロック表示条件を削除対象に。
			// （たぶん、かつて存在し、今は削除されたブロックのものと思われる）
			if (blockIds != null && !blockIds.isEmpty())
				removes.addAll(blockIds.values());
		}

		// 不要になった表示条件IDに紐付くブロック表示条件を削除対象に。
		for (Map<Integer, MwmBlockDisplay> map : dcIds.values()) {
			removes.addAll(map.values());
		}

		// 残余は不要なので物理削除
		repository.delete(removes);

	}

	/** 表示条件マスタの差分更新 */
	private void saveDc(Mm0060SaveRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Map<Long, MwmDc> currents = repository.getMwmDc(localeCode).stream()
				.collect(Collectors.toMap(MwmDc::getDcId, old -> old));

		for (Mm0060Dc input : req.dcs) {
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

	/**
	 * 既存データを削除し、デフォルトのブロック表示設定をコピー
	 * @param req
	 */
	@Transactional
	public Mm0060SaveResponse copyDefault(Mm0060SaveRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		// 表示条件マスタの差分更新
		saveDc(req);

		// 既存のブロック表示条件を削除
		final List<MwmBlockDisplay> olds = repository.getMwmBlockDisplay(corporationCode, req.screenProcessId, localeCode);
		repository.delete(olds);

		// 標準のブロック表示順をもとに、ブロック表示条件を新規作成
		final List<MwmDefaultBlockDisplay> sources = repository.getDefaultBlockDisplay(corporationCode);
		for (MwmDefaultBlockDisplay src : sources) {
			final Mm0060BlockDisplay input = new Mm0060BlockDisplay();
			input.setBlockId(src.getBlockId());
			input.setDcId(src.getDcId());
			input.setDisplayFlag(src.getDisplayFlag());
			input.setExpansionFlag(src.getExpansionFlag());
			input.setSortOrder(src.getSortOrder());
			repository.insert(req.screenProcessId, input);
		}

		final Mm0060SaveResponse res = createResponse(Mm0060SaveResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.blockDisplaySettting));
		res.success = true;
		return res;
	}

	/**
	 * ブロック表示順を削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0060SaveResponse delete(Mm0060SaveRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();

		// 表示条件マスタの差分更新
		saveDc(req);

		// 既存のブロック表示条件を削除
		final List<MwmBlockDisplay> olds = repository.getMwmBlockDisplay(corporationCode, req.screenProcessId, localeCode);
		repository.delete(olds);

		final Mm0060SaveResponse res = createResponse(Mm0060SaveResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.blockDisplaySettting));
		res.success = true;
		return res;
	}
}
