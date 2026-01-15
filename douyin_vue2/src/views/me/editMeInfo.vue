<template>
  <div class="profile-page">
    <!-- 顶部导航 -->
    <div class="nav-bar">
      <div class="nav-back" @click="goBack">
        <span class="back-icon">&lt;</span>
      </div>
      <div class="nav-title">编辑资料</div>
      <div class="nav-right"></div>
    </div>

    <!-- 用户信息展示 -->
    <div class="profile-info" v-if="!currentEdit && !showLocationSelect">
      <div class="avatar-section">
        <img :src="userInfo.userPic" class="avatar" />
        <div class="change-avatar-btn">更换头像</div>
        <div class="progress">资料完善度 78%</div>
      </div>

      <div class="info-list">
        <div class="info-item" @click="openEdit('name')">
          <div class="info-label">名字</div>
          <div class="info-value">
            {{ userInfo.nickname || '填写名字' }}
            <span class="arrow-icon">›</span>
          </div>
        </div>

        <div class="info-item" @click="openEdit('bio')">
          <div class="info-label">简介</div>
          <div class="info-value placeholder">
            {{ userInfo.jieshao || '介绍喜好、个性或@你的亲友' }}
            <span class="arrow-icon">›</span>
          </div>
        </div>

        <div class="info-item" @click="genderDialogVisible = true">
          <div class="info-label">性别</div>
          <div class="info-value">
            {{ userInfo.gender || '不展示' }}
            <span class="arrow-icon">›</span>
          </div>
        </div>

        <div class="info-item" @click="birthdayDialogVisible = true">
          <div class="info-label">生日</div>
          <div class="info-value">
            {{ userInfo.birthday || '不展示' }}
            <span class="arrow-icon">›</span>
          </div>
        </div>

        <div class="info-item" @click="showLocationSelect = true">
          <div class="info-label">所在地</div>
          <div class="info-value placeholder">
            {{ userInfo.city || '暂不设置' }}
            <span class="arrow-icon">›</span>
          </div>
        </div>

        <div class="info-item" @click="openEdit('douyin')">
          <div class="info-label">抖音号</div>
          <div class="info-value highlight">
            {{ userInfo.username || '未设置' }}
            <span class="arrow-icon">›</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑页 -->
    <div v-if="currentEdit && !showLocationSelect" class="edit-page">
      <div class="nav-bar">
        <div class="nav-back" @click="cancelEdit">
          <span class="back-text">取消</span>
        </div>
        <div class="nav-title">修改{{ editTitleMap[currentEdit] }}</div>
        <div class="nav-right" @click="saveEdit">
          <span class="save-text">保存</span>
        </div>
      </div>

      <div class="edit-content">
        <textarea
          v-if="currentEdit === 'bio'"
          v-model="editValue"
          class="bio-input"
          placeholder="介绍喜好、个性或@你的亲友"
          maxlength="100"
          rows="6"
        ></textarea>
        <input
          v-else
          v-model="editValue"
          class="edit-input"
          :placeholder="getPlaceholder(currentEdit)"
          ref="editInput"
        />
      </div>
    </div>

    <!-- 地区选择页面 -->
    <div v-if="showLocationSelect" class="location-select-page">
      <div class="nav-bar">
        <div class="nav-back" @click="showLocationSelect = false">
          <span class="back-icon">&lt;</span>
        </div>
        <div class="nav-title">选择地区</div>
        <div class="nav-right"></div>
      </div>

      <div class="location-content">
        <div class="location-item" @click="selectLocation('')">暂不设置</div>
        <div class="location-section">
          <div class="section-title">当前位置</div>
          <div class="location-item" @click="selectLocation('陕西:西安')">
            陕西:西安
          </div>
        </div>
        <div class="location-section">
          <div class="section-title">其他地区</div>
          <div class="location-group">
            <div class="location-letter">A</div>
            <div class="location-item" @click="selectLocation('安道尔')">
              安道尔
            </div>
            <div class="location-item" @click="selectLocation('奥地利')">
              奥地利
            </div>
            <div class="location-item" @click="selectLocation('澳大利亚')">
              澳大利亚
            </div>
            <div class="location-item" @click="selectLocation('阿尔巴尼亚')">
              阿尔巴尼亚
            </div>
            <div class="location-item" @click="selectLocation('阿尔及利亚')">
              阿尔及利亚
            </div>
            <div class="location-item" @click="selectLocation('爱尔兰')">
              爱尔兰
            </div>
            <div class="location-item" @click="selectLocation('安哥拉')">
              安哥拉
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 性别弹窗 -->
    <div v-if="genderDialogVisible" class="dialog-overlay">
      <div class="bottom-dialog">
        <div class="dialog-header">
          <div class="dialog-title">选择性别</div>
        </div>
        <div class="dialog-content">
          <div
            v-for="gender in ['男', '女', '不展示']"
            :key="gender"
            class="dialog-option"
            :class="{ active: userInfo.gender === gender }"
            @click="selectGender(gender)"
          >
            {{ gender }}
          </div>
        </div>
        <div class="dialog-footer">
          <button
            class="dialog-btn cancel-btn"
            @click="genderDialogVisible = false"
          >
            取消
          </button>
          <button class="dialog-btn confirm-btn" @click="confirmGender">
            确定
          </button>
        </div>
      </div>
    </div>

    <!-- 生日弹窗 -->
    <div v-if="birthdayDialogVisible" class="dialog-overlay">
      <div class="bottom-dialog">
        <div class="dialog-header">
          <div class="dialog-title">选择生日</div>
        </div>
        <div class="dialog-content">
          <!-- 这里可以集成日期选择器组件 -->
          <div class="date-picker-placeholder">日期选择器</div>
        </div>
        <div class="dialog-footer">
          <button
            class="dialog-btn cancel-btn"
            @click="birthdayDialogVisible = false"
          >
            取消
          </button>
          <button class="dialog-btn confirm-btn" @click="confirmBirthday">
            确定
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getUserInfo, updateUserInfo } from '@/api/user';

