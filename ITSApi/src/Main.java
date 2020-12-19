import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import api.domain.model.attendance.IAttendRepo;
import api.domain.model.attendance.InMemoryAttendRepo;
import api.domain.model.employee.Employee;
import api.domain.model.employee.IEmpRepo;
import api.domain.model.employee.InMemoryEmpRepo;
import api.service.AttendService;
import api.service.EmployeeService;
import api.service.PaySlipService;

public class Main {


	public static void main(String[] args) {
		IEmpRepo empRepo = new InMemoryEmpRepo();
		EmployeeService empService = new EmployeeService(empRepo);

		IAttendRepo attendRepo = new InMemoryAttendRepo();
		AttendService attendService = new AttendService(attendRepo);

		PaySlipService paySlipService = new PaySlipService(attendRepo);

		// 必須項目を入力している場合
		try {
			Employee emp = empService.addEmp(false, 2, 2, 2, 1, new Date(), "password", "sho", "山口県", "山口市", "0120-333-999", "733-0000");
			System.out.println("社員を追加しました");
			System.out.println(emp.toString());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println();

		// 必須項目を入力していない場合
		try {
			Employee emp = empService.addEmp(false, 2, 2, 2, 1, new Date(), "password", "sho", null, null, "0120-333-999", "733-0000");
			System.out.println("社員を追加しました");
			System.out.println(emp.toString());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println();

		// 名前が長すぎる場合
		try {
			Employee emp = empService.addEmp(false, 2, 2, 2, 1, new Date(), "password", "shoaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "山口県", "山口市", "0120-333-999", "733-0000");
			System.out.println("社員を追加しました");
			System.out.println(emp.toString());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		// 社員をIDで検索
		System.out.println("idが1の社員");
		System.out.println(empService.findEmp(1).toString());

		System.out.println();

		// 社員を二人追加
		try {
			Employee emp1 = empService.addEmp(false, 4, 3, 2, 1, new Date(), "password", "Mikan", "広島県", "広島市", "0120-222-234", "999-0292");
			Employee emp2 = empService.addEmp(false, 1, 1, 2, 1, new Date(), "password", "Taro", "東京都", "品川区", "0120-333-1111", "212-2222");
			System.out.println("社員を追加しました");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		// 社員一覧をページ番号指定で取得
		ArrayList<Employee> empList = empService.findEmps(1);
		empList.forEach(emp -> System.out.println(emp.toString()));

		// 出勤
		LocalDate ld = LocalDate.of(2020, 12, 21);
		LocalTime lt = LocalTime.of(8, 30);
		LocalTime leaveTime = lt.plusHours(8).plusMinutes(20);

		for(int i=0; i<80; i++) {
			attendService.attend(1, ld, lt);
			attendService.leave(1, ld, leaveTime);
			
			ld = ld.plusDays(1);			
		}

		System.out.println();

		paySlipService.createPaySlip(1, 2020, 1);
	}

}
