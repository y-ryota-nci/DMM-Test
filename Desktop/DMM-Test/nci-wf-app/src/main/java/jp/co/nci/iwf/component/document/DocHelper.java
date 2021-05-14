package jp.co.nci.iwf.component.document;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderAccessibleInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAccessibleInfo;
import jp.co.nci.iwf.util.MiscUtils;


@ApplicationScoped
public class DocHelper extends MiscUtils implements CodeBook, DcCodeBook {

	/**
	 * 文書権限および文書フォルダ権限のハッシュ値を計算して返す
	 * @param login ログイン者情報
	 * @return
	 */
	public Set<String> toHashValues(LoginInfo login) {
		final String corporationCode = login.getCorporationCode();
		final Set<String> values = new HashSet<>();

		// ログイン者のユーザコードによるハッシュ値生成
		values.add( toHashValue(AuthBelongType.USER, corporationCode, null, login.getUserCode()));

		// ログイン者が保持している文書管理用の参加者ロールコードからハッシュ値生成
		if (login.getDocAssignRoleCodes() != null) {
			login.getDocAssignRoleCodes().forEach(assignRoleCode -> {
				values.add( toHashValue(AuthBelongType.ROLE, corporationCode, assignRoleCode, null));
			});
		}

		return values;
	}

	/**
	 * 文書フォルダ権限情報.ハッシュ値を計算して返す
	 * @param acc 文書フォルダ権限情報
	 * @return
	 */
	public String toHashValue(MwmDocFolderAccessibleInfo acc) {
		return toHashValue(acc.getBelongType(), acc.getCorporationCode(), acc.getAssignRoleCode(), acc.getUserCode());
	}

	/**
	 * 文書権限情報.ハッシュ値を計算して返す
	 * @param acc 文書権限情報
	 * @return
	 */
	public String toHashValue(MwtDocAccessibleInfo acc) {
		return toHashValue(acc.getBelongType(), acc.getCorporationCode(), acc.getAssignRoleCode(), acc.getUserCode());
	}

	/**
	 * 文書フォルダ権限情報.ハッシュ値を計算して返す
	 * @param belongType 所属区分
	 * @param corporationCode 企業コード
	 * @param roleCode ロールコード
	 * @param userCode ユーザコード
	 * @return
	 */
	public String toHashValue(String belongType, String corporationCode, String roleCode, String userCode) {
		return String.format("%s@%s|%s|%s", S(belongType), S(corporationCode), S(roleCode), S(userCode));
	}

	/** nullを空文字にする */
	private static String S(String s) {
		return s == null ? "" : s;
	}

}
