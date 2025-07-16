# bruce-douyin(仿抖音短视频)

![](doc/img/开始.png)

​         本项目从2024年6月左右开始写的（目前是2025年5月22日）一开始是提交到gitee，后面转到giehub，一开始也很少提交，只是光写代码，所以从github上面看，最早的提交记录是24年9月。记得当时刚学完前端还不太熟练，那时打算写一个项目练练手，因为作者本人非常爱刷抖音，于是就拿抖音来练手。当时也没想到，这个项目能坚持这么久，当时写的时候感觉写的非常爽，把学到的东西全都能用上。后面，我决定把我新学的东西，全部加到这个项目上面，经过这么久的整理和编写，这个项目正在不断完善，今天我回顾了一下这些年的提交记录，有感而发，写下此话。



====================================================================

2025.6.12 这几天比较闲  于是继续编写了一些页面   我记得有个按钮组件很熟悉  父组件传入参数从而控制子组件按钮的样式  很灵活很好用

这几天总算是把各种功能都串起来了   见识了很多新的技术   

后面想把直播间给搭建起来 问了下AI   接触到了一个agora-rtc-sdk-ng （搭建直播间用这个会很方便）

========================================================================================================================================

现在是2025年6月18日，这几天是毕业领证的几天，同时，我的仿抖音短视频APP大致的框架和基本的功能已经实现了，在今后的日子，我已经失去了学生的身份，从此成为了非常普通的社畜了，我不知道我这一年时间写的项目能对我日后有什么帮助，一切的一切只是在工位上的有感而发，才有了这段话。我也不知道我想写什么，只是想写一些东西。   哦，对了 我本来是想说，  之前的提交记录灭有什么逻辑，基本大部分提交消息都是乱写的，希望日后能把提交消息写好，希望日后的每一次提交都能给我自己带来收获。

=======================================================================================================================================





通过OpenSSL本地生成证书，结合Nginx，使得用户可以通过HTTPS协议访问，因为浏览器的视频拍摄只有通过https协议访问才能打开摄像头

通过Nginx的配置 用户可以通过https或者http协议访问

```
#首先确保已安装 Nginx 和 OpenSSL：
sudo yum install -y nginx openssl
sudo systemctl start nginx
sudo systemctl enable nginx
```

```
#使用 OpenSSL 生成自签名证书和私钥：

# 创建证书目录（如果不存在）
sudo mkdir -p /etc/ssl/private
sudo chmod 700 /etc/ssl/private

# 创建证书配置文件
cat > cert_config.cnf <<EOF
[req]
default_bits = 2048
prompt = no
default_md = sha256
req_extensions = req_ext
distinguished_name = dn

[dn]
C = CN
ST = Shanghai
L = Shanghai
O = Example
CN = localhost

[req_ext]
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
IP.1 = 0.0.0.0  # 允许所有IPv4地址访问
IP.2 = ::        # 允许所有IPv6地址访问
# 如需添加特定IP，可继续添加：
# IP.3 = 192.168.1.100
EOF

# 生成自签名证书（使用配置文件）
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /etc/ssl/private/nginx-selfsigned.key \
    -out /etc/ssl/certs/nginx-selfsigned.crt \
    -config cert_config.cnf

# 生成DH参数（不要中断这个命令，等待完成）
sudo openssl dhparam -out /etc/ssl/certs/dhparam.pem 2048
```

```
#增强 SSL 安全性：

sudo openssl dhparam -out /etc/ssl/certs/dhparam.pem 2048
```



