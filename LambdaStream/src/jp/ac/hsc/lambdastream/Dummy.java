package jp.ac.hsc.lambdastream;

interface Person {
	long getId();															// 抽象メソッド
	default String getName() { return "日本太郎"; }		// デフォルトメソッド
}

interface Names {
	default String getName() { return "安芸次郎"; }		// デフォルトメソッド
}


public class Dummy implements Person, Names {
	@Override
	public String getName() {
		return Person.super.getName();
	}

	@Override
	public long getId() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}

class NamesClass {
	String getName() { return "東京三郎"; }
}

class Sclass extends NamesClass implements Names {
	public void a() {
		super.getName();
	}
	@Override
	public
	String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return super.getName();
	}



}