### 需求场景

> 已经有一个正在迭代更新的APP了，忽然有一天，产品过来跟我说：“能不能再打一个新包，就是那种app的颜色、ICON、名字、包名，甚至是首页样式都不一样的包？” 我的第一想法就是：复制一份代码出来，一点一点的改吧。当然我刚刚开始的做法真的是这样的，一直到我无意间看到了这篇文章。

#### 友情链接(感谢)

> https://www.jianshu.com/p/d1225eabc860

### 实现什么功能

利用一份代码，一个项目可以实现不同的APP开发，当然这些APP大部分都相似，要不然也不需要采用接下来的方式了。只是这些App图标可以不一样，包名可以不一样、名字可以不一样、主题颜色可以不一样、样式可以不一样、基本配置可以不一样，甚至连部分页面逻辑都可以不一样，因为这些都难不倒我。

### 主要实现

丑媳妇总要见公婆

```groovy
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
    compileSdkVersion 27
    defaultConfig {
        minSdkVersion 16
        targetSdkVersion 27
        flavorDimensions "versionCode"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }

    //打包后应用名称
    android.applicationVariants.all { variant ->
        variant.outputs.all {
            if (outputFile != null && outputFile.name.endsWith('release.apk')) {
                def version = variant.versionName.replace(".", "")
                outputFileName = applicationId + "_${variant.productFlavors[0].resValues.get("app_name").value}" + version + ".apk"
            }
        }
    }

    signingConfigs {

        testappone {
            storeFile file("../jks/TestAppOne.jks")
            storePassword "121123"
            keyAlias "testappone"
            keyPassword "121123"
        }

        testapptwo {
            storeFile file("../jks/TestAppTwo.jks")
            storePassword "121123"
            keyAlias "testapptwo"
            keyPassword "121123"
        }
    }

    buildTypes {
        release {
            minifyEnabled false    //是否打开混淆
            shrinkResources false   //是否打开资源压缩
            zipAlignEnabled true     // Zipalign优化
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

        }
        debug {
            minifyEnabled false    //是否打开混淆
            signingConfig null
        }
    }


    productFlavors {

        TestAppOne {
            applicationId 'cn.mifengkong.testappone'
            versionName '1.0.1'
            versionCode 1
            //应用名
            resValue "string", "app_name", 'AppOne'

            //第三方的应用配置也可以放在这里面
            //微信
            buildConfigField "String", "UMENG_SHARE_WEIXIN_ID", "\"wxe1d649292413946e\""
            buildConfigField "String", "UMENG_SHARE_WEIXIN_KEY", "\"99dac6568f9485d8d702b79d86815bcf\""
            //个推
            manifestPlaceholders = [
                    GETUI_APP_ID    : "LRDm4I3goxAvr5lv5is0N2",
                    GETUI_APP_KEY   : "L7YJcRELCG6h4yg1A7UzB9",
                    GETUI_APP_SECRET: "gnYIA0BDol5gI6mzzppus",
                    PACKAGE_NAME    : applicationId,
                    BAIDU_APP_ID    : "mWHP7o3YXmTEGVtMudN0doGCaDjEUT3D"
            ]

            //签名文件
            signingConfig signingConfigs.testappone
        }

        TestAppTwo {
            applicationId 'cn.mifengkong.testapptwo'
            versionName '2.0.1'
            versionCode 2
            //应用名
            resValue "string", "app_name", 'AppTwo'

            //微信
            buildConfigField "String", "UMENG_SHARE_WEIXIN_ID", "\"wxe1d649292413946e\""
            buildConfigField "String", "UMENG_SHARE_WEIXIN_KEY", "\"99dac6568f9485d8d702b79d86815bcf\""
            //个推
            manifestPlaceholders = [
                    GETUI_APP_ID    : "LRDm4I3goxAvr5lv5is0N2",
                    GETUI_APP_KEY   : "L7YJcRELCG6h4yg1A7UzB9",
                    GETUI_APP_SECRET: "gnYIA0BDol5gI6mzzppus",
                    PACKAGE_NAME    : applicationId,
                    BAIDU_APP_ID    : "mWHP7o3YXmTEGVtMudN0doGCaDjEUT3D"
            ]

            //签名文件
            signingConfig signingConfigs.testapptwo
        }


    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"
    implementation 'com.android.support:appcompat-v7:27.0.2'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
}
```

