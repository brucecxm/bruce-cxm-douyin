<template>
    
  <div>
    <button @click="cha">查询</button>
<button @click="zeng">新增</button>
<button @click="gai">修改</button>
<button @click="shan">删除</button>
    <el-table :data="tableData">
      <el-table-column prop="id" label="编号" />
      <el-table-column prop="nickname" label="姓名" />
    </el-table>
  </div>
</template>

<script>
import {  testcha } from '@/api/test';
import {  testzeng } from '@/api/test';
import {  testgai } from '@/api/test';
import {  testshan } from '@/api/test';
export default {
  data() {
    return {
      tableData: []
    };
  },
  created(){
// this.cha();
  },
  methods: {
    cha() {
      
      testcha().then(res => {
        
        console.log(res); 
        this.tableData = res.data; 
      }).catch(err => {
        console.error('查询失败', err);
      });
    },


 zeng() {
      
var user={
  id:3,
  nickname:"xxx"
}

      testzeng(user).then(res => {
        
        console.log(res); 
        this.tableData = res.data; 
        this.cha()
      }).catch(err => {
        console.error('查询失败', err);
      });
    },


 gai() {
      
var user={
  id:3,
  nickname:"bbb",
  avatar:"sss"
}

      testgai(user).then(res => {
        
        console.log(res); 
        this.tableData = res.data; 
              this.cha()
      }).catch(err => {
        console.error('查询失败', err);
      });
    },


     shan() {
      
var user={
  id:3,
  nickname:"bbb",
  avatar:"sss"
}

      testshan(user).then(res => {
        
        console.log(res); 
        this.tableData = res.data; 
              this.cha()
      }).catch(err => {
        console.error('查询失败', err);
      });
    },

  },

};
</script>
