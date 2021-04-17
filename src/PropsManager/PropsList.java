package PropsManager;

import FrameTools.jFrameTools;
import connectionPackage.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class PropsList extends ArrayList<Prop> {
    private int lastID;

    public void setUpPropsList(){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "Select ID, Name, Description, Image, Amount, Status , Used From tbl_Props";

        lastID = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String ID = resultSet.getString("ID");
                String Name = resultSet.getString("Name");
                String Description = resultSet.getString("Description");
                String Image = resultSet.getString("Image");
                String Amount = resultSet.getString("Amount");
                String Status = resultSet.getString("Status");
                String Used = resultSet.getString("Used");
                Prop prop = new Prop(ID, Name, Description, Image, Amount, Used, Status);
                this.add(prop);

                int tmp = Integer.parseInt(prop.getID());
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
