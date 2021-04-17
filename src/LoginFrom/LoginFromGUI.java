package LoginFrom;

import AccountManager.Account;
import AdminManager.AdminGUIJFrame;
import FrameTools.jFrameTools;
import LoadingGUI.LoadingUIJFrame;
import FrameTools.jFrameTools;
import UserManager.UserGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFromGUI extends JFrame{
    private JPanel MainPanel;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JButton loginButton;
    private JPasswordField PasswordTextField;
    private JTextField UserNameTextField;
    private String accountRole = "failed";

    public LoginFromGUI(){
        super("Login Form");
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setIconImage(jFrameTools.takeImage("/image/icon.png"));
        this.setSize(750, 670);
                                                                        jFrameTools.setDefaultLocation(this);
        //button
        UserNameTextField.setBorder(null);
        PasswordTextField.setBorder(null);
        loginButton.setBorder(null);
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon icon = new ImageIcon(jFrameTools.takeImage("buttonLogin_Hover.png"));
                loginButton.setIcon(icon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon icon = new ImageIcon(jFrameTools.takeImage("buttonLogin.png"));
                loginButton.setIcon(icon);
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCLickLogin();
            }
        });
    }

    public Account checkAccount(){
        Connection connection = connectionPackage.DBConnection.getSQLConnection(jFrameTools.sql_url);
        Account account = null;

        String sql ="Select Role, UserName, Password, FullName, PhoneNumber, Email, Image, ID, Description, amountNewNotification From tbl_Account Where UserName = ? and Password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UserNameTextField.getText().trim());
            preparedStatement.setString(2, new String(PasswordTextField.getPassword()).trim());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String role  = resultSet.getString("Role");
                String fullName  = resultSet.getString("FullName");
                String userName = resultSet.getString("UserName");
                String password = resultSet.getString("Password");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                String image = resultSet.getString("Image");
                String id = resultSet.getString("ID");
                String description = resultSet.getString("Description");
                String amountNewNotification = resultSet.getString("amountNewNotification");
                account = new Account(role, fullName, userName, password, phoneNumber, email, image, id, description, amountNewNotification);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            jFrameTools.closeConnection(connection);
        }
        return account;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFromGUI loginFromUI = new LoginFromGUI();
                loginFromUI.setIconImage(jFrameTools.takeImage("icon.png"));
                loginFromUI.setVisible(true);
            }
        });
    }

    public void startAdminJFrameUI(int time, Account account){
        LoadingUIJFrame loadingUIJFrame = new LoadingUIJFrame();
        loadingUIJFrame.setVisible(true);
        AdminGUIJFrame adminGUIJFrame = new AdminGUIJFrame(account);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            loadingUIJFrame.setVisible(false);
                            adminGUIJFrame.setVisible(true);
                            loadingUIJFrame.dispose();
                        }
                    }
                }).start();
            }
        });
    }

    public void startUserJFrameUI(int time, Account account){
        LoadingUIJFrame loadingUIJFrame = new LoadingUIJFrame();
        loadingUIJFrame.setVisible(true);
        UserGUI userGUI = new UserGUI(account);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            loadingUIJFrame.setVisible(false);
                            userGUI.setVisible(true);
                            loadingUIJFrame.dispose();
                        }
                    }
                }).start();
            }
        });
    }

    public void onCLickLogin(){
        String errorText = "";
        if(UserNameTextField.getText().trim().length() == 0){
            errorText += "User name can't be blank! \n";
        }
        if(new String(PasswordTextField.getPassword()).trim().length() == 0){
            errorText += "Password can't be blank! \n";
        }

        if(errorText.length() != 0){
            jFrameTools.showOptionPane(errorText, "Error", MainPanel);
        }else {
            Account account = checkAccount();
            if(account == null){
                jFrameTools.showOptionPane("User name or password incorrect!", "Error", MainPanel);
            }else if(account.getRole().trim().toLowerCase().equals("admin")){
                setVisible(false);
                startAdminJFrameUI(10, account);
                dispose();
            }else if(account.getRole().trim().toLowerCase().equals("user")){
                setVisible(false);
                startUserJFrameUI(10, account);
                dispose();
                this.dispose();
            }
        }
    }

    public String getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }
}
