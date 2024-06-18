import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';
import { BLOG_LINK, GITHUB_LINK } from '../../../config/constant';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright='太阳雨出品'
      links={[
        {
          key: 'Solar-Rain',
          title: 'Solar-Rain',
          href: BLOG_LINK,
          blankTarget: true,
        },
        {
          key: 'github',
          title: <><GithubOutlined /> 太阳雨 Github</>,
          href: GITHUB_LINK,
          blankTarget: true,
        },
        {
          key: 'User-Center',
          title: '用户中心',
          href: GITHUB_LINK + '/usercenter',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
