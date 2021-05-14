$(function() {
	let pager = new Pager($('#seach-result'), '/ti0030/search', search).init();
	let params = {};
	NCI.init("/ti0030/init", params).done(function(res) {
		if (res && res.success) {

			NCI.createOptionTags($('#entityType'), res.entityTypes);
			NCI.createOptionTags($('#categoryId'), res.categories);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果のリンク押下
			.on('click', 'button.btnTableDef, button.btnSelectCondition', function(ev) {
				let $tr = $(this).closest('tr');
				let tableId = $tr.find('[data-field=tableId]').text();
				let version = $tr.find('[data-field=version]').text();
				openEntry(tableId, version, this);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'T.TABLE_NAME';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(tableId, version, button) {
		let $button = $(button);
		if ($button.hasClass('btnTableDef'))
			NCI.redirect("./ti0040.html?tableId=" + tableId + "&version=" + version);
		else if ($button.hasClass('btnSelectCondition'))
			NCI.redirect("./ti0050.html?tableId=" + tableId);
	}
});

