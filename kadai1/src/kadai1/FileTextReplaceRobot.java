package kadai1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;

/**
 * 
 * ファイルの中の任意の文字列を任意の文字列に変換し、新規ファイルに書き込みます
 * @param String 元のパス
 * @param String 先のパス
 * @param String 置き換えられる文字
 * @param String 置き換える文字
 * @author sakaishow
 *
 */
public class FileTextReplaceRobot {
	Path fromPath;
	Path toPath;
	String fromStr;
	String toStr;
	Scanner sc;
	
	FileTextReplaceRobot(String fromPath, String toPath, String fromStr, String toStr) {
		this.fromPath = Path.of(fromPath);
		this.toPath = Path.of(toPath);
		this.fromStr = fromStr;
		this.toStr = toStr;
		this.sc = new Scanner(System.in);
	}
	
	void run() {
		String input;
		Charset charset = Charset.forName("MS932");
		
		while(true) {
			// 元のファイルが存在するか確認
			if(!Files.exists(this.fromPath)) {
				System.out.println(getFileName(this.fromPath) + "が存在しません。作成してください");
				System.out.println("作成したら「1」, キャンセルする場合は「1以外」を入力してください");
				input = sc.nextLine();
				if(Objects.equals(input, "1")) {
					continue;
				}else {
					System.out.println("キャンセルしました\n------------");
					break;
				}
			}
			
			String originText = null;
			try {
				originText = Files.readString(this.fromPath, charset);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 元のファイルになんらかの文字が存在するか確認
			if(originText.isEmpty()) {
				System.out.println(getFileName(this.fromPath) + "に文字を入力してください");
				System.out.println("入力したら「1」, キャンセルする場合は「1以外」を入力してください");
				input = sc.nextLine();
				if(Objects.equals(input, "1")) {
					continue;
				}else {
					System.out.println("キャンセルしました\n------------");
					break;
				}
			}
			
			String replacedText = originText.replaceAll(this.fromStr, this.toStr);
			
			// 保存先フォルダに既に同じ名前のファイルが存在するか確認
			if(Files.exists(this.toPath)) {
				System.out.println(getFileName(this.fromPath) + "が既に存在しています。置き換えますか？\n Yes->「1」, No->「1以外」");
				input = sc.nextLine();
				if(!Objects.equals(input, "1")) {
					System.out.println("キャンセルしました\n------------");
					break;
				}
			}
			try {
				Files.copy(this.fromPath, this.toPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				Files.writeString(this.toPath, replacedText);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println(getFileName(toPath) + "の作成が完了しました\n-------------");
			break;
		}
	}
	
	public static String getFileName(Path path) {
		String strPath = path.toString();
		String[] ele = strPath.split("/");
		return ele[ele.length - 1];
	}
}
