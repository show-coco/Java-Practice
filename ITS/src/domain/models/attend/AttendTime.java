package domain.models.attend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendTime {
	private Date time;
	private SimpleDateFormat df;

	public AttendTime(Date time) {
		if (time == null) throw new NullPointerException("出勤時刻がnullです");
		df = new SimpleDateFormat("HH:mm");
		String strTime = df.format(time);
		
		try {
			this.time = df.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public String toString() {
		return df.format(time);
	}
}
