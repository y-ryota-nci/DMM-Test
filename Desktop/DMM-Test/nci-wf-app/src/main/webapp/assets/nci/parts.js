$(function() {
	// 組織選択パーツの初期化
	ORG_SELECT.init();
	// ユーザ選択パーツの初期化
	USER_SELECT.init();
	// リピーターの初期化
	PAGING_CONTAINER.init();
	// 検索ボタンパーツの初期化
	MASTER_BUTTON.init();
	// マスタ選択パーツの初期化
	MASTER_SELECTOR.init();
	// 添付ファイルパーツの初期化
	ATTACH_FILE.init();
	// 画像パーツの初期化
	IMAGE.init();
	// 独立画面パーツの初期化
	STAND_ALONE.init();
});

/** パーツ共通関数 */
var PARTS = {

	/** パーツの入力値を吸い上げ */
	fillRuntimeMap : function() {
		let $editArea = $('#editArea');
		let runtimeMap = $editArea.data('runtimeMap');

		// 吸上げ対象のパーツ単位で吸上げ
		// 表示条件＝入力可で有効条件＝有効なパーツには '.inputable'が付く。吸上げ対象は '.inputable' であること！
		$editArea.find('article.parts[id].inputable').each(function() {
			let rt = runtimeMap[this.id];	// パーツのランタイム情報
			if (!rt) return true;

			// パーツ配下の役割コードをもつエレメントから値を抜き出し
			let $dataRoles = $(this).find('[data-role]');
			if ($dataRoles.length === 0) return true;	// 役割コードをもつエレメントがなければ書き換えしないということだ

			rt.values = {};	// ゼロから再構築することで、ゴミが混入しないようにする
			$dataRoles.each(function(i, elem) {
				let $elem = $(elem);
				let role = $elem.data('role');
				let value = null;
				if (/radio|checkbox/.test(elem.type)) {
					if (elem.checked)
						value = elem.value;
					else if (rt.values[role] == null)
						value = '';	// 未選択と要素なしによるnull値を区別するため
				}
				else if (/TD|TH|SPAN|LABEL|DIV/.test(elem.tagName))
					value = $elem.text();
				else {
					value = elem.value;
				}
				if (value != null) {
					// カンマ等を除去した値とする
					rt.values[role] = NCI.getPureValue(value, $elem.data('validate'));

					switch (rt.partsType) {
					case PartsType.MASTER:
					case PartsType.DROPDOWN:
						// ドロップダウンリストとマスタ選択パーツのラベルは動的に生成されるので、サーバサイドだと取得するのが困難。なのでクライアント側で吸い上げる
						const roleCodeLabel = rt.partsType === PartsType.DROPDOWN ? 'dropdownLabel' : 'label';
						if (elem.selectedIndex < 0 || value == '')
							rt.values[roleCodeLabel] = '';
						else
							rt.values[roleCodeLabel] = elem.options[elem.selectedIndex].label;
						break;
					}
				}
			});
		});
		return runtimeMap;
	},

	/**
	 * パーツのバリデーション
	 * @param requuired 必須チェックするならtrue
	 * @param action アクション情報(サーバサイドの actionInfo)
	 * @param submitFunctions コンテナ／画面でのサブミット時の呼び出し関数リスト
	 * @param $root バリデーション対象パーツのルート要素（未指定なら $('#editArea')となる）
	 */
	validateParts : function(required, action, submitFunctions, $root) {
		// 既存のバルーンを消す
		const $top = $root || $('#editArea');
		Validator.hideBalloon($top.find('.has-error'));

		// バリデーション
		const $targets = $top.find('input, select, textarea');
		if (!Validator.validate($targets, required)) {
			return false;
		}
		// Submit時の呼び出し関数
		if (submitFunctions && submitFunctions.length) {
			for (let i = 0; i < submitFunctions.length; i++) {
				const funcName = submitFunctions[i].funcName;
				const param = submitFunctions[i].param;
				const r = PARTS.execFunction(funcName, action, param);
				if (!r && typeof(r) !== 'undefined') {
					return false;
				}
			}
		}
		return true;
	},

	/**
	 * バリデーション結果を画面へ反映
	 * @param errors パーツ単位のバリデーションエラー結果
	 * @param html エラー内容を反映したHTML（コンテナがエラーページを表示するため）
	 * @param runtimeMap ランタイムMap（コンテナがエラーページを表示するため）
	 */
	displayValidationResult : function(errors, html, runtimeMap) {
		// バリデーション結果を反映したHTML画面を再描画
		// （エラーが非表示のページにあるかもしれないので、エラー項目へページ移動させてる）
		if (html)
			$('#editArea').html(html);

		// バリデーション結果を反映したランタイムMapを取込み
		if (runtimeMap)
			$('#editArea').data('runtimeMap', runtimeMap);

		// バリデーション結果のバルーンを表示
		if (errors && errors.length > 0) {
			let $focus = null;
			$.each(errors, function(i, error) {
				let $target = null;
				if (error.role)
					// パーツ内の特定エレメントにエラーがある
					$target = $('#' + error.htmlId).find('[data-role=' + error.role + ']:first');
				if (!$target || $target.length === 0)
					// パーツ内のエラーエレメントが特定できないなら、このパーツ自体にエラーがあるということだ
					$target = $('#' + error.htmlId);

				// パーツが見えていればそこにバルーンを表示するが、見えなければエラーメッセージとして表示する
				if ($target.is(':visible')) {
					Validator.showBalloon($target, error.message);
					if ($focus === null)
						$focus = $target;
				} else {
					NCI.addMessage('danger', error.message + " -> " + (error.labelText || error.htmlId));
				}
			});
			if ($focus != null)
				NCI.scrollTo($focus);
		}
	},

	// アイコンCSSクラス
	toIconClass : function(partsType) {
		if (partsType != null) {
			switch (partsType) {
			case PartsType.TEXTBOX:
				return 'glyphicon glyphicon-pencil';
			case PartsType.LABEL:
				return 'glyphicon glyphicon-tag';
			case PartsType.HYPERLINK:
				return 'glyphicon glyphicon-link';
			case PartsType.ATTACHFILE:
				return 'glyphicon glyphicon-file';
			case PartsType.IMAGE:
				return 'glyphicon glyphicon-picture';
			case PartsType.CHECKBOX:
				return 'glyphicon glyphicon-check';
			case PartsType.RADIO:
				return 'glyphicon glyphicon-record';
			case PartsType.DROPDOWN:
				return 'glyphicon glyphicon-collapse-down';
			case PartsType.NUMBERING:
				return 'icon-numberlist';
			case PartsType.ORGANIZATION:
				return 'glyphicon glyphicon-home';
			case PartsType.USER:
				return 'glyphicon glyphicon-user';
			case PartsType.SEARCH_BUTTON:
				return 'glyphicon glyphicon-search';
			case PartsType.EVENT_BUTTON:
				return 'glyphicon glyphicon-flash';
			case PartsType.STAMP:
				return 'icon-rubberstamp';
			case PartsType.REPEATER:
				return 'glyphicon glyphicon-repeat';
			case PartsType.GRID:
				return 'glyphicon glyphicon-th';
			case PartsType.STAND_ALONE:
				return 'glyphicon glyphicon-new-window';
			case PartsType.MASTER:
				return 'glyphicon glyphicon-list-alt';
			}
		}
		return '';
	},

	/**
	 * パーツの値を返す。このとき、カンマ区切り等の書式は除去される。
	 * また、パーツに複数の値を保持する場合はdefaultRoleCodeに指定されている値。
	 * @param $htmlId パーツのhtmlId
	 */
	getPartsValue : function(htmlId) {
		let val = null;
		let rt = $('#editArea').data('runtimeMap')[htmlId];
		if (rt == null)
			throw new Error("htmlId=" + htmlId + "に対応したランタイムパーツ情報がありません");
		if (rt.defaultRoleCode == null) {
			// デフォルト役割コードが未設定なら、そもそも値を保持しないパーツである
			val = null;
		}
		else {
			let $parts = $('#' + htmlId);
			let $elem = $parts.find('[data-role=' + rt.defaultRoleCode + ']');
			if ($parts.length === 0 || $elem.length === 0) {
				// ランタイムMapに格納された画面ロード時点のパーツ値を抜き出す
				val = rt.values[rt.defaultRoleCode];
			}
			else {
				// HTMLとして描画されているエレメントから値を抜き出す
				val = NCI.getPureValue(Validator.getValue($elem), $elem.data('validate'));
			}
		}
		return val;
	},

	/**
	 * パーツに値を設定。この時カンマ区切り等の書式は自動付与される。
	 * また、パーツに複数の値を保持する場合はdefaultRoleCodeに指定されているエレメントへのみ設定。
	 * @param htmlId 対象パーツの htmlId
	 * @param val 設定する値（書式なしであること）
	 * @param isForce パーツがレンダリングされていなくても強制的にランタイム情報を書き換える場合は true
	 */
	setPartsValue : function(htmlId, val, isForce) {
		let rt = $('#editArea').data('runtimeMap')[htmlId];
		if (rt == null)
			throw new Error("htmlId=" + htmlId + "に対応したランタイムパーツ情報がありません");

		if (rt.defaultRoleCode == null) {
			// デフォルト役割コードがないということは、このパーツが固有の値を保持しないパーツであるということだ
			return null;
		}
		else {
			let $parts = $('#' + htmlId);
			let $elem = $parts.find('[data-role=' + rt.defaultRoleCode + ']');
			if ($parts.length === 0 || $elem.length === 0) {
				// セットすべき対象がない。
				// おそらく表示条件＝非表示か、またはページ制御によって非表示頁のコンテンツでレンダリングされていないかであろう。
				// レンダリングされていないものを書き換えるわけにはいかないので通常は無視する（たまに強制書き換えしたいこともある）
				if (isForce) {
					rt.values[rt.defaultRoleCode] = val;
					// マスタ選択パーツの場合、valが空文字であればラベルも自動で空文字で設定しておく
					switch (rt.partsType) {
						case PartsType.MASTER:
							if (val == '')
								rt.values['label'] = '';
							break;
					}
				}
			}
			else {
				// HTMLとして描画されているエレメントへ書式付で値を適用
				// ラジオボタンは複数のelemがあるので数分だけループさせる
				$elem.each(function(i, elem) {
					NCI.setElementFromObj(elem, val);
				});
				$elem.trigger('validate');
			}
			return $elem;
		}
	},

	/**
	 * 「自パーツと同一or下位コンテナに存在」＋「指定パーツIDをもつ」パーツ(jqueryオブジェクト)を求める
	 * @param partsId 対象パーツがもつパーツID
	 * @param htmlId 自パーツのHTML_ID
	 */
	findParts : function(partsId, htmlId) {
		let suffix = '';
		if (htmlId.indexOf('_') > -1) {
			// 同じコンテナであればHTML_IDが前方一致するはず
			suffix += '[id^=' + htmlId.substring(0, (htmlId.lastIndexOf('_') + 1)) + ']';
		}
		let selector = 'article[data-parts-id=' + partsId + ']' + suffix;
		return $(selector);
	},

	/**
	 * 「自パーツと同一or下位コンテナに存在」＋「指定パーツIDをもつ」パーツ(runtimeMapの中身)を求める
	 * @param partsId 対象パーツがもつパーツID
	 * @param htmlId 自パーツのHTML_ID
	 * @param runtimeMap 実行時パーツMap
	 */
	findRuntimes : function(partsId, htmlId, runtimeMap) {
		if (!runtimeMap)
			throw new Error("runtimeMapがnullです");

		const runtimes = [];
		for (let id in runtimeMap) {
			const rt = runtimeMap[id];
			if (rt.htmlId.indexOf(htmlId) === 0 && rt.partsId == partsId) {
				runtimes.push(rt);
			}
		}
		return runtimes;
	},

	/**
	 * 定義済みjavascript関数を実行する。
	 * @param funcName 実行したいJavascript関数名（必須）
	 * @param arg1 関数に渡したい引数1（省略可能）
	 * @param arg2 関数に渡したい引数2（省略可能）
	 * @param arg3 関数に渡したい引数3（省略可能）
	 * @param arg4 関数に渡したい引数4（省略可能）
	 */
	execFunction : function(funcName, arg1, arg2, arg3, arg4) {
		// 関数名が「.」で区切られているか（「aaaa.bbb()」のようなObjectのメソッド形式か）
		if (!funcName)
			return true;

		if (funcName.indexOf('.') < 0) {
			if (window[funcName]) {
				// 明示的にエラーが返ってこなければOKとする
				return window[funcName](arg1, arg2, arg3, arg4);
			}
		}
		else {
			const astr = funcName.split('.'), obj = astr[0], func = astr[1];
			if (window[obj] && window[obj][func]) {
				return window[obj][func](arg1, arg2, arg3, arg4);
			}
		}
		alert("関数「" + funcName + "」を呼び出そうとしましたが、未定義で呼び出せません。外部Javascriptで定義されているか、その外部Javascriptがコンテナから使用されるよう設定されているかを確認してください。");
		return false;
	},

	/** javascriptIDリストをクリエリストリングへ変換 */
	toJavascriptQueryString : function(javascriptIds) {
		let qs = '';
		if (javascriptIds && javascriptIds.length) {
			for (let i = 0; i < javascriptIds.length; i++) {
				qs += (qs.length === 0 ? '' : '&') + 'jsid=' + javascriptIds[i];
			}
		}
		return qs;
	},

	/**
	 * 全パーツを再描画する
	 * @param force 常に強制描画するならtrue、自動判定ならfalse。省略時はfalse扱い
	 * @param htmlIdStandAlone （独立画面から呼ばれるときのみ）独立画面パーツのHTML ID。省略時は通常扱い
	 */
	redraw : function(force, htmlIdStandAlone) {
		const $editArea = $('#editArea');
		if (!force) {
			// 最後に全体を描画したときの表示幅から変更されていなければ何もしない
			const htmlId = $('#editArea>article.parts-container').attr('id');	// ルートコンテナのhtmlId
			const rt = $editArea.data('runtimeMap')[htmlId];
			const oldViewWidth = rt.viewWidth, newViewWidth = NCI.getViewWidth();
			const oldIsMobile = /XS/i.test(oldViewWidth);	// 以前の表示幅はモバイルだった
			const newIsMobile = /XS/i.test(newViewWidth);	// 現在の表示幅はモバイルである
			if ((oldIsMobile && newIsMobile) || (!oldIsMobile && !newIsMobile)) {
				return $.Deferred().resolve().promise();	// 戻り値を promiseオブジェクトにするためのダミー;
			}
		}

		// バリデーション
		if (!PARTS.validateParts(false)) {
			return $.Deferred().resolve().promise();	// 戻り値を promiseオブジェクトにするためのダミー;
		}

		// 画面項目をRuntimeMapへ反映
		PARTS.fillRuntimeMap();

		// 独立画面から呼ばれていれば、再描画も独立画面としてでなければならぬ。
		const renderAsStandAlone = location.pathname.indexOf('vd0320.html') >= 0;

		// 送信パラメータ
		const params = {
				"contents" : vd0310.contents,
				"runtimeMap" : $editArea.data('runtimeMap'),
				"previewContext" : $editArea.data('previewContext'),		// これがあるのはプレビュー時のみである
				'htmlId' : htmlIdStandAlone,
				'renderAsStandAlone' : renderAsStandAlone
		};

		// 送信
		return NCI.post('/vd0310/redrawAllParts', params).done(function(res) {
			$editArea
				.html(res.html)
				.data('runtimeMap', res.runtimeMap);

			NCI.ymdPicker($editArea.find('input.ymdPicker'));
			NCI.ymPicker($editArea.find('input.ymPicker'));
		});
	},

	/**
	 * 全パーツの再計算
	 */
	calculateAll : function() {
		const start = new Date().getTime();

		// 計算対象パーツリストを作成
		const runtimeMap = $('#editArea').data('runtimeMap');
		const targets = [];
		for (let htmlId in runtimeMap) {
			const rt = runtimeMap[htmlId];
			if (rt.calculateCondition
					&& rt.calculateCondition.calculates
					&& rt.calculateCondition.calculates.length) {
				targets.push(rt);
			}
		}

		// 計算式の依存関係でソート
		targets.sort(function(rt1, rt2) {
			const triggerPartsIds = rt1.calculateCondition.targets;
			if (triggerPartsIds && triggerPartsIds.length) {
				const len = triggerPartsIds.length;
				for (let i = 0; i < len; i++) {
					// 自分が計算元で相手が計算先なら、自分が計算根拠なので手前
					const triggerPartsId = triggerPartsIds[i];
					if (triggerPartsId === rt2.partsId) {
						return -1;
					}
				}
			}
			// 依存関係がなければHTML_ID順
			return (rt1.htmlId === rt2.htmlId ? 0
					: (rt1.htmlId < rt2.htmlId ? -1 : 1));
		});

		// 条件判定で有効なら計算実行
		const len = targets.length;
		for (let i = 0; i < len; i++) {
			const rt = targets[i];
			Enabled.evaluateEC(rt, runtimeMap);
			if (rt.evaluateCondition.enabled) {
				Calculate.executeCalc(rt, runtimeMap);
			}
		}

		if (console) {
			console.log('calculateAll() ---> ' + (new Date().getTime() - start) + 'msec')
		}
	},

	/**
	 * パーツのイベント名から、パーツイベントを実行
	 * @param $parts パーツ
	 * @param eventName 発火させるイベント名
	 **/
	firePartsEvent : function($parts, eventName, params) {
		const str = $parts.attr('data-parts-event');
		if (str) {
			const events = JSON.parse(str);
			const pe = events[eventName];
			if (pe && pe.functionName != null) {
				const r = PARTS.execFunction(pe.functionName, $parts[0], eventName, pe.functionParameter, params);
				return (typeof(r) === 'undefined' || r);
			}
		}
		return true;
	}
}

