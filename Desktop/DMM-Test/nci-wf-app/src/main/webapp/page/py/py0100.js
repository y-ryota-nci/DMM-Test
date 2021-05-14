$(function() {
	// 呼び元の画面で使用済みの検収Noは当画面の検索対象から外すため、親画面から選択中の検収Noを受け取る
	var rcvinspNoList = NCI.flushScope('PY0100');
	var params = {
			"corporationCode" : NCI.getQueryString("corporationCode"),
			"orgnzCd" : NCI.getQueryString("orgnzCd")
			};

	var pager = new Pager($('#seach-result'), '/py0100/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.data('entity', entity);
	};
	NCI.init("/py0100/init", {messageCds: ['MSG1001', 'splrCd', 'mnyCd']}).done(function(res) {
		if (res && res.success) {
			NCI.ymdPicker($('input.ymdPicker'));

			$('#corporationCode').val(params.corporationCode);
			$('#orgnzCd').val(params.orgnzCd);
			$('#rcvinspSbmtrNm').val(NCI.loginInfo.userName);

			// 初期検索
			search(1);
		}
	});

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
		var checkedCount = 0;
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			checkedCount++;
		});

		$('#btnDecide').prop('disabled', checkedCount == 0);
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
		// 選択されたレコードに対して呼び出し元に返すためのデータを取得する
		let selectedRcvinspNoList = [];
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			var $tr = $(elem).parent().parent();
			selectedRcvinspNoList.push($tr.data('entity').dispRcvinspNo);
		});
		if (selectedRcvinspNoList.length > 0) {
			let params = {
				"corporationCode": $('#corporationCode').val(),
				"selectedRcvinspNoList": selectedRcvinspNoList
			};
			NCI.post('/py0100/get', params).done(function(res) {
				if (res && res.success) {
					// コールバック関数の呼び出し
					Popup.close(res.results);
				}
			});
		}
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
			cond.sortAsc = true	;
			cond.sortColumn = 'D.COMPANY_CD,D.RCVINSP_NO,D.RCVINSP_DTL_NO';
		}
		cond.excludeRcvinspNoList = rcvinspNoList;	// 検索結果から除外する「検収No＋検収明細No」
		return cond;
	}

	/** 画面固有バリデーション */
	function validRi0010() {
		// 子画面の選択行1行目を基準に取引先・通貨コードが異なっていないかチェック
		var splrCd = '';
		var mnyCd = '';
		var addRto = '';
		var advpayTp = '';
		var result = true;
		$('input[name=chkPurOrd]:checked').each(function(i, elem) {
			var $tr = $(elem).parent().parent();
			let entity = $tr.data('entity');
			if (i == 0) {
				splrCd = entity.splrCd;
				mnyCd = entity.mnyCd;
				addRto = entity.addRto;
				advpayTp = entity.advpayTp;
				return;
			}

			if (splrCd !== entity.splrCd) {
				NCI.alert(NCI.getMessage('MSG1001', NCI.getMessage('splrCd')));
				result = false;
				return;
			}
			if (mnyCd !== entity.mnyCd) {
				NCI.alert(NCI.getMessage('MSG1001', NCI.getMessage('mnyCd')));
				result = false;
				return;
			}
			if (addRto !== entity.addRto) {
				NCI.alert(NCI.getMessage('MSG1001', "計上レート"));
				result = false;
				return;
			}
			if (advpayTp !== entity.advpayTp) {
				NCI.alert(NCI.getMessage('MSG1001', "前払区分"));
				result = false;
				return;
			}
			if (!result) {
				return;
			}
		});
		return result;
	}

	/** ユーザ選択ポップアップからのコールバック */
	function callbackFromCm0040(user, button) {
		if (user) {
			if (button.id === 'btnSelectOrdSbmtr') {
				$('#ordSbmtrCd').val(user.userCode).trigger('validate');
				$('#ordSbmtrNm').val(user.userName).trigger('validate');
			}
			else if (button.id === 'btnSelectRcvinspSbmtr') {
				$('#rcvinspSbmtrCd').val(user.userCode).trigger('validate');
				$('#rcvinspSbmtrNm').val(user.userName).trigger('validate');
			}
		}
	}

	/** 取引先選択ポップアップからのコールバック */
	function callbackFromSelectSplr(splr) {
		if (splr) {
			$('#splrCd').val(splr['SPLR_CD']).trigger('validate');
			$('#splrNmKj').val(splr['SPLR_NM_KJ']).trigger('validate');
		}
	}

	/** 品目選択ポップアップからのコールバック */
	function callbackFromSelectItem(itm) {
		if (itm) {
			$('#itmCd').val(itm['ITEM_CD']).trigger('validate');
			$('#itmNm').val(itm['ITEM_NM']).trigger('validate');
		}
	}

	/** ユーザ選択をクリア */
	function clearForCm0040(button) {
		const $parent = $(button).closest('div.input-group');
		$parent.find('input[type=text], input[type=hidden]').val('').trigger('validate');
	}

	/** 取引先選択をクリア */
	function clearForSplr() {
		$('#splrCd, #splrNmKj').val('').trigger('validate');
	}

	/** 品目選択をクリア */
	function clearForItem() {
		$('#itmCd, #itmNm').val('').trigger('validate');
	}

	/** 明細行を開く */
	function openEntry(corporationCode, processId, timestampUpdated) {
	}
});

