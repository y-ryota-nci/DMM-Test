package jp.co.nci.iwf.designer.service.numbering;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.beanutils.BeanUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.cache.CacheHolder;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;

/**
 * パーツの採番サービス
 */
@BizLogic
public class PartsNumberingService extends BaseRepository {
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;
	@Inject private CacheManager cm;

	/** グループ化対象の書式区分 */
	private Set<String> targets;
	/** 連番仕様マスタのキャッシュ */
	private CacheHolder<Long, MwmPartsSequenceSpec> cacheSpec;
	/** 採番形式マスタのキャッシュ */
	private CacheHolder<Long, MwmPartsNumberingFormat> cacheFormat;

	@PostConstruct
	public void init() {
		targets = new HashSet<>(Arrays.asList("S", "T", "O"));
		cacheSpec = cm.newInstance(CacheInterval.EVERY_10SECONDS);
		cacheFormat = cm.newInstance(CacheInterval.EVERY_10SECONDS);
	}

	/**
	 * パーツ採番形式IDに従って採番を行い、その値を返す
	 * @param partsNumberingFormatId パーツ採番形式ID
	 * @return
	 */
	public String getNumber(long partsNumberingFormatId) {

		// 採番形式マスタ
		final MwmPartsNumberingFormat f = getPartsNumberingFormat(partsNumberingFormatId);

		// 採番形式マスタを書式区分単位でリスト化
		final List<NumberingFormatEntity> details = parse(f);

		// 採番区分を現在のシステム日付や操作者情報をコンテキストとして解釈し、採番値を生成
		final StringBuilder sb = new StringBuilder(64);
		for (NumberingFormatEntity d : details) {
			sb.append(toValue(d, details));
		}
		return sb.toString();
	}

	/**
	 * パーツ採番形式IDに紐付くパーツ採番仕様IDを抜き出す
	 * @param partsNumberingFormatId パーツ採番形式ID
	 * @return
	 */
	public Set<Long> getPartsNumberingSpecIds(long partsNumberingFormatId) {
		// 採番形式マスタ
		final MwmPartsNumberingFormat f = getPartsNumberingFormat(partsNumberingFormatId);
		// 採番形式マスタを書式区分単位でリスト化
		final List<NumberingFormatEntity> details = parse(f);

		final Set<Long> ids = new HashSet<>();
		for (NumberingFormatEntity d : details) {
			if (eq("S", d.formatType) && isNotEmpty(d.formatValue)) {	// S:連番
				ids.add( Long.valueOf(d.formatValue) );
			}
		}
		return ids;
	}

	/** 採番区分を現在のシステム日付や操作者情報をコンテキストとして解釈し、採番値を生成 */
	private String toValue(NumberingFormatEntity d, List<NumberingFormatEntity> details) {
		switch (d.formatType) {
		case "L":	// リテラル
			return d.formatValue;
		case "T":	// システム日付
			return toTimeFormat(d.formatValue);
		case "O":	// ログイン者組織
			return toOrgFormat(d.formatValue);
		case "U":	// ログイン者ユーザ
			return toUserFormat(d.formatValue);
		case "S":	// 連番
			return toSequence(d.formatValue, details);
		case "C":
			return toCorporationFormat(d.formatValue);
		}
		return "";
	}

	/** 採番形式を抽出 */
	private MwmPartsNumberingFormat getPartsNumberingFormat(long partsNumberingFormatId) {
		MwmPartsNumberingFormat f = cacheFormat.get(partsNumberingFormatId);
		if (f == null) {
			synchronized (cacheFormat) {
				f = cacheFormat.get(partsNumberingFormatId);
				if (f == null) {
					f = getMwmPartsNumberingFormat(partsNumberingFormatId);
					if (f != null) {
						cacheFormat.put(partsNumberingFormatId, f);
					}
				}
			}
		}
		return f;
	}

	/**
	 * 連番を採番
	 * @param formatValue 書式値
	 * @param details 有効な採番形式リスト
	 * @return 新たに採番された連番
	 */
	private String toSequence(String formatValue, List<NumberingFormatEntity> details) {
		long partsSequenceSpecId = Long.valueOf(formatValue);

		// グループ化対象の値を抜き出す
		final String groupingKey = details.stream()
			.filter(d -> d.groupingFlag && targets.contains(d.formatType))
			.map(d -> toValue(d, details))
			.collect(Collectors.joining("."));

		// 連番仕様マスタ
		final MwmPartsSequenceSpec spec = getSequenceSpec(partsSequenceSpecId);

		// 連番の取得
		final long newNumber = getNextSequence(spec, groupingKey);

		// 採番値の桁数上限チェック
		long max = (long)Math.pow(10, spec.getSequenceLength());
		if (max < newNumber)
			throw new InternalServerErrorException("採番値の桁数が上限を超えました。連番仕様ID=" + partsSequenceSpecId + " 採番値=" + newNumber + " 最大桁数=" + spec.getSequenceLength());

		// 0埋め
		final String f = "%0" + spec.getSequenceLength() + "d";
		return String.format(f, newNumber);
	}

	/**
	 * 【自律型トランザクション】連番仕様をもとにパーツ採番プロシージャを呼び出す
	 * @param partsSequenceSpecId パーツ採番仕様ID
	 * @param groupingKey グループ化キー
	 * @param spec 採番仕様マスタ
	 * @return 新たに採番された連番
	 */
	@Transactional
	private long getNextSequence(MwmPartsSequenceSpec spec, String groupingKey) {
		final String sql = getSql("CM0014");
		final Query q = em.createNativeQuery(sql);
		int i = 0;
		q.setParameter(++i, spec.getPartsSequenceSpecId());
		q.setParameter(++i, groupingKey);
		q.setParameter(++i, spec.getResetType());
		q.setParameter(++i, timestamp());
		q.setParameter(++i, spec.getStartValue());
		return ((Number)q.getSingleResult()).longValue();
	}

