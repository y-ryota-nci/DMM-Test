$(function() {
	var params = {
		catalogId : NCI.getQueryString("catalogId"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'MSG0208' ]
	};
	NCI.init("/ct0011/init", params).done(function(res) {
		if (res && res.success) {
			// カレンダー（年月日）
			NCI.ymdPicker($('input.ymdPicker'));

			// カタログカテゴリーの選択肢
			if (res.catalogCategories) {
				NCI.createOptionTags($('#catalogCategoryId'), res.catalogCategories);
			}
			// カタログ単位の選択肢
			if (res.catalogUnits) {
				NCI.createOptionTags($('#catalogUnitId'), res.catalogUnits);
			}
			// 消費税区分の選択肢
			if (res.salesTaxTypes) {
				NCI.createOptionTags($('#salesTaxType'), res.salesTaxTypes);
			}

			fillData(res);
		}

		$(document)
		// 戻るボタン押下
		.on('click', '#btnBack', function(ev) {
			NCI.redirect("./ct0010.html");
		})
		// 登録ボタン
		.on('click', '#btnRegister', function(ev) {
			if (!validate())
				return false;

			var msg = NCI.getMessage("MSG0069", "カタログ");
			if (NCI.confirm(msg, function() {
				var params2 = createParam();
				NCI.post("/ct0011/insert", params2).done(function(res) {
					if (res && res.success) {
						fillData(res);
					}
				});
			}));
		})
		// 更新ボタン
		.on('click', '#btnUpdate', function(ev) {
			if (!validate())
				return false;

			var msg = NCI.getMessage("MSG0071", "カタログ");
			if (NCI.confirm(msg, function() {
				var params2 = createParam();
				NCI.post("/ct0011/update", params2).done(function(res) {
					if (res && res.success) {
						fillData(res);
					}
				});
			}));
		})
		// 削除ボタン
		.on('click', '#btnDelete', function(ev) {
			var msg = NCI.getMessage("MSG0072", "カタログ");
			if (NCI.confirm(msg, function() {

				var params2 = {
					catalog : {catalogId: $('#catalogId').text(), version: $('#version').text()}
				};
				NCI.post("/ct0011/delete", params2).done(function(res) {
					if (res && res.success) {
						NCI.redirect("./ct0010.html");
					}
				});
			}));
		})
		.on('click', '#updFile', function() {
			$('#contents').find('input[type=file]').click();
		})
		.on('click', '#btnLineAdd', addRow)
		.on('click', '#btnLineRemove', removeRow)
		.on('click', 'button.btnSearchOrganization', function() {
			const params = null, corporationCode = $('#corporationCode').val();
			let url = "../cm/cm0020.html?initSearch=true";
			if (corporationCode)
				url += "&corporationCode=" + corporationCode;
			Popup.open(url, callbackFromPopup, params, this);
		})
		.on('click', 'button.btnClearOrganization', function() {
			const $tr = $(this).closest('tr');
			$tr.find('input[data-field=organizationCode],input[data-field=organizationName]').val('');
			$tr.find('span[data-field=corporationCode]').text('');
		})
		;

		const selector = '#contents div.dragZone, #contents input[type=file]';
		FileUploader.setup(selector, "/ct0011/upload", false, function(catalogImage) {
			if (catalogImage) {
				const fileName = catalogImage.fileName, pos = fileName.indexOf('.');
				const ext = (pos < 0 ? '' : fileName.substring(pos + 1));
				if (!/PNG|GIF|JPG|JPEG/i.test(ext)) {
					NCI.alert(NCI.getMessage('MSG0208', NCI.getMessage('picture')));
					return false;
				}
				drawImage(catalogImage.catalogImageId);
			}
		});
	});
	var usedDepartments = new ResponsiveTable($('div.used-departments', '#contents'));

	function fillData(res) {
		if (res.catalog) {
			var $root = $('div.tr:not(div.catalog-image,div.used-departments)', '#contents');
			NCI.toElementsFromObj(res.catalog, $root);
			if ($('#catalogId').text() !== '0') {
				$('#btnRegister').hide();
				$('#btnUpdate').show();
				$('#btnDelete').show();
			} else {
				$('#btnRegister').show();
				$('#btnUpdate').hide();
				$('#btnDelete').hide();
			}
		}

		drawImage(res.catalogImage ? res.catalogImage.catalogImageId : null);
		fillTable(res.catalogUsedDepartments);
		usedDepartments.$root.data('removeUsedDepartments', []);

		var editable = !res.catalog || res.catalog.catalogId == 0 || res.editable;
		$('#contents').find('input,select,textarea').prop('disabled', !editable);
		if (editable) {
			$('div.tr.catalog-image div.dragZone', '#contents').show();
		} else {
			$('div.tr.catalog-image div.dragZone', '#contents').hide();
		}
		$('#btnLineRemove').prop('disabled', !editable || $('table.responsive tbody tr', usedDepartments.$root).length == 0);
		$('button:not(#btnBack,#btnLineRemove)').prop('disabled', !editable);
	}

	function drawImage(catalogImageId) {
		var $imageArea = $('#imageArea');
		$imageArea.find('img').remove();

		var url = '../../assets/nci/images/noImage.png';
		if (catalogImageId) {
			url = '../../endpoint/ct0011/download/catalogImage?catalogImageId=' + catalogImageId;
			url += '&_tm=' + +new Date();
		}
		$imageArea.append('<img src="' + url + '" class="img-responsive img-thumbnail col-lg-6 col-md-6 col-sm-9 col-xs-12 ">');
		$('#catalogImageId').text(catalogImageId);
	}
	function fillTable(result) {
		usedDepartments.fillTable(result);
		$('table.responsive tbody tr', usedDepartments.$root).each(function(i, el) {
			var $tr = $(el);
			$tr.find('span.index').text(i + 1);
		});
	}
	function addRow() {
		var newRowIndex = $('table.responsive tbody tr', usedDepartments.$root).length + 1;
		usedDepartments.addRowResult({}).find('span.index').text(newRowIndex);
		$('#btnLineRemove').prop('disabled', $('table.responsive tbody tr', usedDepartments.$root).length == 0);
	}
	function removeRow() {
		var removes = usedDepartments.$root.data('removeUsedDepartments');
		var result = $.makeArray($('table.responsive tbody tr', usedDepartments.$root).map(function() {
			var $tr = $(this);
			var lineData = NCI.toObjFromElements($tr, []);
			if ($(this).find('input.selected').prop('checked')) {
				var $targets = $tr.find('input');
				Validator.hideBalloon($targets);
				if (lineData.catalogUsedDepartmentId
						&& lineData.catalogUsedDepartmentId != '0') {
					removes.push({catalogUsedDepartmentId: lineData.catalogUsedDepartmentId, version: lineData.version});
				}
			} else {
				return lineData;
			}
		}));
		fillTable(result);
		usedDepartments.$root.data('removeUsedDepartments', removes);
	}
	function callbackFromPopup(entity, trigger) {
		if (entity) {
			const $root = $(trigger).closest('tr');
			$root.find('span[data-field=corporationCode]').text(entity.corporationCode);
			$root.find('input[data-field=organizationCode]').val(entity.organizationCode);
			$root.find('input[data-field=organizationName]').val(entity.organizationName);
			$root.find('input').trigger('validate');
		}
	}
	function validate() {
		var $root = $('div.tr:not(div.catalog-image)', '#contents');
		var $targets = $root.find('input, select, textarea');
		if (!Validator.validate($targets, true))
			return false;

		Validator.hideBalloon();
		var doubleCheck = [];
		$('table.responsive tbody tr', usedDepartments.$root).each(function() {
			var $tr = $(this);
			var $target = $tr.find('input[data-field=organizationCode]');
			var lineData = NCI.toObjFromElements($tr, ['organizationName']);
			if (doubleCheck.indexOf(lineData.organizationCode) >= 0) {
				Validator.showBalloon($target, "利用部門が重複しています。");
			} else {
				doubleCheck.push(lineData.organizationCode);
			}
		});
		if (Validator.hasError())
			return false;

		return true;
	}
	function createParam() {
		var $root = $('div.tr:not(div.catalog-image,div.used-departments)', '#contents');
		var ignores = [];
		var params = {
			catalog : NCI.toObjFromElements($root, ignores),
			catalogImage : {catalogImageId: $('#catalogImageId').text()},
			catalogUsedDepartments : $.makeArray($('table.responsive tbody tr', usedDepartments.$root).map(function() {
				var $tr = $(this);
				var lineData = NCI.toObjFromElements($tr, ['organizationName']);
				if (!lineData.inChargeFlag) {
					lineData.inChargeFlag = '0';
				}
				return lineData;
			})),
			removeCatalogUsedDepartments : usedDepartments.$root.data('removeUsedDepartments')
		};
		if (!params.catalog.stockType) {
			params.catalog.stockType = '0';
		}
		return params;
	}
});
