import java.util.Arrays;
import java.util.Scanner;

public class GameRule_21 {
	private int[] cards;
	private Scanner in = new Scanner(System.in);
	private int n1 = 2;
	private int n2 = 2;
	private int winOrLose; // 0代表贏，1代表平手，2代表輸

	public GameRule_21() {
		cards = new int[52]; // 定義52個大小的陣列表示撲克牌
		Arrays.fill(cards, 0); // 還沒用過的牌設0，用過的牌設1
	}

	// 讓所有的牌都變成沒有用過，玩家和電腦雙方的初始牌數回復成1張
	public void clear(Player_21 player, ComPlayer_21 computer) {
		Arrays.fill(cards, 0);
		Arrays.fill(player.getPlayer(), 0);
		Arrays.fill(computer.getComputer(), 0);
		n1 = 2;
		n2 = 2;
	}

	public int getN1() {
		return n1;
	}

	public void setN1(int n1) {
		this.n1 = n1;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public int getN2() {
		return n2;
	}

	// 0代表贏，1代表平手，2代表輸
	public int getWinOrLose() {
		return winOrLose;
	}
	
	// 印花色，a代表玩家或電腦，i代表a的index
	public String printSuit(int[] a, int i) {
		if (0 <= a[i] && a[i] <= 12) {
			return "_spade";
		} else if (13 <= a[i] && a[i] <= 25) {
			return "_heart";
		} else if (26 <= a[i] && a[i] <= 38) {
			return "_diamond";
		} else {
			return "_club";
		}
	}

	// a代表玩家或電腦，number是牌數
	public String[] show(int[] a, int number) {
		int a_change = 0;
		String[] result = new String[number];
		for (int i = 0; i < number; i++) {
			a_change = a[i] % 13 + 1;
			if (a_change == 1) {
				result[i] = "A" + printSuit(a, i);
			} else if (a_change == 11) {
				result[i] = "J" + printSuit(a, i);
			} else if (a_change == 12) {
				result[i] = "Q" + printSuit(a, i);
			} else if (a_change == 13) {
				result[i] = "K" + printSuit(a, i);
			} else {
				result[i] = "" + a_change + printSuit(a, i);
			}
		}
		return result;
	}

	// 隨機抽一張牌(0<=n<=51)
	public int nextOne() {
		int n = (int) (Math.random() * 52);
		if (cards[n] == 0) {
			cards[n] = 1;
			return n;
		} else {
			return nextOne();
		}
	}

	// 算出現在擁有的牌點數總和是多少，a代表玩家或電腦，number代表牌數
	public int checkPoints(int[] a, int number) {
		int[] b = Arrays.copyOf(a, a.length);
		int sum = 0;
		int b_change = 0;
		for (int i = 0; i < number; i++) {
			b_change = b[i] % 13 + 1;
			if (b_change == 11 || b_change == 12 || b_change == 13) {
				b_change = 10;
			}
			sum += b_change;
		}
		return sum;
	}

	// 判定玩家要不要抽下一張牌
	public boolean ask(Player_21 player) {
		if (checkPoints(player.getPlayer(), n1) >= 21) {
			return false;
		}

//		System.out.println("\n還要再抽一張?Y/N");
		String answer = in.nextLine();

		if (!answer.equals("n") && !answer.equals("N")) {
			return true;
		} else {
			return false;
		}
	}

	// 判定電腦要不要抽下一張牌
	public boolean computerDraw(ComPlayer_21 computer) {
		if (checkPoints(computer.getComputer(), n2) > 17) {
			return false;
		} else {
			return true;
		}
	}

	// 判斷勝負，a代表玩家，b代表電腦，number代表牌數
	public String judge(Player_21 player, ComPlayer_21 computer, int number) {
		int sum_a = checkPoints(player.getPlayer(), n1);
		int sum_b = checkPoints(computer.getComputer(), n2);
		if (sum_a <= 21 && number == 5) {
			player.addMoney(player.getStake() * 2);
			winOrLose=0;
			return "恭喜你過五關勝利贏得下注金額" + player.getStake() + "+Bonus" + player.getStake() + "元! 你現在共有"
					+ player.getMoney() + "元。"; // 贏了過五關
		}
		else if (sum_a <= 21 && sum_b > 21) {
				player.addMoney(player.getStake());
				winOrLose=0;
				return "恭喜你勝利贏得下注金額" + player.getStake() + "元! 你現在共有" + player.getMoney() + "元。"; // 贏了沒過五關
		} 
		else if (sum_a > 21 && sum_b > 21) {
			winOrLose=1;
			return "雙方平手，我們將歸還你的下注金額! 你現在共有" + player.getMoney() + "元。"; // 平手
		} 
		else if (sum_a > 21 && sum_b <= 21) {
			player.reduceMoney(player.getStake());
			winOrLose=2;
			return "非常可惜你輸了，沒收你的下注金額! 你現在共有" + player.getMoney() + "元。"; // 輸了
		} 
		else if (sum_a <= 21 && sum_b <= 21) {
			if (Math.abs(sum_a - 21) < Math.abs(sum_b - 21)) {
				if (number == 5) {
					player.addMoney(player.getStake() * 2);
					winOrLose=0;
					return "恭喜你過五關勝利贏得下注金額" + player.getStake() + "+Bonus" + player.getStake() + "元! 你現在共有"
							+ player.getMoney() + "元。"; // 贏了過五關
				} 
				else {
					player.addMoney(player.getStake());
					winOrLose=0;
					return "恭喜你勝利贏得下注金額" + player.getStake() + "元! 你現在共有" + player.getMoney() + "元。"; // 贏了沒過五關
				}
			} 
			else if (Math.abs(sum_a - 21) == Math.abs(sum_b - 21)) {
				winOrLose=1;
				return "雙方平手，我們將歸還你的下注金額! 你現在共有" + player.getMoney() + "元。"; // 平手
			} 
			else {
				player.reduceMoney(player.getStake());
				winOrLose=2;
				return "非常可惜你輸了，沒收你的下注金額! 你現在共有" + player.getMoney() + "元。"; // 輸了
			}
		}
		return null;
	}

}
