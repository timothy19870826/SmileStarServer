package com.bigcat.servlet.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigcat.data.http.HttpResponseMsg;
import com.bigcat.mysql.MainDBConnection;
import com.bigcat.mysql.table.MySqlAcountData;
import com.bigcat.mysql.table.MySqlTable;
import com.bigcat.mysql.table.MySqlTaskData;
import com.bigcat.mysql.table.MySqlUserMoneyLog;
import com.bigcat.mysql.table.MySqlUserTaskLog;

import net.sf.json.JSONObject;

public class TaskController {
	
	private static final String action_add = "add";
	private static final String action_queryPage = "queryPage";
	private static final String action_receive = "receive";
	private static final String action_close = "close";
	
	private static TaskController sInstance = null;
	
	public static TaskController getInstance() {
		if (TaskController.sInstance == null) {
			TaskController.sInstance = new TaskController();
		}
		return TaskController.sInstance;
	}
	
	public void doAction(HttpServletRequest request, HttpServletResponse response, String action) 
			throws ServletException, IOException {
		switch (action) {
		case action_add:
			addTask(request, response);
			break;
		case action_close:
			closeTask(request, response);
			break;
		case action_queryPage:
			queryPage(request, response);
			break;
		case action_receive:
			receiveTask(request, response);
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
	
	private void addTask(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String desc = request.getParameter("desc");
		String publisher = request.getParameter("publisher");
		String strAwardVal = request.getParameter("awardVal");
		String strNeedPerson = request.getParameter("needPerson");
		String strExpire = request.getParameter("expire");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int awardVal = Integer.parseInt(strAwardVal);
			int needPerson = Integer.parseInt(strNeedPerson);
			short expire = Short.parseShort(strExpire);
			mainDBConnection = new MainDBConnection();
			MySqlTaskData.addTask(mainDBConnection, name, desc, publisher, awardVal, needPerson, expire);
			msg.setCode(0);
			msg.setData(true);
			
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
	
	private void closeTask(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String strTaskId = request.getParameter("taskId");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			int taskId = Integer.parseInt(strTaskId);
			MySqlTaskData.closeTask(mainDBConnection, taskId);
			MySqlTaskData mySqlTaskData = MySqlTable.findItemById(
					mainDBConnection, MySqlTaskData.class, MySqlTaskData.tableName, taskId);
			MySqlUserTaskLog.completedTaskLog(mainDBConnection, taskId);
			List<MySqlUserTaskLog> list = MySqlUserTaskLog.findByTaskId(mainDBConnection, taskId);
			MySqlAcountData mySqlAcountData = null;
			MySqlUserTaskLog mySqlUserTaskLog = null;
			mainDBConnection.lockRead(MySqlAcountData.tableName);
			for (int i = list.size() - 1; i >= 0; i--) {
				mySqlUserTaskLog = list.get(i);
				mySqlAcountData = MySqlAcountData.findByUid(mainDBConnection, mySqlUserTaskLog.getUid());
				MySqlUserMoneyLog.addLog(mainDBConnection, mySqlAcountData.getUid(), 0, mySqlTaskData.getAwardVal(), 
						mySqlAcountData.getCapitalWeibi() + mySqlTaskData.getAwardVal(), 
						String.format("完成%s", mySqlTaskData.getName()));
				MySqlAcountData.updateCapitalWeibi(
						mainDBConnection, mySqlAcountData.getUid(), mySqlAcountData.getCapitalWeibi() + mySqlTaskData.getAwardVal());
			}
			msg.setCode(0);
			msg.setData(mySqlTaskData);
			
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
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
	
	private void receiveTask(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {
		String strTaskId = request.getParameter("taskId");
		String strUid = request.getParameter("uid");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			mainDBConnection.lockWrite(MySqlTaskData.tableName);
			int taskId = Integer.parseInt(strTaskId);
			long uid = Long.parseLong(strUid);
			MySqlTaskData mySqlTaskData = MySqlTable.findItemById(mainDBConnection, MySqlTaskData.class, MySqlTaskData.tableName, taskId);
			if (mySqlTaskData.getCurPerson() < mySqlTaskData.getNeedPerson()) {
				mySqlTaskData.setCurPerson((short) (mySqlTaskData.getCurPerson() + 1));
				MySqlTaskData.updateTask(mainDBConnection, taskId, mySqlTaskData.getCurPerson());
				MySqlUserTaskLog.addLog(mainDBConnection, uid, taskId, 1, mySqlTaskData.getName());
				msg.setCode(0);
				msg.setData(mySqlTaskData);
			}
			else {
				msg.setCode(1);
				msg.setData("Member Enough");
			}
			
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
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
	
	private void queryPage(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {

		String strPageIdx = request.getParameter("pageIdx");
		String strPageSize = request.getParameter("pageSize");
		String strActive = request.getParameter("isActive");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int pageIdx = Integer.parseInt(strPageIdx);
			int pageSize = Integer.parseInt(strPageSize);
			mainDBConnection = new MainDBConnection();
			List<MySqlTaskData> list = MySqlTaskData.queryPage(mainDBConnection, pageIdx * pageSize, pageSize, strActive != null && strActive == "1");
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
