package unification;

public class Substitution {
    private final String target;
    private final String replacement;

    public Substitution(String target, String replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    public String getTarget() {
        return target;
    }

    public String getReplacement() {
        return replacement;
    }

    @Override
    public String toString() {
        return "{" + target + " / " + replacement + '}';
    }
}
