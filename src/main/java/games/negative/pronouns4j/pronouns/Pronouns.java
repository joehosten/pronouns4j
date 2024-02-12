package games.negative.pronouns4j.pronouns;

public class Pronouns {

    protected String subjective;
    protected String objective;
    protected String possessive;
    protected String reflexive;

    public Pronouns(String subjective, String objective, String possessive, String reflexive) {
        this.subjective = subjective;
        this.objective = objective;
        this.possessive = possessive;
        this.reflexive = reflexive;
    }

    public String getSubjective() {
        return subjective;
    }

    public void setSubjective(String subjective) {
        this.subjective = subjective;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getPossessive() {
        return possessive;
    }

    public void setPossessive(String possessive) {
        this.possessive = possessive;
    }

    public String getReflexive() {
        return reflexive;
    }

    public void setReflexive(String reflexive) {
        this.reflexive = reflexive;
    }
}
