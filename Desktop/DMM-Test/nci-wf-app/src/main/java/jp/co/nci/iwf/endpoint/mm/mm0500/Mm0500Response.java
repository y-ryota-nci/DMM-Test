package jp.co.nci.iwf.endpoint.mm.mm0500;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * マスター初期値設定のレスポンス
 */
public class Mm0500Response extends BaseResponse {
	/** コピー元企業コード */
	public String srcCorporationCode;
	/** コピー元企業の選択肢 */
	public List<OptionItem> srcCorporations;
	/** コピー元企業との差異リスト */
	public List<Mm0500Entity> entities;
}
