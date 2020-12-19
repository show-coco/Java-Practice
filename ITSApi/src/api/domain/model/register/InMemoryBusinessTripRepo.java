package api.domain.model.register;

import java.util.ArrayList;

public class InMemoryBusinessTripRepo implements IBusinessTripRepo {
	private ArrayList<BusinessTrip> btList = new ArrayList<>();
	@Override
	public void add(BusinessTrip bt) {
		btList.add(bt);

		System.out.println("出張情報を登録しました" + bt.toString());
	}

}