export default {
  data() {
    return {
      userInfo: {}, // 用户信息
      currentEdit: '', // 当前编辑项
      editValue: '',
      genderDialogVisible: false,
      birthdayDialogVisible: false,
      showLocationSelect: false,
      tempGender: '',
      editTitleMap: {
        name: '名字',
        bio: '简介',
        location: '所在地',
        douyin: '抖音号'
      }
    };
  },
  mounted() {
    this.getUserInfoInit(); // 获取用户信息
  },
  methods: {
    // 获取用户信息
    getUserInfoInit() {
      getUserInfo()
        .then((res) => {
          this.userInfo = res.data; // 更新用户信息
        })
        .catch((err) => {
          console.error(err);
        });
    },
    // 返回上一页
    goBack() {
      this.$router.back();
    },
    // 打开编辑框
    openEdit(field) {
      this.currentEdit = field;
      // 根据字段映射获取正确的用户信息
      if (field === 'name') {
        this.editValue = this.userInfo.nickname || '';
      } else if (field === 'bio') {
        this.editValue = this.userInfo.jieshao || '';
      } else if (field === 'douyin') {
        this.editValue = this.userInfo.username || '';
      } else {
        this.editValue = this.userInfo[field] || '';
      }
    },
    // 取消编辑
    cancelEdit() {
      this.currentEdit = '';
      this.editValue = '';
    },
    // 获取占位符文本
    getPlaceholder(field) {
      const placeholders = {
        name: '请输入名字',
        douyin: '请输入抖音号'
      };
      return placeholders[field] || '请输入...';
    },
    // 保存编辑
    async saveEdit() {
      // 更新本地用户信息
      if (this.currentEdit === 'name') {
        this.userInfo.nickname = this.editValue;
      } else if (this.currentEdit === 'bio') {
        this.userInfo.jieshao = this.editValue;
      } else if (this.currentEdit === 'douyin') {
        this.userInfo.username = this.editValue;
      } else {
        this.userInfo[this.currentEdit] = this.editValue;
      }

      try {
        // 字段映射：前端字段名 -> 后端User.java字段名
        const fieldMap = {
          name: 'nickname', // 前端name对应后端nickname
          bio: 'myIntro', // 前端bio对应后端myIntro
          location: 'city', // 前端location对应后端city
          douyin: 'username' // 前端douyin对应后端username
        };

        // 只传递必要的字段，而不是整个对象
        const updateData = {
          id: this.userInfo.id // 确保有用户ID
        };

        // 根据映射关系设置正确的后端字段名
        const backendField = fieldMap[this.currentEdit] || this.currentEdit;
        updateData[backendField] = this.editValue;

        // 调用后端API更新用户信息
        const response = await updateUserInfo(updateData);
        if (response.code === 0) {
          this.$message.success('修改成功');
          const editedField = this.currentEdit; // 保存当前编辑的字段
          this.currentEdit = '';
          this.editValue = '';

          // 保存成功后跳转到简介页
          this.goToProfile();
        } else {
          this.$message.error('修改失败: ' + (response.message || '未知错误'));
        }
      } catch (error) {
        console.error('更新用户信息失败:', error);
        this.$message.error('网络错误，请稍后重试');
      }
    },
    // 退出编辑页，返回上一页
    goToProfile() {
      // 返回上一页，即退出编辑页回到主页
      this.$router.back();
    },
    // 选择性别
    selectGender(gender) {
      this.tempGender = gender;
    },
    // 确认性别
    async confirmGender() {
      if (this.tempGender !== '') {
        this.userInfo.gender = this.tempGender;

        try {
          const updateData = {
            id: this.userInfo.id,
            gender: this.tempGender
          };

          const response = await updateUserInfo(updateData);
          if (response.code === 0) {
            this.$message.success('修改成功');
          }
        } catch (error) {
          console.error('更新性别失败:', error);
        }
      }
      this.genderDialogVisible = false;
    },
    // 确认生日
    confirmBirthday() {
      // 这里可以添加生日保存逻辑
      this.birthdayDialogVisible = false;
    },
    // 选择地区
    async selectLocation(location) {
      this.userInfo.city = location || '';

      try {
        const updateData = {
          id: this.userInfo.id,
          city: location
        };

        const response = await updateUserInfo(updateData);
        if (response.code === 0) {
          this.$message.success('修改成功');
        }
      } catch (error) {
        console.error('更新地区失败:', error);
      }

      this.showLocationSelect = false;
    }
  }
};
</script>

