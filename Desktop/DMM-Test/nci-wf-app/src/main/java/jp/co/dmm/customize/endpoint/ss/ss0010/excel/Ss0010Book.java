package jp.co.dmm.customize.endpoint.ss.ss0010.excel;

import java.io.Serializable;

public class Ss0010Book implements Serializable {
	/** 区切り文字 */
	public final String SEPARATOR = "\t";

	/** シート「SS-GL送信情報」 */
	public Ss0010SheetSSGLSndInf sheetGLSndInf;
	/** シート「SS-GL送信情報(ヘッダー)」 */
	public Ss0010SheetSSGLSndInfHd sheetGLSndInfHd;
	/** シート「SS-GL送信情報(明細)」 */
	public Ss0010SheetSSGLSndInfDt sheetGLSndInfDt;
	/** シート「SS-AP送信情報」 */
	public Ss0010SheetSSAPSndInf sheetAPSndInf;
	/** シート「SS-AP送信情報(ヘッダー)」 */
	public Ss0010SheetSSAPSndInfHd sheetAPSndInfHd;
	/** シート「SS-AP送信情報(支払明細)」 */
	public Ss0010SheetSSAPSndInfPd sheetAPSndInfPd;
	/** シート「SS-AP送信情報(明細)」 */
	public Ss0010SheetSSAPSndInfDt sheetAPSndInfDt;
}
