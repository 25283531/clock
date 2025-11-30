package com.medicinereminder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.medicinereminder.widget.MedicineWidget

/**
 * 服药记录广播接收器
 * 用于处理桌面小组件的服药按钮点击事件，记录服药信息并更新下次服药时间
 */
class MedicineTakenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == MedicineWidget.ACTION_MEDICINE_TAKEN) {
            // 获取提醒ID
            val reminderId = intent.getLongExtra(MedicineWidget.EXTRA_REMINDER_ID, -1L)
            
            if (reminderId != -1L) {
                // 记录服药信息
                recordMedicineTaken(context, reminderId)
                
                // 发送广播更新小组件
                val updateIntent = Intent(MedicineWidget.ACTION_MEDICINE_TAKEN)
                context.sendBroadcast(updateIntent)
            }
        }
    }

    /**
     * 记录服药信息
     * @param context 上下文
     * @param reminderId 提醒ID
     */
    private fun recordMedicineTaken(context: Context, reminderId: Long) {
        // 这里应该调用应用的业务逻辑来记录服药信息
        // 由于这是一个广播接收器，我们需要通过服务或其他方式来处理数据库操作
        // 暂时简化处理，只发送广播
        
        // TODO: 实现实际的服药记录逻辑
        // 1. 获取提醒信息
        // 2. 创建服药记录
        // 3. 计算下次服药时间
        // 4. 更新提醒
        // 5. 更新小组件
    }
}
