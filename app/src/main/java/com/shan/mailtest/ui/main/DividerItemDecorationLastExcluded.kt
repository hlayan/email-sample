package com.shan.mailtest.ui.main

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorationLastExcluded(
    @ColorRes private val dividerColor: Int = android.R.color.darker_gray,
    private val dividerHeight: Int = 2,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = dividerHeight
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val lastItemIndex = parent.childCount - 1

        val paint = Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(parent.context, dividerColor)
        }

        val left = parent.paddingLeft.toFloat()

        val right = (parent.width - parent.paddingRight).toFloat()

        parent.children.forEachIndexed { index, view ->

            if (index != lastItemIndex) {

                val params = view.layoutParams as RecyclerView.LayoutParams
                val top = (view.bottom + params.bottomMargin).toFloat()

                val bottom = top + dividerHeight

                canvas.drawRect(left, top, right, bottom, paint)

            }
        }
    }
}