/** 組織選択パーツ */
var ORG_SELECT = {
	/** 呼び出した組織選択パーツのhtmlId */
	targetHtmlId : null,

	/** 初期化 */
	init : function() {
		$(document)
			// 検索ボタン押下
			.on('click', 'article.parts .btn-selectOrganization', function() {
				// 自身のパーツのhtmlIdを保持
				const $parts = $(this).closest('article.parts[id]');
				const sHtmlId = $parts.prop('id');
				ORG_SELECT.targetHtmlId = sHtmlId;
				// ユーザ選択画面ポップアップ
				const params = {};
				// 連携パーツIDが指定されているか
				const relation = $(this).data('relation');
				let url = "../cm/cm0020.html";
				const result = PARTS.firePartsEvent($parts, 'beforeSelect');
				if (result) {
					Popup.open(url, ORG_SELECT.callbackFromCm0020ByParts, params);
				}
			})
			// クリアボタン押下
			.on('click', 'article.parts .btn-clearOrganization', function() {
				const $parts = $(this).closest('article.parts');
				const result = PARTS.firePartsEvent($parts, 'beforeClear');
				if (result) {
					$parts.find('input[data-role]').val('');
					// 関連するユーザ選択パーツがあれば値を一緒にクリア
					ORG_SELECT.relatedPartsClear($parts);

					PARTS.firePartsEvent($parts, 'afterClear');
				}
			})
	},

	/** 組織選択ポップアップからのコールバック関数 */
	callbackFromCm0020ByParts : function(organization) {
		if (organization) {
			const sHtmlId = ORG_SELECT.targetHtmlId;
			let $self = $('#' + sHtmlId);
			let isChanged = false;
			for (let key in organization) {
				let $target = $('input[data-role="' + key + '"]', $self);
				if ($target.length > 0 && $target.val() != organization[key]) {
					$target.val(organization[key]);
					isChanged = true;
				}
			}
			// 組織が変更になった場合、関連するユーザ選択パーツがあれば値をクリア
			if (isChanged) {
				ORG_SELECT.relatedPartsClear($self);
			}
			PARTS.firePartsEvent($self, 'afterSelect')
		}
		ORG_SELECT.targetHtmlId = null;
	},

	relatedPartsClear : function($parts) {
		const relation = $('button[data-relation]', $parts).data('relation');
		if (relation && relation.length > 0) {
			const partsCode = relation;
			const tHtmlId = sHtmlId.indexOf('_') > -1 ? sHtmlId.substring(0, sHtmlId.lastIndexOf('_') + 1) + partsCode : partsCode;
			let $target = $('#'+tHtmlId);
			if ($target.length > 0) {
				$('input[data-role]', $target).each(function() {
					$(this).val('');
				});
			}
		}
	}
}

