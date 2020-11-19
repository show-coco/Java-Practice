package repository.dayOff;

import java.util.ArrayList;

import domain.dayOff.DayOff;

public class InMemoryDayOffRepo implements IDayOffRepo {
	private ArrayList<DayOff> dayOffList = new  ArrayList<>();
	
	@Override
	public void save(DayOff dayOff) {
		dayOffList.add(dayOff);
	}

	@Override
	public ArrayList<DayOff> getAll() {
		return dayOffList;
	}

}
