package jp.co.nci.iwf.endpoint.up.up0200.sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200User;

/**
 * プロファイル情報アップロードの「ユーザ」シートのマッピング定義
 */
@XlsSheet(name="ユーザ")
public class Up0200SheetUser implements Serializable {
	/** 組織マスタ */
	@XlsHorizontalRecords(tableLabel="ユーザ")
	public List<Up0200User> users = new ArrayList<>();
}
