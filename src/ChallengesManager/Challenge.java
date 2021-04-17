package ChallengesManager;

import java.util.Date;

public class Challenge {
    private String ID;
    private String Name;
    private  String Description;
    private String FilmingLocation;
    private Date StartTime;
    private Date EndTime;
    private String Retakes;

    public Challenge(String ID, String name, String description, String filmLocation, Date startTime, Date endTime, String retakes) {
        this.ID = ID;
        Name = name;
        Description = description;
        FilmingLocation = filmLocation;
        StartTime = startTime;
        EndTime = endTime;
        Retakes = retakes;
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

    public String getFilmLocation() {
        return FilmingLocation;
    }

    public void setFilmLocation(String filmingLocation) {
        FilmingLocation = filmingLocation;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public String getRetakes() {
        return Retakes;
    }

    public void setRetakes(String retakes) {
        Retakes = retakes;
    }

    public Object[] toArray(){
        return new Object[]{ID, Name, FilmingLocation, StartTime, EndTime, Retakes};
    }
}
