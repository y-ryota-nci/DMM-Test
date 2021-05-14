package jp.co.nci.iwf.endpoint.au.au0011;

import java.io.Serializable;

public class Au0011CorpProperty implements Serializable {
	/** プロパティコード */
	private String propertyCode;
	/** プロパティ名称 */
	private String propertyName;
	/** 値 */
	private String propertyValue;



	/** プロパティコード */
	public String getPropertyCode() {
		return propertyCode;
	}
	/** プロパティコード */
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	/** プロパティ名称 */
	public String getPropertyName() {
		return propertyName;
	}
	/** プロパティ名称 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/** 値 */
	public String getPropertyValue() {
		return propertyValue;
	}
	/** 値 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
}
