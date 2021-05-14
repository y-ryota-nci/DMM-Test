package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignHyperlink;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】ハイパーリンクパーツ
 * デザイン時にあらかじめリンク先ファイル／URLを決定しておくコントロール。
 * 申請／承認時にリンク先を変えることは出来ない。申請／承認時にリンク先を変えたい（＝アップロードさせたい）なら添付ファイルパーツを使うこと。
 */
public class PartsHyperlink extends PartsBase<PartsDesignHyperlink> {
	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignHyperlink d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		return null;
	}
}
