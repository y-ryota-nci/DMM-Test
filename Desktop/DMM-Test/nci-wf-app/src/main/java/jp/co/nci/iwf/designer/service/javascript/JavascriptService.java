package jp.co.nci.iwf.designer.service.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmJavascriptEntityEx;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.util.ClassPathResource;

/**
 * 外部Javascriptサービス
 */
@ApplicationScoped
public class JavascriptService extends BaseRepository implements CodeBook, CodeBook.HTTP {
	@Inject private JavascriptRepository repository;
	@Inject private CacheManager cm;

	private CacheHolder<String, JsContents> cacheOutsideJs;
	private CacheHolder<String, JsContents> cacheStaticJs;

	@PostConstruct
	public void init() {
		// 外部javascriptは随時変更の可能性があるので、一定間隔で読み直す
		cacheOutsideJs = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		// 静的Javascriptは本番環境ならキャッシュしっぱなしだが、開発時は適宜読み直す
		cacheStaticJs = cm.newInstance(CacheInterval.FROM_DATABASE);
	}

	/** キャッシュクリア */
	public void clear() {
		cacheOutsideJs.clear();
		cacheStaticJs.clear();
	}

	/**
	 * 静的JavascriptをResponseとして返す
	 * @param fileName ファイル名
	 * @param ifNoneMatch クライアント側のETAG
	 * @param ifModifiedSince クライアント側コンテンツの最終更新日時
	 * @return
	 */
	public Response responseStaticJavascript(String fileName, String ifNoneMatch, Long ifModifiedSince) {
		// コンテンツなしか？
		if (isEmpty(fileName))
			return Response.noContent().build();

		final JsContents js = getStaticJavascript(fileName);

		// 変更なしか？
		if (eq(ifNoneMatch, js.etag) || eq(ifModifiedSince, js.lastModified))
			return Response.notModified().build();

		return createResponse(js);
	}

	/**
	 * 静的Javascriptを返す
	 * @param fileName
	 * @return
	 */
	public JsContents getStaticJavascript(String fileName) {
		JsContents js = cacheStaticJs.get(fileName);
		if (js == null) {
			synchronized (cacheStaticJs) {
				js = cacheStaticJs.get(fileName);
				if (js == null) {
					// 事前読み込み対象ファイルはクラスパス上の /resources/javascript の下にある
					final String path = "javascript/" + fileName;
					final ClassPathResource cpr = new ClassPathResource(path);
					if (!cpr.exists())
						throw new NotFoundException("静的Javascriptが見つかりません。" + path);

					try (BufferedReader r = new BufferedReader(new InputStreamReader(cpr.getInputStream()))) {
						// コンテンツ＋最終更新日でJSON化対象Beanを生成（同時にETAGも計算）
						final String contents = r.lines().collect(Collectors.joining(CRLF));
						final long lastModified = cpr.getFile().lastModified();
						js = new JsContents(contents, lastModified);
					}
					catch (IOException e) {
						throw new InternalServerErrorException("静的Javascriptの読み込みが出来ませんでした。" + path, e);
					}
					cacheStaticJs.put(fileName, js);
				}
			}
		}
		return js;
	}

	/**
	 * 外部Javascriptを返す
	 * @param javascriptIds 外部JavascriptのJavascriptID
	 * @param ifNoneMatch クライアント側のETAG
	 * @param ifModifiedSince クライアント側の最終更新日時
	 * @return
	 */
	public Response responseOutsideJavascript(List<Long> javascriptIds, String ifNoneMatch, Long ifModifiedSince) {
		// コンテンツなしか？
		if (javascriptIds == null || javascriptIds.isEmpty())
			return Response.noContent().build();

		final JsContents js = getOutsideJavascript(javascriptIds);

		// 変更なしか？
		if (eq(ifNoneMatch, js.etag) || eq(ifModifiedSince, js.lastModified))
			return Response.notModified().build();

		return createResponse(js);
	}

