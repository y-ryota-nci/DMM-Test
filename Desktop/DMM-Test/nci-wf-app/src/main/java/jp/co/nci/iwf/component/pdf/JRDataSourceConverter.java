package jp.co.nci.iwf.component.pdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.util.BeanToMapConveter;
import jp.co.nci.iwf.util.MiscUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * リストや配列をJRDataSouceへ変換する
 */
public class JRDataSourceConverter extends MiscUtils {

	/**
	 * リストをiReport用のJRDataSourceへ変換
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> JRDataSource toDataSource(List<E> list) {
		if (list == null || list.isEmpty())
			return new JREmptyDataSource();

		// Mapのリストか？
		final E e = list.get(0);
		if (e instanceof Map)
			return new JRMapCollectionDataSource((Collection<Map<String, ?>>)list);

		// Beanはフィールド直読みか、getter/setter経由か
		final boolean isUseField = isUseField(e);

		// フィールド直読みはJRBeanCollectionが対応してないので、Mapリストに変換する
		if (isUseField) {
			final List<Map<String, ?>> maps = new ArrayList<>();
			for (E row : list) {
				Map<String, ?> map = BeanToMapConveter.fromFields(row);
				maps.add(map);
			}
			return new JRMapCollectionDataSource(maps);
		}
		return new JRBeanCollectionDataSource(list);
	}

	/**
	 * 配列をiReport用のJRDataSourceへ変換
	 * @param array
	 * @return
	 */
	public static <E> JRDataSource toDataSource(E[] array) {
		if (array == null || array.length == 0) {
			return new JREmptyDataSource();
		}

		// Mapの配列か？
		final E e = array[0];
		if (e instanceof Map)
			return new JRMapArrayDataSource(array);

		// Beanはフィールド直読みか、getter/setter経由か
		final boolean isUseField = isUseField(e);

		// フィールド直読みはJRBeanCollectionが対応してないので、Mapリストに変換する
		if (isUseField) {
			final List<Map<String, ?>> maps = new ArrayList<>();
			for (E row : array) {
				Map<String, ?> map = BeanToMapConveter.fromFields(row);
				maps.add(map);
			}
			return new JRMapCollectionDataSource(maps);
		}

		return new JRBeanArrayDataSource(array);
	}

}
