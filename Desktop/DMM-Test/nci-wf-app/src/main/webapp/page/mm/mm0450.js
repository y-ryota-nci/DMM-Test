$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);

	var params = {
		changeDef: parentParams.changeDef,
		messageCds : []
	};

	NCI.init('/mm0450/init', params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			$('#btnCreate').prop('disabled', false);

			NCI.createOptionTags($('#changeRoleCode'), res.changeRoles);
			NCI.createOptionTags($('#expressionDefCode'), res.expressionDefs);

			if (res.changeDef) {
				var $root = $('#inputed');
				NCI.toElementsFromObj(res.changeDef, $root, []);
				$('#changeRoleCode').prop('selectedIndex', 0);
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

				var msg = NCI.getMessage('MSG0069', NCI.getMessage('assignChange'));
				NCI.confirm(msg, function() {
					var params = {changeDef: NCI.toObjFromElements($root, [])};
					NCI.post('/mm0450/create', params).done(function(res) {
						if (res && res.success && res.changeDef) {
							Popup.close(res);
						}
					});
				});
			})
			;
		}
	});
});

