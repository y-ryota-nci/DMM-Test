function _onload() {
	var editor = document.getElementById('editor');
	var jsEditor = CodeMirror.fromTextArea(editor, {
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
}
