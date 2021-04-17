package ChallengesManager;

public class PropUsed {
    private String ID;
    private String Name;
    private String Used;

    public PropUsed(String ID, String name, String used) {
        this.ID = ID;
        Name = name;
        Used = used;
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

    public String getUsed() {
        return Used;
    }

    public void setUsed(String used) {
        Used = used;
    }

    public Object[] toArray() {return new Object[]{ID, Name, Used};}
}
