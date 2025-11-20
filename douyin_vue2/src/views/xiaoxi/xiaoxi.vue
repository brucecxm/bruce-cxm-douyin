<template>
  <div class="xiaoxi">
    <caidan :style="caidancss"></caidan>
    <div class="other" :style="othercss">
      <!-- <div class="header">
        <i class="icon" @click="showcaidanone">☰</i>
        <i class="icon">🏠</i>
        <i class="icon">🔍</i>
      </div> -->
      <div class="icon-message-container">
        <!-- 左侧第一个图标 -->
        <div class="icon left-icon-1" id="icon-left-1"></div>

        <!-- 左侧第二个图标 -->
        <div class="icon left-icon-2" id="icon-left-2"></div>

        <!-- 中间消息文本 -->
        <div class="message-content">消息</div>
        <!-- 右侧第一个图标 -->
        <div class="icon right-icon-1" id="icon-right-1"></div>

        <!-- 右侧第二个图标 -->
        <div class="icon right-icon-2" id="icon-right-2"></div>
      </div>
      <div class="commentbody" ref="scrollContainer">
        <div class="user-list">
          <HorizontalScrollList
            :nameStyle="{
              fontSize: '14px',
              fontWeight: 'bold',
              color: '#333',
              marginTop: '8px'
            }"
            :users="topUsers"
            :avatarStyle="{
              width: '60px',
              height: '60px',
              border: '2px solid #1890ff',
              borderRadius: '30px' // 改为方形
            }"
            @user-click="handleUserClick"
          />
          <!-- <el-input
            v-model="input"
            placeholder="请输入用户名"
            prefix-icon="el-icon-search"
            clearable
          ></el-input> -->
          <!-- <user-info
            :containerStyle="{
              backgroundColor: '#f0f0f0',
              padding: '10px',
              borderRadius: '8px',
              margin: '10px 0'
            }"
            :avatarInnerStyle="{
              borderRadius: '0', // 方形头像
              border: '3px solid #1890ff',
              boxShadow: '0 0 8px rgba(24, 144, 255, 0.5)'
            }"
            :usernameStyle="{
              fontSize: '18px',
              fontWeight: 'bold',
              color: '#1890ff',
              marginBottom: '5px'
            }"
            :statusStyle="{
              fontSize: '14px',
              color: '#52c41a',
              fontStyle: 'italic'
            }"
            :onlineDotStyle="{
              width: '15px',
              height: '15px',
              backgroundColor: '#ff4d4f', // 红色
              borderWidth: '3px',
              borderColor: '#fff2f0'
            }"
            v-for="(item, index) in filteredUsers"
            :key="index"
            :message="item"
          /> -->
          <user-info
            :avatarInnerStyle="{
              border: '3px solid #1890ff',
              boxShadow: '0 0 8px rgba(24, 144, 255, 0.5)'
            }"
            :usernameStyle="{
              fontSize: '18px',
              fontWeight: 'bold',
              color: '#1890ff',
              marginBottom: '5px'
            }"
            :statusStyle="{
              fontSize: '14px',
              color: '#52c41a',
              fontStyle: 'italic'
            }"
            :onlineDotStyle="{
              width: '15px',
              height: '15px',
              backgroundColor: '#ff4d4f', // 红色
              borderWidth: '3px',
              borderColor: '#fff2f0'
            }"
            v-for="(item, index) in filteredUsers"
            :key="index"
            :message="item"
            @click="handleUserClick(item)"
          />
          <div v-if="filteredUsers.length === 0" class="no-data">暂无数据</div>

          <div v-if="loading" style="text-align: center; padding: 10px">
            加载中...
          </div>
          <div
            v-if="!hasMore && userinfos.length > 0"
            style="text-align: center; padding: 10px; color: #888"
          >
            没有更多数据了
          </div>
        </div>
      </div>
    </div>
    <div class="footerbox">
      <footer-vue class="footer"></footer-vue>
    </div>
  </div>
</template>

<script>
import HorizontalScrollList from '@/components/HorizontalScrollList.vue';

import UserInfo from '../../components/userInfo.vue';
import caidan from '@/components/caidan.vue';
import footerVue from '@/components/footer.vue';
import { getFriendApi, getFriendApiDetail } from '@/api/user.js';

