package jp.co.nci.iwf.endpoint.cm.cm0170;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.BbsAttachFileWfInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.base.UploadFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;

/**
 * 要説明(掲示板)ブロック用、投稿内容入力画面サービス
 */
@BizLogic
public class Cm0170Service extends BaseService {
	@Inject private MwmLookupService lookup;
	@Inject private WfInstanceWrapper wf;
	@Inject private Cm0170Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Cm0170Response init(Cm0170Request req) {
		final Cm0170Response res = createResponse(Cm0170Response.class, req);
		res.processBbsMailTypes = lookup.getOptionItems(LookupGroupId.PROCESS_BBS_MAIL_TYPE, false);
		res.belongs = getBelongs();
		res.success = true;
		return res;
	}

	/** 所属の選択肢を返す */
	private List<OptionItem> getBelongs() {
		final java.sql.Date today = today();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setUserCode(sessionHolder.getLoginInfo().getUserCode());
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today);
		in.setValidEndDateOrganization(today);
		in.setValidStartDatePost(today);
		in.setValidEndDatePost(today);
		in.setValidStartDateUser(today);
		in.setValidEndDateUser(today);
		in.setValidStartDateUserBelong(today);
		in.setValidEndDateUserBelong(today);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, WfvUserBelong.JOB_TYPE),
				new OrderBy(true, WfvUserBelong.ORGANIZATION_TREE_NAME),
		});
		return wf.searchWfvUserBelong(in).getUserBelongList()
				.stream()
				.map(e -> {
					String value = e.getOrganizationCode() + "@" + e.getPostCode();
					String label = e.getOrganizationName();
					if (isNotEmpty(e.getPostCode())) {
						label += " (" + e.getPostName() + ")";
					}
					return new OptionItem(value, label);
				})
				.collect(Collectors.toList());
	}

	@Transactional
	public List<BbsAttachFileWfInfo> upload(List<FormDataBodyPart> bodyParts) {
		final List<BbsAttachFileWfInfo> results = new ArrayList<>();
		for (BodyPart bodyPart : bodyParts) {
			FormDataContentDisposition cd = (FormDataContentDisposition)bodyPart.getContentDisposition();
			if (eq("file", cd.getName())) {
				final UploadFile f = new UploadFile(bodyPart);
				final String fileName = f.fileName;

				String fileType = "";
				// 拡張子からファイルタイプを判別
				if (Pattern.compile("\\.(PNG|GIG|JPG|JPEG|BMP|TIF|TIFF)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find())  {
					fileType = "image";
				} else if (Pattern.compile("\\.(PDF)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					fileType = "pdf";
				} else if (Pattern.compile("\\.(DOC|DOCX|DOCM|DOT|DOTX)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					fileType = "word";
				} else if (Pattern.compile("\\.(XLS|XLSX|XLSM|XLT|XLTX)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					fileType = "excel";
				} else if (Pattern.compile("\\.(PPTX|PPTM|PPT|POTX|POTM|POT|PPSX|PPSM|PPS)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					fileType = "powerpoint";
				} else if (Pattern.compile("\\.(CSV|TSV|TXT|LOG|DAT)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					fileType = "text";
				} else if (Pattern.compile("\\.(HTML|HTM)$", Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					fileType = "code";
				}

				final byte[] fileData = toBytes(f.stream);
				final MwtBbsAttachFileWf file = new MwtBbsAttachFileWf();
				file.setDeleteFlag(DeleteFlag.ON);	// まだ仮登録なので論理削除として登録する
				file.setFileType(fileType);
				file.setFileName(f.fileName);
				file.setFileData(fileData);
				file.setFileSize(fileData.length);
				file.setProcessBbsId(0L);
				repository.insert(file);
				results.add(new BbsAttachFileWfInfo(file));
			}
		}
		return results;
	}

	/**
	 * ダウンロード処理
	 * @param bbsAttachFileWfId
	 * @return
	 */
	public MwtBbsAttachFileWf download(Long bbsAttachFileWfId) {
		return repository.getMwtBbsAttachFileWf(bbsAttachFileWfId);
	}

}
