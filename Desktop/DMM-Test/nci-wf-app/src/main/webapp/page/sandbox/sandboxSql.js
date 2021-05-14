$(function() {
	const editor = document.getElementById('editor');
	const jsEditor = CodeMirror.fromTextArea(editor, {
	    mode: "text/x-plsql",	// "text/x-plsql" for Oracle, "text/x-mssql" for SQLServer, "text/x-mysql" for MySQL, "text/x-sql" for ANSI SQL
		lineNumbers: true,
		indentUnit: 2,
		tabSize: 2,
		styleActiveLine: true,
		indentWithTab : true,
		matchBrackets: true,
		theme: "eclipse",
	    autofocus: true,
		continueComments: "Enter",
		autoCloseBrackets: true,
	    extraKeys: {"Ctrl-Space": "autocomplete"},
	    hintOptions: {tables: {
	      users: {name: null, score: null, birthDate: null},
	      countries: {name: null, population: null, size: null}
	    }}
	});

	NCI.init('/sandboxSql/init').done(function(res) {
		if (res && res.success) {
			init(res);
		}
	});

	// イベント定義
	$(document)
		.on('click', 'tbody>tr', selectTR)
		.on('click', '#btnExecSql', execute)
		.on('click', '#btnDownloadCsv', downloadCsv);

	function init(res) {
	}

	/** 検索結果行の選択処理 */
	function selectTR(ev) {
		$('tr.ui-selected').removeClass('ui-selected');
		$(ev.currentTarget).addClass('ui-selected');
	}

	/** SQLを実行し、結果をグリッドへ表示 */
	function execute(ev) {
		NCI.clearMessage();

		// Javascript内容をTextareaへ反映
		jsEditor.save();

		// バリデーション
		const $targets = $('#maxCount, #editor');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const params = {
				"sql" : $('#editor').val(),
				"maxCount" : $('#maxCount').val()
		};
		NCI.post('/sandboxSql/executeSql', params).done(function(res) {
			if (res && res.success) {
				// ヘッダ行とテンプレート行を生成
				const $tblResult = $('#tblResult');
				const $trHead = $tblResult.find('thead>tr').empty();
				const $trFoot = $tblResult.find('tfoot>tr').empty();
				if (res.results && res.results.length) {
					let cols = 0;
					for (let name in res.results[0]) {
						cols++;
						$(document.createElement('th'))
							.text(name.toUpperCase())
							.appendTo($trHead);

						$(document.createElement('td'))
							.attr('data-field', name)
							.appendTo($trFoot);
					}
					$tblResult.width(cols * 120);
				}

				// テンプレートをもとに結果行の反映
				new ResponsiveTable($('#divResult')).fillTable(res.results);
			}
		});
	}

	/** CSVダウンロード */
	function downloadCsv(ev) {
		NCI.clearMessage();

		// Javascript内容をTextareaへ反映
		jsEditor.save();

		// バリデーション
		const $targets = $('#maxCount, #editor');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		const params = { "sql" : $('#editor').val() };
		NCI.download('/sandboxSql/downloadCsv', params);
	}
});