	/** 書式区分＝ユーザの採番形式文字列を返す */
	private String toUserFormat(String formatValue) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(login.getCorporationCode());
		in.setUserCode(login.getUserCode());
		final WfmUser user = wf.searchWfmUser(in).getUserList().get(0);

		String value = null;
		switch (formatValue) {
		case "NAME": value = user.getUserName(); break;
		case "SNAME": value = user.getUserNameAbbr(); break;
		case "ID": value = user.getUserAddedInfo(); break;
		case "EX1": value = user.getExtendedInfo01(); break;
		case "EX2": value = user.getExtendedInfo02(); break;
		case "EX3": value = user.getExtendedInfo03(); break;
		case "EX4": value = user.getExtendedInfo04(); break;
		case "EX5": value = user.getExtendedInfo05(); break;
		default: value = user.getUserAddedInfo(); break;
		}
		return defaults(value, "");
	}

	/** 書式区分＝組織の採番形式文字列を返す */
	private String toOrgFormat(String formatValue) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(login.getCorporationCode());
		in.setOrganizationCode(login.getOrganizationCode());
		final WfcOrganization org = wf.searchWfmOrganization(in).getOrganizationList().get(0);

		String value = null;
		switch (formatValue) {
		case "NAME": value = org.getOrganizationName(); break;
		case "SNAME": value = org.getOrganizationNameAbbr(); break;
		case "ID": value = org.getOrganizationAddedInfo(); break;
		case "EX1": value = org.getExtendedInfo01(); break;
		case "EX2": value = org.getExtendedInfo02(); break;
		case "EX3": value = org.getExtendedInfo03(); break;
		case "EX4": value = org.getExtendedInfo04(); break;
		case "EX5": value = org.getExtendedInfo05(); break;
		}
		return defaults(value, "");
	}

	/** 書式区分＝システム日付の採番形式文字列を返す */
	private String toTimeFormat(String formatValue) {
		final Date today = today();
		String value = null;
		switch (formatValue) {
		case "YYYY":	// 年(4桁)
			value = new SimpleDateFormat("yyyy").format(today);
			break;
		case "YY":		// 年(2桁)
			value = new SimpleDateFormat("yy").format(today);
			break;
		case "MM":		// 月
			value = new SimpleDateFormat("MM").format(today);
			break;
		case "DD":		// 日
			value = new SimpleDateFormat("dd").format(today);
			break;
		case "FYYYY":	// 年度(4桁)
			value = new SimpleDateFormat("yyyy").format(addMonth(today, -3));
			break;
		case "FYY":		// 年度(2桁)
			value = new SimpleDateFormat("yy").format(addMonth(today, -3));
			break;
		default:
			throw new IllegalArgumentException("解釈できない書式値です -> " + formatValue);
		}
		return defaults(value, "");
	}

	/** 書式区分＝会社コード(下2桁)の採番形式文字列を返す */
	private String toCorporationFormat(String formatValue) {
		if (isEmpty(formatValue) || formatValue.length() < 2) {
			throw new IllegalArgumentException("解釈できない書式値です -> " + formatValue);
		}
		int length = formatValue.length();
		return formatValue.substring(length - 2, length);
	}

	/** 採番形式マスタを書式区分単位でリスト化 */
	private List<NumberingFormatEntity> parse(MwmPartsNumberingFormat f) {
		final List<NumberingFormatEntity> list = new ArrayList<>();
		for (int i = 1; f != null && i < 10; i++) {
			NumberingFormatEntity e = new NumberingFormatEntity();
			e.formatType = getProperty(f, "formatType" + i);
			e.formatValue = getProperty(f, "formatValue" + i);
			e.groupingFlag = CommonFlag.ON.equals(getProperty(f, "groupingFlag" + i));
			// 会社コードの場合
			if ("C".equals(e.formatType)) {
				e.formatValue = f.getCorporationCode();
			}
			if (isNotEmpty(e.formatType)) {
				list.add(e);
			}
		}
		return list;
	}

	/** プロパティ値を取得 */
	private String getProperty(Object bean, String propName) {
		try {
			return (String)BeanUtils.getProperty(bean, propName);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 採番形式マスタを抽出
	 * @param partsNumberingFormatId
	 * @return
	 */
	private MwmPartsNumberingFormat getMwmPartsNumberingFormat(long partsNumberingFormatId) {
		return em.find(MwmPartsNumberingFormat.class, partsNumberingFormatId);
	}

	/** 連番仕様マスタを抽出 */
	private MwmPartsSequenceSpec getSequenceSpec(long partsSequenceSpecId) {
		MwmPartsSequenceSpec spec = cacheSpec.get(partsSequenceSpecId);
		if (spec == null) {
			synchronized (cacheSpec) {
				spec = cacheSpec.get(partsSequenceSpecId);
				if (spec == null) {
					spec = em.find(MwmPartsSequenceSpec.class, partsSequenceSpecId);
					if (spec == null)
						throw new NullPointerException("連番仕様マスタが見つかりません partsSequenceSpecId -> " + partsSequenceSpecId);
					cacheSpec.put(partsSequenceSpecId, spec);
				}
			}
		}
		return spec;
	}
}
