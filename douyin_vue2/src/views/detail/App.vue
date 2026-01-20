<template>
  <div class="product-detail">
    <!-- 顶部导航 -->
    <header class="detail-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">商品详情</div>
      <div class="header-right" @click="shareProduct">
        <i class="share-icon">📤</i>
      </div>
    </header>

    <!-- 商品图片轮播 -->
    <div class="product-images">
      <div class="image-slider">
        <img :src="product.image" :alt="product.title" />
      </div>
      <div class="image-indicator">
        <span class="indicator-dot active"></span>
        <span class="indicator-dot"></span>
        <span class="indicator-dot"></span>
      </div>
    </div>

    <!-- 商品基本信息 -->
    <div class="product-info">
      <h1 class="product-title">{{ product.title }}</h1>
      <div class="price-section">
        <span class="price">¥{{ product.price }}</span>
        <span class="original-price">¥{{ product.originalPrice }}</span>
      </div>
      <div class="meta-info">
        <span class="location">{{ product.location }}</span>
        <span class="time">{{ product.time }}</span>
        <span class="views">浏览 {{ product.views }} 次</span>
      </div>
    </div>

    <!-- 商品标签 -->
    <div class="product-tags">
      <span class="tag" v-for="tag in product.tags" :key="tag">{{ tag }}</span>
    </div>

    <!-- 商品详细描述 -->
    <div class="product-description">
      <h3 class="section-title">商品描述</h3>
      <div class="description-content">
        <p>{{ product.description }}</p>
        <div class="description-images">
          <img :src="product.image" alt="商品描述图片" />
          <img :src="product.image" alt="商品描述图片" />
        </div>
      </div>
    </div>

    <!-- 商品评论 -->
    <div class="product-comments">
      <h3 class="section-title">商品评论</h3>

      <!-- 评论统计 -->
      <div class="comments-stats">
        <div class="average-rating">
          <div class="rating-value">{{ comments.averageRating }}</div>
          <div class="rating-stars">
            <span
              class="star"
              v-for="i in 5"
              :key="i"
              :class="{ active: i <= Math.round(comments.averageRating) }"
              >⭐</span
            >
          </div>
        </div>
        <div class="rating-distribution">
          <div
            class="distribution-item"
            v-for="item in comments.ratingDistribution"
            :key="item.rating"
          >
            <span class="rating-label">{{ item.rating }}星</span>
            <div class="rating-bar">
              <div
                class="rating-bar-fill"
                :style="{ width: item.percentage + '%' }"
              ></div>
            </div>
            <span class="rating-count">{{ item.count }}</span>
          </div>
        </div>
      </div>

      <!-- 发表评论 -->
      <div class="comment-form-section">
        <h4 class="comment-form-title">发表评论</h4>
        <div class="comment-form">
          <div class="comment-rating">
            <span class="rating-label">评分：</span>
            <div class="rating-stars">
              <span
                class="star"
                v-for="i in 5"
                :key="i"
                :class="{ active: i <= commentForm.rating }"
                @click="setRating(i)"
                >⭐</span
              >
            </div>
          </div>
          <textarea
            v-model="commentForm.content"
            placeholder="请输入您的评论..."
            rows="4"
          ></textarea>
          <button class="submit-comment-btn" @click="submitComment">
            提交评论
          </button>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="comments-list">
        <h4 class="comments-list-title">
          全部评论 ({{ comments.list.length }})
        </h4>
        <div
          class="comment-item"
          v-for="comment in comments.list"
          :key="comment.id"
        >
          <div class="comment-header">
            <div class="commenter-info">
              <div class="commenter-avatar">
                <img :src="comment.user.avatar" :alt="comment.user.nickname" />
              </div>
              <div class="commenter-details">
                <div class="commenter-nickname">
                  {{ comment.user.nickname }}
                </div>
                <div class="comment-time">{{ comment.time }}</div>
              </div>
            </div>
            <div class="comment-rating">
              <span
                class="star"
                v-for="i in 5"
                :key="i"
                :class="{ active: i <= comment.rating }"
                >⭐</span
              >
            </div>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
          <div class="comment-actions">
            <span class="action-item" @click="likeComment(comment)">
              <i class="action-icon">{{ comment.isLiked ? '❤️' : '🤍' }}</i>
              <span class="action-count">{{ comment.likes }}</span>
            </span>
            <span class="action-item">
              <i class="action-icon">💬</i>
              <span class="action-count">{{ comment.replies }}</span>
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 卖家信息 -->
    <div class="seller-info">
      <h3 class="section-title">卖家信息</h3>
      <div class="seller-card" @click="goToSellerProfile">
        <div class="seller-avatar">
          <img :src="seller.avatar" :alt="seller.nickname" />
        </div>
        <div class="seller-details">
          <div class="seller-nickname">{{ seller.nickname }}</div>
          <div class="seller-rating">
            <span class="star">⭐</span>
            <span class="rating-text">{{ seller.rating }}分</span>
            <span class="transaction-count"
              >{{ seller.transactionCount }}笔交易</span
            >
          </div>
        </div>
        <div class="seller-more">
          <i class="more-icon">→</i>
        </div>
      </div>

      <!-- 卖家其他商品 -->
      <div class="seller-other-products">
        <h4 class="other-products-title">卖家的其他商品</h4>
        <div class="other-product-list">
          <div
            class="other-product-item"
            v-for="item in seller.otherProducts"
            :key="item.id"
            @click="goToDetail(item.id)"
          >
            <div class="other-product-image">
              <img :src="item.image" :alt="item.title" />
            </div>
            <div class="other-product-info">
              <div class="other-product-title">{{ item.title }}</div>
              <div class="other-product-price">¥{{ item.price }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <footer class="detail-bottom-nav">
      <div class="nav-item" @click="addToFavorite">
        <i class="nav-icon">❤️</i>
        <span class="nav-text">收藏</span>
      </div>
      <div class="nav-item" @click="addToCart">
        <i class="nav-icon">🛒</i>
        <span class="nav-text">购物车</span>
      </div>
      <div class="nav-item primary" @click="buyNow">
        <i class="nav-icon">💰</i>
        <span class="nav-text">立即购买</span>
      </div>
      <div class="nav-item primary" @click="contactSeller">
        <i class="nav-icon">💬</i>
        <span class="nav-text">联系卖家</span>
      </div>
    </footer>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { get, post, del } from '../../utils/apiUtils';

export default {
  name: 'ProductDetailPage',
  data() {
    return {
      // 静态商品数据
      product: {
        id: 1,
        title: 'iPhone 13 Pro 256GB 远峰蓝 99新 原装无拆修',
        price: 5999,
        originalPrice: 8999,
        location: '北京 朝阳区',
        time: '2小时前发布',
        views: 128,
        image:
          'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
        tags: ['99新', '原装', '无拆修', '当面交易'],
        description:
          'iPhone 13 Pro 256GB 远峰蓝，99新，原装无拆修，所有功能正常，电池健康95%。配件齐全，包括充电器、数据线、耳机、包装盒。支持当面交易，可验机。' +
          '购买后赠送手机壳和贴膜。有意者联系，非诚勿扰。'
      },
      // 静态卖家数据
      seller: {
        id: 101,
        nickname: '数码达人',
        avatar:
          'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
        rating: 4.9,
        transactionCount: 156,
        otherProducts: [
          {
            id: 2,
            title: 'AirPods Pro 第二代',
            price: 1899,
            image:
              'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          },
          {
            id: 3,
            title: 'MacBook Pro 16英寸 M1 Pro',
            price: 12999,
            image:
              'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
          }
        ]
      },
      // 评论数据
      comments: {
        averageRating: 4.5,
        ratingDistribution: [
          { rating: 5, count: 12, percentage: 60 },
          { rating: 4, count: 5, percentage: 25 },
          { rating: 3, count: 2, percentage: 10 },
          { rating: 2, count: 1, percentage: 5 },
          { rating: 1, count: 0, percentage: 0 }
        ],
        list: [
          {
            id: 1,
            user: {
              id: 201,
              nickname: '买家1',
              avatar:
                'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
            },
            rating: 5,
            content:
              '商品非常好，与描述一致，卖家很nice，物流也很快，推荐购买！',
            time: '2025-12-20 14:30',
            likes: 15,
            replies: 3,
            isLiked: false
          },
          {
            id: 2,
            user: {
              id: 202,
              nickname: '买家2',
              avatar:
                'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
            },
            rating: 4,
            content:
              '商品不错，就是价格稍微有点高，不过一分钱一分货，总体满意。',
            time: '2025-12-18 10:20',
            likes: 8,
            replies: 1,
            isLiked: true
          },
          {
            id: 3,
            user: {
              id: 203,
              nickname: '买家3',
              avatar:
                'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
            },
            rating: 5,
            content:
              '非常满意的一次购物体验，卖家很专业，商品质量也很好，下次还会光顾！',
            time: '2025-12-15 16:45',
            likes: 12,
            replies: 2,
            isLiked: false
          }
        ]
      },
      // 评论表单数据
      commentForm: {
        rating: 5,
        content: ''
      }
    };
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('商品详情');
    // 调用API获取商品详情
    this.fetchProductDetail();
  },
  methods: {
    // 获取商品详情API
    async fetchProductDetail() {
      try {
        // 显示加载提示
        nativeUtils.showLoading('加载中...');

        // 获取URL中的商品ID
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');

        if (!productId) {
          nativeUtils.hideLoading();
          nativeUtils.showToast('商品ID无效', 1500);
          return;
        }

        // 调用真实API获取商品详情
        const response = await get(`/product/${productId}`);

        // 检查API响应
        if (response.code === 0 && response.data) {
          // API请求成功，更新商品数据
          this.product = {
            ...response.data,
            image:
              response.data.images && response.data.images.length > 0
                ? response.data.images[0]
                : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
            originalPrice: response.data.originalPrice || response.data.price,
            tags: response.data.tags || [],
            location: response.data.location || '未知',
            time: response.data.createdAt
              ? this.formatTime(response.data.createdAt)
              : '未知',
            views: Math.floor(Math.random() * 1000) + 100 // 模拟浏览量
          };

          // 设置卖家数据
          const sellerId = response.data.user?.id || response.data.userId || 1;
          this.seller = {
            id: sellerId,
            nickname: response.data.user?.nickname || `卖家${sellerId}`,
            avatar:
              response.data.user?.avatar ||
              'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200',
            rating: Math.random() * 1 + 4, // 4-5分
            transactionCount: Math.floor(Math.random() * 200) + 50,
            otherProducts: []
          };

          // 获取卖家其他商品
          this.fetchSellerOtherProducts(this.seller.id);

          // 获取商品评论
          await this.fetchComments(productId);

          // 添加最近浏览记录
          this.addRecentView(productId);
        } else {
          // API返回数据格式不正确
          nativeUtils.showToast('获取商品详情失败', 1500);
        }
      } catch (error) {
        console.error('获取商品详情失败:', error);
        nativeUtils.showToast('获取商品详情失败', 1500);
      } finally {
        // 隐藏加载提示
        nativeUtils.hideLoading();
      }
    },

    // 添加最近浏览记录
    async addRecentView(productId) {
      try {
        // 检查用户是否登录
        const userInfo = localStorage.getItem('user');
        if (!userInfo) {
          return; // 未登录用户不记录浏览历史
        }

        const user = JSON.parse(userInfo);
        const userId = user.id;

        // 调用API添加浏览记录
        await post('/newshop/recent-view', {
          userId: userId,
          productId: productId
        });

        // 不显示提示，静默添加
      } catch (error) {
        console.error('添加浏览记录失败:', error);
        // 不显示错误提示，避免影响用户体验
      }
    },

    // 获取商品评论API
    async fetchComments(productId) {
      try {
        // 获取评论统计信息
        const statsResponse = await get(`/comments/stats/${productId}`);
        // 获取评论列表
        const listResponse = await get(`/comments/product/${productId}`);

        if (statsResponse.code === 0 && statsResponse.data) {
          // 更新评论统计信息
          this.comments.averageRating = statsResponse.data.averageRating || 0;
          this.comments.ratingDistribution = statsResponse.data
            .ratingDistribution || [
            { rating: 5, count: 0, percentage: 0 },
            { rating: 4, count: 0, percentage: 0 },
            { rating: 3, count: 0, percentage: 0 },
            { rating: 2, count: 0, percentage: 0 },
            { rating: 1, count: 0, percentage: 0 }
          ];
        }

        if (listResponse.code === 0 && listResponse.data) {
          // 更新评论列表
          this.comments.list = listResponse.data.map((comment) => ({
            id: comment.id,
            user: {
              id: comment.user.id,
              nickname: comment.user.nickname || `用户${comment.user.id}`,
              avatar:
                'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
            },
            rating: comment.rating,
            content: comment.content,
            time: comment.createdAt
              ? new Date(comment.createdAt).toLocaleString('zh-CN')
              : '未知时间',
            likes: comment.likes || 0,
            replies: comment.replies || 0,
            isLiked: false
          }));
        }
      } catch (error) {
        console.error('获取商品评论失败:', error);
        // 失败时使用默认数据
        this.comments = {
          averageRating: 4.5,
          ratingDistribution: [
            { rating: 5, count: 12, percentage: 60 },
            { rating: 4, count: 5, percentage: 25 },
            { rating: 3, count: 2, percentage: 10 },
            { rating: 2, count: 1, percentage: 5 },
            { rating: 1, count: 0, percentage: 0 }
          ],
          list: []
        };
      }
    },

    // 获取卖家其他商品
    async fetchSellerOtherProducts(sellerId) {
      try {
        const response = await get(`/newshop/product/user/${sellerId}`);
        if (response.code === 0 && response.data) {
          // 过滤掉当前商品
          this.seller.otherProducts = response.data
            .filter((product) => product.id !== this.product.id)
            .map((product) => ({
              id: product.id,
              title: product.title,
              price: product.price,
              image:
                product.images && product.images.length > 0
                  ? product.images[0]
                  : 'https://gips1.baidu.com/it/u=1025173963,4205445645&fm=3028&app=3028&f=JPEG&fmt=auto?w=3200&h=3200'
            }));
        }
      } catch (error) {
        console.error('获取卖家其他商品失败:', error);
      }
    },

    // 格式化时间
    formatTime(timeString) {
      const date = new Date(timeString);
      const now = new Date();
      const diff = now - date;

      const minutes = Math.floor(diff / (1000 * 60));
      const hours = Math.floor(diff / (1000 * 60 * 60));
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));

      if (minutes < 60) {
        return `${minutes}分钟前`;
      } else if (hours < 24) {
        return `${hours}小时前`;
      } else {
        return `${days}天前`;
      }
    },
    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },
    // 分享商品
    shareProduct() {
      nativeUtils.showToast('分享功能开发中', 1500);
    },
    // 跳转到商品详情（卖家其他商品）
    goToDetail(productId) {
      routerUtils.navigateTo(`/detail.html?id=${productId}`);
    },
    // 跳转到卖家主页
    goToSellerProfile() {
      nativeUtils.showToast('卖家主页开发中', 1500);
    },
    // 收藏商品
    async addToFavorite() {
      try {
        // 检查用户是否登录
        const userInfo = localStorage.getItem('user');
        if (!userInfo) {
          nativeUtils.showToast('请先登录', 1500);
          setTimeout(() => {
            routerUtils.navigateTo('/newshop/login.html');
          }, 1500);
          return;
        }

        const user = JSON.parse(userInfo);
        const userId = user.id;

        // 调用API收藏商品
        const response = await post('/newshop/favorite', {
          userId: userId,
          productId: this.product.id
        });

        if (response.code === 0) {
          nativeUtils.showToast('收藏成功', 1500);
        } else {
          nativeUtils.showToast(response.message || '收藏失败', 1500);
        }
      } catch (error) {
        console.error('收藏商品失败:', error);
        nativeUtils.showToast('收藏失败，请重试', 1500);
      }
    },
    // 加入购物车
    async addToCart() {
      try {
        // 检查用户是否登录
        const userInfo = localStorage.getItem('user');
        if (!userInfo) {
          nativeUtils.showToast('请先登录', 1500);
          setTimeout(() => {
            routerUtils.navigateTo('/newshop/login.html');
          }, 1500);
          return;
        }

        const user = JSON.parse(userInfo);
        const userId = user.id;

        // 调用API加入购物车
        const response = await post('/newshop/cart', {
          userId: userId,
          productId: this.product.id,
          quantity: 1
        });

        if (response.code === 0) {
          // 显示成功提示
          nativeUtils.showToast('已加入购物车', 1500);
        } else {
          nativeUtils.showToast(response.message || '加入购物车失败', 1500);
        }
      } catch (error) {
        console.error('加入购物车失败:', error);
        nativeUtils.showToast('加入购物车失败', 1500);
      }
    },
    // 联系卖家
    contactSeller() {
      try {
        // 保存聊天记录到本地存储
        this.saveChatRecord();
        // 跳转到聊天页面，传递卖家ID和商品ID
        routerUtils.navigateTo(
          `/newshop/chat.html?userId=${this.seller.id}&productId=${this.product.id}`
        );
      } catch (error) {
        console.error('保存聊天记录失败:', error);
        // 即使保存失败，也跳转到聊天页面
        routerUtils.navigateTo(
          `/newshop/chat.html?userId=${this.seller.id}&productId=${this.product.id}`
        );
      }
    },

    // 保存聊天记录到本地存储
    saveChatRecord() {
      // 获取现有的聊天记录
      let chatMessages = [];
      const chatMessagesStr = localStorage.getItem('chatMessages');
      if (chatMessagesStr) {
        chatMessages = JSON.parse(chatMessagesStr);
      }

      // 检查是否已存在与该卖家的聊天记录
      const existingChatIndex = chatMessages.findIndex(
        (msg) => msg.sender.id === this.seller.id
      );

      // 创建聊天预览消息
      const previewMsg = `您好，我对您的商品"${this.product.title}"感兴趣，想了解更多信息`;

      // 生成当前时间
      const now = new Date();
      let timeStr;
      const minutes = Math.floor(
        (now - new Date(now.setHours(0, 0, 0, 0))) / (1000 * 60)
      );
      if (minutes < 60) {
        timeStr = `${minutes}分钟前`;
      } else if (minutes < 24 * 60) {
        timeStr = `${Math.floor(minutes / 60)}小时前`;
      } else {
        timeStr = `${Math.floor(minutes / (24 * 60))}天前`;
      }

      if (existingChatIndex >= 0) {
        // 更新现有聊天记录
        chatMessages[existingChatIndex] = {
          ...chatMessages[existingChatIndex],
          preview: previewMsg,
          time: timeStr,
          unreadCount: chatMessages[existingChatIndex].unreadCount + 1,
          updatedAt: Date.now()
        };
      } else {
        // 创建新的聊天记录
        const newChat = {
          id: Date.now(), // 使用时间戳作为唯一ID
          sender: {
            id: this.seller.id,
            nickname: this.seller.nickname,
            avatar: this.seller.avatar
          },
          preview: previewMsg,
          time: timeStr,
          unreadCount: 1,
          updatedAt: Date.now()
        };
        chatMessages.push(newChat);
      }

      // 按更新时间降序排序
      chatMessages.sort((a, b) => b.updatedAt - a.updatedAt);

      // 保存到localStorage
      localStorage.setItem('chatMessages', JSON.stringify(chatMessages));
    },

    // 立即购买
    async buyNow() {
      // 检查用户是否登录
      const userInfo = localStorage.getItem('user');
      if (!userInfo) {
        nativeUtils.showToast('请先登录', 1500);
        setTimeout(() => {
          routerUtils.navigateTo('/newshop/login.html');
        }, 1500);
        return;
      }

      try {
        const user = JSON.parse(userInfo);
        const userId = user.id;

        // 创建订单数据
        const orderData = {
          userId: userId,
          productId: Number(this.product.id), // 确保productId是数字类型
          quantity: 1, // 确保quantity是数字类型
          totalPrice: Number(this.product.price), // 确保totalPrice是数字类型
          status: 'pending'
          // 订单创建时间会在后端自动设置
        };

        // 调用API创建订单
        const response = await post('/newshop/order', orderData);

        if (response.code === 0 && response.data) {
          // 订单创建成功，跳转到订单详情页面
          routerUtils.navigateTo(
            `/newshop/orderDetail.html?id=${response.data.id}`
          );
        } else {
          nativeUtils.showToast(response.message || '创建订单失败', 1500);
        }
      } catch (error) {
        console.error('创建订单失败:', error);
        nativeUtils.showToast('创建订单失败，请重试', 1500);
        console.log('错误详情:', error); // 打印详细错误信息
      }
    },

    // 设置评论评分
    setRating(rating) {
      this.commentForm.rating = rating;
    },

    // 提交评论
    async submitComment() {
      if (!this.commentForm.content.trim()) {
        nativeUtils.showToast('请输入评论内容', 1500);
        return;
      }

      try {
        // 获取当前用户ID（从localStorage的user对象中获取）
        let userId = 1;
        try {
          const userStr = localStorage.getItem('user');
          if (userStr) {
            const user = JSON.parse(userStr);
            userId = user.id || 1;
          }
        } catch (error) {
          console.error('解析用户信息失败:', error);
        }

        // 调用真实API提交评论
        const response = await post('/newshop/comments', {
          productId: this.product.id,
          userId: userId,
          rating: this.commentForm.rating,
          content: this.commentForm.content
        });

        // 检查API响应
        if (response.code === 0 && response.data) {
          nativeUtils.showToast('评论提交成功', 1500);

          // 清空表单
          this.commentForm.rating = 5;
          this.commentForm.content = '';

          // 重新获取评论列表，更新评论数据
          await this.fetchComments(this.product.id);
        } else {
          nativeUtils.showToast('评论提交失败', 1500);
        }
      } catch (error) {
        console.error('提交评论失败:', error);
        nativeUtils.showToast('提交评论失败', 1500);
      }
    },

    // 点赞评论
    async likeComment(comment) {
      try {
        if (comment.isLiked) {
          // 取消点赞
          await del(`/comments/${comment.id}/like`);
          comment.likes--;
        } else {
          // 点赞
          await post(`/comments/${comment.id}/like`);
          comment.likes++;
        }
        // 更新点赞状态
        comment.isLiked = !comment.isLiked;
      } catch (error) {
        console.error('点赞评论失败:', error);
        nativeUtils.showToast('点赞失败', 1500);
      }
    }
  }
};
</script>