/** ユーザ選択パーツ */
var USER_SELECT = {
	/** 呼び出した組織選択パーツのhtmlId */
	targetHtmlId : null,

	/** 初期化 */
	init : function() {
		$(document)
			// 検索ボタン押下
			.on('click', 'article.parts .btn-selectUser', function() {
				// 自身のパーツのhtmlIdを保持
				const $parts = $(this).closest('article.parts[id]');
				const sHtmlId = $parts.prop('id');
				USER_SELECT.targetHtmlId = sHtmlId;
				// ユーザ所属選択画面ポップアップ
				const params = {};
				let url = "../cm/cm0050.html?initSearch=true";
				// 連携パーツIDが指定されているか
				const relation = $(this).data('relation');
				if (relation && relation.length > 0) {
					const partsCode = relation;
					// 自身のHtmlIdをもとに対象パーツのHtmlIdを生成
					const tHtmlId = sHtmlId.indexOf('_') > -1 ? sHtmlId.substring(0, sHtmlId.lastIndexOf('_') + 1) + partsCode : partsCode;
					let $target = $('input[data-role="organizationCode"]', $('#'+tHtmlId));
					let val = null;
					if ($target.length == 0) {
						val = $('#editArea').data('runtimeMap')[tHtmlId].values['organizationCode'];
					} else {
						val = $target.val();
					}
					if (val) {
						url += '&organizationCode=' + val;
					}
				}
				const result = PARTS.firePartsEvent($parts, 'beforeSelect');
				if (result) {
					Popup.open(url, USER_SELECT.callbackFromCm0040ByParts, params);
				}
			})
			// クリアボタン押下
			.on('click', 'article.parts .btn-clearUser', function() {
				const $parts = $(this).closest('.parts');
				const result = PARTS.firePartsEvent($parts, 'beforeClear');
				if (result) {
					$parts.find('input[data-role]').val('');

					PARTS.firePartsEvent($parts, 'afterClear');
				}
			})
	},

	/** ユーザ選択ポップアップからのコールバック関数 */
	callbackFromCm0040ByParts : function(user) {
		if (user) {
			let $target = $('#' + USER_SELECT.targetHtmlId);
			for (let key in user) {
				$('input[data-role="' + key + '"]', $target).val(user[key]);
			}
			PARTS.firePartsEvent($target, 'afterSelect')
		}
	}
}

