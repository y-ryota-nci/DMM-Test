$(function() {
	let pager = new Pager($('#seach-result'), '/ti0050/search', search).init();
	let tableId = NCI.getQueryString("tableId");
	let params = { "tableId" : tableId, "messageCds" : ['tableSearchCondition'] };
	NCI.init('/ti0050/init', params).done(function(res) {
		if (res && res.success) {
			$('#tableId').val(res.table.tableId);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags).val("0");

			NCI.toElementsFromObj(res.table, $('#divTable'));

			// 検索
			search();

			// リセットボタン押下
			$(document).on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				var pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field=tableSearchCode]', function(ev) {
				let $tr = $(this).closest('tr');
				let tableId = $tr.find('[data-field=tableId]').text();
				let tableSearchId = $tr.find('[data-field=tableSearchId]').text();
				let version = $tr.find('[data-field=version]').text();
				openEntry(tableId, tableSearchId, version);
			})
			// 行選択チェックボックス押下
			.on('click', 'input[type=checkbox].selectable', function() {
				let notSelected = $('input[type=checkbox].selectable:checked').length == 0;
				$('#btnDelete').prop('disabled', notSelected);
			})
			;

			// 検索ボタン押下
			$('#btnSearch').click(function(ev) {
				search(1);
				return false;
			});
			// リセットボタン押下
			$('#btnReset').click(function(ev) {
				$('#formCondition')[0].reset();
			})
			// 追加ボタン押下
			$('#btnAdd').click(function() {
				NCI.redirect('./ti0051.html?tableId=' + $('#tableId').val());
			}).prop('disabled', false);
			// 削除ボタン
			$('#btnDelete').click(execDelete);
		}
		// 戻るボタン押下
		$('#btnBack').click(function(ev) {
			NCI.redirect('./ti0030.html')
		});
	});

	function execDelete(ev) {
		let tableSearchIds = [];
		$('input[type=checkbox].selectable:checked').each(function(i, elem) {
			tableSearchIds.push(elem.value);
		});
		if (tableSearchIds.length === 0)
			return false;
		let msg = NCI.getMessage('MSG0072', NCI.getMessage('tableSearchCondition'));
		NCI.confirm(msg, function() {
			let params = { "tableSearchIds" : tableSearchIds };
			NCI.post('/ti0050/delete', params).done(function(res) {
				search(1, true);
			});
		});
	}

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
			cond.sortColumn = 'S.TABLE_SEARCH_CODE, S.TABLE_SEARCH_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(tableId, tableSearchId, version) {
		NCI.redirect("./ti0051.html" +
				"?tableId=" + tableId +
				"&tableSearchId=" + tableSearchId +
				"&version=" + version);
	}
});
