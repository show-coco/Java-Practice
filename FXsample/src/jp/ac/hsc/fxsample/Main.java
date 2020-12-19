package jp.ac.hsc.fxsample;

import java.util.Date;

import api.domain.model.attendance.IAttendRepo;
import api.domain.model.attendance.InMemoryAttendRepo;
import api.domain.model.employee.Employee;
import api.domain.model.employee.IEmpRepo;
import api.domain.model.employee.InMemoryEmpRepo;
import api.domain.model.register.IBusinessTripRepo;
import api.domain.model.register.InMemoryBusinessTripRepo;
import api.service.AttendService;
import api.service.EmployeeService;
import api.service.PaySlipService;
import api.service.RegisterService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	private static Main instance;					// Mainクラス(自身)のインスタンス
	private static Stage stage;						// ステージ
	static public EmployeeService empService;
	static public AttendService attendService;
	static public PaySlipService paySlipService;
	static public RegisterService registerService;

	@Override
	 public void start(Stage primaryStage) throws Exception {
		instance = this;									// 自身のインスタンスを保存
		stage = primaryStage;						// ステージの保存
	 	stage.setResizable(false);					// ステージサイズの変更不可
	 	sendCalcSalaryController();
	 	stage.setOnCloseRequest(e -> endApp(e));						// ×ボタン処理
	 	stage.showingProperty().addListener((observable, oldValue, newValue) -> {
	 		if (oldValue == true && newValue == false) finishApp();	// 最後に呼ばれる
	 	});
	 	stage.show();									// ログイン画面を表示
	 }

	public static void main(String[] args) {
		setServices();
		setSampleData();
		launch(args);
	}

	private static void setServices() {
		IEmpRepo empRepo = new InMemoryEmpRepo();
		empService = new EmployeeService(empRepo);

		IAttendRepo attendRepo = new InMemoryAttendRepo();
		attendService = new AttendService(attendRepo);

		paySlipService = new PaySlipService(attendRepo);

		IBusinessTripRepo btRepo = new InMemoryBusinessTripRepo();
		registerService = new RegisterService(btRepo);
	}

	private static void setSampleData() {
		try {
			Employee emp1 = empService.addEmp(false, 2, 2, 2, 1, new Date(), "password", "sho", "山口県", "山口市", "0120-333-999", "733-0000");
			Employee emp2 = empService.addEmp(false, 4, 3, 2, 1, new Date(), "password", "Mikan", "広島県", "広島市", "0120-222-234", "999-0292");
			Employee emp3 = empService.addEmp(false, 1, 1, 2, 1, new Date(), "password", "Taro", "東京都", "品川区", "0120-333-1111", "212-2222");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static Stage getStage() {
		return stage;
	}

	public void endApp(WindowEvent e) {										// finishAppより先に呼ばれる
		// TODO 自動生成されたメソッド・スタブ
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);		// 確認ダイアログ
		alert.setTitle("終了確認");
		alert.setHeaderText("この部分はヘッダテキスト部です");
		alert.setContentText("アプリケーションを終了しますか？");
		ButtonType buttonType = alert.showAndWait().orElse(ButtonType.CANCEL);
		if (buttonType == ButtonType.CANCEL) e.consume();
	}

	private void finishApp() {
		 Platform.exit();																		// アプリ終了
	}

	public static Main getInstance() {
		return instance;
	}

	public void sendLoginController(String subValue) {
		stage.setTitle("ログイン画面");			// ステージタイトル設定
		LoginController controller = new LoginController(subValue);	// ログインコントローラの生成
		replaceScene(controller);					// シーンの置換
	}

	public void sendBusinessTripController() {
		stage.setTitle("ログイン画面");			// ステージタイトル設定
		BusinessTripController controller = new BusinessTripController();	// ログインコントローラの生成
		replaceScene(controller);					// シーンの置換
	}

	public void sendCalcSalaryController() {
		stage.setTitle("ログイン画面");			// ステージタイトル設定
		CalcSalaryController controller = new CalcSalaryController();	// ログインコントローラの生成
		replaceScene(controller);					// シーンの置換
	}


	public void sendSubController(String loginname) {
		stage.setTitle("サブ画面");
		SubController controller = new SubController(loginname);		// サブコントローラの生成
		replaceScene(controller);					// シーンの置換
	}

	public void sendEmployeeController() {
		stage.setTitle("サブ画面");
		EmployeeController controller = new EmployeeController();		// サブコントローラの生成
		replaceScene(controller);					// シーンの置換
	}

	public void sendVacationController() {
		stage.setTitle("サブ画面");
		VacationController controller = new VacationController();		// サブコントローラの生成
		replaceScene(controller);					// シーンの置換
	}

	private void replaceScene(Parent controller) {
		Scene scene = stage.getScene();		// ステージからシーンを取得
		if (scene == null) {							// まだステージにシーンが設定されていない場合：初期画面時
			scene = new Scene(controller);		// コントローラを使用してシーンの生成
			stage.setScene(scene);					// ステージにシーンを設定
		 } else {												// すでにステージにシーンが設定されている場合：画面遷移時
			 scene.setRoot(controller);				// ルートノードにコントローラを設定してシーンを置換する
		 }
	}
}
