$(function() {
	var params = {
		optionId : NCI.getQueryString("optionId"),
		version  : NCI.getQueryString("version"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'optionSetting', 'optionItem' ]
	};
	NCI.init("/vd0062/init", params).done(function(res) {
		if (res && res.success) {
			// 選択肢マスタの設定
			NCI.toElementsFromObj(res.entity, $('#option'));
			// 選択肢項目マスタ一覧の設定
			new ResponsiveTable($('#items')).fillTable(res.items);

			// 各種ボタンの活性・非活性の制御
			$('#btnUpdate').prop('disabled', false);
			$('#btnAdd').prop('disabled', false);
			$('#btnRegist').prop('disabled', (res.items.length == 0));

			// ソート機能を付与
			$('#tblOptionItem tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
				}
			});

			$(document)
				// 戻るボタン押下
				.on('click', '#btnBack', function(ev) {
					NCI.redirect("./vd0060.html");
				})
				// 更新ボタン押下
				.on('click', '#btnUpdate', function(ev) {
					let $root = $('#option');
					let $targets = $root.find('input, select, textarea');
					if (!Validator.validate($targets, true)) {
						return false;
					}
					let msg = NCI.getMessage("MSG0071", NCI.getMessage("optionSetting"));
					if (NCI.confirm(msg, function() {
						let params = { entity : NCI.toObjFromElements($root) };
						NCI.post("/vd0062/update", params).done(function(res) {
							if (res && res.success && res.entity) {
								// 選択肢マスタ情報を再設定
								NCI.toElementsFromObj(res.entity, $root);
							}
						});
					}));
				})
				// 追加ボタン押下
				.on('click', '#btnAdd', function(ev) {
					// 一覧に空行を追加
					new ResponsiveTable($('#items')).addRowResult([]);
					// 並び順再設定
					resetSortOrder();
					// 登録ボタンの活性化
					$('#btnRegist').prop('disabled', false);
				})
				// 削除ボタン押下
				.on('click', 'button.btnDelItem', function(ev) {
					// 画面上から削除するのみ
					var $tr = $(this).closest('tr');
					$tr.remove();
					// 全行削除されたら登録ボタンを非活性に
					if ($('#tblOptionItem tbody tr').length == 0) {
						new ResponsiveTable($('#items')).dispNoRecordMessage([]);
						$('#btnRegist').prop('disabled', true);
					}

				})
				// 登録ボタン押下
				.on('click', '#btnRegist', function(ev) {
					let $root = $('#items');
					let $targets = $root.find('input, select, textarea');
					if (!Validator.validate($targets, true)) {
						return false;
					}
					var msg = NCI.getMessage("MSG0069", NCI.getMessage("optionItem"));
					if (NCI.confirm(msg, function() {
						var params = {
							entity : NCI.toObjFromElements($('#option')),
							items  : NCI.toArrayFromTable($root)
						};
						NCI.post("/vd0062/regist", params).done(function(res) {
							if (res && res.success) {
								// 選択肢項目一覧への再設定
								new ResponsiveTable($('#items')).fillTable(res.items);
							}
						});
					}));
				})
			;
		}
	});
});

//並び順をリセット
function resetSortOrder() {
	$('#tblOptionItem tbody').find('td[data-field=sortOrder]').each(function(i, elem) {
		$(elem).text(++i);
	});
}
