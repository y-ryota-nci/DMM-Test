var DcAttrEx = {

	ignores : ['txtInitialValue1', 'numInitialValue1', 'dateInitialValue1','txtareaInitialValue1'
		,'chkInitialValue1', 'rdoInitialValue1', 'ddlInitialValue1', 'orgInitialValue1', 'usrInitialValue1'],

	/** 表示対象エレメントに対して初期値をセット */
	setupInitialValue : function(entity) {
		let $txtInitialValue1 = $('#txtInitialValue1');
		let $numInitialValue1 = $('#numInitialValue1');
		let $txtareaInitialValue1 = $('#txtareaInitialValue1');
		let $dateInitialValue1 = $('#dateInitialValue1');
		let $chkInitialValue1 = $('#chkInitialValue1');
		let $ddlInitialValue1 = $('#ddlInitialValue1');
		let $orgInitialValue1 = $('#orgInitialValue1');
		let $usrInitialValue1 = $('#usrInitialValue1');
		let inputType = entity.inputType;

		// 入力タイプが"数値"の場合
		if (inputType === '1') {
			$numInitialValue1.val(entity.initialValue1);
		}
		// 入力タイプが"文字列"の場合
		else if (inputType === '2') {
			$txtInitialValue1.val(entity.initialValue1);
		}
		// 入力タイプが"文章"の場合
		else if (inputType === '3') {
			$txtareaInitialValue1.val(entity.initialValue1);
		}
		// 入力タイプが"日付"の場合
		else if (inputType === '4') {
			$dateInitialValue1.val(entity.initialValue1);
		}
		// 入力タイプが"チェックボックス"の場合
		else if (inputType === '5') {
			$chkInitialValue1.prop('checked', ($chkInitialValue1.val() === entity.initialValue1));
		}
		// 入力タイプが"ラジオボタン"、"コンボボックス"の場合
		else if (inputType === '6' || inputType === '7') {
			// initialValue2に選択値が設定されてあるので注意(initialValue1は名称)
			$ddlInitialValue1.val(entity.initialValue2);
		}
		// 入力タイオが"組織選択"の場合
		else if (inputType === '8') {
			$orgInitialValue1.val(entity.initialValue1);
		}
		// 入力タイオが"ユーザ選択"の場合
		else if (inputType === '9') {
			$usrInitialValue1.val(entity.initialValue1);
		}
	},

	/** 吸い上げ対象となるエレメントに対して値をセット */
	fillInitialValue : function() {
		let $initialValue1 = $('#initialValue1');
		let $initialValue2 = $('#initialValue2');
		let $initialValue3 = $('#initialValue3');
		let $initialValue4 = $('#initialValue4');
		let $initialValue5 = $('#initialValue5');

		let inputType = $('#inputType').val();
		// 入力タイプが"数値"の場合
		if (inputType === '1') {
			$initialValue1.val($('#numInitialValue1').val());
			$initialValue2.val('');
			$initialValue3.val('');
			$initialValue4.val('');
			$initialValue5.val('');
		}
		// 入力タイプが"文字列"の場合
		else if (inputType === '2') {
			$initialValue1.val($('#txtInitialValue1').val());
			$initialValue2.val('');
			$initialValue3.val('');
			$initialValue4.val('');
			$initialValue5.val('');
		}
		// 入力タイプが"文章"の場合
		else if (inputType === '3') {
			$initialValue1.val($('#txtareaInitialValue1').val());
			$initialValue2.val('');
			$initialValue3.val('');
			$initialValue4.val('');
			$initialValue5.val('');
		}
		// 入力タイプが"日付"の場合
		else if (inputType === '4') {
			$initialValue1.val($('#dateInitialValue1').val());
			$initialValue2.val('');
			$initialValue3.val('');
			$initialValue4.val('');
			$initialValue5.val('');
		}
		// 入力タイプが"チェックボックス"の場合
		else if (inputType === '5') {
			let $chkInitialValue1 = $('#chkInitialValue1');
			$initialValue1.val($chkInitialValue1.prop('checked') ? $chkInitialValue1.val() : '');
			$initialValue2.val('');
			$initialValue3.val('');
			$initialValue4.val('');
			$initialValue5.val('');
		}
		// 入力タイプが"ラジオボタン"、"コンボボックス"の場合
		else if (inputType === '6' || inputType === '7') {
			let $selectOption = $('#ddlInitialValue1 option:selected');
			let val = $selectOption.length > 0 ? $selectOption.val() : ''
			$initialValue1.val(val === '' ? '' : $selectOption.text());
			$initialValue2.val(val);
			$initialValue3.val('');
			$initialValue4.val('');
			$initialValue5.val('');
		}
		// 入力タイオが"組織選択"の場合
		else if (inputType === '8') {
			$initialValue1.val($('#orgInitialValue1').val());
			// initialValue2～5は組織選択時に設定済みのため反映不要
		}
		// 入力タイオが"ユーザ選択"の場合
		else if (inputType === '9') {
			$initialValue1.val($('#usrInitialValue1').val());
			// initialValue2～5はユーザ選択時に設定済みのため反映不要
		}
	},

	/**
	 * 項目の表示非表示の切り替え
	 * @param inputType
	 * @param init 初期ロード時か
	 * @returns
	 */
	toggleDisplay : function(inputType, init) {
		let $divMaxLengths = $('div.maxLengths');
		let $maxLengths = $('#maxLengths');
		let $divOptionId = $('div.optionId');
		let $optionId = $('#optionId');
		let $txtInitialValue1 = $('#txtInitialValue1');
		let $numInitialValue1 = $('#numInitialValue1');
		let $txtareaInitialValue1 = $('#txtareaInitialValue1');
		let $dateInitialValue1 = $('#dateInitialValue1');
		let $chkInitialValue1 = $('#chkInitialValue1');
		let $ddlInitialValue1 = $('#ddlInitialValue1');
		let $orgInitialValue1 = $('#orgInitialValue1');
		let $usrInitialValue1 = $('#usrInitialValue1');

		// いったん各入力タイプ毎の初期値欄を非表示に
		$divMaxLengths.toggleClass('hide', true);
		$divOptionId.toggleClass('hide', true);
		$('div.input-group', $('#initialValue')).toggleClass('hide', true);
		if (!init) {
			$maxLengths.val('');
			$optionId.val('');
			$txtInitialValue1.val('');
			$numInitialValue1.val('');
			$dateInitialValue1.val('');
			$chkInitialValue1.prop('checked', false);
			$ddlInitialValue1.val('');
			$txtareaInitialValue1.val('');
			$('#btnClearOrg').click();
			$('#btnClearUser').click();
		}
		let $target = null;
		// 入力タイプが"数値"の場合
		if (inputType === '1') {
			$divMaxLengths.toggleClass('hide', false);
			$target = $numInitialValue1;
		}
		// 入力タイプが"文字列"の場合
		else if (inputType === '2') {
			$divMaxLengths.toggleClass('hide', false);
			$target = $txtInitialValue1;
		}
		// 入力タイプが"文章"の場合
		else if (inputType === '3') {
			$target = $txtareaInitialValue1;
		}
		// 入力タイプが"日付"の場合
		else if (inputType === '4') {
			$target = $dateInitialValue1;
		}
		// 入力タイプが"チェックボックス"の場合
		else if (inputType === '5') {
			$target = $chkInitialValue1;
		}
		// 入力タイプが"ラジオボタン"、"コンボボックス"の場合
		else if (inputType === '6' || inputType === '7') {
			$divOptionId.toggleClass('hide', false);
			if ($optionId.val() !== '' && $optionId.val() != null) {
				$target = $ddlInitialValue1;
			}
		}
		// 入力タイオが"組織選択"の場合
		else if (inputType === '8') {
			$target = $orgInitialValue1;
		}
		// 入力タイオが"ユーザ選択"の場合
		else if (inputType === '9') {
			$target = $usrInitialValue1;
		}
		if ($target != null) {
			$target.closest('div.input-group').toggleClass('hide', false);
		}
	}
};