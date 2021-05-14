package jp.co.nci.iwf.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GetCssChangeFlagServlet extends HttpServlet {
	/** HTTPリクエスト */
	@Inject
	protected HttpServletRequest hsr;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = hsr.getSession(false);

		// CSS切替対象会社コードリストを取得する。
		List<String> targetList = (List<String>)session.getAttribute("TARGET_LIST");

		// 返却用のJSON返却用マップを定義する。
		Map<String, List<String>> jsonMap = new HashMap<String, List<String>>();

		// CSS切替対象会社コードリストをJSON返却用マップに設定する。
		jsonMap.put("targetList", targetList);

		ObjectMapper mapper = new ObjectMapper();

		// JSON返却用マップをJSON文字列に変換する。
		String jsonStr = mapper.writeValueAsString(jsonMap);

		// ヘッダの設定を行う。
		res.setContentType("application/json;charset=UTF-8");

		// PrintWriterオブジェクトを生成する。
		PrintWriter pw = res.getWriter();

		// JSON文字列を出力する。
		pw.print(jsonStr);

		// PrintWriterをクローズする。
		pw.close();
	}

}
