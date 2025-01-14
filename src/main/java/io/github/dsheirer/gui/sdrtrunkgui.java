package io.github.dsheirer.gui;

import com.jidesoft.icons.MenuCheckIcon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import jiconfont.IconFont;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.javafx.IconFontFX;
import javafx.css.*;
import jiconfont.javafx.IconNode;
import io.github.dsheirer.gui.viewer.FontAwesomeUtil;

import java.util.HashMap;

public class sdrtrunkgui extends Application {
	
	private map<FileMenu> = new HashMap<>
	
	@Override
	public void start(Stage primaryStage) {
		// Create the main layout
		BorderPane root = new BorderPane();
		root.setStyle("main_style");
		
		// Top: Menu Bar
		MenuBar menuBar = new MenuBar();
		Menu mFileMenu = new Menu("File");
		MenuItem editPrefs = new MenuItem("Preferences");
		MenuItem exitGUI = new MenuItem("Exit GUI");
		MenuItem exitApp = new MenuItem("Exit App");
		
		Menu mFunctions = new Menu("Settings");
		MenuItem mSelTuner = new MenuItem("Select Tuners");
		MenuItem mRecordings = new MenuItem("Open Recordings");
		MenuItem mPLEditor = new MenuItem("Playlist Editor");
		MenuItem mBitsView = new MenuItem("*.Bits player");
		MenuItem mAppLogs = new MenuItem("App Logs");
		MenuItem mChanEventLogs = new MenuItem("Channel Event Logs");
		MenuItem mScreenShots = new MenuItem("Screenshots");
		MenuItem mIconManager = new MenuItem("Icon Manager");
		MenuItem mUserPreferences = new MenuItem("User Preferences");
		mFunctions.getItems().addAll(mSelTuner, mRecordings, mPLEditor, mBitsView, mAppLogs,mChanEventLogs, mScreenShots, mIconManager, mUserPreferences);
		
		Menu mOpts = new Menu("View");
		MenuItem mOpts_spectrum = new MenuItem("Disable Waterfall");
		MenuItem mOpts_toggleNPlaying = new MenuItem("Toggle Now Playing");
		MenuItem mOpts_toggleStreamStatus = new MenuItem("Toggle Streaming Status");
		MenuItem mOpts_toggleResourceStatus = new MenuItem("Toggle Resource Status");
		mOpts.getItems().addAll(mOpts_spectrum, mOpts_toggleResourceStatus, mOpts_toggleNPlaying);
		
		Menu mThemes = new Menu("Themes");
		MenuItem mDarkmode = new MenuItem("DarkMode");
		MenuItem mLightMode = new MenuItem("LightMode");
		MenuItem mHideGui = new MenuItem("Hide Gui");
		mThemes.getItems().addAll(mDarkmode, mLightMode, mHideGui);
		Menu mScreenshot = new Menu("Take Screenshot");
		menuBar.getMenus().addAll(mFileMenu, mFunctions, mOpts, mThemes);
		//set the menubar up and quit
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

