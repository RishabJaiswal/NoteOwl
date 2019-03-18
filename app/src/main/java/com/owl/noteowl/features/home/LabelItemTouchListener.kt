package com.owl.noteowl.features.home

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView


class LabelItemTouchListener : ItemTouchHelper.Callback() {

    private var swipeBack: Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            getDefaultUIUtil().onSelected(getForegroundView(viewHolder))
        }
    }

    override fun onChildDrawOver(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        viewHolder?.let {
            getDefaultUIUtil().onDrawOver(
                canvas, recyclerView, getForegroundView(it),
                dX, dY, actionState, isCurrentlyActive
            )
        }
    }

    override fun onChildDraw(
        canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, viewHolder, dX)
        }
        getDefaultUIUtil().onDraw(
            canvas, recyclerView, getForegroundView(viewHolder),
            dX, dY, actionState, isCurrentlyActive
        )
    }

    private fun setTouchListener(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float) {
        recyclerView.setOnTouchListener { v, event ->
            swipeBack = (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP)
            if (dX > 140 && swipeBack && viewHolder is LabelsForFilterAdapter.LabelViewHolder) {
                viewHolder.filterNote()
            }
            false
        }
    }

    private fun getForegroundView(viewHolder: RecyclerView.ViewHolder): View? {
        if (viewHolder is LabelsForFilterAdapter.LabelViewHolder) {
            return viewHolder.getSwipeableView()
        }
        return null
    }
}