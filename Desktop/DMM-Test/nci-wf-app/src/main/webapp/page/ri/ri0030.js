$(function() {
	const pager = new Pager($('#seach-result'), '/ri0030/search', search).init();

	const params = {};
	NCI.init("/ri0030/init", params).done(function(res) {
		if (res && res.success) {
			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}
			else {
				// 初期値
				$('#rcvinspStsApproved').prop('checked', true);
			}

			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			.on('click', 'ul.pagination a', function(ev) {
				// ページ番号リンク押下
				const pageNo = this.getAttribute('data-pageNo');
				search(pageNo);
				return false;
			})
			// リセットボタン押下
			.on('click', '#btnReset', function(ev) {
				$('#formCondition')[0].reset();
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				const $tr = $(this).closest('tr');
				const companyCd = $tr.find('[data-field=companyCd]').val();
				const rcvinspNo = $tr.find('[data-field=rcvinspNo]').text();
				openEntry(companyCd, rcvinspNo);
			})
			.on('click', '.btnSplrCd', function() {
				// 取引先の選択ポップアップ起動
				const conds = {"COMPANY_CD" : NCI.loginInfo.corporationCode };
				NCI.openMasterSearch('SPLR_MST', 'FOR_POPUP_ORD', callbackFromSelectSplr, conds);
			})
			.on('click', '.btnClearSplr', function() {
				// 取引先のクリアボタン
				clearForSplr();
			})
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
				whenSelectRow();
			})
			// 変更_検収申請
			.on('click', '#btnUpdateRequest', function() {
				// 選択されている検収情報取得
				const $checkbox = $('tbody input.selectable[type=checkbox]:checked').eq(0);
				if ($checkbox.length > 0) {
					const $tr = $checkbox.closest('tr');
					const params = {
							companyCd : $tr.find('[data-field=companyCd]').val(),
							rcvinspNo : $tr.find('[data-field=rcvinspNo]').text()
					}
					NCI.init("/ri0030/validate", params).done(function(res) {
						if (res && res.success) {
							// 変更申請
							NCI.redirect("../vd/vd0310.html?screenProcessId=" + res.screenProcessId + "&trayType=NEW&param1=" + params.companyCd + "&param2=" + params.rcvinspNo);
						}
					});
				}
			})
			;
		}
	});

	/** 検索実行 */
	function search(pageNo, keepMessage) {
		const $targets = $('#formCondition').find('input, select, text');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		const cond = createCondition(pageNo);
		pager.search(cond, keepMessage).done(function() {
			$('#seach-result').removeClass('hide');

			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'R.RCVINSP_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 取引先選択ポップアップからのコールバック */
	function callbackFromSelectSplr(splr) {
		if (splr) {
			$('#splrCd').val(splr['SPLR_CD']).trigger('validate');
			$('#splrNmKj').val(splr['SPLR_NM_KJ']).trigger('validate');
		}
	}

	/** 取引先選択ポップアップをクリア */
	function clearForSplr() {
		$('#splrCd, #splrNmKj').val('').trigger('validate');
	}

	/** 明細行を開く */
	function openEntry(companyCd, rcvinspNo) {
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'rcvinspNo' : rcvinspNo },
			'corporationCode' : companyCd,
			'screenCode' : 'SCR0049',
			'screenName' : '管理_検収登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../ri/ri0030.html',
			'dcId' : 2						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnUpdateRequest').prop('disabled', len === 0);
	}
});

