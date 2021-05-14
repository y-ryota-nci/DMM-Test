$(function() {
	const params = { "messageCds" : ['masterInitValue'] };
	NCI.init('/mm0500/init', params).done(function(res) {
		if (res && res.success) {
			init(res);

			$(document).on('click', 'input[type=checkbox]', onChecked);
			$('#btnUpdate').click(update);
			$('#srcCorporationCode').change(onChange)
			$('#btnSelectAll').click(function(ev) { selectAll(true); });
			$('#btnUnselectAll').click(function(ev) { selectAll(false); });
		}
	});

	/** 初期化 */
	function init(res) {
		NCI.createOptionTags($('#srcCorporationCode'), res.srcCorporations).val(res.srcCorporationCode);


		const $root = $('#inputed');
		const tbl = new ResponsiveTable($root);
		$root.find('table.responsive tbody').empty();

		$.each(res.entities, function(i, entity) {
			const $tr = tbl.addRowResult(entity);
			$tr.find('input[type=checkbox]')
				.prop('disabled', !entity.selectable)
				.prop('checked', entity.selectable);
		});

		onChecked();
	}

	/** コピー元企業の変更時 */
	function onChange(ev) {
		const params = { "srcCorporationCode" : $('#srcCorporationCode').val() };
		NCI.post('/mm0500/change', params).done(function(res) {
			if (res && res.success) {
				init(res);
			}
		});
	}

	/** 行の選択時 */
	function onChecked() {
		const checked = $('input[type=checkbox]:checked').length > 0;
		$('#btnUpdate').prop('disabled', !checked);
	}

	/** 更新 */
	function update(ev) {
		const $targets = $('#srcCorporationCode');
		if (!Validator.validate($targets, true)) {
			return false;
		}

		NCI.clearMessage();
		const msg = NCI.getMessage('MSG0071', NCI.getMessage('masterInitValue'));
		NCI.confirm(msg, function() {
			const destCorporationCodes = [];
			$('input[type=checkbox]:checked').each(function(i, checkbox) {
				const $tr = $(checkbox).closest('tr');
				destCorporationCodes.push($tr.find('span[data-field=corporationCode]').text());
			});
			const params = {
					"srcCorporationCode" : $('#srcCorporationCode').val(),
					"destCorporationCodes" : destCorporationCodes
			};
			NCI.post('/mm0500/save', params).done(function(res) {
				init(res);
			});
		});
	}

	function selectAll(check) {
		$('tbody input[type=checkbox].selectable:enabled').prop('checked', check);
	}
});
