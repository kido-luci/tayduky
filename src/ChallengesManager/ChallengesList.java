package ChallengesManager;

import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class ChallengesList extends ArrayList<Challenge> {
    private int lastID;

    public void setUpChallengesList(){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "SELECT ID, Name, Description, FilmingLocation, StartTime, EndTime, Retakes FROM tbl_Challenges";

        lastID = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String ID = resultSet.getString("ID");
                String Name = resultSet.getString("Name");
                String Description = resultSet.getString("Description");
                String FilmingLocation = resultSet.getString("FilmingLocation");
                Date StartTime = resultSet.getDate("StartTime");
                Date EndTime = resultSet.getDate("EndTime");
                String Retakes = resultSet.getString("Retakes");
                Challenge challenge = new Challenge(ID, Name, Description, FilmingLocation, StartTime, EndTime, Retakes);
                this.add(challenge);

                int tmp = Integer.parseInt(challenge.getID());
                if(this.getLastID() < tmp){
                    this.setLastID(tmp);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            jFrameTools.closeConnection(connection);
        }
    }

    public int getLastID() {
        return lastID;
    }

    public void setLastID(int lastID) {
        this.lastID = lastID;
    }
}
