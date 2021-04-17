package AccountManager;

import FrameTools.jFrameTools;
import NotificationPackage.NotificationGUI;
import connectionPackage.DBConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangeElementGUI extends JFrame{

    private JPanel mainPanel;
    private JTextField inputTextField;
    private JButton saveChangeButton;
    private JLabel elementLabel;

    public ChangeElementGUI(Account account, JLabel jLabel1, JLabel jLabel2, JButton jButton, JFrame jFrame){
        super("Change full name");
        this.setContentPane(mainPanel);
        this.setIconImage(jFrameTools.takeImage("/image/icon.png"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        setGUI("Full name", account.getFullName());
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newFullName = inputTextField.getText().trim();
                if(newFullName.length() == 0){
                    jFrameTools.showOptionPane("Full name can be blank!", "Error!", mainPanel);
                }else {
                    jLabel1.setText("Full name: " + newFullName);
                    jLabel2.setText("Full name: " + newFullName);
                    account.setFullName(newFullName);
                    updateAccount(account, "FullName");
                    jFrameTools.showOptionPane("Rename successfully!", "Finished!", mainPanel);
                    jButton.setEnabled(true);
                    jFrame.setEnabled(true);
                    dispose();
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
    }

    public ChangeElementGUI(Account account, JLabel jLabel1, JButton jButton, JFrame jFrame){
        super("Change phone number");
        this.setContentPane(mainPanel);
        this.setIconImage(jFrameTools.takeImage("/image/icon.png"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        jFrame.setEnabled(false);
        jButton.setEnabled(false);
        setGUI("Phone number", account.getPhoneNumber());
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPhoneNumber = inputTextField.getText().trim();
                if(newPhoneNumber.length() == 0){
                    jFrameTools.showOptionPane("Phone number can be blank!", "Error!", mainPanel);
                }else if (!newPhoneNumber.matches("[0-9]+")){
                    jFrameTools.showOptionPane("Phone number must be a number!", "Error!", mainPanel);
                }else {
                    jLabel1.setText("Phone number: " + newPhoneNumber);
                    account.setPhoneNumber(newPhoneNumber);
                    updateAccount(account, "PhoneNumber");
                    jFrameTools.showOptionPane("Change phone number successfully!", "Finished!", mainPanel);
                    jButton.setEnabled(true);
                    jFrame.setEnabled(true);
                    dispose();
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
    }

    public ChangeElementGUI(Account account, JLabel jLabel1, JButton jButton, String title, JFrame jFrame){
        super(title);
        this.setContentPane(mainPanel);
        this.setIconImage(jFrameTools.takeImage("/image/icon.png"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        jFrame.setEnabled(false);
        jButton.setEnabled(false);
        setGUI("Email", account.getEmail());
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newEmail = inputTextField.getText().trim();
                if(newEmail.length() == 0){
                    jFrameTools.showOptionPane("Email can be blank!", "Error!", mainPanel);
                }else if (!newEmail.matches(".+@.+")){
                    jFrameTools.showOptionPane("Invalid email!", "Error!", mainPanel);
                }else {
                    jLabel1.setText("Email: " + newEmail);
                    account.setEmail(newEmail);
                    updateAccount(account, "Email");
                    jFrameTools.showOptionPane("Change email successfully!", "Finished!", mainPanel);
                    jButton.setEnabled(true);
                    jFrame.setEnabled(true);
                    dispose();
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
    }

    public void setGUI(String element, String value){
        this.setSize(300, 230);
        jFrameTools.setDefaultLocation(this);
        inputTextField.setBorder(null);
        inputTextField.setText(value);
        elementLabel.setText(element);
    }

    public void updateAccount(Account account, String fieldName){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String value = "";

        switch (fieldName){
            case "FullName":
                value = account.getFullName();
                NotificationGUI.upNotificationsToDB("001", "User: " + account.getUserName() + " has changed full name to:" + account.getFullName());
                break;
            case "PhoneNumber":
                value = account.getPhoneNumber();
                NotificationGUI.upNotificationsToDB("001", "User: " + account.getUserName() + " has changed phone number to:" + account.getPhoneNumber());
                break;
            case "Email":
                value = account.getEmail();
                NotificationGUI.upNotificationsToDB("001", "User: " + account.getUserName() + " has changed email to:" + account.getEmail());
                break;
        }

        String sql = "Update tbl_Account Set " + fieldName + " = ? Where UserName = ? and Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, account.getUserName());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }finally {
            jFrameTools.closeConnection(connection);
        }
    }
}
