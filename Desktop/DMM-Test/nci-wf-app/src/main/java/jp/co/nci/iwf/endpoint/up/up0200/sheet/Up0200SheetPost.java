package jp.co.nci.iwf.endpoint.up.up0200.sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Post;

/**
 * プロファイル情報アップロードの「役職」シートのマッピング定義
 */
@XlsSheet(name="役職")
public class Up0200SheetPost  implements Serializable {
	/** 役職マスタ */
	@XlsHorizontalRecords(tableLabel="役職")
	public List<Up0200Post> posts = new ArrayList<>();
}