```
#修改nginx配置


#nginx配置

worker_processes 1;

events {
    worker_connections 1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;
    
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

    # HTTPS服务器配置
    server {
        listen       443 ssl;
        server_name  1.94.131.16;
        
        http2 on;
        
        ssl_certificate /etc/ssl/certs/nginx-selfsigned.crt;
        ssl_certificate_key /etc/ssl/private/nginx-selfsigned.key;
        ssl_dhparam /etc/ssl/certs/dhparam.pem;
        
        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_prefer_server_ciphers on;
        ssl_ciphers "ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-SHA384:ECDHE-RSA-AES256-SHA384";
        ssl_ecdh_curve secp384r1;
        ssl_session_timeout 10m;
        ssl_session_cache shared:SSL:10m;
        ssl_session_tickets off;
        
        # 禁用OCSP stapling相关设置（自签名证书不需要）
        # ssl_stapling on;
        # ssl_stapling_verify on;
        # resolver 8.8.8.8 8.8.4.4 valid=300s;
        # resolver_timeout 5s;
        
        add_header Strict-Transport-Security "max-age=63072000; includeSubDomains; preload" always;
        add_header X-Frame-Options DENY;
        add_header X-Content-Type-Options nosniff;
        add_header X-XSS-Protection "1; mode=block";

        # 其他配置保持不变...
        location / {
            root   /usr/share/nginx/html/dist;
            index  index.html index.htm;
            try_files $uri $uri/ /index.html;
        }

        location /api/ {
            rewrite ^/api/(.*)$ /$1 break;
            proxy_pass http://localhost:9430;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        location /minio/ {
            rewrite ^/minio(/.*)$ $1 break;
            proxy_pass http://localhost:9000;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            chunked_transfer_encoding off;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   /usr/share/nginx/html;
        }
    }

    # HTTP服务器配置（移除了重定向）
    server {
        listen       80;
        server_name  1.94.131.16;

        # 与HTTPS相同的内容配置
        location / {
            root   /usr/share/nginx/html/dist;
            index  index.html index.htm;
            try_files $uri $uri/ /index.html;
        }

        location /api/ {
            rewrite ^/api/(.*)$ /$1 break;
            proxy_pass http://localhost:9430;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        location /minio/ {
            rewrite ^/minio(/.*)$ $1 break;
            proxy_pass http://localhost:9000;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            chunked_transfer_encoding off;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   /usr/share/nginx/html;
        }
    }
}
```

















## 2️⃣ HTTP 方法与动作对应关系

- `GET`：查询资源（无副作用）
- `POST`：创建资源
- `PUT`：更新资源（整体更新）
- `PATCH`：更新资源（部分更新）
- `DELETE`：删除资源

**注意：**

- 不滥用 `POST` 做更新或删除。
- `GET` 请求应无副作用（只做查询）。

------

## 3️⃣ URL 设计规范

### 资源定位

- 使用 **名词表示资源**，推荐使用复数
- 使用层级表达资源间关系

### 示例

```
GET    /users              -> 获取用户列表
GET    /users/{id}         -> 获取用户详情
POST   /users              -> 创建用户
PUT    /users/{id}         -> 更新用户（整体更新）
PATCH  /users/{id}         -> 更新用户（部分更新）
DELETE /users/{id}         -> 删除用户
GET    /users/{id}/orders  -> 获取指定用户的订单列表
```

**注意：**

- 不在 URL 中出现动词（如 /getUserList ❌）
- 不在 URL 中携带操作含义（如 /deleteUser ❌）

------

## 4️⃣ 状态码使用规范

- `200 OK`：成功处理请求（GET、PUT、PATCH）
- `201 Created`：成功创建资源（POST）
- `204 No Content`：成功处理，无返回体（DELETE）
- `400 Bad Request`：参数错误
- `401 Unauthorized`：未认证
- `403 Forbidden`：无权限
- `404 Not Found`：资源不存在
- `500 Internal Server Error`：服务器内部错误

------

## 5️⃣ 请求体和响应体格式

- **推荐使用 JSON** 进行请求体与响应体交互。
- 请求体：使用 `@RequestBody` 接收 JSON。
- 响应体：使用 `@RestController` 或 `@ResponseBody` 返回 JSON。

示例：

```java
@PostMapping("/users")
public User createUser(@RequestBody User user) { ... }
```

统一返回结构示例：

```json
{
  "code": 0,
  "message": "success",
  "data": { ... }
}
```























































# 一些界面（全都是本人完成）

目前界面的数据来自与后台 没有整理数据 因此界面不是很好看

## 主界面



![](doc/img/主界面.png)



## 登录界面

![](doc/img/登录界面.png)

## 消息界面

![](doc/img/消息.png)

## 商城界面

![](doc/img/商城.png)

### 项目概述


 本项目为一款仿抖音的短视频社交平台，用户可上传、浏览、点赞、评论短视频，并支持音乐添加、商品购买等功能。平台融合了内容推荐、社交互动、电商变现等模块，构建完整的短视频生态体系，前后端分离开发，支持移动端和管理后台系统。

核心功能：

- 用户注册登录（含第三方授权）
- 视频上传、播放、点赞、评论、收藏
- 音乐库管理与音视频合成
- 好友关注与私信聊天
- 短视频推荐（基于用户行为的算法）
- 商品展示与下单支付
- 后台运营管理系统（用户、内容、商品、订单等）

### 业务说明

![](doc/img/5.png)

项目演示地址：

- 平台管理：暂无

