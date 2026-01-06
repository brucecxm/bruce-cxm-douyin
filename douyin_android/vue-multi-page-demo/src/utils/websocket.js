// WebSocket工具类，支持Android原生和浏览器环境
class WebSocketUtil {
  constructor() {
    this.callback = null;
    this.isConnected = false;
    this.isAndroid = false;
    this.browserWebSocket = null; // 浏览器原生WebSocket实例
  }

  /**
   * 连接WebSocket
   * @param {string} url - WebSocket服务器URL
   * @param {function} callback - 回调函数，接收WebSocket事件
   */
  connect(url, callback) {
    this.callback = callback;

    // 检查是否为Android环境
    if (typeof window.Android !== 'undefined') {
      this.isAndroid = true;
      return this.connectAndroidWebSocket(url);
    } else {
      this.isAndroid = false;
      return this.connectBrowserWebSocket(url);
    }
  }

  /**
   * 连接Android原生WebSocket
   * @param {string} url - WebSocket服务器URL
   */
  connectAndroidWebSocket(url) {
    try {
      // 注册全局回调函数
      window.websocketCallback = (message) => {
        try {
          const data = JSON.parse(message);
          this.handleWebSocketEvent(data);
        } catch (error) {
          console.error('解析WebSocket消息失败:', error);
        }
      };

      // 调用原生WebSocket连接方法
      window.Android.connectWebSocket(url, 'websocketCallback');
      return true;
    } catch (error) {
      console.error('连接Android WebSocket失败:', error);
      return false;
    }
  }

  /**
   * 连接浏览器原生WebSocket
   * @param {string} url - WebSocket服务器URL
   */
  connectBrowserWebSocket(url) {
    try {
      // 创建浏览器原生WebSocket实例
      this.browserWebSocket = new WebSocket(url);

      // 设置WebSocket事件监听器
      this.browserWebSocket.onopen = () => {
        this.handleWebSocketEvent({ type: 'open', message: 'WebSocket连接成功' });
      };

      this.browserWebSocket.onmessage = (event) => {
        this.handleWebSocketEvent({ type: 'message', message: event.data });
      };

      this.browserWebSocket.onerror = () => {
        this.handleWebSocketEvent({ type: 'error', message: 'WebSocket连接错误' });
      };

      this.browserWebSocket.onclose = (event) => {
        this.handleWebSocketEvent({
          type: 'closed',
          code: event.code,
          reason: event.reason
        });
      };

      return true;
    } catch (error) {
      console.error('连接浏览器WebSocket失败:', error);
      return false;
    }
  }

  /**
   * 断开WebSocket连接
   */
  disconnect() {
    if (this.isAndroid) {
      return this.disconnectAndroidWebSocket();
    } else {
      return this.disconnectBrowserWebSocket();
    }
  }

  /**
   * 断开Android原生WebSocket连接
   */
  disconnectAndroidWebSocket() {
    try {
      window.Android.disconnectWebSocket();
      this.isConnected = false;
      return true;
    } catch (error) {
      console.error('断开Android WebSocket失败:', error);
      return false;
    }
  }

  /**
   * 断开浏览器原生WebSocket连接
   */
  disconnectBrowserWebSocket() {
    try {
      if (this.browserWebSocket && this.browserWebSocket.readyState === WebSocket.OPEN) {
        this.browserWebSocket.close(1000, '主动关闭连接');
      }
      this.browserWebSocket = null;
      this.isConnected = false;
      return true;
    } catch (error) {
      console.error('断开浏览器WebSocket失败:', error);
      return false;
    }
  }

  /**
   * 发送WebSocket消息
   * @param {string} message - 要发送的消息
   */
  send(message) {
    if (this.isAndroid) {
      return this.sendAndroidWebSocketMessage(message);
    } else {
      return this.sendBrowserWebSocketMessage(message);
    }
  }

  /**
   * 发送Android原生WebSocket消息
   * @param {string} message - 要发送的消息
   */
  sendAndroidWebSocketMessage(message) {
    try {
      window.Android.sendWebSocketMessage(message);
      return true;
    } catch (error) {
      console.error('发送Android WebSocket消息失败:', error);
      return false;
    }
  }

  /**
   * 发送浏览器原生WebSocket消息
   * @param {string} message - 要发送的消息
   */
  sendBrowserWebSocketMessage(message) {
    try {
      if (this.browserWebSocket && this.browserWebSocket.readyState === WebSocket.OPEN) {
        this.browserWebSocket.send(message);
        // 模拟发送成功事件
        this.handleWebSocketEvent({ type: 'sendSuccess', message: '消息发送成功' });
        return true;
      } else {
        this.handleWebSocketEvent({ type: 'sendError', message: 'WebSocket未连接' });
        return false;
      }
    } catch (error) {
      console.error('发送浏览器WebSocket消息失败:', error);
      this.handleWebSocketEvent({ type: 'sendError', message: error.message });
      return false;
    }
  }

  /**
   * 订阅WebSocket消息
   * @param {function} callback - 回调函数，接收WebSocket事件
   */
  subscribe(callback) {
    this.callback = callback;
    if (this.isAndroid && typeof window.Android !== 'undefined') {
      window.Android.subscribeWebSocket('websocketCallback');
    }
    return true;
  }

  /**
   * 处理WebSocket事件
   * @param {object} event - WebSocket事件对象
   */
  handleWebSocketEvent(event) {
    console.log('WebSocket事件:', event);

    switch (event.type) {
      case 'open':
        this.isConnected = true;
        break;
      case 'close':
      case 'closing':
      case 'closed':
        this.isConnected = false;
        break;
      default:
        break;
    }

    // 调用回调函数
    if (this.callback) {
      this.callback(event);
    }
  }

  /**
   * 获取WebSocket连接状态
   * @returns {boolean} - 连接状态
   */
  getConnectionStatus() {
    return this.isConnected;
  }
}

// 创建单例实例
const websocketUtil = new WebSocketUtil();

export default websocketUtil;