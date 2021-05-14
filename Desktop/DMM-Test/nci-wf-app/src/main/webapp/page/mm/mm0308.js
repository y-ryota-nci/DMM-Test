$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);

	var params = {
		activityDef: parentParams.activityDef,
		conditionDef: parentParams.conditionDef,
		messageCds : []
	};

	NCI.init('/mm0308/init', params).done(function(res) {
		if (res && res.success) {
			$('#btnCreate').prop('disabled', false);

			NCI.createOptionTags($('#transitUniqueKey'), res.activityDefs);
			NCI.createOptionTags($('#businessProcessStatus'), res.businessProcessStatuses);
			NCI.createOptionTags($('#businessActivityStatus'), res.businessActivityStatuses);
			NCI.createOptionTags($('#businessActivityStatusPre'), res.businessActivityStatuses);
			NCI.createOptionTags($('#expressionDefCode'), res.expressionDefs);

			if (res.conditionDef) {
				var $root = $('#inputed');
				NCI.toElementsFromObj(res.conditionDef, $root, []);
				$('select').prop('selectedIndex', 0);
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

			var msg = NCI.getMessage('MSG0069', NCI.getMessage('transitionDestination'));
			NCI.confirm(msg, function() {
				var conditionDef = NCI.toObjFromElements($root, []);
				$root.find('input[type=checkbox]').filter('[id]').each(function(i, e) {if (!conditionDef[$(e).attr('id')]) conditionDef[$(e).attr('id')] = '0';});
				var transitUniqueKey = conditionDef.transitUniqueKey.split(/:/g);
				conditionDef.corporationCodeTransit = transitUniqueKey[0];
				conditionDef.processDefCodeTransit = transitUniqueKey[1];
				conditionDef.processDefDetailCodeTran = transitUniqueKey[2];
				conditionDef.activityDefCodeTransit = transitUniqueKey[3];

				var params = {conditionDef: conditionDef};
				NCI.post('/mm0308/create', params).done(function(res) {
					if (res && res.success && res.conditionDef) {
						Popup.close(res);
					}
				});
			});
		})
		;
	});
});