- app端：暂无


技术栈

![](doc/img/6.png)

- Spring-Cloud-Gateway : 微服务之前架设的网关服务，实现服务注册中的API请求路由，以及控制流速控制和熔断处理都是常用的架构手段，而这些功能Gateway天然支持
- 运用Spring Boot快速开发框架，构建项目工程；并结合Spring Cloud全家桶技术，实现后端个人中心、自媒体、管理中心等微服务。
- 运用Spring Cloud Alibaba Nacos作为项目中的注册中心和配置中心
- 运用mybatis-plus作为持久层提升开发效率
- 运用Kafka完成内部系统消息通知；与客户端系统消息通知；以及实时数据计算
- 运用Redis缓存技术，实现热数据的计算，提升系统性能指标
- 使用Mysql存储用户数据，以保证上层数据查询的高性能
- 使用Mongo存储用户热数据，以保证用户热数据高扩展和高性能指标
- 使用FastDFS作为静态资源存储器，在其上实现热静态资源缓存、淘汰等功能
- 运用Hbase技术，存储系统中的冷数据，保证系统数据的可靠性
- 运用ES搜索技术，对冷数据、文章数据建立索引，以保证冷数据、文章查询性能
- 运用AI技术，来完成系统自动化功能，以提升效率及节省成本。比如实名认证自动化
- PMD&P3C : 静态代码扫描工具，在项目中扫描项目代码，检查异常点、优化点、代码规范等，为开发团队提供规范统一，提升项目代码质量



nginx配置文件

```
worker_processes 1;

events {
    worker_connections 1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;

    server {
        listen       80;
        server_name  localhost;

        # Serve static files from the dist folder
        location / {
            root   /usr/share/nginx/html/dist;  # Path to your dist folder
            index  index.html index.htm;
            try_files $uri $uri/ /index.html;  # Ensure SPA routing works correctly
        }

        # Proxy API requests
        location /api/ {
            rewrite ^/api/(.*)$ /$1 break;  # 去掉 /api 前缀
            proxy_pass http://localhost:9430;  # 代理到后端服务
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }


   # MinIO 反向代理（转发到 localhost:9000，去掉 /minio 前缀）
        location /minio/ {
            rewrite ^/minio(/.*)$ $1 break;
            proxy_pass http://localhost:9000;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }




        # WebSocket proxy if needed
        # location /websocket/ {
        #     proxy_pass http://localhost:9998/ws;  # Adjust to your WebSocket server
        #     proxy_set_header Host $host;
        #     proxy_set_header X-Real-IP $remote_addr;
        #     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        #     proxy_set_header X-Forwarded-Proto $scheme;
        #     proxy_http_version 1.1;
        #     proxy_set_header Upgrade $http_upgrade;
        #     proxy_set_header Connection 'upgrade';
        # }

        # Error pages
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   /usr/share/nginx/html;
        }
    }
}

```







本项目提供了内网穿透的脚本 可以通过norok把此项目部署到公网

```
authtoken: 2tq5jqicVNr4qf98hw6JJl7bWxI_2AmunBhWZcpGxiB9cZJoM
version: "2"
tunnels:
  frontend:
    proto: http
    addr: 8011
    domain: intense-lucky-possum.ngrok-free.app
  backend:
    proto: http
    addr: 8080
  fileendpoint:
    proto: http
    addr: 9000


#运行命令 ngrok start --all

```

服务器拉取代码时用这个强制覆盖  服务端一般不改代码

```
# 清除本地所有未提交的改动（已追踪文件和未追踪文件都会删除）
git reset --hard HEAD
git clean -fd

# 强制拉取远程 main 分支并覆盖本地
git fetch origin
git reset --hard origin/main

```



前端通过https访问（这样拍摄界面才能够正确访问摄像头）

1.通过mkcert生成本地密钥

2.vite.config.js指定密钥

```
  const httpsOptions = {
    key: fs.readFileSync(
      'C:/Users/Administrator/Desktop/tools/127.0.0.1+4-key.pem'
    ),
    cert: fs.readFileSync(
      'C:/Users/Administrator/Desktop/tools/127.0.0.1+4.pem'
    )
  }; 
  server: {
        host: true,
        https: httpsOptions, // 开启 https，使用证书
        }
```



对于无限滑动模块，最好在父组件里面把数据查出来，然后再显示，如果是在子组件里面查，容易卡顿，可能是vue的设计缺陷









一键部署（centos）

本项目提供了一键部署的shell脚本 bruce_douyin_start.sh  打包的时候切记先打basic包

