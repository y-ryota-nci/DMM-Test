// Textbox：vd0114.jsから呼び出される初期化ロジック
var vd0114_1 = {
	"stringValidateTypeList" : null,
	"numberValidateTypeList" : null
};
function initSpecific(ctx, design) {
	var $root = $('#vd0114_1');
	var params = { 'design' : design, 'tableName' : ctx.root.tableName };
	NCI.post('/vd0114/initTextbox', params).done(function(res) {
		// 入力タイプ
		NCI.createOptionTags($('#inputType'), res.inputTypeList);
		// 入力タイプチェック
		vd0114_1.stringValidateTypeList = res.stringValidateTypeList;
		vd0114_1.numberValidateTypeList = res.numberValidateTypeList;
		vd0114_1.dateValidateTypeList = res.dateValidateTypeList;
		setupValidateTypes(design.inputType);
		// 桁数タイプ
		NCI.createOptionTags($('#lengthType'), res.lengthTypeList);
		// IMEの制御
		NCI.createOptionTags($('#imeMode'), res.imeModeList);
		// 数値フォーマット
		NCI.createOptionTags($('#numberFormat'), res.numberFormatList);
		// 端数処理タイプ
		NCI.createOptionTags($('#roundType'), res.roundTypeList);
		// パーセントの格納方法
		NCI.createOptionTags($('#saveMethodPercent'), res.saveMethodPercentList);
		// 連動タイプ
		NCI.createOptionTags($('#coodinationType'), res.coodinationTypeList);
		// 入力チェックタイプが日付のパーツを「連動するパーツ」一覧に設定
		const datePartsList = [ {"value" : "", "label" : "--"} ];
		const validateDates = ["ym", "date"];
		$.each(ctx.root.childPartsIds, function(index, pid) {
			const p = ctx.designMap[pid];
			if (p.partsType === PartsType.TEXTBOX && validateDates.indexOf(p.validateType) > -1 && pid !== design.partsId) {
				datePartsList.push({ "value" : p.partsId, "label" : p.labelText });
			}
		});
		NCI.createOptionTags($('#partsIdFor'), datePartsList);

		// データを画面へ反映
		NCI.toElementsFromObj(design, $root);

		// 入力タイプ（テーブルにこのパーツのカラムが既存＋レコードありなら変更不可）
		$('#inputType').prop('disabled', res.lockInputType);

		$('#inputType').on('change', function(ev) {
			Validator.hideBalloon();

			// ※toggleDisplay()は vd0114.jsの関数
			const charOnly = (PartsInputType.NUMBER != this.value && PartsInputType.DATE != this.value);
			const numberOnly = (PartsInputType.NUMBER == this.value);
			const dateOnly = (PartsInputType.DATE == this.value);
			const validateType = $('#validateType').val();
			const validateDateOnly = (["ym", "date"].indexOf(validateType) > -1);
			toggleDisplay($('#lengthType'), charOnly, true);
			toggleDisplay($('#maxLength'), charOnly, false);
			toggleDisplay($('#minLength'), charOnly, false);
			toggleDisplay($('#rowCount'), (PartsInputType.CLOB == this.value || PartsInputType.TEXTAREA == this.value), true);
			toggleDisplay($('#prefix'), numberOnly, false);
			toggleDisplay($('#suffix'), numberOnly, false);
			toggleDisplay($('#numberFormat'), numberOnly, true);
			toggleDisplay($('#roundType'), numberOnly, false);
			toggleDisplay($('#decimalPlaces'), numberOnly, true);
			toggleDisplay($('#max'), numberOnly, false);
			toggleDisplay($('#min'), numberOnly, false);
			toggleDisplay($('#redIfNegative'), numberOnly, false);
			toggleDisplay($('#saveAsPercent'), numberOnly, false);
			toggleDisplay($('#removeSlash'), validateDateOnly && charOnly, false);

			// 入力タイプにより入力チェックタイプも変更
			setupValidateTypes(this.value);

		}).change();

		$('#validateType').change(function(e) {
			// 入力タイプにより入力チェックタイプも変更
			const inputType = $('#inputType').val();
			const charOnly = (PartsInputType.NUMBER != this.value && PartsInputType.DATE != this.value);
			const validateType = this.value;
			const validateDateOnly = (["ym", "date"].indexOf(validateType) > -1);
			$('coodinatedPartsArea').toggle(validateDateOnly);
			toggleDisplay($('#partsIdFor'), validateDateOnly, false);
			toggleDisplay($('#coodinationType'), validateDateOnly, false);
			toggleDisplay($('#useCalendarUI'), validateDateOnly, false);
			toggleDisplay($('#removeSlash'), validateDateOnly && charOnly, false);
		}).change();
	});

	function setupValidateTypes(inputType) {
		if (PartsInputType.NUMBER == inputType)
			NCI.createOptionTags($('#validateType'), vd0114_1.numberValidateTypeList);
		else if (PartsInputType.DATE == inputType)
			NCI.createOptionTags($('#validateType'), vd0114_1.dateValidateTypeList);
		else
			NCI.createOptionTags($('#validateType'), vd0114_1.stringValidateTypeList);
	}
}

