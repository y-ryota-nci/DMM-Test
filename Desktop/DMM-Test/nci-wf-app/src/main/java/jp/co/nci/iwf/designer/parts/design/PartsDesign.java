package jp.co.nci.iwf.designer.parts.design;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jp.co.nci.iwf.designer.DesignerCodeBook.CalcItemType;
import jp.co.nci.iwf.designer.DesignerCodeBook.ItemClass;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsAttachFileEntity;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【デザイン時】パーツの基底クラス
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)	// クラスのFQCNをJSONフィールドに出力。クライアントから書き戻す際に必要
public abstract class PartsDesign
		extends MiscUtils
		implements Serializable, Comparable<PartsDesign> {
	protected Logger log = LoggerFactory.getLogger(getClass());

	/** パーツID(ユニークキー) */
	public long partsId = -1L;

	/** 背景色(入力時) */
	public String bgColorInput;
	/** 背景色(参照時) */
	public String bgColorRefer;
	/** 背景の透明フラグ */
	public boolean bgTransparentFlag;
	/** CSSクラス */
	public String cssClass;
	/** CSSスタイル */
	public String cssStyle;
	/** フォント太さ */
	public boolean fontBold;
	/** フォントカラー */
	public String fontColor;
	/** フォントサイズ */
	public Integer fontSize;
	/** レンダリング方法（0:Bootstrapのグリッドでレンダリング、1:インラインでレンダリング） */
	public int renderingMethod;
	/** 幅(大画面時) */
	public Integer colLg;
	/** 幅(中画面時) */
	public Integer colMd;
	/** 幅(小画面時) */
	public Integer colSm;
	/** 幅(最小画面時) */
	public Integer colXs;
	/** モバイル端末なら非表示フラグ */
	public boolean mobileInvisibleFlag;
	/** デフォルト値 */
	public String defaultValue;

	/** コピー起票対象フラグ */
	public boolean copyTargetFlag;
	/** 親コンテナID */
	public Long containerId;
	/** 説明 */
	public String description;
	/** 階層化されたパーツコード（親パーツコード_自パーツコード） */
	public String designCode;
	/** タブ順付与フラグ */
	public boolean grantTabIndexFlag;
	/** 表示ラベル */
	public String labelText;
	/** パーツコード */
	public String partsCode;
	/** パーツ種別 */
	public int partsType = -1;
	/** 親パーツID(親パーツがいる場合のみ、ルートコンテナに紐付く場合はNULL) */
	public Long parentPartsId;
	/** 必須フラグ */
	public boolean requiredFlag;
	/** 並び順（タブ順ではない） */
	public Integer sortOrder;
	/** バージョン */
	public Long version;
	/** 業務管理項目コード */
	public String businessInfoCode;
	/** 背景HTMLセル番号 */
	public Integer bgHtmlCellNo;
	/** 文書管理項目コード */
	public String docBusinessInfoCode;

	/** DBカラム定義 */
	public List<PartsColumn> columns = new ArrayList<>();
	/** 条件定義 */
	public List<PartsCond> partsConds = new ArrayList<>();
	/** パーツ関連定義 */
	public List<PartsRelation> relations = new ArrayList<>();
	/** 計算式定義 */
	public List<PartsCalc> partsCalcs = new ArrayList<>();
	/** パーツデザイン定義に紐付く添付ファイル */
	public List<PartsAttachFileEntity> attachFiles = new ArrayList<>();
	/** パーツイベント */
	public List<PartsEvent> events = new ArrayList<>();

	@Override
	public String toString() {
		return new StringBuilder(64)
				.append("[").append(designCode).append("]")
				.append(labelText)
				.toString();
	}

	//====================================================================
	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 * @param container コンテナ定義
	 * @param rowId
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public abstract PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx);

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@JsonIgnore
	public abstract String [] extFieldNames();

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 * @return
	 */
	@JsonIgnore
	public abstract List<PartsColumn> newColumns();


	/** パーツ更新の前処理 */
	public abstract void beforeSave();

	/** パーツ読込後の最終調整処理 */
	public abstract void afterLoad();
	//====================================================================

	/**
	 * 新規パーツを作成した場合の共通初期値を、パーツ定義から転写
	 * @param parts
	 * @param rowId
	 * @param ctx
	 */
	protected <P extends PartsBase<?>> void setPartsCommonValue(P parts, PartsContainerBase<?> parent, Integer rowId, DesignerContext ctx) {
		parts.partsId = partsId;
		parts.partsType = partsType;
		parts.htmlId = PartsUtils.toHtmlId(parts, parent, rowId, ctx);
	}

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	public void setInitValue() {}

	/** 比較 */
	@Override
	public int compareTo(PartsDesign o) {
		int compare = MiscUtils.compareTo(sortOrder, o.sortOrder);
		if (compare != 0)
			return compare;

		return MiscUtils.compareTo(o.partsId, o.partsId);
	}

	private static final Map<String, Field> fields = new ConcurrentHashMap<>();

	/**
	 * 指定フィールドの値を収集してJSON文字列化
	 * @param fieldNames 指定フィールド名リスト
	 * @return
	 */
	protected String toJsonFromFields() {
		if (fields.isEmpty() && extFieldNames() != null) {
			for (String name : extFieldNames()) {
				try {
					final Field field = getClass().getField(name);
					fields.putIfAbsent(name, field);
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		Map<String, Object> map = new HashMap<>(fields.size());
		for (Field f : fields.values()) {
			try {
				map.put(f.getName(), f.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return MiscUtils.toJsonFromObj(map);
	}

	/**
	 * パーツ拡張情報を反映
	 * @param ext パーツ拡張情報
	 */
	public void fillExtInfo(Map<String, Object> ext) {
		Class<?> clazz = getClass();
		for (String name : ext.keySet()) {
			Object value = ext.get(name);
			if (value instanceof Map)
				throw new NotImplementedException("パーツ拡張情報がネストされているめ、反映できません。この関数をオーバーライドした個別対応が必要です");
			final Field f;
			try {
				f = clazz.getField(name);
				setFieldValue(f, this, value);
			}
			catch (NoSuchFieldException e) {
				log.warn("保存されていたパーツ拡張情報を読み込もうとしましたが、該当するフィールドがこのパーツにありません -> {}.{}", getClass().getSimpleName(), name);
			}
//			catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
//				log.error(e.getMessage(), e);
//			}
			catch (Exception e) {
				throw new InternalServerErrorException(e);
			}
		}
	}

	private void setFieldValue(Field f, Object obj, Object value) throws IllegalAccessException, IllegalArgumentException {
		Class<?> type = f.getType();
		if (value == null) {
			if (type.isPrimitive())
				;	// プリミティブ値にnullなら、デフォルト値のままでよい
			else
				f.set(obj, value);
		}
		else {
			if (type == BigDecimal.class) {
				if (value instanceof Double)
					f.set(obj, new BigDecimal((Double)value));
				else if (value instanceof Integer)
					f.set(obj, new BigDecimal((Integer)value));
				else
					f.set(obj, new BigDecimal(value.toString()));
			}
			else if (value instanceof Number) {
				Number num = (Number)value;
				if (type == Integer.class)
					f.set(obj, num.intValue());
				else if (type == Long.class)
					f.set(obj, num.longValue());
				else if (type == Double.class)
					f.set(obj, num.doubleValue());
				else if (type == Short.class)
					f.set(obj, num.shortValue());
				else if (type == Byte.class)
					f.set(obj, num.byteValue());
				else if (type == Float.class)
					f.set(obj, num.floatValue());
				else
					f.set(obj, num);
			}
			else
				f.set(obj, value);
		}
	}

	/**
	 * 当パーツ内から指定パーツIDを使っているものがあれば、それを削除
	 * @param deletePartsId
	 */
	public void removePartsId(Long deletePartsId) {
		// パーツ関連情報
		for (Iterator<PartsRelation> it = relations.iterator(); it.hasNext(); ) {
			PartsRelation pr = it.next();
			if (MiscUtils.eq(deletePartsId, pr.targetPartsId))
				it.remove();
		}
		// 条件
		for (PartsCond pc : partsConds) {
			// 条件項目
			for (Iterator<PartsCondItem> it = pc.items.iterator(); it.hasNext(); ) {
				PartsCondItem ec = it.next();
				if (eq(ItemClass.PARTS, ec.itemClass)) {
					long partsId = Long.valueOf(ec.condType);
					if (eq(deletePartsId, partsId))
						it.remove();
				}
			}
		}
		// 計算式
		for (PartsCalc pc : partsCalcs) {
			// 計算式の有効条件
			for (Iterator<PartsCalcEc> it = pc.ecs.iterator(); it.hasNext(); ) {
				PartsCondItem ec = it.next();
				if (eq(ItemClass.PARTS, ec.itemClass)) {
					long partsId = Long.valueOf(ec.condType);
					if (eq(deletePartsId, partsId))
						it.remove();
				}
			}
			// 計算式項目
			for (Iterator<PartsCalcItem> it = pc.items.iterator(); it.hasNext(); ) {
				PartsCalcItem ec = it.next();
				if (isNotEmpty(ec.calcItemType)
						&& isNotEmpty(ec.calcItemValue)
						&& eq(CalcItemType.PARTS, ec.calcItemType)	// 種別＝パーツ
				) {
					Long targetPartsId = Long.valueOf(ec.calcItemValue);
					if (MiscUtils.eq(deletePartsId, targetPartsId))
						it.remove();
				}
			}
		}
	}
}
