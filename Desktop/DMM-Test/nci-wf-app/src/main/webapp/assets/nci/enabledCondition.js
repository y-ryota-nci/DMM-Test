$(function() {
	//--------------------------------------------------------------
	// カスタムイベント「enable」の定義
	//--------------------------------------------------------------
	$(document).on('enable', 'input,textarea,select', function() {
		let $self = $(this);
		let $article = $self.closest('article.parts[id]');
		if ($article.length == 0) return;
		let htmlId = $article.prop('id');
		let triggerParts = $('#editArea').data('runtimeMap')[htmlId];
		// 条件判定実行
		Enabled.execute(triggerParts);
	})
	/**
	 * blur時の条件判定
	 */
	.on('change', 'input:not([type=checkbox]):not([type=radio]),textarea,select', function(e){
		$(this).trigger('enable');	// ラジオ／チェックボックスは click で拾う
	})
	.on('click', 'input[type=checkbox], input[type=radio]', function(e){
		$(this).trigger('enable');	// ラジオ／チェックボックスはchangeイベントだとトリガーしないので click で拾う。
	})
	.on('keydown', 'article.parts[data-ec] > [data-role]', function(ev) {
		if (ev.key === 'Tab')
			$(this).trigger('enable');	// タブキー押下されたらフォーカスが外れるのが自明なので、先に有効条件判定を行う
	});
});

