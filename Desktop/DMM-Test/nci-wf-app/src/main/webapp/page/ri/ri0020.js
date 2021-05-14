$(function() {
	// 遷移元からの受信パラメータ
	const params = { "source": NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY)) };
	const responsiveTable = new ResponsiveTable($('#splitPO'))
	responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
		$tr.data('entity', entity);
	};
	NCI.init("/ri0020/init", params).done(function(res) {
		if (res && res.success) {
			// 消費税コードの選択肢
			NCI.createOptionTags($('select.taxCd'), res.taxCds);

			$('#purordNo').val(params.source.purordNo);
			$('#purordDtlNo').val(params.source.purordDtlNo);

			init(res);

			$(document)
			// 検索結果の選択ラジオボタン
			.on('change', 'input[type=checkbox][name=chkPurOrd]', function() {
				let count = 0;
				$('input[name=chkPurOrd]:checked').each(function(i, elem) {
					checkedCount++;
				});
				$('#btnDecide').prop('disabled', count === 0);
			})
			// 閉じるボタン
			.on('click', 'button.btnClose', function() {
				Popup.close();
			})
			.on('click', 'button.btnItm', function() {
				// 品目の選択
				const conds = { "COMPANY_CD" : params.source.companyCd,"ORGNZ_CD_SRCH" : params.source.orgnzCd };
				NCI.openMasterSearch('V_ITM_MST', 'FOR_BTN_ITM', callbackFromSelectItm, conds, this);
			})
			.on('click', 'button.btnClearItm', function() {
				// 品目のクリア
				clearItm(this);
			})
			.on('click', 'button.btnItmexps', function() {
				// 費目の選択
				const conds = { "COMPANY_CD" : params.source.companyCd,"ORGNZ_CD_SRCH" : params.source.orgnzCd };
				NCI.openMasterSearch('ITMEXPS_CHRMST_V', 'FOR_POP_ITMCHRMST', callbackFromSelectItmexps, conds, this);
			})
			.on('click', 'button.btnClearItmexps', function() {
				// 費目のクリア
				clearItmexps(this);
			})
			.on('click', 'button.btnBumon', function() {
				// 部門の選択
				const conds = { "COMPANY_CD" : params.source.companyCd };
				NCI.openMasterSearch('BUMON_MST', 'FOR_BTN_BUMON', callbackFromSelectBumon, conds, this);
			})
			.on('click', 'button.btnClearBumon', function() {
				// 部門のクリア
				clearBumon(this);
			})
			;

			// 決定ボタン
			$('#btnDecide').click(decide).prop('disabled', false);
			// 追加ボタン
			$('#btnAddLine').click(addLine).prop('disabled', false);
			// 削除ボタン
			$('#btnDeleteLine').click(deleteLine).prop('disabled', false);
			;
		}
	});

	/** 品目選択からのコールバック */
	function callbackFromSelectItm(entity, trigger) {
		if (entity) {
			const $tr = $(trigger).closest('tr');
			$tr.find('[data-field=itmCd]').val(entity['ITM_CD']);
			$tr.find('[data-field=itmNm]').val(entity['ITM_NM']);
		}
	}

	/** 品目のクリア */
	function clearItm(trigger) {
		const $tr = $(trigger).closest('tr');
		$tr.find('[data-field=itmCd], [data-field=itmNm]').val('');
	}

	/** 費目選択からのコールバック */
	function callbackFromSelectItmexps(entity, trigger) {
		if (entity) {
			const $tr = $(trigger).closest('tr');
			$tr.find('[data-field=itmexpsCd1]').val(entity['ITMEXPS_CD1']);
			$tr.find('[data-field=itmexpsCd2]').val(entity['ITMEXPS_CD2']);
			$tr.find('[data-field=itmexpsNm1]').val(entity['ITMEXPS_NM1']);
			$tr.find('[data-field=itmexpsNm2]').val(entity['ITMEXPS_NM2']);
		}
	}

	/** 費目のクリア */
	function clearItmexps(trigger) {
		const $tr = $(trigger).closest('tr');
		$tr.find('[data-field=itmexpsCd1], [data-field=itmexpsCd2]').val('');
	}

	/** 部門選択からのコールバック */
	function callbackFromSelectBumon(entity, trigger) {
		if (entity) {
			const $tr = $(trigger).closest('tr');
			$tr.find('[data-field=bumonCd]').val(entity['BUMON_CD']);
		}
	}

	/** 部門のクリア */
	function clearBumon(trigger) {
		const $tr = $(trigger).closest('tr');
		$tr.find('[data-field=bumonCd]').val('');
	}

	/** 行追加 */
	function addLine(ev) {
		if (!validate())
			return false;

		// 検収金額（分割中の）
		let sum = 0;
		$('#splitPO tbody>tr').find('input[data-field=rcvinspAmt]').each(function(i, textbox) {
			const val = +NCI.removeComma($(textbox).val());
			if (val)
				sum += val;
		});
		// 分割前のオリジナル行のコピーを作成
		const newRec = $.extend({}, $('#srcPO').data('entity') );
		// 検収金額＝「分割前の発注金額 - SUM(検収金額)」
		const rcvinspAmt = parseInt(NCI.removeComma(newRec.purordAmt), 10) - sum;
		newRec.rcvinspAmt = (rcvinspAmt < 0) ? 0 : rcvinspAmt;

		// 行追加
		responsiveTable.addRowResult(newRec);

		// 行番号の降り直し
		fillRowNumber();
	}

	/** 行番号の降り直し */
	function fillRowNumber() {
		$('#splitTbl>tbody>tr span.rownum').each(function(i, rownum) {
			$(rownum).text(i + 1);
		});
	}

	/** 行削除 */
	function deleteLine(ev) {
		// 選択行の削除
		let count = 0;
		$('#splitTbl>tbody>tr input.selectable:checked').each(function(i, checkbox) {
			const $tr = $(checkbox).closest('tr');
			const subRownum = $tr.index() + 1;
			$('#splitTbl>tbody>tr:eq(' + subRownum + ')').remove();
			$tr.removeData('entity').remove();
			count++;
		});
		// 行番号の降り直し
		if (count > 0)
			fillRowNumber();
	}

	// 分割元情報を画面に表示
	function init(res) {
		$('#srcPO').data('entity', res.results[0])
		// 発注情報（分割元）
		NCI.toElementsFromObj(res.results[0], $('#srcPO'));
		// 発注情報（分割先）
		responsiveTable.fillTable(res.results);

		// 3桁カンマ区切り
		$('#tblHead tbody tr').find('.dataType-number').each(function(i, elem) {
			elem.textContent = NCI.addComma(NCI.removeComma(elem.textContent));
		});
	}

	/** バリデーション */
	function validate() {
		const $targets = $('#splitTbl').find('input, select');
		return Validator.validate($targets, true);
	}

	/** 決定ボタン押下 */
	function decide(ev) {
		// 画面固有のバリデーション
		if (!validate()) {
			return false;
		}
		const rows = [];
		$('#splitPO').find('table tbody tr').each(function(i, tr) {
			// 入力行のデータを吸い上げて「TRに格納されているデータ」とマージして行データとする
			// このとき「TRに格納されているデータ」自体が書き変わっているので、注意が必要だ
			const $tr = $(tr);
			const row = $.extend($tr.data('entity'), NCI.toObjFromElements($tr));

			// 2行で１データなので、偶数行でのみ行追加
			if (i % 2 === 0) {
				rows.push(row);
			}
		});
		Popup.close(rows);
	}
});
