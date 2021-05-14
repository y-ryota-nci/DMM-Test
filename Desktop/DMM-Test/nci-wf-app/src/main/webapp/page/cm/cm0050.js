$(function() {
	var pager = new Pager($('#seach-result'), '/cm0050/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};
	NCI.init("/cm0050/init").done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// 企業が指定されていればその企業コードを、なければ自社
			NCI.createOptionTags($('#corporationCode'), res.corporations)
					.val(NCI.getQueryString("corporationCode") || NCI.loginInfo.corporationCode);

			// 組織コードが指定されてあればその組織配下に属するユーザのみを検索対象とする
			$('#organizationCode').val(NCI.getQueryString("organizationCode"));

			// DMM版では初期検索する
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
				var $tr = $('input[name=rdoSelect]:checked').parent().parent();
				var entity = {};
				$tr.find('[data-field]').each(function(i, elem) {
					var fieldName = elem.getAttribute('data-field');
					entity[fieldName] = $(elem).text();
				});

				// コールバック関数の呼び出し
				Popup.close(entity);
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond);
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'USER_ADDED_INFO, USER_NAME, ORGANIZATION_NAME, POST_NAME';
			cond.sortAsc = true;
		}
		return cond;
	}
});

