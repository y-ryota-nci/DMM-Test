$(function() {
	const pager = new Pager($('#seach-result'), '/py0011/search', search).init();
	pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		let trClass = 'line-' + rowIndex;
		$tr.addClass(trClass);
		$tr.find('.rcvinspTp input[type=radio]').attr('name', 'rcvinspTp-' + rowIndex);
		let rcvinspTp = entity.rcvinspNo ? '1' : '0';
		$tr.find('div.rcvinspTp > label > input[type=radio][value=' + rcvinspTp + ']').prop('checked', true).trigger('change');

		if (entity.matSts === '1') {
			$tr.find('input.selectable[type=checkbox]').addClass('hide');
			$tr.find('input').prop('disabled', true);
			$tr.find('button.btnSearch').closest('span').addClass('hide');
		}
	};

	const params = {};
	NCI.init("/py0011/init", params).done(function(res) {
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
			NCI.ymPicker($('input.ymPicker'));

			$(document)
			.on('click', '#btnSearch', function(ev) {
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
			// 行選択
			.on('click', 'input.selectable[type=checkbox]', function() {
				enableButtons();
			})
			.on('change', 'div.rcvinspTp > label > input[type=radio]', function() {
				let targetTrClass = $(this).closest('tr').attr('class');
				changeRcvinspTp($('tbody > tr.' + targetTrClass), $(this).val() === '1');
			})
			// 費目1検索
			.on('click', 'span.btnItmexpsNm1Search > button.btnSearch', function(ev) {
				let targetTrClass = $(this).closest('tr').attr('class');
				let companyCd = $('tbody > tr.' + targetTrClass).find('span.companyCd').text();
				let orgnzCd = $('tbody > tr.' + targetTrClass).find('span.orgnzCd').text();
				const initConditions = { "COMPANY_CD" : companyCd, "ORGNZ_CD_SRCH": orgnzCd };
				NCI.openMasterSearch('V_ITMEXPS_CHRMST_LV1', 'POPUP_SEARCH', callbackFromSelectItmexps1, initConditions, this, companyCd);
			})
			.on('click', 'span.btnItmexpsNm1Search > button.btnClear', function(ev) {
				clearForItmexps1(this);
			})
			.on('change', 'input.itmexpsCd1', function() {
				let targetTrClass = $(this).closest('tr').attr('class');
				if ($(this).val()) {
					$('tbody > tr.' + targetTrClass).find('input.itmexpsCd2,input.itmexpsNm2,span.btnItmexpsNm2Search>button').prop('disabled', false);
					$('tbody > tr.' + targetTrClass).find('span.btnItmexpsNm2Search>button.btnClear').trigger('click');
				} else {
					$('tbody > tr.' + targetTrClass).find('span.btnItmexpsNm2Search>button.btnClear').trigger('click');
					$('tbody > tr.' + targetTrClass).find('input.itmexpsCd2,input.itmexpsNm2,span.btnItmexpsNm2Search>button').prop('disabled', true);
				}
			})
			// 費目2検索
			.on('click', 'span.btnItmexpsNm2Search > button.btnSearch', function(ev) {
				let targetTrClass = $(this).closest('tr').attr('class');
				let companyCd = $('tbody > tr.' + targetTrClass).find('span.companyCd').text();
				let orgnzCd = $('tbody > tr.' + targetTrClass).find('span.orgnzCd').text();
				let itmexpsCd1 = $('tbody > tr.' + targetTrClass).find('input.itmexpsCd1').val();
				const initConditions = { "COMPANY_CD" : companyCd, "ORGNZ_CD_SRCH": orgnzCd, "ITMEXPS_CD1": itmexpsCd1 };
				NCI.openMasterSearch('V_ITMEXPS_CHRMST_LV2', 'POPUP_SEARCH', callbackFromSelectItmexps2, initConditions, this, companyCd);
			})
			.on('click', 'span.btnItmexpsNm2Search > button.btnClear', function(ev) {
				clearForItmexps2(this);
			})
			// 検収検索
			.on('click', 'span.btnRcvinspSearch > button.btnSearch', function(ev) {
				let targetTrClass = $(this).closest('tr').attr('class');
				let companyCd = $('tbody > tr.' + targetTrClass).find('span.companyCd').text();
				let splrCd = $('tbody > tr.' + targetTrClass).find('span.splrCd').text();
				let dispRcvinspNo = $('tbody > tr.' + targetTrClass).find('input.dispRcvinspNo').val();
				let orgnzCd = $('tbody > tr.' + targetTrClass).find('span.orgnzCd').text();
				const initConditions = { "COMPANY_CD" : companyCd, "SPLR_CD": splrCd, "DISP_RCVINSP_NO": dispRcvinspNo, 'ORGNZ_CD': orgnzCd };
				NCI.openMasterSearch('V_RCVINSPDTL_INF', 'FOR_BTN_RCVINSPDTL_CRDCRD', callbackFromSelectRcvinspdtl, initConditions, this, companyCd);
			})
			.on('click', 'span.btnRcvinspSearch > button.btnClear', function(ev) {
				clearForRcvinspdtl(this);
			})
			// 品目検索
			.on('click', 'span.btnItmSearch > button.btnSearch', function(ev) {
				let targetTrClass = $(this).closest('tr').attr('class');
				let companyCd = $('tbody > tr.' + targetTrClass).find('span.companyCd').text();
				let orgnzCd = $('tbody > tr.' + targetTrClass).find('span.orgnzCd').text();
				const initConditions = { "COMPANY_CD" : companyCd, "ORGNZ_CD_SRCH": orgnzCd };
				NCI.openMasterSearch('V_ITM_MST', 'FOR_BTN_ITM_CRDCRD', callbackFromSelectItm, initConditions, this, companyCd);
			})
			.on('click', 'span.btnItmSearch > button.btnClear', function(ev) {
				clearForItm(this);
			})
			// 部門検索
			.on('click', 'span.btnBumonSearch > button.btnSearch', function(ev) {
				let targetTrClass = $(this).closest('tr').attr('class');
				let companyCd = $('tbody > tr.' + targetTrClass).find('span.companyCd').text();
				const initConditions = {"COMPANY_CD" : companyCd };
				NCI.openMasterSearch('BUMON_MST', 'FOR_BTN_BUMON', callbackFromSelectBumon, initConditions, this, companyCd);
			})
			.on('click', 'span.btnBumonSearch > button.btnClear', function(ev) {
				clearForBumon(this);
			})
			.on('click', '#btnPayCreateRcvinsp', function() {
				const $checked = $('#seach-result tbody tr').find('input.selectable[type=checkbox]:checked').closest('tr');
				const $targets = $('#seach-result tbody').find($.map($checked, function(elem, i) {return '.' + $(elem).attr('class');}).join(',')).find('input[type=text]');
				if (!Validator.validate($targets, true)) {
					return false;
				}

				let params = {screenCode: "SCR0050", inputs: []};
				$checked.each(function(i) {
					const trClass = $(this).attr('class');
					const $tr = $('tbody tr.' + trClass);
					let entity = {};
					entity.companyCd = $tr.find('[data-field=companyCd]').text();
					entity.crdcrdInNo = $tr.find('[data-field=crdcrdInNo]').text();
					entity.payYm = $tr.find('[data-field=payYm]').text();
					entity.splrCd = $tr.find('[data-field=splrCd]').text();
					entity.usrCd = $tr.find('[data-field=usrCd]').text();
					entity.useDt = $tr.find('[data-field=useDt]').text();
					entity.rcvinspNo = $tr.find('[data-field=rcvinspNo]').val();
					entity.rcvinspDtlNo = $tr.find('[data-field=rcvinspDtlNo]').val();
					entity.itmexpsCd1 = $tr.find('[data-field=itmexpsCd1]').val();
					entity.itmexpsCd2 = $tr.find('[data-field=itmexpsCd2]').val();
					entity.itmCd = $tr.find('[data-field=itmCd]').val();
					entity.bumonCd = $tr.find('[data-field=bumonCd]').val();
					params.inputs.push(entity);
				});

				NCI.post('/py0011/createProcess', params).done(function(res) {
					if (res && res.success) {
						search(1, true);
					}
				});
			})
			.on('click', '#btnPayCreateExpense', function() {
				const $checked = $('#seach-result tbody tr').find('input.selectable[type=checkbox]:checked').closest('tr');
				const $targets = $('#seach-result tbody').find($.map($checked, function(elem, i) {return '.' + $(elem).attr('class');}).join(',')).find('input[type=text]');
				if (!Validator.validate($targets, true)) {
					return false;
				}

				let params = {screenCode: "SCR0079", inputs: []};
				$checked.each(function(i) {
					const trClass = $(this).attr('class');
					const $tr = $('tbody tr.' + trClass);
					let entity = {};
					entity.companyCd = $tr.find('[data-field=companyCd]').text();
					entity.crdcrdInNo = $tr.find('[data-field=crdcrdInNo]').text();
					entity.payYm = $tr.find('[data-field=payYm]').text();
					entity.splrCd = $tr.find('[data-field=splrCd]').text();
					entity.usrCd = $tr.find('[data-field=usrCd]').text();
					entity.useDt = $tr.find('[data-field=useDt]').text();
					entity.itmexpsCd1 = $tr.find('[data-field=itmexpsCd1]').val();
					entity.itmexpsCd2 = $tr.find('[data-field=itmexpsCd2]').val();
					entity.itmCd = $tr.find('[data-field=itmCd]').val();
					entity.bumonCd = $tr.find('[data-field=bumonCd]').val();
					params.inputs.push(entity);
				});

				NCI.post('/py0011/createProcess', params).done(function(res) {
					if (res && res.success) {
						search(1, true);
					}
				});
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
		pager.search(cond, keepMessage).done(function(res) {
			$('#seach-result').removeClass('hide');
			enableButtons();

			// 3桁カンマ区切り
			$('#seach-result tbody tr').find('.dataType-number,.dataType-Ym').each(function(i, elem) {
				var $elem = $(elem);
				if ($elem.hasClass("dataType-number")) {
					$elem.text(NCI.addComma($elem.text()));
				} else if ($elem.hasClass("dataType-Ym")) {
					$elem.text(NCI.addSlash($elem.text()));
				}
			});
			$('#seach-result tbody tr').find('').each(function(i, elem) {
				$(elem).text(NCI.addComma($(elem).text()));
			});
		});
	}

	/** 画面入力内容から検索条件を生成 */
	function createCondition(pageNo) {
		let $elements = $('#formCondition').find('input, select, textarea');
		let cond = pager.createCondition($elements, pageNo);
		// デフォルトソート条件
		if (!cond.sortColumn) {
			cond.sortColumn = "C.PAY_YM, C.COMPANY_CD, C.SPLR_CD, C.USR_CD, C.USE_DT, C.CRDCRD_IN_NO";
			cond.sortAsc = true;
		}
		return cond;
	}

	function enableButtons() {
		const $targets = $('#seach-result tbody tr').find('input.selectable[type=checkbox]:checked');
		const checkedLength = $targets.length;
		const rcvinspTpOnLength = $targets.closest('tr').find('div.rcvinspTp input[type=radio][value=1]:checked').length;
		const rcvinspTpOffLength = $targets.closest('tr').find('div.rcvinspTp input[type=radio][value=0]:checked').length;

		$('#btnPayCreateRcvinsp').prop('disabled', checkedLength === 0 || checkedLength !== rcvinspTpOnLength);
		$('#btnPayCreateExpense').prop('disabled', checkedLength === 0 || checkedLength !== rcvinspTpOffLength);
	}

	function changeRcvinspTp($targetTr, rcvinsp) {
		$targetTr.find('input[type=text],input[type=hidden]').val('');
		$targetTr.find('span.btnItmexpsNm1Search,span.btnItmexpsNm2Search,span.btnItmSearch,span.btnBumonSearch').toggleClass('hide', rcvinsp);
		$targetTr.find('span.btnRcvinspSearch').toggleClass('hide', !rcvinsp);
		$targetTr.find('input.dispRcvinspNo').toggleClass('required', rcvinsp);
		$targetTr.find('input.itmexpsNm1,input.itmexpsNm2,input.bumonCd').toggleClass('required', !rcvinsp);
		$targetTr.find('input.dispRcvinspNo,input.itmexpsNm2').prop('disabled', !rcvinsp);
		enableButtons();
	}

	function callbackFromSelectItmexps1(result, trigger) {
		if (result) {
			$(trigger).closest('td').find('input.itmexpsCd1').val(result.ITMEXPS_CD1).trigger('change');
			$(trigger).closest('td').find('input.itmexpsNm1').val(result.ITMEXPS_NM1);
		}
	}
	function clearForItmexps1(trigger) {
		$(trigger).closest('td').find('input.itmexpsCd1').val('').trigger('change');
		$(trigger).closest('td').find('input.itmexpsNm1').val('');
	}

	function callbackFromSelectItmexps2(result, trigger) {
		if (result) {
			$(trigger).closest('td').find('input.itmexpsCd2').val(result.ITMEXPS_CD2);
			$(trigger).closest('td').find('input.itmexpsNm2').val(result.ITMEXPS_NM2);
		}
	}
	function clearForItmexps2(trigger) {
		$(trigger).closest('td').find('input.itmexpsCd2').val('');
		$(trigger).closest('td').find('input.itmexpsNm2').val('');
	}

	function callbackFromSelectRcvinspdtl(result, trigger) {
		if (result) {
			const targetTrClass = $(trigger).closest('tr').attr('class');
			const $tr = $('tbody > tr.' + targetTrClass);

			const $inputs = $('input.dispRcvinspNo').not($tr.find('input.dispRcvinspNo')).filter(function() {
				return $(this).val() === result.DISP_RCVINSP_NO;
			});

			if ($inputs.length > 0) {
				NCI.clearMessage();
				NCI.addMessage('danger', '選択済みの検収の為、選択できません。');
			} else {
				$tr.find('input.rcvinspNo').val(result.RCVINSP_NO);
				$tr.find('input.rcvinspDtlNo').val(result.RCVINSP_DTL_NO);
				$tr.find('input.dispRcvinspNo').val(result.DISP_RCVINSP_NO);
				$tr.find('input.itmexpsCd1').val(result.ITMEXPS_CD1);
				$tr.find('input.itmexpsNm1').val(result.ITMEXPS_NM1);
				$tr.find('input.itmexpsCd2').val(result.ITMEXPS_CD2);
				$tr.find('input.itmexpsNm2').val(result.ITMEXPS_NM2);
				$tr.find('input.itmCd').val(result.ITM_CD);
				$tr.find('input.itmNm').val(result.ITM_NM);
				$tr.find('input.bumonCd').val(result.BUMON_CD);
			}
		}
	}
	function clearForRcvinspdtl(trigger) {
		const targetTrClass = $(trigger).closest('tr').attr('class');
		const $tr = $('tbody > tr.' + targetTrClass);
		$tr.find('input.rcvinspNo').val('');
		$tr.find('input.rcvinspDtlNo').val('');
		$tr.find('input.dispRcvinspNo').val('');
		$tr.find('input.itmexpsCd1').val('');
		$tr.find('input.itmexpsNm1').val('');
		$tr.find('input.itmexpsCd2').val('');
		$tr.find('input.itmexpsNm2').val('');
		$tr.find('input.itmCd').val('');
		$tr.find('input.itmNm').val('');
		$tr.find('input.bumonCd').val('');
	}

	function callbackFromSelectItm(result, trigger) {
		if (result) {
			$(trigger).closest('td').find('input.itmCd').val(result.ITM_CD);
			$(trigger).closest('td').find('input.itmNm').val(result.ITM_NM).prop('readonly', result.IPT_NM_FG != '1');
		}
	}
	function clearForItm(trigger) {
		$(trigger).closest('td').find('input.itmexpsCd2').val('');
		$(trigger).closest('td').find('input.itmexpsNm2').val('').prop('readonly', true);
	}

	function callbackFromSelectBumon(result, trigger) {
		if (result) {
			$(trigger).closest('td').find('input.bumonCd').val(result.BUMON_CD);
		}
	}
	function clearForBumon(trigger) {
		$(trigger).closest('td').find('input.bumonCd').val('');
	}

});

