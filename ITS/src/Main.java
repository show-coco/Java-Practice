

import java.util.ArrayList;

import jp.sample.attendance.AttendanceApplicationService;
import jp.sample.attendance.AttendanceStatus;
import jp.sample.attendance.IAttendRepo;
import jp.sample.attendance.InMemoryAttendRepo;
import jp.sample.employee.EmpService;
import jp.sample.employee.Employee;
import jp.sample.employee.EmployeeApplicationService;
import jp.sample.employee.InMemoryEmpFactory;
import jp.sample.employee.InMemoryEmpRepo;

public class Main {
	public static void main(String[] args) {
		InMemoryEmpFactory empFactory = new InMemoryEmpFactory();
		InMemoryEmpRepo empRepo = new InMemoryEmpRepo();
		EmpService empService = new EmpService(empRepo);
		EmployeeApplicationService empApp = new EmployeeApplicationService(empFactory, empRepo, empService);
		
		IAttendRepo attendRepo = new InMemoryAttendRepo();
		AttendanceApplicationService attendApp = new AttendanceApplicationService(attendRepo);

		// 社員を登録
		try {
			empApp.register("Sho", "Sakai", "show@example.com", 1, "2000/7/1", "023-4567-8912", "広島");
			empApp.register("Mao", "Sakai", "mao@example.com", 1, "2020/7/1", "023-4567-8912", "山口");
		} catch(Exception e) {
			System.out.println(e);
		}
		
		// IDが2の社員を取得
		Employee emp1 = empApp.get(2);
		// 社員情報表示
		System.out.println(emp1.toString());
		
		// 社員の名前を変更
		try {
			empApp.changeName(emp1, "Taka", "Hashi");
		} catch(Exception e) {
			System.out.println(e);
		}
		
		// 出勤
		attendApp.attend(emp1, 1);
		// 出退勤状況取得
		ArrayList<AttendanceStatus> as = attendApp.get(emp1);
		// 出退勤状況表示
		as.forEach(a -> System.out.println(a.toString()));
		
	}
}
