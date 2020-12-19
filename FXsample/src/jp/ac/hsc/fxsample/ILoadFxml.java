package jp.ac.hsc.fxsample;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public interface ILoadFxml {
	default void loadFxml(String fxml) {
		// レイアウト取得
		FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
	 	loader.setRoot(this);								// ルートノードの設定
	 	loader.setController(this);							// コントローラの設定
	 	try {
	 		loader.load();										// レイアウトのロード(try-catch構文必須)
	 	} catch (IOException e) {
	 		throw new RuntimeException(e);			// 例外時は、実行時例外を通知する
	 	}
	 }
}
