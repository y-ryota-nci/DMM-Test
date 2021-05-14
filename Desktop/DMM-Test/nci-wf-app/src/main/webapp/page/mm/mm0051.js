$(function() {
	var initParams = {
		corporationCode : NCI.getQueryString("corporationCode"),
		partsNumberingFormatCode : NCI.getQueryString("partsNumberingFormatCode"),
		version : NCI.getQueryString("version"),
		messageCds : [ 'MSG0069', 'MSG0071', 'MSG0072', 'numberingFormat', 'grouping' ]
	};
	NCI.init("/mm0051/init", initParams).done(function(res) {
		if (res && res.success) {
			// 削除区分の選択肢
			if (res.deleteFlagList) {
				NCI.createOptionTags($('#deleteFlag'), res.deleteFlagList);
			}
			if (res.numberingFormatTypes) {
				NCI.createOptionTags($('select.formatType'), res.numberingFormatTypes);
			}
			if (res.entity) {

				mm0051.numberingFormatOrgs = res.numberingFormatOrgs;
				mm0051.numberingFormatDates = res.numberingFormatDates;
				mm0051.sequenceList = res.sequenceList;

				// DBの値を反映その１
				NCI.toElementsFromObj(res.entity, $('#formCondition'));
				// 書式区分に連動する書式値のエレメントを生成
				$('select.formatType').each(function(i, formatType) {
					mm0051.createFormatValue(formatType);
				});
				NCI.toElementsFromObj(res.entity, $('#formCondition'));

				let isNew = !res.entity.partsNumberingFormatId;
				$("#deleteFlag").prop('disabled', isNew);
				$('#btnRegister').toggle(isNew).prop('disabled', !isNew);
				$('#btnUpdate').toggle(!isNew).prop('disabled', isNew);
				let isDelete = res.entity.deleteFlag == '1';
				$("#partsNumberingFormatCode").prop('disabled', isDelete || !isNew);
				$("#partsNumberingFormatName").prop('disabled', isDelete);
			}


			// 登録ボタン
			$('#btnRegister').click(function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0069", NCI.getMessage("numberingFormat"));
				if (NCI.confirm(msg, function() {
					let params2 = {
						"input" : NCI.toObjFromElements($('#formCondition'))
					};
					NCI.post("/mm0051/insert", params2).done(function(res) {
						if (res && res.success && res.entity) {
							$('#btnRegister').hide();
							$('#btnUpdate').prop('disabled', false);
							$("#partsNumberingFormatCode").prop('disabled', true);
							$("#partsNumberingFormatName").prop('disabled', true);
							$("#deleteFlag").prop('disabled', true);
						}
					});
				}));
			});

			// 更新ボタン
			$('#btnUpdate').click(function(ev) {
				var $root = $('#inputed');
				var $targets = $root.find('input, select, textarea');
				if (!Validator.validate($targets, true))
					return false;

				var msg = NCI.getMessage("MSG0071", NCI.getMessage("numberingFormat"));
				if (NCI.confirm(msg, function() {
					var params2 = {
						"input" : NCI.toObjFromElements($('#formCondition'))
					};
					NCI.post("/mm0051/update", params2).done(function(res) {
						if (res && res.success && res.numberingFormat) {
						}
					});
				}));
			});

			// 連番設定ボタン
			$('#btnSequenceSetting').click(function(ev) {
				Popup.open('./mm0052.html', mm0051.fromMM0052);
			}).prop('disabled', false);

			// 書式区分
			$(document)
				.on('change', '.formatType', function(ev) {
					mm0051.createFormatValue(this);
					mm0051.makeFormat();
				})

				.on('change', '.formatValue', function(){
					mm0051.makeFormat();
				});
		}
		// 戻るボタン押下
		$('#btnBack').on('click', function(ev) {
			NCI.redirect("./mm0050.html");
		})
	});
});
var mm0051 = {
	numberingFormatOrgs : null,
	numberingFormatDates : null,
	sequenceList : null,

	makeFormat : function (){
		var formatValue = "";
		for (var i = 1; i < 10; i = i + 1){
			var formatType = $('#formatType' + i).val() || '';
			// 固定文字列
			if (formatType === 'L') {
				formatValue += ($('#formatValue' + i).val() || '');
			// システム日付
			} else if (formatType === 'T') {
				formatValue += "{T:" +  ($('#formatValue' + i).val() || '') + "}";
			// 連番
			} else if (formatType === 'S') {
				formatValue += "{S:" + ($('#formatValue' + i).val() || '') + "}";
			// ログイン者組織
			} else if (formatType === 'O') {
				formatValue += "{O:" + ($('#formatValue' + i).val() || '') + "}";
			// 会社コード(下2桁)
			} else if (formatType === 'C') {
				formatValue += "{C}";
			}
		}
		$('#numberingFormat').val(formatValue);
	},

	createFormatValue : function (formatType) {
		let $formatType = $(formatType);
		let $parent = $formatType.closest('.searchConditionTd');
		$parent.find('.formatValue,.groupingFlag,.groupingFlagLabel').remove();
		// 固定文字列
		const val = formatType.value;
		const no = formatType.id.slice(-1);
		const id = 'formatValue' + no;
		let $formatValue = $('#' + id);
		const oldValue = $formatValue.val();
		if (val === 'L') {
			$formatValue = $('<input id="' + id + '" type="text" class="formatValue required" maxlength="30" />').appendTo($parent);
		// システム日付
		} else if (val === 'T') {
			$formatValue = $('<select id="' + id + '" class="formatValue required"></select>').appendTo($parent);
			NCI.createOptionTags($formatValue, mm0051.numberingFormatDates);
		// 通し番号
		} else if (val === 'S') {
			$formatValue = $('<select id="' + id + '" class="formatValue required"></select>').appendTo($parent);
			NCI.createOptionTags($formatValue, mm0051.sequenceList);
		// ログイン者組織
		} else if (val === 'O') {
			$formatValue = $('<select id="' + id + '" class="formatValue required"></select>').appendTo($parent);
			NCI.createOptionTags($formatValue, mm0051.numberingFormatOrgs);
		}

		if (typeof oldValue !== 'undefined') {
			$formatValue.val(oldValue);
		}

		// グループ化対象
		if (val === 'T' || val === 'O') {
			$(document.createElement('input'))
				.attr({
					id : 'groupingFlag' + no,
					type : 'checkbox',
					class : 'groupingFlag',
					value : '1'
				})
				.appendTo($parent);

			$(document.createElement('label'))
				.attr({
					'for': ('groupingFlag' + no),
					class: 'groupingFlagLabel'
				})
				.text(NCI.getMessage('grouping'))
				.appendTo($parent);
		}
	},

	// 連番設定画面からのコールバック
	fromMM0052 : function() {
		const params = {
			corporationCode : NCI.getQueryString("corporationCode"),
			partsNumberingFormatCode : NCI.getQueryString("partsNumberingFormatCode"),
			version : NCI.getQueryString("version"),
		};
		NCI.post('/mm0051/getSequenceList', params).done(function(res) {
			mm0051.sequenceList = res.sequenceList;
			$('select.formatType').each(function(i, formatType) {
				// 通し番号の選択肢をリフレッシュ
				if (formatType.value === 'S') {
					mm0051.createFormatValue(formatType);
				}
			});
			mm0051.makeFormat();
		});
	}
}
