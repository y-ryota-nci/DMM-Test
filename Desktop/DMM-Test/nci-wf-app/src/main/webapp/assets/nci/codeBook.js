/** パーツ種別の定数 */
const PartsType = {
	  TEXTBOX     : 1	// 1:テキストボックス
	, LABEL       : 2	// 2:ラベル
	, CHECKBOX    : 3	// 3:チェックボックス
//	, CALENDAR    : 4	// 4:カレンダー
	, RADIO       : 5	// 5:ラジオボタン
	, DROPDOWN    : 6	// 6:ドロップダウンリスト
	, NUMBERING   : 7	// 7:採番
	, ORGANIZATION: 8	// 8:組織選択
	, USER        : 9	// 9:ユーザー選択
	, SEARCH_BUTTON:12	// 12:検索ボタン
	, EVENT_BUTTON: 13	// 13:イベントボタン
	, IMAGE       : 14	// 14:画像
	, GRID        : 15	// 15:グリッド（可変テーブル）
	, STAMP       : 16	// 16：スタンプ
	, HYPERLINK   : 17	// 17：ハイパーリンク
	, ATTACHFILE  : 20	// 20:添付ファイル
	, MASTER      : 50	// 50:マスタ選択
	, STAND_ALONE : 51	// 51:独立画面パーツ
	, REPEATER    : 52	// 52:リピーター
};
// こう書くことでPartsTypeの中の値は変更不可となる
// もちろんPartsTypeはconstなので書換え不可
Object.freeze(PartsType);

/** 入力値の型の定数 */
const PartsInputType = {
	  TEXT     : 1	// 1:文字
	, TEXTAREA : 2	// 2:文章 */
	, CLOB     : 3	// 3:文章（CLOB) */
	, NUMBER   : 4	// 4:数値 */
	, DATE     : 5	// 5:日付 */
};
Object.freeze(PartsInputType);

/** アクション区分の定数 */
const ActionType = {
	  NORMAL:       '0'	// 状態遷移
	, PULLBACK:     '1'	// 引戻
	, SENDBACK:     '2'	// 差戻
	, CANCEL:       '3'	// 取消
	, REQUEST:      '4'	// 要説明
	, APPEND_INFO:  '9'	// 説明回答
	, WF_LINK:      'W'	// ワークフロー間遷移
	, WF_LINK_C:    'C'	// ワークフロー間遷移(差戻)
	, SAVE:         'S'	// 保存
	, DOACTION:     'A'	// アクション機能実行
	, SENDBACK_NC:  'B'	// 差戻(チェックなし)
	, PULLFORWARD:  'F'	// 引上
};
Object.freeze(ActionType);

/** パーツ条件区分の定数 */
const PartsConditionType = {
	  ENABLED:  '1'	//有効条件
	, VISIBLED: '2'	//可視条件
	, READONLY: '3'	//読取専用条件
};
Object.freeze(PartsConditionType);

/** 表示条件設定の定数 */
const DcType = {
	UKNOWN : 0		// 0:未定義（パーツ表示条件が生成されていない）
	, INPUTABLE : 1	// 1:入力可
	, READONLY : 2	// 2:入力不可
	, HIDDEN : 3	// 3:非表示
};
Object.freeze(DcType);
