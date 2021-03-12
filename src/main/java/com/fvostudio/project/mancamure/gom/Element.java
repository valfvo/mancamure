package com.fvostudio.project.mancamure.gom;

import org.w3c.dom.DOMException;

public class Element {
    private Element parent;
    private Element firstChild;
    private Element lastChild;
    private Element previousSibling;
    private Element nextSibling;

    public Element getParent() {
        return parent;
    }

    public Element getFirstChild() {
        return firstChild;
    }

    public Element getLastChild() {
        return lastChild;
    }

    public Element getPreviousSibling() {
        return previousSibling;
    }

    public Element getNextSibling() {
        return nextSibling;
    }

    public int getChildElementCount() {
        int childElementCount = 0;

        for (Element child = getFirstChild();
             child != null;
             child = child.getNextSibling()
        ) {
            ++childElementCount;
        }

        return childElementCount;
    }

    public ElementList getChildren() {
        return new ElementList(this);
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public void setFirstChild(Element firstChild) {
        this.firstChild = firstChild;
    }

    public void setLastChild(Element lastChild) {
        this.lastChild = lastChild;
    }

    public void setPreviousSibling(Element previousSibling) {
        this.previousSibling = previousSibling;
    }

    public void setNextSibling(Element nextSibling) {
        this.nextSibling = nextSibling;
    }

    public Element insertBefore(Element element, Element child) {
        if (element == null) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                                   "The new child element to be inserted is null.");
        }

        if (element.isInclusiveAncestorOf(this)) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                                   "The new child element contains the parent.");
        }

        if (child != null && child.getParent() != this) {
            throw new DOMException(
                DOMException.NOT_FOUND_ERR,
                "The element before which the new element is to be inserted"
                + " is not a child of this element.");
        }

        if ((child == element)
            || (child != null && child.getPreviousSibling() == element)
        ) {
            return child;
        }

        element.remove();

        if (child != null) {
            insertBeforeBase(element, child);
        } else {
            appendChildBase(element);
        }

        return child;
    }

    public Element appendChild(Element element) {
        return insertBefore(element, null);
    }

    public Element removeChild(Element child) {
        if (child == null) {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                                   "The child element to be removed is null.");
        } else if (child.getParent() != this) {
            throw new DOMException(
                DOMException.NOT_FOUND_ERR,
                "The element to be removed is not a child of this element.");
        }

        removeChildBase(child);

        return child;
    }

    public void remove() {
        if (getParent() != null) {
            getParent().removeChild(this);
        }
    }

    private boolean isAncestorOf(Element element) {
        assert(element != null);

        Element parent = element;
        while ((parent = parent.getParent()) != null) {
            if (parent == this) {
                return true;
            }
        }

        return false;
    }

    private boolean isInclusiveAncestorOf(Element element) {
        assert(element != null);

        return this == element || isAncestorOf(element);
    }

    private void insertBeforeBase(Element element, Element child) {
        Element previous = child.getPreviousSibling();

        child.setPreviousSibling(element);

        if (previous != null) {
            previous.setNextSibling(element);
        } else {
            setFirstChild(element);
        }

        element.setParent(this);
        element.setPreviousSibling(previous);
        element.setNextSibling(child);
    }

    private void appendChildBase(Element child) {
        child.setParent(this);

        if (getLastChild() != null) {
            child.setPreviousSibling(getLastChild());
            getLastChild().setNextSibling(child);
        } else {
            setFirstChild(child);
        }

        setLastChild(child);
    }

    private void removeChildBase(Element child) {
        Element previousChild = child.getPreviousSibling();
        Element nextChild = child.getNextSibling();

        if (nextChild != null) {
            nextChild.setPreviousSibling(previousChild);
        }
        if (previousChild != null) {
            previousChild.setNextSibling(nextChild);
        }

        if (getFirstChild() == child) {
            setFirstChild(nextChild);
        }
        if (getLastChild() == child) {
            setLastChild(previousChild);
        }

        child.setParent(null);
        child.setPreviousSibling(null);
        child.setNextSibling(null);
    }
}
