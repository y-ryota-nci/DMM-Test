"Bootstrap-Honoka" は日本語も美しく表示できるBootstrapテーマです。
HonokaはBootstrapを元に製作されているため、非常に高い互換性を持っています。マークアップに関する規約はほとんど変わりません。
本家Bootstrapでは指定されていない日本語フォントに関する指定が行われているため、美しく日本語を表示できます。

	http://honokak.osaka/


■変更点
(a)bootstrap 3.3.7の .modal()の座標がずれるバグ対応
	bootstrap.js の .modal()を使用すると、スクロールバーの分だけ(?)左にコンテンツ全体がズレる不具合への対処をしている
	(.modal()を呼び出すたびに bodyのpadding-rightに17pxずつが追加されていってしまう)
	下記を参考に修正すること
		http://tech.mirukome.com/modal_bootstrap_issue/

		(1) app.css に下記を追記
			.modal-open {
				overflow: auto;
			}
		(2) bootstrap.jsを修正
			Modal.prototype.setScrollbar = function () {
				// ▼下記コメントアウト
				//var bodyPad = parseInt((this.$body.css(‘padding-right’) || 0), 10)
				//if (this.bodyIsOverflowing) this.$body.css(‘padding-right’, bodyPad + this.scrollbarWidth)
				// ▲下記コメントアウト
			}
(b) .dropdown()で、コンテンツ部分にselectタグを含んでいると選択肢を変えられないバグ対応
	input|textareaはclick対応されているが、selectタグは対象外だったので、clearMenus()関数内のclick判定をinput|textarea|selectに変更
