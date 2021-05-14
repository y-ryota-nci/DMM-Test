//VD0310をエミュレートするためのダミー
var vd0310 = {};

$(function() {
	// VD0310をエミュレートするため、引き継ぎパラメータを転写しておく。
	const params = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	vd0310.contents = params.contents;
	$('#editArea').data({
		'htmlId' : params.htmlId,
		'runtimeMap' : params.runtimeMap,
		'previewContext' : params.previewContext	// これがあるのはプレビュー時のみ
	});

	NCI.init('/vd0320/init', params).done(function(res) {
		if (res.success) {
			vd0320.init(res);
		}
	});

	$('#btnOK').on('click', vd0320.doUpdate);
	$('#btnClose').on('click', vd0320.doClose);
	// 画面サイズ変更
	$(window).on('resize', function(ev) {
		if (vd0320.initialized) {
			// 全パーツの再描画を遅延実行予約
			NCI.doLater(function() {
				PARTS.redraw(true, params.htmlId);
			}, 300)

			// 独立画面側で画面サイズが変更になったので、親画面へ戻った時に親画面も読み直す
			vd0320.redraw = true;
		}
	});
});

// 当画面固有
var vd0320 = {
	// 当画面を閉じたときに、呼び元の画面でも再描画処理を行う必要があるか
	redraw : false,

	// 初期化済みか
	initialized : false,

	// 当画面でSubmitするときとき（＝ポップアップを閉じる）の呼び出し関数
	submitFunctions : null,

	// 初期化
	init : function(res) {
		// HTMLのレンダリング
		const $editArea = $('#editArea').html(res.html)
		// 日付／年月UI
		NCI.ymdPicker($('input.ymdPicker', $editArea));
		NCI.ymPicker($('input.ymPicker', $editArea));
		// コンテナ名
		$('#containerName').text(res.containerName);
		// OKボタン

		// 親パーツである独立画面パーツの表示条件設定でOKボタンが押せるかが決まる
		const htmlId = $('#editArea').data('htmlId');
		const rt = $('#editArea').data('runtimeMap')[htmlId];
		const isWorklist = (['WORKLIST', 'NEW', 'FORCE'].indexOf(vd0310.contents.trayType) > -1);
		const isInputable = (rt.dcType === DcType.INPUTABLE);
		$('#btnOK').toggleClass('hide', !isWorklist || !isInputable).prop('disabled', false);
		// カスタムCSSスタイル
		$('#customCssStyleTag').html(res.customCssStyleTag);
		// Submit時の呼び出し関数を退避
		vd0320.submitFunctions = res.submitFunctions;
		// 外部Javascript読み込み
		const outsideURL = '../../endpoint/javascript/outside?'
			+ PARTS.toJavascriptQueryString(res.javascriptIds);
		$('#outside-javascript').attr('src', outsideURL).load(function() {
			// Load時の呼び出し関数
			const loadFunctions = res.loadFunctions;
			if (loadFunctions && loadFunctions.length) {
				for (let i = 0; i < loadFunctions.length; i++) {
					const funcName = loadFunctions[i].funcName;
					const param = loadFunctions[i].param;
					PARTS.execFunction(funcName, param);
				}
			}
		});

		// 初期化終わりの通知。これによって画面リサイズが可能になる。
		// ポップアップ画面は初期化完了前に親画面から強制リサイズが実行されるので、
		// 初期化以降にだけリサイズイベントがフックされるようにしなければならない
		vd0320.initialized = true;
	},

	// OKボタン押下
	doUpdate : function(ev) {
		// 承認扱いでパーツの必須バリデーション実行
		const dummyAction = { 'actionType' : ActionType.NORMAL };
		if (!PARTS.validateParts(true, dummyAction, vd0320.submitFunctions)) {
			return false;
		}
		// パーツから入力値を吸い上げ
		PARTS.fillRuntimeMap();

		Popup.close({
			'runtimeMap' : $('#editArea').data('runtimeMap'),
			'redraw' : vd0320.redraw	// 再描画指示
		});
	},

	// 閉じるボタン押下
	doClose : function(ev) {
		Popup.close({
			'runtimeMap' : null,		// 独立画面としてはデータの書き換え無し
			'redraw' : vd0320.redraw	// 再描画指示
		});
	}
}
