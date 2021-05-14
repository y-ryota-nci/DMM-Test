package jp.co.nci.iwf.component.tray.download;

import java.io.Serializable;
import java.util.List;

import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigResult;

/**
 * トレイ設定ダウンロードDTO
 */
public class TrayConfigDownloadDto extends BaseDownloadDto implements Serializable {
	/** トレイ設定 */
	public List<MwmTrayConfig> configList;
	/** トレイ設定個人マスタ */
	public List<MwmTrayConfigPerson> personList;
	/** トレイ設定検索条件マスタ */
	public List<MwmTrayConfigCondition> conditionList;
	/** トレイ設定検索結果マスタ */
	public List<MwmTrayConfigResult> resultList;
	/** 業務管理項目マスタ */
	public List<MwmBusinessInfoName> businessNameList;
	/** 多言語対応マスタ */
	public List<MwmMultilingual> multilingualList;

	/**
	 * コンストラクタ
	 * @param corporationCode 企業コード
	 * @param corporationName 企業名
	 */
	public TrayConfigDownloadDto(String corporationCode, String corporationName) {
		super(corporationCode, corporationName);
	}

	/**
	 * コンストラクタ
	 */
	public TrayConfigDownloadDto() {}
}
