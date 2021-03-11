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
    public void add(Element e) {
        throw new UnsupportedOperationException();   
    }

    @Override   
    public boolean hasNext() {
        return currentElement != null && currentElement.getNextSibling() != null;
    }

    @Override
    public boolean hasPrevious() {
        return currentElement != null && currentElement.getPreviousSibling() != null;
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

    @Override
    public Element previous() {
        if (hasPrevious()) {
            Element nextElement = currentElement.getPreviousSibling();
            currentElement = nextElement;
            --currentIndex;

            return nextElement;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int previousIndex() {
        return currentIndex() - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(Element e) {
        throw new UnsupportedOperationException();
    }

    public void goTo(int index) {
        while (index < currentIndex()) {
            previous();
        }
        while (index > currentIndex()) {
            next();
        }   
    }

    public Element get() {
        return currentElement;
    }

    public int currentIndex() {
        return currentIndex;
    }
}
