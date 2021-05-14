$(function() {
	let pager = new Pager($('#seach-result'), '/vd0030/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[name=screenId]').prop('disabled', false);
	};

	let params = { messageCds : ['screenInfo'] };
	NCI.init("/vd0030/init", params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#scratchFlag'), res.scratchFlags);
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
			.on('change', '#screens input[name=screenId]', function() {
				const screenIds = getSelectedScreenIds();
				const noSelect = (screenIds == null || screenIds.length === 0);
				disableButtonsByCheckbox(noSelect);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				let $tr = $(this).closest('tr');
				let screenId = $tr.find('[data-field=screenId]').val();
				let version = $tr.find('[data-field=version]').text();
				openEntry(screenId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				const screenIds = getSelectedScreenIds();
				if (screenIds != null && screenIds.length > 0) {
					let msg = NCI.getMessage("MSG0072", NCI.getMessage("screenInfo"));
					if (NCI.confirm(msg, function() {
						const params = { 'screenIds' : screenIds };
						NCI.post("/vd0030/delete", params).done(function(res) {
							search(1, true);
						});
					}));
				}
			})
			// ダウンロードボタン
			.on('click', '#btnDownload', function() {
				const screenIds = getSelectedScreenIds();
				if (screenIds != null && screenIds.length > 0) {
					const params = { 'screenIds' : screenIds };
					NCI.download('/vd0030/downloadZip',  params);
				}
			})
			// EXCELダウンロードボタン押下
			.on('click', '#btnDownloadExcel', function(ev) {
				const screenIds = [];
				$('input[name=screenId]:checked').each(function(i, elem) {
					screenIds.push(elem.value);
				});
				const params = { "screenIds" : screenIds };
				NCI.download('/vd0030/downloadExcel', params);
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
		pager.search(cond, keepMessage).done(function() {
			disableButtonsByCheckbox(true);
			$('#seach-result').removeClass('hide');
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'SCREEN_CODE, SCREEN_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 選択行のデータを開く */
	function openEntry(screenId, version) {
		if (screenId)
			NCI.redirect("./vd0031.html?screenId=" + screenId + "&version=" + version);
		else
			NCI.redirect("./vd0031.html");
	}

	/** 検索結果の選択行によるボタン制御 */
	function disableButtonsByCheckbox(disable) {
		$('#btnDelete, #btnPreview, #btnDownload').prop('disabled', disable);
		const len = $('input[name=screenId]:checked').length;
		$('#btnDownloadExcel').prop('disabled', (len !== 1));
	}

	/** 選択中の画面IDを返す */
	function getSelectedScreenIds() {
		const screenIds = [];
		$('#screens input[name=screenId]:checked').each(function() {
			screenIds.push(this.value);
		});
		return screenIds;
	}

	/** 全選択／全解除 */
	function selectAll(checked) {
		$('tbody input[type=checkbox][data-field=screenId]').prop('checked', checked);
		disableButtonsByCheckbox(!checked);
	}
});

