# 基于MVVM架构的记账本
[![Build Status](https://travis-ci.com/SkywalkerDarren/SimpleAccounting.svg?branch=master)](https://travis-ci.com/SkywalkerDarren/SimpleAccounting)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/github/license/SkywalkerDarren/SimpleAccounting)](https://img.shields.io/github/license/SkywalkerDarren/SimpleAccounting)

## 一、需求分析及主要功能描述

### 1.需求分析

以安卓官方的推荐使用MVVM架构即DataBinding的方式做一个具有实用性质的记账本。
记账本用于记录单位和个人的资产、财产以及在与外界的经济往来中，资产、财产增减变化的情况，其主要的用途是对经济业务和经济往来的备忘备查，对组织和个人的业绩评价，以及为组织的管理者和个人提供决策参考。
记录个人或单位日常的收支的明细，资产增减等经济业务发生情况，有利于理财，并为经济管理及投资决策提供依据等。可以清晰的记录你每天的花销和收入。
在家庭中可以清楚的知道自己的支出情况，从而起到警示作用，以免乱花钱导致家庭出现“赤”字。
支出记录、收入记录、支出分类、收入分类、分类报表、流水报表。收支分类里预设有十几种支出类别及支出项目。

### 2. 主要功能描述

1. 账户分类

需要支持不同的账户钱包进行不同的分类，如现在热门的支付宝，微信，现金等。用户可以根据当前消费进行选择不同的账户钱包类型。

2. 记录账单

用户记录账单的时候可以选择当前的收支类型，钱包类型，可以添加备注，为当前账单选择合适的日期，在输入账单金额的时候使用自定义键盘，支持带计算器功能的账单金额输入。

3. 账目分类

账目分为收入和支出两大类，在两大类中根据日常收支情况又细分了各个小的账目种类，如吃喝，娱乐，工资，兼职等数十种账目分类

4. 账单统计

用折线表展示当年的收入，支出，盈余情况，以及按月份给出每月的收入，支出，盈余情况。
用饼图展示收入或支出中每项账目所占的比例，从高到低排列各个类型。可以切换收入及支出类型，可以自定义时间段来查看。

5. 账单详情

点击账单可以查询该帐单的详细备注，账户类型等各个细节情况，以及当前帐单在当年/月/日的时间点下，在不同的类型，账户，收支中所占的比例，收支平均值等各个细化参数，为用户提供一系列具有参考性的理财思路。

6. 账单列表

展示每个账单的大体情况，如收支金额，账目类型，部分备注。将有一个分隔栏分隔每日的账单，分隔栏有吸顶的设计，显示当前以下的账单日期，当日收支。列表只显示一个月的账单情况。有日期选择功能可以根据月份来查看某月的收支情况。

7. 当前账户情况

展示一系列当前账户情况，如各个钱包金额，调节钱包顺序，当前总的收支情况，盈余情况。给用户一个全局的展现。

8. 遵循Material Design设计

尽量按照官方的安卓Material Design文档进行美化，使用Material Design风格的动效，大量使用CardView，RecyclerView，CoordinatorLayout配合AppbarLayout和NestedScrollView等现代组件来贴合设计风格，不使用老旧的相对布局，转而使用更加智能灵活的ConstraintLayout。

9. 桌面组件

即桌面小部件，可以从桌面就清楚的知道当月的收支情况，点击小部件就可以从桌面快速的新建账单或查询账单，来提高用户使用频率，起到便利的作用。

10. 欢迎页面

进入主界面前进行加载，提高用户的兴趣，为本记账本宣传。在记载的过程中后台初始化数据，减少进入主菜单的延迟。

11. 关于页面

介绍我自己

## 二、软件设计

### 1. 功能模块划分

本项目基于MVVM架构，因此大体分为3部分：Model，View，View Model。
Model中存放有关数据库相关的实体模型，与视图解耦，只与view model交互，为view model提供数据。
View Model用来沟通Model和View Model，主要作用就是解耦视图与模型的关系。
View包括了activity，fragment和layout，他们仅仅和视图相关，view从view model交互来获取数据。
另外有一个Util用于通用工具类，用来存放一些工具相关的类，不属于MVVM。
一套Adapter类，用于存放各种适配器，一套Base用来存放基类。

### 2. 模块设计

1. Model：

Model中不允许存在与视图相关的东西出现。Model主要由核心的3部分，账户account，类别type，账单bill组成，每部分均有一个数据表和一份实例bean，其余还有数据库的表和一个用于多表查询的统计stats。

2. View Model：

为每一种类的实体做一个View Model，而且每个View Model中应持有一个context，如数据库就需要持有context。
每个View Model应该提供足够的交互能力，不允许持有视图中的组件。

3. View：

负责更新视图，与用户交互。不允许持有Model中的任何实体，包括数据库。动画交互，视图刷新，页面切换都应该只存在于View中。即所有的activity，fragment，res都属于view。
跟据谷歌的最新指南，谷歌推荐一个App应该由少数的activity来持有多数的fragment来构成。但应注意生命周期的混乱。

* a) Activity：

一共有8类页面：关于页，欢迎页，设置页，统计页，详单页，主页，账单编辑页，我的账户页。
activity基本不持有控件，所以也不应持有view model，主要作为fragment的容器而存在，如主页则需要持有3个fragment。

* b) Fragment：

