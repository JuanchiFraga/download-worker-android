package com.rembertime.notification.domain.mapper

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rembertime.notification.data.provider.FileNameProvider
import com.rembertime.notification.data.provider.RandomProvider
import com.rembertime.notification.domain.usecase.GetUniqueFileNameUseCase
import com.rembertime.notification.model.NotificationModel
import com.rembertime.notification.util.DrawableProvider
import com.rembertime.notification.util.StringProvider
import com.rembertime.notification.util.createTestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

@ExperimentalCoroutinesApi
internal class NotificationModelMapperTest {

    private lateinit var randomProvider: RandomProvider
    private lateinit var stringProvider: StringProvider
    private lateinit var drawableProvider: DrawableProvider
    private lateinit var fileNameProvider: FileNameProvider
    private lateinit var getUniqueFileNameUseCase: GetUniqueFileNameUseCase
    private lateinit var notificationModelMapper: NotificationModelMapper

    @Before
    fun setUp() = runBlockingTest {
        randomProvider = mock()
        stringProvider = mock()
        drawableProvider = mock()
        fileNameProvider = mock()
        getUniqueFileNameUseCase = mock()
        notificationModelMapper = NotificationModelMapper(
            randomProvider,
            stringProvider,
            drawableProvider,
            createTestDispatcherProvider(),
            fileNameProvider,
            getUniqueFileNameUseCase
        )
        whenever(randomProvider()).thenReturn(mock())
        whenever(fileNameProvider.find(any())).thenReturn("")
        whenever(getUniqueFileNameUseCase(any())).thenReturn("")
        whenever(stringProvider.getString(any())).thenReturn("")
        whenever(drawableProvider.getDrawable(any())).thenReturn(null)
    }

    @Test
    fun givenMapInputDataWithPathThenRetrieveIt() = runBlockingTest {
        val filePath = "http://test.com/book.pdf"
        val model = createNotificationModel(filePath = filePath)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(filePath == workModel.filePath)
    }

    @Test
    fun givenMapInputDataWithoutCustomNameThenRetrieveUniqueNameFromFileName() = runBlockingTest {
        val filePath = "http://test.com/book.pdf"
        val fileName = "book.pdf"
        val model = createNotificationModel(filePath = filePath, customFileName = null)
        whenever(fileNameProvider.find(filePath)).thenReturn(fileName)
        whenever(getUniqueFileNameUseCase(fileName)).thenReturn(fileName)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(fileName == workModel.fileName)
    }

    @Test
    fun givenMapInputDataWithCustomNameThenRetrieveUniqueNameFromCustomName() = runBlockingTest {
        val customName = "book.pdf"
        val model = createNotificationModel(customFileName = customName)
        whenever(getUniqueFileNameUseCase(customName)).thenReturn(customName)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(customName == workModel.fileName)
    }

    @Test
    fun givenMapInputDataWithChannelIdThenRetrieveIt() = runBlockingTest {
        val channelId = "channelId"
        val model = createNotificationModel(channelId = channelId)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(channelId == workModel.channelId)
    }

    @Test
    fun givenMapInputDataWithChannelNameThenRetrieveIt() = runBlockingTest {
        val channelName = "channelName"
        val model = createNotificationModel(channelName = channelName)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(channelName == workModel.channelName)
    }

    @Test
    fun givenMapInputDataThenRetrieveRandomNotificationId() = runBlockingTest {
        val notificationId = 0
        val model = createNotificationModel()
        val random: Random.Default = mock()
        whenever(randomProvider()).thenReturn(random)
        whenever(random.nextInt()).thenReturn(notificationId)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(notificationId == workModel.notificationId)
    }

    @Test
    fun givenMapInputDataWithNotificationTitleThenRetrieveIt() = runBlockingTest {
        val notificationTitle = "custom title"
        val model = createNotificationModel(customNotificationTitle = notificationTitle)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(notificationTitle == workModel.notificationTitle)
    }

    @Test
    fun givenMapInputDataWithoutNotificationTitleThenRetrieveItAsFileName() = runBlockingTest {
        val fileName = "fileName"
        whenever(getUniqueFileNameUseCase(any())).thenReturn(fileName)
        val model = createNotificationModel(customFileName = fileName, customNotificationTitle = null)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(fileName == workModel.notificationTitle)
    }

    @Test
    fun givenMapInputDataWithAppIconThenRetrieveIt() = runBlockingTest {
        val applicationIcon = android.R.drawable.sym_def_app_icon
        val model = createNotificationModel(applicationIcon = applicationIcon)

        val workModel = notificationModelMapper.mapDataToModel(model.getData())

        assert(applicationIcon == workModel.applicationIcon)
    }

    private fun createNotificationModel(
        filePath: String = "http://www.domain.com/book.pdf",
        applicationIcon: Int = android.R.drawable.sym_def_app_icon,
        customFileName: String? = null,
        customNotificationTitle: String? = null,
        notificationIcon: Int = android.R.drawable.sym_def_app_icon,
        channelName: String = "Downloads",
        channelId: String = "harryChannel"
    ) = NotificationModel(
        filePath,
        applicationIcon,
        customFileName,
        customNotificationTitle,
        notificationIcon,
        channelName,
        channelId
    )
}