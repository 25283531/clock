package com.medicinereminder.di

import com.medicinereminder.domain.usecase.data.ExportDataUseCase
import com.medicinereminder.domain.usecase.data.ImportDataUseCase
import com.medicinereminder.domain.usecase.medicine.AddMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.DeleteMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.GetAllMedicinesUseCase
import com.medicinereminder.domain.usecase.medicine.GetMedicineByIdUseCase
import com.medicinereminder.domain.usecase.medicine.GetMedicinesByIdsUseCase
import com.medicinereminder.domain.usecase.medicine.UpdateMedicineUseCase
import com.medicinereminder.domain.usecase.reminder.AddReminderUseCase
import com.medicinereminder.domain.usecase.reminder.CalculateNextDoseUseCase
import com.medicinereminder.domain.usecase.reminder.DeleteReminderUseCase
import com.medicinereminder.domain.usecase.reminder.GetActiveRemindersUseCase
import com.medicinereminder.domain.usecase.reminder.GetAllRemindersUseCase
import com.medicinereminder.domain.usecase.reminder.GetReminderByIdUseCase
import com.medicinereminder.domain.usecase.reminder.UpdateReminderUseCase
import com.medicinereminder.domain.usecase.record.AddMedicineRecordUseCase
import com.medicinereminder.domain.usecase.record.DeleteRecordsBeforeDateUseCase
import com.medicinereminder.domain.usecase.record.GetAllRecordsUseCase
import com.medicinereminder.domain.usecase.record.GetTodayRecordsUseCase
import com.medicinereminder.domain.usecase.record.UpdateMedicineRecordUseCase
import android.content.Context
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.manager.DataExportImportManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 用例模块
 * 用于配置用例相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    // 药物相关用例
    @Provides
    @Singleton
    fun provideAddMedicineUseCase(medicineRepository: MedicineRepository): AddMedicineUseCase {
        return AddMedicineUseCase(medicineRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllMedicinesUseCase(medicineRepository: MedicineRepository): GetAllMedicinesUseCase {
        return GetAllMedicinesUseCase(medicineRepository)
    }

    @Provides
    @Singleton
    fun provideGetMedicineByIdUseCase(medicineRepository: MedicineRepository): GetMedicineByIdUseCase {
        return GetMedicineByIdUseCase(medicineRepository)
    }

    @Provides
    @Singleton
    fun provideGetMedicinesByIdsUseCase(medicineRepository: MedicineRepository): GetMedicinesByIdsUseCase {
        return GetMedicinesByIdsUseCase(medicineRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateMedicineUseCase(medicineRepository: MedicineRepository): UpdateMedicineUseCase {
        return UpdateMedicineUseCase(medicineRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMedicineUseCase(medicineRepository: MedicineRepository): DeleteMedicineUseCase {
        return DeleteMedicineUseCase(medicineRepository)
    }

    // 提醒相关用例
    @Provides
    @Singleton
    fun provideAddReminderUseCase(reminderRepository: ReminderRepository): AddReminderUseCase {
        return AddReminderUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllRemindersUseCase(reminderRepository: ReminderRepository): GetAllRemindersUseCase {
        return GetAllRemindersUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideGetActiveRemindersUseCase(reminderRepository: ReminderRepository): GetActiveRemindersUseCase {
        return GetActiveRemindersUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideGetReminderByIdUseCase(reminderRepository: ReminderRepository): GetReminderByIdUseCase {
        return GetReminderByIdUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateReminderUseCase(reminderRepository: ReminderRepository): UpdateReminderUseCase {
        return UpdateReminderUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteReminderUseCase(reminderRepository: ReminderRepository): DeleteReminderUseCase {
        return DeleteReminderUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideCalculateNextDoseUseCase(
        getMedicinesByIdsUseCase: GetMedicinesByIdsUseCase
    ): CalculateNextDoseUseCase {
        return CalculateNextDoseUseCase(getMedicinesByIdsUseCase)
    }

    // 服药记录相关用例
    @Provides
    @Singleton
    fun provideAddMedicineRecordUseCase(medicineRecordRepository: MedicineRecordRepository): AddMedicineRecordUseCase {
        return AddMedicineRecordUseCase(medicineRecordRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllRecordsUseCase(medicineRecordRepository: MedicineRecordRepository): GetAllRecordsUseCase {
        return GetAllRecordsUseCase(medicineRecordRepository)
    }

    @Provides
    @Singleton
    fun provideGetTodayRecordsUseCase(medicineRecordRepository: MedicineRecordRepository): GetTodayRecordsUseCase {
        return GetTodayRecordsUseCase(medicineRecordRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateMedicineRecordUseCase(medicineRecordRepository: MedicineRecordRepository): UpdateMedicineRecordUseCase {
        return UpdateMedicineRecordUseCase(medicineRecordRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteRecordsBeforeDateUseCase(medicineRecordRepository: MedicineRecordRepository): DeleteRecordsBeforeDateUseCase {
        return DeleteRecordsBeforeDateUseCase(medicineRecordRepository)
    }

    // 数据导出导入相关用例
    @Provides
    @Singleton
    fun provideDataExportImportManager(
        @ApplicationContext context: Context,
        medicineRepository: MedicineRepository,
        reminderRepository: ReminderRepository,
        medicineRecordRepository: MedicineRecordRepository
    ): DataExportImportManager {
        return DataExportImportManager(
            context = context,
            medicineRepository = medicineRepository,
            reminderRepository = reminderRepository,
            medicineRecordRepository = medicineRecordRepository
        )
    }

    @Provides
    @Singleton
    fun provideExportDataUseCase(dataExportImportManager: DataExportImportManager): ExportDataUseCase {
        return ExportDataUseCase(dataExportImportManager)
    }

    @Provides
    @Singleton
    fun provideImportDataUseCase(dataExportImportManager: DataExportImportManager): ImportDataUseCase {
        return ImportDataUseCase(dataExportImportManager)
    }
}
