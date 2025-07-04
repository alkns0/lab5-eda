import java.util.*;

class BST<Key extends Comparable<Key>, Value> {
    private Node raiz; //campeon principal del torneo

    private class Node {
        Integer victorias;    // Número de victorias
        List<String> nombreJugadores;   // Nombre del jugador
        Node left, right; //jugadores con menos y más victorias.

        //constructor con un campeon predispuesto
        public Node(Integer key, String value) {
            this.victorias = key;
            this.nombreJugadores = new ArrayList<>();
            this.nombreJugadores.add(value);
        }
    }

    //funcion de ingresar un jugador, pero utilizando una funcion recursiva.
    public void put(Integer key, String value) {
        raiz = put(raiz, key, value);
    }
    private Node put(Node x, Integer key, String value) {
        if (x == null){
            return new Node(key, value);
        }// si el nodo x es null, quiero decir que no hay nada en el arbol
        //entonces se crea este nodo que sera el que va arriba.

        int cmp = key.compareTo(x.victorias);//comparador de victorias.
        if (cmp < 0) { //si es menor que el actual se va por la rama de la izquierda
            x.left = put(x.left, key, value);//llega hasta la hoja y lo inserta.
        } else if (cmp > 0) {//mayor victorias que el actual se va por la rama de la derecha
            x.right = put(x.right, key, value);//llega hasta la hoja y lo inserta
        } else {
            x.nombreJugadores.add(value);
        }
        return x;//retorna el nodo, ya que esto es la funcion recursiva.
    }


    public List<String> get(Integer key) {

        Node x = raiz;//se empieza desde el campeon
        while (x != null) {//bucle que se repite hasta que lo encuentra
            int cmp = key.compareTo(x.victorias);//se hace un comparador de victorias
            if (cmp < 0) x = x.left;//si es menor se va por la izquierda
            else if (cmp > 0) x = x.right;//si es mayor por la derecha
            else return x.nombreJugadores;//lo encontro, lo retorna
        }
        return Collections.emptyList();//en caso de no encontrarlo retorna null
    }

    public List<String> inOrderVictorias() {
        List<String> result = new ArrayList<>();
        inOrder(raiz, result);
        return result;
    }

    private void inOrder(Node x, List<String> result) {
        if (x != null) {
            inOrder(x.left, result);
            result.addAll(x.nombreJugadores);
            inOrder(x.right, result);
        }
    }

    public Integer findSuccessorKey(int wins) {
        return findSuccessorKey(raiz, wins, null);
    }

    private Integer findSuccessorKey(Node x, int wins, Integer currentSuccessor) {
        if (x == null) return currentSuccessor;

        if (x.victorias > wins) {
            return findSuccessorKey(x.left, wins, x.victorias);
        } else {
            return findSuccessorKey(x.right, wins, currentSuccessor);
        }
    }

    public Integer getRootKey() {
        return raiz != null ? raiz.victorias : null;
    }

}

class HashST<Key, Value> {
    private static final int INIT_CAPACITY = 16;
    private Node[] tablaHash; //Mueble de cajitas
    public int tamanoCajita;

    private static class Node {
        Object key;
        Object val;
        Node next;

        Node(Object key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    //constructor con capacidad fija.
    public HashST() {
        this(INIT_CAPACITY);
    }
    //constructor con capacidad variable.
    public HashST(int capacity) {
        tablaHash = new Node[capacity];
    }

    public void insertarCosa(Key key, Value val) {
        int i = hash(key);//calcula el indice Hash, o sea calcula en que cajita va a estar.
        //recorre la lista enlazada que hay en esa cajita
        for (Node x = tablaHash[i]; x != null; x = x.next) {
            if (key.equals(x.key)) {//si la clave ya existe
                x.val = val;//lo actualiza
                return;//se sale
            }
        }
        //Pero si la clave no existia lo crea.
        tablaHash[i] = new Node(key, val, tablaHash[i]);
        tamanoCajita++;//incrementa el contador de elementos (esto siempre se activa en uno de los dos casos.)
    }

        public Value ObtenerValorAsociadoClave(Key key) {
            int i = hash(key);//calcula el indice de la cajita
            for (Node x = tablaHash[i]; x != null; x = x.next) {//recorre la cajita
                if (key.equals(x.key)) {//si lo encuentra
                    return (Value)x.val;//retorna el valor
                }
            }
            return null;//si no lo encuentra retorna null.
        }

    // Método contains para verificar existencia de clave
    public boolean contains(Key key) {
        return ObtenerValorAsociadoClave(key) != null;//retornara true o false dependiendo si existe la clave
    }

    //Es como un repartidor que decide en qué cajita va cada cosa.
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % tablaHash.length;
    }
}

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
        int totalGames = wins + draws + losses;
        if(totalGames==0) {
            return 0.0;
        } else {
            return (double)wins/totalGames;
        }
    }
    public int getWins() {
        return wins;
    }
}

