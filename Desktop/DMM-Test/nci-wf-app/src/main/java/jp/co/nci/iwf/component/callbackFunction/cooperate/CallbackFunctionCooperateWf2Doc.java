package jp.co.nci.iwf.component.callbackFunction.cooperate;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.component.document.BizDocService;
import jp.co.nci.iwf.component.document.DocAccessibleService;
import jp.co.nci.iwf.component.document.DocFileDataService;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.ScreenLoadRepository;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100Contents;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100Service;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.AttachFileDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.BizDocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAccessibleInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocVersionInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0003Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ワークフローから文書管理への連携コールバックファンクション.
 */
public class CallbackFunctionCooperateWf2Doc extends BaseCallbackFunction {

	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		// この操作は状態遷移処理か
		if (!(result instanceof MoveActivityInstanceOutParam))
			return;

		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess process = out.getProcess();

		// アクション機能定義からパラメータを取得
		String parameter01 = functionDef.getFunctionParameter01();				// 文書管理側への登録方式 0:新規登録 1:更新(バージョンアップなし) 2:更新(マイナーバージョンアップ) 3:更新(メジャーバージョンアップ)
		final String parameter02 = functionDef.getFunctionParameter02();		// 新規登録時に使用する画面文書定義マスタの画面文書定義コード（ワークフローで使用している画面と一致している必要あり）
		final String parameter03 = functionDef.getFunctionParameter03();		// 新規登録時、文書情報を格納する文書フォルダのフォルダコード（未入力の場合は画面文書定義マスタの設定値を使用）
//		final String parameter04 = functionDef.getFunctionParameter04();		// 未使用
//		final String parameter05 = functionDef.getFunctionParameter05();		// 未使用

		if (!MiscUtils.in(parameter01, "0", "1", "2", "3")) {
			throw new InternalServerErrorException("文書管理側への登録方式に誤りがあります。アクション機能定義の第一パラメータの設定値を確認してください。");
		}

		final Long screenDocId;
		final Long docFolderId;
		final Long docId;
		if (eq(parameter01, "0")) {
			// 申請内において既に文書管理に連携されている場合、更新(バージョンアップなし)を行う
			if (isNotEmpty(process.getDocId())) {
				screenDocId = null;
				docFolderId = null;
				docId = process.getDocId();
				// バージョンアップなしで更新するためparameter01を書き換える
				parameter01 = "1";

			} else {
				if (isEmpty(parameter02)) {
					throw new InternalServerErrorException("連携先の画面文書定義が未指定です。アクション機能定義の第ニパラメータの設定値を確認してください。");
				} else {
					// 画面文書定義マスタ
					final BizDocService bizDocService = get(BizDocService.class);
					final MwvScreenDocDef screenDocDef = bizDocService.getMwvScreenDocDef(process.getCorporationCode(), parameter02, LoginInfo.get().getLocaleCode());
					if (screenDocDef == null) {
						throw new InternalServerErrorException("有効な画面文書定義がありません。");
					}
					// 画面IDが異なる場合、同一のコンテナでなければエラー
					else if (!eq(screenDocDef.screenId, contents.screenId)) {
						final ScreenLoadRepository repository = get(ScreenLoadRepository.class);
						final MwmScreen screen1 = repository.get(screenDocDef.screenId, LoginInfo.get().getLocaleCode());
						final MwmScreen screen2 = repository.get(contents.screenId, LoginInfo.get().getLocaleCode());
						if (isEmpty(screen1) || isEmpty(screen2) || !eq(screen1.getContainerId(), screen2.getContainerId())) {
							throw new InternalServerErrorException("本申請書と連携先に指定された画面文書定義とは異なる画面が設定されているため連携できません。");
						}
					}

					// 文書フォルダ
					// 第三パラメータの値を元に文書フォルダ情報を取得
					// TODO 文書フォルダに対するアクセス権限は無視してもよいか要確認
					final DocFolderService docFolderService = get(DocFolderService.class);
					final MwvDocFolder docFolder = docFolderService.getMwvDocFolderByFolderCode(process.getCorporationCode(), parameter03);
					if (docFolder == null && isEmpty(screenDocDef.docFolderId)) {
						throw new InternalServerErrorException("文書情報を保存するフォルダ情報が未設定です。");
					}

					screenDocId = screenDocDef.screenDocId;
					docFolderId = docFolder != null ? docFolder.getDocFolderId() : screenDocDef.docFolderId;
					docId = null;
				}
			}
		} else {
			screenDocId = null;
			docFolderId = null;
			docId = contents.relatedDocId;
		}

