package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bean.RecordSheet;
import bean.User;
import javafx.scene.chart.PieChart.Data;
import tool.Timer;
import util.DatabaseUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public int loginCertification(String card,String password) throws Exception {
		String checkSql = "SELECT * FROM table_user WHERE card = '"+ card +"'";
		ResultSet rs = DatabaseUtil.executeQuery(checkSql);
		// ����������˵���˺���������ȷ
		if (rs.next()) {
			if (password.equals(rs.getString("user_password"))) {
				System.out.println("��¼���˺���:"+rs.getString("card"));
				if (judgeFreezeTime(card)) {
					// ��½�ɹ�,����0
					handleErrorTimes(card,0);// ��������������
					handleFreezeTime(card, 0);// ��ն���ʱ��
					return 0;
				}else {
					// ��½ʧ��,�˻��ѱ�����,����2
					System.out.println("�˻�������");
					handleErrorTimes(card,0);// ��������������
					return 2;
				}
			}else {
				// �������,����1
				System.out.println("�������");
				if (!judgeFreezeTime(card)) {
					// �˻��ѱ�����,��ִ���κβ���
					System.out.println("�˻��ѱ�����");
					return 2;
				}else {
					// �˻���δ����
					handleErrorTimes(card,1);// ������������һ
					// �����������������3��,��������������˻�
					if (searchErrorTimes(card) >= 3) {
						handleErrorTimes(card, 0);
						handleFreezeTime(card, 1);
					}
				}
				return 1;
			}
		}
		return 1;
	}

	@Override
	public boolean modifyPassword(String card,String newPassword) throws Exception {
		String sql1 = "UPDATE table_user SET user_password = '"+ newPassword +"' WHERE card='"+card+"'";
		DatabaseUtil.executeUpdate(sql1);
		DatabaseUtil.close();
		return true;
	}

	@Override
	public User showMessage(String card) throws Exception {
		User user = new User();
		String sql = "SELECT * FROM table_user WHERE card='"+card+"'";
		ResultSet rs = DatabaseUtil.executeQuery(sql);
		if (rs.next()) {
			user.setCard(rs.getString("card"));// �˻�����
			user.setUsername(rs.getString("user_name"));// �û�����
			user.setAge(rs.getString("age"));// �û�����
			user.setMoney(String.valueOf(rs.getDouble("money")));// �˻����
			user.setId(rs.getString("user_id"));// ���֤����
			user.setSex(rs.getString("sex"));// �û��Ա�
		}
		DatabaseUtil.close();
		return user;
	}

	@Override
	public void handleErrorTimes(String card, int action) throws Exception {
		String sql = "UPDATE table_user SET error_times = '0' WHERE card = '"+card+"'";
		String sql1 = "UPDATE table_user SET error_times = error_times + 1 WHERE card = '"+card+"'";
		switch(action){
		 case 0:
			 DatabaseUtil.executeUpdate(sql);
			 break;
		 case 1:
			 DatabaseUtil.executeUpdate(sql1);
			 break;
		}
		DatabaseUtil.close();
	} 

	@Override
	public int searchErrorTimes(String card) throws Exception {
		String checkSql = "SELECT * FROM table_user WHERE card = '"+ card +"'";
		ResultSet rs = DatabaseUtil.executeQuery(checkSql);
		int errorTimes = 0;
		if (rs.next()) {
			errorTimes = rs.getInt("error_times");
		}
		DatabaseUtil.close();
		return errorTimes;
	}

	@Override
	public void handleFreezeTime(String card, int action) throws Exception {
		switch (action) {
		case 0:
			//���㶳��ʱ��,�ָ��˻�����״̬
			String sql1 = "UPDATE table_user SET freeze_time = null WHERE card = '"+card+"'";
			DatabaseUtil.executeUpdate(sql1);
			break;
		case 1:
			//��ȡ��ǰϵͳʱ�����һ��,��д�����ݿ�,�趨����ʱ��
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			today = calendar.getTime();
			String tomorrow = sdf.format(today);
			String sql2 = "UPDATE table_user SET freeze_time = '"+ tomorrow +"' WHERE card = '"+card+"'";
			DatabaseUtil.executeUpdate(sql2);
			break;
		}
		DatabaseUtil.close();
	}

	@Override
	public boolean judgeFreezeTime(String card) throws Exception {
		String checkSql = "SELECT * FROM table_user WHERE card = '"+ card +"'";
		ResultSet rs = DatabaseUtil.executeQuery(checkSql);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ��ʽ��ʱ��
		Date today = new Date();// ��ȡ�˿̵�ʱ��
		today = sdf.parse(sdf.format(today));
		
		boolean judgement = false;// Ĭ���˻�δ����
		if (rs.next()) {
			String time = rs.getString("freeze_time");
			if (time==null || "".equals(time.trim())) {
				// �˻�δ���ڶ���״̬,ֱ�ӷ���true
				judgement = true;
			}else {
				// �˻��Ѷ���
				Date freezeTime = new Date();// ���ݿ��б���Ķ���ʱ��
				freezeTime = sdf.parse(time);
				// ����˿�ʱ���ѹ�����ʱ��,�˻��ⶳ
				if (freezeTime.before(today)) {
					System.out.println("����ʱ���ѹ����˻��ⶳ");
					String sql1 = "UPDATE table_user SET freeze_time = null WHERE card = '"+card+"'";
					DatabaseUtil.executeUpdate(sql1);// ���ö���ʱ��Ϊ��
					judgement = true;
				}else {
					judgement = false;
				}
			}
			DatabaseUtil.close();
		}
		return judgement;// �ѽⶳ����true,���᷵��false
	}

	@Override
	public int saveMoney(String card, double money)  throws Exception {
		// �������
		String balance = showMoney(card);
		// ����ַ���ת��Ϊ������
		double allmoney = Double.parseDouble(balance);
		// ���������
		allmoney = allmoney + money;
		// �����޸����ݿ����
		String sql_save = "update table_user set money="+allmoney+" where card='"+card+"'";
		// ���ò�������ֵ����
		int result = 0;
		// ִ�д�Ǯ����
		result = DatabaseUtil.executeUpdate(sql_save);
		// ��Ǯ�����ɹ���result = 1��ʧ����result = 0
		if (result == 1) {
			// ��ȡϵͳ��ǰʱ����Ϊ��ΪΨһ������
			String record_number = Timer.getPureNumber();
			// ��ȡϵͳ��ǰʱ����Ϊ��ȡʱ��
			String Trade_number = Timer.getNormalDate();
			// ����table_record�Ľ��׼�¼
			String sql_record = "insert into table_record values('"+record_number+"','"+card+"',"+money+",'"+Trade_number+"')";
			// ִ�м�¼����table_record����
			DatabaseUtil.executeUpdate(sql_record);
		}
		return result;
	}

	@Override
	public int outMoney(String card, double money) throws Exception {
		// �������
		String balance = showMoney(card);
		// ����ַ���ת��Ϊ������
		double allmoney = Double.parseDouble(balance);
		// ���ò�������ֵ����,ȡ������ɹ��򷵻�1��ʧ���򷵻�0
		int result = 0;
		// �ж�����Ƿ�С��ȡ����
		if(allmoney<money) {
			return result;
		}
		else {
			// moneyȡ����
		money = -money;
		// ���������
		allmoney = allmoney + money;
		// �����޸����ݿ����
		String sql_out = "update table_user set money="+allmoney+" where card='"+card+"'";
		// ִ��ȡǮ����
		result = DatabaseUtil.executeUpdate(sql_out);
		// ȡǮ�����ɹ���result = 1��ʧ����result = 0
		if (result == 1) {
			// ��ȡϵͳ��ǰʱ����Ϊ��ΪΨһ������
			String record_number = Timer.getPureNumber();
			// ��ȡϵͳ��ǰʱ����Ϊ��ȡʱ��
			String Trade_number = Timer.getNormalDate();
			// ����table_record�Ľ��׼�¼
			String sql_record = "insert into table_record values('"+record_number+"','"+card+"',"+money+",'"+Trade_number+"')";
			// ִ�м�¼����table_record����
			DatabaseUtil.executeUpdate(sql_record);
		}
		// ���ز������result
		return result;
		}
	}

	@Override
	public String showMoney(String card) throws Exception {
		// �������Ĭ��ֵ0.000Ԫ
		String bill = "0.000";
		// ��ѯ�˻�Ϊaccount_number�����
		String sql_show = "select money from table_user where card='"+card+"'";
		ResultSet rs = DatabaseUtil.executeQuery(sql_show);
		try {
			if (rs.next()) {
				bill = rs.getString("money");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		DatabaseUtil.close();
		// �����˻����
		return bill;
	}

	@Override
	public List<RecordSheet> showRecordSheet(String card) throws Exception {
		List<RecordSheet> list = new ArrayList<RecordSheet>();
		String thisYear = Timer.getCurrentYear();
		thisYear = thisYear + "0000000000";
		System.out.println(thisYear);
		String sql = "SELECT * FROM table_record WHERE card ='"+ card +"' and "+"record_number >= '"+thisYear+"' order by time desc";
		ResultSet rs = DatabaseUtil.executeQuery(sql);// ���ҹ�ȥһ���ڵļ�¼
		while (rs.next()) {
			RecordSheet recordSheet = new RecordSheet();
			recordSheet.setCard(rs.getString("card"));
			recordSheet.setMoney(String.valueOf(rs.getDouble("money")));
			recordSheet.setRecordNumber(rs.getString("record_number"));
			recordSheet.setTime(rs.getString("time"));
			list.add(recordSheet);
		}
		return list;
		
	}

}