package repository.dayOff;
import java.util.ArrayList;

import domain.dayOff.DayOff;

public interface IDayOffRepo {
	void save(DayOff dayOff);
	ArrayList<DayOff> getAll();
}
