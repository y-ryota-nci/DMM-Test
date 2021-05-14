package jp.co.dmm.customize.endpoint.mg.mg0300;

import java.io.Serializable;
import java.util.List;

/**
 * アップロードされた住所マスタCSVファイル
 */
public class Mg0300CsvUpload implements Serializable {
	public List<Mg0300CsvEntity> listEntity;
}
