package jp.co.nci.iwf.component.mail.upload;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import jp.co.nci.iwf.component.mail.download.MailTemplateDownloadDto;
import jp.co.nci.iwf.component.mail.download.MailTemplateDownloadService;
import jp.co.nci.iwf.component.upload.BaseUploadRepository;
import jp.co.nci.iwf.component.upload.ChangedPKs;
import jp.co.nci.iwf.component.upload.ChangedPKsMap;
import jp.co.nci.iwf.endpoint.up.up0050.Up0050Request;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;

/**
 * メールテンプレートのアップロードサービス
 */
@ApplicationScoped
public class MailTemplateUploadService extends BaseUploadRepository {
	@Inject private MailTemplatePkReplacer replacer;
	@Inject private MailTemplateDownloadService downloader;
	@Inject private Logger log;


	/**
	 * メールテンプレート定義アップロードによるデータ登録処理
	 * @param newCorporationCode
	 * @param req
	 * @param dto
	 */
	@Transactional
	public void persist(String newCorporationCode, Up0050Request req, MailTemplateDownloadDto dto) {
		// ★実際の更新処理：採番マスタの現在値を、各エンティティのID最大値と同期させる
		syncMwmNumbering(dto);

		// ◇ユーザ指定内容で企業コードを書き換え
		replaceUniqueKeys(dto.corporationCode, newCorporationCode, dto);

		// ◇既存データがあれば、プライマリキーを既存データのもので置換（メモリ上のみ）
		final ChangedPKsMap changedPKsMap = replacer.replaceAllPK(req, dto);

		// ◇差分更新するため、現在のデータベースの内容を抽出（メモリ上のみ）
		final MailTemplateDownloadDto currentDto = downloader.createDto(newCorporationCode);

		// ◇取込対象でないメールテンプレートを処理対象から外す
		filterByUserRequest(req.filenames, dto);
		filterByUserRequest(req.filenames, currentDto);

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		upsertAll(req, dto, currentDto, changedPKsMap);
	}

	/** 取込対象でないトレイ設定を処理対象から外す */
	private void filterByUserRequest(Set<String> filenames, MailTemplateDownloadDto dto) {
		// メールテンプレートファイル
		final Set<Long> fileIds = new HashSet<>();
		for (Iterator<MwmMailTemplateFile> it = dto.fileList.iterator(); it.hasNext(); ) {
			final MwmMailTemplateFile f = it.next();
			if (filenames.contains(f.getMailTemplateFilename()))
				fileIds.add(f.getMailTemplateFileId());
			else
				it.remove();
		}
		// メールテンプレートヘッダ
		final Set<Long> headerIds = new HashSet<>();
		for (Iterator<MwmMailTemplateHeader> it = dto.headerList.iterator(); it.hasNext(); ) {
			final MwmMailTemplateHeader h = it.next();
			if (fileIds.contains(h.getMailTemplateFileId()))
				headerIds.add(h.getMailTemplateHeaderId());
			else
				it.remove();
		}
		// メールテンプレート本文
		for (Iterator<MwmMailTemplateBody> it = dto.bodyList.iterator(); it.hasNext(); ) {
			final MwmMailTemplateBody b = it.next();
			if (!headerIds.contains(b.getMailTemplateHeaderId()))
				it.remove();
		}
	}


	/**
	 * アップロードファイルと現在のデータベース内容の差分更新を行う
	 * @param req リクエスト
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 * @param currentDto 現在のデータベースの内容
	 * @param changedPKsMap PK変更内容
	 */
	private void upsertAll(Up0050Request req,
			MailTemplateDownloadDto dto,
			MailTemplateDownloadDto currentDto,
			ChangedPKsMap changedPKsMap) {
		//-------------------------------------
		// 業務管理項目マスタ
		//-------------------------------------
		{
			deleteAndInsert(dto.businessNameList, currentDto.businessNameList);
		}
		//-------------------------------------
		// メールテンプレート関係マスタ
		//-------------------------------------
		{
			/** メールテンプレート定義ファイルリスト */
			upsert(dto.fileList, currentDto.fileList, changedPKsMap, false);
			/** メールテンプレートヘッダーリスト */
			upsert(dto.headerList, currentDto.headerList, changedPKsMap, false);
			/** メールテンプレート本文リスト */
			upsert(dto.bodyList, currentDto.bodyList, changedPKsMap, false);
			/** メール変数マスタ(MWM_MAIL_VARIABLE) */
			upsert(dto.variableList, currentDto.variableList, changedPKsMap, false);
		}
		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語対応マスタ（MWM_MULTILINGUAL）
			// 多言語マスタは参照元レコードのIDが変わっている可能性があるので、差し替える
			for (MwmMultilingual m : dto.multilingualList) {
				ChangedPKs<?> changed = null;
				if (eq("MWM_MAIL_TEMPLATE_FILE", m.getTableName())) {
					changed = changedPKsMap.get(MwmMailTemplateFile.class);
				}
				if (changed != null && changed.containsKey(m.getId())) {
					m.setId(changed.get(m.getId()).newPK);
				}
			}
			deleteAndInsert(dto.multilingualList, currentDto.multilingualList);
		}
	}


	/** ユーザ指定内容でユニークキーを書き換え */
	private void replaceUniqueKeys(String corporationCode, String newCorporationCode, MailTemplateDownloadDto dto) {
		// メールテンプレートヘッダマスタ
		for (MwmMailTemplateHeader h : dto.headerList)
			h.setCorporationCode(newCorporationCode);

		// 業務管理項目名称マスタ
		for (MwmBusinessInfoName n : dto.businessNameList)
			n.setCorporationCode(newCorporationCode);

		// メール変数マスタ
		for (MwmMailVariable v : dto.variableList)
			v.setCorporationCode(newCorporationCode);
	}


	/**
	 * 採番マスタの現在値を、各エンティティのプライマリキー最大値と同期させる
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 */
	private void syncMwmNumbering(MailTemplateDownloadDto dto) {
		log.debug("START syncMwmNumbering()");

		// DTOのフィールドがリストであればアップロード用エンティティとみなす
		for (Field f : dto.getClass().getDeclaredFields()) {
			if (!List.class.isAssignableFrom(f.getType()))
				continue;
			final List<? extends MwmBaseJpaEntity> entities = getFieldValue(dto, f.getName());
			if (entities == null || entities.isEmpty())
				continue;

			// 各エンティティのプライマリキーの最大値を求める
			final Class<? extends MwmBaseJpaEntity> clazz = entities.get(0).getClass();
			final String pkFieldName = jpaEntityDef.getPkFieldName(clazz);
			final long max = entities.stream()
					.map(e -> (Long)getPropertyValue(e, pkFieldName))
					.filter(id -> id != null)
					.max((v1, v2) -> compareTo(v1, v2))
					.orElse(0L);

			// エンティティのプライマリキーの最大値で「採番マスタの現在値」を更新
			// （採番マスタ.現在値＞新しい値なら更新されないよ）
			if (max > 0L)
				numbering.sync(clazz, max);
		};
		log.debug("END syncMwmNumbering()");
	}

}
