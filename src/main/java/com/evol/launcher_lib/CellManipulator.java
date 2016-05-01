package com.evol.launcher_lib;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class CellManipulator {

    private static final String TAG = CellManipulator.class.getSimpleName();

    public static final int CELL_COUNT_IN_WIDTH = 4;
    public static final int CELL_COUNT_IN_HEIGHT = 4;

    public static final String CELL_KEY = "row_%s_col_%s";

    private static final float VIEW_MARGIN_START = 100;
    private static final float VIEW_MARGIN_END = 100;
    private static final float VIEW_MARGIN_TOP = 100;
    private static final float VIEW_MARGIN_BOTTOM = 100;

    private Context mAppContext;

    private float viewWidth, viewHeight;
    private float viewStartX, viewEndX, viewStartY, viewEndY;

    private float viewBoundaryWidth, viewBoundaryHeight;
    private float viewBoundaryStartX, viewBoundaryEndX, viewBoundaryStartY, viewBoundaryEndY;

    private DashboardData mDashboardData;

    public CellManipulator(Context context, float viewWidth, float viewHeight, float viewStartX, float viewEndX, float viewStartY, float viewEndY) {
        mAppContext = context.getApplicationContext();

        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.viewStartX = viewStartX;
        this.viewEndX = viewEndX;
        this.viewStartY = viewStartY;
        this.viewEndY = viewEndY;

        this.viewBoundaryWidth = viewWidth - VIEW_MARGIN_START - VIEW_MARGIN_END;
        this.viewBoundaryHeight = viewHeight - VIEW_MARGIN_TOP - VIEW_MARGIN_BOTTOM;
        this.viewBoundaryStartX = viewStartX + VIEW_MARGIN_START;
        this.viewBoundaryEndX = viewEndX - VIEW_MARGIN_END;
        this.viewBoundaryStartY = viewStartY + VIEW_MARGIN_TOP;
        this.viewBoundaryEndY = viewEndY - VIEW_MARGIN_BOTTOM;

        mDashboardData = new DashboardData();

        initCellMap();
        printViewDetails();
    }

    private void initCellMap() {
        float cellWidth = viewWidth / CELL_COUNT_IN_WIDTH;
        float cellHeight = viewHeight / CELL_COUNT_IN_HEIGHT;

        float boundaryCellWidth = viewBoundaryWidth / CELL_COUNT_IN_WIDTH;
        float boundaryCellHeight = viewBoundaryHeight / CELL_COUNT_IN_HEIGHT;

        for (int row = 0; row < CELL_COUNT_IN_WIDTH; row++) {
            for (int col = 0; col < CELL_COUNT_IN_HEIGHT; col++) {
                String cellKey = String.format(CELL_KEY, row, col);

                Cell outerCell = new Cell();

                // Outer cell RectF
                RectF outerCellRectF = new RectF();
                outerCellRectF.left = viewStartX + (col * cellWidth);
                outerCellRectF.right = viewStartX + ((col + 1) * cellWidth);
                outerCellRectF.top = viewStartY + (row * cellHeight);
                outerCellRectF.bottom = viewStartY + ((row + 1) * cellHeight);

                // Outer cell Rect
                Rect outerCellRect = new Rect();
                outerCellRectF.roundOut(outerCellRect);

                // Outer cell RectF square
                RectF outerSquareCellRectF = new RectF();
                float length = outerCellRectF.width() < outerCellRectF.height() ? outerCellRectF.width() : outerCellRectF.height();
                outerSquareCellRectF.left = outerCellRectF.centerX() - (length / 2);
                outerSquareCellRectF.right = outerCellRectF.centerX() + (length / 2);
                outerSquareCellRectF.top = outerCellRectF.centerY() - (length / 2);
                outerSquareCellRectF.bottom = outerCellRectF.centerY() + (length / 2);

                // Outer cell Rect square
                Rect outerSquareCellRect = new Rect();
                outerSquareCellRectF.roundOut(outerSquareCellRect);

                // Update Outer cell details
                outerCell.setCellRectF(outerCellRectF);
                outerCell.setCellRect(outerCellRect);
                outerCell.setSquareCellRectF(outerSquareCellRectF);
                outerCell.setSquareCellRect(outerSquareCellRect);
                outerCell.setId(mAppContext.getResources().getInteger(mDashboardData.getCellAppIconIdMap().get(cellKey)));
                outerCell.setIsStartBoundaryCell(col == 0);
                outerCell.setIsEndBoundaryCell(col == CELL_COUNT_IN_WIDTH -1);

                // Add outer cell in DashBoardData outer cell HashMap
                mDashboardData.getOuterCellMap().put(String.format(CELL_KEY, row, col), outerCell);

                ////////////////////////////////////////////////////////////////////////////////////////////////////

                Cell innerCell = new Cell();

                // Outer cell RectF
                RectF innerCellRectF = new RectF();
                innerCellRectF.left = viewBoundaryStartX + (col * boundaryCellWidth);
                innerCellRectF.right = viewBoundaryStartX + ((col + 1) * boundaryCellWidth);
                innerCellRectF.top = viewBoundaryStartY + (row * boundaryCellHeight);
                innerCellRectF.bottom = viewBoundaryStartY + ((row + 1) * boundaryCellHeight);

                // Outer cell Rect
                Rect innerCellRect = new Rect();
                innerCellRectF.roundOut(innerCellRect);

                // Outer cell RectF square
                RectF innerSquareCellRectF = new RectF();
                float innerSquareLength = innerCellRectF.width() < innerCellRectF.height() ? innerCellRectF.width() : innerCellRectF.height();
                innerSquareCellRectF.left = innerCellRectF.centerX() - (innerSquareLength / 2);
                innerSquareCellRectF.right = innerCellRectF.centerX() + (innerSquareLength / 2);
                innerSquareCellRectF.top = innerCellRectF.centerY() - (innerSquareLength / 2);
                innerSquareCellRectF.bottom = innerCellRectF.centerY() + (innerSquareLength / 2);

                // Outer cell Rect square
                Rect innerSquareCellRect = new Rect();
                innerSquareCellRectF.roundOut(innerSquareCellRect);

                // Update Outer cell details
                innerCell.setCellRectF(innerCellRectF);
                innerCell.setCellRect(innerCellRect);
                innerCell.setSquareCellRectF(innerSquareCellRectF);
                innerCell.setSquareCellRect(innerSquareCellRect);
                innerCell.setId(mAppContext.getResources().getInteger(mDashboardData.getCellAppIconIdMap().get(cellKey)));
                innerCell.setIsStartBoundaryCell(col == 0);
                innerCell.setIsEndBoundaryCell(col == CELL_COUNT_IN_WIDTH - 1);

                // Add outer cell in DashBoardData outer cell HashMap
                mDashboardData.getInnerCellMap().put(cellKey, innerCell);
            }
        }
    }

    public void printCellDetails() {
        mDashboardData.printOuterCellsDetails();
        //mDashboardData.printInnerCellsDetails();
    }

    public void printViewDetails() {
        Log.d(TAG, "**********View Details START************");
        Log.d(TAG, "Width: " + viewWidth);
        Log.d(TAG, "Height: " + viewHeight);
        Log.d(TAG, "StartX: " + viewStartX);
        Log.d(TAG, "EndX: " + viewEndX);
        Log.d(TAG, "StartY: " + viewStartY);
        Log.d(TAG, "EndY: " + viewEndY);
        Log.d(TAG, "**********View Details END************");
    }

    public DashboardData getDashboardData() {
        return mDashboardData;
    }

    public LinkedHashMap<String, Cell> getOuterCellDetailsMap() {
        return mDashboardData.getOuterCellMap();
    }

    public LinkedHashMap<String, Cell> getInnerCellDetailsMap() {
        return mDashboardData.getInnerCellMap();
    }

    public Cell findFirstValidCell() {
        Cell cell = null;
        for (Map.Entry<String, Cell> pair : mDashboardData.getOuterCellMap().entrySet()) {
            cell = pair.getValue();
            if (cell.isOccupied()) {
                continue;
            }
            return cell;
        }
        return cell;
    }

    public Cell findRelevantCellAtLocation(float x, float y) {
        Cell cell = null;
        for (Map.Entry<String, Cell> pair : mDashboardData.getOuterCellMap().entrySet()) {
            cell = pair.getValue();
            if (cell.getCellRectF().contains(x, y)) {
                return cell.isOccupied() ? null : cell;
            }
        }
        return cell;
    }

    public Cell findCellById(int requestedCellId) {
        Cell cell = null;
        for (Map.Entry<String, Cell> pair : mDashboardData.getOuterCellMap().entrySet()) {
            cell = pair.getValue();
            if (cell.getId() == requestedCellId) {
                return cell;
            }
        }
        return cell;
    }
}
