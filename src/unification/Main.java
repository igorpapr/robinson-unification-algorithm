package unification;

import unification.entities.Substitution;
import unification.entities.term.Term;
import unification.entities.term.TermFunction;
import unification.entities.term.TermVariable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final int N_MAX = 31;

    public static void main(String[] args) {
        long m = System.currentTimeMillis();
        for (int N = 1; N <= N_MAX; N++) {
            System.out.println("========================================================");
            List<Substitution> result = Unificator.unify(generateFirstHTerm(N), generateSecondHTerm(N));
            System.out.printf("----------RESULT OF N = %d ----------%n", N);
            System.out.println("Number of substitutions: " + result.size());
            //System.out.println(result);
        }
        System.out.println("ELAPSED TIME: ");
        long millisTime = System.currentTimeMillis() - m;
        long minutes
                = TimeUnit.MILLISECONDS.toMinutes(millisTime);
        long seconds
                = (TimeUnit.MILLISECONDS.toSeconds(millisTime)
                % 60);
        if (seconds == 0){
            System.out.println("Miliseconds: " + millisTime);
        } else {
            System.out.println("Minutes: " + minutes + ", seconds: " + seconds);
        }
    }

    //h(x1, x2, ..., xn, f(y0, y0), ..., f(yn-1, yn-1), yn)
    private static Term generateFirstHTerm(int n) {
        TermFunction result = new TermFunction();
        result.setLetter(TermFunction.FunctionLetter.H);
        List<Term> arguments = result.getArguments();
        for (int i = 0; i < n; i++) {
            arguments.add(new TermVariable(TermVariable.VariableLetter.X, i+1));
        }
        for (int j = 0; j < n; j++) {
            TermVariable var1 = new TermVariable(TermVariable.VariableLetter.Y, j);
            TermVariable var2 = new TermVariable(TermVariable.VariableLetter.Y, j);
            arguments.add(new TermFunction(TermFunction.FunctionLetter.F, var1, var2));
        }
        arguments.add(new TermVariable(TermVariable.VariableLetter.Y, n));
        //System.out.println("Generated first term: \n" + result);
        return result;
    }

    //h(f(x0,x0), f(x1,x1), ..., f(xn-1, xn-1), y1, ..., yn, xn)
    private static Term generateSecondHTerm(int n) {
        TermFunction result = new TermFunction();
        result.setLetter(TermFunction.FunctionLetter.H);
        List<Term> arguments = result.getArguments();
        for (int j = 0; j < n; j++) {
            TermVariable var1 = new TermVariable(TermVariable.VariableLetter.X, j);
            TermVariable var2 = new TermVariable(TermVariable.VariableLetter.X, j);
            arguments.add(new TermFunction(TermFunction.FunctionLetter.F, var1, var2));
        }
        for (int i = 0; i < n; i++) {
            arguments.add(new TermVariable(TermVariable.VariableLetter.Y, i+1));
        }
        arguments.add(new TermVariable(TermVariable.VariableLetter.X, n));
        //System.out.println("Generated second term: \n" + result);
        return result;
    }
}
