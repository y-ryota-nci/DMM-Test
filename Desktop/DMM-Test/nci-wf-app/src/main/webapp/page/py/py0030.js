$(function() {
	const pager = new Pager($('#seach-result'), '/py0030/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.find('input[type=radio]').prop('disabled', false);
	};

	const params = {};
	NCI.init("/py0030/init", params).done(function(res) {
		if (res && res.success) {
			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}
			else {
				// 初期値
				$('#payStsApproved').prop('checked', true);
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
				const payNo = $tr.find('[data-field=payNo]').text();
				const screenCode = $tr.find('[data-field=screenCode]').text();
				openEntry(companyCd, payNo, screenCode);
			})
			.on('click', '.btnSplrCd', function() {
				// 取引先の選択ポップアップ起動
				const conds = {"COMPANY_CD" : NCI.loginInfo.corporationCode };
				NCI.openMasterSearch('SPLR_MST', 'FOR_POPUP', callbackFromSelectSplr, conds);
			})
			.on('click', '.btnClearSplr', function() {
				// 取引先のクリアボタン
				clearForSplr();
			})
			// 行選択
			.on('click', 'input.selectable[type=radio]', function() {
				const len = $('tbody input.selectable[type=radio]:checked').length;
				$('#btnUpdateRequest').prop('disabled', len === 0);
			})
			// 変更_支払依頼申請
			.on('click', '#btnUpdateRequest', function() {

				// 選択されている取引先情報取得
				let checkTarget = null;
				let companyCd = null;
				let payNo = null;
				for (let i = 0; i < $('#seach-result .selectable').size(); i++) {
					if ($('#seach-result .selectable').get(i).checked) {
						checkTarget = $('#seach-result .selectable').get(i);
						break;
					}
				}

				if (checkTarget) {
					const $tr = $(checkTarget).parents('tr');
					const companyCd = $tr.find('[data-field=companyCd]').val();
					const payNo = $tr.find('[data-field=payNo]').text();
					const purordTp = $tr.find('[data-field=purordTp]').text();;
					const prdPurordTp = $tr.find('[data-field=prdPurordTp]').text();
					const params = { companyCd: companyCd, payNo: payNo};

					NCI.init("/py0030/validate", params).done(function(res) {
						if (res && res.success) {
							// 変更申請
							NCI.redirect("../vd/vd0310.html?screenProcessId=" + res.screenProcessId + "&trayType=NEW&param1=" + companyCd + "&param2=" + payNo);
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
			cond.sortColumn = 'P.PAY_NO';
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
	function openEntry(companyCd, payNo, screenCode) {
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'payNo' : payNo, 'advpayFg': '0' },
			'corporationCode' : companyCd,
			'screenCode' : screenCode,
			'screenName' : '管理_支払登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../py/py0030.html',
			'dcId' : 4						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}
});

