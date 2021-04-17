package UserManager;

import ChallengesManager.ActingRole;
import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyActingRolesList extends ArrayList<ActingRole> {

    public void setUpList(String ActorID){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "SELECT ChallengeID, ActorName, ActingRole, Description From tbl_ActingRoleInChallenge WHERE ActorID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ActorID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ActingRole actingRole = new ActingRole(
                        resultSet.getString("ActingRole"),
                        ActorID,
                        resultSet.getString("ActorName"),
                        resultSet.getString("Description"),
                        resultSet.getString("ChallengeID")
                );
                this.add(actingRole);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            jFrameTools.closeConnection(connection);
        }
    }
}
