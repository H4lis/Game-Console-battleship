import java.util.*;

interface Tembak {
    boolean kena(int x, int y);
    void terkena();
}

class Kapal implements Tembak {
    private int x;
    private int y;
    private boolean terkena;

    public Kapal(int x, int y) {
        this.x = x;
        this.y = y;
        this.terkena = false;
    }

    public boolean kena(int x, int y) {
        return this.x == x && this.y == y;
    }

    public void terkena() {
        terkena = true;
    }

    public boolean tenggelam() {
        return terkena;
    }
}

class Peta {
    private Tembak[][] grid;
    private Set<String> riwayat;
    private Set<String> kapalDitembak;

    public Peta(int ukuran) {
        this.grid = new Tembak[ukuran][ukuran];
        this.riwayat = new HashSet<>();
        this.kapalDitembak = new HashSet<>();
    }

    public void tempatkanKapal(Tembak kapal, int x, int y) {
        grid[x][y] = kapal;
    }

    public boolean adaKapal(int x, int y) {
        return grid[x][y] != null;
    }

    public boolean kena(int x, int y) {
        Tembak target = grid[x][y];
        if (target != null) {
            target.terkena();
            kapalDitembak.add(x + "-" + y);
            return true;
        }
        return false;
    }

    public boolean sudahTertembak(int x, int y) {
        String koordinat = x + "-" + y;
        return riwayat.contains(koordinat);
    }

    public boolean tembakKapalDitembak(int x, int y) {
        String koordinat = x + "-" + y;
        return kapalDitembak.contains(koordinat);
    }

    public void tambahkanTembakanKeRiwayat(int x, int y) {
        String koordinat = x + "-" + y;
        riwayat.add(koordinat);
    }

    public boolean akhirPermainan() {
        for (Tembak[] baris : grid) {
            for (Tembak sel : baris) {
                if (sel instanceof Kapal && !((Kapal) sel).tenggelam()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void tampilkanPeta() {
        int ukuran = grid.length;

        System.out.print("  ");
        for (int i = 0; i < ukuran; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < ukuran; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < ukuran; j++) {
                if (sudahTertembak(i, j)) {
                    if (adaKapal(i, j)) {
                        System.out.print("X ");
                    } else {
                        System.out.print("O ");
                    }
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }
}

class Pemain {
    private String nama;
    private Peta peta;

    public Pemain(String nama, Peta peta) {
        this.nama = nama;
        this.peta = peta;
    }

    public String getNama() {
        return nama;
    }

    public Peta getPeta() {
        return peta;
    }

    public void giliran(Pemain lawan, int x, int y) {
        Peta petaLawan = lawan.getPeta();
        Peta petaSendiri = getPeta();

        if (petaSendiri.sudahTertembak(x, y)) {
            System.out.println("Sasaran sudah ditembak sebelumnya!");
            return;
        }

        if (petaSendiri.tembakKapalDitembak(x, y)) {
            System.out.println("Kamu tidak bisa menembak kapal yang sudah ditembak!");
            return;
        }

        boolean kena = petaLawan.kena(x, y);
        if (kena) {
            petaSendiri.tambahkanTembakanKeRiwayat(x, y);
            System.out.println("Target terkena!");
        } else {
            if (petaSendiri.adaKapal(x, y)) {
                System.out.println("Gawat, kena kapal sendiri!");
            } else {
                System.out.println("Meleset, Sasaran Zonk");
            }
            petaSendiri.tambahkanTembakanKeRiwayat(x, y);
        }
    }
}

public class Main {
    private static Pemain pemain1;
    private static Pemain pemain2;
    private static Peta peta;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========== Tentang ==========");
        System.out.println("   Permainan Battleship 6B   ");
        System.out.println("============================");
        System.out.println("Bergiliran menebak koordinat");
        System.out.println("untuk menembak kapal lawan");
        System.out.println("============================");
       

        System.out.print("Tekan Enter untuk memulai permainan...");
        scanner.nextLine();
         System.out.println();

        System.out.print("Masukkan nama pemain 1: ");
        String namaPemain1 = scanner.nextLine();
        System.out.print("Masukkan nama pemain 2: ");
        String namaPemain2 = scanner.nextLine();
    

        peta = buatPeta();
        pemain1 = new Pemain(namaPemain1, peta);
        pemain2 = new Pemain(namaPemain2, peta);

        tempatkanKapal(pemain1);
        tempatkanKapal(pemain2);

        int putaran = 1;
        boolean akhirPermainan = false;
        Pemain pemainSaatIni = pemain1;
         System.out.println();

        while (!akhirPermainan) {
            System.out.println();
            System.out.println("Putaran " + putaran);
            System.out.println("Pemain saat ini: " + pemainSaatIni.getNama());
            System.out.print("Masukkan koordinat sasaran (x y): \n");

            int x = scanner.nextInt();
            int y = scanner.nextInt();

            pemainSaatIni.giliran(pemainSaatIni == pemain1 ? pemain2 : pemain1, x, y);
            peta.tampilkanPeta();

            akhirPermainan = peta.akhirPermainan();
            if (akhirPermainan) {
                System.out.println("Permainan selesai!");
                System.out.println("Pemenang: " + pemainSaatIni.getNama());
            } else {
                pemainSaatIni = pemainSaatIni == pemain1 ? pemain2 : pemain1;
                putaran++;
            }
        }

        scanner.close();
    }

    private static Peta buatPeta() {
        return new Peta(7);
    }

    private static void tempatkanKapal(Pemain pemain) {
        Peta peta = pemain.getPeta();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(7);
            int y = random.nextInt(7);

            while (peta.adaKapal(x, y)) {
                x = random.nextInt(7);
                y = random.nextInt(7);
            }

            Tembak kapal = new Kapal(x, y);
            peta.tempatkanKapal(kapal, x, y);
        }
    }
}
