package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jp.co.nci.iwf.cdi.annotation.Endpoint;
import jp.co.nci.iwf.cdi.annotation.RequiredLogin;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.accesslog.WriteAccessLog;
import jp.co.nci.iwf.jersey.base.BaseEndpoint;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * パーツプロパティ設定画面Endpoint
 */
@Path("/vd0114")
@Endpoint
@RequiredLogin
@WriteAccessLog
public class Vd0114Endpoint extends BaseEndpoint<BaseRequest> {
	@Inject
	private Vd0114Service service;

	/**
	 * 初期化
	 */
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BaseResponse init(BaseRequest req) {
		return service.init(req);
	}

	/**
	 * リピーターパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initRepeater")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseRepeater initRepeater(Vd0114Request req) {
		return service.initRepeater(req);
	}

	/**
	 * グリッドパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initGrid")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseGrid initGrid(Vd0114Request req) {
		return service.initGrid(req);
	}

	/**
	 * テキストボックスパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initTextbox")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseTextbox initTextbox(Vd0114Request req) {
		return service.initTextbox(req);
	}

	/**
	 * ユーザ選択パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initUserSelect")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseUserSelect initUserSelect(Vd0114Request req) {
		return service.initUserSelect(req);
	}

	/**
	 * 組織選択パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initOrganizationSelect")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseOrganizationSelect initOrganizationSelect(Vd0114Request req) {
		return service.initOrganizationSelect(req);
	}

	/**
	 * チェックボックスパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initCheckbox")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseCheckbox initCheckbox(Vd0114Request req) {
		return service.initCheckbox(req);
	}

	/**
	 * ラジオボタンパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initRadio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseRadio initRadio(Vd0114Request req) {
		return service.initRadio(req);
	}

	/**
	 * ドロップダウンパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initDropdown")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseDropdown initDropdown(Vd0114Request req) {
		return service.initDropdown(req);
	}

	/**
	 * 選択肢プロパティ変更時のデフォルト値変更処理
	 * @param req
	 * @return
	 */
	@POST
	@Path("/changeOption")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseOptionChange changeOption(Vd0114Request req) {
		return service.changeOption(req);
	}

	/**
	 * 採番パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initNumbering")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseNumbering initNumbering(Vd0114Request req) {
		return service.initNumbering(req);
	}

	/**
	 * マスタ検索Buttonパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initSearchButton")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseButton initSearchButton(Vd0114Request req) {
		return service.initSearchButton(req);
	}

	/**
	 * イベントButtonパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initEventButton")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseButton initEventButton(Vd0114Request req) {
		return service.initEventButton(req);
	}

	/**
	 * ButtonパーツでtableIdを変更したことによる「汎用テーブル検索条件の選択肢」を抽出
	 * @param req
	 * @return
	 */
	@POST
	@Path("/findTableSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> findTableSearch(Vd0114ButtonRequest req) {
		return service.findTableSearch(req);
	}

	/**
	 * ButtonパーツでtableSearchIdを変更したことによる「汎用テーブル検索条件カラムの選択肢」を抽出（絞込条件）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getTableSearchColumnsOut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> getTableSearchColumnsOut(Vd0114ButtonRequest req) {
		return service.getTableSearchColumnsOut(req);
	}

	/**
	 * ButtonパーツでtableSearchIdを変更したことによる「汎用テーブル検索条件カラムの選択肢」を抽出（絞込条件 or 検索結果）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getTableSearchColumnsInOrOut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> getTableSearchColumnsInOrOut(Vd0114ButtonRequest req) {
		return service.getTableSearchColumnsInOrOut(req);
	}

	/**
	 * ButtonパーツでtableSearchIdを変更したことによる「汎用テーブル検索条件カラムの選択肢」を抽出（絞込条件 and 検索結果）
	 * @param req
	 * @return
	 */
	@POST
	@Path("/getTableSearchColumnsInAndOut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> getTableSearchColumnsInAndOut(Vd0114ButtonRequest req) {
		return service.getTableSearchColumnsInAndOut(req);
	}

	/**
	 * Masterパーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initMaster")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseButton initMaster(Vd0114Request req) {
		// 検索ボタンパーツのを流用
		return service.initMaster(req);
	}

	/**
	 * グリッドパーツで子コンテナを変更したことによる対象パーツ一覧の再抽出
	 * @param req
	 * @return
	 */
	@POST
	@Path("/findGridChildParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> findGridChildParts(Vd0114GridRequest req) {
		return service.findGridChildParts(req);
	}

	/**
	 * 子コンテナ内で入力済み判定対象に使用できるパーツ一覧の再抽出
	 * @param req
	 * @return
	 */
	@POST
	@Path("/findInputedJudgeParts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<OptionItem> findInputedJudgeParts(Vd0114GridRequest req) {
		return service.findInputedJudgeParts(req);
	}

	/**
	 * ファイルアップロード
	 * @param multiPart
	 */
	@POST
	@Path("/uploadAttachFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponsePartsDesignAttachFile uploadAttachFile(
			@FormDataParam("partsId") Long partsId,
			@FormDataParam("fileRegExp") String fileRegExp,
			FormDataMultiPart multiPart) {
		return service.uploadAttachFile(partsId, fileRegExp, multiPart);
	}

	/**
	 * 独立画面パーツ用プロパティの初期化
	 * @param req
	 * @return
	 */
	@POST
	@Path("/initStandAloneScreen")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Vd0114ResponseStandAlone initStandAloneScreen(Vd0114Request req) {
		return service.initStandAloneScreen(req);
	}
}
