package jp.co.nci.iwf.endpoint.vd.vd0310.entity;

import java.io.Serializable;
import java.util.Date;

import jp.co.nci.integrated_workflow.model.base.WftProcessAppended;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * プロセスに紐付くメモ情報
 */
public class ProcessMemoInfo implements Serializable {
	public String corporationCodeSubmit;
	public String userCodeSubmit;
	public String userNameSubmit;
	public String timestampSubmit;
	public String memo;
	public String labelNew;

	/** コンストラクタ */
	public ProcessMemoInfo() {
	}

	/** コンストラクタ */
	public ProcessMemoInfo(WftProcessAppended src) {
		timestampSubmit = MiscUtils.FORMATTER_DATETIME.get().format(src.getAppendTime());
		corporationCodeSubmit = src.getCorporationCodeAppended();
		userCodeSubmit = src.getUserCodeAppended();
		userNameSubmit = src.getUserNameAppended();
		memo = src.getMemorandom();

		// 過去48時間以内の投稿なら「NEW」ラベルを表示
		Date to = MiscUtils.now();
		Date from = MiscUtils.addHour(to, -48);
		if (MiscUtils.between(src.getAppendTime(), from, to)) {
			this.labelNew = "NEW";
		}
	}
}
