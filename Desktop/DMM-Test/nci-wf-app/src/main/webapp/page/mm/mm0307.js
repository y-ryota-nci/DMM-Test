$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);

	var params = {
		actionDef: parentParams.actionDef,
		messageCds : []
	};

	NCI.init('/mm0307/init', params).done(function(res) {
		if (res && res.success) {
			$('#btnCreate').prop('disabled', false);

			NCI.createOptionTags($('#actionCode'), res.actions);
			NCI.createOptionTags($('#actionDefType'), res.actionDefTypes);

			if (res.actionDef) {
				var $root = $('#inputed');
				NCI.toElementsFromObj(res.actionDef, $root, []);
				$('#actionCode').prop('selectedIndex', 0);
			}
		}
		$(document)
		// 閉じるボタン押下
		.on('click', '#btnClose', function(ev) {
			Popup.close();
		})
		// 更新ボタン
		.on('click', '#btnCreate', function(ev) {
			var $root = $('#inputed');
			var $targets = $root.find('input, select, textarea');
			if (!Validator.validate($targets, true)) return false;

			var msg = NCI.getMessage('MSG0069', NCI.getMessage('action'));
			NCI.confirm(msg, function() {
				var actionDef = NCI.toObjFromElements($root, []);
				$root.find('input[type=checkbox]').filter('[id]').each(function(i, e) {if (!actionDef[$(e).attr('id')]) actionDef[$(e).attr('id')] = '0';});

				var params = {actionDef: actionDef};
				NCI.post('/mm0307/create', params).done(function(res) {
					if (res && res.success && res.actionDef) {
						Popup.close(res);
					}
				});
			});
		})
		;
	});
});

