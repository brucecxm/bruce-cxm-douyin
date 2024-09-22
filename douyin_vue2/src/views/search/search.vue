<template>
    <div class="search-page">
        <header class="header">
            <i class="fas fa-chevron-left"></i>
            <input type="text" placeholder="人体捐献要求给钱" v-model="searchQuery" />
            <i class="fas fa-search" @click="gosearchdetail"></i>
        </header>
        <main>
            <div class="suggestions">
                <p>你是不是想搜:</p>
                <ul>
                    <li v-for="(suggestion, index) in suggestions" :key="index">{{ suggestion }}</li>
                </ul>
            </div>
            <nav class="tabs">
                <span class="active">抖音热榜</span>
                <span>音视频榜</span>
                <span>百科榜</span>
                <span>问答榜</span>
                <span>博物馆榜</span>
                <span>影视榜</span>
            </nav>
            <ul class="hot-list">
                <li v-for="(item, index) in hotList" :key="index">
                    <span>{{ index + 1 }}</span>
                    <p>{{ item.title }}</p>
                    <i v-if="item.new" class="fas fa-bolt"></i>
                    <span class="views">{{ item.views }}万</span>
                </li>
            </ul>
        </main>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            searchQuery: '',
            suggestions: [
                '安慰捐一次精补偿多少钱',
                '黑料不打烊黑',
                '磁吸共振滑率高吗',
                '底色梦纹',
                '单曲猫咪',
                '植物大战僵尸教学'
            ],
            hotList: [
                { title: '中共中央政治局第十五次集体学习', views: 1130.5, new: true },
                { title: '武汉暴雨江水上涨', views: 1109.6, new: true },
                { title: '美国罕见批评“茶党”伙伴印度', views: 1110.7, new: true },
                { title: '我国行星探测新时间表出炉', views: 1098.5, new: true },
                { title: '盘点欧美被VAR吹毁的场面', views: 1049.7 },
                { title: '外交部：对扣押飞禽世界表示不悦', views: 1014.5 },
                { title: '林更新这回回到圈外了', views: 1003.7 },
                { title: 'Steam夏促买什么', views: 942.7 },
                { title: '十个勒沃逃离疫区', views: 923.2 },
                { title: '高考本硕终点名单竟含禽鸟种类', views: 907.5 },
                { title: '失去大量机会错过金典书记', views: 897.3 },
                { title: '俄修订农业议题并解锁组书记', views: 913.7 },
                { title: '法国豪奢承接中苏组回归', views: 912.5 }
            ]
        };
    },
    methods: {
        gosearchdetail() {
            this.$router.push("/searchdetail")
        }
    }
};
</script>
  
<style scoped>
.search-page {
    width: 414px;
    height: 896px;
    margin: 0 auto;
    background-color: #f8f8f8;
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px;
    background-color: #fff;
    border-bottom: 1px solid #ddd;
}

.header i {
    font-size: 20px;
}

.header input {
    flex-grow: 1;
    margin: 0 10px;
    padding: 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.suggestions {
    padding: 10px;
    background-color: #fff;
    border-bottom: 1px solid #ddd;
}

.suggestions p {
    margin: 0;
    font-weight: bold;
}

.suggestions ul {
    padding: 0;
    list-style-type: none;
}

.suggestions li {
    padding: 5px 0;
}

.tabs {
    display: flex;
    justify-content: space-around;
    padding: 10px 0;
    background-color: #fff;
    border-bottom: 1px solid #ddd;
}

.tabs span {
    padding: 10px 20px;
    cursor: pointer;
}

.tabs .active {
    border-bottom: 2px solid #007bff;
}

.hot-list {
    padding: 10px;
    list-style-type: none;
    background-color: #fff;
}

.hot-list li {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid #ddd;
}

.hot-list li span {
    font-weight: bold;
}

.hot-list li p {
    flex-grow: 1;
    margin: 0 10px;
}

.hot-list li .fas {
    color: #ff0000;
}

.hot-list li .views {
    color: #999;
}
</style>
  