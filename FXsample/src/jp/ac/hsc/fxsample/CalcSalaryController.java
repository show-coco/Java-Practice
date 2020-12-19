package jp.ac.hsc.fxsample;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import api.domain.model.employee.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CalcSalaryController extends BorderPane implements Initializable, ILoadFxml {
	@FXML private TableView<PaySlipTableData> Table;
	@FXML private TableColumn<PaySlipTableData, String>	WorkingTimeKey;
	@FXML private TableColumn<PaySlipTableData, String>	WorkingTimeValue;
	@FXML private TableColumn<PaySlipTableData, String>	SalaryKey;
	@FXML private TableColumn<PaySlipTableData, String>	SalaryValue;
	@FXML private ChoiceBox<String>	Departments;				// 選択ボックス
	@FXML private ListView<String> EmployeesList;

	ObservableList<PaySlipTableData> tdata;

	public CalcSalaryController() {
		 loadFxml("CalcSalary.fxml");			// レイアウトのロードとコントローラ設定
	 }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 選択ボックス
		Departments.getItems().addAll("経理部", "情シス", "人事部");
		Departments.setValue("経理部"); // 初期値セット
		EventHandler<ActionEvent> choiceBoxChanged = ( event ) -> this.choiceBoxChanged( event );
		Departments.addEventHandler( ActionEvent.ACTION , choiceBoxChanged );

		// リストビュー
		ArrayList<Employee> empList = Main.empService.findEmps(1);
		ArrayList<String> empList2 = (ArrayList<String>) empList.stream().map(emp -> emp.getEmpId() + " / " + emp.getName()).collect(Collectors.toList());
		ObservableList<String>lm = FXCollections.observableArrayList(empList2);
		EmployeesList.setItems(lm);

		// テーブルビュー
		WorkingTimeKey.setCellValueFactory(new PropertyValueFactory<>("WorkingTimeKey"));	// 列と内部フィールドの紐づけ
		WorkingTimeValue.setCellValueFactory(new PropertyValueFactory<>("WorkingTimeValue"));
		SalaryKey.setCellValueFactory(new PropertyValueFactory<>("SalaryKey"));
		SalaryValue.setCellValueFactory(new PropertyValueFactory<>("SalaryValue"));
		tdata = FXCollections.observableArrayList(	// データ準備
				new PaySlipTableData("日中総労働時間", "1", "基本給", "102"),
				new PaySlipTableData("深夜総労働時間", "10", "深夜手当", "93"),
				new PaySlipTableData("残労働時間", "2", "通勤手当", "100"),
				new PaySlipTableData("", "", "出張手当", "71"),
				new PaySlipTableData("", "", "休日手当", "0"),
				new PaySlipTableData("", "", "時間外手当", "1"),
				new PaySlipTableData("", "", "役員報酬", "20"),
				new PaySlipTableData("総労働時間", "40", "支給額計", "400")
				);
		Table.setItems(tdata);
		Table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		Table.getSelectionModel().setCellSelectionEnabled(false);
		Callback<TableColumn<PaySlipTableData, String>, TableCell<PaySlipTableData, String>> cellFactory =
				p -> new EditableCell();
		WorkingTimeValue.setCellFactory(cellFactory);
		SalaryValue.setCellFactory(cellFactory);
	}

	/**
	 * 部署が選択されたら発火するメソッド
	 * @param e
	 */
	private void choiceBoxChanged( ActionEvent e ){
		 ChoiceBox c = (ChoiceBox)e.getSource();
		 System.out.println( "選択: "+(String)c.getValue() ) ;
	}

	class EditableCell extends TableCell<PaySlipTableData, String> {
		private TextField textField;		// テキストフィールドで編集する
		private String saveValue;

		public EditableCell() { }			// コンストラクタなし

		@Override
		public void startEdit() {														// 編集開始時の処理
			// TODO 自動生成されたメソッド・スタブ
			super.startEdit();
			saveValue = this.getItem();		// (this=このクラス自身=セルそのもの)
			// 確定後にソートされると赤色のセルは動かないので変更箇所がずれてしまう対処
			WorkingTimeKey.setSortable(false);
			WorkingTimeValue.setSortable(false);
			SalaryKey.setSortable(false);
			SalaryValue.setSortable(false);
			// 編集テキストフィールド作成
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - (this.getGraphicTextGap() * 2));
			// 編集テキストフィールドからフォーカスが外れた場合に確定するようにリスナー登録
			textField.focusedProperty().addListener((p, oldv, newv) -> {	// タブ移動で確定する
				if (!newv) {
					String newValue;
					try {
						newValue = textField.getText();
						if (newValue != saveValue) {
//							System.out.println(newValue);
							this.commitEdit(newValue);
							this.setTextFill(Color.RED);		// 確定後は文字色を赤に変更
//							System.out.println("EditableCell中：" + tdata.get(0).getCday());
						} else cancelEdit();
					} catch (NumberFormatException e) {
						cancelEdit();
					}
				}
			});
			// 表示変更(セルにテキストフィールドをマップ)
			this.setText(null);
			this.setGraphic(textField);
			textField.selectAll();
		}

		@Override
		public void cancelEdit() {														// 編集破棄時の処理
			// TODO 自動生成されたメソッド・スタブ
			super.cancelEdit();
			// 編集内容を破棄
			this.setText(String.valueOf(this.getItem()));
			this.setGraphic(null);
		}

		@Override
		protected void updateItem(String item, boolean empty) {	// セル更新時の処理
			// TODO 自動生成されたメソッド・スタブ
			super.updateItem(item, empty);
			if (empty) {									// 空の場合
				this.setText(null);
				this.setGraphic(null);
			} else if (this.isEditing()) {			// 編集中の場合
				if (textField != null) textField.setText(getString());
				this.setText(null);
				this.setGraphic(textField);
			} else {											// その他
				this.setText(getString());
				this.setGraphic(null);
			}
		}

		private String getString() {
			return (this.getItem() == null ? "" : String.valueOf(this.getItem()));
		}
	}

}