#### `productFlavors`

我们可以在这里面配置我们的APP ，在上面代码里面我们可以看到，我们分别配置了`TestAppOne` 和 `TestAppTwo` 两个App。如果有需要的话，可以继续添加，当然名字可以自己定义的。先说一个注意点：

> 一定要在
>
> ```groovy
> android {
>     compileSdkVersion 27
>     defaultConfig {
>        ...
>         flavorDimensions "versionCode"//一定要配置这句代码
>         testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
>     }
> ```
>
> 如果没有配置的话，你会见到 `All flavors must now belong to a named flavor dimension. Learn more at https://d.android.com/r/tools/flavorDimensions-missing-error-message.html` 这个错误的。

##### `TestAppOne` 里面的配置信息：

- `applicationId` ：包名，这个参数每个包肯定是不一样的，可以放在单独的app里面进行配置。
- `versionName` `versionCode` ：定义版本号
- `buildConfigField` ：如果我们采用 ` buildConfigField "String", "UMENG_SHARE_WEIXIN_ID", "\"wxe1d649292413946e\""` 这种方式定义`UMENG_SHARE_WEIXIN_ID` ，我们可以在代码里面中这样获取，`BuildConfig.UMENG_SHARE_WEIXIN_ID`，这种方式跟我们获取版本和是一样的。
- `manifestPlaceholders` ：占位符，如果我们在app.gradle中配置了的话，可以在`AndroidManifest` 文件中相对于的获取到。 
- `signingConfig` ：配置签名文件
- `resValue` ：我们通过这个属性给我们的app设置应用名为AppOne。当然这里面是有一个坑的，请看下方：

##### resValue坑

我们如果配置了`resValue "string", "app_name", 'AppOne'` 这句代码的话，那么在main/java/res/values/string.xml中的`<string name="app_name">Test</string>`  这句代码删除，否则的话，就会得到以下错误:

> [string/app_name] /app/src/main/res/values/strings.xml: Error: Duplicate resources

上面这些暂且只是实现了能够在同一个项目中运行打两个包，而这两个包的部分配置不一样。哎呦喂，我还没有跟大伙说说要怎么运行这两个包啊。默认的话运行的是第一个包，如果要改的话，请看下图：

