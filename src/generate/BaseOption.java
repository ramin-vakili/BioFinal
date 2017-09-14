package generate;

public class BaseOption {
	private String pattern;
	private int num;
	
	public BaseOption(String pattern, int num) {
		this.pattern = pattern;
		this.num = num;
	}

	public String getPattern() {
		return pattern;
	}

	public int getNum() {
		return num;
	}
	public void decreaseNum(){
		num-- ;
	}
}
