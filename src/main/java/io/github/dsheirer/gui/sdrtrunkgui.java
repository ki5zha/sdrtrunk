package io.github.dsheirer.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class sdrtrunkgui extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		// Create the main layout
		BorderPane root = new BorderPane();
		
		// Top: Menu Bar
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu helpMenu = new Menu("Help");
		menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
		root.setTop(menuBar);
		
		// Center: Main Content Area
		TableView<String> tableView = new TableView<>();
		TableColumn<String, String> column1 = new TableColumn<>("Channel");
		TableColumn<String, String> column2 = new TableColumn<>("Status");
		tableView.getColumns().addAll(column1, column2);
		root.setCenter(tableView);
		
		// Bottom: Status Bar
		HBox statusBar = new HBox();
		statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px;");
		Label statusLabel = new Label("Status: Ready");
		ProgressBar progressBar = new ProgressBar(0);
		progressBar.setPrefWidth(150);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		statusBar.getChildren().addAll(statusLabel, spacer, progressBar);
		root.setBottom(statusBar);
		
		// Set up the scene and stage
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setTitle("SDRTrunk - JavaFX");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	}

