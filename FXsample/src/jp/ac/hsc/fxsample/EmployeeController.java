package jp.ac.hsc.fxsample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EmployeeController extends BorderPane implements Initializable, ILoadFxml {

	@FXML private TableView<TableData>				TableView;
	@FXML private TableColumn<TableData	,String> EmployeeName;
	@FXML private TableColumn	EmployeeId;
	@FXML private TableColumn	Enrollment;
	@FXML private TableColumn	Department;
	@FXML private TableColumn	Details;

//	ObservableList<TableData> tdata;
	Button button = new Button("詳細");

	public EmployeeController() {
		loadFxml("Employee.fxml");								// レイアウトのロードとコントローラ設定
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自動生成されたメソッド・スタブ
		EmployeeName.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
		EmployeeId.setCellValueFactory(new PropertyValueFactory<>("EmployeeId"));
		Enrollment.setCellValueFactory(new PropertyValueFactory<>("Enrollment"));
		Department.setCellValueFactory(new PropertyValueFactory<>("Department"));
//		Details = new TableColumn("詳細");
		Details.setCellValueFactory(new PropertyValueFactory<>("Details"));
		ObservableList<TableData> tdata = FXCollections.observableArrayList(
				new TableData("お掃除　品子", 100009, "在籍中", "経営企画"),
				new TableData("白色　緑", 100006, "在籍中", "人事")
				);

		TableView.setItems(tdata);
//		tableview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		tableview.getSelectionModel().setCellSelectionEnabled(false);

		Details.setCellFactory(new ButtonCellFactory<TableData>("詳細情報・変更", e -> onTableButtonClick(e)));


	}

	private Object onTableButtonClick(TableData e) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
