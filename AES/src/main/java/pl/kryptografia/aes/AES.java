package pl.kryptografia.aes;

public class AES {

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

    public static byte[][] SubBytes(byte[][] TablicaBajtow){

        byte[][] przetlumaczonaTablica = new byte[TablicaBajtow.length][16];

        for(int i = 0; i < TablicaBajtow.length; i++){
            for(int j = 0; j < 15; j++){
                przetlumaczonaTablica[i][j] = RijndaelSBox.ZnajdzWSBoxie(TablicaBajtow[i][j]); //dla kazdego bajta znajdujemy odpowiedni zamiennik w sboxie
            }
        }

        return przetlumaczonaTablica;
    }

    public static byte[][] shiftRows(byte[][] tablica){
        for(int i=1;i<4;i++){
            //kopia rzędu
            byte[] pom = new byte[4];
            for(int j=0;j<4;j++){
                pom[j]=tablica[i][j];
            }
            //pierwszy element rzędu
            byte tmp = tablica[i][0];
            //wzór [i][j-i%4]
            for(int j=1;j<4;j++){
                int modulo = j-i;
                while(modulo<0)modulo+=4;
                modulo=modulo%4;
                tablica[i][modulo] = pom[j];
            }
            //wstawienie pierwszego elementu na odpowiednie miejsce
            tablica[i][4-i]=tmp;
        }
        return  tablica;
    }

}
