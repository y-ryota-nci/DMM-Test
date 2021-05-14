package jp.co.nci.iwf.component.mail;

public interface MailCodeBook {
	/** 改行 */
	String LF = "\n";

	/**
	 * メール環境の列挙型
	 */
	public enum MailEnv {
		/** 開発（＝メール送信せず、ログのみ出力） */
		DEVELOP,
		/** ステージング（指定されたメールアドレスではなく、ダミーアドレスへ実際にメール送信） */
		STAGING,
		/** 本番（指定されたメールアドレスへ実際にメール送信） */
		PRODUCT,
		;
	}

	/**
	 * メールテンプレートで差し込み可能な変数
	 */
	public interface MailVariables {
		/** 試験環境識別文字 */
		String STAGING_ENV_STRING = "STAGING_ENV_STRING";
		/** ログイン者企業名 */
		String LOGIN_CORPORATION_NAME = "LOGIN_CORPORATION_NAME";
		/** ログイン者氏名 */
		String LOGIN_USER_NAME = "LOGIN_USER_NAME";
		/** ログインID */
		String LOGIN_ID = "LOGIN_ID";
		/** ログイン者主務組織名 */
		String LOGIN_ORGANIZATION_NAME = "LOGIN_ORGANIZATION_NAME";
		/** ログイン者主務役職名 */
		String LOGIN_POST_NAME = "LOGIN_POST_NAME";
		/** 添付ファイル名 */
		String ATTACH_FILE = "ATTACH_FILE";
		/** 件名 */
		String SUBJECT = "SUBJECT";
		/** 申請金額 */
		String AMOUNT = "AMOUNT";
		/** 申請番号 */
		String APPLICATION_NO = "APPLICATION_NO";
		/** 決裁番号 */
		String APPROVAL_NO = "APPROVAL_NO";
		/** コメント（引き戻しや差戻しの） */
		String COMMENT = "COMMENT";
		/** プロセス定義名 */
		String PROCESS_DEF_NAME = "PROCESS_DEF_NAME";
		/** 画面プロセス定義名 */
		String SCREEN_PROCESS_NAME = "SCREEN_PROCESS_NAME";
		/** 宛先ユーザ氏名 */
		String TO_USER_NAME = "TO_USER_NAME";
		/** 宛先ユーザのログインID */
		String TO_USER_ADDED_INFO = "TO_USER_ADDED_INFO";
		/** 依頼元ユーザ氏名 */
		String FROM_USER_NAME = "FROM_USER_NAME";
		/** 依頼元ユーザのログインID */
		String FROM_USER_ADDED_INFO = "FROM_USER_ADDED_INFO";
		/** 起案担当者氏名 */
		String USER_NAME_OPERATION_START = "USER_NAME_OPERATION_START";
		/** 起案部門名 */
		String ORGANIZATION_NAME_START = "ORGANIZATION_NAME_START";
		/** 企業コード */
		String CORPORATION_CODE = "CORPORATION_CODE";
		/** プロセスID */
		String PROCESS_ID = "PROCESS_ID";
		/** アクティビティID */
		String ACTIVITY_ID = "ACTIVITY_ID";
		/** 代理元ユーザID */
		String PROXY_USER = "PROXY_USER";
		/** 最終更新日時 */
		String TIMESTAMP = "TIMESTAMP";
		// ▼以下、LOOKUPへ移動になったので不要
//		/** ドメインURL */
//		String DOMAIN_URL = "DOMAIN_URL";
		/** 仮パスワード */
		String TEMPORARY_PASSWORD = "TEMPORARY_PASSWORD";
		/** 新パスワード */
		String NEW_PASSWORD = "NEW_PASSWORD";
		/** 暗号化された値 */
		String CIPHER = "CIPHER";
		/** 時間数 */
		String HOURS = "HOURS";
		/** 送信元ユーザID */
		String SOURCE_USER_ID = "SOURCE_USER_ID";
		/** 送信元ユーザ氏名 */
		String SOURCE_USER_NAME = "SOURCE_USER_NAME";
		/** 送信先ユーザID */
		String TARGET_USER_ID = "TARGET_USER_ID";
		/** 送信先ユーザID */
		String TARGET_USER_NAME = "TARGET_USER_NAME";
		/** 有効期間：開始 */
		String VALID_START_DATE = "VALID_START_DATE";
		/** 有効期間：終了 */
		String VALID_END_DATE = "VALID_END_DATE";
	}

