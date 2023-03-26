package pl.kryptografia.aes;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;

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

    public static void zapiszDoPliku(byte[][][] tablica, String nazwaPliku, boolean zaszyfrowane){
        String hexString = "";
        if(zaszyfrowane) {
            hexString = DatatypeConverter.printHexBinary(AES.zamianaNaPojedynczaTablice(tablica));
            try {
                FileWriter fileWriter = new FileWriter(nazwaPliku);
                fileWriter.write(hexString);
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else  {
            String text = new String(AES.zamianaNaPojedynczaTablice(tablica),StandardCharsets.UTF_8);
            try {
                FileWriter fileWriter = new FileWriter(nazwaPliku);
                fileWriter.write(text);
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
