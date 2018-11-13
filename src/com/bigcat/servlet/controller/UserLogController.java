package com.bigcat.servlet.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigcat.data.http.HttpResponseMsg;
import com.bigcat.mysql.MainDBConnection;
import com.bigcat.mysql.table.MySqlUserMoneyLog;
import com.bigcat.mysql.table.MySqlUserTaskLog;

import net.sf.json.JSONObject;

public class UserLogController {

	private static final String action_queryAchievementLog = "queryAchievementLog";
	private static final String action_queryMoneyLog = "queryMoneyLog";
	private static final String action_queryTaskLog = "queryTaskLog";
	
	private static UserLogController sInstance = null;
	
	public static UserLogController getInstance() {
		if (UserLogController.sInstance == null) {
			UserLogController.sInstance = new UserLogController();
		}
		return UserLogController.sInstance;
	}
	
	public UserLogController() {
		// TODO Auto-generated constructor stub
	}
	
	public void doAction(HttpServletRequest request, HttpServletResponse response, String action) 
			throws ServletException, IOException {
		switch (action) {
		case action_queryAchievementLog:
			break;
		case action_queryTaskLog:
			queryTaskLog(request, response);
			break;
		case action_queryMoneyLog:
			queryMoneyLog(request, response);
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
	private void queryTaskLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String strUid = request.getParameter("uid");
		String strPageIdx = request.getParameter("pageIdx");
		String strPageSize = request.getParameter("pageSize");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int pageIdx = Integer.parseInt(strPageIdx);
			int pageSize = Integer.parseInt(strPageSize);
			long uid = Long.parseLong(strUid);
			mainDBConnection = new MainDBConnection();
			List<MySqlUserTaskLog> list = MySqlUserTaskLog.findByUserUid(mainDBConnection, uid, pageIdx, pageSize);
			msg.setCode(0);
			msg.setData(list);
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setCode(1);
			msg.setMessage(e.getMessage());
		} finally {
			// TODO: handle finally clause
			if (mainDBConnection != null) {
				try {
					mainDBConnection.unlockTable();
					mainDBConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
		}
	}
	
	private void queryMoneyLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String strUid = request.getParameter("uid");
		String strPageIdx = request.getParameter("pageIdx");
		String strPageSize = request.getParameter("pageSize");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int pageIdx = Integer.parseInt(strPageIdx);
			int pageSize = Integer.parseInt(strPageSize);
			long uid = Long.parseLong(strUid);
			mainDBConnection = new MainDBConnection();
			List<MySqlUserMoneyLog> list = MySqlUserMoneyLog.findByUserUid(mainDBConnection, uid, pageIdx, pageSize);
			msg.setCode(0);
			msg.setData(list);
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setCode(1);
			msg.setMessage(e.getMessage());
		} finally {
			// TODO: handle finally clause
			if (mainDBConnection != null) {
				try {
					mainDBConnection.unlockTable();
					mainDBConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
		}
	}
}
