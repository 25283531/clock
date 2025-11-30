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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medicinereminder.ui.navigation.Screen
import com.medicinereminder.ui.viewmodels.AddEditReminderViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.model.ScheduleType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 添加/编辑提醒屏幕
 * 用于添加和编辑提醒，支持选择提醒类型、设置服药次数、间隔时间，以及选择关联的药物
 * @param navController 导航控制器
 * @param reminderId 提醒ID，如果为null则表示添加新提醒
 */
@Composable
fun AddEditReminderScreen(
    navController: NavHostController,
    reminderId: Long?
) {
    val viewModel: AddEditReminderViewModel = hiltViewModel()
    val reminder by viewModel.reminder.collectAsState()
    val medicines by viewModel.medicines.collectAsState()
    val context = LocalContext.current

    // 表单状态
    var scheduleType by remember { mutableStateOf(ScheduleType.DAILY) }
    var timesPerDay by remember { mutableStateOf(1) }
    var intervalHours by remember { mutableStateOf(12) }
    var intervalDays by remember { mutableStateOf(1) }
    var startTime by remember { mutableStateOf(Date()) }
    var selectedMedicineIds by remember { mutableStateOf(emptyList<Long>()) }

    // 加载提醒信息
    LaunchedEffect(reminderId) {
        if (reminderId != null) {
            viewModel.loadReminder(reminderId)
        }
    }

    // 更新表单字段
    LaunchedEffect(reminder) {
        if (reminder != null) {
            scheduleType = reminder.scheduleType
            timesPerDay = reminder.timesPerDay
            intervalHours = reminder.intervalHours
            intervalDays = reminder.intervalDays
            startTime = reminder.startTime
            selectedMedicineIds = reminder.medicineIds
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部导航栏
        TopAppBar(
            title = {
                Text(
                    text = if (reminderId == null) "添加提醒" else "编辑提醒",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // 表单内容
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 提醒类型选择
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "提醒类型",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = scheduleType == ScheduleType.DAILY,
                                    onClick = {
                                        scheduleType = ScheduleType.DAILY
                                    }
                                )
                                Text(text = "每天多次")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = scheduleType == ScheduleType.INTERVAL_DAYS,
                                    onClick = {
                                        scheduleType = ScheduleType.INTERVAL_DAYS
                                    }
                                )
                                Text(text = "间隔天数")
                            }
                        }
                    }
                }
            }

            // 每天多次设置
            if (scheduleType == ScheduleType.DAILY) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                text = "每天服药设置",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "每天次数")
                                    OutlinedTextField(
                                        value = timesPerDay.toString(),
                                        onValueChange = {
                                            val value = it.toIntOrNull() ?: 1
                                            timesPerDay = if (value < 1) 1 else value
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "间隔小时")
                                    OutlinedTextField(
                                        value = intervalHours.toString(),
                                        onValueChange = {
                                            val value = it.toIntOrNull() ?: 1
                                            intervalHours = if (value < 1) 1 else value
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 间隔天数设置
            if (scheduleType == ScheduleType.INTERVAL_DAYS) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                text = "间隔天数设置",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = intervalDays.toString(),
                                onValueChange = {
                                    val value = it.toIntOrNull() ?: 1
                                    intervalDays = if (value < 1) 1 else value
                                },
                                label = { Text(text = "间隔天数") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // 开始时间设置
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "开始时间",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // 这里应该使用时间选择器，暂时简化为文本显示
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        Button(
                            onClick = {
                                // 打开时间选择器
                                // TODO: 实现时间选择器
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = dateFormat.format(startTime))
                        }
                    }
                }
            }

            // 药物选择
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "选择药物",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (medicines.isEmpty()) {
                            Text(
                                text = "暂无药物，请先添加药物",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            medicines.forEach { medicine ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = medicine.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = medicine.dosage,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            selectedMedicineIds = if (selectedMedicineIds.contains(medicine.id)) {
                                                selectedMedicineIds - medicine.id
                                            } else {
                                                selectedMedicineIds + medicine.id
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (selectedMedicineIds.contains(medicine.id)) {
                                                Icons.Default.Check
                                            } else {
                                                Icons.Default.Close
                                            },
                                            contentDescription = if (selectedMedicineIds.contains(medicine.id)) {
                                                "取消选择"
                                            } else {
                                                "选择"
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 保存和取消按钮
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "取消")
                    }
                    Button(
                        onClick = {
                            // 保存提醒信息
                            val reminderModel = ReminderModel(
                                id = reminderId ?: 0,
                                medicineIds = selectedMedicineIds,
                                scheduleType = scheduleType,
                                timesPerDay = timesPerDay,
                                intervalHours = intervalHours,
                                intervalDays = intervalDays,
                                startTime = startTime
                            )
                            if (reminderId == null) {
                                viewModel.addReminder(reminderModel)
                            } else {
                                viewModel.updateReminder(reminderModel)
                            }
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedMedicineIds.isNotEmpty()
                    ) {
                        Text(text = "保存")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
