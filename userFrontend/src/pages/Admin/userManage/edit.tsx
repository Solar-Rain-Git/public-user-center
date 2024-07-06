import {currentUser, updateUser, uploadAvatar} from '@/services/ant-design-pro/api';
import { ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import { useModel } from '@umijs/max';
import {
  Button,
  DatePicker,
  Form,
  GetProp,
  Image,
  Input,
  Modal,
  Select,
  Space,
  Tooltip,
  Upload,
  UploadFile,
  UploadProps,
  message,
} from 'antd';
import dayjs from 'dayjs';
import React, { useEffect, useState } from 'react';
import { DEFAULT_AVATAR, USER_ADMIN_USER, USER_ORDINARY_USER } from '../../../../config/constant';

const EditUser: React.FC<API.EditUserProps> = ({ userValue, onClose }) => {
  const [form] = Form.useForm();
  const { RangePicker } = DatePicker;
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState('');
  const { initialState, setInitialState } = useModel('@@initialState');
  const [fileList, setFileList] = useState<UploadFile[]>([
    {
      uid: '',
      name: '',
      url: userValue?.avatarUrl,
    },
  ]);
  useEffect(() => {
    if (userValue) {
      form.setFieldsValue(userValue);
      setFileList([
        {
          uid: '-1',
          name: 'avatar.png',
          status: 'done',
          url: userValue?.avatarUrl ? userValue.avatarUrl : DEFAULT_AVATAR,
        },
      ]);
    }
  }, [userValue]);
  const onFinish = (values: any) => {
    let userJson = {};
    if (values.userRole === USER_ADMIN_USER) {
      userJson = {
        id: values?.id,
        username: values?.username,
        userAccount: values?.userAccount,
        gender: values?.gender,
        phone: values?.phone,
        email: values?.email,
        userStatus: values?.userStatus,
        userRole: values?.userRole,
      };
    } else {
      userJson = {
        id: values?.id,
        email: values?.email,
        userStatus: values?.userStatus,
        userRole: values?.userRole,
      };
    }
    Modal.confirm({
      title: `确定修改当前 ${userValue?.userAccount} 用户信息?`,
      icon: <ExclamationCircleFilled />,
      async onOk() {
        let uploadResult = true;
        // @ts-ignore
        if (initialState?.currentUser.avatarUrl !== values?.avatarUrl && initialState?.currentUser.id === values?.id) {
          const formData = new FormData();
          // @ts-ignore
          formData.append('file', fileList[0].originFileObj); // 获取上传的文件数据
          // @ts-ignore
          uploadResult = await uploadAvatar(formData);
        }
        if (!uploadResult) return;
        const result = await updateUser(userJson);
        if (result) {
          message.success('个人信息修改成功');
          onClose();
          const userInfo = await currentUser();
          if (userInfo.id === initialState?.currentUser?.id) {
            setInitialState((s) => ({
              ...s,
              currentUser: userInfo,
            }));
          }
          return;
        }
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  };

  const genderArray = [
    {
      value: 1,
      label: '🤵🏻 男',
    },
    {
      value: 2,
      label: '👰🏻 女',
    },
    {
      value: 0,
      label: '🕵🏻‍♀️ 未知',
      disabled: true,
    },
  ];
  const statusArray = [
    {
      value: 0,
      label: '正常',
    },
    {
      value: 1,
      label: '禁用',
    },
  ];
  const roleArray = [
    {
      value: 0,
      label: '普通用户',
    },
    {
      value: 1,
      label: '⭐管理员',
    },
  ];

  type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];
  const getBase64 = (file: FileType): Promise<string> =>
    new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  const handlePreview = async (file: UploadFile) => {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj as FileType);
    }

    setPreviewImage(file.url || (file.preview as string));
    setPreviewOpen(true);
  };
  const handleChange: UploadProps['onChange'] = ({ fileList: newFileList, file: file }) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng && file.status === 'uploading') {
      message.error('只允许上传 JPG/PNG 文件!');
      return;
    }
    // @ts-ignore
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M && file.status === 'uploading') {
      message.error('图片不得大于 2MB!');
      return;
    }
    setFileList(newFileList);
  };

  const uploadButton = (
    <button style={{ border: 0, background: 'none' }} type="button">
      <PlusOutlined />
      <div style={{ marginTop: 8 }}>更新头像</div>
    </button>
  );

  const notCurrentAdminOrOrdinaryUser =
    (userValue?.userRole === USER_ADMIN_USER && userValue?.id !== initialState?.currentUser?.id) ||
    userValue?.userRole === USER_ORDINARY_USER;
  const Admin = userValue?.userRole === USER_ADMIN_USER;
  const notCurrentAdmin =
    userValue?.userRole === USER_ADMIN_USER && userValue?.id !== initialState?.currentUser?.id;

  return (
    <Form layout="horizontal" onFinish={onFinish} form={form} initialValues={userValue}>
      <Form.Item label="ID" name="id">
        <Input disabled />
      </Form.Item>
      <Form.Item label="昵称" name="username">
        <Input disabled={notCurrentAdminOrOrdinaryUser} allowClear />
      </Form.Item>
      <Form.Item
        label="账户"
        name="userAccount"
        rules={[{ required: !notCurrentAdminOrOrdinaryUser, message: '请输入您的账户!' }]}
      >
        <Input disabled={notCurrentAdminOrOrdinaryUser} allowClear />
      </Form.Item>
      <Form.Item label="头像" name="avatarUrl">
        <div>
          <Upload
            listType="picture-card"
            fileList={fileList}
            onPreview={handlePreview}
            onChange={handleChange}
            maxCount={1}
            showUploadList={{showRemoveIcon: false}}
            disabled={notCurrentAdminOrOrdinaryUser}
          >
            {uploadButton}
          </Upload>
          {previewImage && (
            <Image
              wrapperStyle={{display: 'none'}}
              preview={{
                visible: previewOpen,
                onVisibleChange: (visible) => setPreviewOpen(visible),
                afterOpenChange: (visible) => !visible && setPreviewImage(''),
              }}
              src={previewImage}
            />
          )}
        </div>
      </Form.Item>
      <Form.Item
        label="性别"
        name="gender"
        rules={[{ required: !notCurrentAdminOrOrdinaryUser, message: '请选择您的性别!' }]}
      >
        <Select
          disabled={notCurrentAdminOrOrdinaryUser}
          allowClear
          placeholder="选择性别"
          optionFilterProp="label"
          options={genderArray}
        />
      </Form.Item>
      <Form.Item
        label="电话"
        name="phone"
        rules={[{ required: !notCurrentAdminOrOrdinaryUser, message: '请输入您的联系方式!' }]}
      >
        <Input disabled={notCurrentAdminOrOrdinaryUser} allowClear />
      </Form.Item>
      <Form.Item
        label="邮件"
        name="email"
        rules={[
          {
            type: 'email',
            message: '不正确的邮箱格式!',
          },
          {
            required: !notCurrentAdminOrOrdinaryUser,
            message: '请输入邮箱!',
          },
        ]}
      >
        <Input disabled={notCurrentAdminOrOrdinaryUser} allowClear />
      </Form.Item>
      <Form.Item
        label="状态"
        name="userStatus"
        rules={[{ required: !Admin, message: '请选择状态!' }]}
      >
        <Select
          disabled={Admin}
          allowClear
          placeholder="状态"
          optionFilterProp="label"
          options={statusArray}
        />
      </Form.Item>
      <Form.Item
        label="角色"
        name="userRole"
        rules={[{ required: !Admin, message: '请选择状态!' }]}
      >
        <Select
          disabled={Admin}
          allowClear
          placeholder="状态"
          optionFilterProp="label"
          options={roleArray}
        />
      </Form.Item>
      <Form.Item label="创建">
        <RangePicker
          defaultValue={[
            dayjs(userValue?.createTime, 'YYYY-MM-DD '),
            dayjs(userValue?.updateTime, 'YYYY-MM-DD'),
          ]}
          disabled
        />
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
        <Space>
          <Tooltip
            placement="leftTop"
            title={notCurrentAdmin ? '同级管理员不可编辑，请登录该管理账户进行编辑' : ''}
          >
            <Button disabled={notCurrentAdmin} type="primary" htmlType="submit">
              修改
            </Button>
          </Tooltip>
          <Button
            htmlType="button"
            onClick={() => {
              form.resetFields();
              setFileList([
                {
                  uid: '-1',
                  name: 'avatar.png',
                  status: 'done',
                  url: userValue?.avatarUrl ? userValue.avatarUrl : DEFAULT_AVATAR,
                },
              ]);
            }}
          >
            重置
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default EditUser;
