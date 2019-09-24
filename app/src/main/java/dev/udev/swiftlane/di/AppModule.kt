package dev.udev.swiftlane.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dev.udev.swiftlane.R
import dev.udev.swiftlane.db.PicData
import dev.udev.swiftlane.db.SwiftlaneDB
import dev.udev.swiftlane.livedata.LiveDataAdapterFactory
import dev.udev.swiftlane.network.services.SwiftlanePicService
import dev.udev.swiftlane.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideSwiflanePicService(): SwiftlanePicService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataAdapterFactory())
            .build()
            .create(SwiftlanePicService::class.java)
    }

    @Singleton
    @Provides
    fun provideRequestGlideOptions(): RequestOptions {
        return RequestOptions.placeholderOf(R.drawable.white_background)
            .error(R.drawable.white_background)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions) : RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): SwiftlaneDB {
        return Room.databaseBuilder(app, SwiftlaneDB::class.java, "main.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePhotoDao(db: SwiftlaneDB): PicData {
        return db.picData()
    }
}