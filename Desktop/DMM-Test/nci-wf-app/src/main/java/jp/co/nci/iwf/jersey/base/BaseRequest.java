package jp.co.nci.iwf.jersey.base;

import java.io.Serializable;
import java.util.Set;

import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.component.CodeBook.ViewWidth;

/**
 * リクエストの基底クラス
 * @author nakamura.mitsuyuki
 *
 */
public class BaseRequest implements Serializable, IRequest, Cloneable {
	/** メッセージCDリスト */
	public Set<String> messageCds;
	/** メニューHTMLが必要か */
	public boolean needMenuHtml;
	/** フッターHTMLが必要か */
	public boolean needFooterHtml;
	/** クライアント側の表示幅（'xs':モバイル、'sm':タブレット、'md':PC、'lg':PC(大型)） */
	public ViewWidth viewWidth;

	/** 操作者のセッション上の企業コード（クライアント側とサーバ側で同一ユーザが操作しているかの判定用） */
	public String clientCorporationCode;
	/** 操作者のセッション上のユーザコード（クライアント側とサーバ側で同一ユーザが操作しているかの判定用） */
	public String clientUserCode;

	@Override
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalServerErrorException("クローン出来ませんでした。", e);
		}
	}
}
