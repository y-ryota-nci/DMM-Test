package jp.co.nci.iwf.endpoint.dc.dc0020;

public class Dc0020OperationRequest extends Dc0020SearchRequest {

	/** 会社コード */
	public String corporationCode;
	/** 操作対象の文書ID */
	public Long targetDocId;
	/** 操作対象のバージョン */
	public Long version;
	/** 文書情報の移動先またはコピー先の文書フォルダID */
	public Long docFolderIdTo;
	/** コピー時の文書情報の件名 */
	public String newTitle;
	/** 文書ファイルの移動先またはコピー先の文書ID */
	public Long docIdTo;
}
