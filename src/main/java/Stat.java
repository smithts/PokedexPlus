public class Stat {
    private String name;
    private int basePower;
    private int effort;
    private String url;

    public Stat(String name, int basePower, int effort, String url) {
        this.name = name;
        this.basePower = basePower;
        this.effort = effort;
        this.url = url;
    }
    public Stat(String name, int basePower, int effort) {
        this(name, basePower, effort, "");
    }

    public String getName() {
        return name;
    }

    public int getBasePower() {
        return basePower;
    }

    public int getEffort() {
        return effort;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return name + ": " + basePower;
    }

    private enum StatName {
        HP,
        ATTACK,
        DEFENSE,
        SPECIAL_ATTACK,
        SPECIAL_DEFENSE,
        SPEED
    }
}
