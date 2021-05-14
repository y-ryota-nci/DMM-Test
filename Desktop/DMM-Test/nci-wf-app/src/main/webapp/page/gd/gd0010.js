$(function() {
	var params = {needMenuHtml: false, needFooterHtml: false};
	NCI.init("/gd0010/init", params).done(function(res) {
		if (res && res.success) {
			if (res.worklist) {
				$('#applicationPendingCount span.badge').text(res.applicationPendingCount);
				$('#approvalPendingCount span.badge').text(res.approvalPendingCount);
				$('#applicationPendingCount,#approvalPendingCount').removeClass('hide');
			}
			if (res.ownlist) {
				$('#approvedCount span.badge').text(res.approvedCount);
				$('#approvedCount').removeClass('hide');
			}
			if (res.newApplication) {
				$('#newApplication').removeClass('hide');
			}
		}
		$(document)
		.on('click', '#applicationPendingCount', function() { if (res.worklist) {window.open('../wl/wl0030.html?gadget=application', 'nciwf');} })
		.on('click', '#approvalPendingCount', function() { if (res.worklist) {window.open('../wl/wl0030.html?gadget=approval', 'nciwf');} })
		.on('click', '#approvedCount', function() { if (res.ownlist) {window.open('../wl/wl0032.html?gadget=approved', 'nciwf');} })
		.on('click', '#newApplication', function() { if (res.newApplication) {window.open('../na/na0010.html', 'nciwf');} })
		;
	});
});