fragment内部为具体的交互细节，fragment之间不允许使用广播的方式，而应使用callback的方法进行通信。Fragment内才允许持有view model。

## 三、具体实现

1. 数据库的实现

为数据库建表专门做一个表类，一个数据库helper，为每个核心做一个lab库。每套Lab中包含对实体的各种增删改操作，而增删改操作的方式由helper提供。

* a) Account：

账户数据库建表有以下字段：
账户uuid，账户名称，账户备注，账户收支，账户图片，账户主色调。

* b) Type：

账目数据库建表有以下字段：
账目类别uuid，类别名称，类别图片，类别主色调，类别收支类型。

* c) Bill：

账单数据库建表有以下字段：
账单uuid，账单收支金额，账单日期，账单备注，账单类别uuid，账户uuid。
数据库的图片存放的是assets内的文件名，不能存到drawable中，然后取id值，因为重新构建将有可能导致id错乱，导致图片不正确。

2. 帐单列表等各类列表相关的实现

弃用ListView，转而使用支持库内的recycler view，配合开源库BRVAH（BaseRecyclerViewAdapterHelper），其最新版可以支持DataBinding的方式绑定ViewHolder，极大的方便了适配器的自定义定制。账单列表分为两类，一类是有备注的，一类是没有备注的，分别使用两种不同的布局。在主页面的账单列表中使用开源库PinnedSectionItemDecoration，以装饰器模式实现日期的吸顶。

3. 图片加载的实现

类型图片和账户图片的加载使用Glide异步加载，Glide可以将assets中的资源以异步的方式加载到Bitmap中，可以极大的减少因为安卓UI单线程的关系导致的加载过慢和卡顿的问题。

4. 统计图表的实现

统计图表使用MPAndroidChart库提供方法创建饼图及折线统计图，其数据最终来源于model的stats统计库。使用时需要在对应layout内加入MPAndroidChart的自定义组件。

5. 动画效果的实现

动画效果根据官方文档，遵循material design的设计原则，包括动画速率的控制，弹出方式，转场方式，变化方式等，在列表中使用shareElement的方式做转场，新建账单监听视图的图层变化，在点击时记录当前位置，做从点击处向外扩散的动画。左右滑view pager时以旋转的方式渐隐渐显float action button。

6. 桌面控件的实现

在res的xml目录下创建一份widget.xml，使用appwidget-provider来包裹控件信息。小部件属于server，需要在manifest中注册。需要一个widget类继承AppWidgetProvider，重写onReceive和onUpgrade方法，加入对事件的监听。并编写相对应的布局文件。布局中的背景资源需要可伸缩，因此可以使用nine-patch的方式或自己编xml来写shape作为能够自适应大小的背景。