```
#!/bin/bash

# 设置脚本执行失败时立即退出
set -e

echo "==================== 开始执行部署脚本 ===================="

# 1. **检查并拉取最新代码**
if command -v git &> /dev/null; then
    echo "git 已安装，正在从远程仓库获取最新代码..."

    # 检查是否有未提交的修改
    if ! git diff --quiet || ! git diff --staged --quiet; then
        echo "检测到未提交的本地修改，先进行 stash 处理..."
        git stash
        git pull origin main
        git stash pop
    else
        git pull origin main
    fi

    if [ $? -eq 0 ]; then
        echo "最新代码获取成功!"
    else
        echo "从远程仓库获取代码时出错!"
        exit 1
    fi
else
    echo "git 未安装，跳过更新代码步骤!"
fi

## 2. **执行 mvn install**
#echo "开始执行 mvn install..."
#mvn install
#
#if [ $? -eq 0 ]; then
#    echo "mvn install 执行成功!"
#else
#    echo "mvn install 执行失败!"
#    exit 1
#fi
# 2. **先 install douyin_basic 模块**
echo "先安装基础模块 douyin_basic ..."
cd douyin_basic || { echo "无法进入 douyin_basic 模块目录"; exit 1; }

mvn install
if [ $? -eq 0 ]; then
    echo "douyin_basic 安装成功!"
else
    echo "douyin_basic 安装失败!"
    exit 1
fi
# 3. **进入 JAR 文件夹**
cd jar || { echo "无法进入 JAR 文件夹"; exit 1; }

# 4. **检查并启动 Redis**
echo "正在检查 Redis 是否运行..."
if command -v redis-cli &> /dev/null; then
    if redis-cli ping | grep -q "PONG"; then
        echo "Redis 已经在运行!"
    else
        echo "Redis 未运行，尝试启动..."
        if systemctl list-units --type=service | grep -q "redis"; then
            sudo systemctl start redis
            if redis-cli ping | grep -q "PONG"; then
                echo "Redis 启动成功!"
            else
                echo "Redis 启动失败! 请手动检查。"
                exit 1
            fi
        else
            echo "Redis 未安装，请先安装 Redis!"
            exit 1
        fi
    fi
else
    echo "Redis 未安装，请先安装 Redis!"
    exit 1
fi

# 5. **关闭所有包含 'douyin' 和 '.jar' 的进程**
echo "正在关闭所有包含 'douyin' 和 '.jar' 字符串的进程..."
PIDS=$(ps aux | grep 'douyin.*\.jar' | grep -v 'grep' | awk '{print $2}')

if [ -n "$PIDS" ]; then
    echo "检测到正在运行的 douyin 相关进程: $PIDS"
    echo "$PIDS" | xargs kill -9
    echo "进程已关闭!"
else
    echo "没有找到相关进程，无需关闭!"
fi

# 6. **启动所有 JAR 文件**
for jar_file in *.jar; do
    if [ -f "$jar_file" ]; then
        echo "正在启动 $jar_file ..."
        nohup java -Xms256m -Xmx512m -jar "$jar_file" --spring.profiles.active=dev > "$jar_file.log" 2>&1 &
        echo "$jar_file 启动成功!"
    fi
done

echo "所有 JAR 文件已启动!"

# 返回上级目录
cd ..

# 7. **进入 Vue 项目目录**
cd douyin_vue2 || { echo "无法进入 Vue 项目文件夹"; exit 1; }

# 8. **安装 npm 依赖**
echo "开始执行 npm install..."
npm install

if [ $? -eq 0 ]; then
    echo "npm install 执行成功!"
else
    echo "npm install 执行失败!"
    exit 1
fi

# 9. **执行 Vue 项目构建**
echo "开始执行 npm run build..."
npm run build

if [ $? -eq 0 ]; then
    echo "npm build 执行成功!"
else
    echo "npm build 执行失败!"
    exit 1
fi

# 10. **重启 Nginx**
echo "正在重启 Nginx..."
sudo systemctl restart nginx

if [ $? -eq 0 ]; then
    echo "Nginx 重启成功!"
else
    echo "Nginx 重启失败!"
    exit 1
fi

echo "==================== 部署脚本执行完成! ===================="

```













# 分片上传实现



首先，依据文件的实际大小与预设的分片尺寸，精确计算出所需的分片总数。随后，通过循环机制逐次向后端发起请求，将各个分片有序上传至文件服务器。待所有分片上传完毕（即循环结束），再触发合并请求，由后端负责将文件服务器上的所有分片整合为完整文件。



