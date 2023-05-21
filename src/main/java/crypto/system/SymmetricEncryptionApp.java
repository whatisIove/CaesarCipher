package crypto.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SymmetricEncryptionApp extends Application {
    private SymmetricEncryptionSystem encryptionSystem;
    private TextArea inputTextArea;
    private TextField keyTextField;
    private ComboBox<KeyType> keyTypeComboBox;
    private TextArea consoleTextArea;
    private TextArea gammaTextArea;

    @Override
    public void start(Stage primaryStage) {
        encryptionSystem = new SymmetricEncryptionSystem();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem createMenuItem = new MenuItem("Create");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem printMenuItem = new MenuItem("Print");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(createMenuItem, openMenuItem, saveMenuItem, printMenuItem, new SeparatorMenuItem(), exitMenuItem);

        createMenuItem.setOnAction(event -> {
            consoleTextArea.clear();
            inputTextArea.clear();
            keyTextField.clear();
            gammaTextArea.clear();
        });

        openMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Text File");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    String content = FileUtils.readFile(file);
                    consoleTextArea.appendText("Opened file: " + file.getName() + "\n");
                    inputTextArea.setText(content);
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error reading file: " + file.getName());
                }
            }
        });

        saveMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Text File");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    String content = inputTextArea.getText();
                    FileUtils.writeFile(file, content);
                    consoleTextArea.appendText("Saved file: " + file.getName() + "\n");
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error saving file: " + file.getName());
                }
            }
        });

        printMenuItem.setOnAction(e -> {
            System.out.println("Selected menu: Print");
            if (Desktop.isDesktopSupported()) {
                try {
                    File tempFile = File.createTempFile("temp", ".txt");
                    saveFile(tempFile, consoleTextArea.getText());
                    Desktop.getDesktop().print(tempFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitMenuItem.setOnAction(event -> {
            primaryStage.close();
        });

        // About Menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenu.getItems().add(aboutMenuItem);

        aboutMenuItem.setOnAction(event -> {
            showAlert(Alert.AlertType.INFORMATION, "About", "Developer of this project: Butok " +
                    "Vladislav 122m-22-3" + "\n" + "Email: butok.v.o@nmu.one");
        });

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, aboutMenu);

        // Input Area
        Label inputLabel = new Label("Input:");
        inputTextArea = new TextArea();
        inputTextArea.setPrefHeight(200);

        // Key Area
        Label keyLabel = new Label("Key:");
        keyTextField = new TextField();

        // Key Type
        Label keyTypeLabel = new Label("Key Type:");
        keyTypeComboBox = new ComboBox<>();
        keyTypeComboBox.getItems().addAll(KeyType.DIMENSION_2, KeyType.DIMENSION_3, KeyType.NUMERIC, KeyType.TEXT);
        keyTypeComboBox.setValue(KeyType.TEXT);

        // Gamma Area
        Label gammaLabel = new Label("Gamma:");
        gammaTextArea = new TextArea();
        gammaTextArea.setPrefHeight(100);
        gammaTextArea.setEditable(false);

        // Encrypt Button
        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction(event -> {
            String inputText = inputTextArea.getText();
            String key = keyTextField.getText();
            KeyType keyType = keyTypeComboBox.getValue();

            if (inputText.isEmpty() || key.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Input and key cannot be empty!");
                return;
            }

            try {
                String gamma;
                if (keyType == KeyType.DIMENSION_2) {
                    String[] keyParts2D = key.split(",");
                    if (keyParts2D.length != 2) {
                        throw new IllegalArgumentException("Invalid key format for Dimension 2." + "\n" + "Expected format: coefficient1, coefficient2");
                    }
                    int coefficient1_2D = Integer.parseInt(keyParts2D[0]);
                    int coefficient2_2D = Integer.parseInt(keyParts2D[1]);
                    gamma = encryptionSystem.generateGamma(key, keyType, inputText);
                } else if (keyType == KeyType.DIMENSION_3) {
                    String[] keyParts3D = key.split(",");
                    if (keyParts3D.length != 3) {
                        throw new IllegalArgumentException("Invalid key format for Dimension 3." + "\n" + "Expected format: coefficient1, coefficient2, coefficient3");
                    }
                    int coefficient1_3D = Integer.parseInt(keyParts3D[0]);
                    int coefficient2_3D = Integer.parseInt(keyParts3D[1]);
                    int coefficient3_3D = Integer.parseInt(keyParts3D[2]);
                    gamma = encryptionSystem.generateGamma(key, keyType, inputText);
                } else if (keyType == KeyType.NUMERIC) {
                    try {
                        Integer.parseInt(key);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid key format. Numeric key should be an integer.");
                    }
                    gamma = encryptionSystem.generateGamma(key, keyType, inputText);
                } else {
                    gamma = encryptionSystem.generateGamma(key, keyType, inputText);
                }

                String encryptedText = encryptionSystem.encrypt(inputText, gamma);

                consoleTextArea.appendText("Encrypted:\n" + encryptedText + "\n");

                gammaTextArea.setText(gamma);
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });

// Decrypt Button
        Button decryptButton = new Button("Decrypt");
        decryptButton.setOnAction(event -> {
            String inputText = inputTextArea.getText();
            String key = keyTextField.getText();
            String gamma = gammaTextArea.getText();

            if (inputText.isEmpty() || key.isEmpty() || gamma.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Input, key, and gamma cannot be empty!");
                return;
            }

            try {
                String decryptedText = encryptionSystem.decrypt(inputText, gamma);

                consoleTextArea.appendText("Decrypted:\n" + decryptedText + "\n");
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });


        // Console Area
        Label consoleLabel = new Label("Console:");
        consoleTextArea = new TextArea();
        consoleTextArea.setPrefHeight(100);
        consoleTextArea.setEditable(false);

        // Layout
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.getChildren().addAll(menuBar, inputLabel, inputTextArea, keyLabel, keyTextField, keyTypeLabel,
                keyTypeComboBox, gammaLabel, gammaTextArea, encryptButton, decryptButton,
                consoleLabel, consoleTextArea);

        // Scene
        Scene scene = new Scene(root, 400, 600);

        // Stage
        primaryStage.setTitle("Cryptosystem based on the Gamble cipher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


enum KeyType {
    DIMENSION_2,
    DIMENSION_3,
    NUMERIC,
    TEXT
}

class SymmetricEncryptionSystem {


    public String generateGamma(String key, KeyType keyType, String inputText) {
        int gammaLength = inputText.length() + 1;

        switch (keyType) {
            case DIMENSION_2 -> {
                String[] keyParts2D = key.split(",");
                if (keyParts2D.length != 2) {
                    throw new IllegalArgumentException("Invalid key format for Dimension 2. " +
                            "Expected format: coefficient1,coefficient2");
                }

                int coefficient1_2D;
                int coefficient2_2D;
                try {
                    coefficient1_2D = Integer.parseInt(keyParts2D[0]);
                    coefficient2_2D = Integer.parseInt(keyParts2D[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid key format. " +
                            "Coefficients for Dimension 2 should be integers.");
                }

                Random random2D = new Random(coefficient1_2D + coefficient2_2D);
                StringBuilder gammaBuilder = new StringBuilder(gammaLength);
                for (int i = 0; i < gammaLength; i++) {
                    int randomNumber = random2D.nextInt(26) + 65;
                    char randomChar = (char) randomNumber;
                    gammaBuilder.append(randomChar);
                }
                return gammaBuilder.toString();
            }
            case DIMENSION_3 -> {
                String[] keyParts3D = key.split(",");
                if (keyParts3D.length != 3) {
                    throw new IllegalArgumentException("Invalid key format for Dimension 3. " +
                            "Expected format: coefficient1,coefficient2,coefficient3");
                }

                int coefficient1_3D;
                int coefficient2_3D;
                int coefficient3_3D;
                try {
                    coefficient1_3D = Integer.parseInt(keyParts3D[0]);
                    coefficient2_3D = Integer.parseInt(keyParts3D[1]);
                    coefficient3_3D = Integer.parseInt(keyParts3D[2]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid key format. Coefficients for Dimension 3 " +
                            "should be integers.");
                }

                Random random3D = new Random(coefficient1_3D + coefficient2_3D + coefficient3_3D);
                StringBuilder gammaBuilder = new StringBuilder(gammaLength);
                for (int i = 0; i < gammaLength; i++) {
                    int randomNumber = random3D.nextInt(26) + 65;
                    char randomChar = (char) randomNumber;
                    gammaBuilder.append(randomChar);
                }
                return gammaBuilder.toString();
            }
            case NUMERIC -> {
                try {
                    Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid key format. Numeric key should be an integer.");
                }

                Random randomNumeric = new Random(key.hashCode());
                StringBuilder gammaBuilder = new StringBuilder(gammaLength);
                for (int i = 0; i < gammaLength; i++) {
                    int randomNumber = randomNumeric.nextInt(10);
                    gammaBuilder.append(randomNumber);
                }
                return gammaBuilder.toString();
            }
            case TEXT -> {
                Random randomText = new Random(key.hashCode());
                StringBuilder gammaBuilder = new StringBuilder(gammaLength);
                for (int i = 0; i < gammaLength; i++) {
                    int randomNumber = randomText.nextInt(26) + 65;
                    char randomChar = (char) randomNumber;
                    gammaBuilder.append(randomChar);
                }
                return gammaBuilder.toString();
            }
            default -> throw new IllegalArgumentException("Invalid key type");
        }
    }

    public String encrypt(String inputText, String gamma) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < inputText.length(); i++) {
            char inputChar = inputText.charAt(i);
            char gammaChar = gamma.charAt(i % gamma.length());
            char encryptedChar = (char) (inputChar ^ gammaChar);
            encryptedText.append(encryptedChar);
        }
        return encryptedText.toString();
    }

    public String decrypt(String encryptedText, String gamma) {
        return encrypt(encryptedText, gamma);
    }

}

class FileUtils {
    public static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        java.util.Scanner scanner = new java.util.Scanner(file);
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine());
            content.append("\n");
        }
        scanner.close();
        return content.toString();
    }

    public static void writeFile(File file, String content) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}