// パーツのデザイン定義をFlushScopeから取得
let paramters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
let ctx = paramters.ctx;
let design = ctx.designMap[paramters.partsId];

$(function() {
	// partsTypeをキーに、パーツ拡張情報部分のプロパティ用HTMLを読み込み
	$('#specificItem').load("./vd0114_" + design.partsType + ".html", function() {
		// パーツ拡張情報部分のプロパティの初期化を先に実行
		if (window.initSpecific) {
			window.console.info("load initSpecific() --> " + design.partsType);
			initSpecific(ctx, design);
		}
		let params = { "messageCds" : ['MSG0121', 'MSG0140', 'MSG0208', 'MSG0209', 'MSG0211', 'MSG0212', 'noPaging', 'picture', 'MSG0241', 'MSG0242', 'MSG0243'] };
		NCI.init("vd0114/init", params).done(function(res) {
			if (res && res.success) {
				// 選択肢
				NCI.createOptionTags($("select.columnSize"), res.columnSizes);
				NCI.createOptionTags($("select.fontSize"), res.fontSizes);
				NCI.createOptionTags($("#renderingMethod"), res.renderingMethods);
				NCI.createOptionTags($("#businessInfoCode"), res.businessInfoCodes);
				NCI.createOptionTags($("#docBusinessInfoCode"), res.docBusinessInfoCodes);

				const NUMBER = '4';
				$('#btnOK').prop('disabled', false);
				$('#btnEC').prop('disabled', false);	// ラベルを含めたどんなパーツでもパーツ条件を設定できる
				$('#btnCalc').prop('disabled', !(design.partsType == PartsType.TEXTBOX && design.inputType == NUMBER));
				$('#btnEvent').prop('disabled', (design.events.length === 0));

				// パーツ定義を画面へ反映
				NCI.toElementsFromObj(design, $('#commonItem'));

				// カラーピッカー
				$('input.color-picker').each(function(i, elem) {
					$(elem).minicolors({
						swatches : [
							'#000000', '#696969', '#808080', '#a9a9a9', '#d3d3d3', '#f5f5f5', '#ffffff', /* black ～ white */
							'#0000ff' /* blue */, '#008000' /* green */, '#ffff00' /* yellow */, '#00bfff' /* deepskyblue */,
							'#ff0000' /* red */,	'#ffa500' /* orange */, '#8a2be2' /* blueviolet */,
						],
						format : 'hex',
						theme : 'bootstrap'
					});
				});

				$(document)
				// 更新ボタン押下
				.on('click', '#btnOK', function(ev) {
					// 共通バリデーション
					let $root = $('#commonItem, #specificItem');
					if (!Validator.validate($root.find("input, textarea, select", true))) {
						return false;
					}
					if (window.validateSpecific) {
						// パーツ特有のバリデーション（共通処理に追加）
						if (!window.validateSpecific(ctx, design)) {
							return false;
						}
					}

					// 入力内容を吸い上げ
					let inputed = null;
					if (window.toObjOverride) {
						// パーツ特有の吸い上げ処理（共通処理を置換）
						inputed = window.toObjOverride(ctx, design);
					} else {
						// パーツ固有の吸い上げ処理がない場合は共通処理を実施
						inputed = NCI.toObjFromElements($root);
					}

					// 吸い上げたデータで画面ロード時点の内容を上書きしたものを生成
					let edited = $.extend({}, design, inputed);

					// コールバック関数の呼び出し
					Popup.close(edited);
				})
				// 閉じるボタン押下
				.on('click', '#btnClose', function(ev) {
					Popup.close();
				})
				// 有効条件設定ボタン押下
				.on('click', '#btnEC', function(ev) {
					let params = { 'ctx' : ctx, 'partsId' : design.partsId, 'type' : 'parts' };
					Popup.open("../vd/vd0111.html", fromVd0111, params);
				})
				// 計算式ボタン押下
				.on('click', '#btnCalc', function(ev) {
					let params = { 'ctx' : ctx, 'partsId' : design.partsId, 'type' : 'parts' };
					Popup.open("../vd/vd0143.html", fromVd0143, params);
				})
				// パーツコードの変更ボタン
				.on('click', '#btnChangePartsCode', function() {
					const params = {
							'ctx' : ctx,
							'oldPartsCode' : $('#partsCode').val(),
							'newPartsCode' : $('#partsCode').val()
					}
					Popup.open("../vd/vd0180.html", fromVd0180, params);
				})
				// イベントボタン押下
				.on('click', '#btnEvent', function(ev) {
					const params = { 'ctx' : ctx, 'parts' : ctx.designMap[design.partsId] }
					Popup.open("../vd/vd0125.html", fromVd0125, params);
				})
				;
//				// レンダリング方法の変更 ==> インラインでも幅を指定したいことがあるので、これは廃止
//				$('#renderingMethod').on('change', function(ev) {
//					// 幅ドロップダウンの有効無効制御
//					let isBootstrap = ('0' == $('#renderingMethod').val());
//					toggleDisplay($('#colXs'), isBootstrap, false);
//					toggleDisplay($('#colSm'), isBootstrap, false);
//					toggleDisplay($('#colMd'), isBootstrap, false);
//					toggleDisplay($('#colLg'), isBootstrap, false);
//				}).change();

				// コピー起案の対象に出来ないパーツでは「コピー起案の対象」を隠す。
				let copiables = [PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO, PartsType.DROPDOWN, PartsType.USER, PartsType.ORGANIZATION, PartsType.MASTER ];
				if (copiables.indexOf(design.partsType) < 0) {
					toggleDisplay($('#copyTargetFlag'), false, false);
				}
				// 業務管理項目を選択できないパーツでは「業務管理項目」を隠す
				// 上と同じく文書管理項目を選択できないパーツでは「文書管理項目」を隠す
				const bizInfos = [ PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO, PartsType.DROPDOWN, PartsType.NUMBERING, PartsType.MASTER ];
				if (bizInfos.indexOf(design.partsType) < 0) {
					toggleDisplay($('#businessInfoCode'), false, false);
					toggleDisplay($('#docBusinessInfoCode'), false, false);
				}
			}
		});
	});
});


