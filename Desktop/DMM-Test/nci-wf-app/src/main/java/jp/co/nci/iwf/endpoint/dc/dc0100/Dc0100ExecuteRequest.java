package jp.co.nci.iwf.endpoint.dc.dc0100;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BinderInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocVersionInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 業務文書の登録・更新画面の初期化リクエスト
 */
public class Dc0100ExecuteRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** ロード時の文書情報. */
	public Dc0100Contents contents;

	/** 文書情報 */
	public DocInfo docInfo;
	/** 文書バージョン情報 */
	public DocVersionInfo versionInfo;
	/** 業務文書情報 */
	public BizDocInfo bizDocInfo;
	/** バインダー情報 */
	public BinderInfo binderInfo;
//	/** 文書属性 */
//	public DocAttributeInfo attribute;
	/** 文書属性(拡張)情報一覧 */
	public List<DocAttributeExInfo> attributeExs;
	/** 文書権限情報一覧 */
	public List<DocAccessibleInfo> accessibles;
	/** 文書ファイル情報一覧 */
	public List<DocFileInfo> docFiles;
	/** 添付ファイル情報一覧 */
	public List<AttachFileDocInfo> attachFileDocs;

	/** 文書内容（HtmlIdをキーとした実行時パーツMap） */
	public Map<String, PartsBase<?>> runtimeMap = new LinkedHashMap<>();

	/** 文書管理→WFへの連携を行うか(1:WF連携を行う) */
	public String wfApplying;
	/** WF→文書管理への連携か */
	public String fromCoopWfFlag;
}
