package jp.sample.attendance;

import java.util.ArrayList;

public class InMemoryAttendRepo implements IAttendRepo {
	private ArrayList<AttendanceStatus> asList = new ArrayList<>();

	@Override
	public void save(AttendanceStatus as) {
		asList.add(as);
	}

}
