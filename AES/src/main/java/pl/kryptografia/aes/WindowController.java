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
    private TextArea tekstWyjsciowySzyfrowanie;

    @FXML
    private TextArea tekstWyjsciowyDeszyfrowanie;

    @FXML
    private TextArea tekstDoDeszyfrowania;

    @FXML
    private TextArea nazwaPliku;

    public byte[][][] tablicaDzielona;
    byte[] plikTablicaBajtow;

    @FXML
    protected void onSzyfrujButtonClick() {
        szyfruj(false, tekstJawny.getText().getBytes(StandardCharsets.UTF_8));
    }

    @FXML
    protected void onDeszyfrujButtonClick() {
        deszyfruj(false, tablicaDzielona);
    }

    @FXML
    protected void onWczytajPlikButtonClick() {
        plikTablicaBajtow = ObslugaPlikow.pobierzPlikIZamienNaTabliceBajtow(nazwaPliku.getText());
    }

    @FXML
    protected void onSzyfrujPlikButtonClick() {
        szyfruj(true, plikTablicaBajtow);
        System.out.println(Arrays.toString(plikTablicaBajtow));
        System.out.println(Arrays.deepToString(tablicaDzielona));
        String plikDoSzyfrowania = nazwaPliku.getText() + "Zaszyfrowany";
        ObslugaPlikow.zapiszDoPliku(tablicaDzielona, plikDoSzyfrowania, true);
    }

    @FXML
    protected void onDeszyfrujPlikButtonClick() {
        deszyfruj(true, tablicaDzielona);
        String plikDoSzyfrowania = nazwaPliku.getText() + "Odszyfrowany";
        ObslugaPlikow.zapiszDoPliku(tablicaDzielona, plikDoSzyfrowania, false);
        System.out.println(Arrays.toString(plikTablicaBajtow));
    }

    public void szyfruj(boolean czyPlik, byte[] przekazanaTablica){
        //--- przygotowanie podstawowe
        byte[] klucz = kluczPoleTekstowe.getText().getBytes();
        byte[][] tablicaKluczy =  AES.kluczRunda(klucz, 10);
        byte[][][] kluczePodzieloneNaTrzy = AES.podzielNaTrzy(tablicaKluczy);

        byte[][] podzielone = AES.podzielTablice(przekazanaTablica);
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

        if(!czyPlik){
            //wklejenie wyniku do okienka
            String hexString = DatatypeConverter.printHexBinary(AES.zamianaNaPojedynczaTablice(tablicaWynikowa));
            tekstWyjsciowySzyfrowanie.setText(hexString);
            tekstDoDeszyfrowania.setText(hexString);
        }

        tablicaDzielona = tablicaWynikowa;
    }

    public void deszyfruj(boolean czyPlik, byte[][][] przekazanaTablica){
        //przygotowanie kluczy
        byte[] klucz = kluczPoleTekstowe.getText().getBytes();
        byte[][] tablicaKluczy =  AES.kluczRunda(klucz, 10);
        byte[][][] kluczePodzieloneNaTrzy = AES.podzielNaTrzy(tablicaKluczy);

        //odwrotnosc 10 rundy
        AES.dodajKluczRundy(przekazanaTablica, kluczePodzieloneNaTrzy[10]);
        AES.invertShiftRows(przekazanaTablica);
        AES.InvertSubBytes(przekazanaTablica);

        //odwrotnosc 9 rund
        for(int runda = 9; runda > 0; runda--){
            AES.dodajKluczRundy(przekazanaTablica, kluczePodzieloneNaTrzy[runda]);
            AES.invertMixColumns(przekazanaTablica);
            AES.invertShiftRows(przekazanaTablica);
            AES.InvertSubBytes(przekazanaTablica);
        }

        //odwrotnosc przygotowania
        AES.dodajKluczRundy(przekazanaTablica, kluczePodzieloneNaTrzy[0]);
        byte[] zamieniona = AES.zamianaNaPojedynczaTablice(przekazanaTablica);

        //usuwanie zbednych pozycji
        byte[] ucieta = new byte[zamieniona.length - (16 + zamieniona[zamieniona.length-16])]; //zamieniona[zamieniona.length-16] to wartosc ile do usuniecia
        System.arraycopy(zamieniona, 0, ucieta, 0, ucieta.length);

        if(!czyPlik){
            tekstWyjsciowyDeszyfrowanie.setText(new String(ucieta, StandardCharsets.UTF_8));
        }
        if(czyPlik){
            plikTablicaBajtow = ucieta;
        }

    }
}