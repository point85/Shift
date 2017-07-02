package org.point85.workschedule.app;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.point85.workschedule.NonWorkingPeriod;
import org.point85.workschedule.Rotation;
import org.point85.workschedule.RotationSegment;
import org.point85.workschedule.Shift;
import org.point85.workschedule.Team;
import org.point85.workschedule.WorkSchedule;
import org.point85.workschedule.persistence.PersistentWorkSchedule;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class WorkScheduleEditorController extends BaseWorkScheduleController {

	// stage for the dialog editor
	private Stage dialogStage;

	// work schedule being edited
	private WorkSchedule currentSchedule;

	// current shift being edited
	private Shift currentShift;

	// current team being edited
	private Team currentTeam;

	// current rotation being edited
	private Rotation currentRotation;

	// current rotation segment being edited
	private RotationSegment currentRotationSegment;

	// current non-working period being edited
	private NonWorkingPeriod currentPeriod;

	// list of schedule names
	private ObservableList<String> scheduleNames = FXCollections.observableArrayList(new ArrayList<>());

	// list of shifts associated with the work schedule being edited
	private ObservableList<Shift> shiftList = FXCollections.observableArrayList(new ArrayList<>());

	// list of shift names for the rotation segment starting shift choice
	private ObservableList<String> shiftNames = FXCollections.observableArrayList(new ArrayList<>());

	// list of teams associated with the work schedule being edited
	private ObservableList<Team> teamList = FXCollections.observableArrayList(new ArrayList<>());

	// list of rotation names available for team assignment
	private ObservableList<String> rotationNames = FXCollections.observableArrayList(new ArrayList<>());

	// list of rotations
	private ObservableList<Rotation> rotationList = FXCollections.observableArrayList(new ArrayList<>());

	// list of rotation segments
	private ObservableList<RotationSegment> rotationSegmentList = FXCollections.observableArrayList(new ArrayList<>());

	// list of non-working periods associated with the work schedule being
	// edited
	private ObservableList<NonWorkingPeriod> periodList = FXCollections.observableArrayList(new ArrayList<>());

	// work schedule
	@FXML
	private TextField tfScheduleName;

	@FXML
	private TextArea taScheduleDescription;

	@FXML
	private Button btNew;

	@FXML
	private Button btSave;

	@FXML
	private Button btDelete;

	@FXML
	private Button btRefresh;

	@FXML
	private ListView<String> lvSchedules;

	@FXML
	private Tab tShifts;

	@FXML
	private Tab tTeams;

	@FXML
	private TabPane tpShiftTeams;

	// ******************* shifts *********************************************
	@FXML
	private TextField tfShiftName;

	@FXML
	private TextField tfShiftDescription;

	@FXML
	private TextField tfShiftStart;

	@FXML
	private TextField tfShiftDuration;

	@FXML
	private TableView<Shift> tvShifts;

	@FXML
	private TableColumn<Shift, String> shiftNameColumn;

	@FXML
	private TableColumn<Shift, String> shiftDescriptionColumn;

	@FXML
	private TableColumn<Shift, LocalTime> shiftStartColumn;

	@FXML
	private TableColumn<Shift, String> shiftDurationColumn;

	@FXML
	private Button btNewShift;

	@FXML
	private Button btAddShift;

	@FXML
	private Button btRemoveShift;

	// ******************* teams *********************************************
	@FXML
	private TextField tfTeamName;

	@FXML
	private TextField tfTeamDescription;

	@FXML
	private ComboBox<String> cbTeamRotations;

	@FXML
	private DatePicker dpTeamRotationStart;

	@FXML
	private TableView<Team> tvTeams;

	@FXML
	private TableColumn<Team, String> teamNameColumn;

	@FXML
	private TableColumn<Team, String> teamDescriptionColumn;

	@FXML
	private TableColumn<Team, String> teamRotationColumn;

	@FXML
	private TableColumn<Team, LocalDate> teamRotationStartColumn;

	@FXML
	private TableColumn<Team, String> teamAvgHoursColumn;

	@FXML
	private Button btNewTeam;

	@FXML
	private Button btAddTeam;

	@FXML
	private Button btRemoveTeam;

	// ******************* rotations **************************************
	@FXML
	private TextField tfRotationName;

	@FXML
	private TextField tfRotationDescription;

	@FXML
	private ComboBox<String> cbRotationSegmentShifts;

	@FXML
	private Spinner<Integer> spDaysOn;

	@FXML
	private Spinner<Integer> spDaysOff;

	@FXML
	private TableView<Rotation> tvRotations;

	@FXML
	private TableColumn<Rotation, String> rotationNameColumn;

	@FXML
	private TableColumn<Rotation, String> rotationDescriptionColumn;

	@FXML
	private TableColumn<Rotation, String> rotationDurationColumn;

	@FXML
	private TableView<RotationSegment> tvRotationSegments;

	@FXML
	private TableColumn<RotationSegment, String> rotationSegmentSequenceColumn;

	@FXML
	private TableColumn<RotationSegment, String> rotationSegmentShiftColumn;

	@FXML
	private TableColumn<RotationSegment, String> rotationSegmentDaysOnColumn;

	@FXML
	private TableColumn<RotationSegment, String> rotationSegmentDaysOffColumn;

	@FXML
	private Button btNewRotation;

	@FXML
	private Button btAddRotation;

	@FXML
	private Button btRemoveRotation;

	@FXML
	private Button btNewRotationSegment;

	@FXML
	private Button btAddRotationSegment;

	@FXML
	private Button btRemoveRotationSegment;

	// ***** non-working periods *******************************************
	@FXML
	private TextField tfPeriodName;

	@FXML
	private TextField tfPeriodDescription;

	@FXML
	private DatePicker dpPeriodStartDate;

	@FXML
	private TextField tfPeriodStartTime;

	@FXML
	private TextField tfPeriodDuration;

	@FXML
	private TableView<NonWorkingPeriod> tvNonWorkingPeriods;

	@FXML
	private TableColumn<NonWorkingPeriod, String> periodNameColumn;

	@FXML
	private TableColumn<NonWorkingPeriod, String> periodDescriptionColumn;

	@FXML
	private TableColumn<NonWorkingPeriod, LocalDateTime> periodStartColumn;

	@FXML
	private TableColumn<NonWorkingPeriod, String> periodDurationColumn;

	@FXML
	private Button btNewNonWorkingPeriod;

	@FXML
	private Button btAddNonWorkingPeriod;

	@FXML
	private Button btRemoveNonWorkingPeriod;

	private void initializeScheduleList() {
		lvSchedules.setItems(scheduleNames);

		// list of schedules selection listener
		lvSchedules.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					updateEditor(newValue);
				} catch (Exception e) {
					showErrorDialog(dialogStage, e);
				}
			}
		});
	}

	private void initializeShiftEditor() {
		// bind to list of shifts
		tvShifts.setItems(shiftList);

		// table view row selection listener
		tvShifts.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					updateShiftEditor(newValue);
				} catch (Exception e) {
					showErrorDialog(dialogStage, e);
				}
			}
		});

		// shift table callbacks
		// shift name
		shiftNameColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getName());
		});

		// shift description
		shiftDescriptionColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getDescription());
		});

		// shift start time
		shiftStartColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleObjectProperty<LocalTime>(cellDataFeatures.getValue().getStart());
		});

		// shift duration
		shiftDurationColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(stringFromDuration(cellDataFeatures.getValue().getDuration()));
		});
	}

	private void initializeRotationEditor() {

		// spinner value factory
		SpinnerValueFactory<Integer> onValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Integer.MAX_VALUE, 1);

		spDaysOn.setValueFactory(onValueFactory);

		// spinner value factory
		SpinnerValueFactory<Integer> offValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Integer.MAX_VALUE, 1);

		spDaysOff.setValueFactory(offValueFactory);

		// tables
		tvRotations.setItems(rotationList);
		tvRotationSegments.setItems(rotationSegmentList);

		// rotation table view row selection listener
		tvRotations.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					updateRotationEditor(newValue);
				} catch (Exception e) {
					showErrorDialog(dialogStage, e);
				}
			}
		});

		// rotation table callbacks
		// rotation name
		rotationNameColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getName());
		});

		// rotation description
		rotationDescriptionColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getDescription());
		});

		// rotation duration
		rotationDurationColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(this.stringFromDuration(cellDataFeatures.getValue().getDuration()));
		});

		// rotation table view row selection listener
		tvRotationSegments.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldValue, newValue) -> {
					if (newValue != null) {
						try {
							updateRotationSegmentEditor(newValue);
						} catch (Exception e) {
							showErrorDialog(dialogStage, e);
						}
					}
				});

		// segment start shift
		rotationSegmentShiftColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getStartingShift().getName());
		});

		// segment days on
		rotationSegmentDaysOnColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getDaysOn()));
		});

		// segment days off
		rotationSegmentDaysOffColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getDaysOff()));
		});

		// sequence
		rotationSegmentSequenceColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getSequence()));
		});

		// starting shift names
		this.cbRotationSegmentShifts.setItems(shiftNames);

	}

	private void initializeTeamEditor() throws Exception {
		// list of rotations to choose from
		this.cbTeamRotations.setItems(rotationNames);

		// bind to list of teams
		tvTeams.setItems(teamList);

		// table view row selection listener
		tvTeams.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					updateTeamEditor(newValue);
				} catch (Exception e) {
					showErrorDialog(dialogStage, e);
				}
			}
		});

		// team table callbacks
		// team name
		teamNameColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getName());
		});

		// team description
		teamDescriptionColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getDescription());
		});

		// team rotation name
		teamRotationColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getRotation().getName());
		});

		// rotation start
		teamRotationStartColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleObjectProperty<LocalDate>(cellDataFeatures.getValue().getRotationStart());
		});

		// team average hours worked per week
		teamAvgHoursColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(
					this.stringFromDuration(cellDataFeatures.getValue().getHoursWorkedPerWeek()));
		});
	}

	private void initializeNonWorkingPeriodEditor() throws Exception {
		// bind to list of periods
		this.tvNonWorkingPeriods.setItems(periodList);

		// table view row selection listener
		tvNonWorkingPeriods.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, oldValue, newValue) -> {
					if (newValue != null) {
						try {
							updatePeriodEditor(newValue);
						} catch (Exception e) {
							showErrorDialog(dialogStage, e);
						}
					}
				});

		// non-working period table callbacks
		// period name
		periodNameColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getName());
		});

		// period description
		periodDescriptionColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(cellDataFeatures.getValue().getDescription());
		});

		// period start
		periodStartColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleObjectProperty<LocalDateTime>(cellDataFeatures.getValue().getStartDateTime());
		});

		// period duration
		periodDurationColumn.setCellValueFactory(cellDataFeatures -> {
			return new SimpleStringProperty(this.stringFromDuration(cellDataFeatures.getValue().getDuration()));
		});
	}

	// initialize editor
	void initializeEditor(WorkScheduleApp app) throws Exception {
		// main app
		setApp(app);

		// images for buttons
		setButtonImages();

		// all work schedules
		initializeScheduleList();

		// shift editor
		initializeShiftEditor();

		// team editor
		initializeTeamEditor();

		// rotation editor
		initializeRotationEditor();

		// non-working period editor
		initializeNonWorkingPeriodEditor();

		// display all defined work schedules
		displaySchedules();
	}

	// update the editor upon selection of a work schedule or refresh
	private void updateEditor(String scheduleName) throws Exception {
		if (scheduleName == null) {
			return;
		}
		WorkSchedule schedule = PersistentWorkSchedule.getInstance().fetchWorkScheduleByName(scheduleName);

		// attributes
		this.displayAttributes(schedule);

		this.currentSchedule = schedule;
	}

	// update the shift editing part
	private void updateShiftEditor(Shift shift) {
		btAddShift.setText("Update");
		this.currentShift = shift;

		// name
		this.tfShiftName.setText(shift.getName());

		// description
		this.tfShiftDescription.setText(shift.getDescription());

		// start time
		LocalTime startTime = shift.getStart();
		this.tfShiftStart.setText(stringFromLocalTime(startTime));

		Duration duration = shift.getDuration();
		this.tfShiftDuration.setText(stringFromDuration(duration));
	}

	// called on team selection in table listener
	private void updateTeamEditor(Team team) {
		btAddTeam.setText("Update");
		this.currentTeam = team;

		// name
		this.tfTeamName.setText(team.getName());

		// description
		this.tfTeamDescription.setText(team.getDescription());

		// rotation
		this.cbTeamRotations.getSelectionModel().select(team.getRotation().getName());

		// rotation start
		this.dpTeamRotationStart.setValue(team.getRotationStart());
	}

	// called on rotation selection in table listener
	private void updateRotationEditor(Rotation rotation) {
		btAddRotation.setText("Update");

		this.currentRotation = rotation;

		// name
		this.tfRotationName.setText(rotation.getName());

		// description
		this.tfRotationDescription.setText(rotation.getDescription());

		// segments
		List<RotationSegment> segments = rotation.getRotationSegments();

		rotationSegmentList.clear();
		for (RotationSegment segment : segments) {
			rotationSegmentList.add(segment);
		}
		Collections.sort(rotationSegmentList);
		tvRotationSegments.refresh();
	}

	// called on rotation segment selection in table listener
	private void updateRotationSegmentEditor(RotationSegment segment) {
		btAddRotationSegment.setText("Update");

		this.currentRotationSegment = segment;

		this.cbRotationSegmentShifts.setValue(segment.getStartingShift().getName());

		this.spDaysOn.getValueFactory().setValue(segment.getDaysOn());

		this.spDaysOff.getValueFactory().setValue(segment.getDaysOff());
	}

	// called on non-working period selection in table listener
	private void updatePeriodEditor(NonWorkingPeriod period) {
		btAddNonWorkingPeriod.setText("Update");
		this.currentPeriod = period;

		// name
		this.tfPeriodName.setText(period.getName());

		// description
		this.tfPeriodDescription.setText(period.getDescription());

		// start
		this.dpPeriodStartDate.setValue(period.getStartDateTime().toLocalDate());
		LocalTime startTime = period.getStartDateTime().toLocalTime();
		this.tfPeriodStartTime.setText(stringFromLocalTime(startTime));

		// duration
		this.tfPeriodDuration.setText(stringFromDuration(period.getDuration()));
	}

	private void displaySchedules() {
		scheduleNames.clear();

		for (String name : getScheduleNames()) {
			scheduleNames.add(name);
		}
		Collections.sort(scheduleNames);
	}

	// images for editor buttons
	private void setButtonImages() {
		// new schedule
		ImageView newView = new ImageView(new Image("images/New.png", 16, 16, true, true));
		btNew.setGraphic(newView);
		btNew.setContentDisplay(ContentDisplay.RIGHT);

		// save schedule
		ImageView saveView = new ImageView(new Image("images/Save.png", 16, 16, true, true));
		btSave.setGraphic(saveView);
		btSave.setContentDisplay(ContentDisplay.RIGHT);

		// delete schedule
		ImageView deleteView = new ImageView(new Image("images/Delete.png", 16, 16, true, true));
		btDelete.setGraphic(deleteView);
		btDelete.setContentDisplay(ContentDisplay.RIGHT);

		// refresh schedule
		ImageView refreshView = new ImageView(new Image("images/Refresh.png", 16, 16, true, true));
		btRefresh.setGraphic(refreshView);
		btRefresh.setContentDisplay(ContentDisplay.RIGHT);

		// new shift
		ImageView newShiftView = new ImageView(new Image("images/New.png", 16, 16, true, true));
		btNewShift.setGraphic(newShiftView);
		btNewShift.setContentDisplay(ContentDisplay.LEFT);

		// add shift
		ImageView addShiftView = new ImageView(new Image("images/Add.png", 16, 16, true, true));
		btAddShift.setGraphic(addShiftView);
		btAddShift.setContentDisplay(ContentDisplay.LEFT);

		// remove shift
		ImageView removeShiftView = new ImageView(new Image("images/Remove.png", 16, 16, true, true));
		btRemoveShift.setGraphic(removeShiftView);
		btRemoveShift.setContentDisplay(ContentDisplay.LEFT);

		// new team
		ImageView newTeamView = new ImageView(new Image("images/New.png", 16, 16, true, true));
		btNewTeam.setGraphic(newTeamView);
		btNewTeam.setContentDisplay(ContentDisplay.LEFT);

		// add team
		ImageView addTeamView = new ImageView(new Image("images/Add.png", 16, 16, true, true));
		btAddTeam.setGraphic(addTeamView);
		btAddTeam.setContentDisplay(ContentDisplay.LEFT);

		// remove team
		ImageView removeTeamView = new ImageView(new Image("images/Remove.png", 16, 16, true, true));
		btRemoveTeam.setGraphic(removeTeamView);
		btRemoveTeam.setContentDisplay(ContentDisplay.LEFT);

		// new rotation
		ImageView newRotationView = new ImageView(new Image("images/New.png", 16, 16, true, true));
		btNewRotation.setGraphic(newRotationView);
		btNewRotation.setContentDisplay(ContentDisplay.LEFT);

		// add rotation
		ImageView addRotationView = new ImageView(new Image("images/Add.png", 16, 16, true, true));
		btAddRotation.setGraphic(addRotationView);
		btAddRotation.setContentDisplay(ContentDisplay.LEFT);

		// remove rotation
		ImageView removeRotationView = new ImageView(new Image("images/Remove.png", 16, 16, true, true));
		btRemoveRotation.setGraphic(removeRotationView);
		btRemoveRotation.setContentDisplay(ContentDisplay.LEFT);

		// new rotation segment
		ImageView newRotationSegmentView = new ImageView(new Image("images/New.png", 16, 16, true, true));
		btNewRotationSegment.setGraphic(newRotationSegmentView);
		btNewRotationSegment.setContentDisplay(ContentDisplay.LEFT);

		// add rotation segment
		ImageView addRotationSegmentView = new ImageView(new Image("images/Add.png", 16, 16, true, true));
		btAddRotationSegment.setGraphic(addRotationSegmentView);
		btAddRotationSegment.setContentDisplay(ContentDisplay.LEFT);

		// remove rotation segment
		ImageView removeRotationSegmentView = new ImageView(new Image("images/Remove.png", 16, 16, true, true));
		btRemoveRotationSegment.setGraphic(removeRotationSegmentView);
		btRemoveRotationSegment.setContentDisplay(ContentDisplay.LEFT);

		// new non-working period
		ImageView newPeriodView = new ImageView(new Image("images/New.png", 16, 16, true, true));
		btNewNonWorkingPeriod.setGraphic(newPeriodView);
		btNewNonWorkingPeriod.setContentDisplay(ContentDisplay.LEFT);

		// add non-working period
		ImageView addPeriodView = new ImageView(new Image("images/Add.png", 16, 16, true, true));
		btAddNonWorkingPeriod.setGraphic(addPeriodView);
		btAddNonWorkingPeriod.setContentDisplay(ContentDisplay.LEFT);

		// remove non-working period
		ImageView removePeriodView = new ImageView(new Image("images/Remove.png", 16, 16, true, true));
		btRemoveNonWorkingPeriod.setGraphic(removePeriodView);
		btRemoveNonWorkingPeriod.setContentDisplay(ContentDisplay.LEFT);
	}

	// reference to the main app stage
	void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	private void resetEditor() {
		this.currentSchedule = null;

		// main attributes
		this.tfScheduleName.clear();
		this.taScheduleDescription.clear();

		// reset each sub-editor
		handleNewShift();
		handleNewTeam();
		handleNewRotation();
		handleNewRotationSegment();
		handleNewNonWorkingPeriod();

		// clear the tables
		this.shiftList.clear();
		this.shiftNames.clear();
		this.tvShifts.refresh();

		this.teamList.clear();
		this.tvShifts.refresh();

		this.rotationList.clear();
		this.rotationNames.clear();
		this.tvRotations.refresh();

		this.rotationSegmentList.clear();
		this.tvRotationSegments.refresh();

		this.periodList.clear();
		this.tvNonWorkingPeriods.refresh();
	}

	// New button clicked
	@FXML
	private void handleNew() {
		try {
			resetEditor();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// Delete button clicked
	@FXML
	private void handleDelete() {
		if (currentSchedule == null || currentSchedule.getKey() == null) {
			showErrorDialog(dialogStage, "No work schedule has been selected for deletion.");
			return;
		}

		// confirm
		String msg = "Do you want to delete " + currentSchedule.getName() + "?";
		ButtonType type = showConfirmationDialog(dialogStage, msg);

		if (type.equals(ButtonType.CANCEL)) {
			return;
		}

		try {
			// delete
			PersistentWorkSchedule.getInstance().deleteWorkSchedule(currentSchedule);

			// reset editor
			resetEditor();

			// update schedule list
			displaySchedules();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}

	}

	// Refresh button clicked
	@FXML
	private void handleRefresh() {
		try {
			currentSchedule = null;
			String sheduleName = this.lvSchedules.getSelectionModel().getSelectedItem();
			if (sheduleName != null) {
				updateEditor(sheduleName);
			} else {
				displaySchedules();
			}
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	private WorkSchedule setWorkScheduleAttributes() throws Exception {
		WorkSchedule schedule = currentSchedule;

		if (schedule == null) {
			schedule = new WorkSchedule();
		}

		// work schedule attributes
		String name = this.tfScheduleName.getText().trim();
		schedule.setName(name);
		String description = this.taScheduleDescription.getText().trim();
		schedule.setDescription(description);
		return schedule;
	}

	// Save button clicked
	@FXML
	private void handleSave() {

		// work schedule attributes
		WorkSchedule schedule = null;
		try {
			// set main attributes
			schedule = setWorkScheduleAttributes();

			if (schedule.getKey() == null) {
				try {
					WorkSchedule existing = PersistentWorkSchedule.getInstance()
							.fetchWorkScheduleByName(schedule.getName());

					if (existing != null) {
						throw new Exception("Work schedule with name " + schedule.getName() + " already exists.");
					}
				} catch (Exception e) {
					// ignore - schedule does not exist
				}
			}

			// save the created or updated work schedule
			PersistentWorkSchedule.getInstance().saveWorkSchedule(schedule);

			this.currentSchedule = schedule;

		} catch (Exception e) {
			// remove from persistence unit
			if (schedule != null) {
				PersistentWorkSchedule.getInstance().evictWorkSchedule(schedule);
			}

			showErrorDialog(dialogStage, e);
		}

		// show all schedules
		displaySchedules();
	}

	// show the schedule attributes
	private void displayAttributes(WorkSchedule schedule) {
		this.resetEditor();

		// main schedule
		this.tfScheduleName.setText(schedule.getName());
		this.taScheduleDescription.setText(schedule.getDescription());

		// shifts
		List<Shift> shifts = schedule.getShifts();

		shiftList.clear();
		shiftNames.clear();

		for (Shift shift : shifts) {
			shiftList.add(shift);
			shiftNames.add(shift.getName());
		}
		Collections.sort(shiftList);
		tvShifts.refresh();

		// teams
		List<Team> teams = schedule.getTeams();

		teamList.clear();
		rotationList.clear();
		rotationSegmentList.clear();
		rotationNames.clear();

		for (Team team : teams) {
			teamList.add(team);

			Rotation rotation = team.getRotation();

			if (rotation.getName() != null && !rotationList.contains(rotation)) {
				// add to table binding
				rotationList.add(rotation);

				// add to combobox of names
				rotationNames.add(rotation.getName());
			}
		}

		Collections.sort(teamList);
		Collections.sort(rotationList);
		Collections.sort(rotationNames);

		tvTeams.refresh();
		tvRotations.refresh();

		// non-working periods
		List<NonWorkingPeriod> periods = schedule.getNonWorkingPeriods();
		periodList.clear();

		for (NonWorkingPeriod period : periods) {
			periodList.add(period);
		}
		Collections.sort(periodList);

		tvNonWorkingPeriods.refresh();

	}

	protected List<String> getScheduleNames() {
		return PersistentWorkSchedule.getInstance().fetchNames();
	}

	// new shift button clicked
	@FXML
	private void handleNewShift() {
		btAddShift.setText("Add");
		this.currentShift = null;

		// shift editing attributes
		this.tfShiftName.clear();
		this.tfShiftDescription.clear();
		this.tfShiftStart.clear();
		this.tfShiftDuration.clear();

		this.tvShifts.getSelectionModel().clearSelection();
	}

	// new team button clicked
	@FXML
	private void handleNewTeam() {
		btAddTeam.setText("Add");
		this.currentTeam = null;

		// team editing attributes
		this.tfTeamName.clear();
		this.tfTeamDescription.clear();
		this.cbTeamRotations.getSelectionModel().clearSelection();
		this.dpTeamRotationStart.setValue(null);

		this.tvTeams.getSelectionModel().clearSelection();
	}

	// add shift button clicked
	@FXML
	private void handleAddShift() {
		try {
			// name
			String name = this.tfShiftName.getText().trim();

			if (name == null || name.length() == 0) {
				throw new Exception("The name of the shift must be specified.");
			}

			// add to comboBox for rotation segments
			if (!shiftNames.contains(name)) {
				shiftNames.add(name);
			}

			// description
			String description = this.tfShiftDescription.getText().trim();

			// start time
			String start = this.tfShiftStart.getText().trim();
			LocalTime startTime = this.localTimeFromString(start);

			// duration
			String hrsMins = this.tfShiftDuration.getText().trim();

			Duration duration = this.durationFromString(hrsMins);

			if (currentSchedule == null) {
				currentSchedule = setWorkScheduleAttributes();
			}

			if (currentShift == null) {
				// new shift
				currentShift = currentSchedule.createShift(name, description, startTime, duration);
				shiftList.add(currentShift);
				Collections.sort(shiftList);
			} else {
				currentShift.setName(name);
				currentShift.setDescription(description);
				currentShift.setStart(startTime);
				currentShift.setDuration(duration);
			}

			tvShifts.refresh();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// remove shift button clicked
	@FXML
	private void handleRemoveShift() {
		Shift shift = this.tvShifts.getSelectionModel().getSelectedItem();

		if (shift == null) {
			return;
		}

		currentSchedule.getShifts().remove(shift);
		shiftList.remove(shift);
		Collections.sort(shiftList);
		shiftNames.remove(shift.getName());
		currentShift = null;
		tvShifts.getSelectionModel().clearSelection();
		tvShifts.refresh();
	}

	// add team button clicked
	@FXML
	private void handleAddTeam() {
		try {
			// name
			String name = this.tfTeamName.getText().trim();

			if (name == null || name.length() == 0) {
				throw new Exception("The name of the team must be specified.");
			}

			// description
			String description = this.tfTeamDescription.getText().trim();

			// rotation
			String rotationName = this.cbTeamRotations.getSelectionModel().getSelectedItem();

			Rotation teamRotation = null;

			for (Rotation rotation : rotationList) {
				if (rotation.getName().equals(rotationName)) {
					teamRotation = rotation;
					break;
				}
			}

			// rotation start
			LocalDate rotationStart = this.dpTeamRotationStart.getValue();

			if (currentSchedule == null) {
				currentSchedule = setWorkScheduleAttributes();
			}

			if (currentTeam == null) {
				// new team
				currentTeam = currentSchedule.createTeam(name, description, teamRotation, rotationStart);
				teamList.add(currentTeam);
			} else {
				currentTeam.setName(name);
				currentTeam.setDescription(description);
				currentTeam.setRotation(teamRotation);
				currentTeam.setRotationStart(rotationStart);
			}

			tvTeams.refresh();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// remove team button clicked
	@FXML
	private void handleRemoveTeam() {
		Team team = this.tvTeams.getSelectionModel().getSelectedItem();

		if (team == null) {
			return;
		}

		currentSchedule.getTeams().remove(team);
		teamList.remove(team);
		currentTeam = null;
		tvTeams.getSelectionModel().clearSelection();
		tvTeams.refresh();
	}

	// add rotation button clicked
	@FXML
	private void handleAddRotation() {
		try {
			// name
			String name = this.tfRotationName.getText().trim();

			// description
			String description = this.tfRotationDescription.getText().trim();

			if (currentSchedule == null) {
				currentSchedule = setWorkScheduleAttributes();
			}

			if (currentRotation == null) {
				// new rotation
				currentRotation = new Rotation(name, description);
				rotationList.add(currentRotation);
			} else {
				currentRotation.setName(name);
				currentRotation.setDescription(description);
			}

			tvRotations.refresh();

			// also set for use by a team
			if (!rotationNames.contains(name)) {
				rotationNames.add(name);
			}

		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// new rotation button clicked
	@FXML
	private void handleNewRotation() {
		btAddRotation.setText("Add");
		this.currentRotation = null;

		// team editing attributes
		this.tfRotationName.clear();
		this.tfRotationDescription.clear();

		this.tvRotations.getSelectionModel().clearSelection();
	}

	// remove rotation button clicked
	@FXML
	private void handleRemoveRotation() {
		try {
			Rotation rotation = this.tvRotations.getSelectionModel().getSelectedItem();

			if (rotation == null) {
				return;
			}

			// check for team reference
			List<Team> referencingTeams = PersistentWorkSchedule.getInstance().getCrossReferences(rotation);

			if (referencingTeams.size() != 0) {
				String teamNames = "";

				for (Team team : referencingTeams) {
					if (teamNames.length() > 0) {
						teamNames += ", ";
					}
					teamNames += team.getName();
				}
				throw new Exception("Rotation " + rotation.getName()
						+ " cannot be deleted.  It is referenced by team(s) " + teamNames);
			}

			rotationList.remove(rotation);
			currentRotation = null;
			tvRotations.getSelectionModel().clearSelection();
			tvRotations.refresh();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// new rotation segment button clicked
	@FXML
	private void handleNewRotationSegment() {
		btAddRotationSegment.setText("Add");
		this.currentRotationSegment = null;

		// editing attributes
		this.cbRotationSegmentShifts.getSelectionModel().clearSelection();
		this.spDaysOn.getValueFactory().setValue(1);
		this.spDaysOff.getValueFactory().setValue(1);

		this.tvRotationSegments.getSelectionModel().clearSelection();
	}

	// add rotation segment button clicked
	@FXML
	private void handleAddRotationSegment() {
		try {
			if (currentRotation == null) {
				throw new Exception("A rotation must be selected before adding a segment.");
			}

			if (currentSchedule == null) {
				currentSchedule = setWorkScheduleAttributes();
			}

			// shift
			String shiftName = this.cbRotationSegmentShifts.getSelectionModel().getSelectedItem();
			Shift startingShift = null;

			for (Shift shift : shiftList) {
				if (shift.getName().equals(shiftName)) {
					startingShift = shift;
					break;
				}
			}

			if (startingShift == null) {
				throw new Exception("No shift found with name" + shiftName);
			}

			Integer daysOn = this.spDaysOn.getValue();
			Integer daysOff = this.spDaysOff.getValue();

			if (currentRotationSegment == null) {
				// new
				currentRotationSegment = currentRotation.addSegment(startingShift, daysOn, daysOff);
				rotationSegmentList.add(currentRotationSegment);
				currentRotationSegment.setSequence(rotationSegmentList.size());
			} else {
				// update
				currentRotationSegment.setStartingShift(startingShift);
				currentRotationSegment.setDaysOn(daysOn);
				currentRotationSegment.setDaysOff(daysOff);
			}

			tvRotationSegments.refresh();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// remove rotation segment button clicked
	@FXML
	private void handleRemoveRotationSegment() {
		try {
			RotationSegment segment = this.tvRotationSegments.getSelectionModel().getSelectedItem();

			if (segment == null) {
				return;
			}

			rotationSegmentList.remove(segment);

			// re-order
			for (int i = 0; i < rotationSegmentList.size(); i++) {
				rotationSegmentList.get(i).setSequence(i + 1);
			}

			currentRotationSegment = null;
			tvRotationSegments.getSelectionModel().clearSelection();
			tvRotationSegments.refresh();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// new NonWorkingPeriod button clicked
	@FXML
	private void handleNewNonWorkingPeriod() {
		btAddNonWorkingPeriod.setText("Add");
		this.currentPeriod = null;

		// NonWorkingPeriod editing attributes
		this.tfPeriodName.clear();
		this.tfPeriodDescription.clear();
		this.dpPeriodStartDate.setValue(null);
		this.tfPeriodStartTime.clear();
		this.tfPeriodDuration.clear();

		this.tvNonWorkingPeriods.getSelectionModel().clearSelection();
	}

	// add NonWorkingPeriod button clicked
	@FXML
	private void handleAddNonWorkingPeriod() {
		try {
			// name
			String name = this.tfPeriodName.getText().trim();

			if (name == null || name.length() == 0) {
				throw new Exception("The name of the non-working period must be specified.");
			}

			// description
			String description = this.tfPeriodDescription.getText().trim();

			// start date
			LocalDate startDate = this.dpPeriodStartDate.getValue();

			// start time of day
			String start = this.tfPeriodStartTime.getText().trim();
			LocalTime startTime = this.localTimeFromString(start);

			LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

			// duration
			String hrsMins = this.tfPeriodDuration.getText().trim();

			Duration duration = this.durationFromString(hrsMins);

			if (currentSchedule == null) {
				currentSchedule = setWorkScheduleAttributes();
			}

			if (currentPeriod == null) {
				// new non-working period
				currentPeriod = currentSchedule.createNonWorkingPeriod(name, description, startDateTime, duration);
				periodList.add(currentPeriod);
				Collections.sort(periodList);
			} else {
				currentPeriod.setName(name);
				currentPeriod.setDescription(description);
				currentPeriod.setStartDateTime(startDateTime);
				currentPeriod.setDuration(duration);
			}

			tvNonWorkingPeriods.refresh();
		} catch (Exception e) {
			showErrorDialog(dialogStage, e);
		}
	}

	// remove non-working period button clicked
	@FXML
	private void handleRemoveNonWorkingPeriod() {
		NonWorkingPeriod period = this.tvNonWorkingPeriods.getSelectionModel().getSelectedItem();

		if (period == null) {
			return;
		}

		currentSchedule.getNonWorkingPeriods().remove(period);
		periodList.remove(period);
		Collections.sort(periodList);
		currentPeriod = null;
		tvNonWorkingPeriods.getSelectionModel().clearSelection();
		tvNonWorkingPeriods.refresh();
	}
}
