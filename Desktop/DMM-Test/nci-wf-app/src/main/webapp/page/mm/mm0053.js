$(function() {
	var initParams = {
		corporationCode : NCI.getQueryString("corporationCode"),
		partsSequenceSpecCode : NCI.getQueryString("partsSequenceSpecCode"),
		version : NCI.getQueryString("version"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'sequence' ]
	};
	NCI.init("/mm0053/init", initParams).done(function(res) {
		if ('mm0051' in parent) {
			// 採番形式設定から呼び出されるときはポップアップ表示なので
			// メニューを表示しない
			$('header')[0].style.display = 'none';
		}
		if (res && res.success) {
			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}
			if (res.resetTypeList) {
				NCI.createOptionTags($('#resetType'), res.resetTypeList);
			}
			if (res.sequenceLengthList) {
				NCI.createOptionTags($('#sequenceLength'), res.sequenceLengthList);
			}

			if (res.sequence) {
				NCI.toElementsFromObj(res.sequence, $('#formCondition'));

				if (res.sequence.partsSequenceSpecId == null || res.sequence.partsSequenceSpecId == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					if (res.sequence.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#partsSequenceSpecCode").prop('disabled', true);
						$("#partsSequenceSpecName").prop('disabled', true);
						$("#sequenceLength").prop('disabled', true);
						$("#resetType").prop('disabled', true);
					} else {
						$('#btnUpdate').prop('disabled', false);
						$("#partsSequenceSpecCode").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("sequence"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						sequence : NCI.toObjFromElements($('#formCondition'))
					};
					NCI.post("/mm0053/insert", params2).done(function(res) {
						if (res && res.success && res.sequence) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#partsSequenceSpecCode").prop('disabled', true);
							$("#partsSequenceSpecName").prop('disabled', false);
							$("#sequenceLength").prop('disabled', false);
							$("#resetType").prop('disabled', false);
							$("#deleteFlag").prop('disabled', false);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("sequence"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						sequence : NCI.toObjFromElements($('#formCondition'))
					};
					NCI.post("/mm0053/update", params2).done(function(res) {
						if (res && res.success && res.sequence) {
							if (res.sequence.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#partsSequenceSpecCode").prop('disabled', true);
								$("#partsSequenceSpecName").prop('disabled', true);
								$("#sequenceLength").prop('disabled', true);
								$("#resetType").prop('disabled', true);
							} else {
								$('#btnUpdate').prop('disabled', false);
								$("#partsSequenceSpecCode").prop('disabled', true);
								$("#partsSequenceSpecName").prop('disabled', false);
								$("#sequenceLength").prop('disabled', false);
								$("#resetType").prop('disabled', false);
							}
						}
					});
				}));
			})
			;
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0052.html");
		}).toggleClass('hide', Popup.isPopup());

		// 閉じるボタン（採番形式画面から遷移したときだけ）
		$('#btnClose').on('click', function(ev) {
			Popup.close();
		}).toggleClass('hide', !Popup.isPopup());
	});
});

