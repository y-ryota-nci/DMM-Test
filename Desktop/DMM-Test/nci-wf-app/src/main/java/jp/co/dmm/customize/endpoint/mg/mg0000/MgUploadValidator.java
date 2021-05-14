package jp.co.dmm.customize.endpoint.mg.mg0000;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 取引先情報アップロードのバリデーター
 */
public abstract class MgUploadValidator<T extends MgExcelBook<?>> extends MiscUtils {

	@Inject protected I18nService i18n;

	public boolean validate(T book) {
		int errors = 0;
		errors += validateMaster(book);

		return errors == 0;
	}

	protected abstract int validateMaster(T book);

	/**
	 * アップロードデータ内部＋DB中でも有効期限（整合性）チェックを行う
	 * @param code
	 * @param sqno
	 * @param vdDtS
	 * @param vdDtE
	 * @param book
	 * @param isUpdate
	 * @return
	 */
	protected boolean isValidPeriod(String code, long sqno, Date vdDtS, Date vdDtE, T book, boolean isUpdate) {

		if (book.existEntityPeriods.containsKey(code)) {
			for (MgMstCodePeriod entity : book.existEntityPeriods.get(code)) {
				// UPDATEの時、チェックのSQNOは対象外にする
				if (isUpdate && sqno == entity.sqno) {
					continue;
				}

				if ((vdDtE == null || entity.vdDtS.compareTo(vdDtE) <= 0) && (vdDtS == null || entity.vdDtE.compareTo(vdDtS) >= 0 )) {
					return false;
				}
			}
		} else {
			book.existEntityPeriods.put(code, new HashSet<MgMstCodePeriod>());
		}

		MgMstCodePeriod mst = new MgMstCodePeriod();
		mst.codeValue = code;
		mst.vdDtS = vdDtS == null ? book.VD_DT_S_DEF.getTime() : vdDtS;
		mst.vdDtE = vdDtE == null ? book.VD_DT_E_DEF.getTime() : vdDtE;
		book.existEntityPeriods.get(code).add(mst);

		return true;
	}

	/**
	 *
	 * @param name
	 * @param code
	 * @param sqno
	 * @param vdDtS
	 * @param vdDtE
	 * @param book
	 * @param processType
	 * @return
	 */
	protected String validatePeriod(String name, String code, long sqno, Date vdDtS, Date vdDtE, T book, String processType) {
		if (!isValidPeriod(code, sqno, vdDtS, vdDtE, book, "C".equals(processType))) {
			return name + "は有効期限内に含まれるものが既に登録されています。";
		}

		return "";
	}

	/**
	 * アップロード内部で自動チェック（sqno重複、有効期限整合性チェック）
	 * @param list
	 * @return
	 */
	public <E extends MgExcelPeriodEntity> String validateInternalUpload(List<E> list) {
		Set<Integer> sqnoSet = new HashSet<>();
		for (E entity  : list) {

			// 連番重複チェック
			if (!sqnoSet.contains(entity.cmSqno)) {
				//not exist
				sqnoSet.add(entity.cmSqno);
			} else {
				//exist: error
				return "同一コードにおいて連番が重複しています。";
			}

			// 有効期限整合性チェック
			List<E> remainList = list.stream().filter(o -> o.cmSqno != entity.cmSqno).collect( Collectors.toList() );
			for (E other : remainList) {
				if (
					other.cmVdDtS.compareTo(entity.cmVdDtE) <= 0 &&
					other.cmVdDtE.compareTo(entity.cmVdDtS) >= 0
				) {
					return "アップロードファイル内の同一コードにおいて有効期間が重複しています。";
				}
			}
		}

		return "";
	}

	/**
	 * あるEntityはlistの中にsqno重複、有効期限整合性チェック
	 * @param entity
	 * @param list
	 * @return
	 */
	public <E extends MgExcelPeriodEntity> int validateInternalUpload(E entity, List<E> list) {
		List<E> remainList = list.stream().filter(o -> (o.cmSqno != entity.cmSqno)).collect( Collectors.toList() );

		// 連番重複チェック
		if (remainList.size() < (list.size() - 1)) {
			entity.errorText = "アップロードデータ内の同一コードにおいて連番が重複しています。";
			return 1;
		}

		for (E other  : remainList) {

			// 有効期限整合性チェック
			if (in(entity.processType, "A", "U", "C") &&
				in(other.processType, "A", "U", "C") &&
				other.cmVdDtS.compareTo(entity.cmVdDtE) <= 0 &&
				other.cmVdDtE.compareTo(entity.cmVdDtS) >= 0
				){
				entity.errorText = "アップロードデータ内の同一コードにおいて有効期間が重複しています。";
				return 1;
			}
		}

		return 0;
	}
}
