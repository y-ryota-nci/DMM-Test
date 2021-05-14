function _onload() {
	var editor = document.getElementById('editor');
	var jsEditor = CodeMirror.fromTextArea(editor, {
			mode: "css",			// required "../../assets/codemirror/mode/css/css.js";
			lineNumbers: true,
			indentUnit: 2,
			tabSize: 2,
			styleActiveLine: true,
			indentWithTab : true,
			matchBrackets: true,
			theme: "eclipse",
			autoCloseBrackets: true,
			extraKeys: {"Ctrl-Space": "autocomplete"}
	});
}