在此过程中，后端需同步实现断网重传等保障机制：当检测到网络中断导致上传失败时，系统应自动记录已上传的分片信息；待网络恢复后，仅需重新上传未成功的分片，无需从头开始，以此大幅提升文件上传的效率与稳定性，确保大文件或网络环境不稳定情况下的上传任务能够顺利完成。





```

<script>
import axios from 'axios';

export default {
  data() {
    return {
      file: null,
      chunkSize: 6 * 1024 * 1024,
      totalChunks: 0,
      fileHash: '',
      uploadedChunks: [],
      uploading: false,
      cancelTokenSource: null,
      currentChunkIndex: 0,
      progress: 0
    };
  },
  mounted() {
    // 断网恢复自动续传
    window.addEventListener('online', () => {
      if (
        !this.uploading &&
        this.file &&
        this.uploadedChunks.length < this.totalChunks
      ) {
        this.resumeUpload();
      }
    });
  },
  methods: {
    handleFileChange(e) {
      this.file = e.target.files[0];
      this.totalChunks = Math.ceil(this.file.size / this.chunkSize);
      this.fileHash = this.file.name + '_' + this.file.size; // 简易hash
    },

    async startUpload() {
      if (!this.file) return;

      const res = await axios.get('api/upload/check', {
        params: { fileHash: this.fileHash }
      });
      this.uploadedChunks = res.data.uploaded || [];

      this.uploading = true;
      this.currentChunkIndex = 0;
      this.uploadChunks();
    },

    pauseUpload() {
      if (this.cancelTokenSource) {
        this.cancelTokenSource.cancel('用户暂停上传');
      }
      this.uploading = false;
    },

    resumeUpload() {
      this.uploading = true;
      this.uploadChunks();
    },

    async uploadChunks() {
      while (this.currentChunkIndex < this.totalChunks) {
        if (!this.uploading) break;

        if (this.uploadedChunks.includes(this.currentChunkIndex)) {
          this.updateProgress();
          this.currentChunkIndex++;
          continue;
        }

        const start = this.currentChunkIndex * this.chunkSize;
        const end = Math.min(this.file.size, start + this.chunkSize);
        const chunk = this.file.slice(start, end);

        const formData = new FormData();
        formData.append('file', chunk);
        formData.append('index', this.currentChunkIndex);
        formData.append('total', this.totalChunks);
        formData.append('fileName', this.file.name);
        formData.append('fileHash', this.fileHash);

        this.cancelTokenSource = axios.CancelToken.source();

        try {
          await axios.post('api/upload/chunk', formData, {
            cancelToken: this.cancelTokenSource.token,
            onUploadProgress: (progressEvent) => {
              const chunkProgress = progressEvent.loaded / progressEvent.total;
              this.progress = Math.min(
                100,
                Math.round(
                  ((this.currentChunkIndex + chunkProgress) /
                    this.totalChunks) *
                    100
                )
              );
            }
          });
          this.uploadedChunks.push(this.currentChunkIndex);
          this.updateProgress();
          this.currentChunkIndex++;
        } catch (error) {
          if (axios.isCancel(error)) {
            console.log('上传已暂停', error.message);
            return;
          } else {
            console.error('上传失败，重试中...', error);
            return;
          }
        }
      }

      if (this.uploadedChunks.length === this.totalChunks) {
        await axios.post('api/upload/merge', {
          fileName: this.file.name,
          fileHash: this.fileHash,
          total: this.totalChunks
        });
        this.$message.success('上传并合并完成！');
        this.uploading = false;
      }
    },

    updateProgress() {
      this.progress = Math.min(
        100,
        Math.round((this.uploadedChunks.length / this.totalChunks) * 100)
      );
    },

    formatFileSize(size) {
      if (size < 1024) return size + ' B';
      else if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB';
      else if (size < 1024 * 1024 * 1024)
        return (size / (1024 * 1024)).toFixed(2) + ' MB';
      else return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
    },

    resetUpload() {
      this.file = null;
      this.chunkSize = 2 * 1024 * 1024;
      this.totalChunks = 0;
      this.fileHash = '';
      this.uploadedChunks = [];
      this.uploading = false;
      this.cancelTokenSource = null;
      this.currentChunkIndex = 0;
      this.progress = 0;
    }
  }
};
</script>

```



# 分片下载实现



