package jp.co.dmm.customize.endpoint.mg.mg0000;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BaseRepository;

public abstract class MgUploadRepository<B extends MgExcelBook<?>, E extends MgExcelEntity, R extends BasePagingRequest> extends BaseRepository {

	/**
	 * ダウンロード用マスタデータ取得
	 * @param req
	 * @return
	 */
	public abstract List<E> getMasterData(R req);

	/**
	 * マスタチェック用のコート一覧取得
	 * @param book Excel Book
	 */
	public abstract void getUploadMasterCdInfo (B book);

	/**
	 * アップロードされたファイルをＤＢに登録
	 * @param book Excel Book
	 */
	public abstract void uploadRegist(B book, WfUserRole userRole);
}
