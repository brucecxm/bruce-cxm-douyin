<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form :model="form" :rules="rules" ref="registerForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="form.confirmPassword" autocomplete="off" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register">注册</el-button>
          <el-button @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { testlogin } from '@/api/test'; // 注册接口

export default {
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.form.password) {
                callback(new Error('两次输入密码不一致'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    register() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          const user = {
            username: this.form.username,
            password: this.form.password
          };
          testregister(user)
            .then(res => {
              this.$message.success('注册成功');
              this.$router.push('/login');
            })
            .catch(err => {
              this.$message.error('注册失败：' + (err.message || ''));
            });
        }
      });
    }
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  height: 100vh;
  justify-content: center;
  align-items: center;
  background: #f5f5f5;
}
.register-card {
  width: 400px;
  padding: 20px;
}
.register-title {
  text-align: center;
  margin-bottom: 20px;
}
</style>
