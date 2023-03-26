package pl.kryptografia.aes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class WindowController {

    @FXML
    private TextArea tekstJawny;

    @FXML
    private TextField kluczPoleTekstowe;

    @FXML
    private TextArea tekstWyjsciowy;

    public byte[][][] tablicaDzielona;

    @FXML
    protected void onSzyfrujButtonClick() {

        //--- przygotowanie podstawowe
        byte[] klucz = kluczPoleTekstowe.getText().getBytes();
        byte[][] tablicaKluczy =  AES.kluczRunda(klucz, 10);
        byte[][][] kluczePodzieloneNaTrzy = AES.podzielNaTrzy(tablicaKluczy);

        byte[][] podzielone = AES.podzielTablice(tekstJawny.getText().getBytes(StandardCharsets.UTF_8));
        byte[][][] tablicaWynikowa = AES.podzielNaTrzy(podzielone);

        AES.dodajKluczRundy(tablicaWynikowa, kluczePodzieloneNaTrzy[0]);

        //9 rund
        for(int runda = 0; runda < 9; runda++){
            AES.SubBytes(tablicaWynikowa);
            AES.shiftRows(tablicaWynikowa);
            AES.mixColumns(tablicaWynikowa);
            AES.dodajKluczRundy(tablicaWynikowa, kluczePodzieloneNaTrzy[runda + 1]);
        }

        //10 runda
        AES.SubBytes(tablicaWynikowa);
        AES.shiftRows(tablicaWynikowa);
        AES.dodajKluczRundy(tablicaWynikowa, kluczePodzieloneNaTrzy[10]);


        //wklejenie wyniku do okienka
        String hexString = DatatypeConverter.printHexBinary(AES.zamianaNaPojedynczaTablice(tablicaWynikowa));
        tekstWyjsciowy.setText(hexString);

        tablicaDzielona = tablicaWynikowa;
    }

    @FXML
    protected void onDeszyfrujButtonClick() {
        //przygotowanie kluczy
        byte[] klucz = kluczPoleTekstowe.getText().getBytes();
        byte[][] tablicaKluczy =  AES.kluczRunda(klucz, 10);
        byte[][][] kluczePodzieloneNaTrzy = AES.podzielNaTrzy(tablicaKluczy);

        byte[][][] tablicaWynikowa = tablicaDzielona;

        //odwrotnosc 10 rundy
        AES.dodajKluczRundy(tablicaWynikowa, kluczePodzieloneNaTrzy[10]);

    }
}