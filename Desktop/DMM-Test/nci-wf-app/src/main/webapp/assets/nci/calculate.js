$(function() {
	//--------------------------------------------------------------
	// カスタムイベント「enable」の定義
	//--------------------------------------------------------------
	$(document).on('calculate', 'input, select', function(e) {
		let $self = $(e.target);
		let $article = $self.closest('article.parts[id]');
		if ($article.length == 0) return;
		// 計算元パーツのHtmlIdからトリガーパーツを取得
		let htmlId = $article.prop('id');
		let triggerParts = $('#editArea').data('runtimeMap')[htmlId];
		// 計算処理実行
		Calculate.execute(triggerParts);
	})
	/**
	 * パーツ変更時の有効条件判定
	 */
	.on('change', 'article[data-calculate]>input, article[data-calculate]>select', function(e){
		$(e.target).trigger('calculate');
	})
	.on('click', 'article[data-calculate]>label>input[type=checkbox], article[data-calculate]>div>label>input[type=radio]', function(e) {
		$(e.target).trigger('calculate');	// checkboxはON/OFFの切り替えは changeではなく clickでトリガーするので。
	});
});

var Calculate = {

	/** トリガーとなったパーツのHtmlId（無限ループ防止に使用） */
	trigger : null,

	/**
	 * トリガーパーツの値変更により「影響を受ける対象パーツ」の計算処理を行う。
	 * （Calculate.executeCalc()は対象パーツを引数に取るが、こちらはトリガーパーツを引数に取る）
	 * @param triggerParts 計算式／計算条件の判定元パーツ（トリガーパーツ）
	 */
	execute : function(triggerParts) {
		let triggerHtmlId = triggerParts.htmlId;
		if (console && console.info) {
			console.info("Calculate.execute() -> " + triggerHtmlId);
		}
		// 無限ループ防止としてトリガーとなったパーツのHtmlIdを記録しておく
		if (!Calculate.trigger) {
			Calculate.trigger = triggerHtmlId;
		}

		let runtimeMap = $('#editArea').data('runtimeMap');
		// 計算式[calculateCondition]の値を、Objectとして取得
		let condition = triggerParts.calculateCondition;

		// 計算対象パーツ一覧(Key:HtmlId, Value:Runtimeパーツ)
		let targetPatsList = {};

		// トリガーパーツが計算条件の判定元だった場合
		if (condition && condition.ecTargets) {
			// 判定先(計算条件により有効／無効が切り替わる)パーツを特定するためのセレクタ
			// 判定を行う対象となるパーツは自分と同じもしくは下位コンテナ内にいる
			// よってターゲットのパーツは同一のプレフィックスを持っているはず
			let pos = triggerHtmlId.lastIndexOf('_');
			let prefix = (pos > -1) ? triggerHtmlId.substring(0, pos + 1) : '';
			// 計算対象となるパーツを特定
			let len = condition.ecTargets.length;
			for (let i = 0; i < len; i++) {
				let list = Calculate.getEcTargetPartsList(condition.ecTargets[i], prefix, runtimeMap);
				$.each(list, function() {
					targetPatsList[this.htmlId] = this;
				});
			}
		}

		// トリガーパーツが計算式の項目だった場合
		if (condition && condition.targets) {
			for (var i = 0; i < condition.targets.length; i++) {
				// 計算対象パーツを取得
				let target = Calculate.getTargetParts(condition.targets[i], triggerHtmlId);
				if (target != null) {
					targetPatsList[target.htmlId] = target;
				}
			}
		}

		// 計算対象のパーツ一覧を集め終わったので計算処理を実行
		for (targetHtmlId in targetPatsList) {
			// 無限ループ防止
			// トリガーとなったパーツが計算対象の場合は処理しない
			if (Calculate.trigger == targetHtmlId) {
				continue;
			}
			let targetParts = targetPatsList[targetHtmlId];
			Calculate.executeCalc(targetParts, runtimeMap);
		}

		// 処理が終われば初期化
		if (Calculate.trigger == triggerHtmlId) {
			Calculate.trigger = null;
		}
	},

	/**
	 * 対象パーツの計算処理を行う。
	 * （Calculate.execute()は(計算式／計算条件の)トリガーパーツを引数に取るが、こちらは計算先の対象パーツを引数に取る）
	 * @param targetParts 計算先パーツ（対象パーツ）
	 * @param runtimeMap
	 */
	executeCalc : function(targetParts, runtimeMap) {
		// 計算対象パーツの表示条件が"0:未定義"または"1:入力可"以外の場合、計算処理は行わない
		// 計算対象パーツの有効条件が"無効"となっている場合、計算処理は行わない
		if (!Calculate.isExecCalculate(targetParts)) {
			return;
		}
		if (targetParts.calculateCondition != null) {
			let targetHtmlId = targetParts.htmlId;
			// 計算式のJSONオブジェクトを取得
			let calculateCondition = targetParts.calculateCondition;
			let calculates = calculateCondition.calculates;
			// 計算対象パーツのHtmlIdからプレフィックスを取得
			let pos = targetHtmlId.lastIndexOf('_');
			let prefix = (pos > -1) ? targetHtmlId.substring(0, pos + 1) : '';

			// 計算式および計算条件式に使用するパーツの入力値をセット
			$.each(calculates, function(idx, obj) {
				if (obj.values) {
					for (let partsId in obj.values) {
						obj.values[partsId] = Calculate.getTriggerPartsValue(partsId, prefix, runtimeMap);
					}
				}
				if (obj.ecValues) {
					for (let partsId in obj.ecValues) {
						obj.ecValues[partsId] = Enabled.getTriggerPartsValue(targetHtmlId, partsId, runtimeMap);
					}
				}
			});

			// 計算条件の判定結果によって使用される計算式を特定
			// 条件式がある場合、条件式の評価を行い、有効であればその計算式を使う
			let calculate = null;
			$.each(calculates, function(idx, obj) {
				if (obj.ecFormula) {
					if ( Evaluate.execute(obj.ecFormula, obj.ecValues) ) {
						calculate = obj;
						return false;
					}
				} else {
					calculate = obj;
					return false;
				}
			});
			// 計算結果
			// 有効な計算式が見つからなければ計算処理は行わない
			if (calculate != null) {
				let formula = calculate.formula;
				let values = calculate.values;
				// 計算実行
				let result = Calculate.calculate(formula, values);
				if (result == null || !isFinite(result) || isNaN(result)) {
					result = '';
				}
				// 計算結果を反映
				// レンダリングされていればパーツ自身に値を設定
				let $target = $('#' + targetParts.htmlId + '>input[data-role]');
				if ($target.length > 0) {
					const oldValue = $target.val();
					$target.val(result);
					NCI.fillFormat( $target );
					const newValue = $target.val();
					if (newValue !== oldValue)
						$target.change();
				}
				// レンダリングされていないものはRuntimeMapに設定
				else {
					// 計算先パーツが別のパーツの条件の判定元(＝トリガー)になっていれば条件判定処理を呼び出す
					let enabledCondition = targetParts.enabledCondition;
					if (enabledCondition && enabledCondition.targets) {
						Enabled.execute(targetParts);
					}
					// 計算先パーツが別のパーツの計算式や計算条件の判定元(＝トリガー)になっていれば計算処理を呼び出す
					if (calculateCondition.targets || calculateCondition.ecTargets) {
						Calculate.execute(targetParts);
					}
					// 計算先パーツがマスタ検索パーツのトリガーパーツになっていればトリガーパーツ変更処理を呼び出す
					if (targetParts.ajaxTriggers) {
						MASTER_SELECTOR.handleTrigger(targetParts);
					}
				}

				// 最後にコールバック関数を呼び出す
				PARTS.execFunction(calculate.callbackFunction, targetParts);
			}
		}
	},

	isExecCalculate : function(parts) {
		let execute = true;
		// 計算対象パーツの表示条件が"0:未定義"または"1:入力可"以外の場合、計算処理は行わない
		if (parts.dcType !== DcType.UKNOWN && parts.dcType !== DcType.INPUTABLE) {
			execute = false;
		}
		// 計算対象パーツの有効条件が"無効"となっている場合、計算処理は行わない
		else if (parts.evaluateCondition != null && !parts.evaluateCondition.enabled) {
			execute = false;
		}
		return execute;
	},

	/**
	 * 計算処理の対象となるパーツの特定.
	 * @param partsId 対象パーツのパーツID
	 * @param htmlId 計算元パーツのHTMLID
	 * @return
	 */
	getTargetParts : function(partsId, htmlId) {
		let target = null;
		let runtimeMap = $('#editArea').data('runtimeMap');
		$.each(runtimeMap, function(k, v) {
			if (v.partsId == partsId) {
				if (k.indexOf('_') > -1) {
					let prefix = k.substring(0, k.lastIndexOf('_') + 1);
					if (htmlId.indexOf(prefix) > -1) {
						target = v;
					}
				} else {
					target = v;
				}
				if (target != null) {
					return false;
				}
			}
		});
		return target;
	},

	/**
	 * 計算条件の判定元からみた計算処理対象のパーツの特定.
	 * @param partsId 計算処理の対象パーツのパーツID
	 * @param suffix 計算条件の判定元パーツのHTMLIDの接頭語
	 * @return
	 */
	getEcTargetPartsList : function(partsId, prefix, runtimeMap) {
		return Enabled.getTargetPartsList(partsId, prefix, runtimeMap);
	},

	/**
	 * 計算元パーツの入力値を取得.
	 * 計算元パーツがリピータ内などの子画面パーツに含まれる場合、全てを合算した値を戻す
	 * 計算元となるパーツは計算先パーツのHtmlIdと同じプレフィックスをもつので、それらの合計値を
	 * 計算元パーツの値とする
	 * @param partsId 計算元パーツのパーツID
	 * @param prefix 計算先パーツのHTMLIDの接頭語
	 * @param runtimeMap
	 * @return
	 */
	getTriggerPartsValue : function(partsId, prefix, runtimeMap) {
		let result = '';
		for (let htmlId in runtimeMap) {
			// 同一パーツIDで、HTML_IDが接頭語と前方一致するものが対象
			const rt = runtimeMap[htmlId];
			if (rt.partsId === +partsId && (prefix === '' || htmlId.lastIndexOf(prefix, 0) === 0)) {
				const val = PARTS.getPartsValue(htmlId);
				if (val != '' && !isNaN(val)) {
					if (result === '')
						result = Number(val)
					else
						result += Number(val);
				}
			}
		};
		return result;
	},

	/**
	 * 計算処理.
	 * @param formula 計算式
	 * @param args 入力値
	 * @return
	 */
	calculate : function(formula, args) {
		// 要素が数値でなければ、計算不能である。
		for (let key in args) {
			const v = args[key];
			if (v === '')
				// パーツ値が空文字列だと、計算式の定義によっては破壊的なエラーとおこす（Backlog NCIWFV6-279)
				// 例えば計算式が「A*B*1.08」のときに「A=2」「B=''」だと「2**1.08」と解釈され想定外の答えとなってしまう。
				// その予防策として半角スペースをいれておく。これなら「2**1.08」ではなく「2* *1.08」と解釈されるので、ちゃんとエラーとなる
				args[key] = ' ';
			else if (v == null || !isFinite(v) || isNaN(v))
				return null;
		}
		let result = null;
		try {
			const expression = NCI.format(formula, args);
			const func = new Function("return (" + expression + ")");
			result = func();
		} catch (e) {
		}
		return result;
	}
}