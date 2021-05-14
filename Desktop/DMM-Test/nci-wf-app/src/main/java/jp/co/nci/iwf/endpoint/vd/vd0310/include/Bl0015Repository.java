package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileWf;

/**
 * ブロック：ワークフロー文書ファイルのリポジトリ
 */
@ApplicationScoped
public class Bl0015Repository extends BaseRepository implements CodeMaster {

	@Inject
	private NumberingService numbering;

	public List<MwtDocFileWf> getDocFileWfList(String corporationCode, Long processId) {
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(processId);
		return select(MwtDocFileWf.class, getSql("VD0310_17"), params.toArray());
	}

	public MwtDocFileWf upload(UploadFile file) throws IOException {
		MwtDocFileWf e = new MwtDocFileWf();
		e.setDocFileWfId(numbering.newPK(MwtDocFileWf.class));
		e.setCorporationCode(null);
		e.setProcessId(null);
		e.setFileName(file.fileName);
		byte[] fileData = toBytes(file.stream);
		e.setFileData(fileData);
		e.setFileSize(e.getFileData().length);
		// Oracle全文検索用のフォーマットを取得
		final String fmt = this.getFmt(e.getFileName());
		// Oracle全文検索用のキャラクタ・セット名を取得
		final String cset = this.getCharset(fmt, fileData);
		e.setFmt(fmt);
		e.setCset(cset);
		e.setDeleteFlag(DeleteFlag.OFF);
		em.persist(e);
		em.flush();
		return e;
	}

	public MwtDocFileWf get(Long docFileWfId) {
		return em.find(MwtDocFileWf.class, docFileWfId);
	}

	public void delete(Long docFileWfId) {
		final List<Object> params = new ArrayList<>();
		params.add(docFileWfId);
		execSql(getSql("VD0310_18"), params.toArray());
	}

	public void update(MwtDocFileWf entity) {
		em.merge(entity);
		em.flush();
	}

	/**
	 * 文書管理側からワークフロー側へ文書ファイルデータのコピー処理.
	 * @param docFileId 文書ファイルID
	 * @return ワークフロー文書ファイル情報
	 */
	public MwtDocFileWf copyDocFileDoc2Wf(Long docFileId) {
		final long docFileWfId = numbering.newPK(MwtDocFileWf.class);
		final Object[] params = { docFileWfId, docFileId };
		execSql(getSql("VD0310_20"), params);
		return get(docFileWfId);
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
