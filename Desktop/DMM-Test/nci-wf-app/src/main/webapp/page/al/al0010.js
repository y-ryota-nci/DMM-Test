$(function() {
	var pager = new Pager($('#seach-result'), '/al0010/search', search).init();

	NCI.init("/al0010/init").done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#opeCorporationCode'), res.corporations);
			NCI.createOptionTags($('#accessLogResultType'), res.accessLogResultTypes);

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				// アクセスログ詳細からパラメータが指定されていれば適用
				const queryStrings = NCI.getQueryString().split('&');
				$.each(queryStrings, function(i, queryString) {
					const values = queryString.split('=');
					$('#' + values[0]).val(values[1]);
				});

				search();
			}
			else {
				// 復元できなければ、サーバからもらった初期値を使う
				$('#opeCorporationCode').val(NCI.loginInfo.corporationCode);
				$('#accessDate').val(res.today)
				$('#accessTmFrom').val(res.now);
			}

			$(document).on('click', '#btnSearch', function(ev) {
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
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var accessLogId = $(this).text();
				openEntry(accessLogId);
			})
			;
		}
	});

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
			cond.sortColumn = 'AL.ACCESS_TIME, AL.ACCESS_LOG_ID';
			cond.sortAsc = false;
		}
		return cond;
	}

	function openEntry(accessLogId) {
		NCI.redirect("./al0011.html?accessLogId=" + accessLogId);
	}
});

