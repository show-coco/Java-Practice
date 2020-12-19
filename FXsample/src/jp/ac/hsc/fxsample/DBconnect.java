package jp.ac.hsc.fxsample;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnect {
	static Connection getConnection() throws Exception {
		 // DB場所、ユーザ名、パスワード設定
		 String url		= "jdbc:ucanaccess://C:/Users/5192037/Desktop/ws-java/FXsample/Sample_gui.accdb";
		 String user	= "";
		 String pass	= "";

		 Connection con = DriverManager.getConnection(url, user, pass);	// DB接続
		 return con;
	}
}
