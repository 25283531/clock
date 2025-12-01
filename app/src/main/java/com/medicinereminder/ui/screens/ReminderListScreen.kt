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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.medicinereminder.ui.viewmodels.ReminderViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.domain.model.ReminderModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 提醒列表屏幕
 * 用于显示提醒列表，支持添加、编辑和删除提醒
 * @param navController 导航控制器
 */
@Composable
fun ReminderListScreen(navController: NavHostController) {
    val viewModel: ReminderViewModel = hiltViewModel()
    val reminders by viewModel.reminders.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val reminderToDelete by viewModel.reminderToDelete.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 标题
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "提醒列表",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    navController.navigate(Screen.AddEditReminder.route)
                }
            ) {
                Text(text = "添加提醒")
            }
        }

        // 提醒列表
        if (reminders.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "暂无提醒",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.AddEditReminder.route)
                    }
                ) {
                    Text(text = "添加第一个提醒")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reminders) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        onEdit = {
                            navController.navigate("${Screen.AddEditReminder.route}/${reminder.id}")
                        },
                        onDelete = {
                            viewModel.showDeleteDialog(reminder)
                        },
                        onToggleActive = {
                            val updatedReminder = reminder.copy(isActive = !reminder.isActive)
                            viewModel.updateReminder(updatedReminder)
                        }
                    )
                }
            }
        }

        // 删除确认对话框
        if (showDeleteDialog && reminderToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissDeleteDialog()
                },
                title = { Text(text = "确认删除") },
                text = { Text(text = "确定要删除提醒吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteReminder(reminderToDelete!!)
                            viewModel.dismissDeleteDialog()
                        }
                    ) {
                        Text(text = "确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.dismissDeleteDialog()
                        }
                    ) {
                        Text(text = "取消")
                    }
                }
            )
        }
    }
}

/**
 * 提醒卡片
 * @param reminder 提醒模型
 * @param onEdit 编辑按钮点击事件
 * @param onDelete 删除按钮点击事件
 * @param onToggleActive 切换激活状态按钮点击事件
 */
@Composable
fun ReminderCard(
    reminder: ReminderModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // 提醒基本信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    Text(
                        text = dateFormat.format(reminder.startTime),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when (reminder.scheduleType) {
                            ScheduleType.DAILY -> "每天 ${reminder.timesPerDay} 次，间隔 ${reminder.intervalHours} 小时"
                            ScheduleType.INTERVAL_DAYS -> "每 ${reminder.intervalDays} 天一次"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = reminder.isActive,
                    onCheckedChange = { onToggleActive() },
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

            // 药物列表
            Text(
                text = "药物：",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${reminder.medicineIds.size} 种药物",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑提醒",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除提醒",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
