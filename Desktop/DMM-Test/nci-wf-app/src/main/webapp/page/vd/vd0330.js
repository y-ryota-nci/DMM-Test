//VD0310をエミュレートするためのダミー
var vd0310 = {};

$(function() {
	// VD0310をエミュレートするため、引き継ぎパラメータを転写しておく。
	vd0330.transitionParams = NCI.flushScope('_vd0330');
	vd0330.transitionParams.messageCds = ['MSG0067', 'MSG0071', 'MSG0110', 'thisRow', 'docInfo'];
	NCI.init('/vd0330/init', vd0330.transitionParams).done(function(res) {
		if (res.success) {
			// 遷移前に更新結果が正常であれば、ここでその成功メッセージを表示してやる
			if (NCI.flushScope('_vd0330.isSuccess')) {
				NCI.addMessage('success', NCI.getMessage('MSG0067', NCI.getMessage('docInfo')));
			}
			vd0330.init(res);
		}
	});

	// 画面サイズ変更
	$(window).on('resize', function(ev) {
		if (vd0330.initialized) {
			// 全パーツの再描画を遅延実行予約
			NCI.doLater(function() {
				PARTS.redraw(true, params.htmlId);
			}, 300)

			// 独立画面側で画面サイズが変更になったので、親画面へ戻った時に親画面も読み直す
			vd0330.redraw = true;
		}
	});
});

// 当画面固有
var vd0330 = {
	// 当画面を閉じたときに、呼び元の画面でも再描画処理を行う必要があるか
	redraw : false,

	// 初期化済みか
	initialized : false,

	// 初期化
	init : function(res) {
		// HTMLのレンダリング
		const $editArea = $('#editArea')
			.html(res.html)
			.data('runtimeMap', res.runtimeMap);
		vd0310.contents = res.contents;

		// 日付／年月UI
		NCI.ymdPicker($('input.ymdPicker', $editArea));
		NCI.ymPicker($('input.ymPicker', $editArea));
		// コンテナ名
		$('#screenName').text(res.screenName);

		// カスタムCSSスタイル
		$('#customCssStyleTag').html(vd0310.contents.customCssStyleTag);
		// 外部Javascript読み込み
		const outsideURL = '../../endpoint/javascript/outside?'
			+ PARTS.toJavascriptQueryString(vd0310.contents.javascriptIds);
		$('#outside-javascript').attr('src', outsideURL).load(function() {
			// Load時の呼び出し関数
			const loadFunctions = res.contents.loadFunctions;
			if (loadFunctions && loadFunctions.length) {
				for (let i = 0; i < loadFunctions.length; i++) {
					const funcName = loadFunctions[i].funcName;
					const param = loadFunctions[i].param;
					PARTS.execFunction(funcName, param);
				}
			}
		});
		// 戻るボタン
		if (res.backUrl) {
			$('button.btnBack').click(function() {
				NCI.redirect(res.backUrl);
			}).removeClass('hide');
		}
		// 更新ボタン
		$('#btnUpdate')
				.click(vd0330.execute)
				.toggleClass('hide', ['WORKLIST', 'FORCE'].indexOf(res.contents.trayType) < 0)
				.prop('disabled', false);

		// 初期化終わりの通知。これによって画面リサイズが可能になる。
		// ポップアップ画面は初期化完了前に親画面から強制リサイズが実行されるので、
		// 初期化以降にだけリサイズイベントがフックされるようにしなければならない
		vd0330.initialized = true;
	},

	/**
	*
	*/
	execute: function() {
		// バリデーション：必須入力あり
		const action = { 'actionCode' : 2 /* 2:承認 */ };
		if (!PARTS.validateParts(true, action, vd0310.contents.submitFunctions, $(document))) {
			return false;
	}

		// 更新処理
		const msg = NCI.getMessage('MSG0071', NCI.getMessage('docInfo'));
		NCI.confirm(msg, function() {
			const params = {
				actionInfo: action,
				contents : vd0310.contents,
				runtimeMap : PARTS.fillRuntimeMap()
			};
			NCI.post("/vd0330/update", params).done(function(res) {
				if (res && res.success) {
					if (!res.errors || res.errors.length === 0) {
						// 正常なら再表示
						NCI.flushScope('_vd0330', vd0330.transitionParams);
						NCI.flushScope('_vd0330.isSuccess', true);
						NCI.redirect('./vd0330.html');
					}
					else {
						// サーバ側バリデーション結果を反映
						PARTS.displayValidationResult(res.errors, res.html, res.runtimeMap);
					}
				}
			});
		});
	},
}
