# FlowTagLayoutDemo

#### 为什么写这个库？

项目中有时候会有这样的需求，比如*兴趣爱好的选择、搜索历史标签*等，自带的Android控件实现起来贼麻烦，所以自己撸一个呗，当然前提是这个库开发者必须用得简洁高效，而且功能到位。

#### 特点是什么？

1、最基本的功能，标签布局并且自动换行。

2、自定义了各种styleable，在xml布局文件就可以设置tag的属性。

3、可以单选、多选，这个全有开发者自由选择，只要自己加background就完事。

4、最后一点，简洁高效。

#### 如何使用？

对，在使用之前看看效果，来，上效果图

![效果图](https://upload-images.jianshu.io/upload_images/5596129-ca5481c8dadc17bb.gif?imageMogr2/auto-orient/strip)


首先，引入这个库

这是GitHub地址[自定义标签布局FloaTagLayout](https://github.com/jetLee92/FlowTagLayoutDemo)，源码项目里面有，这篇文章暂不说该控件的原理，之后会出源码的分析。

**gradle：**

    compile 'com.jetlee:FlowTagLayout:1.0.1'

**Maven：**

    <dependency>
      <groupId>com.jetlee</groupId>
      <artifactId>FlowTagLayout</artifactId>
      <version>1.0.1</version>
      <type>pom</type>
    </dependency>

把库依赖到项目中后，在布局中使用，并配置各种属性

    <com.jet.flowtaglayout.FlowTagLayout
        android:id="@+id/flowTagLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:item_leftMargin="8dp"  // tag的左外边距
        app:item_rightMargin="8dp"  // tag的右外边距
        app:item_topMargin="8dp"  // tag的上外边距
        app:item_bottomMargin="8dp"  // tag的下外边距
        app:item_leftPadding="12dp"  // tag的左内边距
        app:item_rightPadding="12dp"  // tag的右内边距
        app:item_topPadding="6dp"  // tag的上内边距
        app:item_bottomPadding="6dp"  // tag的下内边距
        app:item_background="@drawable/ripple_gray"  // tag的item背景效果
        app:item_textColor="#F25A25"  // tag的text颜色
        app:item_textSize="16sp"  // tag的text字体大小
		/>

下面列出FlowTagLayout的重要方法（注意，这些方法可以共用，也可以只用一种，看各自的需求）

先准备几条数据，都是tag的label

	List<String> dataList = new ArrayList<>();
	dataList.add("数据结构");
	dataList.add("算法");
	dataList.add("Java");
	dataList.add("多线程编程");

	// 添加tag
	flowTagLayout.addTags(dataList);  // 添加tag的列表，该方法会把之前的tags全部清空
	flowTagLayout.addTag("Kotlin");  // 在尾部添加一个tag
	flowTagLayout.addTagOfIndex(2,"自定义view");  // 在指定的位置插入tag，示例为在index为2，也就是第三个位置插入tag

	// 移除tag
	flowTagLayout.removeTag();  // 移除尾部tag
	flowTagLayout.removeTagOfIndex(3);  // 移除指定位置的tag，示例为移除index为3，也就是第四个tag移除

最后就是绑定点击事件

	flowTagLayout.setTagClickListener(new FlowTagLayout.OnTagClickListener() {
	    @Override
	    public void tagClick(int position) {
			// getChildAt(position)方法在这很实用
	        flowTagLayout.getChildAt(position).setSelected(!flowTagLayout.getChildAt(position).isSelected());
	    }
	});


**这个库的使用就是这么简单，喜欢的给个star呗，哈哈哈。**


