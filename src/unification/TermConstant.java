package unification;

import java.util.Objects;

public class TermConstant implements Term{

    private final ConstantLetter letter;

    public TermConstant(ConstantLetter letter) {
        this.letter = letter;
    }

    public ConstantLetter getLetter() {
        return letter;
    }

    @Override
    public HeadAndTailHolder getHeadAndTail() {
        return new HeadAndTailHolder(this, new EmptyTerm());
    }

    public enum ConstantLetter {
        a, b, c, d
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
        return another.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermConstant that = (TermConstant) o;
        return letter == that.letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }

    @Override
    public String toString() {
        return letter.toString();
    }
}
