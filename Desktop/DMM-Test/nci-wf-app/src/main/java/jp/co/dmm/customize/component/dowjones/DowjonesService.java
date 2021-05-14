package jp.co.dmm.customize.component.dowjones;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.co.dmm.customize.jpa.entity.mw.LndMst;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.util.ValidatorUtils;

@BizLogic
public class DowjonesService extends BaseService implements DowjonesCodeBook {

	private static final Logger log = LoggerFactory.getLogger(DowjonesService.class);

	@Inject private MwmLookupService lookup;
	@Inject private DowjonesRepository repository;

//	@PostConstruct
//	public void initialize() {
//		// プロキシ設定を取得
//		final boolean useProxy = corpProp.getBool(CorporationProperty.USE_PROXY, false);
//		if (!useProxy) {
//			return;
//		}
//		// プロキシユーザ名、パスワードを取得
//		final String proxyUsername = corpProp.getString(CorporationProperty.PROXY_USERNAME);
//		final String proxyPassword = corpProp.getString(CorporationProperty.PROXY_PASSWORD);
//		if (isNotEmpty(proxyUsername) && isNotEmpty(proxyPassword)) {
//			Authenticator authenticator = new Authenticator() {
//				public PasswordAuthentication getPasswordAuthentication() {
//					return (new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray()));
//				}
//			};
//			Authenticator.setDefault(authenticator);
//		}
//	}

	/**
	 * 実行URLを作成
	 * @param service API
	 * @return 実行URL
	 */
	private String createUrl(String service) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Map<String, MwmLookup> lookpus = lookup.getMap(localeCode, CorporationCodes.DMM_COM, LookupGroupId.DOWJONES_CONFIG);
		if (isEmpty(lookpus.get(DowjonesConfig.PROTOCOL)) || isEmpty(lookpus.get(DowjonesConfig.WEBSERVER))
				|| isEmpty(lookpus.get(DowjonesConfig.VERSION)) || isEmpty(service)) {
			throw new InvalidUserInputException("リクエストURLを作成できません。");
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(lookpus.get(DowjonesConfig.PROTOCOL).getLookupName()).append("://");
		builder.append(lookpus.get(DowjonesConfig.WEBSERVER).getLookupName()).append("/");
		builder.append(lookpus.get(DowjonesConfig.VERSION).getLookupName()).append("/");
		builder.append(service);
		return builder.toString();
	}

