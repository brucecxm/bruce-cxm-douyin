<template>
  <div style="width: 100%;">
    <GridDisplay
      :itemWidth="80"
      :itemHeight="100"
      :items="items"
      :gap="5"
    />
  </div>
</template>

<script>
import GridDisplay from '@/components/GridDisplay.vue'

export default {
  components: { GridDisplay },
  data() {
    return {
      items: [
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616408.png', name: '图标1' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616429.png', name: '图标2' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616490.png', name: '图标3' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616554.png', name: '图标4' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616494.png', name: '图标5' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616409.png', name: '图标6' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616426.png', name: '图标7' },
        { icon: 'https://cdn-icons-png.flaticon.com/512/616/616453.png', name: '图标8' },
      ]
    }
  }
}
</script>
