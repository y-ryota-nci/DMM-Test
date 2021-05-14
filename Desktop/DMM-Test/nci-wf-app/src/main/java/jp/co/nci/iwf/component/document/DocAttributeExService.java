package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.endpoint.vd.vd0114.Vd0114Repository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocMetaInfoEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDef;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocMetaInfo;

/**
 * 文書属性(拡張)のサービス
 */
@BizLogic
public class DocAttributeExService extends BaseService {

	@Inject private DocAttributeExRepository repository;
	@Inject private Vd0114Repository repository2;

	/**
	 * メタテンプレート定義一覧取得.
	 * @param corporationCode 企業コード
	 * @return
	 */
	public List<MwmMetaTemplateDef> getMwmMetaTemplateDefList(String corporationCode) {
		return repository.getMwmMetaTemplateDefList(corporationCode, sessionHolder.getLoginInfo().getLocaleCode());
	}

	/**
	 * メタテンプレート定義の選択肢一覧取得.
	 * @param corporationCode 企業コード
	 * @return
	 */
	public List<OptionItem> getMetaTemplateOptionList(String corporationCode) {
		final List<OptionItem> list = new ArrayList<>();
		list.add(OptionItem.EMPTY);
		this.getMwmMetaTemplateDefList(corporationCode).stream().forEach(e -> {
			list.add( new OptionItem(e.getMetaTemplateId(), e.getMetaTemplateName()) );
		});
		return list;
	}

	/**
	 * メタ情報テンプレート明細から文書属性(拡張)一覧取得.
	 *
	 */
	public List<DocAttributeExInfo> getDocAttributeExListByMetaTemplateId(String corporationCode, Long metaTemplateId) {
		final List<DocAttributeExInfo> attributeExs = new ArrayList<>();
		if (isEmpty(metaTemplateId)) {
			return attributeExs;
		}
		final List<MwmMetaTemplateDetailEx> list = repository.getMwmMetaTemplateDetailList(metaTemplateId, sessionHolder.getLoginInfo().getLocaleCode());
		if (list != null) {
			list.stream().forEach(e -> {
				DocAttributeExInfo attrExInfo = new DocAttributeExInfo(e);
				if (isNotEmpty(e.getOptionId())) {
					attrExInfo.optionItems = this.getOptionItems(corporationCode, e.getOptionId());
				}
				attributeExs.add(attrExInfo);
			});
		}
		return attributeExs;
	}

	/**
	 * 文書メタ情報一覧取得.
	 * @param docId 文書ID
	 * @param metaTemplateId メタテンプレートID
	 * @return
	 */
	public List<MwtDocMetaInfoEx> getMwtDocMetaInfoList(Long docId, Long metaTemplateId) {
		if (isEmpty(docId)) {
			return null;
		}
		return repository.getMwtDocMetaInfoList(docId, metaTemplateId, sessionHolder.getLoginInfo().getLocaleCode());
	}

	/**
	 * 文書属性(拡張)一覧取得.
	 * @param docId 文書ID
	 * @param metaTemplateId メタテンプレートID
	 * @return
	 */
	public List<DocAttributeExInfo> getDocAttributeExList(Long docId, Long metaTemplateId, String corporationCode) {
		final List<DocAttributeExInfo> attributeExs = new ArrayList<>();
		final List<MwtDocMetaInfoEx> list = this.getMwtDocMetaInfoList(docId, metaTemplateId);
		if (list != null) {
			list.stream().forEach(e -> {
				DocAttributeExInfo attrExInfo = new DocAttributeExInfo(e);
				if (isNotEmpty(e.getOptionId())) {
					attrExInfo.optionItems = this.getOptionItems(corporationCode, e.getOptionId());
				}
				attributeExs.add(attrExInfo);
			});
		}
		return attributeExs;
	}

	/** 文書メタ情報の差分更新 */
	public void saveMwtDocMetaInfo(Long docId, List<DocAttributeExInfo> attributeExs) {
		// 文書IDに紐づく文書メタ情報一覧を取得し、文書メタIDをKeyにマップ形式に変換
		final Map<Long, MwtDocMetaInfo> map = repository.getMwtDocMetaInfoMap(docId, sessionHolder.getLoginInfo().getLocaleCode());
		for (DocAttributeExInfo inputed: attributeExs) {
			final MwtDocMetaInfo org = map.remove(inputed.docMetaId);
			if (org == null) {
				repository.insert(docId, inputed);
			} else {
				repository.update(org, inputed);
			}
		}
		// 余ったものは不要なので削除(物理削除)
		final Set<Long> deleteIds = map.keySet();
		repository.deleteMwtDocMetaInfo(deleteIds);
	}

	private List<OptionItem> getOptionItems(String corporationCode, Long optionId) {
		if (optionId == null) {
			return null;
		}
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		// デフォルト値の選択肢項目一覧
		final List<OptionItem> items = repository2.getMwmOptionItems(corporationCode, optionId, localeCode)
				.stream()
				.map(i -> new OptionItem(i.getCode(), i.getLabel()))
				.collect(Collectors.toList());
		return items;
	}
}
