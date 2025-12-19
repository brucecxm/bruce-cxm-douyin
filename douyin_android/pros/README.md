# 商城APP

一个简单的Android商城应用，使用本地数据存储。

## 功能特性

### 🛍️ 商品浏览
- 商品分类筛选（电子产品、服装、家居用品、图书等）
- 商品搜索功能
- 商品详情展示（名称、价格、评分、库存等）
- 网格布局展示商品列表

### 🛒 购物车功能
- 添加商品到购物车
- 修改商品数量
- 删除购物车商品
- 实时计算总价
- 空购物车提示

### 📦 订单管理
- 确认订单信息
- 填写收货地址和联系电话
- 提交订单
- 订单历史记录

### 🎨 界面设计
- Material Design设计风格
- 响应式布局
- 直观的用户交互
- 现代化的UI组件

## 技术架构

### 数据模型
- `Product`: 商品信息模型
- `CartItem`: 购物车商品项模型
- `Order`: 订单模型

### 数据管理
- `LocalDataManager`: 单例模式的本地数据管理器
- 内存存储，应用重启后数据会重置
- 包含16个模拟商品数据

### UI组件
- `ProductAdapter`: 商品列表适配器
- `CategoryAdapter`: 分类标签适配器
- `CartAdapter`: 购物车列表适配器

### 页面结构
- `MainActivity`: 主Activity，包含导航和购物车按钮
- `FirstFragment`: 商品列表页面
- `SecondFragment`: 购物车页面
- `OrderActivity`: 订单确认页面

## 项目结构

```
app/src/main/java/com/example/myapplication/
├── MainActivity.java              # 主Activity
├── FirstFragment.java             # 商品列表Fragment
├── SecondFragment.java            # 购物车Fragment
├── OrderActivity.java             # 订单Activity
├── model/                         # 数据模型
│   ├── Product.java
│   ├── CartItem.java
│   └── Order.java
├── adapter/                       # RecyclerView适配器
│   ├── ProductAdapter.java
│   ├── CategoryAdapter.java
│   └── CartAdapter.java
└── data/                          # 数据管理
    └── LocalDataManager.java
```

## 使用方法

1. **浏览商品**: 在首页可以浏览所有商品，使用分类筛选或搜索功能
2. **添加购物车**: 点击商品卡片上的"加入购物车"按钮
3. **管理购物车**: 在购物车页面可以修改数量或删除商品
4. **下单**: 在购物车页面点击"结算"按钮进入订单页面
5. **确认订单**: 填写收货信息后提交订单

## 依赖库

- `androidx.appcompat:appcompat`
- `com.google.android.material:material`
- `androidx.constraintlayout:constraintlayout`
- `androidx.navigation:navigation-fragment`
- `androidx.navigation:navigation-ui`
- `androidx.recyclerview:recyclerview`
- `androidx.cardview:cardview`
- `com.github.bumptech.glide:glide` (图片加载)

## 注意事项

- 这是一个演示应用，所有数据都是本地模拟数据
- 应用重启后购物车和订单数据会清空
- 商品图片使用占位符图片
- 支持Android API 24及以上版本
