<template>
    <div class="allbox">
        <!-- <div class="test" @click="allboxtest"></div> -->
        <videobox-vue v-if="boxtype === '推荐'"></videobox-vue>
        <shopbox-vue v-if="boxtype === '商城'"></shopbox-vue>
        <samecity-vue v-if="boxtype === '同城'"></samecity-vue>
        <followbox-vue v-if="boxtype === '关注'"></followbox-vue>
        <livebox-vue v-if="boxtype === '直播'"></livebox-vue>

    </div>
</template>

<script>
import followboxVue from './followbox.vue';
import liveboxVue from './livebox.vue';
import samecityVue from './samecity.vue';
import shopboxVue from './shopbox.vue';
import videoboxVue from './videobox.vue';

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
        liveboxVue

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
