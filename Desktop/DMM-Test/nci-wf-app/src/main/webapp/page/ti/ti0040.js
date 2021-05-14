$(function() {
	let params = {
		"tableId" : NCI.getQueryString("tableId"),
		"version" : NCI.getQueryString("version")
	};
	NCI.init('/ti0040/init', params).done(function(res) {
		if (res && res.success) {
			// テーブル
			NCI.toElementsFromObj(res.table, $('#divTable'));
			// カラム
			new ResponsiveTable($('#divColumn')).fillTable(res.columns);
		}

		$('#btnBack').click(function(ev) {
			NCI.redirect('./ti0030.html');
		});
	});
});
