const state = () => ({
  messages: [],
  unreadCount: 0,
  socket: null,
  socketStatus: 'closed', // closed, connecting, open
  reconnectTimer: null,
  reconnectCount: 0,
  maxReconnectCount: 5
});

const getters = {
  getMessages: (state) => state.messages,
  getUnreadCount: (state) => state.unreadCount,
  getSocketStatus: (state) => state.socketStatus
};

const mutations = {
  SET_SOCKET_STATUS(state, status) {
    state.socketStatus = status;
  },
  SET_SOCKET(state, socket) {
    state.socket = socket;
  },
  INCREMENT_RECONNECT_COUNT(state) {
    state.reconnectCount++;
  },
  SET_RECONNECT_TIMER(state, timer) {
    state.reconnectTimer = timer;
  },
  CLEAR_RECONNECT_TIMER(state) {
    if (state.reconnectTimer) {
      clearTimeout(state.reconnectTimer);
      state.reconnectTimer = null;
    }
  },
  ADD_MESSAGE(state, message) {
    state.messages.unshift(message);
    state.unreadCount++;
  },
  MARK_ALL_AS_READ(state) {
    state.messages.forEach((msg) => {
      msg.read = true;
    });
    state.unreadCount = 0;
  },
  MARK_MESSAGE_AS_READ(state, messageId) {
    const msg = state.messages.find((m) => m.id === messageId);
    if (msg && !msg.read) {
      msg.read = true;
      state.unreadCount = Math.max(0, state.unreadCount - 1);
    }
  },
  CLEAR_SOCKET(state) {
    state.socket = null;
    state.socketStatus = 'closed';
  },
  RESET_RECONNECT_COUNT(state) {
    state.reconnectCount = 0;
  }
};

const actions = {
  initWebSocket({ commit, state, dispatch }) {
    try {
      commit('SET_SOCKET_STATUS', 'connecting');
      const socket = new WebSocket(
        process.env.VUE_APP_WS_URL || 'ws://localhost:8080/ws'
      );

      socket.onopen = () => {
        commit('SET_SOCKET_STATUS', 'open');
        commit('RESET_RECONNECT_COUNT');
        commit('CLEAR_RECONNECT_TIMER');
        console.log('WebSocket连接成功');
      };

      socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        dispatch('addMessage', message);
      };

      socket.onclose = (event) => {
        commit('SET_SOCKET_STATUS', 'closed');
        console.log('WebSocket连接已关闭', event);
        dispatch('scheduleReconnect');
      };

      socket.onerror = (error) => {
        commit('SET_SOCKET_STATUS', 'closed');
        console.error('WebSocket发生错误', error);
        if (socket) {
          socket.close();
        }
        dispatch('scheduleReconnect');
      };

      commit('SET_SOCKET', socket);
    } catch (error) {
      console.error('初始化WebSocket失败', error);
      dispatch('scheduleReconnect');
    }
  },

  scheduleReconnect({ commit, state, dispatch }) {
    if (state.reconnectCount < state.maxReconnectCount) {
      commit('CLEAR_RECONNECT_TIMER');
      commit('INCREMENT_RECONNECT_COUNT');
      const delay = Math.min(3000, state.reconnectCount * 1000);
      const timer = setTimeout(() => {
        console.log(
          `尝试重新连接WebSocket (${state.reconnectCount}/${state.maxReconnectCount})`
        );
        dispatch('initWebSocket');
      }, delay);
      commit('SET_RECONNECT_TIMER', timer);
    } else {
      console.error('达到最大重连次数，停止尝试');
    }
  },

  addMessage({ commit }, message) {
    const msgObj = {
      ...message,
      id: Date.now().toString(),
      timestamp: new Date().toISOString(),
      read: false
    };
    commit('ADD_MESSAGE', msgObj);
    // 浏览器通知
    if (Notification.permission === 'granted') {
      new Notification('新消息提醒', {
        body: message.content || '您有一条新消息',
        icon: '/logo.png'
      });
    }
    console.log('收到新消息:', message);
  },

  markAllAsRead({ commit }) {
    commit('MARK_ALL_AS_READ');
  },

  markMessageAsRead({ commit }, messageId) {
    commit('MARK_MESSAGE_AS_READ', messageId);
  },

  closeWebSocket({ commit, state }) {
    if (state.socket) {
      commit('CLEAR_RECONNECT_TIMER');
      state.socket.close();
      commit('CLEAR_SOCKET');
    }
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
