package jp.sample.attendance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.sample.employee.Employee;

public class AttendanceApplicationService {
	private IAttendRepo attendRepo;
	
	public AttendanceApplicationService(IAttendRepo attendRepo) {
		this.attendRepo = attendRepo;
	}

	// 出退勤
	public void attend(Employee emp, int typeId) {
		// TODO: リファクタリング
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
		Date now = new Date();
		String currentDate = df1.format(now);
		String currentTime = df2.format(now);

		AttendStatus as = new AttendStatus(emp, currentDate, typeId, currentTime);

		attendRepo.save(as);
	}
	
	// 出退勤状況の取得
	public ArrayList<AttendStatus> get(Employee emp) {
		return attendRepo.get(emp);
	}
	
	public AttendStatus get(Employee emp, String date, int type) {
		return attendRepo.get(emp, date, type);
	}

	// 出退勤の変更・登録
	public void setTime(AttendStatus attendStatus, String time) throws ParseException {
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		Date newTime = new Date();
		newTime = tf.parse(time);
		String strTime = tf.format(newTime);
		
		attendStatus.setTime(strTime);
		attendRepo.save(attendStatus);
	}
}
