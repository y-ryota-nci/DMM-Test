$(function() {
	var params = {messageCds : ['route']};
	NCI.init("/mm0301/init", params).done(function(res) {
		if (res && res.success) {
			$('#btnCreate').prop('disabled', false);
			NCI.createOptionTags($('#sourceRoute'), res.processDefs);
			NCI.createOptionTags($('#executionTermUnitType'), res.executionTermUnitTypes);

			if (res.processDef) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.processDef[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						$(elem).val(val);
					} else {
						$(elem).text(val);
					}
				});
			}

			// カレンダー（年月日）
			//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
			NCI.ymdPicker($('input.ymdPicker'));

			$(document)
			// 戻るボタン押下
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./mm0300.html");
			})
			// 更新ボタン
			.on('click', '#btnCreate', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("route"));
				NCI.confirm(msg, function() {
					var params = {
						processDef: NCI.toObjFromElements($root, ['sourceRoute']),
						sourceRoute: $('option:selected', '#sourceRoute').val()
					};
					NCI.post("/mm0301/create", params).done(function(res) {
						if (res && res.success && res.processDef) {
							openEdit(res.processDef.corporationCode, res.processDef.processDefCode, res.processDef.processDefDetailCode, res.processDef.timestampUpdated);
						}
					});
				});
			})
			.on('change', '#executionTermUnitType', function(ev) {
				var val = $('option:selected', $(this)).val();
				if (val === 'N') {
					Validator.hideBalloon($('#executionTerm'));
					$('#executionTerm').val("");
					$('#executionTerm').prop("disabled", true);
					$('#executionTerm').removeClass('required');
				} else {
					$('#executionTerm').prop("disabled", false);
					$('#executionTerm').addClass('required');
				}
			})
			;

			$('#executionTermUnitType').trigger('change');
		}
	});

	function openEdit(corporationCode, processDefCode, processDefDetailCode, timestampUpdated) {
		NCI.redirect("./mm0302.html" +
				"?corporationCode=" + corporationCode +
				"&processDefCode=" + processDefCode +
				"&processDefDetailCode=" + processDefDetailCode +
				"&timestampUpdated=" + NCI.timestampToLong(timestampUpdated));
	}
});

