$(function() {
	var params = {
			"corporationCode" : NCI.getQueryString("corporationCode"),
			"timestampUpdated" : NCI.getQueryString("timestampUpdated"),
			"messageCds" : ['corporation']
	};
	NCI.init("/mm0001/init", params).done(function(res) {
		if (res && res.success) {
			// 企業グループの選択肢
			NCI.createOptionTags($('#corporationGroupCode'), res.corporationGroupCodes);

			setResults(res);
		}
	});

	$(document)
	// 更新ボタン押下
	.on('click', '#btnUpdate', function(e) {
		var $targets = $('input, select, textarea');
		if (!Validator.validate($targets, true))
			return false;

		NCI.confirm(NCI.getMessage('MSG0071', NCI.getMessage('corporation')), function() {
			var inputs = { corp : NCI.toObjFromElements($('#corporationInfo')) };
			NCI.post('/mm0001/update', inputs).done(function(res) {
				if (res && res.success) {
					setResults(res);
				}
			});
		});
	})
	// 削除ボタン押下
	.on('click', '#btnDelete', function() {
		NCI.confirm(NCI.getMessage('MSG0072', NCI.getMessage('corporation')), function() {
			var inputs = { corp : NCI.toObjFromElements($('#corporationInfo')) };
			NCI.post("/mm0001/delete", inputs).done(function(res) {
				if (res && res.success) {
					setResults(res);
				}
			});
		});
		return false;
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		if (NCI.getQueryString('displayValidOnly')) {
			NCI.redirect("./mm0000.html" +
					"?corporationCode=" + NCI.getQueryString('corporationCode') +
					"&displayValidOnly=" + NCI.getQueryString('displayValidOnly') +
					"&baseDate=" + NCI.getQueryString('baseDate'));
		} else {
			NCI.redirect("./mm0410.html");
		}
	})
	;
});

function setResults(res) {
	$.each(res.corp, function(id, value) {
		$('#' + id).val(value);
	});
	$('#btnDelete, #btnUpdate').prop('disabled', false);
}