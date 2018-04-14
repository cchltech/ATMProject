package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.RecordSheet;
import bean.Result;
import bean.User;
import dao.DaoFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DatabaseUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String action = request.getParameter("action");// 获取前端的动作参数
		response.setCharacterEncoding("GB2312");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		response.setContentType("text/html;charset=utf-8");
		// 处理登录操作
		if ("login".equals(action)) {
			String card = request.getParameter("card");
			String password = request.getParameter("password");
			try {
				login(request,response,card,password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 处理显示用户信息操作
		else if ("showMessage".equals(action)) {
			String card = session.getAttribute("card").toString();
			try {
				showMessage(request,response,card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 处理修改密码操作
		else if ("modifyPassword".equals(action)) {
			String card = (String) session.getAttribute("card");// 获取session中存储卡号
			String oldPassword = (String) session.getAttribute("password");// 获取session中存储的旧密码
			String password1 = request.getParameter("password1");// 前端输入的原密码
			String newPassword = request.getParameter("newPassword");// 前端输入的新密码
			
			int errorTimes = 0;// 初始化密码输错次数
			try {
				// 查询错误次数
				errorTimes = DaoFactory.getUserDaoInstance().searchErrorTimes(card);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (errorTimes >= 3) {
				// 错误次数大于等于3次,不执行修改密码操作
				out.write(new Result<User>(false, "密码已输错三次,本次已不能修改密码").toString());
				out.flush();
			}else {
				// 错误小于3次,输入正确的原密码
				if (password1.equals(oldPassword)) {
					try {
						DaoFactory.getUserDaoInstance().handleErrorTimes(card,0);// 重置错误次数
						errorTimes = 0;
						modifyPassword(request,response,card,newPassword);// 执行修改密码
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					// 输入密码不一致,输入错误次数加一
					try {
						DaoFactory.getUserDaoInstance().handleErrorTimes(card,1);
						errorTimes++;
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 重新判断错误次数是否到达3次
					if(errorTimes >=3) {
						out.write(new Result<User>(false, "密码已输错三次,本次已不能修改密码").toString());
						out.flush();
					}else {
						out.write(new Result<User>(false, "原密码错误,请重新输入").toString());
						out.flush();
					}
				}
			}
		}
		// 处理存钱操作
		else if ("saveMoney".equals(action)) {
			String card = session.getAttribute("card").toString();// 账户
			String money = request.getParameter("money");// 存入金额			
			try {
				saveMoney(request,response,card,Double.parseDouble(money));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 处理取钱操作
		else if ("outMoney".equals(action)) {
			String card = session.getAttribute("card").toString();// 账户
			String money = request.getParameter("money");// 取出金额
			try {
				outMoney(request,response,card,Integer.parseInt(money));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// 处理查看余额操作
		else if ("showMoney".equals(action)) {
			String card = session.getAttribute("card").toString();// 账户
			try {
				showMoney(request,response,card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 处理查看交易记录表操作
		else if ("showRecordSheet".equals(action)) {
			String card = (String) session.getAttribute("card");// 账户
			try {
				showRecordSheet(request,response,card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查看交易记录表
	 * @param card
	 * @throws Exception
	 */
	public void showRecordSheet(HttpServletRequest request, HttpServletResponse response, String card) throws Exception {
		List<RecordSheet> list = DaoFactory.getUserDaoInstance().showRecordSheet(card);
		PrintWriter out = response.getWriter();
		JSONArray object = JSONArray.fromObject(list);
		out.write(object.toString());
		out.flush();
	}
	
	/**
	 * 查看余额
	 * @param card
	 * @throws Exception
	 */
	public void showMoney(HttpServletRequest request, HttpServletResponse response, String card) throws Exception {
		// 从数据库中读取余额值balance
		String balance = DaoFactory.getUserDaoInstance().showMoney(card);
		PrintWriter out = response.getWriter();
		System.out.println(balance);
		out.write((new Result(true, balance)).toString());
		out.flush();
	}
	
	/**
	 * 取钱操作
	 * @param card
	 * @param money
	 * @throws Exception
	 */
	public void outMoney(HttpServletRequest request, HttpServletResponse response, String card, double money) throws Exception {
		int result = DaoFactory.getUserDaoInstance().outMoney(card, money);
		PrintWriter out = response.getWriter();
		// 取钱结果
		if(result == 0) {
			out.write(new Result<User>(false, "取钱失败").toString());
			out.flush();
		}else {
			out.write(new Result<User>(true, "取钱成功").toString());
			out.flush();
		}
	}
	
	/**
	 * 存钱操作
	 * @param card 
	 * @param money
	 * @throws Exception
	 */
	public void saveMoney(HttpServletRequest request, HttpServletResponse response, String card, double money) throws Exception {
		int result = DaoFactory.getUserDaoInstance().saveMoney(card, money);
		PrintWriter out = response.getWriter();
		// 存钱结果
		if(result == 0) {
			out.write(new Result<User>(false, "存钱失败").toString());
			out.flush();
		}else {
			out.write(new Result<User>(true, "存钱成功").toString());
			out.flush();
		}
	}
	
	/**
	 * 登录验证,界面跳转 
	 * @param card 银行卡号
	 * @throws Exception 
	 */
	public void login(HttpServletRequest request, HttpServletResponse response,String card,String password) throws Exception {
		int checkLogin = DaoFactory.getUserDaoInstance().loginCertification(card,password);
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		if (checkLogin == 0) {
			/*
			 * 创建session,存放卡号以及密码
			 * 登陆成功并显示提示框,跳转至主界面
			 */
			session.setAttribute("card",card);
			session.setAttribute("password",password);
			DaoFactory.getUserDaoInstance().handleFreezeTime(card, 0);//登陆成功,冻结时间归零
			response.sendRedirect("Menus.html");//测试跳转页面
			out.write(new Result<User>(true, "登陆成功").toString());
			out.flush();
		}else if(checkLogin == 1) {
			/*
			 * 登录失败并显示提示框
			 * 返回至登录界面
			 */
			response.sendRedirect("Login.html");
			out.write(new Result<User>(false, "密码输入错误").toString());
			out.flush();
		}else if(checkLogin == 2) {
			/*
			 * 输错密码三次并显示提示框
			 * 账号冻结
			 */
			response.sendRedirect("Login.html");
			out.write(new Result<User>(false, "账户已被冻结").toString());
			out.flush();
		}
	}
	
	/**
	 * 查看账户信息
	 * @param request
	 * @param response
	 * @param card 银行卡号
	 * @throws Exception 
	 */
	public void showMessage(HttpServletRequest request, HttpServletResponse response,String card) throws Exception {
		User user = DaoFactory.getUserDaoInstance().showMessage(card);
		JSONObject obj = JSONObject.fromObject(new Result<User>(true, user));
		PrintWriter out = response.getWriter();
		out.write(obj.toString());
		out.flush();
	}
	
	/**
	 * 修改银行卡密码
	 * @param request
	 * @param response
	 * @param card 银行卡号
	 * @throws Exception 
	 */
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response, String card, String newPassword) throws Exception {
		boolean flag = DaoFactory.getUserDaoInstance().modifyPassword(card,newPassword);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		if(flag) {
			HttpSession session = request.getSession();
			//删除session原本保存的旧密码
			session.removeAttribute("password");
			//session保存新密码
			session.setAttribute("password", newPassword);
			out.write(new Result<User>(true, "密码修改成功").toString());
			out.flush();
		}else {
			out.write(new Result<User>(false, "密码修改失败").toString());
			out.flush();
		}
	}	
}
