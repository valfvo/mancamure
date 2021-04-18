package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Player;

public class AwaleBoardStateFactory {
    public static final class Auth { private Auth() {} }
    private static final Auth auth = new Auth();

    private static ArrayList<Integer> modifiablePits;

    private static ArrayList<Integer> modifiableBanks;

    public static LinkedList<AwaleBoardState> queue = new LinkedList<>();

    public static int i = 0;

    public static AwaleBoardState getState(
        AwaleBoard board,
        AwaleMovement lastMovement,
        Player upperPlayer,
        Player currentPlayer
    ) {
        // System.out.println("getState called, size factory = " + queue.size());
        AwaleBoardState newState = queue.isEmpty()
            ? new AwaleBoardState(auth)
            : queue.poll();

        newState.reset(auth, board, lastMovement, upperPlayer, currentPlayer,
                       modifiablePits, modifiableBanks);

        if (queue.isEmpty()) {
            // System.out.println(++i);
            modifiablePits = null;
            modifiableBanks = null;
            // modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            // modifiableBanks = new ArrayList<>(List.of(0, 0));
        } else {
            modifiablePits = queue.peek().getModifiablePits(auth);
            modifiableBanks = queue.peek().getModifiableBanks(auth);
        }

        return newState;
    }

    public static int getPit(int index) {
        if (modifiablePits == null) {
            ++i;
            // System.out.println("state created count = "+ ++i);
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            // modifiableBanks = new ArrayList<>(List.of(0, 0));
        }
        return modifiablePits.get(index);
    }

    public static void setPit(int index, int seedCount) {
        if (modifiablePits == null || modifiableBanks == null) {
            ++i;
            // System.out.println("state created count = "+ ++i);
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            modifiableBanks = new ArrayList<>(List.of(0, 0));
        }
        modifiablePits.set(index, seedCount);
    }

    public static void setPits(List<Integer> pits) {
        if (modifiablePits == null || modifiableBanks == null) {
            ++i;
            // System.out.println("state created count = "+ ++i);
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            modifiableBanks = new ArrayList<>(List.of(0, 0));
        }
        for (int i = 0; i < pits.size(); ++i) {
            setPit(i, pits.get(i));
        }
    }

    public static int getBank(int index) {
        if (modifiablePits == null || modifiableBanks == null) {
            ++i;
            // System.out.println("state created count = "+ ++i);
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            modifiableBanks = new ArrayList<>(List.of(0, 0));
        }
        return modifiableBanks.get(index);
    }

    public static void setBank(int index, int seedCount) {
        if (modifiablePits == null || modifiableBanks == null) {
            ++i;
            // System.out.println("state created count = "+ ++i);
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            modifiableBanks = new ArrayList<>(List.of(0, 0));
        }
        modifiableBanks.set(index, seedCount);
    }

    public static void setBanks(List<Integer> banks) {
        if (modifiablePits == null || modifiableBanks == null) {
            ++i;
            // System.out.println("state created count = "+ ++i);
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            modifiableBanks = new ArrayList<>(List.of(0, 0));
        }
        for (int i = 0; i < banks.size(); ++i) {
            setBank(i, banks.get(i));
        }
    }

    public static void recycle(AwaleBoardState state) {
        if (queue.isEmpty()) {
            modifiablePits = state.getModifiablePits(auth);
            modifiableBanks = state.getModifiableBanks(auth);
        }
        queue.offer(state);
    }
}
