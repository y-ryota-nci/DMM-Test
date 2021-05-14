const MODE_ADD = "1";
const MODE_EDIT = "2";
const MODE_MOVE = "3";
const MODE_COPY = "4";
const MODE_REMOVE = "5";
var nextSeq = 0;
var _mode = MODE_EDIT;

$(function() {

	var params = {
			"containerId" : NCI.getQueryString("containerId")
			, messageCds : ['MSG0072', 'partsCode']
			, "version" : NCI.getQueryString("version")
	};
	NCI.init("/vd0110/init", params).done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			init(res);
		}
	});

	// モードの切り替え
	$("input[type=radio][name=mode]")
		.change(function(ev) {
			_mode = this.value;
			$("#addPartsIcon").hide();
			// パーツの移動
			var draggable = (_mode === MODE_MOVE ? "enable" : "disable");
			$("article.parts-editable").draggable(draggable);

			// カーソルを切り替えるために、編集領域へCSSクラス付与(@see vd0110.css)
			$('#editArea')
				.toggleClass('mode-edit', _mode === MODE_EDIT)
				.toggleClass('mode-move', _mode === MODE_MOVE)
				.toggleClass('mode-add', _mode === MODE_ADD)
				.toggleClass('mode-copy', _mode === MODE_COPY)
				.toggleClass('mode-remove', _mode === MODE_REMOVE);
		});

	// パーツ配置ボタン
	$("button[data-parts-type]")
		.click(function(ev) {
			let $elem = $(this);
			let iconClass = $elem.find('i').attr('class') + ' small';
			$("#addPartsIcon")
				.hide()
				.data("parts-type", $elem.data("parts-type"))
				.removeClass()
				.addClass(iconClass);
		});




	// 動的に配置されるパーツへのイベント定義
	$(document)
		// パーツのクリック時
		.on('click', 'article.parts-editable', function(ev) {
			// パーツは必ずラベル＋本体をセットで扱う
			var $elem = $(this);
			if (_mode === MODE_EDIT) {
				// パーツプロパティ画面を開く
				editParts($elem);
			}
			else if (_mode === MODE_COPY) {
				// パーツのコピー
				copyParts($elem);
			}
			else if (_mode === MODE_REMOVE) {
				// パーツの削除
				deleteParts($elem);
			}
			else if (_mode === MODE_ADD) {
				const partsType = $("#addPartsIcon").data("parts-type");
				const clickedPartsId = $elem.data('partsId');
				if (partsType) {
					addParts(partsType, clickedPartsId, null);
				}
			}
			return false;
		})
		// テーブルのセル（TD / TH）のクリック時
		.on('click', 'td.droppable, th.droppable', function(ev) {
			if (_mode === MODE_ADD) {
				const partsType = $("#addPartsIcon").data("parts-type");
				if (partsType) {
					const bgHtmlCellNo = $(this).find('aside.forBgHtml').text();
					addParts(partsType, null, bgHtmlCellNo);
					return false;
				}
			}
		})
		// 編集エリアでのクリック時
		.on('click', '#editArea', function(ev) {
			if (_mode === MODE_ADD) {
				// パーツ追加時
				const partsType = $("#addPartsIcon").data("parts-type");
				if (partsType) {
					addParts(partsType, null, null);
					return false;
				}
			}
		})
		;

	// 編集エリアでのイベント定義
	$("#editArea")
		.hover(function(ev) {
			// パーツアイコン表示への開始と終了
			var $addPartsIcon = $("#addPartsIcon");
			if (_mode === MODE_ADD) {
				if (ev.type === "mouseenter")
					$addPartsIcon.show();
				else if (ev.type === "mouseleave")
					$addPartsIcon.hide();
			}
		}).mousemove(function(ev) {
			if (_mode === MODE_ADD) {
				// パーツ配置時はマウス移動に追随してパーツアイコンを表示
				$("#addPartsIcon").css({
					left : (ev.pageX + 10)+ "px",
					top : (ev.pageY - 16) + "px"
				});
			}
		});

	// 戻るボタン押下
	$('#btnBack')
		.click(function(ev) {
			NCI.redirect('../vd/vd0010.html');
		});

	// 更新ボタン押下
	$('#btnUpdate')
		.click(function(ev) {
			var params = { 'ctx' : $('#editArea').data('ctx') };
			NCI.post('/vd0110/save', params).done(function(res) {
				if (res && res.success)
					$('#editArea').data('ctx', res.ctx);
			});
		});

	// プレビューボタン押下
	$('#btnPreview')
		.click(function(ev) {
			// プレビュー方法を変更したら VD0110/VD0030も同時に修正すること
			let params = {
					'ctx' : $('#editArea').data('ctx'),
					'dcId' : $('#dcId').val(),
					'trayType' : $('#trayType').val()
			};
			let id = "vd0115";
			NCI.flushScope(id, params);
			window.open('./vd0115.html?' + FLUSH_SCOPE_KEY + '=' + id, 'preview');
		});

	// コンテナ設定ボタン押下
	$('#btnContainerSettings')
		.click(function(ev) {
			Popup.open('./vd0130.html', fromVd0130, $('#editArea').data('ctx'));
		});

	// パーツツリーボタン押下
	$('#btnPartsTree')
		.click(function(ev) {
			Popup.open('./vd0160.html', fromVd0160, $('#editArea').data('ctx'));
		});

	// 背景HTMLボタン押下
	$('#btnBgHtml')
		.click(function(ev) {
			Popup.open('./vd0161.html', fromVd0161, $('#editArea').data('ctx').root);
		});

	// モバイル設定ボタン押下
	$('#btnMobileSettings')
		.click(function(ev) {
			Popup.open('./vd0162.html', fromVd0162, $('#editArea').data('ctx'));
		});

	// 外部Javascript
	$('#btnOutsideJavascript')
		.click(function(ev) {
			Popup.open('./vd0123.html', fromVd0123, $('#editArea').data('ctx'));
		});
});

