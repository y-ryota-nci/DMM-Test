package jp.co.dmm.customize.endpoint.po.po0050;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;

import com.gh.mygreen.xlsmapper.XlsMapperException;

import jp.co.dmm.customize.jpa.entity.mw.BumonMst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps1Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.Itmexps2Chrmst;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.dmm.customize.jpa.entity.mw.VTaxFgChg;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.DownloadUtils;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 通販データ取込画面サービス.
 */
@BizLogic
public class Po0050Service extends BaseService {

	@Inject private Po0050Repository repository;
	@Inject private Po0050ExcelReader reader;
	@Inject private Po0050Validator validator;
	@Inject private Po0050Register uploader;
	@Inject private Logger log;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Po0050InitResponse init(BaseRequest req) {
		final Po0050InitResponse res = createResponse(Po0050InitResponse.class, req);
		// 操作者が組織レベル3以上に所属していた場合、アップロード処理は不可とする
		if (isEmpty(sessionHolder.getLoginInfo().getOrganizationCodeUp3())) {
			res.addAlerts("組織レベル3以上のユーザは取込処理は行えません");
		} else {
			// システム日付の年月を処理対象月の初期値として設定する
			res.targetYm = MiscUtils.toStr(today(), "yyyy/MM");
			res.success = true;
		}
		return res;
	}