<style scoped>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial,
    sans-serif;
  background-color: #f5f5f5;
}

.profile-page {
  background: #f5f5f5;
  min-height: 100vh;
  position: relative;
}

/* 导航栏样式 */
.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 44px;
  background-color: #fff;
  padding: 0 15px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  border-bottom: 1px solid #eee;
}

.nav-back,
.nav-right {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-back {
  justify-content: flex-start;
}

.nav-right {
  justify-content: flex-end;
}

.back-icon {
  font-size: 20px;
  font-weight: bold;
}

.back-text {
  color: #666;
  font-size: 16px;
}

.nav-title {
  font-size: 17px;
  font-weight: 500;
  color: #333;
}

.save-text {
  color: #007aff;
  font-size: 16px;
}

/* 内容区域 */
.profile-info,
.edit-page,
.location-select-page {
  padding-top: 44px;
}

/* 头像区域 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  background-color: #fff;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #fff;
  background-color: #eee;
}

.change-avatar-btn {
  margin-top: 10px;
  color: #007aff;
  font-size: 14px;
}

.progress {
  margin-top: 5px;
  font-size: 12px;
  color: #888;
}

/* 信息列表 */
.info-list {
  background-color: #fff;
  margin-top: 10px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #f5f5f5;
}

.info-label {
  font-size: 16px;
  color: #333;
}

.info-value {
  font-size: 16px;
  color: #333;
  display: flex;
  align-items: center;
}

.placeholder {
  color: #bbb;
}

.highlight {
  color: #333;
}

.arrow-icon {
  margin-left: 5px;
  color: #bbb;
  font-size: 18px;
}

/* 编辑页面样式 */
.edit-page {
  background-color: #fff;
}

.edit-content {
  padding: 20px 15px;
}

.edit-input {
  width: 100%;
  font-size: 16px;
  padding: 10px 0;
  border: none;
  outline: none;
  border-bottom: 1px solid #eee;
}

.bio-input {
  width: 100%;
  font-size: 16px;
  padding: 10px;
  border: none;
  outline: none;
  resize: none;
  background-color: #f9f9f9;
  border-radius: 8px;
  font-family: inherit;
}

/* 地区选择页面 */
.location-select-page {
  background-color: #fff;
  height: 100vh;
  overflow-y: auto;
}

.location-content {
  padding: 15px;
}

.location-item {
  padding: 15px 0;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #f5f5f5;
}

.location-section {
  margin-top: 20px;
}

.section-title {
  font-size: 14px;
  color: #888;
  padding: 10px 0;
}

.location-letter {
  font-size: 14px;
  color: #888;
  padding: 10px 0;
  background-color: #f5f5f5;
  padding-left: 15px;
}

.location-group {
  margin-top: 10px;
}

/* 弹窗样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 200;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.bottom-dialog {
  width: 100%;
  background-color: #fff;
  border-radius: 15px 15px 0 0;
  padding-bottom: env(safe-area-inset-bottom, 0);
}

.dialog-header {
  padding: 15px;
  border-bottom: 1px solid #f5f5f5;
  text-align: center;
}

.dialog-title {
  font-size: 17px;
  font-weight: 500;
  color: #333;
}

.dialog-content {
  padding: 15px;
}

.dialog-option {
  padding: 15px 0;
  text-align: center;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #f5f5f5;
}

.dialog-option.active {
  color: #007aff;
}

.dialog-footer {
  padding: 15px;
  display: flex;
  gap: 10px;
}

.dialog-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #333;
}

.confirm-btn {
  background-color: #007aff;
  color: #fff;
}

.date-picker-placeholder {
  text-align: center;
  padding: 20px;
  color: #888;
}
</style>
-->
