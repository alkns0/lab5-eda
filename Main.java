import java.util.Scanner;
import java.util.TreeMap;
import java.util.ArrayList;

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
	
	public void registerPlayer(String playerName){
        if (!checkPlayer(playerName)){
            Player player = new Player(playerName);
            players.put(playerName, player);
        }	 
        else {
            System.out.println("Ya está en el tree map");
        }
	}
	
	public boolean checkPlayer(String playerName){
	    if (players.containsKey(playerName)){
	        return true;
	    }
	    else {
	        return false;
	    }
	}
	
	public ArrayList <Player> winRange(int desde, int hasta){
	    ArrayList <Player> lista = new ArrayList <Player>();
	    return lista;
	}
	
	
	
}

class ConnectFour {
    char[][] grid = new char[7][6];
    char currentSymbol;
    
    public ConnectFour(){ 
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++){
                grid[i][j] = ' ';
            }
        }
        currentSymbol = 'X';
    }
    
    public boolean makeMove(int z){
        
        if (grid[z][0] != ' ' || z > 7 || z < 0) { //Verificar si el primero está ocupado (Significa que esta lleno)
            return false;
        }
        else { //Se puede realizar el movimiento
            for(int k = 0; k < 6; k++){
                if (grid[z][k] == ' '){
                    grid[z][k] = currentSymbol;
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isGameOver(){
        //Pendiente
        return false;
    }
    
}

class Game {
    String status;
    String winnerPlayerName;
    String playerNameA;
    String playerNameB;
    ConnectFour juego;
    
    public Game(String _playerNameA, String _playerNameB){
        playerNameA = _playerNameA;
        playerNameB = _playerNameB;
        winnerPlayerName = "";
        status = "IN_PROGRESS";
        juego = new ConnectFour();
    }
    
    public String play(){
        return "";
    }
    
}


	public class Main
	{
		public static void main(String[] args) {
            System.out.println("holamundo");
		}
	}
