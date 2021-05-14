package jp.co.nci.iwf.endpoint.vd.vd0130;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.DesignerCodeBook.FontSize;
import jp.co.nci.iwf.designer.service.ContainerLoadRepository;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * コンテナ設定のサービス
 */
@BizLogic
public class Vd0130Service extends BaseService {
	@SuppressWarnings("unused")
	@Inject
	private ContainerLoadRepository repostiory;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0130Response init(Vd0130InitRequest req) {
		final Vd0130Response res = createResponse(Vd0130Response.class, req);

		// フォントサイズの選択肢
		res.fontSizes = createFontSizeList();
		res.dbmsReservedColNames = CodeBook.DBMS_RESERVED_COL_NAMES;
		res.success = true;
		return res;
	}

	/** フォントサイズの選択肢 */
	private List<OptionItem> createFontSizeList() {
		return Arrays.asList(
				new OptionItem(FontSize.Inherit, i18n.getText(MessageCd.inheritHigherContainer)),
				new OptionItem(FontSize.Small, i18n.getText(MessageCd.fontSizeSmall)),
				new OptionItem(FontSize.Medium, i18n.getText(MessageCd.fontSizeMedium)),
				new OptionItem(FontSize.Large, i18n.getText(MessageCd.fontSizeLarge))
		);
	}
}
