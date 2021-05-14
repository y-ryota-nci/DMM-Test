package jp.co.nci.iwf.endpoint.mm.mm0121;

import java.sql.Date;
import java.util.List;

public class Mm0121Entity {
	public Long announcementId;
	public String corporationCode;
	public Date ymdStart;
	public String hhmmStart;
	public Date ymdEnd;
	public String hhmmEnd;
	public Long version;

	public List<Mm0121EntityDetail> locales;
}
