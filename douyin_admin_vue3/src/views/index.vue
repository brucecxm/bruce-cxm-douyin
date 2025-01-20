<route lang="yaml">
name: home
meta:
  title: 主页
  icon: ant-design:home-twotone
</route>

<script setup lang="ts">
const versionType = ref('basic')
watch(versionType, (val) => {
  if (val === 'pro') {
    location.href = `${location.origin}${location.pathname}`.replace('basic-example', 'pro-example')
  }
})

const fantasticStartkitInfo = ref({
  feature: [
    '支持 TypeScript',
    '默认集成 vue-router 和 pinia',
    '支持基于文件系统的路由',
    '全局组件自动引入',
    '全局 SCSS 资源引入',
    '支持 Unocss',
    '支持 SVG 文件图标、Iconify 图标、UnoCSS 图标',
    '支持 mock 数据，可脱离后端束缚独立开发',
    '支持 gzip / brotli 优化项目体积，提高加载速度',
    '结合 IDE 插件、ESlint 、stylelint 、Git 钩子，轻松实现团队代码规范',
  ],
})

const fantasticAdminInfo = ref({
  imageVisible: false,
  index: 0,
  data: [
    'https://fantastic-admin.hurui.me/preview1.png',
    'https://fantastic-admin.hurui.me/preview2.png',
    'https://fantastic-admin.hurui.me/preview3.png',
    'https://fantastic-admin.hurui.me/preview4.png',
    'https://fantastic-admin.hurui.me/preview5.png',
    'https://fantastic-admin.hurui.me/preview6.png',
  ],
})

const oneStepAdminInfo = ref({
  imageVisible: false,
  index: 0,
  data: [
    'https://one-step-admin.hurui.me/preview1.png',
    'https://one-step-admin.hurui.me/preview2.png',
    'https://one-step-admin.hurui.me/preview3.png',
    'https://one-step-admin.hurui.me/preview4.png',
    'https://one-step-admin.hurui.me/preview5.png',
    'https://one-step-admin.hurui.me/preview6.png',
  ],
})

function open(url: string) {
  window.open(url, '_blank')
}
</script>

<template>
  <div>
    <FaPageHeader>
      <template #title>
        <div class="flex items-center gap-4">
          欢迎使用抖音管理平台
        </div>
      </template>
    </FaPageHeader>
  </div>
</template>

<style scoped>
.text-emphasis {
  text-emphasis-style: "❤";
}

.ecology {
  --uno: flex-1 m-0;

  :deep(.title-container) {
    --uno: flex items-center justify-between flex-wrap gap-4;

    .title-info {
      --uno: flex items-center gap-4;

      img {
        --uno: block w-12 h-12;
      }

      h1 {
        --uno: m-0 text-2xl;
      }

      h2 {
        --uno: m-0 text-base text-secondary-foreground/50 font-normal;
      }
    }
  }
}
</style>
