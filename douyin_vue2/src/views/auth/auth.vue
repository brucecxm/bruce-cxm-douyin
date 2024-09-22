<template>
    <div class="auth">
        <div class="backimg">
            <img src="../../assets/mebc.jpg" alt=" no backimg">
            <div class="caidan"></div>
            <div class="fanhui" @click="gohome">fanhui</div>
            <div class="touxiang"></div>

        </div>
        <div class="personinfo">

        </div>
        <div class="uservideo">
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
            <div class="vbox"> </div>
        </div>
    </div>
</template>
<script>


export default {
    methods: {
        gohome() {
            // 这里可以是跳转回主页的逻辑
            this.$router.push('/');
        }
    }
};
</script>
<style scoped>
.vbox {
    width: 100px;
    height: 150px;
    background-color: rgb(217, 25, 25);
    margin: 10px;
    float: left;
    transform: translateX(30px);
}

.info {
    width: 414px;
    height: 250px;
    background-color: rgb(47, 20, 180);
    position: absolute;
    top: 150px;
}


.touxiang {
    width: 150px;
    height: 150px;
    border-radius: 75px;
    background-color: rgb(217, 25, 25);
    position: absolute;
    bottom: 10px;
    left: 10px;
}

.uservideo {
    width: 390px;
    overflow: hidden;
}





.personinfo {
    width: 414px;
    height: 200px;
    background-color: rgba(222, 56, 128, 0.5);
    position: relative;
}

.backimg {
    width: 414px;
    height: 300px;
    background-color: rgba(255, 255, 255, 0.5);
    position: relative;

}

.backimg img {
    width: 414px;
    height: 300px;
}

.backimg .caidan {
    width: 150px;
    height: 50px;
    background-color: rgba(222, 56, 128, 0.5);
    position: absolute;
    right: 20px;
    top: 50px;
}

.fanhui {
    width: 50px;
    height: 50px;
    background-color: rgba(222, 56, 128, 0.5);
    position: absolute;
    left: 20px;
    top: 50px;
}

.auth {
    width: 414px;
    height: 830px
}
</style>