package unification.entities.term;

import unification.entities.HeadAndTailHolder;
import unification.entities.Substitution;

import java.util.*;
import java.util.stream.Collectors;

public class TermFunction implements Term {
    private FunctionLetter letter;
    private List<Term> arguments = new ArrayList<>();

    public TermFunction() {
    }

    public TermFunction(FunctionLetter letter, Term... arguments) {
        this.letter = letter;
        this.arguments.addAll(Arrays.asList(arguments));
    }

    public TermFunction(FunctionLetter letter, List<Term> arguments) {
        this.letter = letter;
        this.arguments = arguments;
    }

    public FunctionLetter getLetter() {
        return letter;
    }

    public List<Term> getArguments() {
        return arguments;
    }

    public void setLetter(FunctionLetter letter) {
        this.letter = letter;
    }

    public void setArguments(List<Term> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return letter.toString() +
                "(" +
                arguments.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(","))
                + ")";
    }

    @Override
    public HeadAndTailHolder getHeadAndTail() {
        return new HeadAndTailHolder(new TermFunction(letter, Collections.singletonList(new EmptyTerm())),
                new TermListOfTerms(arguments));
    }

    @Override
    public Term applySubstitution(Substitution s) {
        if (this.equals(s.getTarget())) {
            return s.getReplacement();
        }
        for (int i = 0; i < arguments.size(); i++) {
            arguments.set(i, arguments.get(i).applySubstitution(s));
        }
        return this;
    }

    @Override
    public boolean occurs(Term another) {
        if (this.equals(another)) {
            return true;
        }
        for (Term argument: arguments) {
            if (argument.occurs(another))
                return true;
        }
        return false;
    }

    public enum FunctionLetter {
        F, H, G
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermFunction that = (TermFunction) o;
        return letter == that.letter && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, arguments);
    }
}
