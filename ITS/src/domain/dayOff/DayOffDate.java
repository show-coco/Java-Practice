package domain.dayOff;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayOffDate {
	private Date value;
	private SimpleDateFormat df;

	public DayOffDate(Date date) {
		if (date == null) throw new NullPointerException("休日がnullです");
		df = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = df.format(date);

		try {
			this.value = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}

	public Date getValue() {
		return value;
	}

	@Override
	public String toString() {
		return df.format(value);
	}
}
