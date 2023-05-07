package pl.kryptografia.elgamal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;


public class ElGamalWindowController {

    @FXML
    private TextField kluczPrywatnya;

    @FXML
    private TextField kluczPublicznyg;

    @FXML
    private TextField kluczPublicznyh;

    @FXML
    private TextArea nazwaPliku;

    @FXML
    private TextArea tekstDoDeszyfrowania;

    @FXML
    private TextArea tekstJawny;

    @FXML
    private TextArea tekstWyjsciowyDeszyfrowanie;

    @FXML
    private TextArea tekstWyjsciowySzyfrowanie;

    private BigInteger p;
    private BigInteger g;
    private BigInteger A;
    private BigInteger h;
    private byte[] plik;

    private BigInteger APlik;

    @FXML
    protected void onGenerujButtonClick() {
        p = ElGamal.generateP();
        g = ElGamal.findGInRange(p);
        kluczPublicznyg.setText(g.toString());

        A = ElGamal.findAInRange(p);
        kluczPrywatnya.setText(A.toString());

        h = ElGamal.calculateH(g, A, p); //nasze y z czatbota
        kluczPublicznyh.setText(h.toString());
    }

    @FXML
    protected void onPodpiszButtonClick() {
        BigInteger[] pom=ElGamal.podpis(tekstJawny.getText().getBytes(), A);
        String tmp = pom[0].toString();
        String tmp2 = pom[1].toString();
        tekstDoDeszyfrowania.setText(tmp+"\n"+tmp2);
    }

    @FXML
    protected void onDeszyfrujButtonClick() {
        Boolean czyZgodny = ElGamal.weryfikacja(tekstJawny.getText().getBytes());
        if(czyZgodny){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Podpis jest prawidłowy");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Podpis nie jest prawidłowy");
            alert.showAndWait();
        }
    }

    @FXML
    protected void onZapiszKluczeButtonClick(){

    }

    @FXML
    protected void onWczytajKluczeButtonClick(){

    }

    @FXML
    protected void onWczytajPlikButtonClick() throws IOException {
        plik = ElGamal.pobierzPlikIZamienNaTabliceBajtow(nazwaPliku.getText());
    }

    @FXML
    protected void onPodpiszPlikButtonClick(){
        BigInteger[] tmp = ElGamal.podpis(plik, A);
        String zapis = nazwaPliku.getText() + "Podpisany";
        ElGamal.zapiszDoPliku(Arrays.toString(tmp).getBytes(),zapis);
    }

    @FXML
    protected void onWeryfikujPlikButtonClick(){
        boolean weryfikacjaPliku = ElGamal.weryfikacja(plik);
        if(weryfikacjaPliku){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Podpis jest prawidłowy");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Podpis nie jest prawidłowy");
            alert.showAndWait();
        }
    }

}
