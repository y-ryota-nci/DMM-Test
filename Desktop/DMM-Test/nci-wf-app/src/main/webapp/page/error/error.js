	// なるべくPureJavascriptで。
	function _toTopPage() {
		if (typeof Popup !== 'undefined' && Popup.isPopup())
			Popup.close();
		else {
			window.location.replace("../" + document.getElementById('___topPageUrl___').textContent);
		}
	}

	function _isPopup() {
		return (typeof Popup !== 'undefined' && Popup.isPopup())
	}

	$(function() {
		$('#sc').find('[data-i18n]').each(function(i, elem) {
			const key = $(elem).data('i18n');
			const value = NCI.getMessage(key);
			if (value)
				$(elem).text(value);
		});
		if (_isPopup())
			$('#___btnTopPage___').text(NCI.getMessage('btnClose'));
	});
