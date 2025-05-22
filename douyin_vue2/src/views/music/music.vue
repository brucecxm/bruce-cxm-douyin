<template>
  <div class="music">
    <InfiniteList
      :itemInfo="videobox"
      :itemsPerRow="3"
      boxWidth="33%"
      boxHeight="250px"
      :gap="3"
      :loading="loading"
      :noMoreData="noMoreData"
      @loadMore="fetchMoreData"
    >
      <template v-slot:header>
        <div class="header">
          <i class="fas fa-arrow-left" @click="goBack"></i>
        </div>

        <div class="musicbody">
          <div class="imgbox">
            <img :src="musicinfo.music_avatar" alt="" />
          </div>
          <div class="info">
            <div class="geming" style="font-size: 0.8rem">
              {{ musicinfo.music_name }} 创作的原声
            </div>
            <div class="zuozhe" style="font-size: 0.5rem">{{ name }}</div>

            <div class="actions">
              <button
                class="action-btn"
                title="去汽水音乐听完整版"
                @click="goToMusic"
              >
                <i class="fas fa-headphones"></i>
                听完整版
              </button>

              <button
                class="action-btn"
                title="收藏此音乐"
                @click="collectMusic"
              >
                <i class="fas fa-heart"></i>
                收藏
              </button>
            </div>
          </div>
        </div>
      </template>
      <template #item="{ item }">
        <div
          class="itemdetail"
          :style="{
            backgroundImage: `url(${item.video_img})`,
            width: '100%',
            height: '100%',
            backgroundSize: 'cover',
            backgroundPosition: 'center center'
          }"
        ></div>
      </template>
    </InfiniteList>
  </div>
</template>

<script>
import { getmumsic } from '@/api/video';
import InfiniteList from '../../components/InfiniteList.vue';

export default {
  components: {
    InfiniteList
  },
  data() {
    return {
      videobox: [], // 视频列表，初始为空
      loading: false,
      noMoreData: false,
      page: 0, // 当前页码
      pageSize: 10,
      musicinfo: {},
      musicid: '1',
      name: '歌曲作者名字'
    };
  },
  created() {
    this.musicid = this.$route.params.musicid || '1';
    this.fetchItemDetails(1); // 加载第一页数据
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    gohome() {
      this.$router.push('/');
    },
    govideodetail(videoid) {
      this.$router.push(`/videodetail?type=music&videoid=${videoid}`);
    },
    fetchItemDetails(page) {
      if (this.loading || this.noMoreData) return;
      this.loading = true;
      getmumsic(this.musicid, page, this.pageSize)
        .then((res) => {
          const videos = res.data.videoimg || [];
          const total = res.data.total || 0;

          // 追加视频数据
          this.videobox = [...this.videobox, ...videos];

          // 只在第一页赋值musicinfo，避免重复赋值
          if (page === 1) {
            this.musicinfo = res.data.music_info || {};
          }

          // 判断是否加载完毕
          if (this.videobox.length >= total) {
            this.noMoreData = true;
          }

          this.page = page; // 更新当前页码
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
          this.$toast && this.$toast('获取视频出错');
        });
    },
    fetchMoreData() {
      if (this.loading || this.noMoreData) return;
      this.fetchItemDetails(this.page + 1);
    },
    goToMusic() {
      if (this.musicinfo.music_link) {
        window.open(this.musicinfo.music_link, '_blank');
      } else {
        this.$toast && this.$toast('该音乐暂无外链');
      }
    },
    collectMusic() {
      this.$toast && this.$toast('收藏成功！');
      // 这里可以改成调用收藏接口
    }
  }
};
</script>

<style scoped>
.music {
  background-color: #fff;
  font-family: 'Helvetica Neue', sans-serif;
  color: #333;
  min-height: 100vh;
}

.header {
  padding: 10px;
  height: 50px;
  display: flex;
  align-items: center;
  font-size: 1.2rem;
  border-bottom: 1px solid #eee;
}

.header i {
  font-size: 1.5rem;
  cursor: pointer;
  color: #333;
  transition: color 0.3s;
}

.header i:hover {
  color: #ff4d4f;
}

.musicbody {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 20px 10px;
  position: relative;
  gap: 15px;
  background-color: #fafafa;
  border-bottom: 1px solid #eee;
}

.imgbox {
  width: 110px;
  height: 110px;
  border-radius: 20px;
  overflow: hidden;
  flex-shrink: 0;
}

.imgbox img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 110px;
  position: relative;
}

.geming {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 5px;
}

.zuozhe {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 10px;
}

.actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  height: 40px;
  background-color: #ff4d4f;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 0.8rem;
  cursor: pointer;
  transition:
    background-color 0.3s,
    transform 0.3s;
}

.action-btn:hover {
  background-color: #e84141;
  transform: scale(1.05);
}

.action-btn i {
  font-size: 1rem;
}
</style>
