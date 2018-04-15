package tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//获取系统当前时间
public class Timer {
	public static String getPureNumber() {
		Date date = new Date();    
        DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        String time=format.format(date);
		return time;
	}
	public static String getNormalDate() {
		Date date = new Date();    
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
		return time;
	}
	public static String getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String thisYear  = sdf.format(date);
        return thisYear;
}
}

