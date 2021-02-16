package unification;

import java.util.List;

public class TermFunction implements Term {
    private FunctionLetter letter;
    private List<Term> arguments;

    public enum FunctionLetter {
        F, H, G
    }
}
