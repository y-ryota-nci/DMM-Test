$(function() {
	let pager = new Pager($('#seach-result'), '/mm0440/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};

	let params = { messageCds : ['MSG0003', 'MSG0070', 'user'] };
	NCI.init("/mm0440/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#corporationCode'), res.corporations);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);
			NCI.createOptionTags($('#defaultLocaleCode'), res.localeCodes);
			$('#btnAdd').prop('disabled', false)

			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			} else {
				$('#corporationCode').val(NCI.loginInfo.corporationCode);
				$('#deleteFlag').val("0");
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
			.on('click', 'input[name=selectable]', function() {
				let noSelect = $('input[name=selectable]:checked').length === 0;
				disableButtonsByCheckbox(noSelect);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				const $tr = $(this).closest('tr');
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const userCode = $tr.find('[data-field=userCode]').text();
				const timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationCode, userCode, timestampUpdated);
			})
			// ユーザ追加ボタン
			.on('click', '#btnAdd', function(ev) {
				const msg = NCI.getMessage('MSG0070', NCI.getMessage('user'));
				NCI.confirm(msg, function() {
					const params = {
							"corporationCode" : $('#corporationCode').val()
					};
					NCI.post('/mm0440/addUser', params).done(function(res) {
						if (res && res.success) {
							// ユーザ編集画面へ遷移
							openEntry(res.newUser.corporationCode, res.newUser.userCode, res.newUser.timestampUpdated);
						}
					});
				});
				return false;
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				const user = getSelectedUser();
				if (user == null) {
					const $radio = $('input[name=selectable]').first();
					const msg = NCI.getMessage('MSG0003', NCI.getMessage('user'));
					Validator.showBalloon($radio, msg);
				}

				const msg = NCI.getMessage("MSG0072", NCI.getMessage("user"));
				if (NCI.confirm(msg, function() {
					const params = { "user" : user, "baseDate" : NCI.today() };
					NCI.post("/mm0440/delete", params).done(function(res) {
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
			cond.sortColumn = 'A.USER_ADDED_INFO, A.ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, userCode, timestampUpdated) {
		let url = "./mm0003.html";
		if (corporationCode)
			url += "?corporationCode=" + corporationCode;
		if (userCode)
			url += "&userCode=" + userCode;
		if (timestampUpdated)
			url += "&timestampUpdated=" + NCI.timestampToLong(timestampUpdated);

		NCI.redirect(url);
	}

	function disableButtonsByCheckbox(disable) {
		$('#btnDelete').prop('disabled', disable);
	}

	function getSelectedUser() {
		const $tr = $('input[name=selectable]:checked').closest('tr');
		const user = {
				"corporationCode" : $tr.find('span[data-field=corporationCode]').text(),
				"userCode" : $tr.find('span[data-field=userCode]').text(),
				"timestampUpdated" : $tr.find('span[data-field=timestampUpdated]').text()
		};
		return user;
	}
});

