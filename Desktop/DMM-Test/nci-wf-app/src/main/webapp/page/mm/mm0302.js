$(function() {
	// jsPlumb インスタンス作成
	jsPlumb.importDefaults({
		DragOptions: {cursor: 'pointer', zIndex: 2000},
		ConnectionOverlays: [
			['Arrow', {location: 1, visible: true, width: 15, length: 12, id: 'arrow'}],
			['Label', {location: 0.7, id: 'label'}]
		],
		Container: 'routeArea'
	});

	// jsPlumb設定
	var endpointHoverStyle = {fillStyle: '#00f', strokeStyle: '#00f'};
	var sourceEndpoint = {
			endpoint: 'Dot',
			paintStyle: {strokeStyle: '#00f', fillStyle: 'transparent', radius: 7, lineWidth: 3},
			hoverPaintStyle: endpointHoverStyle,
			isSource: true,
			isTarget: false,
			connectionsDetachable: false,
			dragOptions: {},
			connector: [ 'Flowchart', {stub: [50, 50], gap: 10, cornerRadius: 5, alwaysRespectStubs: true}],
			connectorStyle: {lineWidth: 2, strokeStyle: '#00f', joinstyle: 'round', outlineStroke: 'white', outlineWidth: 2, cssClass: 'connector-style'},
			connectorHoverStyle: {lineWidth: 3, strokeStyle: '#00f', outlineWidth: 5, outlineStroke: 'white', cssClass: 'connector-style'},
			overlays: [['Label', {location: [2.0, 1.5], label: '－', cssClass: 'label label-primary label-action', visible: true, id: 'label'}]]
	};
	var targetEndpoint = {
			endpoint: 'Dot',
			paintStyle: {fillStyle: '#00f', radius: 7},
			hoverPaintStyle: endpointHoverStyle,
			maxConnections: 99,
			dropOptions: {hoverClass: 'hover', activeClass: 'active'},
			isSource: false,
			isTarget: true,
			connectionsDetachable: false,
			beforeDrop: function(info) {
				beforeDrop(info);
			}
	};

	// ローカルオブジェクト
	var templates = null;
	var contents = null;
	var $designArea = $('#designArea');
	var $componentArea = $('#componentArea');
	var $routeArea = $('#routeArea');
	var $connectionMenu = $('#connectionMenu');

	var expressionDefs = new Pager($('#expressionDefs'));
	var assignedDefs = new Pager($('#assignedDefs'));
	var changeDefs = new Pager($('#changeDefs'));
	var actionDefs = new Pager($('#actionDefs'));
	var conditionDefs = new Pager($('#conditionDefs'));
	var functionDefs = new Pager($('#functionDefs'));

	/**
	 * 初期化関数
	 * @param {*} res
	 */
	var init = function(res) {
		// ローカル変数に保持
		templates = res.templates;
		contents = res.contents;

		// セレクトボックスを生成
		// 共通
		NCI.createOptionTags($('select[data-field=executionTermUnitType]'), res.executionTermUnitTypes);
		// アクティビティ用
		NCI.createOptionTags($('select[data-field=processOpeType]'), res.processOpeTypes);
		NCI.createOptionTags($('select[data-field=skipRuleType]'), res.skipRuleTypes);
		NCI.createOptionTags($('select[data-field=activityEndType]'), res.activityEndTypes);
		NCI.createOptionTags($('select[data-field=dcId]'), res.dcIds);
		// 参加者用
		NCI.createOptionTags($('select[data-field=assignRoleCode]'), res.assignRoles);
		// 参加者変更用
		NCI.createOptionTags($('select[data-field=changeRoleCode]'), res.changeRoles);
		// アクション用
		NCI.createOptionTags($('select[data-field=actionDefType]'), res.actionDefTypes);
		// 遷移先用
		NCI.createOptionTags($('select[data-field=businessProcessStatus]'), res.businessProcessStatuses);
		NCI.createOptionTags($('select[data-field=businessActivityStatus]'), res.businessActivityStatuses);
		NCI.createOptionTags($('select[data-field=businessActivityStatusPre]'), res.businessActivityStatuses);
		// ファンクション用
		NCI.createOptionTags($('select[data-field=functionCode]'), res.functions);
		NCI.createOptionTags($('select[data-field=executionTimingType]'), res.executionTimingTypes);
		// 演算子区分を保持
		ExpressionDef.operatorTypes = res.operatorTypes;
		// アクティビティ種類区分を保持
		ActivityDef.activityKindTypes = res.activityKindTypes;
		// アクティビティ区分を保持
		ActivityDef.activityTypes = res.activityTypes;
		// 待ち合わせ区分を保持
		ActivityDef.waitTypes = res.waitTypes;
		// アクション
		ActionDef.actions = res.actions;

		// 遷移先クリック時メニュー
		$connectionMenu.find('li').hover(function() {$(this).addClass('active');}, function() {$(this).removeClass('active');});

		// 閉じる際にconditionDefUniqueKeyをクリア
		$connectionMenu.on('hide.bs.modal', function() {$('#conditionDefUniqueKey').text('');});

		// テンプレートエリア作成
		createTemplates();

		// ルートエリア描画
		refresh();

		// イベント処理登録
		jsPlumb.bind('click', function(info, e) {
			var sourceId = info.sourceId.split(/:/g)[3];
			var uniqueKey = info.getParameter('conditionDefUniqueKey');
			var expressionDefUniqueKey = info.getParameter('expressionDefUniqueKey');
			if (!expressionDefUniqueKey) {
				$('a.btn-update', $connectionMenu).hide();
			} else {
				$('a.btn-update', $connectionMenu).show();
			}
			if (sourceId === '0000000000' || $('#expressionDefItems li').length == 0) {
				$('#expressionDefSelectMenu', $connectionMenu).parent().hide();
			} else {
				$('#expressionDefSelectMenu', $connectionMenu).parent().show();
			}
			$('#conditionDefUniqueKey').text(uniqueKey);
			$connectionMenu.modal('show');
		});

		$(document)
		.on('dblclick', 'span.component', function() {
			dblclick($(this).attr('id'));
		})
		.on('click', 'span.component > button.btn-remove', function() {
			var uniqueKey = $(this).parent().attr('id');
			ActivityDef.remove(uniqueKey);
		})
		.on('click', '#expressionDefItems > li > a', function() {
			var uniqueKey = $('#conditionDefUniqueKey').text();
			var expressionDefCode = $(this).attr('data-code');
			$connectionMenu.modal('hide');
			ConditionDef.updateExpression(uniqueKey, expressionDefCode);
		})
		.on('click', '#connectionMenu a.btn-update', function() {
			var uniqueKey = $('#conditionDefUniqueKey').text();
			$connectionMenu.modal('hide');
			ConditionDef.updateExpression(uniqueKey, '');
		})
		.on('click', '#businessProcessStatusItems > li > a', function() {
			var uniqueKey = $('#conditionDefUniqueKey').text();
			var businessProcessStatus = $(this).attr('data-code');
			$connectionMenu.modal('hide');
			ConditionDef.updateBusinessProcessStatus(uniqueKey, businessProcessStatus);
		})
		.on('click', '#connectionMenu a.btn-remove', function() {
			var uniqueKey = $('#conditionDefUniqueKey').text();
			$connectionMenu.modal('hide');
			ConditionDef.unconnect(uniqueKey);
		})
		.on('click', '#processDefInfo', function() {
			Validator.hideBalloon($('#processDef').find('input,textarea,select'));
		})
		.on('change', '[data-field=executionTermUnitType]', function(ev) {
			var parent = $(this).parent();
			var val = $('option:selected', $(this)).val();
			if (val === 'N') {
				Validator.hideBalloon($('[data-field=executionTerm]', parent));
				$('[data-field=executionTerm]', parent).val('');
				$('[data-field=executionTerm]', parent).prop('disabled', true);
				$('[data-field=executionTerm]', parent).removeClass('required');
			} else {
				$('[data-field=executionTerm]', parent).prop('disabled', false);
				$('[data-field=executionTerm]', parent).addClass('required');
			}
		})
		;
		// プロパティエリア初期化
		// プロセス情報
		ProcessDef.init();
		// 比較条件式
		ExpressionDef.init();
		// アクティビティ情報
		ActivityDef.init();
		// 参加者ロール情報
		AssignedDef.init();
		// 参加者変更ロール情報
		ChangeDef.init();
		// アクション情報
		ActionDef.init();
		// 遷移先情報
		ConditionDef.init();
		// 機能情報
		FunctionDef.init();
	};

	/**
	 * コンポーネントエリア作成処理
	 */
	var createTemplates = function() {
		$.each(templates.activityTemplates, function(i, e) {
			let $component = createComponent(e.activityKindType, e.displayName);
			$component.prop('id', 'template-' + e.activityTemplateCode);
			$component.addClass('template');
			if (e.displayFlag === '0') {
				$component.hide();
			}
			$componentArea.append($component);
		});

		// draggable追加
		$componentArea.find('span.component').draggable({revert: 'invalid', helper: 'clone', opacity: 0.5, zIndex: 20, grid: [ 5, 5 ]});
		// droppable追加
		$routeArea.droppable({accept: '#componentArea > span', activeClass: 'alert-success', tolerance: 'fit', drop: drop});
	};

	var createComponent = function(activityKindType, name) {
		let $component = $('<span></span>').addClass('component').addClass('kind-' + activityKindType);
		let $name = $('<span></span>').text(name);
		if (activityKindType === '00' || activityKindType === '99') {
			$name.addClass('start-end');
		} else {
			$name.addClass('normal');
		}
		$component.append($name);
		return $component;
	};

	/**
	 * ルートエリア描画処理
	 */
	var refresh = function() {
		jsPlumb.detachEveryConnection();
		jsPlumb.deleteEveryEndpoint();

		$routeArea.children().remove();

		$.each(contents.activityDefs, function(i, e) {
			var $component = createComponent(e.activityKindType, e.activityDefName);
			ActivityDef.draw($component, e);
		});

		$.each(contents.conditionDefs, function(i, e) {
			let actionDef = contents.actionDefs[e.actionDefUniqueKey];
			if (actionDef && actionDef.defaultFlag === '1' && e.displayFlag === '1') {
				var connection = jsPlumb.connect({uuids:[('R' + e.activityDefCode), ('L' + e.activityDefCodeTransit)]});
				if (connection) ConditionDef.initConnection(e, connection);
			}
		});
	};

	/**
	 * アクティビティストップイベント
	 * @param {*} ev
	 * @param {*} ui
	 */
	var stop = function(ev, ui) {ActivityDef.updatePosition(ev, ui);};

	/**
	 * アクティビティドロップイベント
	 * @param {*} ev
	 * @param {*} ui
	 */
	var drop = function(ev, ui) {ActivityDef.create(ev, ui);};

	/**
	 * アクティビティダブルクリック
	 * @param {*} uniqueKey
	 */
	var dblclick = function(uniqueKey) {
		if (ActivityDef.selected(uniqueKey)) {
			// アクティビティ情報
			ActivityDef.refer(uniqueKey);

			// 参加者ロール情報
			AssignedDef.selected(uniqueKey);
			// 参加者変更ロール情報
			ChangeDef.selected(uniqueKey);

			// アクション情報
			var actionDefUniqueKey = ActionDef.selected(uniqueKey);
			ConditionDef.selected(actionDefUniqueKey);
			FunctionDef.selected(actionDefUniqueKey);
		}
	};

	/**
	 * 遷移先接続イベント
	 * @param {*} info
	 */
	var beforeDrop = function(info) {ConditionDef.connect(info);return false;};

	/**
	 * プロセス定義情報制御処理
	 */
	var ProcessDef = {
		/**
		 * 初期処理
		 */
		init: function() {
			var $processDef = $('#processDef');
			ProcessDef.refer();

			$('div.collapse', $processDef)
			.on('hide.bs.collapse', function() {
				$('i', '#processDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#processDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});
			$(document)
			.on('click', '#processDef button.btn-update', function() {
				ProcessDef.update();
			})
			.on('click', '#processDef button.btn-remove', function() {
				ProcessDef.remove();
			})
			;
		},
		/**
		 * 参照処理
		 */
		refer: function() {
			var $processDef = $('#processDef');
			if (contents.processDef) {
				$('input, select, textarea, span', $processDef).filter('[data-field]').each(function(i, elem) {
					var val = contents.processDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}

					} else {
						$(elem).text(val);
					}
				});

				// カレンダー（年月日）
				//	初期状態だとフォーカスアウト時にblankを保存してしまうため位置を修正
				NCI.ymdPicker($('input.ymdPicker'));

				$('[data-field=executionTermUnitType]', $processDef).trigger('change');
			}
		},
		/**
		 * 更新処理
		 */
		update: function() {
			var $processDef = $('#processDef');
			var $targets = $processDef.find('input, select, textarea');
			if (!Validator.validate($targets, true)) return false;
			var processDef = $.extend({}, contents.processDef, NCI.toObjFromElements($processDef, []));
			processDef.batchProcessingFlag = !!processDef.batchProcessingFlag ? processDef.batchProcessingFlag : '0';

			var msg = NCI.getMessage('MSG0071', 'プロセス');
			NCI.confirm(msg, function() {
				var params = {
					processDef: processDef
				};
				NCI.post('/mm0302/ProcessDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.processDef = res.processDef;
						Message.addMessage('success', ['プロセス情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['プロセス情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function() {
			var processDef = contents.processDef;
			var msg = NCI.getMessage('MSG0072', 'プロセス');
			NCI.confirm(msg, function() {
				var params = {
					processDef: processDef
				};
				NCI.post('/mm0302/ProcessDef/remove', params).done(function(res) {
					if (res && res.success) {
						contents.processDef = res.processDef;
						Message.addMessage('success', ['プロセス情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['プロセス情報削除処理でエラーが発生しました。']);
					}
				});
			});
		}
	};
	/**
	 * アクティビティ定義制御処理
	 */
	var ActivityDef = {
		/**
		 * アクティビティ種類区分
		 */
		activityKindTypes: [],
		/**
		 * アクティビティ区分
		 */
		activityTypes: [],
		/**
		 * 待ち合わせ区分
		 */
		waitTypes: [],
		/**
		 * アクティビティ描画
		 */
		draw: function($component, data) {
			var activityDefCode = data.activityDefCode;
			var activityDefName = data.activityDefName;
			var xcoordinate = parseInt(data.xcoordinate) - 95;
			xcoordinate -= (xcoordinate % 5);
			var ycoordinate = parseInt(data.ycoordinate) - 585;
			ycoordinate -= (ycoordinate % 5);
			var activityType = data.activityType;
			var activityDefUniqueKey = data.uniqueKey;
			var actionDefUniqueKey = ActionDef.getDefault(activityDefUniqueKey);

			var $clone = $component.clone();
			$clone.attr('id', data.uniqueKey);
			$clone.removeClass('ui-draggable ui-draggable-handle');
			$clone.css({position: 'absolute', top: (ycoordinate < 5 ? 5 : ycoordinate), left: (xcoordinate < 5 ? 5 : xcoordinate)});
			$clone.css({cursor: 'pointer'});
			$clone.popover({content: activityDefCode, trigger: 'hover', placement: 'top'});

			if ('parallel_s' === activityType) {
				$clone.hover(
						function() {
							$(this).addClass('parallel-hover');
							$('#' + $(this).attr('id').replace('PARALLEL_S', 'PARALLEL_E').replace(/:/g, '\\:')).addClass('parallel-hover');
						},
						function() {
							$(this).removeClass('parallel-hover');
							$('#' + $(this).attr('id').replace('PARALLEL_S', 'PARALLEL_E').replace(/:/g, '\\:')).removeClass('parallel-hover');
						}
				);
				$clone.children('span').remove();
			} else if ('parallel_e' === activityType) {
				$clone.hover(
						function() {
							$(this).addClass('parallel-hover');
							$('#' + $(this).attr('id').replace('PARALLEL_E', 'PARALLEL_S').replace(/:/g, '\\:')).addClass('parallel-hover');
						},
						function() {
							$(this).removeClass('parallel-hover');
							$('#' + $(this).attr('id').replace('PARALLEL_E', 'PARALLEL_S').replace(/:/g, '\\:')).removeClass('parallel-hover');
						}
				);
				$clone.children('span').remove();
			}
			$clone.appendTo($routeArea);

			// 遷移先エンドポイント
			var l = $.extend(true, {}, targetEndpoint);
			l.parameters = {transitUniqueKey: activityDefUniqueKey};

			// 遷移元エンドポイント
			var r = $.extend(true, {}, sourceEndpoint);
			if (actionDefUniqueKey) {
				r.parameters = {actionDefUniqueKey: actionDefUniqueKey};
				r.overlays[0][1].label = contents.actionDefs[actionDefUniqueKey].actionName;
			}

			var remove = $('<button class="btn btn-danger btn-xs btn-remove"><i class="glyphicon glyphicon-remove"></i></button>');
			if (activityType === 'start') {
				r.detachable = false;
				$component.hide();
				jsPlumb.addEndpoint($clone, r, {anchor: ['RightMiddle'], uuid: ('R' + activityDefCode)});
			} else if (activityType === 'end') {
				$component.hide();
				jsPlumb.addEndpoint($clone, l, {anchor: ['LeftMiddle'], uuid: ('L' + activityDefCode)});
			} else {
				r.maxConnections = 99;
				if (actionDefUniqueKey) jsPlumb.addEndpoint($clone, r, {anchor: ['RightMiddle'], uuid: ('R' + activityDefCode)});
				jsPlumb.addEndpoint($clone, l, {anchor:['LeftMiddle'], uuid: ('L' + activityDefCode)});
				if ('parallel_s' != activityType) $clone.append(remove);
			}
			jsPlumb.draggable($clone, {opacity: 0.5, stop: stop, grid: [ 5, 5 ]});
		},
		/**
		 * 初期処理
		 */
		init: function() {
			var $activityDef = $('#activityDef');
			ActivityDef.unselected();
			$('div.collapse', $activityDef)
			.on('hide.bs.collapse', function() {
				$('i', '#activityDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#activityDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});
			$(document)
			.on('click', '#activityDef button.btn-update', function() {
				var uniqueKey = $('span.component', $routeArea).filter('.selected').attr('id') || '';
				ActivityDef.update(uniqueKey);
			})
			.on('click', '#activityDef button.btn-remove', function() {
				var uniqueKey = $('span.component', $routeArea).filter('.selected').attr('id') || '';
				ActivityDef.remove(uniqueKey);
			})
			.on('change', '[data-field=activityEndType]', function() {
				if (this.value == '3' || this.value == '4') {
					// 多数決 or 人数指定
					$('[data-field=activityEndValue]').prop('disabled', false);
					$('[data-field=activityEndValue]').addClass('required');
					if (this.value == '3') {
						$('[data-field=activityEndValue]').attr('data-validate', '{"pattern":"integer","min":1,"max":100}');
					} else {
						$('[data-field=activityEndValue]').removeAttr('data-validate');
					}
				} else {
					$('[data-field=activityEndValue]').prop('disabled', true);
					$('[data-field=activityEndValue]').removeClass('required');
					$('[data-field=activityEndValue]').removeAttr('data-validate');
				}
			})
			;
		},
		/**
		 * 作成処理
		 */
		create: function(ev, ui) {
			var activityTemplateCode = ui.draggable.attr('id').replace('template-', '');
			var activityTemplates = [templates.activityTemplates[activityTemplateCode]];
			if (activityTemplateCode === 'PARALLEL_S') {
				activityTemplates.push(templates.activityTemplates['PARALLEL_E'])
			}

			var assignedTemplates = [];
			var actionTemplates = [];
			var functionTemplates = [];

			$.each(activityTemplates, function(i, d) {
				// 参加者テンプレート
				$.each($.map(templates.assignedTemplates, function(v, k) {if (k.indexOf(d.activityTemplateCode) === 0) return v;}), function(j, t) {
					assignedTemplates.push(t);
				});
				// アクションテンプレート
				$.each($.map(templates.actionTemplates, function(v, k) {if (k.indexOf(d.activityTemplateCode) === 0) return v;}), function(j, t) {
					actionTemplates.push(t);
				});
				// アクション機能テンプレート
				$.each($.map(templates.functionTemplates, function(v, k) {if (k.indexOf(d.activityTemplateCode) === 0) return v;}), function(j, t) {
					functionTemplates.push(t);
				});
			});

			var activityDef = {};
			activityDef.corporationCode = contents.processDef.corporationCode;
			activityDef.processDefCode = contents.processDef.processDefCode;
			activityDef.processDefDetailCode = contents.processDef.processDefDetailCode;
			activityDef.validStartDate = contents.processDef.validStartDate;
			activityDef.validEndDate = contents.processDef.validEndDate;
			var offsetx = Math.round($routeArea.offset().left - $designArea.offset().left);
			var offsety = Math.round($routeArea.offset().top - $designArea.offset().top);
			var xcoordinate = Math.round(ui.position.left + $routeArea.scrollLeft() + 5) - offsetx;
			xcoordinate -= (xcoordinate % 5);
			var ycoordinate = Math.round(ui.position.top + $routeArea.scrollTop() + 5) - offsety;
			ycoordinate -= (ycoordinate % 5);
			activityDef.xcoordinate = (xcoordinate + 95);
			activityDef.ycoordinate = (ycoordinate + 585);

			var params = {
					activityDef: activityDef
					, activityTemplates: activityTemplates
					, assignedTemplates: assignedTemplates
					, actionTemplates: actionTemplates
					, functionTemplates: functionTemplates
			};
			NCI.post('/mm0302/ActivityDef/create', params).done(function(res) {
				if (res && res.success) {
					$.each(res.functionDefs, function(i, e) {
						contents.functionDefs[i] = e;
					});
					$.each(res.actionDefs, function(i, e) {
						contents.actionDefs[i] = e;
					});
					$.each(res.assignedDefs, function(i, e) {
						contents.assignedDefs[i] = e;
					});
					$.each(res.activityDefs, function(i, e) {
						let $component = createComponent(e.activityKindType, e.activityDefName);
						ActivityDef.draw($component, e);
						contents.activityDefs[i] = e;
					});
					Message.addMessage('success', ['アクティビティ情報を作成しました。']);
				} else {
					Message.addMessage('danger', ['アクティビティ情報作成処理でエラーが発生しました。']);
				}
			});
		},
		/**
		 * 位置情報更新処理
		 */
		updatePosition: function(ev, ui) {
			var $target = $(ev.target);
			var activityDef = $.extend({}, contents.activityDefs[$target.attr('id')]);

			activityDef.xcoordinate = (ui.position.left < 5 ? 5 : ui.position.left) + 95;
			activityDef.ycoordinate = (ui.position.top < 5 ? 5 : ui.position.top) + 585;

			var params = {activityDef: activityDef};
			NCI.post('/mm0302/ActivityDef/update', params).done(function(res) {
				if (res && res.success) {
					$.each(res.activityDefs, function(i, e) {
						contents.activityDefs[i] = e;
						var $target = $('#' + i.replace(/:/g, '\\:'));
						$target.css({top: e.ycoordinate - 585, left: e.xcoordinate - 95});
						jsPlumb.repaintEverything();
					});
					Message.addMessage('success', ['位置情報を更新しました。']);
				} else {
					Message.addMessage('danger', ['位置情報更新処理でエラーが発生しました。']);
				}
			});
		},
		/**
		 * 選択処理
		 */
		selected: function(uniqueKey) {
			var srcId = $('span.component', $routeArea).filter('.selected').attr('id') || '';
			var distId = uniqueKey || '';

			if (srcId === distId) {
				ActivityDef.unselected();
				$('#' + distId.replace(/:/g, '\\:')).removeClass('selected');
				return false;
			}

			var activityDefCode = distId.split(/:/g)[3];
			if (activityDefCode === '0000000000') {
				Message.addMessage('warning', ['開始アクティビティは選択できません。']);
				return false;
			} else if (activityDefCode === '9999999999') {
				Message.addMessage('warning', ['終了アクティビティは選択できません。']);
				return false;
			} else if (activityDefCode.indexOf('PARALLEL_S') == 0) {
				Message.addMessage('warning', ['並行開始アクティビティは選択できません。']);
				return false;
			}

			if (!!srcId) {
				$('#' + srcId.replace(/:/g, '\\:')).removeClass('selected');
				$('#' + distId.replace(/:/g, '\\:')).addClass('selected');
			} else {
				$('#' + distId.replace(/:/g, '\\:')).addClass('selected');
			}

			var activityDef = contents.activityDefs[distId];
			$('.parallel_hide.hide').removeClass('hide');
			if (activityDef.activityType === 'parallel_e') {
				$('.parallel_hide').addClass('hide');
				$('select[data-field=waitType]').parent().parent().removeClass('hide');
			} else {
				$('select[data-field=waitType]').parent().parent().addClass('hide');
			}

			// アクティビティ終了判定区分
			if (activityDef.activityEndType == '3' || activityDef.activityEndType == '4') {
				// 多数決 or 人数指定
				$('[data-field=activityEndValue]').prop('disabled', false);
				$('[data-field=activityEndValue]').addClass('required');
				if (this.value == '3') {
					$('[data-field=activityEndValue]').attr('data-validate', '{"pattern":"integer","min":1,"max":100}');
				} else {
					$('[data-field=activityEndValue]').removeAttr('data-validate');
				}
			} else {
				$('[data-field=activityEndValue]').prop('disabled', true);
				$('[data-field=activityEndValue]').removeClass('required');
				$('[data-field=activityEndValue]').removeAttr('data-validate');
			}

			// 表示非表示切替
			$('#activityDefContents').removeClass('hide');
			$('#activityDefContents').parent().find('.message').addClass('hide');

			$('#assignRoleContents').removeClass('hide');
			$('#assignRoleContents').parent().find('.message').addClass('hide');

			$('#actionDefContents').removeClass('hide');
			$('#actionDefContents').parent().find('.message').addClass('hide');

			return true;
		},
		/**
		 * 選択解除処理
		 */
		unselected: function() {
			// 表示非表示切替
			$('#activityDefContents').addClass('hide');
			$('#activityDefContents').parent().find('.message').removeClass('hide');

			$('#assignRoleContents').addClass('hide');
			$('#assignRoleContents').parent().find('.message').removeClass('hide');

			$('#actionDefContents').addClass('hide');
			$('#actionDefContents').parent().find('.message').removeClass('hide');
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var $activityDef = $('#activityDef');
			var activityDef = contents.activityDefs[uniqueKey];
			if (activityDef) {

				// コンボボックス再作成
				NCI.createOptionTags($('select[data-field=activityKindType]'), ActivityDef.activityKindTypes.filter(function(e) {
					return e.value === activityDef.activityKindType
						|| (activityDef.activityType === 'normal' && (e.value !== '00' && e.value !== '99' && e.value !== 'PS' && e.value !== 'PE'));
				}));
				NCI.createOptionTags($('select[data-field=activityType]'), ActivityDef.activityTypes.filter(function(e) {
					return e.value === activityDef.activityType;
				}));
				NCI.createOptionTags($('select[data-field=waitType]'), ActivityDef.waitTypes.filter(function(e) {
					return (e.value === '0' && 'parallel_e' !== activityDef.activityType)
						|| (e.value !== '0' && 'parallel_e' === activityDef.activityType);
				}));
				NCI.createOptionTags($('[data-field=transitUniqueKey]'), $.map(contents.activityDefs, function(v, k) {if (activityDef.uniqueKey !== k) return v;}).filter(function(e) {
					// 開始、並行待合開始の場合
					if (('start' === activityDef.activityType || 'parallel_s' === activityDef.activityType)
							&& 'normal' !== e.activityType) {
						return false;
					}
					// 通常の場合
					if ('normal' === activityDef.activityType && 'start' === e.activityType) {
						return false;
					}
					// 並行待合終了の場合
					if ('parallel_e' === activityDef.activityType
							&& ('normal' !== e.activityType && 'end' !== e.activityType)) {
						return false;
					}
					// 業務連携系の場合
					if ('normal_wf_link' === e.activityType
							|| 'wf_link_d_e' === e.activityType
							|| 'wf_link_i_e' === e.activityType) {
						return false;
					}
					return true;
				}));

				$activityDef.find('input, select, textarea, span').filter('[data-field]').each(function(i, elem) {
					var val = activityDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}

					} else {
						$(elem).text(val);
					}
				});
				$('select[data-field=executionTermUnitType]', $activityDef).trigger('change');
			}
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $activityDef = $('#activityDef');
			var $targets = $activityDef.find('input, select, textarea');
			if (!Validator.validate($targets, true)) return false;

			var activityDef = $.extend({}, contents.activityDefs[uniqueKey], NCI.toObjFromElements($activityDef, []));
			$activityDef.find('input[type=checkbox]').filter('[data-field]').each(function(i, e) {if (!activityDef[$(e).attr('data-field')]) activityDef[$(e).attr('data-field')] = '0';});

			var msg = NCI.getMessage('MSG0071', 'アクティビティ');
			NCI.confirm(msg, function() {
				var params = {
						activityDef: activityDef
				};
				NCI.post('/mm0302/ActivityDef/update', params).done(function(res) {
					if (res && res.success) {
						$.each(res.activityDefs, function(i, e) {contents.activityDefs[i] = e;});
						refresh();
						ActivityDef.selected(uniqueKey)
						Message.addMessage('success', ['アクティビティ情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['アクティビティ情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var activityDef = contents.activityDefs[uniqueKey];
			var parallelStartUniqueKey = activityDef.activityType === 'parallel_e' ? activityDef.parallelStartUniqueKey : null;
			var msg = NCI.getMessage('MSG0072', 'アクティビティ');
			NCI.confirm(msg, function() {
				var array = [activityDef];
				if (parallelStartUniqueKey) array.push(contents.activityDefs[parallelStartUniqueKey]);
				var params = {
					activityDefs: array,
					assignedDefs: $.map(contents.assignedDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0 || (parallelStartUniqueKey && k.indexOf(parallelStartUniqueKey) === 0)) return v;}),
					changeDefs: $.map(contents.changeDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0 || (parallelStartUniqueKey && k.indexOf(parallelStartUniqueKey) === 0)) return v;}),
					actionDefs: $.map(contents.actionDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0 || (parallelStartUniqueKey && k.indexOf(parallelStartUniqueKey) === 0)) return v;}),
					conditionDefs: $.map(contents.conditionDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0 || v.transitUniqueKey === uniqueKey || (parallelStartUniqueKey && (k.indexOf(parallelStartUniqueKey) === 0 || v.transitUniqueKey === parallelStartUniqueKey))) return v;}),
					functionDefs: $.map(contents.functionDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0 || (parallelStartUniqueKey && k.indexOf(parallelStartUniqueKey) === 0)) return v;})
				};
				NCI.post('/mm0302/ActivityDef/remove', params).done(function(res) {
					if (res && res.success) {
						$.each(res.activityDefs, function(i, e) {delete contents.activityDefs[i];});
						$.each(res.assignedDefs, function(i, e) {delete contents.assignedDefs[i];});
						$.each(res.changeDefs, function(i, e) {delete contents.changeDefs[i];});
						$.each(res.actionDefs, function(i, e) {delete contents.actionDefs[i];});
						$.each(res.conditionDefs, function(i, e) {delete contents.conditionDefs[i];});
						$.each(res.functionDefs, function(i, e) {delete contents.functionDefs[i];});
						var srcId = $('span.component', $routeArea).filter('.selected').attr('id') || '';
						ActivityDef.unselected();
						refresh();
						if (srcId && srcId !== uniqueKey) ActivityDef.selected(srcId);
						Message.addMessage('success', ['アクティビティ情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['アクティビティ情報削除処理でエラーが発生しました。']);
					}
				});
			});
		}
	};
	/**
	 * 参加者定義制御処理
	 */
	var AssignedDef = {
		/**
		 * 初期処理
		 */
		init: function() {
			var $assignedDef = $('#assignedDef');
			$('div.collapse', $assignedDef)
			.on('hide.bs.collapse', function() {
				$('i', '#assignedDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#assignedDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});

			$(document)
			.on('click', '#assignedDefs [data-field=assignRoleName]', function() {
				var $tr = $(this).parent().parent();
				var uniqueKey = $tr.find('[data-field=uniqueKey]').text();
				if ($('#assignedDefs tbody tr.info [data-field=uniqueKey]').text() === uniqueKey) return;

				$('#assignedDefs tbody tr').removeClass('info');
				$tr.addClass('info');
				AssignedDef.refer(uniqueKey);
			})
			.on('click', '#assignedDef button.btn-create', function() {
				var uniqueKey = $('span.component', $routeArea).filter('.selected').attr('id') || '';
				AssignedDef.create(uniqueKey);
			})
			.on('click', '#assignedDef button.btn-update', function() {
				var uniqueKey = $('#assignedDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				AssignedDef.update(uniqueKey);
			})
			.on('click', '#assignedDef button.btn-remove', function() {
				var uniqueKey = $('#assignedDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				AssignedDef.remove(uniqueKey);
			})
			;
		},
		/**
		 * 作成処理
		 */
		create: function(uniqueKey) {
			var activityDef = contents.activityDefs[uniqueKey];
			var params = {
				assignedDef: {
					corporationCode: activityDef.corporationCode,
					processDefCode: activityDef.processDefCode,
					processDefDetailCode: activityDef.processDefDetailCode,
					activityDefCode: activityDef.activityDefCode
				}
			};
			Popup.open("../mm/mm0305.html", callbackFromMm0305, params);
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var $assignedDef = $('#assignedDef');
			var assignedDef = contents.assignedDefs[uniqueKey];
			if (assignedDef) {
				$assignedDef
				.find('input, select, textarea, span')
				.filter('[data-field]')
				.not('#assignedDefs [data-field]')
				.each(function(i, elem) {
					var val = assignedDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}
					} else {
						$(elem).text(val);
					}
				});
			}
		},
		/**
		 * 選択処理
		 */
		selected: function(parentUniqueKey, uniqueKey) {
			var array = [];
			$.each(contents.assignedDefs, function(i, e) {if (i.indexOf(parentUniqueKey) === 0) {array.push(e);}});
			array.sort(function(f, s) {
				if (f['sortOrder'] != s['sortOrder'] && !f['sortOrder'] && f['sortOrder'] != 0) return 1;
				if (f['sortOrder'] != s['sortOrder'] && !s['sortOrder'] && s['sortOrder'] != 0) return -1;
				if (f['sortOrder'] > s['sortOrder']) return 1;
				if (f['sortOrder'] < s['sortOrder']) return -1;
				if (f['seqNoAssignedDef'] > s['seqNoAssignedDef']) return 1;
				if (f['seqNoAssignedDef'] < s['seqNoAssignedDef']) return -1;
				if (f['assignRoleCode'] > s['assignRoleCode']) return 1;
				if (f['assignRoleCode'] < s['assignRoleCode']) return -1;
				return 0;
			});
			assignedDefs.fillTable(array);
			var $assignedDef = $('#assignedDef');
			var $tr = $('#assignedDefs tbody tr', $assignedDef);
			$('button.btn-update', $assignedDef).prop('disabled', $tr.length == 0);
			$('button.btn-remove', $assignedDef).prop('disabled', $tr.length == 0);
			$('button.btn-detail', $assignedDef).prop('disabled', $tr.length == 0);
			$('div.collapse', $assignedDef).collapse($tr.length == 0 ? 'hide' : 'show');

			if (!uniqueKey) uniqueKey = $tr.filter(':first').find('[data-field=uniqueKey]').text();
			$tr.find('[data-field=uniqueKey]').filter(function(){return $(this).text() === uniqueKey;}).parent().parent().find('a[data-field]').trigger('click');
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $assignedDef = $('#assignedDef');
			var $targets = $assignedDef.find('input, select, textarea').not('#assignedDefs [data-field]');
			if (!Validator.validate($targets, true)) return false;
			var assignedDef = $.extend({}, contents.assignedDefs[uniqueKey], NCI.toObjFromElements($targets.parent(), []));

			var msg = NCI.getMessage('MSG0071', '参加者');
			NCI.confirm(msg, function() {
				var params = {assignedDef: assignedDef};
				NCI.post('/mm0302/AssignedDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.assignedDefs[uniqueKey] = res.assignedDef;
						AssignedDef.selected(res.assignedDef.activityDefUniqueKey, uniqueKey);
						Message.addMessage('success', ['参加者情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['参加者情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var assignedDef = contents.assignedDefs[uniqueKey];
			var msg = NCI.getMessage('MSG0072', '参加者');
			NCI.confirm(msg, function() {
				var params = {assignedDef: assignedDef};
				NCI.post('/mm0302/AssignedDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.assignedDefs[res.assignedDef.uniqueKey];
						AssignedDef.selected(res.assignedDef.activityDefUniqueKey);
						Message.addMessage('success', ['参加者情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['参加者情報削除処理でエラーが発生しました。']);
					}
				});
			});
		}
	};
	/**
	 * 参加者変更定義制御処理
	 */
	var ChangeDef = {
		/**
		 * 初期処理
		 */
		init: function() {
			var $changeDef = $('#changeDef');
			$('div.collapse', $changeDef)
			.on('hide.bs.collapse', function() {
				$('i', '#changeDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#changeDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});

			$(document)
			.on('click', '#changeDefs [data-field=changeRoleName]', function() {
				var $tr = $(this).parent().parent();
				var uniqueKey = $tr.find('[data-field=uniqueKey]').text();
				if ($('#changeDefs tbody tr.info [data-field=uniqueKey]').text() === uniqueKey) return;

				$('#changeDefs tbody tr').removeClass('info');
				$tr.addClass('info');
				ChangeDef.refer(uniqueKey);
			})
			.on('click', '#changeDef button.btn-create', function() {
				var uniqueKey = $('span.component', $routeArea).filter('.selected').attr('id') || '';
				ChangeDef.create(uniqueKey);
			})
			.on('click', '#changeDef button.btn-update', function() {
				var uniqueKey = $('#changeDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ChangeDef.update(uniqueKey);
			})
			.on('click', '#changeDef button.btn-remove', function() {
				var uniqueKey = $('#changeDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ChangeDef.remove(uniqueKey);
			})
			;
		},
		/**
		 * 作成処理
		 */
		create: function(uniqueKey) {
			var activityDef = contents.activityDefs[uniqueKey];
			var params = {
				changeDef: {
					corporationCode: activityDef.corporationCode,
					processDefCode: activityDef.processDefCode,
					processDefDetailCode: activityDef.processDefDetailCode,
					activityDefCode: activityDef.activityDefCode
				}
			};
			Popup.open("../mm/mm0450.html", callbackFromMm0450, params);
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var $changeDef = $('#changeDef');
			var changeDef = contents.changeDefs[uniqueKey];
			if (changeDef) {
				$changeDef
				.find('input, select, textarea, span')
				.filter('[data-field]')
				.not('#changeDefs [data-field]')
				.each(function(i, elem) {
					var val = changeDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}
					} else {
						$(elem).text(val);
					}
				});
			}
		},
		/**
		 * 選択処理
		 */
		selected: function(parentUniqueKey, uniqueKey) {
			var array = [];
			$.each(contents.changeDefs, function(i, e) {if (i.indexOf(parentUniqueKey) === 0) {array.push(e);}});
			array.sort(function(f, s) {
				if (f['sortOrder'] != s['sortOrder'] && !f['sortOrder'] && f['sortOrder'] != 0) return 1;
				if (f['sortOrder'] != s['sortOrder'] && !s['sortOrder'] && s['sortOrder'] != 0) return -1;
				if (f['sortOrder'] > s['sortOrder']) return 1;
				if (f['sortOrder'] < s['sortOrder']) return -1;
				if (f['seqNoChangeDef'] > s['seqNoChangeDef']) return 1;
				if (f['seqNoChangeDef'] < s['seqNoChangeDef']) return -1;
				if (f['changeRoleCode'] > s['changeRoleCode']) return 1;
				if (f['changeRoleCode'] < s['changeRoleCode']) return -1;
				return 0;
			});
			changeDefs.fillTable(array);
			var $changeDef = $('#changeDef');
			var $tr = $('#changeDefs tbody tr', $changeDef);
			$('button.btn-update', $changeDef).prop('disabled', $tr.length == 0);
			$('button.btn-remove', $changeDef).prop('disabled', $tr.length == 0);
			$('button.btn-detail', $changeDef).prop('disabled', $tr.length == 0);
			$('div.collapse', $changeDef).collapse($tr.length == 0 ? 'hide' : 'show');

			if (!uniqueKey) uniqueKey = $tr.filter(':first').find('[data-field=uniqueKey]').text();
			$tr.find('[data-field=uniqueKey]').filter(function(){return $(this).text() === uniqueKey;}).parent().parent().find('a[data-field]').trigger('click');
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $changeDef = $('#changeDef');
			var $targets = $changeDef.find('input, select, textarea').not('#changeDefs [data-field]');
			if (!Validator.validate($targets, true)) return false;
			var changeDef = $.extend({}, contents.changeDefs[uniqueKey], NCI.toObjFromElements($targets.parent(), []));

			var msg = NCI.getMessage('MSG0071', '参加者');
			NCI.confirm(msg, function() {
				var params = {changeDef: changeDef};
				NCI.post('/mm0302/ChangeDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.changeDefs[uniqueKey] = res.changeDef;
						ChangeDef.selected(res.changeDef.activityDefUniqueKey, uniqueKey);
						Message.addMessage('success', ['参加者情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['参加者情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var changeDef = contents.changeDefs[uniqueKey];
			var msg = NCI.getMessage('MSG0072', '参加者');
			NCI.confirm(msg, function() {
				var params = {changeDef: changeDef};
				NCI.post('/mm0302/ChangeDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.changeDefs[res.changeDef.uniqueKey];
						ChangeDef.selected(res.changeDef.activityDefUniqueKey);
						Message.addMessage('success', ['参加者情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['参加者情報削除処理でエラーが発生しました。']);
					}
				});
			});
		}
	};
	/**
	 * アクション定義制御処理
	 */
	var ActionDef = {
		actions: [],
		/**
		 * 初期処理
		 */
		init: function() {
			var $actionDef = $('#actionDef');
			$('div.collapse', $actionDef)
			.on('hide.bs.collapse', function() {
				$('i', '#actionDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#actionDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});

			$(document)
			.on('click', '#actionDefs [data-field=actionName]', function() {
				var $tr = $(this).parent().parent();
				var uniqueKey = $tr.find('[data-field=uniqueKey]').text();
				if ($('#actionDefs tbody tr.info [data-field=uniqueKey]').text() === uniqueKey) return;
				$('#actionDefs tbody tr').removeClass('info');
				$tr.addClass('info');
				$('button.btn-remove', $actionDef).prop('disabled', contents.actionDefs[uniqueKey] && contents.actionDefs[uniqueKey].defaultFlag === '1');
				ActionDef.refer(uniqueKey);
				ConditionDef.selected(uniqueKey);
				FunctionDef.selected(uniqueKey);
			})
			.on('click', '#actionDef button.btn-create', function() {
				var uniqueKey = $('span.component', $routeArea).filter('.selected').attr('id') || '';
				ActionDef.create(uniqueKey);
			})
			.on('click', '#actionDef button.btn-update', function() {
				var uniqueKey = $('#actionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ActionDef.update(uniqueKey);
			})
			.on('click', '#actionDef button.btn-remove', function() {
				var uniqueKey = $('#actionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ActionDef.remove(uniqueKey);
			})
			;
		},
		/**
		 * デフォルトアクション取得
		 */
		getDefault: function(uniqueKey) {
			var result = null;
			$.each(contents.actionDefs, function(i, e) {
				if (i.indexOf(uniqueKey) === 0 && result == null) {
					result = i;
				} else if (i.indexOf(uniqueKey) === 0 && e.defaultFlag === '1') {
					result = i;
					return false;
				}
			});
			return result;
		},
		/**
		 * 選択処理
		 */
		selected: function(parentUniqueKey, uniqueKey) {
			var array = [];
			$.each(contents.actionDefs, function(i, e) {if (i.indexOf(parentUniqueKey) === 0) {array.push(e);}});
			array.sort(function(f, s) {
				if (f['sortOrder'] != s['sortOrder'] && !f['sortOrder'] && f['sortOrder'] != 0) return 1;
				if (f['sortOrder'] != s['sortOrder'] && !s['sortOrder'] && s['sortOrder'] != 0) return -1;
				if (f['sortOrder'] > s['sortOrder']) return 1;
				if (f['sortOrder'] < s['sortOrder']) return -1;
				// 降順(デフォルト優先)
				if (f['defaultFlag'] > s['defaultFlag']) return 1 * -1;
				if (f['defaultFlag'] < s['defaultFlag']) return -1 * -1;
				if (f['seqNoActionDef'] > s['seqNoActionDef']) return 1;
				if (f['seqNoActionDef'] < s['seqNoActionDef']) return -1;
				if (f['actionCode'] > s['actionCode']) return 1;
				if (f['actionCode'] < s['actionCode']) return -1;
				return 0;
			});
			actionDefs.fillTable(array);
			var $actionDef = $('#actionDef');
			var $tr = $('#actionDefs tbody tr', $actionDef);
			$('button.btn-update', $actionDef).prop('disabled', $tr.length == 0);
			$('button.btn-remove', $actionDef).prop('disabled', $tr.length == 0);
			$('button.btn-detail', $actionDef).prop('disabled', $tr.length == 0);
			$('div.collapse', $actionDef).collapse($tr.length == 0 ? 'hide' : 'show');
			$tr.find('span[data-field=actionType]').not(':contains(0)').parent().parent().find('input[type=radio]').remove();

			if (!uniqueKey) uniqueKey = $tr.filter(':first').find('[data-field=uniqueKey]').text();
			$tr.find('[data-field=uniqueKey]').filter(function(){return $(this).text() === uniqueKey;}).parent().parent().find('a[data-field]').trigger('click');
			return uniqueKey;
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var $actionDef = $('#actionDef');
			var actionDef = contents.actionDefs[uniqueKey];
			if (actionDef) {
				NCI.createOptionTags($('select[data-field=actionCode]'), ActionDef.actions.filter(function(e) {
					return actionDef.actionType !== '0' || (actionDef.actionType === '0' && e.actionType === '0');
				}));
				$actionDef
				.find('input, select, textarea, span')
				.filter('[data-field]')
				.not('#actionDefs [data-field]')
				.each(function(i, elem) {
					var val = actionDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}
					} else {
						$(elem).text(val);
					}
				});
			}
		},
		/**
		 * 作成処理
		 */
		create: function(uniqueKey) {
			var activityDef = contents.activityDefs[uniqueKey];
			var params = {
				actionDef: {
					corporationCode: activityDef.corporationCode,
					processDefCode: activityDef.processDefCode,
					processDefDetailCode: activityDef.processDefDetailCode,
					activityDefCode: activityDef.activityDefCode
				}
			};
			Popup.open("../mm/mm0307.html", callbackFromMm0307, params);
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $actionDef = $('#actionDef');
			var $targets = $actionDef.find('input, select, textarea').not('#actionDefs [data-field]');
			if (!Validator.validate($targets, true)) return false;
			var actionDef = $.extend({}, contents.actionDefs[uniqueKey], NCI.toObjFromElements($targets.parent(), []));
			$actionDef.find('input[type=checkbox]').filter('[data-field]').each(function(i, e) {if (!actionDef[$(e).attr('data-field')]) actionDef[$(e).attr('data-field')] = '0';});

			var defaultActionUniqueKey = $('#actionDefs tbody tr input[name=defaultFlag]:checked').parent().parent().find('[data-field=uniqueKey]').text();
			var activityDefUniqueKey = actionDef.activityDefUniqueKey;
			var actions = [];
			$.each(contents.actionDefs, function(i, e) {
				if (i.indexOf(activityDefUniqueKey) === 0) {
					if (e.actionType !== '0' && actionDef.uniqueKey === i) {
						actions.push(actionDef);
					} else if (e.actionType === '0') {
						var defaultFlag = defaultActionUniqueKey === i ? '1' : '0';
						var target = actionDef.uniqueKey === i ? actionDef : e;
						target.defaultFlag = defaultFlag;
						actions.push(target);
					}
				}
			});

			var msg = NCI.getMessage('MSG0071', NCI.getMessage('action'));
			NCI.confirm(msg, function() {
				var params = {actionDefs: actions};
				NCI.post('/mm0302/ActionDef/update', params).done(function(res) {
					if (res && res.success) {
						$.each(res.actionDefs, function(i, e) {contents.actionDefs[e.uniqueKey] = e;});
						ActivityDef.unselected();
						refresh();
						if (ActivityDef.selected(activityDefUniqueKey)) {
							ActionDef.selected(activityDefUniqueKey, uniqueKey);
							ConditionDef.selected(uniqueKey);
							FunctionDef.selected(uniqueKey);
						}
						Message.addMessage('success', ['アクション情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['アクション情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var actionDef = contents.actionDefs[uniqueKey];
			var msg = NCI.getMessage('MSG0072', NCI.getMessage('action'));
			NCI.confirm(msg, function() {
				var params = {
					actionDef: actionDef,
					conditionDefs: $.map(contents.conditionDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0) return v;}),
					functionDefs: $.map(contents.functionDefs, function(v, k) {if (k.indexOf(uniqueKey) === 0) return v;})
				};
				NCI.post('/mm0302/ActionDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.actionDefs[res.actionDef.uniqueKey];
						$.each(res.conditionDefs, function(i, e) {delete contents.conditionDefs[i];});
						$.each(res.functionDefs, function(i, e) {delete contents.functionDefs[i];});
						ActionDef.selected(res.actionDef.activityDefUniqueKey);
						Message.addMessage('success', ['アクション情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['アクション情報削除処理でエラーが発生しました。']);
					}
				});
			});
		}
	};
	/**
	 * 遷移先定義制御処理
	 */
	var ConditionDef = {
		/**
		 * 初期処理
		 */
		init: function() {
			var $conditionDef = $('#conditionDef');
			$('div.collapse', $conditionDef)
			.on('hide.bs.collapse', function() {
				$('i', '#conditionDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#conditionDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});

			$('#businessProcessStatusItems').empty();
			$.each($('select[data-field=businessProcessStatus] option'), function(i, e) {
				var $e = $(e);
				if ($e.val()) {
					$a = $('<a class="menuitem" tabindex="-1" href="javascript:void(0)"></a>');
					$a.text($e.text());
					$a.attr('data-code', $e.val());
					$('#businessProcessStatusItems').append($('<li></li>').append($a));
				}
			});
			$('#businessProcessStatusItems').find('li').hover(function() {$(this).addClass('active');}, function() {$(this).removeClass('active');});

			$(document)
			.on('click', '#conditionDefs [data-field=activityDefCodeTransitName]', function() {
				var $tr = $(this).parent().parent();
				var uniqueKey = $tr.find('[data-field=uniqueKey]').text();
				if ($('#conditionDefs tbody tr.info [data-field=uniqueKey]').text() === uniqueKey) return;

				$('#conditionDefs tbody tr').removeClass('info');
				$tr.addClass('info');
				ConditionDef.refer(uniqueKey);
			})
			.on('click', '#conditionDef button.btn-create', function() {
				var uniqueKey = $('#actionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ConditionDef.create(uniqueKey);
			})
			.on('click', '#conditionDef button.btn-update', function() {
				var uniqueKey = $('#conditionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ConditionDef.update(uniqueKey);
			})
			.on('click', '#conditionDef button.btn-remove', function() {
				var uniqueKey = $('#conditionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ConditionDef.remove(uniqueKey);
			})
			;
		},
		/**
		 * 遷移先接続初期処理
		 */
		initConnection: function(e, connection) {
			connection.setParameter('conditionDefUniqueKey', e.uniqueKey);
			if (e.expressionDefUniqueKey) {
				connection.setParameter('expressionDefUniqueKey', e.expressionDefUniqueKey);
				connection.getOverlay('label').addClass('label label-success label-expression');
				connection.getOverlay('label').setLabel(e.conditionExpressionAll);
				connection.getOverlay('label').show();
			} else {
				connection.setParameter('expressionDefUniqueKey', '');
				connection.getOverlay('label').removeClass('label label-success');
				connection.getOverlay('label').setLabel('');
				connection.getOverlay('label').hide();
			}
			jsPlumb.repaintEverything();
		},
		/**
		 * 接続処理
		 */
		connect: function(info) {
			if (ConditionDef.validate(info)) {
				var actionDefUniqueKey = info.connection.getParameter('actionDefUniqueKey').split(/:/g);
				var transitUniqueKey = info.dropEndpoint.getParameter('transitUniqueKey').split(/:/g);

				var conditionDef = {};
				conditionDef.corporationCode = actionDefUniqueKey[0];
				conditionDef.processDefCode = actionDefUniqueKey[1];
				conditionDef.processDefDetailCode = actionDefUniqueKey[2];
				conditionDef.activityDefCode = actionDefUniqueKey[3];
				conditionDef.seqNoActionDef = actionDefUniqueKey[4];
				conditionDef.corporationCodeTransit = transitUniqueKey[0];
				conditionDef.processDefCodeTransit = transitUniqueKey[1];
				conditionDef.processDefDetailCodeTran = transitUniqueKey[2];
				conditionDef.activityDefCodeTransit = transitUniqueKey[3];

				var params = {conditionDef: conditionDef};
				NCI.post('/mm0302/ConditionDef/create', params).done(function(res) {
					if (res && res.success) {
						contents.conditionDefs[res.conditionDef.uniqueKey] = res.conditionDef;
						var connection = jsPlumb.connect({uuids:[('R' + res.conditionDef.activityDefCode), ('L' + res.conditionDef.activityDefCodeTransit)]});
						ConditionDef.initConnection(res.conditionDef, connection);
						Message.addMessage('success', ['遷移先情報を作成しました。']);
					} else {
						Message.addMessage('danger', ['遷移先情報作成処理でエラーが発生しました。']);
					}
				});
			}
		},
		/**
		 * 入力チェック処理
		 */
		validate: function(info) {
			var sourceId = info.sourceId.split(/:/g)[3];
			var targetId = info.targetId.split(/:/g)[3];

			// 自分への遷移
			if (sourceId == targetId) {
				return false;
			}

			// 開始の場合
			if (sourceId == '0000000000') {
				if (targetId == '9999999999') {
					Message.addMessage('warning', ['開始から終了には遷移出来ません。']);
					return false;
				}
				if (targetId.indexOf('PARALLEL_') == 0) {
					Message.addMessage('warning', ['開始から並行処理には遷移出来ません。']);
					return false;
				}
			}
			// 並行開始の場合
			if (sourceId.indexOf('PARALLEL_S') == 0) {
				if (targetId == '9999999999') {
					Message.addMessage('warning', ['並行開始から終了には遷移出来ません。']);
					return false;
				}
				if (targetId.indexOf('PARALLEL_E') == 0) {
					Message.addMessage('warning', ['並行開始から並行終了には遷移出来ません。']);
					return false;
				}
			}
			// 並行終了の場合
			if (sourceId.indexOf('PARALLEL_E') == 0) {
				if (targetId.indexOf('PARALLEL_S') == 0) {
					Message.addMessage('warning', ['並行終了から並行開始には遷移出来ません。']);
					return false;
				}
				if (targetId.indexOf('PARALLEL_E') == 0) {
					Message.addMessage('warning', ['並行終了から並行終了には遷移出来ません。']);
					return false;
				}
			}
			// すでに登録済みの場合
			var connections = jsPlumb.getConnections();
			for (i = 0; i < connections.length; i++) {
				if (connections[i].sourceId === info.sourceId
						&& connections[i].targetId === info.targetId) {
					Message.addMessage('warning', ['追加済み遷移先です。']);
					return false;
				}
			}
			return true;
		},
		/**
		 * 接続解除処理
		 */
		unconnect: function(uniqueKey) {
			var msg = NCI.getMessage('MSG0072', '遷移先');
			NCI.confirm(msg, function() {
				var conditionDef = contents.conditionDefs[uniqueKey];
				var params = {conditionDef: conditionDef};
				NCI.post('/mm0302/ConditionDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.conditionDefs[res.conditionDef.uniqueKey];
						$.each(jsPlumb.getConnections(), function(i, e) {if (res.conditionDef.uniqueKey === e.getParameter('conditionDefUniqueKey')) jsPlumb.detach(e);});
						Message.addMessage('success', ['遷移先情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['遷移先情報削除処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 比較条件式更新処理
		 */
		updateExpression: function(uniqueKey, expressionDefCode) {
			var conditionDef = contents.conditionDefs[uniqueKey];
			conditionDef.expressionDefCode = expressionDefCode;

			var msg = NCI.getMessage('MSG0071', '遷移先条件式');
			NCI.confirm(msg, function() {
				var params = {conditionDef: conditionDef};
				NCI.post('/mm0302/ConditionDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.conditionDefs[uniqueKey] = res.conditionDef;
						$.each(jsPlumb.getConnections(), function(i, e) {if (res.conditionDef.uniqueKey === e.getParameter('conditionDefUniqueKey')) jsPlumb.detach(e);});
						var connection = jsPlumb.connect({uuids:[('R' + res.conditionDef.activityDefCode), ('L' + res.conditionDef.activityDefCodeTransit)]});
						if (connection) {
							ConditionDef.initConnection(res.conditionDef, connection);
						}

						var selectedUniqueKey = $('#conditionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
						if (selectedUniqueKey === uniqueKey) ConditionDef.refer(uniqueKey);

						Message.addMessage('success', ['遷移先条件式を更新しました。']);
					} else {
						Message.addMessage('danger', ['遷移先条件式更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 遷移状態更新処理
		 */
		updateBusinessProcessStatus: function(uniqueKey, businessProcessStatus) {
			var conditionDef = contents.conditionDefs[uniqueKey];
			conditionDef.businessProcessStatus = businessProcessStatus;

			var msg = NCI.getMessage('MSG0071', '遷移状態');
			NCI.confirm(msg, function() {
				var params = {conditionDef: conditionDef};
				NCI.post('/mm0302/ConditionDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.conditionDefs[uniqueKey] = res.conditionDef;
						var selectedUniqueKey = $('#conditionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
						if (selectedUniqueKey === uniqueKey) ConditionDef.refer(uniqueKey);

						Message.addMessage('success', ['遷移先遷移状態を更新しました。']);
					} else {
						Message.addMessage('danger', ['遷移先遷移状態更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 作成処理
		 */
		create: function(uniqueKey) {
			var actionDef = contents.actionDefs[uniqueKey];
			var activityDef = contents.activityDefs[actionDef.activityDefUniqueKey];
			var params = {
				activityDef: activityDef,
				conditionDef: {
					corporationCode: actionDef.corporationCode,
					processDefCode: actionDef.processDefCode,
					processDefDetailCode: actionDef.processDefDetailCode,
					activityDefCode: actionDef.activityDefCode,
					seqNoActionDef: actionDef.seqNoActionDef
				}
			};
			Popup.open("../mm/mm0308.html", callbackFromMm0308, params);
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var conditionDef = contents.conditionDefs[uniqueKey];
			var $conditionDef = $('#conditionDef');
			if (conditionDef) {
				$conditionDef
				.find('input, select, textarea, span')
				.filter('[data-field]')
				.not('#conditionDefs [data-field]')
				.each(function(i, elem) {
					var val = conditionDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}
					} else {
						$(elem).text(val);
					}
				});
			}
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $conditionDef = $('#conditionDef');
			var $targets = $conditionDef.find('input, select, textarea').not('#conditionDefs [data-field]');
			if (!Validator.validate($targets, true)) return false;
			var conditionDef = $.extend({}, contents.conditionDefs[uniqueKey], NCI.toObjFromElements($targets.parent(), []));
			$conditionDef.find('input[type=checkbox]').filter('[data-field]').each(function(i, e) {if (!conditionDef[$(e).attr('data-field')]) conditionDef[$(e).attr('data-field')] = '0';});
			var transitUniqueKey = conditionDef.transitUniqueKey.split(/:/g);
			conditionDef.corporationCodeTransit = transitUniqueKey[0];
			conditionDef.processDefCodeTransit = transitUniqueKey[1];
			conditionDef.processDefDetailCodeTran = transitUniqueKey[2];
			conditionDef.activityDefCodeTransit = transitUniqueKey[3];

			var msg = NCI.getMessage('MSG0071', '遷移先');
			NCI.confirm(msg, function() {
				var params = {conditionDef: conditionDef};
				NCI.post('/mm0302/ConditionDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.conditionDefs[uniqueKey] = res.conditionDef;
						if (contents.actionDefs[res.conditionDef.actionDefUniqueKey].defaultFlag === '1') {
							ActivityDef.unselected();
							refresh();
							ActivityDef.selected(res.conditionDef.activityDefUniqueKey);
						}
						ConditionDef.selected(res.conditionDef.actionDefUniqueKey, uniqueKey);
						Message.addMessage('success', ['遷移先情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['遷移先情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var msg = NCI.getMessage('MSG0072', '遷移先');
			NCI.confirm(msg, function() {
				var conditionDef = contents.conditionDefs[uniqueKey];
				var params = {conditionDef: conditionDef};
				NCI.post('/mm0302/ConditionDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.conditionDefs[res.conditionDef.uniqueKey];
						if (contents.actionDefs[res.conditionDef.actionDefUniqueKey].defaultFlag === '1') {
							$.each(jsPlumb.getConnections(), function(i, e) {if (res.conditionDef.uniqueKey === e.getParameter('conditionDefUniqueKey')) jsPlumb.detach(e);});
						}
						ConditionDef.selected(res.conditionDef.actionDefUniqueKey);
						Message.addMessage('success', ['遷移先情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['遷移先情報削除処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 選択処理
		 */
		selected: function(parentUniqueKey, uniqueKey) {
			var array = [];
			$.each(contents.conditionDefs, function(i, e) {if (i.indexOf(parentUniqueKey) === 0) {array.push(e);}});
			array.sort(function(f, s) {
				if (f['defaultFlag'] > s['defaultFlag']) return 1 * -1;
				if (f['defaultFlag'] < s['defaultFlag']) return -1 * -1;
				if (f['sortOrder'] != s['sortOrder'] && !f['sortOrder'] && f['sortOrder'] != 0) return 1;
				if (f['sortOrder'] != s['sortOrder'] && !s['sortOrder'] && s['sortOrder'] != 0) return -1;
				if (f['sortOrder'] > s['sortOrder']) return 1;
				if (f['sortOrder'] < s['sortOrder']) return -1;
				if (f['seqNoConditionDef'] > s['seqNoConditionDef']) return 1;
				if (f['seqNoConditionDef'] < s['seqNoConditionDef']) return -1;
				return 0;
			});
			conditionDefs.fillTable(array);

			var $conditionDef = $('#conditionDef');
			var $tr = $('#conditionDefs tbody tr', $conditionDef);
			$('button.btn-update', $conditionDef).prop('disabled', $tr.length == 0);
			$('button.btn-remove', $conditionDef).prop('disabled', $tr.length == 0);
			$('button.btn-detail', $conditionDef).prop('disabled', $tr.length == 0);
			$('div.collapse', $conditionDef).collapse($tr.length == 0 ? 'hide' : 'show');

			if (!uniqueKey) uniqueKey = $tr.filter(':first').find('[data-field=uniqueKey]').text();
			$tr.find('[data-field=uniqueKey]').filter(function(){return $(this).text() === uniqueKey;}).parent().parent().find('a[data-field]').trigger('click');
		}
	};
	/**
	 * 機能定義制御処理
	 */
	var FunctionDef = {
		/**
		 * 初期処理
		 */
		init: function() {
			var $functionDef = $('#functionDef');
			$('div.collapse', $functionDef)
			.on('hide.bs.collapse', function() {
				$('i', '#functionDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#functionDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});

			$(document)
			.on('click', '#functionDefs [data-field=functionName]', function() {
				var $tr = $(this).parent().parent();
				var uniqueKey = $tr.find('[data-field=uniqueKey]').text();
				if ($('#functionDefs tbody tr.info [data-field=uniqueKey]').text() === uniqueKey) return;

				$('#functionDefs tbody tr').removeClass('info');
				$tr.addClass('info');
				FunctionDef.refer(uniqueKey);
			})
			.on('click', '#functionDef button.btn-create', function() {
				var uniqueKey = $('#actionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				FunctionDef.create(uniqueKey);
			})
			.on('click', '#functionDef button.btn-update', function() {
				var uniqueKey = $('#functionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				FunctionDef.update(uniqueKey);
			})
			.on('click', '#functionDef button.btn-remove', function() {
				var uniqueKey = $('#functionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				FunctionDef.remove(uniqueKey);
			})
			;
		},
		/**
		 * 作成処理
		 */
		create: function(uniqueKey) {
			var actionDef = contents.actionDefs[uniqueKey];
			var params = {
				functionDef: {
					corporationCode: actionDef.corporationCode,
					processDefCode: actionDef.processDefCode,
					processDefDetailCode: actionDef.processDefDetailCode,
					activityDefCode: actionDef.activityDefCode,
					seqNoActionDef: actionDef.seqNoActionDef,
					functionExecutionOrder: $('#functionDefs tbody tr').length + 1
				}
			};
			Popup.open("../mm/mm0309.html", callbackFromMm0309, params);
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var $functionDef = $('#functionDef');
			var functionDef = contents.functionDefs[uniqueKey];
			if (functionDef) {
				$functionDef
				.find('input, select, textarea, span')
				.filter('[data-field]')
				.not('#functionDefs [data-field]')
				.each(function(i, elem) {
					var val = functionDef[$(elem).attr('data-field')];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						if (type === 'radio' || type === 'checkbox') {
							$(elem).prop('checked', $(elem).val() === val);
						} else {
							$(elem).val(val);
						}
					} else {
						$(elem).text(val);
					}
				});
			}
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $functionDef = $('#functionDef');
			var $targets = $functionDef.find('input, select, textarea').not('#functionDefs [data-field]');
			if (!Validator.validate($targets, true)) return false;
			var functionDef = $.extend({}, contents.functionDefs[uniqueKey], NCI.toObjFromElements($targets.parent(), []));
			$functionDef.find('input[type=checkbox]').filter('[data-field]').each(function(i, e) {if (!functionDef[$(e).attr('data-field')]) functionDef[$(e).attr('data-field')] = '0';});

			var msg = NCI.getMessage('MSG0071', NCI.getMessage('function'));
			NCI.confirm(msg, function() {
				var params = {functionDef: functionDef};
				NCI.post('/mm0302/FunctionDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.functionDefs[uniqueKey] = res.functionDef;
						FunctionDef.selected(res.functionDef.actionDefUniqueKey, uniqueKey);
						Message.addMessage('success', ['アクション機能情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['アクション機能情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var functionDef = contents.functionDefs[uniqueKey];
			var msg = NCI.getMessage('MSG0072', NCI.getMessage('function'));
			NCI.confirm(msg, function() {
				var params = {functionDef: functionDef};
				NCI.post('/mm0302/FunctionDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.functionDefs[res.functionDef.uniqueKey];
						FunctionDef.selected(res.functionDef.actionDefUniqueKey);
						Message.addMessage('success', ['アクション機能情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['アクション機能情報削除処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 選択処理
		 */
		selected: function(parentUniqueKey, uniqueKey) {
			var array = [];
			$.each(contents.functionDefs, function(i, e) {if (i.indexOf(parentUniqueKey) === 0) {array.push(e);}});
			array.sort(function(f, s) {
				if (f['functionExecutionOrder'] != s['functionExecutionOrder'] && !f['functionExecutionOrder'] && f['functionExecutionOrder'] != 0) return 1;
				if (f['functionExecutionOrder'] != s['functionExecutionOrder'] && !s['functionExecutionOrder'] && s['functionExecutionOrder'] != 0) return -1;
				if (f['functionExecutionOrder'] > s['functionExecutionOrder']) return 1;
				if (f['functionExecutionOrder'] < s['functionExecutionOrder']) return -1;
				if (f['sortOrder'] != s['sortOrder'] && !f['sortOrder'] && f['sortOrder'] != 0) return 1;
				if (f['sortOrder'] != s['sortOrder'] && !s['sortOrder'] && s['sortOrder'] != 0) return -1;
				if (f['sortOrder'] > s['sortOrder']) return 1;
				if (f['sortOrder'] < s['sortOrder']) return -1;
				if (f['seqNoFunctionDef'] > s['seqNoFunctionDef']) return 1;
				if (f['seqNoFunctionDef'] < s['seqNoFunctionDef']) return -1;
				return 0;
			});
			functionDefs.fillTable(array);
			var $functionDef = $('#functionDef');
			var $tr = $('#functionDefs tbody tr', $functionDef);
			$('button.btn-update', $functionDef).prop('disabled', $tr.length == 0);
			$('button.btn-remove', $functionDef).prop('disabled', $tr.length == 0);
			$('button.btn-detail', $functionDef).prop('disabled', $tr.length == 0);
			$('div.collapse', $functionDef).collapse($tr.length == 0 ? 'hide' : 'show');
			$('select[data-field=functionCode]', $functionDef).change();	// 選択行のツールチップを有効化

			if (!uniqueKey) uniqueKey = $tr.filter(':first').find('[data-field=uniqueKey]').text();
			$tr.find('[data-field=uniqueKey]').filter(function(){return $(this).text() === uniqueKey;}).parent().parent().find('a[data-field]').trigger('click');
		}
	};
	/**
	 * 比較条件式制御処理
	 */
	var ExpressionDef = {
		/**
		 * 演算子区分
		 */
		operatorTypes: [],
		/**
		 * 初期処理
		 */
		init: function() {
			var $expressionDef = $('#expressionDef');
			$('div.collapse', $expressionDef)
			.on('hide.bs.collapse', function() {
				$('i', '#expressionDef button.btn-detail').removeClass('glyphicon-collapse-up').addClass('glyphicon-collapse-down');
			})
			.on('show.bs.collapse', function() {
				$('i', '#expressionDef button.btn-detail').removeClass('glyphicon-collapse-down').addClass('glyphicon-collapse-up');
			});

			$(document)
			.on('click', '#expressionDefs [data-field=conditionExpressionAll]', function() {
				var $tr = $(this).parent().parent();
				var uniqueKey = $tr.find('[data-field=uniqueKey]').text();
				if ($('#expressionDefs tbody tr.info [data-field=uniqueKey]').text() === uniqueKey) return;

				$('#expressionDefs tbody tr').removeClass('info');
				$tr.addClass('info');
				ExpressionDef.refer(uniqueKey);
			})
			.on('click', '#expressionDef input[data-field=expressionLeftType]', function() {
				var uniqueKey = $('#expressionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				var $expressionDef = $('#expressionDef');

				var variables = $.map(contents.variableDefs, function(v, k) {return v;});
				var expressiones = $.map(contents.expressionDefs, function(v, k) {if (k !== uniqueKey) {return v;}})
				.sort(function(f, s) {
					if (f['expressionDefCode'] > s['expressionDefCode']) return 1;
					if (f['expressionDefCode'] < s['expressionDefCode']) return -1;
					return 0;
				});

				var val = $(this).val();
				NCI.createOptionTags($('[data-field=operatorType]', $expressionDef), $.grep(ExpressionDef.operatorTypes, function(e) {
					if (val === '1' && (e.value === '21' || e.value === '22')) return false;
					if (val === '2' && !(e.value === '21' || e.value === '22')) return false;
					return true;
				})).prop('selectedIndex', 0);

				if (val === '1') {
					NCI.createOptionTags($('[data-field=conditionExpressionLeft]', $expressionDef), variables).prop('selectedIndex', 0);
					NCI.createOptionTags($('[data-field=conditionExpressionRight]', $expressionDef), variables).prop('selectedIndex', 0);
					$('input[data-field=expressionRightType][value=1]', $expressionDef).trigger('click');
				} else {
					NCI.createOptionTags($('[data-field=conditionExpressionLeft]', $expressionDef), expressiones).prop('selectedIndex', 0);
					NCI.createOptionTags($('[data-field=conditionExpressionRight]', $expressionDef), expressiones).prop('selectedIndex', 0);
					$('input[data-field=expressionRightType][value=2]', $expressionDef).prop('disabled', false).trigger('click').prop('disabled', true);
				}
				$('[data-field=conditionExpressionAll]', $expressionDef).val('');
			})
			.on('click', '#expressionDef input[data-field=expressionRightType]', function() {
				var val = $(this).val();
				var $select = $('select[data-field=conditionExpressionRight]', $expressionDef);
				var $input = $('input[data-field=conditionExpressionRight]', $expressionDef);
				if (val === '1' || val === '2') {
					$select.removeClass('hide');
					$input.addClass('hide');
				} else if (val === '9') {
					$select.addClass('hide');
					$input.removeClass('hide');
				}
				$select.prop('selectedIndex', 0);
				$input.val('');

				$select.prop('disabled', val === '9');
				$input.prop('disabled', val !== '9')

				if (val === '2') {
					$(this).parent().parent().parent().addClass('hide');
				} else {
					$(this).parent().parent().parent().removeClass('hide');
				}
			})
			.on('click', '#expressionDef button.btn-create', function() {
				ExpressionDef.create();
			})
			.on('click', '#expressionDef button.btn-update', function() {
				var uniqueKey = $('#expressionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ExpressionDef.update(uniqueKey);
			})
			.on('click', '#expressionDef button.btn-remove', function() {
				var uniqueKey = $('#expressionDefs tbody tr.info [data-field=uniqueKey]').text() || '';
				ExpressionDef.remove(uniqueKey);
			})
			;
			ExpressionDef.refresh();
		},
		/**
		 * 比較条件式リフレッシュ処理
		 */
		refresh: function(uniqueKey) {
			var array = $.map(contents.expressionDefs, function(v, k) {return v;})
			.sort(function(f, s) {
				if (f['expressionDefCode'] > s['expressionDefCode']) return 1;
				if (f['expressionDefCode'] < s['expressionDefCode']) return -1;
				return 0;
			});
			$('#expressionDefItems').empty();
			$.each(array, function(i, e) {
				if (e.expressionDefCode) {
					$a = $('<a class="menuitem" tabindex="-1" href="javascript:void(0)"></a>');
					$a.text(e.conditionExpressionAll);
					$a.attr('data-code', e.expressionDefCode);
					$('#expressionDefItems').append($('<li></li>').append($a));
				}
			});
			$('#expressionDefItems').find('li').hover(function() {$(this).addClass('active');}, function() {$(this).removeClass('active');});

			expressionDefs.fillTable(array);
			var $expressionDef = $('#expressionDef');
			var $tr = $('#expressionDefs tbody tr', $expressionDef);
			$('button.btn-update', $expressionDef).prop('disabled', $tr.length == 0);
			$('button.btn-remove', $expressionDef).prop('disabled', $tr.length == 0);
			$('button.btn-detail', $expressionDef).prop('disabled', $tr.length == 0);
			$('div.collapse', $expressionDef).collapse($tr.length == 0 ? 'hide' : 'show');

			if (!uniqueKey) uniqueKey = $tr.filter(':first').find('[data-field=uniqueKey]').text();
			$tr.find('[data-field=uniqueKey]').filter(function(){return $(this).text() === uniqueKey;}).parent().parent().find('a[data-field]').trigger('click');

			array.unshift({value: '', label: '--'});
			NCI.createOptionTags($('[data-field=expressionDefCode]'), array);
		},
		/**
		 * 作成処理
		 */
		create: function() {
			var processDef = contents.processDef;
			var params = {
				expressionDef: {
					corporationCode: processDef.corporationCode,
					processDefCode: processDef.processDefCode,
					processDefDetailCode: processDef.processDefDetailCode
				}
			};
			Popup.open("../mm/mm0311.html", callbackFromMm0311, params);
		},
		/**
		 * 参照処理
		 */
		refer: function(uniqueKey) {
			var $expressionDef = $('#expressionDef');
			var expressionDef = contents.expressionDefs[uniqueKey];
			if (expressionDef) {
				var array = $.map(contents.expressionDefs, function(v, k) {if (k !== uniqueKey) {return v;}})
				.sort(function(f, s) {
						if (f['expressionDefCode'] > s['expressionDefCode']) return 1;
						if (f['expressionDefCode'] < s['expressionDefCode']) return -1;
						return 0;
				});

				$('input[data-field=expressionLeftType][value=' + expressionDef.expressionLeftType + ']', $expressionDef).trigger('click');
				$('[data-field=conditionExpressionLeft]', $expressionDef).val(expressionDef.conditionExpressionLeft);
				$('[data-field=operatorType]', $expressionDef).val(expressionDef.operatorType);

				if (expressionDef.expressionRightType == '2') {
					$('input[data-field=expressionRightType][value=' + expressionDef.expressionRightType + ']', $expressionDef).prop('disabled', false);
				}
				$('input[data-field=expressionRightType][value=' + expressionDef.expressionRightType + ']', $expressionDef).trigger('click');
				if (expressionDef.expressionRightType == '2') {
					$('input[data-field=expressionRightType][value=' + expressionDef.expressionRightType + ']', $expressionDef).prop('disabled', true);
				}
				if (expressionDef.expressionRightType === '9') {
					$('input[data-field=conditionExpressionRight]', $expressionDef).val(expressionDef.conditionExpressionRight);
				} else {
					$('select[data-field=conditionExpressionRight]', $expressionDef).val(expressionDef.conditionExpressionRight);
				}
				$('[data-field=conditionExpressionAll]', $expressionDef).val(expressionDef.conditionExpressionAll);
			}
		},
		/**
		 * 更新処理
		 */
		update: function(uniqueKey) {
			var $expressionDef = $('#expressionDef');
			var $targets = $expressionDef.find('input, select, textarea').not('#expressionDefs [data-field]');
			if (!Validator.validate($targets, true)) return false;

			var msg = NCI.getMessage('MSG0071', NCI.getMessage('conditionalExpression'));
			NCI.confirm(msg, function() {
				var expressionDef = $.extend({}, contents.expressionDefs[uniqueKey], NCI.toObjFromElements($targets.parent(), ['conditionExpressionRight']));
				expressionDef.conditionExpressionRight = $((expressionDef.expressionRightType === '9' ? 'input' : 'select') + '[data-field=conditionExpressionRight]').val();
				var conditions = [];
				$.each(contents.conditionDefs, function(i, e) {if (e.expressionDefUniqueKey === expressionDef.uniqueKey) conditions.push(e);});

				var params = {expressionDef: expressionDef, conditionDefs: conditions};
				NCI.post('/mm0302/ExpressionDef/update', params).done(function(res) {
					if (res && res.success) {
						contents.expressionDefs[uniqueKey] = res.expressionDef;

						// 選択されている条件式をバックアップ(参加者、遷移先)
						var assigned = $('#assignedDef [data-field=expressionDefCode]').val();
						var condition = $('#conditionDef [data-field=expressionDefCode]').val();
						ExpressionDef.refresh(uniqueKey);
						$('#assignedDef [data-field=expressionDefCode]').val(assigned);
						$('#conditionDef [data-field=expressionDefCode]').val(condition);

						$.each(res.conditionDefs, function(i, e) {
							contents.conditionDefs[e.uniqueKey] = e;
							$.each(jsPlumb.getConnections(), function(j, c) {if (e.uniqueKey === c.getParameter('conditionDefUniqueKey')) jsPlumb.detach(c);});
							var connection = jsPlumb.connect({uuids:[('R' + e.activityDefCode), ('L' + e.activityDefCodeTransit)]});
							if (connection) ConditionDef.initConnection(e, connection);
						});
						Message.addMessage('success', ['比較条件式情報を更新しました。']);
					} else {
						Message.addMessage('danger', ['比較条件式情報更新処理でエラーが発生しました。']);
					}
				});
			});
		},
		/**
		 * 削除処理
		 */
		remove: function(uniqueKey) {
			var expressionDef = contents.expressionDefs[uniqueKey];
			var msg = NCI.getMessage('MSG0072', NCI.getMessage('conditionalExpression'));
			NCI.confirm(msg, function() {
				var conditions = [];
				$.each(contents.conditionDefs, function(i, e) {if (e.expressionDefUniqueKey === expressionDef.uniqueKey) conditions.push(e);});
				var assignedes = [];
				$.each(contents.assignedDefs, function(i, e) {if (e.expressionDefUniqueKey === expressionDef.uniqueKey) assignedes.push(e);});

				var params = {
					expressionDef: expressionDef,
					conditionDefs: conditions,
					assignedDefs: assignedes
				};
				NCI.post('/mm0302/ExpressionDef/remove', params).done(function(res) {
					if (res && res.success) {
						delete contents.expressionDefs[res.expressionDef.uniqueKey];
						$.each(res.conditionDefs, function(i, e) {
							contents.conditionDefs[e.uniqueKey] = e;
							$.each(jsPlumb.getConnections(), function(j, c) {if (e.uniqueKey === c.getParameter('conditionDefUniqueKey')) jsPlumb.detach(c);});
							var connection = jsPlumb.connect({uuids:[('R' + e.activityDefCode), ('L' + e.activityDefCodeTransit)]});
							if (connection) ConditionDef.initConnection(e, connection);
						});
						$.each(res.assignedDefs, function(i, e) {contents.assignedDefs[e.uniqueKey] = e;});

						// 選択されている条件式をバックアップ(参加者、遷移先)
						var assigned = $('#assignedDef [data-field=expressionDefCode]').val();
						var condition = $('#conditionDef [data-field=expressionDefCode]').val();
						ExpressionDef.refresh();
						$('#assignedDef [data-field=expressionDefCode]').val(assigned);
						$('#conditionDef [data-field=expressionDefCode]').val(condition);
						if (assigned === res.expressionDef.expressionDefCode) $('#assignedDef [data-field=expressionDefCode]').prop('selectedIndex', 0);
						if (condition === res.expressionDef.expressionDefCode) $('#conditionDef [data-field=expressionDefCode]').prop('selectedIndex', 0);

						Message.addMessage('success', ['参加者情報を削除しました。']);
					} else {
						Message.addMessage('danger', ['参加者情報削除処理でエラーが発生しました。']);
					}
				});
			});
		}
	};

	/**
	 * メッセージ表示をカスタマイズ(標準のメッセージ出力場所を使用しない)
	 */
	var Message = {
		addMessage: function(cssClass, messages) {
			if (cssClass && messages && messages.length) {
				// DIVタグ
				var id = 'div-route-message';
				var $div = $('#' + id);
				if ($div.length === 0) $div = $('<div id="' + id + '" class="route-message alert alert-' + cssClass + '"></div>').appendTo($routeArea);

				// ULタグ
				var $ul = $div.find('ul');
				if ($ul.length === 0) $ul = $('<ul></ul>').appendTo($div);

				// LIタグ（実際のメッセージ）
				var li = document.createElement('li');
				var len = messages ? messages.length : 0;
				for (var i = 0; i < len; i++) {
					$(li.cloneNode(false)).text(messages[i]).appendTo($ul);
				}
				setTimeout(function() {$div.fadeOut('normal', function() {$div.remove();});}, 3000);
			}
		}
	};

	/**
	 * 初期化
	 */
	{
		// 初期化リクエストパラメータ
		var params = {
			corporationCode: NCI.getQueryString('corporationCode'),
			processDefCode: NCI.getQueryString('processDefCode'),
			processDefDetailCode: NCI.getQueryString('processDefDetailCode'),
			timestampUpdated: NCI.getQueryString('timestampUpdated'),
			messageCds : []
		};

		NCI.init('/mm0302/init', params).done(function(res) {
			if (res && res.success) {
				init(res);
				$('#contents-body').removeClass('hide');
				jsPlumb.repaintEverything();
			}
			$(document)
			.on('click', 'button.btn-back', function() {
				NCI.redirect('../mm/mm0300.html');
			});
		});
	};

	/**
	 * 参加者定義作成コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0305 = function(res) {
		if (res && res.success) {
			var $assignedDef = $('#assignedDef');
			var assignedDef = res.assignedDef;
			var uniqueKey = assignedDef.uniqueKey;
			contents.assignedDefs[uniqueKey] = assignedDef;
			AssignedDef.selected(assignedDef.activityDefUniqueKey, uniqueKey);
			Message.addMessage('success', ['参加者情報を作成しました。']);
		} else if (res && !res.success) {
			Message.addMessage('danger', ['参加者情報作成処理でエラーが発生しました。']);
		}
	};

	/**
	 * 参加者定義作成コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0450 = function(res) {
		if (res && res.success) {
			var $changeDef = $('#changeDef');
			var changeDef = res.changeDef;
			var uniqueKey = changeDef.uniqueKey;
			contents.changeDefs[uniqueKey] = changeDef;
			ChangeDef.selected(changeDef.activityDefUniqueKey, uniqueKey);
			Message.addMessage('success', ['参加者変更情報を作成しました。']);
		} else if (res && !res.success) {
			Message.addMessage('danger', ['参加者変更情報作成処理でエラーが発生しました。']);
		}
	};

	/**
	 * アクション定義作成コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0307 = function(res) {
		if (res && res.success) {
			var $actionDef = $('#actionDef');
			var actionDef = res.actionDef;
			var uniqueKey = actionDef.uniqueKey;
			contents.actionDefs[uniqueKey] = actionDef;
			ActionDef.selected(actionDef.activityDefUniqueKey, uniqueKey);
			ConditionDef.selected(uniqueKey);
			FunctionDef.selected(uniqueKey);
			Message.addMessage('success', ['アクション情報を作成しました。']);
		} else if (res && !res.success) {
			Message.addMessage('danger', ['アクション情報作成処理でエラーが発生しました。']);
		}
	};
	/**
	 * 遷移先定義作成コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0308 = function(res) {
		if (res && res.success) {
			var $conditionDef = $('#conditionDef');
			var conditionDef = res.conditionDef;
			var uniqueKey = conditionDef.uniqueKey;
			contents.conditionDefs[uniqueKey] = conditionDef;
			if (contents.actionDefs[conditionDef.actionDefUniqueKey].defaultFlag === '1') {
				ActivityDef.unselected();
				refresh();
				if (ActivityDef.selected(conditionDef.activityDefUniqueKey)) {
					ActionDef.selected(conditionDef.activityDefUniqueKey, conditionDef.actionDefUniqueKey);
					ConditionDef.selected(conditionDef.actionDefUniqueKey, uniqueKey);
				}
			} else {
				ConditionDef.selected(conditionDef.actionDefUniqueKey, uniqueKey);
			}
			Message.addMessage('success', ['遷移先情報を作成しました。']);
		} else if (res && !res.success) {
			Message.addMessage('danger', ['遷移先情報作成処理でエラーが発生しました。']);
		}
	};

	/**
	 * アクション機能定義作成コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0309 = function(res) {
		if (res && res.success) {
			var $functionDef = $('#functionDef');
			var functionDef =  res.functionDef
			var uniqueKey = functionDef.uniqueKey;
			contents.functionDefs[uniqueKey] = functionDef;
			FunctionDef.selected(functionDef.actionDefUniqueKey, uniqueKey);
			Message.addMessage('success', ['アクション機能情報を作成しました。']);
		} else if (res && !res.success) {
			Message.addMessage('danger', ['アクション機能情報作成処理でエラーが発生しました。']);
		}
	};

	/**
	 * 比較条件式定義作成及び遷移先設定コールバック
	 * ※iframeから呼び出すためグローバル化
	 */
	window.callbackFromMm0311 = function(res) {
		if (res && res.success) {
			var $expressionDef = $('#expressionDef');
			var expressionDef =  res.expressionDef
			var uniqueKey = expressionDef.uniqueKey;
			contents.expressionDefs[uniqueKey] = expressionDef;

			// 選択されている条件式をバックアップ(参加者、遷移先)
			var assigned = $('#assignedDef [data-field=expressionDefCode]').val();
			var condition = $('#conditionDef [data-field=expressionDefCode]').val();
			ExpressionDef.refresh(uniqueKey);
			$('#assignedDef [data-field=expressionDefCode]').val(assigned);
			$('#conditionDef [data-field=expressionDefCode]').val(condition);
			Message.addMessage('success', ['比較条件式を設定しました。']);
		} else if (res && !res.success) {
			Message.addMessage('danger', ['比較条件式設定処理でエラーが発生しました。']);
		}
	};
});
