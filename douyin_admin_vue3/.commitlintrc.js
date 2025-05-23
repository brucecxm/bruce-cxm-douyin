/** @type {import('cz-git').UserConfig} */
export default {
  rules: {
    // 这里可以定义 commitlint 规则，参考：https://commitlint.js.org/#/reference-rules
    // 现在是空的，表示使用默认规则
  },
  prompt: {
    // alias 是自定义快捷提交类型，比如 fd 对应 docs: fix typos
    alias: { fd: 'docs: fix typos' },

    // 提交时交互的提示信息
    messages: {
      type: '选择你要提交的类型 :', // 提示选择提交类型
      scope: '选择一个提交范围（可选）:', // 提示选择影响范围（可选）
      customScope: '请输入自定义的提交范围 :', // 提示输入自定义范围
      subject: '填写简短精炼的变更描述 :\n', // 简短描述
      body: '填写更加详细的变更描述（可选）。使用 "|" 换行 :\n', // 详细描述，支持用 | 换行
      breaking: '列举非兼容性重大的变更（可选）。使用 "|" 换行 :\n', // 破坏性变更说明
      footerPrefixsSelect: '选择关联issue前缀（可选）:', // 选择关联 issue 前缀
      customFooterPrefixs: '输入自定义issue前缀 :', // 输入自定义 issue 前缀
      footer: '列举关联issue (可选) 例如: #31, #I3244 :\n', // 关联的 issue 编号
      confirmCommit: '是否提交或修改commit ?', // 确认是否提交
    },

    // 定义提交类型列表及对应 emoji 和描述
    types: [
      { value: 'feat', name: 'feat:     ✨  新增功能 | A new feature', emoji: ':sparkles:' },
      { value: 'fix', name: 'fix:      🐛  修复缺陷 | A bug fix', emoji: ':bug:' },
      { value: 'docs', name: 'docs:     📝  文档更新 | Documentation only changes', emoji: ':memo:' },
      { value: 'style', name: 'style:    💄  代码格式 | Changes that do not affect the meaning of the code', emoji: ':lipstick:' },
      { value: 'refactor', name: 'refactor: ♻️   代码重构 | A code change that neither fixes a bug nor adds a feature', emoji: ':recycle:' },
      { value: 'perf', name: 'perf:     ⚡️  性能提升 | A code change that improves performance', emoji: ':zap:' },
      { value: 'test', name: 'test:     ✅  测试相关 | Adding missing tests or correcting existing tests', emoji: ':white_check_mark:' },
      { value: 'build', name: 'build:    📦️  构建相关 | Changes that affect the build system or external dependencies', emoji: ':package:' },
      { value: 'ci', name: 'ci:       🎡  持续集成 | Changes to our CI configuration files and scripts', emoji: ':ferris_wheel:' },
      { value: 'revert', name: 'revert:   ⏪️  回退代码 | Revert to a commit', emoji: ':rewind:' },
      { value: 'chore', name: 'chore:    🔨  其他修改 | Other changes that do not modify src or test files', emoji: ':hammer:' },
    ],

    useEmoji: false, // 是否启用 emoji（false 表示禁用）
    emojiAlign: 'center', // emoji 对齐方式
    themeColorCode: '', // 主题颜色代码，空表示默认
    scopes: [], // 预定义的提交范围列表
    allowCustomScopes: true, // 是否允许自定义提交范围
    allowEmptyScopes: true, // 是否允许范围为空
    customScopesAlign: 'bottom', // 自定义范围选项位置
    customScopesAlias: 'custom', // 自定义范围别名
    emptyScopesAlias: 'empty', // 空范围别名
    upperCaseSubject: false, // 是否强制首字母大写
    markBreakingChangeMode: true, // 是否开启破坏性变更标记模式
    allowBreakingChanges: ['feat', 'fix'], // 允许破坏性变更的提交类型
    breaklineNumber: 100, // 详细描述和破坏性变更换行长度阈值
    breaklineChar: '|', // 换行符，使用 | 表示换行

    skipQuestions: [], // 跳过的提问项

    issuePrefixs: [ // 关联 issue 的前缀选项，适合 gitee 等平台
      { value: 'link', name: 'link:     链接 ISSUES 进行中' },
      { value: 'closed', name: 'closed:   标记 ISSUES 已完成' },
    ],
    customIssuePrefixsAlign: 'top', // 自定义 issue 前缀显示位置
    emptyIssuePrefixsAlias: 'skip', // 空 issue 前缀别名
    customIssuePrefixsAlias: 'custom', // 自定义 issue 前缀别名
    allowCustomIssuePrefixs: true, // 是否允许自定义 issue 前缀
    allowEmptyIssuePrefixs: true, // 是否允许 issue 前缀为空

    confirmColorize: true, // 确认时是否彩色高亮

    maxHeaderLength: Number.POSITIVE_INFINITY, // 提交头最大长度（无限制）
    maxSubjectLength: Number.POSITIVE_INFINITY, // 提交主题最大长度（无限制）
    minSubjectLength: 0, // 提交主题最小长度

    scopeOverrides: undefined, // 可以为不同提交类型定义不同范围（此处未配置）

    defaultBody: '', // 默认详细描述
    defaultIssues: '', // 默认关联 issues
    defaultScope: '', // 默认范围
    defaultSubject: '', // 默认主题
  },
}
