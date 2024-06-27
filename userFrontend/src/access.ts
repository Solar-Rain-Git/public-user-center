import {USER_ADMIN_USER, USER_NORMAL_STATUS} from '../config/constant';

/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canAdmin: currentUser && currentUser.userRole === USER_ADMIN_USER,
    canNormalStatusUser: currentUser && currentUser.userStatus === USER_NORMAL_STATUS,
  };
}
