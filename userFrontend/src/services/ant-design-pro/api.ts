// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 获取当前的用户 GET /api/currentUser */
export async function currentUser(options?: { [key: string]: any }) {
  return request<API.CurrentUser>('/api/user/current', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 退出登录接口 POST /api/user/outLogin */
export async function outLogin(options?: { [key: string]: any }) {
  return request<API.BaseResponse<Number>>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 登录接口 POST /user/login */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<API.BaseResponse<API.CurrentUser>>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 注册接口 POST /user/register */
export async function register(body: API.RegisterParams, options?: { [p: string]: any }) {
  return request<API.BaseResponse<API.RegisterResult>>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询用户信息接口 POST /user/search */
export async function searchUsers(body: API.UserPageInfo, options?: { [p: string]: any }) {
  return request<API.BaseResponse<API.UserPageInfo>>('/api/user/search', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除接口 POST /user/delete */
export async function deleteById(body: String, options?: { [p: string]: any }) {
  return request<API.BaseResponse<Boolean>>('/api/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新接口 POST /user/update */
export async function updateUser(body: API.CurrentUser, options?: { [p: string]: any }) {
  return request<API.BaseResponse<Boolean>>('/api/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新接口 POST /user/update */
export async function uploadAvatar(body: FormData, options?: { [p: string]: any }) {
  return request<API.BaseResponse<Boolean>>('/api/file/uploadAvatar', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/** 更新接口 POST /user/update */
export async function registerByEmail(body: API.EmailConfig, options?: { [p: string]: any }) {
  return request<API.BaseResponse<String>>('/api/email/send_email', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/** 更新接口 POST /user/update */
export async function changePasswordByEmail(body: API.EmailConfig, options?: { [p: string]: any }) {
  return request<API.BaseResponse<String>>('/api/email/update_pwd_email', {
    method: 'POST',
    data: body,
    ...(options || {}),
  });
}

/** 注册接口 POST /user/register */
export async function resetPwd(body: API.RegisterParams, options?: { [p: string]: any }) {
  return request<API.BaseResponse<API.RegisterResult>>('/api/user/change_pwd', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
