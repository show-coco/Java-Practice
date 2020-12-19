package api.domain.model.employee;
import java.text.SimpleDateFormat;
import java.util.Date;

import api.common.AssertionConcern;


public class Employee {
	private boolean enrollStatus;	// 必須
	private int 	age;
	private int 	genderId; 		// 性別ID(必須)
	private int 	positionId;		// 役職ID(必須)
	private int 	avilityId;		// 職能ID(必須)
	private int 	departmentId;	// 部署ID(必須)
	private int 	workingYears;
	private int	empId;			// 社員ID(必須)
	private Date 	birthDay;		// 生年月日(必須)
	private String 	password;		// パスワード(必須)
	private String 	name;			// 名前(必須)
	private String address1;		// 住所1(必須)
	private String address2;		// 住所2(必須)
	private String phoneNum;		// 電話番号(必須)
	private String postalCode;		// 郵便番号(必須)
	public static int nameMinimum = 0;
	public static int nameMaximum = 21;

	public Employee(boolean enrollStatus, int genderId, int positionId, int avilityId, int departmentId, Date birthDay, String password, String name, String address1,
			String address2, String phoneNum, String postalCode) {

		AssertionConcern.assertArgumentNotNull(enrollStatus, 	"在籍状況が選択されていません");
		AssertionConcern.assertArgumentNotNull(genderId, 		"性別が選択されていません");
		AssertionConcern.assertArgumentNotNull(positionId, 	"役職が選択されていません");
		AssertionConcern.assertArgumentNotNull(avilityId, 	"職能が選択されていません"); 	// 画面に職能選択する場所がない
		AssertionConcern.assertArgumentNotNull(departmentId, 	"部署が選択されていません");
		AssertionConcern.assertArgumentNotNull(birthDay, 		"生年月日が選択されていません");
		AssertionConcern.assertArgumentNotNull(password, 		"パスワードが入力されていません");
		AssertionConcern.assertArgumentNotNull(name, 			"名前が入力されていません");
		AssertionConcern.assertArgumentNotNull(address1, 		"住所1が入力されていません");
		AssertionConcern.assertArgumentNotNull(address2, 		"住所2が入力されていません");
		AssertionConcern.assertArgumentNotNull(phoneNum, 		"電話番号が入力されていません");
		AssertionConcern.assertArgumentNotNull(postalCode, 	"郵便番号が入力されていません");
		AssertionConcern.assertArgumentLength(name, nameMinimum, nameMaximum, "名前の長さは" + (nameMinimum+1) + "文字以上" + nameMaximum + "文字未満です");

		this.enrollStatus 	= enrollStatus;
		this.genderId 		= genderId;
		this.positionId 	= positionId;
		this.avilityId 		= avilityId;
		this.departmentId 	= departmentId;
		this.workingYears 	= 0;
		this.birthDay 		= birthDay;
		this.password 		= password;
		this.name 			= name;
		this.address1 		= address1;
		this.address2		= address2;
		this.phoneNum 		= phoneNum;
		this.postalCode 	= postalCode;
		this.age 			= calcAge(this.birthDay, new Date()); 				// 生年月日から算出
	}

	/**
	 * 勤務年数の更新
	 */
	public void updateWorkingYears() {
		this.workingYears++;
	}

	/**
	 * 年齢を計算
	 * @param birthday
	 * @param now
	 * @return
	 */
	public int calcAge(Date birthday, Date now) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    return (Integer.parseInt(sdf.format(now)) - Integer.parseInt(sdf.format(birthday))) / 10000;
	}

	public boolean isEnrollStatus() {
		return enrollStatus;
	}

	public int getAge() {
		return age;
	}

	public int getGenderId() {
		return genderId;
	}

	public int getPositionId() {
		return positionId;
	}

	public int getAvilityId() {
		return avilityId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public int getWorkingYears() {
		return workingYears;
	}

	public int getEmpId() {
		return empId;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setEnrollStatus(boolean enrollStatus) {
		this.enrollStatus = enrollStatus;
	}

	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public void setAvilityId(int avilityId) {
		this.avilityId = avilityId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	// 確認用
	@Override
	public String toString() {
		return "Employee [enrollStatus=" + enrollStatus + ", age=" + age + ", genderId=" + genderId + ", positionId="
				+ positionId + ", avilityId=" + avilityId + ", departmentId=" + departmentId + ", workingYears="
				+ workingYears + ", empId=" + empId + ", " + (birthDay != null ? "birthDay=" + birthDay + ", " : "")
				+ (password != null ? "password=" + password + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (address1 != null ? "address1=" + address1 + ", " : "")
				+ (address2 != null ? "address2=" + address2 + ", " : "")
				+ (phoneNum != null ? "phoneNum=" + phoneNum + ", " : "")
				+ (postalCode != null ? "postalCode=" + postalCode : "") + "]";
	}


}
