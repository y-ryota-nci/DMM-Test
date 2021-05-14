package jp.co.dmm.customize.component.parts;

import java.util.List;

import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002PartsRequest;

/**
 * 行データをコンテナに反映するリクエスト
 */
public class DmmRowToContainerRequest extends Bl0002PartsRequest {
	/** コンテナ行データの「プライマリキーに相当するカラム名」 */
	public List<String> primaryPartsCodes;
	/** コンテナへ反映したい行データ */
	public List<DmmEntityRow> newRows;
	/** 追加前に既存データをクリアするか */
	public boolean clearOldRows;
}
