import java.util.Scanner;
import java.util.TreeMap;


class Player {
	String playerName;
	int wins;
	int draws;
	int losses;

	public Player(String name) {
		this.playerName=name;
		this.wins=0;
		this.draws=0;
		this.losses=0;
	}

	public void addWin() {
		wins++;
	}

	public void addDraw() {
		draws++;
	}

	public void addLoss() {
		losses++;
	}

	public double winRate() {
		double decimal= wins/(wins+draws+losses);
		if(decimal>0 && decimal<1) {
			return decimal;
		} else {
			return 0.0;
		}
	}
}

class ScoreBoard {
	TreeMap<Integer, String> winTree = new TreeMap<>();
	TreeMap<String, Player> players = new TreeMap<>();
	int playedGames=0;

	public void addGameResult(String winnerPlayerName, String looserPlayerName, boolean draw) {
		Player p1 = new Player(winnerPlayerName);
		Player p2 = new Player(looserPlayerName);
		if(!draw) {
			p1.addWin();
			winTree.put(p1.wins,winnerPlayerName);
			players.put(winnerPlayerName,p1);

			p2.addLoss();
			winTree.put(p1.wins,looserPlayerName);
			players.put(looserPlayerName,p2);
		} else {
			p1.addDraw();
			p2.addLoss();
		}

	}
}
	public class Main
	{
		public static void main(String[] args) {
            System.out.println("holamundo");
		}
	}
