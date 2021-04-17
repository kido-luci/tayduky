package NotificationPackage;

import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class NotificationGUI extends JFrame{
    private JPanel descriptionPanel;
    private JScrollPane textAreaScrollPane;
    private JTextArea notificationTextArea;
    private JPanel mainPanel;
    private String accountID;
    private ArrayList<Notification> notificationArrayList;

    public NotificationGUI(JButton jButton1, JButton jButton2, JFrame jFrame, String accountID){
        super("Notifications");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.accountID = accountID;
        notificationArrayList = new ArrayList<Notification>();
        //disable
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jFrame.setEnabled(false);

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton1.setEnabled(true);
                jButton2.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
    }

    public void setUpGUI(){
        this.setSize(550, 400);
        jFrameTools.setDefaultLocation(this);
        this.setIconImage(jFrameTools.takeImage("icon.png"));

        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "SELECT ID, Notification, Time From tbl_Notifications Where AccountID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accountID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                notificationArrayList.add(new Notification(
                        resultSet.getString("Notification"),
                        resultSet.getDate("Time"),
                        resultSet.getInt("ID")
                ));
            }
            if(notificationArrayList.size() > 0){
                Collections.sort(notificationArrayList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            jFrameTools.closeConnection(connection);
        }

        String text = "--------------------------------------------";
        for (Notification notification : notificationArrayList){
            text += "\n" + notification.getNotificationTime();
            text += "\n" + notification.getNotification();
            text += "\n--------------------------------------------";
        }
        notificationTextArea.setText(text);
    }

    public static void upNotificationsToDB(String accountID, String Notifications){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql1 = "SELECT ID FROM tbl_Notifications";
        String sql2 = "INSERT INTO tbl_Notifications VALUES (?, ?, ?, ?)";
        String sql3 = "UPDATE tbl_Account SET amountNewNotification += 1 WHERE ID = ?";
        int tmpID = 0;
        try {
            PreparedStatement preparedStatement1 =  connection.prepareStatement(sql1);
            ResultSet resultSet = preparedStatement1.executeQuery();
            while (resultSet.next()){
                if (tmpID < resultSet.getInt("ID")){
                    tmpID = resultSet.getInt("ID");
                }
            }

            Date today = new Date();
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setString(1, Notifications);
            preparedStatement2.setString(2, accountID);
            preparedStatement2.setDate(3, new java.sql.Date(today.getTime()));
            preparedStatement2.setInt(4, tmpID + 1);
            preparedStatement2.execute();

            PreparedStatement preparedStatement = connection.prepareStatement(sql3);
            preparedStatement.setString(1, accountID);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            jFrameTools.closeConnection(connection);
        }
    }
}
