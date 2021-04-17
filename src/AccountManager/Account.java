package AccountManager;

public class Account {
    private String Role;
    private String FullName;
    private String UserName;
    private String Password;
    private String PhoneNumber;
    private String Email;
    private String Image;
    private String ID;
    private String Description;
    private String AmountNewNotification;

    public Account(String role, String fullName, String userName, String password, String phoneNumber, String email, String image, String id, String description, String amountNewNotification) {
        Role = role;
        FullName = fullName;
        UserName = userName;
        Password = password;
        PhoneNumber = phoneNumber;
        Email = email;
        Image = image;
        ID = id;
        Description = description;
        AmountNewNotification = amountNewNotification;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAmountNewNotification() {
        return AmountNewNotification;
    }

    public void setAmountNewNotification(String amountNewNotification) {
        AmountNewNotification = amountNewNotification;
    }

    @Override
    public String toString() {
        return "Account{" +
                "Role='" + Role + '\'' +
                ", FullName='" + FullName + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", AvatarLink='" + Image + '\'' +
                '}';
    }

    public Object[] toArray(){return new Object[]{ID, FullName, UserName, Password, PhoneNumber, Email, Image};}
}
