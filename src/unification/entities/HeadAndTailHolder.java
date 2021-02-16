package unification.entities;

import unification.entities.term.Term;

public class HeadAndTailHolder {
    private Term head;
    private Term tail;

    public HeadAndTailHolder() {
    }

    public HeadAndTailHolder(Term head, Term tail) {
        this.head = head;
        this.tail = tail;
    }

    public void setHead(Term head) {
        this.head = head;
    }

    public void setTail(Term tail) {
        this.tail = tail;
    }

    public Term getHead() {
        return head;
    }

    public Term getTail() {
        return tail;
    }
}
