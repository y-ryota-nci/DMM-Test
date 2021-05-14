package jp.co.nci.iwf.endpoint.ml.ml0010;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateBody;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateHeader;

/**
 * メールテンプレート一覧のリポジトリ
 */
@ApplicationScoped
public class Ml0010Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;
	@Inject private MultilingalService multi;

	private static final String SELECT = quotePattern("{SELECT}");

	/**
	 * 件数カウント
	 * @param req
	 * @return
	 */
	public int count(Ml0010Request req) {
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("ML0010_01").replaceFirst(SELECT, "count(*)"));

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページ制御アリで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<Ml0010Entity> select(Ml0010Request req, Ml0010Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("ML0010_01").replaceFirst(SELECT, getSql("ML0010_02")));

		fillCondition(req, sql, params, true);

		return select(Ml0010Entity.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Ml0010Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(req.corporationCode);

		// テンプレートファイル名
		if (isNotEmpty(req.mailTemplateFilename)) {
			sql.append(" and F.MAIL_TEMPLATE_FILENAME like ? escape '~'");
			params.add(escapeLikeBoth(req.mailTemplateFilename));
		}
		// 備考
		if (isNotEmpty(req.remarks)) {
			sql.append(" and F.REMARKS like ? escape '~'");
			params.add(escapeLikeBoth(req.remarks));
		}
		// 送信者
		if (isNotEmpty(req.sendFrom)) {
			sql.append(" and H.SEND_FROM like ? escape '~'");
			params.add(escapeLikeBoth(req.sendFrom));
		}
		// 件名
		if (isNotEmpty(req.mailSubject)) {
			sql.append(" and B.MAIL_SUBJECT like ? escape '~'");
			params.add(escapeLikeBoth(req.mailSubject));
		}

		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}


	/** 削除 */
	public boolean delete(Long mailTemplateFileId, String corporationCode) {
		// メールテンプレートファイルマスタ
		MwmMailTemplateFile f = em.find(MwmMailTemplateFile.class, mailTemplateFileId);
		if (f != null) {
			List<MwmMailTemplateHeader> headers = getMwmMailTemplateHeaders(f.getMailTemplateFileId());
			for (Iterator<MwmMailTemplateHeader> it = headers.iterator(); it.hasNext(); ) {
				MwmMailTemplateHeader h = it.next();
				// 同一企業のメールテンプレートヘッダは削除可
				if (h != null && eq(corporationCode, h.getCorporationCode())) {
					em.remove(h);
					it.remove();

					// メールヘッダに紐付く全メール本文は削除可
					List<MwmMailTemplateBody> bodies = getMwmMailTemplateBodies(h.getMailTemplateHeaderId());
					for (MwmMailTemplateBody b : bodies) {
						if (b != null)
							em.remove(b);
					}
				}
			}

			// どの企業からも参照されなくなったメールテンプレートファイルは削除してよいが、どこかの企業が使用していたら削除できない
			if (headers.isEmpty()) {
				em.remove(f);
				multi.physicalDelete("MWM_MAIL_TEMPLATE_FILE", f.getMailTemplateFileId());
			}
			else {
				return false;
			}
		}
		return true;
	}

	private List<MwmMailTemplateBody> getMwmMailTemplateBodies(long mailTemplateHeaderId) {
		final Object[] params = { mailTemplateHeaderId };
		return select(MwmMailTemplateBody.class, getSql("ML0030_02"), params);
	}

	private List<MwmMailTemplateHeader> getMwmMailTemplateHeaders(long mailTemplateFileId) {
		final Object[] params = { mailTemplateFileId };
		return select(MwmMailTemplateHeader.class, getSql("ML0030_01"), params);
	}

	public String getFilename(Long mailTemplateFileId) {
		MwmMailTemplateFile f = em.find(MwmMailTemplateFile.class, mailTemplateFileId);
		if (f == null)
			return null;
		return f.getMailTemplateFilename();
	}
}
