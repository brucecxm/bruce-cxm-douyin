<template>
  <div class="friend-container">
    <el-input
      v-model="searchKeyword"
      placeholder="输入ID/用户名/手机号/邮箱搜索好友"
      clearable
      @keyup.enter.native="searchFriend"
    />
    <el-button type="primary" @click="searchFriend">搜索好友</el-button>

    <el-table :data="friendList" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="用户ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="openAddDialog(scope.row.id)"
          >添加好友</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="addDialogVisible" title="发送好友请求">
      <el-input v-model="verifyMsg" placeholder="验证消息（可选）" />
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sendRequest">发送请求</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from '@/utils/request';

export default {
  data() {
    return {
      searchKeyword: '',
      friendList: [],
      addDialogVisible: false,
      selectedUserId: null,
      verifyMsg: ''
    };
  },
  methods: {
    searchFriend() {
      axios
        .get('/user/friend/list/search', {
          params: { keyword: this.searchKeyword }
        })
        .then((res) => {
          this.friendList = res.data;
        });
    },
    openAddDialog(userId) {
      this.selectedUserId = userId;
      this.verifyMsg = '';
      this.addDialogVisible = true;
    },
    sendRequest() {
      axios
        .post('/user/friend/request', {
          toUserId: this.selectedUserId,
          verifyMsg: this.verifyMsg
        })
        .then(() => {
          this.$message.success('好友请求已发送');
          this.addDialogVisible = false;
        });
    }
  }
};
</script>
<style scoped>
.friend-container {
  padding: 15px;
  background-color: #f9f9f9;
}

.card-box {
  margin-bottom: 20px;
  border-radius: 10px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.full-button {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}
</style>
