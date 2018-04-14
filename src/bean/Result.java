package bean;

public class Result<T> {
	private boolean success;
	private T data;
	private String msg;
	public Result(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}
	public Result(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
