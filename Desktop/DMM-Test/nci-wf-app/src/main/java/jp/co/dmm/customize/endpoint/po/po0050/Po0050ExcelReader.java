package jp.co.dmm.customize.endpoint.po.po0050;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.StreamingReader.Builder;

import jp.co.nci.iwf.util.PoiUtils;

/**
 * 通販データアップロードのEXCELファイルのリーダー.
 */
@ApplicationScoped
public class Po0050ExcelReader extends PoiUtils {

	/** EXCELファイルを読み込んで、ブックを生成 */
	public Po0050Book parse(InputStream stream) throws IOException {
		// ストリーミングとしてEXCELを読み込む
		final Builder builder = StreamingReader.builder()
				.rowCacheSize(100)
				.bufferSize(4096);
		try (Workbook workbook = builder.open(stream)) {
			final Po0050Book book = new Po0050Book();
			// 通販データ取込では最初のシートしか使用しない(他のシートは無視)
			final List<Po0050MlordInf> list = read(workbook.getSheetAt(0));
			book.mlordInfs = list;
			return book;
		}
	}

	/** シートを読み込み、通販データを生成. */
	private List<Po0050MlordInf> read(Sheet sheet) {
		final List<Po0050MlordInf> list = new ArrayList<>();
	    int r = 0;
	    int sqno = 1;
		for (Row row : sheet) {
			// 通販連携では２行目からがデータ行となる
			if (r++ < 1)
				continue;
			if (row == null)
				continue;

			// A列：レコード番号が未入力なら終わり
			int i = 0;
			String recNo = getCellStringValue(row, i++);
			if (isEmpty(recNo)) {
				break;
			}
			final Po0050MlordInf bean = new Po0050MlordInf();
			bean.sqno = sqno++;
			bean.slpNo = recNo;
			bean.lnNo = getCellIntValue(row, i++);
			bean.buyDt = toSlashYMD( getCellStringValue(row, i++) );
			bean.buyCd = getCellStringValue(row, i++);
			bean.buyNmKj = getCellStringValue(row, i++);
			bean.abst = getCellStringValue(row, i++);
			bean.cmmdtCd = getCellStringValue(row, i++);
			bean.prtNo = getCellStringValue(row, i++);
			bean.cmmdtTtl = getCellStringValue(row, i++);
			bean.qnt = getCellLongValue(row, i++);
			bean.uc = getCellDoubleValue(row, i++);
			bean.amt = getCellLongValue(row, i++);
			bean.rmk = getCellStringValue(row, i++);
			bean.splrCd = getCellStringValue(row, i++);
			bean.bumonCd = getCellStringValue(row, i++);
//			bean.taxCd = getCellStringValue(row, i++);
			bean.taxFgChg = getCellStringValue(row, i++);
			bean.taxUnt = getCellStringValue(row, i++);
			bean.itmExpsCd1 = getCellStringValue(row, i++);
			bean.itmExpsCd2 = getCellStringValue(row, i++);
			list.add(bean);
		}
		return list;
	}

	private String toSlashYMD(String val) {
		if (isEmpty(val)) return val;
		if (in(val, "/")) return val;
		if (val.length() != 8) return val;
		return val.substring(0, 4) + "/" + val.substring(4, 6) + "/" + val.substring(6);
	}
}
