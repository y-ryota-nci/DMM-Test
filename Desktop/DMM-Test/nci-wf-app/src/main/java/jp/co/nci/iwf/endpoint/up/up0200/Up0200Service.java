package jp.co.nci.iwf.endpoint.up.up0200;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.EmptyFileException;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;

import com.gh.mygreen.xlsmapper.XlsMapperException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200Book;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetOrganization;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetPost;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetUser;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200SheetUserBelong;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.DownloadUtils;

/**
 * プロファイル情報アップロード画面サービス
 */
@BizLogic
public class Up0200Service extends BaseService {
	@Inject private Up0200Validator validator;
	@Inject private WfInstanceWrapper wf;
	@Inject private Up0200Register uploader;
	@Inject private Up0200ExcelReader reader;
	@Inject private Up0200Repository repository;
	@Inject private Logger log;

	/** キー文字列の区切り文字 */
	private static final String SEPARATOR = "\t";

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * プロファイル情報をアップロード
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public Response upload(FormDataMultiPart multiPart) {
		final Up0200Response res = createResponse(Up0200Response.class, null);

		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			final UploadFile f = new UploadFile(bodyPart);
			try {
				if (StringUtils.isEmpty(f.fileName) || !f.fileName.trim().endsWith(".xlsx")) {
					// アップロード可能なファイルはExcel文書だけです。
					res.addAlerts(i18n.getText(MessageCd.MSG0208, MessageCd.excel));
					res.success = false;
					return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
				}

				// アップロードファイルをバイト配列化
				final byte[] xlsBytes = IOUtils.toByteArray(f.stream);

				try (ByteArrayInputStream bais = new ByteArrayInputStream(xlsBytes)) {
					// アップロードファイルをパースしてEXCELBook生成
					final Up0200Book book = parse(bais);
					//nullチェック sheet名が規定と異なるとnullになるため
					if (	book == null
						|| book.sheetUserBelong == null || book.sheetUserBelong.userBelongs == null
						|| book.sheetPost== null || book.sheetPost.posts == null
						|| book.sheetOrg== null || book.sheetOrg.organizations == null
						|| book.sheetUser== null || book.sheetUser.users == null) {
						// シート名が正しくありません。
						res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.sheetName));
						res.success = false;
						return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
					}
					// アップロードされたEXCELファイルに対してバリデーションを実施し、
					// エラー無しならアップロード内容をいったん画面へ送り返す。エラーがあればエラー内容をEXCELファイルとして送り返す
					if (validator.validate(book)) {
						// アップロード内容をBASE64エンコードで文字列化し、クライアントへ返す
						// ※これが出来るのは、アップロードファイルがせいぜい30KB程度にしかならないと分かっているから。
						// ※もし別処理でアップロードファイルをBASE64エンコードしてクライアントへ返そうと考えているなら、
						// ※ファイルの最大サイズを考慮せよ。もし100KBを超える可能性があるなら別方式を選ぶべし。
						final byte[] bookBytes = toBytesFromObj(book);
						res.encoded = Base64.getEncoder().encodeToString(bookBytes);
						res.fileName = f.fileName;

						// EXCELファイルに含まれていたマスタの件数
						res.orgCount = book.sheetOrg.organizations.stream().count();
						res.postCount = book.sheetPost.posts.stream().count();
						res.userCount = book.sheetUser.users.stream().count();
						res.ubCount = book.sheetUserBelong.userBelongs.stream().count();
						res.success = true;
						return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
					}
					else {
						// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
						Up0200StreamingOutput streaming = new Up0200StreamingOutput(book);
						return DownloadUtils.download(f.fileName, streaming, ContentType.XLSX);
					}
				}
			}
			catch (XlsMapperException e) {
				// ファイル形式が正しくありません
				res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
				res.success = false;
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			catch (IOException e) {
				throw new InternalServerErrorException(e);
			}catch (EmptyFileException e) {
				// ファイル形式が正しくありません
				res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
				res.success = false;
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
		}
		// ファイル形式が正しくありません
		res.addAlerts(i18n.getText(MessageCd.MSG0152, MessageCd.fileFormat));
		res.success = false;
		return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	/**
	 * プロファイル情報アップロードファイルをDBに登録
	 * @param multiPart
	 * @return
	 */
	@Transactional
	public Response register(Up0200SaveRequest req) {
		try {
			// アップロードファイルをパースしてBookを復元
			final byte[] bookBytes = Base64.getDecoder().decode(req.encoded);
			final Up0200Book book = toObjFromBytes(bookBytes);

			// バリデーション
			if (validator.validate(book)) {
				// ロック
				final WfmCorpPropMaster lock = uploader.lock();

				try {
					// プロファイル情報の登録
					uploader.save(book, req.deleteIfNotUse);
				}
				finally {
					// ロック解除
					if (lock != null)
						uploader.unlock(lock);
				}

				// 正常終了
				final BaseResponse res = createResponse(BaseResponse.class, null);
				res.success = true;
				res.addSuccesses(i18n.getText(MessageCd.MSG0151, MessageCd.profileInfo));
				return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
			}
			else {
				// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
				Up0200StreamingOutput streaming = new Up0200StreamingOutput(book);
				return DownloadUtils.download(req.fileName, streaming, ContentType.XLSX);
			}
		}
		catch (XlsMapperException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
		catch (IOException | ClassNotFoundException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private Up0200Book parse(InputStream stream) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		final Up0200Book book = reader.parse(stream);

		// マスタの存在チェック用に、処理前から存在していたコード値を保存しておく
		book.existCorporationCodes = getWfmCorporationCodes();
		book.existsOrganizationCodes = getWfmOrganizationCodes();
		book.existsPostCodes = getWfmPostCodes();
		book.existUserCodes = getWfmUserCodes();

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

		return book;
	}

	/** マスタの存在チェック用のユーザコード一覧 */
	private Set<String> getWfmUserCodes() {
		return repository.getAllUserCodes(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/** マスタの存在チェック用の組織コード一覧 */
	private Set<String> getWfmOrganizationCodes() {
		return repository.getAllOrganizationCodes(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/** マスタの存在チェック用の役職コード一覧 */
	private Set<String> getWfmPostCodes() {
		SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());	// 自社のみ
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		return wf.searchWfmPost(in).getPostList()
			.stream()
			.map(p -> toKey(p.getCorporationCode(), p.getPostCode()))
			.collect(Collectors.toSet());
	}

	/** マスタの存在チェック用の企業コード一覧 */
	private Set<String> getWfmCorporationCodes() {
		SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
//		in.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());	// 企業コードの存在チェックをパスできるよう、全企業を抽出
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		return wf.searchWfmCorporation(in).getCorporations()
				.stream()
				.map(c -> toKey(c.getCorporationCode()))
				.collect(Collectors.toSet());
	}

	public static String toKey(Object...values) {
		StringBuilder sb = new StringBuilder(64);
		for (Object v : values) {
			if (sb.length() > 0) {
				sb.append(SEPARATOR);
			}
			sb.append(v);
		}
		return sb.toString();
	}

	/**
	 * テンプレートのダウンロード
	 * @return
	 */
	public Response download() {
		final LoginInfo u = sessionHolder.getLoginInfo();
		final String corporationCode = u.getCorporationCode();
		final String localeCode = u.getLocaleCode();
		final Up0200Book book = new Up0200Book();
		{
			// 組織
			book.sheetOrg = new Up0200SheetOrganization();
			book.sheetOrg.organizations = repository.getOrganizations(corporationCode, localeCode);
			// 役職
			book.sheetPost = new Up0200SheetPost();
			book.sheetPost.posts = repository.getPosts(corporationCode, localeCode);
			// ユーザ
			book.sheetUser = new Up0200SheetUser();
			book.sheetUser.users = repository.getUsers(corporationCode, localeCode);
			// ユーザ所属
			book.sheetUserBelong = new Up0200SheetUserBelong();
			book.sheetUserBelong.userBelongs = repository.getUserBelongs(corporationCode, localeCode);
		}
		final String fileName = MessageFormat.format("Profile_{0}_{1}.xlsx", corporationCode, toStr(today(), "yyyyMMdd"));
		final Up0200StreamingOutput streaming = new Up0200StreamingOutput(book);
		return DownloadUtils.download(fileName, streaming, ContentType.XLSX);
	}
}
