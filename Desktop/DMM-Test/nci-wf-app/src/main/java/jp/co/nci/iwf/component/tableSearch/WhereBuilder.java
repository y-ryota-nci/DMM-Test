package jp.co.nci.iwf.component.tableSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.nci.iwf.component.CodeBook.ConditionKanaConvertType;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * Where句を生成するヘルパー
 */
public class WhereBuilder extends MiscUtils {
	private List<String> entries = new ArrayList<>();

	@Override
	public String toString() {
		if (entries.isEmpty())
			return "";

		return " where " + entries.stream()
			.collect(Collectors.joining(" and "));
	}

	/** colName is null */
	public WhereBuilder isNull(TableSearchConditionDef c) {
		entries.add(c.columnName + " is null");
		return this;
	}

	/** colName is not null */
	public WhereBuilder isNotNull(TableSearchConditionDef c) {
		entries.add(c.columnName + " is not null");
		return this;
	}

	/** colName = ? */
	public WhereBuilder eq(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") = TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") = TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") = TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " = ?");
		return this;
	}

	/** 前方一致。colName like ?||'%' escape '~' */
	public WhereBuilder likeForwardMatch(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") like (TO_FULL_WIDTH_HIRAGANA(?) ||'%') escape '~'"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") like (TO_FULL_WIDTH_KANA(?) ||'%') escape '~'"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") like (TO_SINGLE_WIDTH_KANA(?) ||'%') escape '~'"));
		else
			entries.add(c.columnName + " like ? escape '~'");
		return this;
	}

	/** colName like '%'||? escape '~' */
	public WhereBuilder likeBackwardMatch(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") like ('%' || TO_FULL_WIDTH_HIRAGANA(?)) escape '~'"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") like ('%' || TO_FULL_WIDTH_KANA(?)) escape '~'"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") like ('%' || TO_SINGLE_WIDTH_KANA(?)) escape '~'"));
		else
			entries.add(c.columnName + " like ? escape '~'");
		return this;
	}

	/** colName like '%'||? escape '~' */
	public WhereBuilder likePartialMatch(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") like ('%' || TO_FULL_WIDTH_HIRAGANA(?) || '%') escape '~'"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") like ('%' || TO_FULL_WIDTH_KANA(?) || '%') escape '~'"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") like ('%' || TO_SINGLE_WIDTH_KANA(?) || '%') escape '~'"));
		else
			entries.add(c.columnName + " like ? escape '~'");
		return this;
	}

	/** colName between ? and ? */
	public WhereBuilder between(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") between TO_FULL_WIDTH_HIRAGANA(?) and TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") between TO_FULL_WIDTH_KANA(?) and TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") between TO_SINGLE_WIDTH_KANA(?) and TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " between ? and ?");
		return this;
	}

	/** colName > ? */
	public WhereBuilder gt(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") > TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") > TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") > TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " > ?");
		return this;
	}

	/** colName >= ? */
	public WhereBuilder gte(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") >= TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") >= TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") >= TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " >= ?");
		return this;
	}

	/** 検索結果０件を強制する */
	public WhereBuilder forceZeroRecord() {
		entries.add("1 = 0");
		return this;
	}

	/** colName < ? */
	public WhereBuilder lt(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") < TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") < TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") < TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " < ?");
		return this;
	}

	/** colName <= ? */
	public WhereBuilder lte(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") <= TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") <= TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") <= TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " <= ?");
		return this;
	}

	/** colName != ? */
	public WhereBuilder notEq(TableSearchConditionDef c) {
		if (eq(ConditionKanaConvertType.FULL_WIDTH_HIRAGANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_HIRAGANA(", c.columnName, ") != TO_FULL_WIDTH_HIRAGANA(?)"));
		else if (eq(ConditionKanaConvertType.FULL_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_FULL_WIDTH_KANA(", c.columnName, ") != TO_FULL_WIDTH_KANA(?)"));
		else if (eq(ConditionKanaConvertType.SINGLE_WIDTH_KANA, c.conditionKanaConvertType))
			entries.add(join("TO_SINGLE_WIDTH_KANA(", c.columnName, ") != TO_SINGLE_WIDTH_KANA(?)"));
		else
			entries.add(c.columnName + " != ?");
		return this;
	}
}
