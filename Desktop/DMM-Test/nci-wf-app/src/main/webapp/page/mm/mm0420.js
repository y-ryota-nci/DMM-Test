$(function() {
	let pager = new Pager($('#seach-result'), '/mm0420/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};

	let params = { messageCds : ['MSG0003', 'MSG0070', 'organization'] };
	NCI.init("/mm0420/init", params).done(function(res) {
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
				// 上位組織マスタの内容もリセット hidden項目はリセットされないので手動で
				$('#organizationCodeUp, #organizationNameUp').val('');
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
				const organizationCode = $tr.find('[data-field=organizationCode]').text();
				const timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationCode, organizationCode, timestampUpdated);
			})
			// 組織追加ボタン
			.on('click', '#btnAdd', function(ev) {
				const msg = NCI.getMessage('MSG0070', NCI.getMessage('organization'));
				NCI.confirm(msg, function() {
					const params = {
							"corporationCode" : $('#corporationCode').val(),
							"baseDate" : NCI.today(),
							"displayValidOnly" : true
					};
					NCI.post('/mm0420/addOrg', params).done(function(res) {
						if (res && res.success) {
							// 組織編集画面へ遷移
							const newOrg = res.newOrganization;
							openEntry(newOrg.corporationCode, newOrg.organizationCode, newOrg.timestampUpdated);
						}
					});
				});
				return false;
			})
			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				const $tr = $('input[name=selectable]:checked').closest('tr');
				if ($tr.length === 0) {
					const $radio = $('input[name=selectable]').first();
					const msg = NCI.getMessage('MSG0003', NCI.getMessage('organization'));
					Validator.showBalloon($radio, msg);
				}
				const msg = NCI.getMessage("MSG0072", NCI.getMessage("organization"));
				if (NCI.confirm(msg, function() {
					const org = {
							"corporationCode" : $tr.find('span[data-field=corporationCode]').text(),
							"organizationCode" : $tr.find('span[data-field=organizationCode]').text(),
							"timestampUpdated" : $tr.find('span[data-field=timestampUpdated]').text()
					};
					const params = { "org" : org, "baseDate" : NCI.today() };
					NCI.post("/mm0420/delete", params).done(function(res) {
						search(1, true);
					});
				}));
			})
			// 上位組織選択ボタン
			.on('click', '#btnSelectOrg', function() {
				// 組織選択（企業を選択済みならその企業の）
				const params = null;
				const url = "../cm/cm0020.html?initSearch=true&corporationCode=" + NCI.loginInfo.corporationCode;
				Popup.open(url, callbackFromPopup, params, this);
				return false;
			})
			// 上位組織クリアボタン
			.on('click', '#btnClearOrgUp', function() {
				$('#organizationCodeUp, #organizationNameUp').val('');
				return false;
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
			cond.sortColumn = 'H.ORGANIZATION_TREE_NAME, A.SORT_ORDER, A.ORGANIZATION_ADDED_INFO, A.ID';
			cond.sortAsc = true;
		}
		return cond;
	}

	function openEntry(corporationCode, organizationCode, timestampUpdated) {
		let url = "./mm0002.html";
		url += "?corporationCode=" + corporationCode;
		url += "&organizationCode=" + organizationCode;
		url += "&timestampUpdated=" + NCI.timestampToLong(timestampUpdated);
		NCI.redirect(url);
	}

	function disableButtonsByCheckbox(disable) {
		$('#btnDelete').prop('disabled', disable);
	}

	function callbackFromPopup(org) {
		if (org) {
			$('#organizationCodeUp').val(org.organizationCodeUp);
			$('#organizationNameUp').val(org.organizationNameUp);
		}
	}
});

