package jp.co.dmm.customize.endpoint.mg.mg0000;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.StreamingReader.Builder;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * 情報アップロードのEXCELファイルのリーダー
 */
public abstract class MgExcelReader<B extends MgExcelBook<S>, S extends MgExcelSheet> extends PoiUtils {

	/** EXCELファイルを読み込んで、ブックを生成 */
	public void parse(InputStream stream, B book) throws IOException {
		// ストリーミングとしてEXCELを読み込む
		final Builder builder = StreamingReader.builder()
				.rowCacheSize(100)
				.bufferSize(4096);
		try (Workbook workbook = builder.open(stream)) {
			for (Sheet sheet : workbook) {
				String name = sheet.getSheetName();
				if (eq(getSheetName(), name)) {
					book.sheet = readSheet(sheet);
				}
			}
		}
	}

	/** マスタシートの読み込み */
	protected abstract S readSheet(Sheet sheet);

	protected abstract String getSheetName();

	// 2019/11/05 Excelアップロードバグの対応
	// 直接取得しようとするとセルが存在しない場合にNullPointerExceptionを起こすため
	// セルが存在しない場合はnullを返す
	protected CellType getCellType(Row row , int i) {
		Cell cell = row.getCell(i);
		// セルがあるならセルタイプをセルがないならnullを返す
		return cell != null ? cell.getCellTypeEnum() : null;
	}

}
