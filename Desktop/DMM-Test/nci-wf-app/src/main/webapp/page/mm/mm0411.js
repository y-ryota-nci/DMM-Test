$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'corporation' ]
	};
	NCI.init("/mm0411/init", params).done(function(res) {
		if (res && res.success) {
			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			// 所属区分の選択肢
			if (res.corporationTypeList) {
				NCI.createOptionTags($('#corporationType'), res.corporationTypeList);
			}

			if (res.corporation) {
				$('input, select, textarea, span').filter('[id]').each(function(i, elem) {
					var val = res.corporation[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				})
				if (res.corporation.corporationCode == null || res.corporation.corporationCode == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
					// 追加時は削除区分は非表示にして選択できないようにする
					$("#deleteFlag").parents('.searchConditionArea').hide();
				} else {
					$('#btnRegister').hide();
					if (res.corporation.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#corporationCode").prop('disabled', true);
						$("#corporationAddedInfo").prop('disabled', true);
						$("#corporationName").prop('disabled', true);
					} else {
						$('#btnUpdate').prop('disabled', false);
						$("#corporationCode").prop('disabled', true);
					}
				}
			}
		}

		$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./mm0410.html?corporationCode=" + $("#corporationCode").val());
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("corporation"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						corporation : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0411/insert", params2).done(function(res) {
						if (res && res.success && res.corporation) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#corporationCode").prop('disabled', true);
							$("#corporationAddedInfo").prop('disabled', true);
							$("#corporationName").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("corporation"));
				if (NCI.confirm(msg, function() {
					var params2 = {
							corporation : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0411/update", params2).done(function(res) {
						if (res && res.success && res.corporation) {
							if (res.corporation.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#corporationCode").prop('disabled', true);
								$("#corporationAddedInfo").prop('disabled', true);
								$("#corporationName").prop('disabled', true);
							} else {
								$('#btnUpdate').prop('disabled', false);
								$("#corporationCode").prop('disabled', true);
								$("#corporationAddedInfo").prop('disabled', false);
								$("#corporationName").prop('disabled', false);
							}
						}
					});
				}));
			})
	});
});

function openEntry(corporationCode, corporationCode) {
	NCI.redirect("./mm0410.html?corporationCode=" + corporationCode);
}

