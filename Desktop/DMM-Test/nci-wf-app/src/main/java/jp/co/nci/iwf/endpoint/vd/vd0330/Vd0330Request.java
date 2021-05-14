package jp.co.nci.iwf.endpoint.vd.vd0330;

import java.util.Map;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 申請画面(参照)のリクエスト
 */
public class Vd0330Request extends BaseRequest {
	/** 抽出用キー */
	public Map<String, String> keys;
	/** 企業コード*/
	public String corporationCode;
	/** 画面コード */
	public String screenCode;
	/** 画面名（画面コードに対する画面名称よりパラメータで受け取った画面名を優先とする） */
	public String screenName;
	/** 戻り先URL */
	public String backUrl;
	/** 表示条件ID（指定されればその表示条件を使う） */
	public Long dcId;
	/** トレイタイプ */
	public TrayType trayType;
}
