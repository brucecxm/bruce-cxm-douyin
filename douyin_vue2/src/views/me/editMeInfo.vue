<template>
  <div class="profile-page">
    <!-- 封面 -->
    <div class="cover">
      <el-button class="change-cover-btn" type="primary" size="mini">更换封面</el-button>
    </div>

    <!-- 用户信息展示 -->
    <div class="profile-info" v-if="!currentEdit">
      <div class="avatar-box">
        <img :src="userInfo.userPic" class="avatar" />
      </div>
      <div class="progress">资料完善度 60%</div>

      <el-form label-width="80px" class="form">
        <el-form-item label="名字" @click.native="openEdit('name')">
          <span>{{ userInfo.nickname || '填写名字' }}</span>
        </el-form-item>

        <el-form-item label="简介" @click.native="openEdit('bio')">
          <span class="placeholder">{{ userInfo.jieshao || '介绍喜好、个性或@你的亲友' }}</span>
        </el-form-item>

        <el-form-item label="性别" @click.native="genderDialogVisible = true">
          <span>{{ userInfo.gender || '不展示' }}</span>
        </el-form-item>

        <el-form-item label="生日" @click.native="birthdayDialogVisible = true">
          <span>{{ userInfo.birthday || '未填写' }}</span>
        </el-form-item>

        <el-form-item label="所在地" @click.native="openEdit('location')">
          <span class="placeholder">{{ userInfo.city || '填写地区' }}</span>
        </el-form-item>

        <el-form-item label="学校" @click.native="openEdit('school')">
          <span class="placeholder">{{ userInfo.school || '填写学校' }}</span>
        </el-form-item>

        <el-form-item label="抖音号" @click.native="openEdit('douyin')">
          <span class="highlight">{{ userInfo.username || '未设置' }}</span>
        </el-form-item>
      </el-form>
    </div>

    <!-- 编辑页模拟滑入 -->
    <transition name="slide">
      <div v-if="currentEdit" class="edit-page">
        <el-page-header
          @back="currentEdit = ''"
          :content="'编辑 ' + editTitleMap[currentEdit]"
        />

        <div class="edit-content">
          <el-input v-model="editValue" placeholder="请输入..." />
          <el-button type="primary" @click="saveEdit">保存</el-button>
        </div>
      </div>
    </transition>

    <!-- 性别弹窗 -->
    <el-dialog title="选择性别" :visible.sync="genderDialogVisible" append-to-body custom-class="bottom-dialog">
      <el-radio-group v-model="userInfo.gender" style="display: flex; justify-content: space-around;">
        <el-radio-button label="男" />
        <el-radio-button label="女" />
        <el-radio-button label="不展示" />
      </el-radio-group>
      <span slot="footer">
        <el-button @click="genderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="genderDialogVisible = false">确定</el-button>
      </span>
    </el-dialog>

    <!-- 生日弹窗 -->
    <el-dialog title="选择生日" :visible.sync="birthdayDialogVisible" append-to-body custom-class="bottom-dialog">
      <el-date-picker
        v-model="userInfo.birthday"
        type="date"
        placeholder="选择日期"
        style="width: 100%;"
        value-format="yyyy-MM-dd"
      />
      <span slot="footer">
        <el-button @click="birthdayDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="birthdayDialogVisible = false">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { userInfoService } from '@/api/user';

export default {
  data() {
    return {
      userInfo: {}, // 用户信息
      currentEdit: '', // 当前编辑项
      editValue: '',
      genderDialogVisible: false,
      birthdayDialogVisible: false,
      editTitleMap: {
        name: '名字',
        bio: '简介',
        location: '所在地',
        school: '学校',
        douyin: '抖音号'
      }
    };
  },
  mounted() {
    this.getUserInfo(); // 获取用户信息
  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      userInfoService()
        .then(res => {
          this.userInfo = res.data; // 更新用户信息
        })
        .catch(err => {
          console.error(err);
        });
    },
    // 打开编辑框
    openEdit(field) {
      this.currentEdit = field;
      this.editValue = this.userInfo[field] || '';
    },
    // 保存编辑
    saveEdit() {
      this.userInfo[this.currentEdit] = this.editValue;
      this.currentEdit = '';
    }
  }
};
</script>

<style scoped>
.profile-page {
  font-family: sans-serif;
  background: #f5f5f5;
  min-height: 100vh;
}
.cover {
  height: 160px;
  background: linear-gradient(to right, #a6c0fe, #f68084);
  position: relative;
}
.change-cover-btn {
  position: absolute;
  top: 10px;
  right: 10px;
}
.avatar-box {
  position: relative;
  z-index: 10;
  display: flex;
  justify-content: center;
  margin-top: -40px;
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #fff;
}
.progress {
  text-align: center;
  margin: 10px 0;
  color: #888;
}
.form .el-form-item {
  border-bottom: 1px solid #eee;
  padding: 8px 0;
}
.placeholder {
  color: #bbb;
}
.highlight {
  font-weight: bold;
}
.edit-page {
  position: fixed;
  top: 0;
  right: 0;
  background: #fff;
  width: 100%;
  height: 100%;
  z-index: 999;
  padding: 20px;
}
.edit-content {
  margin-top: 30px;
}
.slide-enter-active, .slide-leave-active {
  transition: all 0.3s ease;
}
.slide-enter {
  transform: translateX(100%);
}
.slide-leave-to {
  transform: translateX(100%);
}
/* 自定义底部弹出 dialog */
::v-deep .bottom-dialog .el-dialog {
  bottom: 0;
  margin: 0;
  border-radius: 10px 10px 0 0;
  position: absolute;
  width: 100%;
}
</style>
