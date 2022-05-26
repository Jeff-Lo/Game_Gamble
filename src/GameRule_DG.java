import java.util.Arrays;

public class GameRule_DG {
	private int[] cards;
	private int money;
	private int stake;
	private int[] gate;
	private int draw;

	public GameRule_DG() {
		this.gate = new int[2];// 門柱兩根
		Arrays.fill(gate, 0);
		cards = new int[52]; // 定義52個大小的陣列表示撲克牌
		Arrays.fill(cards, 0); // 還沒用過的牌設0，用過的牌設1
	}
	
	public void setStake(int stake) {
		this.stake = stake;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public int getMoney() {
		return money;
	}

	public int getGate(int id) {
		return gate[id];
	}

	public int getDraw() {
		return draw;
	}

	public void sortGates() {
		if (gate[0] % 13 > gate[1] % 13) {
			int x = gate[1];
			gate[1] = gate[0];
			gate[0] = x;
		}
	}
	
	public void setGates() {
		for (int i=0; i<2; i++) {
			int n = (int) (Math.random() * 52);
			while (cards[n] == 1) {
				n = (int) (Math.random() * 52);
			}
			cards[n] = 1;
			gate[i] = n;		
		}
	}
	
	public void Draw() {
		int n = (int) (Math.random() * 52);
		while (cards[n] == 1) {
			n = (int) (Math.random() * 52);
		}
		draw = n;
	}

	public String show(int a) {
		int a_change = a % 13 + 1;
		String result = "";
		if (a_change == 1) {
			result += "A";
		} else if (a_change == 11) {
			result += "J";
		} else if (a_change == 12) {
			result += "Q";
		} else if (a_change == 13) {
			result += "K";
		} else {
			result +=  "" + a_change;
		}
		
		if (0 <= a && a <= 12) {
			result += "_spade";
		} else if (13 <= a && a <= 25) {
			result += "_heart";
		} else if (26 <= a && a <= 38) {
			result += "_diamond";
		} else {
			result += "_club";
		}
		return result;
	}

	public String judge() {
		if (draw % 13 > gate[0] % 13 && draw % 13 < gate[1] % 13) {
			money += stake;
			return "恭喜過關, 你獲得了" + stake + "元!";
		} else if (draw % 13 == gate[0] % 13 || draw % 13 == gate[1] % 13) {
			money -= stake * 2;
			return "Double, 你失去了" + stake * 2 + "元。";
		} else if (draw % 13 == gate[0] % 13 && draw % 13 == gate[1] % 13) {
			money -= stake * 3;
			return "Triple, 你失去了" + stake * 3+ "元。";
		} else {
			money -= stake;
			return "超出範圍, 你失去了" + stake+ "元。";
		}

	}
}