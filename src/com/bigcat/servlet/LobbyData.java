package com.bigcat.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigcat.data.http.HttpResponseMsg;
import com.bigcat.servlet.controller.AwardController;
import com.bigcat.servlet.controller.TaskController;
import com.bigcat.utils.UrlUtils;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class TaskList
 */
@WebServlet("/LobbyData")
public class LobbyData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String controller_award = "award";
	private static final String controller_task = "task";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LobbyData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	throw new ServletException("GET method used with " +
    		     getClass( ).getName( )+": POST method required.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Content-Encoding", "utf-8");
		response.setCharacterEncoding("utf-8");
		String[] methods = UrlUtils.getMethodPath(request.getQueryString());
		if (methods == null) {
			HttpResponseMsg msg = new HttpResponseMsg();
			msg.setCode(1);
			msg.setData(null);
			msg.setMessage("method is null");
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
			return;
		}
		switch (methods[0]) {
		case controller_award:
			AwardController.getInstance().doAction(request, response, methods[1]);
			break;
		case controller_task:
			TaskController.getInstance().doAction(request, response, methods[1]);
			break;
		default:
			HttpResponseMsg msg = new HttpResponseMsg();
			msg.setCode(1);
			msg.setData(null);
			msg.setMessage("invalid method");
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
			break;
		}
	}

}
