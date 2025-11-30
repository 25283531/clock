package com.medicinereminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.domain.model.MedicineModel
import com.medicinereminder.domain.usecase.medicine.AddMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.UpdateMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.GetMedicineByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 添加/编辑药物ViewModel
 * 用于处理添加和编辑药物的业务逻辑和数据管理
 */
@HiltViewModel
class AddEditMedicineViewModel @Inject constructor(
    private val addMedicineUseCase: AddMedicineUseCase,
    private val updateMedicineUseCase: UpdateMedicineUseCase,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase
) : ViewModel() {

    // 药物信息
    private val _medicine = MutableStateFlow<MedicineModel?>(null)
    val medicine: StateFlow<MedicineModel?> = _medicine

    // 删除照片对话框状态
    private val _showDeletePhotoDialog = MutableStateFlow(false)
    val showDeletePhotoDialog: StateFlow<Boolean> = _showDeletePhotoDialog

    /**
     * 加载药物信息
     * @param medicineId 药物ID
     */
    fun loadMedicine(medicineId: Long) {
        viewModelScope.launch {
            val loadedMedicine = getMedicineByIdUseCase(medicineId)
            _medicine.value = loadedMedicine
        }
    }

    /**
     * 添加药物
     * @param medicine 药物模型
     */
    fun addMedicine(medicine: MedicineModel) {
        viewModelScope.launch {
            addMedicineUseCase(medicine)
        }
    }

    /**
     * 更新药物
     * @param medicine 药物模型
     */
    fun updateMedicine(medicine: MedicineModel) {
        viewModelScope.launch {
            updateMedicineUseCase(medicine)
        }
    }

    /**
     * 更新药物照片URI
     * @param uri 照片URI
     * @param context 上下文
     * @return 保存后的照片路径
     */
    fun updatePhotoUri(uri: Uri?, context: Context): String? {
        if (uri == null) return null
        
        // 保存照片到本地
        val photoPath = savePhotoToLocal(uri, context)
        
        // 更新药物信息
        _medicine.update {\ currentMedicine ->
            currentMedicine?.copy(photoPath = photoPath)
        }
        
        return photoPath
    }

    /**
     * 删除药物照片
     */
    fun deletePhoto() {
        // 更新药物信息，移除照片路径
        _medicine.update {\ currentMedicine ->
            currentMedicine?.copy(photoPath = null)
        }
    }

    /**
     * 显示删除照片对话框
     */
    fun showDeletePhotoDialog() {
        _showDeletePhotoDialog.value = true
    }

    /**
     * 关闭删除照片对话框
     */
    fun dismissDeletePhotoDialog() {
        _showDeletePhotoDialog.value = false
    }

    /**
     * 将照片保存到本地存储
     * @param uri 照片URI
     * @param context 上下文
     * @return 保存后的照片路径
     */
    private fun savePhotoToLocal(uri: Uri, context: Context): String? {
        return try {
            // 创建存储目录
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (storageDir == null) return null
            
            // 创建照片文件
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val photoFile = File(storageDir, "medicine_$timeStamp.jpg")
            
            // 复制照片到本地文件
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream: OutputStream = FileOutputStream(photoFile)
            inputStream?.use { input ->
                outputStream.use { output ->
                    val buffer = ByteArray(4 * 1024) // 4KB buffer
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            
            // 返回照片路径
            photoFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
