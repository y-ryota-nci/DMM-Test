$(function() {
	// パーツのデザイン定義をFlushScopeから取得
	let paramters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let ctx = paramters.ctx;
	let partsId = paramters.partsId;
	let design = ctx.designMap[partsId];
	let index = paramters.index;
	let calc = paramters.calc;
	let type = paramters.type;

	// コンテナ一覧およびコンテナ毎のパーツマップを生成
	let container = (design.parentPartsId > 0) ? ctx.designMap[design.parentPartsId] : ctx.root;
	let containers = createContainerList(container, ctx, []);
	let partsMap = createPartsMapByContainer(containers, ctx, design.partsId);
	// 自パーツがあるコンテナを初期値として設定する
	let defaultContainer = (design.parentPartsId != null && design.parentPartsId > 0) ? ctx.designMap[design.parentPartsId] : ctx.root;
	let defaultContainerCode = defaultContainer.containerCode;
	let defaultPartsList = partsMap[defaultContainerCode];

	var $root = $('#setting');
	var responsiveTable = new ResponsiveTable($root);
	var template = responsiveTable.getTemplate();
	var $tbl = $('#tblCalcItems');
	var labels = null;
	// 初期化時のパラメータ生成
	let params = {};
	NCI.init("/vd0144/init", params).done(function(res) {
		if (res && res.success) {
			vd0144.arithmeticOperators = res.arithmeticOperators;
			vd0144.parentheses         = res.parentheses;
			labels = responsiveTable.getHeaderLabels();

			// 計算式定義があれば計算式名、コールバック関数、計算式項目を表示
			if (calc != null) {
				$('#partsCalcName').val(calc.partsCalcName);
				$('#callbackFunction').val(calc.callbackFunction);
				if (calc.items != null) {
					$.each(calc.items, function(i, item) {
						let idx = null;
						if (item.calcItemType == '1') {
							item.dispItemValue = vd0144.arithmeticOperators[item.calcItemValue];
							idx = 0;
						} else if (item.calcItemType == '2') {
							item.dispItemValue = vd0144.parentheses[item.calcItemValue];
							idx = 1;
						} else if (item.calcItemType == '3') {
							idx = 2;
						} else {
							// パーツが属するコンテナが保持しているパーツ一覧を取得
							let partsList = getPartsList(item.calcItemValue, partsMap, ctx);
							// パーツ指定時のパーツ選択オプションタグを生成
							NCI.createOptionTags($('#tblCalcItems>tfoot>tr select[data-field="calcItemValue"]'), partsList);
							idx = 3;
						}
						vd0144.addRow(item, idx);
					});
					// 全部表示が終わったら表示する計算式を生成
					vd0144.createFormula();
				}
			}
			// コンテナ選択用のオプションタグおよび初期値を設定
			let $select = $('#container');
			NCI.createOptionTags($select, containers).val(defaultContainerCode);
			// パラメータの"type"が"screen"の場合、コンテナ選択ドロップダウンを開放
			if (type == 'screen') {
				$select.closest('tr').removeClass('hide');
			}
			// パーツ指定時のパーツ選択オプションタグを設定
			NCI.createOptionTags($('#tblCalcItems>tfoot>tr select[data-field="calcItemValue"]'), defaultPartsList);

			// ソート機能を付与
			$('#tblCalcItems tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
					vd0144.createFormula();
				}
			});
		}
	});

	$(document)
		// コンテナ変更時処理
		.on('change', '#container', function(ev) {
			let val = $(ev.currentTarget).val();
			let partsList = partsMap[val];
			// パーツ指定時のパーツ選択オプションタグを再設定
			NCI.createOptionTags($('#tblCalcItems>tfoot>tr select[data-field="calcItemValue"]'), partsList);
		})
		// 算術演算子ボタン押下
		.on('click', '#btnPlus, #btnMinus, #btnMultiply, #btnDivide', function(ev) {
			let val = $(ev.currentTarget).val();
			let obj = {'partsId':partsId, 'dispItemValue':vd0144.arithmeticOperators[val], 'calcItemType':1, 'calcItemValue':val};
			vd0144.addRow(obj, 0);
			vd0144.createFormula();
		})
		// 括弧ボタン押下
		.on('click', '#btnOpenParenthesis, #btnCloseParenthesis', function(ev) {
			let val = $(ev.currentTarget).val();
			let obj = {'partsId':partsId, 'dispItemValue':vd0144.parentheses[val], 'calcItemType':2, 'calcItemValue':val};
			vd0144.addRow(obj, 1);
			vd0144.createFormula();
		})
		// 定数ボタン押下
		.on('click', '#btnLiteral', function(ev) {
			let obj = {'partsId':partsId, 'calcItemType':3};
			vd0144.addRow(obj, 2);
			vd0144.createFormula();
		})
		// パーツ指定ボタン押下
		.on('click', '#btnParts', function(ev) {
			let obj = {'partsId':partsId, 'calcItemType':4};
			vd0144.addRow(obj, 3);
			vd0144.createFormula();
		})
		// 定数・パーツの入力値変更時処理
		.on('change', 'input.literal, select.parts', function(ev) {
			vd0144.createFormula();
		})
		// 計算項目削除ボタン押下
		.on('click', 'button.btnDelItem', function(ev) {
			// 画面上から削除するのみ
			var $tr = $(this).closest('tr');
			$tr.remove();
			vd0144.createFormula();
			// 項目が１つもなければ登録ボタンは非活性
			if ($('tbody tr', $tbl).length == 0) {
				$('#btnRegist').prop('disabled', true);
			}
		})
		// 登録ボタン
		.on('click', '#btnRegist', function(ev) {
			if (!Validator.validate($('#partsCalcName'), true) || !Validator.validate($root.find("input, textarea, select", true))) {
				return false;
			}
			// 並び順を再セット
			resetSortOrder();
			let name  = $('#partsCalcName').val();
			let callbackFunction = $('#callbackFunction').val();
			let items = NCI.toArrayFromTable($root, ['dispItemValue']);
			let req = { partsCalcName : name, items : items };
			NCI.post("/vd0144/verify", req).done(function(res) {
				if (res && res.success) {
					let params = {
							partsId:partsId,
							partsCalcName:name,
							index:index,
							items:items,
							callbackFunction:callbackFunction
						};
					// コールバック関数の呼び出し
					Popup.close(params);
				}
			});
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			let params = null;
			if (calc != null) {
				params = {index:index, items:null};
			}
			Popup.close(params);
		})
		// 閉じるボタン
		.on('click', '#btnClose', function(ev) {
			Popup.close();
		})
	;

	var vd0144 = {
		/** 算術演算子一覧 */
		arithmeticOperators: null,
		/** 括弧一覧 */
		parentheses: null,

		addRow: function(entity, idx) {
			let $tr = $(responsiveTable.toNewLine(template)[idx]);
			responsiveTable.fillRowResult($tr, 0, entity, labels);
			$('table.responsive tbody', $root).append($tr);
			resetSortOrder();
			// 登録・削除ボタンを活性化
			$('#btnRegist, #btnDelete').prop('disabled', false);
		},

		/** 表示用計算式を生成 */
		createFormula: function() {
			let formula = '';
			$('tbody tr', $tbl).each(function() {
				const $tr = $(this);
				const calcItemType = $('td[data-field="calcItemType"]', $tr).text();
				const forceCalcFlag = null;
				if (calcItemType === '4') {	// パーツ
					let val = $('select[data-field="calcItemValue"]', $tr).val();
					if (val && val != '') {
						if (forceCalcFlag === 'true')
							formula += "(+" + ctx.designMap[val].designCode + ") ";	// (+パーツ値)とすることで、パーツ値=''でも0として強制計算を実現
						else
							formula += ctx.designMap[val].designCode + " ";
					}
				} else if (calcItemType === '3') {	// リテラル値
					let val = $('input[data-field="calcItemValue"]', $tr).val();
					if (val && val != '') {
						formula += val + " ";
					}
				} else {
					formula += $('span[data-field="dispItemValue"]', $tr).text() + " ";
				}
			});
			$('#formula').text(formula);
		}
	};
});

