<template>
    <div v-if="user">
      <h1>User Information</h1>
  
      <div v-if="isLoading" class="loading">Loading...</div>
      <div v-if="error" class="error">{{ error }}</div>
  
      <div v-if="!isLoading && !error">
        <form @submit.prevent="updateUserData">
          <label>
            Name:
            <input v-model="user.name" type="text" />
            <span v-if="formErrors.name" class="error-message">{{ formErrors.name }}</span>
          </label>
          <br />
          <label>
            Email:
            <input v-model="user.email" type="email" />
            <span v-if="formErrors.email" class="error-message">{{ formErrors.email }}</span>
          </label>
          <br />
          <label>
            Avatar:
            <input type="file" @change="onAvatarChange" />
            <span v-if="formErrors.avatar" class="error-message">{{ formErrors.avatar }}</span>
          </label>
          <br />
          <button type="submit" :disabled="isFormInvalid">Save</button>
        </form>
  
        <div v-if="avatarPreview">
          <h2>Avatar Preview</h2>
          <img :src="avatarPreview" alt="Avatar" width="100" />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        user: null,              // 用户数据
        isLoading: false,        // 是否正在加载数据
        error: null,             // 错误信息
        avatarPreview: null,     // 头像预览
        formErrors: {},          // 表单验证错误
      };
    },
    computed: {
      isFormInvalid() {
        return Object.keys(this.formErrors).length > 0 || !this.user.name || !this.user.email;
      },
    },
    beforeCreate() {
      setTimeout(() => {
        console.log('beforeCreate: 实例化前');
        // 操作：初始化全局数据、监听其他事件
        this.$emit('beforeAppInit', 'app is about to be initialized');
      }, 1000);  // 延迟 1 秒
    },
    created() {
      setTimeout(() => {
        console.log('created: 实例已创建');
        // 操作：可以初始化一些数据，设置监听器
        this.fetchUserData();
        this.$emit('createdApp', 'app is now created');
      }, 1000);  // 延迟 1 秒
    },
    beforeMount() {
      setTimeout(() => {
        console.log('beforeMount: 渲染前');
        // 操作：进行数据的最终准备，处理缓存或者对一些组件进行最后优化
        this.checkUserCache();
      }, 1000);  // 延迟 1 秒
    },
    mounted() {
      setTimeout(() => {
        console.log('mounted: 组件挂载到 DOM 后');
        // 操作：进行依赖于 DOM 的操作，比如初始化插件、绑定外部库事件
        this.initializeAvatarPreview();
      }, 1000);  // 延迟 1 秒
    },
    beforeUpdate() {
      setTimeout(() => {
        console.log('beforeUpdate: 数据即将更新');
        // 操作：清理先前的状态、缓存，优化性能
        this.cleanupBeforeUpdate();
      }, 1000);  // 延迟 1 秒
    },
    updated() {
      setTimeout(() => {
        console.log('updated: 数据更新后');
        // 操作：响应式数据更新后的逻辑，例如通过 API 更新通知
        this.sendUserUpdateNotification();
      }, 1000);  // 延迟 1 秒
    },
    methods: {
      async fetchUserData() {
        this.isLoading = true;
        try {
          // 模拟获取用户数据的API请求
          const response = await this.simulateApiRequest();
          this.user = response.data;
          console.log('User data fetched successfully', this.user);
        } catch (error) {
          this.error = 'Failed to fetch user data';
          console.error(error);
        } finally {
          this.isLoading = false;
        }
      },
  
      simulateApiRequest() {
        // 模拟一个API请求，返回用户数据
        return new Promise((resolve, reject) => {
          setTimeout(() => {
            resolve({
              data: {
                name: 'Jane Doe',
                email: 'janedoe@example.com',
                avatar: 'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/toolspics/1.png',
              },
            });
          }, 1000);
        });
      },
  
      updateUserData() {
        console.log('updateUserData: 提交用户数据');
        this.isLoading = true;
        this.formErrors = {}; // 清空表单错误
  
        // 校验表单
        if (!this.user.name) {
          this.formErrors.name = 'Name is required';
        }
        if (!this.user.email || !this.isValidEmail(this.user.email)) {
          this.formErrors.email = 'A valid email is required';
        }
        if (!this.avatarPreview) {
          this.formErrors.avatar = 'Avatar is required';
        }
  
        if (Object.keys(this.formErrors).length > 0) {
          this.isLoading = false;
          return; // 如果有错误则不提交
        }
  
        // 模拟更新后的静态数据
        const updatedUser = {
          name: 'John Smith',
          email: 'johnsmith@example.com',
          avatar: 'https://emoji.bj.bcebos.com/yige-aigc/index_aigc/final/toolspics/1.png',
        };
  
        // 模拟延迟操作
        setTimeout(() => {
          this.user = updatedUser;
          console.log('User updated successfully', this.user);
          alert('User data updated successfully!');
          this.isLoading = false;
        }, 1000);
      },
  
      isValidEmail(email) {
        const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return regex.test(email);
      },
  
      onAvatarChange(event) {
        const file = event.target.files[0];
        if (file) {
          if (!file.type.startsWith('image/')) {
            this.formErrors.avatar = 'Only image files are allowed';
            return;
          }
  
          const reader = new FileReader();
          reader.onload = () => {
            this.avatarPreview = reader.result; // 更新头像预览
          };
          reader.readAsDataURL(file);
        }
      },
  
      // Additional helper methods
      checkUserCache() {
        // This could check if user data is already in local storage or indexed DB
        console.log('Checking if user data is cached');
      },
  
      initializeAvatarPreview() {
        // Initialize the preview of the user's avatar or setup default avatar
        if (!this.avatarPreview && this.user && this.user.avatar) {
          this.avatarPreview = this.user.avatar;
        }
      },
  
      cleanupBeforeUpdate() {
        // Perform tasks such as removing event listeners or resetting states before update
        console.log('Cleaning up before update');
      },
  
      sendUserUpdateNotification() {
        // Send notifications or make API calls after user data is updated
        console.log('User data updated, notifying other components or systems');
      },
    },
    beforeDestroy() {
      setTimeout(() => {
        console.log('beforeDestroy: 组件销毁前');
        // 清理资源，如停止定时器，取消请求等
        this.$emit('beforeComponentDestroy', 'cleaning up before destruction');
      }, 1000);  // 延迟 1 秒
    },
    destroyed() {
      setTimeout(() => {
        console.log('destroyed: 组件销毁后');
        // 组件销毁后的操作，如解绑事件、清理占用的内存
        this.$emit('componentDestroyed', 'component has been destroyed');
      }, 1000);  // 延迟 1 秒
    },
  };
  </script>
  
  <style scoped>
  .error {
    color: red;
  }
  .error-message {
    color: red;
    font-size: 12px;
    margin-left: 10px;
  }
  .loading {
    font-size: 18px;
    color: #888;
  }
  button:disabled {
    background-color: #ddd;
  }
  </style>
  