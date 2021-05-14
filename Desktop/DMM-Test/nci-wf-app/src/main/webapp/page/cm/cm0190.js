$(function() {
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	NCI.init('/cm0190/init', params).done(function(res) {
		if (res && res.success) {
			new ResponsiveTable($('#seach-result')).fillTable(res.results);
		}
	});

	$(document).on('click', 'a', function(e) {
		const $tr = $(this).closest('tr');
		const activity = NCI.toObjFromElements($tr);
		Popup.close(activity);
		return false;
	}).on('click', 'button.btnClose', function(e) {
		Popup.close();
		return false;
	});
});
