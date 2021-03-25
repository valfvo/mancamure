package com.fvostudio.project.mancamure;

import com.fvostudio.project.mancamure.gom.Board;
import com.fvostudio.project.mancamure.gom.BoardState;
import com.fvostudio.project.mancamure.gom.OwnableElement;
import com.fvostudio.project.mancamure.gom.Cell;
import com.fvostudio.project.mancamure.gom.util.Vector3;
import com.fvostudio.project.mancamure.gom.Player;

import java.util.List;
import java.util.ArrayList;

public class AwaleBoard extends Board {
    public static final long CELL_ELEMENT = 0b10;
    public static final long SEEDBANK_ELEMENT = 0b100;
    
    private boolean validCoordinate(Vector3 coordinate) {
        double x = coordinate.getX();
        double y = coordinate.getY();

        return (x >= 0 && x < 6) && (y >= 0 && y <= 1);
    }

    public Cell getCell(Vector3 coordinate) {
        assert validCoordinate(coordinate) : "coordinate not valid";

        List<OwnableElement> ownedElements = getOwnedElements();

        List<Cell> cells = new ArrayList<Cell>();
        for (OwnableElement element : ownedElements) {
            if (element.belongsTo(CELL_ELEMENT)) {
                cells.add((Cell) element);
            }
        }

        Cell cell = null;

        for (Cell c : cells) {
            if (coordinate.equals(c.getCoordinate())) {
                cell = c;
            }
        }

        return cell;
    }

    public BoardState getState() {

        ArrayList<Integer> pits = new ArrayList<Integer>();
        ArrayList<Integer> banks = new ArrayList<Integer>();

        Player currentPlayer = getGame().getCurrentPlayer();

        /*
            [0.0, 0.1, 0.2, 0.3, 0.4, 0.5]
            [1.0, 1.1, 1.2, 1.3, 1.4, 1.5] 

            si 0.0 est cell du current player alors c'est pits[11] qu'il faut remplir
                puis décrementer l'index
            sinon pits[0]

            si 1.0 est cell du current player alors c'est pits[6] qu'il faut remplir
                puis incrémenter
            sinon pits[5]
        */

        /* [U6, U5, U4, U3, U2, U1,
        *  L1, L2, L3, L4, L5, L6]
        * 
        * U : Upper, L : Lower
        */

        // rempli pits
        int index = 0;
        
        boolean reverseBoard = currentPlayer.owns(getCell(new Vector3(0,0,0)));

        if (reverseBoard){
            index = 11;
        }

        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 6; ++x) {
                Vector3 coordCurrentCell = new Vector3(x, y, 0);
                Cell currentCell = getCell(coordCurrentCell);
                int seedCount = currentCell.getOwnableChildCount();
                if (reverseBoard) {
                    pits.add(index--, seedCount);
                }
                else {
                    pits.add(index++, seedCount);
                }
            }
        }

        // rempli banks
        List<OwnableElement> ownedElements = getOwnedElements();

        for (OwnableElement element : ownedElements) {
            if (element.belongsTo(SEEDBANK_ELEMENT)) {
                int seedCount = element.getOwnableChildCount();
                if (currentPlayer.owns(element)) {
                    banks.add(1, seedCount);
                }
                else {
                    banks.add(0, seedCount);
                }
            }
        }

        return new AwaleBoardState(pits, banks);
    }

    private Vector3 getNewCoordinate(Vector3 startingPosition, int nbMove){
        if (nbMove == 0) {
            return startingPosition;
        }
        else if (startingPosition.getY() == 0 
            && (startingPosition.getX() - nbMove) < 0
        ) {
            return getNewCoordinate(new Vector3(0, 1, 0), 
                                    (nbMove - (int) startingPosition.getX()));
        }
        else if (startingPosition.getY() == 0 
            && (startingPosition.getX() - nbMove) > 0
        ) {
            return new Vector3(startingPosition.getX() - nbMove, 0, 0);
        }
        else if (startingPosition.getY() == 1 
            && (startingPosition.getX() + nbMove) > 5
        ) {
            return getNewCoordinate(new Vector3(5, 0, 0), 
                                    (nbMove - (6 - (int) startingPosition.getX())));
                                    //nbMove - (nbMove deja fait) 
        }
        else {
            return new Vector3(startingPosition.getX() + nbMove, 1, 0);
        }
    }

    public void changeState(BoardState state) {
        // 1) recup la Cell qui faut vider et nombre de graines
        AwaleMovement move = (AwaleMovement) state.getLastMovement();
        Vector3 startingPostion = move.getStartingPitPosition();
        int seedCount = getCell(startingPostion).getOwnableChildCount();

        Player currentPlayer = getGame().getCurrentPlayer();
        ArrayList<Integer> banks = new ArrayList<>();

        // 2) distribuer les ownableElements
        List<OwnableElement> ownedElements = getOwnedElements();
        for (OwnableElement element : ownedElements) {
            if (element.belongsTo(CELL_ELEMENT)) {
                Vector3 coordRecipientCell = getNewCoordinate(startingPostion, seedCount--);
                // ??? on moveTo les MovableElement (seed), pas les Cell ???
                // element.moveTo(getCell(coordRecipientCell));
            }
            else if (element.belongsTo(SEEDBANK_ELEMENT)) {
                if (currentPlayer.owns(element)) {
                    banks.add(1, seedCount);
                }
                else {
                    banks.add(0, seedCount);
                }
            }
        }

        // 3) vider les cases selon les règles (voir state) et remplir les banks
        AwaleBoardState awaleState = (AwaleBoardState) state;
        ArrayList<Integer> stateBanks = awaleState.getBanks();

        boolean banksEquals = true;
        OwnableElement bankChange = null; 

        // if (stateBanks.get(0) != banks.get(0)) {
        //     banksEquals = false;
        //     bankChange = banks.get(0);
        // }
        // else if(stateBanks.get(1) != banks.get(1)) {         //A FINIR
        //     banksEquals = false;
        //     bankChange = banks.get(1);
        // }

        if (!banksEquals) { //verifier d'ou vient le changement et mettre les graines dans la bank 

        }
    }
}
