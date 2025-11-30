package com.medicinereminder.domain.usecase.data

import android.net.Uri
import com.medicinereminder.manager.DataExportImportManager

/**
 * 导出数据用例
 * 用于将应用数据导出到文件
 * @param dataExportImportManager 数据导出导入管理器
 */
class ExportDataUseCase(private val dataExportImportManager: DataExportImportManager) {
    /**
     * 执行数据导出操作
     * @param uri 文件URI
     * @return 导出是否成功
     */
    suspend operator fun invoke(uri: Uri): Boolean {
        return dataExportImportManager.exportData(uri)
    }
}
