package org.point85.workschedule.app;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.point85.workschedule.ShiftInstance;
import org.point85.workschedule.WorkSchedule;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WorkScheduleController extends BaseWorkScheduleController {

	// list of schedule names
	private ObservableList<String> scheduleNames = FXCollections.observableArrayList(new ArrayList<>());

	// list of shift instances associated with the period of time
	private ObservableList<ShiftInstance> shiftInstanceList = FXCollections.observableArrayList(new ArrayList<>());

	// current work schedule
	private WorkSchedule workSchedule;

	@FXML
	private Button btEditor;

	@FXML
	private ComboBox<String> cbSchedules;

	@FXML
	private DatePicker dpPeriodStart;

	@FXML
	private DatePicker dpPeriodEnd;

	@FXML
	private TextField tfStartTime;

	@FXML
	private TextField tfEndTime;

	@FXML
	private TextField tfWorkingTime;

	@FXML
	private TextField tfNonWorkingTime;

	@FXML
	private Button btShowShifts;

	@FXML
	private TableView<ShiftInstance> tvShiftInstances;

	@FXML
	private TableColumn<ShiftInstance, LocalDate> dayColumn;

	@FXML
	private TableColumn<ShiftInstance, String> shiftNameColumn;

	@FXML
	private TableColumn<ShiftInstance, String> teamNameColumn;

	@FXML
	private TableColumn<ShiftInstance, LocalTime> startTimeColumn;

	@FXML
	private TableColumn<ShiftInstance, LocalTime> endTimeColumn;

	@FXML
	private TableColumn<ShiftInstance, String> durationColumn;

	// initialize app
	void initWorkScheduleApp(WorkScheduleApp app) {
		this.setApp(app);

		ImageView newView = new ImageView(new Image("images/Edit.png", 16, 16, true, true));
		btEditor.setGraphic(newView);
		btEditor.setContentDisplay(ContentDisplay.LEFT);

		initializeWorkSchedule();

		initializeShiftInstances();

	}

	private void refreshScheduleNames() {
		scheduleNames.clear();

		// query for the work schedules
		for (String name : getPersistentWorkSchedule().fetchNames()) {
			scheduleNames.add(name);
		}
	}

	private void initializeWorkSchedule() {
		cbSchedules.setItems(scheduleNames);

		refreshScheduleNames();
	}

	private void initializeShiftInstances() {
		// bind to list of shifts
		tvShiftInstances.setItems(shiftInstanceList);

		// instance day
		dayColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleObjectProperty<LocalDate>(cellDataFeatures.getValue().getStartTime().toLocalDate());
		});

		// team name
		teamNameColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getTeam().getName());
		});

		// shift name
		shiftNameColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getShift().getName());
		});

		// starting time
		startTimeColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleObjectProperty<LocalTime>(cellDataFeatures.getValue().getShift().getStart());
		});

		// ending time
		endTimeColumn.setCellValueFactory(cellDataFeatures -> {
			LocalTime end = null;
			try {
				end = cellDataFeatures.getValue().getShift().getEnd();
			} catch (Exception e) {
				showErrorDialog(getApp().getPrimaryStage(), e);
			}
			return new SimpleObjectProperty<LocalTime>(end);
		});

		// duration
		durationColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(stringFromDuration(cellDataFeatures.getValue().getShift().getDuration()));
		});
	}

	// show the editor dialog
	@FXML
	private void handleEditor() {
		try {
			getApp().showEditorDialog();

			// refresh work schedule list
			refreshScheduleNames();

		} catch (Exception e) {
			showErrorDialog(getApp().getPrimaryStage(), e);
		}
	}

	@FXML
	private void handleScheduleSelection() {
		try {
			this.shiftInstanceList.clear();

			String name = this.cbSchedules.getSelectionModel().getSelectedItem();

			// work schedule
			workSchedule = this.getPersistentWorkSchedule().fetchWorkScheduleByName(name);

		} catch (Exception e) {
			showErrorDialog(getApp().getPrimaryStage(), e);
		}
	}

	@FXML
	private void handleShowShifts() {
		try {
			this.shiftInstanceList.clear();

			// period start
			LocalDate startDate = this.dpPeriodStart.getValue();

			if (startDate == null) {
				throw new Exception("A starting date must be chosen.");
			}
			String hrsMins = this.tfStartTime.getText().trim();
			LocalTime startTime = this.localTimeFromString(hrsMins);
			LocalDateTime from = LocalDateTime.of(startDate, startTime);

			// period end
			LocalDate endDate = this.dpPeriodEnd.getValue();
			if (endDate == null) {
				throw new Exception("An ending date must be chosen.");
			}
			hrsMins = this.tfEndTime.getText().trim();
			LocalTime endTime = this.localTimeFromString(hrsMins);
			LocalDateTime to = LocalDateTime.of(endDate, endTime);

			// working time
			Duration working = workSchedule.calculateWorkingTime(from, to);
			this.tfWorkingTime.setText(stringFromDuration(working));

			// non working time
			Duration nonWorking = workSchedule.calculateNonWorkingTime(from, to);
			this.tfNonWorkingTime.setText(stringFromDuration(nonWorking));

			// show shift instances
			long days = endDate.toEpochDay() - startDate.toEpochDay() + 1;

			LocalDate day = startDate;

			for (long i = 0; i < days; i++) {

				List<ShiftInstance> instances = workSchedule.getShiftInstancesForDay(day);

				for (ShiftInstance instance : instances) {
					System.out.println(instance);
					this.shiftInstanceList.add(instance);
				}

				day = day.plusDays(1);
			}

		} catch (Exception e) {
			showErrorDialog(getApp().getPrimaryStage(), e);
		}

	}

}
