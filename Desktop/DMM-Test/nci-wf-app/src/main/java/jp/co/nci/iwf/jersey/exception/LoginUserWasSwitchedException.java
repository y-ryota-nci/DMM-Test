package jp.co.nci.iwf.jersey.exception;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.component.authenticate.LoginInfo;

/**
 * クライアントとサーバ間で、ユーザ情報が異なるときに発生する例外。
 */
public class LoginUserWasSwitchedException extends ClientErrorException {
	/** サーバ側で保持している企業コード */
	public String sessionCorporationCode;
	/** サーバ側で保持しているユーザコード */
	public String sessionUserCode;

	/** クライアント側から送信されてきた企業コード */
	public String clientCorporationCode;
	/** クライアント側から送信されてきたユーザコード */
	public String clientUserCode;


	/**
	 * コンストラクタ
	 * @param clientCorporationCode
	 * @param clientUserCode
	 * @param login
	 */
	public LoginUserWasSwitchedException(String clientCorporationCode, String clientUserCode, LoginInfo login) {
		super(String.format("クライアントとサーバ間で、ログイン者情報が異なります。クライアント={%s/%s} サーバー={%s/%s}",
				clientCorporationCode, clientUserCode, login.getCorporationCode(), login.getUserCode()),
				Response.Status.CONFLICT);
		this.clientCorporationCode = clientCorporationCode;
		this.clientUserCode = clientUserCode;
		this.sessionCorporationCode = login.getCorporationCode();
		this.sessionUserCode = login.getUserCode();
	}

}
