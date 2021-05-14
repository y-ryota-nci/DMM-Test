package jp.co.nci.iwf.endpoint.dc.dc0200;

import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocTrayType;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

public class Dc0200Entity extends BaseJpaEntity {
	public Long docTrayConfigPersonalizeId;
	public DocTrayType docTrayType;
	public String docTrayTypeName;
	public Long docTrayConfigId;
}
