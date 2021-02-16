package unification;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Unificator {

    public static List<Substitution> unify(String e1, String e2) {
        try {
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
            if (empty(e1) || empty(e2)) {
                throw new UnificationFailException("FAIL");
            }
            HeadAndTailHolder headAndTail1 = headAndTail(e1);
            HeadAndTailHolder headAndTail2 = headAndTail(e2);
            List<Substitution> subs1 = unify(headAndTail1.getHead(), headAndTail2.getHead());
            //if SUBS1 = FAIL then return FAIL
            String te1 = apply(subs1, headAndTail1.getTail());
            String te2 = apply(subs1, headAndTail2.getTail());
            List<Substitution> subs2 = unify(te1, te2);
            //???????????????????????????????????????????????
            return composition(subs1, subs2);
        } catch (UnificationFailException e) {
            System.err.println("Failed to unify terms: " + e.getMessage());
            throw new RuntimeException("END");
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    private static HeadAndTailHolder headAndTail(String e) throws UnificationFailException {
        StringBuilder headBuilder = new StringBuilder();
        StringBuilder tailBuilder = new StringBuilder();
        boolean pushToTailMode = false;
        Stack<Character> openedBrackets = new Stack<>();
        for (int i = 0; i < e.length(); i++) {
            char c = e.charAt(i);
            if (c == ',') {
                if (!pushToTailMode) {
                    break;
                } else {

                }
            }
            if (c == '(') {
                if (pushToTailMode)
                    tailBuilder.append(c);
                else {
                    //перевіряти чи рілі тейл чи ні, чи це всередині функції, чи це поза функцією, МБ ПЕРЕРОБИТИ ТЕРМИ ПІД СПИСОК СТРІНГІВ
                    pushToTailMode = true;
                }
                openedBrackets.push(c);
            } else if (c == ')') {
                if (openedBrackets.isEmpty())
                    throw new UnificationFailException("The given term is incorrect (missing brackets).");
                else if (openedBrackets.size() == 1) {
                    break;
                } else {
                    tailBuilder.append(c);
                    openedBrackets.pop();
                }
            } else {
                if (pushToTailMode) {
                    tailBuilder.append(c);
                } else {
                    headBuilder.append(c);
                }
            }
        }
        return new HeadAndTailHolder(headBuilder.toString(), tailBuilder.toString());
    }

    private static List<Substitution> checkVariable(String targetTerm, String anotherTerm) throws UnificationFailException {
        if (variable(targetTerm)) {
            if (targetTerm.equals(anotherTerm)) {
                return Collections.emptyList();
            }
            if (occurs(targetTerm, anotherTerm)) {
                throw new UnificationFailException("FAIL");
            }
            return Collections.singletonList(new Substitution(targetTerm, anotherTerm));
        }
        return Collections.emptyList();
    }

    private static boolean empty(String e1) {
        return e1.equals("");
    }

    private static boolean empty(String e1, String e2) {
        return empty(e1) || empty(e2);
    }

    private static List<Substitution> composition(List<Substitution> first, List<Substitution> second) {
        return Stream.concat(first.stream(), second.stream())
                .collect(Collectors.toList());
    }

    private static boolean variable(String term) {
        return Utils.variableMatches(term);
    }

    private static boolean constant(String term) {
        return Utils.constantMatches(term);
    }

    private static boolean constants(String t1, String t2) {
        return constant(t1) || constant(t2);
    }

    private static boolean occurs(String source, String substring) {
        return source.contains(substring);
    }

    private static String apply(List<Substitution> substitutions, String target) {
        for (Substitution s: substitutions) {
            target = target.replace(s.getTarget(), s.getReplacement());
        }
        return target;
    }

//    private static Substitution parseSubstitution(String replacement) {
//        String[] items = replacement.split(" / ");
//        if (items.length != 2) {
//            throw new RuntimeException("Failed to break substitution to exact 2 parts.");
//        }
//        return new Substitution(items[0],items[1]);
//    }

    public static void main(String[] args) {
    //    //TODO generate h-terms here
        List<Substitution> result = unify("h(x1,f(y0, y0), y1)", "h(f(x0, x0), y1, x1)");
        System.out.println(result);
    }
}