![](https://ws1.sinaimg.cn/large/b9cfdd19gy1fuwij15id7j20uc0iydhi.jpg)

`Build Variants` 在Android studio的右下角，想运行哪个包就选中哪个。

### 如何设置不同app的图标

这个时候我们需要去创建两个文件夹

![](https://ws1.sinaimg.cn/large/b9cfdd19gy1fuwis3yg0oj22gk184n8o.jpg)

文件夹的名字一定要与我们定义在`productFlavors` 中的app别名一致，要不然没有作用的。接下来看看我们文件夹里面的内容

![](https://ws1.sinaimg.cn/large/b9cfdd19gy1fuwizyz7aqj21140lemza.jpg)

内部其实跟main文件夹里面的文件配置是一样的，那就开始我们的换ICON操作了。我们只需要在`TestAppOne`文件夹下相对应的 `mipmap-hdpi` 、`mipmap-lhdpi` 、`mipmap-mhdpi` 、`mipmap-xhdpi` 、`mipmap-xxhdpi` 、`mipmap-xxxhdpi` 文件夹中放入相同的图标ICON，这里我们放入的是`ic_launcher.png` 。运行一下就能够看到AppOne的图标将会被我们换成新的了。

### 更换App主题颜色

我们app的主题颜色主要由以下三个颜色来定义

```
<color name="colorPrimary">#1786fa</color>
<color name="colorPrimaryDark">#1786fa</color>
<color name="colorAccent">#88000000</color>
```

这几个颜色的具体意义在这里就不多讲了，我们这里主要讲讲如果给APP设置不同的主题颜色。我们可以在文件夹TestAppOne/res/values文件夹中新建一个color.xml文件，然后在里面定义颜色值。

```
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <color name="colorPrimary">#1786fa</color>
    <color name="colorPrimaryDark">#1786fa</color>
    <color name="colorAccent">#88000000</color>
    <!--按钮的两种颜色-->
    <color name="btnEnableColor">#23bd9f</color>
    <color name="btnDisableColor">#dddddd</color>
</resources>
```

这样的话，你在这里定义的颜色就将会取代了你在main/res/values/color.xml中定义的颜色值了。

### 替换资源文件

有时候我们不同app之间可能只是图片或者是 `drawable` 文件不同，我们可以在对应的app文件夹中，比如说TestAppOne文件夹对应的位置放置相同名称的文件就行了，TestAppOne文件夹中的文件会自动替换掉main文件中对应的资源的。当然这种操作也只有资源文件才会起作用。

### 代码逻辑存在不同

有时候我们的首页不仅样式不一样，可能连代码逻辑都不一样，怎么呢？这个时候我们是否还有办法呢？肯定是有的。这个时候我们的 `TestAppOne/java/cn/fengrong/gradletest` 文件夹就起作用了。在代码例子中，我们存在着不同的登录逻辑，所以 `LoginActivity` 就只能提取出来了，分别放置到文件夹 `TestAppOne/java/cn/fengrong/gradletest` 和 `TestAppTwo/java/cn/fengrong/gradletest` 中，同时去除`main/java/cn/fengrong/gradletest` 中的 `LoginActivity` 文件。`LoginActivity` 到底要放置到哪些文件夹中呢？我们有几个app文件夹就需要放置几份，而且包名要和main文件夹中的一样。详细的可以查看我们的demo，见文底！

### 签名文件

app.gradle中的定义

```groovy
//签名文件
signingConfig signingConfigs.testappone

    buildTypes {
        release {
            minifyEnabled false    //是否打开混淆
            shrinkResources false   //是否打开资源压缩
            zipAlignEnabled true     // Zipalign优化
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

        }
        debug {
            minifyEnabled false    //是否打开混淆
            signingConfig null
        }
    }

    signingConfigs {

        testappone {
            storeFile file("../jks/TestAppOne.jks")
            storePassword "121123"
            keyAlias "testappone"
            keyPassword "121123"
        }

        testapptwo {
            storeFile file("../jks/TestAppTwo.jks")
            storePassword "121123"
            keyAlias "testapptwo"
            keyPassword "121123"
        }
    }
```

上面代码中，我们给不同的包定义了不同的签名，而且还在 `debug` 模式下设置了 `signingConfig null` 。这样子设置的目的是**比如说：我们的微信分享需要用到的应用签名，如果不设置 `signingConfig null`的话，在debug模式和Release模式下的签名是不一样的，因为它们应用的签名文件都是不一样的。如果在开发的时候没有注意到这点的话，你会发现在debug模式下已经测试通过的分享在线上环境上又不行了！**

### umeng分享

umeng分享添加回调Activity的文档截图

![](https://ws1.sinaimg.cn/large/b9cfdd19gy1fuwn258ijoj21aq0kego1.jpg)

在包名下添加 `Activity` ，在这个demo中，我们的包名是什么呢？`cn.fengrong.gradletest` ，是这个吗？其实不是的，微信分享的是通过反射来获取到回调的Activity的，如果路径不对，名字不对，是无法正常回调的，那么包名到底指的是什么呢？就是我们的 `applicationId` ，那这个时候我们需要如何定义文件的位置呢？举例：在TestAppOne文件夹下面的java文件夹中我们需要新建包名 `cn/mifengkong/testappone`，然后在创建wxapi文件夹，新建一个名为`WXEntryActivity`的activity继承`WXCallbackActivity`。这样子就可以找到我们的回调Activity了。

