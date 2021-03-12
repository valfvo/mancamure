package com.fvostudio.project.mancamure.gom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Owner {
    private ArrayList<OwnableElement> ownedElements = new ArrayList<OwnableElement>();

    public double getWealth() {
        double wealth = 0.0;

        for (OwnableElement ownedElement : ownedElements) {
            wealth += ownedElement.getValue();
        }

        return wealth;
    }

    public List<OwnableElement> getOwnedElements() {
        return Collections.unmodifiableList(ownedElements);
    }

    public void obtain(OwnableElement element) {
        if (!owns(element)) {
            ownedElements.add(element);
            element.directOwners.add(this);
        }
    }

    public void lose(OwnableElement element) {
        assert(owns(element));

        ownedElements.remove(element);
        element.directOwners.remove(this);
    }

    public boolean owns(OwnableElement element) {
        for (OwnableElement ownedElement : ownedElements) {
            if (ownedElement == element) {
                return true;
            }
        }

        return false;
    }

    public void give(OwnableElement element, Owner owner) {
        assert(owns(element));

        if (owner == this) {
            return;
        }

        lose(element);
        owner.obtain(element);
    }
}