/** リピーター/グリッドのようなページ制御ありのコンテナ系パーツ */
var PAGING_CONTAINER = {
	/** 初期化 */
	init : function() {
		$(document)
			// リピーター：空行を追加ボタン押下
			.on('click', 'article.parts > div.line-controller > button.btnAddEmpty', function(ev) {
				return PAGING_CONTAINER.renderHtml('addEmptyLine', this, null, false, true);
			})
			// リピーター：削除ボタン押下
			.on('click', 'article.parts > div.line-controller > button.btnDelete', function(ev) {
				return PAGING_CONTAINER.renderHtml('deleteLine', this, null, true, true);
			})
			// リピーター：コピーボタン押下
			.on('click', 'article.parts > div.line-controller > button.btnCopy', function(ev) {
				return PAGING_CONTAINER.renderHtml('copyLine', this, null, true, true);
			})
			// リピーター：行数変更
			.on('click' , 'article.parts > div.line-controller > button.btnChangeLineCount', function(ev) {
				let exParams = { "newLineCount" : (+ $(this).prev().val()) };
				return PAGING_CONTAINER.renderHtml('changeLineCount', this, exParams, false, true);
			})
			// リピーター：ページリンク押下
			.on('click', 'article.parts ul.pagination > li > a:not(.active)', function(ev) {
				let exParams = { "newPageNo" : (+ $(this).data('page')) };
				return PAGING_CONTAINER.renderHtml('changePageNo', this, exParams, false, false);
			})
			;
	},

	/** 対象パーツから送信用パラメータ生成 */
	createParams : function($trigger) {
		const $parts = $trigger.closest('article.parts');
		// 選択行を求める。ネストしたパーツかも知れないので、探し方に注意が必要だ
		const rowHtmlIds = [];
		let selector = '>table.responsive>tbody>tr>th>input[type=checkbox].selectable:checked';	// リピーターはスクロール枠がない
		selector += ', >div.table-responsive' + selector;										// グリッドはスクロール枠がある
		$parts.find(selector).each(function(i, elem) {
			// コンテナーのチェックボックスには「コンテナー自身のHtmlId + "-" + 行番号」が設定されている
			rowHtmlIds.push(elem.value);
		});

		const params = {
			"contents" : vd0310.contents,
			"htmlId" : $parts[0].id,
			"rowHtmlIds" : rowHtmlIds,
			"runtimeMap" : $('#editArea').data('runtimeMap'),
			"previewContext" : $('#editArea').data('previewContext')		// これがあるのはプレビュー時のみである
		};
		return params;
	},

	/**
	 * サーバからパーツHTMLを再取得して、パーツ自身を再描画する
	 * @param eventName トリガーとなったイベント名（EndpointのURLの一部となるので、扱い注意）
	 * @param trigger トリガーとなったエレメント
	 * @param exParams 追加の検索条件
	 * @param mustSelectRow パーツで行選択が必須か
	 * @param mustCalc 再描画後に再計算が必要か
	 */
	renderHtml : function(eventName, trigger, exParams, mustSelectRow, mustCalc) {
		// バリデーション
		if (!PARTS.validateParts(false)) {
			return false;
		}

		// 行選択が必要か
		const $trigger = $(trigger);
		const $container = $trigger.closest('article.parts');
		const $checked = $container.find('input.selectable:checked');
		const count = $checked.length;
		if (mustSelectRow && count === 0) {
			const msg = NCI.getMessage('MSG0003', NCI.getMessage('targetRow'));
			Validator.showBalloon($trigger, msg);
			return false;
		}
		// 削除可能な行か
		if (eventName === 'deleteLine' && count > 0) {
			let deleteable = true;
			$checked.each(function(i, checkbox) {
				const prefix = checkbox.value;	// 行選択用チェックボックスのvalueは「コンテナのHTML ID＋"-"+行番号」に等しい
				const r = PARTS.firePartsEvent($container, "canDeleteLine", prefix);
				if (!r) {
					const msg = NCI.getMessage('MSG0110', NCI.getMessage('thisRow'));
					Validator.showBalloon($(checkbox), msg);
					deleteable = false;
				}
			});
			if (!deleteable) {
				return false;
			}
		}

		// 画面項目をRuntimeMapへ反映
		PARTS.fillRuntimeMap();

		// 選択パーツの選択行に対してパラメータ生成（追加パラメータがあればそれも追記）
		const params = PAGING_CONTAINER.createParams($trigger);
		for (key in exParams) {
			params[key] = exParams[key];
		}

		// 送信
		return NCI.post('/vd0310/' + eventName, params).done(function(res) {
			$('#editArea').data('runtimeMap', res.runtimeMap);
			const $container = $trigger.closest('article.parts');
			const id = $container.prop('id');
			$container.prop('outerHTML', res.html);
			NCI.ymdPicker($('#' + id).find('input.ymdPicker'));
			NCI.ymPicker($('#' + id).find('input.ymPicker'));

			// 再計算
			// #160342対応 リピーター／グリッド内に計算対象パーツが存在している場合のみ再計算を実行させる
			if (mustCalc && ($('article[data-calculate]', $('#' + id)).length > 0)) {
				PARTS.calculateAll();
			}

			// パーツイベント
			PARTS.firePartsEvent($container, eventName);
		});
	}
}

