package dev.udev.swiftlane.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.udev.swiftlane.ui.fragments.BasePicFragment
import dev.udev.swiftlane.ui.fragments.PicFragment

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeBasePicFragment(): BasePicFragment

    @ContributesAndroidInjector
    abstract fun contributePicFragment(): PicFragment
}