$(function() {
	// 呼び元の画面で使用済みの発注Noは当画面の検索対象から外すため、親画面から選択中の発注Noを受け取る
	const args = NCI.flushScope('RI0010');
	const pager = new Pager($('#seach-result'), '/ri0010/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.data('entity', entity);
	};
	NCI.init("/ri0010/init", {messageCds: ['MSG1001', 'splrCd', 'mnyCd']}).done(function(res) {
		if (res && res.success) {
			$('#corporationCode').val(args.corporationCode);
			$('#splrNmKj').val(args.splrNmKj);
			$('#sbmtrNm').val(NCI.loginInfo.userName);
			$('#orgnzCd').val(args.orgnzCd);	// 第三階層組織(部・室)
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			// 通貨
			NCI.createOptionTags($('#mnyCd'), res.mnyCds).val(args.mnyCd);

			// 初期検索
			search(1);
		}
	});

	// イベント定義
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
	.on('change', 'input[type=checkbox][name=chkPurOrd]', function() {
		let checkedCount = 0, sprtTp = null;
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			checkedCount++;
			if (sprtTp === null) {
				sprtTp = $(elem).closest('tr').find('[data-field=sprtTp]').text();
			}
		});

		$('#btnDecide').prop('disabled', checkedCount == 0);
//		$('#btnDetail').prop('disabled', checkedCount != 1 || sprtTp !== '1');
	})
	// 閉じるボタン
	.on('click', 'button.btnClose', function() {
		Popup.close();
	})
	// 決定ボタン
	.on('click', '#btnDecide', function() {
		// 画面固有のバリデーション
		if (!validRi0010()) {
			return;
		}
		const results = [];
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			const $tr = $(elem).parent().parent();
			results[i] = $tr.data('entity');
		});
		// コールバック関数の呼び出し
		Popup.close(results);
	})
/* 2018.12.10 ユーザ要望により、検収明細分割画面は廃止になった
	// 詳細ボタン：検収明細分割画面の呼び出し
	.on('click', '#btnDetail', function() {
		// 画面固有のバリデーション
		if (!validRi0010()) {
			return;
		}
		let source = null;
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			const $tr = $(elem).parent().parent();
			source = $tr.data('entity');
		});
		Popup.open("./ri0020.html", callBackFromRi0020, source, this);
	})
*/
	.on('click', '.btnSplrCd', function() {
		// 取引先の選択ポップアップ起動
		const conds = { "COMPANY_CD" : args.corporationCode };
		NCI.openMasterSearch('SPLR_MST', 'FOR_POPUP', callbackFromSelectSplr, conds);
	})
	.on('click', '.btnItemCd', function() {
		// 品目の選択ポップアップ起動
		const conds = { "COMPANY_CD" : args.corporationCode,"ORGNZ_CD_SRCH" : args.orgnzCd };
		NCI.openMasterSearch('V_ITM_MST', 'FOR_BTN_ITM', callbackFromSelectItem, conds);
	})
	.on('click', '#btnSelectUser', function() {
		// 発注申請者の選択ポップアップ起動
		const url = "../cm/cm0040.html?corporationCode=" + NCI.loginInfo.corporationCode;
		Popup.open(url, callbackFromCm0040, null, this);
	})
	.on('click', '.btnClearSplr', function() {
		// 取引先のクリアボタン
		clearForSplr();
	})
	.on('click', '.btnClearItem', function() {
		// 品目のクリアボタン
		clearForItem();
	})
	.on('click', '.clear-input-group', function() {
		// ユーザ・組織・役職のクリアボタン
		clearForCm0040();
	})
	;

	/** 検索実行 */
	function search(pageNo) {
		var $targets = $('#formCondition').find('input, select');
		if (!Validator.validate($targets, true))
			return false;

		var cond = createCondition(pageNo);
		pager.search(cond).done(function() {
			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number').each(function(i, elem) {
				elem.textContent = NCI.addComma(elem.textContent);
			});
		});
		$('#btnSelect').prop('disabled', true);
		$('#seach-result').removeClass('invisible');
	}
	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		var $elements = $('#formCondition').find('input, select, textarea');
		var cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = 'D.COMPANY_CD, D.PURORD_NO, D.PURORD_DTL_NO';
			cond.sortAsc = true;
		}
		cond.excludePurOrdNoList = args.purordNoList;	// 検索結果から除外する「発注No＋発注明細No」
		return cond;
	}

	/** 画面固有バリデーション */
	function validRi0010() {
		// 子画面の選択行1行目を基準に取引先・通貨コードが異なっていないかチェック
		var splrCd = '';
		var mnyCd = '';
		var result = true;
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			var $tr = $(elem).parent().parent();
			$tr.find('[data-field]').each(function(j, elem_child) {
				var fieldName = elem_child.getAttribute('data-field');
				if (fieldName === 'splrCd') {
					if (splrCd === '') {
						splrCd = $(elem_child).text();
					}
					else if ($(elem_child).text() !== splrCd) {
						NCI.alert(NCI.getMessage('MSG1001', NCI.getMessage('splrCd')));
						result = false;
						return false;
					}
				}
				if (fieldName === 'mnyCd') {
					if (mnyCd === '') {
						mnyCd = $(elem_child).text();
					}
					else if ($(elem_child).text() !== mnyCd) {
						NCI.alert(NCI.getMessage('MSG1001', NCI.getMessage('mnyCd')));
						result = false;
						return false;
					}
				}
			});
			if (!result) {
				return false;
			}
		});
		return result;
	}

	function callBackFromRi0020(res, trigger) {
		Popup.close(res);
	}

	/** ユーザ選択ポップアップからのコールバック */
	function callbackFromCm0040(user, trigger) {
		if (user) {
			$('#sbmtrCd').val(user.userCode).trigger('validate');
			$('#sbmtrNm').val(user.userName).trigger('validate');
		}
	}

	/** 品目選択ポップアップからのコールバック */
	function callbackFromSelectItem(itm) {
		if (itm) {
			$('#itmCd').val(itm['ITM_CD']).trigger('validate');
			$('#itmNm').val(itm['ITM_NM']).trigger('validate');
		}
	}

	/** ユーザ選択をクリア */
	function clearForCm0040() {
		$('#sbmtrCd, #sbmtrNm').val('').trigger('validate');
	}

	/** 品目選択をクリア */
	function clearForItem() {
		$('#itmCd, #itmNm').val('').trigger('validate');
	}
});

