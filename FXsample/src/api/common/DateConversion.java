package api.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConversion {
	public static LocalDateTime getFormattedDate(String format, LocalDate date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		String strDate = date.format(dtf); // フォーマットされた文字列に変換
		LocalDateTime formattedDate = LocalDateTime.parse(strDate, dtf); // フォーマットされたLocalDateTimeに変換
		
		return formattedDate;
	}
}