class ScoreBoard {
    BST<Integer, String> winTree = new BST<>();
    HashST<String, Player> players = new HashST<>();
    int playedGames=0;

    public void addGameResult(String winnerPlayerName, String looserPlayerName, boolean draw) {
        registerPlayer(winnerPlayerName);
        registerPlayer(looserPlayerName);

        Player winner = players.ObtenerValorAsociadoClave(winnerPlayerName);
        Player loser  = players.ObtenerValorAsociadoClave(looserPlayerName);
        if(!draw) {
            // Eliminar entrada anterior si existe
            if (winner.getWins() > 0) {
                winTree.get(winner.getWins()).remove(winner.playerName);
            }

            winner.addWin();
            loser.addLoss();
            winTree.put(winner.getWins(), winnerPlayerName);

        } else {
            winner.addDraw();
            loser.addDraw();
        }

        playedGames++;
    }

    public void registerPlayer(String playerName){
        if (!checkPlayer(playerName)){
            players.insertarCosa(playerName, new Player(playerName));
        }
    }

    public boolean checkPlayer(String playerName){
        return players.contains(playerName);
    }

    public ArrayList <Player> winRange(int desde, int hasta){

        ArrayList <Player> lista = new ArrayList <Player>();

        for(String playerName : winTree.inOrderVictorias()) {
            Player p = players.ObtenerValorAsociadoClave(playerName);

            if(p.getWins() >= desde && p.getWins() <= hasta) {
                lista.add(p);
            }
        }
        return lista;
    }

    public Player[] winSuccessor(int wins){
        List<Player> result = new ArrayList<>();

        // 1. Encontrar la clave sucesora
        Integer successorKey = winTree.findSuccessorKey(wins);

        // 2. Si existe, obtener todos los jugadores con esa clave
        if (successorKey != null) {
            List<String> playerNames = winTree.get(successorKey);
            for (String name : playerNames) {
                Player p = players.ObtenerValorAsociadoClave(name);
                if (p != null) {
                    result.add(p);
                }
            }
        }
        // 3. Convertir a array y retornar
        return result.toArray(new Player[0]);
    }
}

class ConnectFour {
    final int columnas=6;
    final int filas=7;
    char[][] grid;
    char currentSymbol;

