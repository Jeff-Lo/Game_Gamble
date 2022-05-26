import java.util.Arrays;

public class Player_99 {
	private String name;
	private int money;
	private int stake;
	private int[] player;
	
	public Player_99() {
		this.player=new int[5]; //玩家最多5張牌
		Arrays.fill(player,0);
	}
	
	public Player_99(String name) {
		this.name=name;
		this.money=1000;
		this.player=new int[5]; //玩家最多5張牌
		Arrays.fill(player,0);
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setMoney(int money) {
		this.money=money;
	}
	
	public void setStake(int stake) {
		this.stake = stake;
	}
	
	public void setPlayer(int[] number) {
		this.player=number;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getStake() {
		return stake;
	}
	
	public int[] getPlayer() {
		return player;
	}
	
	public void addMoney(int dollar) {
		money+=dollar;
	}
	
	public void reduceMoney(int dollar) {
		money-=dollar;
	}
	
}