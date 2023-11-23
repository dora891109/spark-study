package com.atguigu.sparkcore.core.framework.controller

import com.atguigu.sparkcore.core.framework.common.TController
import com.atguigu.sparkcore.core.framework.service.WordCountService


/**
  * 控制层
  */
class WordCountController extends TController {

    private val wordCountService = new WordCountService()

    // 调度
    def dispatch(): Unit = {
        // TODO 执行业务操作
        val array = wordCountService.dataAnalysis()
        array.foreach(println)
    }
}
