package jp.co.nci.iwf.designer.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jp.co.nci.iwf.designer.parts.design.PartsDesignAttachFile;
import jp.co.nci.iwf.designer.parts.design.PartsDesignImage;
import jp.co.nci.iwf.util.MiscUtils;

/** パーツで取り扱いの許可をするファイル拡張子情報 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)	// クラスのFQCNをJSONフィールドに出力。クライアントから書き戻す際に必要
public class PartsFileLimitation {
	/** どんなファイル形式でも許可 */
	public boolean enableAny;
	/** 画像を許可 */
	public boolean enableImage;
	/** PDFを許可 */
	public boolean enablePdf;
	/** WORD文書を許可 */
	public boolean enableWord;
	/** EXCEL文書を許可 */
	public boolean enableExcel;
	/** PowerPoint文書を許可 */
	public boolean enablePowerPoint;
	/** CSVファイルを許可 */
	public boolean enableCsv;
	/** テキストファイルを許可 */
	public boolean enableText;
	/** HTMLを許可 */
	public boolean enableHtml;
	/** 「その他」を許可 */
	public boolean enableOther;
	/** 「その他」のファイル拡張子の正規表現 */
	public String regExpOther;

	/**
	 * コンストラクタ。どんなファイル形式でも許可するのがデフォルト
	 */
	public PartsFileLimitation(PartsDesignImage d) {
		this.enableAny = d.enableAny;
		this.enableImage = d.enableImage;
		this.enableOther = d.enableOther;
		this.regExpOther = d.regExpOther;
	}

	/**
	 * コンストラクタ
	 * @param enableAny どんなファイル形式でも許可するならtrue
	 */
	public PartsFileLimitation(PartsDesignAttachFile d) {
		this.enableAny = d.enableAny;
		this.enableImage = d.enableImage;
		this.enablePdf = d.enablePdf;
		this.enableWord = d.enableWord;
		this.enableExcel = d.enableExcel;
		this.enablePowerPoint = d.enablePowerPoint;
		this.enableCsv = d.enableCsv;
		this.enableText = d.enableText;
		this.enableHtml = d.enableHtml;
		this.enableOther = d.enableOther;
		this.regExpOther = d.regExpOther;
	}

	/** 正規表現文字列へ変換 */
	@JsonIgnore
	public String toRegExp() {
		if (enableAny) {	// 拡張子の制限なし
			return "";
		}
		else {
			final List<String> values = new ArrayList<>();
			if (enableImage)	// PNG/JPEG/BitMap/TIFF
				values.add("PNG|GIF|JPG|JPEG|BMP|TIF|TIFF");
			if (enablePdf)
				values.add("PDF");
			if (enableWord)		// WORD文書／WORDテンプレート文書／WORDマクロ文書
				values.add("DOC|DOCX|DOCM|DOT|DOTX");
			if (enableExcel)	// EXCEL文書／EXCELテンプレート文書／EXCELマクロ文書
				values.add("XLS|XLSX|XLSM|XLT|XLTX");
			if (enablePowerPoint)	// PowerPoint文書／PowerPointテンプレート文書／PowerPointマクロ文書／
				values.add("PPTX|PPTM|PPT|POTX|POTM|POT|PPSX|PPSM|PPS");
			if (enableCsv)		// CSV（カンマ区切り）／TSV（=タブ区切り)
				values.add("CSV|TSV");
			if (enableText)		// テキスト／ログ／DAT
				values.add("TXT|LOG|DAT");
			if (enableHtml)
				values.add("HTML|HTM");
			if (enableOther && MiscUtils.isNotEmpty(regExpOther))
				values.add(regExpOther);

			final String regExp = values.stream().collect(Collectors.joining("|", "\\.(", ")$"));
			return regExp;
		}
	}
}
