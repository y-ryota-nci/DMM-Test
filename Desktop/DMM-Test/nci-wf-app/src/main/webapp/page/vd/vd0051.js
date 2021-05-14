$(function() {

	let javascriptHistoryId = NCI.getQueryString('javascriptHistoryId');
	let version = NCI.getQueryString('version');
	let params = {
			'javascriptHistoryId' : javascriptHistoryId,
			'version' : version
	};
	NCI.init('/vd0051/init', params).done(function(res) {
		if (res && res.success) {
			// データを反映
			NCI.toElementsFromObj(res.entity);
			// 登録時
			if (!res.entity.version) {
				$('#btnRegister').prop('disabled', false);
				$('#btnUpdate').css('display', 'none');
			// 更新時
			} else {
				$('#btnRegister').hide();
				$('#btnUpdate').prop('disabled', false);
			}

			$('#fileName').prop('disabled', res.entity.javascriptHistoryId != null)

			var script = document.getElementById('script');
			var jsEditor = CodeMirror.fromTextArea(script, {
				mode: "javascript",		// required "../../assets/codemirror/mode/javascript/javascript.js"
				lineNumbers: true,
				indentUnit: 2,
				tabSize: 2,
				indentWithTabs : true,
				styleActiveLine: true,
				matchBrackets: true,
				theme: "eclipse",
				continueComments: "Enter",
				autoCloseBrackets: true,
				extraKeys: {
					"Ctrl-/": "toggleComment", 		// 選択行へのコメント与奪
					"Ctrl-Space": "autocomplete", 	// 入力補完
					"Ctrl-S": save,					// 上書き保存
					"Ctrl-A": "selectAll",			// 全選択
					"Ctrl-Z": "undo",				// 元に戻す
					"Ctrl-Y": "redo"				// やり直し
				},
				highlightSelectionMatches: {showToken: /\w/, annotateScrollbar: true}	// 選択文字のハイライト、CSSの定義を忘れずにすること
			});

			// 登録ボタン押下
			$('#btnRegister')
				.prop('disabled', false)
				.data('jsEditor', jsEditor)
				.click(registWithConfirm);
			// 更新ボタン押下
			$('#btnUpdate')
				.prop('disabled', false)
				.data('jsEditor', jsEditor)
				.click(updateWithConfirm);
		}
	});

	$('#btnBack').click(function(ev) {
		NCI.redirect('./vd0050.html');
	});

	/** 確認メッセージありで登録処理 */
	function registWithConfirm() {
		// 確認メッセージ
		let msg = NCI.getMessage('MSG0069', 'javascript');
		NCI.confirm(msg, function() {
			save(false);
		});
	}

	/** 確認メッセージありで更新処理 */
	function updateWithConfirm() {
		// 確認メッセージ
		let msg = NCI.getMessage('MSG0071', 'javascript');
		NCI.confirm(msg, function() {
			save(false);
		});
	}

	/** 保存処理 */
	function save(autoClear) {
		// Javascript内容をTextareaへ反映
		let jsEditor = $('#btnUpdate').data('jsEditor');
		jsEditor.save();

		// バリデーション
		let $root = $('#inputed');
		let $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		// 更新処理
		let params = {
				'entity' : NCI.toObjFromElements($root)
		};
		NCI.post('/vd0051/save', params).done(function(res) {
			if (res && res.success) {
				// データを反映
				NCI.toElementsFromObj(res.entity);

				// 一定時間経過したらメッセージをクリア
				if (autoClear) {
					setTimeout(function() {
						NCI.clearMessage('success');
					}, 5000);
				}

				$('#btnRegister').hide();
				$('#btnUpdate').css('display', '');
			}
		})
	}
});
