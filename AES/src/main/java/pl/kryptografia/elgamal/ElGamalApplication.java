package pl.kryptografia.elgamal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElGamalApplication extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElGamalApplication.class.getResource("elgamalWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ElGamalSign");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
