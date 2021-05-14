$(function() {
	const pager = new Pager($('#seach-result'), '/wl0100/search', search).init();
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	NCI.init("/wl0100/init", params).done(function(res) {
		if (res && res.success) {
			// 企業の選択肢
			NCI.createOptionTags($('#corporationCode'), res.corporations);
			// 主務兼務区分の選択肢
			NCI.createOptionTags($('#jobType'), res.jobTypes);

			// 初期値を反映
			NCI.toElementsFromObj(res, $('#condition-contents'))
			// 初期検索
			search();
		}
	});

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
	// 選択ボタン押下
	.on('click', '#btnSelect', function(ev) {
		const $tr = $('input[name=rdoSelect]:checked').closest('tr');
		const entity = NCI.toObjFromElements($tr)
		// コールバック関数の呼び出し
		Popup.close(entity);
	})
	// 検索結果の選択用ラジオボタン
	.on('click', 'input[name=rdoSelect]', function(ev) {
		setButtonEnabled();
	})
	// 閉じるボタン
	.on('click', 'button.btnClose', function(ev) {
		Popup.close();
	})
	;

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		let $targets = $('#condition-contents').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		let cond = createCondition(pageNo);
		pager.search(cond, keepMessage);
		$('#seach-result').removeClass('invisible');
		$('#btnSelect').prop('disabled', true);
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#condition-contents').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'USER_ADDED_INFO, USER_NAME, ORGANIZATION_NAME, POST_NAME';
			cond.sortAsc = true;
		}
		return cond;
	}

	function setButtonEnabled() {
		const disabled = ($('input[name=rdoSelect]:checked').length === 0);
		$('#btnSelect').prop('disabled', disabled);
	}
});
