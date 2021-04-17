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

public class ChangePasswordGUI extends JFrame{
    private JPanel mainPanel;
    private JButton saveChangeButton;
    private JPasswordField currentPasswordText;
    private JPasswordField newPasswordText;
    private JPasswordField verifyPasswordText;
    public Account account;

    public ChangePasswordGUI(Account account, JLabel jLabel, JButton jButton, JFrame jFrame){
        super("Change password");
        this.account = account;
        this.setContentPane(mainPanel);
        this.setIconImage(jFrameTools.takeImage("/image/icon.png"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        jButton.setEnabled(false);
        setGUI();
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String errorText = "";
                String currentPassword = new String(currentPasswordText.getPassword());
                String newPassword = new String(newPasswordText.getPassword());
                String verifyPassword = new String(verifyPasswordText.getPassword());
                if(currentPassword.length() == 0){
                    errorText += "Current password can't be blank! \n";
                }
                if(newPassword.length() == 0){
                    errorText += "New password can't be blank! \n";
                }
                if(verifyPassword.length() == 0){
                    errorText += "Verify password can't be blank! \n";
                }
                //JOptionPane.showMessageDialog(MainPanel, valid, "Valid", 0);
                if(errorText.length() != 0){
                    jFrameTools.showOptionPane(errorText, "Error!", mainPanel);
                }else {
                    if(!(new String(currentPasswordText.getPassword()).equals(account.getPassword()))){
                        errorText = "Incorrect current password!";
                        jFrameTools.showOptionPane(errorText, "Error!", mainPanel);
                    }else {
                        if(newPassword.equals(currentPassword)){
                            errorText = "You used this password recently.\nPlease choose a different one!";
                            jFrameTools.showOptionPane(errorText, "Error!", mainPanel);
                        }else {
                            if(!newPassword.equals(verifyPassword)){
                                errorText = "Verify password don't match!";
                                jFrameTools.showOptionPane(errorText, "Error!", mainPanel);
                            }else  {
                                jFrameTools.showOptionPane("Change password successfully!", "Successfully", mainPanel);
                                updatePassword(newPassword);
                                setPasswordLabel(jLabel);
                                NotificationGUI.upNotificationsToDB("001", "User: " + account.getFullName() + " has changed password to:" + newPassword);

                                jButton.setEnabled(true);
                                jFrame.setEnabled(true);
                                dispose();
                            }
                        }
                    }
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

    private void setGUI(){
        this.setSize(300, 330);
        jFrameTools.setDefaultLocation(this);
        currentPasswordText.setBorder(null);
        newPasswordText.setBorder(null);
        verifyPasswordText.setBorder(null);
    }

    public void updatePassword(String newPassword){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "Update tbl_Account Set Password = ? Where UserName = ? and Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, account.getUserName());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }finally {
            account.setPassword(newPassword);
            jFrameTools.closeConnection(connection);
        }
    }

    public void setPasswordLabel(JLabel jLabel){
        String encodePassword = "";
        for(int i = 0; i < new String(account.getPassword()).length(); i++){
            encodePassword += "*";
        }
        jLabel.setText("Password: " + encodePassword);
    }
}