分片下载是将一个文件分割成多个小块（分片），通过并发请求同时下载这些分片，全部下载完成后再拼接成完整文件的技术。它能利用多线程提升下载速度，且支持断点续传 —— 网络中断后无需重新下载已完成的分片，仅需续传剩余部分，在大文件下载场景中尤为实用。



浏览器的下载方式主要有两种：   

第一种是直接通过链接调用浏览器内置下载工具，以单线程方式获取完整文件流并保存，流程简单直接，但大文件下载时易受单连接速度限制，且中断后需从头开始。   

第二种是分片下载：先将文件拆分为多个独立分片，通过多线程并发请求同时下载这些分片，全部下载完成后在本地拼接成完整文件，最后再调用浏览器下载工具触发保存。这种方式能充分利用网络带宽提升速度，且支持断点续传（中断后仅需重下未完成的分片），尤其适合大文件场景，兼顾了效率与稳定性。



```

<script>
import axios from 'axios';

export default {
  name: 'FileDownload',
  data() {
    return {
      fileName: '',
      fileUrl: '/api/down/files',
      fileInfoUrl: '/api/down/files',

      totalSize: 0,
      totalChunks: 0,
      chunkSize: 50 * 1024 * 1024,

      downloadedChunks: new Set(),
      progress: 0,
      isDownloading: false,
      status: '等待下载',

      cancelTokens: new Map(),
      chunksData: []
    };
  },
  methods: {
    async initDownload() {
      this.status = '获取文件信息...';
      try {
        const res = await axios.get(
          `${this.fileInfoUrl}/${this.fileName}/info`
        );
        if (!res.data.exists) {
          this.status = '文件不存在';
          return false;
        }
        this.totalSize = res.data.fileSize;
        this.totalChunks = Math.ceil(this.totalSize / this.chunkSize);
        this.restoreDownloadedChunks();
        return true;
      } catch (e) {
        this.status = '获取文件信息失败';
        return false;
      }
    },
    restoreDownloadedChunks() {
      const saved = localStorage.getItem(`downloaded_${this.fileName}`);
      if (saved) {
        const chunks = JSON.parse(saved);
        this.downloadedChunks = new Set(chunks);
      }
    },
    saveDownloadedChunks() {
      localStorage.setItem(
        `downloaded_${this.fileName}`,
        JSON.stringify([...this.downloadedChunks])
      );
    },
    async startDownload() {
      if (!this.fileName) {
        this.$message.warning('请输入文件名');
        return;
      }
      if (!(await this.initDownload())) return;
      this.isDownloading = true;
      this.status = '下载中...';
      this.chunksData = [];

      const concurrency = 3;
      let currentChunk = 0;
      let active = 0;

      const downloadNext = async () => {
        if (currentChunk >= this.totalChunks) {
          if (this.downloadedChunks.size === this.totalChunks) {
            this.finishDownload();
          }
          return;
        }
        if (this.downloadedChunks.has(currentChunk)) {
          currentChunk++;
          downloadNext();
          return;
        }
        const chunkIndex = currentChunk++;
        active++;
        this.downloadChunk(chunkIndex)
          .then(() => {
            active--;
            if (active < concurrency && this.isDownloading) downloadNext();
          })
          .catch(() => {
            active--;
            if (this.isDownloading) downloadNext();
          });
        if (active < concurrency && this.isDownloading) downloadNext();
      };

      for (let i = 0; i < Math.min(concurrency, this.totalChunks); i++) {
        downloadNext();
      }
    },
    async downloadChunk(chunkIndex) {
      const start = chunkIndex * this.chunkSize;
      const end = Math.min(start + this.chunkSize - 1, this.totalSize - 1);
      const source = axios.CancelToken.source();
      this.cancelTokens.set(chunkIndex, source);

      try {
        const res = await axios.get(`${this.fileUrl}/${this.fileName}`, {
          headers: { Range: `bytes=${start}-${end}` },
          responseType: 'blob',
          cancelToken: source.token
        });
        this.downloadedChunks.add(chunkIndex);
        this.saveDownloadedChunks();
        this.chunksData[chunkIndex] = res.data;
        this.updateProgress();
      } catch (e) {
        if (!axios.isCancel(e)) console.error(e);
      } finally {
        this.cancelTokens.delete(chunkIndex);
      }
    },
    updateProgress() {
      this.progress = Math.round(
        (this.downloadedChunks.size / this.totalChunks) * 100
      );
    },
    async finishDownload() {
      this.isDownloading = false;
      this.status = '合并文件并下载...';
      const blob = new Blob(this.chunksData);
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = this.fileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
      this.status = '下载完成';
      this.progress = 100;
    },
    pauseDownload() {
      this.isDownloading = false;
      this.status = '已暂停';
      this.cancelTokens.forEach((source) => source.cancel('暂停下载'));
      this.cancelTokens.clear();
    },
    resetDownload() {
      this.pauseDownload();
      this.downloadedChunks.clear();
      localStorage.removeItem(`downloaded_${this.fileName}`);
      this.progress = 0;
      this.status = '已重置';
      this.chunksData = [];
    }
  }
};
</script>

```



