$(function() {
	NCI.init('/mm0510/init').done(function(res) {
		if (res && res.success) {
			NCI.toElementsFromObj(res, $('#inputs'));
		}
	});
});
