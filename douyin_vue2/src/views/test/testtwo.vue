<template>
    
  <div>
    <button @click="cha">查询</button>
    <el-table :data="tableData">
      <el-table-column prop="id" label="编号" />
      <el-table-column prop="name" label="姓名" />
    </el-table>
  </div>
</template>

<script>
import {  testcha } from '@/api/test';

export default {
  data() {
    return {
      tableData: []
    };
  },
  methods: {
    cha() {
      debugger
      testcha().then(res => {
        debugger
        console.log(res); 
        this.tableData = res.data.data; 
      }).catch(err => {
        console.error('查询失败', err);
      });
    }
  },
  mounted() {
    this.cha(); 
  }
};
</script>
