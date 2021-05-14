package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100Repository;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAppendedInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocAppendedInfo;

/**
 * メモ情報ブロック：メモ情報サービス
 */
@BizLogic
public class DcBl0008Service extends BaseService implements CodeMaster {

	@Inject private Dc0100Repository repository;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
	}

	/**
	 * 文書IDに紐付く文書メモ情報を返す
	 * @param req
	 * @return
	 */
	public DcBl0008Response getDocMemo(DcBl0008Request req) {
		final DcBl0008Response res = createResponse(DcBl0008Response.class, req);
		res.memos = this.getDocAppendedList(req.docId);
		res.success = true;
		return res;
	}

	/**
	 * 文書IDに紐付く文書メモ情報を追加
	 * @param req
	 * @return
	 */
	@Transactional
	public DcBl0008Response addDocMemo(DcBl0008Request req) {
		final DcBl0008Response res = createResponse(DcBl0008Response.class, req);
		if (req.docId == null) {
			throw new BadRequestException("文書IDが未指定です");
		}
		String error = validate(req);
		if (isEmpty(error)) {
			final LoginInfo login = sessionHolder.getLoginInfo();
			final Long docId = req.docId;
			// 登録済みの文書メモの連番の最大値を取得
			int seqNo = repository.getMwtDocAppendedMaxSeq(docId);

			// 登録用のメモ情報生成
			final MwtDocAppendedInfo entity = new MwtDocAppendedInfo();
			entity.setDocId(docId);
			entity.setSeqNo(seqNo + 1);
			entity.setMemorandom(req.memo);
			entity.setAppendedDate(timestamp());
			entity.setCorporationCodeAppended(login.getCorporationCode());
			entity.setUserCodeAppended(login.getUserCode());
			entity.setUserNameAppended(login.getUserName());
			repository.insert(entity);

			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.memo));
			res.memos = this.getDocAppendedList(docId);
			res.success = true;
		} else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	private List<DocAppendedInfo> getDocAppendedList(Long docId) {
		final List<DocAppendedInfo> results = new ArrayList<>();
		if (docId != null) {
			final List<MwtDocAppendedInfo> list = repository.getMwtDocAppendedInfoList(docId);
			list.stream().forEach(e -> results.add(new DocAppendedInfo(e)));
		}
		return results;
	}

	private String validate(DcBl0008Request req) {
		if (isEmpty(req.memo))
			return i18n.getText(MessageCd.MSG0001, MessageCd.memo);

		return null;
	}
}
