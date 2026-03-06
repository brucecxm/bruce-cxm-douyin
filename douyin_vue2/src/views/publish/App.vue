<template>
  <div class="publish-product">
    <!-- 顶部导航 -->
    <header class="publish-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">发布商品</div>
      <div class="header-right" @click="submitForm">
        <span class="submit-btn">发布</span>
      </div>
    </header>

    <!-- 发布表单 -->
    <form class="publish-form">
      <!-- 商品标题 -->
      <div class="form-group">
        <label class="form-label">商品标题</label>
        <input
          type="text"
          class="form-input"
          v-model="formData.title"
          placeholder="请输入商品标题，不超过20个字"
        />
      </div>

      <!-- 商品价格 -->
      <div class="form-group">
        <label class="form-label">商品价格</label>
        <div class="price-input-group">
          <span class="price-symbol">¥</span>
          <input
            type="number"
            class="form-input price-input"
            v-model="formData.price"
            placeholder="0.00"
            step="0.01"
          />
        </div>
      </div>

      <!-- 原价 -->
      <div class="form-group">
        <label class="form-label">原价</label>
        <div class="price-input-group">
          <span class="price-symbol">¥</span>
          <input
            type="number"
            class="form-input price-input"
            v-model="formData.originalPrice"
            placeholder="0.00"
            step="0.01"
          />
        </div>
      </div>

      <!-- 商品分类 -->
      <div class="form-group">
        <label class="form-label">商品分类</label>
        <div class="category-select" @click="showCategoryPicker">
          <span class="selected-category">{{ selectedCategory }}</span>
          <span class="select-arrow">▼</span>
        </div>
      </div>

      <!-- 分类选择弹窗 -->
      <div class="category-popup" v-if="showCategoryPopup">
        <div class="popup-overlay" @click="closeCategoryPopup"></div>
        <div class="popup-content">
          <div class="popup-header">
            <span class="popup-title">选择分类</span>
            <span class="popup-close" @click="closeCategoryPopup">×</span>
          </div>
          <div class="popup-body">
            <div class="category-list">
              <div
                class="category-item"
                v-for="category in categories"
                :key="category"
                @click="selectCategory(category)"
              >
                {{ category }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品图片上传 -->
      <div class="form-group">
        <label class="form-label">商品图片</label>
        <div class="image-upload-section">
          <div class="upload-images">
            <div
              class="uploaded-image"
              v-for="(image, index) in formData.images"
              :key="index"
            >
              <img :src="image" :alt="'商品图片' + (index + 1)" />
              <div class="image-delete" @click="deleteImage(index)">×</div>
            </div>
            <div class="upload-btn" @click="uploadImage">
              <i class="upload-icon">+</i>
              <span class="upload-text">添加图片</span>
            </div>
          </div>
          <p class="image-hint">最多上传9张图片，第一张为封面</p>
        </div>
      </div>

      <!-- 商品标签 -->
      <div class="form-group">
        <label class="form-label">商品标签</label>
        <div class="tags-section">
          <div class="available-tags">
            <span
              class="tag-item"
              v-for="tag in availableTags"
              :key="tag"
              @click="toggleTag(tag)"
              :class="{ active: formData.tags.includes(tag) }"
            >
              {{ tag }}
            </span>
          </div>
        </div>
      </div>

      <!-- 商品描述 -->
      <div class="form-group">
        <label class="form-label">商品描述</label>
        <textarea
          class="form-textarea"
          v-model="formData.description"
          placeholder="请详细描述商品的成色、使用情况、有无瑕疵等信息"
          rows="6"
        ></textarea>
      </div>

      <!-- 交易方式 -->
      <div class="form-group">
        <label class="form-label">交易方式</label>
        <div class="transaction-methods">
          <div
            class="method-item"
            v-for="method in transactionMethods"
            :key="method.value"
            @click="selectMethod(method.value)"
          >
            <div
              class="method-checkbox"
              :class="{ checked: formData.transactionMethod === method.value }"
            >
              <span
                v-if="formData.transactionMethod === method.value"
                class="check-icon"
                >✓</span
              >
            </div>
            <div class="method-text">{{ method.label }}</div>
          </div>
        </div>
      </div>

      <!-- 所在地区 -->
      <div class="form-group">
        <label class="form-label">所在地区</label>
        <div class="location-select" @click="showLocationPicker">
          <span class="selected-location">{{ formData.location }}</span>
          <span class="select-arrow">▼</span>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils';
import routerUtils from '../../utils/routerUtils';
import { post, put, uploadFile } from '../../utils/apiUtils';

export default {
  name: 'PublishProductPage',
  data() {
    return {
      // 表单数据
      formData: {
        title: '',
        price: '',
        originalPrice: '',
        quantity: 1,
        category: '',
        images: [],

        tags: [],
        description: '',
        transactionMethod: 'face_to_face',
        location: '北京 朝阳区',
        userId: 1
      },
      // 可用分类
      categories: [
        '手机数码',
        '电脑办公',
        '家用电器',
        '服装鞋包',
        '美妆护肤',
        '运动户外',
        '图书音像',
        '其他'
      ],
      // 选中的分类
      selectedCategory: '请选择分类',
      // 分类弹窗显示状态
      showCategoryPopup: false,
      // 可用标签
      availableTags: [
        '99新',
        '95新',
        '9成新',
        '8成新',
        '7成新',
        '原装',
        '无拆修',
        '配件齐全',
        '当面交易',
        '包邮'
      ],
      // 交易方式
      transactionMethods: [
        { value: 'face_to_face', label: '当面交易' },
        { value: 'express', label: '快递邮寄' },
        { value: 'both', label: '两种方式都可以' }
      ]
    };
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('发布商品');

    // 从localStorage获取用户信息
    const userInfo = localStorage.getItem('userInfo');
    if (userInfo) {
      try {
        const user = JSON.parse(userInfo);
        // 设置用户ID到表单数据中
        this.formData.userId = user.id;
      } catch (error) {
        console.error('解析用户信息失败:', error);
      }
    } else {
      // 如果用户未登录，跳转到登录页面
      nativeUtils.showToast('请先登录', 1500);
      setTimeout(() => {
        routerUtils.navigateTo('/newshop/login.html');
      }, 1500);
    }
  },
  methods: {
    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1);
    },
    // 提交表单
    async submitForm() {
      // 表单验证
      if (!this.formData.title) {
        nativeUtils.showToast('请输入商品标题', 1500);
        return;
      }
      if (this.formData.title.length > 20) {
        nativeUtils.showToast('商品标题不能超过20个字', 1500);
        return;
      }
      if (!this.formData.price) {
        nativeUtils.showToast('请输入商品价格', 1500);
        return;
      }
      if (parseFloat(this.formData.price) <= 0) {
        nativeUtils.showToast('商品价格必须大于0', 1500);
        return;
      }
      if (!this.formData.description) {
        nativeUtils.showToast('请输入商品描述', 1500);
        return;
      }
      if (this.formData.description.length < 10) {
        nativeUtils.showToast('商品描述不能少于10个字', 1500);
        return;
      }
      if (!this.formData.category) {
        nativeUtils.showToast('请选择商品分类', 1500);
        return;
      }
      if (this.formData.images.length === 0) {
        nativeUtils.showToast('请至少上传一张商品图片', 1500);
        return;
      }

      try {
        // 调用商品发布API
        nativeUtils.showLoading('发布中...');

        // 准备商品数据
        const productData = {
          ...this.formData,
          status: 'active'
        };

        let response;
        // 检查是否是编辑模式
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');

        if (productId) {
          // 编辑模式
          response = await put(`/newshop/product/${productId}`, productData);
        } else {
          // 发布新模式
          response = await post('/newshop/product', productData);
        }

        if (response.code === 0) {
          nativeUtils.hideLoading();
          nativeUtils.showToast(
            productId ? '商品编辑成功' : '商品发布成功',
            1500
          );

          // 跳转到商品列表或详情页
          if (productId) {
            routerUtils.navigateTo(`/newshop/detail?id=${productId}`);
          } else {
            routerUtils.navigateTo('/newshop/myProducts.html');
          }
        } else {
          nativeUtils.hideLoading();
          nativeUtils.showToast(response.message || '操作失败', 1500);
        }
      } catch (error) {
        console.error('商品操作失败:', error);
        nativeUtils.hideLoading();
        nativeUtils.showToast('操作失败，请重试', 1500);
      }
    },
    // 显示分类选择器
    showCategoryPicker() {
      this.showCategoryPopup = true;
    },
    // 关闭分类选择器
    closeCategoryPopup() {
      this.showCategoryPopup = false;
    },
    // 选择分类
    selectCategory(category) {
      this.formData.category = category;
      this.selectedCategory = category;
      this.showCategoryPopup = false;
    },
    // 上传图片
    uploadImage() {
      if (this.formData.images.length >= 9) {
        nativeUtils.showToast('最多只能上传9张图片', 1500);
        return;
      }
      // 创建文件选择输入
      const input = document.createElement('input');
      input.type = 'file';
      input.accept = 'image/*';
      input.multiple = true;

      // 限制文件数量
      const maxFiles = 9 - this.formData.images.length;
      if (maxFiles <= 0) {
        nativeUtils.showToast('最多只能上传9张图片', 1500);
        return;
      }

      input.onchange = async (e) => {
        const files = Array.from(e.target.files);
        if (files.length === 0) return;

        // 限制上传数量
        const uploadFiles = files.slice(0, maxFiles);

        nativeUtils.showLoading('图片上传中...');

        try {
          // 逐个上传图片
          for (const file of uploadFiles) {
            const formData = new FormData();
            formData.append('file', file);

            // 调用后端上传API
            const response = await uploadFile(
              '/newshop/product/upload',
              formData
            );

            if (response.code === 0) {
              // 上传成功，添加到图片列表
              this.formData.images.push(response.data);
            } else {
              throw new Error(response.message || '图片上传失败');
            }
          }

          nativeUtils.hideLoading();
          nativeUtils.showToast('图片上传成功', 1500);
        } catch (error) {
          nativeUtils.hideLoading();
          nativeUtils.showToast('图片上传失败: ' + error.message, 1500);
          console.error('图片上传失败:', error);
        }
      };

      // 触发文件选择
      input.click();
    },
    // 删除图片
    deleteImage(index) {
      if (this.formData.images.length <= 1) {
        nativeUtils.showToast('至少保留一张商品图片', 1500);
        return;
      }
      this.formData.images.splice(index, 1);
    },
    // 切换标签
    toggleTag(tag) {
      const index = this.formData.tags.indexOf(tag);
      if (index > -1) {
        this.formData.tags.splice(index, 1);
      } else {
        // 最多选择5个标签
        if (this.formData.tags.length >= 5) {
          nativeUtils.showToast('最多只能选择5个标签', 1500);
          return;
        }
        this.formData.tags.push(tag);
      }
    },
    // 选择交易方式
    selectMethod(method) {
      this.formData.transactionMethod = method;
    },
    // 显示地区选择器
    showLocationPicker() {
      // 模拟地区选择
      const locations = [
        '北京 朝阳区',
        '上海 浦东新区',
        '广州 天河区',
        '深圳 南山区',
        '杭州 西湖区'
      ];
      const location = prompt(
        '请选择所在地区:\n' + locations.join('\n'),
        this.formData.location
      );
      if (location && locations.includes(location)) {
        this.formData.location = location;
      }
    }
  }
};
</script>

