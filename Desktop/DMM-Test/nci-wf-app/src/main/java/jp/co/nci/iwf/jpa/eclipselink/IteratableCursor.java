package jp.co.nci.iwf.jpa.eclipselink;

import java.io.Closeable;

/**
 * Eclipselinkのカーソルに、autoCloseableを実装するためのインターフェース
 *
 * @param <T>
 */
public interface IteratableCursor<T> extends Iterable<T>, Closeable {
}
