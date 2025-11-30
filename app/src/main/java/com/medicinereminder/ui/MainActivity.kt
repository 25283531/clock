package com.medicinereminder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.medicinereminder.ui.navigation.AppNavigation
import com.medicinereminder.ui.screens.OnboardingScreen
import com.medicinereminder.ui.theme.MedicineReminderTheme
import com.medicinereminder.ui.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 应用主Activity
 * 作为应用的入口点，负责设置Compose内容和导航
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicineReminderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
                    val isOnboardingCompleted by onboardingViewModel.isOnboardingCompleted.collectAsState()

                    // 根据引导页面状态决定显示内容
                    if (isOnboardingCompleted) {
                        // 引导页面已完成，显示主应用内容
                        AppNavigation(navController = navController)
                    } else {
                        // 引导页面未完成，显示引导页面
                        OnboardingScreen(
                            navController = navController,
                            onComplete = {
                                onboardingViewModel.completeOnboarding()
                            }
                        )
                    }
                }
            }
        }
    }
}
