package unification;

import unification.entities.*;
import unification.entities.term.*;
import unification.exceptions.UnificationFailException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Unificator {

    public static List<Substitution> unify(Term e1, Term e2) {
        try {
            //Checking for constants or emptiness
            if (constants(e1, e2) || empty(e1, e2)) {
                if (e1.equals(e2)) {
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
            HeadAndTailHolder headTail1 = e1.getHeadAndTail();
            HeadAndTailHolder headTail2 = e2.getHeadAndTail();
            List<Substitution> subs1 = Collections.emptyList();
            //Heads must not be equal functions
            checkForUnequalFunctions(headTail1.getHead(), headTail2.getHead());
            if (!headTail1.getHead().equals(headTail2.getHead())){
                //Calling unify recursively for heads
                subs1 = unify(headTail1.getHead(), headTail2.getHead());
            }
            //Applying returned substitution to the tail 1
            Term te1 = apply(subs1, headTail1.getTail());
            //Applying returned substitution to the tail 2
            Term te2 = apply(subs1, headTail2.getTail());
            //Calling unify for tails
            List<Substitution> subs2 = unify(te1, te2);

            return composition(subs1, subs2);
        } catch (UnificationFailException e) {
            System.err.println("Failed to unify terms: " + e.getMessage());
            throw new RuntimeException("END");
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    //Checking for unequal functions
    private static void checkForUnequalFunctions(Term first, Term second) throws UnificationFailException {
        if (first instanceof TermFunction && second instanceof TermFunction) {
            if (!((TermFunction) first).getLetter().equals(((TermFunction) second).getLetter())){
                throw new UnificationFailException("FAIL");
            }
        }
    }

    //Check target term (if it is a variable) for correctness
    private static List<Substitution> checkVariable(Term targetTerm, Term anotherTerm) throws UnificationFailException {
        if (variable(targetTerm)) {
            if (targetTerm.equals(anotherTerm)) {
                return Collections.emptyList();
            }
            if (targetTerm.occurs(anotherTerm)) {
                throw new UnificationFailException("FAIL");
            }
            Substitution result = new Substitution(targetTerm, anotherTerm);

            return Collections.singletonList(result);
        }
        //The target term was not a variable
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
}
