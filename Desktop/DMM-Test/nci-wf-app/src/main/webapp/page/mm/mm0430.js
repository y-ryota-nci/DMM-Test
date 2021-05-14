$(function() {
	var pager = new Pager($('#seach-result'), '/mm0430/search', search).init();

	var params = {
		messageCds : [ 'MSG0072', 'post' ]
	};
	NCI.init("/mm0430/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
				$('#deleteFlag').val('0');
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
				var selected = $('input[type=checkbox].selectable:checked').length === 0;
				$('#btnDelete').prop('disabled', selected);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				var $tr = $(this).closest('tr');
				var corporationCode = $tr.find('[data-field=corporationCode]').text();
				var postCode = $tr.find('[data-field=postCode]').text();
				var timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationCode, postCode, timestampUpdated);
			})
			// 追加ボタン
			.on('click', '#btnAdd', function(ev) {
				var msg = NCI.getMessage("MSG0070", NCI.getMessage("post"));
				if (NCI.confirm(msg, function() {
					var params = { corporationCode : $("#corporationCode").val() };
					NCI.post("/mm0430/add", params).done(function(res) {
						if (res && res.success && res.newPost) {
							openEntry(res.newPost.corporationCode, res.newPost.postCode, res.newPost.timestampUpdated);
						}
					});
				}));
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				var msg = NCI.getMessage("MSG0072", NCI.getMessage("post"));
				if (NCI.confirm(msg, function() {
					var params = { deletePosts : [] };
					$('input[type=checkbox].selectable:checked').each(function(i, elem) {
						var $tr = $(elem).closest('tr');
						var post = {
							corporationCode : $tr.find('[data-field=corporationCode]').text(),
							postCode : $tr.find('[data-field=postCode]').text(),
							timestampUpdated : $tr.find('[data-field=timestampUpdated]').text()
						};
						params.deletePosts.push(post);
					});
					NCI.post("/mm0430/delete", params).done(function(res) {
						if (res && res.success) {
							search(1, true);
						}
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
		$('#btnDelete').prop('disabled', true);
		$('#seach-result').removeClass('hide');
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'POST_CODE, ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, postCode, timestampUpdated) {
		NCI.redirect("./mm0431.html?corporationCode=" + corporationCode + "&postCode=" + postCode + "&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
	}
});

