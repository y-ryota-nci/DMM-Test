$(function() {
	const params = { "hash" : NCI.getQueryString("hash") };
	NCI.init('/resetPassword/init', params).done(function(res) {
		if (res && res.success) {

		}
	});
	$('#btnBackToLogin').click(function() {
		NCI.redirect('./login.html');
	});
});
