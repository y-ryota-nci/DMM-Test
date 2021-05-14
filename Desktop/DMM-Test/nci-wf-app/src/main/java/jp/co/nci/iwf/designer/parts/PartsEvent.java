package jp.co.nci.iwf.designer.parts;

import java.io.Serializable;
import java.util.Objects;

import jp.co.nci.iwf.jpa.entity.mw.MwmPartsEvent;

/**
 * パーツイベント
 */
public class PartsEvent implements Serializable {
	/** パーツイベントID */
	public Long partsEventId;
	/** イベント名 */
	public String eventName;
	/** 呼び出し関数名 */
	public String functionName;
	/** 呼び出し関数へのパラメータ */
	public String functionParameter;
	/** 並び順 */
	public int sortOrder;

	/** コンストラクタ */
	public PartsEvent() {
	}

	/** コンストラクタ */
	public PartsEvent(String eventName, int sortOrder) {
		this.eventName = eventName;
		this.sortOrder = sortOrder;
	}

	/** コンストラクタ */
	public PartsEvent(MwmPartsEvent ev) {
		partsEventId = ev.getPartsEventId();
		eventName = ev.getEventName();
		functionName = ev.getFunctionName();
		functionParameter = ev.getFunctionParameter();
		sortOrder = ev.getSortOrder();
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventName, functionName, functionParameter);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PartsEvent) {
			return equals((PartsEvent)obj);
		}
		return super.equals(obj);
	}

	public boolean equals(PartsEvent obj) {
		return Objects.equals(eventName, obj.eventName)
				&& Objects.equals(functionName, obj.functionName)
				&& Objects.equals(functionParameter, obj.functionParameter);
	}
}
