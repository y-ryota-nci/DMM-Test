$(function() {
	const pager = new Pager($('#seach-result'), '/wl0010/search', search).init();
	pager.responsiveTable.modifyTR = modifyTR;	// 標準機能を上書き

	const params = { messageCds : ['MSG0120', 'trayConfig'] };
	NCI.init("/wl0010/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#systemFlag'), res.systemFlags);
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
			// 検索結果の選択チェックボックス
			.on('change', 'input[type=checkbox].selectable', function() {
				disableButtonsByCheckbox();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				const $tr = $(this).closest('tr');
				const trayConfigId = $tr.find('[data-field=trayConfigId]').val();
				const version = $tr.find('[data-field=version]').text();
				openEntry(trayConfigId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				let msg = NCI.getMessage("MSG0072", NCI.getMessage("trayConfig"));
				if (NCI.confirm(msg, function() {
					let params = { trayConfigIds : [] };
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						const $tr = $(elem).closest('tr');
						const trayConfigId = $tr.find('[data-field=trayConfigId]').val();
						params.trayConfigIds.push(trayConfigId);
					});
					NCI.post("/wl0010/delete", params).done(function(res) {
						search(1, true);
					});
				}));
			})
			;
			// ダウンロードボタン
			$('#btnDownload').click(function() {
				NCI.download('/wl0010/downloadZip/' + NCI.loginInfo.corporationCode);
			}).prop('disabled', false);
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
		disableButtonsByCheckbox(true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'TC.SORT_ORDER, TC.TRAY_CONFIG_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(trayConfigId, version) {
		let url = "./wl0011.html?from=wl0010";
		if (trayConfigId) {
			url += "&trayConfigId=" + trayConfigId + "&version=" + version;
		}
		NCI.redirect(url);
	}

	function disableButtonsByCheckbox(disable) {
		const len = $('input[type=checkbox].selectable:checked').length;
		$('#btnDelete, #btnSyncDB').prop('disabled', (len === 0));
		$('#btnDownloadExcel').prop('disabled', (len !== 1));
	}

	/** Pagerの行レンダリングのカスタマイズ。行のレンダリングごとに呼び出される */
	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=checkbox].selectable')
			.toggleClass('hide', (entity.systemFlag === '1'))
			.prop('checked', false);
	}
});

