package pl.kryptografia.aes;

import java.util.Arrays;

public class AES {

    public static byte[][] podzielTablice(byte[] oryginalnaTablica) {
        int liczbaKawalkow = (int) Math.ceil((double) oryginalnaTablica.length / 4); //liczba po podzieleniu przez 4, zaokraglona w gore



        byte[][] podzielonaTablica = new byte[liczbaKawalkow + 1][4]; //nowa tablica domyslnie wypelniona zerami, dodajemy jeden kawalek w ktorym bedzie informacja o tym ile zer dodalismy

        int x = 0;
        int y = 0;
        int iloscLiter = 0;

        for (int i = 0; i < oryginalnaTablica.length; i++) {
            podzielonaTablica[x][y] = oryginalnaTablica[i];
            y++;

            if (y == 4) {
                x++;
                y = 0;
            }
            iloscLiter = i + 1; //+1 bo liczy od 0, a potrzebujemy ilosci liter
        }

        podzielonaTablica[liczbaKawalkow][0] = (byte) ((liczbaKawalkow * 4) - iloscLiter);

        return podzielonaTablica;
    }

    public static byte[] SubBytes(byte[] TablicaBajtow){

        byte[] przetlumaczonaTablica = new byte[TablicaBajtow.length];

        for(int i = 0; i < TablicaBajtow.length; i++){
            przetlumaczonaTablica[i] = RijndaelSBox.ZnajdzWSBoxie(TablicaBajtow[i]); //dla kazdego bajta znajdujemy odpowiedni zamiennik w sboxie
        }

        return przetlumaczonaTablica;
    }

}
