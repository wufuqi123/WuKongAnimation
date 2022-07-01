```
    一个优雅的代码动画库。支持直接函数调用和链式调用。内部封装Tween来作为动画内核。
```

#### Demo展示

##### [Demo apk下载](https://github.com/wufuqi123/WuKongAnimation/raw/main/apk/app-release.apk)
1. github Demo apk 二维码

    ![下载二维码](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/img/github_apk.png)

1. 国内 Demo apk 二维码

    ![下载二维码](https://github.com/wufuqi123/WuKongAnimation/raw/main/assets/img/QRCode_258.png)


#### 当前介绍kotlin使用方式
##### [kotlin使用方式](https://github.com/wufuqi123/WuKongAnimation/raw/main/README.md)
[java使用方式](https://github.com/wufuqi123/WuKongAnimation/raw/main/README_JAVA.md)


#### 基础功能
1. 添加依赖
    ``` 
        implementation 'io.github.wufuqi123:WuKongAnimation:1.0.0'
    ```

2. 初始化SDK
    ``` java
        ActionManager.init(mApplication) // 尽可能早，推荐在Application中初始化
    ```

3. runAction方式使用动画 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/raw/main/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        view.runAction(Action.fadeIn(time))//执行渐入动画
    ```

4. 链式动画 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/raw/main/README_JAVA.md)
     ``` java
        //当前为kotlin代码
        //执行渐入动画
        view.createAction()
            .fadeIn(time)
            .start()
    ```

5. Tween 动画 [java使用方式](https://github.com/wufuqi123/WuKongAnimation/raw/main/README_JAVA.md)
    ``` java
        //当前为kotlin代码
        //执行渐入动画
        //当前动画不会重复使用建议调用 setExpire(true)
        TweenManager.builder(runView)
            .to(mutableMapOf("alpha" to 1))
            .time(time)
            .setExpire(true)
            .start()
    ```

#### 动画使用

    接下来介绍 <链式动画> 和 <action动画> 的使用方式,<[Tween动画](https://github.com/wufuqi123/WuKongAnimation/raw/main/README_JAVA.md)>不在当前文档讲述。