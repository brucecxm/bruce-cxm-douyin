<template>
  <div class="test-container">
    <!-- 交易金额颜色规则测试表 -->
    <el-table :data="testData" border style="width: 100%">
      <el-table-column prop="case" label="测试场景" width="200" />
      <el-table-column prop="amount" label="金额(Amount)" width="120" />
      <el-table-column prop="dcFlag" label="借贷标志(DcFlag)" width="120" />
      <el-table-column label="显示效果" width="200">
        <!-- 核心颜色判断逻辑 -->
        <template slot-scope="scope">
          <span
            :class="[
              'amount-style',
              scope.row.amount > 0
                ? scope.row.dcFlag === 'C'
                  ? 'green'
                  : 'red'
                : scope.row.amount < 0
                  ? scope.row.dcFlag === 'C'
                    ? 'red'
                    : 'green'
                  : scope.row.dcFlag === 'C'
                    ? 'green'
                    : 'red'
            ]"
          >
            {{ scope.row.displayText }}
          </span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 颜色说明 -->
    <div class="legend" style="margin-top: 20px">
      <span class="green">● 绿色：收入/正向</span>
      <span class="red" style="margin-left: 20px">● 红色：支出/负向</span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 测试数据：覆盖所有金额和DcFlag组合
      testData: [
        // 金额>0的情况
        {
          case: '金额为正 + C',
          amount: 500,
          dcFlag: 'C',
          displayText: '+500.00'
        },
        {
          case: '金额为正 + D',
          amount: 800,
          dcFlag: 'D',
          displayText: '-800.00'
        },

        // 金额<0的情况
        {
          case: '金额为负 + C',
          amount: -300,
          dcFlag: 'C',
          displayText: '-300.00'
        },
        {
          case: '金额为负 + D',
          amount: -600,
          dcFlag: 'D',
          displayText: '+600.00'
        },

        // 金额=0的情况（重点验证）
        {
          case: '金额为0 + C',
          amount: 0,
          dcFlag: 'C',
          displayText: '0.00'
        },
        {
          case: '金额为0 + D',
          amount: 0,
          dcFlag: 'D',
          displayText: '0.00'
        }
      ]
    };
  }
};
</script>

<style>
/* 基础样式 */
.amount-style {
  font-size: 14px;
  font-weight: 500;
}
/* 颜色规则 */
.green {
  color: #00b42a;
} /* 绿色-正向 */
.red {
  color: #f53f3f;
} /* 红色-负向 */
</style>