/**
 * 項目の表示非表示の切り替え
 * @param $target 対象項目のjqueryオブジェクト
 * @param visible 表示するならtrue
 * @param required 必須項目にするならtrue
 * @returns
 */
function toggleDisplay($target, visible, required) {
	let $parent = $target.closest('div.form-group');
	let $elements = $parent.find('input, select, textarea, span, label');
	visible = !!visible;
	required = !!required;

	$parent.toggle(visible);
	$elements.toggleClass('required', visible && required);

	if (!visible && $target.length) {
		// 値のクリア
		let elem = $target[0];
		let type = elem.type, tagName = elem.tagName;
		if (/radio|checkbox/.test(type))
			elem.checked = false;
		else if (/INPUT|TEXTAREA/.test(tagName))
			elem.value = '';
		else if (/SELECT/.test(tagName))
			if (elem.options && elem.options.length)
				elem.selectedIndex = 0;
			else
				elem.selectedIndex = -1;
	}
}

/** 条件設定画面から戻るときのコールバック */
function fromVd0111(result) {
	if (result) {
		design.partsConds = result.conds;
	}
}

/** 計算式定義一覧から戻るときのコールバック */
function fromVd0143(result) {
	if (result) {
		design.partsCalcs = result.calcs;
	}
}

/** パーツコード変更画面から戻るときのコールバック */
function fromVd0180(newPartsCode) {
	if (newPartsCode) {
		// 編集できるのは現在のコンテナ上のパーツに限られているので、その制限内ではパーツコードとデザインコードは常に同じとなる
		design.partsCode = newPartsCode;
		design.designCode = newPartsCode;
		$('#partsCode, #designCode').val(newPartsCode);
	}
}

/** パーツイベント設定画面から戻るときのコールバック */
function fromVd0125(newEvents) {
	if (newEvents) {
		design.events = newEvents;
	}
}