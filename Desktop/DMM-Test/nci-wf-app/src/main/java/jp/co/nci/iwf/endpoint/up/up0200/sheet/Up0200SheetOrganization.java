package jp.co.nci.iwf.endpoint.up.up0200.sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Organization;

/**
 * プロファイル情報アップロードの「組織」シートのマッピング定義
 */
@XlsSheet(name="組織")
public class Up0200SheetOrganization implements Serializable {
	/** 組織マスタ */
	@XlsHorizontalRecords(tableLabel="組織")
	public List<Up0200Organization> organizations = new ArrayList<>();
}