export default {
  components: {
    footerVue,
    caidan,
    UserInfo,
    HorizontalScrollList
  },
  data() {
    return {
      // 可以在这里定义图标路径，方便后续修改
      iconPaths: {
        // 使用本地图片路径
        left1: '@/assets/xiaoxi/header/xiaoxi_add.png',
        left2: '@/assets/xiaoxi/header/xiaoxi_hongbao.png',
        right1: '@/assets/xiaoxi/header/xiaoxi_menu.png',
        right2: '@/assets/xiaoxi/header/xiaoxi_search.png'
      },
      // 增加默认用户数据，确保初始页面不会空白
      userinfos: [
        {
          username: '初始用户1',
          status: '在线',
          avatar:
            'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
          height: '60px'
        },
        {
          username: '初始用户2',
          status: '离线',
          avatar:
            'http://gips0.baidu.com/it/u=3560029307,576412275&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
          height: '60px'
        }
      ],
      topUsers: [],
      input: '',
      page: 1,
      pageSize: 5,
      hasMore: true,
      loading: false,
      caidancss: {
        position: 'absolute',
        right: '100vw',
        bottom: '0px',
        transition: 'right 0.5s ease',
        height: '100vh',
        width: '80vw',
        background: 'white',
        zIndex: 1000
      },
      othercss: {
        position: 'relative',
        right: '0vw',
        bottom: '0px',
        transition: 'right 0.5s ease',
        height: '100vh',
        width: '100vw'
      }
    };
  },
  computed: {
    filteredUsers() {
      const keyword = this.input.trim().toLowerCase();
      if (!keyword) {
        return this.userinfos;
      }
      return this.userinfos.filter((user) =>
        user.username.toLowerCase().includes(keyword)
      );
    }
  },
  methods: {
    showcaidanone() {
      if (this.caidancss.right === '100vw') {
        this.caidancss.right = '20vw';
        this.othercss.right = '-80vw';
      } else {
        this.caidancss.right = '100vw';
        this.othercss.right = '0vw';
      }
    },

    async fetchFriendInfos() {
      try {
        const response = await getFriendApiDetail();
        // 检查响应数据是否存在且有效
        if (response && response.data && Array.isArray(response.data)) {
          // 将朋友数据转换为topUsers所需的格式
          this.topUsers = response.data.map((friend) => ({
            name: friend.nickname || friend.username || '未知用户',
            avatar: friend.avatar || '默认头像地址',
            userId: friend.userId || '未知用户ID',
            heat: Math.floor(Math.random() * 100) // 可以根据实际需求设置热度值
          }));
          console.log('朋友列表加载成功:', this.topUsers);
        } else {
          console.error('朋友列表数据格式错误:', response);
          // 保留默认数据作为备用
        }
      } catch (error) {
        console.error('加载朋友列表失败:', error);
        // 保留默认数据作为备用
      }
    },

    async fetchUserInfos() {
      if (this.loading || !this.hasMore) return;
      this.loading = true;
      console.log('开始加载第', this.page, '页数据');

      // 模拟异步请求延迟
      await new Promise((resolve) => setTimeout(resolve, 800));

      // 模拟总数据，30条 - 增加更多数据并确保用户名列有变化，便于观察
      const totalData = [];
      // 生成30条不同的用户数据
      for (let i = 1; i <= 30; i++) {
        totalData.push({
          username: i <= 20 ? `用户${i}` : `访客${i - 20}`,
          status: i % 3 === 0 ? '离线' : '在线',
          avatar: `http://gips0.baidu.com/it/u=3560029307,${576412274 + i}&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280`,
          height: '60px'
        });
      }

      const start = (this.page - 1) * this.pageSize;
      const end = this.page * this.pageSize;
      const pageData = totalData.slice(start, end);

      console.log(
        `加载数据范围: ${start}-${end}，加载数量: ${pageData.length}`
      );

      if (pageData.length < this.pageSize) {
        this.hasMore = false;
        console.log('已加载全部数据');
      }

      // 确保数据正确添加到列表
      this.userinfos.push(...pageData);
      console.log('当前用户列表长度:', this.userinfos.length);

      this.page += 1;
      this.loading = false;
    },

    handleScroll(e) {
      const container = e.target;
      // 调试信息，帮助确认滚动事件触发和计算值
      console.log('滚动事件触发');
      console.log(
        `scrollTop: ${container.scrollTop}, clientHeight: ${container.clientHeight}, scrollHeight: ${container.scrollHeight}`
      );

      // 优化滚动加载条件，增加容错范围
      if (
        container.scrollTop + container.clientHeight >=
        container.scrollHeight - 50 // 增加触发距离，更容易触发加载
      ) {
        console.log('触发加载更多');
        this.fetchUserInfos();
      }
    },

    // 处理用户点击事件，跳转到聊天界面
    handleUserClick(user) {
      console.log('User clicked in xiaoxi.vue:', user);
      // 检查用户对象是否存在
      if (!user) {
        console.error('User object is undefined or null');
        return;
      }

      // 检查用户对象中是否包含userId、id、username或name字段作为标识
      const friendId =
        user.userId || user.id || user.username || user.name || '';
      if (friendId) {
        console.log('Navigating to chat with ID:', friendId);
        // 跳转到聊天界面，传递好友id参数
        this.$router.push({
          path: '/chatyi',
          query: {
            id: friendId
          }
        });
      } else {
        console.warn('用户数据中缺少必要的标识字段，无法跳转');
      }
    }
  },

  mounted() {
    // 直接使用ref来定位具体的图标元素，避免样式冲突问题
    this.$nextTick(() => {
      try {
        // 为每个图标添加ID以便更精确地选择
        const iconElements = [
          {
            selector: '.left-icon-1',
            path: '/src/assets/xiaoxi/header/xiaoxi_add.png'
          },
          {
            selector: '.left-icon-2',
            path: '/src/assets/xiaoxi/header/xiaoxi_hongbao.png'
          },
          {
            selector: '.right-icon-1',
            path: '/src/assets/xiaoxi/header/xiaoxi_menu.png'
          },
          {
            selector: '.right-icon-2',
            path: '/src/assets/xiaoxi/header/xiaoxi_search.png'
          }
        ];

        iconElements.forEach((icon) => {
          const element = document.querySelector(icon.selector);
          if (element) {
            // 在Vite中，使用相对路径来加载本地图片
            // 由于在开发环境中，Vite会正确处理这些路径
            element.style.backgroundImage = `url(${icon.path})`;
            // 确保背景图片正确显示
            element.style.backgroundSize = 'contain';
            element.style.backgroundRepeat = 'no-repeat';
            element.style.backgroundPosition = 'center';
          }
        });

        // 额外添加全局样式来防止页面横向滚动
        document.body.style.overflowX = 'hidden';
        document.documentElement.style.overflowX = 'hidden';
      } catch (error) {
        console.error('设置图标时出错:', error);
      }
    });

    // 获取朋友列表数据
    this.fetchFriendInfos();
    // 获取用户信息数据
    this.fetchUserInfos();

    // 确保容器存在再添加事件监听
    this.$nextTick(() => {
      const container = this.$refs.scrollContainer;
      if (container) {
        // 先移除可能存在的监听器，避免重复
        container.removeEventListener('scroll', this.handleScroll);
        // 添加滚动事件监听
        container.addEventListener('scroll', this.handleScroll);
        console.log('滚动事件监听器已添加');
      } else {
        console.error('滚动容器未找到');
      }
    });
  },
  beforeDestroy() {
    const container = this.$refs.scrollContainer;
    container.removeEventListener('scroll', this.handleScroll);

    // 清理添加的全局样式
    document.body.style.overflowX = '';
    document.documentElement.style.overflowX = '';
  }
};
</script>

