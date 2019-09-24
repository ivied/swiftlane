package dev.udev.swiftlane.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.udev.swiftlane.ui.activities.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}