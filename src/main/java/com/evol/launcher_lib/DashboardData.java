package com.evol.launcher_lib;

import android.util.Log;

import com.evol.homeLauncher.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardData {

    private static final String TAG = DashboardData.class.getSimpleName();

    private LinkedHashMap<String, Cell> outerCellMap;
    private LinkedHashMap<String, Cell> innerCellMap;
    private LinkedHashMap<String, Integer> cellAppIconIdMap;

    public DashboardData() {
        outerCellMap = new LinkedHashMap<>();
        innerCellMap = new LinkedHashMap<>();
        fillCellAppIconIdMap();
    }

    public LinkedHashMap<String, Cell> getOuterCellMap() {
        return outerCellMap;
    }

    public void setOuterCellMap(LinkedHashMap<String, Cell> outerCellMap) {
        this.outerCellMap = outerCellMap;
    }

    public LinkedHashMap<String, Cell> getInnerCellMap() {
        return innerCellMap;
    }

    public void setInnerCellMap(LinkedHashMap<String, Cell> innerCellMap) {
        this.innerCellMap = innerCellMap;
    }

    public LinkedHashMap<String, Integer> getCellAppIconIdMap() {
        return cellAppIconIdMap;
    }

    private void fillCellAppIconIdMap() {
        cellAppIconIdMap = new LinkedHashMap<>();

        cellAppIconIdMap.put("row_0_col_0", R.integer.row_0_col_0);
        cellAppIconIdMap.put("row_0_col_1", R.integer.row_0_col_1);
        cellAppIconIdMap.put("row_0_col_2", R.integer.row_0_col_2);
        cellAppIconIdMap.put("row_0_col_3", R.integer.row_0_col_3);

        cellAppIconIdMap.put("row_1_col_0", R.integer.row_1_col_0);
        cellAppIconIdMap.put("row_1_col_1", R.integer.row_1_col_1);
        cellAppIconIdMap.put("row_1_col_2", R.integer.row_1_col_2);
        cellAppIconIdMap.put("row_1_col_3", R.integer.row_1_col_3);

        cellAppIconIdMap.put("row_2_col_0", R.integer.row_2_col_0);
        cellAppIconIdMap.put("row_2_col_1", R.integer.row_2_col_1);
        cellAppIconIdMap.put("row_2_col_2", R.integer.row_2_col_2);
        cellAppIconIdMap.put("row_2_col_3", R.integer.row_2_col_3);

        cellAppIconIdMap.put("row_3_col_0", R.integer.row_3_col_0);
        cellAppIconIdMap.put("row_3_col_1", R.integer.row_3_col_1);
        cellAppIconIdMap.put("row_3_col_2", R.integer.row_3_col_2);
        cellAppIconIdMap.put("row_3_col_3", R.integer.row_3_col_3);
    }

    public void printOuterCellsDetails() {
        Log.d(TAG, "*******************Outer Cell Details START*******************");
        for (Map.Entry<String, Cell> pair : outerCellMap.entrySet()) {
            Log.d(TAG, pair.getKey() + "Details: " + pair.getValue().toString());
        }
        Log.d(TAG, "*******************Outer Cell Details END*******************");
    }

    public void printInnerCellsDetails() {
        Log.d(TAG, "*******************Outer Cell Details START*******************");
        for (Map.Entry<String, Cell> pair : innerCellMap.entrySet()) {
            Log.d(TAG, pair.getKey() + "Details: " + pair.getValue().toString());
        }
        Log.d(TAG, "*******************Outer Cell Details END*******************");
    }
}
