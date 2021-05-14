package jp.co.nci.iwf.endpoint.mm.mm0500;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * マスター初期値設定のリクエスト
 */
public class Mm0500Request extends BaseRequest {
	/** コピー元企業コード */
	public String srcCorporationCode;
	/** コピー先企業コードリスト */
	public Set<String> destCorporationCodes;
}
