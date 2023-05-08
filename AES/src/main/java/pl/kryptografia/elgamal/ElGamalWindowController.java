package pl.kryptografia.elgamal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;


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
    private TextArea nazwaPliku1;

    @FXML
    private TextArea tekstDoDeszyfrowania;

    @FXML
    private TextArea tekstJawny;

    @FXML
    private TextField wartoscP;

    private BigInteger p;
    private BigInteger g;
    private BigInteger A;
    private BigInteger h;
    private byte[] plik;

    private BigInteger[] podpis;

    private Boolean czyPodpisano = false;
    private Boolean czyWczytanoPlikWiadomosci = false;
    private Boolean czyPodpisanoPlik = false;
    private Boolean czyWczytanoPlikPodpisu = false;

    @FXML
    protected void onGenerujButtonClick() {
        p = ElGamal.generateP();
        wartoscP.setText(p.toString());

        g = ElGamal.findGInRange(p);
        kluczPublicznyg.setText(g.toString());

        A = ElGamal.findAInRange(p);
        kluczPrywatnya.setText(A.toString());

        h = ElGamal.calculateH(g, A, p);
        kluczPublicznyh.setText(h.toString());
    }

    @FXML
    protected void onPodpiszButtonClick() {
        if (!czyWygenerowanoKlucze()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nie wygenerowano kluczy!");
            alert.showAndWait();
        }
        else{
            BigInteger[] pom=ElGamal.podpis(tekstJawny.getText().getBytes(), A);
            String tmp = pom[0].toString();
            String tmp2 = pom[1].toString();
            tekstDoDeszyfrowania.setText(tmp+"\n"+tmp2);
            czyPodpisano = true;
        }
    }

    @FXML
    protected void onDeszyfrujButtonClick() {
        if(!czyPodpisano){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nie podpisano wiadomości!");
            alert.showAndWait();
        }
        else {
            Boolean czyZgodny = ElGamal.weryfikacja(tekstJawny.getText().getBytes());
            if(czyZgodny){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Podpis jest prawidłowy");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Podpis nie jest prawidłowy");
                alert.showAndWait();
            }
        }
    }

    @FXML
    protected void onWczytajPodpisZPliku() throws IOException {
        if(nazwaPliku1.getText() == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie podano nazwy pliku z podpisem");
            alert.showAndWait();
        } else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Wczytano plik z podpisem");
            alert.showAndWait();
            podpis = ElGamal.wczytajPodpisZPliku(nazwaPliku1.getText());
            czyWczytanoPlikPodpisu = true;
        }
    }

    @FXML
    protected void onWczytajPlikButtonClick() throws IOException {
        if(nazwaPliku.getText() == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie podano nazwy pliku z wiadomością");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Wczytano plik");
            alert.showAndWait();
            plik = ElGamal.pobierzPlikIZamienNaTabliceBajtow(nazwaPliku.getText());
            czyWczytanoPlikWiadomosci = true;
        }

    }

    @FXML
    protected void onPodpiszPlikButtonClick() throws FileNotFoundException {
        if(!czyWczytanoPlikWiadomosci || !czyWygenerowanoKlucze()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie wczytano pliku z wiadomością lub nie podano kluczy");
            alert.showAndWait();
        } else {
            if(nazwaPliku1.getText()==""){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Nie podano nazwy pliku z podpisem");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Zapisano plik z podpisem");
                alert.showAndWait();
                BigInteger[] tmp = ElGamal.podpis(plik, A);
                String zapis = nazwaPliku1.getText();
                ElGamal.zapiszPodpisDoPliku(tmp, zapis);
            }
        }
    }

    @FXML
    protected void onWeryfikujPlikButtonClick(){
        if(!czyWczytanoPlikWiadomosci || !czyWczytanoPlikPodpisu || !czyWygenerowanoKlucze()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Nie wczytano pliku z wiadomością lub podpisem lub nie podano kluczy");
            alert.showAndWait();
        } else {
            ElGamal.setA(new BigInteger(kluczPrywatnya.getText()));
            ElGamal.setG(new BigInteger(kluczPublicznyg.getText()));
            ElGamal.setH(new BigInteger(kluczPublicznyh.getText()));
            ElGamal.setP(new BigInteger(wartoscP.getText()));

            boolean weryfikacjaPliku = ElGamal.weryfikacja(plik);
            if(weryfikacjaPliku){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Podpis jest prawidłowy");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Podpis nie jest prawidłowy");
                alert.showAndWait();
            }
        }
    }

    private boolean czyWygenerowanoKlucze(){
        if(kluczPrywatnya.getText() == "" || kluczPublicznyg.getText() == "" || kluczPublicznyh.getText() == "" || wartoscP.getText() == ""){
            return false;
        }
        return true;
    };

}
