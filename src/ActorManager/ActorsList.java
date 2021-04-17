package ActorManager;

import AccountManager.Account;
import FrameTools.jFrameTools;
import PropsManager.Prop;
import connectionPackage.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class ActorsList extends ArrayList<Account> {
    private int lastID;

    public void setUpActorsList(){
        Connection connection = DBConnection.getSQLConnection(jFrameTools.sql_url);

        String sql = "SELECT ID, FullName, Description, Image, Email, PhoneNumber, UserName, Password, amountNewNotification FROM tbl_Account WHERE Role = ?";

        lastID = 1;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "user");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String ID = resultSet.getString("ID");
                String Name = resultSet.getString("FullName");
                String Description = resultSet.getString("Description");
                String UserName = resultSet.getString("UserName");
                String Password = resultSet.getString("Password");
                String PhoneNumber = resultSet.getString("PhoneNumber");
                String Email = resultSet.getString("Email");
                String Image = resultSet.getString("Image");
                String AmountNewNotification = resultSet.getString("amountNewNotification");
                Account account = new Account("user", Name, UserName, Password, PhoneNumber, Email , Image, ID, Description, AmountNewNotification);
                this.add(account);

                int tmp = Integer.parseInt(account.getID());
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
