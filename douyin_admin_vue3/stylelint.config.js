export default {
  extends: [
    'stylelint-config-standard-scss', // 标准 SCSS 规则集
    'stylelint-config-standard-vue/scss', // Vue 单文件组件中 SCSS 的标准规则
    'stylelint-config-recess-order', // CSS 属性排序规则（按 recess 约定）
    '@stylistic/stylelint-config', // 一套比较严格的风格规则
  ],
  plugins: [
    'stylelint-scss', // 插件，支持 SCSS 语法相关规则
  ],
  rules: {
    'at-rule-no-unknown': null, // 关闭未知 at 规则检测（比如忽略 v-bind）
    'no-descending-specificity': null, // 关闭选择器特异性下降检测
    'property-no-unknown': null, // 关闭未知属性检测
    'font-family-no-missing-generic-family-keyword': null, // 关闭字体族缺少通用字体关键字检测
    'selector-class-pattern': null, // 关闭选择器类名命名规则限制
    'function-no-unknown': [
      true,
      {
        ignoreFunctions: [
          'v-bind', // 忽略 Vue 绑定函数
          'map-get', // SCSS map-get 函数
          'lighten', // SCSS 颜色淡化函数
          'darken', // SCSS 颜色加深函数
        ],
      },
    ],
    'selector-pseudo-element-no-unknown': [
      true,
      {
        ignorePseudoElements: [
          '/^view-transition/', // 忽略以 view-transition 开头的伪元素（新特性伪元素）
        ],
      },
    ],
    'scss/double-slash-comment-empty-line-before': null, // 关闭 SCSS 注释前空行检测
    'scss/no-global-function-names': null, // 关闭 SCSS 全局函数名限制（允许使用 map-get 等）
    '@stylistic/max-line-length': null, // 关闭最大行长限制
    '@stylistic/block-closing-brace-newline-after': [
      'always',
      {
        ignoreAtRules: ['if', 'else'], // 大括号闭合后总换行，排除 if 和 else 语句
      },
    ],
  },
  allowEmptyInput: true, // 允许空文件通过检测
  ignoreFiles: [
    'node_modules/**/*', // 忽略第三方依赖
    'dist*/**/*', // 忽略构建输出目录
  ],
}
