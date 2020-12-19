package api.service;

import java.time.LocalDate;
import java.time.LocalTime;

import api.domain.model.attendance.Attendance;
import api.domain.model.attendance.IAttendRepo;

public class AttendService {
	public static final String DATE_FORMAT = "yyyy/MM/dd";
	public static final String TIME_FORMAT = "HH:mm:ss";

	private IAttendRepo attendRepo;

	public AttendService(IAttendRepo attendRepo) {
		super();
		this.attendRepo = attendRepo;
	}

	public void attend(int empId, LocalDate attendDate, LocalTime attendTime) {
		Attendance attend = new Attendance(empId, Attendance.ATTEND_TYPE, attendDate, attendTime); // 出勤情報を生成

		attendRepo.save(attend); // 出勤情報をリポジトリに保存
	}

	public void leave(int empId, LocalDate leaveDate, LocalTime leaveTime) {
		Attendance attend = new Attendance(empId, Attendance.LEAVE_TYPE, leaveDate, leaveTime); // 退勤情報を生成

		attendRepo.save(attend); // 退勤情報をリポジトリに保存
	}
}
