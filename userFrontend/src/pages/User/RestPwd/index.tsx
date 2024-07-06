import {Footer} from '@/components';
import {changePasswordByEmail, register, registerByEmail, resetPwd} from '@/services/ant-design-pro/api';
import {KeyOutlined, LockOutlined, MailOutlined, UserOutlined} from '@ant-design/icons';
import {LoginForm, ProForm, ProFormText} from '@ant-design/pro-components';
import {Helmet, history} from '@umijs/max';
import {Tabs, message, theme, AutoComplete, Input} from 'antd';
import React, {useState} from 'react';
import {SYSTEM_LOGO} from '../../../../config/constant';
import Settings from '../../../../config/defaultSettings';
import {ProFormCaptcha} from "@ant-design/pro-form";
import {DefaultOptionType} from "rc-select/es/Select";

const RestPwd: React.FC = () => {
  const [type] = useState<string>('account');
  const {token} = theme.useToken();
  const [email, setEmail] = useState('');
  const [options, setOptions] = React.useState<DefaultOptionType[]>([]);
  const handleSearch = (value: string) => {
    setOptions(() => {
      if (!value || value.includes('@')) {
        return [];
      }
      return ['qq.com', '163.com', 'gmail.com'].map<DefaultOptionType>((domain) => ({
        label: `${value}@${domain}`,
        value: `${value}@${domain}`,
      }));
    });
  };

  const handleSubmit = async (values: API.RegisterParams) => {
    try {
      // 注册
      const result = await resetPwd({...values});
      if (result) {
        const defaultLoginSuccessMessage = '密码重置成功！';
        message.success(defaultLoginSuccessMessage);
        const urlParams = new URL(window.location.href).searchParams;
        history.push(urlParams.get('redirect') || '/');
        return;
      }
    } catch (error) {
      console.log(error);
    }
  };
  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        height: '100vh',
        overflow: 'auto',
        backgroundImage:
          "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
        backgroundSize: '100% 100%',
      }}
    >
      <Helmet>
        <title>
          {'忘记密码'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          submitter={{
            searchConfig: {submitText: '重置密码'},
          }}
          logo={<img alt="logo" src={SYSTEM_LOGO}/>}
          title="Solar-Rain 用户中心"
          subTitle={'Solar-Rain 用户中心 是全区最具影响力的 用户中心 设计规范'}
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.RegisterParams);
          }}
        >
          <Tabs
            activeKey={type}
            centered
            items={[
              {
                key: 'account',
                label: '忘记密码',
              },
            ]}
          />

          {type === 'account' && (
            <>
              <ProForm.Item
                name="email"
                rules={[
                  {
                    required: true,
                    message: '邮箱是必填项！',
                  },
                  {
                    type: 'email',
                    message: '请输入有效的邮箱地址！',
                  }
                ]}
              >
                <AutoComplete
                  defaultActiveFirstOption
                  allowClear
                  onSearch={handleSearch}
                  onChange={(value) => setEmail(value)}
                  options={options}
                  size="large"
                >
                  <Input type="email" prefix={<MailOutlined/>} size="large" placeholder="请输入邮箱" style={{
                    padding: '7px 11px',
                    fontSize: '16px',
                    lineHeight: 1.5,
                    borderRadius: '8px'
                  }}/>
                </AutoComplete>
              </ProForm.Item>
              <ProFormCaptcha
                countDown={10}
                allowClear
                fieldProps={{
                  size: 'large',
                  prefix: <KeyOutlined/>,
                }}
                captchaProps={{
                  size: 'large',
                }}
                placeholder={'请输入验证码'}
                captchaTextRender={(timing, count) => {
                  if (timing) {
                    return `${count} ${'获取验证码'}`;
                  }
                  return '获取验证码';
                }}
                name="verCode"
                rules={[
                  {
                    required: true,
                    message: '请输入验证码！',
                  },
                ]}
                onGetCaptcha={async () => {
                  const emailConfig = {
                    subject: "账户密码重置通知",
                    content: "正在密码重置，验证码为：",
                    email: email
                  }
                  const result = await changePasswordByEmail(emailConfig as API.RegisterResult);
                  if (result) {
                    // @ts-ignore
                    message.success(result);
                  }
                }}
              />
              <ProFormText.Password
                allowClear
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined/>,
                  strengthText:
                    '密码应包含数字和大小写字母，长度至少为8个字符。',
                  statusRender: (value) => {
                    const getStatus = () => {
                      // 正则表达式检查是否包含数字和大小写字母
                      const hasNumber = /\d/;
                      const hasLowerCase = /[a-z]/;
                      const hasUpperCase = /[A-Z]/;
                      if (!value) {
                        return 'poor';
                      }
                      if (value.length > 12 && hasNumber.test(value) && hasLowerCase.test(value) && hasUpperCase.test(value)) {
                        return 'ok';
                      }
                      if (value.length >= 8 && hasNumber.test(value) && hasLowerCase.test(value) && hasUpperCase.test(value)) {
                        return 'pass';
                      }
                      return 'poor';
                    };
                    const status = getStatus();
                    if (status === 'pass') {
                      return (
                        <div style={{color: token.colorWarning}}>
                          强度：中
                        </div>
                      );
                    }
                    if (status === 'ok') {
                      return (
                        <div style={{color: token.colorSuccess}}>
                          强度：强
                        </div>
                      );
                    }
                    return (
                      <div style={{color: token.colorError}}>强度：弱</div>
                    );
                  },
                }}
                placeholder={'请输入密码'}
                rules={[
                  {
                    validator: async (_, value) => {
                      // 正则表达式检查是否包含数字和大小写字母
                      const hasNumber = /\d/;
                      const hasLowerCase = /[a-z]/;
                      const hasUpperCase = /[A-Z]/;
                      if (!value) {
                        return Promise.reject('请输入密码！');
                      }
                      if (!(value.length >= 8)) {
                        return Promise.reject('密码不得小于8位！');
                      }
                      if (!(hasNumber.test(value) && hasLowerCase.test(value) && hasUpperCase.test(value))) {
                        return Promise.reject('密码应包含数字和大小写字母！');
                      }
                      return Promise.resolve();
                    },
                  },
                ]}
              />
              <ProFormText.Password
                dependencies={['userPassword']}
                allowClear
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined/>,
                }}
                placeholder={'请确认密码'}
                rules={[
                  {
                    required: true,
                    message: '确认密码是必填项！',
                  },
                  ({getFieldValue}) => ({
                    validator(_, value) {
                      if (!value || getFieldValue('userPassword') === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject(new Error('确认输入密码一致!'));
                    },
                  }),
                ]}
              />
            </>
          )}
        </LoginForm>
      </div>
      <Footer/>
    </div>
  );
};
export default RestPwd;
