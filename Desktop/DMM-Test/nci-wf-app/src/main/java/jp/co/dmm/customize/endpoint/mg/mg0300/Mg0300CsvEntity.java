package jp.co.dmm.customize.endpoint.mg.mg0300;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

public class Mg0300CsvEntity extends BaseJpaEntity {

	/** 都道府県コード */
	public String adrPrfCd;
	/** 郵便番号 */
	public String zipCd;
	/** 連番 */
	public int sqno;

	/** 都道府県 */
	public String adrPrf;
	/** 都道府県カナ */
	public String adrPrfKn;
	/** 市区町村 */
	public String adr1;
	/** 市区町村カナ */
	public String adr1Kn;
	/** 町名地番 */
	public String adr2;
	/** 町名地番カナ */
	public String adr2Kn;

}
