import { currentUser, updateUser } from '@/services/ant-design-pro/api';
import { useModel } from '@@/exports';
import { ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
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
  Upload,
  UploadFile,
  UploadProps,
  message,
} from 'antd';
import ImgCrop from 'antd-img-crop';
import dayjs from 'dayjs';
import React, { useState } from 'react';
import { DEFAULT_AVATAR } from '../../../../config/constant';

const UserCenter: React.FC = () => {
  const [form] = Form.useForm();
  const { RangePicker } = DatePicker;
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState('');
  const { initialState, setInitialState } = useModel('@@initialState');
  const [fileList, setFileList] = useState<UploadFile[]>([
    {
      uid: '',
      name: '',
      url: initialState?.currentUser?.avatarUrl
        ? initialState.currentUser.avatarUrl
        : DEFAULT_AVATAR,
    },
  ]);

  const getAvatarUrl = (fileList: any[], defaultAvatar: string) => {
    if (fileList.length > 0) {
      const { url, thumbUrl } = fileList[0];
      const avatarUrl = url || thumbUrl;
      if (avatarUrl && avatarUrl !== defaultAvatar) {
        return avatarUrl;
      }
    }
    return null;
  };

  const onFinish = (values: any) => {
    const userJson = {
      id: values?.id,
      username: values?.username,
      userAccount: values?.userAccount,
      avatarUrl: getAvatarUrl(fileList, DEFAULT_AVATAR),
      gender: values?.gender,
      phone: values?.phone,
      email: values?.email,
    };
    Modal.confirm({
      title: '确定修改当前用户信息?',
      icon: <ExclamationCircleFilled />,
      async onOk() {
        const result = await updateUser(userJson);
        if (result) {
          message.success('修改成功');
          const userInfo = await currentUser();
          setInitialState((s) => ({
            ...s,
            currentUser: userInfo,
          }));
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

  return (
    <PageContainer>
      <Form
        layout="horizontal"
        style={{ maxWidth: 600 }}
        onFinish={onFinish}
        form={form}
        initialValues={initialState?.currentUser}
      >
        <Form.Item label="ID" name="id">
          <Input disabled />
        </Form.Item>
        <Form.Item label="昵称" name="username">
          <Input allowClear />
        </Form.Item>
        <Form.Item
          label="账户"
          name="userAccount"
          rules={[{ required: true, message: '请输入您的账户!' }]}
        >
          <Input allowClear />
        </Form.Item>
        <Form.Item label="头像"  name="avatarUrl">
          <div>
            <ImgCrop rotationSlider>
              <Upload
                listType="picture-card"
                fileList={fileList}
                onPreview={handlePreview}
                onChange={handleChange}
                maxCount={1}
              >
                {uploadButton}
              </Upload>
            </ImgCrop>
            {previewImage && (
              <Image
                wrapperStyle={{ display: 'none' }}
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
          rules={[{ required: true, message: '请选择您的性别!' }]}
        >
          <Select
            allowClear
            placeholder="选择性别"
            optionFilterProp="label"
            options={genderArray}
          />
        </Form.Item>
        <Form.Item
          label="电话"
          name="phone"
          rules={[{ required: true, message: '请输入您的联系方式!' }]}
        >
          <Input allowClear />
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
              required: true,
              message: '请输入邮箱!',
            },
          ]}
        >
          <Input allowClear />
        </Form.Item>
        <Form.Item label="状态" name="userStatus">
          <Select
            disabled
            allowClear
            placeholder="状态"
            optionFilterProp="label"
            options={statusArray}
          />
        </Form.Item>
        <Form.Item label="角色" name="userRole">
          <Select
            disabled
            allowClear
            placeholder="状态"
            optionFilterProp="label"
            options={roleArray}
          />
        </Form.Item>
        <Form.Item label="创建">
          <RangePicker
            defaultValue={[
              dayjs(initialState?.currentUser?.createTime, 'YYYY-MM-DD '),
              dayjs(initialState?.currentUser?.updateTime, 'YYYY-MM-DD'),
            ]}
            disabled
          />
        </Form.Item>
        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          <Space>
            <Button type="primary" htmlType="submit">
              修改
            </Button>
            <Button
              htmlType="button"
              onClick={() => {
                form.resetFields();
                setFileList([
                  {
                    uid: '-1',
                    name: 'avatar.png',
                    status: 'done',
                    url: initialState?.currentUser?.avatarUrl
                      ? initialState.currentUser.avatarUrl
                      : DEFAULT_AVATAR,
                  },
                ]);
              }}
            >
              重置
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </PageContainer>
  );
};

export default UserCenter;
