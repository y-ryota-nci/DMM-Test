// 条件の判定元パーツになれるのは次の項目のみ
// テキストボックス・チェックボックス・ラジオボタン・ドロップダウンリスト・マスタ選択
const TargetPartsType = [PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO, PartsType.DROPDOWN, PartsType.MASTER];

$(function() {
	// パーツのデザイン定義をFlushScopeから取得
	let parameters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let ctx = parameters.ctx;
	let design = ctx.designMap[parameters.partsId];
	let index = parameters.index;
	let cond = parameters.cond;
	let type = parameters.type;

	// コンテナ一覧およびコンテナ毎のパーツマップを生成
	let containers = createContainerList(design, ctx, []);
	let partsMap = createPartsMapByContainer(containers, ctx, design.partsId);
	// 自パーツがあるコンテナを初期値として設定する
	let defaultContainer = (design.parentPartsId != null && design.parentPartsId > 0) ? ctx.designMap[design.parentPartsId] : ctx.root;
	let defaultContainerCode = defaultContainer.containerCode;
	let defaultPartsList = partsMap[defaultContainerCode];

	var $root = $('#setting');
	var responsiveTable = new ResponsiveTable($root);
	var template = responsiveTable.getTemplate();
	var labels = null;
	var $tbl = $('#tblCondItems');
	// 初期化処理
	NCI.init("/vd0112/init", {}).done(function(res) {
		if (res && res.success) {
			// sub-titleの設定
//			const selector = "#subTitle" + cond.partsConditionType;
			$("#subTitle" + cond.partsConditionType).toggleClass('hide', false);

			labels = responsiveTable.getHeaderLabels();
			vd0112.logiclOperators     = res.logiclOperators;
			vd0112.parentheses         = res.parentheses;
			// 比較演算子用のオプションタグを生成
			NCI.createOptionTags($('select[data-field="operator"]'), res.comparisonOperators);

			// 現在の条件設定内容を一覧へ反映
			if (cond != null) {
				// コールバック関数を設定
				$('#callbackFunction').val(cond.callbackFunction);
				// 条件項目を設定
				if (cond.items != null) {
					$.each(cond.items, function(i, item) {
						let idx = null;
						if (item.itemClass == '1') {
							idx = 0;
							item.dispCondType = vd0112.logiclOperators[item.condType];
						} else if (item.itemClass == '2') {
							idx = 1;
							item.dispCondType = vd0112.parentheses[item.condType];
						} else if (item.itemClass == '3') {
							// パーツが属するコンテナが保持しているパーツ一覧を取得し、パーツ指定時のパーツ選択オプションタグを生成
							let partsList = getPartsList(item.condType, partsMap, ctx);
							NCI.createOptionTags($('#tblCondItems>tfoot>tr select[data-field="condType"]'), partsList);
							idx = 2;
						}
						vd0112.addRow(item, idx);
					});
				}
				// 条件式を生成して表示
				vd0112.createExpression();
			}

			// コンテナ選択用のオプションタグおよび初期値を設定
			let $select = $('#container');
			NCI.createOptionTags($select, containers).val(defaultContainerCode);
			// パラメータの"type"が"screen"の場合、コンテナ選択ドロップダウンを開放
			if (type == 'screen') {
				$select.closest('tr').removeClass('hide');
			}
			// パーツ指定時のパーツ選択オプションタグを設定
			NCI.createOptionTags($('#tblCondItems>tfoot>tr select[data-field="condType"]'), defaultPartsList);

			// ソート機能を付与
			$('#tblCondItems tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
					vd0112.createExpression();
				}
			});

			$(document)
				// 数値化フラグ変更時の特殊処理
				.on('changeClass', 'input[data-field="numericFlag"]', function() {
					// 比較条件値を数値として処理するなら条件値は必須とする
					let $tr = $(this).closest('tr');
					let $target = $('input[data-field="targetLiteralVal"]', $tr);
					if ($(this).prop('checked')) {
						let validate = {"pattern":"numeric","roundType":4,"decimalPlaces":5};
						$target.addClass('required').data('validate', validate);
					} else {
						$target.removeClass('required').removeData("validate");
					}
				})
				// コンテナ変更時処理
				.on('change', '#container', function(ev) {
					let val = $(ev.currentTarget).val();
					let partsList = partsMap[val];
					// パーツ指定時のパーツ選択オプションタグを再設定
					NCI.createOptionTags($('#tblCondItems>tfoot>tr select[data-field="condType"]'), partsList);
				})
				// パーツ指定ボタン押下
				.on('click', '#btnAndParts', function(ev) {
					let entity = {partsId : design.partsId, itemClass : 3};
					vd0112.addRow(entity, 2);
					vd0112.createExpression();
				})
				// 論理演算子ボタン押下
				.on('click', '#btnAnd, #btnOr, #btnNot', function(ev) {
					let val = $(ev.currentTarget).val();
					let obj = {partsId : design.partsId, dispCondType : vd0112.logiclOperators[val], itemClass : 1, condType : val};
					vd0112.addRow(obj, 0);
					vd0112.createExpression();
				})
				// 括弧ボタン押下
				.on('click', '#btnOpenParenthesis, #btnCloseParenthesis', function(ev) {
					let val = $(ev.currentTarget).val();
					let obj = {partsId : design.partsId, dispCondType : vd0112.parentheses[val], itemClass : 2, condType : val};
					vd0112.addRow(obj, 1);
					vd0112.createExpression();
				})
				// パーツ、比較条件式、条件、数値化フラグ変更時処理
				.on('change', 'select.condType, select.operator, input[data-field="targetLiteralVal"]', function() {
					vd0112.createExpression();
				})
				// 数値化フラグ変更時処理
				.on('click', 'input[data-field="numericFlag"]', function() {
					$(this).trigger('changeClass');
					vd0112.createExpression();
				})
				// 条件項目削除ボタン押下
				.on('click', 'button.btnDelItem', function(ev) {
					// 画面上から削除するのみ
					var $tr = $(this).closest('tr');
					$tr.remove();
					vd0112.createExpression();
				})
				// 登録ボタン押下
				.on('click', '#btnRegist', function(ev) {
					if (!Validator.validate($root.find("input, textarea, select", true))) {
						return false;
					}
					var items = NCI.toArrayFromTable($root, ['dispCondType']);
					var req = { 'items' : items, 'partsId' : design.partsId };
					NCI.post("/vd0112/verify", req).done(function(res) {
						// 問題がなければコールバック関数の呼び出し
						if (res && res.success) {
							let callbackFunction = $('#callbackFunction').val();
							let expression = $('#expression').text();
							let result = { 'index' : index, 'callbackFunction' : callbackFunction, 'items' : items, 'expression' : expression, 'partsId' : design.partsId };
							Popup.close(result);
						}
					});
				})
				// 削除ボタン押下
				.on('click', '#btnDelete', function(ev) {
					let result = { 'index' : index, 'items' : null, 'partsId' : design.partsId };
					Popup.close(result);
				})
				// 閉じるボタン押下
				.on('click', '#btnClose', function(ev) {
					Popup.close();
				})
			;

			$('#tblCondItems tbody input[data-field="numericFlag"]').trigger('changeClass');
		}
	});

	var vd0112 = {
		/** 論理演算子一覧 */
		logiclOperators: null,
		/** 括弧一覧 */
		parentheses: null,
		/** 比較演算子一覧 */
		comparisonOperators: null,

		/** パーツ追加処理 */
		addRow: function(entity, idx) {
			let $tr = $(responsiveTable.toNewLine(template)[idx]);
			responsiveTable.fillRowResult($tr, 0, entity, labels);
			$('table.responsive tbody', $root).append($tr);
			resetSortOrder();
		},

		/** 表示用条件式を生成 */
		createExpression: function() {
			let expression = '';
			$('tbody tr', $tbl).each(function() {
				let $tr = $(this);
				let itemClass = $('td[data-field="itemClass"]', $tr).text();
				if (itemClass == '3') {
					let val1 = $('select[data-field="condType"]', $tr).val();
					let val2 = $('select[data-field="operator"] option:selected', $tr).val();
					let val3 = $('select[data-field="operator"] option:selected', $tr).text();
					let val4 = $('input[data-field="targetLiteralVal"]', $tr).val();
					let val5 = $('input[data-field="numericFlag"]', $tr).prop('checked');
					if ((val1 && val1 != '') && (val2 && val2 != '')) {
						// 表示用の条件式は「デザインコード△比較条件式△条件値」
						if (val5) {
							expression += ctx.designMap[val1].designCode + " " + val3 + " " + val4 + " ";
						} else {
							expression += ctx.designMap[val1].designCode + " " + val3 + " '" + val4 + "' ";
						}
					}
				} else {
					expression += $('span[data-field="dispCondType"]', $tr).text() + " ";
				}
			});
			$('#expression').text(expression);
			// 条件式が空でなければ何からしらの条件項目があるとみなして登録ボタンを活性化
			$('#btnRegist').prop('disabled', (expression == ''));
		}
	};
});