    public ConnectFour(){
        grid= new char[filas][columnas];
        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                grid[i][j] = ' ';
            }
        }
        currentSymbol = 'X';
    }

    public boolean makeMove(int z){

        if (z < 0 || z >= 7) {
            return false;
        }

        for(int f = filas-1 ; f >= 0; f--){
            if (grid[f][z] == ' '){
                grid[f][z] = currentSymbol;
                if(currentSymbol == 'X') {
                    currentSymbol = 'O';
                } else {
                    currentSymbol = 'X';
                }
                return true;
            }
        }
        //si llega hasta aqui quiere decir que esta llena
        return false;
    }

    public boolean isGameOver(){
        if (checkWin('X') || checkWin('O')) {
            return true;
        }
        return Empate();
    }

    boolean Empate() {
        for (int c = 0; c < 6; c++) {
            if (grid[0][c] == ' ') {
                return false;
            }
        }
        return true;
    }

    boolean checkWin(char player) {

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {

                // Horizontal
                if (c <= 2 && grid[f][c] == player && grid[f][c+1] == player &&
                        grid[f][c+2] == player && grid[f][c+3] == player) {
                    return true;
                }

                // Vertical
                if (f <= 3 &&
                        grid[f][c] == player && grid[f+1][c] == player &&
                        grid[f+2][c] == player && grid[f+3][c] == player) {
                    return true;
                }

                // Diagonal derecha (\)
                if (f <= 3 && c <= 2 &&
                        grid[f][c] == player && grid[f+1][c+1] == player &&
                        grid[f+2][c+2] == player && grid[f+3][c+3] == player) {
                    return true;
                }

                // Diagonal izquierda (/)
                if (f <= 3 && c >= 3 &&
                        grid[f][c] == player && grid[f+1][c-1] == player &&
                        grid[f+2][c-2] == player && grid[f+3][c-3] == player) {
                    return true;
                }
            }
        }
        return false;
    }
    public void MostrarJuego() {
        System.out.println("\n  0   1   2   3   4   5"); // Encabezado de columnas

        // Borde superior
        System.out.println("+---+---+---+---+---+---+");

        for (int fila = 0; fila < filas; fila++) {
            System.out.print("| ");
            for (int col = 0; col < columnas; col++) {
                //x-> rojos ; o-> Amarillo
                if (grid[fila][col] == 'X') {
                    System.out.print("\u001B[31mX\u001B[0m | ");  // Rojo
                } else if (grid[fila][col] == 'O') {
                    System.out.print("\u001B[33mO\u001B[0m | ");  // Amarillo
                } else {
                    System.out.print("  | ");
                }
            }
            System.out.println();

            System.out.println("+---+---+---+---+---+---+");
        }

        // Turnos
        System.out.println("Turno actual: " +
                (currentSymbol == 'X' ? "\u001B[31mX\u001B[0m" : "\u001B[33mO\u001B[0m"));
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
        Scanner scanner = new Scanner(System.in);
        while (status.equals("IN_PROGRESS")) {

            System.out.print("\033[H\033[2J");
            //limpiar pantalla cada vez
            System.out.flush();

            // Mostrar tablero
            juego.MostrarJuego();

            // Obtener movimiento válido
            boolean validMove = false;
            while (!validMove && status.equals("IN_PROGRESS")) {
                try {
                    System.out.print("Ingresa columna (0-5): ");
                    int column = scanner.nextInt();

                    if (column < 0 || column > 5) {
                        System.out.println("¡Columna inválida! Debe ser entre 0 y 5.");
                        continue;
                    }

                    validMove = juego.makeMove(column);
                    if (!validMove) {
                        System.out.println("¡Columna llena! Elige otra columna.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("¡Entrada inválida! Debes ingresar un número.");
                    scanner.next(); // Limpiar el buffer
                }
            }

            // Verificar estado del juego después de cada movimiento
            if (juego.isGameOver()) {
                if (juego.checkWin('X')) {
                    status = "VICTORY";
                    winnerPlayerName = playerNameA;
                    System.out.println("¡GANADOR: " + winnerPlayerName + " (X)!");
                } else if (juego.checkWin('O')) {
                    status = "VICTORY";
                    winnerPlayerName = playerNameB;
                    System.out.println("¡GANADOR: " + winnerPlayerName + " (O)!");
                } else {
                    status = "DRAW";
                    winnerPlayerName = "";
                    System.out.println("¡EMPATE!");
                }
                juego.MostrarJuego();
            }
        }
        return winnerPlayerName;
    }
}

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScoreBoard scoreBoard = new ScoreBoard();
        boolean exitProgram = false;
        System.out.println("\n¡Bienvenido a Conecta 4!");

        while (!exitProgram) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Registrar nuevo jugador");
            System.out.println("2. Iniciar una nueva partida");
            System.out.println("3. Ver estadísticas de jugadores");
            System.out.println("4. Buscar jugador por rango de victorias");
            System.out.println("5. Buscar jugador por sucesor de victorias");
            System.out.println("6. Salir");
            System.out.print("Elige una opción: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (choice) {
                    case 1:
                        registrar(scanner, scoreBoard);
                        break;
                    case 2:
                        Jugar(scanner, scoreBoard);
                        break;
                    case 3:
                        Estadisticas(scoreBoard);
                        break;
                    case 4:
                        BuscarJugadoresRango(scanner, scoreBoard);
                        break;
                    case 5:
                        BuscarJugadorSucesor(scanner, scoreBoard);
                        break;
                    case 6:
                        exitProgram = true;
                        System.out.println("Gracias por jugar");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    static void registrar(Scanner scanner, ScoreBoard scoreBoard) {
        System.out.println("\n--- Registrar Nuevo Jugador ---");
        System.out.print("Ingresa el nombre del nuevo jugador: ");
        String playerName = scanner.nextLine();
        if (scoreBoard.checkPlayer(playerName)) {
            System.out.println("Jugador" + playerName + "' ya está registrado");
        } else {
            scoreBoard.registerPlayer(playerName);
            System.out.println("Jugador '" + playerName + "' registrado exitosamente.");
        }
    }
    static String selectPlayer(Scanner scanner, ScoreBoard scoreBoard, String prompt) {
        String chosenPlayerName = null;
        while (chosenPlayerName == null) {
            System.out.print(prompt + " (ingresa el nombre): ");
            String input = scanner.nextLine();
            if (scoreBoard.checkPlayer(input)) {
                chosenPlayerName = input;
            } else {
                System.out.println("Jugador '" + input + "' no encontrado. Por favor, asegúrate de haberlo registrado primero.");
            }
        }
        return chosenPlayerName;
    }
    static void Jugar(Scanner scanner, ScoreBoard scoreBoard) {
        System.out.println("\n--- Iniciar Nueva Partida ---");
        if (scoreBoard.players.tamanoCajita < 2) {
            System.out.println("Necesitas al menos 2 jugadores registrados para iniciar una partida.");
            return;
        }

            String playerA = selectPlayer(scanner, scoreBoard, "Jugador 1 (X)");
        if (playerA == null) return;

        String playerB;
        while (true) {
            playerB = selectPlayer(scanner, scoreBoard, "Jugador 2 (O)");
            if (playerB == null) return;
            if (!playerA.equals(playerB)) {
                break;
            }
            System.out.println("¡Los jugadores no pueden ser los mismos! Por favor, elige otro Jugador 2.");
        }

        System.out.println("\n¡Iniciando partida entre " + playerA + " (X) y " + playerB + " (O)!");
        Game game = new Game(playerA, playerB);
        String gameResult = game.play();

        switch (game.status) {
            case "VICTORY":
                String winnerName = gameResult;
                String loserName = winnerName.equals(playerA) ? playerB : playerA;
                scoreBoard.addGameResult(winnerName, loserName, false);
                System.out.println("Resultado registrado " + winnerName + " gana la partida.");
                break;
            case "DRAW":
                scoreBoard.addGameResult(playerA, playerB, true);
                System.out.println("Resultado registrado, partida empatada.");
                break;
        }
        System.out.println("Presiona Enter para continuar...");
        scanner.nextLine();
    }
    static void Estadisticas(ScoreBoard scoreBoard) {
        System.out.println("\n--- Estadísticas de Todos los Jugadores ---");
        List<String> playerNamesSortedByWins = scoreBoard.winTree.inOrderVictorias();

        if (playerNamesSortedByWins.isEmpty()) { // Simplificado: Solo verificamos el winTree
            System.out.println("No hay jugadores con victorias registradas.");
            System.out.println("Juega una partida para ver estadísticas.");
            return;
        }

        System.out.println("\nJugadores ordenados por victorias:");
        for (String playerName : playerNamesSortedByWins) {
            Player p = scoreBoard.players.ObtenerValorAsociadoClave(playerName);
            if (p != null) {
                System.out.println("Jugador: " + p.playerName + ", Victorias: " + p.wins + ", Derrotas: " + p.losses + ", Empates: " + p.draws);
            }
        }

        if (scoreBoard.playedGames > 0) {
            System.out.println("\nTotal de partidas jugadas: " + scoreBoard.playedGames);
        }
    }
    static void BuscarJugadoresRango(Scanner scanner, ScoreBoard scoreBoard) {
        System.out.println("\n--- Buscar Jugadores por Rango de Victorias ---");
        try {
            System.out.print("Ingresa el número mínimo de victorias (desde): ");
            int desde = scanner.nextInt();
            System.out.print("Ingresa el número máximo de victorias (hasta): ");
            int hasta = scanner.nextInt();
            scanner.nextLine();

            if (desde > hasta || desde < 0) {
                System.out.println("Rango inválido. Asegúrate que 'desde' sea <= 'hasta' y ambos sean no negativos.");
                return;
            }

            ArrayList<Player> playersInRange = scoreBoard.winRange(desde, hasta);
            if (playersInRange.isEmpty()) {
                System.out.println("No se encontraron jugadores con victorias entre " + desde + " y " + hasta + ".");
            } else {
                System.out.println("Jugadores con victorias entre " + desde + " y " + hasta + ":");
                for (Player p : playersInRange) {
                    System.out.println("Jugador: " + p.playerName + ", Victorias: " + p.wins + ", Derrotas: " + p.losses + ", Empates: " + p.draws);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingresa números para el rango.");
            scanner.nextLine();
        }
    }
    static void BuscarJugadorSucesor(Scanner scanner, ScoreBoard scoreBoard) {
        System.out.println("\n--- Buscar Jugador por Sucesores de Victorias ---");
        try {
            System.out.print("Ingresa la cantidad de victorias para buscar su sucesor: ");
            int wins = scanner.nextInt();
            scanner.nextLine();

            Player[] successors = scoreBoard.winSuccessor(wins);
            if (successors.length > 0) {
                System.out.println("Jugadores con " + wins + " o más victorias (el más cercano):");
                for (Player p : successors) {
                    System.out.println("Jugador: " + p.playerName + ", Victorias: " + p.wins + ", Derrotas: " + p.losses + ", Empates: " + p.draws);
                }
            } else {
                System.out.println("No se encontraron jugadores con esa cantidad o más de victorias.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingresa un número.");
            scanner.nextLine();
        }
}
}
