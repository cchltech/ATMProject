package bean;

public class User {
	/** 银行卡号 */
	private String card;
	/** 用户名 */
	private String username;
	/** 账户密码 */
	private String password;
	/** 用户年龄 */
	private String age;
	/** 账户余额 */
	private String money;
	/** 用户身份证 */
	private String Id;
	/** 用户性别 */
	private String sex;
	
	
	
	/*@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{"+
				"\"card\":\"" + card + "\"," + 
				"\"username\":\"" + username + "\"," + 
				"\"password\":\"" + password + "\"," + 
				"\"age\":\"" + age + "\"," + 
				"\"money\":\"" + money + "\"," + 
				"\"Id\":\"" + Id + "\"," + 
				"\"sex\":\"" + sex + "\"" + 
				"}";
	}*/
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCard() {
		return card;
	}
	public String getId() {
		return Id;
	}
	public void setId(String Id) {
		this.Id = Id;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
}
