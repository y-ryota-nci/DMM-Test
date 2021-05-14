$(function() {
	var initParams = {
		corporationCode : NCI.getQueryString("corporationCode"),
		lookupGroupId : NCI.getQueryString("lookupGroupId"),
		lookupId : NCI.getQueryString("lookupId"),
		timestampUpdated : NCI.getQueryString("timestampUpdated"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'lookup' ]
	};
	NCI.init("/mm0013/init", initParams).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
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

				if (res.lookup.screenLookupId == null || res.lookup.screenLookupId == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
//					$('#btnDelete').hide();
				} else {
					$('#btnRegister').hide();
					if (res.lookup.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
//						$('#btnDelete').prop('disabled', true);
						$("#lookupId").prop('disabled', true);
						$("#lookupName2").prop('disabled', true);
						$("#lookupName").prop('disabled', true);
						$("#sortOrder").prop('disabled', true);
					} else {
						$('#btnUpdate').prop('disabled', false);
//						$('#btnDelete').prop('disabled', false);
						$("#lookupId").prop('disabled', true);
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
						lookup : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0013/insert", params2).done(function(res) {
						if (res && res.success && res.lookup) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
//							$('#btnDelete').prop('disabled', false);
							$("#lookupId").prop('disabled', true);
							$("#lookupName2").prop('disabled', true);
							$("#lookupName").prop('disabled', true);
							$("#sortOrder").prop('disabled', true);
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
						lookup : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0013/update", params2).done(function(res) {
						if (res && res.success && res.lookup) {
//							NCI.redirect("./mm0013.html?corporationCode=" + params2.lookup.corporationCode + "&lookupGroupId=" + params2.lookup.lookupGroupId + "&lookupId=" + params2.lookup.lookupId);
							changeDisabled(res.lookup.deleteFlag);
						}
					});
				}));
			})

			// 削除ボタン
			.on('click', '#btnDelete', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0072", NCI.getMessage("lookup"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						lookup : NCI.toObjFromElements($root)
					};
					NCI.post("/mm0013/delete", params2).done(function(res) {
						if (res && res.success && res.lookup) {
//							NCI.redirect("./mm0013.html?corporationCode=" + params2.lookup.corporationCode + "&lookupGroupId=" + params2.lookup.lookupGroupId + "&lookupId=" + params2.lookup.lookupId);
						}
					});
				}));
			})
			;
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0012.html?corporationCode=" + initParams.corporationCode +
					"&lookupGroupId=" + initParams.lookupGroupId);
		})
	});

	function openEntry(corporationCode, lookupGroupId) {
		NCI.redirect("./mm0012.html?corporationCode=" + corporationCode + "&lookupGroupId=" + lookupGroupId);
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

