package jp.ac.hsc.fxsample;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class BusinessTripController extends BorderPane implements Initializable, ILoadFxml {
	@FXML private TextField EmployeeId;
	@FXML private TextField Distance;
	@FXML private TextField AcommFee;
	@FXML private TextField TravelFee;
	@FXML private DatePicker From;
	@FXML private DatePicker To;
	@FXML private Button RegistoryButton;
	@FXML private Label EmployeeIdError;
	@FXML private Label DistanceError;
	@FXML private Label AcommFeeError;
	@FXML private Label TravelFeeError;

	private String regexNum = "^[0-9]+$" ;

	public BusinessTripController() {
		 loadFxml("BusinessTrip.fxml");			// レイアウトのロードとコントローラ設定
	 }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 日付の初期値をセット
		From.setValue(LocalDate.now());
		To.setValue(LocalDate.now());

		// いずれかのTextFieldが空白であればボタンを無効化
		RegistoryButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			return EmployeeId.getText().length() != 5 ||
					Distance.getText().isEmpty() ||
					AcommFee.getText().isEmpty() ||
					TravelFee.getText().isEmpty();
//					!EmployeeIdError.getText().isEmpty() ||
		}, EmployeeId.textProperty(), Distance.textProperty(), AcommFee.textProperty(), TravelFee.textProperty()));


		addValidation(EmployeeId, true, true);
		addValidation(Distance, true, false);
		addValidation(AcommFee, true, false);
		addValidation(TravelFee, true, false);

		RegistoryButton.setOnAction(event -> {
			Main.registerService.addBusinessTrip(EmployeeId.getText(), From.getValue(), To.getValue());
		});
	}

	public void addValidation(TextField tf, boolean num, boolean length) {
		tf.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	//数字のみ
		        if (num && !newValue.matches("\\d*")) {
		        	tf.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		        //文字数制限
		        if(length && tf.getLength() > 5) {
		        	tf.setText(oldValue);
		        }
		    }
		});
	}
}
