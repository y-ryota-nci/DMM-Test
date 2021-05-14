package jp.co.dmm.customize.endpoint.py.py0020;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

@ApplicationScoped
public class Py0020Repository extends BaseRepository {

	public Py0020Entity getEntity(String corporationCode, Long processId) {
		final Object[] params = { corporationCode, processId };
		return selectOne(Py0020Entity.class, getSql("PY0020_01"), params);
	}

	public Py0020RedirectEntity getEntity(String companyCd, String payNo) {
		final Object[] params = { companyCd, payNo };
		return selectOne(Py0020RedirectEntity.class, getSql("PY0020_02"), params);
	}

	public TrayConfig getTrayConfig(LoginInfo login) {
		final Object[] params = {
				login.getCorporationCode()
				, "PY401"
		};
		// union all で抽出するので、優先順位の一番高い一件を使う
		return selectOne(TrayConfig.class, getSql("PY0020_03"), params);
	}

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getOptionItems(String corporationCode, String localeCode, boolean isEmpty) {
		String query = getSql("PY0020_04");
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(localeCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}
		results.stream().forEach(i -> newItems.add(new OptionItem(i.getCode(), i.getLabel())));

		return newItems;
	}


}