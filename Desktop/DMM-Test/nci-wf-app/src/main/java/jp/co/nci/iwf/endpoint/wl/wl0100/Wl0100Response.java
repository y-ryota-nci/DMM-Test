package jp.co.nci.iwf.endpoint.wl.wl0100;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 起案担当者選択画面のレスポンス
 */
public class Wl0100Response extends BaseResponse {
	/** 企業コード */
	public String corporationCode;
	/** 企業コードの選択肢 */
	public List<OptionItem> corporations;
	/** 入力担当者のユーザコード */
	public String userCodeTransfer;
	/** プロセス定義の企業コード */
	public String corporationCodeP;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義明細コード */
	public String processDefDetailCode;
	/** 主務兼務区分の選択肢 */
	public List<OptionItem> jobTypes;
}
