```
    一个优雅的代码动画库。支持直接函数调用和链式调用。内部封装Tween来作为动画内核。
```

#### 文档导航

- [Kotlin 使用文档](https://github.com/wufuqi123/WuKongAnimation/blob/main/README.md)
- [Java 使用文档](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_JAVA.md)
- [Tween 使用文档（当前页）](https://github.com/wufuqi123/WuKongAnimation/blob/main/README_TWEEN.md)

#### 图片和动画看不到
##### [码云链接请进入](https://gitee.com/wu_fuqi/WuKongAnimation/blob/master/README_TWEEN.md)

#### Tween 性能对比说明

如果你要在 Demo 里比较原生动画和 WuKong，请先明确：这组对比数据的 WuKong 一侧，测的是 **Tween 类直接运行**。

也就是类似下面这种方式：

```kotlin
TweenManager.builder(view)
    .time(600)
    .to(mutableMapOf("x" to 100.0))
    .setExpire(true)
    .start()
```

或者复杂场景中，直接组合 `TweenManager.builder(...)` / `Tween(...)` 来跑。

##### 参数参考

对比页中的主要参数含义如下：

- `scenario`
  - 测试场景
  - 当前包括 `moveX`、`alpha`、`scale`、`rotation`、`complex-combo`
- `duration(ms)`
  - 单段动画时长
  - 复杂场景里每个阶段会复用这个时长
- `iterations`
  - 正式统计轮数
  - 页面会先预热 1 次，再按这里设置的轮数求平均
- `view count`
  - 同一轮中，同时执行动画的 view 数量
  - 当前 Demo 支持 `10 / 100 / 1000 / 2000 / 3000`

##### 当前测试环境

下面这组结果对应的当前测试环境如下：

- Android 16
- 存储 6G
- 内存 512MB
- CPU cores 14

同时尽量减少前后台切换、安装更新、日志刷屏、录屏等干扰，让结果更稳定。

##### 当前这 5 组截图结果（complex-combo）

以下表格对应你提供的 5 张截图，统一条件为：
- `scenario = complex-combo`
- `duration(ms) = 600`
- `iterations = 5`

| view count | WuKong / Tween 直接运行 平均总耗时(ms) | Native 平均总耗时(ms) | WuKong 平均帧间隔(ms) | Native 平均帧间隔(ms) | WuKong 最大帧间隔(ms) | Native 最大帧间隔(ms) | WuKong 平均FPS | Native 平均FPS | WuKong 平均卡顿帧 | Native 平均卡顿帧 |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| 10 | 3630.20 | 3430.40 | 16.67 | 16.67 | 16.67 | 16.67 | 59.78 | 59.76 | 0.00 | 0.00 |
| 100 | 3629.80 | 3429.80 | 16.67 | 16.67 | 16.67 | 16.67 | 59.78 | 59.77 | 0.00 | 0.00 |
| 1000 | 3639.00 | 3486.20 | 16.68 | 17.02 | 33.33 | 50.00 | 59.58 | 57.37 | 0.20 | 3.40 |
| 2000 | 3658.80 | 3651.00 | 24.94 | 26.59 | 116.67 | 166.67 | 39.36 | 35.33 | 65.80 | 57.60 |
| 3000 | 3792.40 | 3866.60 | 35.51 | 39.51 | 333.33 | 350.00 | 27.27 | 22.60 | 40.00 | 28.80 |

从这 5 组截图可以直接看出：
- `10 / 100 / 1000 view` 下，Native 的平均总耗时更低
- `2000 view` 下，两边平均总耗时已经非常接近
- `3000 view` 下，这组截图里 `Tween 直接运行` 的平均总耗时更低

##### 结果分析（公平说明）

这组数据不能只看 `FPS`。

`FPS` 更高，通常说明平均帧表现更好，但它 **不等于** 动画就一定更好。要公平评价，至少要同时看下面几项：
- `平均总耗时`：动画实际多久结束，越低越好
- `平均帧间隔`：整体帧节奏是否稳定，越接近 `16.67ms` 越好
- `最大帧间隔`：最严重的一次卡顿有多大，越低越好
- `平均卡顿帧`：卡顿次数 / 程度，越低越好
- `平均 FPS`：只能反映平均帧率，不能单独代表最终体验

按这 5 组数据分别看：

- `10 / 100 view`
  - Native 的平均总耗时明显更低
  - 但两边的平均帧间隔、最大帧间隔、FPS、卡顿帧几乎一致
  - 这说明在小负载下，**Native 更早完成，但两边流畅度差异很小**

- `1000 view`
  - Native 的平均总耗时仍然更低
  - 但 Tween 的平均帧间隔、最大帧间隔、平均 FPS、平均卡顿帧更好
  - 这说明在这个负载下，**Native 在完成时间上占优，Tween 在帧稳定性上占优**

- `2000 view`
  - 两边平均总耗时已经非常接近
  - Tween 的平均帧间隔、最大帧间隔、平均 FPS 更好
  - Native 的平均卡顿帧略低
  - 这说明在高负载下，**双方已经接近，没有绝对赢家，指标上是互有胜负**

- `3000 view`
  - Tween 的平均总耗时、平均帧间隔、最大帧间隔、平均 FPS 更好
  - Native 的平均卡顿帧更低
  - 但要注意，这时两边都已经进入明显高压区，**都不能算“流畅”**，更适合看作极限承压能力对比

##### 结论

如果只基于这 5 组 `complex-combo` 数据，一个更公平的结论是：

- **低负载（10 / 100）**：Native 更占优，主要体现在平均总耗时更低；两边实际流畅度差异不大
- **中负载（1000）**：Native 在完成时间上更占优，Tween 在帧稳定性上更占优
- **高负载（2000）**：两边已经很接近，不能简单说谁全面更强
- **极高负载（3000）**：这组数据里 Tween 直接运行在多个指标上更好，但双方都已经明显吃紧，不能据此得出“全面领先”的结论

所以，这组结果更适合这样理解：
- 它说明了 `Tween 直接运行` 在高负载下具有一定竞争力
- 也说明了 Native 在低负载下依然有很强优势
- **不能只因为某一档 FPS 更高，就直接下结论说哪一边一定更好**
- 最终评价应该结合真实业务负载、你更关心“更早结束”还是“帧更稳”，以及是否处在极限压力场景来判断

##### 数据口径说明

这组对比数据是 **Tween 直接执行口径**，不是高层封装口径。

下面这些写法虽然更方便、更适合业务组织复杂动作，但因为外面还有一层封装，通常达不到同样的数据：

- `createAction()`
- `createSequenceAction()`
- `createSpawnAction()`
- `view.wukong { ... }`

原因很简单：
- Tween 直跑更接近内核能力
- 链式 action 和 Kotlin 语法糖会额外经过动作描述、嵌套结构和执行封装
- 所以它们更偏向“易用性 / 可读性”，不是对比页里这组数字的直接目标

##### 怎么看这些数据

建议按下面顺序自己测试：

1. 先用“同时播放”看轨迹、结束状态和肉眼流畅度
2. 再用“跑性能对比”看平均总耗时、平均帧间隔、最大帧间隔、平均 FPS、平均卡顿帧
3. 先看 `10 / 100`，再逐步切到 `1000 / 2000 / 3000`，更容易观察拐点

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

// adapted from https://github.com/tweenjs/tween.js/blob/9f8c56c4d5856a970b45895bb58cd9d1d56cf3ea/src/Easing.ts

[缓动效果](http://tweenjs.github.io/tween.js/examples/03_graphs.html)


    ``` java
        //使用方式
        TweenManager.builderOne(obj)
            .to(mutableMapOf("alpha" to 1))
            .time(1000)
            .easing(Easing.linear()) //使用线性的缓动函数，默认使用线性
            .start()
    ···
