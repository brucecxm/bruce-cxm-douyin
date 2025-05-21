module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es6: true
  },
  extends: [
    'plugin:vue/essential',        // Vue 2 重要规则
    'eslint:recommended',          // ESLint 推荐规则
    'plugin:prettier/recommended'  // 让 Prettier 作为 ESLint 规则运行
  ],
  plugins: ['vue', 'prettier'],
  rules: {
    // prettier 相关格式规则
    'prettier/prettier': ['error', {
      singleQuote: true,
      semi: true,
      tabWidth: 2,
      trailingComma: 'none',
      endOfLine: 'auto'
    }],

    // 命名规范 - 驼峰
    camelcase: ['error', { properties: 'always' }],

    // 缩进 - 2空格
    indent: ['error', 2],

    // 强制单引号
    quotes: ['error', 'single'],

    // 分号
    semi: ['error', 'always'],

    // 对象花括号内强制空格
    'object-curly-spacing': ['error', 'always'],

    // Vue 模板缩进
    'vue/html-indent': ['error', 2],

    // Vue 属性换行规则
    'vue/max-attributes-per-line': ['error', {
      singleline: 3,
      multiline: {
        max: 1,
        allowFirstLine: false
      }
    }],

    // console 和 debugger 生产环境提示
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off'
  }
};
