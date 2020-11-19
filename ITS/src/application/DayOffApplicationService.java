package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import domain.models.dayOff.DayOff;
import domain.models.dayOff.DayOffDate;
import domain.models.dayOff.IDayOffRepo;
import domain.models.employee.EmpId;

public class DayOffApplicationService {
	private IDayOffRepo dayOffRepo;

	public DayOffApplicationService(IDayOffRepo dayOffRepo) {
		this.dayOffRepo = dayOffRepo;
	}
	
	
	// 休暇登録
	public void registVacation(int empId, String from, String to) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date fromDate = df.parse(from);
			Date toDate = df.parse(to);
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(fromDate);

	        Date pointer = (Date) fromDate.clone();
			while (!pointer.after(toDate)) { // pointerがtoDateより後ろに行っていない間繰り返す
				if (!DayOff.isWeekend(calendar)) { // 週末ではないか判定
					dayOffRepo.save(new DayOff(new EmpId(empId), new DayOffDate(pointer), 2));	// 休暇登録する
				}
				calendar.add(Calendar.DAY_OF_MONTH, 1); // カレンダーを1日進める
				pointer = calendar.getTime();  // カレンダーからDateを取得する
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<DayOff> getAll() {
		return dayOffRepo.getAll();
	}
}
