package api.service;

import java.time.LocalDate;

import api.domain.model.register.BusinessTrip;
import api.domain.model.register.IBusinessTripRepo;

public class RegisterService {
	private IBusinessTripRepo btRepo;

	public RegisterService(IBusinessTripRepo btRepo) {
		super();
		this.btRepo = btRepo;
	}

	public void addBusinessTrip(String empId, LocalDate from, LocalDate to) {
		for(LocalDate datePointer = from; !datePointer.equals(to.plusDays(1)); datePointer = datePointer.plusDays(1)) {
			btRepo.add(new BusinessTrip(empId, datePointer));
		}
	}
}