/** 検索ボタンパーツ */
var MASTER_BUTTON = {
	/** 初期化 */
	init : function() {
		// 検索ボタンパーツのボタン押下
		$(document)
			.on('click', 'button[data-table-search-id][data-ajax-results]', function(ev) {
				const $parts = $(this).closest('article.parts');
				const result = PARTS.firePartsEvent($parts, 'beforeSelect');
				if (result) {
					MASTER_BUTTON.openPopup($(this), ev);
				}
			})
			.on('click', 'button.btnClear[data-ajax-results]', function(ev) {
				const $parts = $(this).closest('article.parts');
				const result = PARTS.firePartsEvent($parts, 'beforeClear');
				if (result) {
					MASTER_BUTTON.clearValues($(this), ev);
					PARTS.firePartsEvent($parts, 'afterClear')
				}
			});
	},

	/** TI0000 汎用テーブル検索ポップアップを開く */
	openPopup : function($button, ev) {
		if (PARTS.validateParts(false)) {
			// 関連パーツからI/O区分＝IN（絞込条件）となっているものをパラメータとして、汎用マスタ検索ポップアップを起動
			// RuntimeMapよりパーツを取得しパラメータを生成
			const parts = $('#editArea').data('runtimeMap')[$button.closest('article.parts').prop('id')];
			const params = MASTER_BUTTON.createParams(parts)
			params.htmlId = $button.parent().prop('id');
			Popup.open('../ti/ti0000.html', MASTER_BUTTON.callback, params);
		}
	},

	/**
	 * 汎用テーブル検索パラメータを生成
	 * @param parts 検索ボタン／マスタ選択パーツ(RuntimeMapより取得)
	 */
	createParams : function(parts) {
		// パーツから検索条件を取得し、絞込み条件を生成
		let initConditions = {};
		$.each(parts.conditions, function(i, r) {
			// 同一の親HTML_IDでパーツIDが同じエレメントの値を抜き出す
			let val = PARTS.getPartsValue(r.targetHtmlId);
			initConditions[r.columnName] = val;
		});
		return {
			"tableSearchId" : parts.tableSearchId,
			"initConditions" : initConditions
		};
	},

	/** TI0000 汎用テーブル検索ポップアップからのコールバック */
	callback : function(entity) {
		if (entity) {
			// 選択内容をパーツへ反映
			// RuntimeMapから自パーツを取得し、検索結果パーツリストを取得
			const $parts = $('#' + entity.htmlId);
			const $button = $parts.find('>button[data-table-search-id]');
			const outputs = $('#editArea').data('runtimeMap')[entity.htmlId].results;
			MASTER_SELECTOR.destributeValues(entity, entity.htmlId, outputs).done(function() {
				if (console) console.log('callback from TI0000 END ----------');
				PARTS.firePartsEvent($parts, 'afterSelect')
			});
		}
	},

	/** 値のクリア */
	clearValues : function($button, ev) {
		// 空エンティティを配布してクリア処理とする
		let htmlId = $button.closest('article.parts').prop('id');
		let outputs = $('#editArea').data('runtimeMap')[htmlId].results;
		MASTER_SELECTOR.destributeValues({}, htmlId, outputs);
	}
};

