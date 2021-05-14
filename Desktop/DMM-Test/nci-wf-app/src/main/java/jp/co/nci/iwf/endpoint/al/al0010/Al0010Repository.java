package jp.co.nci.iwf.endpoint.al.al0010;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * アクセスログ検索のリポジトリ
 */
@ApplicationScoped
public class Al0010Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	/**
	 * 件数抽出
	 * @param req
	 * @return
	 */
	public int count(Al0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("AL0010_01"));

		List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 検索エンティティ抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<Al0010Entity> select(Al0010SearchRequest req, Al0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("AL0010_02"));

		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, true);

		return select(Al0010Entity.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Al0010SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		if (req.accessDate == null)
			throw new IllegalArgumentException("アクセス日時は検索パフォーマンス的に必須です");

		// アクセス日時（必須）
		sql.append(" and (? <= AL.ACCESS_TIME and AL.ACCESS_TIME < ?)");
		params.add(toDatetime(req.accessDate, req.accessTmFrom, "00:00"));
		params.add(addMinutes(toDatetime(req.accessDate, req.accessTmTo, "23:59"), 1));	// 対象日時の1分後未満で検索

		// アクセスログID
		if (req.accessLogId != null) {
			sql.append(" and AL.ACCESS_LOG_ID = ?");
			params.add(req.accessLogId);
		}

		// 操作者企業コード→ログイン前はNULLなので注意せよ
		LoginInfo login = LoginInfo.get();
		if (isNotEmpty(req.opeCorporationCode)) {
			// 企業指定されていれば、その企業
			sql.append(" and (AL.OPE_CORPORATION_CODE = ? or AL.OPE_CORPORATION_CODE is null)");
			params.add(req.opeCorporationCode);
		}
		else if (login.isAspAdmin()) {
			// ASP管理者は全企業を参照できる
		}
		else if (isNotEmpty(login.getCorporationGroupCode())) {
			// 企業未指定で操作者＝グループ企業に所属してれば、参照できるのはグループ企業内すべて
			sql.append("and (AL.OPE_CORPORATION_CODE is null or AL.OPE_CORPORATION_CODE in (select CORPORATION_CODE from WFM_CORPORATION where CORPORATION_GROUP_CODE = ? and DELETE_FLAG = '0'))");
			params.add(login.getCorporationGroupCode());
		}
		else {
			// 企業未指定で操作者＝グループ企業に所属してなければ、参照できるのは自社のみ
			sql.append(" and (AL.OPE_CORPORATION_CODE = ? or AL.OPE_CORPORATION_CODE is null)");
			params.add(login.getCorporationCode());
		}
		// 操作者ログインID→ログイン前はNULLなので注意せよ
		if (isNotEmpty(req.opeUserAddedInfo)) {
			sql.append(" and (AL.OPE_USER_ADDED_INFO like ? escape '~' or AL.OPE_USER_ADDED_INFO is null)");
			params.add(escapeLikeFront(req.opeUserAddedInfo));
		}
		// 操作者氏名→ログイン前はNULLなので注意せよ
		if (isNotEmpty(req.opeUserName)) {
			sql.append(" and (AL.OPE_USER_NAME like ? escape '~' or AL.OPE_USER_NAME is null)");
			params.add(escapeLikeBoth(req.opeUserName));
		}
		// 操作者セッションID
		if (isNotEmpty(req.sessionId)) {
			sql.append(" and AL.SESSION_ID = ?");
			params.add(req.sessionId);
		}
		// 画面ID
		if (isNotEmpty(req.screenId)) {
			sql.append(" and AL.SCREEN_ID like ? escape '~'");
			params.add(escapeLikeFront(req.screenId));
		}
		// 画面名
		if (isNotEmpty(req.screenName)) {
			sql.append(" and AL.SCREEN_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.screenName));
		}
		// 操作
		if (isNotEmpty(req.actionName)) {
			sql.append(" and AL.ACTION_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.actionName));
		}
		// 結果種別
		if (isNotEmpty(req.accessLogResultType)) {
			sql.append(" and AL.ACCESS_LOG_RESULT_TYPE = ?");
			params.add(req.accessLogResultType);
		}
		// 操作者IPアドレス
		if (isNotEmpty(req.opeIpAddress)) {
			sql.append(" and AL.OPE_IP_ADDRESS like ? escape '~'");
			params.add(escapeLikeFront(req.opeIpAddress));
		}
		// ユーザエージェント（ブラウザ）
		if (isNotEmpty(req.userAgent)) {
			sql.append(" and AL.USER_AGENT like ? escape '~'");
			params.add(escapeLikeBoth(req.userAgent));
		}
		// APPバージョン
		if (isNotEmpty(req.appVersion)) {
			sql.append(" and AL.APP_VERSION like ? escape '~'");
			params.add(escapeLikeBoth(req.appVersion));
		}
		// DB接続文字列
		if (isNotEmpty(req.dbConnectString)) {
			sql.append(" and AL.DB_CONNECT_STRING like ? escape '~'");
			params.add(escapeLikeBoth(req.dbConnectString));
		}
		// ホストIPアドレス
		if (isNotEmpty(req.hostIpAddress)) {
			sql.append(" and AL.HOST_IP_ADDRESS like ? escape '~'");
			params.add(escapeLikeBoth(req.hostIpAddress));
		}
		// ホストポート番号
		if (req.hostPort != null) {
			sql.append(" and AL.HOST_PORT = ?");
			params.add(req.hostPort);
		}
		// スレッド名
		if (isNotEmpty(req.threadName)) {
			sql.append(" and AL.THREAD_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.threadName));
		}
		// なりすまししたユーザのユーザ付加情報
		if (isNotEmpty(req.spoofingUserAddedInfo)) {
			sql.append(" and AL.SPOOFING_USER_ADDED_INFO like ? escape '~'");
			params.add(escapeLikeFront(req.spoofingUserAddedInfo));
		}
		// なりすまししたユーザの氏名
		if (isNotEmpty(req.spoofingUserName)) {
			sql.append(" and AL.SPOOFING_USER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.spoofingUserName));
		}
		// アクセスログ詳細（例えばスタックトレースの内容とか）
		if (isNotEmpty(req.accessLogDetail)) {
			sql.append(" and exists (select * from MWT_ACCESS_LOG_DETAIL D where D.KEY_VALUE like ? escape '~' and D.ACCESS_LOG_ID = AL.ACCESS_LOG_ID)");
			params.add(escapeLikeBoth(req.accessLogDetail));
		}

		if (paging && isNotEmpty(req.sortColumn)) {
			// ソート
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	private Date toDatetime(String ymd, String hhmiss, String defaultValue) {
		String s = ymd + " " + defaults(hhmiss, defaultValue);
		try {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(s);
		} catch (ParseException e) {
			throw new BadRequestException(e);
		}
	}
}
