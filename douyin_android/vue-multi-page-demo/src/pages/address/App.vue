<template>
  <div class="address-page">
    <!-- 顶部导航 -->
    <header class="address-header">
      <div class="header-left" @click="goBack">
        <i class="back-icon">←</i>
      </div>
      <div class="header-title">收货地址</div>
      <div class="header-right" @click="addAddress">
        <span class="add-btn">添加</span>
      </div>
    </header>

    <!-- 地址列表 -->
    <div class="address-list">
      <!-- 空状态显示 -->
      <div class="empty-state" v-if="addresses.length === 0 && !loading">
        <div class="empty-icon">📦</div>
        <div class="empty-text">暂无收货地址</div>
        <div class="empty-subtext">点击右上角"添加"按钮添加新地址</div>
      </div>
      
      <!-- 地址项 -->
      <div class="address-item" v-for="address in addresses" :key="address.id">
        <div class="address-info">
          <div class="address-header">
            <div class="consignee">{{ address.receiver }}</div>
            <div class="phone">{{ address.phone }}</div>
            <div class="default-tag" v-if="address.isDefault">默认</div>
          </div>
          <div class="address-detail">{{ address.province }}{{ address.city }}{{ address.district }}{{ address.detail }}</div>
        </div>
        <div class="address-actions">
          <div class="action-btn edit" @click="editAddress(address)">编辑</div>
          <div class="action-btn delete" @click="deleteAddress(address.id)">删除</div>
          <div class="action-btn set-default" v-if="!address.isDefault" @click="setDefault(address.id)">设为默认</div>
        </div>
      </div>
    </div>

    <!-- 地址表单弹窗 -->
    <div class="address-modal" v-if="showModal">
      <div class="modal-overlay" @click="closeModal"></div>
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ editingAddress ? '编辑地址' : '添加地址' }}</h3>
          <span class="close-btn" @click="closeModal">×</span>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveAddress">
            <div class="form-group">
              <label>收货人</label>
              <input type="text" v-model="formData.receiver" placeholder="请输入收货人姓名" required>
            </div>
            
            <div class="form-group">
              <label>手机号</label>
              <input type="tel" v-model="formData.phone" placeholder="请输入11位手机号" required>
              <div class="error-text" v-if="phoneError">{{ phoneError }}</div>
            </div>
            
            <div class="form-row">
              <div class="form-group province">
                <label>省份</label>
                <input type="text" v-model="formData.province" placeholder="请输入省份" required>
              </div>
              
              <div class="form-group city">
                <label>城市</label>
                <input type="text" v-model="formData.city" placeholder="请输入城市" required>
              </div>
              
              <div class="form-group district">
                <label>区县</label>
                <input type="text" v-model="formData.district" placeholder="请输入区县" required>
              </div>
            </div>
            
            <div class="form-group">
              <label>详细地址</label>
              <textarea v-model="formData.detail" placeholder="请输入街道、门牌号等详细信息" rows="3" required></textarea>
            </div>
            
            <div class="form-group checkbox-group">
              <input type="checkbox" id="isDefault" v-model="formData.isDefault">
              <label for="isDefault">设为默认地址</label>
            </div>
            
            <div class="form-actions">
              <button type="button" class="btn cancel" @click="closeModal">取消</button>
              <button type="submit" class="btn confirm">{{ editingAddress ? '保存' : '添加' }}</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import nativeUtils from '../../utils/nativeUtils'
import routerUtils from '../../utils/routerUtils'
import { get, post, put, del } from '../../utils/apiUtils'

