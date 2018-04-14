package dao;

import java.sql.SQLException;
import java.util.List;

import bean.RecordSheet;
import bean.User;

public interface UserDao {
	/**
	 * �˻�������֤
	 * @param cardNumber ���п���
	 * @return ����¼�ɹ�����true������false
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public int loginCertification(String cardNumber,String password) throws SQLException, Exception;
	/**
	 * �޸����п�����
	 * @param cardNumber ���п���
	 * @param newPassword ������
	 * @throws Exception 
	 */
	public boolean modifyPassword(String cardNumber,String newPassword) throws Exception;
	/**
	 * ��ʾ�˻���Ϣ
	 * @param cardNumber ���п���
	 * @throws Exception 
	 */
	public User showMessage(String cardNumber) throws Exception;
	/**
	 * ��������������
	 * @param cardNumber ���п���
	 * @param action ִ�ж��� 0Ϊ���� 1Ϊ��һ
	 * @throws Exception
	 */
	public void handleErrorTimes(String cardNumber, int action) throws Exception;
	/**
	 * ��ѯ����������
	 * @param cardNumber ���п���
	 * @return ����������
	 * @throws Exception
	 */
	public int searchErrorTimes(String cardNumber) throws Exception;
	/**
	 * �����˻�����ʱ��,�����ⶳ�˻��Ͷ����˻�
	 * @param cardNumber ���п���
	 * @param action ִ�ж��� 0Ϊ���� 1Ϊ��һ
	 * @return true or false
	 * @throws Exception
	 */
	public void handleFreezeTime(String cardNumber, int action) throws Exception;
	/**
	 * �ж϶���ʱ��
	 * @param cardNumber ���п���
	 * @return
	 * @throws Exception
	 */
	public boolean judgeFreezeTime(String cardNumber) throws Exception;
	/**
	 * ��Ǯ����
	 * @param cardNumber ���п���
	 * @param money ȡǮ���
	 * @return
	 * @throws Exception
	 */
	public int saveMoney(String cardNumber,double money) throws Exception;
	/**
	 * ȡǮ����
	 * @param cardNumber ���п���
	 * @param money ȡǮ���
	 * @return
	 * @throws Exception
	 */
	public int outMoney(String cardNumber,double money) throws Exception;
	/**
	 * ��ʾ���
	 * @param cardNumber ���п���
	 * @return 
	 * @throws Exception
	 */
	public String showMoney(String cardNumber) throws Exception;
	/**
	 * ��ʾ���׼�¼��
	 * @param cardNumber ���п���
	 * @return 
	 * @throws Exception
	 */
	public List<RecordSheet> showRecordSheet(String cardNumber) throws Exception;
}