# 视频边缓存边播放



基于 MinIO 与 FFmpeg 的视频分片处理及前端播放方案，能有效解决大视频存储占用高、播放加载慢的问题，同时兼顾灵活性与用户体验。

从方案优势来看，这种模式下 MinIO 无需存储完整视频文件，大幅节省存储空间；且支持**断点续传**与**流式播放**，用户无需等待全片上传或加载完成即可开始观看，尤其适合大文件和网络波动场景。

具体处理流程可分为后端处理与前端交互两部分。在后端，前端先将原始视频按固定大小（如 5MB / 片）拆分为多个分片，通过分片上传接口逐个发送至 MinIO。此时，系统会触发 FFmpeg 实时处理：每接收一个分片，便调用 FFmpeg 进行编码转换，按照预设参数（如 10 秒 / 片、H.264 视频编码 + AAC 音频编码）生成对应的.ts 片段，同时动态更新.m3u8 索引文件 —— 该文件会记录已生成的.ts 片段路径、时长及顺序，确保播放时能按正确逻辑拼接。

前端环节则聚焦于播放体验与上传交互。播放时，前端通过获取.m3u8 文件的访问 URL，借助支持 HLS 协议的播放器（如 video.js、Plyr）加载资源。播放器会解析索引文件，按顺序请求对应的.ts 片段，实现 “边下载边播放” 的流式体验。上传交互方面，前端需记录已上传的分片信息（如分片序号、MD5 值），若上传中断，可通过对比已传分片与总分片列表，仅续传未完成部分，减少重复流量消耗。同时，可实时展示上传与转码进度（如通过 WebSocket 接收后端进度数据），提升用户感知。

为优化方案，还可补充这些细节：MinIO 需配置合理的 Bucket 权限，确保前端能直接访问.ts 与.m3u8 文件；FFmpeg 可预设多清晰度转码参数（如 480p、720p、1080p），生成多组.ts 片段及对应的.m3u8 索引，前端根据网络带宽自动切换清晰度；设置 MinIO 生命周期规则，定期清理转码失败的临时分片，释放存储空间。

此外，需注意前端播放器的兼容性处理（如低版本浏览器对 HLS 的支持需借助 Flash fallback），以及转码任务的异步队列管理 —— 避免多分片同时触发 FFmpeg 导致服务器资源占用过高，可通过消息队列（如 RabbitMQ）有序调度转码任务，保障系统稳定性。



```
增加了边缓存边播放的机制  其中视频上传 将视频上传为.m3u8格式和.ts格式的文件 前端下载相关的依赖video.js等
```

![](doc/img/边缓存边播放.png)



```
<template>
  <div class="video-container">
    <video
      id="hls-player"
      class="video-js vjs-default-skin"
      controls
      preload="auto"
      width="100%"
      height="auto"
    >
      <source :src="videoUrl" type="application/x-mpegURL" />
      <p class="vjs-no-js">您的浏览器不支持HTML5视频播放，请升级浏览器。</p>
    </video>
  </div>
</template>

<script>
import { onMounted, onUnmounted, ref } from 'vue';
// 导入 video.js 核心及样式
import videojs from 'video.js';
import 'video.js/dist/video-js.css';
// 导入 HLS 支持（Vite 兼容）
import '@videojs/http-streaming';

export default {
  setup() {
    // 视频链接（替换为你的 m3u8 地址）
    const videoUrl = ref(
      'http://192.168.200.130:9000/douyin/testfile/hls/20230726_054720.mp4_427071097/index.m3u8?Content-Disposition=attachment%3B%20filename%3D%22testfile%2Fhls%2F20230726_054720.mp4_427071097%2Findex.m3u8%22&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minio%2F20250716%2F%2Fs3%2Faws4_request&X-Amz-Date=20250716T010410Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=74f918f930695f00828c276269b09a0704cd7664921706afda785e3bd4b3cc00'
    );
    let player = null;

    onMounted(() => {
      // 初始化播放器
      player = videojs('hls-player', {
        autoplay: false, // 自动播放（需用户交互后生效，浏览器限制）
        fluid: true, // 自适应容器大小
        responsive: true,
        playbackRates: [0.5, 1, 1.5, 2], // 倍速播放选项
        controlBar: {
          fullscreenToggle: true // 显示全屏按钮
        }
      });

      // 监听播放错误
      player.on('error', () => {
        console.error('播放错误详情：', player.error());
        alert('视频播放失败，请检查链接或网络状态');
      });

      // 监听加载状态（验证缓存行为）
      player.on('loadedmetadata', () => {
        console.log('视频元数据加载完成，开始缓存播放');
      });
    });

    onUnmounted(() => {
      // 销毁播放器，释放资源
      if (player) {
        player.dispose();
      }
    });

    return { videoUrl };
  }
};
</script>

<style scoped>
.video-container {
  width: 90%;
  max-width: 1200px;
  margin: 2rem auto;
  padding: 1rem;
  background: #f0f0f0;
  border-radius: 8px;
}

/* 调整视频播放器样式 */
::v-deep .video-js {
  border-radius: 4px;
  overflow: hidden;
}
</style>

```





