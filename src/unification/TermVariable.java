package unification;

import java.util.Objects;

public class TermVariable implements Term{

    private final VariableLetter letter;
    private final int number;

    public TermVariable(VariableLetter letter, int number) {
        this.letter = letter;
        this.number = number;
    }

    public VariableLetter getLetter() {
        return letter;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return letter.toString() + number;
    }

    @Override
    public HeadAndTailHolder getHeadAndTail() {
        return new HeadAndTailHolder(this, new EmptyTerm());
    }

    @Override
    public Term applySubstitution(Substitution substitution) {
        if (this.equals(substitution.getTarget())) {
            return substitution.getReplacement();
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
        if (o == null || getClass() != o.getClass())
            return false;
        TermVariable that = (TermVariable) o;
        return number == that.number && letter == that.letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, number);
    }

    public enum VariableLetter {
        X, Y, Z
    }
}