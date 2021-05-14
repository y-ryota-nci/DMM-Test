$(function() {
	var params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		messageCds : [ 'MSG0069', 'menuRole' ]
	};
	NCI.init("/rm0710/init", params).done(function(res) {
		if (res && res.success) {

			NCI.createOptionTags($('#corporationCode'), res.corporations);
			$('#corporationCode').val(NCI.loginInfo.corporationCode);

			$('#btnRegister').prop('disabled', false);

			if (res.menuRole) {
				$('input, select, textarea, span').filter('[id]').each(function(i, elem) {
					var val = res.menuRole[elem.id];
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
				NCI.redirect("./rm0700.html");
			})
			// 登録ボタン
			.on('click', '#btnRegister', function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("menuRole"));
				if (NCI.confirm(msg, function() {
					var params = {
						menuRole : NCI.toObjFromElements($root)
					};
					NCI.post("/rm0710/insert", params).done(function(res) {
						if (res && res.success && res.menuRole) {
							$('#btnRegister').hide();
							$("#menuRoleCode").prop('disabled', true);
							$("#menuRoleName").prop('disabled', true);
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