# websocket

本项目基于websocket实现了实时聊天、实时弹幕、实时通知

## 实时聊天

```
package com.bruce.config;

import com.bruce.handler.UserHandshakeHandler;
import com.bruce.handler.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 开启两个broker：一个是群聊用的 /topic，一个是一对一用的 /queue
        registry.enableSimpleBroker("/topic", "/queue");

        // 应用前缀，客户端发送消息的路径要以 /app 开头
        registry.setApplicationDestinationPrefixes("/app");

        // 点对点时必须配置
        registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/user/ws") // 客户端连接的端点
                .setHandshakeHandler(new UserHandshakeHandler())   // 使用自定义 HandshakeHandler
                .addInterceptors(new UserHandshakeInterceptor()) // 绑定用户身份
                .setAllowedOrigins("*")
                .withSockJS();
    }
}

```



## 实时弹幕

```
package com.bruce.config;

import com.bruce.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// @Configuration 注解表示该类是一个配置类
@Configuration
// @EnableWebSocket 注解启用 WebSocket 配置
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // 重写 registerWebSocketHandlers 方法来注册 WebSocket 处理器
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 将 WebSocket 请求路径设置为 "/video/ws"，以便匹配前端的 WebSocket 请求路径
        registry.addHandler(new WebSocketHandler(), "/video/ws")
                // setAllowedOrigins("*") 设置允许的跨域请求来源，这里设置为 "*" 表示允许所有来源
                .setAllowedOrigins("*");
    }
}

```



## 实时通知

```
package com.bruce.config;

import com.bruce.handler.WebSocketNoteHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketNoteConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketNoteHandler(), "/user/wsnote")
                .setAllowedOrigins("*");
    }
}


```



## 初始工程搭建

### 环境准备

①：项目依赖环境

- JDK1.8

- Intellij Idea

- maven-3.6.1

- Git





### 主体结构

![](doc/img/7.png)



## 登录

### 需求分析

![](doc/img/登录.png)

- 用户点击**开始使用**

  登录后的用户权限较大，可以查看，也可以操作（点赞，关注，评论）

- 用户点击**不登录，先看看**

​       游客只有查看的权限



项目中的持久层使用的mybatis-plus，一般都使用mybais-plus逆向生成对应的实体类

手动加密（md5+随机字符串）

md5是不可逆加密，md5相同的密码每次加密都一样，不太安全。在md5的基础上手动加盐（salt）处理

注册->生成盐

![](doc/img/1.png)

登录->使用盐来配合验证

![](doc/img/2.png)



### 思路分析

![](/doc/img/3.png)

1，用户输入了用户名和密码进行登录，校验成功后返回jwt(基于当前用户的id生成)

2，用户游客登录，生成jwt返回(基于默认值0生成)‘



### 全局过滤器实现jwt校验

![](doc/img/4.png)

思路分析：

1. 用户进入网关开始登陆，网关过滤器进行判断，如果是登录，则路由到后台管理微服务进行登录
2. 用户登录成功，后台管理微服务签发JWT TOKEN信息返回给用户
3. 用户再次进入网关开始访问，网关过滤器接收用户携带的TOKEN 
4. 网关过滤器解析TOKEN ，判断是否有权限，如果有，则放行，如果没有则返回未认证错误










