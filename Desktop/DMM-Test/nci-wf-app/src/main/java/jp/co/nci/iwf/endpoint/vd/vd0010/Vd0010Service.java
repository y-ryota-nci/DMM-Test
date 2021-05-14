package jp.co.nci.iwf.endpoint.vd.vd0010;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.service.ContainerLoadRepository;
import jp.co.nci.iwf.designer.service.ContainerSaveRepository;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.designer.service.userData.UserTableSyncService;
import jp.co.nci.iwf.endpoint.vd.vd0010.excel.Vd0010OutputStreamExcel;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;

/**
 * コンテナ一覧サービス
 */
@BizLogic
public class Vd0010Service extends BasePagingService {
	/** コンテナ一覧リポジトリ */
	@Inject private Vd0010Repository repository;
	/** パーツ定義の読み込みリポジトリ */
	@Inject private ContainerLoadRepository partsLoadRepository;
	/** パーツ定義の保存リポジトリ */
	@Inject private ContainerSaveRepository partsSaveRepository;
	/** テーブル同期サービス */
	@Inject private UserTableSyncService userDataService;
	/** ルックアップサービス */
	@Inject private MwmLookupService lookup;
	/** 外部Javascriptサービス */
	@Inject private JavascriptService jsService;
	/** 企業サービス */
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Vd0010Response res = createResponse(Vd0010Response.class, req);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		// テーブル同期の選択肢
		res.syncTableList = lookup.getOptionItems(LookupGroupId.SYNC_TABLE, true);

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Vd0010Response search(Vd0010Request req) {
		final int allCount = repository.count(req);
		final Vd0010Response res = createResponse(Vd0010Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * コンテナのカラム定義をDBへ反映
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse syncDB(Vd0010ContainerRequest req) {
		final List<String> allErrors = new ArrayList<>();
		for (Long containerId : req.containerIds) {
			// カラム定義の読み込み
			final MwmContainer c = partsLoadRepository.getMwmContainer(containerId);
			final List<PartsColumn> columns = partsLoadRepository
					.getMwmPartsColumnList(containerId)
					.stream()
					.map(pc -> new PartsColumn(pc))
					.collect(Collectors.toList());
			// カラム定義をDBテーブルへ反映
			final List<String> errors = userDataService.syncTable(c, columns);

			// テーブル同期日時を更新
			if (errors.isEmpty())
				repository.updateTableSyncTimestamp(c);

			allErrors.addAll(errors);
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		if (allErrors.isEmpty()) {
			res.success = true;
			res.addSuccesses(i18n.getText(MessageCd.MSG0119));
		} else {
			res.success = false;
			res.addAlerts(allErrors.toArray(new String[allErrors.size()]));
		}
		return res;
	}

	/**
	 * コンテナとその配下テーブルに対して、コンテナIDに該当する全レコードを削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Vd0010ContainerRequest req) {
		@SuppressWarnings("unused")
		int count = 0;
		for (Long containerId : req.containerIds) {
			count += partsSaveRepository.delete(containerId);

			// コンテナを参照している画面のJavascriptをクリア
			// ・処理前に画面Javascriptを構成していたコンテナ群と現在のコンテナ群は
			// 　変わっているケースがあり、このため任意のコンテナがどの画面に影響があったかを
			// 　判断するのは困難だから、すなおにキャッシュ全クリアだ
			jsService.clear();
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.containerInfo));
		res.success = true;
		return res;
	}

	/**
	 * コンテナ定義をEXCEL形式でダウンロード
	 * @param req
	 * @return
	 */
	public StreamingOutput downloadExcel(Long containerId) {
		return new Vd0010OutputStreamExcel(containerId);
	}

	/** コンテナ情報を抽出 */
	public Vd0010Entity get(Long containerId) {
		return repository.get(containerId);
	}
}
