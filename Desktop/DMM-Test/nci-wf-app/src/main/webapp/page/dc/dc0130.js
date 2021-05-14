$(function() {
	var pager = new Pager($('#seach-result'), '/dc0130/search', search).init();
	NCI.init("/dc0130/init", {messageCds: []}).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);

			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			} else {
				$('#deleteFlag').val('0');
			};
		}
	});

	$(document)
	.on('click', '#btnSearch', function(ev) {
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
		$('#deleteFlag').val('0');
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		var $tr = $(this).closest('tr');
		var corporationCode = $tr.find('[data-field=corporationCode]').text();
		var menuRoleCode = $tr.find('[data-field=menuRoleCode]').text();
		openEntry(corporationCode, menuRoleCode);
	})

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) return false;
		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'MR.MENU_ROLE_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, menuRoleCode) {
		NCI.redirect("./dc0131.html?corporationCode=" + corporationCode + "&menuRoleCode=" + menuRoleCode);
	}
});

