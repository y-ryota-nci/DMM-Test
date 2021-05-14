$(function() {
	const pager = new Pager($('#seach-result'), '/vd0090/search', search).init();
	pager.responsiveTable.modifyTR = modifyTR;	// 標準メソッドを上書き

	const params = { messageCds : ['MSG0072', 'screenProcessMenuInfo'] };
	NCI.init("/vd0090/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#processDefCode'), res.processDefs);

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
				const pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果の選択チェックボックス
			.on('change', 'input[type=checkbox].selectable', function() {
				disableButtonsByCheckbox();
			})
			// 検索結果のリンク押下
			.on('click', 'a.key', function(ev) {
				const $tr = $(this).closest('tr');
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const menuId = $tr.find('[data-field=menuId]').text();
				const screenProcessMenuId = $tr.find('[data-field=screenProcessMenuId]').text();
				const version = $tr.find('[data-field=version]').text();
				openEntry(corporationCode, menuId, screenProcessMenuId, version);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				const msg = NCI.getMessage("MSG0072", NCI.getMessage("screenProcessMenuInfo"));
				if (NCI.confirm(msg, function() {
					const params = { screenProcessMenuIds : [] };
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						const $tr = $(elem).closest('tr');
						const screenProcessMenuId = $tr.find('[data-field=screenProcessMenuId]').text();
						params.screenProcessMenuIds.push(screenProcessMenuId);
					});
					NCI.post("/vd0090/delete", params).done(function(res) {
						if (res && res.success)
							search(1, true);
					});
				}));
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
		pager.search(cond, keepMessage);
		disableButtonsByCheckbox(true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		const $elements = $('#formCondition').find('input, select, textarea');
		const cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'MENU_NAME, SCREEN_PROCESS_MENU_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, menuId, screenProcessMenuId, version) {
		if (menuId)
			NCI.redirect("./vd0091.html"
					+ "?corporationCode=" + corporationCode
					+ "&menuId=" + menuId
					+ "&screenProcessMenuId=" + screenProcessMenuId
					+ "&version=" + version);
	}

	function disableButtonsByCheckbox(disable) {
		const len = $('input[type=checkbox].selectable:checked:visible').length;
		if (disable)
			$('#btnDelete').prop('disabled', true);
		else
			$('#btnDelete').prop('disabled', (len === 0));
	}

	/** テンプレートから生成した行レイアウトを、エンティティ内容で補正 */
	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		const hidden = !entity.screenProcessMenuId;
		$tr.find('input[type=checkbox].selectable')
			.toggleClass('hide', hidden)
			.prop('checked', false);
	}
});

