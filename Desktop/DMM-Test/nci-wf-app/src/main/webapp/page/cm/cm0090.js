$(function() {
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let pager = new Pager($('#seach-result'), '/cm0090/search', search).init();
	pager.responsiveTable.modifyTR = modifyTR;
	NCI.init("/cm0090/init").done(function(res) {
		if (res && res.success) {
			$('#corporationCode').val(params['corporationCode']);
			$('#docFlag').val(params['docFlag']);
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
		let pageNo = this.getAttribute('data-pageNo');
		search(pageNo);
		return false;
	})
	// 検索結果の選択ラジオボタン
	.on('change', 'input[type=radio][name=rdoSelect]', function() {
		$('#btnSelect').prop('disabled', false);
	})
	// 検索結果のリンク押下
	.on('click', 'a[data-field]', function(ev) {
		const $tr = $(this).closest('tr');
		const result = NCI.toObjFromElements($tr);
		Popup.close(result);
		return false;
	})
	.on('click', '#btnSelect', function() {
		const $selected = $('input[type=radio][name=rdoSelect]:checked');
		const $tr = $selected.closest('tr');
		const result = NCI.toObjFromElements($tr);
		Popup.close(result);
		return false;
	})
	.on('click', 'button.btnClose', function(e) {
		Popup.close();
		return false;
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
//		$('#seach-result').removeClass('hide');
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
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

	function modifyTR($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	}
});

