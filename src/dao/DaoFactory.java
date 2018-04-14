package dao;

public class DaoFactory {
	public static UserDao getUserDaoInstance()
	{
		return new UserDaoImpl();
	}
}