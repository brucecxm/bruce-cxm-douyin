<script setup lang="ts">
import { cn } from '@/utils' // 引入自定义的 cn 函数（可能是一个用于组合类名的工具函数）
import {
  ScrollAreaCorner, // 引入 ScrollAreaCorner 组件（用于展示滚动区域的角落部分）
  ScrollAreaRoot, // 引入 ScrollAreaRoot 组件（滚动区域的根组件）
  type ScrollAreaRootProps, // 引入 ScrollAreaRootProps 类型（用于定义 ScrollAreaRoot 组件的属性）
  ScrollAreaViewport, // 引入 ScrollAreaViewport 组件（滚动区域的视口部分）
} from 'radix-vue' // 从 radix-vue 库中引入上述组件

import { computed, type HTMLAttributes } from 'vue' // 引入 Vue 的 computed 和 HTMLAttributes 类型
import ScrollBar from './ScrollBar.vue' // 引入自定义的 ScrollBar 组件（自定义滚动条）

// 定义组件的 props 参数
const props = defineProps<ScrollAreaRootProps & {
  class?: HTMLAttributes['class'] // 支持传递 class 属性，用于自定义样式
  scrollbar?: boolean // 可选的属性，用来控制是否显示滚动条
  onWheel?: (event: WheelEvent) => void // 可选的 onWheel 事件，用于处理滚轮事件
}>()

// 使用 computed 计算属性，将 props 中的某些属性从 props 中去除，留下需要的部分
const delegatedProps = computed(() => {
  const { class: _, onWheel, ...delegated } = props // 解构 props，去除 class 和 onWheel 属性，保留其他属性

  return delegated // 返回剩余的属性
})

// 使用 useTemplateRef 获取模板中的 ref 引用
const viewportRef = useTemplateRef('viewportRef') // 获取 scroll area 视口的引用

// 使用 defineExpose 将引用暴露给父组件
defineExpose({
  el: viewportRef, // 暴露 viewportRef 给外部访问
})
</script>

<template>
  <!-- ScrollAreaRoot 是滚动区域的根组件，绑定了 props 和类名 -->
  <ScrollAreaRoot v-bind="delegatedProps" :class="cn('relative overflow-hidden', props.class)">
    <!-- ScrollAreaViewport 是滚动区域的视口部分，绑定了 vue 的 ref 和处理滚轮事件 -->
    <ScrollAreaViewport ref="viewportRef" class="scroll-area-viewport h-full w-full rounded-[inherit]" @wheel.prevent="onWheel">
      <!-- 插槽，允许外部插入内容 -->
      <slot />
    </ScrollAreaViewport>

    <!-- 自定义 ScrollBar 组件，控制滚动条的显示/隐藏 -->
    <ScrollBar :class="{ 'opacity-0 pointer-events-none': !props.scrollbar }" />

    <!-- ScrollAreaCorner 是滚动区域的角落部分，通常用于显示角落的装饰或功能 -->
    <ScrollAreaCorner />
  </ScrollAreaRoot>
</template>

<style scoped>
/* 使用 :deep 选择器来修改子组件内部的样式（此处是 ScrollAreaViewport 内部的 div 元素） */
:deep(.scroll-area-viewport > div) {
  height: 100%;  /* 设置 ScrollAreaViewport 内部的 div 高度为 100% */
}
</style>
