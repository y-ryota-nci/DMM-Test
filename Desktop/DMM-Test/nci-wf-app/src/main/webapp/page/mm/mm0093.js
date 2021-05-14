$(function() {
	var initParams = {
		corporationCode : NCI.getQueryString("corporationCode"),
		lookupTypeCode : NCI.getQueryString("lookupTypeCode"),
		lookupCode : NCI.getQueryString("lookupCode"),
		timestampUpdated : NCI.getQueryString("timestampUpdated"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'lookup' ]
	};
	NCI.init("/mm0093/init", initParams).done(function(res) {
		if (res && res.success) {

			// 選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}
			if (res.updateFlagList) {
				NCI.createOptionTags($('#updateFlag'), res.updateFlagList);
			}

			if (res.lookup) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.lookup[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});

				if (res.lookupType) {
					$("#lookupTypeName").val(res.lookupType.lookupTypeName);
				}

				if (res.lookup.lookupCode == null || res.lookup.lookupCode == '') {
					$("#deleteFlag").prop('disabled', true);
					$("#updateFlag").prop('disabled', false);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					if (res.lookup.updateFlag == '0') {
						$('#btnUpdate').hide();
						$("#lookupCode").prop('disabled', true);
						$("#lookupName").prop('disabled', true);
						$("#sortOrder").prop('disabled', true);
						$("#updateFlag").prop('disabled', true);
						$("#deleteFlag").prop('disabled', true);
					} else if (res.lookup.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#lookupCode").prop('disabled', true);
						$("#lookupName").prop('disabled', true);
						$("#updateFlag").prop('disabled', true);
						$("#sortOrder").prop('disabled', true);
					} else {
						$('#btnUpdate').prop('disabled', false);
						$("#lookupCode").prop('disabled', true);
					}
				}
			}

			$(document)
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("lookup"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						lookup : NCI.toObjFromElements($root, ['lookupTypeName'])
					};
					NCI.post("/mm0093/insert", params2).done(function(res) {
						if (res && res.success && res.lookup) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#lookupCode").prop('disabled', true);
							$("#lookupName").prop('disabled', true);
							$("#sortOrder").prop('disabled', true);
							$("#updateFlag").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("lookup"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						lookup : NCI.toObjFromElements($root, ['lookupTypeName'])
					};
					NCI.post("/mm0093/update", params2).done(function(res) {
						if (res && res.success && res.lookup) {
							changeDisabled(res.lookup.deleteFlag);
						}
					});
				}));
			})
			;
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0092.html?corporationCode=" + initParams.corporationCode +
					"&lookupTypeCode=" + initParams.lookupTypeCode);
		})
	});

	function openEntry(corporationCode, lookupTypeCode) {
		NCI.redirect("./mm0092.html?corporationCode=" + corporationCode + "&lookupTypeCode=" + lookupTypeCode);
	}

	// 削除フラグ更新による画面項目有効/無効切り替え
	function changeDisabled(deleteFlagVal) {
		if (deleteFlagVal != null && deleteFlagVal != '') {
			const deleteFlag = deleteFlagVal == '1';
			$("#lookupName2").prop('disabled', deleteFlag);
			$("#lookupName").prop('disabled', deleteFlag);
			$("#sortOrder").prop('disabled', deleteFlag);
		}
	}
});

