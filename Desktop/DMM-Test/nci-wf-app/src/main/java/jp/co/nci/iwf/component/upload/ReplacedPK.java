package jp.co.nci.iwf.component.upload;

/**
 * PKが置換されたエンティティを記録するクラス
 */
public class ReplacedPK {
	public Class<?> clazz;
	public long oldPK;
	public long newPK;

	public ReplacedPK(Class<?> clazz, long oldPK, long newPK) {
		this.clazz = clazz;
		this.oldPK = oldPK;
		this.newPK = newPK;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(clazz.getSimpleName())
				.append("@{oldPK=").append(oldPK).append(", newPK=").append(newPK).append("}")
				.toString();
	}
}
