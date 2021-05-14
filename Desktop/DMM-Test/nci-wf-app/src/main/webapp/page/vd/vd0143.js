var ctx;
var partsId;
var type;
var calcs;
$(function() {
	let parameters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	ctx = parameters.ctx;
	partsId = parameters.partsId;
	type = parameters.type;
	calcs = (type == 'parts') ?  ctx.designMap[partsId].partsCalcs : parameters.calcs;
	if (calcs == null) {
		calcs = [];
	}
	var params = {messageCds : [ 'MSG0148', 'calcFormula', 'calcDef' ], partsId : partsId, type : type};
	NCI.init("/vd0143/init", params).done(function(res) {
		if (res && res.success) {
			$('#seach-result').removeClass('hide');
			$('#btnAdd').prop('disabled', false);
			$('#btnUpdate').prop('disabled', false);

			// 「計算式定義一覧」表示
			let $root = $('#seach-result');
			let responsiveTable = new ResponsiveTable($root);
			responsiveTable.fillTable(calcs);
			// 計算条件の編集ボタン、削除ボタンの制御
			vd0143.ctrl();

			// ソート機能を付与
			$('#tblCalcList tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
					resetIndex();
				}
			});
		}
	});

	$(document)
		// 検索結果の選択チェックボックス
		.on('change', 'input[type=checkbox].selectable', function() {
			let selected = $('input[type=checkbox].selectable:checked').length === 0;
			$('#btnDelete').prop('disabled', selected);
		})
		// 検索結果のリンク押下
		.on('click', 'a[data-field]', function(ev) {
			let $tr = $(this).closest('tbody tr');
			let index = $tr.index();
			let calc = calcs[index];
			let params = { 'ctx':ctx, 'partsId':partsId, 'index':index, 'calc':calc, 'type':type }
			Popup.open("../vd/vd0144.html", fromVd0144, params);
		})
		// 優先チェックボックス
		.on('click', 'input[type=checkbox].defaultFlag', function() {
			let $tr = $(this).closest('tbody tr');
			let index = $tr.index();
			let checked = $(this).prop('checked')
			// チェック時、計算条件編集ボタンは非活性
			$('button.btnEditEc', $tr).prop('disabled', checked);
			// チェックONであれば自身以外の優先チェックボックスはチェックを外す
			// またチェックが外れたものは計算条件の編集ボタンを活性化
			$('#tblCalcList tbody tr').each(function(idx) {
				calcs[idx].defaultFlag = (checked && idx == index ? '1' : '0');
				if (checked && idx != index) {
					$('input[type=checkbox].defaultFlag', $(this)).prop('checked', false);
					$('button.btnEditEc', $(this)).prop('disabled', false);
				}
			});
		})
		// 計算条件編集ボタン
		.on('click', 'button.btnEditEc', function(ev) {
			let $tr = $(this).closest('tbody tr');
			let index = $tr.index();
			let calc = calcs[index];
			let params = { 'ctx':ctx, 'partsId':partsId, 'index':index, 'calc':calc, 'type':type }
			Popup.open("../vd/vd0145.html", fromVd0145, params);
		})
		// 計算条件削除ボタン
		.on('click', 'button.btnDelEc', function(ev) {
			let $obj = $(this);
			let $tr = $obj.closest('tbody tr');
			let index = $tr.index();
			calcs[index].ecs = [];
			$obj.prop('disabled', true);
			// デフォルトフラグのチェックボックスを活性化
			$('input[type=checkbox].defaultFlag', $tr).prop('disabled', false);
		})
		// 追加ボタン
		.on('click', '#btnAdd', function(ev) {
			let params = { 'ctx':ctx, 'partsId':partsId, 'index':null, 'calc':null, 'type':type }
			Popup.open("../vd/vd0144.html", fromVd0144, params);
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			$('input[type=checkbox].selectable:checked').each(function() {
				let $tr = $(this).closest('tbody tr');
				let index = $tr.index();
				calcs.splice(index, 1);
				$tr.remove();
			});
			resetIndex();
			resetSortOrder();
		})
		// 更新ボタン
		.on('click', '#btnUpdate', function(ev) {
			// 入力チェック
			if (vd0143.validate()) {
				return false;
			}
			// コールバック関数の呼び出し
			let result = { 'calcs' : calcs, 'partsId' : partsId };
			Popup.close(result);
		})
		// 閉じるボタン
		.on('click', '#btnClose', function(ev) {
			Popup.close();
		})
	;
});

