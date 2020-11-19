package infra.inMemory.dayOff;

import java.util.ArrayList;

import domain.models.dayOff.DayOff;
import domain.models.dayOff.IDayOffRepo;

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
