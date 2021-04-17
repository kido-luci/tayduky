package ChallengesManager;

public class ActingRole {
    private String ActingRoleName;
    private String ActorID;
    private String ActorName;
    private String Description;
    private String ChallengeID;

    public ActingRole(String actingRoleName, String actorID, String actorName, String description, String challengeID) {
        ActingRoleName = actingRoleName;
        ActorID = actorID;
        ActorName = actorName;
        Description = description;
        ChallengeID = challengeID;
    }

    public String getActingRoleName() {
        return ActingRoleName;
    }

    public void setActingRoleName(String actingRoleName) {
        ActingRoleName = actingRoleName;
    }

    public String getActorID() {
        return ActorID;
    }

    public void setActorID(String actorID) {
        ActorID = actorID;
    }

    public String getActorName() {
        return ActorName;
    }

    public void setActorName(String actorName) {
        ActorName = actorName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getChallengeID() {
        return ChallengeID;
    }

    public void setChallengeID(String challengeID) {
        ChallengeID = challengeID;
    }

    public Object[] toArray(){return new Object[]{ActingRoleName, ActorID, ActorName};}
}
