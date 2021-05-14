package jp.co.nci.iwf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.accesslog.AccessLogService;

/**
 * ダウンロード用ユーティリティ
 */
public class DownloadUtils {
	private static final Logger log = LoggerFactory.getLogger(DownloadUtils.class);

	/**
	 * ストリームデータをダウンロードさせる【Endpoint用】。コンテンツタイプはapplication/octet-streamとなる
	 * @param fileName ダウンロードファイル名
	 * @param streaming ダウンロードするコンテンツのストリームを返すロジック。SandboxEndpoint#download
	 * @return
	 */
	public static Response download(String fileName, StreamingOutput streaming) {
		return download(fileName, streaming, MediaType.APPLICATION_OCTET_STREAM);
	}

	/**
	 * ストリームデータをダウンロードさせる【Endpoint用】。
	 * @param fileName ダウンロードファイル名
	 * @param streaming ダウンロードするコンテンツのストリームを返すロジック。SandboxEndpoint#download
	 * @param contentType HTTPのコンテンツタイプ
	 * @return
	 */
	public static Response download(String fileName, StreamingOutput streaming, String contentType) {
		// アクセスログにダウンロード時のファイル名を記録
		final Map<String, Object> map = new HashMap<>();
		map.put("fileName", fileName);
		final AccessLogService accesslog = CDI.current().select(AccessLogService.class).get();
		accesslog.appendDetail(map);

		// StreamingOutput経由でコンテンツをレスポンスへ書き込む
		final String safeFileName = toSafeFileName(fileName);
		return Response
				.ok(streaming)
				.type(contentType)
				.header("content-disposition", "attachment; filename=" + safeFileName + ";filename*=UTF-8''" + safeFileName)
				.build();
	}

	/**
	 * ブラウザでプレビュー。コンテンツをインライン展開してブラウザ内で直接コンテンツを開く。
	 * @param fileName コンテンツを名前を付けて保存するときのファイル名。
	 * @param streaming ダウンロードするコンテンツのストリームを返すロジック。SandboxEndpoint#download
	 * @param contentType コンテンツタイプ。定数 <code>ContentType</code>を参照。
	 * @see CodeBook.ContentType
	 * @return
	 */
	public static Response preview(String fileName, StreamingOutput streaming, String contentType) {
		// アクセスログにダウンロード時のファイル名を記録
		final Map<String, Object> map = new HashMap<>();
		map.put("fileName", fileName);
		final AccessLogService accesslog = CDI.current().select(AccessLogService.class).get();
		accesslog.appendDetail(map);

		// StreamingOutput経由でコンテンツをレスポンスへ書き込む
		final String safeFileName = toSafeFileName(fileName);
		return Response
				.ok(streaming)
				.type(contentType)
				.header("content-disposition", "inline; filename=" + safeFileName + ";filename*=UTF-8''" + safeFileName)
				.build();
	}

	/**
	 * すでに存在しているバイト配列をダウンロードさせる【Endpoint用】。
	 * バイト配列分のメモリを必ず使うことになるため、実装としては望ましくない（OutOfMemoryの可能性がある）。
	 * 可能であればオーバーロード {@link DownloadUtils#download(String, StreamingOutput)} を使うことを検討せよ。
	 * メモリ効率が100倍違ってくるヨ。
	 * @param fileName ダウンロードファイル名
	 * @param bytes ダウンロード内容のバイト配列
	 * @return
	 */
	public static Response download(String fileName, byte[] bytes){
		final Map<String, Object> map = new HashMap<>();
		map.put("fileName", fileName);
		map.put("length", bytes.length);

		final AccessLogService accesslog = CDI.current().select(AccessLogService.class).get();
		accesslog.appendDetail(map);

		final String safeFileName = toSafeFileName(fileName);

		return Response.ok(toStreamingOutput(bytes))
				.header("Content-Disposition", "attachment; filename=" + safeFileName + ";filename*=UTF-8''" + safeFileName)
				.header("content-Length", String.valueOf(bytes == null ? 0L : bytes.length))
				.build();
	}

	/** バイト配列を StreamingOutputへ変換 */
	private static StreamingOutput toStreamingOutput(final byte[] bytes) {
		return new StreamingOutput() {
			@Override
			public void write(OutputStream out) throws IOException, WebApplicationException {
				out.write(bytes);
			}
		};
	}

