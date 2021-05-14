package jp.co.nci.iwf.endpoint.up.up0200;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.StreamingReader.Builder;

import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Organization;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Post;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200User;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200UserBelong;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200Book;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetOrganization;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetPost;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetUser;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetUserBelong;
import jp.co.nci.iwf.util.PoiUtils;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * プロファイル情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class Up0200ExcelReader extends PoiUtils {

	/** EXCELファイルを読み込んで、ブックを生成 */
	public Up0200Book parse(InputStream stream) throws IOException {
		// ストリーミングとしてEXCELを読み込む
		final Builder builder = StreamingReader.builder()
				.rowCacheSize(100)
				.bufferSize(4096);
		try (Workbook workbook = builder.open(stream)) {
			final Up0200Book book = new Up0200Book();
			for (Sheet sheet : workbook) {
				String name = sheet.getSheetName();
				if (eq("組織", name))
					book.sheetOrg = readSheetOrg(sheet);
				else if (eq("役職", name))
					book.sheetPost = readSheetPost(sheet);
				else if (eq("ユーザ", name))
					book.sheetUser = parseSheetUser(sheet);
				else if (eq("所属", name))
					book.sheetUserBelong = parseSheetUserBelong(sheet);
			}
			return book;
		}
	}

	/** 所属シートの読み込み */
	private Up0200SheetUserBelong parseSheetUserBelong(Sheet sheet) {
	    final Up0200SheetUserBelong sheetUserBelong = new Up0200SheetUserBelong();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			// 企業コードが未入力なら終わり
			int i = 0;
			String corporationCode = getCellStringValue(row, i++);
			if (isEmpty(corporationCode)) {
				break;
			}
			final Up0200UserBelong ub = new Up0200UserBelong();
			ub.corporationCode = corporationCode;
			ub.userCode = getCellStringValue(row, i++);
			// 所属連番
			// 2019/11/05 Excelアップロードバグの対応
			ub.strSeqNoUserBelong = getCellStringValue(row, i++);
			ub.seqNoUserBelong = toLong(ub.strSeqNoUserBelong);
			ub.organizationCode = getCellStringValue(row, i++);
			ub.postCode = getCellStringValue(row, i++);
			ub.jobType = getCellStringValue(row, i++);
			// 有効期間（開始）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				ub.validStartDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidStartDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidStartDate)) {
					ub.validStartDate = toDate(strValidStartDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					ub.strValidStartDate = strValidStartDate;
				}
			}
			// 有効期間（終了）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				ub.validEndDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidEndDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidEndDate)) {
					ub.validEndDate = toDate(strValidEndDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					ub.strValidEndDate = strValidEndDate;
				}
			}
			ub.deleteFlag = getCellStringValue(row, i++);
			ub.immediateManagerFlag = getCellStringValue(row, i++);
			ub.directorFlag = getCellStringValue(row, i++);
			sheetUserBelong.userBelongs.add(ub);
		}
		return sheetUserBelong;
	}

	/** ユーザシートの読み込み */
	private Up0200SheetUser parseSheetUser(Sheet sheet) {
	    final Up0200SheetUser sheetUser = new Up0200SheetUser();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			// 企業コードが未入力なら終わり
			int i = 0;
			String corporationCode = getCellStringValue(row, i++);
			if (isEmpty(corporationCode)) {
				break;
			}
			final Up0200User user = new Up0200User();
			user.corporationCode = corporationCode;
			user.userCode = getCellStringValue(row, i++);
			user.userNameJa = getCellStringValue(row, i++);
			user.userNameEn = getCellStringValue(row, i++);
			user.userNameZh = getCellStringValue(row, i++);
			user.userNameAbbrJa = getCellStringValue(row, i++);
			user.userNameAbbrEn = getCellStringValue(row, i++);
			user.userNameAbbrZh = getCellStringValue(row, i++);
			user.postNum = getCellStringValue(row, i++);
			user.addressJa = getCellStringValue(row, i++);
			user.addressEn = getCellStringValue(row, i++);
			user.addressZh = getCellStringValue(row, i++);
			user.telNum = getCellStringValue(row, i++);
			user.telNumCel = getCellStringValue(row, i++);
			user.mailAddress = getCellStringValue(row, i++);
			user.userAddedInfo = getCellStringValue(row, i++);
			user.sealName = getCellStringValue(row, i++);
			user.administratorType = getCellStringValue(row, i++);
			user.defaultLocaleCode = getCellStringValue(row, i++);
			// 有効期間（開始）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				user.validStartDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidStartDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidStartDate)) {
					user.validStartDate = toDate(strValidStartDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					user.strValidStartDate = strValidStartDate;
				}
			}
			// 有効期間（終了）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				user.validEndDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidEndDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidEndDate)) {
					user.validEndDate = toDate(strValidEndDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					user.strValidEndDate = strValidEndDate;
				}
			}
			user.deleteFlag = getCellStringValue(row, i++);
			user.extendedInfo01 = getCellStringValue(row, i++);
			user.extendedInfo02 = getCellStringValue(row, i++);
			user.extendedInfo03 = getCellStringValue(row, i++);
			user.extendedInfo04 = getCellStringValue(row, i++);
			user.extendedInfo05 = getCellStringValue(row, i++);
			user.extendedInfo06 = getCellStringValue(row, i++);
			user.extendedInfo07 = getCellStringValue(row, i++);
			user.extendedInfo08 = getCellStringValue(row, i++);
			user.extendedInfo09 = getCellStringValue(row, i++);
			user.extendedInfo10 = getCellStringValue(row, i++);
			sheetUser.users.add(user);
		}
		return sheetUser;
	}

	/** 役職シートの読み込み */
	private Up0200SheetPost readSheetPost(Sheet sheet) {
	    final Up0200SheetPost sheetPost = new Up0200SheetPost();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			// 企業コードが未入力なら終わり
			int i = 0;
			String corporationCode = getCellStringValue(row, i++);
			if (isEmpty(corporationCode)) {
				break;
			}
			final Up0200Post post = new Up0200Post();
			post.corporationCode = corporationCode;
			post.postCode = getCellStringValue(row, i++);
			post.postNameJa = getCellStringValue(row, i++);
			post.postNameEn = getCellStringValue(row, i++);
			post.postNameZh = getCellStringValue(row, i++);
			post.postAddedInfo = getCellStringValue(row, i++);
			post.postNameAbbrJa = getCellStringValue(row, i++);
			post.postNameAbbrEn = getCellStringValue(row, i++);
			post.postNameAbbrZh = getCellStringValue(row, i++);
			// 役職階層
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				post.postLevel = getCellLongValue(row, i++);
			}else{
				String postLevel = getCellStringValue(row, i++);
				if (ValidatorUtils.isNumeric(postLevel)) {
					post.postLevel = toLong(postLevel);
				} else {
					// エラー出力時は文字列で取得する
					post.strPostLevel = postLevel;
				}
			}
			post.uppperPostSettingsFlag = getCellStringValue(row, i++);
			// 有効期間（開始）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				post.validStartDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidStartDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidStartDate)) {
					post.validStartDate = toDate(strValidStartDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					post.strValidStartDate = strValidStartDate;
				}
			}
			// 有効期間（終了）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				post.validEndDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidEndDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidEndDate)) {
					post.validEndDate = toDate(strValidEndDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					post.strValidEndDate = strValidEndDate;
				}
			}
			post.deleteFlag = getCellStringValue(row, i++);
			sheetPost.posts.add(post);
		}
		return sheetPost;
	}

	/** 組織シートの読み込み */
	private Up0200SheetOrganization readSheetOrg(Sheet sheet) {
	    final Up0200SheetOrganization sheetOrg = new Up0200SheetOrganization();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			// 企業コードが未入力なら終わり
			int i = 0;
			String corporationCode = getCellStringValue(row, i++);
			if (isEmpty(corporationCode)) {
				break;
			}
			final Up0200Organization org = new Up0200Organization();
			org.corporationCode = corporationCode;
			org.organizationCode = getCellStringValue(row, i++);
			org.organizationNameJa = getCellStringValue(row, i++);
			org.organizationNameEn = getCellStringValue(row, i++);
			org.organizationNameZh = getCellStringValue(row, i++);
			org.organizationAddedInfo = getCellStringValue(row, i++);
			org.organizationNameAbbrJa = getCellStringValue(row, i++);
			org.organizationNameAbbrEn = getCellStringValue(row, i++);
			org.organizationNameAbbrZh = getCellStringValue(row, i++);
			org.organizationCodeUp = getCellStringValue(row, i++);
			org.postNum = getCellStringValue(row, i++);
			org.addressJa = getCellStringValue(row, i++);
			org.addressEn = getCellStringValue(row, i++);
			org.addressZh = getCellStringValue(row, i++);
			org.telNum = getCellStringValue(row, i++);
			org.faxNum = getCellStringValue(row, i++);
			// 組織階層
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				org.organizationLevel = getCellLongValue(row, i++);
			}else{
				String organizationLevel = getCellStringValue(row, i++);
				if (ValidatorUtils.isNumeric(organizationLevel)) {
					org.organizationLevel = toLong(organizationLevel);
				} else {
					// エラー出力時は文字列で取得する
					org.strOrganizationLevel = organizationLevel;
				}
			}
			//	ソート順
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				org.sortOrder = getCellLongValue(row, i++);
				// エラー出力時は文字列で取得する
				org.strSortOrder = org.sortOrder.toString();
			}else {
				// エラー出力時は文字列で取得する
				org.strSortOrder = getCellStringValue(row, i++);
			}
			// 有効期間（開始）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				org.validStartDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidStartDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidStartDate)) {
					org.validStartDate = toDate(strValidStartDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					org.strValidStartDate = strValidStartDate;
				}
			}
			// 有効期間（終了）
			// 2019/11/05 Excelアップロードバグの対応
			if(getCellType(row , i)== CellType.NUMERIC) {
				org.validEndDate = getCellSqlDateValue(row, i++);
			}else{
				String strValidEndDate = getCellStringValue(row, i++);
				if (ValidatorUtils.isYMD(strValidEndDate)) {
					org.validEndDate = toDate(strValidEndDate, "yyyy/MM/dd");
				} else {
					// エラー出力時は文字列で取得する
					org.strValidEndDate = strValidEndDate;
				}
			}
			org.deleteFlag = getCellStringValue(row, i++);
			org.extendedInfo01 = getCellStringValue(row, i++);
			org.extendedInfo02 = getCellStringValue(row, i++);
			org.extendedInfo03 = getCellStringValue(row, i++);
			org.extendedInfo04 = getCellStringValue(row, i++);
			org.extendedInfo05 = getCellStringValue(row, i++);
			org.extendedInfo06 = getCellStringValue(row, i++);
			org.extendedInfo07 = getCellStringValue(row, i++);
			org.extendedInfo08 = getCellStringValue(row, i++);
			org.extendedInfo09 = getCellStringValue(row, i++);
			org.extendedInfo10 = getCellStringValue(row, i++);
			sheetOrg.organizations.add(org);
		}
		return sheetOrg;
	}

	// 2019/11/05 Excelアップロードバグの対応
	// 直接取得しようとするとセルが存在しない場合にNullPointerExceptionを起こすため
	// セルが存在しない場合はnullを返す
	protected CellType getCellType(Row row , int i) {
		Cell cell = row.getCell(i);
		// セルがあるならセルタイプをセルがないならnullを返す
		return cell != null ? cell.getCellTypeEnum() : null;
	}


}
