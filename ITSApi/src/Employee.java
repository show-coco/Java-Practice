import java.util.Date;

public class Employee {
	private boolean enrollStatus;	// 必須
	private int 	age;
	private int 	gender; 		// 必須
	private int 	positionId;		// 必須
	private int 	avilityId;		// 必須
	private int 	departmentId;	// 必須
	private int 	workingYears;
	private Date 	birthDay;		// 必須
	private String	empId;			// 必須
	private String 	password;		// 必須
	private String 	name;			// 必須
	private String address1;		// 必須
	private String address2;		// 必須
	private String phoneNum;		// 必須
	private String postalCode;		// 必須
	public static int nameMinimum = 0;
	public static int nameMaximum = 21;

	private Employee(boolean enrollStatus, int gender, int positionId, int avilityId, int departmentId, Date birthDay, String empId, String password, String name, String address1,
			String address2, String phoneNum, String postalCode) {

		AssertionConcern.assertArgumentNotNull(enrollStatus, 	"在籍状況が選択されていません");
		AssertionConcern.assertArgumentNotNull(gender, 		"性別が選択されていません");
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
		this.age 			= 10; 				// 生年月日から算出
		this.gender 		= gender;
		this.positionId 	= positionId;
		this.avilityId 		= avilityId;
		this.departmentId 	= departmentId;
		this.workingYears 	= 0;
		this.birthDay 		= birthDay;
		this.empId 			= empId;
		this.password 		= password;
		this.name 			= name;
		this.address1 		= address1;
		this.address2		= address2;
		this.phoneNum 		= phoneNum;
		this.postalCode 	= postalCode;
	}

	public static Employee addEmployee(boolean enrollStatus, int gender, int positionId, int avilityId, int departmentId, Date birthDay, String empId, String password, String name, String address1,
			String address2, String phoneNum, String postalCode) {

		Employee emp = new Employee(enrollStatus, gender, positionId, avilityId, departmentId, birthDay, empId, password, name,  address1,  address2, phoneNum, postalCode);

		return EmpRepo.save(emp);
	}

	// 確認用
	@Override
	public String toString() {
		return "Employee [enrollStatus=" + enrollStatus + ", age=" + age + ", gender=" + gender + ", positionId="
				+ positionId + ", avilityId=" + avilityId + ", departmentId=" + departmentId + ", workingYears="
				+ workingYears + ", " + (birthDay != null ? "birthDay=" + birthDay + ", " : "")
				+ (empId != null ? "empId=" + empId + ", " : "")
				+ (password != null ? "password=" + password + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (address1 != null ? "address1=" + address1 + ", " : "")
				+ (address2 != null ? "address2=" + address2 + ", " : "")
				+ (phoneNum != null ? "phoneNum=" + phoneNum + ", " : "")
				+ (postalCode != null ? "postalCode=" + postalCode : "") + "]";
	}
}
