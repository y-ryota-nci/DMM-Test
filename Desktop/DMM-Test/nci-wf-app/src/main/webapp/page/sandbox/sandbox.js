$(function() {
	let params = { messageCds : ['btnClose'] };	// バリデーションで必須のメッセージ群は共通関数内へ取り込んだ

	NCI.init("/sandbox/init", params).done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));
			// カレンダー（年月）
			NCI.ymPicker($('input.ymPicker'));

			$('#mailEnv').val(res.mailEnv);
			$('#smtpHost').val(res.smtpHost);
			$('#dummySendTo').text(res.dummySendTo);
			$('#mailSendTo')
				.val(res.dummySendTo)
				.prop('disabled', res.mailEnv != 'PRODUCT');

			// ユーザ検索Suggestion
			Suggestion.setup({
				url : '/suggestion/user',
				title : 'ユーザ入力',
				items : [
					// 暗黙の検索条件（固定値）
					  { type : 'fixedValue', value : NCI.loginInfo.corporationCode, property : 'corporationCode'}
					// Suggestionのトリガーエレメント
					, { type : 'trigger', selector : 'input.suggest-userAddedInfo', property : 'userAddedInfo', width : 250, label : 'ログインID'}
					// 検索条件＆検索結果欄への表示＋値の反映
					, { type : 'input', selector : 'span.suggest-userName', property : 'userName', width : 350, label : '氏名'}
					// 検索結果欄への表示＋値の反映
					, { type : 'output', selector : 'span.suggest-mailAddr', property : 'mailAddress', width : 150, label : 'メールアドレス'}
					// 値の反映のみ
					, { type : 'fillOnly', selector : 'span.suggest-telNum', property : 'telNum', width : 100, label : '電話番号'}
					// 検索結果欄への表示のみ
					, { type : 'displayOnly', selector : 'span.suggest-telNumCel', property : 'telNumCel', width : 100, label : '携帯番号'}
				]
			});
		}
	});

	// 画面の幅確認用
	$(window).on("resize", function() {
		let w = $(window).innerWidth();
		$('#display')
			.toggleClass('large', 1200 <= w)
			.toggleClass('medium', 992 <= w && w < 1200)
			.toggleClass('small', 768 <= w && w < 992)
			.toggleClass('x-small', w < 768);
		$('#window-width').text(w + ' px');
		$('#container-width').text($('.container:first').innerWidth() + ' px');
		$('#column-width').text($('.col-xs-1:first').innerWidth() + ' px');
	})
	.resize();

	$(document)
	.on('click', 'button.validate', function(e){
		// バリデーション
		let $targets = $('input,textarea,select');
		let $button = $(e.currentTarget);
		let isRequired = $button.hasClass("required");

		let isValid = Validator.validate($targets, isRequired);
		if(!isValid) {
//			NCI.alert('Length : NG');
		}
	})
	.on('click','[name=changeCheck]', function(e){
		// エレメントの状態の切り替え
		let value = $(this).val();
		let $targets = $('input,textarea,select').not('[name=changeCheck]');
		switch (value) {
		case '1':
			$targets.prop('disabled', false).prop('readonly', false);
			break;
		case '2':
			$targets.prop('disabled', true).prop('readonly', false);
			break;
		case '3':
			$targets.prop('disabled', false).prop('readonly', true);
			break;
		}

		let $requiredTargets = $('.validateRequired .validateMulti').find('input,textarea,select');
	})
	.on('click', 'button.btnInitVal', function() {
		// 初期値付与
		$(this).closest('fieldset').find('input,textarea,select').not('[name=changeCheck]').not('[type=file]').each(function(i, elem) {
			let type = elem.type;
			if (type === 'radio' || type === 'checkbox')
				elem.checked = true;
			else if (type !== 'file')
				elem.value = '1';
		});
	})
	.on('click', 'button.btnClearVal', function() {
		// 全クリア
		$(this).closest('fieldset').find('input,textarea,select').not('[name=changeCheck]').not('[type=file]').each(function(i, elem) {
			let type = elem.type;
			if (type === 'radio' || type === 'checkbox')
				elem.checked = false;
			else if (type !== 'file')
				elem.value = '';
		});
		Validator.hideBalloon();
	})
	.on('click', 'button.btnAllErrorCrear' ,function(e){
		// エラーバルーンを全削除
		Validator.hideBalloon();
	})
	.on('click', '#btnDownloadAccessLog', function() {
		// CSVダウンロード
		NCI.download('sandbox/downloadAccessLog');
	})
	.on('click', '#btnDownloadPDF', function() {
		// PDFダウンロード
		NCI.download('sandbox/downloadPDF');
	})
	.on('click', '#btnDownloadExcel2', function() {
		// EXCELテンプレートにデータ差込処理してダウンロード(セル結合あり)
		NCI.download('sandbox/downloadExcel2');
	})
	.on('click', '#btnDownloadExcelForm', function() {
		// EXCELフォームをダウンロード
		NCI.download('sandbox/downloadExcelForm');
	})
	.on('click', '#btnUploadExcelForm', function() {
		// EXCELフォームをダウンロード
		NCI.download('sandbox/uploadExcelForm');
	})
	.on('click', '#btnSelectCorp', function() {
		// 企業選択
		const params = null;
		Popup.open("../cm/cm0010.html?initSearch=true", callbackFromPopup, params, this);
	})
	.on('click', '#btnSelectOrg', function() {
		// 組織選択（企業を選択済みならその企業の）
		const params = null, corporationCode = $('#corporationCode').val();
		let url = "../cm/cm0020.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode;
		Popup.open(url, callbackFromPopup, params, this);
	})
	.on('click', '#btnSelectPost', function() {
		// 役職選択（企業を選択済みならその企業の）
		const params = null, corporationCode = $('#corporationCode').val();
		let url = "../cm/cm0030.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode;
		Popup.open(url, callbackFromPopup, params, this);
	})
	.on('click', '#btnSelectUser', function() {
		// ユーザ選択
		const params = null, corporationCode = $('#corporationCode').val();
		let url = "../cm/cm0040.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode;
		Popup.open(url, callbackFromPopup, params, this);
	})
	.on('click', '#btnSelectUserBelong', function() {
		// ユーザ所属選択
		const params = null, corporationCode = $('#corporationCode').val();
		const organizationCode = $('#organizationCode').val();
		const postCode = $('#postCode').val();
		let url = "../cm/cm0050.html?initSearch=true";
		if (corporationCode)
			url += "&corporationCode=" + corporationCode;
		if (organizationCode)
			url += "&organizationCode" + organizationCode;
		if (postCode)
			url += "&postCode" + postCode;
		Popup.open(url, callbackFromCm0050, params, this);
	})
	.on('click', '.clear-input-group', function() {
		// ユーザ・組織・役職のクリアボタン
		$(this).closest('.input-group').find('input,select,textarea').val('');
	})
	.on('click', '#btnSendMail', function() {
		// メール送信
		let params = {
			"localeCode": $('#mailLocaleCode').val(),
			"sendTo": $('#mailSendTo').val()
		};
		NCI.clearMessage();
		let res = NCI.post('/sandbox/sendMail', params, false);
	})
	.on('click', '#btnDialog', function() {
		// Yes/Noダイアログ
		NCI.confirm("Yes/Noダイアログ。コールバックを省略する事もできます",
			function() {
				NCI.alert("Yes！");
			},
			function() {
				NCI.alert("No！");
			})
	})
	.on('click', '#btnValidationError', function() {
		NCI.alert('例外をスローせずにサーバ側からエラーメッセージを返します。ロールバックは実施されません。', function() {
			// バリデーションエラー：サーバ側でエラー判定し、エラーありならメッセージとともに戻す。
			// クライアントでの可否判定は res.successで行うこと。
			// 	・[利点]ログに例外が記録されない
			// 	・[欠点]ロールバックされない
			NCI.post('/sandbox/validationError').done(function(res) {
				if (res && res.success) {
					// 正常時
				} else {
					// 異常時。サーバ側から送られたメッセージは NCI.post()内で画面に反映している。
				}
			});
		});
	})
	.on('click', '#btnValidationException', function(ev) {
		NCI.alert('例外をスローしエラーログを記録したうえでサーバ側からエラーメッセージを返します。ロールバックが実施されます。', function() {
			// バリデーションエラー：サーバ側で例外をスローするが、エラー画面へ遷移せず、バリデーションエラーと同様にメッセージだけを表示する
			// 	・[利点]ロールバックされる
			// 	・[欠点]ログに例外が記録される
			NCI.post('/sandbox/validationException').done(function(res) {
				if (res && res.success) {
					// 正常時
				} else {
					// 異常時。サーバ側から送られたメッセージは NCI.post()内で画面に反映している。
				}
			});
		});
	})
	.on('click', '#btnJsonParseError', function() {
		// JSONパース例外：サーバ側でマッピングできない値を送ることでHTTP400 BAD REQUESTが返るが、
		// それをフレームワーク側で自動的にハンドリングしている
		// （実際のハンドリング処理は NCI.defaultErrorCallback()で行ってる）
		NCI.alert('例外をスローしエラーログを記録したうえでエラー画面に遷移します。ロールバックが実施されます。', function() {
			NCI.post('/sandbox/sendMail', {"hoge" : "fuga"});	// 意図的に誤ったパラメータをPOSTする
		});
	})
	.on('click', '#btnNotFoundError', function() {
		// コンテンツが見つからないエラー例外：
		NCI.alert('例外をスローしエラーログを記録したうえでエラー画面に遷移します。ロールバックが実施されます。', function() {
			NCI.post('/sandbox/notFoundError');
		});
	})
	.on('click', '#btnServiceUnavailable', function() {
		// サービス利用不可例外：
		NCI.alert('例外をスローしエラーログを記録したうえでエラー画面に遷移します。ロールバックが実施されます。', function() {
			NCI.post('/sandbox/serviceUnavailableError');
		});
	})
	.on('click', '#btnForbidden', function() {
		// 権限なし例外：
		NCI.alert('例外をスローしエラーログを記録したうえでエラー画面に遷移します。ロールバックが実施されます。', function() {
			NCI.post('/sandbox/forbiddenError');
		});
	})
	.on('click', '#btnSystemError', function() {
		// システムエラー例外：
		NCI.alert('例外をスローしエラーログを記録したうえでエラー画面に遷移します。ロールバックが実施されます。', function() {
			NCI.post('/sandbox/systemError');
		});
	})
	.on('click', '#btnAlreadyUpdatedError', function() {
		// 排他ロックエラー：
		NCI.alert('例外をスローしエラーログを記録したうえでサーバ側からエラーメッセージを返します。ロールバックが実施されます。', function() {
			NCI.post('/sandbox/alreadyUpdatedError');
		});
	})
	// 申請内容
	.on('click', '#btnVd0330', function() {
		NCI.flushScope('_vd0330', {
			'keys' : { 'applicationNo' : '2018/08-営業部00014' },
			'corporationCode' : 'NCI',
			'screenCode' : 'SCR0029',
			'backUrl' : '../sandbox/sandbox.html',	// VD0330から戻ってくるときのURL。つまり、この画面のURL
			'screenName' : '参照画面のサンプル',			// 指定されていればその画面名を使ってレンダリングされる
			'dcId' : 1,								// 指定されていればその表示条件ID、無指定なら申請
			'trayType' : 'ALL'						// 指定されていればそのトレイタイプ、無指定なら汎用案件。変更したいなら'FORCE'あたりがオススメ
		});
		NCI.redirect('../vd/vd0330.html');
	})
	// リクエスト・タイムアウト検証
	.on('click', '#btnRequestTimeout', function() {
		const $timeoutSec = $('#timeoutSec');
		if (!Validator.validate($timeoutSec)) {
			return false;
		}
		NCI.post('sandbox/verifyTimeout', { 'timeoutSec' : $timeoutSec.val() });
	})
	;

	// ドラッグ＆ドロップによるファイルアップロード
	FileUploader.setup("#divFileUpload", "/sandbox/upload", true);
	FileUploader.setup("#divExcelUpload", "/sandbox/uploadExcelForm", true, callbackFromExcelUpload);

	// ファイルコントロールによるファイルアップロード
	FileUploader.setup('#fileFileUpload', "/sandbox/upload", true);

});

/** 企業選択／組織選択／役職選択／ユーザ選択の各ポップアップ画面からのコールバック */
function callbackFromPopup(entity, trigger) {
	if (entity) {
		const $root = $(trigger).closest('div.input-group');
		NCI.toElementsFromObj(entity, $root);
		$root.find('input').trigger('validate');
	}
}

/** ユーザ所属選択画面からのコールバック関数 */
function callbackFromCm0050(ub, trigger) {
	if (ub) {
		// 共通部分のコールバック
		callbackFromPopup(ub, trigger);

		// 「氏名＋組織＋役職」は同じフィールドがないので、個別に設定
		$('#userAndOrgAndPostName')
			.val(ub.organizationName + ' ' + ub.postName + ' ' + ub.userName)
			.trigger('validate');
	}
}
/** EXCELフォームのアップロード後のコールバック関数 */
function callbackFromExcelUpload(res, exParams) {
	if (res && res.success) {
		// 取込結果の表示
		const $user = $('#excelUploadResult div.modal-header');
		NCI.toElementsFromObj(res.user, $user);

		const $belongs = $('#excelUploadResult div.modal-body');
		new ResponsiveTable($belongs).fillTable(res.user.belongs)

		$('#excelUploadResult').modal();
	}
}
