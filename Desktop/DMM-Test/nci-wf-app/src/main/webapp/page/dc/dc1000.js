$(function() {
	const pager = new Pager($('#seach-result'), '/dc1000/search', search).init();

	const params = {};
	NCI.init("/dc1000/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#ocrFlag'), res.ocrFlagItems).val('0');
			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
//				search();
			}

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			search();

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				const pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage).done(function() {
			$('#seach-result').removeClass('hide');
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'P.APPLICATION_NO,F.DOC_FILE_DATA_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

});

