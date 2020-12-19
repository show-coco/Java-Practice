package jp.ac.hsc.fxsample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SidebarController extends GridPane implements Initializable {
	 @FXML private Button	businessTripButton;
	 @FXML private Button	calcSalaryButton;
	 @FXML private Button	vacationButton;
	 @FXML private Button	employeeListButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		businessTripButton.setOnAction(event -> {
			Main.getInstance().sendBusinessTripController();
		});

		calcSalaryButton.setOnAction(event -> {
			Main.getInstance().sendCalcSalaryController();
		});

		vacationButton.setOnAction(event -> {
			Main.getInstance().sendVacationController();
		});

		employeeListButton.setOnAction(event -> {
			Main.getInstance().sendEmployeeController();
		});
	}

}
