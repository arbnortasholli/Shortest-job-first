package com.example.shortestjobfirst;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;

public class SFJController {
    @FXML
    private TextField arrivalTimeProcess1, burstTimeProcess1;
    @FXML
    private TextField arrivalTimeProcess2, burstTimeProcess2;
    @FXML
    private TextField arrivalTimeProcess3, burstTimeProcess3;
    @FXML
    private TextField arrivalTimeProcess4, burstTimeProcess4;
    @FXML
    private TextField arrivalTimeProcess5, burstTimeProcess5;

    @FXML
    private TableView<Process> Table;
    @FXML
    private TableColumn<Process, Integer> idColumn;
    @FXML
    private TableColumn<Process, Integer> arrivalTimeColumn;
    @FXML
    private TableColumn<Process, Integer> burstTimeColumn;
    @FXML
    private TableColumn<Process, Integer> responseTimeColumn;
    @FXML
    private TableColumn<Process, Integer> completionTimeColumn;
    @FXML
    private TableColumn<Process, Integer> turnaroundTimeColumn;
    @FXML
    private TableColumn<Process, Integer> waitingTimeColumn;

    @FXML
    private Label avgTATLabel;

    @FXML
    private Button addbtn;

    @FXML
    private Label avgWTLabel;

    @FXML
    private Label avgRTLabel;


    private ObservableList<Process> processList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTimeColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        responseTimeColumn.setCellValueFactory(new PropertyValueFactory<>("responseTime"));
        completionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        turnaroundTimeColumn.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        Table.setItems(processList);
        int rowHeight = 28;
        int headerHeight = 26;

        Table.setFixedCellSize(rowHeight);
        Table.setPrefHeight((rowHeight * 6) + headerHeight - 24);
        Table.setMaxHeight((rowHeight * 6) + headerHeight - 22);

        addbtn.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                onAddProcesses();
            }
        });
    }

    @FXML
    public void onAddProcesses() {
        try {
            Process p1 = new Process(
                    Integer.parseInt(arrivalTimeProcess1.getText()),
                    Integer.parseInt(burstTimeProcess1.getText()));
            Process p2 = new Process(
                    Integer.parseInt(arrivalTimeProcess2.getText()),
                    Integer.parseInt(burstTimeProcess2.getText()));
            Process p3 = new Process(
                    Integer.parseInt(arrivalTimeProcess3.getText()),
                    Integer.parseInt(burstTimeProcess3.getText()));
            Process p4 = new Process(
                    Integer.parseInt(arrivalTimeProcess4.getText()),
                    Integer.parseInt(burstTimeProcess4.getText()));
            Process p5 = new Process(
                    Integer.parseInt(arrivalTimeProcess5.getText()),
                    Integer.parseInt(burstTimeProcess5.getText()));

            processList.addAll(p1, p2, p3, p4, p5);

            sortProcessesByBurstTime();

            calculateTimes();
        } catch (NumberFormatException e) {
            showError("Invalid input! Please enter valid numbers.");
        }
    }

    private void sortProcessesByBurstTime() {
        processList.sort(Comparator.comparingInt(Process::getBurstTime));
        // Comparator.comparingInt - krahason vlera numerike nga objektet, krahasohet getBurstTime() i secilit proces
    }   // rendit listen e proceseve processList sipas burstTime ne menyre rritese

    private void calculateTimes() {
        int currentTime = 0;
        for (Process process : processList) {
            if (currentTime < process.getArrivalTime()) { // nese procesi nuk ka ardhur ende koha shtyhet ne arrivalTime
                currentTime = process.getArrivalTime();
            }
            // currentTime mban kohen aktuale te sistemit
            process.setResponseTime(currentTime - process.getArrivalTime()); // koha deri ne fillimin e ekzekutimit

            process.setCompletionTime(currentTime + process.getBurstTime()); // koha kur perfundon procesi

            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime()); // koha totale prej kur arrin deri kur perfundon procesi

            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime()); // koha qe procesi ka pritur ne rradhe

            currentTime = process.getCompletionTime(); // koha perditesohet ne completionTime
        }

        double totalTAT = 0, totalWT = 0, totalRT = 0;
        int size = processList.size();

        for (Process p : processList) {
            totalTAT += p.getTurnaroundTime(); // per secilin proces ne liste, caktohen totalTAT, totalWT, totalRT
            totalWT += p.getWaitingTime();
            totalRT += p.getResponseTime();
        }

        avgTATLabel.setText(String.format("%.2f", totalTAT / size)); // llogariten mesataret per secilen vlere dhe vendosen ne GUI
        avgWTLabel.setText(String.format("%.2f", totalWT / size));
        avgRTLabel.setText(String.format("%.2f", totalRT / size));

    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        // shfaq paralajmerim nese perdoruesi fut te dhena jo te vlefshme psh tekst
    }

    public static class Process {
        private static int idCounter = 1;
        private int id;
        private int arrivalTime;
        private int burstTime;
        private int responseTime;
        private int completionTime;
        private int turnaroundTime;
        private int waitingTime;

        public Process(int arrivalTime, int burstTime) {
            this.id = idCounter++;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.responseTime = burstTime;
            this.completionTime = -1;
            this.turnaroundTime = -1;
            this.waitingTime = -1;
        }

        public int getId() {
            return id;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getBurstTime() {
            return burstTime;
        }

        public int getResponseTime() {
            return responseTime;
        }

        public void setCompletionTime(int completionTime) {
            this.completionTime = completionTime;
        }

        public void setTurnaroundTime(int turnaroundTime) {
            this.turnaroundTime = turnaroundTime;
        }

        public void setWaitingTime(int waitingTime) {
            this.waitingTime = waitingTime;
        }

        public int getCompletionTime() {
            return completionTime;
        }

        public int getTurnaroundTime() {
            return turnaroundTime;
        }

        public int getWaitingTime() {
            return waitingTime;
        }

        public void setResponseTime(int responseTime) {
            this.responseTime = responseTime;
        }
    }

}
