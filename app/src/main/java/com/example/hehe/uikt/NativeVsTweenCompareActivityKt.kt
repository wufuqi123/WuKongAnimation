package com.example.hehe.uikt

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.view.Choreographer
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.toColorInt
import com.example.hehe.R
import com.wukonganimation.action.extend.stopAction
import com.wukonganimation.tween.Easing
import com.wukonganimation.tween.Tween
import com.wukonganimation.tween.TweenManager
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.sqrt

class NativeVsTweenCompareActivityKt : Activity() {

    companion object {
        private const val DEFAULT_DURATION_MS = 600L
        private const val DEFAULT_ITERATIONS = 5

        private const val PREPARE_BATCH_SIZE = 80
    }

    private lateinit var flTweenTargets: FrameLayout
    private lateinit var flNativeTargets: FrameLayout
    private lateinit var tvStatus: TextView
    private lateinit var tvTweenResult: TextView
    private lateinit var tvNativeResult: TextView
    private lateinit var tvPrepareStatus: TextView
    private lateinit var etDuration: EditText
    private lateinit var etIterations: EditText
    private lateinit var rgScenario: RadioGroup
    private lateinit var rgTargetCount: RadioGroup
    private lateinit var btnRunVisual: AppCompatButton
    private lateinit var btnRunBenchmark: AppCompatButton
    private lateinit var btnReset: AppCompatButton

    private val tweenTargets = mutableListOf<View>()
    private val nativeTargets = mutableListOf<View>()
    private val tweenInitialStates = LinkedHashMap<View, ViewState>()
    private val nativeInitialStates = LinkedHashMap<View, ViewState>()
    private val runningTweens = mutableListOf<Tween>()
    private val runningNativeAnimators = mutableListOf<AnimatorSet>()

