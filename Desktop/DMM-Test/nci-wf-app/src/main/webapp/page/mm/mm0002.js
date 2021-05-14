$(function() {
	var params = {
			"corporationCode" : NCI.getQueryString("corporationCode"),
			"organizationCode" : NCI.getQueryString("organizationCode"),
			"timestampUpdated" : NCI.getQueryString("timestampUpdated"),
			"messageCds" : ['organization']
	};
	NCI.init("/mm0002/init", params).done(function(res) {
		if (res && res.success) {

			setResults(res);
		}
		// カレンダー（年月日）
		//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
		NCI.ymdPicker($('input.ymdPicker'));
	});

	$(document)
	// 上位組織選択ボタン
	.on('click', '#btnSelectOrg:enabled', function() {
		var params = null;
		Popup.open("../cm/cm0020.html", callbackFromCm0020, params, this);
	})
	// 上位組織クリア
	.on('click', 'button.clear-input-group', function() {
		$('#organizationCodeUp').val( $('#organizationCode').val() );
		$('#organizationNameUp').val( $('#organizationName').val() );
		$('#organizationLevel').val('1');
	})
	// 更新ボタン押下
	.on('click', '#btnUpdate', function(e) {
		var $targets = $('input, select, textarea');
		if (!Validator.validate($targets, true))
			return false;

		NCI.confirm(NCI.getMessage('MSG0071', NCI.getMessage('organization')), function() {
			var inputs = { org : NCI.toObjFromElements($('#organizationInfo')) };
			NCI.post('/mm0002/update', inputs).done(function(res) {
				if (res && res.success) {
					setResults(res);
				}
			});
		});
	})
	// 削除ボタン押下
	.on('click', '#btnDelete', function() {
		NCI.confirm(NCI.getMessage('MSG0072', NCI.getMessage('organization')), function() {
			var inputs = {
					org : NCI.toObjFromElements($('#organizationInfo')),
					baseDate : NCI.getQueryString('baseDate')
			};
			NCI.post("/mm0002/delete", inputs).done(function(res) {
				if (res && res.success) {
					setResults(res);
				}
			});
		});
		return false;
	})
	// 戻るボタン
	.on('click', '#btnBack', function() {
		let url = "";
		if (NCI.getQueryString('displayValidOnly')) {
			// 「有効データのみ表示」が初期パラメータならプロファイル管理画面へ戻る
			url += "./mm0000.html?displayValidOnly=" + NCI.getQueryString('displayValidOnly');
			url += "&corporationCode=" + NCI.getQueryString('corporationCode');
			url += "&organizationCode=" + NCI.getQueryString('organizationCode');
			url += "&baseDate=" + NCI.getQueryString('baseDate');
		} else {
			// その他は組織一覧へ戻る
			url += "./mm0420.html";
		}
		NCI.redirect(url);
	})
	;
});

function callbackFromCm0020(org, trigger) {
	if (org) {
		$('#organizationCodeUp').val(org.organizationCode);
		$('#organizationNameUp').val(org.organizationName);

		var level = org.organizationLevel;
		if (level == null) {
			if ($("#organizationCode").val() === org.organizationCode)
				level = 1;
			else
				level = 2;
		} else {
			level++;
		}
		$('#organizationLevel').val(level);
	}
}

function setResults(res) {
	$.each(res.org, function(id, value) {
		$('#' + id).val(value);
	});
	$('#btnDelete, #btnUpdate').prop('disabled', false);
}