package jp.co.nci.iwf.component.route.download;

public interface ProcessDefDownloadUtils {


	/** エンティティのクラス名からテーブル名を取得 */
	public static String getTableName(Object object) {
		Class<?> clazz = object.getClass();
		return getTableName(clazz);
	}

	/** エンティティのクラスからテーブル名を取得 */
	public static String getTableName(Class<?> clazz) {
		String name = clazz.getSimpleName();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (i != 0 && Character.isUpperCase(c)) {
				sb.append('_');
			}
			sb.append(Character.toUpperCase(c));
		}
		return sb.toString();
	}
}
