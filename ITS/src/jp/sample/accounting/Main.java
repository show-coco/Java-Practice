package jp.sample.accounting;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		InMemoryEmpRepo empRepo = new InMemoryEmpRepo();
		EmpService empService = new EmpService(empRepo);
		EmployeeApplicationService empApp = new EmployeeApplicationService(empRepo, empService);

		try {
			empApp.register("E001", "Sho", "Sakai", "show@example.com", 1, "2000/7/1", "023-4567-8912", "広島");
		} catch(Exception e) {
			System.out.println(e);
		}
		
		// IDがABCDE123のユーザを取得
		Employee emp1 = empApp.get("E001");
		System.out.println(emp1.toString());
		
		// 名前を変更
		try {
			empApp.changeName(emp1, "Taka", "Hashi");
		} catch(Exception e) {
			System.out.println(e);
		}
		emp1 = empApp.get("E001");
		System.out.println(emp1.toString());
		
		empApp.changePw(emp1, "aaaaaaaaa");
		
		System.out.println(empApp.login("E001", "aaaaaaaaa"));
	}
}
