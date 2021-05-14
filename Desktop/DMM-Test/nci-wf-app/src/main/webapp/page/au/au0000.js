$(function() {
	let params = { messageCds : ['MSG0165']};
	NCI.init('/au0000/init', params).done(function(res) {
		if (res && res.success) {
			NCI.createOptionTags($('#corporationCode'), res.corporations);

			NCI.toElementsFromObj(res, $('#inputs'))

			$('#corporationCode').change(clearOrgAndUser);
			$('#btnSelectOrg').click(openOrgSelect);
			$('#btnClearOrg').click(clearOrg);
			$('#btnSelectUser').click(openUserSelect);
			$('#btnClearUser').click(clearUser);
			$('#btnSpoof').prop('disabled', false).click(spoof);
		}
	});

	// トップページへ
	$('#btnTopPage').click(function() {
		NCI.redirect('../' + NCI.loginInfo.topPageUrl);
	});

	/** ユーザ選択画面を開く */
	function openUserSelect(ev) {
		const url = "../cm/cm0040.html" +
				"?corporationCode=" + $('#corporationCode').val() +
				"&organizationCode=" + $('#organizationCode').val();
		Popup.open(url, fromCm0040, null, this);
	}

	/** 組織選択画面を開く */
	function openOrgSelect(ev) {
		const url = "../cm/cm0020.html?corporationCode=" + $('#corporationCode').val();
		Popup.open(url, fromCm0020, null, this);
	}

	/** なりすまし */
	function spoof(ev) {
		const $targets = $('input');
		if (!Validator.validate($targets, true))
			return false;

		const params = {
				"corporationCode" : $('#corporationCode').val(),
				"userAddedInfo" : $('#userAddedInfo').val(),
				"userName" : $('#userName').val(),
		};
		if (params.corporationCode === NCI.loginInfo.corporationCode
				&& params.userAddedInfo === NCI.loginInfo.userAddedInfo) {
			// ログイン者から変更なしなので、そのままトップへ
			NCI.redirect('../' + NCI.loginInfo.topPageUrl);
		} else {
			// なりすまし
			const msg = NCI.getMessage('MSG0165', [params.userAddedInfo, params.userName]);
			NCI.confirm(msg, function() {
				NCI.post('/au0000/spoof', params).done(function(res) {
					$('#btnTopPage').click();
				});
			});
		}
	}
});

/** 組織選択画面からのコールバック */
function fromCm0020(org, trigger) {
	// 組織が変わっていればユーザをクリア
	if (org) {
		if ($('#organizationCode').val() != org.organizationCode) {
			clearUser();
		}
		$('#organizationAddedInfo').val(org.organizationAddedInfo || '');
		$('#organizationCode').val(org.organizationCode || '');
		$('#organizationTreeName').val(org.organizationTreeName || '');
	}
}

/** ユーザ選択画面からのコールバック関数 */
function fromCm0040(user, trigger) {
	if (user) {
		$('#userCode').val(user.userCode || '');
		$('#userAddedInfo').val(user.userAddedInfo || '');
		$('#userName').val(user.userName || '');
	}
}

/** ユーザ&組織をクリア */
function clearOrgAndUser(ev) {
	clearOrg();
	clearUser();
}

function clearOrg(ev) {
	$('#organizationAddedInfo').val('');
	$('#organizationCode').val('');
	$('#organizationTreeName').val('');
}

function clearUser(ev) {
	$('#userCode').val('');
	$('#userAddedInfo').val('');
	$('#userName').val('');
}

