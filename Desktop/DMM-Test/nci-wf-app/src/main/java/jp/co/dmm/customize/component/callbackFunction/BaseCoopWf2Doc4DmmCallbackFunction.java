package jp.co.dmm.customize.component.callbackFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.AssignedStatus;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmInformationSharerDefOutParam;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.component.document.BizDocService;
import jp.co.nci.iwf.component.document.DocAccessibleService;
import jp.co.nci.iwf.component.document.DocFileDataService;
import jp.co.nci.iwf.component.document.DocFolderService;
import jp.co.nci.iwf.component.document.DocHelper;
import jp.co.nci.iwf.component.route.ActivityEntity;
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
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Request;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Response;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0007Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0015Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderAccessibleInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * DMM用のワークフローから文書管理への連携コールバックファンクションの基底クラス.
 */
public abstract class BaseCoopWf2Doc4DmmCallbackFunction extends BaseCallbackFunction {

	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		// この操作は状態遷移処理か
		if (!(result instanceof MoveActivityInstanceOutParam))
			return;

		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess process = out.getProcess();
		final String corporationCode = process.getCorporationCode();

		// アクション機能定義からパラメータを取得
		String parameter01 = functionDef.getFunctionParameter01();				// 文書管理側への登録方式 0:新規登録 1:更新(バージョンアップなし) 2:更新(マイナーバージョンアップ) 3:更新(メジャーバージョンアップ)
		final String parameter02 = functionDef.getFunctionParameter02();		// 新規登録時に使用する画面文書定義マスタの画面文書定義コード（ワークフローで使用している画面と一致している必要あり）
		final String parameter03 = functionDef.getFunctionParameter03();		// 年度フォルダ生成に使用（0:システム日付、1:申請日、2:決裁日、左記以外はパーツコードを指定）
		final String parameter04 = functionDef.getFunctionParameter04();		// 取引先フォルダ生成に使用（各画面毎の取引先名称のパーツコードを指定）
		final String parameter05 = functionDef.getFunctionParameter05();		// 申請種別フォルダ生成に使用（1:取引先、2:契約、3:発注、4:検収、5:支払）

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
					final MwvScreenDocDef screenDocDef = bizDocService.getMwvScreenDocDef(corporationCode, parameter02, LoginInfo.get().getLocaleCode());
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
					// 第三～五パラメータの値に応じて文書情報を格納するフォルダを決定する
					// フォルダがない場合、内部で生成する
					// 年度フォルダの文書フォルダIDを取得
					final Long nendoDocFolderId = this.createDocFolder4Nendo(corporationCode, parameter03, ctx, process);
					// 取引先フォルダの文書フォルダIDを取得
					final Long splrDocFolderId = this.createDocFolder4Splr(corporationCode, nendoDocFolderId, parameter04, ctx);
					// 申請種別フォルダの文書フォルダIDを取得
					final Long applyDocFolderId = this.createDocFolder4ApplyType(corporationCode, splrDocFolderId, parameter05);

