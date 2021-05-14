package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;

/**
 * パーツ添付ファイルサービス
 */
@ApplicationScoped
public class PartsAttachFileService extends BaseRepository {
	@Inject private NumberingService numbering;
	@Inject private CacheManager cm;

	/** キャッシュ */
	private CacheHolder<String, MwmPartsAttachFile> cache;

	/** 初期化 */
	@PostConstruct
	public void init() {
		cache = cm.newInstance(CacheInterval.EVERY_10SECONDS);
	}

	/** パーツ添付ファイルを抽出 */
	public MwmPartsAttachFile getMwmPartsAttachFileByPK(Long partsAttachFileId) {
		if (partsAttachFileId == null) {
			return null;
		}
		return em.find(MwmPartsAttachFile.class, partsAttachFileId);
	}

	/** パーツ添付ファイルを抽出 */
	public MwmPartsAttachFile getMwmPartsAttachFileByUK(Long partsId, String groupKey) {
		if (partsId == null)
			return null;

		final String key = toKey(partsId, groupKey);
		MwmPartsAttachFile f = cache.get(key);
		if (f == null) {
			synchronized (cache) {
				f = cache.get(key);
				if (f == null) {
					f = getMwmPartsAttachFile(partsId, groupKey);
					if (f != null)
						cache.put(key, f);
				}
			}
		}
		return f;
	}

	/** パーツ添付ファイルを抽出 */
	private MwmPartsAttachFile getMwmPartsAttachFile(Long partsId, String groupKey) {
		if (partsId == null)
			return null;

		final List<Object> params = new ArrayList<>();
		final StringBuilder sb = new StringBuilder(getSql("VD0114_09"));
		params.add(partsId);

		if (isEmpty(groupKey))
			sb.append(" and GROUP_KEY is null");
		else {
			sb.append(" and GROUP_KEY = ?");
			params.add(groupKey);
		}

		return selectOne(MwmPartsAttachFile.class, sb.toString(), params.toArray());
	}

	/** キー生成 */
	private String toKey(Long partsId, String groupKey) {
		return partsId + "~/^" + defaults(groupKey, "");
	}


	/** パーツ添付ファイルをインサートし、そのPKを返す */
	public long save(UploadFile src, Long partsId) {
		long partsAttachFileId;
		byte[] fileData = toBytes(src.stream);
		MwmPartsAttachFile current = getMwmPartsAttachFileByUK(partsId, null);
		if (current == null) {
			final MwmPartsAttachFile f = new MwmPartsAttachFile();
			partsAttachFileId = numbering.newPK(MwmPartsAttachFile.class);
			f.setFileName(src.fileName);
			f.setDeleteFlag(DeleteFlag.OFF);
			f.setFileData(fileData);
			f.setFileSize(fileData == null ? 0 : fileData.length);
			f.setGroupKey(null);
			f.setPartsId(partsId);
			f.setPartsAttachFileId(partsAttachFileId);
			em.persist(f);
		} else {
			current.setFileName(src.fileName);
			current.setFileData(fileData);
			current.setFileSize(fileData == null ? 0 : fileData.length);
			partsAttachFileId = current.getPartsAttachFileId();
		}

		return partsAttachFileId;
	}

	/** ワークフロー添付ファイルを抽出 */
	public MwtPartsAttachFileWf getMwtPartsAttachFileWfByPK(Long partsAttachFileWfId) {
		if (partsAttachFileWfId == null)
			return null;
		return em.find(MwtPartsAttachFileWf.class, partsAttachFileWfId);
	}
}
