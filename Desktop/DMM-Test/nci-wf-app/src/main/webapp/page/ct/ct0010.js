$(function() {
	// 遷移元からの受信パラメータ
	var params = {
		"corporationCode" : NCI.getQueryString("corporationCode"),
	};
	var pager = new Pager($('#seach-result'), '/ct0010/search', search).init();
	NCI.init("/ct0010/init").done(function(res) {
		if (res && res.success) {
			// 企業グループの選択肢
			NCI.createOptionTags($('#catalogCategoryId'), res.catalogCategories).val('');

			if (pager.loadCondition()) {
				search();
			}
			$('#btnAdd').prop('disabled', false);

			$(document)
			.on('click', '#btnSearch', function() {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function() {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var $tr = $(this).closest('tr');
				var catalogId = $tr.find('[data-field=catalogId]').text();
				openEntry(catalogId);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				NCI.redirect("./ct0011.html");
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'C.CATALOG_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(catalogId) {
		var params = {'catalogId': catalogId};
		NCI.redirect("./ct0011.html?" + $.param(params));
	}
});