					screenDocId = screenDocDef.screenDocId;
//					docFolderId = docFolder != null ? docFolder.getDocFolderId() : screenDocDef.docFolderId;
					docFolderId = applyDocFolderId;
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
		req.corporationCode = corporationCode;
		req.screenDocId = screenDocId;
		req.docId = docId;
		req.fromCoopWfFlag = CommonFlag.ON;
		final Dc0100InitResponse res = service.init(req);
		// 次にDc0100Service#executeを呼び出して文書情報等の新規登録を行う
		final Dc0100ExecuteRequest req2 = new Dc0100ExecuteRequest();
		// 連携の場合、WF側のプロセスIDをそのまま使用
		res.contents.processId = process.getProcessId();
		req2.contents = res.contents;
		req2.docInfo = createDocInfo(res.contents, process, docFolderId, parameter01);
		req2.bizDocInfo = createBizDocInfo(res.contents.bizDocInfo, process.getProcessId());
		req2.versionInfo = createVersionInfo(res.contents.docInfo, parameter01);
		req2.accessibles = createDocAccessibles(res.contents.accessibles, process);
		req2.attributeExs = createDocAttributeExInfos(res.contents.attributeExs);
		req2.docFiles = createDocFiles(res.contents.docFiles, process);
		req2.attachFileDocs = createAttachFileDocs(process);
		req2.runtimeMap = ctx.runtimeMap;
		req2.fromCoopWfFlag = CommonFlag.ON;
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
	private List<DocAccessibleInfo> createDocAccessibles(List<DocAccessibleInfo> accessibles, WftProcess process) {
		final List<DocAccessibleInfo> result = new ArrayList<>();
		final DocHelper helper = get(DocHelper.class);
		// 元々文書情報に紐付いていた権限情報はそのまま
		final Set<String> duplicate = new HashSet<>();
		if (accessibles != null) {
			result.addAll(accessibles);
			accessibles.stream().forEach(a -> {
				duplicate.add(helper.toHashValue(a.belongType, a.corporationCode, a.roleCode, a.userCode));
			});
		}

		// 今回の申請において承認者となったユーザを新たに追加していく
		{
			final Bl0007Service service = get(Bl0007Service.class);
			// 現時点での承認ルート情報を取得
			Bl0007Request req = new Bl0007Request();
			req.corporationCode = process.getCorporationCode();
			req.processId = process.getProcessId();
			req.simulation = false;
			final Bl0007Response res = service.init(req);
			final List<ActivityEntity> routeList = res.routeList;
			for (ActivityEntity activity: routeList) {
				activity.assignedUserList.stream().forEach(a -> {
					String hashValue = helper.toHashValue("U", a.corporationCode, null, a.userCode);
					if (!duplicate.contains(hashValue)) {
						DocAccessibleInfo addAccessible = new DocAccessibleInfo();
						addAccessible.authRefer = CommonFlag.ON;
						addAccessible.authDownload = CommonFlag.ON;
						addAccessible.corporationCode = a.corporationCode;
						addAccessible.userCode = a.userCode;
						result.add(addAccessible);
						duplicate.add(hashValue);
					}
					// 承認済の場合、代理で処理した人にも閲覧権限を付与
					if (eq(AssignedStatus.END, a.assignedStatus) && a.proxyUserList != null) {
						a.proxyUserList.stream().forEach(p -> {
							String hashValue2 = helper.toHashValue("U", p.getCorporationCode(), null, p.getUserCode());
							if (!duplicate.contains(hashValue2)) {
								DocAccessibleInfo addAccessible = new DocAccessibleInfo();
								addAccessible.authRefer = CommonFlag.ON;
								addAccessible.authDownload = CommonFlag.ON;
								addAccessible.corporationCode = p.getCorporationCode();
								addAccessible.userCode = p.getUserCode();
								result.add(addAccessible);
								duplicate.add(hashValue2);
							}
						});
					}
				});
			}
		}
		// 次に申請に紐付くデフォルト閲覧者を追加
		{
			final List<WfmInformationSharerDef> list = this.getDefaultInformationSharerList(process);
			if (list != null) {
				list.stream().forEach(e -> {
					String hashValue = helper.toHashValue("R", e.getCorporationCodeAssign(), e.getAssignRoleCode(), null);
					if (!duplicate.contains(hashValue)) {
						DocAccessibleInfo addAccessible = new DocAccessibleInfo();
						addAccessible.authRefer = CommonFlag.ON;
						addAccessible.authDownload = CommonFlag.ON;
						addAccessible.corporationCode = e.getCorporationCodeAssign();
						addAccessible.roleCode = e.getAssignRoleCode();
						result.add(addAccessible);
						duplicate.add(hashValue);
					}
				});
			}
		}

		return result;
	}

