package crypto.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptographySystemInterface extends Application {
    private CaesarCipher caesarCipher;
    private String selectedLanguage;
    private TextArea consoleTextArea;
    private File currentFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cryptography System with Caesar Cipher");

        caesarCipher = new CaesarCipher(3); // Example: using key 3

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
            if (currentFile != null) {
                System.out.println("Selected file for printing: " + currentFile.getAbsolutePath());
                printFile(currentFile);
            }
        });
        exitItem.setOnAction(e -> primaryStage.close());

        encryptUkrainianItem.setOnAction(e -> {
            selectedLanguage = "Ukrainian";
            System.out.println("Selected menu: Encrypt (Ukrainian)");
        });

        encryptEnglishItem.setOnAction(e -> {
            selectedLanguage = "English";
            System.out.println("Selected menu: Encrypt (English)");
        });

        developerItem.setOnAction(e -> {
            Stage developerStage = new Stage();
            developerStage.setTitle("Developer");
            Label developerLabel = new Label("Developer of this project: \nButok Vladislav 122m-22-3");
            VBox developerLayout = new VBox(developerLabel);
            developerLayout.setPadding(new Insets(10));
            Scene developerScene = new Scene(developerLayout, 200, 100);
            developerStage.setScene(developerScene);
            developerStage.show();
        });

        encryptButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Encrypt Message");
            dialog.setHeaderText("Enter the message to encrypt:");
            dialog.setContentText("Message:");
            String message = dialog.showAndWait().orElse("");
            String encryptedMessage = encryptMessage(message);
            consoleTextArea.appendText("Encrypted message: " + encryptedMessage + "\n");
            System.out.println("Encrypted message: " + encryptedMessage);
        });

        decryptButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Decrypt Message");
            dialog.setHeaderText("Enter the encrypted message to decrypt:");
            dialog.setContentText("Encrypted message:");

            String encryptedMessage = dialog.showAndWait().orElse("");
            String decryptedMessage = decryptMessage(encryptedMessage);
            consoleTextArea.appendText("Decrypted message: " + decryptedMessage + "\n");
            System.out.println("Decrypted message: " + decryptedMessage);
        });

        exitButton.setOnAction(e -> primaryStage.close());

        // Create vertical container
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.getChildren().addAll(menuBar, encryptButton, decryptButton, exitButton, consoleTextArea);

        // Create scene
        Scene scene = new Scene(vbox, 400, 500);

        // Set background image
        Image backgroundImage = new Image("file:///path/to/background_image.jpg");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        vbox.setBackground(new Background(background));

        // Center the window on the screen
        primaryStage.centerOnScreen();

        // Set the scene for the primaryStage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayFileContent(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            consoleTextArea.setText(content.toString());
            System.out.println("File content displayed in the program.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printFile(File file) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.print(file);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file printing errors
            }
        }

    }

    private void saveFile(File file, String text) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encryptMessage(String message) {
        String encryptedMessage = "";

        if (selectedLanguage.equals("Ukrainian")) {
            // Проверка, что введенное сообщение содержит только украинские символы
            if (!isUkrainianText(message)) {
                consoleTextArea.appendText("Invalid input. Only Ukrainian characters are allowed.\n");
                return message;
            }
            // Encrypt Ukrainian text
            encryptedMessage = caesarCipher.encryptUkrainian(message);
        } else if (selectedLanguage.equals("English")) {
            // Проверка, что введенное сообщение содержит только английские символы
            if (isUkrainianText(message)) {
                consoleTextArea.appendText("Invalid input. Only English characters are allowed.\n");
                return message;
            }
            // Encrypt English text
            encryptedMessage = caesarCipher.encryptEnglish(message);
        } else {
            // Return the original message if the language is not selected
            return message;
        }

        // Display the encrypted message if no error occurred
        consoleTextArea.appendText("Encrypted message: " + encryptedMessage + "\n");
        return encryptedMessage;
    }

    private String decryptMessage(String encryptedMessage) {
        String decryptedMessage = "";

        if (selectedLanguage.equals("Ukrainian")) {
            // Decrypt Ukrainian text
            decryptedMessage = caesarCipher.decryptUkrainian(encryptedMessage);
            // Проверка, что расшифрованное сообщение содержит только украинские символы
            if (!isUkrainianText(decryptedMessage)) {
                consoleTextArea.appendText("Decryption error. The decrypted message contains invalid characters.\n");
                return encryptedMessage; // Возвращаем зашифрованное сообщение
            }
        } else if (selectedLanguage.equals("English")) {
            // Decrypt English text
            decryptedMessage = caesarCipher.decryptEnglish(encryptedMessage);
            // Проверка, что расшифрованное сообщение содержит только английские символы
            if (!isUkrainianText(decryptedMessage)) {
                consoleTextArea.appendText("Decryption error. The decrypted message contains invalid characters.\n");
                return encryptedMessage; // Возвращаем зашифрованное сообщение
            }
        } else {
            // Return the encrypted message if the language is not selected
            return encryptedMessage;
        }

        // Display the decrypted message if no error occurred
        consoleTextArea.appendText("Decrypted message: " + decryptedMessage + "\n");
        return decryptedMessage;
    }


    private boolean isUkrainianText(String text) {
        // Паттерн для проверки, что текст содержит только украинские символы
        Pattern pattern = Pattern.compile("^[А-ЩЬЮЯҐЄІЇа-щьюяґєії]+$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}