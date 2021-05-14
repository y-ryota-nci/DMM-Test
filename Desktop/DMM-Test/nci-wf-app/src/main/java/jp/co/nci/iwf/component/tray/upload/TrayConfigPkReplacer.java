package jp.co.nci.iwf.component.tray.upload;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.tray.download.TrayConfigDownloadDto;
import jp.co.nci.iwf.component.upload.BasePkReplacer;
import jp.co.nci.iwf.component.upload.ChangedPKs;
import jp.co.nci.iwf.component.upload.ChangedPKsMap;
import jp.co.nci.iwf.endpoint.up.up0040.Up0040Request;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigResult;


/**
 * トレイ表示設定アップロードのID置換ロジック
 */
@ApplicationScoped
public class TrayConfigPkReplacer extends BasePkReplacer {

	/**
	 * DTO内の全エンティティに対して、各エンティティのユニークキーからプライマリキーの置換要否を調べ、
	 * すでに使われているのであればプライマリキーを置き換える。
	 * @param dto
	 */
	public ChangedPKsMap replaceAllPK(Up0040Request req, TrayConfigDownloadDto dto) {
		log.debug("START replaceAllPK()");

		// プライマリーキー置換対象
		final ChangedPKsMap changedPKsMap = new ChangedPKsMap();

		//------------------------------------------
		// 業務管理項目マスタ
		//------------------------------------------
		{
			final ChangedPKs<MwmBusinessInfoName> changes = replacePK(dto.businessNameList, MwmBusinessInfoName.class);
			changedPKsMap.put(changes);
		}
		//------------------------------------------
		// トレイ設定　関係
		//------------------------------------------
		// トレイ設定マスタ
		{
			final ChangedPKs<MwmTrayConfig> changes = replacePK(dto.configList, MwmTrayConfig.class);
			changedPKsMap.put(changes);
			// トレイ設定マスタ(MWM_TRAY_CONFIG_CONDITION)
			copyPK("trayConfigId", changes, dto.conditionList);
			// トレイ設定検索結果マスタ(MWM_TRAY_CONFIG_RESULT)
			copyPK("trayConfigId", changes, dto.resultList);
			// トレイ設定個人マスタ(MWM_TRAY_CONFIG_PERSON)
			copyPK("trayConfigId", changes, dto.personList);
		}
		// トレイ設定検索条件マスタ
		{
			final ChangedPKs<MwmTrayConfigCondition> changes = replacePK(dto.conditionList, MwmTrayConfigCondition.class);
			changedPKsMap.put(changes);
		}
		// トレイ設定検索結果マスタ
		{
			final ChangedPKs<MwmTrayConfigResult> changes = replacePK(dto.resultList, MwmTrayConfigResult.class);
			changedPKsMap.put(changes);
		}
		// トレイ設定個人マスタ
		{
			final ChangedPKs<MwmTrayConfigPerson> changes = replacePK(dto.personList, MwmTrayConfigPerson.class);
			changedPKsMap.put(changes);
		}

		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語はDelete＆Insertするから差分不要
//			final ChangedPKs<MwmMultilingual> changes = replacePK(dto.multilingualList, changedPKsMap);
//			changedPKsMap.put(changes);
		}

		log.debug("END replaceAllPK()");

		return changedPKsMap;
	}
}
