$(function() {
	var pager = new Pager($('#seach-result'), '/wm0200/search', search).init();

	var params = {
		messageCds : []
	};
	NCI.init("/wm0200/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
				$('#deleteFlag').val('0');
			}
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
		var assignRoleCode = $tr.find('[data-field=assignRoleCode]').text();
		openEntry(corporationCode, assignRoleCode);
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		NCI.redirect("./wm0210.html?corporationCode=" + NCI.loginInfo.corporationCode);

	})
	;

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
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
			cond.sortColumn = 'ASSIGN_ROLE_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, assignRoleCode) {
		NCI.redirect("./wm0220.html?corporationCode=" + corporationCode + "&assignRoleCode=" + assignRoleCode);
	}
});

