package com.fvostudio.project.mancamure;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.OwnableElement;
import com.fvostudio.project.mancamure.gom.Cell;
import com.fvostudio.project.mancamure.gom.MovableElement;
import com.fvostudio.project.mancamure.gom.util.Vector3;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AwaleBoard extends Board {
    private AwaleBoardState state;
    private int lines = 2;
    private int columns = 6;
    private int startingSeedCountPerPit = 4;

    public AwaleBoard() {
        // OwnableElement root = new OwnableElement();

        for (int x = 0; x < columns; ++x) {
            for (int y = 0 ; y < lines; ++y) {
                Cell cell = new Cell(new Vector3(x, y, 0));

                for (int i = 0; i < startingSeedCountPerPit; ++i) {
                    cell.appendChild(new MovableElement());
                }

                obtain(cell);
                // root.appendChild(cell);
            }
        }

        obtain(new Bank());
        obtain(new Bank());

        // obtain(root);
    }

    public int getLineCount() {
        return lines;
    }

    public int getColumnCount() {
        return columns;
    }

    private boolean validPosition(Vector3 position) {
        double x = position.getX();
        double y = position.getY();

        return (x >= 0 && x < 6) && (y >= 0 && y <= 1);
    }

    public Cell getCell(Vector3 position) {
        assert validPosition(position) : "position not valid";

        List<OwnableElement> ownedElements = getOwnedElements();
        Cell cell = null;

        for (OwnableElement element : ownedElements) {
            if ((element instanceof Cell)
                && position.equals(((Cell) element).getPosition())
            ) {
                cell = (Cell) element;
                break;
            }
        }

        return cell;
    }

    public ArrayList<Bank> getBanks() {
        ArrayList<Bank> banks = new ArrayList<>(2);

        int ownedElementCount = ownedElements.size();
        banks.add((Bank) ownedElements.get(ownedElementCount - 2));
        banks.add((Bank) ownedElements.get(ownedElementCount - 1));

        return banks;
    }

    @Override
    public AwaleBoardState getState() {
        if (getGame().getCurrentPlayer() == null) {
            throw new IllegalStateException(
                "Can not get a board state because the first round has not started.");
        }
        if (state != null) {
            return state;
        }

        /*
         *   [5.0, 4.0, 3.0, 2.0, 1.0, 0.0]
         *   [0.1, 1.1, 2.1, 3.1, 4.1, 5.1] 
         *
         *   
         *  [U1, U2, U3, U4, U5, U6,
         *   L1, L2, L3, L4, L5, L6]
         * 
         *  U : Upper, L : Lower
         */

        // ArrayList<Integer> pits = 
        //     new ArrayList<>(Collections.nCopies(lines * columns, 0));

        // ArrayList<Integer> banks = new ArrayList<>(List.of(0, 0));

        // fill pits
        for (int y = 0; y < lines; ++y) {
            for (int x = 0; x < columns; ++x) {
                Vector3 coordCurrentCell = new Vector3(x, y, 0);
                Cell currentCell = getCell(coordCurrentCell);
                int seedCount = currentCell.getOwnableChildCount();

                if (y == 0) {
                    // 5 4 3 2 1 0
                    AwaleBoardStateFactory.setPit((columns - x) - 1 , seedCount);
                }
                else {
                    // 6 7 8 9 10 11
                    AwaleBoardStateFactory.setPit(columns + x, seedCount);
                }
            }
        }

        // fill banks
        List<OwnableElement> ownedElements = getOwnedElements();

        boolean isFirstBank = true;

        for (OwnableElement element : ownedElements) {
            if (element instanceof Bank) {
                int seedCount = element.getOwnableChildCount();

                if (isFirstBank) {  //first bank found = upper bank
                    AwaleBoardStateFactory.setBank(0, seedCount);
                    isFirstBank = false;
                }
                else {
                    AwaleBoardStateFactory.setBank(1, seedCount);
                    break;
                }
            }
        }

        state = AwaleBoardStateFactory.getState(
            this,
            (AwaleMovement) getGame().getLastMovement(),
            getGame().getPlayers().get(0),
            getGame().getCurrentPlayer()
        );

        return state;
    }

    // private Vector3 getNewPosition(Vector3 startingPosition, int nbMove){
    //     if (nbMove == 0) {
    //         return startingPosition;
    //     }
    //     else if (startingPosition.getY() == 0 
    //         && (startingPosition.getX() - nbMove) < 0
    //     ) {
    //         return getNewPosition(new Vector3(0, 1, 0), 
    //                                 (nbMove - (int) startingPosition.getX()));
    //     }
    //     else if (startingPosition.getY() == 0 
    //         && (startingPosition.getX() - nbMove) > 0
    //     ) {
    //         return new Vector3(startingPosition.getX() - nbMove, 0, 0);
    //     }
    //     else if (startingPosition.getY() == 1 
    //         && (startingPosition.getX() + nbMove) > 5
    //     ) {
    //         return getNewPosition(new Vector3(5, 0, 0), 
    //                                 (nbMove - (6 - (int) startingPosition.getX())));
    //                                 //nbMove - (nbMove deja fait) 
    //     }
    //     else {
    //         return new Vector3(startingPosition.getX() + nbMove, 1, 0);
    //     }
    // }

    public void changeState(BoardState boardState) {
        assert(boardState instanceof AwaleBoardState);
        AwaleBoardStateFactory.recycle(state);
        state = (AwaleBoardState) boardState;
        return;

        // List<Integer> pits = state.getPits();

        // for (int i = 0; i < pits.size(); ++i) {
        //     int pit = pits.get(i);
        //     Cell cell = getCell(state.getPitPosition(i));
        //     int seedCount = cell.getOwnableChildCount();

        //     while (seedCount > pit) {
        //         cell.getLastOwnableChild().remove();
        //         --seedCount;
        //     }
        //     while (seedCount < pit) {
        //         cell.appendChild(new MovableElement());
        //         ++seedCount;
        //     }
        // }

        /*
         * current board:
         * banks: 10 15
         * pits: 1 2 2 1 3 4
         *       3 4 1 0 0 1
         * 
         * state:
         * banks: 14 20
         * pits: 0 2 5 8 3 0
         *       3 0 1 0 0 1
         */

        // // 1) recup la Cell qui faut vider et nombre de graines
        // AwaleMovement move = (AwaleMovement) state.getLastMovement();
        // Vector3 startingPostion = move.getStartingPitPosition();
        // int seedCount = getCell(startingPostion).getOwnableChildCount();

        // Player currentPlayer = getGame().getCurrentPlayer();
        // ArrayList<Integer> banks = new ArrayList<>();

        // // 2) distribuer les ownableElements
        // List<OwnableElement> ownedElements = getOwnedElements();
        // for (OwnableElement element : ownedElements) {
        //     if (element.belongsTo(CELL_ELEMENT)) {
        //         Vector3 coordRecipientCell = getNewPosition(startingPostion, seedCount--);
        //         // ??? on moveTo les MovableElement (seed), pas les Cell ???
        //         // element.moveTo(getCell(coordRecipientCell));
        //     }
        //     else if (element.belongsTo(SEEDBANK_ELEMENT)) {
        //         if (currentPlayer.owns(element)) {
        //             banks.add(1, seedCount);
        //         }
        //         else {
        //             banks.add(0, seedCount);
        //         }
        //     }
        // }

        // // 3) vider les cases selon les règles (voir state) et remplir les banks
        // AwaleBoardState awaleState = (AwaleBoardState) state;
        // ArrayList<Integer> stateBanks = awaleState.getBanks();

        // boolean banksEquals = true;
        // OwnableElement bankChange = null; 

        // // if (stateBanks.get(0) != banks.get(0)) {
        // //     banksEquals = false;
        // //     bankChange = banks.get(0);
        // // }
        // // else if(stateBanks.get(1) != banks.get(1)) {         //A FINIR
        // //     banksEquals = false;
        // //     bankChange = banks.get(1);
        // // }

        // if (!banksEquals) { //verifier d'ou vient le changement et mettre les graines dans la bank 

        // }
    }
}
