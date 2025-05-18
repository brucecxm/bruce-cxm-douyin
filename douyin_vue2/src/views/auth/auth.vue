<template>
    <div class="auth">
      <InfiniteList
        :itemInfo="videobox"
        :itemsPerRow="3"
        boxWidth="33%"
        boxHeight="250px"
        :gap="3"
      >
        <template v-slot:header>
            <div class="backimg" :style="{ backgroundImage: 'url(' + auth.backimg + ')' }">
         <!-- 左侧返回按钮 -->
<div class="fanhui" @click="onBackClick" title="返回">
  <svg viewBox="0 0 24 24" class="icon">
    <path d="M15 18l-6-6 6-6" stroke="white" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round" />
  </svg>
</div>

<!-- 右侧操作栏 -->
<div class="right-buttons">
  <!-- 放大镜图标 -->
  <div class="icon-button" @click="onSearchClick" title="搜索">
    <svg viewBox="0 0 24 24" class="icon">
      <circle cx="11" cy="11" r="7" stroke="white" stroke-width="2" fill="none" />
      <line x1="16.5" y1="16.5" x2="21" y2="21" stroke="white" stroke-width="2" />
    </svg>
  </div>

  <!-- 更多（三个点）图标 -->
  <div class="icon-button" @click="onMoreClick" title="更多">
    <svg viewBox="0 0 24 24" class="icon">
      <circle cx="5" cy="12" r="2" fill="white" />
      <circle cx="12" cy="12" r="2" fill="white" />
      <circle cx="19" cy="12" r="2" fill="white" />
    </svg>
  </div>
</div>

        
            <div class="touxiang">
              <img :src="auth.touimg" alt="头像" />
            </div>
          </div>
          <div class="personinfo">
            <div class="authnum">
              <span>{{ auth.like }} 获赞</span>
              <span>{{ auth.follow }} 关注</span>
              <span>{{ auth.fan }} 粉丝</span>
            </div>
            <div class="authinfo">信息介绍</div>
            <div class="city">
              <span>IP:{{ auth.city }}</span>
            </div>
            <div class="followbutt">+ 关注</div>
          </div>
          <under-line-tags-vue :navItems="parentMessage"></under-line-tags-vue>
        </template>
      </InfiniteList>
    </div>
  </template>
  
  <script>
  import { getauthzzz } from "@/api/video";
  import InfiniteList from '../../components/InfiniteList.vue';
  import underLineTagsVue from '../../components/underLineTags.vue';
  
  export default {
    created() {
      this.userId = this.$route.params.userId;
      console.log(this.userId);
      this.fetchItemDetails(this.userId);
    },
    data() {
      return {
        parentMessage:['视频', '评论', '点赞','确定'],
        videobox:"" ,
        userId: "",
        videonum: "10000", // 修正拼写为 videonum
        auth: {
          like: "1",
          follow: "1",
          fan: "1",
          city: "陕西",
          touimg: "http://gips0.baidu.com/it/u=3602773692,1512483864&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280",
          backimg: "http://gips0.baidu.com/it/u=3602773692,1512483864&fm=3028&app=3028&f=JPEG&fmt=auto?w=960&h=1280"
        },
        
      };
    },
    components: {
      InfiniteList,
      underLineTagsVue,

    },
    methods: {
      govideodetail(videoid) {
        this.$router.push(`/videodetail?type=auth&videoid=${videoid}`);
      },
      gohome() {
        this.$router.push('/');
      },
      onBackClick () {
        this.$router.back();
      },
      
      onSearchClick () {
        this.$router.push('/search');
      },
      
      onMoreClick() {
        // 处理更多操作
        console.log('更多操作');
      },
      fetchItemDetails(id) {
        if (id) {
          getauthzzz(id)
            .then(result => {
              console.log(result);
              this.auth = result.data.auth || this.auth;
              this.videobox = result.data.videobox || this.videobox;
              this.videonum = result.data.videonum || this.videonum;
            })
            .catch(error => {
              console.error('获取认证信息出错:', error);
            });
        } else {
          console.error('用户 ID 无效');
        }
      }
    }
  };
  </script>
  
  <style scoped>
  html,
  body {
    height: 100%;
    margin: 0;
    padding: 0;
  }
  
  .auth {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    background-color: #f8f8f8;
  }
  
  .backimg {
    width: 100%;
    height: 30vh;
    position: relative;
    z-index: 1;
    background-size: cover;
    background-position: center;
  }
  

  
  .fanhui {
  position: absolute;
  left: 5vw;
  top: 5vh;
  width: 40px;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.right-buttons {
  position: absolute;
  right: 5vw;
  top: 5vh;
  display: flex;
  gap: 10px;
}

.icon-button {
  width: 40px;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.icon {
  width: 24px;
  height: 24px;
}

  
  .touxiang {
    width: 25vw;
    height: 25vw;
    border-radius: 50%;
    overflow: hidden;
    position: absolute;
    bottom: 7vh;
    left: 10px;
    border: 3px solid white;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }
  
  .touxiang img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .personinfo {
    width: 100%;
    height: 30%;
    position: relative;
    background-color: white;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    z-index: 2;
    margin-top: -5vh;
    padding: 15px;
    box-sizing: border-box;
  }
  
  .authnum {
    font-size: 0.6rem;
    display: flex;
    margin-bottom: 10px;
  }
  
  .authnum span {
    font-weight: bold;
    color: #333;
  }
  
  .city {
    font-size: 0.4rem;
    background-color: rgba(0, 0, 0, 0.1);
    width: 10vw;
    padding: 5px;
    border-radius: 2px;
    text-align: center;
    margin-top: 10px;
  }
  
  .authinfo {
    font-size: 0.7rem;
    color: #666;
    margin-top: 15px;
  }
  
  .followbutt {
    width: 90%;
    height: 5vh;
    background-color: #fe1e1e;
    margin: 15px auto;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 30px;
    color: white;
    font-size: 0.8rem;
    cursor: pointer;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  }
  
  .videonum {
    font-size: 0.5rem;
    margin-top: 15px;
    color: #333;
    margin-bottom: 0;
    position: absolute;
    bottom: 0;
  }
  
  /* 你可以根据需要补充 InfiniteList 内部元素样式 */
  </style>
  