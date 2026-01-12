<template>
  <div class="search-detail">
    <div class="search-bar">
      <input
        type="text"
        placeholder="搜索"
        v-model="searchQuery"
        @input="onSearch"
      />
      <button>搜索</button>
    </div>
    <div class="tab-bar">
      <span
        :class="{ active: selectedTab === '综合' }"
        @click="selectTab('综合')"
        >综合</span
      >
      <span
        :class="{ active: selectedTab === '视频' }"
        @click="selectTab('视频')"
        >视频</span
      >
      <span
        :class="{ active: selectedTab === '用户' }"
        @click="selectTab('用户')"
        >用户</span
      >
      <span
        :class="{ active: selectedTab === '经验' }"
        @click="selectTab('经验')"
        >经验</span
      >
      <span
        :class="{ active: selectedTab === 'AI搜' }"
        @click="selectTab('AI搜')"
        >AI搜</span
      >
    </div>
    <transition name="slide">
      <div class="posts" v-if="filteredPosts.length">
        <div class="post" v-for="post in filteredPosts" :key="post.id">
          <div class="post-header">
            <img :src="post.avatar" alt="avatar" class="avatar" />
            <div class="user-info">
              <span class="username">{{ post.username }}</span>
              <span class="date">{{ post.date }}</span>
            </div>
          </div>
          <div class="post-content">
            <img :src="post.image" alt="post image" class="post-image" />
            <p>{{ post.description }}</p>
          </div>
          <div class="post-footer">
            <span>{{ post.views }} 次观看</span>
            <span>{{ post.likes }} 点赞</span>
            <span>{{ post.comments }} 评论</span>
            <span>{{ post.shares }} 分享</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '',
      selectedTab: '综合',
      postsData: {
        综合: [
          {
            id: 1,
            username: '关注入海无名之辈',
            date: '2019.08.25',
            avatar:
              '//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_859859bd152f82fb7889b9d91ff3d56f.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/81392394beb5481faecd2236cfdce182~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=DFL5to8UnmMI55fN2bhyMFqAqoc%3D',
            description:
              '电视剧《搜索》: 叶蓝秋死了，并不是死于疾病，而是死于网络暴力',
            views: '5.1万',
            likes: 139,
            comments: 13,
            shares: 36
          }
        ],
        视频: [
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          }
          // 其他视频标签下的帖子...
        ],
        用户: [
          // 视频标签对应的帖子
          {
            id: 1,
            username: '关注入海无名之辈',
            date: '2019.08.25',
            avatar:
              '//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_859859bd152f82fb7889b9d91ff3d56f.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/81392394beb5481faecd2236cfdce182~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=DFL5to8UnmMI55fN2bhyMFqAqoc%3D',
            description:
              '电视剧《搜索》: 叶蓝秋死了，并不是死于疾病，而是死于网络暴力',
            views: '5.1万',
            likes: 139,
            comments: 13,
            shares: 36
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          }
          // 其他视频标签下的帖子...
        ],
        经验: [
          // 视频标签对应的帖子
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          }
          // 其他视频标签下的帖子...
        ],
        AI搜: [
          {
            id: 1,
            username: '关注入海无名之辈',
            date: '2019.08.25',
            avatar:
              '//p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_859859bd152f82fb7889b9d91ff3d56f.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/81392394beb5481faecd2236cfdce182~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=DFL5to8UnmMI55fN2bhyMFqAqoc%3D',
            description:
              '电视剧《搜索》: 叶蓝秋死了，并不是死于疾病，而是死于网络暴力',
            views: '5.1万',
            likes: 139,
            comments: 13,
            shares: 36
          },
          // 视频标签对应的帖子
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          },
          {
            id: 2,
            username: '热榜',
            date: '2024.07.04',
            avatar:
              '//p3-pc.douyinpic.com/img/aweme-avatar/tos-cn-i-c9aec8xkvj_cdb6e753b6fa4c3dbf80c22d84ac7964~c5_300x300.jpeg?from=2956013662',
            image:
              '//p3-pc-sign.douyinpic.com/tos-cn-i-pk90l89vgd/d4836254e2764565a359883e2a847d16~tplv-dy-cropcenter:323:430.jpeg?biz_tag=pcweb_cover&from=327834062&s=PackSourceEnum_PUBLISH&sc=cover&se=true&sh=323_430&x-expires=2038143600&x-signature=ma2EwvmFioXogcAHIHar154AQCY%3D',
            description: '周鸿祎谈OpenAI进军搜索',
            views: '4.7万',
            likes: 85,
            comments: 8,
            shares: 24
          }
          // 其他视频标签下的帖子...
        ]
      }
    };
  },
  computed: {
    filteredPosts() {
      const posts = this.postsData[this.selectedTab] || [];
      return posts.filter((post) =>
        post.description.includes(this.searchQuery)
      );
    }
  },
  methods: {
    onSearch() {
      // Handle search input
    },
    selectTab(tab) {
      this.selectedTab = tab;
    }
  }
};
</script>

<style scoped>
.search-detail {
  padding: 16px;
}

.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

.search-bar input {
  width: 80%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.tab-bar {
  display: flex;
  justify-content: space-around;
  margin-bottom: 16px;
}

.tab-bar span {
  padding: 8px;
  font-size: 0.5rem;
  cursor: pointer;
}

.tab-bar .active {
  font-weight: bold;
  border-bottom: 2px solid #000;
}

.posts {
  display: flex;
  flex-direction: column;
  gap: 16px;
  font-size: 0.6rem;
}

.post {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 16px;
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: bold;
}

.date {
  font-size: 12px;
  color: #888;
}

.post-content {
  margin-bottom: 8px;
}

.post-image {
  width: 100%;
  border-radius: 4px;
  margin-bottom: 8px;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #888;
}

/* 添加左右移动的过渡效果 */
.slide-enter-active,
.slide-leave-active {
  transition:
    transform 0.5s ease,
    opacity 0.5s ease;
}

.slide-enter,
.slide-leave-to

/* .slide-leave-active 在 Vue 2 中 */ {
  transform: translateX(100%);
  opacity: 0;
}

.slide-leave {
  transform: translateX(-100%);
  opacity: 0;
}
</style>
