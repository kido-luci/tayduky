package ChallengesManager;

import FrameTools.jFrameTools;
import NotificationPackage.NotificationGUI;
import connectionPackage.DBConnection;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChallengesManagerGUI extends JFrame{
    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JTable challengesTable;
    private JButton deleteChallengeButton;
    private JButton refreshTableButton;
    private JTextField searchByNameTextField;
    private JTextField nameTextField;
    private JScrollPane textAreaScrollPane;
    private JTextArea descriptionTextArea;
    private JButton addNewButton;
    private JButton saveChangeButton;
    private JButton refreshButton;
    private JTextField locationTextField;
    private JTextField endTimeTextField;
    private JTextField retakesTextField;
    private JTextField startTimeTextField;
    private JButton propsManagerButton;
    private JButton actorsManagerButton;
    private JButton startTimeButton;
    private JButton endTimeButton;
    private ChallengesList challengesList;
    private final JFrame thisFrame = this;
    private String tmpSearchText;

    public ChallengesManagerGUI(JButton jButton, JFrame jFrame){
        super("Challenges Management");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });

        tableScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(28, 78, 99);
            }
        });
        tableScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(28, 78, 99);
            }
        });
        challengesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setUpOnClickTable();
            }
        });
        propsManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                propsManagerButtonOnClick();
            }
        });
        refreshTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCLickRefreshTable();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCLickRefresh();
            }
        });
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickSaveChange();
            }
        });
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickAddNewButton();
            }
        });
        deleteChallengeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickDeleteButton();
            }
        });
        actorsManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickActorManagerButton();
            }
        });
        startTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public void setUpGUI(){
        this.setSize(1100, 700);
        jFrameTools.setDefaultLocation(this);
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        //border
        searchByNameTextField.setBorder(null);
        nameTextField.setBorder(null);
        locationTextField.setBorder(null);
        startTimeTextField.setBorder(null);
        endTimeTextField.setBorder(null);
        retakesTextField.setBorder(null);
        descriptionTextArea.setBorder(null);
        textAreaScrollPane.setBorder(null);

        challengesTable.setDefaultEditor(Object.class, null);
        tableScrollPane.getVerticalScrollBar().setBackground(new Color(1, 39, 68));
        tableScrollPane.getHorizontalScrollBar().setBackground(new Color(1, 39, 68));
        takeChallengesListInDB();
        printChallengesTable(challengesList);

        refreshSearchText();
        checkSearchInRealTime();
    }

    public void takeChallengesListInDB(){
        challengesList = new ChallengesList();
        challengesList.setUpChallengesList();
    }

    public void printChallengesTable(ChallengesList tmpList){
        //disable button on update
        deleteChallengeButton.setEnabled(false);
        propsManagerButton.setEnabled(false);
        saveChangeButton.setEnabled(false);
        actorsManagerButton.setEnabled(false);

        //border
        startTimeButton.setBorder(null);
        endTimeButton.setBorder(null);

        DefaultTableModel defaultTableModel = new DefaultTableModel();

        defaultTableModel.addColumn("ID");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Filming location");
        defaultTableModel.addColumn("Start time");
        defaultTableModel.addColumn("End time");
        defaultTableModel.addColumn("Retakes");

        for(Challenge challenge : tmpList){
            defaultTableModel.addRow(challenge.toArray());
        }

        challengesTable.setModel(defaultTableModel);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel tableColumnModel = challengesTable.getColumnModel();
        tableColumnModel.getColumn(0).setMaxWidth(50);
        tableColumnModel.getColumn(1).setMinWidth(80);
        tableColumnModel.getColumn(2).setMinWidth(80);
        tableColumnModel.getColumn(3).setMinWidth(70);
        tableColumnModel.getColumn(4).setMinWidth(70);
        tableColumnModel.getColumn(5).setMaxWidth(60);

        tableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(3).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(4).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(5).setCellRenderer(defaultTableCellRenderer);

        /*JTableHeader challengesTableTableHeader = challengesTable.getTableHeader();
        challengesTableTableHeader.setBackground(new Color(11, 36, 58));
        challengesTableTableHeader.setForeground(new Color(212, 201, 169));*/
    }

    public void setUpOnClickTable(){
        //button
        deleteChallengeButton.setEnabled(true);
        propsManagerButton.setEnabled(true);
        saveChangeButton.setEnabled(true);
        actorsManagerButton.setEnabled(true);

        int index = challengesTable.getSelectedRow();
        Challenge challenge = null;

        if(index >= 0){
            challenge = searchChallengeByID(challengesTable.getValueAt(index, 0).toString());
        }

        if(challenge != null){
            nameTextField.setText(challenge.getName());
            locationTextField.setText(challenge.getFilmLocation());
            startTimeTextField.setText(challenge.getStartTime().toString());
            endTimeTextField.setText(challenge.getEndTime().toString());
            retakesTextField.setText(challenge.getRetakes());
            descriptionTextArea.setText(challenge.getDescription());
        }
    }

    public Challenge searchChallengeByID(String ID){
        for(Challenge challenge : challengesList){
            if(challenge.getID().equals(ID)){
                return challenge;
            }
        }
        return  null;
    }

    public void propsManagerButtonOnClick(){
        int index = challengesTable.getSelectedRow();
        if(index >= 0){
            String challengeID = challengesTable.getValueAt(index, 0).toString();

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    PropsUsedInChallengeGUI propsUsedInChallengeGUI = new PropsUsedInChallengeGUI(challengeID, thisFrame);
                    propsUsedInChallengeGUI.setVisible(true);
                }
            });
        }
    }

    public void onCLickRefreshTable(){
        challengesList = new ChallengesList();
        challengesList.setUpChallengesList();
        printChallengesTable(challengesList);
        jFrameTools.showOptionPane("Refresh table successfully", "NotificationPackage", mainPanel);
    }

    public void onCLickRefresh(){
        nameTextField.setText("Không có tên");
        locationTextField.setText("Không có địa điểm");
        startTimeTextField.setText("yyyy-MM-dd");
        endTimeTextField.setText("yyyy-MM-dd");
        retakesTextField.setText("1");
        descriptionTextArea.setText("Không có mô tả");
    }

    public void onClickSaveChange(){
        int index = challengesTable.getSelectedRow();
        if(index >= 0){
            String Error = "";

            String ID = challengesTable.getValueAt(index, 0).toString();

            String Name = nameTextField.getText().trim();
            String nowName = challengesTable.getValueAt(index, 1).toString();
            if(Name.length() == 0){
                Error += "Name can't be blank\n";
            }else if(!Name.equals(nowName)){
                for (Challenge challenge : challengesList){
                    if(challenge.getName().equals(Name)){
                        Error += "Name already exists\n";
                        break;
                    }
                }
            }

            String Location = locationTextField.getText().trim();
            if(Location.length() == 0){
                Error += "Location can't be blank\n";
            }

            String Description = descriptionTextArea.getText().trim();
            if(Description.length() == 0){
                Description = "Không có mô tả";
            }

            String Retakes = retakesTextField.getText().trim();
            if(!Retakes.matches("[0-9]+")){
                Error += "Retakes must be a number >= 0\n";
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date StartTime = null;
            try {
                StartTime = dateFormat.parse(startTimeTextField.getText());
            } catch (ParseException e) {
                e.printStackTrace();
                Error += "Invalid start time\n";
            }

            Date EndTime = null;
            try {
                EndTime = dateFormat.parse(endTimeTextField.getText().trim());
            } catch (ParseException e){
                e.printStackTrace();
                Error += "Invalid end time\n";
            }

            if(StartTime.compareTo(EndTime) > 0){
                Error += "Start time have to before end time\n";
            }

            if(Error.length() != 0){
                jFrameTools.showOptionPane(Error, "Error", mainPanel);
            }else {
                Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                String sql = "UPDATE tbl_Challenges SET Name = ?, FilmingLocation = ?, Description = ?, StartTime = ?, EndTime = ?, Retakes = ? WHERE ID = ?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Name);
                    preparedStatement.setString(2, Location);
                    preparedStatement.setString(3, Description);
                    preparedStatement.setDate(4, new java.sql.Date(StartTime.getTime()));
                    preparedStatement.setDate(5, new java.sql.Date(EndTime.getTime()));
                    preparedStatement.setString(6 , Retakes);
                    preparedStatement.setString(7 , ID);

                    preparedStatement.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    jFrameTools.closeConnection(connection);
                    challengesList = new ChallengesList();
                    challengesList.setUpChallengesList();
                    printChallengesTable(challengesList);
                    jFrameTools.showOptionPane("Saved changes", "NotificationPackage", mainPanel);
                }
            }
        }
    }

    public void onClickAddNewButton(){
        String Error = "";

        String Name = nameTextField.getText().trim();

        if(Name.length() == 0){
            Error += "Name can't be blank\n";
        }else {
            for (Challenge challenge : challengesList){
                if(challenge.getName().equals(Name)){
                    Error += "Name already exists\n";
                    break;
                }
            }
        }

        String Location = locationTextField.getText().trim();
        if(Location.length() == 0){
            Error += "Location can't be blank\n";
        }

        String Description = descriptionTextArea.getText().trim();
        if(Description.length() == 0){
            Description = "Không có mô tả";
        }

        String Retakes = retakesTextField.getText().trim();
        if(!Retakes.matches("[0-9]+")){
            Error += "Retakes must be a number >= 0\n";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date StartTime = null;
        try {
            StartTime = dateFormat.parse(startTimeTextField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
            Error += "Invalid start time\n";
        }

        Date EndTime = null;
        try {
            EndTime = dateFormat.parse(endTimeTextField.getText().trim());
        } catch (ParseException e){
            e.printStackTrace();
            Error += "Invalid end time\n";
        }

        if(Error.length() == 0){
            if(StartTime.compareTo(EndTime) > 0){
                Error += "Start time have to before end time\n";
            }
        }

        if(Error.length() != 0){
            jFrameTools.showOptionPane(Error, "Error", mainPanel);
        }else {
            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "INSERT INTO tbl_Challenges Values (?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, new String("00" + (challengesList.getLastID() + 1)));
                preparedStatement.setString(2, Name);
                preparedStatement.setString(3, Description);
                preparedStatement.setString(4, Location);
                preparedStatement.setDate(5, new java.sql.Date(StartTime.getTime()));
                preparedStatement.setDate(6, new java.sql.Date(EndTime.getTime()));
                preparedStatement.setString(7 , Retakes);

                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                jFrameTools.closeConnection(connection);
                challengesList = new ChallengesList();
                challengesList.setUpChallengesList();
                printChallengesTable(challengesList);
                jFrameTools.showOptionPane("Added!", "NotificationPackage", mainPanel);
            }
        }
    }

    public void onClickDeleteButton(){
        int index = challengesTable.getSelectedRow();

        if(index != 0){
            String ID = challengesTable.getValueAt(index, 0).toString();

            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "SELECT PropID, Used FROM tbl_PropsUsedInChallenge WHERE ChallengeID = ?";
            String sql1 = "DELETE FROM tbl_PropsUsedInChallenge WHERE ChallengeID = ?";
            String sql2 = "DELETE FROM tbl_Challenges WHERE ID = ?";
            String sql3 = "DELETE FROM tbl_ActingRoleInChallenge WHERE ChallengeID = ?";
            String sql4 = "SELECT ActorID, ActingRole FROM tbl_ActingRoleInChallenge WHERE ChallengeID = ?";

            try {
                PreparedStatement preparedStatement4 = connection.prepareStatement(sql4);
                preparedStatement4.setString(1, ID);
                ResultSet resultSet1 = preparedStatement4.executeQuery();
                while (resultSet1.next()){
                    NotificationGUI.upNotificationsToDB(resultSet1.getString("ActorID"), "Admin has removed you from acting role(" + resultSet1.getString("ActingRole") + ") in challenge(ID: " + ID + ")");
                }

                PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
                preparedStatement3.setString(1, ID);
                preparedStatement3.execute();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, ID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    String tmpSql = "UPDATE tbl_Props SET Used -= ? WHERE ID = ?";
                    PreparedStatement tmpPreparedStatement = connection.prepareStatement(tmpSql);
                    tmpPreparedStatement.setInt(1, resultSet.getInt("Used"));
                    tmpPreparedStatement.setString(2, resultSet.getString("PropID"));
                    tmpPreparedStatement.execute();
                }
                

                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                preparedStatement1.setString(1, ID);
                preparedStatement1.execute();

                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setString(1, ID);
                preparedStatement2.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                jFrameTools.closeConnection(connection);
                challengesList = new ChallengesList();
                challengesList.setUpChallengesList();
                printChallengesTable(challengesList);
                jFrameTools.showOptionPane("Delete successfully", "NotificationPackage", mainPanel);
            }
        }
    }

    public void checkSearchInRealTime(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            try{
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finally {
                                String searchText = searchByNameTextField.getText().trim().toLowerCase();
                                if(!searchText.equals(tmpSearchText)){
                                    if(searchText.length() != 0){
                                        searchChallengesByName(searchText);
                                    }else {
                                        printChallengesTable(challengesList);
                                    }
                                }
                                tmpSearchText = searchText;
                            }
                        }
                    }
                }).start();
            }
        });
    }

    public void searchChallengesByName(String Name){
        ChallengesList tmpChallengesList = new ChallengesList();

        for(Challenge challenge: challengesList){
            if((challenge.getName().toLowerCase().trim()).matches(".*" + Name.toLowerCase().trim() + ".*")){
                tmpChallengesList.add(challenge);
            }
        }

        printChallengesTable(tmpChallengesList);
    }

    public void refreshSearchText(){
        tmpSearchText = "";
        searchByNameTextField.setText("");
    }

    public void onClickActorManagerButton(){
        int index = challengesTable.getSelectedRow();
        if(index >= 0){
            String ChallengeID = challengesTable.getValueAt(index, 0).toString();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ActingRoleInChallengeGUI actingRoleInChallengeGUI = new ActingRoleInChallengeGUI(actorsManagerButton, thisFrame, ChallengeID);
                    actingRoleInChallengeGUI.setVisible(true);
                }
            });
        }
    }
}
