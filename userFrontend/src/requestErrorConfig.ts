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
  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      // 拦截请求配置，进行个性化处理。
      let url = process.env.NODE_ENV === 'production' ? "http://192.168.6.129:8080" + config?.url : config?.url;
      // 设置带上 cookie 的选项
      const withCredentials = true;
      // @ts-ignore
      const timeout = config?.timeout + 10000;
      return {...config, url, timeout, withCredentials};
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