export default {
  name: 'AddressPage',
  data() {
    return {
      addresses: [],
      loading: false,
      showModal: false,
      editingAddress: null,
      phoneError: '',
      formData: {
        receiver: '',
        phone: '',
        province: '',
        city: '',
        district: '',
        detail: '',
        isDefault: false,
        userId: null
      }
    }
  },
  mounted() {
    // 页面加载时设置标题
    nativeUtils.setTitleBar('收货地址')
    // 从API加载地址列表
    this.loadAddresses()
  },
  methods: {
    // 从API加载地址列表
    loadAddresses() {
      try {
        this.loading = true
        nativeUtils.showLoading('加载中...')
        
        // 获取当前用户信息
        const userInfo = localStorage.getItem('user')
        if (userInfo) {
          const user = JSON.parse(userInfo)
          const userId = user.id
          
          // 调用API获取地址列表
          get(`/address/user/${userId}`)
            .then(response => {
              if (response.code === 0) {
                this.addresses = response.data || []
              } else {
                this.addresses = []
              }
              this.loading = false
              nativeUtils.hideLoading()
            })
            .catch(error => {
              console.error('加载地址列表失败:', error)
              this.addresses = []
              this.loading = false
              nativeUtils.hideLoading()
              nativeUtils.showToast('加载地址失败，请重试', 1500)
            })
        } else {
          // 用户未登录，跳转登录页
          this.loading = false
          nativeUtils.hideLoading()
          nativeUtils.showToast('请先登录', 1500)
          setTimeout(() => {
            routerUtils.navigateTo('/login.html')
          }, 1500)
        }
      } catch (error) {
        console.error('加载地址列表失败:', error)
        this.addresses = []
        this.loading = false
        nativeUtils.hideLoading()
        nativeUtils.showToast('加载地址失败，请重试', 1500)
      }
    },
    
    // 返回上一页
    goBack() {
      routerUtils.navigateBack(1)
    },
    
    // 打开添加地址弹窗
    addAddress() {
      const userInfo = localStorage.getItem('user')
      if (userInfo) {
        const user = JSON.parse(userInfo)
        this.editingAddress = null
        this.formData = {
          receiver: '',
          phone: '',
          province: '',
          city: '',
          district: '',
          detail: '',
          isDefault: this.addresses.length === 0,
          userId: user.id
        }
        this.phoneError = ''
        this.showModal = true
      }
    },
    
    // 打开编辑地址弹窗
    editAddress(address) {
      this.editingAddress = address
      this.formData = {
        ...address,
        userId: address.userId || JSON.parse(localStorage.getItem('user')).id
      }
      this.phoneError = ''
      this.showModal = true
    },
    
    // 关闭弹窗
    closeModal() {
      this.showModal = false
      this.editingAddress = null
      this.phoneError = ''
    },
    
    // 手机号验证
    validatePhone(phone) {
      const phoneRegex = /^1[3-9]\d{9}$/
      if (!phone) {
        this.phoneError = '请输入手机号'
        return false
      } else if (!phoneRegex.test(phone)) {
        this.phoneError = '请输入有效的11位手机号'
        return false
      } else {
        this.phoneError = ''
        return true
      }
    },
    
    // 保存地址
    saveAddress() {
      // 验证手机号
      if (!this.validatePhone(this.formData.phone)) {
        return
      }
      
      // 验证其他必填字段
      if (!this.formData.receiver || !this.formData.province || 
          !this.formData.city || !this.formData.district || !this.formData.detail) {
        nativeUtils.showToast('请填写完整的地址信息', 1500)
        return
      }
      
      nativeUtils.showLoading(this.editingAddress ? '更新中...' : '添加中...')
      
      // 调用API保存地址
      const request = this.editingAddress 
        ? put(`/address/${this.editingAddress.id}`, this.formData)
        : post('/address', this.formData)
      
      request.then(response => {
        if (response.code === 0) {
          // 重新加载地址列表
          this.loadAddresses()
          this.closeModal()
          nativeUtils.showToast(this.editingAddress ? '地址更新成功' : '地址添加成功', 1500)
        } else {
          nativeUtils.hideLoading()
          nativeUtils.showToast(response.message || (this.editingAddress ? '更新地址失败' : '添加地址失败'), 1500)
        }
      })
      .catch(error => {
        console.error(this.editingAddress ? '更新地址失败:' : '添加地址失败:', error)
        nativeUtils.hideLoading()
        nativeUtils.showToast(this.editingAddress ? '更新地址失败，请重试' : '添加地址失败，请重试', 1500)
      })
    },
    
    // 删除地址
    deleteAddress(addressId) {
      if (confirm('确定要删除这个地址吗？')) {
        nativeUtils.showLoading('删除中...')
        
        // 调用API删除地址
        del(`/address/${addressId}`)
          .then(response => {
            if (response.code === 0) {
              // 重新加载地址列表
              this.loadAddresses()
              nativeUtils.showToast('地址删除成功', 1500)
            } else {
              nativeUtils.hideLoading()
              nativeUtils.showToast(response.message || '删除地址失败', 1500)
            }
          })
          .catch(error => {
            console.error('删除地址失败:', error)
            nativeUtils.hideLoading()
            nativeUtils.showToast('删除地址失败，请重试', 1500)
          })
      }
    },
    
    // 设置为默认地址
    setDefault(addressId) {
      // 获取要设置为默认的地址
      const address = this.addresses.find(addr => addr.id === addressId)
      if (address) {
        // 更新地址对象，将isDefault设为true
        const updatedAddress = {
          ...address,
          isDefault: true
        }
        
        nativeUtils.showLoading('设置中...')
        
        // 调用API更新地址
        put(`/address/${addressId}`, updatedAddress)
          .then(response => {
            if (response.code === 0) {
              // 重新加载地址列表
              this.loadAddresses()
              nativeUtils.showToast('已设为默认地址', 1500)
            } else {
              nativeUtils.hideLoading()
              nativeUtils.showToast(response.message || '设置默认地址失败', 1500)
            }
          })
          .catch(error => {
            console.error('设置默认地址失败:', error)
            nativeUtils.hideLoading()
            nativeUtils.showToast('设置默认地址失败，请重试', 1500)
          })
      }
    }
  }
}
</script>

