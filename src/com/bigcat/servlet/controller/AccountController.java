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
import com.bigcat.mysql.table.MySqlAdminData;
import com.bigcat.mysql.table.MySqlGlobalInfo;
import com.bigcat.utils.Md5Utils;

import net.sf.json.JSONObject;

public class AccountController {
	private static final String actionLogin = "login";
	private static final String actionRegister = "register";
	private static final String actionQueryPage = "queryPage";
	private static final String actionAdminLogin = "adminLogin";
	
	private static AccountController sInstance = null;
	
	public static AccountController getInstance() {
		if (AccountController.sInstance == null) {
			AccountController.sInstance = new AccountController();
		}
		return AccountController.sInstance;
	}

	public AccountController() {
		// TODO Auto-generated constructor stub
	}
	
	public void doAction(HttpServletRequest request, HttpServletResponse response, String action) 
			throws ServletException, IOException {
		switch (action) {
		case actionLogin:
			login(request, response);
			break;
		case actionRegister:
			register(request, response);
			break;
		case actionQueryPage:
			queryPage(request, response);
			break;
		case actionAdminLogin:
			adminLogin(request, response);
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
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			String pwd = Md5Utils.MD5(password + "?wld");
			MySqlAcountData data = MySqlAcountData.findByAccountPwd(mainDBConnection, account, pwd);
			if (data != null) {
				msg.setCode(0);
				msg.setData(data);
				MySqlAcountData.updateLastLoginDate(mainDBConnection, data.getId());
			}
			else {
				msg.setCode(1);
				msg.setMessage("找不到指定用户");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setCode(1);
			msg.setData(null);
			msg.setMessage(e.getMessage());
		}
		finally {
			if (mainDBConnection != null) {
				try {
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
	
	private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String account = request.getParameter("account");
		String password = request.getParameter("password");
		HttpResponseMsg msg = new HttpResponseMsg();

		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			String pwd = Md5Utils.MD5(password + "?wld");

			MySqlAcountData mySqlAcountData = MySqlAcountData.findByAccount(mainDBConnection, account);
			if (mySqlAcountData != null){
				msg.setCode(1);
				msg.setMessage("account exist");
			}
			else {
				mainDBConnection.begin();
				mainDBConnection.lockWrite(
						MySqlGlobalInfo.tableName,
						MySqlAcountData.tableName);
				MySqlGlobalInfo mySqlGlobalInfo = MySqlGlobalInfo.loadGlobalInfo(mainDBConnection);
				mySqlAcountData = MySqlAcountData.insert(mainDBConnection, account, pwd, mySqlGlobalInfo.getTotalRegister() + 1);
				mainDBConnection.commit();
				msg.setCode(0);
				msg.setData(mySqlAcountData);
				MySqlGlobalInfo.writeGlobalInfo(mainDBConnection, mySqlGlobalInfo.getTotalRegister() + 1);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setCode(1);
			msg.setMessage(e.getMessage());
		}
		finally {
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
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			int pageIdx = Integer.parseInt(strPageIdx);
			int pageSize = Integer.parseInt(strPageSize);
			int offset = pageIdx * pageSize;			
			List<MySqlAcountData> list = MySqlAcountData.queryPage(mainDBConnection, offset, pageSize);
			msg.setCode(0);
			msg.setData(list);
			
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setCode(1);
			msg.setMessage(e.getMessage());
		} finally {
			// TODO: handle finally clause
			if (mainDBConnection != null) {
				try {
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
	
	private void adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			String pwd = Md5Utils.MD5(password + "?wld");
			MySqlAdminData data = MySqlAdminData.findByAccountPwd(mainDBConnection, account, pwd);
			if (data != null) {
				msg.setCode(0);
				msg.setData(data);
			}
			else {
				msg.setCode(1);
				msg.setMessage("找不到指定用户");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setCode(1);
			msg.setData(null);
			msg.setMessage(e.getMessage());
		}
		finally {
			if (mainDBConnection != null) {
				try {
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
