package jp.ac.hsc.fxsample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PaySlipTableData {
	private final StringProperty		WorkingTimeKey;				// 学生No(列1)
	private final StringProperty		WorkingTimeValue;			// 氏名(列2-1)
	private final StringProperty		SalaryKey;				// 学科CD(列2-2)
	private final StringProperty		SalaryValue;			// 出席日数(列3)

	public PaySlipTableData(String WorkingTimeKey, String WorkingTimeValue, String SalaryKey, String SalaryValue) {
		this.WorkingTimeKey		= new SimpleStringProperty(WorkingTimeKey);
		this.WorkingTimeValue	= new SimpleStringProperty(WorkingTimeValue);
		this.SalaryKey			= new SimpleStringProperty(SalaryKey);
		this.SalaryValue		= new SimpleStringProperty(SalaryValue);
	}
	public final String getWorkingTimeKey() {
		return WorkingTimeKey.get();
	}
	public final String getWorkingTimeValue() {
		return WorkingTimeValue.get();
	}
	public final String getSalaryKey() {
		return SalaryKey.get();
	}
	public final String getSalaryValue() {
		return SalaryValue.get();
	}

	public final void setWorkingTimeKey(String WorkingTimeKey)	{this.WorkingTimeKey.set(WorkingTimeKey);}
	public final void setWorkingTimeValue(String WorkingTimeValue)	{this.WorkingTimeValue.set(WorkingTimeValue);}
	public final void setSalaryKey(String SalaryKey)			{this.SalaryKey.set(SalaryKey);}
	public final void setSalaryValue(String SalaryValue)			{this.SalaryValue.set(SalaryValue);}

	public final StringProperty	WorkingTimeKeyProp()		{return WorkingTimeKey;}
	public final StringProperty		WorkingTimeValueProp()	{return WorkingTimeValue;}
	public final StringProperty		SalaryKeyProp()		{return SalaryKey;}
	public final StringProperty	SalaryValueProp()		{return SalaryValue;}
}
