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
import com.bigcat.mysql.table.MySqlAwardData;
import com.bigcat.mysql.table.MySqlTable;
import com.bigcat.mysql.table.MySqlUserMoneyLog;

import net.sf.json.JSONObject;

public class AwardController {

	private static final String actionAdd = "add";
	private static final String actionExchange = "exchange";
	private static final String actionDelete = "delete";
	private static final String actionQueryPage = "queryPage";
	
	private static AwardController sInstance = null;
	
	public static AwardController getInstance() {
		if (AwardController.sInstance == null) {
			AwardController.sInstance = new AwardController();
		}
		return AwardController.sInstance;
	}
	
	public AwardController() {
		// TODO Auto-generated constructor stub
	}
	
	public void doAction(HttpServletRequest request, HttpServletResponse response, String action) 
			throws ServletException, IOException {
		switch (action) {
		case actionAdd:
			addAward(request, response);
			break;
		case actionExchange:
			exchangeAward(request, response);
			break;
		case actionDelete:
			deleteAward(request, response);
			break;
		case actionQueryPage:
			queryPage(request, response);
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
	
	private void addAward(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String imgUrl = request.getParameter("imgUrl");
		String strPrice = request.getParameter("price");
		String strCount = request.getParameter("count");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int price = Integer.parseInt(strPrice);
			int count = Integer.parseInt(strCount);
			mainDBConnection = new MainDBConnection();
			MySqlAwardData.addAwardData(mainDBConnection, name, imgUrl, price, count);
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
	
	private void exchangeAward(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String strUid = request.getParameter("uid");
		String strAwardId = request.getParameter("awardId");
		String strExchangeNum = request.getParameter("exchangeNum");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int uid = Integer.parseInt(strUid);
			int awardId = Integer.parseInt(strAwardId);
			int exchangeNum = Integer.parseInt(strExchangeNum);
			mainDBConnection = new MainDBConnection();
			mainDBConnection.lockWrite(MySqlAwardData.tableName, MySqlAcountData.tableName);
			MySqlAcountData mySqlAcountData = MySqlAcountData.findByUid(mainDBConnection, uid);
			MySqlAwardData mySqlAwardData = MySqlTable.findItemById(
					mainDBConnection, MySqlAwardData.class, MySqlAwardData.tableName, awardId);
			if (mySqlAwardData.getPrice() * exchangeNum < mySqlAcountData.getCapitalWeibi()) {
				int decrease = mySqlAwardData.getPrice() * exchangeNum;
				MySqlAcountData.updateCapitalWeibi(
						mainDBConnection, uid, mySqlAcountData.getCapitalWeibi() - decrease);
				MySqlAwardData.updateAwardCount(
						mainDBConnection, awardId, mySqlAwardData.getCount() - exchangeNum);
				MySqlUserMoneyLog.addLog(mainDBConnection, uid, mySqlAwardData.getPrice() * exchangeNum, 0, 
						mySqlAcountData.getCapitalWeibi() - mySqlAwardData.getPrice() * exchangeNum, 
						String.format("兑换%s*%d", mySqlAwardData.getName(), exchangeNum));
				mySqlAwardData.setCount((short) (mySqlAwardData.getCount() - exchangeNum));
				msg.setCode(0);
				msg.setData(mySqlAwardData);
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
	
	private void deleteAward(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String strAwardId = request.getParameter("awardId");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int awardId = Integer.parseInt(strAwardId);
			mainDBConnection = new MainDBConnection();
			MySqlTable.deleteItem(mainDBConnection, MySqlAwardData.tableName, awardId);
			msg.setCode(0);
			msg.setData(awardId);
			
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
	
	private void queryPage(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {

		String strPageIdx = request.getParameter("pageIdx");
		String strPageSize = request.getParameter("pageSize");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			int pageIdx = Integer.parseInt(strPageIdx);
			int pageSize = Integer.parseInt(strPageSize);
			int offset = pageIdx * pageSize;			
			mainDBConnection = new MainDBConnection();
			List<MySqlAwardData> list = MySqlAwardData.queryPage(mainDBConnection, offset, pageSize);
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
