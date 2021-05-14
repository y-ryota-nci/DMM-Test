$(function() {
	const pager = new Pager($('#seach-result'), '/po0010/search', search).init();

	let screenProcessId = '';
	NCI.init("/po0010/init", { messageCds: ['MSG0068', 'complete'] }).done(function(res) {
		if (res && res.success) {
			// 以前の検索条件を復元できれば再検索する
			if (pager.loadCondition()) {
				search();
			}
			else {
				// 初期値
				$('#purordStsApproved').prop('checked', true);
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
				const purordNo = $tr.find('[data-field=purordNo]').text();
				const screenCode = $tr.find('[data-field=screenCode]').text();
				openEntry(companyCd, purordNo, screenCode);
			})
			.on('click', '.btnSplrCd', function() {
				// 取引先の選択ポップアップ起動
				const conds = {"COMPANY_CD" : NCI.loginInfo.corporationCode };
				NCI.openMasterSearch('SPLR_MST', 'FOR_POPUP', callbackFromSelectSplr, conds);
			})
			.on('click', '#btnSelectUser', function() {
				// 発注申請者の選択ポップアップ起動
				const params = null;
				const url = "../cm/cm0040.html?corporationCode=" + NCI.loginInfo.corporationCode;
				Popup.open(url, callbackFromCm0040, params, this);
			})
			.on('click', '.btnClearSplr', function() {
				// 取引先のクリアボタン
				clearForSplr();
			})
			.on('click', '.clear-input-group', function() {
				// ユーザ・組織・役職のクリアボタン
				clearForCm0040();
			})
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
				whenSelectRow();
			})
			// 変更_発注申請
			.on('click', '#btnUpdateRequest', function() {

				// 選択されている取引先情報取得
				let checkTarget = null;
				let companyCd = null;
				let purordNo = null;
				for (let i = 0; i < $('#seach-result .selectable').size(); i++) {
					if ($('#seach-result .selectable').get(i).checked) {
						checkTarget = $('#seach-result .selectable').get(i);
						break;
					}
				}

				if (checkTarget) {
					const $tr = $(checkTarget).parents('tr');
					const companyCd = $tr.find('[data-field=companyCd]').val();
					const purordNo = $tr.find('[data-field=purordNo]').text();
					const purordTp = $tr.find('[data-field=purordTp]').text();;
					const prdPurordTp = $tr.find('[data-field=prdPurordTp]').text();
					const params = { companyCd : companyCd, purordNo : purordNo, purordTp : purordTp, prdPurordTp : prdPurordTp };

					NCI.init("/po0010/validate", params).done(function(res) {
						if (res && res.success) {
							// 変更申請
							NCI.redirect("../vd/vd0310.html?screenProcessId=" + res.screenProcessId + "&trayType=NEW&param1=" + companyCd + "&param2=" + purordNo);
						}
					});
				}
			})
			// 完了ボタン押下
			.on('click', '#btnComplete', function(ev) {
				// バリデーション
				let check = true;
				$('tbody input.selectable[type=checkbox]:checked').each(function(i, elem) {
					const $tr = $(elem).closest('tr');
					// ステータスが発注済のもののみ完了可
					if ('10' != $tr.find('[data-field=purordSts]').text()) {
						alert('[ステータス]発注済のものだけ完了処理できます。');
						check = false;
						return false;	// break扱い
					}
				});
				if (!check) {
					return false;
				}
				let msg = NCI.getMessage('MSG0068', NCI.getMessage('complete'));
				if (NCI.confirm(msg, function() {
					updateSts(true);
				}));
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

	/** ユーザ選択ポップアップからのコールバック */
	function callbackFromCm0040(user, trigger) {
		if (user) {
			$('#sbmtrCd').val(user.userCode).trigger('validate');
			$('#sbmtrNm').val(user.userName).trigger('validate');
		}
	}

	/** 取引先選択ポップアップからのコールバック */
	function callbackFromSelectSplr(splr) {
		if (splr) {
			$('#splrCd').val(splr['SPLR_CD']).trigger('validate');
			$('#splrNmKj').val(splr['SPLR_NM_KJ']).trigger('validate');
		}
	}

	/** ユーザ選択ポップアップをクリア */
	function clearForCm0040() {
		$('#sbmtrCd, #sbmtrNm').val('').trigger('validate');
	}

	/** 取引先選択ポップアップをクリア */
	function clearForSplr() {
		$('#splrCd, #splrNmKj').val('').trigger('validate');
	}

	/** 明細行を開く */
	function openEntry(companyCd, purordNo, screenCode) {
//		NCI.redirect("./po0011.html?companyCd=" + companyCd + "&purordNo=" + purordNo);
		NCI.flushScope('_vd0330', {
			'keys' : { 'companyCd' : companyCd, 'purordNo' : purordNo },
			'corporationCode' : companyCd,
			'screenCode' : screenCode,
			'screenName' : '管理_発注登録',	// screenNameを指定していればVD0330でそれが画面名として使われる
			'backUrl' : '../po/po0010.html',
			'dcId' : 2						// 指定されていればその表示条件IDを使う
		});
		NCI.redirect('../vd/vd0330.html');
	}

	/** 行選択時の動作 */
	function whenSelectRow() {
		const len = $('tbody input.selectable[type=checkbox]:checked').length;
		$('#btnComplete, #btnUpdateRequest').prop('disabled', len === 0);
	}
	/** ステータス更新処理 */
	function updateSts(complete) {
		let params = { purordNoList: [] };
		$('tbody input.selectable[type=checkbox]:checked').each(function(i, elem) {
			const $tr = $(elem).closest('tr');
			if ('10' != $tr.find('[data-field=purordSts]').text()) {
				alert('aaaa');
				return;
			}
			params.purordNoList.push($tr.find('[data-field=purordNo]').text());
		});
		if (params.purordNoList == null || params.purordNoList.length == 0) {
			return;
		}
		if (complete) {
			// 完了処理
			NCI.init("/po0010/complete", params).done(function(res) {
				search(1);
				$('#btnComplete').prop('disabled', true);
			});
		}
	}

});

