package dao;

import java.sql.SQLException;
import java.util.List;

import bean.RecordSheet;
import bean.User;

public interface UserDao {
	/**
	 * 账户密码验证
	 * @param cardNumber 银行卡号
	 * @return 若登录成功返回true，否则false
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public int loginCertification(String cardNumber,String password) throws SQLException, Exception;
	/**
	 * 修改银行卡密码
	 * @param cardNumber 银行卡号
	 * @param newPassword 新密码
	 * @throws Exception 
	 */
	public boolean modifyPassword(String cardNumber,String newPassword) throws Exception;
	/**
	 * 显示账户信息
	 * @param cardNumber 银行卡号
	 * @throws Exception 
	 */
	public User showMessage(String cardNumber) throws Exception;
	/**
	 * 增加输错密码次数
	 * @param cardNumber 银行卡号
	 * @param action 执行动作 0为归零 1为加一
	 * @throws Exception
	 */
	public void handleErrorTimes(String cardNumber, int action) throws Exception;
	/**
	 * 查询输错密码次数
	 * @param cardNumber 银行卡号
	 * @return 输错密码次数
	 * @throws Exception
	 */
	public int searchErrorTimes(String cardNumber) throws Exception;
	/**
	 * 处理账户冻结时间,包括解冻账户和冻结账户
	 * @param cardNumber 银行卡号
	 * @param action 执行动作 0为归零 1为加一
	 * @return true or false
	 * @throws Exception
	 */
	public void handleFreezeTime(String cardNumber, int action) throws Exception;
	/**
	 * 判断冻结时间
	 * @param cardNumber 银行卡号
	 * @return
	 * @throws Exception
	 */
	public boolean judgeFreezeTime(String cardNumber) throws Exception;
	/**
	 * 存钱操作
	 * @param cardNumber 银行卡号
	 * @param money 取钱额度
	 * @return
	 * @throws Exception
	 */
	public int saveMoney(String cardNumber,double money) throws Exception;
	/**
	 * 取钱操作
	 * @param cardNumber 银行卡号
	 * @param money 取钱额度
	 * @return
	 * @throws Exception
	 */
	public int outMoney(String cardNumber,double money) throws Exception;
	/**
	 * 显示余额
	 * @param cardNumber 银行卡号
	 * @return 
	 * @throws Exception
	 */
	public String showMoney(String cardNumber) throws Exception;
	/**
	 * 显示交易记录表
	 * @param cardNumber 银行卡号
	 * @return 
	 * @throws Exception
	 */
	public List<RecordSheet> showRecordSheet(String cardNumber) throws Exception;
}
