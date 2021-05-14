// Stamp：vd0114.jsから呼び出される初期化ロジック
function initSpecific(ctx, design) {
	let $root = $('#vd0114_16');
	$('#stampFrameDisplaty').on('change', function() {
		if ($(this).prop('checked')) {
			$('#stampBorderColor').closest('span').removeClass('hide');
			$('#stampBorderColor').addClass('required');
		} else {
			$('#stampBorderColor').closest('span').addClass('hide');
			$('#stampBorderColor').removeClass('required');
			$('#stampBorderColor').val('');
		}
		Validator.hideBalloon($('#stampBorderColor'));
	});
	NCI.toElementsFromObj(design, $root);
	$('#stampFrameDisplaty').change();
}
