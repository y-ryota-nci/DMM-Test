$(function() {
	NCI.init('/ti0052/init').done(function(res) {
		if (res && res.success) {
			// 選択肢
			NCI.createOptionTags($('#searchColumnType'), res.searchColumnTypes);
			NCI.createOptionTags($('#conditionDisplayType'), res.conditionDisplayTypes);
			NCI.createOptionTags($('#conditionDisplayPosition'), res.positions);
			NCI.createOptionTags($('#conditionMatchType'), res.conditionMatchTypes);
			NCI.createOptionTags($('#conditionInitType1'), res.conditionInitTypes);
			NCI.createOptionTags($('#conditionInitType2'), res.conditionInitTypes);
			NCI.createOptionTags($('#conditionOptionId'), res.conditionOptions);
			NCI.createOptionTags($('#conditionBlankType'), res.conditionBlankTypes);
			NCI.createOptionTags($('#conditionKanaConvertType'), res.conditionKanaConvertTypes);
			NCI.createOptionTags($('#conditionTrimFlag'), res.conditionTrimFlags);
			NCI.createOptionTags($('#resultDisplayType'), res.resultDisplayTypes);
			NCI.createOptionTags($('#resultDisplayPosition'), res.positions);
			NCI.createOptionTags($('#resultAlignType'), res.resultAlignTypes);
			NCI.createOptionTags($('#resultOrderByDirection'), res.resultOrderByDirections);
			NCI.createOptionTags($('#resultOrderByPosition'), res.positions);
			NCI.createOptionTags($('#resultDisplayWidth'), res.colWidths);


			let row = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
			NCI.toElementsFromObj(row, $('#divRow'));

			// 検索条件表示区分による制御
			$('#conditionDisplayType').change(conditionDisplayType_change);
			conditionDisplayType_change();

			// 検索結果表示区分による制御
			$('#resultDisplayType').change(resultDisplayType_change);
			resultDisplayType_change();

			// 一致区分／初期値区分の変更による表示／非表示の制御
			$('#conditionMatchType, #conditionInitType1, #conditionInitType2').change(matchType_change);
			matchType_change();

			// 入力ボタン押下
			$('#btnInput').click(execUpdate).prop('disabled', false);
		}
		// 閉じるボタン押下
		$('#btnClose').click(function(ev) {
			Popup.close();
		});
	});

	/** 更新処理 */
	function execUpdate(ev) {
		let $targets = $('input, select');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		// 入力値を吸い上げ
		let $root = $('#divRow');
		let row = NCI.toObjFromElements($root);

		// ドロップダウンの選択肢のラベルを補てん
		$root.find('select').each(function(i, elem) {
			let id = elem.id;
			if (id in row) {
				let label = '';
				if (row[id] != '' && elem.selectedIndex >= 0) {
					label = elem.options[elem.selectedIndex].label;
				}
				row[id + 'Name'] = label;
			}
		});

		// コールバックを呼び出す
		Popup.close(row);
	}

	/** 一致区分／初期値区分の変更による表示／非表示の制御 */
	function matchType_change(ev) {
		const DISPLAY_TEXTBOX = "1", DISPLAY_DROPDOWN = "2";
		const LETERAL = "1";
		const RANGE = "4";

		let displayType = $('#conditionDisplayType').val();
		let matchType = $('#conditionMatchType').val();
		let initType1 = $('#conditionInitType1').val();
		let initType2 = $('#conditionInitType2').val();
		let $show = $('#conditionInitType1');

		// 初期値１＝固定値
		if (initType1 == LETERAL)
			$show = $show.add('#conditionInitValue1');
		// ドロップダウンの選択肢
		if (displayType == DISPLAY_DROPDOWN)
			$show = $show.add('#conditionOptionId');
		// 範囲
		if (matchType == RANGE) {
			$show = $show.add('#fromTo, #conditionInitType2');
			// 範囲で初期値２＝固定値
			if (initType2 == LETERAL)
				$show = $show.add('#conditionInitValue2');
		}
		// 表示する
		showElement($show, true);
		// 表示したもの以外を非表示にする
		let $all = $('.controlled-by-match-type').not($show);
		showElement($all, false);
	}

	/** 検索条件表示区分の変更による有効／無効の切り替え制御 */
	function conditionDisplayType_change(ev) {
		const DISPLAY_TEXTBOX = "1", DISPLAY_DROPDOWN = "2", HIDDEN = "3";
		let $displayType = $('#conditionDisplayType');
		let displayType = $displayType.val();
		if (displayType == DISPLAY_TEXTBOX || displayType == DISPLAY_DROPDOWN) {
			let $targets = $displayType.closest('dl').find('input, select').not($displayType);
			enableElement($targets, true);
		}
		else if (displayType == HIDDEN) {
			// 隠し項目なら表示位置以外を指定できる
			let $conditionDisplayPosition = $('#conditionDisplayPosition');
			let $targets = $displayType.closest('dl').find('input, select').not($displayType).not($conditionDisplayPosition);
			enableElement($targets, true);
			enableElement($conditionDisplayPosition, false);
		}
		else {
			let $targets = $displayType.closest('dl').find('input, select').not($displayType);
			enableElement($targets, false);
		}
		matchType_change();
	}

	/** 検索結果表示区分の変更による有効／無効の切り替え制御 */
	function resultDisplayType_change(ev) {
		const DISPLAY = "1", HIDDEN = "2";
		let $displayType = $('#resultDisplayType');
		let displayType = $displayType.val();
		if (displayType == HIDDEN) {
			// 隠し項目ならデフォルトソートだけ指定できるようにする
			enableElement($('select.enable-when-display'), false);
			enableElement($('select.enable-when-hidden'), true);
		}
		else {
			let $targets = $('select.enable-when-display, select.enable-when-hidden');
			enableElement($targets, displayType == DISPLAY);
		}
	}

	/** 有効／無効の切り替え */
	function enableElement($target, enable) {
		$target.prop('disabled', !enable).each(function(i, elem) {
			if (!enable) {
				Validator.hideBalloon($target);
				if (elem.type && /radio|checkbox/.test(elem.type))
					elem.checked = false;
				else
					elem.value = '';
			}
		});
	}

	/** 表示／非表示の切り替え */
	function showElement($target, show) {
		$target.toggleClass("hide", !show).each(function(i, elem) {
			if (!show) {
				Validator.hideBalloon($target);
				if (elem.type && /radio|checkbox/.test(elem.type))
					elem.checked = false;
				else
					elem.value = '';
			}
		});
	}
});
