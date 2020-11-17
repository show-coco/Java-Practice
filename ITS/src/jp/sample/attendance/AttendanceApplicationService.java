package jp.sample.attendance;

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

		AttendanceStatus as = new AttendanceStatus(emp, currentDate, typeId, currentTime);

		attendRepo.save(as);
	}
	
	// 出退勤状況の取得
	public ArrayList<AttendanceStatus> get(Employee emp) {
		return attendRepo.get(emp);
	}
	// TODO: 出退勤の変更・登録
}