<style scoped>
.xiaoxi {
  overflow: hidden;
  overflow-x: hidden; /* 明确禁止横向滚动 */
  font-family: 'Arial', sans-serif;
  background-color: #ffffff; /* 修改为白色背景 */
  color: #333;
  min-height: 100vh;
  width: 100%;
  box-sizing: border-box;
  position: relative;
}
.other {
  position: relative;
  width: 100%;
  overflow-x: hidden;
  box-sizing: border-box;
  height: 100vh;
}

.header {
  background-color: #fff;
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  top: 0;
  left: 0;
  z-index: 10;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* 注释掉这个可能导致冲突的.icon样式定义 */
/* .icon {
  font-size: 24px;
  color: #333;
  cursor: pointer;
} */

.commentbody {
  overflow-y: auto;
  overflow-x: hidden; /* 禁止横向滚动 */
  height: calc(100vh - 120px); /* 调整高度计算，确保占满剩余空间 */
  padding: 10px;
  width: 100%;
  box-sizing: border-box;
  position: relative; /* 确保相对定位以便正确计算滚动 */
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
}

/* 确保子组件不会导致横向滚动 */
:deep(.HorizontalScrollList) {
  width: 100%;
  overflow-x: hidden;
  box-sizing: border-box;
}

/* 确保el-input不会超出容器 */
:deep(.el-input) {
  width: 100%;
  box-sizing: border-box;
}

.no-data {
  text-align: center;
  color: #999;
  margin-top: 20px;
}

/* Hide scrollbar */
.commentbody::-webkit-scrollbar {
  display: none;
}

.icon-message-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 100%; /* 修改为100%以适应屏幕宽度 */
  padding: 10px 20px;
  background-color: #ffffff; /* 修改为白色背景 */
  min-height: 60px;
  box-sizing: border-box;
  overflow-x: hidden;
  border: none;
  outline: none;
}

.icon {
  width: 30px;
  height: 30px;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  cursor: pointer;
  transition: transform 0.2s ease;
  display: inline-block;
  box-sizing: border-box;
  border: none;
  outline: none;
}

.icon:hover {
  transform: scale(1.05);
}

.message-content {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  padding: 0 20px;
  flex: 1;
  text-align: center;
}

/* 可以根据需要调整左右图标的间距 */
.left-icon-1 {
  margin-right: 15px;
}

.right-icon-1 {
  margin-right: 15px;
}

.footerbox {
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>
