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
          <HorizontalScrollList :users="topUsers" />
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
import HorizontalScrollList from '@/components/HorizontalScrollList.vue';

import UserInfo from '../../components/userInfo.vue';
import caidan from '@/components/caidan.vue';
import footerVue from '@/components/footer.vue';

export default {
  components: {
    footerVue,
    caidan,
    UserInfo,
    HorizontalScrollList
  },
  data() {
    return {
      topUsers: [
        {
          name: '单曲猫咪',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280',
          heat: 91
        },
        {
          name: '阿泽',
          avatar:
            'http://gips2.baidu.com/it/u=4160611580,2154032802&fm=3028&app=3028&f=JPEG&fmt=auto?w=720&h=1280'
        }
      ],
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
        }
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
