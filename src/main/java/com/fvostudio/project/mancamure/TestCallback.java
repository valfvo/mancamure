package com.fvostudio.project.mancamure;

import netscape.javascript.JSObject;

public class TestCallback {
    public void ok(String s) {
        System.out.println(s);
    }

    public void ok2(JSObject mouseEvent) {
        Integer x = (Integer) mouseEvent.getMember("clientX");
        Integer y = (Integer) mouseEvent.getMember("clientY");
        System.out.println(x.toString() + ", " + y.toString());
    }
}
