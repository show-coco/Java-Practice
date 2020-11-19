package domain.models.dayOff;
import java.util.ArrayList;

public interface IDayOffRepo {
	void save(DayOff dayOff);
	ArrayList<DayOff> getAll();
}
