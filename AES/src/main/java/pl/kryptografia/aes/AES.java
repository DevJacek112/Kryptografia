package pl.kryptografia.aes;

public class AES {
    public static byte[][][] dodajKluczRundy(byte[][][] naszaTablica, byte[][] kluczRundy){
        for(int i = 0; i < naszaTablica.length; i++){
            naszaTablica[i] = xor2d(naszaTablica[i], kluczRundy);
        }

        return naszaTablica;
    }

    public static byte[] zamianaNaPojedynczaTablice(byte[][][] originalArray) {
        int n = originalArray.length;
        int m = originalArray[0].length;
        int p = originalArray[0][0].length;
        byte[] flattenedArray = new byte[n * m * p];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    flattenedArray[idx++] = originalArray[i][j][k];
                }
            }
        }
        return flattenedArray;
    }

    public static byte[][] Rcon = {
            {0x01, 0x00, 0x00, 0x00},
            {0x02, 0x00, 0x00, 0x00},
            {0x04, 0x00, 0x00, 0x00},
            {0x08, 0x00, 0x00, 0x00},
            {0x10, 0x00, 0x00, 0x00},
            {0x20, 0x00, 0x00, 0x00},
            {0x40, 0x00, 0x00, 0x00},
            {(byte) 0x80, 0x00, 0x00, 0x00},
            {0x1B, 0x00, 0x00, 0x00},
            {0x36, 0x00, 0x00, 0x00}
    };

    public static byte[][] kluczRunda(byte[] klucz, int iloscRund){

        byte[][] tablicaZwrotna = new byte[iloscRund + 1][16];
        System.arraycopy(klucz, 0, tablicaZwrotna[0], 0, 16);

        for(int i = 0; i < iloscRund; i++) {

            byte[] naszaKolumna = {tablicaZwrotna[i][3], tablicaZwrotna[i][7], tablicaZwrotna[i][11], tablicaZwrotna[i][15]};


            byte tymczasowy = naszaKolumna[3];
            naszaKolumna[3] = naszaKolumna[0];
            naszaKolumna[0] = tymczasowy; //zamieniamy 4 miejsce z 16

            for (int j = 1; j < 4; j++) {
                naszaKolumna[j] = RijndaelSBox.ZnajdzWSBoxie(naszaKolumna[j]);
            }

            byte[] pierwszaKolumna = {tablicaZwrotna[i][0], tablicaZwrotna[i][4], tablicaZwrotna[i][8], tablicaZwrotna[i][12]};

            byte[] wynikPierwszaKolumna = xor((xor(naszaKolumna, pierwszaKolumna)), Rcon[i]);

            byte[] drugaKolumna = {tablicaZwrotna[i][1], tablicaZwrotna[i][5], tablicaZwrotna[i][9], tablicaZwrotna[i][13]};

            byte[] wynikDrugaKolumna = xor(wynikPierwszaKolumna, drugaKolumna);

            byte[] trzeciaKolumna = {tablicaZwrotna[i][2], tablicaZwrotna[i][6], tablicaZwrotna[i][10], tablicaZwrotna[i][14]};

            byte[] wynikTrzeciaKolumna = xor(wynikDrugaKolumna, trzeciaKolumna);

            byte[] czwartaKolumna = {tablicaZwrotna[i][3], tablicaZwrotna[i][7], tablicaZwrotna[i][11], tablicaZwrotna[i][15]};

            byte[] wynikCzwartaKolumna = xor(wynikTrzeciaKolumna, czwartaKolumna);

            byte[] wynikowaTablica = new byte[16];

            System.arraycopy(wynikPierwszaKolumna, 0, wynikowaTablica, 0, 4);
            System.arraycopy(wynikDrugaKolumna, 0, wynikowaTablica, 4, 4);
            System.arraycopy(wynikTrzeciaKolumna, 0, wynikowaTablica, 8, 4);
            System.arraycopy(wynikCzwartaKolumna, 0, wynikowaTablica, 12, 4);

            tablicaZwrotna[i+1] = wynikowaTablica;
        }

        return tablicaZwrotna;
    }

    public static byte[][] podzielTablice(byte[] oryginalnaTablica) {
        int liczbaKawalkow = (int) Math.ceil((double) oryginalnaTablica.length / 16); //liczba po podzieleniu przez 16, zaokraglona w gore



        byte[][] podzielonaTablica = new byte[liczbaKawalkow + 1][16]; //nowa tablica domyslnie wypelniona zerami, dodajemy jeden kawalek w ktorym bedzie informacja o tym ile zer dodalismy

        int x = 0;
        int y = 0;
        int iloscLiter = 0;

        for (int i = 0; i < oryginalnaTablica.length; i++) {
            podzielonaTablica[x][y] = oryginalnaTablica[i];
            y++;

            if (y == 16) {
                x++;
                y = 0;
            }
            iloscLiter = i + 1; //+1 bo liczy od 0, a potrzebujemy ilosci liter
        }

        podzielonaTablica[liczbaKawalkow][0] = (byte) ((liczbaKawalkow * 16) - iloscLiter);

        return podzielonaTablica;
    }

    public static byte[][][] podzielNaTrzy(byte[][] TablicaBajtow){
        byte[][][] wynik = new byte[TablicaBajtow.length][4][4];
        for (int i = 0; i < TablicaBajtow.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    wynik[i][j][k] = TablicaBajtow[i][(j * 4) + k];
                }
            }
        }
        return wynik;
    }

    public static byte[][][] SubBytes(byte[][][] TablicaBajtow){

        for (int i = 0; i < TablicaBajtow.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    TablicaBajtow[i][j][k] = RijndaelSBox.ZnajdzWSBoxie(TablicaBajtow[i][j][k]); //dla kazdego bajta znajdujemy odpowiedni zamiennik w sboxie
                }
            }
        }

        return TablicaBajtow;
    }

    public static byte[][][] shiftRows(byte[][][] tablica){

        for(int k = 0; k < tablica.length; k++){

            for(int i=1;i<4;i++) {
                //kopia rzędu
                byte[] pom = new byte[4];
                for (int j = 0; j < 4; j++) {
                    pom[j] = tablica[k][i][j];
                }
                //pierwszy element rzędu
                byte tmp = tablica[k][i][0];
                //wzór [i][j-i%4]
                for (int j = 1; j < 4; j++) {
                    int modulo = j - i;
                    while (modulo < 0) modulo += 4;
                    modulo = modulo % 4;
                    tablica[k][i][modulo] = pom[j];
                }
                //wstawienie pierwszego elementu na odpowiednie miejsce
                tablica[k][i][4 - i] = tmp;
            }

        }
        return  tablica;
    }
    public static byte[][][] mixColumns(byte[][][] tablica){
        int[] nowaKolumna = new int[4];
        byte b2=0x02, b3=0x03;
        for(int i=0;i< tablica.length;i++){
            for(int j=0;j<4;j++){
                nowaKolumna[0] = mul(b2, tablica[i][0][j]) ^ mul(b3, tablica[i][1][j]) ^ tablica[i][2][j] ^ tablica[i][3][j];
                nowaKolumna[1] = tablica[i][0][j] ^ mul(b2, tablica[i][1][j]) ^ mul(b3, tablica[i][2][j]) ^ tablica[i][3][j];
                nowaKolumna[2] = tablica[i][0][j] ^ tablica[i][1][j] ^ mul(b2, tablica[i][2][j]) ^ mul(b3, tablica[i][3][j]);
                nowaKolumna[3] = mul(b3, tablica[i][0][j]) ^ tablica[i][1][j] ^ tablica[i][2][j] ^ mul(b2, tablica[i][3][j]);
                for(int k=0;k<4;k++)tablica[i][k][j] = (byte) nowaKolumna[k];
            }
        }
        return tablica;
    }

    public static byte mul(byte a, byte b){
        byte p = 0;
        byte hbit = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
            hbit = (byte) (a & 0x80);
            a <<= 1;
            if (hbit != 0) {
                a ^= 0x1b; //  x^8 + x^4 + x^3 + x + 1
            }
            b >>= 1;
        }
        return p;
    }
    public static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[Math.min(a.length, b.length)];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    public static byte[][] xor2d(byte[][] a, byte[][] b) {
        int rows = a.length;
        int cols = a[0].length;
        byte[][] result = new byte[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = (byte) (a[i][j] ^ b[i][j]);
            }
        }

        return result;
    }
}
