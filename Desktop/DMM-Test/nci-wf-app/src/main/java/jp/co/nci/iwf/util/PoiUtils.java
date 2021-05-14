package jp.co.nci.iwf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.InternalServerErrorException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Apache POI用のユーティリティ
 */
public class PoiUtils extends MiscUtils {

	/**
	 * ワークシートのコピー
	 * @param newSheet コピー先ワークシート
	 * @param sheet コピー元のワークシート
	 */
	public static void copySheet(Sheet newSheet, Sheet sheet) {
		final Map<Integer, CellStyle> styleMap = new HashMap<>();
		copySheet(newSheet, sheet, styleMap);
	}

	/**
	 * ワークシートのコピー
	 * @param newSheet コピー先ワークシート
	 * @param sheet コピー元のワークシート
	 * @param styleMap セル書式Map、セル書式をコピーしないならnull
	 */
	public static void copySheet(Sheet newSheet, Sheet sheet, Map<Integer, CellStyle> styleMap) {

		int maxColumnNum = 0;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			Row srcRow = sheet.getRow(i);
			Row destRow = newSheet.createRow(i);
			if (srcRow != null) {
				copyRow(srcRow, destRow, styleMap, true);
				if (srcRow.getLastCellNum() > maxColumnNum) {
					maxColumnNum = srcRow.getLastCellNum();
				}
			}
		}
		for (int i = 0; i <= maxColumnNum; i++) {
			newSheet.setColumnWidth(i, sheet.getColumnWidth(i));
		}
	}

	/**
	 * 行のコピー
	 * @param srcSheet コピー元ワークシート
	 * @param newSheet  コピー先ワークシート
	 * @param srcRow コピー元の行
	 * @param newRow コピー先の行
	 * @param styles セル書式Map、セル書式をコピーしないならnull
	 * @param copyWithValue セルの値までコピーをするならtrue
	 */
	public static void copyRow(Row srcRow, Row newRow, Map<Integer, CellStyle> styles, boolean copyWithValue) {
		final Sheet srcSheet = srcRow.getSheet();
		final Sheet newSheet = newRow.getSheet();

		// 行の高さ
		newRow.setHeight(srcRow.getHeight());
		// 行自体のデフォルト書式
		newRow.setRowStyle(srcRow.getRowStyle());

		// この行にコピーするコンテンツなし
		if (srcRow.getFirstCellNum() < 0) {
			return;
		}
		// 行内のセル単位でコピー
		for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
			Cell oldCell = srcRow.getCell(j); // ancienne cell
			Cell newCell = newRow.getCell(j); // new cell
			if (oldCell != null) {
				if (newCell == null) {
					newCell = newRow.createCell(j);
				}

				// セルのコピー
				copyCell(oldCell, newCell, styles, copyWithValue);

				// 対象セルは結合セルなら、コピー先でも結合セルを作成
				CellRangeAddress mergedRegion = getMergedRegion(srcSheet, srcRow.getRowNum(), oldCell.getColumnIndex());
				if (mergedRegion != null) {
					try {
						newSheet.addMergedRegion(mergedRegion);
					} catch (IllegalStateException e) {
						// すでに存在する結合セルと重複している
						//（たぶん、複数行にまたがった結合セルの2行目以降をコピーしようとしたはず）
					}
				}
			}
		}
	}

	/**
	 * セルのコピー
	 * @param oldCell コピー元のセル
	 * @param newCell コピー先のセル
	 * @param styles セル書式Map、セル書式をコピーしないならnull
	 * @param copyWithValue セルの値までコピーをするならtrue
	 */
	public static void copyCell(Cell oldCell, Cell newCell, Map<Integer, CellStyle> styles, boolean copyWithValue) {
		if (styles != null) {
			if (oldCell.getSheet().getWorkbook() == newCell.getSheet().getWorkbook()) {
				newCell.setCellStyle(oldCell.getCellStyle());
			} else {
				final int hashCode = toHashCode(oldCell.getCellStyle());
				CellStyle newCellStyle = styles.get(hashCode);
				if (newCellStyle == null) {
					newCellStyle = newCell.getSheet().getWorkbook().createCellStyle();
					newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
					styles.put(hashCode, newCellStyle);
				}
				newCell.setCellStyle(newCellStyle);
			}
		}
		if (copyWithValue) {
			switch (oldCell.getCellTypeEnum()) {
			case STRING:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case BLANK:
				newCell.setCellType(CellType.BLANK);
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellType(CellType.FORMULA);
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 対象位置の結合セル範囲を返す
	 * @param sheet 対象シート
	 * @param rowNum 対象行番号
	 * @param cellNum 対象セル番号
	 * @return
	 */
	private static CellRangeAddress getMergedRegion(Sheet sheet, int rowNum, int cellNum) {
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress merged = sheet.getMergedRegion(i);
			if (merged.isInRange(rowNum, cellNum)) {
				return merged;
			}
		}
		return null;
	}

	/**
	 * セル書式のハッシュ値を計算
	 * @param cs
	 * @return
	 */
	private static int toHashCode(CellStyle cs) {
		return Objects.hash(
				cs.getAlignmentEnum(),
				cs.getBorderBottomEnum(),
				cs.getBorderLeftEnum(),
				cs.getBorderRightEnum(),
				cs.getBorderTopEnum(),
				cs.getBottomBorderColor(),
				cs.getDataFormat(),
				cs.getDataFormatString(),
				cs.getFillBackgroundColor(),
				cs.getFillBackgroundColorColor(),
				cs.getFillForegroundColor(),
				cs.getFillForegroundColorColor(),
				cs.getFillPatternEnum(),
				cs.getFontIndex(),
				cs.getHidden(),
				cs.getIndention(),
				cs.getIndex(),
				cs.getLeftBorderColor(),
				cs.getLocked(),
				cs.getRightBorderColor(),
				cs.getRotation(),
				cs.getShrinkToFit(),
				cs.getTopBorderColor(),
				cs.getVerticalAlignmentEnum(),
				cs.getWrapText()
		);
	}

	/** 行を取得（なければ自動生成） */
	public static Row getOrCreateRow(Sheet sheet, int rowIndex) {
		assert sheet != null : "sheetがNULL";

		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		return row;
	}

	/** セルを取得（なければ自動生成） */
	public static Cell getOrCreateCell(Sheet sheet, int rowIndex, int colIndex) {
		assert sheet != null : "sheetがNULL";

		final Row row = getOrCreateRow(sheet, rowIndex);
		final Cell cell = getOrCreateCell(row, colIndex);
		return cell;
	}

	/** セルを取得（なければ自動生成） */
	public static Cell getOrCreateCell(Row row, int colIndex) {
		assert row != null : "rowがNULL";

		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		return cell;
	}

	/**
	 * セルの値をセットする
	 * @param row 行
	 * @param colIndex セルIndex(0ベース)
	 * @param value 値
	 */
	public static Cell setCellValue(Row row, int colIndex, Object value) {
		final Cell cell = getOrCreateCell(row, colIndex);
		if (value == null) {
			cell.setCellValue((String)null);
			cell.setCellType(CellType.BLANK);
		}
		else if (value instanceof String)
			cell.setCellValue((String)value);
		else if (value instanceof Number)
			cell.setCellValue(((Number)value).doubleValue());
		else if (value instanceof java.util.Date)
			cell.setCellValue((java.util.Date)value);
		else if (value instanceof Boolean)
			cell.setCellValue((Boolean)value);
		else if (value.getClass().isPrimitive())
			cell.setCellValue((double)value);

		return cell;
	}

	/** String型としてセル値を取得 */
	public static String getCellStringValue(Row row, int colIndex) {
		final Cell cell = row.getCell(colIndex);
		if (cell != null) {
			return cell.getStringCellValue();
		}
		return null;
	}

	/** Long型としてセル値を取得 */
	public static Long getCellLongValue(Row row, int colIndex) {
		final Cell cell = row.getCell(colIndex);
		if (cell != null) {
			final CellType type = cell.getCellTypeEnum();
			if (type == CellType.NUMERIC) {
				return (long)cell.getNumericCellValue();
			}
			else if (type == CellType.STRING) {
				final String val = cell.getStringCellValue();
				return toLong(val);
			}
		}
		return null;
	}

	/** Inteter型としてセル値を取得 */
	public static Integer getCellIntValue(Row row, int colIndex) {
		final Cell cell = row.getCell(colIndex);
		if (cell != null) {
			final CellType type = cell.getCellTypeEnum();
			if (type == CellType.NUMERIC) {
				return (int)cell.getNumericCellValue();
			}
			else if (type == CellType.STRING) {
				final String val = cell.getStringCellValue();
				return toInt(val);
			}
		}
		return null;
	}

	/** java.util.Date型としてセル値を取得 */
	public static java.util.Date getCellDateValue(Row row, int colIndex) {
		final Cell cell = row.getCell(colIndex);
		if (cell != null) {
			final CellType type = cell.getCellTypeEnum();
			if (type == CellType.NUMERIC)
				return new java.util.Date(cell.getDateCellValue().getTime());
			else if (type == CellType.STRING) {
				final String val = cell.getStringCellValue();
				try {
					return new SimpleDateFormat("yyyy/M/d").parse(val);
				} catch (ParseException e) {
					throw new InternalServerErrorException("文字列の日付変換に失敗しました。", e);
				}
			}
		}
		return null;
	}

	/** java.sql.Date型としてセル値を取得 */
	public static java.sql.Date getCellSqlDateValue(Row row, int colIndex) {
		final java.util.Date d = getCellDateValue(row, colIndex);
		if (d == null) {
			return null;
		}
		return new java.sql.Date(d.getTime());
	}

	/** Double型としてセル値を取得 */
	public static Double getCellDoubleValue(Row row, int colIndex) {
		final Cell cell = row.getCell(colIndex);
		if (cell != null) {
			final CellType type = cell.getCellTypeEnum();
			if (type == CellType.NUMERIC) {
				return cell.getNumericCellValue();
			}
			else if (type == CellType.STRING) {
				final String val = cell.getStringCellValue();
				return toDouble(val);
			}
		}
		return null;
	}
}