<style scoped>
.product-detail {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航 */
.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left,
.header-right {
  cursor: pointer;
}

.back-icon {
  font-size: 20px;
  color: #333;
}

.header-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.share-icon {
  font-size: 18px;
  color: #333;
}

/* 商品图片轮播 */
.product-images {
  background-color: white;
  padding-bottom: 15px;
}

.image-slider {
  width: 100%;
  height: 300px;
  overflow: hidden;
}

.image-slider img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-indicator {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.indicator-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #ddd;
  margin: 0 5px;
}

.indicator-dot.active {
  background-color: #42b983;
}

/* 商品基本信息 */
.product-info {
  background-color: white;
  padding: 15px;
  margin-bottom: 10px;
}

.product-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.4;
}

.price-section {
  margin-bottom: 10px;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #ff4444;
  margin-right: 10px;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.meta-info {
  display: flex;
  font-size: 12px;
  color: #999;
}

.meta-info span {
  margin-right: 15px;
}

/* 商品标签 */
.product-tags {
  background-color: white;
  padding: 15px;
  margin-bottom: 10px;
  display: flex;
  flex-wrap: wrap;
}

.tag {
  display: inline-block;
  background-color: #f0f0f0;
  color: #666;
  font-size: 12px;
  padding: 5px 10px;
  border-radius: 15px;
  margin-right: 10px;
  margin-bottom: 10px;
}

/* 商品详细描述 */
.product-description {
  background-color: white;
  padding: 15px;
  margin-bottom: 10px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.description-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.description-images {
  display: flex;
  flex-wrap: wrap;
  margin-top: 15px;
}

.description-images img {
  width: 100%;
  max-width: 200px;
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
}

/* 卖家信息 */
.seller-info {
  background-color: white;
  padding: 15px;
  margin-bottom: 70px;
}

.seller-card {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.seller-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
}

.seller-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.seller-details {
  flex: 1;
}

.seller-nickname {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.seller-rating {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #999;
}

.star {
  margin-right: 5px;
  color: #ffc107;
}

.rating-text {
  margin-right: 10px;
}

.seller-more {
  color: #999;
}

.more-icon {
  font-size: 16px;
}

/* 卖家其他商品 */
.seller-other-products {
  margin-top: 15px;
}

.other-products-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.other-product-list {
  display: flex;
  overflow-x: auto;
}

.other-product-item {
  width: 120px;
  margin-right: 15px;
  cursor: pointer;
}

.other-product-image {
  width: 120px;
  height: 120px;
  border-radius: 5px;
  overflow: hidden;
  margin-bottom: 5px;
}

.other-product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.other-product-info {
  font-size: 12px;
}

.other-product-title {
  color: #333;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 30px;
}

.other-product-price {
  color: #ff4444;
  font-weight: bold;
}

/* 底部操作栏 */
.detail-bottom-nav {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 10px 20px;
  border-top: 1px solid #eee;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}

.detail-bottom-nav .nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 20px;
  cursor: pointer;
  color: #666;
}

.detail-bottom-nav .nav-item.primary {
  flex: 1;
  background-color: #42b983;
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  flex-direction: row;
  justify-content: center;
}

.detail-bottom-nav .nav-icon {
  font-size: 18px;
  margin-bottom: 3px;
}

.detail-bottom-nav .nav-item.primary .nav-icon {
  margin-bottom: 0;
  margin-right: 5px;
}

.detail-bottom-nav .nav-text {
  font-size: 12px;
}

/* 商品评论 */
.product-comments {
  background-color: white;
  padding: 15px;
  margin-bottom: 10px;
}

/* 评论统计 */
.comments-stats {
  display: flex;
  margin-bottom: 20px;
}

.average-rating {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 30px;
  min-width: 80px;
}

.rating-value {
  font-size: 32px;
  font-weight: bold;
  color: #ff4444;
  margin-bottom: 5px;
}

.rating-stars {
  display: flex;
  margin-bottom: 5px;
}

.star {
  font-size: 18px;
  color: #ddd;
  cursor: pointer;
  margin-right: 3px;
}

.star.active {
  color: #ffc107;
}

.rating-distribution {
  flex: 1;
}

.distribution-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.rating-label {
  width: 40px;
  font-size: 12px;
  color: #666;
}

.rating-bar {
  flex: 1;
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
  margin: 0 10px;
  overflow: hidden;
}

.rating-bar-fill {
  height: 100%;
  background-color: #ff4444;
  border-radius: 4px;
}

.rating-count {
  font-size: 12px;
  color: #999;
  min-width: 25px;
  text-align: right;
}

/* 评论表单 */
.comment-form-section {
  margin-bottom: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.comment-form-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.comment-form {
  background-color: #fafafa;
  padding: 15px;
  border-radius: 8px;
}

.comment-rating {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.comment-rating .rating-label {
  font-size: 14px;
  margin-right: 10px;
}

.comment-form textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  resize: none;
  outline: none;
  margin-bottom: 15px;
  box-sizing: border-box;
}

.comment-form textarea:focus {
  border-color: #42b983;
}

.submit-comment-btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  float: right;
}

.submit-comment-btn:hover {
  background-color: #3aa876;
}

/* 评论列表 */
.comments-list-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.commenter-info {
  display: flex;
  align-items: center;
}

.commenter-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 10px;
}

.commenter-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.commenter-details {
  display: flex;
  flex-direction: column;
}

.commenter-nickname {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 3px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-rating {
  display: flex;
  align-items: center;
}

.comment-content {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 10px;
}

.comment-actions {
  display: flex;
  font-size: 12px;
  color: #666;
}

.action-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
  cursor: pointer;
}

.action-icon {
  margin-right: 5px;
  font-size: 14px;
}

.action-count {
  color: #999;
}
</style>
