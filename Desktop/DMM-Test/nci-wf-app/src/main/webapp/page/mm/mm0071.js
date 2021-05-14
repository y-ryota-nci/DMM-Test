$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		actionCode : !!NCI.getQueryString("actionCode") ? decodeURIComponent(NCI.getQueryString("actionCode")) : null,
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'action' ]
	};
	NCI.init("/mm0071/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			// 所属区分の選択肢
			if (res.actionTypeList) {
				NCI.createOptionTags($('#actionType'), res.actionTypeList);
			}

			if (res.action) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.action[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				})
				if (res.action.actionCode == null || res.action.actionCode == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					if (res.action.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#actionCode").prop('disabled', true);
						$("#actionName").prop('disabled', true);
						$("#actionType").prop('disabled', true);
					} else {
						$('#btnUpdate').prop('disabled', false);
						$("#actionCode").prop('disabled', true);
					}
				}
			}
		}

		$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./mm0070.html");
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("action"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						action : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0071/insert", params2).done(function(res) {
						if (res && res.success && res.action) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#actionCode").prop('disabled', true);
							$("#actionName").prop('disabled', true);
							$("#actionType").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("action"));
				if (NCI.confirm(msg, function() {
					var params2 = {
							action : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0071/update", params2).done(function(res) {
						if (res && res.success && res.action) {
							if (res.action.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#actionCode").prop('disabled', true);
								$("#actionName").prop('disabled', true);
								$("#actionType").prop('disabled', true);
							} else {
								$('#btnUpdate').prop('disabled', false);
								$("#actionCode").prop('disabled', true);
								$("#actionName").prop('disabled', false);
								$("#actionType").prop('disabled', false);
							}
						}
					});
				}));
			})
	});
});

function openEntry(corporationCode, actionCode) {
	NCI.redirect("./mm0070.html?corporationCode=" + corporationCode + "&actionCode=" + actionCode);
}

