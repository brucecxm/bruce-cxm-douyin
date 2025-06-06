<template>
  <div>
    <input v-model="fromUser" placeholder="发起人ID" />
    <input v-model="toUser" placeholder="接收人ID" />
    <input v-model="amount" placeholder="金额" />
    <button @click="submit">转账</button>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios'; // 直接引入 axios

export default {
  data() {
    return {
      fromUser: '',
      toUser: '',
      amount: '',
      stomp: null
    };
  },
  mounted() {
    const socket = new SockJS('http://localhost:7426/ws');
    this.stomp = Stomp.over(socket);
    this.stomp.connect({}, () => {
      this.stomp.subscribe('/topic/transfer/' + this.fromUser, (msg) => {
        alert('WebSocket: ' + msg.body);
      });
    });
  },
  methods: {
    submit() {
      axios
        .post('http://localhost:7426/api/transfer', null, {
          // 直接使用 axios
          params: {
            fromUserId: this.fromUser,
            toUserId: this.toUser,
            amount: this.amount
          }
        })
        .then((res) => {
          alert('成功: ' + res.data);
        })
        .catch((err) => {
          alert('失败: ' + err.response.data);
        });
    }
  }
};
</script>