	/**
	 * 通販データのアップロード処理.
	 * @param targetYm 処理対象月（YYYY/MM形式）
	 * @param multiPart
	 * @return
	 */
	public Response upload(final String targetYm, FormDataMultiPart multiPart) {

		validateParams(targetYm, multiPart);

		// 会社コード
		final String companyCd = sessionHolder.getLoginInfo().getCorporationCode();

		// 戻り値生成
		final BaseResponse res = createResponse(BaseResponse.class, null);

		// システム日付に対する会計カレンダマスタの存在チェック
		// 存在しないマスタ不備として処理を終了する
		if (!repository.isExistsAccClndMst(companyCd)) {
			final String baseDate = toStr(today());
			res.addAlerts(baseDate + "の会計カレンダーが存在しないため、処理を続行できません。");
			return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
		}

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		// 発注No、検収Noの採番に使用するパーツ採番形式IDを取得する
		// もしIDが取得できなかった場合はシステムエラーにしてしまう
		final Long purordNoId = repository.getPartsNumberingFormatId(corporationCode, "PO");	//発注No用のパーツ採番形式ID
		final Long rcvinspNoId = repository.getPartsNumberingFormatId(corporationCode, "RI");	//検収No用のパーツ採番形式ID
		if (purordNoId == null) {
			throw new InternalServerErrorException("発注Noのパーツ採番形式IDが取得できません。パーツ採番コードを確認してください。");
		}
		if (rcvinspNoId == null) {
			throw new InternalServerErrorException("検収Noのパーツ採番形式IDが取得できません。パーツ採番コードを確認してください。");
		}

		// 新規_支払依頼申請(通常)の画面プロセス定義IDおよび起票アクティビティが持つ保存アクションのアクションコード
		final MwmScreenProcessDef screenProcessDef = getScreenProcessDef(corporationCode);
		if (screenProcessDef == null) {
			throw new InternalServerErrorException("新規_支払依頼申請の画面プロセス定義が取得できません。画面プロセス定義コードを確認してください。");
		}

		final UploadFile f = new UploadFile(multiPart.getBodyParts().get(0));
		try {
			// アップロードファイルをバイト配列化
			final byte[] xlsBytes = IOUtils.toByteArray(f.stream);

			try (ByteArrayInputStream bais = new ByteArrayInputStream(xlsBytes)) {
				// アップロードファイルをパースしてEXCELBook生成
				final Po0050Book book = parse(bais);

				// 取込データ数が5桁を超えていた場合、エラーとする
				if (book.mlordInfs.size() > 99999) {
					res.addAlerts("通販連携データの処理件数が10万件を超えています。");
					return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();
				}

				if (validator.validate(book)) {
					// ロック
					final WfmCorpPropMaster lock = uploader.lock();
					if (lock == null) {
						res.addAlerts("別のユーザが取込処理を行っております。しばらく待ってから再度実行してください。");
					} else {
						// 処理対象月からロットNoを生成（ロットNoは"K" + YYMM + 連番(2桁)）
						final String yymm = toStr(toDate(targetYm + "/01", FORMAT_DATE), "yyMM");
						final Long seqNo = repository.getLotSeqNo(companyCd, yymm);
						// 連番は2桁なので100以上であればエラーとして処理をとめる
						if (seqNo >= 100L) {
							res.addAlerts("入力した処理対象月に対するアップロード回数が100回以上となったため、これ以上の処理できません。");
						} else {
							try {
								final String lotNo = String.format("M%s%02d", yymm, seqNo);

								// 発注No・検収Noの採番形式IDをbookにセット
								book.purordNoId = purordNoId;
								book.rcvinspNoId = rcvinspNoId;
								// 新規_支払依頼申請の画面プロセス定義IDをセット
								book.screenProcessId = screenProcessDef.getScreenProcessId();
								// データ登録処理
								uploader.save(book, companyCd, lotNo);
							}
							finally {
								// ロック解除
								if (lock != null)
									uploader.unlock(lock);
							}
							res.addSuccesses(i18n.getText(MessageCd.MSG0151, "通販データ"));
						}
					}
					res.success = true;
					return Response.ok(res).type(MediaType.APPLICATION_JSON_TYPE).build();

				} else {
					// アップロード内容に不備ありなので、エラー内容がセットされたコンテンツをEXCELに書き戻す
					Po0050StreamingOutput streaming = new Po0050StreamingOutput(book);
					return DownloadUtils.download(f.fileName, streaming, ContentType.XLSX);
				}
			}
		}
		catch (XlsMapperException | NotOfficeXmlFileException e) {
			// ファイル形式が正しくありません
			throw new InvalidUserInputException(e, MessageCd.MSG0152, MessageCd.fileFormat);
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** アップロード時のパラメータのバリデーション */
	private void validateParams(final String targetYm, FormDataMultiPart multiPart) {
		if (isEmpty(targetYm)) {
			throw new BadRequestException("処理対象月が未指定です");
		}
		if (!ValidatorUtils.isYM(targetYm)) {
			throw new BadRequestException("処理対象月は年月形式(yyyy/mm)で指定してください");
		}
		if (multiPart == null || multiPart.getBodyParts().size() == 0) {
			throw new NotAcceptableException("アップロードファイルがありません");
		}
		// 操作者が組織レベル3以上に所属していた場合、アップロード処理は不可とする
		if (isEmpty(sessionHolder.getLoginInfo().getOrganizationCodeUp3())) {
			throw new NotAcceptableException("組織レベル3以上のユーザは取込処理は行えません");
		}
	}

	/**
	 * 新規_支払依頼申請の画面プロセス定義取得.
	 * ※ 画面プロセス定義取得時の画面プロセスコードは固定で書いている
	 *    枝番が上がった際のことは考慮していないのでどう取得するのがよいかは要検討
	 * @param corporationCode 企業コード
	 * @return 画面プロセスID
	 */
	private MwmScreenProcessDef getScreenProcessDef(String corporationCode) {
		// 画面プロセス定義コードは固定
		return repository.getMwmScreenProcessDef(corporationCode, "0000000055");
	}

	/** アップロード内容をパースしてExcelBookを生成 */
	private Po0050Book parse(InputStream stream) throws IOException {
		final long start = System.currentTimeMillis();
		log.debug("parse() START");

		// XlsMapperでストリームのパース
		final Po0050Book book = reader.parse(stream);

		// 各種コードの存在チェック用のコード値一覧を取得しておく
		// 部門についてはあっても1000件程度ということなので一括取得しておく（柚木さん情報）
		final List<BumonMst> bumonMstList = getBumonMstList();
		book.bumonCds = getBumonCds(bumonMstList);
		book.taxKndCdMap = getTaxKndCdMap(bumonMstList);
		book.itmExpsCds = getItmExpsCds();
		book.itmExpsChrCd = getItmExpsChrCds();
		// 消費税マスタより消費税コード、課税対象の消費税コードSetおよび消費税コード毎のマスタ一覧取得
		final List<TaxMst> taxMstList = getTaxMstList();
		book.taxCds = getTaxCds(taxMstList);
//		book.taxableCds = getTaxableCds(taxMstList);
		book.taxMap = getTaxMstMapByTaxCd(taxMstList);
		// 費目関連1マスタより伝票GL、経費区分のコードMap、仮払消費税のコードSet取得
		final List<Itmexps1Chrmst> itmexps1ChrmstList = getItmexps1ChrmstList();
		book.slpGrpGlMap = getSlpGrpGlMap(itmexps1ChrmstList);
		book.cstTpMap = getCstTpMap(itmexps1ChrmstList);
		book.accCdTaxSet = getAccCdTaxSet(itmexps1ChrmstList);
		book.taxSbjTpMap = getTaxSbjTpMap(itmexps1ChrmstList);
		// 消費税フラグビュー一覧取得
		book.taxFgChgMap = getTaxFgChgMap();

		// 取引先マスタ、振込先銀行口座マスタはチェック時にDBへ問い合わせる
		// 問い合わせた結果はキャッシュとして保持して、次回以降に再利用
		book.splrCds = new HashSet<>();
		book.payeeBnkaccCds = new HashSet<>();

		log.debug("parse() END --> {}msec", (System.currentTimeMillis() - start));

		return book;
	}

	/** 部門マスタ一覧の取得. */
	private List<BumonMst> getBumonMstList() {
		return repository.getBumonMstList(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/** 部門コード一覧取得. */
	private Set<String> getBumonCds(final List<BumonMst> list) {
		return list.stream().map(e -> e.getId().getBumonCd()).collect(Collectors.toSet());
	}

	/** 消費税種類コードMap取得. */
	private Map<String, String> getTaxKndCdMap(final List<BumonMst> list) {
		return list.stream()
					.filter(e -> isNotEmpty(e.getTaxKndCd()))
					.collect(Collectors.toMap(e -> e.getId().getBumonCd(), e -> e.getTaxKndCd()));
	}

	/** 消費税マスタ一覧取得. */
	private List<TaxMst> getTaxMstList() {
		return repository.getTaxMstList(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/** 消費税コード一覧取得. */
	private Set<String> getTaxCds(final List<TaxMst> list) {
		return list.stream().map(e -> e.getId().getTaxCd()).collect(Collectors.toSet());
	}

//	/** 課税対象となる消費税コード一覧取得. */
//	private Set<String> getTaxableCds(final List<TaxMst> list) {
//		return list.stream().filter(e -> compareTo(e.getTaxRto(), BigDecimal.ZERO) > 0).map(e -> e.getId().getTaxCd()).collect(Collectors.toSet());
//	}

	/** 消費税コード毎の消費税マスタ一覧取得. */
	private Map<String, List<TaxMst>> getTaxMstMapByTaxCd(final List<TaxMst> list) {
		return list.stream().collect(Collectors.groupingBy(e -> e.getId().getTaxCd(), Collectors.toList()));
	}

	/** 費目コード一覧取得. */
	private Set<String> getItmExpsCds() {
		return repository.getItmExpsCds(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/**
	 * 費目関連2マスタから費目コード(1)と費目コード(2)の組み合わせ一覧の取得.
	 * 費目コード(1)と費目コード(2)はハイフン(-)にて連結する
	 */
	private Set<String> getItmExpsChrCds() {
		final List<Itmexps2Chrmst> list =
				repository.getItmexps2ChrmstList(sessionHolder.getLoginInfo().getCorporationCode(), sessionHolder.getLoginInfo().getOrganizationCodeUp3());
		return list.stream().map(e -> String.join("-", e.getId().getItmexpsCd1(), e.getId().getItmexpsCd2())).collect(Collectors.toSet());
	}

	/** 費目関連1マスタ一覧取得. */
	private List<Itmexps1Chrmst> getItmexps1ChrmstList() {
		return repository.getItmexps1ChrmstList(sessionHolder.getLoginInfo().getCorporationCode());
	}

	/**
	 * 「1500：仮払消費税」となっている費目コード(1)と費目コード(2)の一覧取得.
	 * ※費目コード(1)と費目コード(2)をハイフン(-)にて連結したもの
	 */
	private Set<String> getAccCdTaxSet(final List<Itmexps1Chrmst> list) {
		return list.stream()
					.filter(e -> eq("1500", e.getAccCd()))
					.map(e -> String.join("-", e.getId().getItmexpsCd1(), e.getId().getItmexpsCd2()))
					.collect(Collectors.toSet());
	}

	/**
	 * 伝票グループ(GL)一覧取得.
	 * キーは費目コード(1)と費目コード(2)をハイフン(-)にて連結したもの
	 */
	private Map<String, String> getSlpGrpGlMap(final List<Itmexps1Chrmst> list) {
		return list.stream()
					.filter(e -> isNotEmpty(e.getSlpGrpGl()))
					.collect(Collectors.toMap(e -> String.join("-", e.getId().getItmexpsCd1(), e.getId().getItmexpsCd2()), e -> e.getSlpGrpGl()));
	}

	/**
	 * 経費区分一覧取得.
	 * キーは費目コード(1)と費目コード(2)をハイフン(-)にて連結したもの
	 */
	private Map<String, String> getCstTpMap(final List<Itmexps1Chrmst> list) {
		return list.stream()
					.filter(e -> isNotEmpty(e.getCstTp()))
					.collect(Collectors.toMap(e -> String.join("-", e.getId().getItmexpsCd1(), e.getId().getItmexpsCd2()), e -> e.getCstTp()));
	}

	/**
	 * 課税対象区分一覧取得.
	 * キーは費目コード(1)と費目コード(2)をハイフン(-)にて連結したもの
	 */
	private Map<String, String> getTaxSbjTpMap(final List<Itmexps1Chrmst> list) {
		return list.stream()
					.filter(e -> isNotEmpty(e.getTaxSbjTp()))
					.collect(Collectors.toMap(e -> String.join("-", e.getId().getItmexpsCd1(), e.getId().getItmexpsCd2()), e -> e.getTaxSbjTp()));
	}

	/**
	 * 消費税フラグビュー一覧取得.
	 * キーは課税対象区分と消費税種類コード、消費税フラグをハイフン(-)にて連結したもの
	 */
	private Map<String, VTaxFgChg> getTaxFgChgMap() {
		return repository.getVTaxFgChg(sessionHolder.getLoginInfo().getCorporationCode(), sessionHolder.getLoginInfo().getLocaleCode()).stream()
				.filter(e -> isNotEmpty(e.getTaxSbjTp()))
					.collect(Collectors.toMap(e -> String.join("-", e.getTaxSbjTp(), e.getTaxKndCd(), e.getTaxFgChg()), e -> e));
	}

}