/** 初期化 */
function init(res) {
	// パーツ移動用に、ソート機能を付与
	var $editArea = $("#editArea").data("ctx", res.ctx);

	$('#containerCode').text(res.ctx.root.containerCode);
	$('#containerName').text(res.ctx.root.containerName);
	NCI.createOptionTags($('#trayType'), res.trayTypes).val('NEW');

	// 表示条件の選択肢
	let $dcId = $('#dcId');
	NCI.createOptionTags($dcId, res.dcList);
	// 表示条件の選択肢の初期値
	let dcLength = $dcId[0].options.length;
	if (dcLength <= 1)
		$dcId[0].selectedIndex = dcLength - 1;
	else
		$dcId[0].selectedIndex = 1;

	// パーツHTMLをレンダリング
	renderHtml(res);

	$('#btnUpdate, #btnPreview, #btnContainerSettings').prop('disabled', false);

	// 編集モードへ切り替え
	$('#modeEdit').click();
}

// サーバからのレスポンスをもとにパーツHTMLをレンダリング
function renderHtml(res) {
	var $editArea = $('#editArea').html(res.html);
	var ctx = $editArea.data('ctx');

	if (res.designMap != null)
		ctx.designMap = res.designMap;
	if (res.childPartsIds != null)
		ctx.root.childPartsIds = res.childPartsIds;

	// コンテナのカスタムCSSスタイル
	$('#customCssStyleTag').html(res.customCssStyleTag || '');

	// ドラッグイベント定義
	$('article.parts-editable').not('.ui-draggable').draggable({
		appendTo : $editArea,
		cancel: "option",					// ドラッグ対象外とするエレメント
		containment : "#editArea",			// ドラッグ可能領域
		disabled: (_mode != MODE_MOVE),		// 一時的に無効化
		cursor : 'move',
		helper: "clone",
		opacity: 0.5,
		distance : '3',
		tolerance : "pointer",
		revert : "invalid"
	});
	// パーツのコピー（ドラッグ＆ドロップのドロップ定義）
	$('article.parts-editable, .droppable').not('.ui-droppable').droppable({
		accept : "article.parts-editable",
		tolerance : "pointer",
		drop : onDropEnd
	});
}

