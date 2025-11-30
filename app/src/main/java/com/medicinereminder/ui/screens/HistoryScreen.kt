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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medicinereminder.ui.viewmodels.HistoryViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.domain.model.MedicineRecordModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CalendarMonth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 服药记录历史屏幕
 * 用于显示服药记录历史，支持按日期范围查询和记录删除
 * @param navController 导航控制器
 */
@Composable
fun HistoryScreen(navController: NavHostController) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val records by viewModel.records.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val recordToDelete by viewModel.recordToDelete.collectAsState()
    val showBatchDeleteDialog by viewModel.showBatchDeleteDialog.collectAsState()
    val batchDeleteOption by viewModel.batchDeleteOption.collectAsState()
    val context = LocalContext.current

    // 批量删除选项
    val batchDeleteOptions = listOf(
        "最近7天" to HistoryViewModel.BatchDeleteOption.LAST_7_DAYS,
        "最近30天" to HistoryViewModel.BatchDeleteOption.LAST_30_DAYS,
        "最近90天" to HistoryViewModel.BatchDeleteOption.LAST_90_DAYS,
        "最近6个月" to HistoryViewModel.BatchDeleteOption.LAST_6_MONTHS,
        "最近1年" to HistoryViewModel.BatchDeleteOption.LAST_1_YEAR
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部导航栏
        TopAppBar(
            title = {
                Text(
                    text = "服药记录",
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
            actions = {
                // 批量删除按钮
                IconButton(onClick = {
                    // 显示批量删除选项菜单
                    // TODO: 实现批量删除选项菜单
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "批量删除"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // 日期范围选择
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "日期范围",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 开始日期
                    Button(
                        onClick = {
                            // 打开日期选择器
                            // TODO: 实现日期选择器
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "选择开始日期",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "开始日期")
                    }

                    // 结束日期
                    Button(
                        onClick = {
                            // 打开日期选择器
                            // TODO: 实现日期选择器
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "选择结束日期",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "结束日期")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 查询按钮
                    Button(
                        onClick = {
                            // 执行查询
                            // TODO: 实现查询逻辑
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "查询")
                    }

                    // 重置按钮
                    TextButton(
                        onClick = {
                            // 重置日期范围
                            // TODO: 实现重置逻辑
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "重置")
                    }
                }
            }
        }

        // 批量删除选项
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "批量删除",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    batchDeleteOptions.forEach { (label, option) ->
                        TextButton(
                            onClick = {
                                viewModel.showBatchDeleteDialog(option)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = label)
                        }
                    }
                }
            }
        }

        // 记录列表
        if (records.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "暂无服药记录",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(records) {\ record ->
                    MedicineRecordCard(
                        record = record,
                        onDelete = {
                            viewModel.showDeleteDialog(record)
                        }
                    )
                }
            }
        }

        // 单个删除确认对话框
        if (showDeleteDialog && recordToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissDeleteDialog()
                },
                title = { Text(text = "确认删除") },
                text = { Text(text = "确定要删除这条服药记录吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteRecord(recordToDelete!!)
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

        // 批量删除确认对话框
        if (showBatchDeleteDialog) {
            val optionText = batchDeleteOptions.find { it.second == batchDeleteOption }?.first ?: ""
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissBatchDeleteDialog()
                },
                title = { Text(text = "确认批量删除") },
                text = { Text(text = "确定要删除${optionText}的服药记录吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.executeBatchDelete()
                            viewModel.dismissBatchDeleteDialog()
                        }
                    ) {
                        Text(text = "确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.dismissBatchDeleteDialog()
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
 * 服药记录卡片
 * @param record 服药记录模型
 * @param onDelete 删除按钮点击事件
 */
@Composable
fun MedicineRecordCard(
    record: MedicineRecordModel,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // 记录时间
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormat.format(record.takenTime),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (record.isTaken) "已服药" else "未服药",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (record.isTaken) MaterialTheme.colorScheme.success else MaterialTheme.colorScheme.error
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
                text = "${record.medicineIds.size} 种药物",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除记录",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