	/** メールアクション種別 */
	public enum MailActionTypes {
		/** アクション：起票 */
		CREATE,
		/** アクション：状態遷移 */
		MOVE,
		/** アクション：引戻し */
		PULLBACK,
		/** アクション：差戻し */
		SENDBACK,
//		/** アクション：却下 */
//		STOP,
		/** アクション：却下(取下げ) */
		WITHDRAW,
		/** アクション：却下(却下) */
		REJECT,
		/** アクション：要説明 */
		REQUEST_DESCRIPTION,
		/** アクション：要説明回答 */
		ANSWER_DESCRIPTION,
		/** アクション：保存 */
		SAVE;
	}

	/* プロパティクラス設定key */
	/** SMTPサーバ識別子 */
	String PROPS_SMTP_HOST = "mail.smtp.host";
	/** SMTPサーバ識別子（Message-ID生成用） */
	String PROPS_HOST = "mail.host";
	/** SMTPサーバ接続までのタイムアウト(DNS的な) */
	String PROPS_CONNECT_TIMEOUT = "mail.smtp.connectiontimeout";
	/** SMTPサーバ接続後、メール送信までのタイムアウト */
	String PROPS_TIMEOUT = "mail.smtp.timeout";
	/** 差出人メールアドレス */
	String PROPS_FROM = "mail.smtp.from";
	/** 差出人名 */
	String PROPS_FROM_PERSONAL = "mail.smtp.from.personal";

	/** SMTP認証 true / false */
	String PROPS_SMTP_AUTH = "mail.smtp.auth";
	/**  ポート */
	String PROPS_SMTP_PORT = "mail.smtp.port";
	/**  ユーザID */
	String PROPS_SMTP_USER = "mail.smtp.user";
	/**  パスワード */
	String PROPS_SMTP_PASSWORD = "mail.smtp.password";
	/**  プロトコル (smpts) */
	String PROPS_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	/**  TLS */
	String PROPS_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	/**  SSL */
	String PROPS_SMTP_ENABLESSL_ENABLE = "mail.smtp.EnableSSL.enable";
	/**  SSL SocketFactory */
	String PROPS_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
	/**  SSL SocketFactory fallback */
	String PROPS_SMTP_SOCKETFACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
	/**  SSL SocketFactory PORT */
	String PROPS_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";

	/** CC宛先 */
	String PROPS_CC = "mail.cc";
	/** BCC宛先 */
	String PROPS_BCC = "mail.bcc";
	/** 返信先パス */
	String PROPS_RETURN_PATH = "mail.returnPath";
	/** 返信先 */
	String PROPS_REPLY_TO = "mail.replyTo";
	/** メーラー */
	String PROPS_MAILER = "mail.mailer";
	/** ダミー宛先 */
	String DUMMY_SEND_TO = "mail.dummySendTo";
	/** メールのエンコード */
	String PROPS_ENCODE = "mail.encode";
	/** メールのContent-Transfer-Encoding */
	String PROPS_CONTENT_TRANSFER_ENCODING = "mail.contentTransferEncoding";

	String PROPS_PATH_PREFIX = "mail.path";
	String PROPS_MAIL_ENV = "mail.env";

	/* ヘッダー設定key */
	/** ヘッダーへの配信エラーメール送信先設定 */
	String HEADER_RETURN_PAHT = "Return-Path";
	/** ヘッダーへのメーラー設定 */
	String HEADER_X_MAILER ="X-Mailer";
	/** ヘッダーのContent-Transfer-Encoding設定 */
	String HEADER_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	/** 添付ファイルのcontent_type */
	String CONTENT_TYPE = "application/octet-stream";

	/** メールテンプレートの文字コード */
	String TEMPLATE_CHARSET = "UTF-8";

	//----- デフォルト値
	/** デフォルトタイムアウト（秒） */
	String DEFAULT_TIME_OUT = "60";
	/** デフォルトエンコード */
	String DEFAULT_ENCODE = "ISO-2022-JP";
	/** デフォルトContent-Transfer-Encoding */
	String DEFAULT_CONTENT_TRANSFER_ENCODEING = "7bit";
	/** デフォルトSMTP認証 */
	String DEFAULT_SMTP_AUTH = "false";
}
