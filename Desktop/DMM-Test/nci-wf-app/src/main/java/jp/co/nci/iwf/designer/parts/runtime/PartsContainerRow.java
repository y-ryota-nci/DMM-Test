package jp.co.nci.iwf.designer.parts.runtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】コンテナパーツの行クラス。
 * この行内に列として子パーツをもつ
 */
public class PartsContainerRow {
	/** 行ID（htmlIdの元になる番号。いったん行がインスタンス化されたら不変）*/
	public int rowId;
	/** 並び順（表示用、テーブルデータには無関係）  */
	public int sortOrder;
	/** テーブル更新時のユニークキー */
	public Long runtimeId;
	/** テーブル更新時の楽観排他キーであるバージョン */
	public Long version;
	/** 列要素である子パーツのHtmlID */
	public List<String> children = new ArrayList<>();

	/**
	 * この行を入力値ありの行とみなすか
	 * @param inputedJudgePartsIds 入力判定パーツIDリスト
	 * @param ctx デザイナーコンテキスト
	 * @param ecResults 有効条件キャッシュ
	 * @return
	 */
	@JsonIgnore
	public boolean isInputed(List<BigDecimal> inputedJudgePartsIds, DesignerContext ctx, Map<String, EvaluateCondition> ecResults) {
		// 比較用にBigDecimalリストをLongリストに変換
		final Set<Long> targets = new HashSet<>();
		for (Number v : inputedJudgePartsIds) {
			if (v != null)
				targets.add(v.longValue());
		}
		for (String htmlId : children) {
			// 有効条件として無効なら不要
			final EvaluateCondition ec = ecResults.get(htmlId);
			if (ec != null && !ec.enabled)
				continue;

			// 入力判定パーツが未定義なら、行内のいずれかのパーツに値が設定されていれば入力行とみなす
			// 入力判定パーツが定義済みでそのパーツに値が設定されていたら入力行とみなす（複数定義されていても、いずれか１つに値があれば入力行とする）
			final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
			if (targets.isEmpty() || targets.contains(p.partsId)) {
				// 一般的なパーツ（値があれば入力済みとする）
				if (MiscUtils.isNotEmpty(p.getValue())) {
					return true;
				}
				// 添付ファイルパーツ（添付されていれば入力済みとする）
				if (p instanceof PartsAttachFile) {
					final PartsAttachFile attach = (PartsAttachFile)p;
					for (PartsAttachFileRow row : attach.rows) {
						if (row.partsAttachFileWfId != null) {
							return true;
						}
					}
				}
				// 画像パーツ（実行時にアップロードされた画像があれば入力済みとする）
				if (p instanceof PartsImage) {
					final PartsImage image = (PartsImage)p;
					if (image.partsAttachFileWfId != null) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
