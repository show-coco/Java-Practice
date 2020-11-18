package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import domain.attend.AttendStatus;
import domain.employee.EmpId;
import repository.attend.IAttendRepo;

public class AttendanceApplicationService {
	private IAttendRepo attendRepo;
	
	public AttendanceApplicationService(IAttendRepo attendRepo) {
		this.attendRepo = attendRepo;
	}

	// 出勤
	public void attend(int empId) {
		AttendStatus as = new AttendStatus(new EmpId(empId), getCurrentDate(), 1, getCurrentTime());

		attendRepo.save(as);
	}
	
	// 退勤
	public void leave(int empId) {
		AttendStatus as = new AttendStatus(new EmpId(empId), getCurrentDate(), 2, getCurrentTime());
		
		attendRepo.save(as);
	}
	
	// 休暇登録
	public void registVacation(int empId, String from, String to) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date fromDate = df.parse(from);
			Date toDate = df.parse(to);
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(fromDate);

	        Date date = fromDate;
			while (!date.equals(toDate)) {
				attendRepo.save(new AttendStatus(new EmpId(empId), df.format(date), 3, null));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				date = calendar.getTime();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public String getCurrentTime() { 
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date now = new Date();
		return df.format(now);
	}
	
	public String getCurrentDate() { 
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date now = new Date();
		return df.format(now);
	}
}
