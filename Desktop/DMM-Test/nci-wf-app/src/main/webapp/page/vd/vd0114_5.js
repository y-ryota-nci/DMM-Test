// Radio：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	var $root = $('#vd0114_5');
	var params = { 'design' : design };
	NCI.post('/vd0114/initRadio', params).done(function(res) {
		if (res && res.success) {
			// パーツ選択肢
			NCI.createOptionTags($('#optionId'), res.options);

			// デフォルト値の選択肢
			NCI.createOptionTags($('#defaultValue'), res.defaultValueList);

			$('#optionId').on('change', function(ev) {
				Validator.hideBalloon();
				params.design.optionId = $(this).val();
				change();
			});

			if (!params.design.optionId) {
				$('#optionId').prop('selectedIndex', 0);
				$('#optionId').change();
			}
			// データを画面へ反映
			NCI.toElementsFromObj(design, $root);
			if ($('#defaultValue').prop('selectedIndex') < 0) {
				$('#defaultValue').prop('selectedIndex', 0);
			}
		}
	});

	function change() {
		NCI.post('/vd0114/changeOption', params).done(function(res) {
			if (res && res.success) {
				// デフォルト値の選択肢
				NCI.createOptionTags($('#defaultValue'), res.defaultValueList);
				$('#defaultValue').prop('selectedIndex', 0);
				design.optionItems = res.defaultValueList;
				design.optionItems.shift();
			}
		});
	}
}
