package com.medicinereminder.ui.screens

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.ui.viewmodels.SettingsViewModel

/**
 * 设置屏幕
 * 用于显示应用设置，包括数据导出和导入功能
 */
@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val context = LocalContext.current
    val isExporting by viewModel.isExporting.collectAsState()
    val isImporting by viewModel.isImporting.collectAsState()
    val exportResult by viewModel.exportResult.collectAsState()
    val importResult by viewModel.importResult.collectAsState()
    val isVoiceReminderEnabled by viewModel.isVoiceReminderEnabled.collectAsState()

    // 导出结果处理
    exportResult?.let { success ->
        Toast.makeText(
            context,
            if (success) "数据导出成功" else "数据导出失败",
            Toast.LENGTH_SHORT
        ).show()
        viewModel.resetExportResult()
    }

    // 导入结果处理
    importResult?.let { success ->
        Toast.makeText(
            context,
            if (success) "数据导入成功" else "数据导入失败",
            Toast.LENGTH_SHORT
        ).show()
        viewModel.resetImportResult()
    }

    // 导出数据启动器
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            if (uri != null) {
                viewModel.exportData(uri)
            }
        }
    )

    // 导入数据启动器
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.importData(uri, false)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "设置",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 提醒设置卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "提醒设置",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 语音提醒开关
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "语音提醒",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "使用系统内置TTS进行语音提醒",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Switch(
                        checked = isVoiceReminderEnabled,
                        onCheckedChange = {
                            // 检查系统是否有TTS组件
                            val ttsIntent = Intent()
                            ttsIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
                            val resolveInfo = context.packageManager.resolveActivity(ttsIntent, 0)
                            if (it && resolveInfo == null) {
                                Toast.makeText(
                                    context,
                                    "您的手机不支持文本转语音功能",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Switch
                            }
                            viewModel.setVoiceReminderEnabled(it)
                        }
                    )
                }
            }
        }

        // 数据管理卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "数据管理",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 导出数据按钮
                Button(
                    onClick = {
                        exportLauncher.launch("medicine_reminder_data.json")
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    enabled = !isExporting && !isImporting
                ) {
                    Text(text = if (isExporting) "导出中..." else "导出数据")
                }

                // 导入数据按钮
                Button(
                    onClick = {
                        importLauncher.launch(arrayOf("application/json"))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isExporting && !isImporting
                ) {
                    Text(text = if (isImporting) "导入中..." else "导入数据")
                }
            }
        }

        // 关于卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "关于",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "服药提醒助手 v1.0",
                    fontSize = 16.sp
                )
                Text(
                    text = "帮助您按时服药，保持健康",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
