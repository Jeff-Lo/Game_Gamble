import java.util.Arrays;

public class ComPlayer_21 {
	private String name;
	private int[] computer;
	
	public ComPlayer_21() {
		this.computer=new int[5]; //電腦最多5張牌
		Arrays.fill(computer,0);
	}
	
	public ComPlayer_21(String name) {
		this.name=name;
		this.computer=new int[5]; //電腦最多5張牌
		Arrays.fill(computer,0);
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setComputer(int[] number) {
		this.computer=number;
	}
	
	public String getName() {
		return name;
	}
	
	public int[] getComputer() {
		return computer;
	}
	
}