	/** 外部Javascript用JSコンテンツを生成 */
	private JsContents getOutsideJavascript(List<Long> javascriptIds) {
		// 複数あるjavascriptIDを連結してキー文字列化
		final String key = toOutsideJavascriptKey(javascriptIds);

		// キャッシュにあるか
		JsContents js = cacheOutsideJs.get(key);
		if (js == null) {
			synchronized (cacheOutsideJs) {
				js = cacheOutsideJs.get(key);
				if (js == null) {
					// 存在しなければ外部Javascriptを抽出し、JavascriptIdをキーにMap化
					final Map<Long, MwmJavascriptEntityEx> map = repository.get(javascriptIds)
							.stream()
							.collect(Collectors.toMap(MwmJavascriptEntityEx::getJavascriptId, j -> j));

					// JavascriptIDの出現順にスクリプトを連結してコンテンツ化
					long lastModified = 0L;
					final StringBuilder contents = new StringBuilder(1024);
					for (Long id : javascriptIds) {
						final MwmJavascriptEntityEx entity = map.get(id);
						if (entity != null) {
							contents.append(CRLF);
							contents.append("/*************************************").append(CRLF);
							contents.append(" * ").append(entity.getFileName()).append(CRLF);
							contents.append(" *************************************/").append(CRLF);
							contents.append(entity.getScript()).append(CRLF);
							lastModified = Math.max(entity.getTimestampUpdated().getTime(), lastModified);
						}
					}

					// コンテンツ＋最終更新日でJSON化対象Beanを生成
					js = new JsContents(contents, lastModified);
					cacheOutsideJs.put(key, js);
				}
			}
		}
		return js;
	}

	/** 外部Javascriptのキャッシュキーを生成 */
	private String toOutsideJavascriptKey(Collection<Long> javascriptIds) {
		StringBuilder sb = new StringBuilder(32);
		for (long id : javascriptIds) {
			sb.append(sb.length() == 0 ? "" : "&").append(id);
		}
		return sb.toString();
	}

	/**
	 * デザイナーコンテキスト内の全JavascriptIDを求める
	 * @param ctx
	 * @return
	 */
	public List<Long> toJavascriptIds(DesignerContext ctx) {
		final List<Long> ids = new ArrayList<>(ctx.javascriptIds);
		collectJavascriptIds(ctx.root, ids, ctx, false);
		return ids;
	}

	/**
	 * コンテナ配下の外部JavascriptのJavascriptIDを収集
	 * @param c コンテナ
	 * @param ids 収集先JavascriptIdリスト
	 * @param standAlone 独立画面パーツ以外のコンテナのJavascriptをロードならfalse、独立画面用のJavascriptをロードするならtrue。
	 * 					独立画面パーツはポップアップ画面として単独で使用されるため、通常のスクリプトと同じように読み込むとエレメントが存在しなかったりするので、それを抑制する。
	 */
	public void collectJavascriptIds(PartsDesignContainer c, Collection<Long> ids, DesignerContext ctx, boolean standAlone) {
		// 自コンテナの外部JavascriptIDを収集
		boolean stopLoading = false;
		for (PartsJavascript js : c.javascripts) {
			if (!ids.contains(js.javascriptId)) {
				if (standAlone) {
					// 独立画面は指定コンテナ配下をすべて読み込む。
					ids.add(js.javascriptId);
				} else {
					// 通常画面(VD0310)では独立画面パーツ以外のを読み込む。独立画面パーツならロードしないし、その子パーツ用の再帰呼び出しもしない
					if (c.partsType == PartsType.STAND_ALONE)
						stopLoading = true;
					else
						ids.add(js.javascriptId);
				}
			}
		}

		// 配下コンテナ分を再帰呼び出し
		if (!stopLoading) {
			for (Long partsId : c.childPartsIds) {
				PartsDesign d = ctx.designMap.get(partsId);
				if (d instanceof PartsDesignContainer) {
					collectJavascriptIds((PartsDesignContainer)d, ids, ctx, standAlone);
				}
			}
		}
	}

	/** 成功時のレスポンスを生成 */
	private Response createResponse(JsContents js) {
		return Response.ok(js.contents)
				.type(TEXT_JAVASCRIPT)
				.lastModified(new Date(js.lastModified))
				.tag(js.etag)
				.build();
	}

	/** 画面で使用している外部JavascriptのJavascriptIDリストを抽出 */
	public List<Long> getJavascriptIds(MwvScreen screen) {
		return repository.getScreenJavascript(screen.screenId).stream()
				.map(j -> j.getJavascriptId())
				.collect(Collectors.toList());
	}
}
