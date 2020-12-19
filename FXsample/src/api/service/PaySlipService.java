package api.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import api.domain.model.attendance.Attendance;
import api.domain.model.attendance.IAttendRepo;
import api.domain.model.payslip.PaySlip;

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
		Attendance attendance;
		Attendance leave;
		long attendMinutes = 0;
		int preMonth = LocalDate.of(year, month, 21).minusMonths(1).getMonthValue();
		int currentMonth = month;
		LocalDate datePointer = LocalDate.of(year, preMonth, 21);

		// TODO: remove
		System.out.println(month + "月分の出退勤情報");
		attendList.forEach(attend -> System.out.println(attend.toString()));

			//		for (LocalDate datePointer = LocalDate.of(year, preMonth, 25); (datePointer.getDayOfMonth() >= 25) && (datePointer.getMonthValue() == preMonth) || (datePointer.getDayOfMonth() < 25) && (datePointer.getMonthValue() == currentMonth); datePointer.plusDays(1)) {
			System.out.println(datePointer);
			ArrayList<Attendance> oneDayStatus = (ArrayList<Attendance>) attendList.stream().filter(attend -> attend.getDate().equals(datePointer)).collect(Collectors.toList());
			
			if (oneDayStatus != null) {
				attendance = oneDayStatus.stream().filter(attend -> attend.getType() == 1).findFirst().get();
				leave = oneDayStatus.stream().filter(attend -> attend.getType() == 2).findFirst().get();
				
				// 出勤時間の計算
				attendMinutes += ChronoUnit.MINUTES.between(attendance.getTime(), leave.getTime());
				
				// TODO: remove
				System.out.println("attendance: " + attendance.toString());
				System.out.println("leave: " + leave.toString());
				System.out.println("attendHours: " + (attendMinutes/60.0));
			}
//		}

		return new PaySlip(empId, month, 2, 2, 2, 2, 2);
	}
}
