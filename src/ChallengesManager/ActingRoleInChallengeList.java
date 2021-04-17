package ChallengesManager;

import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActingRoleInChallengeList extends ArrayList<ActingRole> {

    public void setUpList(String ChallengeID){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "SELECT ActorID, ActorName, ActingRole, Description From tbl_ActingRoleInChallenge WHERE ChallengeID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ChallengeID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ActingRole actingRole = new ActingRole(
                        resultSet.getString("ActingRole"),
                        resultSet.getString("ActorID"),
                        resultSet.getString("ActorName"),
                        resultSet.getString("Description"),
                        ChallengeID
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