		// Dc0100Service#initを呼び出してDc0100InitResponseを取得
		final Dc0100Service service = get(Dc0100Service.class);
		final Dc0100InitRequest req = new Dc0100InitRequest();
		req.corporationCode = process.getCorporationCode();
		req.screenDocId = screenDocId;
		req.docId = docId;
		final Dc0100InitResponse res = service.init(req);
		// 次にDc0100Service#executeを呼び出して文書情報等の新規登録を行う
		final Dc0100ExecuteRequest req2 = new Dc0100ExecuteRequest();
		// 連携の場合、WF側のプロセスIDをそのまま使用
		res.contents.processId = process.getProcessId();
		req2.contents = res.contents;
		req2.docInfo = createDocInfo(res.contents, process, docFolderId, parameter01);
		req2.bizDocInfo = createBizDocInfo(res.contents.bizDocInfo, process.getProcessId());
		req2.versionInfo = createVersionInfo(res.contents.docInfo, parameter01);
		req2.accessibles = createDocAccessibles(res.contents.accessibles, docFolderId);
		req2.attributeExs = createDocAttributeExInfos(res.contents.attributeExs);
		req2.docFiles = createDocFiles(process);
		req2.attachFileDocs = createAttachFileDocs(process);
		req2.runtimeMap = ctx.runtimeMap;
		final BaseResponse res2 = service.execute(req2);
		if (!res2.success || !res2.alerts.isEmpty()) {
			throw new InternalServerErrorException("文書管理への連携処理にてエラーが発生しました。");
		}

