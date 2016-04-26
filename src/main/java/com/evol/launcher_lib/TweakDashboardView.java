package com.evol.launcher_lib;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Map;

public class TweakDashboardView extends RelativeLayout {

    private static final String TAG = TweakDashboardView.class.getSimpleName();
    private static final int BOUNDARY_CELL_TRIGGER_PAGE_CHANGE_TIME_MILLIS = 1000;

    private Context mContext = getContext();
    private boolean mShouldDrawCells;
    private Paint mInnerCellPaint;
    private Paint mOuterCellPaint;

    private CellManipulator cellManipulator;
    private IDashboardSetupListener mDashboardSetupListener;
    private boolean mIsSetUpCompleted;
    private DashboardData mDashboardData;
    private int mPagePos;
    private PreviousBoundaryCellTrackerRunnable mPreviousBoundaryCellRunnable;

    private IViewPagerCallbacks mViewPagerCallbacks;
    private boolean isEndTimerInProgress;

    public interface IViewPagerCallbacks {
        void moveToNextPage(int currentPagePos);

        void moveToPreviousPage(int currentPagePos);

        ImageView removeAppIconFromPage(int pagePos, int appIconId);

        void clearIsOccupiedFlag(int pagePos, int cellId);
    }

    public TweakDashboardView(Context context) {
        super(context);
        init();
    }

    public TweakDashboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TweakDashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mInnerCellPaint = new Paint();
        mInnerCellPaint.setColor(Color.rgb(1, 0, 0));
        mInnerCellPaint.setStrokeWidth(2);
        mInnerCellPaint.setStyle(Paint.Style.STROKE);

        mOuterCellPaint = new Paint();
        mOuterCellPaint.setColor(Color.rgb(1, 1, 0));
        mOuterCellPaint.setStrokeWidth(2);
        mOuterCellPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initCellManipulator();

