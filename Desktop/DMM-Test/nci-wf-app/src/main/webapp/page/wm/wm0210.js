$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		messageCds : [ 'MSG0069', 'assignRole' ]
	};
	NCI.init("/wm0210/init", params).done(function(res) {
		if (res && res.success) {

			$('#btnRegister').prop('disabled', false);

			if (res.assignRole) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.assignRole[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
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
				NCI.redirect("./wm0200.html");
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("assignRole"));
				if (NCI.confirm(msg, function() {
					var params = {
						assignRole : NCI.toObjFromElements($root)
					};
					NCI.post("/wm0210/insert", params).done(function(res) {
						if (res && res.success && res.assignRole) {
							$('#btnRegister').hide();
							$("#assignRoleCode").prop('disabled', true);
							$("#assignRoleName").prop('disabled', true);
							$("#validStartDate").prop('disabled', true);
							$("#validEndDate").prop('disabled', true);
						}
					});
				}));
			})
			;
		}
	});
});

