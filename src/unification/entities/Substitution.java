package unification.entities;

import unification.entities.term.Term;

public class Substitution {
    private final Term target;
    private final Term replacement;

    public Substitution(Term target, Term replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    public Term getTarget() {
        return target;
    }

    public Term getReplacement() {
        return replacement;
    }

    @Override
    public String toString() {
        return "{" + target + " / " + replacement + '}';
    }
}
