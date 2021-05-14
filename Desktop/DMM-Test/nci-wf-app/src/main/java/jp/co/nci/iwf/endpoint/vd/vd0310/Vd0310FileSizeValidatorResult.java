package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import com.ibm.icu.text.MessageFormat;

/**
 * 申請文書のファイルサイズのバリデーション結果
 */
public class Vd0310FileSizeValidatorResult implements Serializable {
	/** バイト換算用の係数 */
	private static final BigDecimal MB = new BigDecimal(1024 * 1024);

	/** 文書管理の文書ファイルのファイルサイズ */
	private int docFileSize;
	/** 申請文書の添付ファイルブロックのファイルサイズ */
	private int wfFileSize;
	/** 添付ファイルパーツ／画像パーツのファイルサイズ */
	private int partsFileSize;
	/** ファイルサイズ上限 */
	private int maxFileSize;
	/** エラー時のメッセージ */
	private String message;

	/** コンストラクタ */
	public Vd0310FileSizeValidatorResult(String message) {
		this.message = message;
	}

	/** エラー時のエラーメッセージを返す */
	public String toErrorMessage() {
		return MessageFormat.format(message, new Object[] {
				toMB(getTotalFileSize()), toMB(getMaxFileSize()), toMB(getPartsFileSize()),
				toMB(getWfFileSize()), toMB(getDocFileSize())
		});
	}

	/** バイトとメガバイトに換算 */
	private String toMB(int size) {
		NumberFormat f = NumberFormat.getNumberInstance();
		return f.format(new BigDecimal(size).divide(MB).setScale(1, RoundingMode.UP));
	}

	/** ファイルサイズ合計 */
	public int getTotalFileSize() {
		return docFileSize + wfFileSize + partsFileSize;
	}

	/** バリデーション結果を返す */
	public boolean isOK() {
		// 上限値が未定義ならチェックなし
		if (maxFileSize < 1) {
			return true;
		}
		return getTotalFileSize() <= maxFileSize;
	}

	/** 文書管理の文書ファイルのファイルサイズ */
	public int getDocFileSize() {
		return docFileSize;
	}
	/** 文書管理の文書ファイルのファイルサイズ */
	public void setDocFileSize(int docFileSize) {
		this.docFileSize = docFileSize;
	}

	/** 申請文書の添付ファイルブロックのファイルサイズ */
	public int getWfFileSize() {
		return wfFileSize;
	}
	/** 申請文書の添付ファイルブロックのファイルサイズ */
	public void setWfFileSize(int wfFileSize) {
		this.wfFileSize = wfFileSize;
	}

	/** 添付ファイルパーツ／画像パーツのファイルサイズ */
	public int getPartsFileSize() {
		return partsFileSize;
	}
	/** 添付ファイルパーツ／画像パーツのファイルサイズ */
	public void setPartsFileSize(int fileSizeParts) {
		this.partsFileSize = fileSizeParts;
	}

	/** ファイルサイズ上限 */
	public int getMaxFileSize() {
		return maxFileSize;
	}
	/** ファイルサイズ上限 */
	public void setMaxFileSize(int maxfileSize) {
		this.maxFileSize = maxfileSize;
	}
}
