package dev.udev.swiftlane.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.udev.swiftlane.ui.viewmodels.PicViewModel
import dev.udev.swiftlane.ui.viewmodels.ViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PicViewModel::class)
    abstract fun bindPicViewModel(picViewModel: PicViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}