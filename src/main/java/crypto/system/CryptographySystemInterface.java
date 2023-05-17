package crypto.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class CryptographySystemInterface extends Application {
    private Locale selectedLocale;
    private TextArea consoleTextArea;
    private File currentFile;
    private final int key = 3;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cryptography System with Caesar Cipher");

        // Create menu
        Menu fileMenu = new Menu("File");
        MenuItem createItem = new MenuItem("Create");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem printItem = new MenuItem("Print");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(createItem, openItem, saveItem, printItem, new SeparatorMenuItem(), exitItem);

        Menu encryptMenu = new Menu("Encryption and Decryption");
        MenuItem encryptUkrainianItem = new MenuItem("Ukrainian Language");
        MenuItem encryptEnglishItem = new MenuItem("English Language");
        encryptMenu.getItems().addAll(encryptUkrainianItem, encryptEnglishItem);

        Menu aboutMenu = new Menu("About");
        MenuItem developerItem = new MenuItem("Developer");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, encryptMenu, aboutMenu);
        aboutMenu.getItems().add(developerItem);

        // Create buttons
        Button encryptButton = new Button("Encrypt");
        Button decryptButton = new Button("Decrypt");
        Button exitButton = new Button("Exit");

        // Create console text area
        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);

        // Create input text fields
        TextField encryptInputField = new TextField();
        TextField decryptInputField = new TextField();

        // Event handlers
        createItem.setOnAction(e -> {
            consoleTextArea.clear();
            currentFile = null;
            System.out.println("Selected menu: Create");
        });

        openItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                currentFile = selectedFile;
                displayFileContent(selectedFile);
            }
        });

        saveItem.setOnAction(e -> {
            if (currentFile != null) {
                saveFile(currentFile, consoleTextArea.getText());
                System.out.println("File saved: " + currentFile.getAbsolutePath());
            } else {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                File selectedFile = fileChooser.showSaveDialog(primaryStage);
                if (selectedFile != null) {
                    currentFile = selectedFile;
                    saveFile(selectedFile, consoleTextArea.getText());
                    System.out.println("File saved: " + selectedFile.getAbsolutePath());
                }
            }
        });

        printItem.setOnAction(e -> {
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

        exitItem.setOnAction(e -> {
            System.out.println("Selected menu: Exit");
            primaryStage.close();
        });

        encryptUkrainianItem.setOnAction(e -> {
            System.out.println("Selected menu: Ukrainian Language");
            selectedLocale = new Locale("uk", "UA");
            encryptButton.setText("Зашифрувати");
            decryptButton.setText("Розшифрувати");
            encryptInputField.setPromptText("Введіть український текст");
            decryptInputField.setPromptText("Введіть український текст");
        });

        encryptEnglishItem.setOnAction(e -> {
            System.out.println("Selected menu: English Language");
            selectedLocale = Locale.ENGLISH;
            encryptButton.setText("Encrypt");
            decryptButton.setText("Decrypt");
            encryptInputField.setPromptText("Enter English text");
            decryptInputField.setPromptText("Enter English text");
        });

        encryptButton.setOnAction(e -> {
            System.out.println("Encrypting...");
            String inputText = encryptInputField.getText();
            if (!isEnglishText(inputText)) {
                showAlert("Invalid Input", "Only English letters are allowed.");
                return;
            }
            String encryptedText = encryptMessage(inputText);
            consoleTextArea.setText(encryptedText);
            System.out.println("Encrypted message: " + encryptedText);
        });

        decryptButton.setOnAction(e -> {
            System.out.println("Decrypting...");
            String inputText = decryptInputField.getText();
            if (!isEnglishText(inputText)) {
                showAlert("Invalid Input", "Only English letters are allowed.");
                return;
            }
            String decryptedText = decryptMessage(inputText);
            consoleTextArea.setText(decryptedText);
            System.out.println("Decrypted message: " + decryptedText);
        });

        encryptButton.setOnAction(e -> {
            System.out.println("Encrypting...");
            String inputText = encryptInputField.getText();
            if (selectedLocale.equals(Locale.ENGLISH) && !isEnglishText(inputText)) {
                showAlert("Invalid Input", "Only English letters are allowed.");
                return;
            } else if (selectedLocale.equals(new Locale("uk", "UA")) && !isUkrainianText(inputText)) {
                showAlert("Invalid Input", "Only Ukrainian letters are allowed.");
                return;
            }
            String encryptedText = encryptMessage(inputText);
            consoleTextArea.appendText("Encrypted message: " +  encryptedText + "\n");
            System.out.println("Encrypted message: " + encryptedText);
        });

        decryptButton.setOnAction(e -> {
            System.out.println("Decrypting...");
            String inputText = decryptInputField.getText();
            if (selectedLocale.equals(Locale.ENGLISH) && !isEnglishText(inputText)) {
                showAlert("Invalid Input", "Only English letters are allowed.");
                return;
            } else if (selectedLocale.equals(new Locale("uk", "UA")) && !isUkrainianText(inputText)) {
                showAlert("Invalid Input", "Only Ukrainian letters are allowed.");
                return;
            }
            String decryptedText = decryptMessage(inputText);
            consoleTextArea.appendText("Decrypted message: " + decryptedText + "\n");
            System.out.println("Decrypted message: " + decryptedText);
        });

        exitButton.setOnAction(e -> {
            System.out.println("Exit button clicked");
            primaryStage.close();
        });

        developerItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Developer");
            alert.setHeaderText("Cryptography System with Caesar Cipher");
            alert.setContentText("Developer: Butok Vladislav 122m-22-3\nEmail: butok.v.o@nmu.one");
            alert.showAndWait();
        });

        // Create layout
        VBox layout = new VBox();
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.getChildren().addAll(menuBar, consoleTextArea, encryptInputField, encryptButton, decryptInputField, decryptButton, exitButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String encryptMessage(String message) {
        StringBuilder encryptedMessage = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);

            if (Character.isLetter(ch)) {
                char encryptedChar;
                if (selectedLocale.equals(Locale.ENGLISH)) {
                    encryptedChar = encryptEnglishLetter(ch);
                } else if (selectedLocale.equals(new Locale("uk", "UA"))) {
                    encryptedChar = encryptUkrainianLetter(ch);
                } else {
                    encryptedChar = ch;
                }
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(ch);
            }
        }

        return encryptedMessage.toString();
    }

    private String decryptMessage(String encryptedMessage) {
        StringBuilder decryptedMessage = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char ch = encryptedMessage.charAt(i);

            if (Character.isLetter(ch)) {
                char decryptedChar;
                if (selectedLocale.equals(Locale.ENGLISH)) {
                    decryptedChar = decryptEnglishLetter(ch);
                } else if (selectedLocale.equals(new Locale("uk", "UA"))) {
                    decryptedChar = decryptUkrainianLetter(ch);
                } else {
                    decryptedChar = ch;
                }
                decryptedMessage.append(decryptedChar);
            } else {
                decryptedMessage.append(ch);
            }
        }

        return decryptedMessage.toString();
    }

    private boolean isEnglishText(String text) {
        if (selectedLocale.equals(Locale.ENGLISH)) {
            String pattern = "^[A-Za-z\\s]+$";
            return text.matches(pattern);
        }
        return false;
    }

    private boolean isUkrainianText(String text) {
        if (selectedLocale.equals(new Locale("uk", "UA"))) {
            String pattern = "^[А-ЩЬЮЯҐЄІЇа-щьюяґєії\\s]+$";
            return text.matches(pattern);
        }
        return false;
    }

    private char encryptEnglishLetter(char letter) {
        char shiftedLetter = (char) (letter + key);
        if (shiftedLetter > 'z') {
            shiftedLetter = (char) ('a' + (shiftedLetter - 'z' - 1));
        }
        return shiftedLetter;
    }

    private char decryptEnglishLetter(char letter) {
        char shiftedLetter = (char) (letter - key);
        if (shiftedLetter < 'a') {
            shiftedLetter = (char) ('z' - ('a' - shiftedLetter - 1));
        }
        return shiftedLetter;
    }

    private char encryptUkrainianLetter(char letter) {
        char shiftedLetter = (char) (letter + key);
        if (shiftedLetter > 'Я') {
            shiftedLetter = (char) ('А' + (shiftedLetter - 'Я' - 1));
        }
        return shiftedLetter;
    }

    private char decryptUkrainianLetter(char letter) {
        char shiftedLetter = (char) (letter - key);
        if (shiftedLetter < 'А') {
            shiftedLetter = (char) ('Я' - ('А' - shiftedLetter - 1));
        }
        return shiftedLetter;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void displayFileContent(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            consoleTextArea.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}