package com.example.myawesomelauncher.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.myawesomelauncher.R

class CirclePageIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : View(context, attrs, defStyle), PageIndicator {
  private var mRadius: Float = 0.toFloat()
  private val mPaintPageFill = Paint(ANTI_ALIAS_FLAG)
  private val mPaintStroke = Paint(ANTI_ALIAS_FLAG)
  private val mPaintFill = Paint(ANTI_ALIAS_FLAG)
  private val mPaintOutside = Paint(ANTI_ALIAS_FLAG)
  private var mCurrentPage: Int = 0
  private var mSnapPage: Int = 0
  private val mCentered: Boolean
  private val mSnap: Boolean
  private var mPageCount: Int = 0
  private val mDividerWidth: Int
  private var mViewPager: ViewPager? = null
  private var mListener: ViewPager.OnPageChangeListener? = null

  init {
    //Load defaults from resources
    val defaultPageColor = ContextCompat.getColor(getContext(), android.R.color.white)
    val defaultFillColor = Color.parseColor("#88FFFFFF")
    val defaultStrokeColor = ContextCompat.getColor(getContext(), android.R.color.transparent)
    val defaultStrokeWidth = 0f
    val defaultRadius = 0f
    val defaultCentered = false
    val defaultSnap = true

    //Retrieve styles attributes
    val a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyle, 0)

    mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered, defaultCentered)
    mPaintPageFill.style = Paint.Style.FILL
    mPaintPageFill.color = a.getColor(R.styleable.CirclePageIndicator_pageColor, defaultPageColor)
    mPaintStroke.style = Paint.Style.STROKE
    mPaintStroke.color = a.getColor(R.styleable.CirclePageIndicator_strokeColor, defaultStrokeColor)
    mPaintStroke.strokeWidth = a.getDimension(R.styleable.CirclePageIndicator_strokeWidth, defaultStrokeWidth)
    mPaintFill.style = Paint.Style.FILL
    mPaintFill.color = a.getColor(R.styleable.CirclePageIndicator_fillColor, defaultFillColor)
    mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius, defaultRadius)
    mDividerWidth = a.getDimensionPixelSize(R.styleable.CirclePageIndicator_divider_width, 0)
    mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, defaultSnap)

    val background = a.getDrawable(R.styleable.CirclePageIndicator_android_background)
    if (background != null) setBackground(background)

    a.recycle()
  }

  override fun setViewPager(view: ViewPager) {
    setViewPager(view, 0)
  }

  override fun setViewPager(view: ViewPager, initialPosition: Int) {
    if (mViewPager === view) {
      return
    }
    if (mViewPager != null) {
      mViewPager?.removeOnPageChangeListener(this)
    }
    if (view.adapter == null) {
      throw IllegalStateException("ViewPager does not have adapter instance.")
    }
    mViewPager = view
    mViewPager?.addOnPageChangeListener(this)
    invalidate()
  }

  private var radius: Float
    get() = mRadius
    set(radius) {
      mRadius = radius
      invalidate()
    }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    if (mCurrentPage >= mPageCount) {
      setCurrentItem(mPageCount - 1)
      return
    }

    val longSize: Int = width
    val longPaddingBefore: Int = paddingLeft
    val longPaddingAfter: Int = paddingRight
    val shortPaddingBefore: Int = paddingTop

    val shortOffset = shortPaddingBefore + mRadius
    var longOffset = longPaddingBefore + mRadius
    if (mCentered) {
      longOffset += (longSize - longPaddingBefore - longPaddingAfter) / 2.0f - mPageCount / 2.0f
    }

    var dX: Float
    var dY: Float

    var pageFillRadius = mRadius
    if (mPaintStroke.strokeWidth > 0) {
      pageFillRadius -= mPaintStroke.strokeWidth / 2.0f
    }

    //Draw stroked circles
    for (iLoop in 0 until mPageCount) {
      val drawLong = longOffset + iLoop.toFloat() * mRadius * 2f + (iLoop * mDividerWidth).toFloat()
      dX = drawLong
      dY = shortOffset
      // Only paint fill if not completely transparent
      if (mPaintPageFill.alpha > 0) {
        canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill)
      }

      // Only paint stroke if a stroke width was non-zero
      if (pageFillRadius != mRadius) {
        canvas.drawCircle(dX, dY, mRadius, mPaintStroke)
      }
    }

    //Draw the filled circle according to the current scroll
    val cx = (if (mSnap) mSnapPage else mCurrentPage) * (2 * mRadius + mDividerWidth)
    dX = longOffset + cx
    dY = shortOffset

    canvas.drawCircle(dX, dY, mRadius, mPaintFill)
  }

  override fun setCurrentItem(item: Int) {
    mCurrentPage = item
    invalidate()
  }

  override fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
    mListener = listener
  }

  override fun notifyDataSetChanged() {
    invalidate()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec))
  }

  /**
   * Determines the width of this view

   * @param measureSpec A measureSpec packed into an int
   * *
   * @return The width of the view, honoring constraints from measureSpec
   */
  private fun measureLong(measureSpec: Int): Int {
    var result: Int
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)
    mPageCount = if (!isInEditMode)
      mViewPager?.adapter?.count ?: 0
    else
      5

    if (specMode == View.MeasureSpec.EXACTLY) {
      //We were told how big to be
      result = specSize
    } else {
      //Calculate the width according the views count
      val count = mPageCount
      result = (paddingLeft.toFloat() + paddingRight.toFloat()
              + count.toFloat() * 2f * mRadius + ((count - 1) * mDividerWidth).toFloat()).toInt()
      //Respect AT_MOST value if that was what is called for by measureSpec
      if (specMode == View.MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize)
      }
    }
    return result
  }

  /**
   * Determines the height of this view

   * @param measureSpec A measureSpec packed into an int
   * *
   * @return The height of the view, honoring constraints from measureSpec
   */
  private fun measureShort(measureSpec: Int): Int {
    var result: Int
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)

    if (specMode == View.MeasureSpec.EXACTLY) {
      //We were told how big to be
      result = specSize
    } else {
      //Measure the height
      result = (2 * mRadius + paddingTop.toFloat() + paddingBottom.toFloat() + 2 * mPaintStroke.strokeWidth + 1f).toInt()
      //Respect AT_MOST value if that was what is called for by measureSpec
      if (specMode == View.MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize)
      }
    }
    return result
  }

  public override fun onRestoreInstanceState(state: Parcelable) {
    val savedState = state as SavedState
    super.onRestoreInstanceState(savedState.superState)
    mCurrentPage = savedState.currentPage
    mSnapPage = savedState.currentPage
    requestLayout()
  }

  public override fun onSaveInstanceState(): Parcelable {
    val superState = super.onSaveInstanceState()
    val savedState = SavedState(superState)
    savedState.currentPage = mCurrentPage
    return savedState
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    if (mListener != null) {
      mListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }
  }

  override fun onPageSelected(position: Int) {
    mCurrentPage = position
    mSnapPage = position
    invalidate()

    if (mListener != null) {
      mListener!!.onPageSelected(position)
    }
  }

  override fun onPageScrollStateChanged(state: Int) {

    if (mListener != null) {
      mListener!!.onPageScrollStateChanged(state)
    }
  }

  internal class SavedState : View.BaseSavedState {
    var currentPage: Int = 0

    constructor(superState: Parcelable) : super(superState)

    private constructor(`in`: Parcel) : super(`in`) {
      currentPage = `in`.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
      super.writeToParcel(dest, flags)
      dest.writeInt(currentPage)
    }

    companion object {

      @JvmField
      val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
        override fun createFromParcel(`in`: Parcel): SavedState = SavedState(`in`)

        override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
      }
    }
  }
}