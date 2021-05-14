// VD0310をエミュレートするためのダミー
var vd0310 = {
	"contents" : {
		"trayType" : null,
		"dcId" : null,
	},
};
$(function() {
	let params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	params.ctx.runtimeMap = {};	// 毎回ゼロベースでランタイムを作り直させる
	params.messageCds = ['MSG0110', 'thisRow'];
	NCI.init('/vd0115/init', params).done(function(res) {
		if (res.success) {
			vd0310.contents = res.contents;

			$('#editArea')
				.html(res.html)
				.data({
					'runtimeMap': res.ctx.runtimeMap,
					'previewContext' : res.ctx
				});

			NCI.ymdPicker($('input.ymdPicker', '#editArea'));
			NCI.ymPicker($('input.ymPicker', '#editArea'));

			// カスタムCSSスタイル
			$('#customCssStyleTag').html(res.customCssStyleTag);

			// 外部Javascript読み込み
			const outsideURL = '../../endpoint/javascript/outside?'
				+ PARTS.toJavascriptQueryString(res.ctx.javascriptIds);
			$('#outside-javascript').attr('src', outsideURL).load(function() {
				// Load時の呼び出し関数
				const loadFunctions = res.ctx.loadFunctions;
				if (loadFunctions && loadFunctions.length) {
					for (let i = 0; i < loadFunctions.length; i++) {
						const funcName = loadFunctions[i].funcName;
						const param = loadFunctions[i].param;
						PARTS.execFunction(funcName, param);
					}
				}
			});
		}

		// クライアント側のバリデーション
		$('#btnValidateClient').on('click', function(ev) {
			var $targets = $('#editArea').find('input,select,textarea');
			if (Validator.validate($targets, true)) {
				alert('OK');
			}
		}).prop('disabled', false);

		// サーバー側のバリデーション
		$('#btnValidateServer').on('click', function(ev) {
			// 既存エラーのクリア
			Validator.hideBalloon();

			// 入力内容を吸い上げてRuntimeMapを最新状態へ。
			let ctx = $('#editArea').data('previewContext');
			ctx.runtimeMap = PARTS.fillRuntimeMap();

			// 業務管理項目Mapを取得
			let p = { 'ctx' : ctx };
			NCI.post('/vd0115/validateServer', p).done(function(res) {
				if (res && res.success) {
					if (res.errors.length === 0)
						alert('OK');
					else {
						// サーバ側バリデーション結果を反映
						PARTS.displayValidationResult(res.errors, res.html, res.runtimeMap);
					}
				}
				NCI.ymdPicker($('input.ymdPicker'));
				NCI.ymPicker($('input.ymPicker'));
			});
		}).prop('disabled', false);

		// 業務管理項目の表示ボタン
		$('#btnDispBusinessInfo').on('click', function(ev) {
			// 既存エラーのクリア
			Validator.hideBalloon();

			// 入力内容を吸い上げてRuntimeMapを最新状態へ。
			let ctx = $('#editArea').data('previewContext');
			ctx.runtimeMap = PARTS.fillRuntimeMap();

			// 業務管理項目Mapを取得
			let p = { 'ctx' : ctx };
			NCI.post('/vd0115/createBusinessInfoMap', p).done(function(res) {
				if (res && res.success) {
					let $dl = $('#businessInfo').empty();
					let srcDT = document.createElement('dt');
					let srcDD = document.createElement('dd');
					let count = 0;
					$.each(res.businessInfoMap, function(name, value) {
						$(srcDT.cloneNode(true)).text(name).appendTo($dl);
						$(srcDD.cloneNode(true)).text(value).appendTo($dl);
						count++;
					});
					if (count === 0) {
						$(srcDT.cloneNode(true)).text(NCI.getMessage('noRecord')).appendTo($dl);
					}

					$('#businessInfoArea').modal();
				}
			});
		}).prop('disabled', false);
	});

	$('#btnClose').on('click', function(ev) {
		if (window === window.parent)
			window.close();
		else
			Popup.close();
	});
	// 画面サイズ変更
	$(window).on('resize', function(ev) {
		// 全パーツの再描画を遅延実行予約
		NCI.doLater(PARTS.redraw, 300);
	});
});
