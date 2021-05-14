$(function() {
	// 初期表示
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		userCode : NCI.getQueryString("userCode"),
		messageCds : ['danger', 'strong', 'soft', 'weak', 'password']
	};
	NCI.init("/au0011/init", params).done(function(res) {
		if (res && res.success) {
			fillPasswordInfo(res);
			if (!res.password) {
				$('#corporationCode').val(params.corporationCode);
				$('#userCode').val(params.userCode);
			}

			// パスワード入力したとき
			$('#newPassword1').on('keyup blur', function() {
				showPasswordStrength(this.value);	// @see pswd-strength.js
			});

			// パスワード変更ボタン
			$('#btnChangePassword').on('click', function() {
				if (!validate()) return false;

				var msg = NCI.getMessage('MSG0071', NCI.getMessage('password'));
				if (NCI.confirm(msg, function() {
					var params = {
						corporationCode: $('#corporationCode').val(),
						userCode: $('#userCode').val(),
						newPassword1: $('#newPassword1').val(),
						newPassword2: $('#newPassword2').val()
					};
					NCI.post('/au0011/save', params).done(function(res) {
						if (res.success) {
							// 再表示
							fillPasswordInfo(res);
						};
					});
				}));
			})
		}
	});

	$('#btnBack').on('click', function() {
		NCI.redirect('./au0010.html');
	}).toggle(params.userCode != null);
});

/** パスワード情報を再表示 */
function fillPasswordInfo(res) {
	$('#btnChangePassword').prop('disabled', false);
	$('#newPassword1, #newPassword2').val('');

	// パスワード情報の表示
	NCI.toElementsFromObj(res.password, $('#current'));

	// パスワード変更履歴の表示
	var pager = new Pager($('#history-info'));
	pager.fillTable(res.histories);

	// 企業属性
	$corpProperties = $('#corpProperties').empty();
	var li = document.createElement('li');
	$.each(res.corpProperties, function(i, row) {
		$li = $(li.cloneNode(false));
		$li.text(row.propertyName + ' : ' + row.propertyValue).appendTo($corpProperties);
	});
}

function validate() {
	return Validator.validate($('input[type=password]'));
}