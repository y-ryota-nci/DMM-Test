package jp.co.nci.iwf.endpoint.vd.vd0129;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.designer.DesignerCodeBook.UserTable;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * DBカラム名一覧サービス
 */
@BizLogic
public class Vd0129Service extends BaseService {
	@Inject
	private TableMetaDataService meta;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0129Response init(Vd0129Request req) {

		// パーツ定義からカラム情報を抜き出し
		List<PartsColumn> results = new ArrayList<>();
		for (Long partsId : req.ctx.root.childPartsIds) {
			PartsDesign design = req.ctx.designMap.get(partsId);
			if (design == null || design.columns == null || design.columns.isEmpty()) continue;
			for (PartsColumn pc : design.columns) {
				results.add(pc);
			}
		}
		// カラムの並び順でソート
		results.sort((c1, c2) -> {
			int compare = MiscUtils.compareTo(c1.sortOrder, c2.sortOrder);
			if (compare != 0) return compare;
			return MiscUtils.compareTo(c1.columnName, c2.columnName);
		});

		// テーブル作成済みか
		String tableName = req.ctx.root.tableName;
		final boolean existRecord = meta.isExistTable(tableName) && meta.isExistRecord(tableName);

		Vd0129Response res = createResponse(Vd0129Response.class, req);
		res.columns = results;
		res.existRecord = existRecord;
		res.dbmsReservedColNames = CodeBook.DBMS_RESERVED_COL_NAMES;
		res.designerReservedColNames = UserTable.RESERVED_COL_NAMES;
		res.success = true;
		return res;
	}
}
