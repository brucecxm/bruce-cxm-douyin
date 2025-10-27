// Vuex 注入测试文件
// 此文件用于验证 Vuex 是否正确注入到项目中

// 测试 Vuex 模块是否正确导入
console.log('=== Vuex 注入测试 ===');

try {
  // 测试 store 是否正确导入
  const store = require('./index.js').default;
  console.log('✅ Store 导入成功:', typeof store);
  
  // 测试模块是否正确注册
  const modules = store._modules.root._children;
  console.log('✅ 注册的模块:', Object.keys(modules));
  
  // 测试状态访问
  console.log('✅ 根状态:', store.state);
  
  // 测试模块状态
  if (store.state.theme) {
    console.log('✅ Theme 模块状态:', store.state.theme);
  }
  
  if (store.state.auth) {
    console.log('✅ Auth 模块状态:', store.state.auth);
  }
  
  console.log('🎉 Vuex 注入测试完成！');
  
} catch (error) {
  console.error('❌ Vuex 注入测试失败:', error.message);
}

export default {};



