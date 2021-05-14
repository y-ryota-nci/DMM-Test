package jp.co.nci.iwf.component.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * リクエストから多言語対応用のメッセージCDを抜き出して、リクエスト生存中に保持し続ける。
 * 意図としては、例外が発生した場合でも元々要求されていた多言語メッセージをクライアントへ返せるようにするため。
 */
@ApplicationScoped
public class MessageCdHolder {

	@Inject private I18nService i18n;

	/** 多言語対応用のメッセージCD */
	private static final ThreadLocal<Set<String>> thread = new ThreadLocal<>();


	/** 初期化 */
	@PostConstruct
	public void init() {
	}

	/** 破棄 */
	@PreDestroy
	public void destory() {
		clearThreadLocal();
	}

	/**
	 * リクエストから多言語対応用のメッセージCDをスレッドローカルへ保存。
	 **/
	public void saveThreadLocal(BaseRequest req) {
		if (req != null)
			thread.set(req.messageCds);
	}

	/** リクエストされたメッセージCDを取得し、対応する多言語リソースを返す */
	public List<JsonMessage> getRequestedMessage() {
		// スレッドローカルからリクエストされたメッセージCDを取得し、対応する多言語リソースを返す
		if (thread.get() == null) {
			return new ArrayList<>();
		}
		return i18n.getJsonMessages(thread.get());
	}

	/**
	 * リクエストから多言語対応用のメッセージCDをスレッドローカルからクリア
	 */
	public void clearThreadLocal() {
		if (thread.get() != null) {
			thread.get().clear();
		}
		thread.set(null);
	}
}
