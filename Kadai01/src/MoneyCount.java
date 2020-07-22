import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MoneyCount { 
	public static final boolean	FALSE	 	= false; 										// boolean型の値false
	public static final boolean	TRUE	 	= true;											// boolean型の値true
	public static final int		MAX_DIGIT 	= 3;											// コインやお札の枚数の最大桁数
	public static final int		MONEY_LOWER	= 0;											// 入力できる料金の最低値
	public static final int		MONEY_UPPER	= (int)(10000*Math.pow(10, MAX_DIGIT))-1;		// 入力できる料金の最大値
	public static final int		LOWER		= 0;											// 繰り返しの初期値
	public static final int		ZERO		= 0;											// int型の値0
	public static final int		ABNORMAL	= -1;											// 異常が発生した場合に返却する値
	public static final String	E001 		= "入出力エラーが発生しました";						// IOExceptionが発生した場合に標準出力する値
	public static final String	W001 		= "数値以外または整数範囲外が入力されました\n";		// NumberFormatExceptionが発生した場合に標準出力する値
	public static final String	W002 		= "数値が%d以下です\n";							// MONEY_LOWER以下の場合に標準出力する値
	public static final String	W003 		= "数値が%dを超えました\n";							// MONEY_UPPER以上の場合に標準出力する値
	public static final String 	I001 		= "数値を入力してください";							// 最初の数値入力前に標準出力する値
	public static final String 	I002 		= "数値を再入力してください\n";						// 入力値が正しくなく、再入力前に標準出力する値
	public static final String 	I003 		= "プログラムを強制終了します";						// 異常が発生し、プログラムを強制終了する場合に標準出力する値
	public static final String 	FORMAT 		= "%5d円：%" + MAX_DIGIT + "d枚\n";				// それぞれのコインとお札の枚数を標準出力する際に使用する値
	public static final int[] 	YENS 		= { 10000, 5000, 1000, 500, 100, 50, 10, 5, 1 };// 使用するコインやお札の配列
	
	public static void main(String[] args) {
		int money = enterMoney(); 						// 入力値または強制終了するための値を受け取る
		if(money < ZERO) return; 						// 強制終了するための値の場合、mainを終了
		
		int[] eachMoneyCount = calcMoneyCount(money);	// それぞれのコインやお札の枚数を受け取る
		
		for(int i=LOWER; i<YENS.length; i++) {			// それぞれのコインやお札の枚数を標準出力
			System.out.printf(FORMAT, YENS[i], eachMoneyCount[i]);
		}
	}
	
	/**
	 * 標準入力を受け取り、正しい値かを判定するメソッドを呼び出し、正しければ入力された値を返却する
	 * @return int型 標準入力から受け取り、バリデーションされた値を返却。IOExceptionが吐かれた場合は、-1を返却
	 */
	static int enterMoney() {
		System.out.println(I001);
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(System.in));) {
			while(true) {											// 正しい値が入力されるまで繰り返す
				try {
					int input = Integer.parseInt(in.readLine());	// 標準入力を受け取る
					if(!validation(input))							// 正しい値か検証
						continue;
					return input;									// inputを返却
				} catch (NumberFormatException e) {
					System.out.println(W001 + I001);
				}
			}
		} catch (IOException e) {
			System.out.println(E001 + I003);
			return ABNORMAL;
		}
	}
	
	/**
	 * 入力された料金から使用するコインやお札の枚数を計算するメソッド
	 * @param money
	 * @return int[]型 それぞれのコインやお札の枚数を返却
	 */
	static int[] calcMoneyCount(int money) {
		int[] result = new int[YENS.length];
		for(int i=ZERO; i<YENS.length; i++) {	// それぞれのお札やコインの枚数を計算
			result[i] = money / YENS[i];
			money %= YENS[i];
		}
		return result;							// resultを返却
	}
	
	/**
	 * 入力された数値が正しいか検証するメソッド
	 * @param input
	 * @return boolean型 値が正しければTRUE、正しくなければFALSEを返却する
	 */
	static boolean validation(int input) {
		if(input <= MONEY_LOWER) {							// MONEY_LOWER以下の場合
			 System.out.printf(W002 + I002, MONEY_LOWER);
			 return FALSE;									// FALSEを返却
		 }
		 if(input > MONEY_UPPER) {							// MONEY_UPPERより大きい場合
			 System.out.printf(W003 + I002, MONEY_UPPER);
			 return FALSE;									// FALSEを返却
		 }
		 return TRUE;										// 値が正しいのでTRUEを返却
	}
}
