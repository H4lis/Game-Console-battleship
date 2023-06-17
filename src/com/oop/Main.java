import java.util.*;

// Interface untuk objek yang bisa ditembak
interface Shootable {
    boolean isHit(int x, int y);
    void hit();
}
// Objek kapal
class Ship implements Shootable {
    private int x;
    private int y;
    private boolean isHit;

    public Ship(int x, int y) {
        this.x = x;
        this.y = y;
        this.isHit = false;
    }

    public boolean isHit(int x, int y) {
        return this.x == x && this.y == y;
    }

    public void hit() {
        isHit = true;
    }

    public boolean isSunk() {
        return isHit;
    }
}

// Objek peta
class Map {
    private Shootable[][] grid;
    private Set<String> history;

    public Map(int size) {
        this.grid = new Shootable[size][size];
        this.history = new HashSet<>();
    }

    public void placeShip(Shootable ship, int x, int y) {
        grid[x][y] = ship;
    }

    public boolean isOccupied(int x, int y) {
        return grid[x][y] != null;
    }

    public boolean isHit(int x, int y) {
        Shootable target = grid[x][y];
        if (target != null) {
            target.hit();
            return true;
        }
        return false;
    }

    public boolean isAlreadyHit(int x, int y) {
        String coordinate = x + "-" + y;
        return history.contains(coordinate);
    }

    public void addHitToHistory(int x, int y) {
        String coordinate = x + "-" + y;
        history.add(coordinate);
    }

    public boolean isGameEnd() {
        for (Shootable[] row : grid) {
            for (Shootable cell : row) {
                if (cell instanceof Ship && !((Ship) cell).isSunk()) {
                    return false;
                }
            }
        }
        return true;
    }
}

// Objek pemain
class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void takeTurn(Map opponentMap, int x, int y) {
        boolean isHit = opponentMap.isHit(x, y);
        if (isHit) {
            opponentMap.addHitToHistory(x, y);
            System.out.println("Target bombed!");
        } else {
            System.out.println("Missed!");
        }
    }
}

// Main class
 class BattleshipGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inisialisasi pemain
        System.out.println("========== About ===========");
        System.out.println("     Battleship Game 6A  ");
        System.out.println("============================");
        System.out.println("bergiliran menebak koordinat");
        System.out.println("untuk mengebom kapal lawan");
        System.out.println("============================");
        System.out.println();

     System.out.print("Press Enter to start the game...");
        scanner.nextLine();

        System.out.print("Enter player 1 name: ");
        String player1Name = scanner.nextLine();
        System.out.print("Enter player 2 name: ");
        String player2Name = scanner.nextLine();

        Map player1Map = createMap();
        Map player2Map = createMap();
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);

        int round = 1;
        boolean gameEnd = false;
        Player currentPlayer = player1;

        while (!gameEnd) {
            System.out.println("Round " + round);
            System.out.println("Current player: " + currentPlayer.getName());
            System.out.print("Enter target coordinates (x y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (currentPlayer == player1) {
                player2.takeTurn(player1Map, x, y);
                gameEnd = player1Map.isGameEnd();
                currentPlayer = player2;
            } else {
                player1.takeTurn(player2Map, x, y);
                gameEnd = player2Map.isGameEnd();
                currentPlayer = player1;
            }

            round++;
        }

        System.out.println("Game over!");
        System.out.println("Winner: " + currentPlayer.getName());
    }

    public static Map createMap() {
        Map map = new Map(7);

        // Menambahkan kapal ke peta secara acak
        Random random = new Random();
        int shipCount = 0;
        while (shipCount < 5) {
            int x = random.nextInt(7);
            int y = random.nextInt(7);
            if (!map.isOccupied(x, y)) {
                map.placeShip(new Ship(x, y), x, y);
                shipCount++;
            }
        }

        return map;
    }
}
