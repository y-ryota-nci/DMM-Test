package jp.co.nci.iwf.component.upload;

import java.util.HashMap;

public class ChangedPKsMap
		extends HashMap<Class<?>, ChangedPKs<?>> {

	public void put(ChangedPKs<?> changes) {
		this.put(changes.clazz, changes);
	}
}
