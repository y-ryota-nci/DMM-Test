var ctx = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
$(function() {
	const params = { messageCds : ['MSG0273']}
	NCI.init('/vd0130/init').done(function(res) {
		// フォントサイズの選択肢
		NCI.createOptionTags($("#fontSize"), res.fontSizes);

		/** DB予約語 */
		vd0130.dbmsReservedColNames = res.dbmsReservedColNames;

		var root = ctx.root;
		NCI.toElementsFromObj(root, $('#inputed'));

		var currentMax = 0;		// 差分更新してしまうので、DBの現在値がいつであろうと現パーツの中だけで整合性が取れていれば問題ない

		// パーツコード連番は、コンテナ内の全パーツのパーツコードの最大値
		var childPartsIds = ctx.root.childPartsIds, len = childPartsIds.length;
		for (var i = 0; i < len; i++) {
			var childPartsId = childPartsIds[i];
			var childParts = ctx.designMap[childPartsId];
			if (!childParts) continue;
			var partsCodeSeq = childParts.partsCode.substring(3);
			currentMax = Math.max(currentMax, partsCodeSeq);
		};
		$('#partsCodeSeq').data('validate').min = currentMax;

		$('#btnOK').prop('disabled', false);
	});

	$(document)
	// 更新ボタン押下
	.on('click', '#btnOK', function(ev) {
		NCI.clearMessage('danger');
		var $root = $('#inputed');
		if (!Validator.validate($root.find("input, textarea, select", true))) {
			return false;
		}
		// DB予約語／画面デザイナー予約語をカラム名に使用していないか
		const tableName = $('#tableName').val();
		if (vd0130.dbmsReservedColNames.indexOf(tableName) > -1) {
			const msg = NCI.getMessage('MSG0273', [NCI.getMessage('tableName'), tableName]);
			NCI.addMessage('danger', [msg]);
			return false;
		}

		// 入力内容を吸い上げて、画面ロード時点の内容を上書きしたものを生成
		var inputed = NCI.toObjFromElements($root);
		ctx.root = $.extend({}, ctx.root, inputed);

		// コールバック関数の呼び出し
		Popup.close(ctx);
	})
	// 閉じるボタン押下
	.on('click', '#btnClose', function(ev) {
		Popup.close();
	})
	// DBカラム定義
	.on('click', '#btnDbColumnDef', function(ev) {
		Popup.open('./vd0129.html', callbackFromVd0129, ctx);
	});
});

// DBカラム名一覧からのコールバック
function callbackFromVd0129(rows) {
	if (rows) {
		// パーツIDをキーとしたカラム定義リストを作成
		var map = {};
		for (let i = 0; i < rows.length; i++) {
			let partsId = rows[i].partsId;
			if (map[partsId] == null)
				map[partsId] = [];
			map[partsId].push(rows[i]);
		}
		// パーツ定義に対してカラム定義を反映
		for (let i = 0; i < ctx.root.childPartsIds.length; i++) {
			let partsId = ctx.root.childPartsIds[i];
			let design = ctx.designMap[partsId];
			design.columns = map[partsId];
		}
	}
}

const vd0130 = {
		/** DB予約語 */
		dbmsReservedColNames : []
};