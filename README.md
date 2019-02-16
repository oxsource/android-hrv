# HRV自我测评

## 1、开发环境
<b>开发工具</b>
<pre>Android Studio 3.3.1</pre>
<b>开发语言</b>
<pre>Kotlin</pre>
<b>构建依赖</b>  
<pre>
buildscript {
    ext.kotlin_version = '1.3.21'
    ext.room_version = '1.1.1'
    ext.appcompat_version = "28.0.0"
	... ...
    dependencies {
        classpath 'com.android.tools.build:gradle:3.3.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
		... ...
    }
}
dependencies{
    ... ...
    //FastJson.android
    implementation 'com.alibaba:fastjson:1.1.70.android'
    //Room数据库
    implementation "android.arch.persistence.room:runtime:$room_version"
    annotationProcessor "android.arch.persistence.room:compiler:$room_version"
    kapt "android.arch.persistence.room:compiler:$room_version"
    //UI组件
    implementation "com.android.support:design:$appcompat_version"
    implementation "com.android.support:recyclerview-v7:$appcompat_version"
}
</pre>

## 2、内容说明
#### 2.1、功能模块：
<pre>分类答题、展示结果、网络获取接口数据、数据库缓存及更新等</pre>

#### 2.2、代码结构： 
<pre>
common                      核心通用库包  
    -context                APP上下文  
    -database               Room相关的数据库表、接口、类型转换器等  
    -entity                 通用数据实体  
    -extend                 Kotlin扩展特性   赋予了部分数据类型诸如json解析、时间比较等功能  
    -http                   HttpUrlConnection请求库  
    -repos                  云端及本地数据仓库抽象类  
    -view                   基础的Activity、适配器、自定义控件  
    -vm                     视图模型、AsyncTask封装以及任务管理  
  
index                       首页模块  
    -...                      
quest                       问答模块  
    -...    
</pre>
  
#### 2.3、提示说明:
<b>Kotlin</b>
<pre>
common.extend中使用扩展特性赋予了部分数据类型诸如json解析、时间比较等功能
common.repos中使用lambda实现dsl风格的调用接口，http{client->}，database{db->}
</pre>

<b>HTTP</b>
<pre>
以HttpUrlConnection为基础封装了一套类OkHttp的简版网络请求库(请求+响应+客户端+拦截器)
</pre>

<b>ViewModel</b>
<pre>
未采用google官网的ViewModel及LiveData等组件，基于AsyncTask封装了一套易于在Activity中进行异步请求的视图模型组件
</pre>

## 3、结果分析
<pre>
基本实现计划的功能，但个人认为实现此类功能更适合使用WebApp；
最后设计Recycled基类(频繁的请求、响应、负载等进行复用)，对内存性能就行优化；
遗憾的是APP开发中涉及的异常统计、混淆加固、升级更新等功能此次并未实现。
</pre>

## 4、演示效果

<img src="https://app-screenshot.pgyer.com/image/view/app_screenshots/0ac48a32a3e195599b8d2f345e2aebbc-528" width="240"/>     <img src="https://app-screenshot.pgyer.com/image/view/app_screenshots/483ff5a62dfc2d923d963e0d213c4f39-528" width="240"/>  <img src="https://app-screenshot.pgyer.com/image/view/app_screenshots/ba24ba25746fcf8576ff4a5842d1409f-528" width="240"/>

## 5、资源下载
<b>源码地址</b>
<pre>https://codeload.github.com/oxsource/android-hrv/zip/V1.0</pre>
<b>APK下载</b>
<pre>https://www.pgyer.com/pizzkandroidhrv</pre>
<b>扫码体验</b>  
<img src="https://www.pgyer.com/app/qrcode/pizzkandroidhrv" width="120"/>
