$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);

	var params = {
		assignedDef: parentParams.assignedDef,
		messageCds : []
	};

	NCI.init('/mm0305/init', params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			$('#btnCreate').prop('disabled', false);

			NCI.createOptionTags($('#assignRoleCode'), res.assignRoles);
			NCI.createOptionTags($('#expressionDefCode'), res.expressionDefs);

			if (res.assignedDef) {
				var $root = $('#inputed');
				NCI.toElementsFromObj(res.assignedDef, $root, []);
				$('#assignRoleCode').prop('selectedIndex', 0);
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

				var msg = NCI.getMessage('MSG0069', NCI.getMessage('assigned'));
				NCI.confirm(msg, function() {
					var params = {assignedDef: NCI.toObjFromElements($root, [])};
					NCI.post('/mm0305/create', params).done(function(res) {
						if (res && res.success && res.assignedDef) {
							Popup.close(res);
						}
					});
				});
			})
			;
		}
	});
});

