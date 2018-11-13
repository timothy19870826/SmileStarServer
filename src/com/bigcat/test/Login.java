package com.bigcat.test;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigcat.data.http.HttpResponseMsg;
import com.bigcat.mysql.MainDBConnection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
    	throw new ServletException("GET method used with " +
      		     getClass( ).getName( )+": POST method required.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response); 
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		HttpResponseMsg msg = new HttpResponseMsg();
		MainDBConnection mainDBConnection = null;
		try {
			mainDBConnection = new MainDBConnection();
			AccountBaseInfoTab tab_account_base_info = new AccountBaseInfoTab(mainDBConnection.getConnection());
			AccountBaseInfoTab.AccountBaseInfo account_base_info = tab_account_base_info.findAccount(account, password);
			if (account_base_info == null) {
				msg.setCode(1);
				msg.setData(null);
				msg.setMessage("invalid account or password");
			}
			else {
				msg.setCode(0);
				msg.setData(account_base_info);
				tab_account_base_info.updateAccount(account_base_info.getId());
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
