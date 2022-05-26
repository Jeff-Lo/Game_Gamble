import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class GameRule_99 {
	private int[] cards;
	private Scanner in = new Scanner(System.in);
	private int n1 = 5;
	private int n2 = 5;
	private int counter;
	public int number;

	public GameRule_99() {
		cards = new int[52]; // 定義52個大小的陣列表示撲克牌
		counter = 0;
		number = 0;
		Arrays.fill(cards, 0); // 還沒用過的牌設0，用過的牌設1
	}

	// 讓所有的牌都變成沒有用過，玩家和電腦雙方的初始牌數回復成1張
	public void clear(Player_99 player, ComPlayer_99 computer) {
		Arrays.fill(cards, 0);
		Arrays.fill(player.getPlayer(), 0);
		Arrays.fill(computer.getComputer(), 0);
		n1 = 5;
		n2 = 5;
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

	// a代表玩家或電腦，number是牌數，如果pOrC是1就顯示玩家的牌面，pOrC是0就顯示電腦的牌面
	public String[] show(int[] a, int number, int pOrC) {
		String result[] = new String[5];
		if (pOrC == 1) {
			//System.out.println("\n" + "你現在的牌(共" + number + "張)是:");
		}

		else {
			//System.out.println("\n" + "電腦現在的牌(共" + number + "張)是:");
		}

		int a_change = 0;
		for (int i = 0; i < number; i++) {
			a_change = a[i] % 13 + 1;
			if (a_change == 1) {
				result[i] = "A";
				result[i] += printSuit(a, i);
			} else if (a_change == 11) {
				result[i] = "J";
				result[i] += printSuit(a, i);
			} else if (a_change == 12) {
				result[i] = "Q";
				result[i] += printSuit(a, i);
			} else if (a_change == 13) {
				result[i] = "K";
				result[i] += printSuit(a, i);
			} else {
				result[i] = "" + a_change;
				result[i] += printSuit(a, i);
			}
		}
		return result;
	}

	// 隨機抽一張牌(0<=n<=51)
	public int nextOne() {
		int n = (int) (Math.random() * 52);
		while (cards[n] == 1) {
			n = (int) (Math.random() * 52);
		}
		cards[n] = 1;
		return n;
	}

	// 計算點數
	public void computerCounter(int i) {
		Random ran = new Random();
		if (i == -1) {
			counter += 0;
		} else if (i % 13 == 0) { // A
			counter += 1;
		} else if (i % 13 == 9) { // 10
			if (ran.nextInt(2) == 0) {
				counter += 10;
			} else {
				counter -= 10;
			}
		} else if (i % 13 == 10) { // 11
			System.out.println("Pass");
		} else if (i % 13 == 11) { // 12
			if (ran.nextInt(2) == 0) {
				counter += 20;
			} else {
				counter -= 20;
			}
		} else if (i % 13 == 12) { // 13
			counter = 99;

		} else { // 2.3.4.5.6.7.8.9
			counter = counter + i % 13 + 1;
		}
		number++;
	}

	public void counter(int i) {
		if (i == -1) {
			counter += 0;
		} else if (i % 13 == 0) { // A
			counter += 1;
		} else if (i % 13 == 9) { // 10
			System.out.println("你要選擇+10或是-10(+代表+10)");
			String input = in.next();
			if (input.equals("+")) {
				counter += 10;
			} else {
				counter -= 10;
			}
		} else if (i % 13 == 10) { // 11
			System.out.println("Pass");
		} else if (i % 13 == 11) { // 12
			System.out.println("你要選擇+20或是-20(+代表+20)");
			String input = in.next();
			if (input.equals("+")) {
				counter += 20;
			} else {
				counter -= 20;
			}
		} else if (i % 13 == 12) { // 13
			counter = 99;

		} else { // 2.3.4.5.6.7.8.9
			counter = counter + i % 13 + 1;
		}
		number++;
	}

	public boolean judge() {
		if (counter > 99) {
//			System.out.println("Game Over");
			return false;
		} else {
//			System.out.println("現在累積點數為" + counter);
			return true;
		}
	}

	public int getCounter() {
		return counter;
	}

	public int getNumber() {
		return number;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getN1() {
		return n1;
	}

}
