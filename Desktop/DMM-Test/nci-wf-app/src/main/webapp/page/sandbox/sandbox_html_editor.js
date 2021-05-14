function _onload() {
	var editor = document.getElementById('editor');
	var jsEditor = CodeMirror.fromTextArea(editor, {
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
			extraKeys: {"Ctrl-Space": "autocomplete"},

	});
}
