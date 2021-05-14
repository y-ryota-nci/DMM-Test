$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		functionCode : decodeURIComponent(NCI.getQueryString("functionCode") || ''),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'function' ]
	};
	NCI.init("/mm0081/init", params).done(function(res) {
		if (res && res.success) {
			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}
			// 同期フラグの選択肢
			if (res.synchronousFunctionFlagList) {
				NCI.createOptionTags($('#synchronousFunctionFlag'), res.synchronousFunctionFlagList);
			}

			if (res.function) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.function[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				})
				if (res.function.functionCode == null || res.function.functionCode == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					if (res.function.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#functionCode").prop('disabled', true);
						$("#functionName").prop('disabled', true);
						$("#functionContents").prop('disabled', true);
						$("#synchronousFunctionFlag").prop('disabled', true);
					} else {
						$('#btnUpdate').prop('disabled', false);
						$("#functionCode").prop('disabled', true);
					}
				}
			}
		}

		$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./mm0080.html");
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("function"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						function : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0081/insert", params2).done(function(res) {
						if (res && res.success && res.function) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#functionCode").prop('disabled', true);
							$("#functionName").prop('disabled', true);
							$("#functionContents").prop('disabled', true);
							$("#synchronousFunctionFlag").prop('disabled', true);
							$("#deleteFlag").prop('disabled', true);
						}
					});
				}));
			})

			// 更新ボタン
			.on('click', '#btnUpdate', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("function"));
				if (NCI.confirm(msg, function() {
					var params2 = {
							function : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0081/update", params2).done(function(res) {
						if (res && res.success && res.function) {
							if (res.function.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#functionCode").prop('disabled', true);
								$("#functionName").prop('disabled', true);
								$("#functionContents").prop('disabled', true);
								$("#synchronousFunctionFlag").prop('disabled', true);
							} else {
								$('#btnUpdate').prop('disabled', false);
								$("#functionCode").prop('disabled', true);
								$("#functionName").prop('disabled', false);
								$("#functionContents").prop('disabled', false);
								$("#synchronousFunctionFlag").prop('disabled', false);
							}
						}
					});
				}));
			})
	});
});

function openEntry(corporationCode, functionCode) {
	NCI.redirect("./mm0080.html?corporationCode=" + corporationCode + "&functionCode=" + functionCode);
}

