$(function() {
	let pager = new Pager($('#seach-result'), '/vd0010/search', search).init();

	let params = { messageCds : ['MSG0120', 'containerInfo'] };
	NCI.init("/vd0010/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#syncTable'), res.syncTableList);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
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
				let $tr = $(this).closest('tr');
				let containerId = $tr.find('[data-field=containerId]').text();
				let version = $tr.find('[data-field=version]').text();
				openEntry(containerId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				let msg = NCI.getMessage("MSG0072", NCI.getMessage("containerInfo"));
				if (NCI.confirm(msg, function() {
					let params = { containerIds : [] };
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						let $tr = $(elem).closest('tr');
						let containerId = $tr.find('[data-field=containerId]').text();
						params.containerIds.push(containerId);
					});
					NCI.post("/vd0010/delete", params).done(function(res) {
						search(1, true);
					});
				}));
			})
			// DBと同期ボタン押下
			.on('click', '#btnSyncDB', function(ev) {
				let msg = NCI.getMessage("MSG0120");
				if (NCI.confirm(msg, function() {
					let containerIds = [];
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						let $tr = $(elem).closest('tr');
						let containerId = $tr.find('[data-field=containerId]').text();
						containerIds.push(containerId);
					});
					let params = { 'containerIds' : containerIds };
					NCI.post('/vd0010/syncDB', params).done(function(res) {
						search(1, true);
					});
				}));
			})
			// EXCELダウンロードボタン押下
			.on('click', '#btnDownloadExcel', function(ev) {
				const containerIds = [];
				$('input[type=checkbox].selectable:checked').each(function(i, elem) {
					let $tr = $(elem).closest('tr');
					let containerId = $tr.find('[data-field=containerId]').text();
					containerIds.push(containerId);
				});
				const params = { "containerIds" : containerIds };
				NCI.download('/vd0010/downloadExcel', params);
			})
			.on('click', '#btnSelectAll', function(){
				selectAll(true);
			})
			.on('click', '#btnCancelAll', function(){
				selectAll(false);
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
		disableButtonsByCheckbox(true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'C.CONTAINER_CODE, C.CONTAINER_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(containerId, version) {
		if (containerId)
			NCI.redirect("./vd0110.html?containerId=" + containerId + "&version=" + version);
		else
			NCI.redirect("./vd0110.html");
	}

	function disableButtonsByCheckbox(disable) {
		const len = $('input[type=checkbox].selectable:checked').length;
		$('#btnDelete, #btnSyncDB').prop('disabled', (len === 0));
		$('#btnDownloadExcel').prop('disabled', (len !== 1));
	}

	/** 全選択／全解除 */
	function selectAll(checked) {
		$('tbody input[type=checkbox].selectable').prop('checked', checked);
		disableButtonsByCheckbox(!checked);
	}
});

