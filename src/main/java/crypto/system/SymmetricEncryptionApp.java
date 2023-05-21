package crypto.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class SymmetricEncryptionApp extends Application {
    private SymmetricEncryptionSystem encryptionSystem;
    private TextArea inputTextArea;
    private TextField keyTextField;
    private ComboBox<KeyType> keyTypeComboBox;
    private TextArea consoleTextArea;

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
            showAlert(Alert.AlertType.INFORMATION, "About",  "Developer of this project: Butok " +
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

        // Key Type Area
        Label keyTypeLabel = new Label("Key Type:");
        keyTypeComboBox = new ComboBox<>();
        keyTypeComboBox.getItems().addAll(KeyType.values());
        keyTypeComboBox.getSelectionModel().selectFirst();

        // Encrypt Button
        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction(event -> {
            String input = inputTextArea.getText();
            String key = keyTextField.getText();
            KeyType keyType = keyTypeComboBox.getValue();

            try {
                String encryptedText = encryptionSystem.encrypt(input, key, keyType);
                consoleTextArea.appendText("Encryption successful\n");
                consoleTextArea.appendText("Encrypted text: " + encryptedText + "\n");
            } catch (Exception e) {
                consoleTextArea.appendText("Encryption failed: " + e.getMessage() + "\n");
            }
        });

        // Decrypt Button
        Button decryptButton = new Button("Decrypt");
        decryptButton.setOnAction(event -> {
            String input = inputTextArea.getText();
            String key = keyTextField.getText();
            KeyType keyType = keyTypeComboBox.getValue();

            try {
                String decryptedText = encryptionSystem.decrypt(input, key, keyType);
                consoleTextArea.appendText("Decryption successful\n");
                consoleTextArea.appendText("Decrypted text: " + decryptedText + "\n");
            } catch (Exception e) {
                consoleTextArea.appendText("Decryption failed: " + e.getMessage() + "\n");
            }
        });

        // Console Area
        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);

        // Layout
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(menuBar, inputLabel, inputTextArea, keyLabel, keyTextField, keyTypeLabel, keyTypeComboBox,
                encryptButton, decryptButton, consoleTextArea);
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Сryptosystem based on the Trithemius cipher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
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
    TEXT
}

class SymmetricEncryptionSystem {
    public boolean validateKey(String key, KeyType keyType) {
        return switch (keyType) {
            case DIMENSION_2 -> validateDimension2Key(key);
            case DIMENSION_3 -> validateDimension3Key(key);
            case TEXT -> validateTextKey(key);
        };

    }

    private boolean validateDimension2Key(String key) {
        // Перевірка валідності 2-вимірного вектору
        return key.matches("[0-9]+,[0-9]+");
    }

    private boolean validateDimension3Key(String key) {
        // Перевірка валідності 3-вимірного вектору
        return key.matches("[0-9]+,[0-9]+,[0-9]+");
    }

    private boolean validateTextKey(String key) {
        // Перевірка валідності текстового ключа
        return !key.isEmpty();
    }

    public String encrypt(String input, String key, KeyType keyType) {
        return switch (keyType) {
            case DIMENSION_2 -> encryptWithDimension2Key(input, key);
            case DIMENSION_3 -> encryptWithDimension3Key(input, key);
            case TEXT -> encryptWithTextKey(input, key);
        };

    }

    private String encryptWithDimension2Key(String input, String key) {
        String[] keyParts = key.split(",");
        int coefficient1 = Integer.parseInt(keyParts[0]);
        int coefficient2 = Integer.parseInt(keyParts[1]);

        StringBuilder encryptedText = new StringBuilder();
        for (char c : input.toCharArray()) {
            int encryptedChar = c + coefficient1 + coefficient2;
            encryptedText.append((char) encryptedChar);
        }

        return encryptedText.toString();
    }

    private String encryptWithDimension3Key(String input, String key) {
        String[] keyParts = key.split(",");
        int coefficient1 = Integer.parseInt(keyParts[0]);
        int coefficient2 = Integer.parseInt(keyParts[1]);
        int coefficient3 = Integer.parseInt(keyParts[2]);

        StringBuilder encryptedText = new StringBuilder();
        for (char c : input.toCharArray()) {
            int encryptedChar = c + coefficient1 + coefficient2 + coefficient3;
            encryptedText.append((char) encryptedChar);
        }

        return encryptedText.toString();
    }

    private String encryptWithTextKey(String input, String key) {
        StringBuilder encryptedText = new StringBuilder();
        int keyIndex = 0;

        for (char c : input.toCharArray()) {
            int keyChar = key.charAt(keyIndex % key.length());
            int encryptedChar = c + keyChar;
            encryptedText.append((char) encryptedChar);
            keyIndex++;
        }

        return encryptedText.toString();
    }

    public String decrypt(String input, String key, KeyType keyType) {
        return switch (keyType) {
            case DIMENSION_2 -> decryptWithDimension2Key(input, key);
            case DIMENSION_3 -> decryptWithDimension3Key(input, key);
            case TEXT -> decryptWithTextKey(input, key);
        };

    }

    private String decryptWithDimension2Key(String input, String key) {
        String[] keyParts = key.split(",");
        int coefficient1 = Integer.parseInt(keyParts[0]);
        int coefficient2 = Integer.parseInt(keyParts[1]);

        StringBuilder decryptedText = new StringBuilder();
        for (char c : input.toCharArray()) {
            int decryptedChar = c - coefficient1 - coefficient2;
            decryptedText.append((char) decryptedChar);
        }

        return decryptedText.toString();
    }

    private String decryptWithDimension3Key(String input, String key) {
        String[] keyParts = key.split(",");
        int coefficient1 = Integer.parseInt(keyParts[0]);
        int coefficient2 = Integer.parseInt(keyParts[1]);
        int coefficient3 = Integer.parseInt(keyParts[2]);

        StringBuilder decryptedText = new StringBuilder();
        for (char c : input.toCharArray()) {
            int decryptedChar = c - coefficient1 - coefficient2 - coefficient3;
            decryptedText.append((char) decryptedChar);
        }

        return decryptedText.toString();
    }

    private String decryptWithTextKey(String input, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int keyIndex = 0;

        for (char c : input.toCharArray()) {
            int keyChar = key.charAt(keyIndex % key.length());
            int decryptedChar = c - keyChar;
            decryptedText.append((char) decryptedChar);
            keyIndex++;
        }

        return decryptedText.toString();
    }

}
