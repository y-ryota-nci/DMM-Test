package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.vd.vd0310.BaseContents;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パーツに対するリクエスト
 */
public class Bl0002PartsRequest extends BaseRequest {
	/** コンテンツ */
	public BaseContents contents;

	/** 対象パーツのHtmlId */
	public String htmlId;
	/** 複数行が対象の場合のプレフィックスHtmlId */
	public Set<String> rowHtmlIds;
	/** HtmlIdをキーとした実行時パーツMap */
	public Map<String, PartsBase<?>> runtimeMap = new LinkedHashMap<>();
	/** 新しい行数 */
	public Integer newLineCount;
	/** 新しいページ番号 */
	public Integer newPageNo;
	/** デザイナーコンテキスト（プレビュー時のみ） */
	public DesignerContext previewContext;
	/** （削除する）ワークフローパーツ添付ファイルIDリスト */
	public Set<Long> partsAttachFileWfIds;
	/** 独立画面としてレンダリングを行うか */
	public boolean renderAsStandAlone;
}
