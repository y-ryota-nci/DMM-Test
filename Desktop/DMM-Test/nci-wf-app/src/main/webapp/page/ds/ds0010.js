$(function() {

	NCI.init('/ds0010/init').done(function(res) {
		if (res && res.success) {
			var url = res.dashboardUrl;
			$('#dashboardFrame').prop('src', url + '/api/repos/%3Ahome%3Aadmin%3Adash7.wcdf/generatedContent?corporation_code=' + res.loginInfo.corporationCode + '&user_id=' + res.loginInfo.userAddedInfo)
		}
	});
});