/** パーツの追加 */
function addParts(partsType, clickedPartsId, bgHtmlCellNo) {
	const ctx = $("#editArea").data("ctx");
	const params = {
			"ctx" : ctx,
			"partsType" : partsType,
			"partsId" : clickedPartsId,
			"bgHtmlCellNo" : bgHtmlCellNo
	};
	NCI.post("/vd0110/addParts", params).done(function(res) {
		if (res && res.success) {
			renderHtml(res);
			ctx.root.partsCodeSeq = res.partsCodeSeq;
		}
	});
}

/** パーツのコピー */
function copyParts($elem) {
	var ctx = $("#editArea").data("ctx");
	ctx.root.partsCodeSeq++;
	var params = { "ctx" : ctx, "partsId" : $elem.data("partsId") };
	NCI.post("/vd0110/copyParts", params).done(function(res) {
		if (res && res.success) {
			renderHtml(res);
		}
	});
}

/** パーツの削除 */
function deleteParts($elem) {
	const ctx = $("#editArea").data("ctx");
	const partsId = $elem.data("partsId");
	const d = ctx.designMap[partsId];
	const msg = NCI.getMessage('MSG0072', (NCI.getMessage('partsCode') + '=' + d.partsCode));
	NCI.confirm(msg, function() {
		const params = { "ctx" : $("#editArea").data("ctx"), "partsId" : $elem.data("partsId") };
		NCI.post('/vd0110/deleteParts', params).done(function(res) {
			if (res && res.success) {
				renderHtml(res);
			}
		});
	});
}

/** パーツプロパティ画面を開く */
function editParts($elem) {
	var params = {
		'ctx' : $("#editArea").data("ctx"),
		'partsId' : $elem.data("partsId")
	}
	$elem.addClass("selected-parts");
	Popup.open("../vd/vd0114.html", fromVd0114, params);
}

/** 表示されているHTMLの順番でパーツ定義の並び順を再設定 */
function resetSortOrder() {
	let $editArea = $('#editArea'), ctx = $editArea.data('ctx'), designMap = ctx.designMap;
	// パーツ定義の並び順を設定(ドラッグ中のパーツはドラッグイメージ用のコピーが作成されているので、それを除外する)
	let elements = document.querySelectorAll('article.parts-editable:not(.ui-draggable-dragging)');
	for (let i = 0; i < elements.length; i++) {
		let partsId = $(elements[i]).data('partsId');
		if (partsId) {
			designMap[partsId].sortOrder = (i + 1);
		}
	}
	// 並び順に従ってコンテナの子要素リストをソート
	ctx.root.childPartsIds.sort(function(id1, id2) {
		let d1 = designMap[id1], d2 = designMap[id2];
		return d1.sortOrder === d2.sortOrder ? 0
				: d1.sortOrder > d2.sortOrder ? 1
				: -1;
	});
}

function slideSortOrder(targetPartsId) {

	// 指定パーツの並び順を取得
	let baseSortOrder = -1;
	for (let partsId in designMap) {
		if (partsId === targetPartsId)
			baseSortOrder = designMap[partsId].sortOrder;
	}
	// 指定パーツの並び順以降のパーツの並び順を１つずつずらす
	for (let partsId in designMap) {
		let d = designMap[partsId];
		if (d.sortOrder > baseSortOrder)
			d.sortOrder++;
	}
}

/** 再描画 */
function refreshScreen() {
	let params = { "ctx" : $('#editArea').data('ctx') };
	NCI.post("/vd0110/refresh", params).done(function(res) {
		if (res && res.success) {
			renderHtml(res);
		}
	});
}

