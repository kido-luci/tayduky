package NotificationPackage;

import java.util.Date;

public class Notification implements Comparable<Notification>{
    private String Notification;
    private Date notificationTime;
    private int ID;

    public Notification(String notification, Date notificationTime, int ID) {
        Notification = notification;
        this.notificationTime = notificationTime;
        this.ID = ID;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public int compareTo(NotificationPackage.Notification o) {
        if(ID == o.getID()){
            return 0;
        }else if(ID > o.getID()){
            return 1;
        }else {
            return -1;
        }
    }
}
