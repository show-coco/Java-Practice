package jp.ac.hsc.fxsample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableData {
	private final	StringProperty	EmployeeName;	//社員名
	private final	IntegerProperty	EmployeeId;		//社員ID
	private final	StringProperty	Enrollment;		//在籍状況
	private final	StringProperty	Department;		//部署
	private final	StringProperty	Details;		//詳細ボタン

	//コンストラクタ
	public TableData(String EmployeeName, int EmployeeId, String Enrollment, String Department) {
		this.EmployeeName	=	new	SimpleStringProperty(EmployeeName);
		this.EmployeeId		=	new	SimpleIntegerProperty(EmployeeId);
		this.Enrollment		=	new	SimpleStringProperty(Enrollment);
		this.Department		=	new	SimpleStringProperty(Department);
		this.Details		=	new	SimpleStringProperty("詳細情報・変更");
//		Details.onMouseClickedProperty();
	}


	//アクセサ
	public final String	getEmployeeName()						{return EmployeeName.get();}
	public final int		getEmployeeId()							{return EmployeeId.get();}
	public final String	getEnrollment()							{return Enrollment.get();}
	public final String	getDepartment()							{return Department.get();}
	public final String	getDetails()							{return Details.get();}

	public final void		setEmployeeName(String EmployeeName)	{this.EmployeeName.set(EmployeeName);}
	public final void		setEmployeeId(int EmployeeId)			{this.EmployeeId.set(EmployeeId);}
	public final void		setEnrollment(String Enrollment)		{this.Enrollment.set(Enrollment);}
	public final void		setDepartment(String Department)		{this.Department.set(Department);}
	public final void		setDetails(String Details)				{this.Details.set(Details);}

	//プロパティ設定
	public final StringProperty	EmployeeNameProperty()	{return EmployeeName;}
	public final IntegerProperty	EmployeeIdProperty()	{return EmployeeId;}
	public final StringProperty	EnrollmentProperty()	{return Enrollment;}
	public final StringProperty	DepartmentProperty()	{return Department;}
	public final StringProperty	DetailsProperty()		{return Details;}


}