//並び順をリセット
function resetSortOrder() {
	$('#tblCondItems tbody').find('td[data-field=sortOrder]').each(function(i, elem) {
		$(elem).text(++i);
	});
}

/**
 * コンテナ一覧を生成.
 * 対象となるコンテナは有効条件を設定するパーツと同じか上位コンテナが対象
 */
function createContainerList(parts, ctx, containers) {
	if (parts.parentPartsId == null || parts.parentPartsId < 0) {
		let root = ctx.root;
		containers.push( {label:root.containerName, value:root.containerCode, partsId:root.partsId} );
	} else {
		let parent = ctx.designMap[parts.parentPartsId];
		createContainerList(parent, ctx, containers);
		containers.push( {label:parent.containerName, value:parent.containerCode, partsId:parent.partsId} );
	}
	return containers;
}

/**
 * パーツ指定時に使用するコンテナ毎のパーツ選択一覧を生成.（ただし自分自身は除外）
 * @param containers コンテナ一覧
 * @param ctx
 * @param targetPartsId (有効条件を設定する対象パーツの)パーツID
 * @returns
 */
function createPartsMapByContainer(containers, ctx, targetPartsId) {
	let partsMap = [];
	let len = containers.length;
	let designMap = ctx.designMap;

	for (let i = 0; i < len; i++) {
		let container = null;
		if (containers[i].partsId > 0) {
			container = designMap[containers[i].partsId];
		} else {
			container = ctx.root;
		}
		if (container && container.childPartsIds) {
			let partsList = [{label:'--', value:''}];
			$.each(container.childPartsIds, function(idx, val) {
				let parts = designMap[val];
				// 有効条件の判定元対象パーツで、かつ(有効条件を設定する)パーツ自身でないならpartsListに追加
				if ( ($.inArray(parts.partsType, TargetPartsType) >= 0) && (parts.partsId != targetPartsId) ) {
					let option = {label:parts.partsCode + " " + parts.labelText, value:parts.partsId};
					partsList.push(option);
				}
			});
			partsMap[container.containerCode] = partsList;
		}
	}
	return partsMap;
}

/**
 * パーツIDが属しているコンテナ内のパーツマップ取得.
 * @param partsId 対象パーツID
 * @param partsMap コンテナ毎のパーツ一覧
 * @param ctx
 */
function getPartsList(partsId, partsMap, ctx) {
	let parts = ctx.designMap[partsId];
	let container = null;
	if (parts.parentPartsId == null || parts.parentPartsId < 0) {
		container = ctx.root;
	} else {
		container = ctx.designMap[parts.parentPartsId];
	}
	return partsMap[container.containerCode];
}