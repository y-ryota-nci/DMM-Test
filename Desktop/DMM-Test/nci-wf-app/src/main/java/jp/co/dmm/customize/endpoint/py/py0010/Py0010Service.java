package jp.co.dmm.customize.endpoint.py.py0010;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAcceptableException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import jp.co.dmm.customize.jpa.entity.mw.CrdcrdInf;
import jp.co.dmm.customize.jpa.entity.mw.CrdcrdInfPK;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.service.numbering.PartsNumberingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * クレカ明細データ取込画面サービス.
 */
@BizLogic
public class Py0010Service extends BaseService {

	@Inject private Py0010Repository repository;
	/** パーツの採番サービス */
	@Inject private PartsNumberingService partsNumberingService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Py0010InitResponse init(BaseRequest req) {
		final Py0010InitResponse res = createResponse(Py0010InitResponse.class, req);
		// 操作者が組織レベル3以上に所属していた場合、アップロード処理は不可とする
		if (isEmpty(sessionHolder.getLoginInfo().getOrganizationCodeUp3())) {
			res.addAlerts("組織レベル3以上のユーザは取込処理は行えません");
		} else {
			// システム日付の年月を処理対象月の初期値として設定する
			res.payYm = MiscUtils.toStr(today(), "yyyy/MM");
			res.success = true;
		}
		return res;
	}

