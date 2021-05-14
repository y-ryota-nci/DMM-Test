$(function() {
	const params = {
			'corporationCode' : NCI.getQueryString('corporationCode'),
			'menuId' : NCI.getQueryString('menuId'),
			'screenProcessMenuId' : NCI.getQueryString('screenProcessMenuId'),
			'version' : NCI.getQueryString('version')
	};
	NCI.init('/vd0091/init', params).done(function(res) {
		if (res && res.success) {
			// 画面プロセス定義の選択肢
			NCI.createOptionTags($('#screenProcessId'), res.screenProcessDefs);

			init(res);
		}
	});

	$(document)
		.on('click', '#btnUpdate', doUpdate)
		.on('click', '#btnBack', doBack);

	/** 初期化 */
	function init(res) {
		// データの反映
		NCI.toElementsFromObj(res.entity, $('#inputed'));

		$('#btnUpdate').prop('disabled', false);
	}

	/** 更新処理 */
	function doUpdate(ev) {
		const $targets = $('#inputed').find('input, select, textarea');
		if (!Validator.validate($targets))
			return false;

		const msg = NCI.getMessage("MSG0071", NCI.getMessage("screenProcessMenuInfo"));
		if (NCI.confirm(msg, function() {
			const params = {
					'entity' : NCI.toObjFromElements($('#inputed'))
			};
			NCI.post('/vd0091/save', params).done(function(res) {
				init(res);
			});
		}));

	}

	/** 戻るボタン押下 */
	function doBack(ev) {
		NCI.redirect('./vd0090.html');
	}
});
