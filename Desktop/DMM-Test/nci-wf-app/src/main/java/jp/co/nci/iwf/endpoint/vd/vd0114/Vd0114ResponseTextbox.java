package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面：テキストボックスパーツのレスポンス
 */
public class Vd0114ResponseTextbox extends BaseResponse {
	/** 入力タイプの選択肢 */
	public List<OptionItem> inputTypeList;
	/** 入力チェックタイプの選択肢（文字） */
	public List<OptionItem> stringValidateTypeList;
	/** 入力チェックタイプの選択肢（数値） */
	public List<OptionItem> numberValidateTypeList;
	/** 入力チェックタイプの選択肢（日付） */
	public List<OptionItem> dateValidateTypeList;
	/** 数値フォーマットの選択肢 */
	public List<OptionItem> numberFormatList;
	/** IME制御の選択肢 */
	public List<OptionItem> imeModeList;
	/** 端数処理の選択肢 */
	public List<OptionItem> roundTypeList;
	/** パーセントの格納方法 */
	public List<OptionItem> saveMethodPercentList;
	/** 桁数タイプの選択肢 */
	public List<OptionItem> lengthTypeList;
	/** カラム型の変更不可フラグ */
	public boolean lockInputType;
	/** 連動タイプの選択肢 */
	public List<OptionItem> coodinationTypeList;
}
