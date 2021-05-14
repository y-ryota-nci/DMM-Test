package jp.co.nci.iwf.endpoint.mm.mm0050;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.endpoint.mm.mm0051.Mm0051Entity;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 採番形式一覧リポジトリ
 */
@ApplicationScoped
public class Mm0050Repository extends BaseRepository implements CodeBook {

	/**
	 * 採番形式マスタ検索
	 * @param corporationCode
	 * @param userCode
	 * @param localeCode
	 * @return
	 */
	public List<Mm0051Entity> search(Mm0050Request req, String localeCode, boolean isPaging) {
		List<Object> params = new ArrayList<>();

		StringBuilder replace = new StringBuilder();

		params.add(localeCode);

		if (!CommonUtil.isEmpty(req.corporationCode)) {
			replace.append(" and N.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		if (!CommonUtil.isEmpty(req.partsNumberingFormatCode)) {
			replace.append(" and N.PARTS_NUMBERING_FORMAT_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.partsNumberingFormatCode));
		}
		if (!CommonUtil.isEmpty(req.partsNumberingFormatName)) {
			replace.append(" and N.PARTS_NUMBERING_FORMAT_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.partsNumberingFormatName));
		}
		if (!CommonUtil.isEmpty(req.deleteFlag)) {
			replace.append(" and N.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
		}

		if (!CommonUtil.isEmpty(req.sortColumn)) {
			replace.append(" ORDER BY");
			String[] cols = req.sortColumn.split(",");
			int i = 0;
			for (String c : cols) {
				if (i == 0) {
					replace.append(" N." + c.trim());
				} else {
					replace.append(" , N." + c.trim());
				}
				if (!req.sortAsc) {
					replace.append(" DESC");
				}
				i++;
			}
		}
		// ページング
		if (isPaging) {
			replace.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}

		String sql = getSql("MM0050_01").replaceFirst("###REPLACE###", replace.toString());
		List<Mm0051Entity> list = select(Mm0051Entity.class, sql, params.toArray());
		return list;
	}

	public int count(Mm0050Request req, String localeCode) {
		List<Object> params = new ArrayList<>();
		StringBuilder replace = new StringBuilder();
		params.add(localeCode);

		if (!CommonUtil.isEmpty(req.corporationCode)) {
			replace.append(" and N.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		if (!CommonUtil.isEmpty(req.partsNumberingFormatCode)) {
			replace.append(" and N.PARTS_NUMBERING_FORMAT_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.partsNumberingFormatCode));
		}
		if (!CommonUtil.isEmpty(req.partsNumberingFormatName)) {
			replace.append(" and N.PARTS_NUMBERING_FORMAT_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.partsNumberingFormatName));
		}
		if (!CommonUtil.isEmpty(req.deleteFlag)) {
			replace.append(" and N.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
		}

		if (!CommonUtil.isEmpty(req.sortColumn)) {
			replace.append(" ORDER BY");
			String[] cols = req.sortColumn.split(",");
			int i = 0;
			for (String c : cols) {
				if (i == 0) {
					replace.append(" N." + c.trim());
				} else {
					replace.append(" , N." + c.trim());
				}
				if (!req.sortAsc) {
					replace.append(" DESC");
				}
				i++;
			}
		}

		String sql = getSql("MM0050_01").replaceFirst("###REPLACE###", replace.toString());
		return count("SELECT COUNT(*) FROM (" + sql + ")", params.toArray());
	}
}
