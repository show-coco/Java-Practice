package service;
import java.util.ArrayList;
import java.util.Date;

import domain.model.employee.Employee;
import domain.model.employee.IEmpRepo;

public class EmployeeService {
	IEmpRepo empRepo;

	public EmployeeService(IEmpRepo empRepo) {
		super();
		this.empRepo = empRepo;
	}

	/**
	 * 社員を追加
	 * @param enrollStatus
	 * @param gender
	 * @param positionId
	 * @param avilityId
	 * @param departmentId
	 * @param birthDay
	 * @param password
	 * @param name
	 * @param address1
	 * @param address2
	 * @param phoneNum
	 * @param postalCode
	 * @return
	 */
	public Employee addEmp(boolean enrollStatus, int gender, int positionId, int avilityId, int departmentId, Date birthDay, String password, String name, String address1,
			String address2, String phoneNum, String postalCode) {

		Employee emp = new Employee(enrollStatus, gender, positionId, avilityId, departmentId, birthDay, password, name,  address1,  address2, phoneNum, postalCode);

		return empRepo.save(emp);
	}

	/**
	 * 社員をIDで検索
	 * @param empId
	 * @return
	 */
	public Employee findEmp(int empId) {
		return empRepo.find(empId);
	}


	/**
	 * 社員一覧を取得
	 * @param pageNum
	 * @return
	 */
	public ArrayList<Employee> findEmps(int pageNum) {
		return empRepo.findAll(pageNum);
	}

	/**
	 * 社員を名前で検索
	 * @param pageNum
	 * @param name
	 * @return
	 */
	public ArrayList<Employee> findEmpsByName(int pageNum, String name) {
		return empRepo.findAll(pageNum, name);
	}

	/**
	 * 社員を部署で検索
	 * @param pageNum
	 * @param name
	 * @param departmentId
	 * @return
	 */
	public ArrayList<Employee> findEmpsByDepartment(int pageNum, String name, int departmentId) {
		return empRepo.findAll(pageNum, departmentId);
	}

	/**
	 *	名前を変更
	 * @param empId
	 * @param name
	 * @return
	 */
	public Employee changeEmpName(int empId, String name) {
		Employee emp = empRepo.find(empId);

		emp.setName(name);

		return empRepo.save(emp);
	}

	/**
	 * パスワードを変更
	 * @param empId
	 * @param password
	 * @return
	 */
	public Employee changePassword(int empId, String password) {
		Employee emp = empRepo.find(empId);

		emp.setPassword(password);

		return empRepo.save(emp);
	}
}
