/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.nci.iwf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.lucene.util.IOUtils;

/**
 * Resource implementation for class path resources.
 * Uses either the thread context class loader, a given ClassLoader
 * or a given Class for loading resources.
 *
 * <p>Supports resolution as <code>java.io.File</code> if the class path
 * resource resides in the file system, but not for resources in a JAR.
 * Always supports resolution as URL.
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see java.lang.Thread#getContextClassLoader()
 * @see java.lang.ClassLoader#getResourceAsStream(String)
 * @see java.lang.Class#getResourceAsStream(String)
 */
public class ClassPathResource {

	private final String path;

	private ClassLoader classLoader;

	private Class<?> clazz;

	/**
	 * ファイル名等のリソース名を指定して、本クラスを生成します.
	 * 現在のThreadのClassLoaderからそのリソースを検索する
	 * ClassPathResourceを生成します。

	 * @param path クラスパスからのリソースに対するパス
	 */
	public ClassPathResource(String path) {
		this(path, (ClassLoader) null);
	}

	/**
	 * ファイル名等のリソース名を指定して、本クラスを生成します.
	 * 指定されたClassLoaderからそのリソースを検索する
	 * ClassPathResourceを生成します。

	 * @param path クラスパスからのリソースに対するパス
	 * @param classLoader 検索するクラスローダ
	 */
	public ClassPathResource(String path, ClassLoader classLoader) {
		if (path == null) {
			throw new IllegalArgumentException("Path must not be null");
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		this.path = cleanPath(path);
		this.classLoader = (classLoader != null ? classLoader : getDefaultClassLoader());
	}

//	/**
//	 * Create a new ClassPathResource for Class usage.
//	 * The path can be relative to the given class,
//	 * or absolute within the classpath via a leading slash.
//	 * @param path relative or absolute path within the class path
//	 * @param clazz the class to load resources with
//	 * @see java.lang.Class#getResourceAsStream
//	 */
//	public ClassPathResource(String path, Class clazz) {
//		if (path == null) {
//			throw new IllegalArgumentException("Path must not be null");
//		}
//		this.path = cleanPath(path);
//		this.clazz = clazz;
//	}
//
//	/**
//	 * Create a new ClassPathResource with optional ClassLoader and Class.
//	 * Only for internal usage.
//	 * @param path relative or absolute path within the classpath
//	 * @param classLoader the class loader to load the resource with, if any
//	 * @param clazz the class to load resources with, if any
//	 */
//	protected ClassPathResource(String path, ClassLoader classLoader, Class clazz) {
//		this.path = path;
//		this.classLoader = classLoader;
//		this.clazz = clazz;
//	}

	/**
	 * 指定されたリソース名を返却します.
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * 対象リソースの入力ストリームを返却します.
	 * @return InputStream　対象の入力ストリーム
	 * @throws IOException
	 * @see java.lang.ClassLoader#getResourceAsStream(String)
	 */
	public InputStream getInputStream() throws IOException {
		InputStream is = null;
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		} else {
			is = this.classLoader.getResourceAsStream(this.path);
		}
		if (is == null) {
			throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
		}
		return is;
	}

	/**
	 * 指定されたリソースのURLを返却します.
	 * @return URL　リソースのURL
	 * @throws IOException
	 * @see java.lang.ClassLoader#getResource(String)
	 */
	public URL getURL() throws IOException {
		URL url = null;
		if (this.clazz != null) {
			url = this.clazz.getResource(this.path);
		} else {
			url = this.classLoader.getResource(this.path);
		}
		if (url == null) {
			throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
		}
		return url;
	}

	/**
	 * 指定されたリソースをFileオブジェクトとしてハンドリングして返却します.
	 * @return リソースのFileオブジェクト

	 * @throws IOException
	 */
	public File getFile() throws IOException {
		return getFile(getURL(), getDescription());
	}

