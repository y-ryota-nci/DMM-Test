package jp.co.nci.iwf.endpoint.mm.mm0500;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SetupMasterInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * マスター初期値設定サービス
 */
@BizLogic
public class Mm0500Service extends BaseService {
	@Inject private Mm0500Repository repository;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0500Response init(Mm0500Request req) {
		final Mm0500Response res = createResponse(Mm0500Response.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		res.srcCorporations = createSrcCorporations();
		res.srcCorporationCode = defaults(req.srcCorporationCode, CorporationCodes.ASP);
		res.entities = search(res.srcCorporationCode, localeCode);
		res.success = true;
		return res;
	}

	/**
	 * コピー元企業の選択肢を生成
	 * @return
	 */
	private List<OptionItem> createSrcCorporations() {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setLanguage(login.getLocaleCode());
		wf.searchWfmCorporation(in).getCorporations().forEach(c -> {
			final String code = c.getCorporationCode();
			final String name = c.getCorporationName();
			final String label = String.format("%s %s", code, name);
			items.add(new OptionItem(code, label));
		});
		return items;
	}

	/** 検索 */
	private List<Mm0500Entity> search(String srcCorporationCode, String localeCode) {
		List<Mm0500Entity> entities = repository.search(srcCorporationCode, localeCode);
		entities.forEach(e -> {
			int rowResult = 0;
			{	// WFM_LOOKUP_TYPE
				int r = toResult(e.wfmLookupTypeAll, e.wfmLookupTypeCount);
				rowResult += r;
				e.wfmLookupType = toResultString(r);
			}
			{	// WFM_LOOKUP
				int r = toResult(e.wfmLookupAll, e.wfmLookupCount);
				rowResult += r;
				e.wfmLookup = toResultString(r);
			}
			{	// WFM_ACTION
				int r = toResult(e.wfmActionAll, e.wfmActionCount);;
				rowResult += r;
				e.wfmAction = toResultString(r);
			}
			{	// WFM_FUNCTION
				int r = toResult(e.wfmFunctionAll, e.wfmFunctionCount);
				rowResult += r;
				e.wfmFunction = toResultString(r);
			}
			{	// MWM_LOOKUP_GROUP
				int r = toResult(e.mwmLookupGroupAll, e.mwmLookupGroupCount);
				rowResult += r;
				e.mwmLookupGroup = toResultString(r);
			}
			{	// MWM_LOOKUP
				int r = toResult(e.mwmLookupAll, e.mwmLookupCount);
				rowResult += r;
				e.mwmLookup = toResultString(r);
			}
			{	// MWM_BUSINESS_INFO_NAME
				int r = toResult(e.mwmBusinessInfoNameAll, e.mwmBusinessInfoNameCount);
				rowResult += r;
				e.businessInfo = toResultString(r);
			}
			{	// MWM_DOC_BUSINESS_INFO_NAME
				int r = toResult(e.mwmDocBusinessInfoNameAll, e.mwmDocBusinessInfoNameCount);
				rowResult += r;
				e.docBusinessInfo = toResultString(r);
			}
			{	// MWM_TRAY_CONFIG
				int r = toResult(e.mwmTrayConfigAll, e.mwmTrayConfigCount);
				rowResult += r;
				e.trayConfig = toResultString(r);
			}
			{	// MWM_DOC_TRAY_CONFIG
				int r = toResult(e.mwmDocTrayConfigAll, e.mwmDocTrayConfigCount);
				rowResult += r;
				e.docTrayConfig = toResultString(r);
			}
			{	// MWM_MAIL_VARIABLE
				int r = toResult(e.mwmMailVariableAll, e.mwmMailVariableCount);
				rowResult += r;
				e.mailVaribale = toResultString(r);
			}
			{
				// MWM_OPTION
				int r = toResult(e.MwmOptionAll, e.MwmOptionCount);
				rowResult += r;
				e.optionSetting = toResultString(r);
			}
			{
				// MWM_OPTION_ITEM
				int r = toResult(e.MwmOptionItemAll, e.MwmOptionItemCount);
				rowResult += r;
				e.optionItemSetting = toResultString(r);
			}
			// 行全体の判定結果として、行選択可能か
			e.selectable = (rowResult != Result.REGISTERED);
		});
		return entities;
	}

	/** 各種マスタで、ASPに設定されているレコード数と各企業に設定されているレコード数の比較結果を文字列で返す */
	private int toResult(Integer all, Integer count) {
		if (all == 0) {
			// (NOTHING) or コピー元で不足
			return (count == 0) ? Result.NOTHING : Result.SOURCE_MISSING;
		}
		else if (compareTo(all, count) == 0) {
			return Result.REGISTERED;	// 設定済
		}
		else if (count == 0) {
			return Result.NOT;			// 未設定
		}
		else {
			return Result.PERTIAL;		// 一部設定済
		}
	}

	/** 判定結果 */
	private interface Result {
		/** (過不足なく)設定済み  */
		int REGISTERED = 0;
		/** コピー元に存在しない */
		int NOTHING = 1;
		/** 一部設定済 */
		int PERTIAL = 2;
		/** 未設定 */
		int NOT = 3;
		/** コピー元側で不足 */
		int SOURCE_MISSING = 4;
	}

	private String toResultString(int result) {
		switch (result) {
		case Result.REGISTERED:	// 設定済
			return i18n.getText(MessageCd.registered);
		case Result.PERTIAL: // 一部設定済
			return i18n.getText(MessageCd.registeredPartially);
		case Result.NOT:	// 未設定
			return i18n.getText(MessageCd.notRegistered);
		case Result.SOURCE_MISSING:	// コピー元で不足
			return i18n.getText(MessageCd.lackAtCopySource);
		case Result.NOTHING:
		default:
			return "(NOTHING)";
		}
	}

	/**
	 * 設定
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0500Response save(Mm0500Request req) {
		if (isEmpty(req.srcCorporationCode))
			throw new BadRequestException("コピー元企業コードが未指定です");

		// コピー元企業からコピー先企業へマスターデータをコピー
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		for (String destCorporationCode : req.destCorporationCodes) {
			insertInitMaster(req.srcCorporationCode, destCorporationCode, localeCode);
		}

		// 処理結果
		final Mm0500Response res = createResponse(Mm0500Response.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.MM0500));
		// 再表示
		res.entities = search(req.srcCorporationCode, localeCode);
		res.srcCorporations = createSrcCorporations();
		res.srcCorporationCode = defaults(req.srcCorporationCode, CorporationCodes.ASP);
		res.success = true;
		return res;
	}

	/**
	 * 対象企業のマスタ初期値をASPからINSERT～SELECTする
	 * @param destCorporationCode
	 */
	public void insertInitMaster(String srcCorporationCode, String destCorporationCode, String localeCode) {
		// WFM系のマスタをINSERT SELECT
		insertSelectWfm(srcCorporationCode, destCorporationCode);

		// MWM系のマスタを INSERT SELECT
		repository.insertSelectMwm(srcCorporationCode, destCorporationCode, localeCode);
	}

	/** 指定企業に対して、不足しているレコードをASPをベースにINSERT～SELECT */
	private void insertSelectWfm(String srcCorporationCode, String destCorporationCode) {
		final SetupMasterInParam in = new SetupMasterInParam();
		in.setSrcCorporationCode(srcCorporationCode);
		in.setDestCorporationCode(destCorporationCode);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// WFM_LOOKUP_TYPEテーブルの設定
		in.setMode(SetupMasterInParam.MODE_LOOKUP_TYPE);
		wf.setupMaster(in);
		// WFM_LOOKUPテーブルの設定
		in.setMode(SetupMasterInParam.MODE_LOOKUP);
		wf.setupMaster(in);
		// WFM_ACTIONテーブルの設定
		in.setMode(SetupMasterInParam.MODE_ACTION);
		wf.setupMaster(in);
		// WFM_FUNCTIONテーブルの設定
		in.setMode(SetupMasterInParam.MODE_FUNCTION);
		wf.setupMaster(in);
	}
}
