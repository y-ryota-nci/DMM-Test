$(function() {

	var params = {
			messageCds: []
	};

	NCI.init("/vd0311/init", params).done(function(res, textStatus, jqXHR) {
		if (res && res.success) {
			var argument = NCI.flushScope('argument');
			vd0311.init(argument);

			// 戻るボタン押下
			$(document).on('click', 'button.btnBack', function(ev) {
				TrayHelper.backToTray(argument.trayType);
			})
			// 連続処理ボタン押下
			.on('click', '#btnContinuousProcessing', function(ev) {
				const next = vd0311.processList.removeCurrent()
				if (next) {
					redirectVd0310(next.corporationCode, next.processId, next.activityId,
							next.timestampUpdated, next.proxyUser, next.trayType, vd0311.processList.targets);
				}
			})
			;
		}
	});

});

var vd0311 = {
	/** 連続処理用のプロセスリスト */
	processList : null,

	/**
	 * 初期処理
	 */
	init: function(argument) {
		const arg = argument || {};
		$('#subject').text(arg.subject);
		$('.subject').toggleClass('hide', (arg.subject == null));

		$('#applicationNo').text(arg.applicationNo);
		$('.applicationNo').toggleClass('hide', (arg.applicationNo == null));

		$('#approvalNo').text(arg.approvalNo);
		$('.approvalNo').toggleClass('hide', (arg.approvalNo == null));

		// 連続処理用の初期化
		vd0311.processList = new ProcessList(
				arg.corporationCode,
				arg.processId,
				arg.activityId,
				arg.processTargets);
		const hasNext = vd0311.processList.hasNext();
		$('#btnContinuousProcessing').toggleClass('hide', !hasNext);
	}
};