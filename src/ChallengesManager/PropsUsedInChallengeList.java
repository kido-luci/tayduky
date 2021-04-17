package ChallengesManager;

import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PropsUsedInChallengeList extends ArrayList<PropUsed> {
    public void setUpPropsList(String ChallengeID){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "Select PropID, PropName, Used From tbl_PropsUsedInChallenge Where ChallengeID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ChallengeID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String ID = resultSet.getString("PropID");
                String Name = resultSet.getString("PropName");
                String Used = resultSet.getString("Used");
                PropUsed propUsed = new PropUsed(ID, Name, Used);
                this.add(propUsed);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            jFrameTools.closeConnection(connection);
        }
    }
}
