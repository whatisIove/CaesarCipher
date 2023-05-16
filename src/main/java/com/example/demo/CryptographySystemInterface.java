package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CryptographySystemInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cryptography System");

        // Создание меню
        Menu fileMenu = new Menu("Файл");
        MenuItem createItem = new MenuItem("Створити");
        MenuItem openItem = new MenuItem("Відкрити");
        MenuItem saveItem = new MenuItem("Зберегти");
        MenuItem printItem = new MenuItem("Друкувати");
        MenuItem exitItem = new MenuItem("Вийти");
        fileMenu.getItems().addAll(createItem, openItem, saveItem, printItem, new SeparatorMenuItem(), exitItem);

        Menu encryptMenu = new Menu("Шифрування");
        MenuItem encryptUkrainianItem = new MenuItem("Зашифрувати (українська)");
        MenuItem encryptEnglishItem = new MenuItem("Зашифрувати (англійська)");
        encryptMenu.getItems().addAll(encryptUkrainianItem, encryptEnglishItem);

        Menu decryptMenu = new Menu("Розшифрування");
        MenuItem decryptUkrainianItem = new MenuItem("Розшифрувати (українська)");
        MenuItem decryptEnglishItem = new MenuItem("Розшифрувати (англійська)");
        decryptMenu.getItems().addAll(decryptUkrainianItem, decryptEnglishItem);

        Menu aboutMenu = new Menu("Про програму");
        MenuItem developerItem = new MenuItem("Розробник");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, encryptMenu, decryptMenu, aboutMenu);
        aboutMenu.getItems().add(developerItem);

        // Создание кнопок
        Button encryptButton = new Button("Зашифрувати");
        Button decryptButton = new Button("Розшифрувати");
        Button exitButton = new Button("Вийти");

        // Обработчики событий
        createItem.setOnAction(e -> System.out.println("Обраний пункт: Створити"));
        openItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Відкрити файл");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                System.out.println("Обраний файл: " + selectedFile.getAbsolutePath());
            }
        });
        saveItem.setOnAction(e -> System.out.println("Обраний пункт: Зберегти"));
        printItem.setOnAction(e -> System.out.println("Обраний пункт: Друкувати"));
        exitItem.setOnAction(e -> primaryStage.close());
        encryptUkrainianItem.setOnAction(e -> System.out.println("Обраний пункт: Зашифрувати (українська)"));
        encryptEnglishItem.setOnAction(e -> System.out.println("Обраний пункт: Зашифрувати (англійська)"));
        decryptUkrainianItem.setOnAction(e -> System.out.println("Обраний пункт: Розшифрувати (українська)"));
        decryptEnglishItem.setOnAction(e -> System.out.println("Обраний пункт: Розшифрувати (англійська)"));

        developerItem.setOnAction(e -> System.out.println("Обраний пункт: Розробник"));

        encryptButton.setOnAction(e -> System.out.println("Натиснута кнопка: Зашифрувати"));
        decryptButton.setOnAction(e -> System.out.println("Натиснута кнопка: Розшифрувати"));
        exitButton.setOnAction(e -> primaryStage.close());

        // Создание вертикального контейнера
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.getChildren().addAll(menuBar, encryptButton, decryptButton, exitButton);

        // Создание сцены
        Scene scene = new Scene(vbox, 300, 200);

        // Установка сцены для primaryStage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
