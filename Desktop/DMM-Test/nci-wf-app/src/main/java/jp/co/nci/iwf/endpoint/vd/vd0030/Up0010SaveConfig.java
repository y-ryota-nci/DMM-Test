package jp.co.nci.iwf.endpoint.vd.vd0030;

import java.io.Serializable;
import java.util.Map;

public class Up0010SaveConfig implements Serializable {
	/** アップロード先の企業コード */
	public String corporationCode;
	/** オリジナルの画面コード */
	public String screenCode;
	/** 新しい画面コード */
	public String newScreenCode;
	/** 新しいコンテナコード */
	public Map<String, String> newContainerCodes;
	/** 公開管理設定を取り込むか */
	public boolean isPublication;
	/** 画面プロセス定義を取り込むか */
	public boolean isScreenProcess;
	/** 新規申請フォルダ設定を取り込むか */
	public boolean isFolder;
	/** 新規申請メニュー割当設定を取り込むか */
	public boolean isScreenProcessMenu;
}
