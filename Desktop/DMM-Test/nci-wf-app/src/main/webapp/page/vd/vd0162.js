let ctx = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
const LABEL = 2;

$(function() {
	NCI.init('/vd0162/init').done(function(res) {
		if (res && res.success) {
			let rows = [];
			ctx.root.childPartsIds.forEach(function(partsId, i, array) {
				let design = ctx.designMap[partsId];
				rows.push(design);
			});

			new ResponsiveTable().fillTable(rows);

			$('#btnOK').prop('disabled', false);
		}

		// OKボタン押下
		$('#btnOK').click(function(ev) {
			let rows = [];
			$('#inputed tbody tr').each(function(i, tr) {
				let $tr = $(tr);
				rows.push({
					'partsId' : $tr.find('span[data-field=partsId]').text(),
					'mobileInvisibleFlag' : $tr.find('input[data-field=mobileInvisibleFlag]').prop('checked')
				});
			});
			Popup.close(rows);
		});

		// 閉じるボタン押下
		$('#btnClose').click(function(ev) {
			Popup.close();
		});
	});
});
