public class Country {
    public final String name;
    public final String region;
    public final int happinessRank;
    public final double happinessScore;
    public final double standardError;
    public final double economy;
    public final double family;
    public final double health;
    public final double freedom;
    public final double trust;
    public final double generosity;
    public final double dystopiaResidual;



    public Country(String name, String region, int happinessRank, double happinessScore, double standardError, double economy, double family, double health, double freedom, double trust, double generosity, double dystopiaResidual) {
        this.name = name;
        this.region = region;
        this.happinessRank = happinessRank;
        this.happinessScore = happinessScore;
        this.standardError = standardError;
        this.economy = economy;
        this.family = family;
        this.health = health;
        this.freedom = freedom;
        this.trust = trust;
        this.generosity = generosity;

        this.dystopiaResidual = dystopiaResidual;
    }
}
