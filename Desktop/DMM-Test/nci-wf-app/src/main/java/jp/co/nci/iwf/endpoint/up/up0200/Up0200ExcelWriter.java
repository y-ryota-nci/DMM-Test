package jp.co.nci.iwf.endpoint.up.up0200;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Organization;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200Post;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200User;
import jp.co.nci.iwf.endpoint.up.up0200.entity.Up0200UserBelong;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetOrganization;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetPost;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetUser;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetUserBelong;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * プロファイル情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class Up0200ExcelWriter extends PoiUtils {

	/** 所属シートの書き込み */
	public void writeUserBelong(Sheet sheet, Up0200SheetUserBelong sheetUserBelong, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Up0200UserBelong ub : sheetUserBelong.userBelongs) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, ub.corporationCode);
			setCellValue(row, i++, ub.userCode);
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合はlong型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(ub.strSeqNoUserBelong) ? ub.strSeqNoUserBelong : ub.seqNoUserBelong);
			setCellValue(row, i++, ub.organizationCode);
			setCellValue(row, i++, ub.postCode);
			setCellValue(row, i++, ub.jobType);
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( ub.strValidStartDate) ? ub.strValidStartDate : ub.validStartDate);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( ub.strValidEndDate) ? ub.strValidEndDate : ub.validEndDate);
			setCellValue(row, i++, ub.deleteFlag);
			setCellValue(row, i++, ub.immediateManagerFlag);
			setCellValue(row, i++, ub.directorFlag);
			setCellValue(row, i++, ub.errorText);
		}
	}

	/** ユーザシートを書き込み */
	public void writeUser(Sheet sheet, Up0200SheetUser sheetUser, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Up0200User user : sheetUser.users) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, user.corporationCode);
			setCellValue(row, i++, user.userCode);
			setCellValue(row, i++, user.userNameJa);
			setCellValue(row, i++, user.userNameEn);
			setCellValue(row, i++, user.userNameZh);
			setCellValue(row, i++, user.userNameAbbrJa);
			setCellValue(row, i++, user.userNameAbbrEn);
			setCellValue(row, i++, user.userNameAbbrZh);
			setCellValue(row, i++, user.postNum);
			setCellValue(row, i++, user.addressJa);
			setCellValue(row, i++, user.addressEn);
			setCellValue(row, i++, user.addressZh);
			setCellValue(row, i++, user.telNum);
			setCellValue(row, i++, user.telNumCel);
			setCellValue(row, i++, user.mailAddress);
			setCellValue(row, i++, user.userAddedInfo);
			setCellValue(row, i++, user.sealName);
			setCellValue(row, i++, user.administratorType);
			setCellValue(row, i++, user.defaultLocaleCode);
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( user.strValidStartDate) ? user.strValidStartDate : user.validStartDate);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( user.strValidEndDate) ? user.strValidEndDate : user.validEndDate);
			setCellValue(row, i++, user.deleteFlag);
			setCellValue(row, i++, user.extendedInfo01);
			setCellValue(row, i++, user.extendedInfo02);
			setCellValue(row, i++, user.extendedInfo03);
			setCellValue(row, i++, user.extendedInfo04);
			setCellValue(row, i++, user.extendedInfo05);
			setCellValue(row, i++, user.extendedInfo06);
			setCellValue(row, i++, user.extendedInfo07);
			setCellValue(row, i++, user.extendedInfo08);
			setCellValue(row, i++, user.extendedInfo09);
			setCellValue(row, i++, user.extendedInfo10);
			setCellValue(row, i++, user.errorText);
		}
	}

	/** 役職シートを書き込み */
	public void writePost(Sheet sheet, Up0200SheetPost sheetPost, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Up0200Post post : sheetPost.posts) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, post.corporationCode);
			setCellValue(row, i++, post.postCode);
			setCellValue(row, i++, post.postNameJa);
			setCellValue(row, i++, post.postNameEn);
			setCellValue(row, i++, post.postNameZh);
			setCellValue(row, i++, post.postAddedInfo);
			setCellValue(row, i++, post.postNameAbbrJa);
			setCellValue(row, i++, post.postNameAbbrEn);
			setCellValue(row, i++, post.postNameAbbrZh);
			// エラー内容出力時はString型、問題ない場合はlong型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(post.strPostLevel) ? post.strPostLevel : post.postLevel);
			setCellValue(row, i++, post.uppperPostSettingsFlag);
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( post.strValidStartDate) ? post.strValidStartDate : post.validStartDate);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( post.strValidEndDate) ? post.strValidEndDate : post.validEndDate);
			setCellValue(row, i++, post.deleteFlag);
			setCellValue(row, i++, post.errorText);
		}
	}

	/** 組織シートの書き込み */
	public void writeOrg(Sheet sheet, Up0200SheetOrganization sheetOrg, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Up0200Organization org : sheetOrg.organizations) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, org.corporationCode);
			setCellValue(row, i++, org.organizationCode);
			setCellValue(row, i++, org.organizationNameJa);
			setCellValue(row, i++, org.organizationNameEn);
			setCellValue(row, i++, org.organizationNameZh);
			setCellValue(row, i++, org.organizationAddedInfo);
			setCellValue(row, i++, org.organizationNameAbbrJa);
			setCellValue(row, i++, org.organizationNameAbbrEn);
			setCellValue(row, i++, org.organizationNameAbbrZh);
			setCellValue(row, i++, org.organizationCodeUp);
			setCellValue(row, i++, org.postNum);
			setCellValue(row, i++, org.addressJa);
			setCellValue(row, i++, org.addressEn);
			setCellValue(row, i++, org.addressZh);
			setCellValue(row, i++, org.telNum);
			setCellValue(row, i++, org.faxNum);
			// エラー内容出力時はString型、問題ない場合はlong型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(org.strOrganizationLevel) ? org.strOrganizationLevel : org.organizationLevel);
			// エラー内容出力時はString型、問題ない場合はlong型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(org.strSortOrder) ? org.strSortOrder : org.sortOrder);
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( org.strValidStartDate) ? org.strValidStartDate : org.validStartDate);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty( org.strValidEndDate) ? org.strValidEndDate : org.validEndDate);
			setCellValue(row, i++, org.deleteFlag);
			setCellValue(row, i++, org.extendedInfo01);
			setCellValue(row, i++, org.extendedInfo02);
			setCellValue(row, i++, org.extendedInfo03);
			setCellValue(row, i++, org.extendedInfo04);
			setCellValue(row, i++, org.extendedInfo05);
			setCellValue(row, i++, org.extendedInfo06);
			setCellValue(row, i++, org.extendedInfo07);
			setCellValue(row, i++, org.extendedInfo08);
			setCellValue(row, i++, org.extendedInfo09);
			setCellValue(row, i++, org.extendedInfo10);
			setCellValue(row, i++, org.errorText);
		}
	}
}
