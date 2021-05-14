$(function() {
	var $treeScreenDocLevelDef = $('#treeScreenDocLevelDef');
	$treeScreenDocLevelDef.jstree();
	var $confirmLevelDef = $('#confirmLevelDef');

	var corporationCode = NCI.getQueryString('corporationCode');
	var menuRoleCode = NCI.getQueryString('menuRoleCode');

	let params = {
		corporationCode: corporationCode,
		menuRoleCode: menuRoleCode,
		messageCds : ['MSG0003', 'MSG0069', 'accessibleManagement']
	};

	var init = function(res) {
		// 検索結果反映
		let pager = new Pager($('#searchResult'));
		pager.responsiveTable.modifyTR = function($tr, rowIndex, entity, labels, ignores) {
			let trClass = 'row-data-' + rowIndex;
			$tr.addClass(trClass);

			let from = $tr.find('input[data-field=validStartDate]').data('validate');
			from.to = ('tr.' + trClass + ' ' + from.to);
			$tr.find('input[data-field=validStartDate]').data(from);

			let to = $tr.find('input[data-field=validEndDate]').data('validate');
			to.from = ('tr.' + trClass + ' ' + to.from);
			$tr.find('input[data-field=validStartDate]').data(to);
		};
		pager.fillTable(res.accessibleDocs);
		NCI.ymdPicker($('#searchResult tbody').find('input.ymdPicker'));

		refreshTree(res.treeItems);
		var checked = $('#tblAccessibleDocs tbody input[type=checkbox]:checked').length === 0;
		$('#btnUpdate,#btnRemove').prop('disabled', checked);
	};

	var refreshTree = function(treeItems) {
		$treeScreenDocLevelDef.jstree().destroy();
		$treeScreenDocLevelDef
		.on('ready.jstree', function(ev, instance) {
			refreshTreeItem($treeScreenDocLevelDef.jstree().get_json('#'));
		})
		.on('changed.jstree', function(e, data) {
			var selected = $treeScreenDocLevelDef.jstree().get_selected();
			$('#btnOK').prop('disabled', selected.length === 0);
		})
		.jstree({
			core: {
				check_callback: true,
				data: treeItems
			},
			types: {
				'#': {
					'max_children': 1,
					'max_depth': -1,
					'valid_children': ['book', 'file']
				},
				'book': {
					'valid_children': ['book', 'file']
				},
				'file': {
					'valid_children': []
				}
			},
			checkbox: {
				keep_selected_style: false
			},
			plugins: ['types', 'checkbox']
		});
	};

	var refreshTreeItem = function(children) {
		if (!children || !children.length) return;
		for (var i in children) {
			var node = children[i];
			refreshTreeItem(node.children);
			if (node.type === 'book' && $treeScreenDocLevelDef.jstree().is_leaf(node.id)) $treeScreenDocLevelDef.jstree().delete_node(node.id);
		}
	};

	NCI.init('/dc0131/init', params).done(function(res) {
		if (res && res.success) {
			if (res.menuRole) {
				$('input, select, textarea, span', '#inputed').filter('[id]').each(function(i, elem) {
					var val = res.menuRole[elem.id];
					var tagName = elem.tagName, type = elem.type;
					if (tagName === 'INPUT' || tagName === 'SELECT' || tagName === 'TEXTAREA') {
						elem.value = val;
					} else {
						$(elem).text(val);
					}
				});
			}

			init(res);

			$(document)
			.on('change', '#tblAccessibleDocs tbody input[type=checkbox]', function() {
				var checked = $('#tblAccessibleDocs tbody input[type=checkbox]:checked').length === 0;
				$('#btnUpdate,#btnRemove').prop('disabled', checked);
			})
			.on('click', '#btnAdd', function(ev) {
				$confirmLevelDef.modal();
			})
			.on('click', '#btnUpdate', function(ev) {
				var checked = $('#tblAccessibleDocs tbody input[type=checkbox]:checked');
				if (!Validator.validate(checked.parent().parent().find('input[type=text]'), true)) return false;
				var msg = NCI.getMessage('MSG0071', NCI.getMessage('accessibleManagement'));
				NCI.confirm(msg, function() {
					var accessibleDocs = [];
					$.each(checked, function(i, e) {
						var $tr = $(e).parent().parent();
						var screenDocId = $tr.find('[data-field=screenDocId]').text();
						var accessibleDocId = $tr.find('[data-field=accessibleDocId]').text();
						var validStartDate = $tr.find('[data-field=validStartDate]').val();
						var validEndDate = $tr.find('[data-field=validEndDate]').val();
						accessibleDocs.push({
							corporationCode : corporationCode,
							menuRoleCode : menuRoleCode,
							screenDocId : screenDocId,
							accessibleDocId: accessibleDocId,
							validStartDate: validStartDate,
							validEndDate: validEndDate
						});

					});
					var params = {
						corporationCode: corporationCode,
						menuRoleCode: menuRoleCode,
						accessibleDocs: accessibleDocs
					};
					NCI.post('/dc0131/update', params).done(function(res) {
						if (res && res.success) {
							init(res);
						}
					});
				});
			})
			.on('click', '#btnRemove', function(ev) {
				var checked = $('#tblAccessibleDocs tbody input[type=checkbox]:checked');
				var msg = NCI.getMessage('MSG0072', NCI.getMessage('accessibleManagement'));
				NCI.confirm(msg, function() {
					var accessibleDocs = [];
					$.each(checked, function(i, e) {
						var $tr = $(e).parent().parent();
						var accessibleDocId = $tr.find('[data-field=accessibleDocId]').text();
						accessibleDocs.push({accessibleDocId: accessibleDocId});
					});
					var params = {
						corporationCode: corporationCode,
						menuRoleCode: menuRoleCode,
						accessibleDocs: accessibleDocs
					};
					NCI.post('/dc0131/remove', params).done(function(res) {
						if (res && res.success) {
							init(res);
						}
					});
				});
			})
			.on('click', '#btnBack', function(ev) {
				NCI.redirect("./dc0130.html");
			})
			.on('click', '#btnOK', function(e) {
				var selected = $treeScreenDocLevelDef.jstree().get_selected();
				Validator.hideBalloon($('i.jstree-checkbox:first', $confirmLevelDef));
				if (!selected || !selected.length) {
					Validator.showBalloon($('i.jstree-checkbox:first', $confirmLevelDef), NCI.getMessage('MSG0003', NCI.getMessage('accessibleManagement')));
					return false;
				}

				var accessibleDocs = [];
				$.each(selected, function(i, e) {
					var node = $treeScreenDocLevelDef.jstree().get_node(e);
					if (node.type === 'file') {
						accessibleDocs.push({
							corporationCode : corporationCode,
							menuRoleCode : menuRoleCode,
							screenDocId: node.data.screenDocId,
							validStartDate: node.data.validStartDate,
							validEndDate: node.data.validEndDate
						});
					}
				});

				var params = {
						corporationCode: corporationCode,
						menuRoleCode: menuRoleCode,
						accessibleDocs: accessibleDocs
				};
				NCI.post('/dc0131/create', params).done(function(res) {
					if (res && res.success) {
						init(res);
					}
					$confirmLevelDef.modal('hide');
				});
				return true;
			})
			.on('click', '#btnCancel', function(e) {
				$confirmLevelDef.modal('hide');
			})
			;
			$confirmLevelDef.on('hide.bs.modal', function() {
				Validator.hideBalloon($('i.jstree-checkbox:first', $confirmLevelDef));
			});
		}
	});
});
