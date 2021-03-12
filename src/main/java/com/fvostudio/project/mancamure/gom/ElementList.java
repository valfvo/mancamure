package com.fvostudio.project.mancamure.gom;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ElementList implements ListIterator<Element> {
    private int currentIndex = 0;
    private Element currentElement;

    public ElementList(Element element) {
        if (element != null) {
            currentElement = element.getFirstChild();
        } else {
            currentElement = null;
        }
    }

    @Override
    public boolean hasPrevious() {
        return hasCurrent() && currentElement.getPreviousSibling() != null;
    }

    @Override
    public Element previous() {
        if (hasPrevious()) {
            Element previousElement = currentElement.getPreviousSibling();
            currentElement = previousElement;
            --currentIndex;

            return previousElement;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int previousIndex() {
        return currentIndex() - 1;
    }

    public boolean hasCurrent() {
        return currentElement != null;
    }

    public Element current() {
        if (hasCurrent()) {
            return currentElement;
        } else {
            throw new NoSuchElementException();
        }
    }

    public int currentIndex() {
        return currentIndex;
    }

    @Override   
    public boolean hasNext() {
        return hasCurrent() && currentElement.getNextSibling() != null;
    }

    @Override
    public Element next() {
        if (hasNext()) {
            Element nextElement = currentElement.getNextSibling();
            currentElement = nextElement;
            ++currentIndex;

            return nextElement;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int nextIndex() {
        return currentIndex() + 1;
    }

    public Element get(int index) {
        while (index < currentIndex()) {
            previous();
        }
        while (index > currentIndex()) {
            next();
        }

        return current();
    }

    @Override
    public void set(Element e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(Element e) {
        throw new UnsupportedOperationException();   
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
