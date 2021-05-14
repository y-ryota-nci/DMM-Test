$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		assignRoleCode : NCI.getQueryString("assignRoleCode"),
		seqNoAssignRoleDetail : NCI.getQueryString("seqNoAssignRoleDetail"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'assignRoleDetail' ]
	};
	NCI.init("/wm0230/init", params).done(function(res) {
		if (res && res.success) {

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			// 所属区分の選択肢
			if (res.belongTypeList) {
				NCI.createOptionTags($('#belongType'), res.belongTypeList);
			}

			if (res.assignRoleDetail) {
				NCI.toElementsFromObj(res.assignRoleDetail, $('#inputed'));

				if (res.assignRoleDetail.seqNoAssignRoleDetail == null || res.assignRoleDetail.seqNoAssignRoleDetail == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					// 所属区分初期化
					changeBelongType(res.assignRoleDetail.belongType);

					if (res.assignRoleDetail.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#validStartDate").prop('disabled', true);
						$("#validEndDate").prop('disabled', true);
						$("#belongType").prop('disabled', true);
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
				NCI.redirect("./wm0220.html?corporationCode=" + $("#corporationCode").val() + "&assignRoleCode=" + $("#assignRoleCode").val());
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("assignRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						assignRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/wm0230/insert", params2).done(function(res) {
						if (res && res.success && res.assignRoleDetail) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#validStartDate").prop('disabled', true);
							$("#validEndDate").prop('disabled', true);
							$("#belongType").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("assignRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						assignRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/wm0230/update", params2).done(function(res) {
						if (res && res.success && res.assignRoleDetail) {
							if (res.assignRoleDetail.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#validStartDate").prop('disabled', true);
								$("#validEndDate").prop('disabled', true);
								$("#belongType").prop('disabled', true);
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
								$("#belongType").prop('disabled', false);
								changeBelongType(res.assignRoleDetail.belongType);
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

				var msg = NCI.getMessage("MSG0072", NCI.getMessage("assignRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						assignRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/wm0230/delete", params2).done(function(res) {
						if (res && res.success && res.assignRoleDetail) {
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
			.on('change', '#belongType', function(ev) {
				$('#organizationCodeAssigned').val(null);
				$('#organizationNameAssigned').val(null);
				$('#postCodeAssigned').val(null);
				$('#postNameAssigned').val(null);
				$('#userCodeAssigned').val(null);
				$('#userNameAssigned').val(null);

				changeBelongType($('#belongType').val());
			})
	});
});

function openEntry(corporationCode, assignRoleCode) {
	NCI.redirect("./wm0220.html?corporationCode=" + corporationCode + "&assignRoleCode=" + assignRoleCode);
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

function changeBelongType(selectedBelongType) {
	if (selectedBelongType != null && selectedBelongType != '') {
		const ORG = '1', ORG_POST = '2', POST = '3', USER = '4', CORP = 'A';
		// 相対指定(組織)+役職
		const RELATIVE_ORG_POST = '9';
		// 相対指定(組織)+役職(所属長)
		const RELATIVE_ORG_POST_CHIEF = 'M';

		const organizations = [ORG, ORG_POST], users = [USER],
			posts = [ORG_POST, POST, RELATIVE_ORG_POST, RELATIVE_ORG_POST_CHIEF];

		// 企業
		const enableCorp = true;
		{
			$('#corporationLabel, #corporationCodeAccess').toggleClass('required', enableCorp);
			$('#corporationCodeAccess, #corporationNameAccess').prop('disabled', !enableCorp);
			$('#btnSelectCorp, #btnClearCorp').toggle(enableCorp);
		}
		// 組織
		const enableOrg = organizations.indexOf(selectedBelongType) >= 0;
		{
			$('#organizationLabel, #organizationCodeAssigned').toggleClass('required', enableOrg);
			$('#organizationCodeAssigned, #organizationNameAssigned').prop('disabled', !enableOrg);
			$('#btnSelectOrg, #btnClearOrg').toggle(enableOrg);
		}
		// 役職
		const enablePost = posts.indexOf(selectedBelongType) >= 0;
		{
			$('#postLabel, #postCodeAssigned').toggleClass('required', enablePost);
			$('#postCodeAssigned, #postNameAssigned').prop('disabled', !enablePost);
			$('#btnSelectPost, #btnClearPost').toggle(enablePost);
		}
		// ユーザ
		const enableUser = users.indexOf(selectedBelongType) >= 0;
		{
			$('#userLabel, #userCodeAssigned').toggleClass('required', enableUser);
			$('#userCodeAssigned, #userNameAssigned').prop('disabled', !enableUser);
			$('#btnSelectUser, #btnClearUser').toggle(enableUser);
		}
	}
}

