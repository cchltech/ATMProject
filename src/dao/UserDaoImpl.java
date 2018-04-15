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
		// 存在数据则说明账号与密码正确
		if (rs.next()) {
			if (password.equals(rs.getString("user_password"))) {
				System.out.println("登录的账号是:"+rs.getString("card"));
				if (judgeFreezeTime(card)) {
					// 登陆成功,返回0
					handleErrorTimes(card,0);// 输错密码次数清零
					handleFreezeTime(card, 0);// 清空冻结时间
					return 0;
				}else {
					// 登陆失败,账户已被冻结,返回2
					System.out.println("账户被冻结");
					handleErrorTimes(card,0);// 输错密码次数清零
					return 2;
				}
			}else {
				// 密码错误,返回1
				System.out.println("密码输错");
				if (!judgeFreezeTime(card)) {
					// 账户已被冻结,不执行任何操作
					System.out.println("账户已被冻结");
					return 2;
				}else {
					// 账户尚未冻结
					handleErrorTimes(card,1);// 输错密码次数加一
					// 如果输错密码次数等于3次,置零次数并冻结账户
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
			user.setCard(rs.getString("card"));// 账户卡号
			user.setUsername(rs.getString("user_name"));// 用户姓名
			user.setAge(rs.getString("age"));// 用户年龄
			user.setMoney(String.valueOf(rs.getDouble("money")));// 账户余额
			user.setId(rs.getString("user_id"));// 身份证号码
			user.setSex(rs.getString("sex"));// 用户性别
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
			//清零冻结时间,恢复账户正常状态
			String sql1 = "UPDATE table_user SET freeze_time = null WHERE card = '"+card+"'";
			DatabaseUtil.executeUpdate(sql1);
			break;
		case 1:
			//获取当前系统时间的下一天,并写入数据库,设定冻结时间
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
		Date today = new Date();// 获取此刻的时间
		today = sdf.parse(sdf.format(today));
		
		boolean judgement = false;// 默认账户未冻结
		if (rs.next()) {
			String time = rs.getString("freeze_time");
			if (time==null || "".equals(time.trim())) {
				// 账户未处于冻结状态,直接返回true
				judgement = true;
			}else {
				// 账户已冻结
				Date freezeTime = new Date();// 数据库中保存的冻结时间
				freezeTime = sdf.parse(time);
				// 如果此刻时间已过冻结时间,账户解冻
				if (freezeTime.before(today)) {
					System.out.println("冻结时间已过，账户解冻");
					String sql1 = "UPDATE table_user SET freeze_time = null WHERE card = '"+card+"'";
					DatabaseUtil.executeUpdate(sql1);// 设置冻结时间为空
					judgement = true;
				}else {
					judgement = false;
				}
			}
			DatabaseUtil.close();
		}
		return judgement;// 已解冻返回true,冻结返回false
	}

	@Override
	public int saveMoney(String card, double money)  throws Exception {
		// 查找余额
		String balance = showMoney(card);
		// 余额字符串转换为浮点型
		double allmoney = Double.parseDouble(balance);
		// 存款加上余额
		allmoney = allmoney + money;
		// 创建修改数据库语句
		String sql_save = "update table_user set money="+allmoney+" where card='"+card+"'";
		// 设置操作返回值变量
		int result = 0;
		// 执行存钱操作
		result = DatabaseUtil.executeUpdate(sql_save);
		// 存钱操作成功则result = 1，失败则result = 0
		if (result == 1) {
			// 获取系统当前时间作为作为唯一交易码
			String record_number = Timer.getPureNumber();
			// 获取系统当前时间作为存取时间
			String Trade_number = Timer.getNormalDate();
			// 创建table_record的交易记录
			String sql_record = "insert into table_record values('"+record_number+"','"+card+"',"+money+",'"+Trade_number+"')";
			// 执行记录插入table_record操作
			DatabaseUtil.executeUpdate(sql_record);
		}
		return result;
	}

	@Override
	public int outMoney(String card, double money) throws Exception {
		// 查找余额
		String balance = showMoney(card);
		// 余额字符串转换为浮点型
		double allmoney = Double.parseDouble(balance);
		// 设置操作返回值变量,取款操作成功则返回1，失败则返回0
		int result = 0;
		// 判断余额是否小于取款金额
		if(allmoney<money) {
			return result;
		}
		else {
			// money取负数
		money = -money;
		// 存款加上余额
		allmoney = allmoney + money;
		// 创建修改数据库语句
		String sql_out = "update table_user set money="+allmoney+" where card='"+card+"'";
		// 执行取钱操作
		result = DatabaseUtil.executeUpdate(sql_out);
		// 取钱操作成功则result = 1，失败则result = 0
		if (result == 1) {
			// 获取系统当前时间作为作为唯一交易码
			String record_number = Timer.getPureNumber();
			// 获取系统当前时间作为存取时间
			String Trade_number = Timer.getNormalDate();
			// 创建table_record的交易记录
			String sql_record = "insert into table_record values('"+record_number+"','"+card+"',"+money+",'"+Trade_number+"')";
			// 执行记录插入table_record操作
			DatabaseUtil.executeUpdate(sql_record);
		}
		// 返回操作结果result
		return result;
		}
	}

	@Override
	public String showMoney(String card) throws Exception {
		// 设置余额默认值0.000元
		String bill = "0.000";
		// 查询账户为account_number的余额
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
		// 返回账户余额
		return bill;
	}

	@Override
	public List<RecordSheet> showRecordSheet(String card) throws Exception {
		List<RecordSheet> list = new ArrayList<RecordSheet>();
		String thisYear = Timer.getCurrentYear();
		thisYear = thisYear + "0000000000";
		System.out.println(thisYear);
		String sql = "SELECT * FROM table_record WHERE card ='"+ card +"' and "+"record_number >= '"+thisYear+"' order by time desc";
		ResultSet rs = DatabaseUtil.executeQuery(sql);// 查找过去一年内的记录
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