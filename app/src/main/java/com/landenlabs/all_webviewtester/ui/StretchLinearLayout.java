/*
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (3/21/2015)
 *  @see http://landenlabs.com/
 *
 */

package com.landenlabs.all_webviewtester.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.landenlabs.all_webviewtester.R;

/**
 * A LinearLayout which can resize to a maximum and/or minimum dimensions.
 *
 * <pre>
 * Example:
 *      &lt;com.yourPackage.StretchLinearLayout
 *          android:layout_width="match_parent"
 *          android:layout_height="0dp"
 *          android:layout_weight="1"
 *          custom:maxHeight="400dp"
 *          custom:minHeight="200dp"
 *          custom:layout_gravity="bottom"
 *          custom:layout_below="@id/someOtherView>
 *          &lt;View
 *              android:layout_width="match_parent"
 *              android:layout_height="match_parent" />
 *      &lt;/com.wsi.android.weather.ui.widget.BoundedLinearLayout>
 * </pre>
 *
 * Attributes implementation by StretchLinearLayout:
 *<pre>
 *  R.styleable#StretchLinearLayout_maxWidth
 *  R.styleable#StretchLinearLayout_maxHeight
 *  R.styleable#StretchLinearLayout_minHeight
 *  R.styleable#StretchLinearLayout_maxHeight
 *
 *  R.styleable#StretchLinearLayout_gravity
 *
 *  R.styleable#StretchLinearLayout_layout_below
 *  R.styleable#StretchLinearLayout_layout_above
 *  R.styleable#StretchLinearLayout_layout_toLeftOf
 *  R.styleable#StretchLinearLayout_layout_toRightOf
 *</pre>
 * @author Chase, Dennis Lang
 *
 */

public class StretchLinearLayout extends LinearLayout {

    // Values to limit Stretching of layout size.
    private int mMinWidthInside;
    private int mMinHeightInside;
    private int mMaxWidthInside;
    private int mMaxHeightInside;
    private int mMinWidthOutside;
    private int mMinHeightOutside;
    private int mMaxWidthOutside;
    private int mMaxHeightOutside;
    
    private int mGravity;

    // View resources to optionally adjust edges (see RelativeLayout)
    private int mBelowId;
    private View mBelowView;
    private int mAboveId;
    private View mAboveView;
    private int mLeftId;
    private View mLeftView;
    private int mRightId;
    private View mRightView;

    private static final int NOT_SET = -1;

    public StretchLinearLayout(Context context) {
        super(context);
        mMinWidthInside = mMaxWidthInside = NOT_SET;
        mMinHeightInside = mMaxHeightInside = NOT_SET;
        mMinWidthOutside = mMaxWidthOutside = NOT_SET;
        mMinHeightOutside = mMaxHeightOutside = NOT_SET;
    }