/** マスタ選択パーツ */
var MASTER_SELECTOR = {
	init : function() {
		$(document)
			// マスタ選択のトリガーパーツ変更時（チェックボックス／ラジオ以外）
			.on('change', '[data-ajax-triggers]:not([type=checkbox]):not([type=radio])', function(ev) {
				let htmlId = $(this).closest('article.parts').prop('id');
				let trigger = $('#editArea').data('runtimeMap')[htmlId];
				MASTER_SELECTOR.handleTrigger(trigger).done(function() {
					if (console) console.log(NCI.format('マスタ選択のトリガーパーツ #{0}の変更 END ---------', htmlId));
				});
			})
			// マスタ選択のトリガーパーツ変更時（チェックボックス／ラジオ）
			.on('click', '[data-ajax-triggers][type=checkbox], [data-ajax-triggers][type=radio]', function(ev) {
				let htmlId = $(this).closest('article.parts').prop('id');
				let trigger = $('#editArea').data('runtimeMap')[htmlId];
				MASTER_SELECTOR.handleTrigger(trigger).done(function() {
					if (console) console.log(NCI.format('マスタ選択のトリガーパーツ #{0}の変更 END ---------', htmlId));
				});
			})
			// マスタ選択パーツの変更時
			.on('change', 'select[data-table-search-id]', function() {
				const $parts = $(this).closest('article.parts');
				const htmlId = $parts.prop('id');
				const parts = $('#editArea').data('runtimeMap')[htmlId];
				MASTER_SELECTOR.handleSelected(parts, $(this)).done(function() {
					if (console) console.log(NCI.format('マスタ選択パーツ #{0}の変更 END ---------', htmlId));
					PARTS.firePartsEvent($parts, 'afterSelect');
				});
			})
	},

	/**
	 * マスタ選択のトリガーパーツの変更イベントにより、マスタ選択の選択肢を再生成する
	 * @param trigger トリガーパーツ(RuntimeMapより取得)
	 */
	handleTrigger : function(trigger) {
		let triggerHtmlId = trigger.htmlId;

		// トリガーパーツの変更によりマスタ選択パーツの選択肢が変更となったので、選択肢を再生成する
		// このときトリガーされるマスタ選択パーツは複数の可能性があるので、これをマルチスレッドで実行
		let ids = trigger.ajaxTriggers;
		let threads = [];
		$.each(ids, function(i, masterHtmlId) {
			let $select = $('#' + masterHtmlId).find('select[data-role]');
			let parts   = $('#editArea').data('runtimeMap')[masterHtmlId];
			threads[i] = MASTER_SELECTOR.rebuildOptions(parts, $select, triggerHtmlId);
		});
		return $.when.apply(null, threads);
	},

	/**
	 * マスタ選択パーツの選択肢を再生成する
	 * @param parts マスタ選択パーツ(RuntimeMapより取得)
	 * @param $select レンダリングされてあるマスタ選択パーツ自身
	 * @param triggerHtmlId トリガーパーツのHtmlId
	 */
	rebuildOptions : function(parts, $select, triggerHtmlId) {
		const runtimeMap = $('#editArea').data('runtimeMap');
		const masterHtmlId = parts.htmlId;
		const oldValue = ($select && $select.length > 0) ? $select.val() : parts.values[parts.defaultRoleCode];

		const params = MASTER_BUTTON.createParams(parts);
		params.columnNameValue = parts.columnNameValue;
		params.columnNameLabel = parts.columnNameLabel;

		const defer = $.Deferred();
		NCI.post('/vd0310/getMasterOptionItem', params).done(function(items) {
			// 空行の仕様
			const USE_ALWAYS = 1, USE_MULTI = 2;
			const useEmptyLine = (parts.emptyLineType === USE_ALWAYS || (parts.emptyLineType === USE_MULTI && (items.length == 0 || items.length > 1)));
			if (useEmptyLine)
				items.unshift({ "value" : "", "label" : "--" });
			// レンダリングされてあるパーツであれば選択肢を書き換える
			if ($select && $select.length > 0) {
				NCI.createOptionTags($select, items);

				// 選択肢の変更により未選択状態になら、空行を選択状態にする
				const select = $select[0];
				if (select.selectedIndex < 0 && useEmptyLine) {
					select.selectedIndex = 0;	// ブランク行「--」のはず
				}

				// マスタ選択パーツ自身が読取専用なら現在選択されている選択肢以外を非活性かつ非表示にする
				$select.find('option:not(:selected)').prop('disabled', $select.hasClass('readonly')).toggleClass('hide', $select.hasClass('readonly'));

				// 生成された選択肢に対して、パーツ条件設定を再評価する（有効／読取／可視）
				Enabled.evaluateEC(parts, runtimeMap);

				// 選択肢を変更した結果、パーツの値に変化があればイベント起動
				let newValue = $select.val();
				if (oldValue == newValue) {
					if (console)
						console.log(NCI.format('トリガー #{0} の変更により、マスタ選択パーツ #{1} の選択肢を {2}行生成しました。選択値の変更はありませんでしたが、トリガーが変更されたためにばらまき処理を再実施します。', [triggerHtmlId, masterHtmlId, items.length]));
					// マスタ選択パーツ自身に変更が無くてもトリガーパーツに変更があったら、絞込条件が変わる可能性があるのでばら撒き処理が必要だ
					MASTER_SELECTOR.handleSelected(parts, $select);
				}
				else {
					if (console)
						console.log(NCI.format('トリガー #{0} の変更により、マスタ選択パーツ #{1} の選択肢を {2}行生成した結果、選択値が変更されました', [triggerHtmlId, masterHtmlId, items.length]));
					// 変更通知
					$select.change();
				}
				defer.resolve();
			}
			// レンダリングされていないパーツは。。。
			else {
				// 戻り値のitems内にoldValueに該当するものがあるか
				let exists = false;
				$.each(items, function() {
					if (this.value == oldValue) {
						exists = true;
						return false;
					}
				});
				// 該当するものがなければRuntimeMap内のパーツ自身のvaluesをクリア
				// さらに空データを他パーツへ配布
				if (!exists) {
					$.each(parts.values, function(key, value) {
						parts.values[key] = '';
					});
					MASTER_SELECTOR.destributeValues({}, masterHtmlId, parts.results);
				}
				defer.resolve();
			}
		});
		return defer.promise();
	},

	/**  */
	/**
	 * マスタ選択パーツの現在値で検索を行い、その結果を他パーツへ配布
	 * @param parts マスタ選択パーツ(RuntimeMapより取得)
	 * @param select レンダリングされてあるマスタ選択パーツ自身
	 */
	handleSelected : function(parts, $select) {
		let val = parts.values[parts.defaultRoleCode];
		if ($select && $select.length > 0) {
			if ($select[0].selectedIndex < 0) {
				val = '';
			} else {
				val = $select[0].value;		// optionがhideになっていると、$select.val()が null を返すので、elementから直接取得する
			}
		}
		let htmlId = parts.htmlId;
		let defer = new $.Deferred();
		let outputs = parts.results;

		// 配布先パーツがないならこれ以上は不要である
		if (!outputs || outputs.length === 0) {
			defer.resolve();
		}
		else if (val === '' || val === null) {
			// マスタ選択パーツが未選択なら空データを他パーツへ配布
			MASTER_SELECTOR.destributeValues({}, htmlId, outputs).done(function() {
				defer.resolve();
			});
		}
		else {
			// マスタ選択パーツが入力済なら値を絞り込み条件へ反映
			let params = MASTER_BUTTON.createParams(parts);
			params.initConditions[parts.columnNameValue] = val;

			// 検索
			NCI.post('/vd0310/getMasterResult', params).done(function(entities) {
				let entity = {};	// 正しい内容以外はブランクにする
				if (entities.length === 1)
					entity = entities[0];
				else if (entities.length > 1) {
					// 抽出結果が複数件であれば、設計バグである。
					// もし複数抽出を許可してしまえば、同じ選択肢を選んでいても抽出結果の配布内容が保障されなくなってしまう。
					// 「汎用テーブル検索条件」の現行仕様では抽出結果がユニークソートであることを担保出来ない仕様であるため、
					// 複数抽出されると結果が不定である。
					// ⇒プロジェクトの都合でここをコメントアウトしてもよいが、TRUNKへマージする際には元へ戻すこと。
					// ⇒「業務用件としてユニークでなくても問題ない」と「システム的に結果が保障される」とでは100万光年の隔たりがある。
					// ⇒そしてTRUNKでは「システム的に結果が保障」されなければならぬ。
					NCI.addMessage('danger', [NCI.getMessage('MSG0141', entities.length)]);
				}
				// 検索結果を他パーツへ配布
				MASTER_SELECTOR.destributeValues(entity, htmlId, outputs).done(function() {
					defer.resolve();
				});
			});
		}
		return defer.promise();
	},

	/**
	 * 検索結果の他パーツへの配布処理
	 * @param entity 検索結果エンティティ
	 * @param htmlId マスタ選択パーツの htmlId
	 * @param outputs パーツ関連情報のうち、パーツI/O区分＝OUTPUT、つまり検索結果配布するものだけを抜き出したリスト
	 */
	destributeValues : function(entity, htmlId, outputs) {
		// 最初に他パーツへの値の配布をすべて終わらせる
		let changed1 = [];
		// レンダリングされていないパーツの場合、こちらにRuntimeMapのパーツを格納
		let changed2 = [];
		$.each(outputs, function(i, r) {
			let $parts = $('#' + r.targetHtmlId);
			let oldValue = PARTS.getPartsValue(r.targetHtmlId);
			let newValue = entity[r.columnName];
			if (newValue === undefined)
				newValue = '';

			if (oldValue == newValue) {
				if (console)
					console.log(NCI.format('マスタ選択／検索ボタンパーツ #{0} の選択により、パーツ #{1} に {2}="{3}"をセット（同値なのでスキップ）',
							[htmlId, r.targetHtmlId, r.columnName, newValue]));
			}
			else {
				if (console)
					console.log(NCI.format('マスタ選択／検索ボタンパーツ #{0} の選択により、パーツ #{1} に {2}="{3}"をセット',
							[htmlId, r.targetHtmlId, r.columnName, newValue]));

				// パーツエレメントに値をセット
				let $elem = PARTS.setPartsValue(r.targetHtmlId, newValue, true);

				if (!r.noChangeEventFlag) {
					if ($elem && $elem.length) {
						changed1.push($elem);
					} else {
						let parts = $('#editArea').data('runtimeMap')[r.targetHtmlId];
						changed2.push(parts);
					}
				}
			}
		});

		// 他パーツへの値の配布がすべて完了したのちに、一括で変更通知
		// こうしないと複数パーツへの配布をしたときに、パーツには配布が終わってない状態で再検索に行ってしまうケースが出てきてしまう。
		$.each(changed1, function(i, $partsElement) {
			if (/checkbox|radio/.test($partsElement.prop('type'))) {
				$partsElement.triggerHandler('click');	// .click()だと 未選択時に「ラジオボタンの選択」処理が行われてしまうので、jqueryのイベントだけ蹴飛ばす
				// triggerHandlerはイベントのバブリングはしない
				// そのため$partsElementに直接紐づく"click"イベントしか実行してくれない
				// 例)
				//    $partsElement.click(function(){ … })や$partsElement.on('click', function(){ … })は実行される
				//    $(document).on('click', '$partsElementへのセレクタ', function(){ … })は実行されない
				// 結果としてラジオボタンやチェックボックスの有効条件／計算式は実行されないため、個別で呼び出してやることにする
				// ただDMMではどこに影響があるか不明なためコメントアウトしておく
//				$partsElement.trigger('enable');
//				$partsElement.trigger('calculate');
			} else {
				$partsElement.change();
			}
		});
		$.each(changed2, function(i, parts) {
			let value = parts.values[parts.defaultRoleCode];
			// パーツが別パーツの有効条件の判定元になっていれば有効条件処理を呼び出す
			let enabledCondition = parts.enabledCondition;
			if (enabledCondition != null && enabledCondition.targets) {
				Enabled.execute(parts, value, enabledCondition.disabled);
			}
			// パーツが別パーツの計算元になっていれば計算処理を呼び出す
			let calculateCondition = parts.calculateCondition;
			if (calculateCondition != null && (calculateCondition.targets || calculateCondition.ecTargets)) {
				Calculate.execute(parts, value);
			}
			// パーツが別マスタ検索パーツのトリガーになっていればトリガーパーツ変更処理を呼び出す
			let ids = parts.ajaxTriggers;
			if (ids) {
				MASTER_SELECTOR.handleTrigger(parts);
			}
		});

		return $.Deferred().resolve().promise();	// 戻り値を promiseオブジェクトにするためのダミー
	}
}

