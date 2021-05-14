$(function() {

	const params = { "messageCds" : [ 'MSG0071' ]};
	NCI.init('/ml0040/init', params).done(function(res) {
		if (res && res.success) {

			init(res);
		}
	});

	$(document).on('click', '#btnUpdate', doUpdate);

	/** 初期化 */
	function init(res) {
		new ResponsiveTable($('#inputs')).fillTable(res.results);

		$('#btnUpdate').prop('disabled', false);
	}

	/** 更新処理 */
	function doUpdate(ev) {
		const $targets = $('#inputs').find('input, select, textarea');
		if (!Validator.validate($targets, true))
			return false;

		const msg = NCI.getMessage('MSG0071', NCI.getMessage('mailVariable'));
		NCI.confirm(msg, function() {
			const params = {
					"inputs" : NCI.toArrayFromTable($('#inputs'))
			};
			NCI.post('/ml0040/save', params).done(function(res) {
				if (res && res.success) {
					init(res);
				}
			});
		});
	}
});
