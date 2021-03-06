package com.fvostudio.project.mancamure.gom;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OwnableElement extends Element {
    protected LinkedList<Owner> directOwners = new LinkedList<Owner>();
    private double ownValue = 0.0;
    private long categories = 0;

    public OwnableElement getOwnableParent() {
        if (getParent() instanceof OwnableElement) {
            return (OwnableElement) getParent();
        }

        return null;
    }

    public OwnableElement getFirstOwnableChild() {
        for (Element child = getFirstChild(); 
             child != null;
             child = child.getNextSibling()
        ) {
            if (child instanceof OwnableElement) {
                return (OwnableElement) child;
            }
        }
            
        return null;
    }

    public OwnableElement getLastOwnableChild() {
        for (Element child = getLastChild();
             child != null;
             child = child.getPreviousSibling()
        ) {
            if (child instanceof OwnableElement) {
                return (OwnableElement) child;
            }
        }

        return null;
    }

    public OwnableElement getPreviousOwnableSibling() {
        for (Element sibling = getPreviousSibling(); 
             sibling != null;
             sibling = sibling.getPreviousSibling()
        ) {
            if (sibling instanceof OwnableElement) {
                return (OwnableElement) sibling;
            }
        }

        return null;
    }

    public OwnableElement getNextOwnableSibling() {
        for (Element sibling = getNextSibling();
             sibling != null;
             sibling = sibling.getNextSibling()
        ) {
            if (sibling instanceof OwnableElement) {
                return (OwnableElement) sibling;
            }
        }

        return null;
    }

    public int getOwnableChildCount() {
        int ownableChildCount = 0;

        for (Element child = getFirstChild();
             child != null;
             child = child.getNextSibling()
        ) {
            if (child instanceof OwnableElement) {
                ++ownableChildCount;
            }
        }

        return ownableChildCount;
    }

    public OwnableElementList getOwnableChildren() {
        return new OwnableElementList(this);
    }

    public double getOwnValue() {
        return ownValue;
    }

    public double getValue() {
        double value = getOwnValue();

        for (Element child = getFirstChild();
             child != null;
             child = child.getNextSibling()
        ) {
            if (child instanceof OwnableElement) {
                value += ((OwnableElement) child).getValue();
            }
        }

        return value;
    }

    public long getCategories() {
        return categories;
    }

    public List<Owner> getDirectOwners() {
        return Collections.unmodifiableList(directOwners);
    }

    public List<Owner> getOwners() {
        LinkedList<Owner> owners = new LinkedList<Owner>(directOwners);
        OwnableElement parent = this;

        while ((parent = parent.getOwnableParent()) != null) {
            owners.addAll(0, parent.getDirectOwners());
        }

        return owners;
    }

    public void setOwnValue(double ownValue) {
        this.ownValue = ownValue;
    }

    public void setCategories(long categories) {
        this.categories = categories;
    }

    public boolean belongsTo(long categories) {
        return (this.categories & categories) != 0;
    }
}