    public StretchLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        commonInit(context, attrs, defStyle);
    }

    public StretchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs, 0);
    }

    private void commonInit(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StretchLinearLayout, defStyle, 0);

        mMinWidthInside = mMinWidthOutside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_minWidth, NOT_SET);
        mMinHeightInside = mMaxHeightOutside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_minHeight, NOT_SET);
        mMaxWidthInside = mMaxWidthOutside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_maxWidth, NOT_SET);
        mMaxHeightInside = mMaxHeightOutside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_maxHeight, NOT_SET);

        mMinWidthInside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_minWidthInside, NOT_SET);
        mMinHeightInside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_minHeightInside, NOT_SET);
        mMaxWidthInside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_maxWidthInside, NOT_SET);
        mMaxHeightInside = 
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_maxHeightInside, NOT_SET);

        mMinWidthOutside =
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_minWidthOutside, NOT_SET);
        mMinHeightOutside =
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_minHeightOutside, NOT_SET);
        mMaxWidthOutside =
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_maxWidthOutside, NOT_SET);
        mMaxHeightOutside =
                a.getDimensionPixelSize(R.styleable.StretchLinearLayout_maxHeightOutside, NOT_SET);

        mGravity = a.getInt(R.styleable.StretchLinearLayout_layout_gravity, 0); //  Gravity.LEFT + Gravity.TOP);

        mBelowId = a.getResourceId(R.styleable.StretchLinearLayout_layout_below, NOT_SET);
        mAboveId = a.getResourceId(R.styleable.StretchLinearLayout_layout_above, NOT_SET);
        mLeftId = a.getResourceId(R.styleable.StretchLinearLayout_layout_toLeftOf, NOT_SET);
        mRightId = a.getResourceId(R.styleable.StretchLinearLayout_layout_toRightOf, NOT_SET);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int oldTop = getTop();
        int oldBot = getBottom();
        int oldLeft = getLeft();
        int oldRight = getRight();
        if (oldBot != 0 || oldRight != 0) {
            if (resizeLayout(oldLeft, oldTop, oldRight, oldBot)) {
                widthMeasureSpec =
                        MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                heightMeasureSpec =
                        MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected boolean resizeLayout(int l, int t, int r, int b) {

        // TODO - shrink size when child shrink, ex set vis to gone.

        boolean changed = false;
        int orgWidth = r - l;
        int orgHeight =  b - t;

        int newTop = t;
        int newBot = b;
        int newLeft = l;
        int newRight = r;

        // Move any relative layout edges.
        if (mBelowId != NOT_SET) {
            mBelowView = getView(mBelowView, mBelowId);
            if (mBelowView != null)
                newTop = mBelowView.getBottom();
        }
        if (mAboveId != NOT_SET) {
            mAboveView = getView(mAboveView, mAboveId);
            if (mAboveView != null)
                newBot = mAboveView.getTop();
        }
        if (mLeftId != NOT_SET) {
            mLeftView = getView(mLeftView, mLeftId);
            if (mLeftView != null)
                newRight = mLeftView.getLeft();
        }
        if (mRightId != NOT_SET) {
            mRightView = getView(mRightView, mRightId);
            if (mRightView != null)
                newLeft = mRightView.getRight();
        }

        // Adjust width as necessary
        int newWidth = newRight - newLeft;
        if (mMinWidthOutside != NOT_SET  && newWidth < mMinWidthOutside )
            newWidth = mMinWidthOutside ;
        if (mMaxWidthOutside  != NOT_SET && newWidth > mMaxWidthOutside )
            newWidth = mMaxWidthOutside ;

        if (mMinWidthInside != NOT_SET && getMeasuredWidth() >= mMinWidthInside &&
                getMeasuredWidth() < newWidth)
            newWidth = getMeasuredWidth();

        if (newWidth != orgWidth) {
            if (isFlag(mGravity, Gravity.LEFT))
                newRight = l + newWidth;
            else if (isFlag(mGravity, Gravity.RIGHT))
                newLeft = r - newWidth;
            else if (isFlag(mGravity, Gravity.CENTER_HORIZONTAL)) {
                int c = (l + r) / 2;
                newLeft = c - newWidth/2;
                newRight = l + newWidth;
            }

            changed = (newLeft != l || newRight != r);

            // These are expensive, they invalide layout.
            // They are optimized to only fire if value changed.
            setLeft(newLeft);
            setRight(newRight);
        }

        // Adjust height as necessary
        int newHeight = newBot - newTop;
        // newHeight = Math.min(getMeasuredHeight(), newHeight);

        if (mMinHeightOutside != NOT_SET && newHeight < mMinHeightOutside)
            newHeight = mMinHeightOutside;
        if (mMaxHeightOutside != NOT_SET && newHeight > mMaxHeightOutside)
            newHeight = mMaxHeightOutside;

        if (mMinHeightInside != NOT_SET && getMeasuredHeight() >= mMinHeightInside &&
                getMeasuredHeight() < newHeight)
            newHeight = getMeasuredHeight();

        if (newHeight != orgHeight) {
            if (isFlag(mGravity, Gravity.TOP)) {
                newBot = t + newHeight;
            } else if (isFlag(mGravity, Gravity.BOTTOM)) {
                newTop  = b - newHeight;
            } else if (isFlag(mGravity, Gravity.CENTER_VERTICAL)) {
                int c = (t + b) / 2;
                newTop = c - newHeight/2;
                newBot = t + newHeight;
            }

            changed |= (newTop != t || newBot != b);

            // These are expensive, they invalide layout.
            // They are optimized to only fire if value changed.
            setTop(newTop);
            setBottom(newBot);
        }

        setMeasuredDimension(newWidth, newHeight);
        return changed;
    }

    private View getView(View  view, int viewId) {
        if (view == null && viewId != -1)
            view = getRootView().findViewById(viewId);
        return view;
    }

    static private boolean isFlag(int flags, int flag) {
        return ((flags & flag) == flag);
    }
}