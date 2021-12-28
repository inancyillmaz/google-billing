package com.inanc.easypurchase.inapppurchase.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
object ModuleProvider {

    /*
    @Provides
    fun provideModuleStarter(@ActivityContext context: Context)= ModuleStarter(context)
     */

}