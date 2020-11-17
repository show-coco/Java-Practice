

import java.text.ParseException;
import java.util.ArrayList;

import attendance.AttendStatus;
import attendance.AttendanceApplicationService;
import attendance.IAttendRepo;
import attendance.InMemoryAttendRepo;
import employee.EmpService;
import employee.Employee;
import employee.EmployeeApplicationService;
import employee.InMemoryEmpFactory;
import employee.InMemoryEmpRepo;

public class Main {
	public static void main(String[] args) {
		InMemoryEmpFactory empFactory = new InMemoryEmpFactory();
		InMemoryEmpRepo empRepo = new InMemoryEmpRepo();
		EmpService empService = new EmpService(empRepo);
		EmployeeApplicationService empApp = new EmployeeApplicationService(empFactory, empRepo, empService);
		
		IAttendRepo attendRepo = new InMemoryAttendRepo();
		AttendanceApplicationService attendApp = new AttendanceApplicationService(attendRepo);

		/**************** 社員サンプル *****************/

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
		
		/**************** 出退勤サンプル *****************/
		
		// emp1が出勤
		attendApp.attend(emp1, 1);
		// emp1の出退勤状況取得
		ArrayList<AttendStatus> as = attendApp.get(emp1);
		// 出退勤状況表示
		as.forEach(a -> System.out.println(a.toString()));

		// emp1の特定の出勤況取得
		AttendStatus as1 = attendApp.get(emp1, "2020/11/17", 1);
		// as1の出勤時間を変更
		try {
			attendApp.setTime(as1, "22:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 変更後の出退勤状況取得 
		as.forEach(a -> System.out.println(a.toString()));
	}
}
