$(function() {
	let params = { "accessLogId" : NCI.getQueryString('accessLogId') };
	NCI.init('/al0011/init', params).done(function(res) {
		if (res && res.success) {
			// アクセスログ
			NCI.toElementsFromObj(res.accessLog);

			// アクセスログ明細
			if (res.details) {
				let $root = $('#accessLogDetail').removeClass('hide');
				new ResponsiveTable($root).fillTable(res.details);
			}

			// セッションIDリンク押下
			$('a.sessionId').click(function() {
				let sessionId = $(this).find('span[data-field=sessionId]').text();
				NCI.redirect('./al0010.html?sessionId=' + sessionId)
			});

			// 操作者ログインIDリンク押下
			$('a.opeUserAddedInfo').click(function() {
				let opeUserAddedInfo = $(this).find('span[data-field=opeUserAddedInfo]').text();
				NCI.redirect('./al0010.html?opeUserAddedInfo=' + opeUserAddedInfo)
			});

			// なりすまししたユーザのログインIDリンク押下
			$('a.spoofingUserAddedInfo').click(function() {
				let spoofingUserAddedInfo = $(this).find('span[data-field=spoofingUserAddedInfo]').text();
				NCI.redirect('./al0010.html?spoofingUserAddedInfo=' + spoofingUserAddedInfo)
			});

			// 画面コードリンク押下
			$('a.screenId').click(function() {
				let screenId = $(this).find('span[data-field=screenId]').text();
				NCI.redirect('./al0010.html?screenId=' + screenId)
			})
		}

		$('#btnBack').click(function(ev) {
			NCI.redirect('./al0010.html');
		});
	});
});
