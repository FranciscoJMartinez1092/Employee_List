package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import javafx.stage.*;
import javafx.stage.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.scene.control.ListView;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.JAXB;


public class DirectoryController {
	//from scenebuilder
	@FXML
	private TextField nameTextField;
	
	@FXML
	private TextField companyTextField;
	
	@FXML
	private TextField extensionTextField;
	
	@FXML
	private Label fileNameLabel;
	
	@FXML
	private Label recordLabel;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Button exitButton;
	
	@FXML
	private Button nextButton;

	@FXML
	private Button previousButton;

	@FXML
	private Button removeButton;

	@FXML
	private Button serializeButton;
	
	@FXML
	//event when add button is pressed. Enters a new employee and saves it in 
	//the EmployeeList Regex make
	void addButtonPressed(ActionEvent e) {
		nameTextField.setDisable(false);
		companyTextField.setDisable(false);
		extensionTextField.setDisable(false);
		turn++;
		if(place < employeelist.getLst().size()) {
			nextButton.setDisable(false);
		}
		
		
		if(turn > 1) {
		boolean correctName = nameTextField.getText().matches("([A-Z][a-z]{2,}\\s*){1,2}");
		boolean correctCompany = companyTextField.getText().matches("([A-Z]([a-z]*\\d*)*\\s*){1,2}");
		boolean correctExtension = extensionTextField.getText().matches("\\d{1,3}-\\d{1,2}");
		if(!correctName) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Value Error");
			alert.setHeaderText("Valid names consist of one or two words. Each word must start with an uppercase letter followed by at\r\n" + 
					"least two characters. Numbers are not allowed.");
			alert.showAndWait();
		}
		if(!correctCompany) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Value Error");
			alert.setHeaderText("Valid department names consist of one or two words. Each word must start with an uppercase letter. A\r\n" + 
					"word can be a single uppercase letter. Numbers are allowed ");
			alert.showAndWait();	
		}
		if(!correctExtension) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Value Error");
			alert.setHeaderText("Valid extensions start with 1, 2, or 3 numbers followed by a dash \" - \" followed by 1 or 2 numbers");
			alert.showAndWait();	
		}
		if(correctName && correctCompany && correctExtension) {
			if(turn == 2) {
				Employee employee = new Employee(nameTextField.getText(), companyTextField.getText(), extensionTextField.getText());
				employeelist.getLst().add(employee);
			}
			
			if(place == employeelist.getLst().size() && turn != 2) {
			Employee employee = new Employee(nameTextField.getText(), companyTextField.getText(), extensionTextField.getText());
			employeelist.getLst().add(employee);
			}
			
			if(employeelist.getLst().size() > 1) {
				index = employeelist.getLst().size() - 1;
				place = employeelist.getLst().size();
				previousButton.setDisable(false);
				removeButton.setDisable(false);
			}
			recordLabel.setText(place + " of " + employeelist.getLst().size());
			nameTextField.clear();
			companyTextField.clear();
			extensionTextField.clear();
			serializeButton.setDisable(false);
			
		}
		}
		
		
		
	}
	
	
	
	@FXML
	void removeButtonPressed(ActionEvent e) {
		employeelist.getLst().remove(index);
		
		
		index -= 1;
		place -= 1;
		
		if(place == 0) {
			removeButton.setDisable(true);
			recordLabel.setText(place + " of " + employeelist.getLst().size());
			
		}
		else if(place > 0) {
		displayTextFields(index);
		recordLabel.setText(place + " of " + employeelist.getLst().size());
		if(place == 1) {
			previousButton.setDisable(true);
			
		}
		
		if(place == employeelist.getLst().size()) {
			nextButton.setDisable(true);
		}
		}
		nameTextField.clear();
		companyTextField.clear();
		extensionTextField.clear();
	}
	
	@FXML
	 void previousButtonPressed(ActionEvent e) {
		index -= 1;
		place -= 1;
		displayTextFields(index);
		recordLabel.setText(place + " of " + employeelist.getLst().size());
		if(place == 1) {
			previousButton.setDisable(true);
		}
		nextButton.setDisable(false);
	}
	
	@FXML
	void nextButtonPressed(ActionEvent e) {
		index +=1;
		place +=1;
		displayTextFields(index);
		recordLabel.setText(place + " of " + employeelist.getLst().size());
		if(place == employeelist.getLst().size()) {
			nextButton.setDisable(true);
		}
		previousButton.setDisable(false);
		
	}
	@XmlElement(name = "Employee")
	EmployeeList employeelist = new EmployeeList();
	int index = 0;
	int place = index + 1;
	int turn = 0;
	
	
	
	
	@FXML
	void loadButtonPressed(ActionEvent e) {
		
		FileChooser filechooser = new FileChooser();
		File selectedFile = filechooser.showOpenDialog(null);
		
		
			
			BufferedReader input;
			try {
				input = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
			
			EmployeeList employees = JAXB.unmarshal(input, EmployeeList.class);
			employeelist.setLst(employees.getLst());
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Serilization Failed");
			alert.setHeaderText("The file you chose failed to deserialize./n"
					+ "a new list was created.");
			alert.showAndWait();
			EmployeeList employees = new EmployeeList();
			employeelist.setLst(employees.getLst());
		}
		finally {
			if(employeelist.getLst().size() > 0) {
				if(employeelist.getLst().size() > 1) {
					nextButton.setDisable(false);
					}
				turn++;
				recordLabel.setText(place + " of " + employeelist.getLst().size());
				displayTextFields(index);
				removeButton.setDisable(false);
				nameTextField.setDisable(false);
				companyTextField.setDisable(false);
				extensionTextField.setDisable(false);
				
			}
			if(employeelist.getLst().size() == 0) {
			recordLabel.setText(0 + " of " + employeelist.getLst().size());
			}
			fileNameLabel.setText(selectedFile.getName());
			addButton.setDisable(false);
			
			
			
		
			
			
		}
		
		
		
		
	}
	
	@FXML
	void serializeButtonPressed(ActionEvent e) {
		FileChooser filechooser = new FileChooser();
		File selectedFile = filechooser.showOpenDialog(null);
		try(BufferedWriter output = Files.newBufferedWriter(Paths.get(selectedFile.getAbsolutePath()))){
			JAXB.marshal(employeelist, output);
		}
		catch(IOException ioException) {
			System.err.println("Error opening file. Terminating...");
		}
	}
	@FXML
	void exitButtonPressed(ActionEvent e) {
		 Stage stage = (Stage) exitButton.getScene().getWindow();
		 Alert alert = new Alert(AlertType.CONFIRMATION);
		 alert.setTitle("Exit");
		 alert.setHeaderText("Are you sure you want to exit? ");
		 Optional<ButtonType> result =alert.showAndWait();	
		 if(result.get() == ButtonType.OK)
			     //oke button is pressed
		 stage.close(); 
		  	
	}
	
	void displayTextFields(int index) {
		Employee employee = employeelist.getLst().get(index);
		nameTextField.setText(employee.getName());
		companyTextField.setText(employee.getDepartment());
		extensionTextField.setText(employee.getExtension());
	}
	
	public void initialize() {
		
	}
}
