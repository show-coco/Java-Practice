

import java.util.ArrayList;

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

		try {
			empApp.register("Sho", "Sakai", "show@example.com", 1, "2000/7/1", "023-4567-8912", "広島");
			empApp.register("Mao", "Sakai", "mao@example.com", 1, "2020/7/1", "023-4567-8912", "山口");
		} catch(Exception e) {
			System.out.println(e);
		}
		
		// IDが2のユーザを取得
		Employee emp1 = empApp.get(2);
		System.out.println(emp1.toString());
		
		// 名前を変更
		try {
			empApp.changeName(emp1, "Taka", "Hashi");
		} catch(Exception e) {
			System.out.println(e);
		}

		emp1 = empApp.get(2);
		System.out.println(emp1.toString());
		
		empApp.changePw(emp1, "aaaaaaaaa");
		
		System.out.println(empApp.login(2, "aaaaaaaaa"));
	}
}
