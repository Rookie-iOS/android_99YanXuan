package com.jjshop.utils.httputil;

import com.jjshop.bean.BaseBean;
import com.jjshop.bean.HomeClassifyBean;
import com.jjshop.bean.JJHomeBean;
import com.jjshop.bean.JsonCityBean;
import com.jjshop.listener.OnCommonCallBackIm;
import com.jjshop.utils.BuglyUtil;
import com.jjshop.utils.JjLog;
import com.jjshop.utils.JjToast;
import com.jjshop.utils.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：张国庆
 * 时间：2018/7/27
 */

public class HttpHelper {
    private static volatile HttpHelper httpHelper;

    private HttpHelper(){

    }

    public static HttpHelper bulid(){
        if(null == httpHelper){
            synchronized (HttpHelper.class){
                if(null == httpHelper){
                    httpHelper = new HttpHelper();
                }
            }
        }
        return httpHelper;
    }

    /**
     * Get请求
     * @param url               请求url
     * @param classBean         实体bean
     * @param requestCallBack   请求回调
     * @param <T>               返回实体泛型
     * @return                  不要用这个返回值（只是为了传泛型）
     */
    public <T extends BaseBean> T getRequest(String url, final Class<T> classBean, final OnRequestCallBack<T> requestCallBack){
        return getRequest(url, classBean, null, requestCallBack);
    }

