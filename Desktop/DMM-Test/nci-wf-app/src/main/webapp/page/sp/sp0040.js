$(function() {
	let params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let pager = new Pager($('#seach-result'), '/sp0040/search', search).init();

	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
		$tr.find('input[type=radio]').data('entity', entity);
	};
	NCI.init("/sp0040/init").done(function(res) {
		if (res && res.success) {
			NCI.toElementsFromObj(params, $('#formCondition'))
			search(1);

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
			// 検索結果の選択ラジオボタン
			.on('change', 'input[type=radio][name=rdoSelect]', function() {
				$('#btnSelect').prop('disabled', false);
			})
			// 戻るボタン
			.on('click', 'button.btnClose', function() {
				Popup.close();
			})
			// 選択ボタン
			.on('click', '#btnSelect', function() {
				let entity = $('input[name=rdoSelect]:checked').data('entity');
				Popup.close(entity);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('#parentRuntimeId');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'SORT_ORDER';
			cond.sortAsc = true;
		}
		return cond;
	}
});

