package com.atguigu.sparkcore.core.framework.common

import com.atguigu.sparkcore.core.framework.util.EnvUtil

trait TDao {

    def readFile(path:String) = {
        EnvUtil.take().textFile(path)
    }
}
