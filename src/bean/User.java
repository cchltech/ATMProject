package bean;

public class User {
	/** ���п��� */
	private String card;
	/** �û��� */
	private String username;
	/** �˻����� */
	private String password;
	/** �û����� */
	private String age;
	/** �˻���� */
	private String money;
	/** �û����֤ */
	private String Id;
	/** �û��Ա� */
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
