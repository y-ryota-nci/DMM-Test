package jp.co.dmm.customize.endpoint.sp.sp0011;

import java.util.List;

import jp.co.dmm.customize.endpoint.sp.PayeeBnkaccMstEntity;
import jp.co.dmm.customize.endpoint.sp.SplrMstEntity;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 取引先登録情報取得結果レスポンス
 */
public class Sp0011GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public SplrMstEntity entity;

	/** 口座明細リスト */
	public List<PayeeBnkaccMstEntity> accList;

	/** 法人・個人区分選択肢 */
	public List<OptionItem> crpPrsTps;
	/** 国内・海外区分選択肢 */
	public List<OptionItem> dmsAbrTps;
	/** 都道府県選択肢 */
	public List<OptionItem> adrPrfCds;
	/** 取引状況区分選択肢 */
	public List<OptionItem> trdStsTps;
	/** 振込手数料負担区分選択肢 */
	public List<OptionItem> payCmmOblTps;
	/** 休日処理区分選択肢 */
	public List<OptionItem> hldTrtTps;
	/** 振込先口座種別  */
	public List<OptionItem> bnkaccTps;
	/** 下請法区分 */
	public List<OptionItem> subCntrctTps;
	/** 関係会社区分 */
	public List<OptionItem> affcmpTps;
}