/** パーツプロパティ画面から戻るときのコールバック */
function fromVd0114(design) {
	if (design) {
		var ctx = $('#editArea').data('ctx');
		var params = {
				"ctx" : ctx,
				"design" : design
		};
		NCI.post("/vd0110/editParts", params).done(function(res) {
			if (res && res.success) {
				renderHtml(res);
			}
		});
	}
}

/** 外部Javascriptから戻るときのコールバック */
function fromVd0123(javascripts) {
	if (javascripts) {
		let ctx = $('#editArea').data('ctx');
		ctx.root.javascripts = javascripts;
		// 再描画
		refreshScreen();
	}
}

/** コンテナ設定画面から戻るときのコールバック */
function fromVd0130(ctx) {
	if (ctx) {
		$('#editArea').data('ctx', ctx);
		$('#containerCode').text(ctx.root.containerCode);
		$('#containerName').text(ctx.root.containerName);

		// 再描画
		refreshScreen();
	}
}

/** パーツツリー画面から戻るときのコールバック */
function fromVd0160(partsId) {
	if (partsId) {
		// 既存のパーツツリー画面(VD0160)を閉じ、返す刀でパーツプロパティ画面(VD0114)を開く
		Popup.close();
		$('#modeEdit').click();	// 編集モードに切り替え
		let $elem = $('.parts[data-parts-id=' + partsId + ']');
		editParts($elem);
	}
}

/** 背景HTML設定画面から戻るときのコールバック */
function fromVd0161(r) {
	if (r) {
		let ctx = $('#editArea').data('ctx');
		ctx.root.bgHtml = r.bgHtml;
		ctx.root.customCssStyle = r.customCssStyle;
		ctx.root.containerVersion = r.containerVersion;

		// 再描画
		let params = { "ctx" : ctx };
		NCI.post("/vd0110/editBgHtml", params).done(function(res) {
			if (res && res.success) {
				renderHtml(res);
			}
		});
	}
}

/** モバイル設定画面から戻るときのコールバック */
function fromVd0162(rows) {
	if (rows) {
		const INVISIBLE = 0;
		let ctx = $('#editArea').data('ctx');
		let designMap = ctx.designMap;
		rows.forEach(function(row, i, array) {
			let design = designMap[row.partsId];
			design.mobileInvisibleFlag = row.mobileInvisibleFlag;
		});
		// 再描画
		refreshScreen();
	}
}

/** ドロップ完了イベント */
function onDropEnd(ev, ui) {
	let $dragged = $(ui.draggable[0]), $this = $(this);
	let $dropped = null, $cell = null;
	if ($this.is('article.parts-editable'))
		// ドロップ先がパーツである
		$dropped = $this;
	else if ($this.is('th.droppable, td.droppable')) {
		// ドロップ先がTD/THである
		$cell = $this;
		// 背景HTMLセル連番を更新
		let partsId = $dragged.data('partsId');
		let design = $('#editArea').data('ctx').designMap[partsId];
		let $cellNo = $cell.children('aside.forBgHtml').first();
		design.bgHtmlCellNo = ($cellNo.length === 0 ? '' : +$cellNo.text());
	}
	else {
		// ドロップ先は編集領域(editArea)であったので、ドロップ地点以降の最初のパーツをドロップ先とみなす
		$('article.parts-editable').each(function(i, elem) {
			let $parts = $(elem);
			if ($parts.offset().top >= ui.offset.top) {
				$dropped = $parts;
				return false;
			}
		});
	}

	// パーツ移動
	moveParts($dragged, $dropped, $cell);
	return true;	// イベントバブルによってなんども発生することがあるが、最後のイベントを正とする
}

/** パーツ移動 */
function moveParts($dragged, $dropped, $cell) {
	if ($dropped && $dropped.length)
		// ドロップ先パーツの後ろに移動
		$dropped.after($dragged);
	else if ($cell && $cell.length)
		// ドロップ先TH/TDの子要素として末尾に移動
		$dragged.appendTo($cell);
	else
		// 入れ替え先が見つからないので、とにかく末尾に追加
		$dragged.appendTo($('#editArea'));

	// パーツの並び順を再設定
	resetSortOrder();
}