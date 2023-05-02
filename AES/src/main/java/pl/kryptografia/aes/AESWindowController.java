package pl.kryptografia.aes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class AESWindowController {

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

    private boolean czyWczytanoPlik=false;

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
    protected void onWczytajPlikButtonClick() throws IOException {
        if(nazwaPliku.getText() == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie podano nazwy pliku");
            alert.showAndWait();
        } else {
            plikTablicaBajtow = ObslugaPlikow.pobierzPlikIZamienNaTabliceBajtow(nazwaPliku.getText());
            czyWczytanoPlik = true;
        }
    }

    @FXML
    protected void onSzyfrujPlikButtonClick() {
        if (!czyWczytanoPlik){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie wczytano pliku!");
            alert.showAndWait();
        } else {
            szyfruj(true, plikTablicaBajtow);
            String plikDoSzyfrowania = nazwaPliku.getText() + "Zaszyfrowany";
            ObslugaPlikow.zapiszDoPliku(tablicaDzielona, plikDoSzyfrowania);
        }
    }

    @FXML
    protected void onDeszyfrujPlikButtonClick() {
        if (!czyWczytanoPlik){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie wczytano pliku!");
            alert.showAndWait();
        } else if (nazwaPliku.getText().contains("Zaszyfrowany") == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nie wczytano zaszyfrowanego pliku");
            alert.showAndWait();
        } else {
            deszyfruj(true, AES.podzielNaTrzy(AES.podzielTablice(plikTablicaBajtow)));
            String plikDoSzyfrowania = nazwaPliku.getText() + "Odszyfrowany";
            ObslugaPlikow.zapiszDoPliku(AES.podzielNaTrzy(AES.podzielTablice(plikTablicaBajtow)), plikDoSzyfrowania);
        }
    }

    @FXML
    public void onGenerujKluczButtonClick() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<16;i++){
            int pom = random.nextInt(126 - 33) + 33;
            stringBuilder.append((char)pom);
        }
        kluczPoleTekstowe.setText(stringBuilder.toString());
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

        //usuwanie zbednych pozycji w przypadku tekstu okienkowego
        if(!czyPlik){
            byte[] ucieta = new byte[zamieniona.length - (16 + zamieniona[zamieniona.length-16])]; //zamieniona[zamieniona.length-16] to wartosc ile do usuniecia
            System.arraycopy(zamieniona, 0, ucieta, 0, ucieta.length);
            tekstWyjsciowyDeszyfrowanie.setText(new String(ucieta, StandardCharsets.UTF_8));
        }

        //brak dzialania w przypadku pliku
        if(czyPlik){
            plikTablicaBajtow = zamieniona;
        }

    }
}