var Enabled = {
	target : null,
	disabledCaches : [],
	readonlyCaches : [],

	/**
	 * トリガーパーツの値変更により「影響を受ける対象パーツ」の条件判定処理を行い、
	 * 有効無効／読取専用／表示可否を再判定する
	 * （Enabled.evaluateEC()は対象パーツを引数に取るが、こちらはトリガーパーツを引数に取る）
	 * @param triggerParts 条件判定元パーツ（トリガーパーツ）
	 */
	execute : function(triggerParts) {
		let htmlId = triggerParts.htmlId;
		// 無限ループ防止
		if (Enabled.target == null) {
			Enabled.target = htmlId;
		} else if (Enabled.target == htmlId) {
			return;
		}

		// 条件判定元パーツの入力値を取得
		let val = PARTS.getPartsValue(htmlId);

		let runtimeMap = $('#editArea').data('runtimeMap');

		let triggerPartsId = triggerParts.partsId;
		// トリガーとなったパーツのパーツ条件[evaluateCondition]の値を、Objectとして取得
		let triggerCondition = triggerParts.evaluateCondition;

		// パーツ条件の判定先パーツが設定されてあれば処理実行
		if (triggerCondition && triggerCondition.targets) {
			// 判定先(条件により有効／無効・可視・読取専用が切り替わる)パーツを特定するためのセレクタ
			// 判定を行う対象となるパーツは自分と同じもしくは下位コンテナ内にいる
			// よってターゲットのパーツは同一のプレフィックスを持っているはず
			let prefix = '';
			let pos = htmlId.lastIndexOf('_');
			if (pos > -1) {
				prefix = htmlId.substring(0, pos + 1);
			}
			for (let i = 0; i < triggerCondition.targets.length; i++) {
				// 条件判定の対象となるパーツを特定
				let targetPartsList = Enabled.getTargetPartsList(triggerCondition.targets[i], prefix, runtimeMap);
				// 対象パーツ毎に判定処理を実行
				$.each(targetPartsList, function(i, targetParts) {
					Enabled.evaluateEC(targetParts, runtimeMap);
				});
			}
		}

		// 処理が終われば初期化
		if (Enabled.target == htmlId) {
			Enabled.target = null;
			Enabled.disabledCaches = [];
			Enabled.readonlyCaches = [];
		}
	},

	/**
	 * 対象パーツの条件判定処理を行い、有効無効／読取専用／表示可否を再判定する。
	 * （Enabled.execute()はトリガーパーツを引数に取るが、こちらは対象パーツを引数に取る）
	 * @param targetParts 条件判定先パーツ（対象パーツ）
	 */
	evaluateEC : function(targetParts, runtimeMap) {
		if (targetParts.evaluateCondition != null) {
			let targetHtmlId = targetParts.htmlId;
			// 条件式のJSONオブジェクトを取得
			let evaluateCondition = targetParts.evaluateCondition;
			let conditions = evaluateCondition.conditions;
			// 現在の条件の判定結果を取得しておく
			const currentEnabled  = evaluateCondition.enabled;
			const currentVisibled = evaluateCondition.visibled;
			const currentReadonly = evaluateCondition.readonly;

			// 各条件式の判定元パーツ項目にパーツの入力値にセット
			Enabled.setPartsValue(targetHtmlId, conditions, runtimeMap);

			// 計算対象パーツの表示条件が"0:未定義"または"1:入力可"の場合だけ条件判定処理は行なう
			if (targetParts.dcType == DcType.UKNOWN || targetParts.dcType == DcType.INPUTABLE) {
				// 判定元パーツの親パーツの状態(有効／無効と読取専用)を取得
				let parentDisabled = Enabled.isParentDisabled(targetHtmlId);
				let parentReadOnly = Enabled.isParentReadOnly(targetHtmlId);

				// 条件の判定結果を取得
				// パーツ条件区分をキーにした連想配列にて結果が戻る
				const results = Enabled.getEvaluateResult(conditions);

				// 判定結果に応じて有効条件、可視条件、読取専用条件の結果を反映させる
				// 有効条件    :親パーツが無効であれば判定処理に関わらず無効とする
				// 可視条件    :判定結果を使用
				// 読取専用条件:親パーツが読取専用であれば判定処理に関わらず読取専用とする
				evaluateCondition.enabled  = !parentDisabled && results[PartsConditionType.ENABLED];
				evaluateCondition.visibled = results[PartsConditionType.VISIBLED];
				evaluateCondition.readonly = parentReadOnly || results[PartsConditionType.READONLY];

				// 結果を反映したのでパーツの制御処理を行う
				Enabled.ctrlParts(targetParts, conditions, evaluateCondition.enabled, evaluateCondition.visibled, evaluateCondition.readonly);

				// 有効条件の判定結果がfalse(無効)の場合、$target配下にある要素で、かつ属性[data-role]をもつ要素の入力値をクリア
				// またRuntimeMapにあるparts自身が保持しているvaluesの一緒にクリア
				if (!evaluateCondition.enabled) {
					Enabled.clearValue(targetHtmlId, evaluateCondition.enabled);
				}
				// 有効判定処理を行い"無効→有効"となった場合、計算処理を実行(＝有効条件の判定処理を行っていなければ計算処理はしない）
				$.each(conditions, function(idx, condition) {
					if (condition.partsConditionType == PartsConditionType.ENABLED) {
						if (evaluateCondition.enabled && currentEnabled != evaluateCondition.enabled)
							Calculate.executeCalc(targetParts, runtimeMap);
					}
				});
			}

			// 最後にコールバック関数が設定されてあれば呼び出す
			Enabled.callback(targetParts, evaluateCondition, conditions);
		}
	},

	/**
	 * パーツ条件の判定元からみた条件判定処理対象のパーツの特定.
	 * @param partsId 条件判定を行う対象パーツのパーツID
	 * @param prefix 条件判定を行う判定元パーツのHTMLIDの接頭語
	 * @param runtimeMap
	 * @return
	 */
	getTargetPartsList : function(partsId, prefix, runtimeMap) {
		if (!runtimeMap)
			throw new Error("runtimeMapがnullです");

		let targets = [];
		$.each(runtimeMap, function(k, v) {
			if (v.partsId == partsId && (prefix == '' || k.indexOf(prefix) > -1)) {
				targets.push(v);
			}
		});
		return targets;
	},

	/**
	 * 条件式に渡す入力値に対して判定元パーツの入力値を反映.
	 * @param targetHtmlId 条件判定を行うパーツのHtmlId
	 * @param conditions 条件一覧
	 * @param triggerPartsId 判定元パーツのパーツID
	 * @param val 判定元パーツの入力値
	 * @param runtimeMap
	 * @return
	 */
	setPartsValue : function(targetHtmlId, conditions, runtimeMap) {
		$.each(conditions, function(idx, obj) {
			if (obj.values) {
				for (let partsId in obj.values) {
					obj.values[partsId] = Enabled.getTriggerPartsValue(targetHtmlId, partsId, runtimeMap);
				}
			}
		});
	},

	/**
	 * 条件判定を行う対象パーツの条件式に使用するパーツの入力値を取得.
	 * @param targetHtmlId 条件判定を行うパーツのHtmlId
	 * @param partsId 条件判定元パーツのパーツID
	 * @param runtimeMap
	 * @return 条件判定元パーツの入力値（レンダリングされていない場合はruntimeMapが持つvalue）
	 */
	getTriggerPartsValue : function(targetHtmlId, partsId, runtimeMap) {
		if (!runtimeMap)
			throw new Error("runtimeMapがnullです");

		let triggerHtmlId = null;
		for (let htmlId in runtimeMap) {
			if (runtimeMap[htmlId].partsId == partsId) {
				if (htmlId.indexOf('_') > -1) {
					let prefix = htmlId.substring(0, htmlId.lastIndexOf('_') + 1);
					if (targetHtmlId.indexOf(prefix) > -1) {
						triggerHtmlId = htmlId;
						break;
					}
				} else {
					triggerHtmlId = htmlId;
					break;
				}
			}
		}
		if (triggerHtmlId != null) {
			return PARTS.getPartsValue(triggerHtmlId);
		}
		return null;
	},

	/**
	 * 有効条件、可視条件、読取専用条件の各種判定を行い結果を戻す
	 * @param conditions 条件一覧
	 * @return 条件の判定結果の連想配列（有効条件の判定結果、可視条件の判定結果、読取専用条件の判定結果）
	 */
	getEvaluateResult : function(conditions) {
		// 初期値（有効条件：true(有効)、可視条件：true(表示)、読取専用条件：false(入力可)）
		const results = {};
		results[PartsConditionType.ENABLED]  = true;
		results[PartsConditionType.VISIBLED] = true;
		results[PartsConditionType.READONLY] = false;
		// 各条件を判定
		$.each(conditions, function(idx, obj) {
			results[obj.partsConditionType] = Evaluate.execute(obj.formula, obj.values);
		});
		return results;
	},

	/**
	 * 子パーツ制御.
	 * @param targetParts (RuntimeMapから取り出した)パーツ情報
	 * @param conditions 条件一覧
	 * @param enabled 有効条件 trueなら有効
	 * @param visibled 可視条件 trueなら表示
	 * @param readonly 読取専用条件 trueなら読取専用
	 */
	ctrlParts : function(targetParts, conditions, enabled, visibled, readonly) {
		if (!targetParts) return;

		let $target = $('#' + targetParts.htmlId);
		// 有効条件、可視条件、読取専用条件の判定結果に応じた制御処理を実行
		Enabled.setEnabled(targetParts, $target, enabled);
		Enabled.setVisibled(targetParts, $target, visibled);
		Enabled.setReadOnly(targetParts, $target, readonly, enabled);

		// パーツが変数"rows"を持っている(＝リピータパーツや子画面パーツ＝親パーツ)場合、配下にあるパーツも制御対象とする
		// なお配下にある子パーツ自身に条件が設定されてあれば、親子それぞれの判定結果をマージしたもので制御を行う
		// 例えば有効条件の場合、親パーツ自身が「有効」でも子パーツが「無効」であれば子パーツは「無効」となる
		if (targetParts.rows) {
			let runtimeMap = $('#editArea').data('runtimeMap');
			let childParts = null;
			$.each(targetParts.rows, function(i, row) {
				$.each(row.children, function(j, childHtmlId) {
					childParts = runtimeMap[childHtmlId];
					if (childParts) {
						let childEnabled  = enabled;
						let childVisibled = visibled;
						let childReadOnly = readonly;

						// 子パーツ自身に条件が設定されてあれば条件の判定処理を行う
						let childEc = childParts.evaluateCondition;
						if (childEc) {
							if (childEc.conditions) {
								// 子パーツの条件判定処理
								const childResults = Enabled.getEvaluateResult(childEc.conditions);
								// 有効条件、読取専用条件に関しては親子の判定結果をマージ
								// 可視条件はパーツ自身の結果のみで判定
								childEnabled  = enabled && childResults[PartsConditionType.ENABLED];
								childVisibled = childResults[PartsConditionType.VISIBLED];
								childReadOnly = readonly || childResults[PartsConditionType.READONLY];
							}
							childEc.enabled  = childEnabled;
							childEc.visibled = childVisibled;
							childEc.readonly = childReadOnly;
						}
						// 制御処理実行
						Enabled.ctrlParts(childParts, childEc.conditions, childEnabled, childVisibled, childReadOnly);
					}
				});
			});
		}
	},

	/**
	 * 有効条件の判定結果に応じたパーツの有効／無効の設定処理.
	 * @param targetParts (RuntimeMapから取り出した)パーツ情報 特に使用していない
	 * @param $target パーツのarticle テキストボックスやラジオボタン本体ではないので注意
	 * @param enabled trueなら有効
	 */
	setEnabled : function(targetParts, $target, enabled) {
		// 無効となった場合、パーツ自身が読取専用だろうと問答無用で非活性にする
		if (!enabled) {
			// $target配下にある要素で、かつ属性[data-ec-target]をもつものが制御対象
			$target.find('[data-ec-target]').prop('disabled', !enabled);
		}
		// 有効となった場合、パーツによってはパーツ自身の読取専用により非活性となっているものもある
		// それらのパーツは有効となったからといってdisabled属性をはずしてはいけない
		else {
			switch (targetParts.partsType) {
			case PartsType.CHECKBOX:
			case PartsType.RADIO:
				// チェックボックスとラジオボタンはパーツ自身が読取(＝class属性でreadonlyが付与)となっている場合
				// 有効条件により"有効"となった場合でもdisabled属性は外さないようにする
				$target.find('[data-ec-target]:not(.readonly)').prop('disabled', !enabled);
				break;
			default:
				$target.find('[data-ec-target]').prop('disabled', !enabled);
				break;
			}
		}
		// また有効／無効で$targetに対して"inputable"のClass属性を付与／削除する
		$target.toggleClass('inputable', enabled);
	},

	/**
	 * 可視条件の判定結果に応じたパーツの表示／非表示の設定処理.
	 * @param targetParts (RuntimeMapから取り出した)パーツ情報 特に使用していない
	 * @param $target パーツのarticle テキストボックスやラジオボタン本体ではないので注意
	 * @param visibled trueなら表示
	 */
	setVisibled : function(targetParts, $target, visibled) {
		$target.toggleClass('hide', !visibled);
	},

	/**
	 * 読取専用条件の判定結果に応じたパーツの入力／読取の設定処理.
	 * なおパーツによって処理内容が異なる
	 * @param targetParts (RuntimeMapから取り出した)パーツ情報
	 * @param $target パーツのarticle テキストボックスやラジオボタン本体ではないので注意
	 * @param readonly trueなら読取専用
	 * @param enabled trueなら有効
	 */
	setReadOnly : function(targetParts, $target, readonly, enabled) {
		switch (targetParts.partsType) {
		case PartsType.TEXTBOX:
			$target.find('[data-ec-target]:not(.readonly)').prop('readonly', readonly);
			break;
		case PartsType.CHECKBOX:
			if (enabled)
				$target.find('[data-ec-target]:not(.readonly)').prop('disabled', readonly);
			break;
		case PartsType.RADIO:
			// 選択されているElement以外は非活性にする
			if (enabled)
				$target.find('[data-ec-target]:not(:checked):not(.readonly)').prop('disabled', readonly);
			break;
		case PartsType.DROPDOWN:
		case PartsType.MASTER:
			// 選択中の項目(option)以外は非活性かつ非表示
			$target.find('[data-ec-target]:not(.readonly)').toggleClass('ecReadonly', readonly);
			$target.find('[data-ec-target]:not(.readonly)>option:not(:selected)').prop('disabled', readonly).toggleClass('hide', readonly);
			break;
		case PartsType.USER:
		case PartsType.ORGANIZATION:
		case PartsType.SEARCH_BUTTON:
			if (enabled)
				$target.find('[data-ec-target]:not(.readonly)').prop('disabled', readonly);
			break;
		case PartsType.IMAGE:
		case PartsType.ATTACHFILE:
			if (enabled)
				$target.find('[data-ec-target]:not(.readonly)').prop('disabled', readonly);
			break;
		case PartsType.GRID:
		case PartsType.REPEATER:
			if (enabled)
				$target.find('div.line-controller>[data-ec-target]:not(.readonly)').prop('disabled', readonly);
			break;
		default:
			break;
		};
	},

	/**
	 * 各条件毎に設定されたコールバック関数の呼出し.
	 * @param targetParts 条件判定を行ったパーツ（runtimeMapから取得したパーツ情報
	 * @param evaluateCondition 条件結果
	 * @param conditions 条件一覧
	 */
	callback : function(targetParts, evaluateCondition, conditions) {
		$.each(conditions, function(idx, obj) {
			let result = null;
			switch (obj.partsConditionType) {
			case PartsConditionType.ENABLED:
				result = evaluateCondition.enabled;
				break;
			case PartsConditionType.VISIBLED:
				result = evaluateCondition.visibled;
				break;
			case PartsConditionType.READONLY:
				result = evaluateCondition.readonly;
				break;
			};
			PARTS.execFunction(obj.callbackFunction, targetParts, result);
		});
	},

	/**
	 * 有効条件判定結果に伴う対象パーツの入力値のクリア処理.
	 *
	 * @param targetHtmlId 対象パーツのHtmlId
	 * @param result 判定結果
	 */
	clearValue : function(targetHtmlId, result) {
		let runtimeMap = $('#editArea').data('runtimeMap');
		// 有効条件の対象となったターゲットのHtmlIdを元にRuntimeMapよりパーツを取得する
		let rt = runtimeMap[targetHtmlId];
		// パーツが変数"rows"を持っている(＝リピータパーツや子画面パーツ)場合、配下にあるパーツも制御対象とする
		if (rt && rt.rows) {
			let parts = null;
			$.each(rt.rows, function(i, row) {
				$.each(row.children, function(j, htmlId) {
					parts = runtimeMap[htmlId];
					if (parts) {
						Enabled.clear(parts);
						if (parts.partsType == PartsType.REPEATER || parts.partsType == PartsType.GRID || parts.partsType == PartsType.STAND_ALONE) {
							// リピーター／グリッドパーツは再帰呼出でさらに配下のパーツの処理
							Enabled.clearValue(parts.htmlId, result);
						}
					}
				});
			});
		}
		Enabled.clear(rt);
	},

	/**
	 * 入力値のクリア処理.
	 * パーツを追加したらこちらの処理も合わせて修正してください。
	 * @param rt (RuntimeMapから取り出した)パーツ情報
	 */
	clear : function(rt) {
		let oldValue, $elem = $('#' + rt.htmlId + ' [data-role]');
		switch (rt.partsType) {
		case PartsType.TEXTBOX:
		case PartsType.USER:
		case PartsType.ORGANIZATION:
		case PartsType.DROPDOWN:
		case PartsType.MASTER:
			// runtimeが保持しているパーツのvalues(入力値)をクリア
			$.each(rt.values, function(key, val) {
				rt.values[key] = '';
			});
			// レンダリングされていれば合わせてクリアし、さらにイベントをキック
			if ($elem.length > 0) {
				if ($elem.val() != '')
					$elem.val('').change();
			}
			// レンダリングされていないパーツは「有効条件」や「計算式」のexecuteを直接呼び出す
			else {
				Enabled.execute(rt, rt.values[rt.defaultRoleCode], true);
				Calculate.execute(rt, rt.values[rt.defaultRoleCode]);
				// マスタ検索パーツのトリガーパーツ、またはマスタ検索パーツ自身の場合
				if (rt.ajaxTriggers)
					MASTER_SELECTOR.handleTrigger(rt);
				if (rt.results)
					MASTER_SELECTOR.handleSelected(rt, null);
			}
			break;
		case PartsType.CHECKBOX:
		case PartsType.RADIO:
			// runtimeが保持しているパーツのvalues(入力値)をクリア
			$.each(rt.values, function(key, val) {
				rt.values[key] = '';
			});
			// レンダリングされていれば合わせてクリア
			if ($elem.length > 0) {
				$elem.each(function() {
					if ($(this).prop('checked'))
						$elem.prop('checked', false).triggerHandler('click');	// .click()だと 未選択時に「ラジオボタンの選択」処理が行われてしまうので、jqueryのイベントだけ蹴飛ばす
				});
			}
			// レンダリングされていないパーツは「有効条件」や「計算式」のexecuteを直接呼び出す
			else {
				Enabled.execute(rt, rt.values[rt.defaultRoleCode], true);
				Calculate.execute(rt, rt.values[rt.defaultRoleCode]);
				// マスタ検索パーツのトリガーパーツの場合
				if (rt.ajaxTriggers)
					MASTER_SELECTOR.handleTrigger(rt);
			}
			break;
		case PartsType.ATTACHFILE:
			$('#' + rt.htmlId + '>div.line-controller>table>tbody>tr>td').empty();
			rt.rows.forEach(function(row, i) {
				row = {};
			});
			break;
		case PartsType.IMAGE:
			rt.values = {};
			rt.partsAttachFileWfId = null;
			// プレビュー画像もクリア
			const $img = $('#' + rt.htmlId).find('img');
			if (rt.partsAttachFileId != null) {
				// デザイン時用の画像があればそちらをデフォルト画像とする
				$img.attr('src', '../../endpoint/vd0310/download/partsAttachFile'
						+ '?partsAttachFileId=' + rt.partsAttachFileId
						+ '&t=' + new Date().getTime());
			}
			else if (rt.useDummyIfNoImage) {
				// ダミー画像を使うようになっていれば、それをデフォルト画像とする
				$img.attr('src', '../../assets/nci/images/noImage.png');
			}
			else {
				$img.attr('src', '#');
			}
			break;
		default:
			break;
		};
	},

	isParentDisabled : function(htmlId) {
		// 親コンテナのパーツIDを取得
		let parentHtmlId = htmlId.substring(0, htmlId.lastIndexOf('-'));
		if (parentHtmlId != null && parentHtmlId != '') {
			if (Enabled.disabledCaches[parentHtmlId] == null) {
				let parentEvaluateCondition = $('#editArea').data('runtimeMap')[parentHtmlId].evaluateCondition;
				let disabled = (parentEvaluateCondition ? !parentEvaluateCondition.enabled : false);
				// 再帰呼出でさらに親を判定
				let parentDisabled = Enabled.isParentDisabled(parentHtmlId);
				// 自身と親との判定結果を取得（どちらかでも無効なら無効とする）
				let result = (disabled || parentDisabled);
				// キャッシュにセット
				Enabled.disabledCaches[parentHtmlId] = result;
			}
			return Enabled.disabledCaches[parentHtmlId];
		}
		return false;
	},

	isParentReadOnly : function(htmlId) {
		// 親コンテナのパーツIDを取得
		let parentHtmlId = htmlId.substring(0, htmlId.lastIndexOf('-'));
		if (parentHtmlId != null && parentHtmlId != '') {
			if (Enabled.readonlyCaches[parentHtmlId] == null) {
				let parentEvaluateCondition = $('#editArea').data('runtimeMap')[parentHtmlId].evaluateCondition;
				let readonly = (parentEvaluateCondition ? parentEvaluateCondition.readonly : false);
				// 再帰呼出でさらに親を判定
				let parentReadOnly = Enabled.isParentReadOnly(parentHtmlId);
				// 自身と親との判定結果を取得（どちらかでも読取専用なら読取専用とする）
				let result = (readonly || parentReadOnly);
				// キャッシュにセット
				Enabled.readonlyCaches[parentHtmlId] = result;
			}
			return Enabled.readonlyCaches[parentHtmlId];
		}
		return false;
	}
};