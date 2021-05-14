$(function() {

	NCI.init('/restorePassword/init', {"messageCds" : ['MSG0174', 'loginIdOrMailAddress']}).done(function(res) {

		$('#corporationCode').attr('placeHolder', NCI.getMessage('corporationCode'));
		$('#loginIdOrMailAddress').attr('placeHolder', NCI.getMessage('loginIdOrMailAddress'));

		// 仮パスワード発行ボタン押下
		$('#btnOneTimePassword').click(function(ev) {
			const $targets = $('input');
			if (!Validator.validate($targets, true))
				return false;

			// パスワードを復旧させるための仮パスワードを発行します。よろしいですか？
			const msg = NCI.getMessage('MSG0174');
			NCI.confirm(msg, function() {
				const params = {
						"corporationCode" : $('#corporationCode').val(),
						"loginIdOrMailAddress" : $('#loginIdOrMailAddress').val()
				};
				NCI.post('/restorePassword/sendMail', params).done(function(res) {
					if (res && res.success) {
						$('input').val('');
					}
				});
			});
			return false;
		}).prop('disabled', false);
	});

	// 戻るボタン押下
	$('#btnBackToLogin').click(function(ev) {
		NCI.redirect('./login.html');
		return false;
	});
});

