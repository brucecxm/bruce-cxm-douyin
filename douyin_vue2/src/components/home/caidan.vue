<template>
  <div class="container">
    <NotImplementedPopup
      v-if="showNotImplemented"
      @close="showNotImplemented = false"
    />
    <div class="header">
      <div class="settings" @click="gosetting">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M12 15C13.6569 15 15 13.6569 15 12C15 10.3431 13.6569 9 12 9C10.3431 9 9 10.3431 9 12C9 13.6569 10.3431 15 12 15Z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M19.4 15C19.2521 15.3095 19.0423 15.5825 18.7913 15.8065C18.5403 16.0305 18.2568 16.2018 17.95 16.31L19.1 18.5C19.4095 18.3521 19.6825 18.1423 19.9065 17.8913C20.1305 17.6403 20.3018 17.3568 20.41 17.05L18.2 15.9C18.31 15.5932 18.4813 15.3097 18.7053 15.0587C18.9293 14.8077 19.1923 14.6363 19.4 14.5L19.4 15Z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M16 11.4C16.3095 11.2521 16.5825 11.0423 16.8065 10.7913C17.0305 10.5403 17.2018 10.2568 17.31 9.95L15.1 8.8C14.9521 9.10951 14.7423 9.38252 14.4913 9.60653C14.2403 9.83053 13.9568 10.0018 13.65 10.11L14.8 12.3C15.1095 12.1521 15.3825 11.9423 15.6065 11.6913C15.8305 11.4403 16.0018 11.1568 16.11 10.85L16 11.4Z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M8 12.6C7.69049 12.7479 7.41748 12.9577 7.19347 13.2087C6.96947 13.4597 6.79819 13.7432 6.69 14.05L8.8 15.2C8.94791 14.8905 9.15771 14.6175 9.40872 14.3935C9.65972 14.1695 9.94321 13.9982 10.25 13.89L9.1 11.6C8.79049 11.7479 8.51748 11.9577 8.29347 12.2087C8.06947 12.4597 7.89819 12.7432 7.79 13.05L8 12.6Z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M4.6 9C4.74787 9.30951 4.95768 9.58252 5.20869 9.80653C5.45969 10.0305 5.74318 10.2018 6.05 10.31L4.9 12.5C4.59049 12.6479 4.31748 12.8577 4.09347 13.1087C3.86947 13.3597 3.69819 13.6432 3.59 13.95L5.79999 15.1C5.69049 15.4095 5.51916 15.693 5.29516 15.944C5.07116 16.195 4.80814 16.3664 4.50001 16.42L4.50001 18.9C4.50001 19.4523 4.94772 20 5.50001 20H8.00001C8.05975 19.6919 8.23117 19.4084 8.48218 19.1844C8.73319 18.9604 9.01669 18.789 9.32499 18.73L9.32499 16.23C9.01669 16.1703 8.73319 15.9989 8.48218 15.7749C8.23117 15.5509 8.05975 15.2674 8.00001 14.9599L6.90001 13.7599C7.01001 13.4532 7.18133 13.1697 7.40534 12.9187C7.62935 12.6677 7.89236 12.4963 8.20001 12.3999L9.40001 10.1999C9.0905 10.3478 8.81749 10.5577 8.59348 10.8087C8.36948 11.0597 8.19819 11.3432 8.09001 11.65L6.89001 9.45C6.78001 9.14321 6.60869 8.85973 6.38468 8.60872C6.16067 8.35771 5.87717 8.1863 5.57001 8.12999L3.07001 8.12999C2.51773 8.12999 2.07001 8.57771 2.07001 9.12999V11.63C2.07001 11.9381 2.24143 12.2216 2.49244 12.4456C2.74345 12.6696 3.02695 12.841 3.33516 12.8999L3.33516 10.4C3.02685 10.3403 2.74335 10.1689 2.49234 9.94491C2.24133 9.7209 2.07 9.4374 2.07 9.12999C2.07 8.57771 2.51772 8.12999 3.07 8.12999H5.57C5.87814 8.1863 6.14116 8.35771 6.36516 8.60872C6.58917 8.85973 6.76049 9.14321 6.87 9.45L4.6 9Z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
      </div>
      <div class="scan" @click="fanhui">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M8 12H16"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M12 8V16"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M3.93 3.93L7.76 7.76"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M16.24 16.24L20.07 20.07"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M3.93 20.07L7.76 16.24"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M16.24 7.76L20.07 3.93"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
        <span>扫一扫</span>
      </div>
    </div>
    <div class="section">
      <div class="section-title">常用功能</div>
      <div class="item">
        <div class="icon-wrapper" @click="gowallet">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M19 7V17C19 18.1046 18.1046 19 17 19H7C5.89543 19 5 18.1046 5 17V7C5 5.89543 5.89543 5 7 5H17C18.1046 5 19 5.89543 19 7Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 11.5C16 12.0523 15.5523 12.5 15 12.5H9C8.44772 12.5 8 12.0523 8 11.5C8 10.9477 8.44772 10.5 9 10.5H15C15.5523 10.5 16 10.9477 16 11.5Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M9 15.5C9 15.2239 9.22386 15 9.5 15H14.5C14.7761 15 15 15.2239 15 15.5C15 15.7761 14.7761 16 14.5 16H9.5C9.22386 16 9 15.7761 9 15.5Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>我的钱包</span>
        </div>
        <div class="icon-wrapper" @click="gocoupon">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M16 4H8C6.89543 4 6 4.89543 6 6V20C6 21.1046 6.89543 22 8 22H16C17.1046 22 18 21.1046 18 20V6C18 4.89543 17.1046 4 16 4Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 10V14"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M9 12H15"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M6 10L4 8C4 7.46957 4.21071 6.96086 4.58579 6.58579C4.96086 6.21071 5.46957 6 6 6H18C18.5304 6 19.0391 6.21071 19.4142 6.58579C19.7893 6.96086 20 7.46957 20 8L18 10"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>优惠券</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>小程序</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 8V12L15 15"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>观看历史</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 16C14.2091 16 16 14.2091 16 12C16 9.79086 14.2091 8 12 8C9.79086 8 8 9.79086 8 12C8 14.2091 9.79086 16 12 16Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>内容偏好</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M19 18H5C3.89543 18 3 17.1046 3 16C3 15.7348 3.09424 15.4804 3.26757 15.2929C3.44091 15.1053 3.71957 15 4 15H19C19.2804 15 19.5591 15.1053 19.7324 15.2929C19.9058 15.4804 20 15.7348 20 16C20 17.1046 19.1046 18 18 18H19Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 13V3"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M8 13V7"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 13V7"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>离线模式</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 6V12L16 14"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>稍后再看</span>
        </div>
      </div>
    </div>
    <div class="section">
      <div class="section-title">生活动态</div>
      <div class="item">
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 2L2 7L12 12L22 7L12 2Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M2 17L12 22L22 17"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M2 12L12 17L22 12"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>直播广场</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M16 10C16.5523 10 17 10.4477 17 11C17 11.5523 16.5523 12 16 12C15.4477 12 15 11.5523 15 11C15 10.4477 15.4477 10 16 10Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 6C16.5523 6 17 6.44772 17 7C17 7.55228 16.5523 8 16 8C15.4477 8 15 7.55228 15 7C15 6.44772 15.4477 6 16 6Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M8 10C8.55228 10 9 10.4477 9 11C9 11.5523 8.55228 12 8 12C7.44772 12 7 11.5523 7 11C7 10.4477 7.44772 10 8 10Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M8 6C8.55228 6 9 6.44772 9 7C9 7.55228 8.55228 8 8 8C7.44772 8 7 7.55228 7 7C7 6.44772 7.44772 6 8 6Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M3 4V18C3 19.1046 3.89543 20 5 20H19C20.1046 20 21 19.1046 21 18V4C21 2.89543 20.1046 2 19 2H5C3.89543 2 3 2.89543 3 4Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M3 10L21 10"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>附近团购</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M8 2V5"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 2V5"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M3 9H21"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M19 5V19C19 20.1046 18.1046 21 17 21H7C5.89543 21 5 20.1046 5 19V5C5 3.89543 5.89543 3 7 3H17C18.1046 3 19 3.89543 19 5Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>活动中心</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M6 10H4C3.46957 10 2.96086 10.2107 2.58579 10.5858C2.21071 10.9609 2 11.4696 2 12C2 12.5304 2.21071 13.0391 2.58579 13.4142C2.96086 13.7893 3.46957 14 4 14H6"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M18 10H20C20.5304 10 21.0391 10.2107 21.4142 10.5858C21.7893 10.9609 22 11.4696 22 12C22 12.5304 21.7893 13.0391 21.4142 13.4142C21.0391 13.7893 20.5304 14 20 14H18"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M9.5 15C9.5 16.3807 10.6193 17.5 12 17.5C13.3807 17.5 14.5 16.3807 14.5 15"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 12C13.6569 12 15 10.6569 15 9C15 7.34315 13.6569 6 12 6C10.3431 6 9 7.34315 9 9C9 10.6569 10.3431 12 12 12Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M15 9V5C15 3.89543 14.1046 3 13 3H11C9.89543 3 9 3.89543 9 5V9"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>玩游戏</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M9 18V5L18 12L9 19V18Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>听抖音</span>
        </div>
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M18 3H6C4.89543 3 4 3.89543 4 5V19C4 20.1046 4.89543 21 6 21H18C19.1046 21 20 20.1046 20 19V5C20 3.89543 19.1046 3 18 3Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M8 21V17"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 21V17"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 21V13"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>放映厅</span>
        </div>
      </div>
      <div class="item">
        <div class="icon-wrapper" @click="openNotImplemented">
          <div class="icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M12 14C13.1046 14 14 13.1046 14 12C14 10.8954 13.1046 10 12 10C10.8954 10 10 10.8954 10 12C10 13.1046 10.8954 14 12 14Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M19 10V7C19 5.89543 18.1046 5 17 5H7C5.89543 5 5 5.89543 5 7V10C5 11.1046 5.89543 12 7 12H17C18.1046 12 19 11.1046 19 10Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M19 10V16C19 17.1046 18.1046 18 17 18H7C5.89543 18 5 17.1046 5 16V10"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M9 22V21"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M15 22V21"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <span>K歌</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { eventBus } from '../../main'; // 导入全局事件总线
