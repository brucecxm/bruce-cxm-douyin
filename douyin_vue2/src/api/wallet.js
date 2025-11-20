// 钱包相关API
import request from '../utils/request';

/**
 * 检查钱包状态
 * @returns {Promise} 包含钱包状态的Promise
 */
export const checkWalletStatus = () => {
  return request({
    url: '/user/wallet/status',
    method: 'GET'
  });
};

/**
 * 开通钱包
 * @param {Object} data - 包含支付密码等信息
 * @returns {Promise} 开通结果的Promise
 */
export const enableWallet = (data) => {
  return request({
    url: '/user/wallet/enable',
    method: 'POST',
    data
  });
};

/**
 * 获取钱包余额
 * @returns {Promise} 包含余额信息的Promise
 */
export const getWalletBalance = () => {
  return request({
    url: '/user/wallet/balance',
    method: 'GET'
  });
};

/**
 * 钱包充值
 * @param {Object} data - 包含充值金额等信息
 * @returns {Promise} 充值结果的Promise
 */
export const rechargeWallet = (data) => {
  return request({
    url: '/user/wallet/recharge',
    method: 'POST',
    data
  });
};

/**
 * 钱包提现
 * @param {Object} data - 包含提现金额、支付密码等信息
 * @returns {Promise} 提现结果的Promise
 */
export const withdrawWallet = (data) => {
  return request({
    url: '/user/wallet/withdraw',
    method: 'POST',
    data
  });
};

/**
 * 获取钱包交易明细
 * @param {Object} params - 包含分页参数
 * @returns {Promise} 交易明细列表的Promise
 */
export const getWalletTransactions = (params = {}) => {
  return request({
    url: '/user/wallet/transactions',
    method: 'GET',
    params: {
      page: params.page || 1,
      pageSize: params.pageSize || 20
    }
  });
};