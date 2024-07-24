import EditUser from '@/pages/Admin/userManage/edit';
import { deleteById, searchUsers } from '@/services/ant-design-pro/api';
import { useModel } from '@@/exports';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Avatar, Button, Drawer, Popconfirm, Space, Tag, Typography, message } from 'antd';
import { useRef, useState } from 'react';
import { DEFAULT_AVATAR, USER_ADMIN_USER } from '../../../../config/constant';
const { Text } = Typography;
export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};

const deleteUserById = async (userId: string) => {
  return await deleteById(userId);
};

export default () => {
  const actionRef = useRef<ActionType>();
  const [open, setOpen] = useState(false);
  let [userList, setUserList] = useState<any>([]);
  let [pagination, setPagination] = useState({total: 0, current: 1})
  let [userValue, setUserValue] = useState<Object>({});
  const columns: ProColumns<API.CurrentUser>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      valueType: 'text',
      search: false,
      width: 200,
      align: 'center',
    },
    {
      title: '昵称',
      width: 150,
      dataIndex: 'username',
      align: 'center',
      render: (_, record) => (
        <Text hidden={!record.username} ellipsis={true} copyable={{ tooltips: false }}>
          {record.username}
        </Text>
      ),
    },
    {
      title: '账户',
      width: 150,
      dataIndex: 'userAccount',
      align: 'center',
      render: (_, record) => (
        <Text ellipsis={true} copyable={{ tooltips: false }}>
          {record.userAccount}
        </Text>
      ),
    },
    {
      title: '头像',
      width: 100,
      align: 'center',
      search: false,
      dataIndex: 'avatarUrl',
      render: (_, record) => (
        <Avatar
          src={
            record.avatarUrl?.trim() === '' || record.avatarUrl === null
              ? DEFAULT_AVATAR
              : record.avatarUrl
          }
          alt="Avatar"
        />
      ),
    },
    {
      title: '性别',
      width: 100,
      align: 'center',
      dataIndex: 'gender',
      valueType: 'select',
      valueEnum: {
        0: {
          text: '🕵🏻‍♀️ 未知',
        },
        1: {
          text: '🤵🏻 男',
        },
        2: {
          text: '👰🏻 女',
        },
      },
    },
    {
      title: '电话',
      dataIndex: 'phone',
      align: 'center',
      render: (_, record) => (
        <Text ellipsis={true} hidden={!record.phone} copyable={{ tooltips: false }}>
          {record.phone}
        </Text>
      ),
    },
    {
      title: '邮件',
      dataIndex: 'email',
      width: 200,
      align: 'center',
      render: (_, record) => (
        <Text ellipsis={true} hidden={!record.email} copyable={{ tooltips: false }}>
          {record.email}
        </Text>
      ),
    },
    {
      title: '状态',
      align: 'center',
      dataIndex: 'userStatus',
      valueType: 'select',
      valueEnum: {
        0: {
          text: '正常',
          status: 'Processing',
        },
        1: {
          text: '禁用',
          status: 'Error',
        },
      },
    },
    {
      title: '角色',
      width: 100,
      align: 'center',
      dataIndex: 'userRole',
      valueType: 'select',
      valueEnum: {
        0: {
          text: '普通用户',
        },
        1: {
          text: '管理员',
        },
      },
      renderFormItem: (_, { defaultRender }) => {
        return defaultRender(_);
      },
      render: (_, record) => (
        <Space>
          <Tag color={record.userRole === USER_ADMIN_USER ? 'green' : 'blue'} key="userRole">
            {record.userRole === USER_ADMIN_USER ? '管理员' : '普通用户'}
          </Tag>
        </Space>
      ),
    },
    {
      title: '创建时间',
      align: 'center',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      search: false,
    },
    {
      title: '更新时间',
      align: 'center',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      search: false,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      align: 'center',
      width: 120,
      render: (text, record, _, action) => [
        <div style={{
          width: '100%',
          display: 'flex',
          justifyContent: 'space-around'
        }}>
          <Button
            size="small"
            key="editable"
            ghost
            type="primary"
            onClick={() => {
              setUserValue(record);
              setOpen(true);
            }}
          >
            编辑
          </Button>
          <Popconfirm
            title="删除用户"
            description={`确认删除 ${record.userAccount} 用户吗？`}
            okText="确认"
            showCancel={false}
            placement="leftTop"
            onConfirm={async () => {
              if (record.id != null) {
                const result = await deleteUserById(record.id);
                if (result) {
                  action?.reload();
                  message.success('用户删除成功');
                  return;
                }
              }
            }}
          >
            <Button
              size="small"
              danger
              key="deteletable"
              disabled={record.userRole === USER_ADMIN_USER}
            >
              删除
            </Button>
          </Popconfirm>
        </div>
      ],
    },
  ];
  return (
    <>
      <PageContainer>
        <ProTable<API.CurrentUser>
          columns={columns}
          actionRef={actionRef}
          cardBordered
          scroll={{ x: 2000 }}
          request={async (params, sort, filter) => {
            await waitTime(500);
            userList = await searchUsers(params as API.UserPageInfo);
            setPagination(prev => ({ ...prev, total: userList.total }));
            return { data: userList.userList };
          }}
          editable={{
            type: 'multiple',
          }}
          columnsState={{
            persistenceKey: 'pro-table-singe-demos',
            persistenceType: 'localStorage',
            defaultValue: {
              option: { fixed: 'right', disable: true },
            },
            onChange(value) {
              // console.log('value: ', value);
            },
          }}
          rowKey="id"
          search={{
            labelWidth: 'auto',
          }}
          options={{
            setting: {
              listsHeight: 400,
            },
          }}
          form={{
            // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
            syncToUrl: (values, type) => {
              if (type === 'get') {
                return {
                  ...values,
                  created_at: [values.startTime, values.endTime],
                };
              }
              return values;
            },
          }}
          pagination={{
            current:pagination.current,
            pageSize: 5,
            total:pagination.total,
            onChange: (page) => {
              setPagination(prev => ({ ...prev, current: page }));
            },
          }}
          dateFormatter="string"
          headerTitle="管理"
        ></ProTable>
      </PageContainer>
      <Drawer
        title="用户信息"
        onClose={() => {
          setOpen(false);
        }}
        open={open}
      >
        <EditUser
          userValue={userValue}
          onClose={() => {
            setOpen(false);
            actionRef.current?.reload();
          }}
        />
      </Drawer>
    </>
  );
};
