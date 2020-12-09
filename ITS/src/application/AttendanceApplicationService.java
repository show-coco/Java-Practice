package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import domain.models.attend.AttendDate;
import domain.models.attend.AttendStatus;
import domain.models.attend.AttendTime;
import domain.models.attend.IAttendRepo;
import domain.models.employee.EmpId;

public class AttendanceApplicationService {
	private IAttendRepo attendRepo;
	
	public AttendanceApplicationService(IAttendRepo attendRepo) {
		this.attendRepo = attendRepo;
	}

	// 出勤
	public void attend(int empId) throws Exception {
		Date now = new Date();
		AttendStatus todayAttend = attendRepo.get(new EmpId(empId), new AttendDate(now), 1);
		
		if (todayAttend != null) {
			throw new Exception("既に出勤しています");
		}
		
		AttendStatus as = new AttendStatus(new EmpId(empId), new AttendDate(now), new AttendTime(now), 1);
		System.out.println(as);

		attendRepo.save(as);
	}
	
	// 退勤
	public void leave(int empId) {
		Date date = new Date();
		AttendStatus as = new AttendStatus(new EmpId(empId), new AttendDate(date), new AttendTime(date), 2);
		
		attendRepo.save(as);
	}
	
	// 出退勤状況の取得
	public ArrayList<AttendStatus> get(int empId) {
		return attendRepo.get(new EmpId(empId));
	}
	
	public AttendStatus get(int empId, Date date, int type) {
		return attendRepo.get(new EmpId(empId), new AttendDate(date), type);
	}

	// 出退勤の変更・登録 
	public void changeTime(AttendStatus attendStatus, String time)  {
		try {
			attendStatus.changeTime(new AttendTime(getSpecifiedDate(time)));
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
