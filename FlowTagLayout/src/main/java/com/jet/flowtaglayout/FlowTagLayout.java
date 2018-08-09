package com.jet.flowtaglayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author：Jet啟思
 * @date:2018/8/7 11:44
 */
public class FlowTagLayout extends ViewGroup {

    private Context context;
    private Rect[] childrenBounds;

    private List<String> tagList;
    private OnTagClickListener onTagClickListener;

    private int defaultColor = Color.parseColor("#666666");
    private float defaultTextSize = Utils.spToPx(16);

    /****************** 自定义的Attribute *************************/
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private int leftPadding;
    private int rightPadding;
    private int topPadding;
    private int bottomPadding;
    private int background;
    private int textColor;
    private float textSize;

    public FlowTagLayout(Context context) {
        super(context);
        this.context = context;
    }

    public FlowTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public FlowTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowTagLayout);
        leftMargin = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_leftMargin, 0);
        rightMargin = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_rightMargin, 0);
        topMargin = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_topMargin, 0);
        bottomMargin = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_bottomMargin, 0);
        leftPadding = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_leftPadding, 0);
        rightPadding = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_rightPadding, 0);
        topPadding = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_topPadding, 0);
        bottomPadding = (int) typedArray.getDimension(R.styleable.FlowTagLayout_item_bottomPadding, 0);
        background = typedArray.getResourceId(R.styleable.FlowTagLayout_item_background, -1);
        textColor = typedArray.getColor(R.styleable.FlowTagLayout_item_textColor, defaultColor);
        textSize = typedArray.getDimension(R.styleable.FlowTagLayout_item_textSize, defaultTextSize);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 已用宽度
        int widthUsed = getPaddingLeft();
        // 已用高度
        int heightUsed = getPaddingTop();
        // 当前行高度
        int lineHeight = 0;
        // 当前行宽度
        int lineWidth = 0;
        // 本身的宽度
        int parentWidth = 0;
        // 本身的高度
        int parentHeight = 0;

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();
        if (childrenBounds == null) {
            childrenBounds = new Rect[childCount];
        } else if (childrenBounds.length < childCount) {
            childrenBounds = Arrays.copyOf(childrenBounds, childCount);
        }

        // 遍历测量子view
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量子view
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 判断是否换行（1.不是非限制  2.已用宽度+当前添加的child宽度+左右的padding 是否大于 父view自身测量的宽度）
            int paddingRight = getPaddingRight();
            Log.e("20000", paddingRight + "：padding");
            Log.e("20000", lp.leftMargin + "：margin");
            Log.e("20000", child.getMeasuredWidth() + "：width");
            widthUsed = widthUsed + leftMargin;
            if (specWidthMode != MeasureSpec.UNSPECIFIED &&
                    widthUsed + child.getMeasuredWidth() + rightMargin + getPaddingRight() > specWidth) {
                parentWidth = specWidth;
                widthUsed = getPaddingLeft() + leftMargin;
                // 已用高度 = 之前已用高度 + 当前行的高度
                heightUsed = heightUsed + lineHeight;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }

            Rect childBounds = childrenBounds[i];
            if (childBounds == null) {
                childBounds = childrenBounds[i] = new Rect();
            }

            lineHeight = child.getMeasuredHeight() + topMargin + bottomMargin;

            // 保存child的位置
            childBounds.set(widthUsed, heightUsed + topMargin,
                    widthUsed + child.getMeasuredWidth(), heightUsed + lineHeight - bottomMargin);
            // 当前行已用的宽度
            widthUsed = widthUsed + child.getMeasuredWidth() + rightMargin;
        }
        // 因为换行parentWidth会被赋值，所以需要对比是否换行过赋值后的parentWidth
        parentWidth = Math.max(parentWidth, widthUsed + getPaddingRight());
        // 父view的高度 = 已用高度 + 当前行高度 + padding
        parentHeight = heightUsed + lineHeight + getPaddingBottom();

        setMeasuredDimension(resolveSizeAndState(parentWidth, widthMeasureSpec, 0),
                resolveSizeAndState(parentHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            Rect childRect = childrenBounds[i];
            view.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 必须重写这个方法，不然measure的时候child.getLayoutParams()的margin是为0的
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 添加tags列表
     *
     * @param list
     */
    public void addTags(List<String> list) {
        if (tagList == null) {
            tagList = Collections.synchronizedList(new ArrayList<String>());
        } else {
            tagList.clear();
        }
        tagList.addAll(list);
        loadTagView();
    }

    /**
     * 初始化tags
     */
    private void loadTagView() {
        removeAllViews();
        for (int i = 0; i < tagList.size(); i++) {
            setTagContent(i);
        }
    }

    /**
     * 添加到尾部
     *
     * @param label
     */
    public void addTag(String label) {
        tagList.add(label);
        setTagContent(tagList.size() - 1);
    }

    /**
     * 添加tag到index位置
     *
     * @param index
     * @param label
     */
    public void addTagOfIndex(int index, String label) {
        tagList.add(index, label);
        setTagOfIndex(index);
    }

    /**
     * 初始化item
     *
     * @param i
     */
    @SuppressLint("NewApi")
    private void setTagContent(int i) {
        LayoutInflater.from(context).inflate(R.layout.item_tag, this);
        // 这里用是getChildAt(index)获取子view，不能直接拿上面inflate的view
        TextView textView = getChildAt(i).findViewById(R.id.item_text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(leftPadding, topPadding, rightPadding, bottomPadding);
        textView.setLayoutParams(lp);
        textView.setText(tagList.get(i));
        textView.setTextColor(textColor);
        textView.setTextSize(Utils.pxToSp(textSize));
        final int finalI = i;
        if (background == -1) {
            getChildAt(i).setBackground(null);
        } else {
            getChildAt(i).setBackground(getResources().getDrawable(background));
        }
        getChildAt(i).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagClickListener != null) {
                    onTagClickListener.tagClick(finalI);
                }
            }
        });
    }

    /**
     * 添加到index位置，使用的是addView(view ,index)，最后需要把点击事件重置
     *
     * @param i
     */
    @SuppressLint("NewApi")
    private void setTagOfIndex(int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag, null, false);
        addView(view, i);
        // 这里用是getChildAt(index)获取子view，不能直接拿上面inflate的view
        TextView textView = getChildAt(i).findViewById(R.id.item_text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(leftPadding, topPadding, rightPadding, bottomPadding);
        textView.setLayoutParams(lp);
        textView.setText(tagList.get(i));
        textView.setTextColor(textColor);
        textView.setTextSize(Utils.pxToSp(textSize));

        if (background == -1) {
            getChildAt(i).setBackground(null);
        } else {
            getChildAt(i).setBackground(getResources().getDrawable(background));
        }
        for (int j = 0; j < getChildCount(); j++) {
            // 去掉点击事件
            getChildAt(j).setOnClickListener(null);
            final int finalJ = j;
            // 重新绑定点击监听
            getChildAt(j).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null) {
                        onTagClickListener.tagClick(finalJ);
                    }
                }
            });
        }
    }

    /**
     * 移除最后一个tag
     */
    public void removeTag() {
        getChildAt(tagList.size() - 1).setOnClickListener(null);
        removeViewAt(tagList.size() - 1);
        tagList.remove(tagList.size() - 1);
    }

    /**
     * 移除index位置的tag
     *
     * @param index
     */
    public void removeTagOfIndex(int index) {
        getChildAt(index).setOnClickListener(null);
        removeViewAt(index);
        tagList.remove(index);
        for (int j = index; j < getChildCount(); j++) {
            // 去掉点击事件
            getChildAt(j).setOnClickListener(null);
            final int finalJ = j;
            // 重新绑定点击监听
            getChildAt(j).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null) {
                        onTagClickListener.tagClick(finalJ);
                    }
                }
            });
        }
    }

    public void setTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    public interface OnTagClickListener {
        void tagClick(int position);
    }

}
