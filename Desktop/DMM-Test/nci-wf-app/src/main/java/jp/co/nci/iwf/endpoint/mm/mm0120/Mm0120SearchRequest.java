package jp.co.nci.iwf.endpoint.mm.mm0120;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * お知らせ一覧画面検索リクエスト
 */
public class Mm0120SearchRequest extends BasePagingRequest {
	public Long announcementId;
	public String corporationCode;
	public java.sql.Date ymd;
	public String hhmm;
	public String subject;
	public String contents;
	/** 削除用、お知らせIDリスト */
	public Set<Long> announcementIds;
}
