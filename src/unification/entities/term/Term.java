package unification.entities.term;

import unification.entities.HeadAndTailHolder;
import unification.entities.Substitution;

public interface Term {

    HeadAndTailHolder getHeadAndTail();

    Term applySubstitution(Substitution s);

    boolean occurs(Term another);
}
