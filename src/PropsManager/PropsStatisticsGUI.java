package PropsManager;

import ChallengesManager.Challenge;
import ChallengesManager.ChallengesList;
import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PropsStatisticsGUI extends JFrame{
    private JPanel descriptionPanel;
    private JPanel mainPanel;
    private JScrollPane textAreaScrollPane;
    private JTextArea statisticsTextArea;

    public PropsStatisticsGUI(JButton jButton, JFrame jFrame){
        super("Notifications");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        //disable
        jButton.setEnabled(false);
        jFrame.setEnabled(false);
        statisticsTextArea.setEditable(false);

        setUpGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jButton.setEnabled(true);
                jFrame.setEnabled(true);
            }
        });
    }

    public void setUpGUI(){
        this.setSize(520, 550);
        jFrameTools.setDefaultLocation(this);
        this.setIconImage(jFrameTools.takeImage("icon.png"));
        textAreaScrollPane.setBorder(null);

        ChallengesList challengesList = new ChallengesList();
        challengesList.setUpChallengesList();
        String Statistics = "-----------------------------------------------------------------------------";
        for(Challenge challenge : challengesList){
            if(challenge != null){
                Statistics += "\nChallenge: " + challenge.getName() + "(" + challenge.getStartTime() + "->" + challenge.getEndTime() + ")";

                Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

                String sql = "SELECT PropName, Used FROM tbl_PropsUsedInChallenge WHERE ChallengeID = ?";

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, challenge.getID());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        Statistics += "\n" + resultSet.getString("PropName") + " - Used: " + resultSet.getString("Used");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                Statistics += "\n-----------------------------------------------------------------------------";
            }
        }
        statisticsTextArea.setText(Statistics);
    }
}
