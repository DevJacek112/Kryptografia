package pl.kryptografia.aes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WindowController {

    @FXML
    private TextArea tekstJawny;

    @FXML
    private TextField kluczPoleTekstowe;

    @FXML
    protected void onSzyfrujButtonClick() {

        //--- przygotowanie tekstu do dzialania na nim
        byte[] klucz = kluczPoleTekstowe.getText().getBytes();
        byte[][] podzielone = AES.podzielTablice(tekstJawny.getText().getBytes(StandardCharsets.UTF_8));
        //System.out.println("Tekst podzielony na bloczki: ");
        //System.out.println(Arrays.deepToString(podzielone)); //podzielony tekst

        byte[][] tablicaKluczy = AES.UzupelnijKlucze(podzielone.length, klucz);
        System.out.println("Tablica kluczy: ");
        System.out.println(Arrays.deepToString(tablicaKluczy)); //przypisane klucze

        byte[][] poRundzieJeden =  AES.kluczRunda(1, tablicaKluczy);
        System.out.println(Arrays.deepToString(poRundzieJeden));

        //--- tu sie zaczyna algorytm do kazdego bloczku
//        System.out.println("");
//        System.out.println("SubBytes: ");
//        byte[][][] zamienionyNaBajty = AES.SubBytes(podzielone);
//        System.out.println(Arrays.deepToString(zamienionyNaBajty));
//
//        System.out.println("MixColumns ");
//        byte[][][] przetasowaneKolumny = AES.shiftRows(zamienionyNaBajty);
//        System.out.println(Arrays.deepToString(przetasowaneKolumny));


    }
}