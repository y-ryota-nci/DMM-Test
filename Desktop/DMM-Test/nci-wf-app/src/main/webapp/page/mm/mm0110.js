$(function() {
	let params = {
			'corporationCode' : NCI.getQueryString('corporationCode'),
			'messageCds': [ 'MSG0258' ]
	};
	NCI.init('/mm0110/init', params).done(function(res) {
		if (res && res.success) {
			// 企業の選択肢
			NCI.createOptionTags($('#corporationCode'), res.corporations).val(res.corporationCode);

			// カラーピッカー
			$('input.color-picker').each(function(i, elem) {
				$(elem).minicolors({
					swatches : [
						'#000000', '#696969', '#808080', '#a9a9a9', '#d3d3d3', '#f5f5f5', '#ffffff', /* black ～ white */
						'#0000ff' /* blue */, '#008000' /* green */, '#ffff00' /* yellow */, '#00bfff' /* deepskyblue */,
						'#ff0000' /* red */,	'#ffa500' /* orange */, '#8a2be2' /* blueviolet */,
					],
					format : 'hex',
					theme : 'bootstrap'
				});
			});

			// ASP管理者ならデフォルト値を編集可能
			$('[data-field=defaultValue]').prop('disabled', !NCI.loginInfo.aspAdmin);

			init(res);

			$('#btnUpdate').click(update).prop('disabled', false);
			$('#corporationCode').change(reload);
			$('#btnDownload').click(function() {
				NCI.confirm(NCI.getMessage('MSG0258'), function() {
					const corporationCode = $('#corporationCode').val();
					NCI.download('/mm0110/download/' + corporationCode);
				});
			}).prop('disabled', false);
		}
	});

	// システムプロパティを画面に反映
	function init(res) {
		$('#tblInput>tbody>tr').each(function(i, tr) {
			let $tr = $(tr);
			// 対象行のプロパティコードから、システムプロパティを特定してデータを反映
			let $propertyCode = $tr.find('td[data-field=propertyCode]');
			let propertyCode = $.trim($propertyCode.text());
			$.each(res.props, function(ii, prop) {
				if (prop.propertyCode === propertyCode) {
					NCI.toElementsFromObj(prop, $tr);

					if ($tr.find('td[data-field=corporationCode]').length === 0) {
						$('<td class="hide">').attr('data-field', 'corporationCode').appendTo($tr);
						$('<td class="hide">').attr('data-field', 'corporationTimestampUpdated').text(prop.corporationTimestampUpdated).appendTo($tr);
						$('<td class="hide">').attr('data-field', 'defaultTimestampUpdated').appendTo($tr);
					}
					$tr.find('td[data-field=corporationCode]').text(prop.corporationCode);
					$tr.find('td[data-field=corporationTimestampUpdated]').text(prop.corporationTimestampUpdated);
					$tr.find('td[data-field=defaultTimestampUpdated]').text(prop.defaultTimestampUpdated);
				}
			});
		});
	}

	function reload(ev) {
		let params = { "corporationCode" : $(ev.target).val() };
		NCI.post('/mm0110/search', params).done(function(res) {
			if (res && res.success) {
				init(res);
			}
		})
	}

	function update(ev) {
		// バリデーション
		if (!Validator.validate($('input, select'), true)) {
			return false;
		}
		// 確認
		let msg = NCI.getMessage('MSG0071', NCI.getMessage('systemProperty'));
		if (NCI.confirm(msg, function() {
			let inputs = [];
			$('#tblInput>tbody>tr').each(function(i, tr) {
				let $tr = $(tr);
				inputs.push(NCI.toObjFromElements($tr));
			});
			// 更新処理
			let params = {
					"corporationCode" : $('#corporationCode').val(),
					"inputs" : inputs
			};
			NCI.post('/mm0110/save', params).done(function(res) {
				if (res && res.success) {
					// 再表示
					init(res);
				}
			});
		}));
	}
});
