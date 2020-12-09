package domain.model.attendance;

import java.util.ArrayList;

public interface IAttendRepo {
	public Attendance save(Attendance attend);

	/**
	 * 指定した月分の出退勤状況を取得
	 * @param empId
	 * @param year
	 * @param month
	 * @return
	 */
	public ArrayList<Attendance> findForMonth(int empId, int year, int month);
}
