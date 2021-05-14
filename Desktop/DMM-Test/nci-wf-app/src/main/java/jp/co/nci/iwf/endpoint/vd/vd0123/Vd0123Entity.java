package jp.co.nci.iwf.endpoint.vd.vd0123;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.util.MiscUtils;

@Entity
@Access(AccessType.FIELD)
public class Vd0123Entity extends BaseJpaEntity implements Comparable<Vd0123Entity> {

	@Id
	@Column(name="JAVASCRIPT_ID")
	public Long javascriptId;

	@Column(name="FILE_NAME")
	public String fileName;

	@Column(name="REMARKS")
	public String remarks;

	@Transient
	public Integer sortOrder;
	@Transient
	public boolean selected;

	@Override
	public int compareTo(Vd0123Entity o) {
		if (o == null)
			return 1;

		int so1 = MiscUtils.defaults(sortOrder, Integer.MAX_VALUE);
		int so2 = MiscUtils.defaults(o.sortOrder, Integer.MAX_VALUE);
		int c = MiscUtils.compareTo(so1, so2);
		if (c != 0)
			return c;

		return MiscUtils.compareTo(fileName, o.fileName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vd0123Entity) {
			Vd0123Entity j = (Vd0123Entity)obj;
			return MiscUtils.eq(j.fileName, fileName)
					|| MiscUtils.eq(j.javascriptId, javascriptId);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(javascriptId, fileName);
	}
}
