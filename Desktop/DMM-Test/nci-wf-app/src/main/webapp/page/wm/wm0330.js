$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		changeRoleCode : NCI.getQueryString("changeRoleCode"),
		seqNoChangeRoleDetail : NCI.getQueryString("seqNoChangeRoleDetail"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'changeRoleDetail' ]
	};
	NCI.init("/wm0330/init", params).done(function(res) {
		if (res && res.success) {

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			// 所属区分の選択肢
			if (res.changeRoleAssignmentTypeList) {
				NCI.createOptionTags($('#changeRoleAssignmentType'), res.changeRoleAssignmentTypeList);
			}

			if (res.changeRoleDetail) {
				NCI.toElementsFromObj(res.changeRoleDetail, $('#inputed'));

				if (res.changeRoleDetail.seqNoChangeRoleDetail == null || res.changeRoleDetail.seqNoChangeRoleDetail == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					// 所属区分初期化
					changeRoleAssignmentType(res.changeRoleDetail.changeRoleAssignmentType);

					if (res.changeRoleDetail.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#validStartDate").prop('disabled', true);
						$("#validEndDate").prop('disabled', true);
						$("#changeRoleAssignmentType").prop('disabled', true);
						$('#btnSelectCorp').hide();
						$('#btnClearCorp').hide();
						$('#btnSelectOrg').hide();
						$('#btnClearOrg').hide();
						$('#btnSelectPost').hide();
						$('#btnClearPost').hide();
						$('#btnSelectUser').hide();
						$('#btnClearUser').hide();
					} else {
						$('#btnUpdate').prop('disabled', false);
					}
				}
			}
		}

		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));

		$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./wm0320.html?corporationCode=" + $("#corporationCode").val() + "&changeRoleCode=" + $("#changeRoleCode").val());
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("changeRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						changeRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/wm0330/insert", params2).done(function(res) {
						if (res && res.success && res.changeRoleDetail) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#validStartDate").prop('disabled', true);
							$("#validEndDate").prop('disabled', true);
							$("#changeRoleAssignmentType").prop('disabled', true);
							$('#btnSelectCorp').hide();
							$('#btnClearCorp').hide();
							$('#btnSelectOrg').hide();
							$('#btnClearOrg').hide();
							$('#btnSelectPost').hide();
							$('#btnClearPost').hide();
							$('#btnSelectUser').hide();
							$('#btnClearUser').hide();
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("changeRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						changeRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/wm0330/update", params2).done(function(res) {
						if (res && res.success && res.changeRoleDetail) {
							if (res.changeRoleDetail.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#validStartDate").prop('disabled', true);
								$("#validEndDate").prop('disabled', true);
								$("#changeRoleAssignmentType").prop('disabled', true);
								$('#btnSelectCorp').hide();
								$('#btnClearCorp').hide();
								$('#btnSelectOrg').hide();
								$('#btnClearOrg').hide();
								$('#btnSelectPost').hide();
								$('#btnClearPost').hide();
								$('#btnSelectUser').hide();
								$('#btnClearUser').hide();
							} else {
								$('#btnUpdate').prop('disabled', false);
								$("#validStartDate").prop('disabled', false);
								$("#validEndDate").prop('disabled', false);
								$("#changeRoleAssignmentType").prop('disabled', false);
								changeRoleAssignmentType(res.changeRoleDetail.deleteFlag);
							}
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

				var msg = NCI.getMessage("MSG0072", NCI.getMessage("changeRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						changeRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/wm0330/delete", params2).done(function(res) {
						if (res && res.success && res.changeRoleDetail) {
						}
					});
				}));
			})
			// 企業選択ボタン
			.on('click', '#btnSelectCorp:enabled', function() {
				const corporationCode = $('#corporationCode').val();
				let url = "../cm/cm0010.html";
				if (corporationCode)
					url += "?corporationCode=" + corporationCode;
				Popup.open(url, callbackFromCm0010, null, this);
			})
			// 組織選択ボタン
			.on('click', '#btnSelectOrg:enabled', function() {
				const corporationCode = $('#corporationCodeAssigned').val();
				let url = "../cm/cm0020.html";
				if (corporationCode)
					url += "?corporationCode=" + corporationCode;
				Popup.open(url, callbackFromCm0020, null, this);
			})
			// 役職ボタン
			.on('click', '#btnSelectPost:enabled', function() {
				var params = null;
				const corporationCode = $('#corporationCodeAssigned').val();
				let url = "../cm/cm0030.html";
				if (corporationCode)
					url += "?corporationCode=" + corporationCode;
				Popup.open(url, callbackFromCm0030, null, this);
			})
			// ユーザボタン
			.on('click', '#btnSelectUser:enabled', function() {
				const corporationCode = $('#corporationCodeAssigned').val();
				let url = "../cm/cm0040.html";
				if (corporationCode)
					url += "?corporationCode=" + corporationCode;
				Popup.open(url, callbackFromCm0040, params, this);
			})
			// クリアボタン
			.on('click', '.clear-input-group:enabled', function() {
				$(this).closest('.input-group').find('input[type=text],span').val('').text('');
			})

			// 所属区分
			.on('change', '#changeRoleAssignmentType', function(ev) {
				$('#organizationCodeAssigned').val(null);
				$('#organizationNameAssigned').val(null);
				$('#postCodeAssigned').val(null);
				$('#postNameAssigned').val(null);
				$('#userCodeAssigned').val(null);
				$('#userNameAssigned').val(null);

				changeRoleAssignmentType($('#changeRoleAssignmentType').val());
			})
	});
});

