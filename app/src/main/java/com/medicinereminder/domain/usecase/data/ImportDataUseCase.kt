package com.medicinereminder.domain.usecase.data

import android.net.Uri
import com.medicinereminder.manager.DataExportImportManager

/**
 * 导入数据用例
 * 用于从文件导入应用数据
 * @param dataExportImportManager 数据导出导入管理器
 */
class ImportDataUseCase(private val dataExportImportManager: DataExportImportManager) {
    /**
     * 执行数据导入操作
     * @param uri 文件URI
     * @param overwrite 是否覆盖现有数据
     * @return 导入是否成功
     */
    suspend operator fun invoke(uri: Uri, overwrite: Boolean = false): Boolean {
        return dataExportImportManager.importData(uri, overwrite)
    }
}
