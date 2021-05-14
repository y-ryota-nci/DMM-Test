package jp.co.nci.iwf.endpoint.vd.vd0031;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面設定のコンテナ情報ベースのリクエスト
 */
public class Vd0031ContainerRequest extends BaseRequest {
	public Long screenId;
	public Long containerId;
	public Long dcId;
	public TrayType trayType;
	public String screenCustomClass;
	/** 画面パーツ条件定義Map */
	public Map<Long, List<PartsCond>> condMap;
	/** 画面計算式定義Map */
	public Map<Long, List<PartsCalc>> calcMap;
	/** 画面Javascript定義リスト */
	public List<PartsJavascript> scripts;
}
