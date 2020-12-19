package jp.ac.hsc.fxsample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class VacationController extends BorderPane implements Initializable, ILoadFxml {
	public VacationController() {
		loadFxml("Vacation.fxml");								// レイアウトのロードとコントローラ設定
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
