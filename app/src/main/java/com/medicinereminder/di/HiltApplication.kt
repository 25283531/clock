package com.medicinereminder.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt应用程序类
 * 用于初始化Hilt依赖注入框架
 */
@HiltAndroidApp
class HiltApplication : Application()