    /**
     * Get请求
     * @param url               请求url
     * @param classBean         实体bean
     * @param headMap           头部信息
     * @param requestCallBack   请求回调
     * @param <T>               返回实体泛型
     * @return                  不要用这个返回值（只是为了传泛型）
     */
    public <T extends BaseBean> T getRequest(final String url, final Class<T> classBean, final Map<String, String> headMap,
                                             final OnRequestCallBack<T> requestCallBack){
        if(StringUtil.isEmpty(url)){
            JjToast.getInstance().toast("接口地址不能为空");
            return null;
        }
        JjLog.e("GET请求地址：" + url);
        GetBuilder builder = OkHttpUtils.get();
        builder.url(url);
        if(null != headMap){// map传参方式
            builder.params(headMap);
            JjLog.e("GET请求地址参数：" + headMap.toString());
        }
        builder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                String up = url;
                if(null != headMap){
                    up = up + headMap.toString();
                }
                Exception exception = new Exception(up);
                BuglyUtil.build().postCatchedException(exception);
                JjLog.e("请求失败=" + e.getMessage());
                if(null != requestCallBack){
                    requestCallBack.onError("连接超时，稍后再试");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if(null == requestCallBack){// 不需要获取到数据
                    if(StringUtil.isEmpty(response)){
                        response = "返回的数据是空的-Z";
                    }
                    JjLog.e("接口返回数据 = " + response);
                    return;
                }
                // 需要获取到数据进行处理
                if(StringUtil.isEmpty(response)){
                    requestCallBack.onError("获取到的数据为空");
                    return;
                }
                T bean = GsonUtil.getGsonUtil().strJsonToBean(response, classBean);
                if(null == bean){
                    requestCallBack.onError("解析失败");
                    return;
                }
                if(bean.getCode() == 0){
                    // 需要保存json
                    if(bean instanceof JsonCityBean || bean instanceof HomeClassifyBean
                            || bean instanceof JJHomeBean){
                        bean.setJson(response);
                    }
                    requestCallBack.onSuccess(bean);
                    return;
                }
                requestCallBack.onError(bean.getMsg());

            }
        });
        return null;
    }

    /**
     * Post请求
     * @param url               请求url
     * @param classBean         实体bean
     * @param map               参数
     * @param requestCallBack   请求回调
     * @param <T>               返回实体泛型
     * @return                  不要用这个返回值（只是为了传泛型）
     */
    public <T extends BaseBean> T postRequest(String url, final Class<T> classBean, Map<String, String> map, final OnRequestCallBack<T> requestCallBack){
        return postRequest(url, classBean, map, null, "", requestCallBack);
    }
    public <T extends BaseBean> T postRequest(final String url, final Class<T> classBean, final Map<String, String> map, final File file, final String fileParams, final OnRequestCallBack<T> requestCallBack){
        if(StringUtil.isEmpty(url)){
            JjToast.getInstance().toast("接口地址不能为空");
            return null;
        }
        JjLog.e("POST请求地址：" + url);
        if(null != map && map.containsKey(HttpUrl.COOKIE)){// 删除之前传的cookie，因为在Application里面做了统一处理
            map.remove(HttpUrl.COOKIE);
        }
        if(null != map && map.containsKey(HttpUrl.User_Agent)){// 删除之前传的useragent，因为在Application里面做了统一处理
            map.remove(HttpUrl.User_Agent);
        }
        if(null != map){
            JjLog.e("POST参数：" + map.toString());
        }
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(url);// 接口地址
        builder.params(map);// 接口所需参数
        if(null != file && !StringUtil.isEmpty(fileParams)){// 上传文件
            builder.addFile(fileParams, file.getName(), file);
        }
        builder.build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String up = url;
                        if(null != map){
                            up = up + map.toString();
                        }
                        Exception exception = new Exception(up);
                        BuglyUtil.build().postCatchedException(exception);
                        JjLog.e("请求失败=" + e.getMessage());
                        if(null != requestCallBack){
                            requestCallBack.onError("连接超时，稍后再试");
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(null == requestCallBack){// 不需要获取到数据
                            if(StringUtil.isEmpty(response)){
                                response = "返回的数据是空的-Z";
                            }
                            JjLog.e("接口返回数据 = " + response);
                            return;
                        }
                        // 需要获取到数据进行处理
                        if(StringUtil.isEmpty(response)){
                            requestCallBack.onError("获取到的数据为空");
                            return;
                        }
                        T bean = GsonUtil.getGsonUtil().strJsonToBean(response, classBean);
                        if(null != requestCallBack){
                            if(null == bean){
                                requestCallBack.onError("解析失败");
                                return;
                            }
                            if(bean.getCode() == GsonUtil.CODE_SUCCESS || bean.getCode() == GsonUtil.CODE_STOCK){
                                requestCallBack.onSuccess(bean);
                                return;
                            }
                            requestCallBack.onError(bean.getMsg());
                        }
                    }
                });
        return null;
    }

    /**
     * Post请求,获取head的值
     * @param url               请求url
     * @param classBean         实体bean
     * @param map               参数
     * @param requestCallBack   请求回调
     * @param <T>               返回实体泛型
     * @return                  不要用这个返回值（只是为了传泛型）
     */
    public <T extends BaseBean> T postCookieRequest(String url, Map<String, String> map, final Class<T> classBean, final OnRequestCallBack<T> requestCallBack){
        if(StringUtil.isEmpty(url)){
            JjToast.getInstance().toast("接口地址不能为空");
            return null;
        }
        JjLog.e("POST-Cookie请求地址：" + url);
        Request request = new Request.Builder()
                .url(url)
                .post(getRequestBody(map))
                .build();
        new OkHttpClient()
                .newCall(request)
                .enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BuglyUtil.build().postCatchedException(e);
                if(null != requestCallBack){
                    requestCallBack.onError("连接超时，请稍后再试");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(null == response) {
                    if(null != requestCallBack){
                        requestCallBack.onError("数据是空");
                    }
                    return;
                }
                if(!response.isSuccessful()){
                    if(null != requestCallBack){
                        requestCallBack.onError("失败");
                    }
                    return;
                }
                Headers headers = response.headers();
                String userId = "";
                String userMobile = "";
                if(null != headers){
                    List<String> cookies = headers.values("Set-Cookie");
                    JjLog.e("cookies:" + cookies.toString());
                    for(int i = 0; i < cookies.size(); i++){
                        String session = cookies.get(i);
                        if(session.contains("user-id")){
                            String[] split = session.split(";");
                            if(split.length > 0){
                                userId = split[0];
                            }
                        }else if(session.contains("user-mobile")){
                            String[] split = session.split(";");
                            if(split.length > 0){
                                userMobile = split[0];
                            }
                        }
                    }
                    JjLog.e("userId:" + userId);
                    JjLog.e("userMobile:" + userMobile);
                }
                String body = response.body().string();
                JjLog.e("返回数据" + body);
                if(null != requestCallBack){
                    T bean = GsonUtil.getGsonUtil().strJsonToBean(body, classBean);
                    if(null == bean){
                        requestCallBack.onError("解析失败");
                        return;
                    }
                    if(bean.getCode() == 0){
                        bean.setUserId(userId);
                        bean.setUserMobile(userMobile);
                        requestCallBack.onSuccess(bean);
                        return;
                    }
                    requestCallBack.onError(bean.getMsg());
                }
            }
        });
        return null;
    }

    private RequestBody getRequestBody(Map<String, String> map){
        //拿到body的构建器
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if(null != map){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                JjLog.e("key = "+entry.getKey() + "value = " + entry.getValue());
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        //拿到requestBody
        RequestBody requestBody = builder.build();
        return requestBody;
    }


    /**
     * 下载文件
     * @param url
     * @param filePath      本地路径
     * @param fileName      文件名称
     * @param callBackIm
     */
    public void getRequestFile(String url, String filePath, String fileName,
                               final OnCommonCallBackIm callBackIm){
        if(StringUtil.isEmpty(url) || StringUtil.isEmpty(filePath) || StringUtil.isEmpty(fileName)){
            return;
        }
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(filePath, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        if(null != callBackIm){
                            callBackIm.onProgress(progress, total);
                        }
                        JjLog.e("progress = " + (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(null != callBackIm){
                            callBackIm.onError("下载失败");
                        }
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        if(null != callBackIm){
                            callBackIm.onSuccess(file);
                        }
                    }
                });
    }

}
