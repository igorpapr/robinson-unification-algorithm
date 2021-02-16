package unification.entities.term;

import unification.entities.HeadAndTailHolder;
import unification.entities.Substitution;

public class EmptyTerm implements Term {
    @Override
    public HeadAndTailHolder getHeadAndTail() {
        return new HeadAndTailHolder(this, new EmptyTerm());
    }

    @Override
    public Term applySubstitution(Substitution s) {
        if (this.equals(s.getTarget())) {
            return s.getReplacement();
        } else {
            return this;
        }
    }

    @Override
    public boolean occurs(Term another) {
        return this.equals(another);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return getClass() == o.getClass();
    }

    @Override
    public String toString() {
        return "<empty>";
    }
}
