<template>
  <div >
<!-- 
<img class="tupian"  src="http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960">
<img class="y"  src="http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960">
  <button @click="tiaozhuan">登录</button>
  <button @click="tiaozhuan2">点击</button>
  <button @click="tiaozhuan3">点击</button>
  <button @click="tiaozhuan4">点击</button>  -->
  <!-- <el-input v-model="input" placeholder="请输入内容"></el-input>
  <el-button>默认按钮</el-button>
  <el-carousel indicator-position="outside">
    <el-carousel-item v-for="item in 4" :key="item">
      <h3>{{ item }}</h3>
    </el-carousel-item>
  </el-carousel>

  <el-transfer
    filterable
    :filter-method="filterMethod"
    filter-placeholder="请输入城市拼音"
    v-model="value"
    :data="data">
  </el-transfer> -->
  <button @click="getuserinfos">登录</button>
  <el-table
    :data="userInfos"
    style="width: 100%"
    height="250">
    <el-table-column
      fixed
      prop="email"
      label="日期"
      width="150">
    </el-table-column>
    <el-table-column
      prop="id"
      label="姓名"
      width="120">
    </el-table-column>
    <el-table-column
      prop="backImg"
      label="省份"
      width="120">
    </el-table-column>
    <el-table-column
      prop="city"
      label="市区"
      width="120">
    </el-table-column>
    <el-table-column
      prop="address"
      label="地址"
      width="300">
    </el-table-column>
    <el-table-column
      prop="zip"
      label="邮编"
      width="120">
    </el-table-column>
  </el-table>
  </div>
</template>

<script>
import { userLoginService } from "../../api/user.js";
import { userInfoUpdateService } from "../../api/user.js"; // 引入登录服务
import { userInfoServiceSSSS } from "../../api/user.js";
export default {
  data() 
  { 
  
    return {
      
      tableData: [{
          date: '2016-05-03',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-02',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-04',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-01',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-08',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-06',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-07',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }],
      user: null,              // 用户数据
      input: '',
   
      


userInfo: [],
userInfos:[]

    };
  },
  methods: {

    getuserinfo()
    {


      userInfoServiceSSSS().then(response => {
      
        console.log(response); // 得到后端数据

this.userInfo= response.data.data
        console.log(this.userInfo);

      })
      .catch(error => {
        console.error("请求失败：", error);
      });




    },

    
    getuserinfos()
    {


      userInfoServiceSSSS().then(response => {
      
        console.log(response); // 得到后端数据

this.userInfos= response.data.data
        console.log(this.userInfos);

      })
      .catch(error => {
        console.error("请求失败：", error);
      });




    },

    tiaozhuan() {

        
               userLoginService();
   
    },
    tiaozhuan2() {
      
      userInfoUpdateService({
        userId: 1,
        userName: "张三",
        userAge: 18,
        userSex: "男",
      });
    },
    tiaozhuan3() {
      

    var xaa=  userInfoServiceSSSS();
      userInfoServiceSSSS() .then(response => {
    console.log(response); // 得到后端数据
    this.tableData= response.data.data
  })
  .catch(error => {
    console.error("请求失败：", error);
  });
  // this.tableData= xaa.data.data

    console.log(xaa+"sss");
    
    console.log("前面的已经被打印");

    },
    tiaozhuan4() {
      
      userInfoServiceSSSS().then(response => {
        console.log(response); // 得到后端数据
      })
      .catch(error => {
        console.error("请求失败：", error);
      });
    },
  },


};
</script>

<style scoped>
 .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
  }
  
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

.tupian{
  width: 100px;
  height: 200px;
  border-radius: 10px;
  margin-top: 2vh;
}
.y{
  width: 500px;
  height: 500px;
  border-radius: 10px;
  margin-top: 2vh;
}
</style>
