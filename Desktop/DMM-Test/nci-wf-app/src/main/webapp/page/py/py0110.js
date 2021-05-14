$(function() {
	let params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));

	const pager = new Pager($('#seach-result'), '/py0110/search', search).init();
	NCI.init("/py0110/init", {}).done(function(res) {
		if (res && res.success) {
			// 初期値
			$('#splrCd').val(params.splrCd);
			$('#splrNmKj').val(params.splrNm);

			$(document).on('click', '#btnSearch', function(ev) {
				// 検索ボタン押下
				search(1);
				return false;
			})
			// 閉じるボタン
			.on('click', 'button.btnClose', function() {
				Popup.close();
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
				$('#splrCd').val(params.splrCd);
				$('#splrNmKj').val(params.splrNm);
			})
			// 決定ボタン押下
			.on('click', '#btnDecide', function(ev) {
				let result = {};
				let maebaraiNo = '';
				let maebaraiJutoAmt = 0;
				// 画面固有のバリデーション
				if (!validPy0110()) {
					return;
				}
				$('.selectable:checked').each(function(i, elem) {
					let $tr = $(elem).closest('tr');
					$tr.find('[data-field]').each(function(j, elem_child) {
						let fieldName = elem_child.getAttribute('data-field');
						if ('maebaraiNo' === fieldName) {
							if (maebaraiNo.length != 0) {
								maebaraiNo += ', ';
							}
							maebaraiNo += $(elem_child).text();
						}
						if ('jutoNowAmtJpy' === fieldName) {
							let jutoAmt = NCI.removeComma(elem_child.value);
							if (jutoAmt.length > 0) {
								maebaraiJutoAmt += Number(jutoAmt);
							}
						}
					});
					result['maebaraiNo'] = maebaraiNo;
					result['maebaraiJutoAmt'] = maebaraiJutoAmt;
				});
				// コールバック関数の呼び出し
				Popup.close(result);
			})
			.on('change', '.selectable', function(ev) {
				let chk = $(this).prop('checked');
				const $text = $(this).closest('tr').find('[data-field=jutoNowAmtJpy]');
				$text.prop('disabled', !chk);
				if (!chk) {
					$text.val(0);
				}
				let checkedCount = 0;
				$('.selectable:checked').each(function(i, elem) {
					checkedCount++;
				});
				$('#btnDecide').prop('disabled', checkedCount == 0);
			})
			// 検索結果のリンク押下
			.on('click', 'a[data-field]', function(ev) {
				const $tr = $(this).closest('tr');
				const corporationCode = $tr.find('[data-field=corporationCode]').text();
				const processId = $tr.find('[data-field=processId]').text();
				const timestampUpdated = $tr.find('[data-field=timestampUpdated]').text();
				openEntry(corporationCode, processId, timestampUpdated);
			})
			.on('click', '.btnSplrCd', function() {
				// 取引先の選択ポップアップ起動
				const conds = {};
				NCI.openMasterSearch('SPLR_MST', 'FOR_POPUP', callbackFromSelectSplr, conds);
			})
			.on('click', '.btnClearSplr', function() {
				// 取引先のクリアボタン
				clearForSplr();
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
			cond.sortColumn = 'O.PURORD_NO';
			cond.sortAsc = true;
		}
		return cond;
	}

	/** 画面固有のバリデーション */
	function validPy0110() {
		let result = true;
		let maebaraiNo = '';
		let maebaraiJutoAmt = 0;
		let jutoKanoAmtJpy;
		$('.selectable:checked').each(function(i, elem) {
			let $tr = $(elem).closest('tr');
			$tr.find('[data-field]').each(function(j, elem_child) {
				let fieldName = elem_child.getAttribute('data-field');
				if ('maebaraiNo' === fieldName) {
					if (maebaraiNo.length != 0) {
						maebaraiNo += ', ';
					}
					maebaraiNo += $(elem_child).text();
				}
				if ('jutoKanoAmtJpy' === fieldName) {
					let val = NCI.removeComma(elem_child.textContent);
					if (val.length > 0) {
						jutoKanoAmtJpy = Number(val);
					} else {
						jutoKanoAmtJpy = 0;
					}
				}
				if ('jutoNowAmtJpy' === fieldName) {
					let jutoAmt = NCI.removeComma(elem_child.value);
					if (jutoAmt.length > 0) {
						let numAmt = Number(jutoAmt);
						// バリデーション
						if (numAmt <= 0) {
							alert('今回充当金額には「0」以上を入力して下さい。', null);
							result = false;
							return;
						}
						if (jutoKanoAmtJpy < numAmt) {
							alert('今回充当金額が充当可能金額を超えています。', null);
							result = false;
							return;
						}
					}
				}
			});
		});
		return result;
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
	function openEntry(corporationCode, processId, timestampUpdated) {
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnComplete, #btnRestore').prop('disabled', len === 0);
	}

});

