// 組織選択：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	var $root = $('#vd0114_8');
	var params = { 'design' : design };
	NCI.post('/vd0114/initOrganizationSelect', params).done(function(res) {
		if (res && res.success) {
			// 表示項目の選択肢
			NCI.createOptionTags($('select.itemColumnSize'), res.columnSizes);
			// デフォルト値の選択肢
			NCI.createOptionTags($('#defaultValue'), res.defaultValues);
			// ボタンサイズの選択肢
			NCI.createOptionTags($("#buttonSize"), res.buttonSizes);

			// データを画面へ反映
			NCI.toElementsFromObj(design, $root);

			// 表示項目を並び順でソート
			let $trs = $('tr', '#tblDispItems tbody');
			$trs.sort(function($tr1, $tr2) {
				let v1 = $('td.sortOrder', $tr1).text();
				let v2 = $('td.sortOrder', $tr2).text();
				return (+v1) - (+v2);
			});
			// tbodyに対してappendし直す
			let $tbody = $('tbody', '#tblDispItems').empty();
			$.each($trs, function(idx, $tr) {
				$tbody.append($tr);
			});

			// ソート機能を付与
			$('#tblDispItems tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
				}
			});

			// 表示のチェックボックスON/OFF切替時の処理
			$('input[type=checkbox]', $('#tblDispItems')).on('change', function(ev) {
				let checked = $(this).prop('checked');
				let $tr = $(this).closest('tr');
				$('select.itemColumnSize', $tr).each(function() {
					$(this).prop('disabled', !checked);
				});
				$('i.glyphicon', $tr).each(function() {
					if (checked) {
						$(this).removeClass('invisible');
					} else {
						$(this).addClass('invisible');
					}
				});
			}).change();
		}
	});
}

//並び順をリセット
function resetSortOrder() {
	$('#tblDispItems tbody').find('td.sortOrder').each(function(i, elem) {
		$(elem).text(++i);
	});
}
