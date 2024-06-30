import type {RequestOptions} from '@@/plugin-request/request';
import type {RequestConfig} from '@umijs/max';
import {history} from '@umijs/max';
import {stringify} from 'querystring';
import {message, notification} from 'antd';

// 错误处理方案： 错误类型
enum ErrorCode {
  success = 0,
  params_error = 40000,
  null_error = 40001,
  no_auth = 40101,
  system_error = 50000,
  not_login = 40100,
}

// 与后端约定的响应数据格式
interface ResponseStructure {
  code: number;
  data: any;
  message: string;
  description: string;
}

/**
 * @name 错误处理
 * pro 自带的错误处理， 可以在这里做自己的改动
 * @doc https://umijs.org/docs/max/request#配置
 */
export const errorConfig: RequestConfig = {
  // 错误处理： umi@3 的错误处理方案。
  errorConfig: {
    // 错误抛出
    errorThrower: (res) => {
      const {code, data, message, description} =
        res as unknown as ResponseStructure;
      if (code !== 0) {
        const error: any = new Error(message);
        error.name = 'BizError';
        error.info = {code, message, data};
        throw error; // 抛出自制的错误
      }
    },
    // 错误接收及处理
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) throw error;
      // 我们的 errorThrower 抛出的错误。
      if (error.name === 'BizError') {
        const errorInfo: ResponseStructure | undefined = error.info;
        if (errorInfo) {
          const {code, description} = errorInfo;
          switch (errorInfo.code) {
            case ErrorCode.success:
              // do nothing
              break;
            case ErrorCode.params_error:
              message.warning(errorInfo.message);
              break;
            case ErrorCode.null_error:
              message.error(errorInfo.message);
              break;
            case ErrorCode.no_auth:
              notification.open({
                description: description,
                message: code + errorInfo.message
              });
              break;
            case ErrorCode.system_error:
              // TODO: redirect
              break;
            default:
              message.error(errorInfo.message);
          }
        }
      } else if (error.response) {
        // Axios 的错误
        // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
        message.error(`Response status:${error.response.status}`);
      } else if (error.request) {
        // 请求已经成功发起，但没有收到响应
        // \`error.request\` 在浏览器中是 XMLHttpRequest 的实例，
        // 而在node.js中是 http.ClientRequest 的实例
        message.error('None response! Please retry.');
      } else {
        // 发送请求时出了点问题
        message.error('Request error, please retry.');
      }
    },
  },

  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      // 拦截请求配置，进行个性化处理。
      const url = config?.url;
      // @ts-ignore
      const timeout = config?.timeout + 10000;
      return {...config, url, timeout};
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      // 拦截响应数据，进行个性化处理
      const {data} = response as unknown as ResponseStructure;
      if (data?.code === 0) {
        return data;
      }
      if (data?.code === ErrorCode.not_login) {
        message.error("请先登录")
        history.replace({
          pathname: '/user/login',
          search: stringify({
            redirect: location.pathname,
          }),
        });
      } else {
        message.error(`${data?.description}`);
      }
      return data;
    },
  ],
};
