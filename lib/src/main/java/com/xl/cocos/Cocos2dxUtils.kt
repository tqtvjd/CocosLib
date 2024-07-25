package com.xl.cocos

/**
 * Cocos2dx 代理者
 */
object Cocos2dxUtils {

    private var jsExecutor: Function1<String, Unit>? = null

    @JvmStatic
    fun setJsExecutor(executor: Function1<String, Unit>) {
        jsExecutor = executor
    }

    fun execute(js: String) {
        jsExecutor?.invoke(js)
    }
}