<template>
    <div class="allbox">
        <!-- <div class="test" @click="allboxtest"></div> -->
        <videobox-vue v-if="boxtype === '推荐'"></videobox-vue>
        <shopbox-vue v-if="boxtype === '商城'"></shopbox-vue>
        <samecity-vue v-if="boxtype === '同城'"></samecity-vue>
        <followbox-vue v-if="boxtype === '关注'"></followbox-vue>
        <livebox-vue v-if="boxtype === '直播'"></livebox-vue>
        <experiencebox v-if="boxtype === '经验'"></experiencebox>
        <experiencebox v-if="boxtype === '团购'"></experiencebox>
        <experiencebox v-if="boxtype === '精选'"></experiencebox>
        <experiencebox v-if="boxtype === '热点'"></experiencebox>
<!-- 其实可以通过传入一个type  yongv-if等控制组件的显示和消失  避免创建这么多组件 -->
<!-- 其实可以通过传入一个type  yongv-if等控制组件的显示和消失  避免创建这么多组件 -->
<!-- 其实可以通过传入一个type  yongv-if等控制组件的显示和消失  避免创建这么多组件 -->
    </div>
</template>

<script>
import followboxVue from './followbox.vue';
import liveboxVue from './livebox.vue';
import samecityVue from './samecity.vue';
import shopboxVue from './shopbox.vue';
import videoboxVue from './videobox.vue';
import experiencebox from './experiencebox.vue';

export default {
    props: {
        boxtype: {
            type: String, // 数据类型
            required: true, // 是否必填
        }
    }
    ,
    data() {
        return {

        };
    },
    components: {
        videoboxVue,
        shopboxVue,
        samecityVue,
        followboxVue,
        liveboxVue,
        shopboxVue,
        experiencebox

    },
    methods: {
        allboxtest() {
            console.log("sssssssss")
        }
    }
};
</script>

<style scoped>
.test {
    width: 200px;
    height: 200px;
    position: absolute;
    left: 0px;
    background-color: red;
    z-index: 10;
}
</style>
