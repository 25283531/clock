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
import com.medicinereminder.ui.viewmodels.MedicineViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.domain.model.MedicineModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo

/**
 * 药物列表屏幕
 * 用于显示药物列表，支持添加、编辑和删除药物
 * @param navController 导航控制器
 */
@Composable
fun MedicineListScreen(navController: NavHostController) {
    val viewModel: MedicineViewModel = hiltViewModel()
    val medicines by viewModel.medicines.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val medicineToDelete by viewModel.medicineToDelete.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 标题
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "药物列表",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    navController.navigate(Screen.AddEditMedicine.route)
                }
            ) {
                Text(text = "添加药物")
            }
        }

        // 药物列表
        if (medicines.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "暂无药物",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.AddEditMedicine.route)
                    }
                ) {
                    Text(text = "添加第一个药物")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(medicines) { medicine ->
                    MedicineCard(
                        medicine = medicine,
                        onEdit = {
                            navController.navigate("${Screen.AddEditMedicine.route}/${medicine.id}")
                        },
                        onDelete = {
                            viewModel.showDeleteDialog(medicine)
                        }
                    )
                }
            }
        }

        // 删除确认对话框
        if (showDeleteDialog && medicineToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissDeleteDialog()
                },
                title = { Text(text = "确认删除") },
                text = { Text(text = "确定要删除药物 ${medicineToDelete?.name} 吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteMedicine(medicineToDelete!!)
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
 * 药物卡片
 * @param medicine 药物模型
 * @param onEdit 编辑按钮点击事件
 * @param onDelete 删除按钮点击事件
 */
@Composable
fun MedicineCard(
    medicine: MedicineModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 药物照片占位符
            Column(
                modifier = Modifier.size(80.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (medicine.photoPath != null) {
                    // 这里应该显示药物照片，暂时用图标代替
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "药物照片",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "添加照片",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 药物信息
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (medicine.description != null) {
                    Text(
                        text = medicine.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = medicine.dosage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 操作按钮
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑药物",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除药物",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
