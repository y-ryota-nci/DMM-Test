$(function() {
	let params = {
		corporationCode : NCI.getQueryString("corporationCode"),
		menuRoleCode : NCI.getQueryString("menuRoleCode"),
		messageCds : ['MSG0069', 'menuRole']
	};
	NCI.init('/rm0111/init', params).done(function(res) {
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

			// 登録ボタン押下
			$('#btnUpdate').on('click', function(ev) {
				register(params.corporationCode, params.menuRoleCode);
			})
			// 戻るボタン押下
			$('#btnBack').on('click', function(ev) {
				NCI.redirect("./rm0100.html");
			})
			;
		}
	});
});

function init(res) {
	// 検索結果反映
	new Pager($('#searchResult')).fillTable(res.accessibleMenus);
}

// 登録処理
function register(corporationCode, menuRoleCode) {
	let $targets = $('input, select');
	if (!Validator.validate($targets, true)) {
		return false;
	}

	let msg = NCI.getMessage('MSG0069', NCI.getMessage('accessibleMenu'));
	NCI.confirm(msg, function() {
		let accessibleMenuList = [];
		$('#tblAccessibleMenus tbody tr').each(function(i, tr) {
			let $tr = $(tr);
			accessibleMenuList.push({
				  "exist"				 : $tr.find('[data-field=exist]').prop('checked')
				, "menuId"				 : $tr.find('[data-field=menuId]').text()
			});
		});
		// 更新処理
		let params = {
			"corporationCode"	 : corporationCode,
			"menuRoleCode" 		 : menuRoleCode,
			"accessibleMenuList" : accessibleMenuList
		};
		NCI.post('/rm0111/save', params).done(function(res) {
			init(res);
		});
	});
}