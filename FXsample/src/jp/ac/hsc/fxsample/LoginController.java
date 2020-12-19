package jp.ac.hsc.fxsample;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

public class LoginController extends GridPane implements Initializable, ILoadFxml {
	// レイアウトとの紐づけ
	 @FXML private TextField				loginname;			// ログイン名
	 @FXML private PasswordField		password;			// パスワード
	 @FXML private Button				authButton;		// 認証ボタン
	 @FXML private Button				exitButton;			// 終了ボタン

	 private String subString;  			// サブ画面から渡される文字列
	 private String sName;				// ログイン名から取得した氏名

	 public LoginController(String subString) {
		 this.subString = subString;		// サブ画面から渡される文字列：メインから遷移するときはnull
		 loadFxml("Login.fxml");			// レイアウトのロードとコントローラ設定
	 }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		authButton.setOnAction(event -> {				// サブ画面へ遷移する
			 // パスワードチェックとログイン名から氏名を取得するDBアクセス
			 Boolean bTrans = chkPwAndDBAccess(loginname.getText(), password.getText());
			 if (bTrans)  Main.getInstance().sendSubController(sName);
		});

		// 終了ボタンクリック時のアクション
		exitButton.setOnAction(event -> {
			displayDialog();											// サブ画面から渡された文字列のダイアログ表示
			 // メインで定義したステージに対してクローズリクエストを発行
			 Main.getStage().fireEvent(new WindowEvent(Main.getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
		});
	}

	private Boolean chkPwAndDBAccess(String login, String pw) {
		Boolean ret = false;
		if (pw.trim().equals("pw")) {		// trimしたパスワードをチェックする
			 // DB接続
		 	sName = "";										// DB検索で取得する氏名の初期化
		 	try (Connection db = DBconnect.getConnection()) {					// 自動DBクローズ
		 		String mySQL = "select S_NAME from syain where S_ID = ?";	// SQL文を準備
		 		PreparedStatement pstmt = db.prepareStatement(mySQL);	// プリペアドステートメント設定
		 		pstmt.setInt(1, Integer.parseInt(login.trim()));						// プレースホルダにパラメタセット
		 		ResultSet rs = pstmt.executeQuery();										// SQL文を実行
		 		if (rs.next()) sName = rs.getString("S_NAME");						// 取得に成功：氏名を取得
		 		rs.close();																				// 結果破棄
		 		pstmt.close();																		// SQL破棄
		 	} catch (Exception e) {
		 		throw new RuntimeException(e);	// 例外時は、実行時例外を通知する
		 	}
		 	if (!sName.isEmpty()) ret = true;		// 取得成功時はtrueを返却
		 	else {									// 取得失敗：ダイアログ表示
		 	Alert alert = new Alert(AlertType.WARNING,
		 			"ログイン名がDBに存在しません。\n再入力してください。", ButtonType.OK);
		 		alert.setTitle("ログイン名不一致");	// 警告ダイアログで表示
		 		alert.showAndWait();						// OKボタンクリックまで待機
		 		loginname.clear();							// ログイン名入力域をクリア
		 		password.clear();							// パスワード入力域をクリア
		 		loginname.requestFocus();				// フォーカス要求
		 	}
		 } else {										// パスワード不一致：ダイアログ表示
		 	Alert alert = new Alert(AlertType.WARNING,
		 			"パスワードが一致していません。\n再入力してください。", ButtonType.OK);
		 	alert.setTitle("パスワード不一致");		// 警告ダイアログで表示
		 	alert.showAndWait();							// OKボタンクリックまで待機
		 	password.clear();								// パスワード入力域のみをクリア
		 	password.requestFocus();					// フォーカス要求
		 }
		 return ret;
	}

	private void displayDialog() {
		 // サブ画面から渡される文字列を情報ダイアログで表示：ボタンはOKボタン1つ
		 Alert alert = new Alert(AlertType.INFORMATION, subString, ButtonType.OK);
		 alert.setTitle("情報ダイアログ");						// タイトル設定
		 alert.showAndWait();										// OKボタンがクリックされるまで待機する
	}

}
