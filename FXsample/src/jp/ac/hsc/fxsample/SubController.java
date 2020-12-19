package jp.ac.hsc.fxsample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SubController extends GridPane implements Initializable, ILoadFxml{
	// レイアウトとの紐づけ
	 @FXML private Label		name;		// ログイン名を表示するラベル
	 @FXML	private Button	okButton;	// OKボタン

	 private String loginname;				 // ログイン画面から渡されるログイン名

	 public SubController(String loginname) {
		 this.loginname = loginname;		// ログイン画面から渡されるログイン名
		 loadFxml("Sub.fxml");					// レイアウトのロードとコントローラ設定
	 }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// ログイン名のラベルへの設定
		name.setText(loginname);

		// OKボタンクリック時のアクション
		okButton.setOnAction(event -> {			// ログイン画面へ遷移する
			Main.getInstance().sendLoginController("サブ画面から値が渡されました！");
		});
	}

}
