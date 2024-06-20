import { AvatarDropdown, AvatarName, Footer, Question } from '@/components';
import { currentUser as queryCurrentUser } from '@/services/ant-design-pro/api';
import { LinkOutlined } from '@ant-design/icons';
import type { Settings as LayoutSettings } from '@ant-design/pro-components';
import { SettingDrawer } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { Link, history } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import { errorConfig } from './requestErrorConfig';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';
const registerPath = '/user/register';
const WhiteList = [registerPath, loginPath];

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      return await queryCurrentUser({
        skipErrorHandler: true,
      });
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  // 如果不是登录页面，执行
  const { location } = history;
  if (!WhiteList.includes(location.pathname)) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }
  return {
    fetchUserInfo,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    actionsRender: () => [<Question key="doc" />],
    avatarProps: {
      src: initialState?.currentUser?.avatarUrl?.avatarUrl
        ? initialState.currentUser.avatarUrl.avatarUrl
        : 'data:image/jpeg;base64,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',
      title: <AvatarName />,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.userAccount,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      if (WhiteList.includes(location.pathname)) {
        return;
      }
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser) {
        history.push(loginPath);
      }
    },
    bgLayoutImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: isDev
      ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined />
            <span>OpenAPI 文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {isDev && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = {
  ...errorConfig,
};
