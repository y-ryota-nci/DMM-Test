package jp.co.dmm.customize.endpoint.ri.ri0020;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.endpoint.ri.ri0010.Ri0010Entity;
import jp.co.dmm.customize.jpa.entity.mw.TaxMst;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 検収明細分割サービス
 */
@ApplicationScoped
public class Ri0020Service extends BaseService {
	@Inject private Ri0020Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ri0020Response init(Ri0020Request req) {
		Ri0010Entity source = req.source;
		if (isEmpty(source) || isEmpty(source.companyCd) || isEmpty(source.purordNo) || isEmpty(source.purordDtlNo))
			throw new BadRequestException("パラメータが足りません");

		final Ri0020Response res = createResponse(Ri0020Response.class, req);
		res.results.add(source);
		res.taxCds = toOptionItems(repository.getTaxCdList(source.companyCd));
		res.success = true;
		return res;
	}

	/**
	 * 消費税コードドロップダウンリストの作成
	 * @param taxCds
	 * @param emptyLine
	 * @return
	 */
	private List<OptionItem> toOptionItems(List<TaxMst> taxCds) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll(taxCds.stream()
				.map(t -> new OptionItem(t.getId().getTaxCd(), t.getTaxNm()))
				.collect(Collectors.toList()));
		return items;
	}
}
