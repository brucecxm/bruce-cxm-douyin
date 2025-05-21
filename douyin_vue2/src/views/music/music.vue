<template>
  <div class="music">
    <InfiniteList
      :itemInfo="userInfo"
      :itemsPerRow="3"
      boxWidth="33%"
      boxHeight="250px"
      :gap="3"
    >
      <template v-slot:header>
        <div class="header">
          <i class="fas fa-arrow-left" @click="goBack"></i>
          <!-- <label>转发</label> -->
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

        <div class="wanfa" v-if="false">
          <!-- 玩法说明区域（预留） -->
        </div>
      </template>
    </InfiniteList>
  </div>
</template>

<script>
import { getmumsic } from '@/api/video';
import InfiniteList from '../../components/InfiniteList.vue';

export default {
  created() {
    this.musicid = this.$route.params.musicid;
    console.log(this.musicid);
    this.fetchItemDetails(this.musicid);
  },
  components: {
    InfiniteList
  },
  data() {
    return {
      musicinfo: {},
      musicid: '1',
      is_wanfa: false,
      name: '歌曲作者名字',
      videobox: [
        {
          imgurl:
            'https://gips2.baidu.com/it/u=1651586290,17201034&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f600_800',
          linkurl: ''
        },
        {
          imgurl:
            'https://gips2.baidu.com/it/u=1651586290,17201034&fm=3028&app=3028&f=JPEG&fmt=auto&q=100&size=f600_800',
          linkurl: ''
        }
      ]
    };
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
    fetchItemDetails(id) {
      getmumsic(id)
        .then((res) => {
          this.videobox = res.data.videoimg || [];
          this.musicinfo = res.data.music_info || {};
        })
        .catch((error) => {
          console.error('获取视频出错:', error);
        });
    },

    // 点击“去使用”跳转到音乐外链
    goToMusic() {
      if (this.musicinfo.music_link) {
        window.open(this.musicinfo.music_link, '_blank');
      } else {
        this.$toast && this.$toast('该音乐暂无外链');
      }
    },

    // 点击收藏按钮
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

/* 新增按钮样式 */
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

.videobody {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  padding: 10px;
  background-color: #fff;
}

.box {
  width: 31vw;
  height: 160px;
  margin-bottom: 10px;
  border-radius: 10px;
  overflow: hidden;
  background-color: #f0f0f0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
}

.box:hover {
  transform: scale(1.03);
}

.box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.wanfa {
  height: 100px;
  background-color: rgba(0, 0, 0, 0.05);
  margin: 10px;
  border-radius: 10px;
}
</style>
