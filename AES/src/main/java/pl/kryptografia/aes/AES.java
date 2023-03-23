package pl.kryptografia.aes;

import java.util.Arrays;

public class AES {

    public static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[Math.min(a.length, b.length)];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
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

    public static byte[][] kluczRunda(int numerRundy, byte[][] kluczeTablica){

        if(numerRundy == 1){
            byte[] naszaKolumna = {kluczeTablica[1][3], kluczeTablica[1][7], kluczeTablica[1][11], kluczeTablica[1][15]};

            byte tymczasowy = naszaKolumna[3];
            naszaKolumna[3] = naszaKolumna[0];
            naszaKolumna[0] = tymczasowy; //zamieniamy 4 miejsce z 16

            for(int j = 1; j < 4; j++){
                naszaKolumna[j] = RijndaelSBox.ZnajdzWSBoxie(naszaKolumna[j]);
            }

            byte[] pierwszaKolumna = {kluczeTablica[1][0], kluczeTablica[1][4], kluczeTablica[1][8], kluczeTablica[1][12]};

            byte[] wynikPierwszaKolumna = xor((xor(naszaKolumna, pierwszaKolumna)), Rcon[0]);

            byte[] drugaKolumna = {kluczeTablica[1][1],kluczeTablica[1][5],kluczeTablica[1][9],kluczeTablica[1][13]};

            byte[] wynikDrugaKolumna = xor(wynikPierwszaKolumna, drugaKolumna);

            byte[] trzeciaKolumna = {kluczeTablica[1][2],kluczeTablica[1][6],kluczeTablica[1][10],kluczeTablica[1][14]};

            byte[] wynikTrzeciaKolumna = xor(wynikDrugaKolumna, trzeciaKolumna);

            byte[] czwartaKolumna = {kluczeTablica[1][3],kluczeTablica[1][7],kluczeTablica[1][11],kluczeTablica[1][15]};

            byte[] wynikCzwartaKolumna = xor(wynikTrzeciaKolumna, czwartaKolumna);

            byte[] wynikowaTablica = new byte[16];

            System.arraycopy(wynikPierwszaKolumna, 0, wynikowaTablica, 0, 4);
            System.arraycopy(wynikDrugaKolumna, 0, wynikowaTablica, 4, 4);
            System.arraycopy(wynikTrzeciaKolumna, 0, wynikowaTablica, 8, 4);
            System.arraycopy(wynikCzwartaKolumna, 0, wynikowaTablica, 12, 4);

            kluczeTablica[1] = wynikowaTablica;
        }

        return kluczeTablica;
    }

    public static byte[][] UzupelnijKlucze(int dlugoscTablicy, byte[] pojedynczyKlucz){

        byte[][] tablicaKluczy = new byte[dlugoscTablicy][16];

        for(int i =0; i < dlugoscTablicy; i++){
            System.arraycopy(pojedynczyKlucz, 0, tablicaKluczy[i], 0, 16);
        }

        return tablicaKluczy;

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

    public static byte[][][] SubBytes(byte[][] TablicaBajtow){

        byte[][] przetlumaczonaTablica = new byte[TablicaBajtow.length][16];

        for(int i = 0; i < TablicaBajtow.length; i++){
            for(int j = 0; j < 15; j++){
                przetlumaczonaTablica[i][j] = RijndaelSBox.ZnajdzWSBoxie(TablicaBajtow[i][j]); //dla kazdego bajta znajdujemy odpowiedni zamiennik w sboxie
            }
        }

        byte[][][] podzielonaTablica = new byte[TablicaBajtow.length][4][4];

        for (int i = 0; i < TablicaBajtow.length; i++){

            for(int j = 0; j < 4; j++){

                for(int k = 0; k < 4; k++){
                    podzielonaTablica[i][j][k] = przetlumaczonaTablica[i][j + k];
                }

            }

        }

        return podzielonaTablica;
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

}