		// 連携が成功したら文書情報(MWT_DOC_INFO)のDOC_IDをプロセスインスタンスに焼き付ける
		if (res2 instanceof Dc0100InitResponse) {
			final DocInfo docInfo = ((Dc0100InitResponse)res2).contents.docInfo;
			if (!eq(process.getDocId(), docInfo.docId)) {
				process.setDocId(docInfo.docId);
				process.setMajorVersion(docInfo.majorVersion.longValue());
				process.setMinorVersion(docInfo.minorVersion.longValue());
				updateWftProcess(process);
			}
		}
	}

	private void updateWftProcess(WftProcess wftProcess) {
		final String sql = "update WFT_PROCESS set DOC_ID = ?, MAJOR_VERSION = ?, MINOR_VERSION = ? where CORPORATION_CODE = ? and PROCESS_ID = ? ";
		final Object[] params = {wftProcess.getDocId(), wftProcess.getMajorVersion(), wftProcess.getMinorVersion(),
				wftProcess.getCorporationCode(), wftProcess.getProcessId()};
		execSql(sql, params);
	}

	private DocInfo createDocInfo(Dc0100Contents contents, WftProcess process, Long docFolderId, String parameter01) {
		final DocInfo docInfo = new DocInfo();
		MiscUtils.copyFields(contents.docInfo, docInfo);
		// 文書管理側の件名には申請書の件名をセット
		docInfo.title = process.getSubject();
		// 新規登録の場合、下記項目をセット
		if (MiscUtils.eq("0", parameter01)) {
			// 文書責任者には申請者をセットする
			docInfo.ownerCorporationCode = process.getCorporationCodeStart();
			docInfo.ownerUserCode = process.getUserCodeProxyStart();
			docInfo.ownerUserName = process.getUserNameProxyStart();
			// 公開フラグは「1:公開」にて設定
			docInfo.publishFlag = CommonFlag.ON;
			docInfo.publishStartDate = MiscUtils.today();
			docInfo.publishEndDate = MiscUtils.ENDDATE;
			// 保存期間は「0:無期限」
			docInfo.retentionTermType = CommonFlag.OFF;
			// 文書フォルダ
			docInfo.docFolderId = docFolderId;
		}
		// プロセスID(WFからの連携時のみ設定)
		docInfo.processId = process.getProcessId();
		return docInfo;
	}

	private BizDocInfo createBizDocInfo(BizDocInfo bizDocInfo, Long processId) {
		bizDocInfo.processId = processId;
		bizDocInfo.cooperateWf2DocFlag = CommonFlag.ON;
		return bizDocInfo;
	}

	private DocVersionInfo createVersionInfo(DocInfo docInfo, String parameter01) {
		final DocVersionInfo versionInfo = new DocVersionInfo();
		versionInfo.majorVersion = docInfo.majorVersion;
		versionInfo.minorVersion = docInfo.minorVersion;
		versionInfo.unlockFlag = CommonFlag.ON;
		if (MiscUtils.in(parameter01, "0", "1")) {
			versionInfo.updateVersionType =  DcCodeBook.UpdateVersionType.DO_NOT_UPDATE;
		} else if (MiscUtils.eq(parameter01, "2")) {
			versionInfo.updateVersionType =  DcCodeBook.UpdateVersionType.MINOR_VERSION_UP;
		} else if (MiscUtils.eq(parameter01, "3")) {
			versionInfo.updateVersionType =  DcCodeBook.UpdateVersionType.MAJOR_VERSION_UP;
		}
		return versionInfo;
	}

	/**
	 * 文書フォルダに紐付く権限情報を取得.
	 * @param docFolderId 文書フォルダID
	 * @return
	 */
	private List<DocAccessibleInfo> createDocAccessibles(List<DocAccessibleInfo> accessibles, Long docFolderId) {
		if (accessibles == null || accessibles.isEmpty()) {
			final DocAccessibleService service = get(DocAccessibleService.class);
			return service.getFolderAccessibles(docFolderId);
		}
		return accessibles;
	}

	/**
	 * 文書フォルダに紐付く権限情報を取得.
	 * @param docFolderId 文書フォルダID
	 *
	 * @return
	 */
	private List<DocAttributeExInfo> createDocAttributeExInfos(List<DocAttributeExInfo> attributeExs) {
		if (attributeExs == null || attributeExs.isEmpty()) {
			return new ArrayList<>();
		}
		return attributeExs;
	}

	/**
	 * 文書ファイル情報一覧を生成.
	 * ワークフロー側の文書ファイルブロックにある情報を文書管理側へコピーする
	 * @param process プロセスインスタンス
	 */
	private List<DocFileInfo> createDocFiles(WftProcess process) {
		final Bl0015Service bl0015Service = get(Bl0015Service.class);
		final DocFileDataService docFileDataService = get(DocFileDataService.class);
		// ワークフロー文書ファイル一覧を取得
		final List<DocFileWfInfo> list = bl0015Service.getDocFileWfList(process.getCorporationCode(), process.getProcessId());
		// ワークフロー文書ファイル一覧のファイルデータをコピーし、文書ファイル情報一覧を取得
		final List<DocFileInfo> docFiles = docFileDataService.copyDocFileWf2Doc(list);
		return docFiles;
	}

	/**
	 * 添付ファイル情報一覧を生成
	 * ワークフロー側の添付ファイルブロックにある情報を文書管理側へコピーする
	 */
	private List<AttachFileDocInfo> createAttachFileDocs(WftProcess process) {
		final Bl0003Service bl0003Service = get(Bl0003Service.class);
		final DocFileDataService docFileDataService = get(DocFileDataService.class);
		// ワークフロー添付ファイル一覧を取得
		final List<AttachFileWfInfo> list = bl0003Service.getAttachFileWfList(process.getCorporationCode(), process.getProcessId());
		// ワークフロー添付ファイル一覧のファイルデータをコピーし、文書管理側添付ファイル情報一覧を取得
		final List<AttachFileDocInfo> attachFiles = docFileDataService.copyAttachFileWf2Doc(list);
		return attachFiles;
	}
}