7. 编辑账单小键盘的实现

自定义键盘根据官方文档的指引，需要先在res下的xml目录里面创建keyboard.xml。根据所需要的keyboard布局创建每个按键的位置，按键返回值，按键标识。在layout下创建一个num_pad.xml放一个merge，里面包含了一对android.inputmethodservice.KeyboardView，里面定义一些键盘的标准样式，用来包裹键盘布局。创建一个NumPad类继承LinearLayout，在该类中inflate刚创建的num_pad.xml。new一个keyboard引用自keyboard.xml。new一个KeyboardView注册Keyboard，然后注册按键事件。在numpad类中写入显示和隐藏键盘的方法。让要使用该布局的文本框注册该numpad类。

8. 编辑账单的计算器实现

先使用有限状态机判定表达式是否合法，若合法则使用两个栈实现带有优先级计算器，计算器支持高精度计算。

计算表达式由两个栈（val，flag）管理，计算则交给calcExp方法处理。
运算符为初始设定，Operator定义接口方法，Operators中由HashMap初始化实现并存储运算操作符。
这样如果添加新的操作符时只需从Operators类的HashMap中添加即可。

9. 欢迎界面的实现

欢迎界面使用的是一个viewpager和linearlayout组合而成的带圆点指示器的导航页，在开启导航页的时候会在后台打开一个handler，来初始化数据库。

## 心得

这个记账本最初是用最简单的MVC架构编写的，但是由于MVC架构的controller部分太过臃肿，activity的各种视图交互与模型交互混杂在一起，一不小心就上几百行的代码量，容易导致后期维护痛苦。因此后来改为谷歌推崇的先进的MVVM架构。MVVM架构有效的解耦了各部分之间的关联度，使得各组件能有效的复用，view model中的bind则用了注解与反射的方式，体验到了AOP面向切面编程带来的方便。后来activity管理愈发复杂，遂改为有少数核心的activity管理多数fragment的方式，这样管理会更高效。随着图片资源增多和数据量增大，我增加了欢迎页来减少延迟感，欢迎页由单张图片改为了多张图片的导航式欢迎界面，在欢迎页处理数据初始化大大的提速了启动时间。数据库一开始存图片的方式是取drawable内的id值，后来发现了bug，即id值在rebuild后有可能发生变动，而数据库不会变动，将导致图片错乱发生。后改为Blob的方式存取资源，这又导致了数据存取过慢的问题，在Stream中要经过多次到Bitmap的压缩与转换还原，极大的浪费了系统资源，也增大了数据库大小，这些可以从Android Profiler中观察flame graph得到Bitmap转换时调用方法的资源耗费情况。为解决这个问题我将图片资源从drawable中换到了assets中，这样系统是不会在一开始时就编译assets的，适合放一些图片，音视频等大型资源，使用Assets Manager即可访问到assets资源，只是稍微丧失了一点在IDE内即时预览的能力，因此又把数据库中的的图片资源改为了assets中的文件路径名，即可加速数据库存取。但这样并没有解决图片加载的问题，想到有Rxjava，Glide这样的库可以异步加载UI，可以不再受UI单线程的限制，而Glide专为图片异步加载而生，所以用了Glide后极大的提高了软件的流畅性。

在整体设计完成后，我们考虑先开发出简单的记账软件，然后逐步细化，因此，在详细设计时，我们简化了一些东西，先开发出一个基本原型，用以验证技术并进一步明确需求。然后，对部分技术进行改进和细化，最后，再次基础上不断的迭代进行，由于我的水平有限，我最初的设计并不一定是好的设计，只有不断的试验和改进，才能开发出好的软件，当然，整体架构设计非常重要，这将很大程度上决定软件的质量和适应需求变更的能力。
