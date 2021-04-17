package ChallengesManager;

import AccountManager.Account;
import ActorManager.ActorsList;
import FrameTools.jFrameTools;
import NotificationPackage.NotificationGUI;
import connectionPackage.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActingRoleInChallengeGUI extends JFrame{
    private JLabel titleLabel;
    private JScrollPane tableScrollPane;
    private JTable actingRoleTable;
    private JButton addNewButton;
    private JPanel mainPanel;
    private JButton saveChangeButton;
    private JButton deleteActorButton;
    private JTextField actingRoleTextField;
    private JScrollPane textAreaScrollPane;
    private JTextArea descriptionTextArea;
    private JPanel p;
    private JTextField actorNameTextField;
    private JButton refreshTableButton;
    private String ChallengeID;
    private ActingRoleInChallengeList actingRoleInChallengeList;
    private ActorsList actorList;

    public ActingRoleInChallengeGUI(JButton jButton, JFrame jFrame, String ChallengeID){
        super("Acting Role in Challenge");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        //disable
        jButton.setEnabled(false);
        jFrame.setEnabled(false);

        this.ChallengeID = ChallengeID;
        actorList = new ActorsList();
        actorList.setUpActorsList();

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
        actingRoleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickTable();
            }
        });
        refreshTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickRefreshTable();
            }
        });
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickAddNew();
            }
        });
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickSaveChange();
            }
        });
        deleteActorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickDelete();
            }
        });
    }

    public void setUpGUI(){
        this.setSize(450, 720);
        jFrameTools.setDefaultLocation(this);
        this.setIconImage(jFrameTools.takeImage("icon.png"));

        //Border
        tableScrollPane.setBorder(null);
        textAreaScrollPane.setBorder(null);
        actingRoleTextField.setBorder(null);
        actorNameTextField.setBorder(null);

        actingRoleTable.setDefaultEditor(Object.class, null);

        actingRoleInChallengeList = new ActingRoleInChallengeList();
        actingRoleInChallengeList.setUpList(ChallengeID);
        printActingRolesTable(actingRoleInChallengeList);
    }

    public void printActingRolesTable(ActingRoleInChallengeList tmpList){
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Acting Role");
        defaultTableModel.addColumn("Actor ID");
        defaultTableModel.addColumn("Actor Name");

        for(ActingRole actingRole : tmpList){
            defaultTableModel.addRow(actingRole.toArray());
        }

        actingRoleTable.setModel(defaultTableModel);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel tableColumnModel = actingRoleTable.getColumnModel();
        tableColumnModel.getColumn(0).setMinWidth(80);
        tableColumnModel.getColumn(1).setMaxWidth(60);
        tableColumnModel.getColumn(2).setMinWidth(80);


        tableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        tableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);

        //disable
        deleteActorButton.setEnabled(false);
        saveChangeButton.setEnabled(false);
    }

    public void onClickTable(){
        int index = actingRoleTable.getSelectedRow();
        if(index >= 0){
            String tmpActingRoleName = actingRoleTable.getValueAt(index, 0).toString();
            ActingRole tmpActingRole = null;
            for(ActingRole actingRole : actingRoleInChallengeList){
                if(actingRole.getActingRoleName().equals(tmpActingRoleName)){
                    tmpActingRole = actingRole;
                    break;
                }
            }
            if(tmpActingRole != null){
                actingRoleTextField.setText(tmpActingRole.getActingRoleName());
                actorNameTextField.setText(tmpActingRole.getActorName());
                descriptionTextArea.setText(tmpActingRole.getDescription());
            }

            //enable
            deleteActorButton.setEnabled(true);
            saveChangeButton.setEnabled(true);
        }
    }

    public void onClickRefreshTable(){
        actingRoleTextField.setText("");
        actorNameTextField.setText("");
        descriptionTextArea.setText("Không có mô tả");
        actingRoleInChallengeList = new ActingRoleInChallengeList();
        actingRoleInChallengeList.setUpList(ChallengeID);
        printActingRolesTable(actingRoleInChallengeList);
        jFrameTools.showOptionPane("Refresh table successfully", "NotificationPackage", mainPanel);
    }

    public void onClickAddNew(){
        String Error = "";
        String ActorName = actorNameTextField.getText().trim();
        String ActingRole = actingRoleTextField.getText().trim();
        String Description = descriptionTextArea.getText().trim();
        Account tmpActor = null;

        if(ActorName.length() == 0){
            Error += "Actor name can't be blank\n";
        }else {
            for(Account actor : actorList){
                if(actor.getFullName().toLowerCase().equals(ActorName.toLowerCase())){
                    tmpActor = actor;
                    break;
                }
            }
            if(tmpActor == null){
                Error += "Actor name doesn't exists\n";
            }
        }

        if(ActingRole.length() == 0){
            Error += "Acting Role can't be blank\n";
        }else {
            for(ActingRole actingRole : actingRoleInChallengeList){
                if(actingRole.getActingRoleName().toLowerCase().equals(ActingRole.toLowerCase())){
                    Error += "Acting Role have been exists\n";
                    break;
                }
            }
        }

        if(Description.length() == 0){
            Description = "Không có mô tả";
        }

        if(Error.length() != 0){
            jFrameTools.showOptionPane(Error, "Error", mainPanel);
        }else {
            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "INSERT INTO tbl_ActingRoleInChallenge VALUES(?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, tmpActor.getID());
                preparedStatement.setString(2, tmpActor.getFullName());
                preparedStatement.setString(3, ActingRole);
                preparedStatement.setString(4, Description);
                preparedStatement.setString(5, ChallengeID);

                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                jFrameTools.closeConnection(connection);
                actingRoleInChallengeList = new ActingRoleInChallengeList();
                actingRoleInChallengeList.setUpList(ChallengeID);
                printActingRolesTable(actingRoleInChallengeList);
                jFrameTools.showOptionPane("Added!", "NotificationPackage", mainPanel);
                NotificationGUI.upNotificationsToDB(tmpActor.getID(), "Admin has added you to acting role(" + ActingRole + ") in challenge(ID: " + ChallengeID + ")");
            }
        }
    }

    public void onClickSaveChange(){
        int index = actingRoleTable.getSelectedRow();
        if(index >= 0){
            String Error = "";
            String ActorName = actorNameTextField.getText().trim();
            String ActingRole = actingRoleTextField.getText().trim();
            String Description = descriptionTextArea.getText().trim();
            String tmpActingRole = actingRoleTable.getValueAt(index,0).toString();
            Account tmpActor = null;

            if(ActorName.length() == 0){
                Error += "Actor name can't be blank\n";
            }else {
                for(Account actor : actorList){
                    if(actor.getFullName().toLowerCase().equals(ActorName.toLowerCase())){
                        tmpActor = actor;
                        break;
                    }
                }
                if(tmpActor == null){
                    Error += "Actor name doesn't exists\n";
                }
            }

            if(ActingRole.length() == 0){
                Error += "Acting Role can't be blank\n";
            }else if(!ActingRole.toLowerCase().equals(tmpActingRole.toLowerCase())){
                for(ActingRole actingRole : actingRoleInChallengeList){
                    if(actingRole.getActingRoleName().toLowerCase().equals(ActingRole.toLowerCase())){
                        Error += "Acting Role have been exists\n";
                        break;
                    }
                }
            }

            if(Description.length() == 0){
                Description = "Không có mô tả";
            }

            if(Error.length() != 0){
                jFrameTools.showOptionPane(Error, "Error", mainPanel);
            }else {
                Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                String sql = "UPDATE tbl_ActingRoleInChallenge SET ActorID = ?, ActorName = ?, ActingRole = ?, Description = ? WHERE ActingRole = ?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, tmpActor.getID());
                    preparedStatement.setString(2, tmpActor.getFullName());
                    preparedStatement.setString(3, ActingRole);
                    preparedStatement.setString(4, Description);
                    preparedStatement.setString(5, tmpActingRole);

                    preparedStatement.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }finally {
                    jFrameTools.closeConnection(connection);
                    actingRoleInChallengeList = new ActingRoleInChallengeList();
                    actingRoleInChallengeList.setUpList(ChallengeID);
                    printActingRolesTable(actingRoleInChallengeList);
                    jFrameTools.showOptionPane("Saved changes!", "NotificationPackage", mainPanel);
                    String selectID = actingRoleTable.getValueAt(index, 1).toString();
                    System.out.println(selectID + "--" + tmpActor.getID());
                    if(selectID.equals(tmpActor.getID())){
                        NotificationGUI.upNotificationsToDB(selectID, "Admin has changed your acting role(" + ActingRole + ") in challenge(ID: " + ChallengeID + ")");
                    }else {
                        NotificationGUI.upNotificationsToDB(selectID, "Admin has removed you from acting role(" + ActingRole + ") in challenge(ID: " + ChallengeID + ")");
                        NotificationGUI.upNotificationsToDB(tmpActor.getID(), "Admin has added you to acting role(" + ActingRole + ") in challenge(ID: " + ChallengeID + ")");
                    }
                }
            }
        }
    }

    public void onClickDelete(){
        int index = actingRoleTable.getSelectedRow();
        if(index >= 0){
            String tmpActingRoleName = actingRoleTable.getValueAt(index, 0).toString();
            String tmpID = actingRoleTable.getValueAt(index, 1).toString();

            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "Delete tbl_ActingRoleInChallenge WHERE ActingRole = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, tmpActingRoleName);

                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                jFrameTools.closeConnection(connection);
                actingRoleInChallengeList = new ActingRoleInChallengeList();
                actingRoleInChallengeList.setUpList(ChallengeID);
                printActingRolesTable(actingRoleInChallengeList);
                jFrameTools.showOptionPane("Delete successfully!", "NotificationPackage", mainPanel);
                NotificationGUI.upNotificationsToDB(tmpID, "Admin has removed you from acting role(" + tmpActingRoleName + ") in challenge(ID: " + ChallengeID + ")");
            }
        }
    }
}
