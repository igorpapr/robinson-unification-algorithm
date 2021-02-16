package unification.entities.term;

import unification.entities.HeadAndTailHolder;
import unification.entities.Substitution;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TermListOfTerms implements Term {
    private final List<Term> termsList;

    public TermListOfTerms(List<Term> termsList) {
        this.termsList = termsList;
    }

    @Override
    public String toString() {
        return termsList.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public HeadAndTailHolder getHeadAndTail() {
        HeadAndTailHolder result = new HeadAndTailHolder();
        if (termsList.isEmpty()) {
            result.setHead(new EmptyTerm());
            result.setTail(new EmptyTerm());
        } else {
            result.setHead(termsList.get(0));
            if (termsList.size() == 1) {
                result.setTail(new EmptyTerm());
            } else {
                result.setTail(new TermListOfTerms(termsList.stream().skip(1).collect(Collectors.toList())));
            }
        }
        return result;
    }

    @Override
    public Term applySubstitution(Substitution s) {
        if (this.equals(s.getTarget())) {
            return s.getReplacement();
        }
        for (int i = 0; i < termsList.size(); i++) {
            termsList.set(i, termsList.get(i).applySubstitution(s));
        }
        return this;
    }

    @Override
    public boolean occurs(Term another) {
        if (this.equals(another))
            return true;
        for (Term term: termsList) {
            if (term.occurs(another))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermListOfTerms that = (TermListOfTerms) o;
        return Objects.equals(termsList, that.termsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(termsList);
    }
}
