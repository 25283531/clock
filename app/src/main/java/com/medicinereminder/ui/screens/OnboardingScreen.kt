package com.medicinereminder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medicinereminder.ui.navigation.Screen
import kotlinx.coroutines.launch

/**
 * 引导页面数据类
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

/**
 * 引导页面
 * 用于向用户介绍应用的主要功能和使用方法
 */
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    // 引导页面数据
    val onboardingPages = listOf(
        OnboardingPage(
            title = "欢迎使用服药提醒助手",
            description = "专业的服药提醒应用，帮助您按时服药，保持健康",
            imageRes = R.mipmap.ic_launcher
        ),
        OnboardingPage(
            title = "添加药物信息",
            description = "轻松添加您需要服用的药物，包括名称、剂量和图片",
            imageRes = R.mipmap.ic_launcher
        ),
        OnboardingPage(
            title = "设置服药提醒",
            description = "根据您的服药习惯，设置个性化的服药提醒",
            imageRes = R.mipmap.ic_launcher
        ),
        OnboardingPage(
            title = "开始使用",
            description = "现在开始使用服药提醒助手，让健康管理变得更简单",
            imageRes = R.mipmap.ic_launcher
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // 引导页面内容
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val onboardingPage = onboardingPages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 图片
                Image(
                    painter = painterResource(id = onboardingPage.imageRes),
                    contentDescription = onboardingPage.title,
                    modifier = Modifier.size(200.dp)
                )

                // 标题
                Text(
                    text = onboardingPage.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                )

                // 描述
                Text(
                    text = onboardingPage.description,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }

        // 指示器和按钮
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 指示器
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(onboardingPages.size) { index ->
                    val isSelected = index == pagerState.currentPage
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .size(if (isSelected) 16.dp else 8.dp)
                            .background(
                                if (isSelected) Color.Blue else Color.Gray,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }

            // 按钮
            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        // 不是最后一页，跳转到下一页
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        // 最后一页，完成引导
                        onComplete()
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(0)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage < onboardingPages.size - 1) "下一页" else "开始使用",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
