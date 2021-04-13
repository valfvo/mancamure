package com.fvostudio.project.mancamure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fvostudio.project.mancamure.gom.Player;

public class AwaleBoardStateFactory {
    public static final class Auth { private Auth() {} }
    private static final Auth auth = new Auth();

    private static ArrayList<Integer> modifiablePits = 
        new ArrayList<>(Collections.nCopies(12, 0));

    private static ArrayList<Integer> modifiableBanks =
        new ArrayList<>(List.of(0, 0));

    private static LinkedList<AwaleBoardState> queue = new LinkedList<>();

    public static AwaleBoardState getState(
        AwaleBoard board,
        AwaleMovement lastMovement,
        Player upperPlayer,
        Player currentPlayer
    ) {
        AwaleBoardState newState = queue.isEmpty()
            ? new AwaleBoardState(auth)
            : queue.poll();

        newState.reset(auth, board, lastMovement, upperPlayer, currentPlayer,
                       modifiablePits, modifiableBanks);

        if (queue.isEmpty()) {
            modifiablePits = new ArrayList<>(Collections.nCopies(12, 0));
            modifiableBanks = new ArrayList<>(List.of(0, 0));
        } else {
            modifiablePits = queue.peek().getModifiablePits(auth);
            modifiableBanks = queue.peek().getModifiableBanks(auth);
        }

        return newState;
    }

    public static int getPit(int index) {
        return modifiablePits.get(index);
    }

    public static void setPit(int index, int seedCount) {
        modifiablePits.set(index, seedCount);
    }

    public static void setPits(List<Integer> pits) {
        for (int i = 0; i < pits.size(); ++i) {
            setPit(i, pits.get(i));
        }
    }

    public static int getBank(int index) {
        return modifiableBanks.get(index);
    }

    public static void setBank(int index, int seedCount) {
        modifiableBanks.set(index, seedCount);
    }

    public static void setBanks(List<Integer> banks) {
        for (int i = 0; i < banks.size(); ++i) {
            setBank(i, banks.get(i));
        }
    }

    public static void recycle(AwaleBoardState state) {
        queue.offer(state);
    }
}
