package jp.co.nci.iwf.endpoint.vd.vd0030;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.designer.service.download.ScreenDownloader;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.endpoint.vd.vd0030.excel.Vd0030OutputStreamExcel;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面一覧サービス
 */
@BizLogic
public class Vd0030Service extends BasePagingService {
	/** ルックアップサービス */
	@Inject private MwmLookupService lookup;
	/** 画面一覧のリポジトリ */
	@Inject private Vd0030Repository repository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;
	/** 画面Javascriptサービス */
	@Inject private JavascriptService jsService;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** 画面定義のダウンロードサービス */
	@Inject private ScreenDownloader downloader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0030InitResponse init(BaseRequest req) {
		final Vd0030InitResponse res = createResponse(Vd0030InitResponse.class, req);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		// スクラッチ区分の選択肢
		res.scratchFlags = lookup.getOptionItems(LookupGroupId.SCRATCH_FLAG, true);

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Vd0030SearchResponse search(Vd0030SearchRequest req) {
		final int allCount = repository.count(req);
		final Vd0030SearchResponse res = createResponse(Vd0030SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Vd0030Request req) {
		// 画面と画面IDで紐付くテーブルに対して、画面IDに該当する全レコードを削除
		// （コンテナに紐付くテーブルは削除対象外）
		@SuppressWarnings("unused")
		int count = 0;
		for (Long screenId : req.screenIds) {
			count += repository.delete(screenId);
			multi.physicalDelete("MWM_SCREEN", screenId);

			// 画面で参照しているJavascriptの参照をクリア
			jsService.clear();
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.screenInfo));
		res.success = true;
		return res;
	}

	/**
	 * 画面IDに紐付く画面定義を返す
	 * @param req
	 * @return
	 */
	public MwmScreen getMwmScreen(Long screenId) {
		if (screenId == null)
			throw new BadRequestException("画面IDが未指定です");

		final MwmScreen s = repository.get(screenId);
		if (s == null)
			throw new NotFoundException("画面ID=" + screenId + "に紐付く画面定義がありません");
		return s;
	}

	/**
	 * 画面定義のZIPダウンロード
	 * @param screenCode
	 * @return
	 */
	public StreamingOutput downloadZip(Vd0030Request req) {
		final List<MwmScreen> screens = repository.getScreens(req.screenIds);
		return downloader.setup(screens);
	}

	/**
	 * 画面定義をEXCEL形式でダウンロード
	 * @param screenId
	 * @return
	 */
	public StreamingOutput downloadExcel(Long screenId) {
		return new Vd0030OutputStreamExcel(screenId);
	}

	/**
	 * 画面IDをキーに画面マスタを抽出
	 * @param screenId
	 * @return
	 */
	public MwmScreen get(Long screenId) {
		return repository.get(screenId);
	}
}
