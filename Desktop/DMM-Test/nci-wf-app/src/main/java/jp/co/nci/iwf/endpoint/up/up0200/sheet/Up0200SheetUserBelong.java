package jp.co.nci.iwf.endpoint.up.up0200.sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200UserBelong;

/**
 * プロファイル情報アップロードの「ユーザ所属」シートのマッピング定義
 */
@XlsSheet(name="所属")
public class Up0200SheetUserBelong implements Serializable {
	/** ユーザ所属マスタ */
	@XlsHorizontalRecords(tableLabel="所属")
	public List<Up0200UserBelong> userBelongs = new ArrayList<>();
}
