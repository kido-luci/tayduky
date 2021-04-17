package PropsManager;

public class Prop {
    private String ID;
    private String Name;
    private String Description;
    private String Image;
    private String Amount;
    private String Used;
    private String Status;

    public Prop(String ID, String name, String description, String image, String amount, String used, String status) {
        this.ID = ID;
        Name = name;
        Description = description;
        Image = image;
        Amount = amount;
        Status = status;
        this.Used = used;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUsed() {
        return Used;
    }

    public void setUsed(String used) {
        Used = used;
    }

    public Object[] toArray(){return new Object[]{ID, Name, Amount, Used, Status, Image};};
}
