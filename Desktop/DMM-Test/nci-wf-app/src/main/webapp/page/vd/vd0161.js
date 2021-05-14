let inPreview = false;
const EMPTY_ONLY = '1';
const ALL_CELLS = '2';
let cssEditor, htmlEditor;
let retBgHtml, retCustomCssStyle, containerVersion;

$(function() {
	let root = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	containerVersion = root.containerVersion;
	let params = { messageCds : ['MSG0125']};
	NCI.init('/vd0161/init', params).done(function(res) {
		// CSSスタイル
		let customCssStyle = document.getElementById('customCssStyle');
		customCssStyle.value = root.customCssStyle || '';
		cssEditor = CodeMirror.fromTextArea(customCssStyle, {
				mode: "css",			// required "../../assets/codemirror/mode/css/css.js";
				lineNumbers: true,
				indentUnit: 2,
				tabSize: 2,
				styleActiveLine: true,
				indentWithTab : true,
				matchBrackets: true,
				theme: "eclipse",
				autoCloseBrackets: true,
				extraKeys: {
					"Ctrl-/": "toggleComment", 		// 選択行へのコメント与奪
					"Ctrl-Space": "autocomplete", 	// 入力補完
					"Ctrl-S": update,				// 上書き保存
					"Ctrl-A": "selectAll",			// 全選択
					"Ctrl-Z": "undo",				// 元に戻す
					"Ctrl-Y": "redo"				// やり直し
				}
		});
		cssEditor.on('change', function() {
			previewCss();
			previewHtml();
		});
		// 背景HTML
		let bgHtml = document.getElementById('bgHtml');
		bgHtml.value = root.bgHtml || '';
		htmlEditor = CodeMirror.fromTextArea(bgHtml, {
				mode: "text/html",
				lineNumbers: true,
				indentUnit: 2,
				tabSize: 2,
				styleActiveLine: true,
				indentWithTab : true,
				matchBrackets: true,
				theme: "eclipse",
				autoCloseBrackets: true,
				autoCloseTags: true,
				extraKeys: {
					"Ctrl-/": "toggleComment", 		// 選択行へのコメント与奪
					"Ctrl-Space": "autocomplete", 	// 入力補完
					"Ctrl-S": update,				// 上書き保存
					"Ctrl-A": "selectAll",			// 全選択
					"Ctrl-Z": "undo",				// 元に戻す
					"Ctrl-Y": "redo"				// やり直し
				}
		});
		htmlEditor.on('change', function() {
			previewHtml();
		});

		// プレビュー初期化
		previewCss();
		previewHtml();

		// 更新ボタン押下
		$('#btnOK')
			.click(function(ev) {
				let r = {
						'bgHtml' : $('#bgHtml').val(),
						'customCssStyle' : $('#customCssStyle').val(),
						'containerVersion' : containerVersion
				};
				Popup.close(r);
			}).prop('disabled', false);

		// セル番号を付与
		$('#btnFillCellNo')
			.click(function(ev) {
				fillCellNo();
			});

		// セル番号を除去
		$('#btnResetCellNo')
			.click(function(ev) {
				NCI.confirm(NCI.getMessage('MSG0125'), function() {
					resetCellNo();
				});
			});

		// 空テーブル生成
		$('#btnAddEmptyTable').click(addEmptyTable);

		// ac-contentsをHTMLで指定するとレイアウト崩れするため
		// jsで後から追加する。（app.css対応）
		$('.codemirror-box').addClass('ac-contents');
		$('.ac-contents').css('background-color', 'transparent');
		$('.ac-contents').css('padding-bottom', '0px');
	});

	// 閉じるボタン押下
	$('#btnClose').click(function() {
		if (retCustomCssStyle != null && retBgHtml != null && containerVersion != null) {
			let r = {
					'bgHtml' : retBgHtml,
					'customCssStyle' : retCustomCssStyle,
					'containerVersion' : containerVersion
			};
			Popup.close(r);
		} else {
			Popup.close();
		}
	})


	function previewCss() {
		let val = cssEditor.getValue();
		$('#customCssStyle').val(val);
		$('#customCssStyleTag').html(val);
	}

	function previewHtml() {
		let val = htmlEditor.getValue();
		$('#bgHtml').val(val)
		$('#preview').html(val);
	}

	const TAG = 'aside';
	const CLASS = 'forBgHtml';

	/** テーブルのセルに連番を付与 */
	function fillCellNo() {
		let aside = document.createElement(TAG);
		aside.setAttribute('class', CLASS);

		// テーブル内の最大連番を求める
		let seq = 0, empties = [];
		let $cells = $('#preview table').find('th, td').each(function(i, cell) {
			let $cell = $(cell).addClass('droppable');	// これでドラッグ＆ドロップ対象になる
			let $aside = $cell.find('>' + TAG + '.' + CLASS);	// セル直下のasideだけ。セルの中にテーブルを含むようなケースは除外
			if ($aside.length === 0)
				// 後続処理のために、空セルを集めておく
				empties.push($cell);
			else
				seq = Math.max(seq, +$aside.text());
		});
		// 空セルに連番を付与した<aside>タグを設定
		// これがJava側でパーツHTMLを置換するためのマーカーとなる
		empties.forEach(function($cell, i, array) {
			let newAside = aside.cloneNode(true);
			newAside.textContent = (++seq);
			$cell.append(newAside);
		});
		// エディタへも反映
		htmlEditor.setValue( $('#preview').html() );
	}

	/** テーブルのセルから連番をリセット */
	function resetCellNo() {
		$(TAG + '.' + CLASS).remove();
		$('.droppable').removeClass('droppable');

		fillCellNo();
	}

	/** 空のテーブルを追加 */
	function addEmptyTable() {
		let $targets = $('#tableDialog').find('input.form-control');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		// 列数と行数
		let colCount = $('#colCount').val();
		let rowCount = $('#rowCount').val();

		// 空テーブル生成し、プレビューへ反映
		let html = '<table class="table table-hover table-bordered table-striped table-condensed responsive">\r\n';
		html += '<tbody>\r\n';
		for (let r = 0; r < rowCount; r++) {
			html += '\t<tr>\r\n';
			for (let c = 0; c < colCount; c++) {
				html += '\t\t<td></td>\r\n';
			}
			html += '\t</tr>\r\n';
		}

		// エディタへも反映
		htmlEditor.setValue( $('#preview').append($(html)).html() );
	}


	/** 更新処理（Ctrl+S） */
	function update() {
		// 更新後、変数に対して値を設定。戻るボタンクリック時にその値を親画面へ返す。
		let entity = {
				'containerId' : root.containerId,
				'bgHtml' : $('#bgHtml').val(),
				'customCssStyle' : $('#customCssStyle').val(),
				'version' : containerVersion
		}

		let params = {
				'entity' : entity
		};
		NCI.post('/vd0161/save', params).done(function(res) {
			if (res && res.success) {
				// データを反映
				retBgHtml = $('#bgHtml').val();
				retCustomCssStyle = $('#customCssStyle').val();
				// 更新後、親画面との整合性を合わせるためにバージョンを上げる
				containerVersion = res.entity.version;

				// 一定時間経過したらメッセージをクリア
				setTimeout(function() {
					NCI.clearMessage('success');
				}, 5000);
			}
		})
	}
});
