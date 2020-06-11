package kadai1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		Path fromPath = Path.of("/Users/sakaishow/bss/test.txt");
		Path editPath = Path.of("/Users/sakaishow/bss/edit.txt");
		Charset charset = Charset.forName("MS932");
		Scanner sc = new Scanner(System.in);
		String input;
		
		try {
			// 元のファイルが存在するか確認
			while(true) {
				if(!Files.exists(fromPath)) {
					System.out.println(getFileName(fromPath) + "が存在しません。作成してください");
					System.out.println("作成したら「1」, キャンセルする場合は「1以外」を入力してください");
					input = sc.nextLine();
					if(Objects.equals(input, "1")) {
						continue;
					}else {
						System.out.println("キャンセルしました");
						throw new Exception(fromPath + "ファイルが存在しません");
					}
				}
				
				// 元のファイルに文字が存在するか確認
				String originText = Files.readString(fromPath, charset);
				if(originText.isEmpty()) {
					System.out.println(getFileName(fromPath) + "に文字を入力してください");
					System.out.println("入力したら「1」, キャンセルする場合は「1以外」を入力してください");
					input = sc.nextLine();
					if(Objects.equals(input, "1")) {
						continue;
					}else {
						System.out.println("キャンセルしました");
						throw new Exception(fromPath + "に文字が存在しません");
					}
				}
				
				String replacedText = originText.replaceAll("12", "xy");
				Files.copy(fromPath, editPath, StandardCopyOption.REPLACE_EXISTING);
				Files.writeString(editPath, replacedText);
				System.out.println("処理が完了しました");
				break;
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileName(Path path) {
		String strPath = path.toString();
		String[] ele = strPath.split("/");
		return ele[ele.length - 1];
	}
}
