<template>
  <div class="user-relation-page">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>用户关系管理</span>
        <el-input
          v-model="userId"
          placeholder="请输入当前用户ID"
          style="width: 200px; float: right"
        />
      </div>

      <el-form inline>
        <el-form-item label="目标用户ID">
          <el-input v-model="targetUserId" placeholder="请输入目标用户ID" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="follow">关注</el-button>
          <el-button type="success" @click="unfollow">取消关注</el-button>
          <el-button type="warning" @click="addBestFriend">加密友</el-button>
          <el-button type="danger" @click="block">拉黑</el-button>
          <el-button type="danger" @click="unblock">取消拉黑</el-button>
          <el-button type="info" @click="canChat">是否可聊天</el-button>
          <el-button type="info" @click="getRelationStatus">查看状态</el-button>
        </el-form-item>
      </el-form>

      <el-divider>查看关系列表</el-divider>

      <el-button-group>
        <el-button @click="loadFollowList">我关注的</el-button>
        <el-button @click="loadFansList">我的粉丝</el-button>
        <el-button @click="loadFriends">我的好友</el-button>
        <el-button @click="loadBestFriends">我的密友</el-button>
        <el-button @click="loadBlacklist">我的黑名单</el-button>
      </el-button-group>

      <el-table
        :data="resultList"
        v-if="resultList.length"
        style="margin-top: 20px"
      >
        <el-table-column prop="userId" label="发起者ID" />
        <el-table-column prop="targetUserId" label="目标ID" />
        <el-table-column prop="relationType" label="关系类型" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      userId: '', // 模拟登录用户ID
      targetUserId: '',
      resultList: []
    };
  },
  methods: {
    getHeaders() {
      return {
        headers: {
          // 模拟登录，前提是你登录后会写入token
          satoken: localStorage.getItem('token') || ''
        }
      };
    },

    async follow() {
      await axios.post(
        `/api/user/relation/follow?targetUserId=${this.targetUserId}`,
        {},
        this.getHeaders()
      );
      this.$message.success('关注成功');
    },

    async addBestFriend() {
      await axios.post(
        `/api/user/relation/addBestFriend?targetUserId=${this.targetUserId}`,
        {},
        this.getHeaders()
      );
      this.$message.success('已加密友');
    },

    async block() {
      await axios.post(
        `/api/user/relation/block?targetUserId=${this.targetUserId}`,
        {},
        this.getHeaders()
      );
      this.$message.success('已拉黑');
    },
    async unblock() {
      await axios.post(
        `/api/user/relation/unblock?targetUserId=${this.targetUserId}`,
        {},
        this.getHeaders()
      );
      this.$message.success('已取消拉黑');
    },

    async canChat() {
      const res = await axios.get(
        `/api/user/relation/canChat?targetUserId=${this.targetUserId}`,
        this.getHeaders()
      );
      this.$message.info('是否可聊天: ' + (res.data.data ? '是' : '否'));
    },

    async loadFollowList() {
      const res = await axios.get(
        '/api/user/relation/myFollowList',
        this.getHeaders()
      );
      this.resultList = res.data.data;
    },

    async loadFansList() {
      const res = await axios.get(
        '/api/user/relation/myFansList',
        this.getHeaders()
      );
      this.resultList = res.data.data;
    },

    async loadFriends() {
      const res = await axios.get(
        '/api/user/relation/myFriends',
        this.getHeaders()
      );
      this.resultList = res.data.data;
    },

    async loadBestFriends() {
      const res = await axios.get(
        '/api/user/relation/myBestFriends',
        this.getHeaders()
      );
      this.resultList = res.data.data;
    },

    async loadBlacklist() {
      const res = await axios.get(
        '/api/user/relation/myBlacklist',
        this.getHeaders()
      );
      this.resultList = res.data.data;
    },
    async unfollow() {
      try {
        await axios.delete('/api/user/relation/unfollow', {
          params: { targetUserId: this.targetUserId },
          ...this.getHeaders()
        });
        this.$message.success('已取消关注');
      } catch (e) {
        this.$message.error('取消关注失败');
      }
    },

    async getRelationStatus() {
      try {
        const res = await axios.get('/api/user/relation/status', {
          params: { targetUserId: this.targetUserId },
          ...this.getHeaders()
        });
        const status = res.data.data;
        this.$alert(
          `
      关注: ${status.followed ? '是' : '否'}<br/>
      粉丝: ${status.fan ? '是' : '否'}<br/>
      互关: ${status.mutual ? '是' : '否'}<br/>
      拉黑: ${status.blocked ? '是' : '否'}<br/>
      密友: ${status.bestFriend ? '是' : '否'}
    `,
          '关系状态',
          {
            dangerouslyUseHTMLString: true
          }
        );
      } catch (e) {
        this.$message.error('获取关系状态失败');
      }
    }
  }
};
</script>
<style scoped>
.user-relation-page {
  padding: 20px;
}
.box-card {
  max-width: 800px;
  margin: 0 auto;
}
</style>
