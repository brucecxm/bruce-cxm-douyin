<template>
 <div>


<button @click="cha">查询</button>
<button @click="zeng">增加</button>
<button @click="gai">修改</button>
<button @click="shan">删除</button>

  <el-table
      :data="tableData"
      style="width: 100%">
      <el-table-column
        prop="id"
        label="日期"
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="id"
        label="地址">
      </el-table-column>
    </el-table>

 </div>


</template>

<script>

import { testzeng,testshan,testgai,testcha } from '@/api/test.js';



export default {
  data() 
  { 
  
    return {
      
      tableData: [
  
        ]
    };
  },
  methods: {

    cha() {
      testcha().then(res => {
        console.log(res);
        
        this.tableData = res.data.data;
      });
    },

zeng() {


var data = {
id: 1,
name:1
}


      testzeng(data).then(res => {
        console.log(res);
        this.tableData = res.data;
      });
    },
    gai() {


var data = {
id: 1,
name:555555555
}

      testgai(data).then(res => {
        console.log(res);
        this.tableData = res.data;
      });
    },
shan() {
  var data = {
id: 1,

}
      testshan(data).then(res => {
        console.log(res);
        this.tableData = res.data;
      });
    }

  }

};
</script>

<style scoped>
 
</style>
