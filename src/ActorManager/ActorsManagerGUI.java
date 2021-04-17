package ActorManager;

import AccountManager.Account;
import FrameTools.jFrameTools;
import LoadingGUI.LoadingUIJFrame;
import NotificationPackage.NotificationGUI;
import connectionPackage.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ActorsManagerGUI extends JFrame{
    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JTable actorsTable;
    private JButton deleteActorButton;
    private JTextField searchByNameTextField;
    private JTextField nameTextField;
    private JButton browserButton;
    private JLabel ImageLabel;
    private JScrollPane textAreaScrollPane;
    private JTextArea descriptionTextArea;
    private JButton refreshButton;
    private JButton removeImageButton;
    private JTextField userNameTextField;
    private JTextField passwordTextField;
    private JTextField phoneNumberTextField;
    private JTextField emailTextField;
    private JButton addNewButton;
    private JButton saveChangeButton;
    private JButton refreshTableButton;
    private JPanel imagePanel;
    private ActorsList actorsList;
    private String tmpImage;
    private String tmpSearchText;

    public ActorsManagerGUI(JButton jButton, JFrame jFrame){
        super("Actor management");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        //disable
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        saveChangeButton.setEnabled(false);
        deleteActorButton.setEnabled(false);

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
        actorsTable.addMouseMotionListener(new MouseMotionAdapter() {
        });
        actorsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickTable();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshEdit();
            }
        });
        removeImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickRemoveImage();
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
        deleteActorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickDeleteButton();
            }
        });
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickSaveChange();
            }
        });
        browserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });
    }

    public void setUpGUI(){
        this.setSize(1250, 730);
        jFrameTools.setDefaultLocation(this);
        this.setIconImage(jFrameTools.takeImage("icon.png"));

        actorsTable.setDefaultEditor(Object.class, null);

        //border
        nameTextField.setBorder(null);
        userNameTextField.setBorder(null);
        passwordTextField.setBorder(null);
        phoneNumberTextField.setBorder(null);
        emailTextField.setBorder(null);
        searchByNameTextField.setBorder(null);
        descriptionTextArea.setBorder(null);
        textAreaScrollPane.setBorder(null);
        tableScrollPane.setBorder(null);

        actorsList = new ActorsList();
        actorsList.setUpActorsList();
        printActorsTable(actorsList);

        tmpImage = "No image";
        refreshSearchText();
        checkSearchInRealTime();
    }

    public void printActorsTable(ActorsList tmpActorsList){
        //disable button on update
        saveChangeButton.setEnabled(false);
        deleteActorButton.setEnabled(false);

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("ID");
        defaultTableModel.addColumn("Full name");
        defaultTableModel.addColumn("User name");
        defaultTableModel.addColumn("Password");
        defaultTableModel.addColumn("Phone number");
        defaultTableModel.addColumn("Email");
        defaultTableModel.addColumn("Image");

        if(tmpActorsList.size() > 0){
            for (Account account: tmpActorsList) {
                defaultTableModel.addRow(account.toArray());
            }
        }

        actorsTable.setModel(defaultTableModel);

        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel actorsTableColumnModel = actorsTable.getColumnModel();
        actorsTableColumnModel.getColumn(0).setMaxWidth(50);
        actorsTableColumnModel.getColumn(1).setMinWidth(70);
        actorsTableColumnModel.getColumn(2).setMinWidth(70);
        actorsTableColumnModel.getColumn(3).setMinWidth(70);
        actorsTableColumnModel.getColumn(4).setMinWidth(70);
        actorsTableColumnModel.getColumn(5).setMinWidth(70);
        actorsTableColumnModel.getColumn(6).setMinWidth(70);

        actorsTableColumnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        actorsTableColumnModel.getColumn(1).setCellRenderer(defaultTableCellRenderer);
        actorsTableColumnModel.getColumn(2).setCellRenderer(defaultTableCellRenderer);
        actorsTableColumnModel.getColumn(3).setCellRenderer(defaultTableCellRenderer);
        actorsTableColumnModel.getColumn(4).setCellRenderer(defaultTableCellRenderer);
        actorsTableColumnModel.getColumn(5).setCellRenderer(defaultTableCellRenderer);
        actorsTableColumnModel.getColumn(6).setCellRenderer(defaultTableCellRenderer);

        /*JTableHeader propsTableHeader = propsTable.getTableHeader();
        propsTableHeader.setBackground(new Color(11, 36, 58));
        propsTableHeader.setForeground(new Color(212, 201, 169));*/
    }

    public void onClickTable(){
        int index = actorsTable.getSelectedRow();
        if(index >= 0){
            String ID = actorsTable.getValueAt(index, 0).toString().trim();
            Account actor = null;
            for(Account tmpAccount : actorsList){
                if(tmpAccount.getID().equals(ID)){
                    actor = tmpAccount;
                    break;
                }
            }

            if(actor != null){
                nameTextField.setText(actor.getFullName());
                userNameTextField.setText(actor.getUserName());
                passwordTextField.setText(actor.getPassword());
                phoneNumberTextField.setText(actor.getPhoneNumber());
                emailTextField.setText(actor.getEmail());
                descriptionTextArea.setText(actor.getDescription());
                if(actor.getImage().equals("No image")){
                    ImageLabel.setText("No image");
                    ImageLabel.setIcon(null);
                }else {
                    ImageLabel.setText("");
                    ImageLabel.setIcon(new ImageIcon(jFrameTools.takeImage(actor.getImage()).getScaledInstance(113, 113, Image.SCALE_SMOOTH)));
                }
                tmpImage = actor.getImage();
            }
        }

        //enable
        saveChangeButton.setEnabled(true);
        deleteActorButton.setEnabled(true);
    }

    public void refreshEdit(){
        nameTextField.setText("");
        userNameTextField.setText("");
        passwordTextField.setText("");
        phoneNumberTextField.setText("");
        emailTextField.setText("");
        descriptionTextArea.setText("");
        ImageLabel.setText("No image");
        ImageLabel.setIcon(null);
        tmpImage = "No image";
    }

    public void onClickRemoveImage(){
        ImageLabel.setText("NoImage");
        ImageLabel.setIcon(null);
        tmpImage = "No image";
    }

    public void refreshTable(){
        actorsList = new ActorsList();
        actorsList.setUpActorsList();
        printActorsTable(actorsList);
    }

    public void onClickRefreshTable(){
        refreshTable();
        refreshSearchText();
        jFrameTools.showOptionPane("Refresh table successfully", "NotificationPackage", mainPanel);
    }

    public void onClickAddNew(){
        String Error = "";
        String FullName = nameTextField.getText().trim();
        String UserName = userNameTextField.getText().trim();
        String Password = passwordTextField.getText().trim();
        String PhoneNumber = phoneNumberTextField.getText().trim();
        String Email = emailTextField.getText();
        String Description = descriptionTextArea.getText().trim();

        if(FullName.length() == 0){
            Error += "Full name can't be blank\n";
        }

        if(UserName.length() == 0){
            Error += "User name can't be blank\n";
        }else {
            for(Account account : actorsList){
                if(account.getUserName().equals(UserName)){
                    Error += "User name already exists\n";
                    break;
                }
            }
        }

        if(Password.length() == 0){
            Error += "Password can't be blank\n";
        }else if(Password.equals(UserName)){
            Error += "Password can't be the same with user name\n";
        }

        if(!PhoneNumber.matches("[0-9]+")){
            Error += "Invalid phone number\n";
        }

        if(!Email.matches(".+@.+")){
            Error += "Invalid email\n";
        }

        if(Description.length() == 0){
            Description = "Không có mô tả";
        }

        if(Error.length() != 0){
            jFrameTools.showOptionPane(Error, "Error", mainPanel);
        }else {
            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "INSERT INTO tbl_Account VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, UserName);
                preparedStatement.setString(2, Password);
                preparedStatement.setString(3, "user");
                preparedStatement.setString(4, FullName);
                preparedStatement.setString(5, PhoneNumber);
                preparedStatement.setString(6, Email);
                preparedStatement.setString(7, tmpImage);
                preparedStatement.setString(8, new String("00" + (actorsList.getLastID() + 1)));
                preparedStatement.setString(9, Description);
                preparedStatement.setInt(10, 1);
                preparedStatement.execute();

                NotificationGUI.upNotificationsToDB(new String("00" + (actorsList.getLastID() + 1)), "You were created by the admin");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
              jFrameTools.closeConnection(connection);
              refreshTable();
              refreshSearchText();
              jFrameTools.showOptionPane("Added!", "NotificationPackage", mainPanel);
            }
        }
    }

    public void onClickDeleteButton(){
        int index = actorsTable.getSelectedRow();
        if(index >= 0){
            String ID = actorsTable.getValueAt(index, 0).toString();

            Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

            String sql = "DELETE tbl_Account WHERE ID = ?";
            String sql1 = "DELETE tbl_Notifications WHERE AccountID = ?";
            String sql2 = "DELETE tbl_ActingRoleInChallenge WHERE ActorID = ?";

            try {
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setString(1, ID);
                preparedStatement2.execute();

                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                preparedStatement1.setString(1, ID);
                preparedStatement1.execute();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, ID);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                jFrameTools.closeConnection(connection);
                refreshTable();
                refreshSearchText();
                jFrameTools.showOptionPane("Delete successfully", "NotificationPackage", mainPanel);
            }
        }
    }

    public void onClickSaveChange(){
        int index = actorsTable.getSelectedRow();
        if(index >= 0){
            String ID = actorsTable.getValueAt(index, 0).toString();
            String Error = "";
            String FullName = nameTextField.getText().trim();
            String UserName = userNameTextField.getText().trim();
            String Password = passwordTextField.getText().trim();
            String PhoneNumber = phoneNumberTextField.getText().trim();
            String Email = emailTextField.getText();
            String Description = descriptionTextArea.getText().trim();

            if(FullName.length() == 0){
                Error += "Full name can't be blank\n";
            }

            if(UserName.length() == 0){
                Error += "User name can't be blank\n";
            }else if(!UserName.equals(actorsTable.getValueAt(index, 2))){
                Error += "User name doesn't allow to change\n";
            }

            if(Password.length() == 0){
                Error += "Password can't be blank\n";
            }else if(Password.equals(UserName)){
                Error += "Password can't be the same with user name\n";
            }

            if(!PhoneNumber.matches("[0-9]+")){
                Error += "Invalid phone number\n";
            }

            if(!Email.matches(".+@.+")){
                Error += "Invalid email\n";
            }

            if(Description.length() == 0){
                Description = "Không có mô tả";
            }

            if(Error.length() != 0){
                jFrameTools.showOptionPane(Error, "Error", mainPanel);
            }else {
                Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                String sql = "UPDATE tbl_Account SET Password = ?, FullName = ?, PhoneNumber = ?, Email = ?, Image = ?, Description = ? WHERE ID = ?";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Password);
                    preparedStatement.setString(2, FullName);
                    preparedStatement.setString(3, PhoneNumber);
                    preparedStatement.setString(4, Email);
                    preparedStatement.setString(5, tmpImage);
                    preparedStatement.setString(6, Description);
                    preparedStatement.setString(7, ID);

                    preparedStatement.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }finally {
                    jFrameTools.closeConnection(connection);
                    refreshTable();
                    refreshSearchText();
                    jFrameTools.showOptionPane("Saved changes!", "NotificationPackage", mainPanel);
                    NotificationGUI.upNotificationsToDB(ID, "The administrator has updated your information:"
                        + "\nFull name: " + FullName + "\nPassword: " + Password + "\nPhone number: " + PhoneNumber + "\nEmail: " + Email + "\nAvatar image: " + tmpImage + "\nDescription: " + Description
                    );
                }
            }
        }
    }

    public void selectImage(){
        File file = jFrameTools.choseFile(this);
        if(file != null){
            String copyFilePath = System.getProperty("user.dir") + "\\src\\image\\" + file.getName();
            System.out.println(copyFilePath);
            File fileCopy = new File(copyFilePath);

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setVisible(false);
                            LoadingUIJFrame loadingUIJFrame = new LoadingUIJFrame();
                            loadingUIJFrame.setVisible(true);
                            tmpImage = file.getName();

                            try{
                                if(!fileCopy.exists()){
                                    jFrameTools.copyFile(file, fileCopy.getAbsolutePath());
                                }
                                Thread.sleep(3000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                ImageIcon avatarImage = new ImageIcon(jFrameTools.takeImage(file.getName()).getScaledInstance(113, 113, Image.SCALE_SMOOTH));
                                ImageLabel.setText("");
                                tmpImage = file.getName();
                                ImageLabel.setIcon(avatarImage);
                                loadingUIJFrame.setVisible(false);
                                setVisible(true);
                                loadingUIJFrame.dispose();
                            }
                        }
                    }).start();
                }
            });
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
                                        searchActorsByName(searchText);
                                    }else {
                                        printActorsTable(actorsList);
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

    public void refreshSearchText(){
        tmpSearchText = "";
        searchByNameTextField.setText("");
    }

    public void searchActorsByName(String Name){
        ActorsList tmpActorsList = new ActorsList();

        for(Account account : actorsList){
            if((account.getFullName().toLowerCase().trim()).matches(".*" + Name.toLowerCase().trim() + ".*")){
                tmpActorsList.add(account);
            }
        }

        printActorsTable(tmpActorsList);
    }
}