	/**
	 * 現行のClassPathResourceのpathの最後尾にrelativePathで指定された
	 * パスを追加した形のClassPathResourceを返却します。

	 * @param relativePath 現在のリソースパスからの相対パス
	 * @return ClassPathResource
	 */
	public ClassPathResource createRelative(String relativePath) {
		String pathToUse = applyRelativePath(this.path, relativePath);
		return new ClassPathResource(pathToUse, this.classLoader);
//		return new ClassPathResource(pathToUse, this.classLoader, this.clazz);
	}

	/**
	 * 指定されたリソース(path)のリソース名のみを返却します.
	 * @return String リソース名

	 */
	public String getFilename() {
		return getFilename(this.path);
	}

	private String getDescription() {
		return "指定されたリソース [" + this.path + "]";
	}

	/**
	 * 指定されたClassPathResourceが現在のClassPathResourceに等しいか判定します.
	 * 判定基準：

	 * 同一のインスタンスもしくは、同一の属性値を持つインスタンスは同じと判断する。

	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ClassPathResource) {
			ClassPathResource otherRes = (ClassPathResource) obj;
			return (this.path.equals(otherRes.path) && nullSafeEquals(this.classLoader, otherRes.classLoader) && nullSafeEquals(this.clazz, otherRes.clazz));
		}
		return false;
	}

	/**
	 * パスのハッシュコードを返却します.
	 * @return int ハッシュコード

	 */
	public int hashCode() {
		return this.path.hashCode();
	}

