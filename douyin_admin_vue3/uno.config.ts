import type { PresetOrFactory } from '@unocss/core' // UnoCSS 预设类型定义
import type { Theme } from 'unocss/preset-uno' // UnoCSS 主题类型定义
import { entriesToCss, toArray } from '@unocss/core' // UnoCSS 核心工具函数
import presetLegacyCompat from '@unocss/preset-legacy-compat' // 兼容旧版 CSS 预设
import {
  defineConfig,
  presetAttributify, // 支持在标签上使用属性写法（例如：bg="red-500"）
  presetIcons, // 图标预设，支持通过类名引用图标
  presetTypography, // 典型排版样式预设，提升文本表现力
  presetUno, // UnoCSS 默认核心预设，包含常用实用类
  transformerCompileClass, // 转换器，编译类名到最终 CSS
  transformerDirectives, // 转换器，支持 UnoCSS 指令（@apply、@variants）
  transformerVariantGroup, // 转换器，支持分组变体语法（如 hover:(bg-red text-white)）
} from 'unocss'
import { presetAnimations } from 'unocss-preset-animations' // 动画预设，支持丰富动画类
import { presetScrollbar } from 'unocss-preset-scrollbar' // 滚动条样式预设
import { darkTheme, lightTheme } from './themes' // 自定义的暗黑和明亮主题变量

export default defineConfig<Theme>({ // 定义 UnoCSS 配置，指定主题类型

  // 指定扫描内容范围，UnoCSS 会扫描匹配的文件来生成对应的 CSS
  content: {
    pipeline: {
      include: [
        /\.(vue|svelte|[jt]sx|mdx?|astro|elm|php|phtml|html)($|\?)/, // 各类模板和脚本文件
        'src/**/*.{js,ts}', // js/ts 源代码目录
      ],
    },
  },

  // 自定义快捷方式，动态生成一系列 flex 布局相关的类名
  shortcuts: [
    [
      /^flex-?(col)?-(start|end|center|baseline|stretch)-?(start|end|center|between|around|evenly|left|right)?$/,
      ([, col, items, justify]) => {
        const cls = ['flex']
        if (col === 'col') {
          cls.push('flex-col') // flex 方向为列
        }
        if (items === 'center' && !justify) {
          cls.push('items-center') // 项目交叉轴居中
          cls.push('justify-center') // 主轴居中
        }
        else {
          cls.push(`items-${items}`) // 交叉轴对齐方式
          if (justify) {
            cls.push(`justify-${justify}`) // 主轴对齐方式
          }
        }
        return cls.join(' ') // 返回组合后的类名字符串
      },
    ],
  ],

  // 预置全局样式（类似 reset 和主题样式）
  preflights: [
    {
      getCSS: () => {
        const returnCss: any = []

        // 明亮主题样式转换，将 lightTheme 对象转成 CSS
        const lightCss = entriesToCss(Object.entries(lightTheme))
        // 明亮主题应用范围，所有元素及伪元素、backdrop
        const lightRoots = toArray([`*,::before,::after`, `::backdrop`])
        // 生成明亮主题的 CSS 并加入结果
        returnCss.push(lightRoots.map(root => `${root}{${lightCss}}`).join(''))

        // 暗黑主题样式转换，将 darkTheme 对象转成 CSS
        const darkCss = entriesToCss(Object.entries(darkTheme))
        // 暗黑主题应用范围，带有 html.dark 类的元素及其子元素和伪元素
        const darkRoots = toArray([
          `html.dark,html.dark *,html.dark ::before,html.dark ::after`,
          `html.dark ::backdrop`,
        ])
        // 生成暗黑主题的 CSS 并加入结果
        returnCss.push(darkRoots.map(root => `${root}{${darkCss}}`).join(''))

        // 返回合并的主题 CSS
        return returnCss.join('')
      },
    },
  ],

  // 自定义主题配置（覆盖或扩展默认主题）
  theme: {
    colors: {
      border: 'hsl(var(--border))', // 边框颜色，使用 CSS 变量
      input: 'hsl(var(--input))', // 输入框颜色
      ring: 'hsl(var(--ring))', // focus 环绕色
      background: 'hsl(var(--background))', // 背景色
      foreground: 'hsl(var(--foreground))', // 前景色（文本色）
      primary: { // 主色调
        DEFAULT: 'hsl(var(--primary))',
        foreground: 'hsl(var(--primary-foreground))',
      },
      secondary: { // 次要色
        DEFAULT: 'hsl(var(--secondary))',
        foreground: 'hsl(var(--secondary-foreground))',
      },
      destructive: { // 危险色（删除、错误等）
        DEFAULT: 'hsl(var(--destructive))',
        foreground: 'hsl(var(--destructive-foreground))',
      },
      muted: { // 静默色（弱化文字）
        DEFAULT: 'hsl(var(--muted))',
        foreground: 'hsl(var(--muted-foreground))',
      },
      accent: { // 强调色
        DEFAULT: 'hsl(var(--accent))',
        foreground: 'hsl(var(--accent-foreground))',
      },
      popover: { // 弹出层背景色
        DEFAULT: 'hsl(var(--popover))',
        foreground: 'hsl(var(--popover-foreground))',
      },
      card: { // 卡片背景色
        DEFAULT: 'hsl(var(--card))',
        foreground: 'hsl(var(--card-foreground))',
      },
    },
    borderRadius: {
      xl: 'calc(var(--radius) + 4px)', // 超大圆角，基于 CSS 变量 radius 调整
      lg: 'var(--radius)', // 大圆角
      md: 'calc(var(--radius) - 2px)', // 中圆角
      sm: 'calc(var(--radius) - 4px)', // 小圆角
    },
  },

  // 使用的 UnoCSS 预设集合，提供基础功能和扩展支持
  presets: [
    presetUno(), // 默认核心预设，提供基础实用类
    presetAnimations() as PresetOrFactory<Theme>, // 动画预设
    presetAttributify(), // 支持属性风格的类名写法
    presetIcons({ // 图标预设，支持内联 svg 图标
      extraProperties: { // 图标元素默认样式
        'display': 'inline-block',
        'vertical-align': 'middle',
      },
    }),
    presetTypography(), // 典型文本排版样式
    presetScrollbar(), // 滚动条样式美化预设
    presetLegacyCompat({ // 兼容旧版颜色空间，避免兼容性问题
      legacyColorSpace: true,
    }) as PresetOrFactory<Theme>,
  ],

  // 转换器列表，用于对样式指令和类名进行处理转换
  transformers: [
    transformerDirectives(), // 支持 @apply、@variants 等指令
    transformerVariantGroup(), // 支持分组变体语法，如 hover:(bg-red text-white)
    transformerCompileClass(), // 编译类名到最终的 CSS
  ],

  // 依赖配置文件，发生改动时会重新编译
  configDeps: [
    'themes/index.ts',
  ],
})
