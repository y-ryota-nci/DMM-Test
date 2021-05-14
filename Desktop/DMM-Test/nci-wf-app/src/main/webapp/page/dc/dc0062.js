$(function() {
	const params = {'metaId' : NCI.getQueryString("metaId"), 'version' : NCI.getQueryString("version"), 'messageCds':['extendedItem']};
	NCI.init("/dc0062/init", params).done(function(res) {
		if (res && res.success) {
			NCI.ymdPicker($('input.ymdPicker'));

			NCI.createOptionTags($('#inputType'), res.inputTypes);
			NCI.createOptionTags($('#optionId'), res.options);
			NCI.createOptionTags($('#ddlInitialValue1'), res.items);
			NCI.createOptionTags($('#deleteFlag'), res.deleteFlags);

			init(res.entity);
		}
	});

	$(document)
	.on('change', '#inputType', function(ev) {
		DcAttrEx.toggleDisplay($(this).val(), false);
	})
	.on('change', '#optionId', function(ev) {
		let params = {'optionId' : $(this).val(), 'corporationCode' : $('#corporationCode').val()};
		NCI.get('/dc0062/changeOption', params).done(function(res) {
			let $ddlInitialValue1 = $('#ddlInitialValue1');
			NCI.createOptionTags($ddlInitialValue1, res);
			$ddlInitialValue1.closest('div.input-group').toggleClass('hide', false);
		});
	})
	.on('click', '#btnSelectOrg', function(ev) {
		const params = null, corporationCode = $('#corporationCode').val();
		let url = "../cm/cm0020.html";
		if (corporationCode)
			url += "?corporationCode=" + corporationCode;
		Popup.open(url, callbackFromCm0020, params, this);
	})
	.on('click', '#btnClearOrg', function(ev) {
		$('#orgInitialValue1, #initialValue2, #initialValue3, #initialValue4, #initialValue5').val('');
	})
	.on('click', '#btnSelectUser', function(ev) {
		const params = null, corporationCode = $('#corporationCode').val();
		let url = "../cm/cm0040.html";
		if (corporationCode)
			url += "?corporationCode=" + corporationCode;
		Popup.open(url, callbackFromCm0040, params, this);
	})
	.on('click', '#btnClearUser', function(ev) {
		$('#usrInitialValue1, #initialValue2, #initialValue3, #initialValue4, #initialValue5').val('');
	})
	.on('click', '#btnRegister', function(ev) {
		const $root = $('#inputed');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		// 初期値の値をinitialValue1～5に反映
		DcAttrEx.fillInitialValue();

		var msg = NCI.getMessage("MSG0069", NCI.getMessage("extendedItem"));
		if (NCI.confirm(msg, function() {
			// 入力値
			const obj = NCI.toObjFromElements($root, DcAttrEx.ignores);
			const params = {entity : obj};
			NCI.post("/dc0062/save", params).done(function(res) {
				if (res && res.success) {
					init(res.entity);
				}
			});
		}));
	})
	.on('click', '#btnUpdate', function(ev) {
		const $root = $('#inputed');
		const $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}
		// 初期値の値をinitialValue1～5に反映
		DcAttrEx.fillInitialValue();

		var msg = NCI.getMessage("MSG0071", NCI.getMessage("extendedItem"));
		if (NCI.confirm(msg, function() {
			// 入力値
			const obj = NCI.toObjFromElements($root, DcAttrEx.ignores);
			const params = {entity : obj};
			NCI.post("/dc0062/save", params).done(function(res) {
				if (res && res.success) {
					init(res.entity);
				}
			});
		}));
	})
	.on('click', '#btnBack', function(ev) {
		NCI.redirect("./dc0060.html");
	})
	;

	function init (entity) {
		let isNew = (entity.metaCode == null);
		let isDelete = (entity.deleteFlag === '1');
		$('input, select, textarea, button', $('#inputed')).prop('disabled', (isDelete));
		$('#metaCode').prop('disabled', !(isNew));
		$('div.deleteFlag').toggleClass('hide', (isNew));
		$('#inputType').prop('disabled', !(isNew));
		$('#optionId').prop('disabled', !(isNew));
		$('#deleteFlag').prop('disabled', false);
		$('#btnRegister').prop('disabled', !(isNew)).toggleClass('hide', !(isNew));
		$('#btnUpdate').prop('disabled', (isNew)).toggleClass('hide', (isNew));
		$('#btnBack').prop('disabled', false);
		// 画面項目設定
		NCI.toElementsFromObj(entity, $('#inputed'));
		// 初期値を画面上のElementに反映
		DcAttrEx.setupInitialValue(entity);
		// 入力タイプに応じて項目の表示／非表示を切り替え
		DcAttrEx.toggleDisplay(entity.inputType, true);
	}

	function callbackFromCm0020(result) {
		if (result) {
			$('#orgInitialValue1').val(result.organizationName);
			$('#initialValue2').val(result.corporationCode);
			$('#initialValue3').val(result.organizationCode);
			$('#initialValue4').val('');
			$('#initialValue5').val('');
		}
	}

	function callbackFromCm0040(result) {
		if (result) {
			$('#usrInitialValue1').val(result.userName);
			$('#initialValue2').val(result.corporationCode);
			$('#initialValue3').val(result.userCode);
			$('#initialValue4').val('');
			$('#initialValue5').val('');
		}
	}
});

