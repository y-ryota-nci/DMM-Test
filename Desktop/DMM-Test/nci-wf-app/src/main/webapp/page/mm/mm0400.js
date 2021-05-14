$(function() {
	const pager = new Pager($('#seach-result'), '/mm0400/search', search).init();

	let params = { messageCds : ['MSG0072', 'MSG0120', 'corporationGroup'] };
	NCI.init("/mm0400/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#deleteFlag').val('0');
			}

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				let pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				const $tr = $(this).closest('tr');
				const corporationGroupCode = $tr.find('[data-field=corporationGroupCode]').text();
				const timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationGroupCode, timestampUpdated);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		let $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		let cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'G.CORPORATION_GROUP_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationGroupCode, timestampUpdated) {
		if (corporationGroupCode)
			NCI.redirect("./mm0401.html" +
					"?corporationGroupCode=" + corporationGroupCode +
					"&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
		else
			NCI.redirect("./mm0401.html");
	}
});

