$(function() {
	var pager = new Pager($('#seach-result'), '/rm0000/search', search).init();

	var params = {
		messageCds : ['MSG0003', 'targetRow']
	};
	NCI.init("/rm0000/init", params).done(function(res) {
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
		var menuRoleCode = $tr.find('[data-field=menuRoleCode]').text();
		openEntry(corporationCode, menuRoleCode);
	})
	// 追加ボタン
	.on('click', '#btnAdd', function(ev) {
		NCI.redirect("./rm0010.html?corporationCode=" + NCI.loginInfo.corporationCode);

	})
	// ダウンロードボタン
	.on('click', '#btnDownload', downloadMenuRole)
	// チェックボックス押下によるボタン制御
	.on('click', 'input[type=checkbox].selectable', enableDownloadButton)
	// 全選択
	.on('click', '.btnSelectAll', function() { selectCheckbox(true); })
	// 全解除
	.on('click', '.btnCancelAll', function() { selectCheckbox(false); })
	;

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		var cond = createCondition(pageNo);
		pager.search(cond).done(function(res) {
			if (res && res.success) {
				$('#btnDownload').prop('disabled', res.results.length === 0);
			}
		});
		$('#seach-result').removeClass('hide');

		// チェックボックス選択によるダウンロードボタンの有効無効制御
		enableDownloadButton();
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'MENU_ROLE_CODE';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, menuRoleCode) {
		NCI.redirect("./rm0020.html?corporationCode=" + corporationCode + "&menuRoleCode=" + menuRoleCode);
	}

	/** チェックボックス選択によるダウンロードボタンの有効無効制御 */
	function enableDownloadButton() {
		const checked = $('tbody>tr input[type=checkbox].selectable:checked').length > 0;
		$('#btnDownload').prop('disabled', !checked);
	}

	/** メニューロール定義のダウンロード */
	function downloadMenuRole() {
		const $checkeds = $('tbody input[type=checkbox].selectable:checked');
		if ($checkeds.length === 0) {
			NCI.alert(NCI.getMessage('MSG0003', NCI.getMessage('targetRow')));
			return false;
		}

		const params = {
			"corporationCode": $('tbody>tr [data-field=corporationCode]').first().text(),
			"menuRoleCodes" : []
		};
		$checkeds.each(function(i, checkbox) {
			const $checkbox = $(checkbox);
			if ($checkbox.prop('checked')) {
				const $tr = $checkbox.closest('tr');
				const menuRoleCode = $tr.find('[data-field=menuRoleCode]').text();
				params.menuRoleCodes.push(menuRoleCode);
			}
		});
		NCI.download('/rm0000/downloadZip', params);
	}

	/** 全選択／全解除 */
	function selectCheckbox(checked) {
		$('tbody>tr input[type=checkbox].selectable').prop('checked', checked);
		enableDownloadButton();
	}
});

