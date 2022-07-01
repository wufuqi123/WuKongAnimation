```
    一个优雅的代码动画库。支持直接函数调用和链式调用。内部封装Tween来作为动画内核。
```


#### 动画使用
    接下来介绍 <Tween动画> 的使用方式。
##### [链式动画 和 action动画](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)不在当前文档讲述。



#### 创建一个可复用tween动画

    当前创建的Tween动画可以重复调用start和stop，可复用。

    ``` java

        //声明一个obj对象
        class Obj{
            var alpha = 0
        }

        //创建obj对象
        val obj = Obj()

        //让obj对象的“alpha” 从0 - 1在1000毫秒内变化
        //并开始执行动画
        val tween = TweenManager.builder(obj)
                        .to(mutableMapOf("alpha" to 1))
                        .time(1000)
                        .start()
        
        //停止obj动画
        tween.stop()

        //清除当前tween动画，清除之后就不能再使用了。
        tween.remove()

    ```


#### 创建一个一次性tween动画

    当前创建的Tween动画为一次性动画，当动画执行完成后，自动删除，不用再重复调用remove()方法

    ``` java

        //声明一个obj对象
        class Obj{
            var alpha = 0
        }

        //创建obj对象
        val obj = Obj()

        //让obj对象的“alpha” 从0 - 1在1000毫秒内变化
        //并开始执行动画
        val tween = TweenManager.builderOne(obj)
                        .to(mutableMapOf("alpha" to 1))
                        .time(1000)
                        .start()

        //或者可以使用setExpire(true)方法来创建一次性tween动画
        val tween = TweenManager.builder(obj)
                        .setExpire(true)
                        .to(mutableMapOf("alpha" to 1))
                        .time(1000)
                        .start()
        
        
        //停止obj动画
        tween.stop()

    ```


#### 创建一个循环次数Tween动画

    使用repeat(1)来设置循环次数，repeat(-1) -1为无限循环。

    ``` java

        //声明一个obj对象
        class Obj{
            var alpha = 0
        }

        //创建obj对象
        val obj = Obj()

        //让obj对象的“alpha” 从0 - 1在1000毫秒内变化
        //并开始执行动画
        val tween = TweenManager.builderOne(obj)
        // val tween = TweenManager.builder(obj)
                        .to(mutableMapOf("alpha" to 1))
                        // .repeat(-1) //无限循环
                        .repeat(10) //无限10次
                        .time(1000)
                        .start()

        
        
        //停止obj动画
        tween.stop()

    ```

#### 创建一个pingpong效果的Tween动画

    使用pingPong(true) 来设置pingpong效果

    ``` java

        //声明一个obj对象
        class Obj{
            var alpha = 0
        }

        //创建obj对象
        val obj = Obj()

        //让obj对象的“alpha” 从0 - 1在1000毫秒内变化
        //并开始执行动画
        val tween = TweenManager.builderOne(obj)
        // val tween = TweenManager.builder(obj)
                        .to(mutableMapOf("alpha" to 1))
                        .pingPong(true) //设置pingpong
                        .time(1000)
                        .start()

        
        
        //停止obj动画
        tween.stop()

    ```

#### Tween动画 事件

    ``` java

        TweenManager.builder(obj)
            .to(mutableMapOf("alpha" to 1))
            .time(1000)
            .pingPong(true)
            .repeat(-1)
            .on(TweenManager.EVENT_START) {
                //动画开始
                Log.i(this::class.java.name, "Tween start")
            }
            .on(TweenManager.EVENT_RESTART) {
                //动画重新开始
                Log.i(this::class.java.name, "Tween restart")
            }
            .on(TweenManager.EVENT_UPDATE) {
                //动画关键帧，正常一秒大约走60次，跟随系统。
                //名称为：FPS帧率
                Log.i(this::class.java.name, "Tween update")
            }
            .on(TweenManager.EVENT_ERPEAT) {
                //重复执行动画时调用
                Log.i(this::class.java.name, "Tween repeat")
            }
            .on(TweenManager.EVENT_END) {
                //动画执行完成后调用
                Log.i(this::class.java.name, "Tween end")
            }
            .on(TweenManager.EVENT_STOP) {
                //调用stop()方法时调用
                Log.i(this::class.java.name, "Tween stop")
            }

    ```


#### Easing 缓动函数

    缓动函数计算引用 [tween.js](http://tweenjs.github.io/tween.js/) 的缓动函数。

    [缓动效果](http://tweenjs.github.io/tween.js/examples/03_graphs.html)
