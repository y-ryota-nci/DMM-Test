package jp.co.nci.iwf.endpoint.vd.vd0091;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.vd.vd0090.Vd0091SaveRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;

/**
 * 新規申請メニュー割当設定サービス
 */
@BizLogic
public class Vd0091Service extends BaseService {
	@Inject private Vd0091Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0091InitResponse init(Vd0091InitRequest req) {
		if (req.menuId == null)
			throw new BadRequestException("メニューIDが未指定です");
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.screenProcessMenuId != null && req.version == null)
			throw new BadRequestException("VERSIOnが未指定です");

		String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		Vd0091InitResponse res = createResponse(Vd0091InitResponse.class, req);

		// 画面プロセス／メニュー連携マスタのPKは画面プロセス／メニュー連携IDだが、メニューID＋企業コードでもユニークである
		if (req.screenProcessMenuId == null)
			res.entity = repository.get(req.menuId, req.corporationCode, localeCode);
		else
			res.entity = repository.get(req.screenProcessMenuId, localeCode);

		// 排他チェック
		if (req.version != null && res.entity != null && !eq(res.entity.version, req.version))
			throw new AlreadyUpdatedException();

		// 画面プロセス定義の選択肢
		res.screenProcessDefs = getScreenProcessDefs(req.corporationCode, localeCode);
		res.success = true;
		return res;
	}

	/** 画面プロセス定義の選択肢を生成 */
	private List<OptionItem> getScreenProcessDefs(String corporationCode, String localeCode) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll(repository.getScreenProcessDefs(corporationCode, localeCode)
				.stream()
				.map(s -> new OptionItem(s.screenProcessId, s.screenProcessName))
				.collect(Collectors.toList())
		);
		return items;
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Vd0091SaveRequest req) {
		if (req.entity == null)
			throw new BadRequestException("エンティティがありません");
		if (req.entity.menuId == 0L)
			throw new BadRequestException("メニューIDが未指定です");
		if (isEmpty(req.entity.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.entity.screenProcessMenuId != null && req.entity.version == null)
			throw new BadRequestException("VERSIONが未指定です");
		if (req.entity.screenProcessMenuId == null && req.entity.version != null)
			throw new BadRequestException("画面プロセス／メニュー連携IDが未指定です");

		Vd0091InitResponse res = createResponse(Vd0091InitResponse.class, req);
		if (req.entity.screenProcessId == null || req.entity.screenProcessId == 0L) {
			res.addAlerts(i18n.getText(MessageCd.MSG0001, MessageCd.screenProcessInfo));
			res.success = false;
		} else {
			// 差分更新
			MwmScreenProcessMenu current = repository.get(req.entity.screenProcessMenuId);
			Long screenProcessMenuId = null;
			if (current == null)
				screenProcessMenuId = repository.insert(req.entity);
			else
				screenProcessMenuId = repository.update(req.entity, current);

			// 読み直し
			String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
			res.entity = repository.get(screenProcessMenuId, localeCode);

			// 処理結果
			res.success = true;
			if (req.entity.screenProcessMenuId == null)
				res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.screenProcessMenuInfo));
			else
				res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.screenProcessMenuInfo));
		}
		return res;
	}
}