import NotImplementedPopup from '../Common/NotImplementedPopup.vue';

export default {
  name: 'Tanchuang',
  components: {
    NotImplementedPopup
  },
  data() {
    return {
      tanchuang: {
        home: 'home',
        istanchuangshow: false
      },
      showNotImplemented: false
    };
  },
  methods: {
    fanhui() {
      // 要传一个参数给home主界面
      eventBus.$emit('message-from-one', this.tanchuang); // 发送事件和数据
    },
    gosetting() {
      this.$router.push('/setting');
    },
    gocoupon() {
      this.showNotImplemented = true;
    },
    gowallet() {
      this.$router.push('/wallet');
    },
    openNotImplemented() {
      this.showNotImplemented = true;
    }
  }
};
</script>

<style scoped>
.container {
  width: 80vw;
  height: 100vh;
  margin: 0 auto;
  background-color: #f0f0f0;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  border-radius: 10px;
  overflow: hidden;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 2vw;
  background-color: white;
  border-bottom: 1px solid #ddd;
}

.settings {
  display: flex;
  align-items: center;
}

.scan {
  display: flex;
  align-items: center;
  transform: translateX(-3vw);
}

.settings i,
.scan i {
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.scan span {
  margin-left: 1vw;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.section {
  width: 100%;
  padding: 2vw;
  background-color: white;
  margin-top: 2vw;
  border-radius: 10px;
}

.section-title {
  font-weight: bold;
  margin-bottom: 1vw;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1vw;
}

.icon-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 33.33%;
}

.icon {
  width: 10vw;
  height: 10vw;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}

.icon-wrapper span {
  margin-top: 1vw;
  text-align: center;
  font-size: 0.5rem;
  /* 设置字体大小为0.5rem */
}
</style>