	/**
	 * Replace all occurences of a substring within a string with
	 * another string.
	 * @param inString String to examine
	 * @param oldPattern String to replace
	 * @param newPattern String to insert
	 * @return a String with the replacements
	 */
	private static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuilder sbuf = new StringBuilder();
		// output StringBuilder we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	/**
	 * Normalize the path by suppressing sequences like "path/.." and
	 * inner simple dots.
	 * <p>The result is convenient for path comparison. For other uses,
	 * notice that Windows separators ("\") are replaced by simple slashes.
	 * @param path the original path
	 * @return the normalized path
	 */
	private static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(pathArray[i])) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, pathArray[i]);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR, "", "");
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of potential
	 * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 */
	private static String[] delimitedListToStringArray(String str, String delimiter) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(str.substring(i, i + 1));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(str.substring(pos, delPos));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(str.substring(pos));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the Collection
	 * was <code>null</code> as well)
	 */
	private static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param coll Collection to display
	 * @param delim delimiter to use (probably a ",")
	 * @param prefix string to start each element with
	 * @param suffix string to end each element with
	 */
	private static String collectionToDelimitedString(Collection<String> coll, String delim, String prefix, String suffix) {
		if (isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Return <code>true</code> if the supplied Collection is <code>null</code>
	 * or empty. Otherwise, return <code>false</code>.
	 * @param collection the Collection to check
	 */
	private static boolean isEmpty(Collection<String> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * Determine if the given objects are equal, returning <code>true</code>
	 * if both are <code>null</code> or <code>false</code> if only one is
	 * <code>null</code>.
	 * <p>Compares arrays with <code>Arrays.equals</code>, performing an equality
	 * check based on the array elements rather than the array reference.
	 * @param o1 first Object to compare
	 * @param o2 second Object to compare
	 * @return whether the given objects are equal
	 * @see java.util.Arrays#equals
	 */
	private static boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		if (o1 instanceof Object[] && o2 instanceof Object[]) {
			return Arrays.equals((Object[]) o1, (Object[]) o2);
		}
		if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
			return Arrays.equals((boolean[]) o1, (boolean[]) o2);
		}
		if (o1 instanceof byte[] && o2 instanceof byte[]) {
			return Arrays.equals((byte[]) o1, (byte[]) o2);
		}
		if (o1 instanceof char[] && o2 instanceof char[]) {
			return Arrays.equals((char[]) o1, (char[]) o2);
		}
		if (o1 instanceof double[] && o2 instanceof double[]) {
			return Arrays.equals((double[]) o1, (double[]) o2);
		}
		if (o1 instanceof float[] && o2 instanceof float[]) {
			return Arrays.equals((float[]) o1, (float[]) o2);
		}
		if (o1 instanceof int[] && o2 instanceof int[]) {
			return Arrays.equals((int[]) o1, (int[]) o2);
		}
		if (o1 instanceof long[] && o2 instanceof long[]) {
			return Arrays.equals((long[]) o1, (long[]) o2);
		}
		if (o1 instanceof short[] && o2 instanceof short[]) {
			return Arrays.equals((short[]) o1, (short[]) o2);
		}
		return false;
	}

	/**
	 * Extract the filename from the given path,
	 * e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	private static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/** URL protocol for a file in the file system: "file" */
	public static final String URL_PROTOCOL_FILE = "file";
	/** URL protocol for a file in the file system: "zip" */
	private static final String URL_PROTOCOL_ZIP = "zip";

	/**
	 * Resolve the given resource URL to a <code>java.io.File</code>,
	 * i.e. to a file in the file system.
	 * @param resourceUrl the resource URL to resolve
	 * @param description a description of the original resource that
	 * the URL was created for (for example, a class path location)
	 * @return a corresponding File object
	 * @throws FileNotFoundException if the URL cannot be resolved to
	 * a file in the file system
	 */
	@SuppressWarnings("deprecation")
	private static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
		if (resourceUrl == null) {
			throw new IllegalArgumentException("Resource URL must not be null");
		}
		if (URL_PROTOCOL_ZIP.equals(resourceUrl.getProtocol())) {
			return new File(resourceUrl.getFile());
		}
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
			throw new FileNotFoundException(description + " cannot be resolved to absolute file path " + "because it does not reside in the file system: " + resourceUrl);
		}
		return new File(URLDecoder.decode(resourceUrl.getFile()));
	}

	/**
	 * Apply the given relative path to the given path,
	 * assuming standard Java folder separation (i.e. "/" separators);
	 * @param path the path to start from (usually a full file path)
	 * @param relativePath the relative path to apply
	 * (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	private static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * Return a default ClassLoader to use (never <code>null</code>).
	 * Returns the thread context ClassLoader, if available.
	 * The ClassLoader that loaded the ClassUtils class will be used as fallback.
	 * <p>Call this method if you intend to use the thread context ClassLoader
	 * in a scenario where you absolutely need a non-null ClassLoader reference:
	 * for example, for class path resource loading (but not necessarily for
	 * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
	 * reference as well).
	 * @see Thread#getContextClassLoader()
	 */
	private static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassPathResource.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * This implementation checks whether a File can be opened,
	 * falling back to whether an InputStream can be opened.
	 * This will cover both directories and content resources.
	 */
	public boolean exists() {
		// Try file existence: can we find the file in the file system?
		try {
			return getFile().exists();
		}
		catch (IOException ex) {
			// Fall back to stream existence: can we open the stream?
			try {
				InputStream is = getInputStream();
				is.close();
				return true;
			}
			catch (Throwable isEx) {
				return false;
			}
		}
	}

	/** コンテンツを「新たなテンポラリファイル」へコピーして返す */
	public File copyFile() throws IOException {
		return copyFile("NCIWF_");
	}

	/**
	 * コンテンツを「新たなテンポラリファイル」へコピーして返す
	 * @param prefix テンポラリファイルのファイル名プレフィックス、最低3文字以上が必要
	 */
	public File copyFile(String prefix) throws IOException {
		return copyFile(prefix, "tmp");
	}

	/**
	 * コンテンツを「新たなテンポラリファイル」へコピーして返す
	 * @param prefix テンポラリファイルのファイル名プレフィックス、最低3文字以上が必要
	 * @param suffix テンポラリファイルのファイル名の拡張子、省略時は "tmp"
	 *  */
	public File copyFile(String prefix, String suffix) throws IOException {
		if (!exists()) {
			throw new NotFoundException("テンプレートファイルがありません." + getPath());
		}
		final File work = File.createTempFile(suffix, suffix);
		IOUtils.copy(getFile(), work);
		return work;
	}
}
