package jp.co.nci.iwf.jersey.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import jp.co.nci.integrated_workflow.common.util.OrderBy;

/**
 * ページ制御する検索サービスの基底クラス
 *
 * @param <E> 検索結果エンティティ
 */
public class BasePagingService extends BaseService {

	/**
	 * 総行数とページあたりの行数から、総ページ数を計算
	 * @param allCount 総行数
	 * @param pageSize ページあたりの行数
	 * @return 総ページ数
	 */
	protected int calcPageCount(int allCount, int pageSize) {
		final int pageCount = new BigDecimal(allCount / (double)pageSize)
				.setScale(0, RoundingMode.UP)
				.intValue();
		return pageCount;
	}

	/**
	 * 指定されたページ番号を、総ページ数で補正
	 * @param basePageNo
	 * @param pageCount
	 * @return
	 */
	protected int calcPageNo(int basePageNo, int pageCount) {
		int pageNo = (basePageNo > pageCount ? pageCount : basePageNo);
		if (pageNo < 1) {
			pageNo = 1;
		}
		return pageNo;
	}


	/**
	 * ページ番号とページあたりの行数から、対象ページの開始INDEXを計算して返す
	 * @param pageNo ページ番号
	 * @param pageSize ページあたりの行数
	 * @return 対象ページの開始INDEX
	 */
	protected int calcStartIndex(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * 総件数とページあたりの行数、開始位置から終了INDEXを計算
	 * @param startPos
	 * @param allCount
	 * @param pageSize
	 * @return
	 */
	protected int calcEndIndex(int startPos, int allCount, int pageSize) {
		int end = startPos + pageSize;
		if (end > allCount)
			end = allCount;
		return end;
	}

	/**
	 * ページ制御用のレスポンスを生成
	 * @param c
	 * @param req
	 * @param allCount
	 * @return
	 */
	protected <REQ extends BasePagingRequest, E, RES extends BasePagingResponse> RES createResponse(Class<RES> c, REQ req, int allCount) {
		RES res = createResponse(c, req);

		final int pageCount = calcPageCount(allCount, req.pageSize);
		final int pageNo = calcPageNo(req.pageNo, pageCount);
		res.allCount = allCount;
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.start = calcStartRowNo(pageNo, req.pageSize);
		res.end = calcEndRowNo(pageNo, req.pageSize, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		return res;
	}

	/**
	 * (IWF APIの)ソート条件へ変換
	 * @param req
	 * @param alias テーブル名の別名(エイリアス)
	 * @return
	 */
	protected OrderBy[] toOrderBy(BasePagingRequest req, String alias) {
		List<OrderBy> list = new ArrayList<>();
		String[] cols = req.sortColumn.split(",");
		for (String c : cols) {
			if (isNotEmpty(c)) {
				// エイリアスが元々指定されていれば、あえて追加しない
				final String col;
				if (c.indexOf(".") < 0)
					col = (isEmpty(alias) ? "" : alias) + c.trim();
				else
					col = c.trim();
				list.add(new OrderBy(req.sortAsc, col));
			}
		}
		return list.toArray(new OrderBy[list.size()]);
	}

	/**
	 * ページ番号とページあたりの行数から、対象ページの開始行番号を計算して返す（1スタート）
	 * @param pageNo ページ番号
	 * @param pageSize ページあたりの行数
	 * @return 対象ページの開始行番号
	 */
	protected Integer calcStartRowNo(Integer pageNo, Integer pageSize) {
		if (pageNo == null || pageSize == null)
			return null;
		return (pageNo - 1) * pageSize + 1;
	}

	/**
	 * ページ番号とページあたりの行数、および総レコード数から、対象頁の終了行番号を計算して返す（１スタート）
	 * @param pageNo ページ番号
	 * @param pageSize ページあたりの行数
	 * @param allCount 総レコード数
	 * @return 対象ページの終了行番号
	 */
	protected Integer calcEndRowNo(Integer pageNo, Integer pageSize, Integer allCount) {
		if (pageNo == null || pageSize == null || allCount == null)
			return null;
		int end = Math.min(pageNo * pageSize, allCount);
		return end;
	}

}