	/**
	 * クレカ明細データのアップロード処理.
	 * @param targetYm 支払月（YYYY/MM形式）
	 * @param multiPart
	 * @return
	 */
	public BaseResponse upload(final String targetYm, FormDataMultiPart multiPart) {

		validateParams(targetYm, multiPart);

		final Set<String> splrMstKeys = new HashSet<>();
		final Set<String> userKeys = new HashSet<>();
		final Set<String> crdcrdBnkaccMstKeys = new HashSet<>();

		final List<String> alerts = new ArrayList<>();
		final List<String> successes = new ArrayList<>();
		for (BodyPart bodyPart : multiPart.getBodyParts()) {
			// ファイル以外はチェックしない
			if (!"file".equals(bodyPart.getContentDisposition().getParameters().get("name"))) {
				continue;
			}

			final UploadFile f = new UploadFile(bodyPart);

			final String fileName = f.fileName;
			// check file extension
			if (StringUtils.isEmpty(fileName) || !fileName.trim().endsWith(".csv")) {
				alerts.add(String.format("CSVファイルのみアップロード可能です。[%s]", fileName));
				continue;
			}

			try (Reader in = new InputStreamReader(f.stream, MS932)) {
				Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
				List<CrdcrdInf> inputs = new ArrayList<>();

				int line = 0;
				for (CSVRecord record : records) {
					line++;
					final CrdcrdInf entity = new CrdcrdInf();
					try {
						entity.setId(new CrdcrdInfPK());
						entity.setCrdCompanyNm(trim(record.get(0)));
						entity.getId().setCompanyCd(trim(record.get(1)));
						entity.setPayYm(targetYm.replaceAll("/", ""));
						entity.setSplrCd(trim(record.get(2)));
						entity.setUsrCd(trim(record.get(3)));
						entity.setUseDt(toDate(trim(record.get(4)), FORMAT_DATE));
						entity.setAmtJpy(toBD(trim(record.get(5))));
						entity.setUse(trim(record.get(6)));
						entity.setAmtFc(trim(record.get(7)));
						entity.setMatSts(CommonFlag.OFF);
						entity.setUseDtlItm1(trim(record.get(8)));
						entity.setUseDtlItm2(trim(record.get(9)));
						entity.setUseDtlItm3(trim(record.get(10)));
						entity.setUseDtlItm4(trim(record.get(11)));
						entity.setUseDtlItm5(trim(record.get(12)));
						entity.setUseDtlItm6(trim(record.get(13)));
						entity.setUseDtlItm7(trim(record.get(14)));
						entity.setUseDtlItm8(trim(record.get(15)));
						entity.setUseDtlItm9(trim(record.get(16)));
						entity.setUseDtlItm10(trim(record.get(17)));
						entity.setUseDtlItm11(trim(record.get(18)));
						entity.setUseDtlItm12(trim(record.get(19)));
						entity.setUseDtlItm13(trim(record.get(20)));
						entity.setUseDtlItm14(trim(record.get(21)));
						entity.setUseDtlItm15(trim(record.get(22)));
						entity.setUseDtlItm16(trim(record.get(23)));
						entity.setUseDtlItm17(trim(record.get(24)));
						entity.setUseDtlItm18(trim(record.get(25)));
						entity.setUseDtlItm19(trim(record.get(26)));
						entity.setUseDtlItm20(trim(record.get(27)));
						entity.setUseDtlItm21(trim(record.get(28)));
						entity.setUseDtlItm22(trim(record.get(29)));
						entity.setUseDtlItm23(trim(record.get(30)));
						entity.setUseDtlItm24(trim(record.get(31)));
						entity.setUseDtlItm25(trim(record.get(32)));
						entity.setUseDtlItm26(trim(record.get(33)));
					} catch (Exception e) {
						alerts.add(String.format("CSVファイルのデータにエラーがあるため取り込むことが出来ませんでした。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					}

					final String companyCd = entity.getId().getCompanyCd();
					final String splrCd = entity.getSplrCd();
					final String usrCd = entity.getUsrCd();
					if (isEmpty(companyCd)) {
						alerts.add(String.format("会社コードが設定されていません。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					} else if (isEmpty(splrCd)) {
						alerts.add(String.format("取引先コードが設定されていません。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					} else if (isEmpty(usrCd)) {
						alerts.add(String.format("ユーザコードが設定されていません。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					}

					if (!splrMstKeys.contains(String.format("%s|%s", companyCd, splrCd))
							&& !repository.existsSplrMst(companyCd, splrCd)) {
						alerts.add(String.format("取引先マスタに登録されていないデータがあるため取り込むことが出来ませんでした。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					} else {
						splrMstKeys.add(String.format("%s|%s", companyCd, splrCd));
					}

					if (!userKeys.contains(String.format("%s|%s", companyCd, usrCd))
							&& !repository.existsUser(companyCd, usrCd)) {
						alerts.add(String.format("ユーザマスタに登録されていないデータがあるため取り込むことが出来ませんでした。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					} else {
						userKeys.add(String.format("%s|%s", companyCd, usrCd));
					}

					if (!crdcrdBnkaccMstKeys.contains(String.format("%s|%s|%s", companyCd, splrCd, usrCd))
							&& !repository.existsCrdcrdBnkaccMst(companyCd, splrCd, usrCd)) {
						alerts.add(String.format("クレカ口座マスタに登録されていないデータがあるため取り込むことが出来ませんでした。[%s(%d行目)]", fileName, line));
						throw new NotAcceptableException();
					} else {
						crdcrdBnkaccMstKeys.add(String.format("%s|%s|%s", companyCd, splrCd, usrCd));
					}
					inputs.add(entity);
				}

				if (isEmpty(inputs)) {
					alerts.add(String.format("CSVファイルに登録データが存在しません。[%s]", fileName));
					continue;
				}
				save(inputs);
				successes.add(String.format("CSVファイルを取込みました。[%s]", fileName));
			} catch (NotAcceptableException e) {
				continue;
			} catch (Exception e) {
				alerts.add(String.format("CSVファイルの取込みに失敗しました。[%s]", fileName));
			}

		}

		final BaseResponse res = createResponse(BaseResponse.class, null);
		if (isNotEmpty(successes)) {
			res.successes.addAll(successes);
		}
		if (isNotEmpty(alerts)) {
			res.alerts.addAll(alerts);
		}
		res.success = isNotEmpty(res.successes);

		// 戻り値生成
		return res;
	}

	/** アップロード時のパラメータのバリデーション */
	private void validateParams(final String targetYm, FormDataMultiPart multiPart) {
		if (isEmpty(targetYm)) {
			throw new BadRequestException("支払月が未指定です");
		}
		if (!ValidatorUtils.isYM(targetYm)) {
			throw new BadRequestException("支払月は年月形式(yyyy/mm)で指定してください");
		}
		if (multiPart == null || multiPart.getBodyParts().size() == 0) {
			throw new NotAcceptableException("アップロードファイルがありません");
		}
		// 操作者が組織レベル3以上に所属していた場合、アップロード処理は不可とする
		if (isEmpty(sessionHolder.getLoginInfo().getOrganizationCodeUp3())) {
			throw new NotAcceptableException("組織レベル3以上のユーザは取込処理は行えません");
		}
	}

	@Transactional
	private void save(List<CrdcrdInf> inputs) {
		for (CrdcrdInf entity : inputs) {
			final Long partsNumberingFormatId = repository.getPartsNumberingFormatId(entity.getId().getCompanyCd(), "CD");
			entity.getId().setCrdcrdInNo(partsNumberingService.getNumber(partsNumberingFormatId));
			repository.save(entity);
		}

	}

}
