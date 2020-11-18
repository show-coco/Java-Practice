package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import domain.attend.AttendStatus;
import domain.employee.EmpId;
import repository.attend.IAttendRepo;

public class AttendanceApplicationService {
	private IAttendRepo attendRepo;
	
	public AttendanceApplicationService(IAttendRepo attendRepo) {
		this.attendRepo = attendRepo;
	}

	// 出退勤
	public void attend(int empId, int typeId) {
		// TODO: リファクタリング
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
		Date now = new Date();
		String currentDate = df1.format(now);
		String currentTime = df2.format(now);

		AttendStatus as = new AttendStatus(new EmpId(empId), currentDate, typeId, currentTime);

		attendRepo.save(as);
	}
	
	// 出退勤状況の取得
	public ArrayList<AttendStatus> get(int empId) {
		return attendRepo.get(new EmpId(empId));
	}
	
	public AttendStatus get(int empId, String date, int type) {
		return attendRepo.get(new EmpId(empId), date, type);
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
