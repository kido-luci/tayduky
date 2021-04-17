package AdminManager;

import AccountManager.Account;
import AccountManager.ChangeElementGUI;
import AccountManager.ChangePasswordGUI;
import ActorManager.ActorsManagerGUI;
import ChallengesManager.ChallengesManagerGUI;
import FrameTools.jFrameTools;
import LoadingGUI.LoadingUIJFrame;
import LoginFrom.LoginFromGUI;
import NotificationPackage.NotificationGUI;
import PropsManager.PropsManagerGUI;
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

public class AdminGUIJFrame extends JFrame {

    private JPanel MainPanel;
    private JLabel AvatarImageLabel;
    private JButton actorsButton;
    private JButton challengesButton;
    private JButton propsButton;
    private JButton logOutButton;
    private JLabel adminFullNameLabel;
    private JButton browserButton;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel fullNameLabel;
    private JLabel phoneNumberLabel;
    private JLabel emailLabel;
    private JButton changePasswordButton;
    private JButton changeFullNameButton;
    private JButton changePhoneNumberButton;
    private JButton changeEmailButton;
    private JButton notificationsButton;
    private JLabel idLabel;
    private JLabel iconLabel;
    private JPanel settingPanel;
    private JButton settingButton;
    private JPanel LeftPanel;
    private JButton notifications_2Button;
    private JPanel avatarImageLabel;
    private Account account;
    private final JFrame thisFrame = this;

    public AdminGUIJFrame(Account account){
        super("Admin - " + account.getFullName());
        this.account = account;
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        setGUI();

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutAccount();
            }
        });
        actorsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                actorsButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonActors.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                actorsButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonActors_Hover.png")));
            }
        });
        challengesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                challengesButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonChallenges.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                challengesButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonChallenges_Hover.png")));
            }
        });
        propsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                propsButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonProps.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                propsButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonProps_Hover.png")));
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
        changeFullNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ChangeElementGUI changeFullNameGUI = new ChangeElementGUI(account, fullNameLabel, adminFullNameLabel, changeFullNameButton, thisFrame);
                        changeFullNameGUI.setVisible(true);
                    }
                });
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
        notificationsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                notificationsButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonNotifications.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                notificationsButton.setIcon(new ImageIcon(jFrameTools.takeImage("buttonNotifications_Hover.png")));
            }
        });
        propsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        PropsManagerGUI propGUI = new PropsManagerGUI(propsButton, thisFrame);
                        propGUI.setVisible(true);
                    }
                });
            }
        });
        challengesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ChallengesManagerGUI challengeManagerGUI = new ChallengesManagerGUI(challengesButton, thisFrame);
                        challengeManagerGUI.setVisible(true);
                    }
                });
            }
        });
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingButtonOnClick();
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
        actorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onclickActorsButton();
            }
        });
        notifications_2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickNotificationsButton();
            }
        });
        notificationsButton.addActionListener(new ActionListener() {
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
        notifications_2Button.setBorder(null);
        actorsButton.setBorder(null);
        challengesButton.setBorder(null);
        propsButton.setBorder(null);
        notificationsButton.setBorder(null);
        changeEmailButton.setBorder(null);
        changeFullNameButton.setBorder(null);
        changePasswordButton.setBorder(null);
        changePhoneNumberButton.setBorder(null);
        logOutButton.setBorder(null);
        settingButton.setText("Show setting");
        //avatar
        AvatarImageLabel.setText("");
        ImageIcon avatarImage = new ImageIcon(jFrameTools.takeImage(account.getImage()).getScaledInstance(113, 113, Image.SCALE_SMOOTH));
        AvatarImageLabel.setIcon(avatarImage);
        //label
        adminFullNameLabel.setText("Full name: " + account.getFullName());
        fullNameLabel.setText("Full name: " + account.getFullName());
        userNameLabel.setText("User Name: " + account.getUserName());
        setPasswordLabel(passwordLabel);
        phoneNumberLabel.setText("Phone number: " + account.getPhoneNumber());
        emailLabel.setText("Email: " + account.getEmail());
        idLabel.setText("ID: " + account.getID());
        notifications_2Button.setText("(" + account.getAmountNewNotification() + ")");
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

    public void setPasswordLabel(JLabel jLabel){
        String encodePassword = "";
        for(int i = 0; i < new String(account.getPassword()).length(); i++){
            encodePassword += "*";
        }
        jLabel.setText("Password: " + encodePassword);
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

    public void onclickActorsButton(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ActorsManagerGUI actorsManagerGUI = new ActorsManagerGUI(actorsButton, thisFrame);
                actorsManagerGUI.setVisible(true);
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
                new NotificationGUI(notificationsButton, notifications_2Button, thisFrame, account.getID()).setVisible(true);
            }
        });
    }
}