	/**
	 * ダウンロードしても安全なファイル名へ変換【IE10以前も対応】
	 * @param fileName
	 * @return
	 */
	public static String toSafeFileName(String fileName) {
		if (MiscUtils.isEmpty(fileName))
			return "";

		try {
/*			// クライアントに合わせてファイル名をエンコード
			// このときのエンコードはCSVファイルのエンコードとは関係がないのに留意せよ
			final String agent = req.getHeader("User-Agent").toUpperCase();
			if (agent.indexOf("MSIE") >= 0 || agent.indexOf("TRIDENT") >= 0) {
				// Internet Explorer
				// 【注意】
				// ファイル名は本来RFC2231に従ってエンコード指定を受け入れるべきなのだが、
				// 古いIEではOSのデフォルトエンコード(Shift-JIS)でデコードする仕様。
				// （ http://support.microsoft.com/kb/436616 ）
				//
				// そのため、Shift-JISでファイル名をエンコードしたいところだが
				// Shift-JIS の文字コードが XX5c または XX7c だとIE7で文字化けするケースがある。
				// （ http://support.microsoft.com/kb/930938/ja ）
				//
				// よってUTF-8→URLエンコードしてこれらの問題を回避する。
				//
				// さらにURLエンコードすると半角スペースが半角プラス「+」に変換されて
				// しまうので、URLエンコード後に半角プラス「+」を「%20」に手動変換してやる。
				// この場合、元々のファイル名に半角プラスが含まれている場合はまずいが、
				// 一般的な用途であればまず大丈夫だろう。少なくとも半角スペースが半角プラスに
				// 変わってしまうよりはマシであろう。
				//
				// ただし、全角文字を含むファイル名をURLエンコードした際、
				// ローカルにキャッシュされたファイル名が約250バイト以上になってしまうと
				// ファイル自体を開くことができない（これはWindowsの仕様）。
				// Shift-JISにエンコードすると軽減されるが、上記で述べたように文字化け
				// するようになってしまう
				// →例）fileName = new String(fileName.getBytes("MS932"), "ISO-8859-1")
				fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			}
			else if(agent.indexOf("FIREFOX") >= 0 || agent.indexOf("CHROME") >= 0){
				// RFC2231の仕様に従ってファイル名のエンコードを指定
				String encode = Base64.getEncoder().encodeToString(fileName.getBytes("utf-8"));
				fileName = ("=?utf-8?B?" + encode + "?=");
			}
			else {
				// Safariは必ず日本語が化けるので、諦めるしかない
				// どうもUTF-8文字列の管理として、例えば「バ」を｢ハ｣｢゛｣のように2文字で管理してるためっぽい
				// response.setCharacterEncoding("utf-8")が使えるなら、無変換でもいけるらしい？
			}
			return fileName;
*/

			// 現代的にはこれだけですべて良くなった...進歩って素晴らしい
			// 【注意】もしこれをレガシーなやり方にするのであれば、NCI.download()を呼び出している個所を再修正すること。
			return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		}
		catch (UnsupportedEncodingException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * ファイル内容をストリームに書き込み
	 * @param input ファイル
	 * @param output ストリーム
	 * @param deleteFile 書き込み完了後にファイルを削除するか
	 * @throws IOException
	 */
	public static void copyFileToStream(File input, OutputStream output, boolean deleteFile) throws IOException {
		if (input == null)
			throw new NullPointerException("元ファイルが null です");
		if (!input.exists())
			throw new NotFoundException("元ファイル" + input.getName() + "が存在しません");
		if (output == null)
			throw new NullPointerException("出力先ストリームが null です");

		boolean success = false;
		byte[] buff = new byte[8192];
		try (
				BufferedOutputStream bos = new BufferedOutputStream(output);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input));
		) {
			while (bis.read(buff) > 0) {
				bos.write(buff);
			}
			bos.flush();
			success = true;
		}
		finally {
			if (deleteFile && input.exists()) {
				if (success) {
					try { input.delete(); }
					catch (Exception ioe) { log.warn(ioe.getMessage(), ioe); }
				}
				else {
					log.warn("ダウンロードに失敗したため、ファイルの削除をスキップしました。手動で削除してください。fileName=" + input.getName());
				}
			}
		}
	}
}
