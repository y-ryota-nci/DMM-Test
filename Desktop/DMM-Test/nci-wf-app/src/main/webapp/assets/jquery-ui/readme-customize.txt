jquery-ui は座標計算やドラッグ＆ドロップ用に導入しているが、
Widget関連はUIがレスポンシブにそぐわないので、カスタマイズして
jquery-ui.jsから削除している。
具体的なカスタマイズ内容は下記の通り。
・Core：すべて
・Interactions：すべて
・Widgets：Mouse/Dialogのみ(付随して Button/Checkboxradio/Controlgroupも)
・Effects：すべて
・Theme： No Theme