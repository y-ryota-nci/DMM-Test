package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;
import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocMetaInfoEx;

/**
 * 文書属性(拡張)情報.
 */
public class DocAttributeExInfo implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 文書メタID */
	public Long docMetaId;
	/** メタテンプレートID */
	public Long metaTemplateId;
	/** メタテンプレート明細ID */
	public Long metaTemplateDetailId;
	/** メタコード */
	public String metaCode;
	/** メタ項目名 */
	public String metaName;
	/** 入力タイプ */
	public String inputType;
	/** 必須フラグ */
	public String requiredFlag;
	/** 桁数 */
	public Integer maxLengths;
	/** メタ情報1 */
	public String metaValue1;
	/** メタ情報2 */
	public String metaValue2;
	/** メタ情報3 */
	public String metaValue3;
	/** メタ情報4 */
	public String metaValue4;
	/** メタ情報5 */
	public String metaValue5;
	/** 入力タイプが「ラジオボタン」「コンボボックス」時の選択肢一覧 */
	public List<OptionItem> optionItems;

	/** コンストラクタ(デフォルト). */
	public DocAttributeExInfo() {
	}

	/** コンストラクタ. */
	public DocAttributeExInfo(MwtDocMetaInfoEx e) {
		this.docMetaId = e.getDocMetaId();
		this.metaTemplateId = e.getMetaTemplateId();
		this.metaTemplateDetailId = e.getMetaTemplateDetailId();
		this.metaCode = e.getMetaCode();
		this.metaName = e.getMetaName();
		this.inputType = e.getInputType();
		this.requiredFlag = e.getRequiredFlag();
		this.maxLengths = e.getMaxLengths();
		this.metaValue1 = e.getMetaValue1();
		this.metaValue2 = e.getMetaValue2();
		this.metaValue3 = e.getMetaValue3();
		this.metaValue4 = e.getMetaValue4();
		this.metaValue5 = e.getMetaValue5();
	}

	public DocAttributeExInfo(MwmMetaTemplateDetailEx e) {
		this.metaTemplateId = e.getMetaTemplateId();
		this.metaTemplateDetailId = e.getMetaTemplateDetailId();
		this.metaCode = e.getMetaCode();
		this.metaName = e.getMetaName();
		this.inputType = e.getInputType();
		this.requiredFlag = e.getRequiredFlag();
		this.maxLengths = e.getMaxLengths();
		this.metaValue1 = e.getInitialValue1();
		this.metaValue2 = e.getInitialValue2();
		this.metaValue3 = e.getInitialValue3();
		this.metaValue4 = e.getInitialValue4();
		this.metaValue5 = e.getInitialValue5();
	}
}