var ATTACH_FILE = {
	init : function() {
		// 添付ファイル：追加ボタン押下
		$(document).on('click', 'article.parts>div.line-controller>span>button.btnOpenAttachFilePopup', function(ev) {
			const $parts = $(this).closest('article.parts');
			if (PARTS.firePartsEvent($parts, 'beforeSelect')) {
				const params = {
						"htmlId" : $parts[0].id,
						"runtimeMap" : $('#editArea').data('runtimeMap')
				};
				// パーツ添付ファイルのアップロード画面を開く
				Popup.open('./vd0070.html', ATTACH_FILE.callbackAddAttachFile, params);
			}
		})
		// 添付ファイル（複数ファイル）：削除ボタン押下
		.on('click', 'article.parts>div.line-controller>span>.btnDeleteMultiAttachFile', function(ev) {
			const $parts = $(this).closest('article.parts');
			if (PARTS.firePartsEvent($parts, 'beforeClear')) {
				const partsAttachFileWfIds = [];
				$parts.find('>div.line-controller>table.responsive>tbody>tr>th>input[type=checkbox].selectable:checked').each(function(i, elem) {
					// 添付ファイルのチェックボックス値には「ワークフローパーツ添付ファイルID」が設定されている
					partsAttachFileWfIds.push(elem.value);
				});
				// ページングコンテナに相乗りさせてもらう
				const exParams = { "partsAttachFileWfIds" : partsAttachFileWfIds };
				return PAGING_CONTAINER.renderHtml('deletePartsAttachFileWf', this, exParams, true, false).done(function() {
					PARTS.firePartsEvent($parts, 'afterClear');
				});
			}
		})
		// 添付ファイル（単一ファイル）：削除ボタン押下
		.on('click', 'article.parts>div.line-controller>span>.btnDeleteSingleAttachFile', function(ev) {
			const $parts = $(this).closest('article.parts');
			if (PARTS.firePartsEvent($parts, 'beforeClear')) {
				const htmlId = $parts.attr('id');
				const rt = $('#editArea').data('runtimeMap')[htmlId];
				if (rt.rows && rt.rows.length && rt.rows[0].partsAttachFileWfId) {
					// ページングコンテナに相乗りさせてもらう
					const exParams = { "partsAttachFileWfIds" : [ rt.rows[0].partsAttachFileWfId ] };
					return PAGING_CONTAINER.renderHtml('deletePartsAttachFileWf', this, exParams, false, false).done(function() {
						PARTS.firePartsEvent($parts, 'afterClear');
					});
				}
			}
		});
	},

	/** 添付ファイル追加画面からのコールバック */
	callbackAddAttachFile : function(params) {
		if (params && params.htmlId && params.rows && params.rows.length > 0) {
			const $parts = $('#' + params.htmlId);
			const runtimeMap = $('#editArea').data('runtimeMap');
			const rt = runtimeMap[params.htmlId];
			if (rt.multiple)
				rt.rows = rt.rows.concat(params.rows);
			else
				rt.rows = params.rows;

			$.each(rt.rows, function(i, row) {
				row.sortOrder = i + 1;
			});
			// ページングコントローラーに相乗りさせてもらう
			const $trigger = $parts.find('button.btnOpenAttachFilePopup');
			return PAGING_CONTAINER.renderHtml('refreshParts', $trigger[0], null, false, false).done(function() {
				PARTS.firePartsEvent($parts, 'afterSelect', params.rows);	// 追加行のみ。全行ならruntimeMapから取得せよ
			});
		}
	}
}

