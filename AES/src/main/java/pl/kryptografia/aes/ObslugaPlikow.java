package pl.kryptografia.aes;
import java.io.*;
import java.util.Arrays;

public class ObslugaPlikow {

    public static byte[] pobierzPlikIZamienNaTabliceBajtow(String nazwaPliku) throws IOException {
        File file = new File(nazwaPliku);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytesArray = new byte[(int) file.length()];
        fis.read(bytesArray);
        fis.close();
        return bytesArray;
    }

    public static void zapiszDoPliku(byte[][][] tablica, String nazwaPliku){
        byte[] bajty;
        bajty = AES.zamianaNaPojedynczaTablice(tablica);
        try {
            FileOutputStream fos = new FileOutputStream(nazwaPliku);
            fos.write(bajty);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
