$(function() {
	let paramters = NCI.flushScope(NCI.getQueryString(FLUSH_SCOPE_KEY));
	let parasm = {
			messageCds : ["MSG0069", "processBbs"]
	};
	NCI.init("/cm0170/init").done(function(res) {
		if (res && res.success) {
			// プロセス掲示板メール区分の選択肢
			const APPROVED_USERS = "0";
			NCI.createOptionTags($('#processBbsMailType'), res.processBbsMailTypes).val(APPROVED_USERS);
			// 所属の選択肢
			NCI.createOptionTags($('#belong'), res.belongs);
			if (res.belongs.length > 0) {
				$('#belong').val(res.belongs[0].value);
			}

			const selector = '#uploadArea div.dragZone, #uploadArea input[type=file]';
			FileUploader.setup(selector, "/cm0170/upload", false, showResults);

			$('#attachFiles').parent().toggleClass('hide', $('#attachFiles a').length == 0);
			$(document)
			.on('click', '#updFile', function() {
				$('#uploadArea').find('input[type=file]').click();
			});

			$('#btnOK').click(update);
		}
		$('#btnClose').click(function() {
			Popup.close();
		})
	});

	/** コールバック関数を実行 */
	function update(ev) {
		// バリデーション
		let $inputs = $('input, select, textarea');
		if (!Validator.validate($inputs, true)) {
			return false;
		}
		let msg = NCI.getMessage('MSG0069', NCI.getMessage('processBbs'));
		NCI.confirm(msg, function() {
			// コールバック関数の呼び出し
			let processBbsInfo = NCI.toObjFromElements($('#inputed'));
			processBbsInfo.processBbsIdUp = paramters.processBbsIdUp;
			processBbsInfo.attachFiles = $.map($('#attachFiles a span[data-field=bbsAttachFileWfId]'), function(e, i) {return {bbsAttachFileWfId: parseInt($(e).text())};});
			Popup.close(processBbsInfo);
		});
	}

	function showResults(results) {
		let template = $('#template a')[0];
		$.each(results, function(i, result) {
			let $a = $(template.cloneNode(true));
			let url = '../../endpoint/cm0170/download?bbsAttachFileWfId=' + result.bbsAttachFileWfId;
			$a.attr('href', url);
			NCI.toElementsFromObj(result, $a);
			let fileType = result.fileType || '';
			if (fileType) {
				fileType = ('-' + fileType);
			}
			$a.find('span.file-icon').addClass('fa-file' + fileType + '-o');
			$('#attachFiles').append($a);
		});
		$('#attachFiles').parent().toggleClass('hide', $('#attachFiles a').length == 0);
	}
});

