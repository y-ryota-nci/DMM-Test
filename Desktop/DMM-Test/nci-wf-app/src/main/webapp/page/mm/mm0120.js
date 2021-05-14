$(function() {
	var pager = new Pager($('#seach-result'), '/mm0120/search', search).init();

	var params = { messageCds : ['MSG0120', 'informationSetting'] };
	NCI.init("/mm0120/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#corporationCode'), res.corporations).val(NCI.loginInfo.corporationCode);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#ymd').val(res.ymd);
				$('#hhmm').val(res.hhmm);
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
			// 検索結果の選択チェックボックス
			.on('change', 'input[type=checkbox].selectable', function() {
				var noSelect = $('input[type=checkbox].selectable:checked').length === 0;
				disableButtonsByCheckbox(noSelect);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var $tr = $(this).closest('tr');
				var announcementId = $tr.find('[data-field=announcementId]').text();
				let version = $tr.find('[data-field=version]').text();
				openEntry(announcementId, version);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				openEntry(null);
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				var msg = NCI.getMessage("MSG0072", NCI.getMessage("informationSetting"));
				if (NCI.confirm(msg, function() {
					var params = { announcementIds : [] };
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						var $tr = $(elem).closest('tr');
						var announcementId = $tr.find('[data-field=announcementId]').text();
						params.announcementIds.push(announcementId);
					});
					NCI.post("/mm0120/delete", params).done(function(res) {
						search(1, true);
					});
				}));
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
		disableButtonsByCheckbox(true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'A.TIMESTAMP_START, A.TIMESTAMP_END, A.ANNOUNCEMENT_ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(announcementId, version) {
		if (announcementId)
			NCI.redirect("./mm0121.html?announcementId=" + announcementId + "&version=" + version);
		else
			NCI.redirect("./mm0121.html");
	}

	function disableButtonsByCheckbox(disable) {
		$('#btnDelete').prop('disabled', disable);
	}
});