var vd0143 = {

	/** デフォルトフラグ、(計算条件の)編集／削除ボタンの制御 */
	ctrl : function() {
		$('#tblCalcList tbody tr').each(function(idx) {
			let $tr = $(this);
			let index = $tr.index();
			// デフォルトチェックボックスはONか
			let checked = $('input[type=checkbox].defaultFlag', $tr).prop('checked');
			// 計算条件は設定済みか
			let exist = !(calcs[idx].ecs == null || calcs[idx].ecs.length == 0);

			// 計算条件があれば優先チェックボックスは選択不可
			$('input[type=checkbox].defaultFlag', $tr).prop('disabled', exist);
			// デフォルトチェックボックスがONならば編集ボタンは非活性
			$('button.btnEditEc', $tr).prop('disabled', checked);
			// 計算条件が未設定であれば削除ボタンは非活性
			$('button.btnDelEc', $tr).prop('disabled', !exist);
			// 行番号を設定
			$('td[data-field=index]', $tr).text(index);
		});
	},

	/** 更新時入力チェック処理 */
	validate : function() {
		let error = false;
		$('#tblCalcList tbody tr').each(function(idx) {
			let $tr = $(this);
			let checked = $('input[type=checkbox].defaultFlag', $tr).prop('checked');
			if (checked && !(calcs[idx].ecs == null || calcs[idx].ecs.length == 0)) {
				var msg = NCI.getMessage('MSG0148');
				NCI.addMessage('danger', [msg]);
				error = true;
			}
		});
		return error;
	}
}

// 行番号のリセット
function resetIndex() {
	$('#tblCalcList tbody tr').find('td[data-field=index]').each(function(i, elem) {
		$(elem).text(i);
	});
}

//並び順をリセット
function resetSortOrder() {
	$('#tblCalcList tbody tr').find('td[data-field=index]').each(function(i, elem) {
		calcs[$(elem).text()].sortOrder = (++i);
	});
	calcs.sort(function(a, b){
	    if(a.sortOrder < b.sortOrder) return -1;
	    if(a.sortOrder > b.sortOrder) return 1;
	    return 0;
	});
	console.log(calcs);
}

function fromVd0144(result) {
	if (result) {
		// 計算項目がない場合、その計算式定義は削除とする
		let isDelete = (result.items == null || result.items.length == 0);
		// 計算式定義を更新
		if (result.index != null) {
			if (isDelete) {
				calcs.splice(result.index, 1);
			} else {
				let calc = calcs[result.index];
				calc.partsCalcName = result.partsCalcName;
				calc.items = result.items;
				calc.callbackFunction = result.callbackFunction;
			}
		} else if (result.items != null) {
			let calc = {};
			calc.partsId = result.partsId;
			calc.partsCalcName = result.partsCalcName;
			calc.items = result.items;
			calc.callbackFunction = result.callbackFunction;
			calc.sortOrder = (calcs.length + 1);
			calc.defaultFlag = '0';
			calc.ecs = [];
			calcs.push(calc);
		}
		// 一覧の内容を再設定
		let $root = $('#seach-result');
		let responsiveTable = new ResponsiveTable($root);
		responsiveTable.fillTable(calcs);
		// 計算条件の編集ボタン、削除ボタンの制御
		vd0143.ctrl();
	}
}

function fromVd0145(result) {
	if (result) {
		// 計算条件項目がない場合、その計算条件の削除ボタンは非活性
		let isDelete = (result.ecs == null || result.ecs.length == 0);
		// 計算式定義を取得
		let index = result.index;
		let calc = calcs[index];
		calc.ecs = result.ecs;

		// 計算条件の削除ボタン、デフォルトフラグの制御
		let $tr = $('#tblCalcList tbody tr').eq(index);
		// 計算条件があれば優先チェックボックスは選択不可
		$('input[type=checkbox].defaultFlag', $tr).prop('disabled', !isDelete);
		$('button.btnDelEc', $tr).prop('disabled', isDelete);
	}
}