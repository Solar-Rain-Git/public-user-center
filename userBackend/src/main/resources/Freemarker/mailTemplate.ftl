<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="email code">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body style="background-color: #ECECEC; font-family: '微软雅黑', 黑体, sans-serif;">
<!--邮箱验证码模板-->
<div style="padding: 35px;">
    <table cellpadding="0" align="center"
           style="width: 100%; max-width: 660px; margin: 0 auto; text-align: left; position: relative; border-radius: 5px; font-size: 14px; line-height: 1.5; box-shadow: 0 0 5px rgb(153, 153, 153); background: #fff;">
        <tbody>
        <tr>
            <th valign="middle"
                style="height: 25px; line-height: 25px; padding: 15px 35px; background-color: rgb(21, 23, 40); border-radius: 5px 5px 0 0; color: #fff;">
                <font face="微软雅黑" size="5" style="color: rgb(255, 255, 255);">Solar-Rain 用户中心</font>
            </th>
        </tr>
        <tr>
            <td style="word-break: break-all; padding: 25px 35px 40px; background-color: #fff; opacity: 0.8;">
                <div>
                    <h2 style="margin: 5px 0; font-size: 22px; line-height: 20px; color: #333333;">尊敬的用户：</h2>
                    <!-- 中文 -->
                    <p style="text-indent: 2em;font-size: 18px;text-align: justify">
                        您好！感谢您使用Solar-Rain 用户中心，您的账号{0}
                    </p>
                    <br>
                    <div style="width: 100%; margin: 0 auto;">
                        <div style="padding: 10px 10px 0; border-top: 1px solid #ccc; color: #747474; margin-bottom: 20px; line-height: 1.3em; font-size: 12px;">
                            <p>Solar-Rain 团队</p>
                            <p>联系我们：936648031@qq.com</p>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>

</html>
