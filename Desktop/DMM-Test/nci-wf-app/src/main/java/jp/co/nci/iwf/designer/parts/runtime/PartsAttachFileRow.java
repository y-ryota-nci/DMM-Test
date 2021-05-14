package jp.co.nci.iwf.designer.parts.runtime;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;

/**
 * 【実行時】添付ファイルパーツの行クラス。
 * このインスタンスがワークフロー添付ファイル情報(MWT_ATTACH_FILE_WF)の１レコードに相当する
 *
 */
public class PartsAttachFileRow implements Serializable {
	/** ワークフローパーツ添付ファイルID */
	public Long partsAttachFileWfId;
	/** 並び順（表示用、テーブルデータには無関係）  */
	public int sortOrder;
	/** テーブル更新時の楽観排他キーであるバージョン */
	public Long version;
	/** ファイル名 */
	public String fileName;
	/** コメント */
	public String comments;


	/** コンストラクタ */
	public PartsAttachFileRow() {
	}

	/** コンストラクタ */
	public PartsAttachFileRow(MwtPartsAttachFileWf f) {
		this.partsAttachFileWfId = f.getPartsAttachFileWfId();
		this.sortOrder = f.getSortOrder();
		this.comments = f.getComments();
		this.fileName = f.getFileName();
		this.version = f.getVersion();
	}
}
