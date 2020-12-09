package service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import domain.model.attendance.Attendance;
import domain.model.attendance.IAttendRepo;
import domain.model.payslip.PaySlip;

public class PaySlipService {
	private IAttendRepo attendRepo;

	public PaySlipService(IAttendRepo attendRepo) {
		super();
		this.attendRepo = attendRepo;
	}

	/**
	 * 給与や手当を計算する
	 * @param empId
	 * @param year
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public PaySlip createPaySlip(int empId, int year, int month) {
		ArrayList<Attendance> attendList = attendRepo.findForMonth(empId, year, month);

		// TODO: remove
		attendList.forEach(attend -> System.out.println(attend.toString()));

		ArrayList<Attendance> oneDayStatus = (ArrayList<Attendance>) attendList.stream().filter(attend -> attend.getDate().getDayOfMonth() == 9).collect(Collectors.toList());
		Attendance attendance = oneDayStatus.stream().filter(attend -> attend.getType() == 1).findFirst().get();
		Attendance leave = oneDayStatus.stream().filter(attend -> attend.getType() == 2).findFirst().get();

		long attendMinutes = ChronoUnit.MINUTES.between(leave.getTime(), attendance.getTime());

		// TODO: remove
		System.out.println("attendance: " + attendance.toString());
		System.out.println("leave: " + leave.toString());
		System.out.println("attendHours: " + (attendMinutes/60.0));


		return new PaySlip(empId, month, 2, 2, 2, 2, 2);
	}
}
