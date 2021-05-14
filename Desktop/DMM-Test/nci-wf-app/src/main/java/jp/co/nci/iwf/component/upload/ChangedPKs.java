package jp.co.nci.iwf.component.upload;

import java.util.HashMap;
import java.util.Map;

import jp.co.nci.iwf.component.upload.ReplacedPK;

public class ChangedPKs<E>
		extends HashMap<Long, ReplacedPK>
		implements Map<Long, ReplacedPK> {

	public Class<E> clazz;

	public ChangedPKs(Class<E> clazz) {
		this.clazz = clazz;
	}
}
