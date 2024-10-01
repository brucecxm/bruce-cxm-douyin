<template>
    <div class="header">
        <div class="menu" @click="triggerParentEvent"></div>
        <div class="nav" ref="nav">
            <p v-for="(item, index) in navItems" :key="index" :class="{ active: activeIndex === index }"
                @click="changeIndex(index)" ref="navItem">
                {{ item }}
            </p>
            <div class="underline-container">
                <div class="underline" ref="underline" v-show="underlineVisible"></div>
            </div>
        </div>
        <div class="search"></div>
    </div>
</template>

<script>
export default {
    props: {
        activeIndex: {
            type: Number,
            default: 0
        },
        onChildEvent: {
            type: Function,
            required: true,
        },
    },

    data() {
        return {
            navItems: ["推荐", "同城", "关注", "直播", "经验", "商城", "团购", "精选", "热点"],
            underlineVisible: true, // 控制下划线的可见性
        };
    },
    methods: {
        triggerParentEvent() {
            // 调用父组件的方法
            this.onChildEvent('Hello from Child');
        },
        changeIndex(index) {
            this.$emit('update:index', index);
            this.updateUnderline(); // 更新下划线位置
        },
        updateUnderline() {
            const underline = this.$refs.underline;
            const activeItem = this.$refs.navItem[this.activeIndex]; // 获取当前高亮项
            if (activeItem) {
                const nav = this.$refs.nav;
                const itemWidth = activeItem.offsetWidth;
                const itemOffsetLeft = activeItem.offsetLeft;
                underline.style.left = `${itemOffsetLeft}px`;
                underline.style.width = `${itemWidth}px`;

                // 确保当前项在视口内，更新下划线的可见性
                const navScrollLeft = nav.scrollLeft;
                const navWidth = nav.clientWidth;

                const isVisible = (
                    itemOffsetLeft >= navScrollLeft &&
                    itemOffsetLeft + itemWidth <= navScrollLeft + navWidth
                );

                this.underlineVisible = isVisible; // 根据是否可见更新下划线
            }
        }
    },
    mounted() {
        this.updateUnderline();
        const nav = this.$refs.nav;
        let isDragging = false;
        let startX;
        let scrollLeft;

        nav.addEventListener('mousedown', (e) => {
            isDragging = true;
            startX = e.pageX - nav.offsetLeft;
            scrollLeft = nav.scrollLeft;
        });

        nav.addEventListener('mouseleave', () => {
            isDragging = false;
        });

        nav.addEventListener('mouseup', () => {
            isDragging = false;
        });

        nav.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            e.preventDefault();
            const x = e.pageX - nav.offsetLeft;
            const scroll = (x - startX) * 2;
            nav.scrollLeft = scrollLeft - scroll;
            this.updateUnderline(); // 在移动时更新下划线位置和可见性
        });
    },
    watch: {
        activeIndex(newIndex) {
            this.updateUnderline(); // 更新下划线位置
        }
    }
};
</script>

<style scoped>
.header {
    position: relative;
}

.nav {
    position: relative;
}

.underline-container {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 0.1rem;
    /* 与下划线高度一致 */
    background-color: inherit;
    /* 与导航栏背景色一致 */
    overflow: hidden;
    /* 让下划线在容器内被遮住 */
}

.underline {
    position: absolute;
    bottom: 0;
    height: 0.1rem;
    background-color: white;
    transition: left 0.3s ease, width 0.3s ease;
}

.header {
    width: 100%;
    height: 5vh;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}

.nav {
    width: 80vw;
    height: 100%;
    display: flex;
    flex-direction: row;
    overflow: hidden;
    align-items: center;
    overflow-x: auto;
    cursor: grab;
    color: white;
}

.nav::-webkit-scrollbar {
    display: none;
}

p {
    color: rgb(255, 255, 255, 0.9);
    font-size: 0.5rem;
    margin: 0.6rem;
    flex-shrink: 0;
    user-select: none;
}

.active {
    color: white;
    font-weight: bold;
}

.search {
    width: 1rem;
    height: 1rem;
    background-image: url(../assets/home/放大镜.png);
    background-size: 0.8rem 0.8rem;
    background-repeat: no-repeat;
    margin-right: 0.5rem;
    margin-top: 0.25rem;
}

.menu {
    width: 1rem;
    height: 1rem;
    background-image: url(../assets/home/菜单.png);
    background-size: 0.8rem 0.7rem;
    background-repeat: no-repeat;
    margin-left: 0.5rem;
    margin-top: 0.5rem;
}
</style>
