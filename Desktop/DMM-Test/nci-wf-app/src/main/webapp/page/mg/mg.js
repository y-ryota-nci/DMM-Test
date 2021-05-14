var MstCommon = {

	// 基本情報
	baseRenderInfo : null,

	// パーツ要素
	partsElements : {},

	// パーツ作成
	createField : function (partsArray, entityObj, displayMode) {
		if ($.isArray(entityObj)) {
			$.each(entityObj, function(i, obj) {
				MstCommon.createParts(partsArray, MstCommon.baseRenderInfo, obj, i, displayMode);
			});
		} else {
			MstCommon.createParts(partsArray, MstCommon.baseRenderInfo, entityObj, -1, displayMode);
		}
	},

	// パーツ作成
	createParts : function (partsArray, listObj, entityObj, gridIndex, displayMode) {

		$.each(partsArray, function(i, parts) {
			//作成対象要素取得
			var $targetObj = $(parts['selectorKey']);

			if ($targetObj.size() > 1) {
				if ($targetObj.size() > gridIndex) {
					$targetObj = $($targetObj.get(gridIndex));
				} else {
					$targetObj = null;
				}
			}

			if ($targetObj) {
				var id = parts['id'];
				var className = parts['class'];
				var dataRole = parts['data-role'];
				var dataField = parts['data-field'];
				var dataValidate = parts['data-validate'];
				var option = null;

				if (dataValidate) {
					option = JSON.parse(dataValidate);
				}

				var readonly = parts['readonly'];
				var disabled = parts['disabled'];
				var listName = parts['listName'];
				var rows = parts['rows'];
				var style= parts['style'];

				if (displayMode == '0') {
					readonly = false;
					disabled = true;
				}

				var targetClass = '';
				var targetDataValidate = '';
				var targetMaxLength = '';
				var targetReadOnly = '';
				var targetDisabled = '';
				var targetStyle = '';
				var targetPattern = '';

				if (className) {
					if (displayMode == '0') {
						targetClass = 'class="' + className + ' displayReadonly" ';
					} else {
						targetClass = 'class="' + className + '" ';
					}
				} else if (displayMode == '0') {
					targetClass = 'class="displayReadonly" ';
				}

				if (dataValidate) {
					targetDataValidate = "data-validate=\'" + dataValidate + "\' ";

					if (option['maxlength']) {
						targetMaxLength = 'maxlength="' + option['maxlength'] + '" ';
					}
					if (option['pattern']) {
						targetPattern = option['pattern'];
					}
				}
				if (readonly || dataRole == 'numbering') {
					targetReadOnly = 'readonly="readonly" ';
				}
				if (disabled) {
					targetDisabled = 'disabled="disabled" ';
				}

				if (style) {
					targetStyle = 'style="' + style + '" ';
				}

				var html = '';

				// テキスト
				if (dataRole == 'text' || dataRole == 'numbering') {

					// テキストエリア
					if (rows) {
						// 値
						var targetVal = '';
						if (entityObj[dataField] != null && entityObj[dataField] != undefined) {
							targetVal = entityObj[dataField];
						}
						html = '<textarea rows="' + rows + '" ' + targetClass + targetDataValidate + targetMaxLength + ' data-role="' + dataRole + '" ' + targetReadOnly + targetDisabled + targetStyle + '>' + targetVal + '</textarea>';
						dataRole = 'textarea';

					// テキスト
					} else {
						// 値
						var targetVal = '';
						if (entityObj[dataField] != null && entityObj[dataField] != undefined) {
							targetVal = 'value="' + entityObj[dataField] + '" ';
						}
						html = '<input type="text" ' + targetClass + targetDataValidate + targetMaxLength + ' data-role="' + dataRole + '" ' + targetReadOnly + targetDisabled + targetVal + targetStyle + '/>';

						if (displayMode != '0' && option && option['pattern'] == 'date' && targetClass && targetClass.indexOf('ymdPicker') != -1) {
							html += '<i class="glyphicon glyphicon-calendar form-control-feedback"></i>';
						}
					}
					$targetObj.html(html);

				// ラジオボタン
				} else if (dataRole == 'radioCode') {
					// 値
					var targetVal = '';
					if (entityObj[dataField] != null && entityObj[dataField] != undefined) {
						targetVal = entityObj[dataField];
					}
					html = MstCommon.getRadioTag(dataField, listObj[listName], targetVal, disabled, gridIndex, targetStyle);
					$targetObj.html(html);

				// ドロップダウン
				} else if (dataRole == 'dropdownCode') {
					// 値
					var targetVal = '';
					if (entityObj[dataField] != null && entityObj[dataField] != undefined) {
						targetVal = entityObj[dataField];
					}
					html = '<select ' + targetStyle + targetClass.replace('form-control', '') + '></select>';
					if (displayMode == '0') {
						var dispVal = MstCommon.getDropdownDispValue(listObj[listName], targetVal);
						html = '<span class="form-control displayReadonly" ' + targetStyle + '>' + dispVal + '</span>';
						$targetObj.html(html);

					} else {
						$targetObj.html(html);

						NCI.createOptionTags($(' select', $targetObj), listObj[listName]).val(targetVal);

						if (disabled) {
							$(' select', $targetObj).attr('disabled','disabled');

						} else if (readonly) {
							$(' select>option', $targetObj).attr('readonly','readonly');
						}
					}

				// チェックボックス
				} else if (dataRole == 'check') {
					// 値
					var targetVal = '';
					if (entityObj[dataField] == '1') {
						targetVal = 'checked="checked" ';
					}

					var targetLabel = '';
					var checkLabel = parts['checkLabel'];
					if (checkLabel) {
						targetLabel = checkLabel;
					}

					html = '<label class="form-control-static"><input type="checkbox" data-role="check" ' + targetVal + ' ' + targetDisabled + targetStyle + '/>' + targetLabel + '</label>';

					$targetObj.html(html);

				// 隠しフィールド
				} else if (dataRole == 'hidden') {
					// 値
					var targetVal = '';
					if (entityObj[dataField] != null && entityObj[dataField] != undefined) {
						targetVal = entityObj[dataField];
					}
					$targetObj.val(targetVal);
				}

				if (!MstCommon.partsElements[dataField]) {
					MstCommon.partsElements[dataField] = new Array();
				}

				MstCommon.partsElements[dataField].push({'element':$targetObj, 'dataRole':dataRole, 'pattern':targetPattern});
			}
		});
	},

	// ラジオボタン生成
	getRadioTag : function (dataField, list, value, disabled, gridIndex, targetStyle) {
		var sql = '<div class="form-control-static">';
		$.each(list, function(i, item) {
			var checked = '';
			if (item.value == value) {
				checked = 'checked';
			}

			var targetName = '';

			if (gridIndex != -1) {
				targetName = 'name="' + dataField + '-' + (gridIndex+1) + '" ';
			} else {
				targetName = 'name="' + dataField + '" ';
			}

			sql +=
				'<label class="radio-inline">' +
				' <input type="radio" ' + targetName + 'data-field="' + dataField + '" value="' + item.value + '" data-role="radioCode" ' + checked + ' ' + (disabled ? 'disabled' : '') + ' ' + targetStyle + '/>' +
				' <span>' + item.label + '</span>' +
				'</label>';

		});
		sql += '</div>';
		return sql;
	},

	// 値取得
	getValue : function (dataField, gridIndex) {

		if (MstCommon.partsElements[dataField]) {
			var objArray = MstCommon.partsElements[dataField];
			var $targetObj = null;

			if (gridIndex != -1 && gridIndex != undefined) {
				$targetObj = objArray[gridIndex];
			} else {
				$targetObj = objArray[0];
			}

			if ($targetObj) {
				var $targetElement = $targetObj['element'];
				var dataRole = $targetObj['dataRole'];
				var pattern = $targetObj['pattern'];

				// テキスト
				if (dataRole == 'text' || dataRole == 'numbering') {
					if (pattern == 'integer') {
						var val = $('>input', $targetElement).val();
						return val.replace(/,/g, '');

					} else if (pattern == 'ym') {
						var val = $('>input', $targetElement).val();
						return val.replace('/', '');

					} else {
						return $('>input', $targetElement).val();
					}

				// テキストエリア
				} else if (dataRole == 'textarea') {
					return $('>textarea', $targetElement).val();

				// ラジオボタン
				} else if (dataRole == 'radioCode') {
					return MstCommon.getRadioValue($targetElement);

				// ドロップダウン
				} else if (dataRole == 'dropdownCode') {
					if (displayMode == '1' || displayMode == '2') {
						return $('>select', $targetElement).val();
					} else {
						return $('>span', $targetElement).html();
					}

				// チェックボックス
				} else if (dataRole == 'check') {
					return $(' input', $targetElement).get(0).checked ? '1' : '0';

				// 隠しフィールド
				} else if (dataRole == 'hidden') {
					return $targetElement.val();
				}
			}
		}
		return '';
	},

	// ラジオボタンの値取得
	getRadioValue : function ($targetObj) {
		if ($(' input', $targetObj).size() != 0) {
			for (var i=0; i<$(' input', $targetObj).size(); i++) {
				var item = $(' input', $targetObj).get(i);
				if (item.checked) {
					return $(item).val();
				}
			}
		}
		return '';
	},

	// 値設定
	setValue : function (dataField, value, gridIndex) {

		if (MstCommon.partsElements[dataField]) {
			var objArray = MstCommon.partsElements[dataField];
			var $targetObj = null;

			if (gridIndex != -1 && gridIndex != undefined) {
				$targetObj = objArray[gridIndex];
			} else {
				$targetObj = objArray[0];
			}

			if ($targetObj) {
				var $targetElement = $targetObj['element'];
				var dataRole = $targetObj['dataRole'];
				var pattern = $targetObj['pattern'];

				// テキスト
				if (dataRole == 'text' || dataRole == 'numbering') {
					if (pattern == 'integer') {
						$('>input', $targetElement).val(value);
						$('>input', $targetElement).blur();

					} else if (pattern == 'ym') {
						if (value && value.length > 4) {
							value = value.substring(0, 4) + '/' + value.substring(4);
						}
						$('>input', $targetElement).val(value);

					} else {
						$('>input', $targetElement).val(value);
					}

				// テキストエリア
				} else if (dataRole == 'textarea') {
					$('>textarea', $targetElement).val(value);

				// ラジオボタン
				} else if (dataRole == 'radioCode') {
					return MstCommon.setRadioValue($targetElement, value);

				// ドロップダウン
				} else if (dataRole == 'dropdownCode') {

					if (displayMode == '1' || displayMode == '2') {
						$('>select', $targetElement).val(value);
					} else {
						$('>span', $targetElement).html(value);
					}

				// チェックボックス
				} else if (dataRole == 'check') {
					$(' input', $targetElement).get(0).checked = (value == '1');

				// 隠しフィールド
				} else if (dataRole == 'hidden') {
					$targetElement.val(value);
				}
				return $targetElement;
			}
		}
		return null;
	},

	// ラジオボタンの値設定
	setRadioValue : function ($targetObj, value) {
		if ($(' input', $targetObj).size() != 0) {
			for (var i=0; i<$(' input', $targetObj).size(); i++) {
				var item = $(' input', $targetObj).get(i);
				item.checked = $(item).val() == value;
			}
		}
	},

	// ドロップダウン名称取得
	getDropdownDispValue : function(list, value) {
		if (list && list.length > 0) {
			for (var i=0; i<list.length; i++) {
				var item = list[i];
				if (item.value == value) {
					return item.label;
				}
			}
		}
		return '';
	},

	// 年月フォーマット
	formatYearMonth : function (id) {
		var val = $('#' + id).val();

		if (val && val.indexOf('/') == -1 && val.length > 4) {
			var left = val.substring(0, 4);
			var right = val.substring(4);

			if (right.length == 1) {
				right = '0' + right;
			}

			$('#' + id).val(left + '/' + right);
		}
	}
};