package com.example.mbanking.ui.transactions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.mbanking.R

class RecyclerItemDecoration(
    con: Context,
    headerHeight: Int,
    isSticky: Boolean,
    callback: SectionCallback
) :
    ItemDecoration() {
    var context: Context = con
    private var headerOffset: Int = headerHeight
    private var sticky: Boolean = isSticky
    private var sectionCallback: SectionCallback = callback
    private var headerView: View? = null
    var date: TextView? = null
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (headerView == null) {
            headerView = inflateHeader(parent)
            date = headerView!!.findViewById(R.id.txtDate)
            fixLayoutSize(headerView, parent)
        }
        var prevTitle = ""
        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            val childPos = parent.getChildAdapterPosition(child)
            val title = sectionCallback.getSectionHeaderName(childPos)
            date!!.text = title
            if (!prevTitle.equals(
                    title,
                    ignoreCase = true
                ) || sectionCallback.isSection(childPos)
            ) {
                drawHeader(c, child, headerView)
                prevTitle = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View?) {
        c.save()
        if (sticky) {
            if (headerView != null) {
                c.translate(0F, 0.coerceAtLeast(child.top - headerView.height).toFloat())
            }
        } else {
            if (headerView != null) {
                c.translate(0F, (child.top - headerView.height).toFloat())
            }
        }
        headerView?.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(view: View?, viewGroup: ViewGroup) {
        val widthSpec: Int =
            View.MeasureSpec.makeMeasureSpec(viewGroup.width, View.MeasureSpec.EXACTLY)
        val heightSpec: Int =
            View.MeasureSpec.makeMeasureSpec(viewGroup.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = view?.layoutParams?.width?.let {
            ViewGroup.getChildMeasureSpec(
                widthSpec,
                viewGroup.paddingLeft + viewGroup.paddingRight,
                it
            )
        }
        val childHeight = view?.layoutParams?.height?.let {
            ViewGroup.getChildMeasureSpec(
                heightSpec,
                viewGroup.paddingTop + viewGroup.paddingBottom,
                it
            )
        }
        if (childWidth != null && childHeight != null) {
            view.measure(childWidth, childHeight)
        }
        view?.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun inflateHeader(recyclerView: RecyclerView): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.header_recycler_view, recyclerView, false)
    }

    interface SectionCallback {
        fun isSection(pos: Int): Boolean
        fun getSectionHeaderName(pos: Int): String
    }
}