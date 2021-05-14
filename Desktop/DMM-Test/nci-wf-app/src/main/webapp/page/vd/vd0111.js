var ctx;
var partsId;
var type;
var conds;
$(function() {
	let parameters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	ctx = parameters.ctx;
	partsId = parameters.partsId;
	type = parameters.type;
	let tmpConds = (type == 'parts') ?  ctx.designMap[partsId].partsConds : parameters.conds;
	let tmpCondsMap = {};
	if (tmpConds != null) {
		$.each(tmpConds, function(idx, tmp) {
			tmpCondsMap[tmp.partsConditionType] = tmp;
		});
	}
	var params = {messageCds : [ 'MSG0148', 'condDef' ], partsId : partsId, type : type};
	NCI.init("/vd0111/init", params).done(function(res) {
		if (res && res.success) {
			$('#seach-result').removeClass('hide');
			$('#btnUpdate').prop('disabled', false);

			let templates = res.templates;
			// 渡された条件定義一覧にない条件定義はtemplatesから補完
			conds = [];
			$.each(templates, function(idx, obj) {
				let tmp = tmpCondsMap[obj.partsConditionType];
				if (tmp)
					conds[idx] = tmp;
				else
					conds[idx] = obj;
			});
			// 条件式の生成
			for (let i = 0; i < conds.length; i++) {
				let cond = conds[i];
				// 条件式が生成されていない場合のみここで生成してやる
				if (!cond.expression) {
					cond.expression = vd0111.createExpression(cond.items, res.logicalOperators, res.parentheses, res.comparisonOperators);
				}
			}

			// 「条件定義一覧」表示
			let $root = $('#seach-result');
			let responsiveTable = new ResponsiveTable($root);
			responsiveTable.fillTable(conds);
			// 条件定義のボタンの制御
			vd0111.ctrl();
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
			let cond = conds[index];
			let params = { 'ctx':ctx, 'partsId':partsId, 'index':index, 'cond':cond, 'type':type }
			Popup.open("../vd/vd0112.html", fromVd0112, params);
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			$('input[type=checkbox].selectable:checked').each(function() {
				let $tr = $(this).closest('tbody tr');
				let index = $tr.index();
				let cond = conds[index];
				cond.callbackFunction = '';
				cond.items = [];
				cond.expression = '';
				$('span[data-field=expression]', $tr).text(cond.expression);
				$('span[data-field=callbackFunction]', $tr).text(cond.callbackFunction);
				// チェックボックスはオフにして非活性
				vd0111.ctrl();
			});
		})
		// 更新ボタン
		.on('click', '#btnUpdate', function(ev) {
			// 入力チェック
			if (vd0111.validate()) {
				return false;
			}
			// 条件一覧より条件項目が未設定のものは対象外とする
			let rtnConds = [];
			$.each(conds, function(idx, cond) {
				if (cond.items != null && cond.items.length > 0) {
					rtnConds.push(cond);
				}
			});
			// コールバック関数の呼び出し
			let result = { 'conds' : rtnConds, 'partsId' : partsId };
			Popup.close(result);
		})
		// 閉じるボタン
		.on('click', '#btnClose', function(ev) {
			Popup.close();
		})
	;
});

var vd0111 = {

	/** チェックボックスの制御 */
	ctrl : function() {
		$('#btnAddCond1, btnAddCond2, btnAddCond3').prop('disabled', false);
		$('#tblCondList tbody tr').each(function(idx) {
			let $tr = $(this);
			let index = $tr.index();
			// 条件項目が設定されてなければチェックボックスはオフにして非表示（visibility:hidden)
			let visibility = (conds[idx].items != null && conds[idx].items.length > 0) ? 'visible' : 'hidden';
			$('input[type=checkbox]', $tr).prop('checked', false).css('visibility', visibility);
		});
	},

	/**
	 * 表示用条件式を生成.
	 * ※論理演算子一覧、括弧一覧、比較演算子一覧はKey-Valueの形式
	 * @param items 条件項目一覧
	 * @param logicalOperators 論理演算子一覧
	 * @param parentheses 括弧一覧
	 * @param comparisonOperators 比較演算子一覧
	 * @return 条件式
	 */
	createExpression: function(items, logicalOperators, parentheses, comparisonOperators) {
		let expression = '';
		$.each(items, function(idx, item) {
			if (item.itemClass == '1') {
				expression += logicalOperators[item.condType] + " ";
			}
			else if (item.itemClass == '2') {
				expression += parentheses[item.condType] + " ";
			}
			else if (item.itemClass == '3') {
				let val1 = item.condType;
				let val2 = comparisonOperators[item.operator];
				let val3 = (item.numericFlag === '1') ? "" : "'";
				let val4 = (item.targetLiteralVal) ? item.targetLiteralVal : "";
				// 表示用の条件式は「デザインコード△比較条件式△条件値」
				expression += ctx.designMap[val1].designCode + " " + val2 + " " + val3 + val4 + val3 + " ";
			}
		});
		return expression;
	},

	/** 更新時入力チェック処理 */
	validate : function() {
		let error = false;
		return error;
	}
}

function fromVd0112(result) {
	if (result) {
		// 条件項目がない場合、その条件定義は削除とする
		let isDelete = (result.items == null || result.items.length === 0);
		// 条件定義を更新
		if (result.index != null) {
			let cond = conds[result.index];
			if (isDelete) {
				cond.callbackFunction = '';
				cond.items = [];
				cond.expression = '';
			} else {
				cond.callbackFunction = result.callbackFunction;
				cond.items = result.items;
				cond.expression = result.expression;
			}
			// 条件式、コールバック関数を再設定
			let $tr = $('#tblCondList tbody tr').get(result.index);
			$('span[data-field=expression]', $tr).text(cond.expression);
			$('span[data-field=callbackFunction]', $tr).text(cond.callbackFunction);
		}
		vd0111.ctrl();
	}
}
