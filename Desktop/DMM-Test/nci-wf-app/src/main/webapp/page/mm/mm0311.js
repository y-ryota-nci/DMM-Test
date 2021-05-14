$(function() {
	var scopeKey = NCI.getQueryString(FLUSH_SCOPE_KEY);
	var parentParams = NCI.flushScope(scopeKey);

	var params = {
		expressionDef: parentParams.expressionDef,
		messageCds : []
	};

	NCI.init('/mm0311/init', params).done(function(res) {
		var variables = null;
		var expressiones = null;
		var operatorTypes = null;

		$(document)
		.on('click', '#inputed input[name=expressionLeftType]', function() {
			var val = $(this).val();
			NCI.createOptionTags($('#operatorType'), $.grep(operatorTypes, function(e) {
				if (val === '1' && (e.value === '21' || e.value === '22')) return false;
				if (val === '2' && !(e.value === '21' || e.value === '22')) return false;
				return true;
			})).prop('selectedIndex', 0);

			if (val === '1') {
				NCI.createOptionTags($('#conditionExpressionLeft'), variables).prop('selectedIndex', 0);
				NCI.createOptionTags($('select[data-field=conditionExpressionRight]'), variables).prop('selectedIndex', 0);
				$('input[name=expressionRightType][value=1]').trigger('click');
			} else {
				NCI.createOptionTags($('#conditionExpressionLeft'), expressiones).prop('selectedIndex', 0);
				NCI.createOptionTags($('select[data-field=conditionExpressionRight]'), expressiones).prop('selectedIndex', 0);
				$('input[name=expressionRightType][value=2]').prop('disabled', false).trigger('click').prop('disabled', true);
			}
			$('#conditionExpressionAll').val('');
		})
		.on('click', '#inputed input[name=expressionRightType]', function() {
			Validator.hideBalloon($('#inputed [data-field=conditionExpressionRight]'));
			var val = $(this).val();
			var $select = $('select[data-field=conditionExpressionRight]');
			var $input = $('input[data-field=conditionExpressionRight]');
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
				$(this).parent().parent().addClass('hide');
				$(this).parent().parent().next('span.input-group-addon').addClass('hide');
			} else {
				$(this).parent().parent().removeClass('hide');
				$(this).parent().parent().next('span.input-group-addon').removeClass('hide');
			}
		})
		// 閉じるボタン押下
		.on('click', '#btnClose', function(ev) {
			Popup.close();
		})
		// 更新ボタン
		.on('click', '#btnCreate', function(ev) {
			var $root = $('#inputed');
			var $targets = $root.find('input, select, textarea');
			if (!Validator.validate($targets, true)) return false;

			var msg = NCI.getMessage('MSG0069', NCI.getMessage('conditionalExpression'));
			NCI.confirm(msg, function() {
				var expressionDef = NCI.toObjFromElements($root, ['conditionExpressionRight']);
				expressionDef.conditionExpressionRight = $((expressionDef.expressionRightType === '9' ? 'input' : 'select') + '[data-field=conditionExpressionRight]').val();

				var params = {expressionDef: expressionDef};
				NCI.post('/mm0311/create', params).done(function(res) {
					if (res && res.success && res.expressionDef) {
						Popup.close(res);
					}
				});
			});
		});
		if (res && res.success) {
			$('#btnCreate').prop('disabled', false);
			if (res.expressionDef) {
				variables = res.variableDefs;
				expressiones = res.expressionDefs;
				operatorTypes = res.operatorTypes;
				var $root = $('#inputed');
				NCI.toElementsFromObj(res.expressionDef, $root, []);
				$('#inputed input[name=expressionLeftType][value=2]').prop('disabled', expressiones.length == 0);
				$('#inputed input[name=expressionLeftType][value=' + res.expressionDef.expressionLeftType + ']').trigger('click');
				$('#inputed input[name=expressionRightType][value=' + res.expressionDef.expressionRightType + ']').trigger('click');
			}
		}
	});
});

