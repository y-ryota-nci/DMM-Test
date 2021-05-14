$(function() {
	var params = {
		messageCds : [ 'MSG0069', 'optionSetting' ]
	};
	NCI.init("/vd0061/init", params).done(function(res) {
		if (res && res.success) {
			// 選択肢マスタ情報の初期値設定
			NCI.toElementsFromObj(res.entity, $('#inputed'));
			// 登録ボタンを活性化
			$('#btnRegist').prop('disabled', false);

			$(document)
				// 戻るボタン押下
				.on('click', '#btnBack', function(ev) {
					NCI.redirect("./vd0060.html");
				})
				// 登録ボタン
				.on('click', '#btnRegist', function(ev) {
					let $root = $('#inputed');
					let $targets = $root.find('input, select, textarea');
					if (!Validator.validate($targets, true))
						return false;

					var msg = NCI.getMessage("MSG0069", NCI.getMessage("optionSetting"));
					if (NCI.confirm(msg, function() {
						var params = {
							entity : NCI.toObjFromElements($root)
						};
						NCI.post("/vd0061/regist", params).done(function(res) {
							if (res && res.success && res.entity) {
								let optionId = res.entity.optionId;
								let version  = res.entity.version;
								console.log(optionId + ":" + version);
								// 選択肢項目設定画面へ遷移
								NCI.redirect("./vd0062.html?optionId=" + optionId + "&version=" + version);
							}
						});
					}));
				})
			;
		}
	});
});

