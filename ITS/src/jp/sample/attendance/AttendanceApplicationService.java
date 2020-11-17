package jp.sample.attendance;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.sample.employee.Employee;

public class AttendanceApplicationService {
	private IAttendRepo attendRepo;
	
	public AttendanceApplicationService(IAttendRepo attendRepo) {
		this.attendRepo = attendRepo;
	}



	// TODO: 出退勤処理
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
	
	// TODO: 出退勤状況の表示
	// TODO: 出退勤の変更・登録
}
