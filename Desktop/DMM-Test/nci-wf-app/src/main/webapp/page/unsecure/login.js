$(function() {
	window.sessionStorage.clear();
	const params = {"messageCds" : ['password']};
	NCI.init("/login/init", params).done(function(res) {
		if (res && res.success) {
			// 本日のお知らせ（お知らせがあるときのみ表示）
			let showAnnouncement = res.announcements && res.announcements.length > 0;
			$('#announcements').toggleClass('hide', !showAnnouncement);
			if (showAnnouncement) {
				let $tbl = new ResponsiveTable($('#announcements'));
				$.each(res.announcements, function(i, row) {
					let $tr = $tbl.addRowResult(row);
					$tr.find('a[data-field="linkTitle"]').attr('href', row.linkUrl);
					$tr.find('[data-field=contents]').html(NCI.escapeHtmlBR(row.contents));
				});
			}

			$('#corporationCode').attr('placeHolder', NCI.getMessage('corporationCode'));
			$('#userAddedInfo').attr('placeHolder', NCI.getMessage('userAddedInfo'));
			$('#password').attr('placeHolder', NCI.getMessage('password'));

			$(document)
				.on('click', '#btnLogin', function(e) {
					const params = {
						corporationCode : $('#corporationCode').val(),
						userAddedInfo : $('#userAddedInfo').val(),
						password : $('#password').val()
					};
					NCI.post('/login/login', params).done(function(res) {
						if (res && res.success) {
							NCI.redirect('../' + res.redirectUrl);
						}
					});
					return false;
				}).on('click', '#restorePassword', function() {
					NCI.redirect('./restorePassword.html');
					return false;
				});
		}
	});
});
