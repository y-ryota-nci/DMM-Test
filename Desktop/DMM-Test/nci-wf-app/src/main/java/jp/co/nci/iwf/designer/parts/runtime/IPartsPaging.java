package jp.co.nci.iwf.designer.parts.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ページングするパーツのインターフェース
 */
public interface IPartsPaging {

	/**
	 * 新しいページ番号を総ページ数で補正してセット
	 */
	@JsonIgnore
	void adjustNewPageNo(int newPageNo);
}
