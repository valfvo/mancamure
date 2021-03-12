package com.fvostudio.project.mancamure.gom;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class OwnableElementList implements ListIterator<OwnableElement> {
    private int currentIndex = 0;
    private OwnableElement currentElement;

    public OwnableElementList(OwnableElement element) {
        if (element != null) {
            currentElement = element.getFirstOwnableChild();
        } else {
            currentElement = null;
        }
    }

    public boolean hasCurrent() {
        return currentElement != null;
    }

    public OwnableElement current() {
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
        return hasCurrent() && currentElement.getNextOwnableSibling() != null;
    }

    @Override
    public boolean hasPrevious() {
        return hasCurrent() && currentElement.getPreviousOwnableSibling() != null;
    }

    @Override
    public OwnableElement next() {
        if (hasNext()) {
            OwnableElement nextElement = currentElement.getNextOwnableSibling();
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

    @Override
    public OwnableElement previous() {
        if (hasPrevious()) {
            OwnableElement previousElement = currentElement.getPreviousOwnableSibling();
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

    public OwnableElement get(int index) {
        while (index < currentIndex()) {
            previous();
        }
        while (index > currentIndex()) {
            next();
        }

        return current();
    }

    @Override
    public void set(OwnableElement e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(OwnableElement e) {
        throw new UnsupportedOperationException();   
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
