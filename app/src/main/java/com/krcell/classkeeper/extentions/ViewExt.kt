package com.krcell.classkeeper.extentions

import android.view.View


var View.topPadding: Int
    inline get() = paddingTop
    set(value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)