<style scoped>
.publish-product {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航 */
.publish-header {
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

.header-left {
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

.submit-btn {
  color: #42b983;
  font-size: 16px;
  cursor: pointer;
  font-weight: bold;
}

/* 发布表单 */
.publish-form {
  padding: 15px;
  background-color: white;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  box-sizing: border-box;
}

/* 价格输入组 */
.price-input-group {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 0 12px;
}

.price-symbol {
  color: #999;
  font-size: 16px;
  margin-right: 5px;
}

.price-input {
  flex: 1;
  border: none;
  padding: 12px 0;
  font-size: 16px;
  outline: none;
}

/* 分类选择 */
.category-select,
.location-select {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 12px;
  cursor: pointer;
  background-color: white;
}

.selected-category,
.selected-location {
  color: #333;
  font-size: 14px;
}

.select-arrow {
  color: #999;
  font-size: 12px;
}

/* 图片上传 */
.image-upload-section {
  margin-top: 10px;
}

.upload-images {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.uploaded-image {
  position: relative;
  width: 80px;
  height: 80px;
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  overflow: hidden;
  border: 1px solid #ddd;
}

.uploaded-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-delete {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: rgba(255, 0, 0, 0.8);
  color: white;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  cursor: pointer;
}

.upload-btn {
  width: 80px;
  height: 80px;
  border: 1px dashed #ddd;
  border-radius: 5px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  background-color: #f9f9f9;
}

.upload-icon {
  font-size: 24px;
  color: #999;
  margin-bottom: 5px;
}

.upload-text {
  font-size: 12px;
  color: #999;
}

.image-hint {
  font-size: 12px;
  color: #999;
  margin: 0;
}

/* 商品标签 */
.tags-section {
  margin-top: 10px;
}

.available-tags {
  display: flex;
  flex-wrap: wrap;
}

.tag-item {
  display: inline-block;
  background-color: #f0f0f0;
  color: #666;
  font-size: 12px;
  padding: 6px 12px;
  border-radius: 15px;
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  border: 1px solid transparent;
}

.tag-item.active {
  background-color: #e0f5e9;
  color: #42b983;
  border-color: #42b983;
}

/* 商品描述 */
.form-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  resize: vertical;
  min-height: 100px;
  box-sizing: border-box;
}

/* 交易方式 */
.transaction-methods {
  display: flex;
  flex-direction: column;
}

.method-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  cursor: pointer;
}

.method-checkbox {
  width: 20px;
  height: 20px;
  border: 1px solid #ddd;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 10px;
}

.method-checkbox.checked {
  background-color: #42b983;
  border-color: #42b983;
  color: white;
}

.check-icon {
  font-size: 12px;
  color: white;
}

.method-text {
  color: #333;
  font-size: 14px;
}

/* 分类选择弹窗样式 */
.category-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.popup-content {
  position: relative;
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
  max-height: 80vh;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  animation: popupSlideIn 0.3s ease-out;
}

@keyframes popupSlideIn {
  from {
    transform: translateY(50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  background-color: white;
}

.popup-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.popup-close {
  font-size: 20px;
  color: #999;
  cursor: pointer;
  padding: 5px;
  line-height: 1;
}

.popup-close:hover {
  color: #333;
}

.popup-body {
  padding: 0;
  max-height: calc(80vh - 60px);
  overflow-y: auto;
}

.category-list {
  padding: 10px 0;
}

.category-item {
  padding: 15px 20px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}

.category-item:last-child {
  border-bottom: none;
}

.category-item:hover {
  background-color: #f0f0f0;
}
</style>
