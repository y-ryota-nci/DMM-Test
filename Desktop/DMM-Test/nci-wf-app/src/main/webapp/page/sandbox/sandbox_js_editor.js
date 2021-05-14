function _onload() {
	var editor = document.getElementById('editor');
	var jsEditor = CodeMirror.fromTextArea(editor, {
		mode: "javascript",		// required "../../assets/codemirror/mode/javascript/javascript.js"
		lineNumbers: true,
		indentUnit: 2,
		tabSize: 2,
		styleActiveLine: true,
		indentWithTab : true,
		matchBrackets: true,
		theme: "eclipse",
		continueComments: "Enter",
		autoCloseBrackets: true,
		extraKeys: {"Ctrl-/": "toggleComment", "Ctrl-Space": "autocomplete"},
		highlightSelectionMatches: {showToken: /\w/, annotateScrollbar: true}	// 選択文字のハイライト、CSSの定義を忘れずにすること
	});
}
