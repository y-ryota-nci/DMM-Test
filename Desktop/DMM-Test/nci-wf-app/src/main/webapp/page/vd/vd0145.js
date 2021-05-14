// 有効条件の判定元パーツになれるのは次の項目のみ
// テキストボックス・チェックボックス・ラジオボタン・ドロップダウンリスト
const TargetPartsType = [PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO, PartsType.DROPDOWN, PartsType.MASTER];

$(function() {
	// パーツのデザイン定義をFlushScopeから取得
	let paramters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let ctx = paramters.ctx;
	let design = ctx.designMap[paramters.partsId];
	let index = paramters.index;
	let calc = paramters.calc;
	let type = paramters.type;
	let ecs = calc.ecs;
	// コンテナ一覧およびコンテナ毎のパーツマップを生成
	let containers = createContainerList(design, ctx, []);
	let partsMap = createPartsMapByContainer(containers, ctx, design.partsId);
	// 自パーツがあるコンテナを初期値として設定する
	let defaultContainer = (design.parentPartsId != null && design.parentPartsId > 0) ? ctx.designMap[design.parentPartsId] : ctx.root;
	let defaultContainerCode = defaultContainer.containerCode;
	let defaultPartsList = partsMap[defaultContainerCode];

	let $root = $('#setting');
	let responsiveTable = new ResponsiveTable($root);
	let template = responsiveTable.getTemplate();
	let $tbl = $('#tblCalcEcItems');
	let labels = null;
	// 初期化時のパラメータ生成
	let params = {};
	// 初期化処理
	NCI.init("/vd0145/init", params).done(function(res) {
		if (res && res.success) {
			labels = responsiveTable.getHeaderLabels();
			vd0145.logiclOperators     = res.logiclOperators;
			vd0145.parentheses         = res.parentheses;
			// 比較演算子用のオプションタグを生成
			NCI.createOptionTags($('select[data-field="operator"]'), res.comparisonOperators);

			// 計算条件式定義があれば計算条件の項目を表示
			if (calc != null && calc.ecs != null) {
				$.each(calc.ecs, function(i, ec) {
					let idx = null;
					if (ec.itemClass == '1') {
						idx = 0;
						ec.dispCondType = vd0145.logiclOperators[ec.condType];
					} else if (ec.itemClass == '2') {
						idx = 1;
						ec.dispCondType = vd0145.parentheses[ec.condType];
					} else if (ec.itemClass == '3') {
						// パーツが属するコンテナが保持しているパーツ一覧を取得し、パーツ指定時のパーツ選択オプションタグを生成
						let partsList = getPartsList(ec.condType, partsMap, ctx);
						NCI.createOptionTags($('#tblCalcEcItems>tfoot>tr select[data-field="condType"]'), partsList);
						idx = 2;
					}
					vd0145.addRow(ec, idx);
				});
				// 計算条件式を生成
				vd0145.createExpression();
			}

			// コンテナ選択用のオプションタグおよび初期値を設定
			let $select = $('#container');
			NCI.createOptionTags($select, containers).val(defaultContainerCode);
			// パラメータの"type"が"screen"の場合、コンテナ選択ドロップダウンを開放
			if (type == 'screen') {
				$select.closest('tr').removeClass('hide');
			}
			// パーツ指定時のパーツ選択オプションタグを設定
			NCI.createOptionTags($('#tblCalcEcItems>tfoot>tr select[data-field="condType"]'), defaultPartsList);

			// ソート機能を付与
			$('#tblCalcEcItems tbody').sortable({
				handle : 'i.glyphicon.glyphicon-align-justify',
				update : function(ev, ui) {
					resetSortOrder();
					vd0145.createExpression();
				}
			});

			$(document)
				// 数値化フラグ変更時の特殊処理
				.on('changeClass', 'input[data-field="numericFlag"]', function() {
					// 比較条件値を数値として処理するなら条件値は必須とする
					let $tr = $(this).closest('tr');
					let $target = $('input[data-field="targetLiteralVal"]', $tr);
					if ($(this).prop('checked')) {
						$target.addClass('required');
					} else {
						$target.removeClass('required');
					}
				})
				// コンテナ変更時処理
				.on('change', '#container', function(ev) {
					let val = $(ev.currentTarget).val();
					let partsList = partsMap[val];
					// パーツ指定時のパーツ選択オプションタグを再設定
					NCI.createOptionTags($('#tblCalcEcItems>tfoot>tr select[data-field="condType"]'), partsList);
				})
				// パーツ指定ボタン押下
				.on('click', '#btnAndParts', function(ev) {
					let entity = {partsId : design.partsId, itemClass : 3};
					vd0145.addRow(entity, 2);
					vd0145.createExpression();
				})
				// 論理演算子ボタン押下
				.on('click', '#btnAnd, #btnOr, #btnNot', function(ev) {
					let val = $(ev.currentTarget).val();
					let obj = {partsId : design.partsId, dispCondType : vd0145.logiclOperators[val], itemClass : 1, condType : val};
					vd0145.addRow(obj, 0);
					vd0145.createExpression();
				})
				// 括弧ボタン押下
				.on('click', '#btnOpenParenthesis, #btnCloseParenthesis', function(ev) {
					let val = $(ev.currentTarget).val();
					let obj = {partsId : design.partsId, dispCondType : vd0145.parentheses[val], itemClass : 2, condType : val};
					vd0145.addRow(obj, 1);
					vd0145.createExpression();
				})
				// パーツ、比較条件式、条件、数値化フラグ変更時処理
				.on('change', 'select.condType, select.operator, input[data-field="targetLiteralVal"]', function() {
					vd0145.createExpression();
				})
				// 数値化フラグ変更時処理
				.on('click', 'input[data-field="numericFlag"]', function() {
					$(this).trigger('changeClass');
					vd0145.createExpression();
				})
				// 有効条件項目削除ボタン押下
				.on('click', 'button.btnDelItem', function(ev) {
					// 画面上から削除するのみ
					var $tr = $(this).closest('tr');
					$tr.remove();
					vd0145.createExpression();
				})
				// 登録ボタン押下
				.on('click', '#btnRegist', function(ev) {
					if (!Validator.validate($root.find("input, textarea, select", true))) {
						return false;
					}
					var rows = NCI.toArrayFromTable($root, ['dispCondType']);
					var req = { ecs : rows };
					NCI.post("/vd0145/verify", req).done(function(res) {
						if (res && res.success) {
							var params = {index:index, ecs:rows};
							// コールバック関数の呼び出し
							Popup.close(params);
						}
					});
				})
				// 削除ボタン押下
				.on('click', '#btnDelete', function(ev) {
					var params = {index:index, ecs:null};
					Popup.close(params);
				})
				// 閉じるボタン押下
				.on('click', '#btnClose', function(ev) {
					Popup.close();
				})
			;

			$('#tblEcItems tbody input[data-field="numericFlag"]').trigger('changeClass');
		}
	});

	var vd0145 = {
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
					if (val1 != null && val1 != '' && val2 != '') {
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
	$('#tblCalcEcItems tbody').find('td[data-field=sortOrder]').each(function(i, elem) {
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
	if (parts) {
		let container = null;
		if (parts.parentPartsId == null || parts.parentPartsId < 0) {
			container = ctx.root;
		} else {
			container = ctx.designMap[parts.parentPartsId];
		}
		return partsMap[container.containerCode];
	}
	return null;
}