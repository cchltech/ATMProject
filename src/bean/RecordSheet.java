package bean;

public class RecordSheet {
	/** 交易记录号 */
	private String recordNumber;
	/** 银行卡号 */
	private String card;
	/** 交易额度 */
	private String money;
	/** 交易时间 */
	private String time;
	
	
	public String getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
