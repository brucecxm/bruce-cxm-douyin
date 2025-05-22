<template>
  <div class="xiaoxi">
    <caidan :style="caidancss"></caidan>
    <div class="other" :style="othercss">
      <div class="header">
        <i class="icon" @click="showcaidanone">☰</i>
        <i class="icon">🏠</i>
        <i class="icon">🔍</i>
      </div>

      <div class="commentbody">
        <div class="user-list">
          <el-input
            v-model="input"
            placeholder="请输入用户名"
            prefix-icon="el-icon-search"
            clearable
          >
            >
          </el-input>

          <user-info
            v-for="(item, index) in filteredUsers"
            :key="index"
            :message="item"
          />
          <div v-if="filteredUsers.length === 0" class="no-data">暂无数据</div>
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
      dialogVisible: false,
      input: '',
      caidancss: {
        position: 'absolute',
        right: '100vw', // 初始在屏幕外
        bottom: '0px',
        transition: 'right 0.5s ease',
        height: '100vh',
        width: '80vw', // 菜单宽度设置为 80vw
        background: '#fff', // 可选
        zIndex: 1000
      },
      othercss: {
        position: 'relative',
        right: '0vw', // 初始正常显示
        bottom: '0px',
        transition: 'right 0.5s ease',
        height: '100vh',
        width: '100vw'
      },
      userinfos: [
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
      ]
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
    goxiaodetail(id) {
      this.$router.push({ path: `/xiaodetail/${id}` });
    },
    showcaidanone() {
      if (this.caidancss.right === '100vw') {
        // 菜单滑入，other也向右滑80vw
        this.caidancss.right = '20vw';
        this.othercss.right = '-80vw';
      } else {
        // 菜单滑出，other归位
        this.caidancss.right = '100vw';
        this.othercss.right = '0vw';
      }
    }
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

::v-deep .jiahao {
  background-image: url(../../assets/home/加号黑.png);
}

.touxiang img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}
.commentbody {
  overflow-y: auto;
  height: calc(100vh - 60px - 50px);
  padding: 10px;
}

.comment {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 10px;
  padding: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.comment:hover {
  background-color: #f9f9f9;
}

.neirong {
  margin-left: 15px;
  flex: 1;
}

.username {
  font-weight: bold;
  color: #333;
  font-size: 16px;
}

.message {
  display: block;
  color: #777;
  font-size: 14px;
  margin-top: 5px;
}

/* Hide scrollbar */
.commentbody::-webkit-scrollbar {
  display: none;
}
</style>