	/**
	 * 申請書に紐付くデフォルト閲覧者一覧を取得.
	 * @param process プロセスインスタンス
	 * @return デフォルト閲覧者一覧
	 */
	private List<WfmInformationSharerDef> getDefaultInformationSharerList(WftProcess process) {
		final SearchWfmInformationSharerDefInParam in = new SearchWfmInformationSharerDefInParam();
		in.setCorporationCode(process.getCorporationCode());
		in.setProcessDefCode(process.getProcessDefCode());
		in.setProcessDefDetailCode(process.getProcessDefDetailCode());
		in.setProcessId(process.getProcessId());

		in.setValidStartDate(MiscUtils.today());
		in.setValidEndDate(MiscUtils.today());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "S.ASSIGN_ROLE_CODE")});

		final SearchWfmInformationSharerDefOutParam out = getWfInstanceWrapper().searchWfmInformationSharerDef(in);
		return out.getInformationSharerDefs();
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
	private List<DocFileInfo> createDocFiles(List<DocFileInfo> orgDocFiles, WftProcess process) {
		final Bl0015Service bl0015Service = get(Bl0015Service.class);
		final DocFileDataService docFileDataService = get(DocFileDataService.class);
		// ワークフロー文書ファイル一覧を取得
		final List<DocFileWfInfo> list = bl0015Service.getDocFileWfList(process.getCorporationCode(), process.getProcessId());
		// 未連携のワークフロー文書ファイルのみ文書管理側へコピー
		if (orgDocFiles == null) {
			orgDocFiles = new ArrayList<>();
		}
		final Set<Long> docFileWfIds = orgDocFiles.stream().map(e -> e.docFileWfId).collect(Collectors.toSet());
		final List<DocFileWfInfo> addList = new ArrayList<>();
		for (DocFileWfInfo file : list) {
			if (!docFileWfIds.contains(file.docFileWfId)) {
				addList.add(file);
			}
		}
		// ワークフロー文書ファイル一覧のファイルデータをコピーし、文書ファイル情報一覧を取得
		final List<DocFileInfo> docFiles = docFileDataService.copyDocFileWf2Doc(addList);
		orgDocFiles.addAll(docFiles);
		return orgDocFiles;
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

	/**
	 * 年度のフォルダ生成処理.
	 * ※年度ではなく、単純に年でフォルダを生成するようになりました
	 * @param corporationCode 企業コード
	 * @param parameter03 年度算出に使用する日付の区分（0:システム日付、1:申請日、2:決裁日、左記以外はパーツコードを指定）
	 * @param ctx
	 * @param process
	 * @return 作成された年度フォルダの文書フォルダID
	 */
	protected Long createDocFolder4Nendo(String corporationCode, String parameter03, RuntimeContext ctx, final WftProcess process) {
		// 年度はトップフォルダの配下に位置するので親フォルダIDには0を指定
		final Long parentDocFolderId = 0L;
		if (isEmpty(parameter03)) {
			return parentDocFolderId;
		}

		final Date d;
		if (eq("0", parameter03)) {
			d = MiscUtils.today();
		} else if (eq("1", parameter03)) {
			d = MiscUtils.defaults(process.getApplicationDate(), MiscUtils.today());
		} else if (eq("2", parameter03)) {
			d = MiscUtils.defaults(process.getApprovalDate(), MiscUtils.today());
		} else {
			String strDate = ctx.runtimeMap.get(parameter03).getValue();
			d = MiscUtils.toDate(strDate, MiscUtils.FORMAT_DATE);
		}
		if (d == null) {
			throw new InternalServerErrorException("年度フォルダ生成に使用する基準日付が取得できませんでした。");
		}

		// 年を取得
		String nendo = new SimpleDateFormat("yyyy").format(d);
		// フォルダ名はxxxx年
		String folderName = nendo + "年";

		return this.createDocFolder(parentDocFolderId, corporationCode, folderName, Integer.valueOf(nendo));
	}

	/**
	 * 取引先のフォルダ生成処理.
	 * @param corporationCode 企業コード
	 * @param parentDocFolderId 親フォルダの文書フォルダID
	 * @param parameter04 取引先名称のパーツコード
	 * @param ctx
	 * @return 作成された取引先フォルダの文書フォルダID
	 */
	protected Long createDocFolder4Splr(String corporationCode, Long parentDocFolderId, String parameter04, RuntimeContext ctx) {
		if (isEmpty(parameter04)) {
			return parentDocFolderId;
		}
		final String splrNm = ctx.runtimeMap.get(parameter04).getValue();
		if (isEmpty(splrNm)) {
			throw new InternalServerErrorException("取引先フォルダ生成に使用する取引先名称が取得できませんでした。");
		}
		// フォルダ名＝取引先名
		// 並び順は1000固定
		final String folderName = splrNm;
		return this.createDocFolder(parentDocFolderId, corporationCode, folderName, 1000);
	}

	/**
	 * 申請書毎のフォルダ生成処理.
	 * @param corporationCode 企業コード
	 * @param parentDocFolderId 親フォルダの文書フォルダID
	 * @param parameter05 申請種別
	 * @return 作成された取引先フォルダの文書フォルダID
	 */
	protected Long createDocFolder4ApplyType(String corporationCode, Long parentDocFolderId, String parameter05) {
		// フォルダ名は申請種別の値によって切り替える
		final String folderName;
		Integer sortOrder = null;
		if (eq("1", parameter05)) {
			folderName = "取引先";
		} else if (eq("2", parameter05)) {
			folderName = "契約";
		} else if (eq("3", parameter05)) {
			folderName = "発注";
		} else if (eq("4", parameter05)) {
			folderName = "検収";
		} else if (eq("5", parameter05)) {
			folderName = "支払";
		} else if (eq("6", parameter05)) {
			folderName = "購入依頼";
			// 購入依頼は一番下に配置したいため、並び順は9999を指定
			sortOrder = 9999;
		} else {
			throw new InternalServerErrorException("不正な申請種別です。[1:取引先、2:契約、3:発注、4:検収、5:支払、6:購入依頼]を指定してください。");
		}
		if (sortOrder == null) {
			sortOrder = Integer.valueOf(parameter05);
		}

		return this.createDocFolder(parentDocFolderId, corporationCode, folderName, sortOrder);
	}

	/**
	 * 文書フォルダ生成処理.
	 * 引数のフォルダ名に合致する文書フォルダがない場合、フォルダを生成し、そのフォルダの文書フォルダIDを返す
	 * @param parentDocFolderId 親文書フォルダID
	 * @param corporationCode 企業コード
	 * @param folderName フォルダ名
	 * @param sortOrder 並び順 ※未指定の場合、9999固定
	 * @return 文書フォルダID
	 */
	private Long createDocFolder(Long parentDocFolderId, String corporationCode, String folderName, Integer sortOrder) {
		final DocFolderService docFolderService = get(DocFolderService.class);
		final MwvDocFolder docFolder = docFolderService.getMwvDocFolderByFolderName(corporationCode, folderName, parentDocFolderId);
		if (docFolder != null) {
			return docFolder.getDocFolderId();
		}

		// フォルダがなければ生成
		final MwvDocFolder inputed = new MwvDocFolder();
		inputed.setCorporationCode(corporationCode);
		inputed.setFolderName(folderName);
		inputed.setSortOrder(sortOrder);
		if (sortOrder == null) {
			inputed.setSortOrder(9999);
		}
		inputed.setParentDocFolderId(parentDocFolderId);
		// フォルダを登録
		final Long docFolderId = docFolderService.saveMwmDocFolder(inputed);
		// フォルダ階層を設定
		docFolderService.saveMwmDocFolderHierarchy(docFolderId, inputed);
		// アクセス権限を設定
		final List<MwmDocFolderAccessibleInfo> accessibles = this.createDocFolderAccessibles(corporationCode);
		final DocAccessibleService docAccessibleService = get(DocAccessibleService.class);
		docAccessibleService.saveMwmDocFolderAccessible(docFolderId, accessibles, false);

		return docFolderId;
	}

	/**
	 * 登録する文書フォルダに対するアクセス権限情報を生成.
	 * 文書連携にて生成されるフォルダに対しては「DMMALL」(ロール構成は全社員)を付与する
	 * また設定されるアクセス権は"参照"および"ダウンロード"のみ
	 * @param corporationCode 企業コード
	 * @return
	 */
	private List<MwmDocFolderAccessibleInfo> createDocFolderAccessibles(String corporationCode) {
		final List<MwmDocFolderAccessibleInfo> accessibles = new ArrayList<>();
		MwmDocFolderAccessibleInfo accessible = new MwmDocFolderAccessibleInfo();
		accessible.setAuthRefer(CommonFlag.ON);
		accessible.setAuthDownload(CommonFlag.ON);
		accessible.setAssignRoleCode("DMMALL");
		accessible.setCorporationCode(corporationCode);
		accessibles.add(accessible);
		return accessibles;
	}

}