                mIsSetUpCompleted = true;
                mDashboardData = cellManipulator.getDashboardData();
                if (mDashboardSetupListener != null) {
                    mDashboardSetupListener.onSetupCompleted();
                }
            }
        };

        getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        setOnDragListener(new ContainerDropListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure()");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout()");
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw()");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d(TAG, "dispatchDraw()");
        if (mShouldDrawCells) {
            //drawInnerCells(canvas);
            drawOuterCells(canvas);
            drawOuterSquareCells(canvas);
        }
    }

    private void initCellManipulator() {
        cellManipulator = new CellManipulator(
                getContext(),
                getWidth(),
                getHeight(),
                getLeft(),
                getRight(),
                getTop(),
                getBottom()
        );
        cellManipulator.printCellDetails();

        mShouldDrawCells = true;
        invalidate();
    }

    private void drawInnerCells(Canvas canvas) {
        for (Map.Entry<String, Cell> pair : cellManipulator.getInnerCellDetailsMap().entrySet()) {
            canvas.drawRect(pair.getValue().getCellRectF(), mInnerCellPaint);
        }
    }

    private void drawOuterCells(Canvas canvas) {
        for (Map.Entry<String, Cell> pair : cellManipulator.getOuterCellDetailsMap().entrySet()) {
            canvas.drawRect(pair.getValue().getCellRectF(), mOuterCellPaint);
        }
    }

    private void drawOuterSquareCells(Canvas canvas) {
        for (Map.Entry<String, Cell> pair : cellManipulator.getOuterCellDetailsMap().entrySet()) {
            canvas.drawRect(pair.getValue().getSquareCellRect(), mOuterCellPaint);
        }
    }

    public void addAppIcon(ImageView appIconImageView) {
        Cell imageViewCellContainer = cellManipulator.findFirstValidCell();

        if (imageViewCellContainer == null) {
            Toast.makeText(mContext, "No empty cell location found to put additional app icon.", Toast.LENGTH_LONG).show();
            return;
        }

        addAppIcon(appIconImageView, imageViewCellContainer);
    }

    private void addAppIcon(ImageView appIconImageView, Cell imageViewCellContainer) {
        Rect emptyLocationRect = imageViewCellContainer.getCellRect();
        LayoutParams layoutParams = new LayoutParams(emptyLocationRect.width(), emptyLocationRect.height());
        layoutParams.leftMargin = emptyLocationRect.left;
        layoutParams.topMargin = emptyLocationRect.top;

        appIconImageView.setLayoutParams(layoutParams);
        appIconImageView.setId(imageViewCellContainer.getId());

        imageViewCellContainer.setIsOccupied(true);
        addView(appIconImageView);

        appIconImageView.setOnTouchListener(new AppIconTouchListener());
    }

    private class AppIconTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);

                DraggedCellLocalState localState = new DraggedCellLocalState();
                localState.appIconId = v.getId();
                localState.pagePos = mPagePos;

                v.startDrag(data, shadowBuilder, localState, 0);
                return true;
            }
            return false;
        }
    }

    private class ContainerDropListener implements OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    projectAppIconHintAtLocation(v, event);
                    checkAndProcessEndCellDragEvent(v, event);
                    break;
                case DragEvent.ACTION_DROP:
                    translateAppIconToLocation(v, event);
                    break;
            }
            return true;
        }
    }

    private void translateAppIconToLocation(View containerView, DragEvent event) {
        DraggedCellLocalState localState = (DraggedCellLocalState) event.getLocalState();

        Log.d(TAG, "translateAppIconToLocation() Drop Drag: LocalState is appIconId: " + localState.appIconId + " Start Page Pos: " + localState.pagePos + " current page pos: " + mPagePos);

        int viewId = localState.appIconId;
        Cell draggedViewCurrentCell = cellManipulator.findCellById(viewId);

        if (draggedViewCurrentCell == null) {
            Toast.makeText(mContext, "Trouble finding cell related to view id: " + viewId, Toast.LENGTH_LONG).show();
            return;
        }

        float locationX = event.getX();
        float locationY = event.getY();

        Cell cellAtLocation = cellManipulator.findRelevantCellAtLocation(locationX, locationY);
        if (cellAtLocation == null) {
            Toast.makeText(mContext, "Can't drop app icon at this location.", Toast.LENGTH_LONG).show();
            return;
        }

        ImageView draggedAppIconImageView = null;
        if (localState.pagePos == mPagePos) {
            draggedAppIconImageView = removeAppIcon(draggedViewCurrentCell.getId());
            clearIsOccupiedFlag(draggedViewCurrentCell.getId());
        } else if (mViewPagerCallbacks != null) {
            draggedAppIconImageView = mViewPagerCallbacks.removeAppIconFromPage(localState.pagePos, localState.appIconId);
            mViewPagerCallbacks.clearIsOccupiedFlag(localState.pagePos, localState.appIconId);
        }

        if (draggedAppIconImageView == null) {
            Log.d(TAG, "Something bad happened while removing appIconId: " + localState.appIconId + " from page pos: " + localState.pagePos + "current page pos: " + mPagePos);
            return;
        }

        // Add view at relevant location
        addAppIcon(draggedAppIconImageView, cellAtLocation);
    }

    private void projectAppIconHintAtLocation(View v, DragEvent event) {

    }

    private void checkAndProcessEndCellDragEvent(View v, DragEvent event) {
        Cell draggedViewCurrentCell = cellManipulator.findRelevantCellAtLocation(event.getX(), event.getY());

        if (draggedViewCurrentCell == null) {
            Log.d(TAG, "checkAndProcessEndCellDragEvent() draggedViewCurrentCell is null, hence returning");
            return;
        }

        if (!draggedViewCurrentCell.isBoundaryCell()) {
            if (mPreviousBoundaryCellRunnable == null) {
                Log.d(TAG, "checkAndProcessEndCellDragEvent() Current cell is not boundary cell and previous boundary cell runnable is null hence returning");
                return;
            } else {
                Log.d(TAG, "checkAndProcessEndCellDragEvent() Current cell is not boundary cell, but previous boundary cell runnable is not null hence cancelling previous runnable and returning");
                cancelEndBoundaryRunnable();
                return;
            }
        }

        if (mPreviousBoundaryCellRunnable == null) {
            Log.d(TAG, "checkAndProcessEndCellDragEvent() Current cell is boundary cell and previous boundary cell runnable is null, hence adding post delayed runnable for it");
            addPreviousBoundaryCellRunnable(draggedViewCurrentCell);
            return;
        }

        if (mPreviousBoundaryCellRunnable.getPreviousBoundaryCell().getId() != draggedViewCurrentCell.getId()) {
            Log.d(TAG, "checkAndProcessEndCellDragEvent() Current cell is boundary cell and previous boundary cell is not null and current cell id is not equal to previous cell id, hence cancelling previous boundary cell runnable and adding current boundary cell runnable");
            cancelEndBoundaryRunnable();
            addPreviousBoundaryCellRunnable(draggedViewCurrentCell);
            return;
        }

        Log.d(TAG, "checkAndProcessEndCellDragEvent() Current cell is boundary cell and it is same as previous one and hence doing nothing");
    }

    private void addPreviousBoundaryCellRunnable(Cell currentBoundaryCell) {
        mPreviousBoundaryCellRunnable = new PreviousBoundaryCellTrackerRunnable(currentBoundaryCell);
        postDelayed(mPreviousBoundaryCellRunnable, BOUNDARY_CELL_TRIGGER_PAGE_CHANGE_TIME_MILLIS);
    }

    private class PreviousBoundaryCellTrackerRunnable implements Runnable {

        private Cell mPreviousBoundaryCell;

        PreviousBoundaryCellTrackerRunnable(Cell previousBoundaryCell) {
            this.mPreviousBoundaryCell = previousBoundaryCell;
        }

        @Override
        public void run() {
            Log.d(TAG, "Alarm triggers mViewPagerCallbacks is: " + mViewPagerCallbacks);
            if (mViewPagerCallbacks != null) {
                if (mPreviousBoundaryCell.isStartBoundaryCell()) {
                    Log.d(TAG, "PreviousBoundaryCellTrackerRunnable: Timer blow out and hence moving to next page");
                    mViewPagerCallbacks.moveToPreviousPage(mPagePos);
                } else {
                    Log.d(TAG, "PreviousBoundaryCellTrackerRunnable: Timer blow out and hence moving to previous page");
                    mViewPagerCallbacks.moveToNextPage(mPagePos);
                }
            }
            cancelEndBoundaryRunnable();
        }

        public Cell getPreviousBoundaryCell() {
            return mPreviousBoundaryCell;
        }
    }

    private void cancelEndBoundaryRunnable() {
        removeCallbacks(mPreviousBoundaryCellRunnable);
        mPreviousBoundaryCellRunnable = null;
    }

    public void addSetupListener(IDashboardSetupListener listener) {
        this.mDashboardSetupListener = listener;
    }

    public void setViewPagerCallbacks(int pagePos, IViewPagerCallbacks viewPagerCallbacks) {
        this.mPagePos = pagePos;
        this.mViewPagerCallbacks = viewPagerCallbacks;
    }

    public ImageView removeAppIcon(int appIconId) {
        ImageView draggedAppIconImageView = (ImageView) findViewById(appIconId);
        Log.d(TAG, "TweakDashboard at pos: " + mPagePos + " Says appIcon view is: " + draggedAppIconImageView);
        if (draggedAppIconImageView != null) {
            removeView(draggedAppIconImageView);
            return draggedAppIconImageView;
        }
        return null;
    }

    public void clearIsOccupiedFlag(int cellId) {
        Cell cell = cellManipulator.findCellById(cellId);
        if (cell != null) {
            cell.setIsOccupied(false);
        }
    }
}