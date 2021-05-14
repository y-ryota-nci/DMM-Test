$(function() {
	let params = { messageCds : ['MSG0069', 'businessInfoName'] };
	NCI.init('/mm0030/init', params).done(function(res) {
		if (res && res.success) {
			init(res);

			// 更新ボタン押下
			$('#btnUpdate').on('click', function(ev) {
				update();
			});

			// リセットボタン押下
			$('#btnInitialize').on('click', function(ev) {
				initialize();
			});

		}
	});
});

function init(res) {
	NCI.createOptionTags($('select.businessInfoType'), res.businessInfoTypes);
	NCI.createOptionTags($('select.validFlag'), res.validFlags);
	NCI.createOptionTags($('select.screenPartsInputFlag'), res.screenPartsInputFlags);
	NCI.createOptionTags($('select.dataType'), res.dataTypes);
	// 検索結果反映
	new Pager($('#searchResult')).fillTable(res.businessInfoNames);

	if (res.businessInfoNames && res.businessInfoNames.length > 0) {
		if (res.businessInfoNames.length == 254) {
			$('#btnInitialize').prop('disabled', true);
		} else {
			$('#btnInitialize').prop('disabled', false);
		}
		$('#btnUpdate').prop('disabled', false);
	} else {
		$('#btnInitialize').prop('disabled', false);
		$('#btnUpdate').prop('disabled', true);
	}
}

// 更新処理
function update() {
	let $targets = $('input, select');
	if (!Validator.validate($targets, true)) {
		return false;
	}

	let msg = NCI.getMessage('MSG0069', NCI.getMessage('businessInfoName'));
	NCI.confirm(msg, function() {
		let businessInfoNameList = [];
		$('#tblBusinessInfoNames tbody tr').each(function(i, tr) {
			let $tr = $(tr);
			businessInfoNameList.push({
				  "businessInfoNameId"		 : $tr.find('[data-field=businessInfoNameId]').text()
				, "corporationCode"			 : $tr.find('[data-field=corporationCode]').text()
				, "businessInfoCode"		 : $tr.find('[data-field=businessInfoCode]').val()
				, "businessInfoName"		 : $tr.find('[data-field=businessInfoName]').val()
				, "businessInfoType"		 : $tr.find('[data-field=businessInfoType]').val()
				, "validFlag"				 : $tr.find('[data-field=validFlag]').val()
				, "screenPartsInputFlag"	 : $tr.find('[data-field=screenPartsInputFlag]').val()
				, "dataType"				 : $tr.find('[data-field=dataType]').val()
				, "sortOrder"				 : $tr.find('[data-field=sortOrder]').val()
				, "deleteFlag"				 : $tr.find('[data-field=deleteFlag]').text()
				, "version"					 : $tr.find('[data-field=version]').text()
				, "corporationCodeCreated"	 : $tr.find('[data-field=corporationCodeCreated]').text()
				, "userCodeCreated"			 : $tr.find('[data-field=userCodeCreated]').text()
				, "timestampCreated"		 : $tr.find('[data-field=timestampCreated]').text()
				, "corporationCodeUpdated"	 : $tr.find('[data-field=corporationCodeUpdated]').text()
				, "userCodeUpdated"			 : $tr.find('[data-field=userCodeUpdated]').text()
				, "timestampUpdated"		 : $tr.find('[data-field=timestampUpdated]').text()
			});
		});
		// 更新処理
		let params = {
			"businessInfoNameList" : businessInfoNameList
		};
		NCI.post('/mm0030/save', params).done(function(res) {
			$('#btnUpdate').prop('disabled', false);
			init(res);
		});
	});
}

//登録処理
function initialize() {
	let msg = NCI.getMessage('MSG0071', NCI.getMessage('businessInfoName'));
	NCI.confirm(msg, function() {
		// 更新処理
		let params = {};
		NCI.post('/mm0030/reset', params).done(function(res) {
			init(res);
		});
	});

}