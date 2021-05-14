// キーボードのキーコード
const F1=112, UP_ARROW=38, DOWN_ARROW=40, ENTER=13, TAB=9, ESC=27, PAGE_DOWN=34, PAGE_UP=33, HOME=36, END=35;
const LEFT_ARROW=37, RIGHT_ARROW=39, DELETE=46, BACKSPACE=8, INSERT=45, SHIFT=16, CTRL=17, ALT=18, ENGNUM=240, KANA=242, HANKAKU=244;

/** サジェスチョン用Javascript */
var Suggestion = {
	optionMap : {},

	/**
	 * Suggestionのセットアップ。
	 * ※NCIWFv4と違ってv6では動的バインドになったので、繰り返し要素や動的に生成されるエレメントに
	 * ※対しても一度だけ定義すれば良くなった。
	 *
	 * @params args パラメータ。詳細は下記のとおり。
	 *  'url'			SuggestionデータのEndpointへのURL。
	 *  'createIcon'	Suggestionのトリガーに起動用アイコンを作成するならtrue、作成しないならfalse。デフォルトはtrue。（動的にエレメントが生成される場合はcreateIcon=falseにして、代わりに既存エレメントに item.type="icon"として定義することを検討してください）
	 *  'rootSelector'	Suggestionのトリガーや値配布先が複数になるとき、起点エレメントへのセレクター。デフォルトは 'tr'
	 *  'items'			検索条件や検索結果の項目定義（配列）。
	 *  	[{
	 *  	'type'			Suggestionの入力／出力の種類。
	 *  		'trigger'		Suggesionを起動し、検索条件Textboxとしてユーザ手入力による絞込を行い、検索結果欄に表示し、選択行の値反映も行う
	 *  		'input'			検索条件Textboxとしてユーザ手入力による絞込を行い、検索結果欄に表示し、選択行の値反映も行う
	 *  		'fixedValue'	リテラル値を暗黙の検索条件とする。例えば削除区分とか
	 *  		'fixedObj'		エレメント値を暗黙の検索条件とする。例えばHIDDEN項目に設定した基準日とか
	 *  		'output'		絞込を行わないが、検索結果欄に表示し、選択行の値反映も行う
	 *  		'fillOnly'		検索結果欄に表示せず、選択行の値反映だけを行う
	 *  		'displayOnly'	検索結果欄に表示するが、選択行の値反映は行わない
	 *  		'icon'			Suggestion起動するためのイベントフック用エレメント（虫眼鏡アイコンの代わりに特定エレメントをクリックしてSuggestionを起動させる）
	 *  	'selector'		トリガー元／反映先を示すjQueryのセレクター（typeが'fixedValue'以外）
	 *  	'value'			検索条件用のリテラル値（typeが'fixedValue'のみ）
	 *  	'property'		サーバ側の検索条件/検索結果をマッピングする際のJSONフィールド名
	 *  	'label'			検索結果欄の表示ラベル。検索結果欄への表示がなければ不要
	 *  	'width'			検索結果欄の幅(pixel)。検索結果欄への表示がなければ不要
	 *  	}]
	 */
	setup : function(args) {
		const options = $.extend({}, Suggestion.defaults, args);
		options.pageNo = 1;

		if (!Suggestion.canSetup(options))
			return false;

		// ユニークキーを生成し、Suggestionのオプションを保存。
		// これはダイナミックにセレクターの対象エレメントが追加／削除されても、Suggestionのオプションを復元できるようにするため。
		const uuid = NCI.getUUID();
		Suggestion.optionMap[uuid] = options;

		options.items.forEach(function(item, i) {
			if ('trigger' === item.type) {
				// トリガーエレメントにサジェスチョン起動用のマーキング付与
				const $trigger = $(item.selector)
					.addClass('suggestion-trigger')
					.attr("autocomplete", "off");
				// 入力項目をトリガーとしてイベント付与
				$(document)
					.on('keydown', item.selector, uuid, Suggestion.onTriggerKeydown)
					.on('change', item.selector, uuid, Suggestion.onTriggerChange)
					.on('dblclick', item.selector, uuid, Suggestion.onTriggerDblClick);
				// トリガー用アイコン作成
				if (options.createIcon) {
					Suggestion.createIcon($trigger, options);
				}
			}
			else if ('icon' === item.type) {
				// 既存エレメントにSuggestion起動イベントを割り付け
				Suggestion.bindTriggerEvent(item, uuid, options);
			}
		});
	}

	/** サジェスチョン用のoption内容が正しいかを検証 */
	, canSetup : function(options) {
		if (!options.items || options.items.length === 0)
			throw new Error('Suggestionの項目定義がありません');

		const len = options.items.length;
		let hasTrigger = false;
		for (let i = 0; i < len; i++) {
			const item = options.items[i];
			if (!item.type)
				throw new Error('type は必須定義です');
			if (Suggestion.displayables.indexOf(item.type) >= 0 && !item.width)
				throw new Error('検索欄の表示項目なら、widthは必須項目です');
			if (Suggestion.displayables.indexOf(item.type) >= 0 && (item.label == null))
				throw new Error('検索欄の表示項目なら、labelは必須項目です');
			if (item.type !== 'icon' && !item.property)
				throw new Error('type="icon"以外なら、 propertyは必須定義です');
			if (item.type !== 'fixedValue' && !item.selector)
				throw new Error('type="fixedValue"以外なら、selectorは必須定義です');
			if (item.type === 'fixedValue' && item.value === undefined)
				throw new Error('type="fixedValue"なら、valueは必須定義です');
			if (item.type === 'trigger') {
				hasTrigger = true;
				if ($(item.selecotr).hasClass('suggestion-trigger')) {
					return false;	// トリガーがすでにセットアップ済みなら二重定義になるのでセットアップしない
				}
			}
		}
		if (!hasTrigger) {
			throw new Error('Suggestionを起動させるためには、最低でも１つの type="trigger" が必要です');
		}
		return true;
	}

	/** エレメントへサジェスチョン起動用のイベントをバインド */
	, bindTriggerEvent : function(item, uuid, options) {
		$(document).on('click', item.selector, uuid, function(ev) {
			// 起点がアイコンになっているので、アイコンの指し示すトリガーを探し出してSuggestionを起動
			const icon = ev.currentTarget;
			const $root = $(icon).closest(options.rootSelector);
			$.each(options.items, function(ii, f) {
				if ('trigger' === f.type) {
					const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
					$elem.trigger('dblclick');
				}
			})
		});
	}

	/** エレメントへサジェスチョン起動用アイコンを付与 */
	, createIcon : function($trigger, options) {
		$('<label class="glyphicon glyphicon-search suggestion-trigger" tabindex="-1"></label>')
			.insertAfter($trigger)
			.on('click', function(ev) {
				$trigger.trigger('dblclick');
			});
	}

	/** optionsから入力フィールドの値を求めて連想配列化 */
	, toTriggerCondition : function(options) {
		const $root = $(options.currentTrigger).closest(options.rootSelector);
		const conditions = {};
		options.items.forEach(function(item, i) {
			const type = item.type;
			if(type === 'fixedValue') {
				conditions[item.property] = item.value;
			}
			else {
				const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
				if (type === 'trigger' || type === 'input') {
					conditions[item.property] = $.trim($elem.val());
				} else if(type === 'fixedObj') {
					conditions[item.property] = $.trim($elem.val()) || '';
				}
			}
		});
		return conditions;
	}

	/** 出力先エレメントの値をクリア */
	, clearOutput : function(options) {
		const $root = $(options.currentTrigger).closest(options.rootSelector);
		for (let i in options.items) {
			const item = options.items[i];
			const type = item.type;
			if (type === 'output' || type === 'fillOnly') {
				const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
				const tagName = $elem.attr('tagName');
				if (tagName === 'INPUT') {
					let oldValue = $elem.val();
					$elem.val('').trigger('validate');
					if (oldValue != '') {
						$elem.trigger('change');
					}
				} else {
					$elem.text('').trigger('validate');
				}
			}
		}
	}

	/** 入力元エレメントの値をクリア */
	, clearInput : function(options) {
		const $root = $(options.currentTrigger).closest(options.rootSelector);
		for (index in options.items) {
			const item = options.items[index];
			if (item.type === 'trigger' || item.type === 'input') {
				const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
				if ($elem.is('input')) {
					let oldValue = $elem.val();
					$elem.val('').trigger('validate');
					if (oldValue != '') {
						$elem.trigger('change');
					}
				} else {
					$elem.text('').trigger('validate');
				}
			}
		}
	}

	/** トリガーエレメントでキーが押下されたとき -> F1ならSuggestionダイアログ起動 */
	, onTriggerKeydown : function(ev) {
		if (ev.which === F1)
			$(this).trigger('dblclick');
	}

	/** トリガーエレメントで値が変更された時 -> 自動検索を実施し、検索行が一件なら検索結果を出力先エレメントへ反映 */
	, onTriggerChange : function(ev) {
		const uuid = ev.data;	// .on()で定義された uuid
		const options = Suggestion.optionMap[uuid];
		options.currentTrigger = this;
		const conditions = Suggestion.toTriggerCondition(options);
		if (!conditions) {
			if (options.callback)
				options.callback(options.callbackParams);
			return true;
		}

		// 検索
		Suggestion.callAjax({
			url:	options.url,
			data:	conditions,
		})
		.done(function(res, textStatus, jqXHR) {
			// 検索結果が単一行なら結果を反映
			Suggestion.showSingleResult(res, options);
		})
		.fail(function( jqXHR, textStatus, errorThrown) {
			// 現在値は信用できないのでクリア;
			Suggestion.clearInput(options);
			Suggestion.clearOutput(options);
		});
	}

	/** トリガーエレメントでダブルクリックされた時 -> Suggestionダイアログ起動 */
	, onTriggerDblClick : function(ev) {
		const trigger = this;
		const uuid = ev.data;	// .on()で定義された uuid
		const options = Suggestion.optionMap[uuid];
		options.pageNo = 1;
		options.currentTrigger = trigger;

		if (trigger.disabled)
			return;
		if (!options)
			throw new Error('optionsが未定義です');

		// 位置調整
		if (options.positionObj) {
			options.position = $.extend(options.position, { of : options.positionObj })
		} else {
			options.position = $.extend(options.position, { of : trigger })
		}

		// 前回の検索条件を保存しておくObject
		const prevConditions = {};

		// Suggestionダイアログ
		const $dialogContent = Suggestion.createDialog(options);
		const $root = $(options.currentTrigger).closest(options.rootSelector);
		$dialogContent
			.on('click', 'input.btnPrev', function() {
				const conditions = {};
				$.extend(conditions, prevConditions);
				conditions.pageNo = --options.pageNo;
				$('input:first', $dialogContent).focus();
				Suggestion.update(options, $dialogContent, conditions, prevConditions);
			})	// #btnPrev click
			.on('click', 'input.btnNext', function(ev) {
				const conditions = {};
				$.extend(conditions, prevConditions);
				conditions.pageNo = ++options.pageNo;
				$('input:first', $dialogContent).focus();
				Suggestion.update(options, $dialogContent, conditions, prevConditions);
			})	// #btnNext click
			.on('keyup', 'input', function(ev) {
				// イベントの遅延実行（パフォーマンス対策に、一定時間入力がないまま経過したら実際のイベントを起動）
				NCI.doLater(function() {
					const conditions = {};
					for (let i in options.items) {
						const item = options.items[i], type = item.type;
						const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
						if (type === 'trigger' || type === 'input') {
							conditions[item.property] = $.trim($('input[name="'+item.property+'"]', $dialogContent).val());
						} else if(type === 'fixedValue') {
							conditions[item.property] = $.trim(item.value);
						} else if(type === 'fixedObj') {
							conditions[item.property] = $.trim($elem.val());
						}
					}
					conditions.pageNo = options.pageNo;

					// 入力内容に変更点があれば検索実行
					if (!Suggestion.objectsEquivalent(conditions, prevConditions)){
						Suggestion.update(options, $dialogContent, conditions, prevConditions);
					}
				}, 300);

			})	// input keyup
			.on('keydown', 'input', function(ev) {
				const trCurrent = $( 'tbody>tr.highlight:first', $dialogContent );
				const trNext = trCurrent.next(), trPrev = trCurrent.prev();
				const $btnNext = $('input.btnNext', $dialogContent), $btnPrev = $('input.btnPrev', $dialogContent);
				const pageCount = +$('input.txtPageCount', $dialogContent).val();
				switch(ev.keyCode){
				case DOWN_ARROW:	// down arrow key
					trNext.addClass('highlight');
					if (trNext.length === 0 && options.pageNo < pageCount){
						++options.pageNo;
						trCurrent.removeClass('highlight');
					} else if (trNext.length > 0) {
						trCurrent.removeClass('highlight');
					}
					return false;
				case UP_ARROW:		// up arrow key
					trPrev.addClass('highlight');
					if (trPrev.length === 0 && options.pageNo > 1) {
						--options.pageNo;
						trCurrent.removeClass('highlight');
					} else if (trPrev.length > 0) {
						trCurrent.removeClass('highlight');
					}
					return false;
				case ENTER:	// enter key
					trCurrent.triggerHandler("click");
					return false;
				case ESC:	// esc key
					$dialogContent.dialog('destroy');
					return false;
				case PAGE_DOWN:	// PageDown
					if (options.pageNo < pageCount)
						++options.pageNo;
					return false;
				case PAGE_UP:	// PageUp
					if (options.pageNo > 1)
						--options.pageNo;
					return false;
				case HOME:	// Home
					options.pageNo = 1;
					return false;
				case END:	// End
					options.pageNo = pageCount;
					return false;
				case RIGHT_ARROW: case LEFT_ARROW: case DELETE:
				case BACKSPACE: case INSERT: case SHIFT: case CTRL:
				case ALT: case ENGNUM: case KANA: case HANKAKU:
					break;
				default:
					// ページ番号リセット
					options.pageNo = 1;
					$('input.txtPage', $dialogContent).text('PAGE: ' + options.pageNo + ' / ' + options.pageNo);
					break;
				}
			})	// input keydown
			.on('dialogopen', function(ev) {
				$dialogContent.prev('div.ui-dialog-titlebar').remove();
				$('div.ui-dialog-content').each(function(){
					$dialogContent.css('overflow', 'hidden');
				});
				Suggestion.deleteProperties( prevConditions );
				// 初期検索
				if( options.initSearch ){
					for (let i in options.items) {
						const item = options.items[i];
						if (item.type === 'trigger') {
							// 入力内容を初期値として絞り込む
							const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
							$('input[name="'+item.property+'"]', $dialogContent).val($elem.val());
						}
					}
					$('input:first', $dialogContent).trigger("keyup");
				}
				$('input:first', $dialogContent).focus();
			})	// dialogopen
			.on('dialogclose', function() {
				options.pageNo = 1;
				options.currentTrigger = null;
			});	// dialogclose

		// UI.dialog呼出
		$dialogContent.dialog({
			title: '',//options.title,
			closeText: options.closeText,
			autoOpen: options.autoOpen,
			modal:	options.modal,
			resizable:	false,//options.resizable,
			position:	options.position,
			width: 	options.width,
			height:	options.height,
			maxHeight: options.maxHeight,
			maxWidth: options.maxWidth,
			minHeight: options.minHeight,
			minWidth: options.minWidth,
			hide: 	options.hide,
			zIndex: options.zIndex
		});

		// サジェスチョンのフォーカスが失ったら閉じるように
		$('div.ui-widget-overlay').click(function() {
			$dialogContent.dialog('destroy');
		});

		return false;
	}

	/** Suggestionダイアログを生成 */
	, createDialog : function(options) {
		// 検索結果の列数
		let colCount = 0;
		const $wrapper = $(document.createElement('div')).addClass(options.css).addClass('container');
		if (options.title)
			$(document.createElement('label')).addClass('sub-title').text(options.title || '').appendTo($wrapper);
		const $table = $(document.createElement('table')).addClass('suggestion-table').appendTo($wrapper);
		const $root = $(options.currentTrigger).closest(options.rootSelector);

		// COLGROUP
		const $colgroup = $(document.createElement('colgroup')).appendTo($table);
		let tableWidth = 0;
		for (let i in options.items) {
			const item = options.items[i];
			if (Suggestion.displayables.indexOf(item.type) >= 0) {
				const width = parseInt(item.width, 10) || 100;
				tableWidth += width;
				$(document.createElement('col')).css('width', width + 'px').appendTo($colgroup);
			}
		}
		$table.css('width', tableWidth);

		// THEAD
		const $thead = $(document.createElement('thead')).appendTo($table);
		for (let r = 0; r < 2; r++) {
			const $tr = $(document.createElement('tr')).appendTo($thead);
			for (let i in options.items) {
				const item = options.items[i];
				if (Suggestion.displayables.indexOf(item.type) >= 0) {
					let $td = $(document.createElement('td')).appendTo($tr);
					if (r === 0) {
						$td.text(item.label);
						colCount++;
					} else {
						const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
						$(document.createElement('input'))
							.addClass('form-control')
							.attr({
								'name' : item.property,
								'readonly' : (item.type !== 'trigger' && item.type !== 'input'),
								'maxlength' : $elem.attr('maxlength') || 100,
								'autocomplete' : 'off',
//								'autofocus' : 'autofocus'
							})
							.css('width', '95%')
							.appendTo($td);
					}
				}
			}
		}
		// TBODY
		const $tbody = $(document.createElement('tbody')).appendTo($table);
		// TFOOT
		const $tfoot = $(document.createElement('tfoot')).appendTo($table);
		$(document.createElement('tr')).css('height', '22px').appendTo($tfoot);
		const $tr = $(document.createElement('tr')).appendTo($tfoot);
		const $td = $(document.createElement('td'))
			.attr('colspan', colCount)
			.appendTo($tr);
		$(document.createElement('input'))
			.attr({ 'type' : 'button', 'tabindex' : -1, 'disabled' : 'disabled'})
			.val(NCI.getMessage('prevPage'))
			.addClass('btn btn-success btnPrev')
			.appendTo($td);
		$(document.createElement('input'))
			.attr({ 'type' : 'button', 'tabindex' : -1, 'disabled' : 'disabled'})
			.val(NCI.getMessage('nextPage'))
			.addClass('btn btn-success btnNext')
			.appendTo($td);
		$(document.createElement('span'))
			.attr({ 'style' : 'color:blue; margin-right:5px;' })
			.addClass('form-contorol-static pull-right txtPage')
			.appendTo($td);
		$(document.createElement('input'))
			.attr({'type' : 'hidden', 'value' : '0'})
			.addClass('form-control txtPageCount')
			.appendTo($td);
		return $wrapper;
	}

	/**
	 * トリガー元のエレメント群へ検索結果を反映する。
	 * （トリガー元へ反映するデータは必ず１件である保証が必要だ）
	 */
	, showSingleResult : function(res, options) {
		if (res.results.length === 1){ // 一行の場合のみ
			// 検索結果が一意なので、呼び元のTextBox等へ反映
			const $root = $(options.currentTrigger).closest(options.rootSelector);
			const entity = res.results[0];
			for (let i in options.items) {
				const item = options.items[i];
				if (Suggestion.fillables.indexOf(item.type) >= 0) {
					const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
					const value = entity[item.property] || '';
					if ($elem.is('input, select, textarea')) {
						let oldValue = $elem.val();
						$elem.val(value).trigger('validate');
						if (oldValue != value) {
							$elem.trigger('change');
						}
					} else {
						$elem.text(value).trigger('validate');
					}
				}
			}
		} else {
			// 値が一意に定まらず検索結果としては意味をなさないため、値をクリア
			Suggestion.clearInput(options);
			Suggestion.clearOutput(options);
		}
		if (options.callback) {
			options.callback(options.callbackParams);
		}
		options.currentTrigger = null;
	}

	/** suggestionダイアログで検索を行って、その結果をダイアログへ反映 */
	, update : function(options, dialogContent, conditions, prevConditions) {
		// retrieving matched items if one or more conditions are valid
		Suggestion.deleteProperties( prevConditions );
		$.extend( prevConditions, conditions );

		const $root = $(options.currentTrigger).closest(options.rootSelector);

		dialogContent.queue( function(next){
			Suggestion.callAjax({
				url:	options.url,
				data:	conditions,
			})
			.done(function(res, textStatus, jqXHR) {
				// 既存の検索結果をクリア
				$( 'tbody', dialogContent ).empty();
				$.each( res.results, function(r, entity){
					const $tr = $(document.createElement('tr')).toggleClass('highlight', r === 0);
					for (let i in options.items) {
						const item = options.items[i];
						const value = entity[item.property] || '';
						if (Suggestion.displayables.indexOf(item.type) >= 0) {
							$('<td>').text(value).appendTo($tr);
						}
					}

					// 検索結果の行に対するイベントのバインド
					$tr.mouseover( function(){	// 検索結果行へのマウスイベント
						$('tr.highlight', dialogContent).removeClass('highlight');
						$(this).addClass('highlight');
					})
					.click( function(ev){		// 検索結果から一行を選択
						for (let i in options.items) {
							const item = options.items[i];
							if (Suggestion.fillables.indexOf(item.type) >= 0) {
								const value = entity[item.property];
								const $elem = ($root.length ? $root.find(item.selector) : $(item.selector));
								if ($elem.is('input, select, textarea')) {
									let oldValue = $elem.val();
									$elem.val(value).trigger('validate');
									if (oldValue != value) {
										$elem.trigger('change');
									}
								} else {
									$elem.text(value);
								}
							}
						}
						// コールバック
						if (options.callback) {
							options.callback(options.callbackParams);
						}
						dialogContent.dialog('destroy');
					})
					$('tbody', dialogContent ).append($tr);
				});

				$('input.btnNext', dialogContent).prop("disabled", (options.pageNo >= res.pageCount));
				$('input.btnPrev', dialogContent).prop("disabled", (options.pageNo <= 1));
				$('input.txtPageCount', dialogContent).val(res.pageCount);
				if (res.results.length === 0)
					$('input.txtPage', dialogContent).text('PAGE: 0 / 0');
				else
					$('input.txtPage', dialogContent).text('PAGE: ' + options.pageNo + ' / ' + res.pageCount);
				// コンテンツの高さ・幅が決まったので、表示位置を再調整
				dialogContent.closest('div.ui-dialog').position(options.position);
			})	// success
			.fail(function( jqXHR, textStatus, errorThrown) {
				// キューを空にして後続処理を停止し、ダイアログを閉じる
				dialogContent.queue([]).stop().dialog('destroy');
			})
			.always(function(res, textStatus, jqXHR) {
				next();
			});	// .callAjax() END
		});	// queue
	}

	/**
	 * オブジェクトが等価であるかを返す。
	 * （アドレスの比較ではなく、中の属性同士を比較）
	 */
	, objectsEquivalent : function( obj1, obj2 ){
		let tmp = $.extend( {}, obj1, obj2 );

		try{
			$.each( tmp, function( name, value ){
				if( !(name in obj1) )			throw null;
				if( !(name in obj2) )			throw null;
				if( obj1[name] != obj2[name] )	throw null;
			});
		}
		catch(e){
			return false;
		}

		return true;
	}

	/**
	 * プロパティ属性削除
	 */
	, deleteProperties : function( obj ){
		$.each( obj, function( name, value ){
			delete obj[ name ];
		});
	}

	/**
	 * jQueryの $.ajax()のラッパー。
	 * エラー時のデフォルト動作と、送信内容にセッションユニークキーを自動的に含める
	 */
	, callAjax : function (args){
		const options = {
				'pageNo' : 1,
				'pageSize' : 10,
				'messageCds' : ['btnClose', 'prevPage', 'nextPage']
		};
		for (let name in args.data) {
			options[name] = args.data[name];
		}
		return NCI.post(args.url, options, true, true);
	}

	/** Suggestionの各種設定のデフォルト値 */
	, defaults : {
		 url:				undefined 			// 呼出元指定、必須
		,items:			[]					// 呼出元指定、必須
		,title:				'' 					// 呼出元指定、オプション
		,position:			{		// jquery-ui position API準拠
			'my': 'left top'
			, 'at': 'left bottom'
			, 'of': undefined		// undefinedならSuggestionの起動トリガーとなったエレメントが基準
			, 'collision':	'fit'
		}
		,css:				'suggestion-dialog-content'	// サジェスチョンのCSSクラス
		,initSearch:		true 				// ダイアログを開いたときに、trueなら入力値をもとに初期検索を行う
		,hide: 				"slide"				// アニメーション
		,closeText: 		NCI.getMessage('btnClose')			// 右上閉じるボタンの文字
		,modal:				true				// モーダル
		,resizable:			true				// リサイズ
		,width: 			"auto"				// 幅
		,height:	  		"auto"				// 高さ
		,maxWidth: 			800					// 最大幅
		,maxHeight: 		600					// 最大高さ
		,minWidth: 			300					// 最小幅
		,minHeight: 		200					// 最小高さ
		,autoOpen: 			true				// 自動ダイアログ表示
		,zIndex: 			1000				// 画面
		,async: 			true				// true:非同期、false:同期
		,pageNo:			1					// ページNo.
		,callback: 			undefined			// サジェスチョン閉じた後のコールバック処理用関数
		,params: 			undefined			// サジェスチョン閉じた後のコールバック処理用関数の引数
		,createIcon:		true				// Suggestionのトリガーに起動用アイコンを作成するか
		,rootSelector:		'tr'				// トリガー／値配布先エレメントを特定するための起点エレメント探索用セレクター
	}

	/** 検索結果に表示可能な item.type */
	, displayables : ['trigger', 'input', 'output', 'displayOnly']

	/** 検索結果をエレメントに反映可能な item.type */
	, fillables : ['trigger', 'input', 'output', 'fillOnly']
};