function openEntry(corporationCode, changeRoleCode) {
	NCI.redirect("./wm0320.html?corporationCode=" + corporationCode + "&changeRoleCode=" + changeRoleCode);
}

/** 企業選択画面からのコールバック関数 */
function callbackFromCm0010(corp, trigger) {
	if (corp) {
		// 企業の下位データである組織／役職／ユーザは破棄
		if ($('#corporationCodeAssigned').val() !== corp.corporationCode) {
			$('#organizationCodeAssigned, #organizationNameAssigned').val('').trigger('validate');
			$('#postCodeAssigned, #postNameAssigned').val('').trigger('validate');
			$('#userCodeAssigned, #userNameAssigned').val('').trigger('validate');
		}

		$('#corporationCodeAssigned').val(corp.corporationCode).trigger('validate');
		$('#corporationNameAssigned').val(corp.corporationName).trigger('validate');
	}
}

/** 組織選択画面からのコールバック関数 */
function callbackFromCm0020(org, trigger) {
	if (org) {
		$('#organizationCodeAssigned').val(org.organizationCode).trigger('validate');
		$('#organizationNameAssigned').val(org.organizationName).trigger('validate');
	}
}

/** 役職選択画面からのコールバック関数 */
function callbackFromCm0030(post, trigger) {
	if (post) {
		$('#postCodeAssigned').val(post.postCode).trigger('validate');
		$('#postNameAssigned').val(post.postName).trigger('validate');
	}
}

/** 役職選択画面からのコールバック関数 */
function callbackFromCm0040(user, trigger) {
	if (user) {
		$('#userCodeAssigned').val(user.userCode).trigger('validate');
		$('#userNameAssigned').val(user.userName).trigger('validate');
	}
}

function changeRoleAssignmentType(selectedValue) {
	if (selectedValue != null && selectedValue != '') {
		const ORG = '1', ORG_POST = '2', POST = '3', USER = '4', CORP = 'A';
		const organizations = [ORG, ORG_POST], users = [USER], posts = [ORG_POST, POST];

		// 企業
		const enableCorp = true;
		{
			$('#corporationLabel, #corporationCodeAccess').toggleClass('required', enableCorp);
			$('#corporationCodeAccess, #corporationNameAccess').prop('disabled', !enableCorp);
			$('#btnSelectCorp, #btnClearCorp').toggle(enableCorp);
		}
		// 組織
		const enableOrg = organizations.indexOf(selectedValue) >= 0;
		{
			$('#organizationLabel, #organizationCodeAssigned').toggleClass('required', enableOrg);
			$('#organizationCodeAssigned, #organizationNameAssigned').prop('disabled', !enableOrg);
			$('#btnSelectOrg, #btnClearOrg').toggle(enableOrg);
		}
		// 役職
		const enablePost = posts.indexOf(selectedValue) >= 0;
		{
			$('#postLabel, #postCodeAssigned').toggleClass('required', enablePost);
			$('#postCodeAssigned, #postNameAssigned').prop('disabled', !enablePost);
			$('#btnSelectPost, #btnClearPost').toggle(enablePost);
		}
		// ユーザ
		const enableUser = users.indexOf(selectedValue) >= 0;
		{
			$('#userLabel, #userCodeAssigned').toggleClass('required', enableUser);
			$('#userCodeAssigned, #userNameAssigned').prop('disabled', !enableUser);
			$('#btnSelectUser, #btnClearUser').toggle(enableUser);
		}
	}
}

