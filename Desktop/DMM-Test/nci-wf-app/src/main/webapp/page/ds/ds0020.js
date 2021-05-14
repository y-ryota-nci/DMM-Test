$(function() {

	NCI.init('/ds0020/init').done(function(res) {
		if (res && res.success) {
			var url = res.dashboardUrl;
			$('#dashboardFrame').prop('src', url + '/content/saiku-ui/index.html?biplugin5=true&dimension_prefetch=false#query/open/%3AHome%3A20181031.saiku')
		}
	});
});
