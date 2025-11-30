# 服药提醒助手

一款专为老年人设计的服药提醒安卓应用，帮助用户按时按量服药。

## 主要功能

### 核心功能
- **个性化服药计划**：支持每天多次、间隔天数等多种服药周期
- **智能闹钟提醒**：根据实际服药时间自动计算下次提醒
- **多药物管理**：支持每次服药包含多种药物
- **药物拍照**：为每种药物添加照片，方便识别

### 桌面小组件
- 显示下次服药时间和药物种类
- 直接在桌面勾选完成服药
- 自动更新下次服药信息

### 服药记录
- 记录每次服药时间
- 支持撤销误操作（长按设置为未服药）
- 重新触发提醒

## 技术栈

- **开发语言**：Kotlin
- **最低 Android 版本**：Android 8.0 (API 26)
- **目标 Android 版本**：Android 14 (API 34)
- **架构**：MVVM + Repository 模式
- **数据库**：Room
- **UI 框架**：Jetpack Compose
- **依赖注入**：Hilt
- **异步处理**：Kotlin Coroutines + Flow

## 项目结构

```
MedicineReminder/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/medicinereminder/
│   │   │   │   ├── data/          # 数据层
│   │   │   │   ├── domain/        # 业务逻辑层
│   │   │   │   ├── ui/            # UI 层
│   │   │   │   ├── widget/        # 桌面小组件
│   │   │   │   └── utils/         # 工具类
│   │   │   ├── res/               # 资源文件
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

## 开始开发

详细的开发计划请查看 `开发计划.md`