    private var activeCollector: FrameCollector? = null
    private var benchmarkCancelled = false
    private var targetsInitialized = false
    private var preparedTargetCount = 0
    private var isPreparingTargets = false
    private var preparationGeneration = 0
    private var resetAfterPreparation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_vs_tween_compare)

        findViewById<TextView>(R.id.tv_title).text = getString(R.string.native_vs_tween_title)
        flTweenTargets = findViewById(R.id.fl_tween_targets)
        flNativeTargets = findViewById(R.id.fl_native_targets)
        tvStatus = findViewById(R.id.tv_status)
        tvTweenResult = findViewById(R.id.tv_tween_result)
        tvNativeResult = findViewById(R.id.tv_native_result)
        tvPrepareStatus = findViewById(R.id.tv_prepare_status)
        etDuration = findViewById(R.id.et_duration)
        etIterations = findViewById(R.id.et_iterations)
        rgScenario = findViewById(R.id.rg_scenario)
        rgTargetCount = findViewById(R.id.rg_target_count)
        btnRunVisual = findViewById(R.id.btn_run_visual)
        btnRunBenchmark = findViewById(R.id.btn_run_benchmark)
        btnReset = findViewById(R.id.btn_reset)

        btnRunVisual.setOnClickListener {
            runVisualComparison()
        }
        btnRunBenchmark.setOnClickListener {
            runBenchmarkComparison()
        }
        btnReset.setOnClickListener {
            resetTargets()
        }
        rgTargetCount.setOnCheckedChangeListener { _, _ ->
            if (targetsInitialized) {
                requestTargetPreparation(selectedTargetCount(), resetWhenReady = true)
            }
        }
        updatePreparationUi(selectedTargetCount())

        flTweenTargets.post {
            initializeTargetsIfNeeded()
        }
    }

    private fun initializeTargetsIfNeeded() {
        if (targetsInitialized || flTweenTargets.width == 0 || flNativeTargets.width == 0) {
            return
        }
        targetsInitialized = true
        requestTargetPreparation(selectedTargetCount(), resetWhenReady = true)
    }

    private fun requestTargetPreparation(targetCount: Int, resetWhenReady: Boolean) {
        if (!targetsInitialized || flTweenTargets.width == 0 || flNativeTargets.width == 0) {
            return
        }
        preparationGeneration++
        val generation = preparationGeneration
        resetAfterPreparation = resetAfterPreparation || resetWhenReady
        if (preparedTargetCount >= targetCount) {
            isPreparingTargets = false
            applyVisibleTargetCount()
            updatePreparationUi(targetCount)
            if (resetAfterPreparation) {
                resetAfterPreparation = false
                resetPreparedTargetsOnly()
                tvStatus.text = getString(R.string.native_vs_tween_status_idle)
                clearResults()
            }
            return
        }
        isPreparingTargets = true
        updatePreparationUi(targetCount)
        schedulePreparationBatch(generation, targetCount)
    }

    private fun schedulePreparationBatch(generation: Int, targetCount: Int) {
        if (generation != preparationGeneration) {
            return
        }
        val remaining = targetCount - preparedTargetCount
        if (remaining <= 0) {
            finishTargetPreparation(generation, targetCount)
            return
        }
        val batchCount = remaining.coerceAtMost(PREPARE_BATCH_SIZE)
        appendTargetBatch(batchCount)
        preparedTargetCount += batchCount
        updatePreparationUi(targetCount)
        if (preparedTargetCount >= targetCount) {
            finishTargetPreparation(generation, targetCount)
            return
        }
        Choreographer.getInstance().postFrameCallback {
            schedulePreparationBatch(generation, targetCount)
        }
    }

    private fun appendTargetBatch(batchCount: Int) {
        repeat(batchCount) {
            appendTarget(flTweenTargets, tweenTargets, tweenInitialStates, "#FF9800".toColorInt())
            appendTarget(flNativeTargets, nativeTargets, nativeInitialStates, "#3F51B5".toColorInt())
        }
    }

    private fun appendTarget(
        container: FrameLayout,
        targetList: MutableList<View>,
        stateMap: LinkedHashMap<View, ViewState>,
        color: Int
    ) {
        val target = View(this).apply {
            setBackgroundColor(color)
        }
        container.addView(target, FrameLayout.LayoutParams(dp(8), dp(8)))
        targetList.add(target)
        stateMap[target] = captureViewState(target)
    }


    private fun layoutTargets(container: FrameLayout, targets: List<View>, visibleCount: Int) {
        if (container.width == 0 || container.height == 0) {
            return
        }
        targets.forEach { it.visibility = View.INVISIBLE }
        if (visibleCount <= 0) {
            return
        }
        val padding = dp(8)
        val gap = if (visibleCount >= 1000) dp(1) else dp(4)
        val usableWidth = (container.width - padding * 2).coerceAtLeast(1)
        val usableHeight = (container.height - padding * 2).coerceAtLeast(1)
        val columns = calculateColumns(visibleCount, usableWidth, usableHeight)
        val rows = ceil(visibleCount / columns.toDouble()).toInt().coerceAtLeast(1)
        val cellWidth = ((usableWidth - gap * (columns - 1)) / columns).coerceAtLeast(1)
        val cellHeight = ((usableHeight - gap * (rows - 1)) / rows).coerceAtLeast(1)
        val cellSize = minOf(cellWidth, cellHeight).coerceAtLeast(1)

        for (index in 0 until visibleCount.coerceAtMost(targets.size)) {
            val target = targets[index]
            val params = (target.layoutParams as? FrameLayout.LayoutParams)
                ?: FrameLayout.LayoutParams(cellSize, cellSize)
            val row = index / columns
            val column = index % columns
            params.width = cellSize
            params.height = cellSize
            params.leftMargin = padding + column * (cellSize + gap)
            params.topMargin = padding + row * (cellSize + gap)
            target.layoutParams = params
            target.visibility = View.VISIBLE
        }
    }

    private fun calculateColumns(count: Int, width: Int, height: Int): Int {
        if (count <= 10) {
            return count.coerceAtLeast(1)
        }
        val ratio = width.toDouble() / height.toDouble().coerceAtLeast(1.0)
        val estimated = sqrt(count * ratio).toInt().coerceAtLeast(1)
        return estimated.coerceAtMost(count)
    }

    private fun finishTargetPreparation(generation: Int, targetCount: Int) {
        if (generation != preparationGeneration) {
            return
        }
        isPreparingTargets = false
        applyVisibleTargetCount()
        updatePreparationUi(targetCount)
        if (resetAfterPreparation) {
            resetAfterPreparation = false
            resetPreparedTargetsOnly()
            tvStatus.text = getString(R.string.native_vs_tween_status_idle)
            clearResults()
        }
    }

    private fun updatePreparationUi(targetCount: Int) {
        val ready = !isPreparingTargets && preparedTargetCount >= targetCount
        btnRunVisual.isEnabled = ready
        btnRunBenchmark.isEnabled = ready
        btnReset.isEnabled = ready
        tvPrepareStatus.text = if (ready) {
            getString(R.string.native_vs_tween_prepare_ready, targetCount)
        } else {
            getString(R.string.native_vs_tween_prepare_progress, preparedTargetCount.coerceAtMost(targetCount), targetCount)
        }
    }

    private fun runVisualComparison() {
        if (!targetsInitialized) return
        val count = selectedTargetCount()
        if (isPreparingTargets || preparedTargetCount < count) {
            requestTargetPreparation(count, resetWhenReady = false)
            return
        }
        benchmarkCancelled = true
        stopRunningAnimations()
        clearResults()
        val config = buildScenarioConfig()
        tvStatus.text = getString(R.string.native_vs_tween_status_visual)
        startCurrentAnimations(visibleTweenTargets(), config, null)
        startNativeAnimations(visibleNativeTargets(), config, null)
    }

    private fun runBenchmarkComparison() {
        if (!targetsInitialized) return
        val count = selectedTargetCount()
        if (isPreparingTargets || preparedTargetCount < count) {
            requestTargetPreparation(count, resetWhenReady = false)
            return
        }
        benchmarkCancelled = false
        stopRunningAnimations()
        clearResults()
        prepareTargetsForRun()
        val iterations = parsePositiveInt(etIterations.text.toString(), 5)
        val config = buildScenarioConfig()
        val targetCount = selectedTargetCount()
        tvStatus.text = getString(
            R.string.native_vs_tween_status_benchmark,
            getString(config.labelRes),
            targetCount,
            iterations
        )

        runCurrentBenchmark(config, iterations, warmup = true) { currentStats ->
            if (benchmarkCancelled) {
                tvStatus.text = getString(R.string.native_vs_tween_status_cancelled)
                return@runCurrentBenchmark
            }
            tvTweenResult.text = formatStats(
                "${getString(config.labelRes)} / WuKong current / ${targetCount} view",
                currentStats
            )
            prepareTargetsForRun()
            runNativeBenchmark(config, iterations, warmup = true) { nativeStats ->
                if (benchmarkCancelled) {
                    tvStatus.text = getString(R.string.native_vs_tween_status_cancelled)
                    return@runNativeBenchmark
                }
                tvNativeResult.text = formatStats(
                    "${getString(config.labelRes)} / Android native / ${targetCount} view",
                    nativeStats
                )
                tvStatus.text = buildComparisonSummary(config, targetCount, currentStats, nativeStats)
            }
        }
    }

    private fun runCurrentBenchmark(
        config: ScenarioConfig,
        iterations: Int,
        warmup: Boolean,
        onDone: (BenchmarkStats) -> Unit
    ) {
        if (benchmarkCancelled) return
        if (warmup) {
            prepareTargetsForRun()
            startCurrentAnimations(visibleTweenTargets(), config) {
                runCurrentBenchmark(config, iterations, warmup = false, onDone = onDone)
            }
            return
        }
        val metrics = BenchmarkMetrics()
        runCurrentIteration(config, iterations, metrics, onDone)
    }

    private fun runCurrentIteration(
        config: ScenarioConfig,
        remaining: Int,
        metrics: BenchmarkMetrics,
        onDone: (BenchmarkStats) -> Unit
    ) {
        if (benchmarkCancelled) return
        if (remaining <= 0) {
            onDone(metrics.toStats())
            return
        }
        prepareTargetsForRun()
        val collector = FrameCollector()
        startCurrentAnimations(visibleTweenTargets(), config) { elapsedMs ->
            metrics.add(collector.stopAndBuild(elapsedMs))
            runCurrentIteration(config, remaining - 1, metrics, onDone)
        }
        collector.start()
    }

    private fun runNativeBenchmark(
        config: ScenarioConfig,
        iterations: Int,
        warmup: Boolean,
        onDone: (BenchmarkStats) -> Unit
    ) {
        if (benchmarkCancelled) return
        if (warmup) {
            prepareTargetsForRun()
            startNativeAnimations(visibleNativeTargets(), config) {
                runNativeBenchmark(config, iterations, warmup = false, onDone = onDone)
            }
            return
        }
        val metrics = BenchmarkMetrics()
        runNativeIteration(config, iterations, metrics, onDone)
    }

    private fun runNativeIteration(
        config: ScenarioConfig,
        remaining: Int,
        metrics: BenchmarkMetrics,
        onDone: (BenchmarkStats) -> Unit
    ) {
        if (benchmarkCancelled) return
        if (remaining <= 0) {
            onDone(metrics.toStats())
            return
        }
        prepareTargetsForRun()
        val collector = FrameCollector()
        startNativeAnimations(visibleNativeTargets(), config) { elapsedMs ->
            metrics.add(collector.stopAndBuild(elapsedMs))
            runNativeIteration(config, remaining - 1, metrics, onDone)
        }
        collector.start()
    }

    private fun startCurrentAnimations(
        targets: List<View>,
        config: ScenarioConfig,
        onAllEnd: ((Long) -> Unit)?
    ) {
        startAnimationsForTargets(targets, onAllEnd) { target, onEnd ->
            when (config.currentMode) {
                CurrentAnimationMode.TWEEN -> startTweenAnimation(target, config, onEnd)
                CurrentAnimationMode.TWEEN_COMPLEX_CHAIN -> startTweenComplexChainAnimation(target, config, onEnd)
            }
        }
    }

    private fun startNativeAnimations(
        targets: List<View>,
        config: ScenarioConfig,
        onAllEnd: ((Long) -> Unit)?
    ) {
        startAnimationsForTargets(targets, onAllEnd) { target, onEnd ->
            startNativeAnimation(target, config, onEnd)
        }
    }

    private fun startAnimationsForTargets(
        targets: List<View>,
        onAllEnd: ((Long) -> Unit)?,
        starter: (View, () -> Unit) -> Unit
    ) {
        if (targets.isEmpty()) {
            onAllEnd?.invoke(0L)
            return
        }
        val batchStartTime = SystemClock.elapsedRealtime()
        var remaining = targets.size
        targets.forEach { target ->
            starter(target) {
                remaining--
                if (remaining == 0) {
                    Choreographer.getInstance().postFrameCallback {
                        onAllEnd?.invoke(SystemClock.elapsedRealtime() - batchStartTime)
                    }
                }
            }
        }
    }

    private fun startTweenAnimation(
        target: View,
        config: ScenarioConfig,
        onEnd: (() -> Unit)? = null
    ) {
        val resolver = config.tweenToValues ?: return
        val baseState = tweenInitialStates[target] ?: captureViewState(target)
        val tween = TweenManager.builder(target)
            .setExpire(true)
            .time(config.durationMs)
            .easing(Easing.linear())
            .to(resolver.invoke(baseState))
            .on(TweenManager.EVENT_END) {
                onEnd?.invoke()
            }
        runningTweens.add(tween)
        tween.start()
    }

    private fun startTweenComplexChainAnimation(
        target: View,
        config: ScenarioConfig,
        onEnd: (() -> Unit)? = null
    ) {
        val baseState = tweenInitialStates[target] ?: captureViewState(target)
        val duration = config.durationMs
        val moveAndFade = TweenManager.builder(target)
            .setExpire(true)
            .time(duration)
            .easing(Easing.linear())
            .to(
                mutableMapOf(
                    "alpha" to config.alphaTo,
                    "x" to (baseState.x + config.deltaX),
                    "y" to (baseState.y + config.deltaY)
                )
            )
        val rotate = Tween(target)
            .setExpire(true)
            .time(duration)
            .easing(Easing.linear())
            .to(mutableMapOf("rotation" to (baseState.rotation + config.rotationDelta)))
        val waitTween = Tween(target)
            .setExpire(true)
            .delay(config.waitDurationMs)
            .time(0)
        val moveBack = Tween(target)
            .setExpire(true)
            .time(duration)
            .easing(Easing.linear())
            .to(
                mutableMapOf(
                    "x" to baseState.x,
                    "y" to baseState.y
                )
            )
        val scaleAndRotate = Tween(target)
            .setExpire(true)
            .time(duration)
            .easing(Easing.linear())
            .to(
                mutableMapOf(
                    "scaleX" to config.scaleTo,
                    "scaleY" to config.scaleTo,
                    "rotation" to (baseState.rotation + config.rotationDelta * 2)
                )
            )
            .on(TweenManager.EVENT_END) {
                onEnd?.invoke()
            }
        moveAndFade.chain(rotate)
        rotate.chain(waitTween)
        waitTween.chain(moveBack)
        moveBack.chain(scaleAndRotate)
        runningTweens.add(moveAndFade)
        runningTweens.add(rotate)
        runningTweens.add(waitTween)
        runningTweens.add(moveBack)
        runningTweens.add(scaleAndRotate)
        moveAndFade.start()
    }

    private fun startNativeAnimation(
        target: View,
        config: ScenarioConfig,
        onEnd: (() -> Unit)? = null
    ) {
        val baseState = nativeInitialStates[target] ?: captureViewState(target)
        val stepAnimators = config.nativePhases.map { phase ->
            when (phase.type) {
                NativePhaseType.WAIT -> ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = phase.durationMs
                }
                NativePhaseType.TO_VALUES -> AnimatorSet().apply {
                    val values = phase.valueResolver?.invoke(baseState).orEmpty()
                    playTogether(values.map { (propertyName, value) ->
                        ObjectAnimator.ofFloat(target, propertyName, value.toFloat()).apply {
                            duration = phase.durationMs
                        }
                    })
                }
            }
        }
        val animatorSet = AnimatorSet().apply {
            if (stepAnimators.size == 1) {
                playTogether(stepAnimators)
            } else {
                playSequentially(stepAnimators)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onEnd?.invoke()
                }
            })
            start()
        }
        runningNativeAnimators.add(animatorSet)
    }

    private fun buildScenarioConfig(): ScenarioConfig {
        val duration = parsePositiveLong(etDuration.text.toString())
        val waitDuration = 1_000L
        val moveDeltaX = computeMoveDeltaX()
        val moveDeltaY = computeMoveDeltaY()
        val alphaTo = 0.35f
        val scaleTo = 1.35f
        val rotationDelta = 180f
        return when (rgScenario.checkedRadioButtonId) {
            R.id.rb_alpha -> ScenarioConfig(
                labelRes = R.string.native_vs_tween_scenario_alpha,
                currentMode = CurrentAnimationMode.TWEEN,
                durationMs = duration,
                tweenToValues = { _ -> mutableMapOf("alpha" to alphaTo) },
                nativePhases = listOf(
                    NativePhase(duration, NativePhaseType.TO_VALUES) { mutableMapOf("alpha" to alphaTo) }
                )
            )
            R.id.rb_scale -> ScenarioConfig(
                labelRes = R.string.native_vs_tween_scenario_scale,
                currentMode = CurrentAnimationMode.TWEEN,
                durationMs = duration,
                tweenToValues = { mutableMapOf("scaleX" to scaleTo, "scaleY" to scaleTo) },
                nativePhases = listOf(
                    NativePhase(duration, NativePhaseType.TO_VALUES) { mutableMapOf("scaleX" to scaleTo, "scaleY" to scaleTo) }
                )
            )
            R.id.rb_rotation -> ScenarioConfig(
                labelRes = R.string.native_vs_tween_scenario_rotation,
                currentMode = CurrentAnimationMode.TWEEN,
                durationMs = duration,
                tweenToValues = { state -> mutableMapOf("rotation" to state.rotation + rotationDelta) },
                nativePhases = listOf(
                    NativePhase(duration, NativePhaseType.TO_VALUES) { state -> mutableMapOf("rotation" to state.rotation + rotationDelta) }
                )
            )
            R.id.rb_complex_combo -> ScenarioConfig(
                labelRes = R.string.native_vs_tween_scenario_complex_combo,
                currentMode = CurrentAnimationMode.TWEEN_COMPLEX_CHAIN,
                durationMs = duration,
                waitDurationMs = waitDuration,
                deltaX = moveDeltaX,
                deltaY = moveDeltaY,
                alphaTo = alphaTo,
                scaleTo = scaleTo,
                rotationDelta = rotationDelta,
                nativePhases = listOf(
                    NativePhase(duration, NativePhaseType.TO_VALUES) { state ->
                        mutableMapOf(
                            "alpha" to alphaTo,
                            "x" to state.x + moveDeltaX,
                            "y" to state.y + moveDeltaY
                        )
                    },
                    NativePhase(duration, NativePhaseType.TO_VALUES) { state ->
                        mutableMapOf("rotation" to state.rotation + rotationDelta)
                    },
                    NativePhase(waitDuration, NativePhaseType.WAIT),
                    NativePhase(duration, NativePhaseType.TO_VALUES) { state ->
                        mutableMapOf(
                            "x" to state.x,
                            "y" to state.y
                        )
                    },
                    NativePhase(duration, NativePhaseType.TO_VALUES) { state ->
                        mutableMapOf(
                            "scaleX" to scaleTo,
                            "scaleY" to scaleTo,
                            "rotation" to state.rotation + rotationDelta * 2
                        )
                    }
                )
            )
            else -> ScenarioConfig(
                labelRes = R.string.native_vs_tween_scenario_move,
                currentMode = CurrentAnimationMode.TWEEN,
                durationMs = duration,
                tweenToValues = { state -> mutableMapOf("x" to state.x + moveDeltaX) },
                nativePhases = listOf(
                    NativePhase(duration, NativePhaseType.TO_VALUES) { state -> mutableMapOf("x" to state.x + moveDeltaX) }
                )
            )
        }
    }

    private fun resetTargets() {
        benchmarkCancelled = true
        if (!targetsInitialized) return
        val count = selectedTargetCount()
        if (isPreparingTargets || preparedTargetCount < count) {
            requestTargetPreparation(count, resetWhenReady = true)
            return
        }
        resetPreparedTargetsOnly()
        tvStatus.text = getString(R.string.native_vs_tween_status_idle)
        clearResults()
    }

    private fun prepareTargetsForRun() {
        stopRunningAnimations()
        applyVisibleTargetCount()
        tweenTargets.forEach { target -> tweenInitialStates[target]?.let { resetView(target, it) } }
        nativeTargets.forEach { target -> nativeInitialStates[target]?.let { resetView(target, it) } }
    }

    private fun resetPreparedTargetsOnly() {
        prepareTargetsForRun()
    }

    private fun resetView(target: View, state: ViewState) {
        target.x = state.x
        target.y = state.y
        target.alpha = state.alpha
        target.rotation = state.rotation
        target.scaleX = state.scaleX
        target.scaleY = state.scaleY
    }

    private fun captureViewState(target: View): ViewState {
        return ViewState(
            x = target.x,
            y = target.y,
            alpha = target.alpha,
            rotation = target.rotation,
            scaleX = target.scaleX,
            scaleY = target.scaleY
        )
    }

    private fun clearResults() {
        tvTweenResult.text = getString(R.string.native_vs_tween_result_tween_idle)
        tvNativeResult.text = getString(R.string.native_vs_tween_result_native_idle)
    }

    private fun stopRunningAnimations() {
        activeCollector?.cancel()
        activeCollector = null
        tweenTargets.forEach { it.stopAction() }
        runningTweens.forEach { it.stop().remove() }
        runningTweens.clear()
        runningNativeAnimators.forEach { it.cancel() }
        runningNativeAnimators.clear()
    }

    private fun buildComparisonSummary(
        config: ScenarioConfig,
        targetCount: Int,
        currentStats: BenchmarkStats,
        nativeStats: BenchmarkStats
    ): String {
        val winner = if (currentStats.avgElapsedMs <= nativeStats.avgElapsedMs) "WuKong current" else "Android native"
        return getString(
            R.string.native_vs_tween_compare_done,
            getString(config.labelRes),
            targetCount,
            winner,
            currentStats.avgElapsedMs,
            nativeStats.avgElapsedMs
        )
    }

    private fun formatStats(title: String, stats: BenchmarkStats): String {
        return getString(
            R.string.native_vs_tween_stats_template,
            title,
            stats.avgElapsedMs,
            stats.avgFrameDeltaMs,
            stats.maxFrameDeltaMs,
            stats.avgFps,
            stats.avgJankCount
        )
    }

    private fun visibleTweenTargets(): List<View> = tweenTargets.take(selectedTargetCount().coerceAtMost(preparedTargetCount))

    private fun visibleNativeTargets(): List<View> = nativeTargets.take(selectedTargetCount().coerceAtMost(preparedTargetCount))

    private fun selectedTargetCount(): Int {
        return when (rgTargetCount.checkedRadioButtonId) {
            R.id.rb_target_100 -> 100
            R.id.rb_target_1000 -> 1_000
            R.id.rb_target_2000 -> 2_000
            R.id.rb_target_3000 -> 3_000
            else -> 10
        }
    }

    private fun applyVisibleTargetCount() {
        val count = selectedTargetCount().coerceAtMost(preparedTargetCount)
        layoutTargets(flTweenTargets, tweenTargets, count)
        layoutTargets(flNativeTargets, nativeTargets, count)
    }

    private fun computeMoveDeltaX(): Float {
        val width = flTweenTargets.width.coerceAtLeast(dp(120))
        return (width * 0.25f).coerceAtMost(dp(64).toFloat()).coerceAtLeast(dp(24).toFloat())
    }

    private fun computeMoveDeltaY(): Float {
        val height = flTweenTargets.height.coerceAtLeast(dp(120))
        return (height * 0.16f).coerceAtMost(dp(48).toFloat()).coerceAtLeast(dp(16).toFloat())
    }

    private fun dp(value: Int): Int {
        return (resources.displayMetrics.density * value).toInt()
    }

    private fun parsePositiveLong(text: String, defaultValue: Long = DEFAULT_DURATION_MS): Long {
        return text.toLongOrNull()?.takeIf { it > 0 } ?: defaultValue
    }

    private fun parsePositiveInt(text: String, defaultValue: Int = DEFAULT_ITERATIONS): Int {
        return text.toIntOrNull()?.takeIf { it > 0 } ?: defaultValue
    }

    override fun onDestroy() {
        benchmarkCancelled = true
        super.onDestroy()
        stopRunningAnimations()
    }

    private inner class FrameCollector : Choreographer.FrameCallback {
        private val frameDeltas = mutableListOf<Long>()
        private var lastFrameNanos = 0L
        private var frameCallbackCount = 0
        private var running = false

        fun start() {
            activeCollector?.cancel()
            activeCollector = this
            frameDeltas.clear()
            lastFrameNanos = 0L
            frameCallbackCount = 0
            running = true
            Choreographer.getInstance().postFrameCallback(this)
        }

        override fun doFrame(frameTimeNanos: Long) {
            if (!running) return
            frameCallbackCount++
            if (lastFrameNanos != 0L) {
                frameDeltas.add(frameTimeNanos - lastFrameNanos)
            }
            lastFrameNanos = frameTimeNanos
            Choreographer.getInstance().postFrameCallback(this)
        }

        fun stopAndBuild(elapsedMs: Long): RunMetrics {
            running = false
            if (activeCollector === this) {
                activeCollector = null
            }
            val safeElapsedMs = elapsedMs.coerceAtLeast(1L).toDouble()
            val avgFrameMs = if (frameDeltas.isEmpty()) 0.0 else frameDeltas.average() / 1_000_000.0
            val avgFps = frameCallbackCount * 1000.0 / safeElapsedMs
            var maxFrameMs = 0.0
            var jankCount = 0
            frameDeltas.forEach {
                val frameMs = it / 1_000_000.0
                maxFrameMs = max(maxFrameMs, frameMs)
                if (frameMs > 24.0) {
                    jankCount++
                }
            }
            return RunMetrics(
                elapsedMs = safeElapsedMs,
                avgFrameDeltaMs = avgFrameMs,
                maxFrameDeltaMs = maxFrameMs,
                avgFps = avgFps,
                jankCount = jankCount.toDouble()
            )
        }

        fun cancel() {
            running = false
        }
    }

    private data class ScenarioConfig(
        val labelRes: Int,
        val currentMode: CurrentAnimationMode,
        val durationMs: Long,
        val tweenToValues: ((ViewState) -> MutableMap<String, Number>)? = null,
        val nativePhases: List<NativePhase>,
        val waitDurationMs: Long = 0L,
        val deltaX: Float = 0f,
        val deltaY: Float = 0f,
        val alphaTo: Float = 1f,
        val scaleTo: Float = 1f,
        val rotationDelta: Float = 0f
    )

    private enum class CurrentAnimationMode {
        TWEEN,
        TWEEN_COMPLEX_CHAIN
    }

    private data class NativePhase(
        val durationMs: Long,
        val type: NativePhaseType,
        val valueResolver: ((ViewState) -> MutableMap<String, Number>)? = null
    )

    private enum class NativePhaseType {
        TO_VALUES,
        WAIT
    }

    private data class RunMetrics(
        val elapsedMs: Double,
        val avgFrameDeltaMs: Double,
        val maxFrameDeltaMs: Double,
        val avgFps: Double,
        val jankCount: Double
    )

    private data class ViewState(
        val x: Float,
        val y: Float,
        val alpha: Float,
        val rotation: Float,
        val scaleX: Float,
        val scaleY: Float
    )

    private class BenchmarkMetrics {
        private val runs = mutableListOf<RunMetrics>()

        fun add(metrics: RunMetrics) {
            runs.add(metrics)
        }

        fun toStats(): BenchmarkStats {
            return BenchmarkStats(
                avgElapsedMs = runs.map { it.elapsedMs }.average(),
                avgFrameDeltaMs = runs.map { it.avgFrameDeltaMs }.average(),
                maxFrameDeltaMs = runs.maxOfOrNull { it.maxFrameDeltaMs } ?: 0.0,
                avgFps = runs.map { it.avgFps }.average(),
                avgJankCount = runs.map { it.jankCount }.average()
            )
        }
    }

    private data class BenchmarkStats(
        val avgElapsedMs: Double,
        val avgFrameDeltaMs: Double,
        val maxFrameDeltaMs: Double,
        val avgFps: Double,
        val avgJankCount: Double
    )
}
