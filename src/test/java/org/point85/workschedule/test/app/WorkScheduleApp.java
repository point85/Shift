package org.point85.workschedule.test.app;

import java.net.URL;

import org.point85.workschedule.WorkSchedule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WorkScheduleApp extends Application {
	// the main stage
	private Stage primaryStage;

	// parent layout
	private AnchorPane scheduleLayout;

	// start the app
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Work Schedule");

		// Set the application icon.
		Image appIcon = new Image(getImagesPath() + "Point85.png");
		this.primaryStage.getIcons().add(appIcon);

		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		String path = getFxmlPath() + "WorkSchedule.fxml";
		URL url = WorkScheduleApp.class.getResource(path);
		loader.setLocation(url);
		scheduleLayout = (AnchorPane) loader.load();

		// Show the scene containing the root layout.
		Scene scene = new Scene(scheduleLayout);
		primaryStage.setScene(scene);

		// Give the controller access to the main app.
		WorkScheduleController controller = loader.getController();
		controller.initWorkScheduleApp(this);

		// show the converter
		primaryStage.show();
	}
	
	// path to images
	private String getImagesPath() {
		return "images/";
	}

	// path to FXML
	private String getFxmlPath() {
		// path relative to "bin" folder
		return "../app/";
	}

	// display the UOM editor as a dialog
	void showEditorDialog(WorkSchedule schedule) throws Exception {
		// Load the fxml file and create a new stage for the pop-up dialog.
		FXMLLoader loader = new FXMLLoader();
		String path = getFxmlPath() + "WorkScheduleEditor.fxml";
		loader.setLocation(WorkScheduleApp.class.getResource(path));
		AnchorPane page = (AnchorPane) loader.load();

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Edit Work Schedule");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the schedule into the controller.
		WorkScheduleEditorController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.currentSchedule = schedule;
		controller.initializeEditor(this);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Main entry point
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
