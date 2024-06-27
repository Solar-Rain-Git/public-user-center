import { QuestionCircleOutlined } from '@ant-design/icons';
import '@umijs/max';
export type SiderTheme = 'light' | 'dark';
export const Question = () => {
  return (
    <div
      style={{
        display: 'none',
        height: 0,
      }}
      onClick={() => {
        window.open('https://pro.ant.design/docs/getting-started');
      }}
    >
      {/*<QuestionCircleOutlined />*/}
    </div>
  );
};
