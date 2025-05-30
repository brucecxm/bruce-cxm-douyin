<template>
  <div class="xiaoxi">
    <caidan :style="caidancss"></caidan>
    <div class="other" :style="othercss">
      <div class="header">
        <i class="icon" @click="showcaidanone">☰</i>
        <i class="icon">🏠</i>
        <i class="icon">🔍</i>
      </div>

      <div class="commentbody" ref="scrollContainer">
        <div class="user-list">
          <el-input
            v-model="input"
            placeholder="请输入用户名"
            prefix-icon="el-icon-search"
            clearable
          ></el-input>

          <user-info
            v-for="(item, index) in filteredUsers"
            :key="index"
            :message="item"
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

      <footer-vue class="footer"></footer-vue>
    </div>
  </div>
</template>

<script>
import UserInfo from '../../components/userInfo.vue';
import caidan from '@/components/caidan.vue';
import footerVue from '@/components/footer.vue';

export default {
  components: {
    footerVue,
    caidan,
    UserInfo
  },
  data() {
    return {
      input: '',
      userinfos: [],
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
        background: '#fff',
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

    async fetchUserInfos() {
      if (this.loading || !this.hasMore) return;
      this.loading = true;

      // 模拟异步请求延迟
      await new Promise((resolve) => setTimeout(resolve, 1000));

      // 模拟总数据，30条
      const totalData = [
        {
          username: 'wang',
          status: '在线',
          avatar:
            'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
          height: '60px'
        },
        {
          username: 'li',
          status: '在线',
          avatar:
            'http://gips0.baidu.com/it/u=3560029307,576412274&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280',
          height: '60px'
        },
        {
          username: 'zhang',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/men/10.jpg',
          height: '60px'
        },
        {
          username: 'liu',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/women/20.jpg',
          height: '60px'
        },
        {
          username: 'chen',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/30.jpg',
          height: '60px'
        },
        {
          username: 'yang',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/women/40.jpg',
          height: '60px'
        },
        {
          username: 'zhao',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/50.jpg',
          height: '60px'
        },
        {
          username: 'wu',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/women/60.jpg',
          height: '60px'
        },
        {
          username: 'sun',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/70.jpg',
          height: '60px'
        },
        {
          username: 'ma',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/women/80.jpg',
          height: '60px'
        },
        {
          username: 'guo',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/15.jpg',
          height: '60px'
        },
        {
          username: 'he',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/women/25.jpg',
          height: '60px'
        },
        {
          username: 'gao',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/men/35.jpg',
          height: '60px'
        },
        {
          username: 'lin',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/women/45.jpg',
          height: '60px'
        },
        {
          username: 'fang',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/55.jpg',
          height: '60px'
        },
        {
          username: 'ding',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/women/65.jpg',
          height: '60px'
        },
        {
          username: 'liu2',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/75.jpg',
          height: '60px'
        },
        {
          username: 'hu',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/women/85.jpg',
          height: '60px'
        },
        {
          username: 'cai',
          status: '在线',
          avatar: 'https://randomuser.me/api/portraits/men/85.jpg',
          height: '60px'
        },
        {
          username: 'zheng',
          status: '离线',
          avatar: 'https://randomuser.me/api/portraits/women/95.jpg',
          height: '60px'
        }
        // 共20条，你可以根据需要继续扩充
      ];

      const start = (this.page - 1) * this.pageSize;
      const end = this.page * this.pageSize;
      const pageData = totalData.slice(start, end);

      if (pageData.length < this.pageSize) {
        this.hasMore = false;
      }

      this.userinfos.push(...pageData);
      this.page += 1;
      this.loading = false;
    },

    handleScroll(e) {
      const container = e.target;
      if (
        container.scrollTop + container.clientHeight >=
        container.scrollHeight - 10
      ) {
        this.fetchUserInfos();
      }
    }
  },

  mounted() {
    this.fetchUserInfos();

    const container = this.$refs.scrollContainer;
    container.addEventListener('scroll', this.handleScroll);
  },
  beforeDestroy() {
    const container = this.$refs.scrollContainer;
    container.removeEventListener('scroll', this.handleScroll);
  }
};
</script>

<style scoped>
.xiaoxi {
  overflow: hidden;
  font-family: 'Arial', sans-serif;
  background-color: #f4f6f9;
  color: #333;
  min-height: 100vh;
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

.icon {
  font-size: 24px;
  color: #333;
  cursor: pointer;
}

.commentbody {
  overflow-y: auto;
  height: calc(100vh - 60px - 50px);
  padding: 10px;
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
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
</style>
