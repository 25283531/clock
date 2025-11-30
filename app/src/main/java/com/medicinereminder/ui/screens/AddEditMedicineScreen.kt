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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.medicinereminder.ui.viewmodels.AddEditMedicineViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.medicinereminder.domain.model.MedicineModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider

/**
 * 添加/编辑药物屏幕
 * 用于添加和编辑药物信息，包括药物名称、描述、剂量和照片
 * @param navController 导航控制器
 * @param medicineId 药物ID，如果为null则表示添加新药物
 */
@Composable
fun AddEditMedicineScreen(
    navController: NavHostController,
    medicineId: Long?
) {
    val viewModel: AddEditMedicineViewModel = hiltViewModel()
    val medicine by viewModel.medicine.collectAsState()
    val showDeletePhotoDialog by viewModel.showDeletePhotoDialog.collectAsState()
    val context = LocalContext.current

    // 表单状态
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // 加载药物信息
    LaunchedEffect(medicineId) {
        if (medicineId != null) {
            viewModel.loadMedicine(medicineId)
        }
    }

    // 更新表单字段
    LaunchedEffect(medicine) {
        if (medicine != null) {
            name = medicine.name
            description = medicine.description ?: ""
            dosage = medicine.dosage
            // 这里应该处理药物照片的加载，暂时用null代替
            photoUri = null
        }
    }

    // 拍照和选择照片的启动器
    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val imageUri = result.data?.data
            photoUri = imageUri
            // 保存照片到本地并更新药物信息
            viewModel.updatePhotoUri(imageUri)
        }
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri
        // 保存照片到本地并更新药物信息
        viewModel.updatePhotoUri(uri)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部导航栏
        TopAppBar(
            title = {
                Text(
                    text = if (medicineId == null) "添加药物" else "编辑药物",
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
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 药物照片
            Card(
                modifier = Modifier.size(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (photoUri != null) {
                        // 显示药物照片
                        Image(
                            painter = rememberAsyncImagePainter(model = photoUri),
                            contentDescription = "药物照片",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // 删除照片按钮
                        IconButton(
                            onClick = {
                                viewModel.showDeletePhotoDialog()
                            },
                            modifier = Modifier.align(Alignment.End).padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "删除照片",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        // 照片占位符
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = "添加照片",
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 照片操作按钮
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        // 拍照
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePhotoLauncher.launch(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "拍照",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "拍照")
                }
                Button(
                    onClick = {
                        // 从相册选择
                        pickPhotoLauncher.launch("image/*")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "从相册选择",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "从相册选择")
                }
            }

            Divider(modifier = Modifier.fillMaxWidth())

            // 药物名称
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "药物名称") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            // 药物描述
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "药物描述") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                maxLines = 3
            )

            // 服用剂量
            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text(text = "服用剂量") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 保存和取消按钮
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
                        // 保存药物信息
                        val medicineModel = MedicineModel(
                            id = medicineId ?: 0,
                            name = name,
                            description = if (description.isBlank()) null else description,
                            dosage = dosage,
                            photoPath = photoUri?.toString()
                        )
                        if (medicineId == null) {
                            viewModel.addMedicine(medicineModel)
                        } else {
                            viewModel.updateMedicine(medicineModel)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = name.isNotBlank() && dosage.isNotBlank()
                ) {
                    Text(text = "保存")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // 删除照片确认对话框
        if (showDeletePhotoDialog) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissDeletePhotoDialog()
                },
                title = { Text(text = "确认删除") },
                text = { Text(text = "确定要删除药物照片吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            photoUri = null
                            viewModel.deletePhoto()
                            viewModel.dismissDeletePhotoDialog()
                        }
                    ) {
                        Text(text = "确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.dismissDeletePhotoDialog()
                        }
                    ) {
                        Text(text = "取消")
                    }
                }
            )
        }
    }
}