// テキストボックス固有のバリデーション
var validateSpecific = function() {
	const $maxLength = $('#maxLength');
	if (Validator.hasError($maxLength))
		return false;

	// 業務管理項目が設定されているなら最大文字数／バイト数を強制
	const bizCode = $('#businessInfoCode').val();
	const CHARACTORS = '1', SJIS = '2', UTF8 = '3';
	const lengthType = $('#lengthType').val();
	const maxLength = parseInt(NCI.getPureValue($maxLength.val(), $maxLength.data('validate')), 10);
	if (bizCode && bizCode.length) {
		// 業務管理項目
		// 	・申請番号／決裁番号／金額はVARCHAR(100)なので、30文字 or 100バイト
		// 	・その他は VARCHAR(2000)なので、600文字 or 2000バイト
		let bizMaxLength = (lengthType === CHARACTORS ? 600 : 2000);
		if (bizCode === 'APPLICATION_NO' || bizCode === 'APPROVAL_NO' || bizCode === 'AMOUNT') {
			bizMaxLength = (lengthType === CHARACTORS ? 30 : 100);
		}
		const fieldName = NCI.getMessage('businessInfo') + ' = ' + bizCode;
		if (lengthType === CHARACTORS && (isNaN(maxLength) || maxLength > bizMaxLength)) {
			// 「{0}を設定しているパーツ」の最大文字数は{1}以下にしてください
			Validator.showBalloon($maxLength, NCI.getMessage('MSG0212', [fieldName, bizMaxLength]));
			return false;
		}
		else if ((lengthType === SJIS || lengthType === UTF8) && (isNaN(maxLength) || maxLength > bizMaxLength)) {
			//「{0}を設定しているパーツ」の最大バイト数は{1}以下にしてください
			Validator.showBalloon($maxLength, NCI.getMessage('MSG0211', [fieldName, bizMaxLength]));
			return false;
		}
	}
	// 文書管理項目が設定されているなら最大文字数／バイト数を強制
	const docBizCode = $('#docBusinessInfoCode').val();
	if (docBizCode && docBizCode.length) {
		const docMaxLength = (lengthType === CHARACTORS ? 600 : 2000);
		const fieldName = NCI.getMessage('docBusinessInfo') + ' = ' + bizCode;
		if (lengthType === CHARACTORS && (isNaN(maxLength) || maxLength > docMaxLength)) {
			// 「{0}を設定しているパーツ」の最大文字数は{1}以下にしてください
			Validator.showBalloon($maxLength, NCI.getMessage('MSG0212', [fieldName, docMaxLength]));
			return false;
		}
		else if ((lengthType === SJIS || lengthType === UTF8) && (isNaN(maxLength) || maxLength > docMaxLength)) {
			//「{0}を設定しているパーツ」の最大バイト数は{1}以下にしてください
			Validator.showBalloon($maxLength, NCI.getMessage('MSG0211', [fieldName, docMaxLength]));
			return false;
		}
	}
	return true;
};
