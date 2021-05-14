package jp.co.nci.iwf.endpoint.dc.dc0100;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BinderInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocWfRelationInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.UpdateHistoryInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.BaseContents;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.BlockDisplayEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessMemoInfo;

/**
 * 業務文書の登録・更新画面コンテンツ情報
 */
public class Dc0100Contents extends BaseContents {

	/**  */
	private static final long serialVersionUID = 1L;

	/** (文書情報の)バージョン */
	public Long version;
	/** コンテンツ種別(1:業務文書、2:バインダ) */
	public String contentsType;

//	/** 操作者(ログイン者)がロック中か 1：ロック中 */
//	// 文書情報が持つロック	フラグではないので注意
//	public String locked;

	/** ブロック情報 */
	public List<BlockDisplayEntity> blockList = new ArrayList<>();
	/** 文書情報 */
	public DocInfo docInfo;
//	/** 文書バージョン情報 */
//	public DocVersionInfo versionInfo;
	/** 業務文書情報 */
	public BizDocInfo bizDocInfo;
	/** バインダー情報 */
	public BinderInfo binderInfo;
//	/** 文書属性情報 */
//	public DocAttributeInfo attribute;
	/** 文書属性(拡張)情報 */
	public List<DocAttributeExInfo> attributeExs;
	/** 文書権限情報一覧 */
	public List<DocAccessibleInfo> accessibles;
	/** 文書ファイル一覧 */
	public List<DocFileInfo> docFiles;
	/** 文書更新履歴 */
	public List<UpdateHistoryInfo> updateLogs;
	/** メモ情報 */
	public List<ProcessMemoInfo> memoList;
	/** 添付ファイル情報 */
	public List<AttachFileDocInfo> attachFileDocs;
	/** 文書WF連携情報 */
	public List<DocWfRelationInfo> docWfRelations;

	/** バージョン更新区分の選択肢 */
	public List<OptionItem> updateVersionTypes;
	/** 文書フォルダの選択肢 */
	public List<OptionItem> folders;
	/** 保存期間区分の選択肢 */
	public List<OptionItem> retentionTermTypes;
	/** 権限設定用ロールの選択肢 */
	public List<OptionItem> roles;
	/** 文書属性(拡張)テンプレートの選択肢 */
	public List<OptionItem> metaTemplates;

}