//並び順をリセット
function resetSortOrder() {
	$('#tblCalcItems tbody tr').find('td[data-field=sortOrder]').each(function(i, elem) {
		$(elem).text(++i);
	});
}

/**
 * コンテナ一覧を生成.
 * 対象となるコンテナは計算式を設定するパーツと同じか下位コンテナが対象
 */
function createContainerList(container, ctx, containers) {
	containers.push( {label:container.containerName, value:container.containerCode, partsId:container.partsId} );

	// コンテナの配下にさらに子コンテナがあれば再帰呼出
	$.each(container.childPartsIds, function() {
		let parts = ctx.designMap[this];
		if (parts.childPartsIds) {
			createContainerList(parts, ctx, containers);
		}
	});
	return containers;
}

/**
 * パーツ指定時に使用するコンテナ毎のパーツ選択一覧を生成.
 * 指定できるパーツは「テキストパーツで、かつ入力タイプが"4:数値"。ただし自分自身は除外」
 * @param containers コンテナ一覧
 * @param ctx
 * @param targetPartsId (有効条件を設定する対象パーツの)パーツID
 * @returns
 */
function createPartsMapByContainer(containers, ctx, targetPartsId) {
	let partsMap = {};
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
				if (parts.partsType == PartsType.TEXTBOX && parts.inputType == PartsInputType.NUMBER && parts.partsId != targetPartsId) {
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