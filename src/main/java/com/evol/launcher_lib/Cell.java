package com.evol.launcher_lib;

import android.graphics.Rect;
import android.graphics.RectF;

public class Cell {

    private RectF cellRectF;
    private Rect cellRect;
    private RectF squareCellRectF;
    private Rect squareCellRect;
    private int id;
    private boolean isOccupied;
    private boolean isBoundaryCell;
    private boolean isStartBoundaryCell;
    private boolean isEndBoundaryCell;

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public Rect getCellRect() {
        return cellRect;
    }

    public void setCellRect(Rect cellRect) {
        this.cellRect = cellRect;
    }

    public RectF getSquareCellRectF() {
        return squareCellRectF;
    }

    public void setSquareCellRectF(RectF squareCellRectF) {
        this.squareCellRectF = squareCellRectF;
    }

    public Rect getSquareCellRect() {
        return squareCellRect;
    }

    public void setSquareCellRect(Rect squareCellRect) {
        this.squareCellRect = squareCellRect;
    }

    public RectF getCellRectF() {
        return cellRectF;
    }

    public void setCellRectF(RectF cellRectF) {
        this.cellRectF = cellRectF;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBoundaryCell() {
        return isBoundaryCell;
    }

    public boolean isStartBoundaryCell() {
        return isStartBoundaryCell;
    }

    public void setIsStartBoundaryCell(boolean isStartBoundaryCell) {
        this.isStartBoundaryCell = isStartBoundaryCell;
        this.isBoundaryCell = this.isStartBoundaryCell || this.isEndBoundaryCell;
    }

    public boolean isEndBoundaryCell() {
        return isEndBoundaryCell;
    }

    public void setIsEndBoundaryCell(boolean isEndBoundaryCell) {
        this.isEndBoundaryCell = isEndBoundaryCell;
        this.isBoundaryCell = this.isStartBoundaryCell || this.isEndBoundaryCell;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "width=" + cellRectF.width() +
                ", height=" + cellRectF.height() +
                ", startX=" + cellRectF.left +
                ", startY=" + cellRectF.right +
                ", endX=" + cellRectF.top +
                ", endY=" + cellRectF.bottom +
                ", centerX=" + cellRectF.centerX() +
                ", centerY=" + cellRectF.centerY() +
                ", id=" + id +
                ", isBoundaryCell=" + isBoundaryCell +
                ", isStartBoundaryCell=" + isStartBoundaryCell +
                ", isEndBoundaryCell=" + isEndBoundaryCell +
                '}' +
                "IsOccupied { " +
                isOccupied +
                "}";
    }
}
