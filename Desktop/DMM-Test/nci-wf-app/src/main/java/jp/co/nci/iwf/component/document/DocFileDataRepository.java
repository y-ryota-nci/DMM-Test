package jp.co.nci.iwf.component.document;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocFileDataEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileData;

/**
 * 文書ファイル情報用のリポジトリ
 */
@ApplicationScoped
public class DocFileDataRepository extends BaseRepository {

	@Inject
	private NumberingService numbering;

	/**
	 * 文書ファイルデータ取得.
	 * @param docFileDataId 文書ファイルデータID
	 * @return
	 */
	public MwtDocFileData getMwtDocFileDataByPk(Long docFileDataId) {
		if (docFileDataId == null) {
			return null;
		}
		return em.find(MwtDocFileData.class, docFileDataId);
	}

	/**
	 * 文書ファイルデータ登録.
	 * @param file
	 * @param isDocFile 文書ファイルか
	 * @return
	 */
	public MwtDocFileData insert(UploadFile file, boolean isDocFile) throws IOException {
		MwtDocFileData e = new MwtDocFileData();
		byte[] fileData = toBytes(file.stream);
		e.setDocFileDataId(numbering.newPK(MwtDocFileData.class));
		e.setFileName(file.fileName);
		e.setFileData(fileData);
		e.setFileSize(fileData == null ? 0 : fileData.length);

		// 文書ファイルの場合、Oracle全文検索用のフォーマット、キャラクタ・セットを取得
		// 添付ファイルの場合はフォーマットはIGNORE、キャラクタ・セットはNULL
		final String fmt;
		final String cset;
		if (isDocFile) {
			fmt = this.getFmt(e.getFileName());
			cset = this.getCharset(fmt, fileData);
		} else {
			fmt = "IGNORE";
			cset = null;
		}
		e.setFmt(fmt);
		e.setCset(cset);

		// 新規登録時は削除フラグは"1:削除済"で設定
		// 文書情報が登録・更新されたタイミングで"0:未削除"に更新される
		e.setDeleteFlag(DeleteFlag.ON);
		em.persist(e);
		em.flush();
		return e;
	}

	/**
	 * 文書ファイルデータ更新.
	 * 新規登録時、削除フラグが"1:削除済"で設定されたデータを"0:未削除"へと更新する
	 * @param docFileDataId
	 */
	public void update(Set<Long> updateDocFileDataId, LoginInfo login) {
		if (!updateDocFileDataId.isEmpty()) {
			final StringBuilder sql = new StringBuilder(getSql("DC0020"));
			sql.append(toInListSql("DOC_FILE_DATA_ID", updateDocFileDataId.size()));
			final List<Object> params = new ArrayList<>();
			params.add(login.getCorporationCode());
			params.add(login.getUserCode());
			params.add(timestamp());
			params.addAll(updateDocFileDataId);
			execSql(sql, params.toArray());
		}
	}

	/**
	 * 文書ファイルデータ一括削除.
	 * 削除フラグが"1:削除済"となっているデータが削除対象となる
	 * @param deleteDocFileDataIds 削除対象となる文書ファイルデータID一覧
	 */
	public void deleteMwtDocFileData(Set<Long> deleteDocFileDataIds) {
		if (!deleteDocFileDataIds.isEmpty()) {
			final StringBuilder sql = new StringBuilder(getSql("DC0008"));
			sql.append(toInListSql("DOC_FILE_DATA_ID", deleteDocFileDataIds.size()));
			execSql(sql, deleteDocFileDataIds.toArray());
		}
	}

	/**
	 * ワークフロー側から文書管理側へ文書ファイルデータのコピー処理.
	 * @param docFileWfId ワークフロー文書ファイルID
	 * @return 文書ファイルデータID
	 */
	public MwtDocFileDataEx copyDocFileWf2Doc(Long docFileWfId) {
		final long docFileDataId = numbering.newPK(MwtDocFileData.class);
		final Object[] params = { docFileDataId, docFileWfId };
		execSql(getSql("DC0026"), params);
		return getMwtDocFileDataEx(docFileDataId);
	}

	/**
	 * ワークフロー側から文書管理側へ添付ファイルデータのコピー処理.
	 * @param docFileWfId ワークフロー文書ファイルID
	 * @return 文書ファイルデータID
	 */
	public MwtDocFileDataEx copyAttachFileWf2Doc(Long attachFileWfId) {
		final long docFileDataId = numbering.newPK(MwtDocFileData.class);
		final Object[] params = { docFileDataId, attachFileWfId };
		execSql(getSql("DC0027"), params);
		return getMwtDocFileDataEx(docFileDataId);
	}

	private MwtDocFileDataEx getMwtDocFileDataEx(long docFileDataId) {
		final Object[] params = { docFileDataId };
		return selectOne(MwtDocFileDataEx.class, getSql("DC0032"), params);
	}

	/**
	 * Oracle全文検索にて使用するBINARY/TEXT/IGNOREのいずれかを返す
	 * ファイル名からMIMEタイプを取得し以下のルールに従う
	 * MIMEタイプが
	 *  ・text/plain、またはtext/htmlならTEXT
	 *  ・application/zip、image/jpeg、image/png、image/gifならIGNORE
	 *  ・上記以外はBINARY
	 * @param fileName ファイル名
	 * @return
	 */
	private String getFmt(final String fileName) {
		final String mineType = URLConnection.guessContentTypeFromName(fileName);
		if (in(mineType, "text/plain", "text/html")) {
			return "TEXT";
		} else if (in(mineType, "application/zip", "image/jpeg", "image/png", "image/gif")) {
			return "IGNORE";
		}
		return "BINARY";
	}

	/**
	 * Oracle全文検索で使用するキャラクタ・セットを返す
	 * ただしフォーマットが「BINARY」「IGNORE」の場合はNULLを返す
	 * ファイルデータから文字コードを推察し以下のルールに従う
	 * 文字コードが
	 *  ・UTF-8、ISO-8859-1ならUTF8
	 *  ・EUC-JPならJA16EUCTILDE
	 *  ・上記以外はJA16SJISTILDE
	 *
	 * ※ファイルの中身から文字コードを推察するがデータ量が少ないと判定の精度がだいぶ落ちる
	 * 　本番ではそんなに短いファイルはないことを祈って、とりあえず対応
	 * 　あと日本語以外のファイルは対象外（世界中のすべての文字コードなんて対応無理だ。。。）
	 *
	 * @param fmt フォーマット
	 * @param fileData ファイルデータ
	 * @return
	 */
	private String getCharset(final String fmt, final byte[] fileData) {
		if (in(fmt, "BINARY", "IGNORE")) {
			return null;
		}

		CharsetDetector cd = new CharsetDetector();
		cd.setText(fileData);
		CharsetMatch matches[] = cd.detectAll();
		String tmpCharset = null;
		if (matches != null) {
			int confidence = 0;
			for (CharsetMatch match : matches) {
				if (confidence < match.getConfidence() && isNotEmpty(match.getName())) {
					confidence = match.getConfidence();
					tmpCharset = match.getName();
				}
			}
		}
		if (in(tmpCharset, "UTF-8", "ISO-8859-1")) {
			return "UTF8";
		} else if (eq("EUC-JP", tmpCharset)) {
			return "JA16EUCTILDE";
		}
		return "JA16SJISTILDE";
	}
}
