package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import domain.attend.AttendDate;
import domain.attend.AttendStatus;
import domain.attend.AttendTime;
import domain.employee.EmpId;
import repository.attend.IAttendRepo;

public class AttendanceApplicationService {
	private IAttendRepo attendRepo;
	
	public AttendanceApplicationService(IAttendRepo attendRepo) {
		this.attendRepo = attendRepo;
	}

	// 出勤
	public void attend(int empId) {
		AttendStatus as = new AttendStatus(new EmpId(empId), new AttendDate(new Date()), new AttendTime(new Date()), 1);
		System.out.println(as);

		attendRepo.save(as);
	}
	
	// 退勤
	public void leave(int empId) {
		Date date = new Date();
		AttendStatus as = new AttendStatus(new EmpId(empId), new AttendDate(date), new AttendTime(date), 2);
		
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

	        Date pointer = (Date) fromDate.clone();
			while (!pointer.after(toDate)) { // pointerがtoDateより後ろに行ってないか
				attendRepo.save(new AttendStatus(new EmpId(empId), new AttendDate(pointer), new AttendTime(null), 3));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				pointer = calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	// 出退勤状況の取得
	public ArrayList<AttendStatus> get(int empId) {
		return attendRepo.get(new EmpId(empId));
	}
	
	public AttendStatus get(int empId, Date date, int type) {
		return attendRepo.get(new EmpId(empId), new AttendDate(date), type);
	}

	// 出退勤の変更・登録 
	public void setTime(AttendStatus attendStatus, String time)  {
		try {
			attendStatus.setTime(new AttendTime(getSpecifiedDate(time)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		attendRepo.save(attendStatus);
	}
	
	public Date getSpecifiedDate(String time) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.parse(time);
	}
}