	/**
	 * リクエストからクエリパラメータを作成
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String createNameSearchQueryParameter(DowjonesRequest req) throws UnsupportedEncodingException {
		final StringBuilder builder = new StringBuilder();
		if (isNotEmpty(req.rltPrtNm)) {
			builder.append(builder.length() == 0 ? "" : "&");
			builder.append("name=").append(URLEncoder.encode(req.rltPrtNm, UTF8.toString()));
		}
		if (isNotEmpty(req.crpPrsTp)) {
			builder.append(builder.length() == 0 ? "" : "&");
			builder.append("record-type=").append(req.crpPrsTp);
		}
		if (isNotEmpty(req.brthDt)) {
			builder.append(builder.length() == 0 ? "" : "&");
			builder.append("date-of-birth=").append(req.brthDt);
		}
		if (isNotEmpty(req.lndCdDjii)) {
			builder.append(builder.length() == 0 ? "" : "&");
			builder.append("filter-region=").append(req.lndCdDjii);
		}
		builder.append(builder.length() == 0 ? "" : "&");
		builder.append("filter-pep=-ANY");
		builder.append(builder.length() == 0 ? "" : "&");
		builder.append("hits-to=100");

		return "?" + builder.toString();
	}

	/**
	 * プロキシ情報を作成
	 * @return プロキシ情報
	 */
	private Proxy createProxy() {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Map<String, MwmLookup> lookpus = lookup.getMap(localeCode, CorporationCodes.DMM_COM, LookupGroupId.PROXY_CONFIG);
		// プロキシ設定を取得
		final boolean use = lookpus.containsKey(ProxyConfig.USE) && toBool(lookpus.get(ProxyConfig.USE).getLookupName());
		if (!use) {
			return null;
		}
		final String protocol = lookpus.containsKey(ProxyConfig.PROTOCOL) ? lookpus.get(ProxyConfig.PROTOCOL).getLookupName() : null;
		if (isEmpty(protocol)) {
			throw new InvalidUserInputException("プロキシ情報が正しく設定されていません。");
		}
		final String proxyHostKey = eq(PROTOCOL.HTTP, protocol) ? ProxyConfig.HTTP_HOST : (eq(PROTOCOL.HTTPS, protocol) ? ProxyConfig.HTTPS_HOST : null);
		final String proxyPortKey = eq(PROTOCOL.HTTP, protocol) ? ProxyConfig.HTTP_PORT : (eq(PROTOCOL.HTTPS, protocol) ? ProxyConfig.HTTPS_PORT : null);
		if (isEmpty(proxyHostKey) || isEmpty(proxyPortKey)) {
			throw new InvalidUserInputException("プロキシ情報が正しく設定されていません。");
		}

		final String proxyHost = lookpus.containsKey(proxyHostKey) ? lookpus.get(proxyHostKey).getLookupName() : null;
		final String proxyPort = lookpus.containsKey(proxyPortKey) ? lookpus.get(proxyPortKey).getLookupName() : null;
		if (isEmpty(proxyHost) || isEmpty(proxyPort) || !ValidatorUtils.isNumeric(proxyPort)) {
			throw new InvalidUserInputException("プロキシ情報が正しく設定されていません。");
		}
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, toInt(proxyPort)));
	}

	private String createAuthorization() {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Map<String, MwmLookup> lookpus = lookup.getMap(localeCode, CorporationCodes.DMM_COM, LookupGroupId.DOWJONES_CONFIG);
		final String username = lookpus.containsKey(DowjonesConfig.USERNAME) ? lookpus.get(DowjonesConfig.USERNAME).getLookupName() : null;
		final String password = lookpus.containsKey(DowjonesConfig.PASSWORD) ? lookpus.get(DowjonesConfig.PASSWORD).getLookupName() : null;
		final String namespace = lookpus.containsKey(DowjonesConfig.NAMESPACE) ? lookpus.get(DowjonesConfig.NAMESPACE).getLookupName() : null;

		if (isEmpty(username) || isEmpty(password) || isEmpty(namespace)) {
			throw new InvalidUserInputException("Dow Jonesの認証情報を作成できません。");
		}
		final StringBuilder s = new StringBuilder();
		s.append(namespace);
		s.append("/");
		s.append(username);
		s.append(":");
		s.append(password);

		final StringBuilder result = new StringBuilder();
		result.append("Basic ");
		result.append(Base64.getEncoder().encodeToString(s.toString().getBytes(StandardCharsets.UTF_8)));

		return result.toString();
	}

	public DowjonesResponse executeNameSearch(DowjonesRequest req) {
		final DowjonesResponse res = createResponse(DowjonesResponse.class, req);
		HttpURLConnection con = null;
		try {
			final String requestURL = createUrl("search/name");
			final String queryParameter = createNameSearchQueryParameter(req);
			log.debug("実行URL={} 入力パラメータ={}", requestURL, queryParameter);

			final URL url = new URL(requestURL + queryParameter);
			final Proxy proxy = createProxy();
			if (isEmpty(proxy)) {
				con = (HttpURLConnection)url.openConnection();
			} else {
				con = (HttpURLConnection)url.openConnection(proxy);
			}
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", createAuthorization());

			con.connect();

			final int status = con.getResponseCode();
			final InputStream in;
			switch (status) {
			case HttpURLConnection.HTTP_OK:
				in = con.getInputStream();
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_NOT_FOUND:
				in = null;
				break;
			case HttpURLConnection.HTTP_FORBIDDEN:
				throw new InvalidUserInputException("Dow JonesのWebサービスを利用できません。");
			default:
				in = null;
				break;
			}

			if (isEmpty(in)) {
				res.success = false;
			} else {
				convertResponse(res, in);
				res.success = true;
			}
			return res;

		} catch (InvalidUserInputException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidUserInputException(e.getMessage());
		} finally {
			if (con != null) {
				// コネクションを切断
				con.disconnect();
			}
		}
	}

	/**
	 * レスポンスをエンティティにコンバート
	 * @param res
	 * @param in
	 */
	private void convertResponse(DowjonesResponse res, InputStream in) {
		final Map<String, List<LndMst>> lndMstMap = repository.select();
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(in);
			final Element element = doc.getDocumentElement();

			// 初期化
			res.totalHits = 0;
			res.matchs = new ArrayList<>();

			// head情報取得
			final NodeList heads = element.getElementsByTagName("head");
			if (heads.getLength() == 1) {
				final Element head = (Element)heads.item(0);
				final NodeList totalHits = head.getElementsByTagName("total-hits");
				if (totalHits.getLength() == 1) {
					res.totalHits = Integer.parseInt(totalHits.item(0).getTextContent());
				}
			}

			// 0件の他場合、処理終了
			if (res.totalHits == 0) {
				return;
			}

			// body情報取得
			final NodeList bodys = element.getElementsByTagName("body");
			if (bodys.getLength() != 1) {
				return;
			}

			final Element body = (Element)bodys.item(0);
			final NodeList matchs = body.getElementsByTagName("match");
			for (int i = 0; i < matchs.getLength(); i++) {
				final Element match = (Element)matchs.item(i);
				final String peid = match.getAttribute("peid");
				if (isEmpty(peid)) {
					continue;
				}

				final DowjonesEntity entity = new DowjonesEntity();
				res.matchs.add(entity);

				// プロファイルID
				entity.peid = peid;

				// レコードタイプ取得
				final String recordType = match.getAttribute("record-type");

				final NodeList payloads = match.getElementsByTagName("payload");
				if (payloads.getLength() == 0) {
					continue;
				}
				final Element payload = (Element)payloads.item(0);

				// 一致名称
				final NodeList matchedNames = payload.getElementsByTagName("matched-name");
				if (matchedNames.getLength() > 0) {
					entity.mtchNm = matchedNames.item(0).getTextContent();
				}

				// DJIIコード
				final NodeList countryCodes = payload.getElementsByTagName("country-code");
				if (countryCodes.getLength() > 0) {
					final String lndCdDjii = countryCodes.item(0).getTextContent();
					if (isNotEmpty(lndCdDjii) && lndMstMap.containsKey(lndCdDjii)) {
						final LndMst lndMst = lndMstMap.get(lndCdDjii).stream().findFirst().orElse(null);
						if (isNotEmpty(lndMst)) {
							entity.lndCd = lndMst.getId().getLndCd();
							entity.lndNm = lndMst.getLndNm();
							entity.lndCdDjii = lndCdDjii;
						}
					}
				}

				if (!"PERSON".equals(recordType)) {
					continue;
				}

				// 性別
				final NodeList genders = payload.getElementsByTagName("gender");
				if (genders.getLength() > 0) {
					entity.gndTp = genders.item(0).getTextContent();
				}

				// 誕生日
				final NodeList datesOfBirths = payload.getElementsByTagName("dates-of-birth");
				if (datesOfBirths.getLength() > 0) {
					final Element datesOfBirth = (Element)datesOfBirths.item(0);
					final NodeList dateOfBirths = datesOfBirth.getElementsByTagName("date-of-birth");
					final List<String> brthDts = new ArrayList<>();

					// 複数存在する可能性がある
					for (int j = 0; j < dateOfBirths.getLength(); j++) {
						final Element dateOfBirth = (Element)dateOfBirths.item(j);
						final List<String> birthdays = new ArrayList<>();

						// 年
						if (dateOfBirth.getElementsByTagName("year").getLength() > 0) {
							final String year = dateOfBirth.getElementsByTagName("year").item(0).getTextContent();
							if (isNotEmpty(year)) {
								birthdays.add(year);
							}
						}

						// 月
						if (dateOfBirth.getElementsByTagName("month").getLength() > 0) {
							final String month = dateOfBirth.getElementsByTagName("month").item(0).getTextContent();
							if (isNotEmpty(month)) {
								birthdays.add((month.length() == 1 ? "0" : "") + month);
							}
						}

						// 日
						if (dateOfBirth.getElementsByTagName("day").getLength() > 0) {
							final String day = dateOfBirth.getElementsByTagName("day").item(0).getTextContent();
							if (isNotEmpty(day)) {
								birthdays.add((day.length() == 1 ? "0" : "") + day);
							}
						}

						if (isNotEmpty(birthdays)) {
							brthDts.add(StringUtils.join(birthdays, "/"));
						}
					}

					if (isNotEmpty(brthDts)) {
						entity.brthDt = StringUtils.join(brthDts, ", ");
					}
				}
			}
		} catch (Exception e) {
			throw new InvalidUserInputException("レスポンスの変換処理でエラーが発生しました。");
		}
	}
}
