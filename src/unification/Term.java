package unification;

public interface Term {

    HeadAndTailHolder getHeadAndTail();

    Term applySubstitution(Substitution s);

    boolean occurs(Term another);
}
