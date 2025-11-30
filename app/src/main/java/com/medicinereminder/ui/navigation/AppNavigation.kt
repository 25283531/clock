package com.medicinereminder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.medicinereminder.ui.screens.DashboardScreen
import com.medicinereminder.ui.screens.MedicineListScreen
import com.medicinereminder.ui.screens.ReminderListScreen
import com.medicinereminder.ui.screens.HistoryScreen
import com.medicinereminder.ui.screens.AddEditMedicineScreen
import com.medicinereminder.ui.screens.AddEditReminderScreen

/**
 * 应用导航组件
 * 用于定义应用的导航路由和导航逻辑
 * @param navController 导航控制器
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // 首页
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        // 药物列表
        composable(route = Screen.MedicineList.route) {
            MedicineListScreen(navController = navController)
        }
        // 添加/编辑药物
        composable(route = "${Screen.AddEditMedicine.route}/{medicineId}?") {\ backStackEntry ->
            val medicineId = backStackEntry.arguments?.getString("medicineId")?.toLongOrNull()
            AddEditMedicineScreen(
                navController = navController,
                medicineId = medicineId
            )
        }
        // 提醒列表
        composable(route = Screen.ReminderList.route) {
            ReminderListScreen(navController = navController)
        }
        // 添加/编辑提醒
        composable(route = "${Screen.AddEditReminder.route}/{reminderId}?") {\ backStackEntry ->
            val reminderId = backStackEntry.arguments?.getString("reminderId")?.toLongOrNull()
            AddEditReminderScreen(
                navController = navController,
                reminderId = reminderId
            )
        }
        // 服药记录
        composable(route = Screen.History.route) {
            HistoryScreen(navController = navController)
        }
    }
}

/**
 * 应用屏幕路由枚举
 */
enum class Screen(val route: String) {
    /**
     * 首页
     */
    Dashboard("dashboard"),
    /**
     * 药物列表
     */
    MedicineList("medicine_list"),
    /**
     * 添加/编辑药物
     */
    AddEditMedicine("add_edit_medicine"),
    /**
     * 提醒列表
     */
    ReminderList("reminder_list"),
    /**
     * 添加/编辑提醒
     */
    AddEditReminder("add_edit_reminder"),
    /**
     * 服药记录
     */
    History("history")
}
