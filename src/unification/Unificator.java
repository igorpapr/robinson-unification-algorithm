package unification;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Unificator {

    public static List<Substitution> unify(Term e1, Term e2) {
        try {
            //System.out.println("Entered unify with terms: " + e1 + " AND " + e2);
            //System.out.println("Checking for constants or empty");
            if (constants(e1, e2) || empty(e1, e2)) {
                //System.out.println("Entered constants or empty");
                if (e1.equals(e2)) {
                    //System.out.println("Returning empty list");
                    return Collections.emptyList();
                }
                throw new UnificationFailException("FAIL");
            }
            List<Substitution> variableCheckList1 = checkVariable(e1, e2);
            if (!variableCheckList1.isEmpty()) {
                return variableCheckList1;
            }
            List<Substitution> variableCheckList2 = checkVariable(e2, e1);
            if (!variableCheckList2.isEmpty()) {
                return variableCheckList2;
            }
            //System.out.println("Checking for emptiness both terms");
            if (empty(e1) || empty(e2)) {
                //System.out.println("Entered empty one or two terms");
                throw new UnificationFailException("FAIL");
            }
            //System.out.println("Getting head and tail of the first term: " + e1);
            HeadAndTailHolder headTail1 = e1.getHeadAndTail();
            //System.out.println("Returned head of the first term: " + headTail1.getHead());
            //System.out.println("Returned tail of the first term: " + headTail1.getTail());
            //System.out.println("Getting head and tail of the second term: " + e2);
            HeadAndTailHolder headTail2 = e2.getHeadAndTail();
            //System.out.println("Returned head of the second term: " + headTail2.getHead());
            //System.out.println("Returned tail of the second term: " + headTail2.getTail());
            List<Substitution> subs1 = Collections.emptyList();
            checkForUnequalFunctions(headTail1.getHead(), headTail2.getHead());
            if (!headTail1.getHead().equals(headTail2.getHead())){
                //System.out.println("Calling unify on heads: " + headTail1.getHead() + " AND " + headTail2.getHead());
                subs1 = unify(headTail1.getHead(), headTail2.getHead());
            }
            //if SUBS1 = FAIL then return FAIL
            //System.out.println("Applying returned substitution(" + subs1 +") to the tail 1: " + headTail1.getTail());
            Term te1 = apply(subs1, headTail1.getTail());
            //System.out.println("Applying returned substitution(" + subs1 +") to the tail 2: " + headTail2.getTail());
            Term te2 = apply(subs1, headTail2.getTail());
            //System.out.println("Calling unify on tails: " + headTail1.getTail() + " AND " +  headTail2.getTail());
            List<Substitution> subs2 = unify(te1, te2);
            //???????????????????????????????????????????????
            //System.out.println("Returning composition of " + subs1 +" and " + subs2);
            return composition(subs1, subs2);
        } catch (UnificationFailException e) {
            System.err.println("Failed to unify terms: " + e.getMessage());
            throw new RuntimeException("END");
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    private static void checkForUnequalFunctions(Term first, Term second) throws UnificationFailException {
        //System.out.println("Checking for unqueal functions");
        if (first instanceof TermFunction && second instanceof TermFunction) {
            if (!((TermFunction) first).getLetter().equals(((TermFunction) second).getLetter())){
                throw new UnificationFailException("FAIL");
            }
        }
    }

    private static List<Substitution> checkVariable(Term targetTerm, Term anotherTerm) throws UnificationFailException {
        //System.out.println("Checking for variables");
        if (variable(targetTerm)) {
            if (targetTerm.equals(anotherTerm)) {
                //System.out.println("Target term equals to another term, returning empty list");
                return Collections.emptyList();
            }
            if (targetTerm.occurs(anotherTerm)) {
                //System.out.println("Target term occurs in another term, returning FAIL");
                throw new UnificationFailException("FAIL");
            }
            Substitution result = new Substitution(targetTerm, anotherTerm);
            //System.out.println("Returning substitution: " + result);
            return Collections.singletonList(result);
        }
        //System.out.println("The target term was not variable");
        return Collections.emptyList();
    }

    private static boolean empty(Term e1) {
        return e1 instanceof EmptyTerm;
    }

    private static boolean empty(Term e1, Term e2) {
        return empty(e1) || empty(e2);
    }

    private static List<Substitution> composition(List<Substitution> first, List<Substitution> second) {
        return Stream.concat(first.stream(), second.stream())
                .collect(Collectors.toList());
    }

    private static boolean variable(Term term) {
        return term instanceof TermVariable;
    }

    private static boolean constant(Term term) {
        return term instanceof TermConstant;
    }

    private static boolean constants(Term t1, Term t2) {
        return constant(t1) || constant(t2);
    }

    private static Term apply(List<Substitution> substitutions, Term target) {
        for (Substitution s: substitutions) {
            target = target.applySubstitution(s);
        }
        return target;
    }

    public static void main(String[] args) {
        long m = System.currentTimeMillis();
        for (int N = 1; N <= 5  ; N++) {
            System.out.println("========================================================");
            List<Substitution> result = unify(generateFirstTerm(N),generateSecondTerm(N));
            System.out.printf("----------RESULT OF N = %d ----------%n", N);
            System.out.println("Number of substitutions: " + result.size());
            System.out.println(result);

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
    private static Term generateFirstTerm(int n) {
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
    private static Term generateSecondTerm(int n) {
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
