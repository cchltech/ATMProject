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
		String action = request.getParameter("action");// ��ȡǰ�˵Ķ�������
		response.setCharacterEncoding("GB2312");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		response.setContentType("text/html;charset=utf-8");
		// �����¼����
		if ("login".equals(action)) {
			String card = request.getParameter("card");
			String password = request.getParameter("password");
			try {
				login(request,response,card,password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ������ʾ�û���Ϣ����
		else if ("showMessage".equals(action)) {
			String card = session.getAttribute("card").toString();
			try {
				showMessage(request,response,card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// �����޸��������
		else if ("modifyPassword".equals(action)) {
			String card = (String) session.getAttribute("card");// ��ȡsession�д洢����
			String oldPassword = (String) session.getAttribute("password");// ��ȡsession�д洢�ľ�����
			String password1 = request.getParameter("password1");// ǰ�������ԭ����
			String newPassword = request.getParameter("newPassword");// ǰ�������������
			
			int errorTimes = 0;// ��ʼ������������
			try {
				// ��ѯ�������
				errorTimes = DaoFactory.getUserDaoInstance().searchErrorTimes(card);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (errorTimes >= 3) {
				// ����������ڵ���3��,��ִ���޸��������
				out.write(new Result<User>(false, "�������������,�����Ѳ����޸�����").toString());
				out.flush();
			}else {
				// ����С��3��,������ȷ��ԭ����
				if (password1.equals(oldPassword)) {
					try {
						DaoFactory.getUserDaoInstance().handleErrorTimes(card,0);// ���ô������
						errorTimes = 0;
						modifyPassword(request,response,card,newPassword);// ִ���޸�����
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					// �������벻һ��,������������һ
					try {
						DaoFactory.getUserDaoInstance().handleErrorTimes(card,1);
						errorTimes++;
					} catch (Exception e) {
						e.printStackTrace();
					}
					// �����жϴ�������Ƿ񵽴�3��
					if(errorTimes >=3) {
						out.write(new Result<User>(false, "�������������,�����Ѳ����޸�����").toString());
						out.flush();
					}else {
						out.write(new Result<User>(false, "ԭ�������,����������").toString());
						out.flush();
					}
				}
			}
		}
		// �����Ǯ����
		else if ("saveMoney".equals(action)) {
			String card = session.getAttribute("card").toString();// �˻�
			String money = request.getParameter("money");// ������			
			try {
				saveMoney(request,response,card,Double.parseDouble(money));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ����ȡǮ����
		else if ("outMoney".equals(action)) {
			String card = session.getAttribute("card").toString();// �˻�
			String money = request.getParameter("money");// ȡ�����
			try {
				outMoney(request,response,card,Integer.parseInt(money));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// ����鿴������
		else if ("showMoney".equals(action)) {
			String card = session.getAttribute("card").toString();// �˻�
			try {
				showMoney(request,response,card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ����鿴���׼�¼�����
		else if ("showRecordSheet".equals(action)) {
			String card = (String) session.getAttribute("card");// �˻�
			try {
				showRecordSheet(request,response,card);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �鿴���׼�¼��
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
	 * �鿴���
	 * @param card
	 * @throws Exception
	 */
	public void showMoney(HttpServletRequest request, HttpServletResponse response, String card) throws Exception {
		// �����ݿ��ж�ȡ���ֵbalance
		String balance = DaoFactory.getUserDaoInstance().showMoney(card);
		PrintWriter out = response.getWriter();
		System.out.println(balance);
		out.write((new Result(true, balance)).toString());
		out.flush();
	}
	
	/**
	 * ȡǮ����
	 * @param card
	 * @param money
	 * @throws Exception
	 */
	public void outMoney(HttpServletRequest request, HttpServletResponse response, String card, double money) throws Exception {
		int result = DaoFactory.getUserDaoInstance().outMoney(card, money);
		PrintWriter out = response.getWriter();
		// ȡǮ���
		if(result == 0) {
			out.write(new Result<User>(false, "ȡǮʧ��").toString());
			out.flush();
		}else {
			out.write(new Result<User>(true, "ȡǮ�ɹ�").toString());
			out.flush();
		}
	}
	
	/**
	 * ��Ǯ����
	 * @param card 
	 * @param money
	 * @throws Exception
	 */
	public void saveMoney(HttpServletRequest request, HttpServletResponse response, String card, double money) throws Exception {
		int result = DaoFactory.getUserDaoInstance().saveMoney(card, money);
		PrintWriter out = response.getWriter();
		// ��Ǯ���
		if(result == 0) {
			out.write(new Result<User>(false, "��Ǯʧ��").toString());
			out.flush();
		}else {
			out.write(new Result<User>(true, "��Ǯ�ɹ�").toString());
			out.flush();
		}
	}
	
	/**
	 * ��¼��֤,������ת 
	 * @param card ���п���
	 * @throws Exception 
	 */
	public void login(HttpServletRequest request, HttpServletResponse response,String card,String password) throws Exception {
		int checkLogin = DaoFactory.getUserDaoInstance().loginCertification(card,password);
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		if (checkLogin == 0) {
			/*
			 * ����session,��ſ����Լ�����
			 * ��½�ɹ�����ʾ��ʾ��,��ת��������
			 */
			session.setAttribute("card",card);
			session.setAttribute("password",password);
			DaoFactory.getUserDaoInstance().handleFreezeTime(card, 0);//��½�ɹ�,����ʱ�����
			response.sendRedirect("Menus.html");//������תҳ��
			out.write(new Result<User>(true, "��½�ɹ�").toString());
			out.flush();
		}else if(checkLogin == 1) {
			/*
			 * ��¼ʧ�ܲ���ʾ��ʾ��
			 * ��������¼����
			 */
			response.sendRedirect("Login.html");
			out.write(new Result<User>(false, "�����������").toString());
			out.flush();
		}else if(checkLogin == 2) {
			/*
			 * ����������β���ʾ��ʾ��
			 * �˺Ŷ���
			 */
			response.sendRedirect("Login.html");
			out.write(new Result<User>(false, "�˻��ѱ�����").toString());
			out.flush();
		}
	}
	
	/**
	 * �鿴�˻���Ϣ
	 * @param request
	 * @param response
	 * @param card ���п���
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
	 * �޸����п�����
	 * @param request
	 * @param response
	 * @param card ���п���
	 * @throws Exception 
	 */
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response, String card, String newPassword) throws Exception {
		boolean flag = DaoFactory.getUserDaoInstance().modifyPassword(card,newPassword);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		if(flag) {
			HttpSession session = request.getSession();
			//ɾ��sessionԭ������ľ�����
			session.removeAttribute("password");
			//session����������
			session.setAttribute("password", newPassword);
			out.write(new Result<User>(true, "�����޸ĳɹ�").toString());
			out.flush();
		}else {
			out.write(new Result<User>(false, "�����޸�ʧ��").toString());
			out.flush();
		}
	}	
}
