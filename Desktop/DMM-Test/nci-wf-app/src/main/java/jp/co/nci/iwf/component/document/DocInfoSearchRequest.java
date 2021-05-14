package jp.co.nci.iwf.component.document;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class DocInfoSearchRequest extends BasePagingRequest {

	/** 文書ID */
	public Long docId;
	/** 会社コード */
	public String corporationCode;
	/** コンテンツ種別(1：業務文書、2：バインダー) */
	public String contentsType;
	/** 件名 */
	public String title;
	/** 公開フラグ */
	public String publishFlag;
	/** 公開開始日From */
	public Date publishStartDateFrom;
	/** 公開開始日To */
	public Date publishStartDateTo;
	/** 公開終了日From */
	public Date publishEndDateFrom;
	/** 公開終了日To */
	public Date publishEndDateTo;
	/** ロックフラグ */
	public String lockFlag;
	/** 除外する文書ID */
	public Long excludeDocId;
	/** 削除済みも対象とするか */
	public String includeDeleted;

}
