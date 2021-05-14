package jp.co.nci.iwf.util;

/**
 * 範囲を表現するクラス
 *
 * @param <T> 範囲のfrom/toを示す型
 */
public class Range <T extends Comparable<T>> {
	/** 開始 */
	public T from;
	/** 終了 */
	public T to;

	/** コンストラクタ */
	public Range(T from, T to) {
		this.from = from;
		this.to = to;
	}

	public Range<T> clone() {
		Range<T> clone = new Range<T>(this.from, this.to);
		return clone;
	}

	@Override
	public String toString() {
		return new StringBuilder("from=")
				.append(from)
				.append(" to=")
				.append(to)
				.toString();
	}
}
