$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		menuRoleCode : NCI.getQueryString("menuRoleCode"),
		seqNoMenuRoleDetail : NCI.getQueryString("seqNoMenuRoleDetail"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'menuRoleDetail' ]
	};
	NCI.init("/rm0730/init", params).done(function(res) {
		if (res && res.success) {

			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}

			// メニューロール区分の選択肢
			if (res.menuRoleAssignmentTypeList) {
				NCI.createOptionTags($('#menuRoleAssignmentType'), res.menuRoleAssignmentTypeList);
			}

			if (res.menuRoleDetail) {
				NCI.toElementsFromObj(res.menuRoleDetail, $('#inputed'));

				if (res.menuRoleDetail.seqNoMenuRoleDetail == null || res.menuRoleDetail.seqNoMenuRoleDetail == '') {
					$("#deleteFlag").prop('disabled', true);
					$('#btnRegister').prop('disabled', false);
					$('#btnUpdate').hide();
				} else {
					$('#btnRegister').hide();
					// 所属区分初期化
					changeMenuRoleAssignmentType(res.menuRoleDetail.menuRoleAssignmentType);

					if (res.menuRoleDetail.deleteFlag == '1') {
						$('#btnUpdate').prop('disabled', false);
						$("#validStartDate").prop('disabled', true);
						$("#validEndDate").prop('disabled', true);
						$("#menuRoleAssignmentType").prop('disabled', true);
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
				NCI.redirect("./rm0720.html?corporationCode=" + $("#corporationCode").val() + "&menuRoleCode=" + $("#menuRoleCode").val());
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("menuRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						menuRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/rm0730/insert", params2).done(function(res) {
						if (res && res.success && res.menuRoleDetail) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#validStartDate").prop('disabled', true);
							$("#validEndDate").prop('disabled', true);
							$("#menuRoleAssignmentType").prop('disabled', true);
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

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("menuRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						menuRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/rm0730/update", params2).done(function(res) {
						if (res && res.success && res.menuRoleDetail) {
							if (res.menuRoleDetail.deleteFlag == '1') {
								$('#btnUpdate').prop('disabled', false);
								$("#validStartDate").prop('disabled', true);
								$("#validEndDate").prop('disabled', true);
								$("#menuRoleAssignmentType").prop('disabled', true);
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
								$("#menuRoleAssignmentType").prop('disabled', false);
								changeMenuRoleAssignmentType(res.menuRoleDetail.menuRoleAssignmentType);
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

				var msg = NCI.getMessage("MSG0072", NCI.getMessage("menuRoleDetail"));
				if (NCI.confirm(msg, function() {
					var ignoreColumn = ['organizationLabel', 'postLabel', 'userLabel'];
					var params2 = {
						menuRoleDetail : NCI.toObjFromElements($root, ignoreColumn)
					};
					NCI.post("/rm0730/delete", params2).done(function(res) {
						if (res && res.success && res.menuRoleDetail) {
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
				const corporationCode = $('#corporationCodeAccess').val();
				let url = "../cm/cm0020.html";
				if (corporationCode)
					url += "?corporationCode=" + corporationCode;
				Popup.open(url, callbackFromCm0020, null, this);
			})
			// 役職ボタン
			.on('click', '#btnSelectPost:enabled', function() {
				const corporationCode = $('#corporationCodeAccess').val();
				let url = "../cm/cm0030.html";
				if (corporationCode)
					url += "?corporationCode=" + corporationCode;
				Popup.open(url, callbackFromCm0030, null, this);
			})
			// ユーザボタン
			.on('click', '#btnSelectUser:enabled', function() {
				const corporationCode = $('#corporationCodeAccess').val();
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
			.on('change', '#menuRoleAssignmentType', function(ev) {
				$('#organizationCodeAccess').val(null);
				$('#organizationNameAccess').val(null);
				$('#postCodeAccess').val(null);
				$('#postNameAccess').val(null);
				$('#userCodeAccess').val(null);
				$('#userNameAccess').val(null);

				changeMenuRoleAssignmentType($('#menuRoleAssignmentType').val());
			})
	});
});

function openEntry(corporationCode, menuRoleCode) {
	NCI.redirect("./rm0720.html?corporationCode=" + corporationCode + "&menuRoleCode=" + menuRoleCode);
}

/** 企業選択画面からのコールバック関数 */
function callbackFromCm0010(corp, trigger) {
	if (corp) {
		// 企業の下位データである組織／役職／ユーザは破棄
		if ($('#corporationCodeAccess').val() !== corp.corporationCode) {
			$('#organizationCodeAccess, #organizationNameAccess').val('').trigger('validate');
			$('#postCodeAccess, #postNameAccess').val('').trigger('validate');
			$('#userCodeAccess, #userNameAccess').val('').trigger('validate');
		}

		$('#corporationCodeAccess').val(corp.corporationCode).trigger('validate');
		$('#corporationNameAccess').val(corp.corporationName).trigger('validate');
	}
}

/** 組織選択画面からのコールバック関数 */
function callbackFromCm0020(org, trigger) {
	if (org) {
		$('#organizationCodeAccess').val(org.organizationCode).trigger('validate');
		$('#organizationNameAccess').val(org.organizationName).trigger('validate');
	}
}

/** 役職選択画面からのコールバック関数 */
function callbackFromCm0030(post, trigger) {
	if (post) {
		$('#postCodeAccess').val(post.postCode).trigger('validate');
		$('#postNameAccess').val(post.postName).trigger('validate');
	}
}

/** 役職選択画面からのコールバック関数 */
function callbackFromCm0040(user, trigger) {
	if (user) {
		$('#userCodeAccess').val(user.userCode).trigger('validate');
		$('#userNameAccess').val(user.userName).trigger('validate');
	}
}

function changeMenuRoleAssignmentType(selectedMenuRoleAssignmentType) {
	if (selectedMenuRoleAssignmentType != null && selectedMenuRoleAssignmentType != '') {
		if (selectedMenuRoleAssignmentType != null && selectedMenuRoleAssignmentType != '') {
			const ORG = '1', ORG_POST = '2', POST = '3', USER = '4', CORP = 'A';
			const corporations = [ORG, ORG_POST, POST, USER, CORP]
				, organizations = [ORG, ORG_POST], posts = [ORG_POST, POST], users = [USER];

			// 企業
			const enableCorp = corporations.indexOf(selectedMenuRoleAssignmentType) >= 0;
			{
				$('#corporationLabel, #corporationCodeAccess').toggleClass('required', enableCorp);
				$('#corporationCodeAccess, #corporationNameAccess').prop('disabled', !enableCorp);
				$('#btnSelectCorp, #btnClearCorp').toggle(enableCorp);
			}
			// 組織
			const enableOrg = organizations.indexOf(selectedMenuRoleAssignmentType) >= 0;
			{
				$('#organizationLabel, #organizationCodeAccess').toggleClass('required', enableOrg);
				$('#organizationCodeAccess, #organizationNameAccess').prop('disabled', !enableOrg);
				$('#btnSelectOrg, #btnClearOrg').toggle(enableOrg);
			}
			// 役職
			const enablePost = posts.indexOf(selectedMenuRoleAssignmentType) >= 0;
			{
				$('#postLabel, #postCodeAccess').toggleClass('required', enablePost);
				$('#postCodeAccess, #postNameAccess').prop('disabled', !enablePost);
				$('#btnSelectPost, #btnClearPost').toggle(enablePost);
			}
			// ユーザ
			const enableUser = users.indexOf(selectedMenuRoleAssignmentType) >= 0;
			{
				$('#userLabel, #userCodeAccess').toggleClass('required', enableUser);
				$('#userCodeAccess, #userNameAccess').prop('disabled', !enableUser);
				$('#btnSelectUser, #btnClearUser').toggle(enableUser);
			}
		}
	}
}

