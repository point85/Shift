package org.point85.workschedule.test.app;

import java.time.Duration;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.point85.workschedule.WorkSchedule;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class BaseWorkScheduleController {
	// no text
	protected static final String EMPTY_STRING = "";

	// Reference to the main application
	private WorkScheduleApp app;
	
	// work schedule being edited
	protected WorkSchedule currentSchedule;

	protected WorkScheduleApp getApp() {
		return this.app;
	}

	protected void setApp(WorkScheduleApp app) {
		this.app = app;
	}

	// display a general alert
	protected ButtonType showAlert(Stage dialogStage, AlertType type, String title, String header,
			String errorMessage) {
		// Show the error message.
		Alert alert = new Alert(type);
		alert.initOwner(dialogStage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(errorMessage);

		Optional<ButtonType> result = alert.showAndWait();

		ButtonType buttonType = null;
		try {
			buttonType = result.get();
		} catch (NoSuchElementException e) {

		}
		return buttonType;
	}

	// display an error dialog
	protected void showErrorDialog(Stage dialogStage, String message) {
		showAlert(dialogStage, AlertType.ERROR, "Application Error", "Exception", message);
	}

	// display an error dialog
	protected void showErrorDialog(Stage dialogStage, Exception e) {
		String message = e.getMessage();

		if (message == null) {
			message = e.getClass().getSimpleName();
		}
		showAlert(dialogStage, AlertType.ERROR, "Application Error", "Exception", message);
	}

	// display an ok/cancel dialog
	protected ButtonType showConfirmationDialog(Stage dialogStage, String message) {
		return showAlert(dialogStage, AlertType.CONFIRMATION, "Confirmation", "Confirm Action", message);
	}

	protected Duration durationFromString(String hrsMins) throws Exception {

		String[] fields = hrsMins.split(":");

		if (fields.length != 2) {
			throw new Exception("Both hours and minutes for the shift duration must be specified.");
		}

		long seconds = Integer.valueOf(fields[0]) * 3600 + Integer.valueOf(fields[1]) * 60;

		Duration duration = Duration.ofSeconds(seconds);

		return duration;
	}

	protected LocalTime localTimeFromString(String hrsMins) throws Exception {
		String[] fields = hrsMins.split(":");

		if (fields.length != 2) {
			throw new Exception("Both hours and minutes for the start time of day must be specified.");
		}

		LocalTime time = LocalTime.of(Integer.valueOf(fields[0]), Integer.valueOf(fields[1]));

		return time;
	}

	protected String stringFromLocalTime(LocalTime time) {
		return String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute());
	}

	protected String stringFromDuration(Duration duration) {
		long seconds = duration.getSeconds();
		long hours = seconds / 3600;
		long minutes = (seconds - hours * 3600) / 60;

		return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
	}
}
