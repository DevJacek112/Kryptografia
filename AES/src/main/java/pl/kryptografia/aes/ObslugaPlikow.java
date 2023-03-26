package pl.kryptografia.aes;

import java.io.*;

public class ObslugaPlikow {

    public static byte[] pobierzPlikIZamienNaTabliceBajtow(String nazwaPliku){

        String fileName = nazwaPliku;
        File file = new File(fileName);
        byte[] fileContent = new byte[(int)file.length()];

        try {
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(fileContent);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

}