<style scoped>
.address-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部导航 */
.address-header {
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

.back-icon {
  font-size: 20px;
  color: #333;
  cursor: pointer;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.add-btn {
  color: #42b983;
  font-size: 16px;
  cursor: pointer;
  font-weight: bold;
}

/* 地址列表 */
.address-list {
  padding: 15px;
  flex: 1;
}

/* 空状态显示 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  color: #999;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}

.empty-subtext {
  font-size: 14px;
  line-height: 1.5;
}

/* 地址项 */
.address-item {
  background-color: white;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
}

/* 地址信息 */
.address-info {
  margin-bottom: 15px;
}

.address-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.consignee {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-right: 15px;
}

.phone {
  font-size: 14px;
  color: #666;
  margin-right: 15px;
}

.default-tag {
  background-color: #ff4400;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.address-detail {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

/* 地址操作 */
.address-actions {
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #f0f0f0;
  padding-top: 10px;
}

.action-btn {
  font-size: 14px;
  padding: 5px 15px;
  margin-left: 15px;
  cursor: pointer;
  border-radius: 15px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}

.action-btn.edit {
  color: #42b983;
  border: 1px solid #42b983;
}

.action-btn.delete {
  color: #ff4444;
  border: 1px solid #ff4444;
}

.action-btn.set-default {
  color: #666;
  border: 1px solid #ddd;
}

/* 地址表单弹窗 */
.address-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: flex-end;
}

.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  position: relative;
  width: 100%;
  max-height: 90vh;
  background-color: white;
  border-radius: 16px 16px 0 0;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.15);
  overflow-y: auto;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.close-btn {
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 0;
  margin: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background-color: #f5f5f5;
  color: #333;
}

.modal-body {
  padding: 24px;
}

/* 表单样式 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #42b983;
  box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.error-text {
  color: #ff4444;
  font-size: 12px;
  margin-top: 4px;
}

/* 一行三列布局 */
.form-row {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.form-row .form-group {
  flex: 1;
  margin-bottom: 0;
}

/* 复选框组 */
.checkbox-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
  margin: 0;
}

.checkbox-group label {
  margin: 0;
  font-weight: normal;
  cursor: pointer;
}

/* 表单操作按钮 */
.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.form-actions .btn {
  flex: 1;
  padding: 14px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn.cancel {
  background-color: #f5f5f5;
  color: #666;
  border: 1px solid #ddd;
}

.btn.confirm {
  background-color: #42b983;
  color: white;
}

.btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.btn:active {
  transform: translateY(0);
}
</style>