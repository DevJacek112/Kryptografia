package pl.kryptografia.elgamal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
        BigInteger[] pom=ElGamal.podpis(tekstJawny.getText(), A);
        String tmp = pom[0].toString();
        String tmp2 = pom[1].toString();
        tekstDoDeszyfrowania.setText(tmp+"\n"+tmp2);
    }

    @FXML
    protected void onDeszyfrujButtonClick() {
        System.out.println("test");
        // System.out.println(ElGamal.byteArrayToString(ElGamal.decrypt(A, p)));
        //tekstWyjsciowySzyfrowanie.setText(ElGamal.decrypt(tekstWyjsciowyDeszyfrowanie.getText(), A, p));
    }


}
