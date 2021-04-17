package UserManager;

import AccountManager.Account;
import AccountManager.ChangeElementGUI;
import AccountManager.ChangePasswordGUI;
import FrameTools.jFrameTools;
import LoadingGUI.LoadingUIJFrame;
import LoginFrom.LoginFromGUI;
import NotificationPackage.NotificationGUI;
import connectionPackage.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserGUI extends JFrame{
    private JLabel AvatarImageLabel;
    private JLabel userFullNameLabel;
    private JButton settingButton;
    private JButton logOutButton;
    private JButton browserButton;
    private JButton myActingRoleButton;
    private JButton notificationButton;
    private JPanel settingPanel;
    private JLabel iconLabel;
    private JLabel idLabel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton changePasswordButton;
    private JLabel fullNameLabel;
    private JButton changeFullNameButton;
    private JLabel phoneNumberLabel;
    private JButton changePhoneNumberButton;
    private JLabel emailLabel;
    private JButton changeEmailButton;
    private JPanel MainPanel;
    private JPanel LeftPanel;
    private JButton ChallengeFilmedButton;
    private JButton challengeUpcoming;
    private JButton notifications_2Button;
    private Account account;
    private JFrame thisFrame = this;

    public UserGUI(Account account){
        super("User - " + account.getFullName());
        this.account = account;
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        setGUI();
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingButtonOnClick();
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutAccount();
            }
        });
        logOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logOutButton.setIcon(new ImageIcon(jFrameTools.takeImage("icons8_sign_out_20px_1.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logOutButton.setIcon(new ImageIcon(jFrameTools.takeImage("icons8_sign_out_20px.png")));
            }
        });
        browserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordButtonOnClick();
            }
        });
        changePasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changePasswordButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changePasswordButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance_Hover.png")));
            }
        });
        changeFullNameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changeFullNameButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeFullNameButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance_Hover.png")));
            }
        });
        changeFullNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ChangeElementGUI changeFullNameGUI = new ChangeElementGUI(account, fullNameLabel, userNameLabel, changeFullNameButton, thisFrame);
                        changeFullNameGUI.setVisible(true);
                    }
                });
            }
        });
        changePhoneNumberButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changePhoneNumberButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changePhoneNumberButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance_Hover.png")));
            }
        });
        changePhoneNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ChangeElementGUI changeElementGUI = new ChangeElementGUI(account, phoneNumberLabel, changePhoneNumberButton, thisFrame);
                        changeElementGUI.setVisible(true);
                    }
                });
            }
        });
        changeEmailButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changeEmailButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeEmailButton.setIcon(new ImageIcon(jFrameTools.takeImage("iconMaintenance_Hover.png")));
            }
        });
        changeEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ChangeElementGUI changeElementGUI = new ChangeElementGUI(account, emailLabel, changeEmailButton, "Change email", thisFrame);
                        changeElementGUI.setVisible(true);
                    }
                });
            }
        });
        myActingRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickMyActingRoleButton();
            }
        });
        ChallengeFilmedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickFilmedButton();
            }
        });
        challengeUpcoming.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickUpComingButton();
            }
        });
        notifications_2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickNotificationsButton();
            }
        });
        notificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickNotificationsButton();
            }
        });
    }

    public void setGUI(){
        MainPanel.setBackground(new Color(1, 21, 39));
        this.setSize(750, 670);
        jFrameTools.setDefaultLocation(this);
        settingPanel.setVisible(false);
        //button

        changeEmailButton.setBorder(null);
        changeFullNameButton.setBorder(null);
        changePasswordButton.setBorder(null);
        changePhoneNumberButton.setBorder(null);
        logOutButton.setBorder(null);
        settingButton.setText("Show setting");
        //avatar
        if(!account.getImage().equals("No image")){
            AvatarImageLabel.setText("");
            ImageIcon avatarImage = new ImageIcon(jFrameTools.takeImage(account.getImage()).getScaledInstance(113, 113, Image.SCALE_SMOOTH));
            AvatarImageLabel.setIcon(avatarImage);
        }else {
            AvatarImageLabel.setText("No image");
        }
        //label
        userFullNameLabel.setText(account.getFullName());
        fullNameLabel.setText("Full name: " + account.getFullName());
        userNameLabel.setText("User Name: " + account.getUserName());
        setPasswordLabel(passwordLabel);
        phoneNumberLabel.setText("Phone number: " + account.getPhoneNumber());
        emailLabel.setText("Email: " + account.getEmail());
        idLabel.setText("ID: " + account.getID());
        notifications_2Button.setBorder(null);
        notifications_2Button.setText("(" + account.getAmountNewNotification() + ")");
    }

    public void setPasswordLabel(JLabel jLabel){
        String encodePassword = "";
        for(int i = 0; i < new String(account.getPassword()).length(); i++){
            encodePassword += "*";
        }
        jLabel.setText("Password: " + encodePassword);
    }

    public void settingButtonOnClick(){
        if(settingButton.getText().equals("Show setting")){
            settingButton.setText("Hide setting");
            this.setSize(750, 670);
            MainPanel.setBackground(new Color(1, 34, 61));
            LeftPanel.setVisible(false);
            settingPanel.setVisible(true);
            jFrameTools.setDefaultLocation(this);
        }else {
            settingButton.setText("Show setting");
            this.setSize(750, 670);
            MainPanel.setBackground(new Color(1, 21, 39));
            LeftPanel.setVisible(true);
            settingPanel.setVisible(false);
            jFrameTools.setDefaultLocation(this);
        }
    }

    public void logOutAccount(){
        this.setVisible(false);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoginFromGUI login = new LoginFromGUI();
                        LoadingUIJFrame loadingUIJFrame = new LoadingUIJFrame();
                        try {
                            loadingUIJFrame.setVisible(true);
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            loadingUIJFrame.setVisible(false);
                            login.setVisible(true);
                        }
                    }
                }).start();
            }
        });
        this.dispose();
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
                            account.setImage(file.getName());
                            saveImagePathToDB();
                            NotificationGUI.upNotificationsToDB("001", "User: " + account.getUserName() + " has changed avatar image(path:" + copyFilePath + ")");

                            try{
                                if(!fileCopy.exists()){
                                    jFrameTools.copyFile(file, fileCopy.getAbsolutePath());
                                }
                                Thread.sleep(3000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                ImageIcon avatarImage = new ImageIcon(jFrameTools.takeImage(file.getName()).getScaledInstance(113, 113, Image.SCALE_SMOOTH));
                                AvatarImageLabel.setIcon(avatarImage);
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

    public void saveImagePathToDB(){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "Update tbl_Account Set Image = ? Where UserName = ? and Password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, account.getImage());
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

    public void changePasswordButtonOnClick(){
        this.setEnabled(false);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChangePasswordGUI changePasswordGUI = new ChangePasswordGUI(account, passwordLabel, changePasswordButton, thisFrame);
                changePasswordGUI.setVisible(true);
            }
        });
    }

    public void onClickMyActingRoleButton(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MyActingRoleGUI myActingRoleGUI = new MyActingRoleGUI(myActingRoleButton, thisFrame, account.getID());
                myActingRoleGUI.setVisible(true);
            }
        });
    }

    public void onClickFilmedButton(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MyActingRoleGUI myActingRoleGUI = new MyActingRoleGUI(myActingRoleButton, thisFrame, account.getID(), "Filmed Challenge");
                myActingRoleGUI.setVisible(true);
            }
        });
    }

    public void onClickUpComingButton(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MyActingRoleGUI myActingRoleGUI = new MyActingRoleGUI(myActingRoleButton, thisFrame, account.getID(), "Upcoming Challenge");
                myActingRoleGUI.setVisible(true);
            }
        });
    }

    public void onClickNotificationsButton(){
        notifications_2Button.setText("(0)");
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);
        String sql = "UPDATE tbl_Account SET amountNewNotification = 0 WHERE ID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getID());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            jFrameTools.closeConnection(connection);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NotificationGUI(notificationButton, notifications_2Button, thisFrame, account.getID()).setVisible(true);
            }
        });
    }
}