var IMAGE = {
	init : function() {
		// 画像パーツ：追加ボタン押下
		$(document).on('click', 'article.parts .btnUploadImage', function(ev) {
			const $parts = $(this).closest('article.parts');
			if (PARTS.firePartsEvent($parts, 'beforeSelect')) {
				const params = {
						"htmlId" : $parts[0].id,
						"runtimeMap" : $('#editArea').data('runtimeMap')
				};
				// パーツ添付ファイルのアップロード画面を開く
				Popup.open('./vd0080.html', IMAGE.callbackUploadImage, params);
			}
			return false;
		})
		// 画像パーツ：削除ボタン押下
		.on('click', 'article.parts .btnClearImage', function(ev) {
			const $parts = $(this).closest('article.parts');
			if (PARTS.firePartsEvent($parts, 'beforeClear')) {
				const runtimeMap = $('#editArea').data('runtimeMap');
				const rt = runtimeMap[$parts[0].id];
				Enabled.clear(rt);

				PARTS.firePartsEvent($parts, 'afterClear');
			}
			return false;
		});
	},

	/** 画像追加画面からのコールバック */
	callbackUploadImage : function(params) {
		if (params && params.htmlId && params.rows && params.rows.length > 0) {
			const $parts = $('#' + params.htmlId);
			const runtimeMap = $('#editArea').data('runtimeMap');
			const rt = runtimeMap[params.htmlId];
			rt.partsAttachFileWfId = params.rows[0].partsAttachFileWfId;

			$('#' + params.htmlId).find('img').attr('src',
					'../../endpoint/vd0310/download/partsAttachFileWf' +
							'?partsAttachFileWfId=' + rt.partsAttachFileWfId +
							'&t=' + new Date().getTime());

			PARTS.firePartsEvent($parts, 'afterSelect', params.rows[0]);
		}
	}
}

/** 独立画面パーツ */
var STAND_ALONE = {
	/** 初期化 */
	init : function() {
		$(document).on('click', 'article.parts > .btn-stand-alone-screen', STAND_ALONE.popup);
	},

	/** 独立画面パーツをポップアップ画面として起動 */
	popup : function(ev) {
		// 既存のバルーンを消す
		const $editArea = $('#editArea');
		Validator.hideBalloon($editArea.find('.has-error'));

		// バリデーション
		const $targets = $editArea.find('input, select, textarea');
		if (!Validator.validate($targets, false)) {
			return false;
		}

		// 入力データの吸い上げ
		PARTS.fillRuntimeMap();

		// イベント起動
		const $parts = $(this).closest('article.parts');
		const result = PARTS.firePartsEvent($parts, 'openPopup');
		if (result) {
			// 独立画面をポップアップとして起動
			const params = {
				'htmlId' : $parts.attr('id'),
				'contents' : vd0310.contents,
				'previewContext' : $editArea.data('previewContext'),	// プレビュー画面でしか値が設定されない（申請画面だと必ずnull）
				'runtimeMap' : $editArea.data('runtimeMap')
			};
			Popup.open('../vd/vd0320.html', STAND_ALONE.callback, params, $parts);
		}
		return result;
	},

	/** 独立画面ポップアップを閉じたときのコールバック関数 */
	callback : function(params, $parts) {
		const result = PARTS.firePartsEvent($parts, 'beforeClosePopup', params);
		if (result) {
			if (params) {
				if (params.runtimeMap) {
					$('#editArea').data('runtimeMap', params.runtimeMap);
				}
				// 強制再描画
				if (params.redraw) {
					PARTS.redraw(true);
				}
			}
		}
		return PARTS.firePartsEvent($parts, 'afterClosePopup', params);
	}
};
