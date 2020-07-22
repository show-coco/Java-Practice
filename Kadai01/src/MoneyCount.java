import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MoneyCount {
	public static final boolean	EXIT_FIRST 	= false;
	public static final boolean	EXIT_FINAL 	= true;
	public static final boolean	FALSE	 	= false;
	public static final boolean	TRUE	 	= true;
	public static final int		INPUT_FIRST	= 0;
	public static final int		MAX_DIGIT 	= 4;
	public static final int		MONEY_LOWER	= 0;
	public static final int		MONEY_UPPER	= (int)(10000*Math.pow(10, MAX_DIGIT))-1;
	public static final int		LOWER		= 0;
	public static final int		ZERO		= 0;
	public static final int		ABNORMAL	= -1;
	public static final String	E001 		= "入出力エラーが発生しました";
	public static final String	W001 		= "数値以外または整数範囲外が入力されました\n";
	public static final String	W002 		= "数値が%d以下です\n";
	public static final String	W003 		= "数値が%dを超えました\n";
	public static final String 	I001 		= "数値を入力してください";
	public static final String 	I002 		= "数値を再入力してください\n";
	public static final String 	I003 		= "プログラムを終了します";
	public static final String 	FORMAT 		= "%5d円：%" + MAX_DIGIT + "d枚\n";
	public static final int[] 	YENS 		= { 10000, 5000, 1000, 500, 100, 50, 10, 5, 1 };
	
	public static void main(String[] args) {
		int money = enterMoney();
		if(money < ZERO) return;
		
		int[] eachMoneyCount = calcMoneyCount(money);
		
		for(int i=LOWER; i<YENS.length; i++) {
			System.out.printf(FORMAT, YENS[i], eachMoneyCount[i]);
		}
	}
	
	/**
	 * 標準入力を受け取り、正しい値かを判定します
	 * @return int型 標準入力から受け取り、バリデーションされた値。IOExceptionが吐かれた場合は、-1を返す
	 */
	static int enterMoney() {
		System.out.println(I001);
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(System.in));) {
			while(true) {
				try {
					int input = Integer.parseInt(in.readLine());
					if(!validation(input))
						continue;
					return input;
				} catch (NumberFormatException e) {
					System.out.println(W001 + I001);
				}
			}
		} catch (IOException e) {
			System.out.println(E001 + I003);
			return ABNORMAL;
		}
	}
	
	static int[] calcMoneyCount(int money) {
		int[] result = new int[YENS.length];
		for(int i=0; i<YENS.length; i++) {
			result[i] = money / YENS[i];
			money %= YENS[i];
		}
		return result;
	}
	
	static boolean validation(int input) {
		if(input <= MONEY_LOWER) {
			 System.out.printf(W002 + I002, MONEY_LOWER);
			 return FALSE;
		 }
		 if(input > MONEY_UPPER) {
			 System.out.printf(W003 + I002, MONEY_UPPER);
			 return FALSE;
		 }
		 return TRUE;
	}
}
