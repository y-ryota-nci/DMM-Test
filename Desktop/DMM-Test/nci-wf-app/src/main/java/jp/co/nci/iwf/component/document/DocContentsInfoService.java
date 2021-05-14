package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.renderer.IPartsRenderer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocAttributeExInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocFileInfo;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocContentsInfo;

/**
 * 文書コンテンツ情報サービス.
 */
@BizLogic
public class DocContentsInfoService extends BaseService {

	@Inject private DocContentsInfoRepository repository;
	/** パーツレンダラーのファクトリー */
	@Inject private PartsRenderFactory factory;

	/**
	 * 文書コンテンツ情報の差分更新.
	 * @param docId 文書ID
	 * @param docInfo 画面上で入力された文書情報
	 * @param attributeExs 文書属性(拡張)情報
	 * @param docFiles 文書ファイル情報
	 * @param ctx 文書内容情報（業務文書の場合のみ）
	 * @return 文書ID
	 */
	public void saveMwtDocContentsInfo(Long docId, DocInfo docInfo, List<DocAttributeExInfo> attributeExs, List<DocFileInfo> docFiles, RuntimeContext ctx) {
		final List<String> contentsList = new ArrayList<>();
		// 文書情報からは「件名」「コメント」を対象
		contentsList.add(docInfo.title);
		contentsList.add(docInfo.comments);
		// 文書属性(拡張)情報からは「メタ情報1」を対象
		if (attributeExs != null) {
			attributeExs.stream().forEach(e -> {
				contentsList.add(e.metaValue1);
			});
		}
		// 文書ファイル情報からは「ファイル名」を対象
		if (docFiles != null) {
			docFiles.stream().forEach(e -> {
				contentsList.add(e.fileName);
			});
		}
		// 文書内容情報からは表示用の値を取得
		// 例：ラジオボタンやドロップダウンであれば選択したデータのラベル名、組織選択やユーザ選択であれば組織名／ユーザ名など
		if (ctx != null && ctx.runtimeMap != null) {
			final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);
			contentsList.addAll( this.getDisplayValues(root, ctx) );
		}
		// コンテンツ情報を半角スペースにて連結
		String docContents = contentsList.stream().filter(s -> StringUtils.isNotBlank(s)).collect(Collectors.joining(" "));

		final MwtDocContentsInfo org = repository.getMwtDocContent(docId);
		if (org == null) {
			final MwtDocContentsInfo entity = new MwtDocContentsInfo();
			entity.setDocId(docId);
			entity.setDocContents(docContents);
			repository.insert(entity);
		} else {
			org.setDocContents(docContents);
			repository.update(org);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> getDisplayValues(PartsContainerBase<?> container, RuntimeContext ctx) {
		final List<String> values = new ArrayList<>();
		for (PartsContainerRow row : container.rows) {
			row.runtimeId = null;
			for (String htmlId : row.children) {
				final PartsBase p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);
				final IPartsRenderer r = factory.get(d.partsType);
				final String val = r.getDisplayValue(p, d, ctx);
				if (isNotEmpty(val)) {
					values.add(val);
				}
				// 子要素があれば再帰呼び出し
				if (d instanceof PartsDesignContainer) {
					values.addAll(this.getDisplayValues((PartsContainerBase<?>)p, ctx));
				}
			}
		}
		return values;
	}
}
