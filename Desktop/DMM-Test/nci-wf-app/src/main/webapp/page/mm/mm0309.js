$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);

	var params = {
		functionDef: parentParams.functionDef,
		messageCds : []
	};

	NCI.init('/mm0309/init', params).done(function(res) {
		if (res && res.success) {
			$('#btnCreate').prop('disabled', false);

			NCI.createOptionTags($('#functionCode'), res.functions);
			NCI.createOptionTags($('#executionTimingType'), res.executionTimingTypes);

			if (res.functionDef) {
				var $root = $('#inputed');
				NCI.toElementsFromObj(res.functionDef, $root, []);
				$('#functionCode').prop('selectedIndex', 0);
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

			var msg = NCI.getMessage('MSG0069', NCI.getMessage('function'));
			NCI.confirm(msg, function() {
				var functionDef = NCI.toObjFromElements($root, []);
				$root.find('input[type=checkbox]').filter('[id]').each(function(i, e) {if (!functionDef[$(e).attr('id')]) functionDef[$(e).attr('id')] = '0';});

				var params = {functionDef: functionDef};
				NCI.post('/mm0309/create', params).done(function(res) {
					if (res && res.success && res.functionDef) {
						Popup.close(res);
					}
				});
			});
		})
		;
	});
});

