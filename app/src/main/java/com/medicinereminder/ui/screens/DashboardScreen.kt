package com.medicinereminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medicinereminder.ui.navigation.Screen
import com.medicinereminder.ui.viewmodels.DashboardViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.domain.model.NextDoseModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 首页屏幕
 * 用于显示下次服药时间、药物信息和统计数据
 * @param navController 导航控制器
 */
@Composable
fun DashboardScreen(navController: NavHostController) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val nextDose by viewModel.nextDose.collectAsState()
    val statistics by viewModel.statistics.collectAsState()
    val todayRecords by viewModel.todayRecords.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 下次服药卡片
        item {
            NextDoseCard(
                nextDose = nextDose,
                onTakeMedicine = { reminderId ->
                    viewModel.markMedicineTaken(reminderId)
                }
            )
        }

        // 统计卡片
        item {
            StatisticsCard(
                totalMedicines = statistics.totalMedicines,
                totalReminders = statistics.totalReminders,
                takenToday = statistics.takenToday,
                missedToday = statistics.missedToday
            )
        }

        // 操作按钮
        item {
            ActionButtons(
                onAddMedicine = {
                    navController.navigate(Screen.AddEditMedicine.route)
                },
                onAddReminder = {
                    navController.navigate(Screen.AddEditReminder.route)
                },
                onViewMedicines = {
                    navController.navigate(Screen.MedicineList.route)
                },
                onViewReminders = {
                    navController.navigate(Screen.ReminderList.route)
                },
                onViewHistory = {
                    navController.navigate(Screen.History.route)
                },
                onViewSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // 今日记录
        item {
            Text(
                text = "今日记录",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            TodayRecordsList(
                records = todayRecords,
                onUndo = { record ->
                    viewModel.undoMedicineTaken(record)
                }
            )
        }
    }
}

/**
 * 下次服药卡片
 * @param nextDose 下次服药信息
 * @param onTakeMedicine 服药按钮点击事件
 */
@Composable
fun NextDoseCard(
    nextDose: NextDoseModel?,
    onTakeMedicine: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "下次服药",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (nextDose != null) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                Text(
                    text = dateFormat.format(nextDose.nextDoseTime),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "药物：",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                nextDose.medicines.forEach { medicine ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = medicine.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = medicine.dosage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onTakeMedicine(nextDose.reminderId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "我已服药")
                }
            } else {
                Text(
                    text = "暂无服药提醒",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 统计卡片
 * @param totalMedicines 药物总数
 * @param totalReminders 提醒总数
 * @param takenToday 今日已服
 * @param missedToday 今日漏服
 */
@Composable
fun StatisticsCard(
    totalMedicines: Int,
    totalReminders: Int,
    takenToday: Int,
    missedToday: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "统计信息",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatisticItem(
                    title = "药物总数",
                    value = totalMedicines.toString()
                )
                StatisticItem(
                    title = "提醒总数",
                    value = totalReminders.toString()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatisticItem(
                    title = "今日已服",
                    value = takenToday.toString(),
                    color = MaterialTheme.colorScheme.success
                )
                StatisticItem(
                    title = "今日漏服",
                    value = missedToday.toString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * 统计项
 * @param title 标题
 * @param value 值
 * @param color 颜色
 */
@Composable
fun StatisticItem(
    title: String,
    value: String,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 操作按钮
 * @param onAddMedicine 添加药物按钮点击事件
 * @param onAddReminder 添加提醒按钮点击事件
 * @param onViewMedicines 查看药物列表按钮点击事件
 * @param onViewReminders 查看提醒列表按钮点击事件
 * @param onViewHistory 查看历史记录按钮点击事件
 * @param onViewSettings 查看设置按钮点击事件
 */
@Composable
fun ActionButtons(
    onAddMedicine: () -> Unit,
    onAddReminder: () -> Unit,
    onViewMedicines: () -> Unit,
    onViewReminders: () -> Unit,
    onViewHistory: () -> Unit,
    onViewSettings: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "快速操作",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAddMedicine,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "添加药物")
                }
                Button(
                    onClick = onAddReminder,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "添加提醒")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewMedicines,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "药物列表")
                }
                Button(
                    onClick = onViewReminders,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "提醒列表")
                }
                Button(
                    onClick = onViewHistory,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "历史记录")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewSettings,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "设置")
                }
            }
        }
    }
}

/**
 * 今日记录列表
 * @param records 今日服药记录
 */
@Composable
fun TodayRecordsList(
    records: List<MedicineRecordModel>,
    onUndo: (MedicineRecordModel) -> Unit
) {
    if (records.isEmpty()) {
        Text(
            text = "暂无今日记录",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    } else {
        records.forEach { record ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        Text(
                            text = dateFormat.format(record.takenTime),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (record.isTaken) "已服药" else "未服药",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (record.isTaken) MaterialTheme.colorScheme.success else MaterialTheme.colorScheme.error
                        )
                    }
                    // 撤销按钮，只在已服药记录上显示
                    if (record.isTaken) {
                        TextButton(
                            onClick = {
                                onUndo(record)
                            }
                        ) {
                            Text(
                                text = "撤销",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
